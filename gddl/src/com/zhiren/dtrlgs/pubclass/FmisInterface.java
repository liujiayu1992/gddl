/*
 * 2009-08-29 李增强 
 * 
 * 一、销售暂估传送接口说明：
 * 	1、场地交割：(1)发运未到货：每天传送Fmis接口。
 * 							装船发货后进行暂估，直到客户确认收货并完成化验；
 * 				(2) 确认到货 ：只传送一次。
 * 							必要条件：客户确认收货(fahbtmp中有该客户对应的收货信息，并完成与发运数据的匹配),标志：fahbtmp.fayslb_id>0 ；
 * 								   客户完成对收货数据的化验，并将化验数据传送到公司zhilb中，标志：fahbtmp.zhilb_id>0 ;
 * 				(3) 结	 算 ：只传送一次。最好与结算接口数据同时传送。(尚未改成同步传送)
 * 							公司与客户完成结算，diancjsmkb表中的ruzrq字段不为空；
 * 2、直		达：(1) 确认到货 ：只传送一次。
 * 							必要条件：客户确认收货(fahbtmp中有该客户对应的收货信息，并完成导入数据操作),
 * 										标志：fahbtmp.fayslb_id>0 或者 fahbtmp.fahb_id>0；
 * 								  	  客户完成对收货数据的化验，并将化验数据传送到公司zhilb中，标志：fahbtmp.zhilb_id>0 ;
 * 
 * 				(2) 结	 算	：只传送一次。最好与结算接口数据同时传送。(尚未改成同步传送)
 * 							公司与客户完成结算，diancjsmkb表中的ruzrq字段不为空；
 */

package com.zhiren.dtrlgs.pubclass;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;

public class FmisInterface {
	public static long CAIG_DATA=0;
	public static long XIAOS_DATA=1;
	public static long ALL_DATA=2;

	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}

	public String JsFmis(Date datInterfaceDate,long intDataRange){
		
		JDBCcon con=new JDBCcon();
		ResultSet rs;
		con.setAutoCommit(false);
		
		StringBuffer sb = new StringBuffer();
		sb.append("begin \n");
		int rownum = 0;

		String sql1 = "";
		String sql2 = "";
		int Exist = 0;
		int result1 = -1;
		
//		int result2 = -1;
//		int result3 = -1;
		
		try{
			if (intDataRange==XIAOS_DATA || intDataRange==ALL_DATA){
				//处理厂方数据
				String strIds = "";
				String cfsql = "select js.id,yf.id as yid,dc.mingc,js.bianm as jiesbh,js.fapbh,to_char(js.ruzrq, 'yyyy-mm-dd') as ruzrq,js.hansmk as fapje, \n"
					+"	     　js.shuik as shuije,(js.hansmk - js.shuik) as buhsje,js.guohl as shul,dc.quanc as shouhdw,substr(gy.bianm,0,6) as diqbm, \n"
					+"		   decode(js.guohl,0,0,round((js.hansmk - js.shuik) / js.guohl, 7)) as buhsdj,dc.quanc as kehmc,js.jiessl, \n"
					+"		   (kf.hansmk - kf.shuik) + (nvl(yf.hansyf, 0) - nvl(yf.shuik, 0)) as chengbje, \n"
					+"		   nvl((select hetbz from jieszbsjb where jiesdid=js.id and zhibb_id=1),0) as hetsl, \n"
					+"		   nvl((select hetbz from jieszbsjb where jiesdid=js.id and zhibb_id=2),0) as hetrl, \n"
					+"		   nvl((select hetbz from jieszbsjb where jiesdid=js.id and zhibb_id=3),0) as hetlf, \n"
					+"		   nvl((select jies from jieszbsjb where jiesdid=js.id and zhibb_id=2),0) as jiesrz, \n"
					+"		   nvl((select jies from jieszbsjb where jiesdid=js.id and zhibb_id=3),0) as jieslf,ht.hetbh \n"
					+"	　from diancjsmkb js,kuangfjsmkb kf,kuangfjsyfb yf,diancxxb dc,gongysb gy,hetb ht--,diancxxb gs \n"
					+"  where js.diancxxb_id = dc.id and js.kuangfjsmkb_id = kf.id(+) and js.id = yf.diancjsmkb_id(+) and js.hetb_id=ht.id(+) \n --and js.shoukdw=gs.quanc \n"
					+"	　and js.gongysb_id=gy.id and js.ruzrq>="+OraDate(datInterfaceDate)+" and js.ruzrq<"+OraDate(datInterfaceDate)+"+1";

				rs = con.getResultSet(cfsql);

				for (int i=0;rs.next();i++) {

					long mid = rs.getLong("id");
					String mfapbh =  rs.getString("fapbh");
					String mruzrq = rs.getString("ruzrq");
					String mkehmc = rs.getString("kehmc");
					String mfapje = new DecimalFormat("0.00").format(rs.getDouble("fapje"));
					String mshuije = new DecimalFormat("0.00").format(rs.getDouble("shuije"));
					String mbuhsje = new DecimalFormat("0.00").format(rs.getDouble("buhsje"));
					String mchengbje = new DecimalFormat("0.00").format(rs.getDouble("chengbje"));

					double mbuhsdj = rs.getDouble("buhsdj");
					double mshul = rs.getDouble("shul");
					String mshouhdw = rs.getString("shouhdw");
					String mdiqbm = "F"+rs.getString("diqbm");
					double mhetsl = rs.getDouble("hetsl");
					String mhetrl = rs.getString("hetrl");
					if(mhetrl.indexOf("-")!=-1){
						mhetrl = mhetrl.substring(mhetrl.indexOf("-")+1);
					}
					double mhetlf = rs.getDouble("hetlf");
					String mjiesdbh = rs.getString("jiesbh");
					double mjiessl = rs.getDouble("jiessl");
					String mhetbh = rs.getString("hetbh");
					String mjiesrz = rs.getString("jiesrz");
					if(mjiesrz.indexOf("-")!=-1){
						mjiesrz = mjiesrz.substring(mjiesrz.indexOf("-")+1);
					}

					double mjieslf = rs.getDouble("jieslf");
					String checksql = "select P_INDEX from ZR_AR_INTERFACE where P_INDEX="+(mid*10);
					ResultSet ckrs = con.getResultSet(checksql);
					if(!ckrs.next()){
						
						sql1 = "insert into ZR_AR_INTERFACE (P_INDEX,INVOICE_NUM,INVOICE_DATE,GL_DATE,CUSTOMER_CODE,INVOICE_AMOUNT,INVOICE_CURRENCY_CODE,TAX_AMOUNT,SALE_AMOUNT,PRICE,TRAN_QUANTITY,COST_AMOUNT,CUSTOMER_NAME,INVENTORY_ITEM_CODE,CONTRACT_QUANTITY,CONTRACT_HEAT,CONTRACT_S2,TRANSACTION_DATE,ATTRIBUTE1,ATTRIBUTE2,ATTRIBUTE3,ATTRIBUTE4,ATTRIBUTE5,CONTRACT_NUM,ORG_ID) "
							+ " values ("+mid*10+",'"
							+mfapbh+"',to_date('"+mruzrq+"','yyyy-mm-dd'),to_date('"+mruzrq+"','yyyy-mm-dd'),'"//发票日期
							+mkehmc+"',"+mfapje+",'CNY',"+mshuije+","+mbuhsje+","+mbuhsdj+","
							+mshul+","+mchengbje+",'"+mshouhdw+"','"+mdiqbm+"',"+mhetsl+","+mhetrl+","
							+mhetlf+",to_date('"+mruzrq+"','yyyy-mm-dd'),'"
							+mjiesdbh+"',"+mjiessl+","+mjiesrz+","+mjieslf+","+mbuhsdj+",'"+mhetbh+"',81);";

						sb.append( sql1 +" \n");
						if(strIds.equals("")){
							strIds = ""+mid;
						}else{
							strIds=strIds+","+mid;
						}
						rownum++;
						
					}else{
						Exist = i+1;
					}
				}
				sb.append("end; \n");
				
				if(rownum>0){
					result1 = con.getInsert(sb.toString());
					if(result1<0 ){
						con.rollBack();
						return "上传失败!";
					}else{
						
						String strINV_DAILY = "insert into zr_inv_daily_interface\n"
							+ "  (d_index, inventory_item_code, organization_name, subinventory_code, freight_lot, transaction_quantity, transaction_uom, transaction_cost, transaction_date, transaction_price, internal_sale, transaction_reference, process_flag,Customer_Code)\n"
							+ "\n"
							+ "select qm.id*10 as id,'F'||substr(gy.bianm,0,6) as gongysbm,decode(1,1,'北京大唐燃料有限公司','北京大唐燃料有限公司') as kuczzz,decode(fy.yewlxb_id,2,lx.mingc||decode(mc.duowgsb_id,5,'_X','_Q'),1,'场地交割'||decode(mc.duowgsb_id,5,'_X','_Q'),lx.mingc) as kuczzmc,\n"
							+ "       decode(lx.id,3,mc.mingc,substr(dwgs.mingc,0,1)||'-'||mc.mingc) as huow,qm.chuksl,'TON' jiesdw,gs.meij as zangjg,to_date(to_char(js.ruzrq, 'yyyy-mm-dd'), 'yyyy-mm-dd') as jiesrq,\n"
							+ "       js.buhsdj as jiesjg,decode(fy.neibxs,0,'Y','N') as neibxs,js.bianm as jiesdh,'0' as zhuangt,gy.quanc as Customer_Code \n"
							+ "  from diancjsmkb js, diancxxb dc, gongysb gy,fayslb fy,qumxxb qm,yewlxb lx,meicb mc,duowgsb dwgs,faygslsb gs --, fahbtmp ft,fahb fh \n"
							+ " where js.diancxxb_id = dc.id and fy.xiaosjsb_id=js.id and qm.zhuangcb_id=fy.id and fy.yewlxb_id=lx.id -- and ft.fahb_id=fh.id(+) and ft.fayslb_id=fy.id \n"
							+ "   and qm.meicb_id=mc.id and dwgs.id(+)=mc.duowgsb_id  and fy.gongysb_id = gy.id and gs.fayslb_id=fy.id\n"
							+ "   and gs.id=(select nvl(max(id),0) from faygslsb g where g.fayslb_id=fy.id )\n"
							+ "   and js.ruzrq >= "+OraDate(datInterfaceDate)+" and js.ruzrq<"+OraDate(datInterfaceDate)+"+1 and js.id in ("+strIds+")";

						int INV_DAILY = con.getUpdate(strINV_DAILY);
						if(INV_DAILY<0){
							con.rollBack();
							return "上传失败!";
						}
						
					}
				}else if(Exist>0){
					return "当天数据已传送到FMIS接口，不可以重复传数!";
					
				}else{
					return "没有结算数据可上传！";
				}
				
			}

			if (intDataRange==CAIG_DATA || intDataRange==ALL_DATA){
//				处理矿方结算数据
				String strIds = "";
				String kfsql = "select js.id,dc.mingc,js.bianm as jiesdh,to_char(js.ruzrq, 'yyyy-mm-dd') as ruzrq,js.hansmk as jiasje,(js.hansmk - js.shuik) as meik, "
					+ "       js.shuik as shuik, nvl((select gongf from jieszbsjb where jiesdid=js.id and zhibb_id=1),0) as gongfsl, "
					+ "       js.shoukdw, nvl((select gongf from jieszbsjb where jiesdid=js.id and zhibb_id=2),0) as gongfrl, "
					+ "       nvl((select gongf from jieszbsjb where jiesdid=js.id and zhibb_id=3),0) as gongflf,js.yuanshr as yuanshdw, "
					+ "       js.xianshr as xianshdw, to_char(js.fahjzrq, 'yyyy-mm-dd') as daohrq,js.fapbh as beiz, gy.fmisbm as gongysbm "
					+ "  from kuangfjsmkb js,diancxxb dc,gongysb gy "
					+ " where js.diancxxb_id=dc.id and js.shoukdw=gy.quanc and js.ruzrq>="+OraDate(datInterfaceDate)+" and js.ruzrq<"+OraDate(datInterfaceDate)+"+1 ";

				rs = con.getResultSet(kfsql);
				for (int i=0;rs.next();i++) {
					long mid = rs.getLong("id");
					String mjiesdbh = rs.getString("jiesdh");
					String mruzrq = rs.getString("ruzrq");

					String mjiasje = new DecimalFormat("0.00").format(rs.getDouble("jiasje"));
					String mmeik = new DecimalFormat("0.00").format(rs.getDouble("meik"));
					String mshuik = new DecimalFormat("0.00").format(rs.getDouble("shuik"));

					double mgongfsl = rs.getDouble("gongfsl");
					String mshoukdw = rs.getString("shoukdw");
					String mgongfrl = rs.getString("gongfrl");
					if(mgongfrl.indexOf("-")!=-1){
						mgongfrl = mgongfrl.substring(mgongfrl.indexOf("-")+1);
					}
					double mgongflf = rs.getDouble("gongflf");
					String myuanshdw =  rs.getString("yuanshdw");
					String mxianshdw =  rs.getString("xianshdw");
					String mdaohrq = rs.getString("daohrq");
//					String mbeiz = rs.getString("beiz");
					String mgongysbm = rs.getString("gongysbm");

					String checksql  = "select P_INDEX from ZR_AP_INTERFACE where P_INDEX = "+(mid*10+1);
					ResultSet ckrs = con.getResultSet(checksql);
					if(!ckrs.next()){

						sql1 = "insert into ZR_AP_INTERFACE (P_INDEX,INVOICE_NUM,INVOICE_DATE,BOOK_DATE,VENDOR_CODE,VENDOR_SITE_CODE,INVOICE_AMOUNT,INVOICE_CURRENCY_CODE,LINE_TYPE_CODE,LINE_AMOUNT,ORGANIZATION_NAME,PLAN_QUANTITY,SUPPLIER,CONTRACT_QUANTITY,CONTRACT_HEAT,CONTRACT_S2,ORIGINAL_RECEIVER,CURRENT_RECEIVER,TRANSACTION_DATE,ARRIVE_DATE) "
							+ " values ("+(mid*10+1)+",'"
							+mjiesdbh+"',to_date('"+mruzrq+"','yyyy-mm-dd'),to_date('"+mruzrq+"','yyyy-mm-dd'),'"
							+mgongysbm+"','燃料结算',"+mjiasje+",'CNY','燃煤',"+mmeik+",'北京大唐燃料有限公司',"
							+mgongfsl+",'"+mshoukdw+"',"+mgongfsl+","+mgongfrl+","+mgongflf+",'"
							+myuanshdw+"','"+mxianshdw+"',to_date('"+mruzrq+"','yyyy-mm-dd'),to_date('"+mdaohrq+"','yyyy-mm-dd'));";

						sql2 = "insert into ZR_AP_INTERFACE (P_INDEX,INVOICE_NUM,INVOICE_DATE,BOOK_DATE,VENDOR_CODE,VENDOR_SITE_CODE,INVOICE_AMOUNT,INVOICE_CURRENCY_CODE,LINE_TYPE_CODE,LINE_AMOUNT,ORGANIZATION_NAME,PLAN_QUANTITY,SUPPLIER,CONTRACT_QUANTITY,CONTRACT_HEAT,CONTRACT_S2,ORIGINAL_RECEIVER,CURRENT_RECEIVER,TRANSACTION_DATE,ARRIVE_DATE) "
							+ " values ("+(mid*10+2)+",'"
							+mjiesdbh+"',to_date('"+mruzrq+"','yyyy-mm-dd'),to_date('"+mruzrq+"','yyyy-mm-dd'),'"
							+mgongysbm+"','燃料结算',"+mjiasje+",'CNY','燃煤税',"+mshuik+",'北京大唐燃料有限公司',"
							+mgongfsl+",'"+mshoukdw+"',"+mgongfsl+","+mgongfrl+","+mgongflf+",'"+myuanshdw+"','"
							+mxianshdw+"',to_date('"+mruzrq+"','yyyy-mm-dd'),to_date('"+mdaohrq+"','yyyy-mm-dd')); ";

						sb.append( sql1 +" \n");
						sb.append( sql2 +" \n");
						if(strIds.equals("")){
							strIds = ""+mid;
						}else{
							strIds=strIds+","+mid;
						}
						rownum++;
						
					}else{
						Exist = i+1;
					}
				}
				
				sb.append("end; \n");
				
				if(rownum>0){
					result1 = con.getInsert(sb.toString());
					if(result1<0 ){
						con.rollBack();
						return "上传失败!";
					}else{
				
						String strMTL_DAILY = "insert into zr_mtl_daily_interface\n"
							+ "  (d_index, inventory_item_code,organization_name, subinventory_code, freight_lot, transaction_quantity,\n"
							+ "  transaction_uom, transaction_cost,\n"
							+ "  transaction_date, transaction_reference, process_flag,\n"
							+ "  transaction_gu_cost, supply_unit, internal_purchase, sale_clear, transaction_gu_cost1,\n"
							+ "  transaction_gu_cost2, transaction_freight, transaction_ys, railway_freight_js)\n"
							+ "\n"
							+ "select (dm.id*10+1) as id,'F'||substr(gy.bianm,0,6) as gongysbm,decode(1,1,'北京大唐燃料有限公司','北京大唐燃料有限公司') as kuczzz,\n"
							+ "       decode(fh.yewlxb_id,2,lx.mingc||decode(mc.duowgsb_id,5,'_X','_Q'),1,'场地交割'||decode(mc.duowgsb_id,5,'_X','_Q'),lx.mingc) as kuczzmc,\n"
							+ "       decode(lx.id,3,mc.mingc,substr(dwgs.mingc,0,1)||'-'||mc.mingc) as huow,\n"
							+ "       dm.ruksl,'TON' jiesdw,js.buhsdj as jiesjg,to_date(to_char(js.ruzrq, 'yyyy-mm-dd'), 'yyyy-mm-dd') as jiesrq,\n"
							+ "       js.bianm as jiesdh,'0' as zhuangt,nvl(gs.meij,0)+nvl(gs.yunf,0) as zangjg,gy.quanc as gonghdw,\n"
							+ "       decode(fh.neibcg,0,'Y','N') as neibcg,\n"
							+ "       case when js.diancjsmkb_id>0 then 'Y' else 'N' end as xiaosjszt,0,0,gs.yunf,fh.yuns,0\n"
							+ "  from kuangfjsmkb js, diancxxb dc, gongysb gy,fahb fh,duimxxb dm,yewlxb lx,meicb mc,duowgsb dwgs,shoumgslsb gs\n"
							+ " where js.diancxxb_id = dc.id and fh.jiesb_id=js.id and dm.fahb_id=fh.id and fh.yewlxb_id=lx.id\n"
							+ "   and dm.meicb_id=mc.id and dwgs.id(+)=mc.duowgsb_id  and fh.gongysb_id = gy.id \n"
							+ "   and gs.id=(select nvl(max(id),0) from shoumgslsb g where g.fahbid=fh.id )\n"
							+ "   and js.ruzrq >= " + OraDate(datInterfaceDate)+" and js.ruzrq<"+OraDate(datInterfaceDate)+"+1 and js.id in ("+strIds+")";
					
						int MTL_DAILY = con.getUpdate(strMTL_DAILY);
						if(MTL_DAILY<0){
							con.rollBack();
							return "上传失败!";
						}
						
					}
				}else if(Exist>0){
					return "当天数据已传送到FMIS接口，不可以重复传数!";
					
				}else{
					return "没有结算数据可上传！";
				}
						
				/*
				String yfsql = "select yf.id,dc.mingc,yf.bianm as jiesdh,to_char(yf.ruzrq, 'yyyy-mm-dd') as ruzrq,yf.hansyf as jiasje, \n"
						+ "       (yf.hansyf - yf.shuik) as meik,yf.shuik as shuik, yf.shoukdw,yf.fapbh as beiz,gy.bianm as gongysbm,\n"
						+ "       nvl((select gongf from jieszbsjb where jiesdid=js.id and zhibb_id=1),0) as gongfsl,\n"
						+ "       nvl((select gongf from jieszbsjb where jiesdid=js.id and zhibb_id=2),0) as gongfrl,\n"
						+ "       nvl((select gongf from jieszbsjb where jiesdid=js.id and zhibb_id=3),0) as gongflf,\n"
						+ "       yf.yuanshr as yuanshdw,yf.xianshr as xianshdw,to_char(yf.fahjzrq, 'yyyy-mm-dd') as daohrq \n"
						+ "  from diancjsyfb dy,kuangfjsyfb yf, kuangfjsmkb js, diancxxb dc, gongysb gy\n"
						+ " where yf.diancxxb_id = dc.id and yf.kuangfjsmkb_id=js.id and yf.shoukdw = gy.quanc and yf.diancjsmkb_id=dy.diancjsmkb_id(+) \n"
						+ "   and yf.ruzrq = "+ OraDate(datInterfaceDate);

				rs = con.getResultSet(yfsql);
				for (int i=0;rs.next();i++) {
					long mid = rs.getLong("id");
					String mjiesdbh = rs.getString("jiesdh");
					String mruzrq = rs.getString("ruzrq");

					String mjiasje = new DecimalFormat("0.00").format(rs.getDouble("jiasje"));
					String mmeik = new DecimalFormat("0.00").format(rs.getDouble("meik"));
					String mshuik = new DecimalFormat("0.00").format(rs.getDouble("shuik"));

					double mgongfsl = rs.getDouble("gongfsl");
					String mshoukdw = rs.getString("shoukdw");
					String mgongfrl = rs.getString("gongfrl");
					if(mgongfrl.indexOf("-")!=-1){
						mgongfrl = mgongfrl.substring(mgongfrl.indexOf("-")+1);
					}
					double mgongflf = rs.getDouble("gongflf");
					String myuanshdw =  rs.getString("yuanshdw");
					String mxianshdw =  rs.getString("xianshdw");
					String mdaohrq = rs.getString("daohrq");
					String mbeiz = rs.getString("beiz");
					String mgongysbm = rs.getString("gongysbm");

					String checksql  = "select P_INDEX from ZR_AP_INTERFACE where P_INDEX = "+(mid*10+1);
					ResultSet ckrs = con.getResultSet(checksql);
					if(!ckrs.next()){

						sql1 = "insert into ZR_AP_INTERFACE (P_INDEX,INVOICE_NUM,INVOICE_DATE,BOOK_DATE,VENDOR_CODE,VENDOR_SITE_CODE,INVOICE_AMOUNT,INVOICE_CURRENCY_CODE,LINE_TYPE_CODE,LINE_AMOUNT,ORGANIZATION_NAME,PLAN_QUANTITY,SUPPLIER,CONTRACT_QUANTITY,CONTRACT_HEAT,CONTRACT_S2,ORIGINAL_RECEIVER,CURRENT_RECEIVER,TRANSACTION_DATE,ARRIVE_DATE,ATTRIBUTE10,ORG_ID) "
							+ " values ("+(mid*10+1)+",'"
							+mjiesdbh+"',to_date('"+mruzrq+"','yyyy-mm-dd'),to_date('"+mruzrq+"','yyyy-mm-dd'),'"
							+mgongysbm+"','燃料结算',"+mjiasje+",'CNY','运费',"+mmeik+",'北京大唐燃料有限公司',"
							+mgongfsl+",'"+mshoukdw+"',"+mgongfsl+","+mgongfrl+","+mgongflf+",'"
							+myuanshdw+"','"+mxianshdw+"',to_date('"+mruzrq+"','yyyy-mm-dd'),to_date('"+mdaohrq+"','yyyy-mm-dd'),'"
							+mbeiz+"',157)";

						sql2 = "insert into ZR_AP_INTERFACE (P_INDEX,INVOICE_NUM,INVOICE_DATE,BOOK_DATE,VENDOR_CODE,VENDOR_SITE_CODE,INVOICE_AMOUNT,INVOICE_CURRENCY_CODE,LINE_TYPE_CODE,LINE_AMOUNT,ORGANIZATION_NAME,PLAN_QUANTITY,SUPPLIER,CONTRACT_QUANTITY,CONTRACT_HEAT,CONTRACT_S2,ORIGINAL_RECEIVER,CURRENT_RECEIVER,TRANSACTION_DATE,ARRIVE_DATE,ATTRIBUTE10,ORG_ID) "
							+ " values ("+(mid*10+2)+",'"
							+mjiesdbh+"',to_date('"+mruzrq+"','yyyy-mm-dd'),to_date('"+mruzrq+"','yyyy-mm-dd'),'"
							+mgongysbm+"','燃料结算',"+mjiasje+",'CNY','运费税',"+mshuik+",'北京大唐燃料有限公司',"
							+mgongfsl+",'"+mshoukdw+"',"+mgongfsl+","+mgongfrl+","+mgongflf+",'"+myuanshdw+"','"
							+mxianshdw+"',to_date('"+mruzrq+"','yyyy-mm-dd'),to_date('"+mdaohrq+"','yyyy-mm-dd'),'"
							+mbeiz+"',157) ";


						result1 = con.getInsert(sql1);
						result2 = con.getInsert(sql2);

						if(result1<0 || result2<0){
							con.rollBack();
							return "上传失败!";
						}
					}else{
						Exist = i+1;
					}
				}
				*/

			}
			con.commit();
		}catch(Exception e){
			e.printStackTrace();
			return"数据异常，传数失败！";
		}finally{
			con.Close();
		}
		if(Exist>0){
			return"上传成功！共有"+Exist+"条记录重复!";
		}else{
			return"上传成功！";
		}
	}
	
public String ZgFmis(Date datInterfaceDate,long intDataRange){
		
		JDBCcon con=new JDBCcon();
		con.setAutoCommit(false);
		
		StringBuffer sb = new StringBuffer();
		sb.append("begin \n");
		try{
			if (intDataRange==CAIG_DATA || intDataRange==ALL_DATA){
				//处理厂方数据
				String check = "select g_index from ZR_MTL_ZANGU_INTERFACE zg where zg.g_index like to_char("+DateUtil.FormatOracleDate(datInterfaceDate)+",'yyyymmdd')||'%'";
				ResultSetList rsl = con.getResultSetList(check);
				if(rsl.next()){
					System.out.println(DateUtil.Formatdate("yyyy-mm-dd", datInterfaceDate)+"采购入库暂估信息已经上传FMIS，不可以重复传送！");
					return DateUtil.Formatdate("yyyy-mm-dd", datInterfaceDate)+"暂估数据已经传入FMIS接口，不可以重复传送！";
				}else{
				
					String sql = "insert into ZR_MTL_ZANGU_INTERFACE(\n"
							+ "g_index,inventory_item_code,Organization_Name,Subinventory_Code,FREIGHT_LOT,Transaction_Quantity,Transaction_Uom,\n"
							+ "Transaction_Cost,transaction_date,Contract_Num,Lot_Num,Price,Railway_Freight,arrive_date,clearing_unit,process_flag,\n"
							+ "Railway_Freight_Zg,Railway_Freight_Js,Clearing_Date,SUPPLY_UNIT, INTERNAL_PURCHASE\n"
							+ ")\n"
							+ "select to_char("
							+ DateUtil.FormatOracleDate(datInterfaceDate)
							+ ",'yyyymmdd')||substr(dm.id,4)  as id,'F'||substr(g.bianm,0,6) as bianm,'北京大唐燃料有限公司' as kuczzz,\n"
							+ "       decode(lx.id,2,lx.mingc||decode(m.duowgsb_id,5,'_X','_Q'),1,'场地交割'||decode(m.duowgsb_id,5,'_X','_Q'),lx.mingc) kczz,decode(lx.id,3,m.mingc,substr(dwgs.mingc,0,1)||'-'||m.mingc) as huow,dm.ruksl,'TON',\n"
							+ "       s.meij+s.yunf as zangjg,"+ DateUtil.FormatOracleDate(datInterfaceDate)+" as zangrq,kc.biaoz,substr(f.id,4)||f.chec as chec,"
							+"		  round(jg.jij/1.17,2) as price,--s.meij,\n"
							+"s.yunf,f.daohrq,g.quanc,\n"
							+ "       0,0,0,null,g.quanc,decode(f.NEIBCG,0,'Y','N') as neibcg\n"
							+ "from\n"
							+ "  (select id,fahbid,leix,meij,yunf,riq from shoumgslsb where id in (select max(id) from shoumgslsb gs group by gs.fahbid) and leix<>4 ) s,\n"
//							+ "  (select id,fahbid,leix,meij,yunf,riq from shoumgslsb where id in (select max(id) from shoumgslsb gs group by gs.fahbid) and (leix<>4 or (leix=4 and fmisjksj is null)) ) s,\n"
							+ "  (select id,mingc,fuid from diancxxb where cangkb_id<>1) d,\n"
							+ "  (select id,quanc,bianm from gongysb) g,\n"
							+ "  fahb f,meicb m,duowgsb dwgs,duimxxb dm,duowkcb kc,yewlxb lx,hetb ht,hetjgb jg \n"
							+ "where s.fahbid=f.id and f.diancxxb_id=d.id and f.gongysb_id=g.id and dm.fahb_id=f.id and dm.meicb_id=m.id and f.yewlxb_id=lx.id \n"
							+ "  and kc.duiqm_id=dm.id and m.duowgsb_id=dwgs.id(+) and f.daohrq<" + DateUtil.FormatOracleDate(datInterfaceDate) +" and f.hetb_id=ht.id and ht.id=jg.hetb_id \n" ;
		
		            int result = con.getUpdate(sql);
		             
					if (result < 0) {
						con.rollBack();
						System.out.println(DateUtil.Formatdate("yyyy-MM-dd",datInterfaceDate)+"采购入库暂估信息上传FMIS失败！");
						return "接口传数失败!";
					}else{
//						String sql2 = "select distinct gs.id from shoumgslsb gs,fahb f "
//									+ " where leix=4 and fmisjksj is null and gs.fahbid=f.id "
//									+ "   and f.daohrq<"+DateUtil.FormatOracleDate(datInterfaceDate)+ "+1\n"
//									+ " order by gs.id ";
					
//					ResultSetList rsl2 = con.getResultSetList(sql2);

//					StringBuffer listbf = new StringBuffer();
//					listbf.append("begin \n");
//					int rows = 0;
//					for (int i = 0;rsl2.next(); i++) {
//						listbf.append("update shoumgslsb set fmisjksj="+ DateUtil.FormatOracleDate(datInterfaceDate)+ " where id="+ rsl2.getLong("id") + ";\n");
//						rows++;
//					}
//					listbf.append("end; \n");
//					if(rows>0){
//						int flage = con.getUpdate(listbf.toString());
//						if (flage < 0) {
//							con.rollBack();
//							return "接口传数状态更新失败!";
//						}
//					}
						
						con.commit();
						System.out.println(DateUtil.Formatdate("yyyy-MM-dd",datInterfaceDate)+"采购入库暂估信息上传FMIS成功！");
						return"传数成功！";
					}
				}
			}
			
			if (intDataRange==XIAOS_DATA || intDataRange==ALL_DATA){
//				处理矿方结算数据
				String check = "select g_index from zr_inv_zangu_interface zg where zg.g_index like to_char("+DateUtil.FormatOracleDate(datInterfaceDate)+",'yyyymmdd')||'%'";
				ResultSetList rsl = con.getResultSetList(check);
				if(rsl.next()){
					System.out.println(DateUtil.Formatdate("yyyy-mm-dd", datInterfaceDate)+"销售出库暂估信息已经上传FMIS，不可以重复传送！");
					return DateUtil.Formatdate("yyyy-mm-dd", datInterfaceDate)+"暂估数据已经传入FMIS接口，不可以重复传送！";
				}else{
					
				String sql = "insert into zr_inv_zangu_interface\n"
							+ "  (g_index,inventory_item_code, organization_name, subinventory_code, freight_lot, internal_sale, transaction_quantity,\n"
							+ "   transaction_uom, transaction_cost, transaction_price, transaction_date, transaction_status, transaction_reference,\n"
							+ "   contract_num, lot_num, customer_code, process_flag)\n"
							+ "\n"
							+ "  select to_char("+ DateUtil.FormatOracleDate(datInterfaceDate)+ ",'yyyymmdd')||substr(qu.id,4) G_INDEX,\n"
							+ "         f.bianm INVENTORY_ITEM_CODE,'北京大唐燃料有限公司' ORGANIZATION_NAME,\n"
							+ "         decode(lx.id,2,lx.mingc||decode(mei.duowgsb_id,5,'_X','_Q'),1,'场地交割'||decode(mei.duowgsb_id,5,'_X','_Q'),lx.mingc) SUBINVENTORY_CODE,\n"
							+ "         decode(lx.id,3,mei.mingc,substr(dw.mingc, 0, 1)||'-'||mei.mingc) FREIGHT_LOT, decode(f.neibxs, 0, 'Y', 1, 'N') INTERNAL_SALE,\n"
							+ "         nvl(kc.chuksl,0) TRANSACTION_QUANTITY, 'TON' TRANSACTION_UOM,\n"
							+ "         nvl(cb.chengb,0) TRANSACTION_COST,f.meij TRANSACTION_PRICE,"+ DateUtil.FormatOracleDate(datInterfaceDate)+ " TRANSACTION_DATE,\n"
							+ "         case when dj.ruzrq is not null then '已结算' when f.daohzt>0 then '确认收货' else '未确认收货' end TRANSACTION_STATUS,\n"
							+ "         '' TRANSACTION_REFERENCE,0 CONTRACT_NUM, f.chec LOT_NUM, f.shr CUSTOMER_CODE, 0 PROCESS_FLAG\n"
							+ "    from (\n"
							+ "    select f2.id, substr(f2.id,4)||f2.chec as chec, s.quanc as shr, f.meij, 'F'||substr(g.bianm,0,6) as bianm, f2.yewlxb_id,\n"
							+ "                  nvl(tmp.fahb_id,0) as fahb_id,f2.neibxs, f2.xiaosjsb_id, \n"
							+ "                  case when nvl(tmp.fayslb_id,0)>0 and nvl(tmp.zhilb_id,0)>0 then 1 else 0 end as daohzt \n"
							+ "            from faygslsb f, fayslb f2, fahbtmp tmp,  diancxxb dc, luncxxb lc,\n"
							+ "                 (select max(id) as id from faygslsb group by (fayslb_id)) f3,\n"
							+ "                 (select id, quanc from diancxxb where cangkb_id <> 1) d,\n"
							+ "                 (select id, quanc, bianm from gongysb) g,\n"
							+ "                 (select id, quanc from diancxxb where cangkb_id = 1) s\n"
							+ "           where f3.id = f.id and tmp.fayslb_id(+)=f2.id and f2.diancxxb_id = dc.id \n"
							+ "             and lc.id(+) = f2.luncxxb_id and f2.diancxxb_id = d.id and g.id = f2.gongysb_id and s.id = f2.keh_diancxxb_id\n"
							+ "             and f2.id = f.fayslb_id and f2.fahrq<"+ DateUtil.FormatOracleDate(datInterfaceDate)+ "\n"
							+ "				and (nvl(tmp.fayslb_id,0)=0 or (nvl(tmp.fayslb_id,0)>0 and f.fmisjksj is null))\n	"
//							+ "				 and (f.leix<>4 or (f.leix=4 and f.fmisjksj is null)) \n"
//							+ "             and (case when f2.yewlxb_id=2 and tmp.fayslb_id>0 and f.fmisjksj is not null then 1 else 0 end)=0 \n"
							+ "          ) f,\n"
							+ "         ( select fahb.id as fahbid,nvl(meij,0)+nvl(yunf,0) as chengb from shoumgslsb gs,fahb  where gs.id in\n"
							+ "          (select max(id) as smid from shoumgslsb group by (fahbid)) and fahbid=fahb.id and fahb.yewlxb_id=3 ) cb,\n"
							+ "         diancjsmkb dj,duowgsb dw, meicb mei, qumxxb qu, duowkcb  kc, yewlxb lx\n"
							+ "   where dj.id(+) = f.xiaosjsb_id and qu.zhuangcb_id = f.id and qu.meicb_id = mei.id\n"
							+ "     and mei.duowgsb_id = dw.id(+) and qu.id=kc.duiqm_id(+) and f.yewlxb_id=lx.id and f.fahb_id=cb.fahbid(+)";

					int result = con.getUpdate(sql);
					if (result < 0) {
						con.rollBack();
						System.out.println(DateUtil.Formatdate("yyyy-mm-dd",datInterfaceDate)+"销售出库暂估信息上传FMIS失败！");
						return "接口传数失败！";
					}else{
						
						String sql2 = "select distinct gs.id from faygslsb gs, fayslb fy, fahbtmp tmp,\n"
								+ "                (select max(id) as id from faygslsb group by (fayslb_id)) g\n"
								+ " where gs.id=g.id and fy.id = gs.fayslb_id and fy.id=tmp.fayslb_id(+) "
								+ "   and fy.fahrq<"+DateUtil.FormatOracleDate(datInterfaceDate)+ "\n"
								+ "   and nvl(tmp.fayslb_id,0)>0 and gs.fmisjksj is null order by gs.id ";
						
//							"   and (gs.leix<>4 or (gs.leix=4 and gs.fmisjksj is null))\n" + 
//							"   and (case when fy.yewlxb_id=2 and tmp.fayslb_id>0 and gs.fmisjksj is not null then 1 else 0 end)=0 order by gs.id ";

						ResultSetList rsl2 = con.getResultSetList(sql2);
	
						StringBuffer listbf = new StringBuffer();
						listbf.append("begin \n");
						int rows = 0;
						for (int i = 0;rsl2.next(); i++) {
							listbf.append("update faygslsb set fmisjksj="+ DateUtil.FormatOracleDate(datInterfaceDate)+ " where id="+ rsl2.getLong("id") + ";\n");
							rows++;
						}
						listbf.append("end; \n");
						if(rows>0){
							int flage = con.getUpdate(listbf.toString());
							if (flage < 0) {
								con.rollBack();
								System.out.println(DateUtil.Formatdate("yyyy-MM-dd",datInterfaceDate)+"销售出库暂估信息上传FMIS失败！接口传数状态更新失败!");
								return "接口传数状态更新失败!";
							}
						}
						
						con.commit();
						System.out.println(DateUtil.Formatdate("yyyy-MM-dd",datInterfaceDate)+"销售出库暂估信息上传FMIS成功！");
						return"传数成功！";
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(DateUtil.Formatdate("yyyy-MM-dd",datInterfaceDate)+"数据异常，传数失败！");
			return "数据异常，传数失败！";
		}finally{
			con.Close();
		}
		return "传数成功!";
	}


}
