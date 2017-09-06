package com.zhiren.jt.zdt.monthreport.cpijibreport;

/**
 * 
 * <!--cpi05-->Yuemtgyqkbreport> <!--cpi06-->Yuemthcqkbreport>
 * <!--cpi07-->Yuesyhcqkbreport> <!--cpi08-->Biaomhyqkreport>
 * <!--cpi09-->Yuemyzlqkbreport> <!--cpi10-->Rucmdj_newreport>
 * <!--cpi11-->Yuefdbmdjqkbreport> <!--cpi12-->Ranlcbfybreport>
 */
public class Cpijibreportbean {

	/**
	 * 
	 * 燃料管理05表:煤炭供应情况表
	 * 
	 * @param biaot
	 * @param dianc
	 * @param tiaoj
	 * @param fenz
	 * @param dianckjmx_bm
	 * @param dianckjmx_tj
	 * @param strGongsID
	 * @param zhuangt
	 * @param intyear
	 * @param intMonth
	 * @param intyear2
	 * @param intMonth2
	 * @return
	 */
	public String getSql5(String biaot, String dianc, String tiaoj,String grouping,String shul,
			String fenz, String dianckjmx_tj,String dianckjmx_bm,
			String strGongsID, String zhuangt,String yb_sl,String yb_cg,
			long intyear, long intMonth, long intyear2, long intMonth2) {
		String strSQL="";
		
		if(intyear2==intyear&&intMonth==1){
			strSQL=
				"select  "+biaot+",\n" +
				"     decode(1,1,fx.fenx,'') as fenx, sum(nvl(sm.dinghml,0)+nvl(sm.caigml,0)) as bqsm,sum(nvl(smtq.dinghml,0)+nvl(smtq.caigml,0)) as tqsm,\n" + 
				"     sum(sm.dinghml) as bqzd, sum(smtq.dinghml) as tqzd,\n" + 
				"     sum(sm.caigml) as bqsc, sum(smtq.caigml) as tqsc,\n" + 
				"     decode(sum(bq.ht),0,0,round( sum(sm.dinghml) /(sum(bq.ht))*100,2)) as bqdxl,\n" + 
				"     decode(sum(tq.ht),0,0,round( sum(smtq.dinghml) /(sum(tq.ht))*100,2)) as tqdxl,\n" + 
				"     sum(sm.yingd) as bqyd,sum(smtq.yingd) as tqyd,\n" + 
				"     sum(sm.kuid) as bqkd,sum(smtq.kuid) as tqkd,\n" + 
				"     sum(sm.yuns) as bqysl,sum(smtq.yuns) as tqysl,\n" + 
				"     grouping(fx.fenx) f ,grouping(dc.mingc) dcmc \n"+grouping+
				" from\n" + 
				/*" (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n" + 
				"     (select distinct diancxxb_id from yuetjkjb\n" + 
				"             where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))\n" + 
				"     ) dcid,(select decode(1,1,'本月') as fenx,1 as xuh  from dual union select decode(1,1,'累计')  as fenx,2 as xhu from dual ) fx,diancxxb dc"+dianckjmx_bm+"\n" + 
				"     where dc.id=dcid.diancxxb_id  "+dianckjmx_tj+strGongsID+" ) fx,\n" + */
				" (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n" + 
				"     (select distinct diancxxb_id from yuetjkjb\n" + 
				"             where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))\n" + 
				"     ) dcid,(select decode(1,1,'累计')  as fenx,1 as xuh from dual ) fx,diancxxb dc"+dianckjmx_bm+"\n" + 
				"     where dc.id=dcid.diancxxb_id  "+dianckjmx_tj+strGongsID+" ) fx,\n" + 
				" (select decode(1,1,'累计') as fenx,decode(dingh.diancxxb_id,null,caig.diancxxb_id,dingh.diancxxb_id) as diancxxb_id,nvl(dingh.laimsl,0) as dinghml,nvl(caig.laimsl,0) as caigml,(nvl(dingh.yingd,0)+nvl(caig.yingd,0)) as yingd,(nvl(dingh.kuid,0)+nvl(caig.kuid,0)) as kuid,(nvl(dingh.yuns,0)+nvl(caig.yuns,0)) as yuns\n" + 
				"          from (  select tj.diancxxb_id as diancxxb_id,1 as jihkjb_id,"+shul+"\n" + 
				"                    from  "+yb_sl+" sl,yuetjkjb tj,jihkjb j,diancxxb dc\n" + 
				"                    where sl.yuetjkjb_id=tj.id and j.id =tj.jihkjb_id and dc.id= tj.diancxxb_id\n" + 
				"                          "+strGongsID+" and sl.fenx='累计' and tj.riq=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n" + 
				"                          and (tj.jihkjb_id=1 or tj.jihkjb_id=3)"+zhuangt+"\n" + 
				"                    group by(tj.diancxxb_id)\n" + 
				"               ) dingh\n" + 
				"             FULL JOIN (  select tj.diancxxb_id as diancxxb_id,2 as jihkjb_id,"+shul+"\n" + 
				"                 from  "+yb_sl+" sl,yuetjkjb tj,jihkjb j,diancxxb dc\n" + 
				"                 where sl.yuetjkjb_id=tj.id and j.id =tj.jihkjb_id and dc.id= tj.diancxxb_id\n" + 
				"                        "+strGongsID+" and sl.fenx='累计' and tj.riq=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n" + 
				"                        and (tj.jihkjb_id<>1 and  tj.jihkjb_id<>3)"+zhuangt+"\n" + 
				"                  group by(tj.diancxxb_id)\n" + 
				"               ) caig\n" + 
				"           ON dingh.diancxxb_id=caig.diancxxb_id\n" + 
				") sm,\n" +
				"(select decode(1,1,'累计') as fenx,decode(dingh.diancxxb_id,null,caig.diancxxb_id,dingh.diancxxb_id) as diancxxb_id,nvl(dingh.laimsl,0) as dinghml,nvl(caig.laimsl,0) as caigml,(nvl(dingh.yingd,0)+nvl(caig.yingd,0)) as yingd,(nvl(dingh.kuid,0)+nvl(caig.kuid,0)) as kuid,(nvl(dingh.yuns,0)+nvl(caig.yuns,0)) as yuns\n" + 
				"          from (  select tj.diancxxb_id as diancxxb_id,1 as jihkjb_id,"+shul+"\n" + 
				"                    from  "+yb_sl+" sl,yuetjkjb tj,jihkjb j,diancxxb dc\n" + 
				"                    where sl.yuetjkjb_id=tj.id and j.id =tj.jihkjb_id and dc.id= tj.diancxxb_id\n" + 
				"                         "+strGongsID+" and sl.fenx='累计' and tj.riq=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)\n" + 
				"                          and (tj.jihkjb_id=1 or tj.jihkjb_id=3)"+zhuangt+"\n" + 
				"                    group by(tj.diancxxb_id)\n" + 
				"               ) dingh\n" + 
				"             FULL JOIN (  select tj.diancxxb_id as diancxxb_id,2 as jihkjb_id,"+shul+"\n" + 
				"                 from  "+yb_sl+" sl,yuetjkjb tj,jihkjb j,diancxxb dc\n" + 
				"                 where sl.yuetjkjb_id=tj.id and j.id =tj.jihkjb_id and dc.id= tj.diancxxb_id\n" + 
				"                        "+strGongsID+" and sl.fenx='累计' and tj.riq=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)\n" + 
				"                        and (tj.jihkjb_id<>1 and  tj.jihkjb_id<>3)"+zhuangt+"\n" + 
				"                  group by(tj.diancxxb_id)\n" + 
				"               ) caig\n" + 
				"           ON dingh.diancxxb_id=caig.diancxxb_id\n" + 
				") smtq,\n" + 
//---------------------------------------------------------------------------------------------------
				" (select decode(1,1,'累计') as fenx,htqk.diancxxb_id,sum(htqk.yuejhcgl) as ht\n" + 
				"         from  "+yb_cg+" htqk\n" + 
				"         where htqk.riq>=to_date('"+intyear+"-01-01','yyyy-mm-dd') and htqk.riq<=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n" + 
				"               and (htqk.jihkjb_id=1 or htqk.jihkjb_id=3)\n" + 
				"         group by htqk.diancxxb_id\n" + 
				"          )bq,"+

				"(select decode(1,1,'累计') as fenx,htqk.diancxxb_id,sum(htqk.yuejhcgl) as ht\n" + 
				"         from  "+yb_cg+" htqk\n" + 
				"         where htqk.riq>=add_months(to_date('"+intyear+"-01-01','yyyy-mm-dd'),-12) and htqk.riq<=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)\n" + 
				"               and (htqk.jihkjb_id=1 or htqk.jihkjb_id=3)\n" + 
				"         group by htqk.diancxxb_id ) tq,"+

				dianc + 
				"where dc.id=fx.diancxxb_id "+tiaoj+"\n" + 
				"  and fx.diancxxb_id=sm.diancxxb_id(+)\n" + 
				"  and fx.fenx=sm.fenx(+)\n" + 
				"  and fx.diancxxb_id=bq.diancxxb_id(+)\n" + 
				"  and fx.fenx=bq.fenx(+)\n" + 
				"  and fx.diancxxb_id=tq.diancxxb_id(+)\n" + 
				"  and fx.fenx=tq.fenx(+)\n" + 
				"  and fx.diancxxb_id=smtq.diancxxb_id(+)\n" + 
				"  and fx.fenx=smtq.fenx(+)\n" +  fenz;
			
		}else{
			   strSQL="select  "+biaot+",\n" +
			"     decode(1,1,fx.fenx,'') as fenx, sum(nvl(sm.dinghml,0)+nvl(sm.caigml,0)) as bqsm,sum(nvl(smtq.dinghml,0)+nvl(smtq.caigml,0)) as tqsm,\n" + 
			"     sum(sm.dinghml) as bqzd, sum(smtq.dinghml) as tqzd,\n" + 
			"     sum(sm.caigml) as bqsc, sum(smtq.caigml) as tqsc,\n" + 
			"     decode(sum(bq.ht),0,0,round( sum(sm.dinghml) /(sum(bq.ht))*100,2)) as bqdxl,\n" + 
			"     decode(sum(tq.ht),0,0,round( sum(smtq.dinghml) /(sum(tq.ht))*100,2)) as tqdxl,\n" + 
			"     sum(sm.yingd) as bqyd,sum(smtq.yingd) as tqyd,\n" + 
			"     sum(sm.kuid) as bqkd,sum(smtq.kuid) as tqkd,\n" + 
			"     sum(sm.yuns) as bqysl,sum(smtq.yuns) as tqysl,\n" + 
			"     grouping(fx.fenx) f ,grouping(dc.mingc) dcmc \n"+grouping+
			" from\n" + 
			/*" (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n" + 
			"     (select distinct diancxxb_id from yuetjkjb\n" + 
			"             where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))\n" + 
			"     ) dcid,(select decode(1,1,'本月') as fenx,1 as xuh  from dual union select decode(1,1,'累计')  as fenx,2 as xhu from dual ) fx,diancxxb dc"+dianckjmx_bm+"\n" + 
			"     where dc.id=dcid.diancxxb_id  "+dianckjmx_tj+strGongsID+" ) fx,\n" + */
			"(select dcid.diancxxb_id, fx.fenx, fx.xuh\n" +
					 "         from (select distinct diancxxb_id\n" +
					  "                from yuetjkjb\n" +
					   "              where ( riq >= to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd') and riq <= to_date('"+intyear2+"-"+intMonth2+"-01', 'yyyy-mm-dd') or\n" +
					    "                   (riq >=add_months(to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd'), -12) and riq<=add_months(to_date('"+intyear2+"-"+intMonth2+"-01', 'yyyy-mm-dd'), -12))\n" +
					     "          ) )dcid,\n" +
					      "         (select decode(1, 1, '累计') as fenx, 1 as xuh from dual\n" +
					       "        ) fx,diancxxb dc"+dianckjmx_bm+"\n" + 
					         "where dc.id = dcid.diancxxb_id "+dianckjmx_tj+strGongsID+"\n" +
			") fx,\n" +
			" (select decode(1,1,'累计') as fenx,decode(dingh.diancxxb_id,null,caig.diancxxb_id,dingh.diancxxb_id) as diancxxb_id,nvl(dingh.laimsl,0) as dinghml,nvl(caig.laimsl,0) as caigml,(nvl(dingh.yingd,0)+nvl(caig.yingd,0)) as yingd,(nvl(dingh.kuid,0)+nvl(caig.kuid,0)) as kuid,(nvl(dingh.yuns,0)+nvl(caig.yuns,0)) as yuns\n" + 
			"          from (  select tj.diancxxb_id as diancxxb_id,1 as jihkjb_id,"+shul+"\n" + 
			"                    from  "+yb_sl+" sl,yuetjkjb tj,jihkjb j,diancxxb dc\n" + 
			"                    where sl.yuetjkjb_id=tj.id and j.id =tj.jihkjb_id and dc.id= tj.diancxxb_id\n" + 
			//"                          "+strGongsID+" and sl.fenx='累计' and tj.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
			" "+strGongsID+"\n"+
            "and sl.fenx = '本月'\n" +
            "and tj.riq >= to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd') and tj.riq <= to_date('"+intyear2+"-"+intMonth2+"-01', 'yyyy-mm-dd')\n" +
			"                          and (tj.jihkjb_id=1 or tj.jihkjb_id=3)"+zhuangt+"\n" + 
			"                    group by(tj.diancxxb_id)\n" + 
			"               ) dingh\n" + 
			"             FULL JOIN (  select tj.diancxxb_id as diancxxb_id,2 as jihkjb_id,"+shul+"\n" + 
			"                 from  "+yb_sl+" sl,yuetjkjb tj,jihkjb j,diancxxb dc\n" + 
			"                 where sl.yuetjkjb_id=tj.id and j.id =tj.jihkjb_id and dc.id= tj.diancxxb_id\n" + 
			//"                        "+strGongsID+" and sl.fenx='累计' and tj.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
			" "+strGongsID+"\n"+
            "and sl.fenx = '本月'\n" +
            "and tj.riq >= to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd') and tj.riq <= to_date('"+intyear2+"-"+intMonth2+"-01', 'yyyy-mm-dd')\n" +
			"                        and (tj.jihkjb_id<>1 and  tj.jihkjb_id<>3)"+zhuangt+"\n" + 
			"                  group by(tj.diancxxb_id)\n" + 
			"               ) caig\n" + 
			"           ON dingh.diancxxb_id=caig.diancxxb_id\n" + 
			") sm,\n" +
			"( select decode(1,1,'累计') as fenx,decode(dingh.diancxxb_id,null,caig.diancxxb_id,dingh.diancxxb_id) as diancxxb_id,nvl(dingh.laimsl,0) as dinghml,nvl(caig.laimsl,0) as caigml,(nvl(dingh.yingd,0)+nvl(caig.yingd,0)) as yingd,(nvl(dingh.kuid,0)+nvl(caig.kuid,0)) as kuid,(nvl(dingh.yuns,0)+nvl(caig.yuns,0)) as yuns\n" + 
			"          from (  select tj.diancxxb_id as diancxxb_id,1 as jihkjb_id,"+shul+"\n" + 
			"                    from  "+yb_sl+" sl,yuetjkjb tj,jihkjb j,diancxxb dc\n" + 
			"                    where sl.yuetjkjb_id=tj.id and j.id =tj.jihkjb_id and dc.id= tj.diancxxb_id\n" + 
			//"                         "+strGongsID+" and sl.fenx='累计' and tj.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" + 
			" "+strGongsID+"\n"+
            "and sl.fenx = '本月'\n" +
           " and tj.riq >=add_months(to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd'), -12) and tj.riq <=add_months(to_date('"+intyear2+"-"+intMonth2+"-01', 'yyyy-mm-dd'), -12)\n" +
			"                          and (tj.jihkjb_id=1 or tj.jihkjb_id=3)"+zhuangt+"\n" + 
			"                    group by(tj.diancxxb_id)\n" + 
			"               ) dingh\n" + 
			"             FULL JOIN (  select tj.diancxxb_id as diancxxb_id,2 as jihkjb_id,"+shul+"\n" + 
			"                 from  "+yb_sl+" sl,yuetjkjb tj,jihkjb j,diancxxb dc\n" + 
			"                 where sl.yuetjkjb_id=tj.id and j.id =tj.jihkjb_id and dc.id= tj.diancxxb_id\n" + 
			//"                        "+strGongsID+" and sl.fenx='累计' and tj.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" + 
			" "+strGongsID+"\n"+
            "and sl.fenx = '本月'\n" +
           " and tj.riq >=add_months(to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd'), -12) and tj.riq <=add_months(to_date('"+intyear2+"-"+intMonth2+"-01', 'yyyy-mm-dd'), -12)\n" +
			"                        and (tj.jihkjb_id<>1 and  tj.jihkjb_id<>3)"+zhuangt+"\n" + 
			"                  group by(tj.diancxxb_id)\n" + 
			"               ) caig\n" + 
			"           ON dingh.diancxxb_id=caig.diancxxb_id\n" + 
			") smtq,\n" + 
//---------------------------------------------------------------------------------------------------
			" (select decode(1,1,'累计') as fenx,htqk.diancxxb_id,sum(htqk.yuejhcgl) as ht\n" + 
			"         from  "+yb_cg+" htqk\n" + 
			//"         where htqk.riq>=to_date('"+intyear+"-01-01','yyyy-mm-dd') and htqk.riq<=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
			"    where htqk.riq >= to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd') and htqk.riq <= to_date('"+intyear2+"-"+intMonth2+"-01', 'yyyy-mm-dd')\n" +
			"               and (htqk.jihkjb_id=1 or htqk.jihkjb_id=3)\n" + 
			"         group by htqk.diancxxb_id\n" + 
			"          )bq,"+

			"(select decode(1,1,'累计') as fenx,htqk.diancxxb_id,sum(htqk.yuejhcgl) as ht\n" + 
			"         from  "+yb_cg+" htqk\n" + 
			//"         where htqk.riq>=add_months(to_date('"+intyear+"-01-01','yyyy-mm-dd'),-12) and htqk.riq<=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" + 
			"    where htqk.riq >=add_months(to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd'), -12) and htqk.riq <=add_months(to_date('"+intyear2+"-"+intMonth2+"-01', 'yyyy-mm-dd'), -12)\n" +
			"               and (htqk.jihkjb_id=1 or htqk.jihkjb_id=3)\n" + 
			"         group by htqk.diancxxb_id ) tq,"+
			dianc + 
			"where dc.id=fx.diancxxb_id "+tiaoj+"\n" + 
			"  and fx.diancxxb_id=sm.diancxxb_id(+)\n" + 
			"  and fx.fenx=sm.fenx(+)\n" + 
			"  and fx.diancxxb_id=bq.diancxxb_id(+)\n" + 
			"  and fx.fenx=bq.fenx(+)\n" + 
			"  and fx.diancxxb_id=tq.diancxxb_id(+)\n" + 
			"  and fx.fenx=tq.fenx(+)\n" + 
			"  and fx.diancxxb_id=smtq.diancxxb_id(+)\n" + 
			"  and fx.fenx=smtq.fenx(+)\n" +  fenz;
		}
		return strSQL;

	}

	/**
	 * 
	 * 燃料管理06表:月煤炭耗用与库存情况表
	 * 
	 * @param biaot
	 * @param dianc
	 * @param yueshcmzt
	 * @param tiaoj
	 * @param fenz
	 * @param dianckjmx_bm
	 * @param dianckjmx_tj
	 * @param strGongsID
	 * @param zhuangt
	 * @param intyear
	 * @param intMonth
	 * @param intyear2
	 * @param intMonth2
	 * @return
	 */
	public String getSql6(String biaot, String dianc, String yueshcmzt,String shouhcsl_bq,String shouhckc_bq,String shouhcsl_tq,String shouhckc_tq,
			String tiaoj, String fenz, String dianckjmx_bm,String grouping,
			String dianckjmx_tj, String strGongsID, String zhuangt,String yb_zb,String yb_shcm,
			long intyear, long intMonth, long intyear2, long intMonth2) {
		String strSQL = "";
		if(intyear2==intyear&&intMonth==1){
			strSQL=
				"select  "+biaot+",\n" +
				"         fx.fenx, Round(sum(sm.fadl),0) as bqfdl,Round(sum(smtq.fadl),0) as tqfdl,\n" + 
				"         sum(sm.fadhml+sm.gongrhml+sm.qithyl) as bqqbml, sum(smtq.fadhml+smtq.gongrhml+smtq.qithyl) as tqqbml,\n" + 
				"         sum(sm.fadhml) as bqfhml, sum(smtq.fadhml) as tqfhml,\n" + 
				"         sum(sm.gongrhml) as bqghml, sum(smtq.gongrhml) as tqghml,\n" + 
				"         sum(sm.qithyl) as bqqt,sum(smtq.qithyl) as tqqt,\n" + 
				"         sum(sm.chucshl) as bqcc,sum(smtq.chucshl) as tqcc,\n" + 
				"         sum(sm.shuifctz) as bqsfc,sum(smtq.shuifctz) as tqsfc,\n" + 
				"         sum(smkc.shijkc) as bqskc,sum(smkctq.shijkc) as tqskc,\n" + 
				"         sum(smkc.shijkc-smkc.panyk) as bqzkc,sum(smkctq.shijkc-smkctq.panyk) as tqzkc\n" + 
				"          ,grouping(fx.fenx) f ,grouping(dc.mingc) dcmc \n"+grouping+
				" from\n" + 
				/*" (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n" + 
				"     (select distinct diancxxb_id from yueshchjb\n" + 
				"             where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))\n" + 
				"     ) dcid,(select decode(1,1,'累计')  as fenx,1 as xuh from dual ) fx,diancxxb dc"+dianckjmx_bm+"\n" + 
				"     where dc.id=dcid.diancxxb_id  "+dianckjmx_tj+strGongsID+"  ) fx,\n" +*/ 
				 " (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n"
				+ "     (select distinct diancxxb_id from  "+yb_shcm+"\n"
				+ "             where  riq=to_date('"
				+ intyear2
				+ "-"
				+ intMonth2
				+ "-01','yyyy-mm-dd') or riq=add_months(to_date('"
				+ intyear2
				+ "-"
				+ intMonth2
				+ "-01','yyyy-mm-dd'),-12)\n"
				+ "     ) dcid,(select decode(1,1,'累计') as fenx,1 as xuh  from dual " +
						" ) fx,diancxxb dc"
				+ dianckjmx_bm
				+ "\n"
				+ "     where dc.id=dcid.diancxxb_id  "
				+ dianckjmx_tj
				+ strGongsID
				+ "  ) fx,\n"+
												
				"(select decode(1,1,'累计') as fenx,shc.diancxxb_id as diancxxb_id,"+shouhcsl_bq+"\n" + 
				"       from  "+yb_zb+" zb, "+yb_shcm+" shc,diancxxb dc\n" + 
				"       where zb.diancxxb_id =dc.id and shc.diancxxb_id =dc.id "+strGongsID+"\n" + 
				"             and zb.riq=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n" + 
				"             and shc.riq=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n" + 
				"             and zb.fenx(+)=shc.fenx and shc.fenx='累计'"+zhuangt+yueshcmzt+" ) sm,\n" + 
				"(select decode(1,1,'累计') as fenx,shc.diancxxb_id as diancxxb_id,"+shouhcsl_tq+"\n" + 
				"       from  "+yb_zb+" zb, "+yb_shcm+" shc,diancxxb dc\n" + 
				"       where zb.diancxxb_id =dc.id and shc.diancxxb_id =dc.id "+strGongsID+"\n" + 
				"             and zb.riq=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)\n" + 
				"             and shc.riq=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)\n" + 
				"             and zb.fenx(+)=shc.fenx and shc.fenx='累计'"+zhuangt+yueshcmzt+" ) smtq,\n" + 
//----------------------------库存累计取本月---------------------------------------------------------------------------------------
				"(select decode(1,1,'累计') as fenx,shc.diancxxb_id as diancxxb_id,\n" + 
				shouhckc_bq+ " from  "+yb_zb+" zb, "+yb_shcm+" shc,diancxxb dc\n" +
				"      where zb.diancxxb_id =dc.id and shc.diancxxb_id =dc.id  "+strGongsID+"\n" + 
				"            and zb.riq=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n" + 
				"            and shc.riq=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n" + 
				"            and zb.fenx(+)=shc.fenx and shc.fenx='累计' "+zhuangt+yueshcmzt+" ) smkc,\n" + 
				"(select decode(1,1,'累计') as fenx,shc.diancxxb_id as diancxxb_id,\n" +
				shouhckc_tq+ "\n" +
				"      from  "+yb_zb+" zb, "+yb_shcm+" shc,diancxxb dc\n" + 
				"      where zb.diancxxb_id =dc.id and shc.diancxxb_id =dc.id  "+strGongsID+"\n" +
				"            and zb.riq=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)\n" + 
				"            and shc.riq=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)\n" + 
				"            and zb.fenx(+)=shc.fenx and shc.fenx='累计' "+zhuangt+yueshcmzt+") smkctq,"+
//-------------------------------------------------------------------------------------------------------------------
				dianc + 
				"where dc.id=fx.diancxxb_id "+tiaoj+"\n" +
				"and fx.diancxxb_id=smkc.diancxxb_id(+)\n" +
				"    and fx.fenx=smkc.fenx(+)\n" + 
				"    and fx.diancxxb_id=smkctq.diancxxb_id(+)\n" + 
				"    and fx.fenx=smkctq.fenx(+)"+
				"      and fx.diancxxb_id=sm.diancxxb_id(+)\n" + 
				"      and fx.fenx=sm.fenx(+)\n" + 
				"      and fx.diancxxb_id=smtq.diancxxb_id(+)\n" + 
				"      and fx.fenx=smtq.fenx(+)\n" + fenz;
			
		}
		else{
			strSQL=
				"select  "+biaot+",\n" +
				"         fx.fenx, Round(sum(sm.fadl),0) as bqfdl,Round(sum(smtq.fadl),0) as tqfdl,\n" + 
				"         sum(sm.fadhml+sm.gongrhml+sm.qithyl) as bqqbml, sum(smtq.fadhml+smtq.gongrhml+smtq.qithyl) as tqqbml,\n" + 
				"         sum(sm.fadhml) as bqfhml, sum(smtq.fadhml) as tqfhml,\n" + 
				"         sum(sm.gongrhml) as bqghml, sum(smtq.gongrhml) as tqghml,\n" + 
				"         sum(sm.qithyl) as bqqt,sum(smtq.qithyl) as tqqt,\n" + 
				"         sum(sm.chucshl) as bqcc,sum(smtq.chucshl) as tqcc,\n" + 
				"         sum(sm.shuifctz) as bqsfc,sum(smtq.shuifctz) as tqsfc,\n" + 
				"         sum(smkc.shijkc) as bqskc,sum(smkctq.shijkc) as tqskc,\n" + 
				"         sum(smkc.shijkc-smkc.panyk) as bqzkc,sum(smkctq.shijkc-smkctq.panyk) as tqzkc\n" + 
				"          ,grouping(fx.fenx) f ,grouping(dc.mingc) dcmc \n"+grouping+
				" from\n" + 
				/*" (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n" + 
				"     (select distinct diancxxb_id from yueshchjb\n" + 
				"             where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))\n" + 
				"     ) dcid,(select decode(1,1,'本月') as fenx,1 as xuh  from dual union select decode(1,1,'累计')  as fenx,2 as xhu from dual ) fx,diancxxb dc"+dianckjmx_bm+"\n" + 
				"     where dc.id=dcid.diancxxb_id  "+dianckjmx_tj+strGongsID+"  ) fx,\n" + */
				 " (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n"
				+ "     (select distinct diancxxb_id from  "+yb_shcm+"\n"
				+"             where  ((riq>=to_date('"
				+ intyear
				+ "-"
				+ intMonth
				+ "-01','yyyy-mm-dd') and riq<=to_date('"
				+ intyear2
				+ "-"
				+ intMonth2
				+ "-01','yyyy-mm-dd')) or (riq>=add_months(to_date('"
				+ intyear
				+ "-"
				+ intMonth
				+ "-01','yyyy-mm-dd'),-12)) and riq<=add_months(to_date('"
				+ intyear2
				+ "-"
				+ intMonth2
				+ "-01','yyyy-mm-dd'),-12))\n"
				+ "     ) dcid,(select decode(1,1,'累计') as fenx,1 as xuh  from dual " 
				+		" ) fx,diancxxb dc"
				+ dianckjmx_bm
				+ "\n"
				+ "     where dc.id=dcid.diancxxb_id  "
				+ dianckjmx_tj
				+ strGongsID
				+ "  ) fx,\n"
				+"(\n" + 
				"  select decode(1,1,'累计') as fenx,shc.diancxxb_id as diancxxb_id,"+shouhcsl_bq+"\n" + 
				"       from  "+yb_zb+" zb, "+yb_shcm+" shc,diancxxb dc\n" + 
				"       where zb.diancxxb_id =dc.id and shc.diancxxb_id =dc.id "+strGongsID+"\n" + 
				/*"             and zb.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
				"             and shc.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + */
				"             and zb.riq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
				"             and zb.riq<=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n" + 
				"             and shc.riq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" +
				"             and shc.riq<=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n" +
				"             and zb.riq = shc.riq and zb.fenx(+)=shc.fenx and shc.fenx='本月'"+zhuangt+yueshcmzt+"\n" + 
				"  group by shc.diancxxb_id \n" + 
				") sm,\n" + 
				"(select decode(1,1,'累计') as fenx,shc.diancxxb_id as diancxxb_id,"+shouhcsl_tq+"\n" + 
				"       from  "+yb_zb+" zb, "+yb_shcm+" shc,diancxxb dc\n" + 
				"       where zb.diancxxb_id =dc.id and shc.diancxxb_id =dc.id "+strGongsID+"\n" + 
				//"             and zb.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" + 
				//"             and shc.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" + 
				"             and zb.riq>=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" + 
				"             and zb.riq<=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)\n" + 
				"             and shc.riq>=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" + 
				"             and shc.riq<=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)\n" + 
				"             and zb.riq = shc.riq and zb.fenx(+)=shc.fenx and shc.fenx='本月'"+zhuangt+yueshcmzt+"\n" + 
				" group by shc.diancxxb_id \n" + 
				") smtq,\n" + 
//----------------------------库存累计取本月---------------------------------------------------------------------------------------
				"(select decode(1,1,'累计') as fenx,shc.diancxxb_id as diancxxb_id,\n" +
				 //盘盈亏和实际库存都取末月的
				shouhckc_bq+ "\n" +
				"      from  "+yb_zb+" zb, "+yb_shcm+" shc,diancxxb dc\n" + 
				"      where zb.diancxxb_id =dc.id and shc.diancxxb_id =dc.id  "+strGongsID+"\n" +
				/*"             and zb.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
				"             and shc.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + */
				"             and zb.riq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
				"             and zb.riq<=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n" + 
				"             and shc.riq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" +
				"             and shc.riq<=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n" +
				"             and zb.riq = shc.riq and zb.fenx(+)=shc.fenx and shc.fenx='本月' "+zhuangt+yueshcmzt+" \n" + 
				" group by shc.diancxxb_id \n" + 
				") smkc,\n" + 
				"(select decode(1,1,'累计') as fenx,shc.diancxxb_id as diancxxb_id,\n" +
//				盘盈亏和实际库存都取末月的
			    shouhckc_tq+ "\n" +
				"      from  "+yb_zb+" zb, "+yb_shcm+" shc,diancxxb dc\n" + 
				"      where zb.diancxxb_id =dc.id and shc.diancxxb_id =dc.id  "+strGongsID+"\n" +
				//"            and zb.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" + 
				//"            and shc.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" + 
				"             and zb.riq>=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" + 
				"             and zb.riq<=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)\n" + 
				"             and shc.riq>=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" + 
				"             and shc.riq<=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)\n" + 
				"             and zb.riq = shc.riq and zb.fenx(+)=shc.fenx and shc.fenx='本月' "+zhuangt+yueshcmzt+" \n" + 
				" group by shc.diancxxb_id\n" + 
				") smkctq,"+
//-------------------------------------------------------------------------------------------------------------------
				dianc + 
				"where dc.id=fx.diancxxb_id "+tiaoj+"\n" +
				"and fx.diancxxb_id=smkc.diancxxb_id(+)\n" +
				"    and fx.fenx=smkc.fenx(+)\n" + 
				"    and fx.diancxxb_id=smkctq.diancxxb_id(+)\n" + 
				"    and fx.fenx=smkctq.fenx(+)"+
				"      and fx.diancxxb_id=sm.diancxxb_id(+)\n" + 
				"      and fx.fenx=sm.fenx(+)\n" + 
				"      and fx.diancxxb_id=smtq.diancxxb_id(+)\n" + 
				"      and fx.fenx=smtq.fenx(+)\n" + fenz;
		}

		return strSQL;

	}

	/**
	 * 
	 * 燃料管理07表:石油供应耗用与库存情况表
	 * 
	 * @param biaot
	 * @param dianc
	 * @param tiaoj
	 * @param fenz
	 * @param dianckjmx_bm
	 * @param dianckjmx_tj
	 * @param strGongsID
	 * @param zhuangt
	 * @param intyear
	 * @param intMonth
	 * @param intyear2
	 * @param intMonth2
	 * @return
	 */
	public String getSql7(String biaot, String dianc, String tiaoj,String grouping,String shouhcysl_bq,String shouhcykc_bq,
			String fenz, String dianckjmx_bm, String dianckjmx_tj,String shouhcysl_tq,String shouhcykc_tq,
			String strGongsID, String zhuangt, String yb_shcy,long intyear, long intMonth,
			long intyear2, long intMonth2) {
		String strSQL ="";
		if(intyear2==intyear&&intMonth==1){
			strSQL=
				"select  "+biaot+",\n" +
				"     fx.fenx, sum(sm.shiygyl) as bqgyl,sum(smtq.shiygyl) as tqgyl,\n" + 
				"     sum(sm.fadhyl+sm.gongrhyl+sm.qithyl) as bqqhyl, sum(smtq.fadhyl+smtq.gongrhyl+smtq.qithyl) as tqqhyl,\n" + 
				"     sum(sm.fadhyl) as bqfhl, sum(smtq.fadhyl) as tqfhl,\n" + 
				"     sum(sm.gongrhyl) as bqghl, sum(smtq.gongrhyl) as tqghl,\n" + 
				"     sum(sm.qithyl) as bqqt,sum(smtq.qithyl) as tqqt,\n" + 
				"     sum(sm.chucshl) as bqcc,sum(smtq.chucshl) as tqcc,\n" + 
				"     sum(smkc.shijkc) as bqskc,sum(smkctq.shijkc) as tqskc,\n" + 
				"     sum(smkc.shijkc-smkc.panyk) as bqzkc,sum(smkctq.shijkc-smkctq.panyk) as tqzkc,\n" + 
				"     grouping(fx.fenx) f ,grouping(dc.mingc) dcmc \n"+grouping+
				" from\n" + 
				/*" (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n" + 
				"     (select distinct diancxxb_id from yueshcyb\n" + 
				"             where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))\n" + 
				"     ) dcid,(select decode(1,1,'本月') as fenx,1 as xuh  from dual union select decode(1,1,'累计')  as fenx,2 as xhu from dual ) fx,diancxxb dc"+dianckjmx_bm+"\n" + 
				"     where dc.id=dcid.diancxxb_id  "+dianckjmx_tj+strGongsID+" ) fx,\n" +*/ 
				 " (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n"
				+ "     (select distinct diancxxb_id from  "+yb_shcy+"\n"
				+
				" where  (riq=to_date('"
				+ intyear2
				+ "-"
				+ intMonth2
				+ "-01','yyyy-mm-dd')  or  riq=add_months(to_date('"
				+ intyear2
				+ "-"
				+ intMonth2
				+ "-01','yyyy-mm-dd'),-12))\n"
				+ "     ) dcid,(select decode(1,1,'累计') as fenx,1 as xuh  from dual " +
						") fx,diancxxb dc"
				+ dianckjmx_bm
				+ "\n"
				+ "     where dc.id=dcid.diancxxb_id  "
				+ dianckjmx_tj
				+ strGongsID
				+ " ) fx,\n"+
				"(select decode(1,1,'累计') as fenx,shcy.diancxxb_id as diancxxb_id,"+shouhcysl_bq+"\n" + 
				"       from  "+yb_shcy+" shcy,diancxxb dc\n" + 
				"       where shcy.diancxxb_id=dc.id and shcy.riq=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n" + 
				"             and shcy.fenx='累计' "+strGongsID+zhuangt+"\n" + 
				"       group by (shcy.diancxxb_id)\n" + 
				") sm,\n" + 
				"(select decode(1,1,'累计') as fenx,shcy.diancxxb_id as diancxxb_id,"+shouhcysl_tq+"\n" + 
				"       from  "+yb_shcy+" shcy,diancxxb dc\n" + 
				"       where shcy.diancxxb_id=dc.id and shcy.riq=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)\n" + 
				"             and shcy.fenx='累计' "+strGongsID+zhuangt+"\n" + 
				"       group by (shcy.diancxxb_id)) smtq,\n" + 
//------------------------------------累计库存显示本月----------------------------------------------
				"(select decode(1,1,'累计') as fenx,shcy.diancxxb_id as diancxxb_id,"+shouhcykc_bq+"\n" + 
				"       from  "+yb_shcy+" shcy,diancxxb dc\n" + 
				"       where shcy.diancxxb_id=dc.id and shcy.riq=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n" + 
				"             and shcy.fenx='本月'   "+strGongsID+zhuangt+"\n" +
				"       group by (shcy.diancxxb_id)) smkc,\n" + 
				"(select decode(1,1,'累计') as fenx,shcy.diancxxb_id as diancxxb_id,"+shouhcykc_tq+"\n" + 
				"       from  "+yb_shcy+" shcy,diancxxb dc\n" + 
				"       where shcy.diancxxb_id=dc.id and shcy.riq=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)\n" + 
				"             and shcy.fenx='本月'  "+strGongsID+zhuangt+"\n" +
				"       group by (shcy.diancxxb_id)) smkctq,"+
//----------------------------------------------------------------------------------
				dianc + 
				"where dc.id=fx.diancxxb_id "+tiaoj+"\n" +
				"and fx.diancxxb_id=smkc.diancxxb_id(+)\n" +
				"and fx.fenx=smkc.fenx(+)\n" + 
				"and fx.diancxxb_id=smkctq.diancxxb_id(+)\n" + 
				"and fx.fenx=smkctq.fenx(+)"+
				"  and fx.diancxxb_id=sm.diancxxb_id(+)\n" + 
				"  and fx.fenx=sm.fenx(+)\n" + 
				"  and fx.diancxxb_id=smtq.diancxxb_id(+)\n" + 
				"  and fx.fenx=smtq.fenx(+)\n" + fenz;
			
			}
		else{strSQL=
			"select  "+biaot+",\n" +
			"     fx.fenx, sum(sm.shiygyl) as bqgyl,sum(smtq.shiygyl) as tqgyl,\n" + 
			"     sum(sm.fadhyl+sm.gongrhyl+sm.qithyl) as bqqhyl, sum(smtq.fadhyl+smtq.gongrhyl+smtq.qithyl) as tqqhyl,\n" + 
			"     sum(sm.fadhyl) as bqfhl, sum(smtq.fadhyl) as tqfhl,\n" + 
			"     sum(sm.gongrhyl) as bqghl, sum(smtq.gongrhyl) as tqghl,\n" + 
			"     sum(sm.qithyl) as bqqt,sum(smtq.qithyl) as tqqt,\n" + 
			"     sum(sm.chucshl) as bqcc,sum(smtq.chucshl) as tqcc,\n" + 
			"     sum(smkc.shijkc) as bqskc,sum(smkctq.shijkc) as tqskc,\n" + 
			"     sum(smkc.shijkc-smkc.panyk) as bqzkc,sum(smkctq.shijkc-smkctq.panyk) as tqzkc,\n" + 
			"     grouping(fx.fenx) f ,grouping(dc.mingc) dcmc \n"+grouping+
			" from\n" + 
			/*" (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n" + 
			"     (select distinct diancxxb_id from yueshcyb\n" + 
			"             where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))\n" + 
			"     ) dcid,(select decode(1,1,'本月') as fenx,1 as xuh  from dual union select decode(1,1,'累计')  as fenx,2 as xhu from dual ) fx,diancxxb dc"+dianckjmx_bm+"\n" + 
			"     where dc.id=dcid.diancxxb_id  "+dianckjmx_tj+strGongsID+" ) fx,\n" + */
			 " (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n"
			+ "     (select distinct diancxxb_id from  "+yb_shcy+"\n"
			+" where  ((riq>=to_date('"
			+ intyear
			+ "-"
			+ intMonth
			+ "-01','yyyy-mm-dd') and riq<=to_date('"
			+ intyear2
			+ "-"
			+ intMonth2
			+ "-01','yyyy-mm-dd')) or (riq>=add_months(to_date('"
			+ intyear
			+ "-"
			+ intMonth
			+ "-01','yyyy-mm-dd'),-12) and riq<=add_months(to_date('"
			+ intyear2
			+ "-"
			+ intMonth2
			+ "-01','yyyy-mm-dd'),-12)))\n"
			+ "     ) dcid,(select decode(1,1,'累计') as fenx,1 as xuh  from dual " +
					") fx,diancxxb dc"
			+ dianckjmx_bm
			+ "\n"
			+ "     where dc.id=dcid.diancxxb_id  "
			+ dianckjmx_tj
			+ strGongsID
			+ " ) fx,\n"+
			"(select decode(1,1,'累计') as fenx,shcy.diancxxb_id as diancxxb_id,\n" + 
			shouhcysl_bq+   " \n" +
			"       from  "+yb_shcy+" shcy,diancxxb dc\n" + 
			"       where shcy.diancxxb_id=dc.id and shcy.riq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" + 
			"              and shcy.riq<=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n"+
			"             and shcy.fenx='本月' "+strGongsID+zhuangt+"\n" + 
			"       group by (shcy.diancxxb_id)\n" + 
			") sm,\n" + 
			"(select decode(1,1,'累计') as fenx,shcy.diancxxb_id as diancxxb_id,\n" + 
			shouhcysl_tq+   " \n" +
			"       from  "+yb_shcy+" shcy,diancxxb dc\n" + 
			"       where shcy.diancxxb_id=dc.id and shcy.riq>=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" + 
			"        and shcy.riq<=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)\n"+
			"             and shcy.fenx='本月' "+strGongsID+zhuangt+"\n" + 
			"       group by (shcy.diancxxb_id)\n" + 
			") smtq,\n" + 
//------------------------------------累计库存显示本月----------------------------------------------
			"(select decode(1,1,'累计') as fenx,shcy.diancxxb_id as diancxxb_id,\n" + 
			shouhcykc_bq+" \n" +
			"       from  "+yb_shcy+" shcy,diancxxb dc\n" + 
			"       where shcy.diancxxb_id=dc.id and shcy.riq=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n" + 
			"             and shcy.fenx='本月'   "+strGongsID+zhuangt+"\n" + 
			"       group by (shcy.diancxxb_id)\n" + 
			") smkc,\n" + 
			"(select decode(1,1,'累计') as fenx,shcy.diancxxb_id as diancxxb_id,\n" +
			shouhcykc_tq+" \n" +
			"       from  "+yb_shcy+" shcy,diancxxb dc\n" + 
			"       where shcy.diancxxb_id=dc.id and shcy.riq=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)\n" + 
			"             and shcy.fenx='本月' "+strGongsID+zhuangt+"\n" + 
			"       group by (shcy.diancxxb_id)\n" + 
			") smkctq,"+
//----------------------------------------------------------------------------------
			dianc + 
			"where dc.id=fx.diancxxb_id "+tiaoj+"\n" +
			"and fx.diancxxb_id=smkc.diancxxb_id(+)\n" +
			"and fx.fenx=smkc.fenx(+)\n" + 
			"and fx.diancxxb_id=smkctq.diancxxb_id(+)\n" + 
			"and fx.fenx=smkctq.fenx(+)"+
			"  and fx.diancxxb_id=sm.diancxxb_id(+)\n" + 
			"  and fx.fenx=sm.fenx(+)\n" + 
			"  and fx.diancxxb_id=smtq.diancxxb_id(+)\n" + 
			"  and fx.fenx=smtq.fenx(+)\n" + fenz;
			
		}

		return strSQL;

	}
		
	/**
	 * 
	 * 燃料管理08表:标煤耗用情况表
	 * 
	 * @param biaot
	 * @param dianc
	 * @param tiaoj
	 * @param fenz
	 * @param dianckjmx_bm
	 * @param dianckjmx_tj
	 * @param strGongsID
	 * @param zhuangt
	 * @param intyear
	 * @param intMonth
	 * @param intyear2
	 * @param intMonth2
	 * @return
	 */
	public String getSql8(String biaot, String dianc, String tiaoj,
			String fenz, String dianckjmx_bm, String dianckjmx_tj,String zhibbsl,
			String strGongsID, String zhuangt,String yb_zb, long intyear, long intMonth,
			long intyear2, long intMonth2) {
		    String strSQL ="";
		if(intyear2==intyear&&intMonth==1){
			strSQL = 
			    "select "+biaot+"\n"
				+  "from\n"
				/*+ "     (select distinct diancxxb_id from yuezbb\n"
				+ "             where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')))\n"
				+ "     ) dcid,(select decode(1,1,'本月','') as fenx,1 as xuh  from dual union select decode(1,1,'累计','')  as fenx,2 as xhu from dual ) fx,diancxxb dc"+dianckjmx_bm+"\n" 
				+ "     where dc.id=dcid.diancxxb_id  "+dianckjmx_tj+strGongsID+" ) fx,\n"*/
				+ "   (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n"
				+ "     (select distinct diancxxb_id from  "+yb_zb+"\n"
				+ " where (riq=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd') or riq=last_year_today(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')))) dcid,\n"
				+ " (select decode(1,1,'累计','') as fenx,1 as xuh  from dual " 
				+		") fx,diancxxb dc"
				+ dianckjmx_bm
				+ "\n"
				+ "     where dc.id=dcid.diancxxb_id \n"
				+ dianckjmx_tj
				+ strGongsID
				+ " ) fx,\n"+
				" ((select  decode (1,1,'累计','') as fenx,y.diancxxb_id,\n"+
				zhibbsl+  "\n"
				/*"(sum(y.RULMZBZML) + sum(y.RULYZBZML)) as quanbhybml,sum(y.FADMZBML) as fadhmzbml,\n"+
		 		"sum(y.GONGRMZBML) as gongrhmzbml,sum(y.FADYZBZML) as fadhyzbml,\n"+
		 		"sum(y.GONGRYZBZML) as gongrhyzbml,sum(y.FADL) as fadl,\n"+
		 		"decode(sum(y.FADL),0,0,Round(((sum(y.FADMZBML) + sum(y.FADYZBZML)) /sum(y.FADL) * 100),2)) as fadbmhl,sum(y.GONGRL) as gongrl,\n"+
		 		"decode(sum(y.GONGRL),0,0,Round(((sum(y.GONGRMZBML) + sum(y.GONGRYZBZML)) /sum(y.GONGRL) * 1000),2)) as gongrbmhl"*/
				+"    from  "+yb_zb+" y,diancxxb dc \n"
				+ "    where y.riq=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n"
				+ "    and y.fenx='累计' "+zhuangt+"  and y.diancxxb_id=dc.id \n"
				+ "    group by (y.diancxxb_id))) bq,\n"
				+ "((select  decode (1,1,'累计','') as fenx,y.diancxxb_id,\n"+
				zhibbsl+  "\n"
				/*"(sum(y.RULMZBZML) + sum(y.RULYZBZML)) as quanbhybml,sum(y.FADMZBML) as fadhmzbml,\n"+
		 		"sum(y.GONGRMZBML) as gongrhmzbml,sum(y.FADYZBZML) as fadhyzbml,\n"+
		 		"sum(y.GONGRYZBZML) as gongrhyzbml,sum(y.FADL) as fadl,\n"+
		 		"decode(sum(y.FADL),0,0,Round(((sum(y.FADMZBML) + sum(y.FADYZBZML)) /sum(y.FADL) * 100),2)) as fadbmhl,sum(y.GONGRL) as gongrl,\n"+
		 		"decode(sum(y.GONGRL),0,0,Round(((sum(y.GONGRMZBML) + sum(y.GONGRYZBZML)) /sum(y.GONGRL) * 1000),2)) as gongrbmhl"*/
				+"    from  "+yb_zb+" y,diancxxb dc\n"
				+ "    where y.riq=last_year_today(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'))\n"
				+ "    and y.fenx='累计' "+zhuangt+" and y.diancxxb_id=dc.id \n"
				+ "    group by (y.diancxxb_id))) tq,\n"
				+ dianc 
				+ "where fx.diancxxb_id=dc.id(+) "+tiaoj+"\n"
				+ "and fx.diancxxb_id=bq.diancxxb_id(+)\n"
				+ "and fx.diancxxb_id=tq.diancxxb_id(+)\n"
				+ "and fx.fenx=bq.fenx(+)\n"
				+ "and fx.fenx=tq.fenx(+) "+strGongsID+"\n"
				+ fenz;
			
		}else{
			strSQL = 
			    "select "+biaot+"\n"
				+  "from\n"
				/*+ "   (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n"
				+ "     (select distinct diancxxb_id from yuezbb\n"
				+ "             where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')))\n"
				+ "     ) dcid,(select decode(1,1,'本月','') as fenx,1 as xuh  from dual union select decode(1,1,'累计','')  as fenx,2 as xhu from dual ) fx,diancxxb dc"+dianckjmx_bm+"\n" 
				+ "     where dc.id=dcid.diancxxb_id  "+dianckjmx_tj+strGongsID+" ) fx,\n" */
				+ " (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n"
				+ "     (select distinct diancxxb_id from  "+yb_zb+"\n"
				+ "             where  ((riq>=to_date('"
				+ intyear
				+ "-"
				+ intMonth
				+ "-01','yyyy-mm-dd') and riq<=to_date('"
				+ intyear2
				+ "-"
				+ intMonth2
				+ "-01','yyyy-mm-dd')) or (riq>=last_year_today(to_date('"
				+ intyear
				+ "-"
				+ intMonth
				+ "-01','yyyy-mm-dd')) and riq<=last_year_today(to_date('"
				+ intyear2
				+ "-"
				+ intMonth2
				+ "-01','yyyy-mm-dd'))))\n"
				+ "     ) dcid,(select decode(1,1,'累计','') as fenx,1 as xuh  from dual " +
						") fx,diancxxb dc"
				+ dianckjmx_bm
				+ "\n"
				+ "     where dc.id=dcid.diancxxb_id  "
				+ dianckjmx_tj
				+ strGongsID
				+ " ) fx,\n"
				+ "   (( select  decode (1,1,'累计','') as fenx,y.diancxxb_id,\n"
				+zhibbsl+  "\n"
				/*+ "(sum(y.RULMZBZML)+sum(y.RULYZBZML)) as quanbhybml,\n"
				+ "    sum(y.FADMZBML) as fadhmzbml,sum(y.GONGRMZBML) as gongrhmzbml,sum(y.FADYZBZML) as fadhyzbml,\n"
				+ "    sum(y.GONGRYZBZML) as gongrhyzbml,sum(y.FADL) as fadl,\n"
				+ "    decode(sum(y.FADL),0,0,Round(((sum(y.FADMZBML)+sum(y.FADYZBZML))/sum(y.FADL)*100),2)) as fadbmhl,\n"
				+ "    sum(y.GONGRL) as gongrl,\n"
				+ "    decode(sum(y.GONGRL),0,0,Round(((sum(y.GONGRMZBML)+sum(y.GONGRYZBZML))/sum(y.GONGRL)*1000),2)) as gongrbmhl\n"*/
				+ "    from  "+yb_zb+" y,diancxxb dc\n"
				+ "    where y.riq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and y.riq<=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n"
				+ "    and y.fenx='本月' "+zhuangt+" and y.diancxxb_id=dc.id \n"
				+ "    group by (y.diancxxb_id))\n"
				+ ") bq,\n"
				+ "   ((select  decode (1,1,'累计','') as fenx,y.diancxxb_id,\n"
				+zhibbsl+  "\n"
				/*+ "(sum(y.RULMZBZML)+sum(y.RULYZBZML)) as quanbhybml,\n"
				+ "    sum(y.FADMZBML) as fadhmzbml,sum(y.GONGRMZBML) as gongrhmzbml,sum(y.FADYZBZML) as fadhyzbml,\n"
				+ "    sum(y.GONGRYZBZML) as gongrhyzbml,sum(y.FADL) as fadl,\n"
				+ "    decode(sum(y.FADL),0,0,Round(((sum(y.FADMZBML)+sum(y.FADYZBZML))/sum(y.FADL)*100),2)) as fadbmhl,\n"
				+ "    sum(y.GONGRL) as gongrl,\n"
				+ "    decode(sum(y.GONGRL),0,0,Round(((sum(y.GONGRMZBML)+sum(y.GONGRYZBZML))/sum(y.GONGRL)*1000),2)) as gongrbmhl\n"*/
				+ "    from  "+yb_zb+" y,diancxxb dc\n"
				+ "    where y.riq>=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')) and y.riq<=last_year_today(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'))\n"
				+ "    and y.fenx='本月' "+zhuangt+" and y.diancxxb_id=dc.id \n"
				+ "    group by (y.diancxxb_id))\n"
				+ ") tq,\n"
				+ dianc 
				+ " where fx.diancxxb_id=dc.id(+) "+tiaoj+"\n"
				+ "and fx.diancxxb_id=bq.diancxxb_id(+)\n"
				+ "and fx.diancxxb_id=tq.diancxxb_id(+)\n"
				+ "and fx.fenx=bq.fenx(+)\n"
				+ "and fx.fenx=tq.fenx(+) "+strGongsID+"\n"
				+ fenz;
		}
		return strSQL;

	}

	/**
	 * 
	 * 燃料管理09表:煤油质量情况表
	 * 
	 * @param biaot
	 * @param dianc
	 * @param tiaoj
	 * @param fenz
	 * @param dianckjmx_bm
	 * @param dianckjmx_tj
	 * @param strGongsID
	 * @param shulzt
	 * @param zhilzt
	 * @param zhuangt
	 * @param intyear
	 * @param intMonth
	 * @param intyear2
	 * @param intMonth2
	 * @return
	 */
	public String getSql9(String biaot, String dianc, String tiaoj,String grouping,
			String fenz, String dianckjmx_bm, String dianckjmx_tj,String shul,String strZhib_bq,String strZhib_tq,
			String strGongsID, String shulzt, String zhilzt, String zhuangt,String yb_sl,String yb_zl,String yb_zb,String yb_rcy,
			long intyear, long intMonth, long intyear2, long intMonth2) {
		String strSQL="";
		if(intyear2==intyear&&intMonth==1){
			strSQL=
				 "select  "+biaot+",\n" +
				 "  decode(1,1,fx.fenx,'') as fenx,\n" + 
				 "                   decode(sum(bq.laimsl),0,0,round(sum(bq.rc_farl*bq.laimsl)/sum(bq.laimsl),2)*1000) as ruc_frl_bq ,\n" + 
				 "                   decode(sum(tq.laimsl),0,0,round(sum(tq.rc_farl*tq.laimsl)/sum(tq.laimsl),2)*1000) as ruc_frl_tq,\n" + 
				 "                   decode(sum(bq.laimsl_zd),0,0,round(sum(bq.rc_farl_zd*bq.laimsl_zd)/sum(bq.laimsl_zd),2)*1000) as ruc_frl_zd_bq ,\n" + 
				 "                   decode(sum(tq.laimsl_zd),0,0,round(sum(tq.rc_farl_zd*tq.laimsl_zd)/sum(tq.laimsl_zd),2)*1000) as ruc_frl_zd_tq,\n" + 
				 "                   decode(sum(bq.laimsl_sc),0,0,round(sum(bq.rc_farl_sc*bq.laimsl_sc)/sum(bq.laimsl_sc),2)*1000) as ruc_frl_sc_bq ,\n" + 
				 "                   decode(sum(tq.laimsl_sc),0,0,round(sum(tq.rc_farl_sc*tq.laimsl_sc)/sum(tq.laimsl_sc),2)*1000) as ruc_frl_sc_tq,\n" + 
				 "                   decode(sum(bq.rulml),0,0,round(sum(bq.rul_farl*bq.rulml)/sum(bq.rulml),2)*1000) as rul_frl_bq,\n" + 
				 "                   decode(sum(tq.rulml),0,0,round(sum(tq.rul_farl*tq.rulml)/sum(tq.rulml),2)*1000) as rul_frl_tq,\n" + 
				 "                   decode(decode(sum(bq.laimsl),0,0,round(sum(bq.rc_farl*bq.laimsl)/sum(bq.laimsl),2)*1000),0,0,(decode(sum(bq.laimsl),0,0,round(sum(bq.rc_farl*bq.laimsl)/sum(bq.laimsl),2))-decode(sum(bq.rulml),0,0,round(sum(bq.rul_farl*bq.rulml)/sum(bq.rulml),2)))*1000) as rezc_bq,\n" + 
				 "                   decode(decode(sum(tq.laimsl),0,0,round(sum(tq.rc_farl*tq.laimsl)/sum(tq.laimsl),2)*1000),0,0,(decode(sum(tq.laimsl),0,0,round(sum(tq.rc_farl*tq.laimsl)/sum(tq.laimsl),2))-decode(sum(tq.rulml),0,0,round(sum(tq.rul_farl*tq.rulml)/sum(tq.rulml),2)))*1000) as rezc_tq,\n" + 
				 "                   decode(sum(bq.rulyl),0,0,round(sum(bq.sy_farl*bq.rulyl)/sum(bq.rulyl),2)*1000) as sy_farl_bq,\n" + 
				 "                   decode(sum(tq.rulyl),0,0,round(sum(tq.sy_farl*tq.rulyl)/sum(tq.rulyl),2)*1000) as sy_farl_tq,\n" + 
				 "                   grouping(fx.fenx) f ,grouping(dc.mingc) dcmc \n"+grouping+"\n" + 
				 "      from\n" + 
				 /*"       ( select yid.diancxxb_id,fx.fenx,fx.xuh from\n" + 
				 "                   (select distinct diancxxb_id from  "+yb_sl+" sl,yuezlb zl,yuetjkjb tj\n" + 
				 "                       where sl.yuetjkjb_id=tj.id and sl.yuetjkjb_id=zl.yuetjkjb_id(+) and (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))\n" + 
				 "                    union\n" + 
				 "                     select distinct diancxxb_id from yuezbb\n" + 
				 "                        where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))\n" + 
				 "                    ) yid,\n" + 
				 "               (select decode(1,1,'本月') as fenx,1 as xuh  from dual union select decode(1,1,'累计') as fenx,2 as xhu from dual ) fx,diancxxb dc"+dianckjmx_bm+"\n" + 
				 "               where dc.id=yid.diancxxb_id   "+dianckjmx_tj+strGongsID+"   ) fx,\n" +*/
				 "       ( select yid.diancxxb_id,fx.fenx,fx.xuh from\n" + 
				 "                   (select distinct diancxxb_id from  "+yb_sl+" sl, "+yb_zl+" zl,yuetjkjb tj\n" + 
				 "                       where sl.yuetjkjb_id=tj.id and sl.yuetjkjb_id=zl.yuetjkjb_id(+) \n" +
              "                        and (riq=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12))\n" + 				 
				 "                    union\n" + 
				 "                     select distinct diancxxb_id from  "+yb_zb+"\n" + 
				 "                        where  (riq=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12))\n" + 				 
				 ") yid,\n" +
				 "               (select decode(1,1,'累计') as fenx,1 as xuh  from dual " +
				 ") fx,diancxxb dc"+dianckjmx_bm+"\n" + 
				 "               where dc.id=yid.diancxxb_id   "+dianckjmx_tj+strGongsID+"   ) fx,\n" + 
				 "((select  decode(1,1,'累计') as fenx,dc.id as diancxxb_id,\n" + 
				 "                                          r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
				 "              from (select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
				 "                        (select tj.diancxxb_id as diancxxb_id,"+shul+"\n" + 
				 "                                                 from  "+yb_sl+" sl, yuetjkjb tj,  "+yb_zl+" zl,diancxxb dc\n" + 
				 "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id "+strGongsID+"\n" + 
				 "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  "+shulzt+zhilzt+"\n" + 
				 "                                                 group by (tj.diancxxb_id) )a,\n" + 
				 "                        (select tj.diancxxb_id as diancxxb_id,"+shul+"\n" + 
				 "                                                 from  "+yb_sl+" sl, yuetjkjb tj,  "+yb_zl+" zl,diancxxb dc\n" + 
				 "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id "+strGongsID+"\n" + 
				 "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  "+shulzt+zhilzt+" and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
				 "                                                 group by (tj.diancxxb_id) )b,\n" + 
				 "                        (select tj.diancxxb_id as diancxxb_id,"+shul+"\n" + 
				 "                                                 from  "+yb_sl+" sl, yuetjkjb tj,  "+yb_zl+" zl,diancxxb dc\n" + 
				 "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id "+strGongsID+"\n" + 
				 "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  "+shulzt+zhilzt+" and  tj.jihkjb_id=2\n" + 
				 "                                                 group by (tj.diancxxb_id) )c\n" + 
				 "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r,\n" + 
				 "("+strZhib_bq+")  y,vwdianc dc\n" + 
				 "               where R.DIANCXXB_ID(+)=dc.id and  Y.DIANCXXB_ID(+) =dc.id "+strGongsID+" )\n" + 
				 ")bq,\n" + 
				 "(select  decode(1,1,'累计') as fenx,dc.id as diancxxb_id,\n" + 
				 "                        r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
				 "              from ( select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
				 "                        (select tj.diancxxb_id as diancxxb_id,"+shul+"\n" +
				 "                                                 from  "+yb_sl+" sl, yuetjkjb tj,  "+yb_zl+" zl,diancxxb dc\n" + 
				 "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id "+strGongsID+"\n" + 
				 "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  "+shulzt+zhilzt+"\n" + 
				 "                                                 group by (tj.diancxxb_id) )a,\n" + 
				 "                        (select tj.diancxxb_id as diancxxb_id,"+shul+"\n" +
				 "                                                 from  "+yb_sl+" sl, yuetjkjb tj,  "+yb_zl+" zl,diancxxb dc\n" + 
				 "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id "+strGongsID+"\n" + 
				 "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  "+shulzt+zhilzt+" and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
				 "                                                 group by (tj.diancxxb_id) )b,\n" +  
				 "                        (select tj.diancxxb_id as diancxxb_id,"+shul+"\n" +
				 "                                                 from  "+yb_sl+" sl, yuetjkjb tj,  "+yb_zl+" zl,diancxxb dc\n" + 
				 "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id "+strGongsID+"\n" + 
				 "                                                       and sl.fenx = '累计' and zl.fenx(+)=sl.fenx  "+shulzt+zhilzt+" and  tj.jihkjb_id=2\n" + 
				 "                                                 group by (tj.diancxxb_id) )c\n" + 
				 "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r,\n" + 
				 "("+strZhib_tq+")  y,vwdianc dc\n" + 
				 "               where R.DIANCXXB_ID(+)=dc.id and  Y.DIANCXXB_ID(+) =dc.id "+strGongsID+" \n" + 
			 	 ")tq, \n" + dianc+
				 "          where dc.id=fx.diancxxb_id "+tiaoj+"\n" + 
				 "                 and  fx.diancxxb_id=tq.diancxxb_id(+)\n" + 
				 "                 and  fx.diancxxb_id=bq.diancxxb_id(+)\n" + 
				 "                 and fx.fenx=tq.fenx(+)\n" + 
				 "                 and fx.fenx=bq.fenx(+)\n" +  fenz;
			
		}else{
			strSQL="select  "+biaot+",\n" +
				 "  decode(1,1,fx.fenx,'') as fenx,\n" + 
				 "                   decode(sum(bq.laimsl),0,0,round(sum(bq.rc_farl*bq.laimsl)/sum(bq.laimsl),2)*1000) as ruc_frl_bq ,\n" + 
				 "                   decode(sum(tq.laimsl),0,0,round(sum(tq.rc_farl*tq.laimsl)/sum(tq.laimsl),2)*1000) as ruc_frl_tq,\n" + 
				 "                   decode(sum(bq.laimsl_zd),0,0,round(sum(bq.rc_farl_zd*bq.laimsl_zd)/sum(bq.laimsl_zd),2)*1000) as ruc_frl_zd_bq ,\n" + 
				 "                   decode(sum(tq.laimsl_zd),0,0,round(sum(tq.rc_farl_zd*tq.laimsl_zd)/sum(tq.laimsl_zd),2)*1000) as ruc_frl_zd_tq,\n" + 
				 "                   decode(sum(bq.laimsl_sc),0,0,round(sum(bq.rc_farl_sc*bq.laimsl_sc)/sum(bq.laimsl_sc),2)*1000) as ruc_frl_sc_bq ,\n" + 
				 "                   decode(sum(tq.laimsl_sc),0,0,round(sum(tq.rc_farl_sc*tq.laimsl_sc)/sum(tq.laimsl_sc),2)*1000) as ruc_frl_sc_tq,\n" + 
				 "                   decode(sum(bq.rulml),0,0,round(sum(bq.rul_farl*bq.rulml)/sum(bq.rulml),2)*1000) as rul_frl_bq,\n" + 
				 "                   decode(sum(tq.rulml),0,0,round(sum(tq.rul_farl*tq.rulml)/sum(tq.rulml),2)*1000) as rul_frl_tq,\n" + 
				 "                   decode(decode(sum(bq.laimsl),0,0,round(sum(bq.rc_farl*bq.laimsl)/sum(bq.laimsl),2)*1000),0,0,(decode(sum(bq.laimsl),0,0,round(sum(bq.rc_farl*bq.laimsl)/sum(bq.laimsl),2))-decode(sum(bq.rulml),0,0,round(sum(bq.rul_farl*bq.rulml)/sum(bq.rulml),2)))*1000) as rezc_bq,\n" + 
				 "                   decode(decode(sum(tq.laimsl),0,0,round(sum(tq.rc_farl*tq.laimsl)/sum(tq.laimsl),2)*1000),0,0,(decode(sum(tq.laimsl),0,0,round(sum(tq.rc_farl*tq.laimsl)/sum(tq.laimsl),2))-decode(sum(tq.rulml),0,0,round(sum(tq.rul_farl*tq.rulml)/sum(tq.rulml),2)))*1000) as rezc_tq,\n" + 
				 "                   decode(sum(bq.rulyl),0,0,round(sum(bq.sy_farl*bq.rulyl)/sum(bq.rulyl),2)*1000) as sy_farl_bq,\n" + 
				 "                   decode(sum(tq.rulyl),0,0,round(sum(tq.sy_farl*tq.rulyl)/sum(tq.rulyl),2)*1000) as sy_farl_tq,\n" + 
				 "                   grouping(fx.fenx) f ,grouping(dc.mingc) dcmc \n"+grouping+"\n" + 
				 "      from\n" + 
				 /*"       ( select yid.diancxxb_id,fx.fenx,fx.xuh from\n" + 
				 "                   (select distinct diancxxb_id from  "+yb_sl+" sl,yuezlb zl,yuetjkjb tj\n" + 
				 "                       where sl.yuetjkjb_id=tj.id and sl.yuetjkjb_id=zl.yuetjkjb_id(+) and (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))\n" + 
				 "                    union\n" + 
				 "                     select distinct diancxxb_id from yuezbb\n" + 
				 "                        where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))\n" + 
				 "                    ) yid,\n" + 
				 "               (select decode(1,1,'本月') as fenx,1 as xuh  from dual union select decode(1,1,'累计') as fenx,2 as xhu from dual ) fx,diancxxb dc"+dianckjmx_bm+"\n" + 
				 "               where dc.id=yid.diancxxb_id   "+dianckjmx_tj+strGongsID+"   ) fx,\n" + */
				 "       ( select yid.diancxxb_id,fx.fenx,fx.xuh from\n" + 
				 "                   (select distinct diancxxb_id from  "+yb_sl+" sl, "+yb_zl+" zl,yuetjkjb tj\n" + 
				 "                       where sl.yuetjkjb_id=tj.id and sl.yuetjkjb_id=zl.yuetjkjb_id(+) \n" +
				 "                   and (riq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and riq<=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd') or " +
				 		"                (riq>=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and riq<=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)))\n" + 
				 "                    union\n" + 
				 "                     select distinct diancxxb_id from  "+yb_zb+"\n" + 
				 //"                        where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))\n" + 	
				 "                        where  (riq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and riq<=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd') or " +
				 		"                (riq>=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and riq<=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)))\n" + 
				 ") yid,\n" +
				 "(select decode(1,1,'累计') as fenx,1 as xuh  from dual " +
				 ") fx,diancxxb dc"+dianckjmx_bm+"\n" + 
				 "               where dc.id=yid.diancxxb_id   "+dianckjmx_tj+strGongsID+"   ) fx,\n" +  
				 "(select  decode(1,1,'累计') as fenx,dc.id as diancxxb_id,\n" + 
				 "                    r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
				 "              from (select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" +
//					---------------------------------判断电厂id为141(芜湖电厂)，入厂低位热值取yuezlb中的diancrz字段，其它电厂入厂低位热值取yuezlb中的qnet_ar字段------------------
				 "                        (select tj.diancxxb_id as diancxxb_id,"+shul+"\n" + 
				 "                                                 from  "+yb_sl+" sl, yuetjkjb tj,  "+yb_zl+" zl,diancxxb dc\n" + 
				 "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id " +
				 "       and tj.riq >= to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and tj.riq<=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')" +
				 "       and tj.diancxxb_id = dc.id "+strGongsID+"\n" + 
				 "                                                       and sl.fenx = '本月' and zl.fenx(+)=sl.fenx  "+shulzt+zhilzt+"\n" + 
				 "                                                 group by (tj.diancxxb_id) )a,\n" + 
//					---------------------------------判断电厂id为141(芜湖电厂)，入厂低位热值取yuezlb中的diancrz字段，其它电厂入厂低位热值取yuezlb中的qnet_ar字段------------------
				 "                        (select tj.diancxxb_id as diancxxb_id,"+shul+"\n" + 
				 "                                                 from  "+yb_sl+" sl, yuetjkjb tj,  "+yb_zl+" zl,diancxxb dc\n" + 
				 "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id " +
				 "       and tj.riq >= to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and tj.riq<=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')" +
				 "       and tj.diancxxb_id = dc.id "+strGongsID+"\n" + 
				 "                                                       and sl.fenx = '本月' and zl.fenx(+)=sl.fenx  "+shulzt+zhilzt+" and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
				 "                                                 group by (tj.diancxxb_id) )b,\n" + 
//					---------------------------------判断电厂id为141(芜湖电厂)，入厂低位热值取yuezlb中的diancrz字段，其它电厂入厂低位热值取yuezlb中的qnet_ar字段------------------
				 "                        (select tj.diancxxb_id as diancxxb_id,"+shul+"\n" + 
				 "                                                 from  "+yb_sl+" sl, yuetjkjb tj,  "+yb_zl+" zl,diancxxb dc\n" + 
				 "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id " +
				 "      and tj.riq >= to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and tj.riq<=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')" +
				 "      and tj.diancxxb_id = dc.id "+strGongsID+"\n" + 
				 "      and sl.fenx = '本月' and zl.fenx(+)=sl.fenx  "+shulzt+zhilzt+" and  tj.jihkjb_id=2\n" + 
				 "       group by (tj.diancxxb_id) )c\n" + 
				 "      where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r,\n" + 
				 "("+strZhib_bq+")  y,vwdianc dc\n" + 
				 " where R.DIANCXXB_ID(+)=dc.id and  Y.DIANCXXB_ID(+) =dc.id "+strGongsID+"\n" +
				 ")bq,\n" + 
				 "(select  decode(1,1,'累计') as fenx,dc.id as diancxxb_id,\n" + 
				 "                  r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
				 "              from (\n" + 
				 "                   select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
				 "                        (select tj.diancxxb_id as diancxxb_id,"+shul+"\n" +
				 "                                                 from  "+yb_sl+" sl, yuetjkjb tj,  "+yb_zl+" zl,diancxxb dc\n" + 
				 "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id " +
				 "and tj.riq >= add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and tj.riq <= add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)" +
				 		"and tj.diancxxb_id = dc.id "+strGongsID+"\n" + 
				 "                                                       and sl.fenx = '本月' and zl.fenx(+)=sl.fenx  "+shulzt+zhilzt+"\n" + 
				 "                                                 group by (tj.diancxxb_id) )a,\n" + 
				 "                        (select tj.diancxxb_id as diancxxb_id,"+shul+"\n" + 
				 "                                                 from  "+yb_sl+" sl, yuetjkjb tj,  "+yb_zl+" zl,diancxxb dc\n" + 
				 "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id " +
				 "and tj.riq >= add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and tj.riq <= add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)" +
				 		"and tj.diancxxb_id = dc.id "+strGongsID+"\n" + 
				 "                                                       and sl.fenx = '本月' and zl.fenx(+)=sl.fenx  "+shulzt+zhilzt+" and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
				 "                                                 group by (tj.diancxxb_id) )b,\n" + 
				 "                        (select tj.diancxxb_id as diancxxb_id,"+shul+"\n" +
				 "                                                 from  "+yb_sl+" sl, yuetjkjb tj,  "+yb_zl+" zl,diancxxb dc\n" + 
				 "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id " +
				 "and tj.riq >= add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and tj.riq <= add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)" +
				 		"and tj.diancxxb_id = dc.id "+strGongsID+"\n" + 
				 "                                                       and sl.fenx = '本月' and zl.fenx(+)=sl.fenx  "+shulzt+zhilzt+" and  tj.jihkjb_id=2\n" + 
				 "                                                 group by (tj.diancxxb_id) )c\n" + 
				 "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r\n" + 
				 " ,("+strZhib_tq+")  y,vwdianc dc\n" + 
				 "               where R.DIANCXXB_ID(+)=dc.id and  Y.DIANCXXB_ID(+) =dc.id "+strGongsID+" \n" +
			 	 ")tq, \n" + dianc+
				 "          where dc.id=fx.diancxxb_id "+tiaoj+"\n" + 
				 "                 and  fx.diancxxb_id=tq.diancxxb_id(+)\n" + 
				 "                 and  fx.diancxxb_id=bq.diancxxb_id(+)\n" + 
				 "                 and fx.fenx=tq.fenx(+)\n" + 
				 "                 and fx.fenx=bq.fenx(+)\n" +  fenz;
        }
		
		return strSQL;
	}
	
	/**
	 * 
	 * 燃料管理10_1表:入厂煤(电厂、口径统计)价格情况表
	 * @param biaot
	 * @param dianc
	 * @param tiaoj
	 * @param shulzt
	 * @param zhilzt
	 * @param youzt
	 * @param mc
	 * @param fenz
	 * @param dianckjmx_bm
	 * @param dianckjmx_tj
	 * @param strGongsID
	 * @param zhuangt
	 * @param intyear
	 * @param intMonth
	 * @param intyear2
	 * @param intMonth2
	 * @return
	 */
	public String getSql10_1(String biaot,String biaot0,String fenz_bt,String strzt,String shul,String strYouzb, String dianc, String tiaoj,String shulzt,String zhilzt,String youzt,String mc,
			String fenz, String dianckjmx_bm, String dianckjmx_tj,String where,
			String strGongsID, String zhuangt,String yb_sl,String yb_zl,String yb_dj,String yb_rcy, long intyear, long intMonth,
			long intyear2, long intMonth2) {
		String strSQL="";
		if(intyear2==intyear&&intMonth==1){
			strSQL=biaot0+strzt+"\n"  
			+ " from ( select rownum as xuh,c.* from(select "+fenz_bt+biaot+" fx.fenx as fenx,\n"
			+ "      Round(decode(sum(bq.laimsl),0,0,sum(bq.daoczhj*bq.laimsl)/sum(bq.laimsl)),2) as bq_daoczhj ,\n"
			+ "      Round(decode(sum(tq.laimsl),0,0,sum(tq.daoczhj*tq.laimsl)/sum(tq.laimsl)),2) as tq_daoczhj  ,\n"
			+ "      Round(decode(sum(bq.laimsl),0,0,sum(bq.chebj*bq.laimsl)/sum(bq.laimsl)),2) as bq_chebj,\n"
			+ "      Round(decode(sum(tq.laimsl),0,0,sum(tq.chebj*tq.laimsl)/sum(tq.laimsl)),2) as tq_chebj,\n"
			+ "      Round(decode(sum(bq.laimsl),0,0,sum(bq.yunj*bq.laimsl)/sum(bq.laimsl)),2) as bq_yunj,\n"
			+ "      Round(decode(sum(tq.laimsl),0,0,sum(tq.yunj*tq.laimsl)/sum(tq.laimsl)),2) as tq_yunj,\n"
			+ "      Round(decode(sum(bq.laimsl),0,0,sum(bq.zaf*bq.laimsl)/sum(bq.laimsl)),2) as bq_zaf,\n"
			+ "      Round(decode(sum(tq.laimsl),0,0,sum(tq.zaf*tq.laimsl)/sum(tq.laimsl)),2) as tq_zaf,\n"
			+ "		 Round(decode(sum(bq.farl*bq.laimsl),0,0,round(sum(bq.buhs_daoczhj*bq.laimsl)/sum(bq.laimsl),2)*29.271/round(sum(bq.farl*bq.laimsl)/sum(bq.laimsl),2)),2)  as bq_rucbmdj,\n" 
			+ "      Round(decode(sum(tq.farl*tq.laimsl),0,0,round(sum(tq.buhs_daoczhj*tq.laimsl)/sum(tq.laimsl),2)*29.271/round(sum(tq.farl*tq.laimsl)/sum(tq.laimsl),2)),2)  as tq_rucbmdj\n"
			+ " from\n"
			/*+ "     (select distinct dcid.diancxxb_id,kouj,fx.fenx,fx.xuh from\n"
			+ "             (select distinct diancxxb_id,decode(kj.jihkjb_id,3,1,kj.jihkjb_id) as kouj\n"
			+ "                     from yuercbmdj y,yuetjkjb kj\n"
			+ "                     where y.yuetjkjb_id=kj.id\n"
			+ "                     and (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n"
			+ "                     or riq=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'))) ) dcid," +
					"vwfenxYue fx," +
					"diancxxb dc"+dianckjmx_bm+" \n" 
			+ "               where dc.id=dcid.diancxxb_id   "+dianckjmx_tj+strGongsID+"   ) fx,\n"*/
			+ "     (select distinct dcid.diancxxb_id,kouj,fx.fenx,fx.xuh from\n"
			+ "             (select distinct diancxxb_id,decode(kj.jihkjb_id,3,1,kj.jihkjb_id) as kouj\n"
			+ "                     from  "+yb_dj+" y,yuetjkjb kj\n"
			+ "                     where y.yuetjkjb_id=kj.id\n"
			+ "                     and (riq=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n"
			+ "                     or riq=last_year_today(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'))\n"
			+		") ) dcid," +
			"		(select decode(1, 1, '累计', '') as fenx, 1 as xuh from dual) fx,\n"+
					"diancxxb dc"+dianckjmx_bm+" \n" 
			+ "               where dc.id=dcid.diancxxb_id   "+dianckjmx_tj+strGongsID+"   ) fx,\n" 
			+ "     (select y.fenx,kj.diancxxb_id,decode(kj.jihkjb_id,3,1,kj.jihkjb_id) as kouj," +shul+ "\n"
			+ "      from  "+yb_dj+" y,yuetjkjb kj, "+yb_sl+" sl, "+yb_zl+" zl,diancxxb dc\n"
			+ "           where kj.riq=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd') \n"
			+ "           and y.yuetjkjb_id=kj.id and sl.yuetjkjb_id=kj.id and zl.yuetjkjb_id=kj.id and kj.diancxxb_id=dc.id\n"
			+ "           and y.fenx=sl.fenx(+) and y.fenx=zl.fenx(+) "+zhuangt+shulzt+zhilzt+" \n"
			+ "           and y.fenx='累计' and sl.fenx='累计' and zl.fenx='累计'             \n"
			+ "           group by (kj.diancxxb_id,decode(kj.jihkjb_id,3,1,kj.jihkjb_id),y.fenx)) bq,\n"
			
			+ "     (select y.fenx,kj.diancxxb_id,decode(kj.jihkjb_id,3,1,kj.jihkjb_id) as kouj," +shul+ "\n"
			+ "      from  "+yb_dj+" y,yuetjkjb kj, "+yb_sl+" sl, "+yb_zl+" zl,diancxxb dc\n"
			+ "      	where kj.riq=last_year_today(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')) \n"
			+ "         and y.yuetjkjb_id=kj.id and sl.yuetjkjb_id=kj.id and zl.yuetjkjb_id=kj.id  and kj.diancxxb_id=dc.id\n"
			+ "         and y.fenx=sl.fenx(+) and y.fenx=zl.fenx(+) "+zhuangt+shulzt+zhilzt+" \n"
			+ "         and y.fenx='累计' and sl.fenx='累计' and zl.fenx='累计'             \n"
			+ "      	group by (kj.diancxxb_id,decode(kj.jihkjb_id,3,1,kj.jihkjb_id),y.fenx)) tq,"+dianc+",jihkjb j\n"
			+ " where fx.diancxxb_id=tq.diancxxb_id(+)\n"
			+ "   and fx.diancxxb_id=bq.diancxxb_id(+)\n"
			+ "   and fx.diancxxb_id=dc.id\n"
			+ "   and fx.fenx=bq.fenx(+)\n"
			+ "   and fx.fenx=tq.fenx(+)\n"
			+ "   and fx.kouj=bq.kouj(+)\n"
			+ "   and fx.kouj=tq.kouj(+)\n"
			+ "   and fx.kouj=j.id\n" + tiaoj
			+strGongsID+"\n"
			+ fenz +")c " 
            + where+") c,"
			+"(select decode(grouping(dc.mingc)+grouping("+mc+"),2,'总计',1,"+mc+", '&nbsp;&nbsp;'||dc.mingc) as diancmc,\n" 
			+"     r.fenx,sum(r.shul) as youl,decode(sum(r.shul),0,0,Round(sum(r.hanszhj*r.shul)/sum(r.shul),2)) as tianrypjdj\n"  
			+"      from  "+yb_rcy+" r ,"+dianc +"\n"  
			+"      where r.riq=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n" 
			+"      and r.diancxxb_id=dc.id and r.fenx='累计'\n" +tiaoj  +youzt +strGongsID
			+"      group by rollup (r.fenx,"+mc+",dc.mingc)\n"  
			+"      having not grouping(fenx)=1) a,"
			+"(select decode(grouping(dc.mingc)+grouping("+mc+"),2,'总计',1,"+mc+", '&nbsp;&nbsp;'||dc.mingc) as diancmc,\n" 
			+"     	r.fenx,sum(r.shul) as youl,decode(sum(r.shul),0,0,Round(sum(r.hanszhj*r.shul)/sum(r.shul),2)) as tianrypjdj\n"  
			+" from  "+yb_rcy+" r ,"+dianc +"\n"  
			+"      where r.riq=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12) \n" 
			+"      and r.diancxxb_id=dc.id and r.fenx='累计'\n" +tiaoj +youzt +strGongsID
			+"      group by rollup (r.fenx,"+mc+",dc.mingc)\n"  
			+"      having not grouping(fenx)=1) b"
			+"    where c.aa=a.diancmc (+) and c.fenx=a.fenx(+) and c.aa=b.diancmc(+) and c.fenx=b.fenx(+) order by c.xuh";
			
		}else{
			strSQL=biaot0+strzt+"\n"  
			+ " from ( select rownum as xuh,c.* from(select    "+fenz_bt+biaot+" fx.fenx as fenx,\n"
			+ "      Round(decode(sum(bq.laimsl),0,0,sum(bq.daoczhj*bq.laimsl)/sum(bq.laimsl)),2) as bq_daoczhj ,\n"
			+ "      Round(decode(sum(tq.laimsl),0,0,sum(tq.daoczhj*tq.laimsl)/sum(tq.laimsl)),2) as tq_daoczhj  ,\n"
			+ "      Round(decode(sum(bq.laimsl),0,0,sum(bq.chebj*bq.laimsl)/sum(bq.laimsl)),2) as bq_chebj,\n"
			+ "      Round(decode(sum(tq.laimsl),0,0,sum(tq.chebj*tq.laimsl)/sum(tq.laimsl)),2) as tq_chebj,\n"
			+ "      Round(decode(sum(bq.laimsl),0,0,sum(bq.yunj*bq.laimsl)/sum(bq.laimsl)),2) as bq_yunj,\n"
			+ "      Round(decode(sum(tq.laimsl),0,0,sum(tq.yunj*tq.laimsl)/sum(tq.laimsl)),2) as tq_yunj,\n"
			+ "      Round(decode(sum(bq.laimsl),0,0,sum(bq.zaf*bq.laimsl)/sum(bq.laimsl)),2) as bq_zaf,\n"
			+ "      Round(decode(sum(tq.laimsl),0,0,sum(tq.zaf*tq.laimsl)/sum(tq.laimsl)),2) as tq_zaf,\n"
			+ "		 Round(decode(sum(bq.farl*bq.laimsl),0,0,round(sum(bq.buhs_daoczhj*bq.laimsl)/sum(bq.laimsl),2)*29.271/round(sum(bq.farl*bq.laimsl)/sum(bq.laimsl),2)),2)  as bq_rucbmdj,\n" 
			+ "      Round(decode(sum(tq.farl*tq.laimsl),0,0,round(sum(tq.buhs_daoczhj*tq.laimsl)/sum(tq.laimsl),2)*29.271/round(sum(tq.farl*tq.laimsl)/sum(tq.laimsl),2)),2)  as tq_rucbmdj\n"
			+ " from\n"
			/*+ "     (select distinct dcid.diancxxb_id,kouj,fx.fenx,fx.xuh from\n"
			+ "             (select distinct diancxxb_id,decode(kj.jihkjb_id,3,1,kj.jihkjb_id) as kouj\n"
			+ "                     from yuercbmdj y,yuetjkjb kj\n"
			+ "                     where y.yuetjkjb_id=kj.id\n"
			+ "                     and (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n"
			+ "                     or riq=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'))) ) dcid,vwfenxYue fx,diancxxb dc"+dianckjmx_bm+" \n" 
			+ "               where dc.id=dcid.diancxxb_id   "+dianckjmx_tj+strGongsID+"   ) fx,\n" */
			+ "     (select distinct dcid.diancxxb_id,kouj,fx.fenx,fx.xuh from\n"
			+ "             (select distinct diancxxb_id,decode(kj.jihkjb_id,3,1,kj.jihkjb_id) as kouj\n"
			+ "                     from  "+yb_dj+" y,yuetjkjb kj\n"
			+ "                     where y.yuetjkjb_id=kj.id\n"
			+ "                     and ( riq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and riq<=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n"
			+ "                     or ( riq>=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')) and riq<=last_year_today(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')) )\n" +
					") ) dcid," +
			"		(select decode(1, 1, '本月', '') as fenx, 1 as xuh from dual) fx,\n"+
					"diancxxb dc"+dianckjmx_bm+" \n" 
			+ "               where dc.id=dcid.diancxxb_id   "+dianckjmx_tj+strGongsID+"   ) fx,\n"
			+ "     (select y.fenx,kj.diancxxb_id,decode(kj.jihkjb_id,3,1,kj.jihkjb_id) as kouj," +shul+ "\n"
			+ "      from  "+yb_dj+" y,yuetjkjb kj, "+yb_sl+" sl, "+yb_zl+" zl,diancxxb dc\n"
			+ "           where kj.riq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and kj.riq<=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd') \n"
			+ "           and y.yuetjkjb_id=kj.id and sl.yuetjkjb_id=kj.id and zl.yuetjkjb_id=kj.id and kj.diancxxb_id=dc.id\n"
			+ "           and y.fenx=sl.fenx(+) and y.fenx=zl.fenx(+) "+zhuangt+shulzt+zhilzt+" \n"
			+ "           and y.fenx='本月' and sl.fenx='本月' and zl.fenx='本月'             \n"
			+ "           group by (kj.diancxxb_id,decode(kj.jihkjb_id,3,1,kj.jihkjb_id),y.fenx)) bq,\n"
			+ "     (select y.fenx,kj.diancxxb_id,decode(kj.jihkjb_id,3,1,kj.jihkjb_id) as kouj," +shul+ "\n"
			+ "      from  "+yb_dj+" y,yuetjkjb kj, "+yb_sl+" sl, "+yb_zl+" zl,diancxxb dc\n"
			+ "      	where kj.riq>=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')) and kj.riq<=last_year_today(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'))\n"
			+ "         and y.yuetjkjb_id=kj.id and sl.yuetjkjb_id=kj.id and zl.yuetjkjb_id=kj.id  and kj.diancxxb_id=dc.id\n"
			+ "         and y.fenx=sl.fenx(+) and y.fenx=zl.fenx(+) "+zhuangt+shulzt+zhilzt+" \n"
			+ "         and y.fenx='本月' and sl.fenx='本月' and zl.fenx='本月'             \n"
			+ "      	group by (kj.diancxxb_id,decode(kj.jihkjb_id,3,1,kj.jihkjb_id),y.fenx)) tq,"+dianc+",jihkjb j\n"
			+ " where fx.diancxxb_id=tq.diancxxb_id(+)\n"
			+ "   and fx.diancxxb_id=bq.diancxxb_id(+)\n"
			+ "   and fx.diancxxb_id=dc.id\n"
			+ "   and fx.fenx=bq.fenx(+)\n"
			+ "   and fx.fenx=tq.fenx(+)\n"
			+ "   and fx.kouj=bq.kouj(+)\n"
			+ "   and fx.kouj=tq.kouj(+)\n"
			+ "   and fx.kouj=j.id\n" + tiaoj
			+strGongsID+"\n"
			+ fenz +")c " 
            + where+") c,"
			+"(select decode(grouping(dc.mingc)+grouping("+mc+"),2,'总计',1,"+mc+", '&nbsp;&nbsp;'||dc.mingc) as diancmc,\n" 
			+"     r.fenx,"+strYouzb+"\n"  
			+"      from  "+yb_rcy+" r ,diancxxb dianc,"+dianc +"\n"  
			+"      where r.riq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and r.riq<=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n" 
			+"      and r.diancxxb_id=dc.id and r.fenx='本月'\n" +tiaoj  +youzt +strGongsID
			+"      group by rollup (r.fenx,"+mc+",dc.mingc)\n"  
			+"      having not grouping(fenx)=1) a,"
			+"(select decode(grouping(dc.mingc)+grouping("+mc+"),2,'总计',1,"+mc+", '&nbsp;&nbsp;'||dc.mingc) as diancmc,\n" 
			+"     	r.fenx,"+strYouzb+"\n"  
			+" from  "+yb_rcy+" r ,diancxxb dianc,"+dianc +"\n"  
			+"      where r.riq>=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and r.riq<=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)\n" 
			+"      and r.diancxxb_id=dc.id and r.fenx='本月'\n" +tiaoj +youzt +strGongsID
			+"      group by rollup (r.fenx,"+mc+",dc.mingc)\n"  
			+"      having not grouping(fenx)=1) b"
			+"    where c.aa=a.diancmc (+) and c.fenx=a.fenx(+) and c.aa=b.diancmc(+) and c.fenx=b.fenx(+) order by c.xuh";
		}
		
		return strSQL;
	}
	
	/**
	 * 
	 * 燃料管理10_2表:入厂煤(电厂、口径统计)价格情况表
	 * @param biaot
	 * @param dianc
	 * @param tiaoj
	 * @param shulzt
	 * @param zhilzt
	 * @param youzt
	 * @param mc
	 * @param fenz
	 * @param dianckjmx_bm
	 * @param dianckjmx_tj
	 * @param rucycbb_bt
	 * @param jihkj
	 * @param rucycbb_bq
	 * @param rucycbb_tq
	 * @param rucycbb_tj
	 * @param strGongsID
	 * @param zhuangt
	 * @param intyear
	 * @param intMonth
	 * @param intyear2
	 * @param intMonth2
	 * @return
	 */
	public String getSql10_2(String biaot,String biaot0,String tj,String fx,String notHuiz,String strzt, String dianc, String tiaoj,String shulzt,String zhilzt,String youzt,String mc,String shul,
			String fenz, String dianckjmx_bm, String dianckjmx_tj,String rucycbb_bt,String jihkj,String rucycbb_bq,String rucycbb_tq,String rucycbb_tj,
			String strGongsID, String zhuangt, String yb_sl,String yb_zl,String yb_dj,long intyear, long intMonth,
			long intyear2, long intMonth2) {
		String strSQL="";
		if(intyear2==intyear&&intMonth==1){
			strSQL= biaot0+strzt+"from (select "+tj+biaot+",\n" +
   		 "        decode(1,1,fx.fenx,'') as fenx,\n" + 
   		 "        Round(decode(sum(bq.laimsl),0,0,sum(bq.daoczhj*bq.laimsl)/sum(bq.laimsl)),2) as bq_daoczhj ,\n" + 
   		 "        Round(decode(sum(tq.laimsl),0,0,sum(tq.daoczhj*tq.laimsl)/sum(tq.laimsl)),2) as tq_daoczhj  ,\n" + 
   		 "        Round(decode(sum(bq.laimsl),0,0,sum(bq.chebj*bq.laimsl)/sum(bq.laimsl)),2) as bq_chebj,\n" + 
   		 "        Round(decode(sum(tq.laimsl),0,0,sum(tq.chebj*tq.laimsl)/sum(tq.laimsl)),2) as tq_chebj,\n" + 
   		 "        Round(decode(sum(bq.laimsl),0,0,sum(bq.yunj*bq.laimsl)/sum(bq.laimsl)),2) as bq_yunj,\n" + 
   		 "        Round(decode(sum(tq.laimsl),0,0,sum(tq.yunj*tq.laimsl)/sum(tq.laimsl)),2) as tq_yunj,\n" + 
   		 "        Round(decode(sum(bq.laimsl),0,0,sum(bq.zaf*bq.laimsl)/sum(bq.laimsl)),2) as bq_zaf,\n" + 
   		 "        Round(decode(sum(tq.laimsl),0,0,sum(tq.zaf*tq.laimsl)/sum(tq.laimsl)),2) as tq_zaf,\n" + 
   		 "        Round(decode(sum(bq.farl*bq.laimsl),0,0,round(sum(bq.buhs_daoczhj*bq.laimsl)/sum(bq.laimsl),2)*29.271/round(sum(bq.farl*bq.laimsl)/sum(bq.laimsl),2)),2)  as bq_rucbmdj,\n" + 
   		 "        Round(decode(sum(tq.farl*tq.laimsl),0,0,round(sum(tq.buhs_daoczhj*tq.laimsl)/sum(tq.laimsl),2)*29.271/round(sum(tq.farl*tq.laimsl)/sum(tq.laimsl),2)),2)  as tq_rucbmdj,\n" + 
   		 rucycbb_bt +
   		 " from\n" + 
   		 /*"     ( select yid.diancxxb_id,fx.fenx,fx.xuh from\n" + 
   		 "                   (select distinct diancxxb_id from yuercbmdj y,yuetjkjb kj\n" + 
   		 "                       where y.yuetjkjb_id=kj.id and (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))\n" + 
   		 "                    ) yid,vwfenxYue fx ,diancxxb dc"+dianckjmx_bm+"\n" + 
   		 "               where dc.id=yid.diancxxb_id "+dianckjmx_tj+strGongsID+" ) fx,\n" +*/
   		"     ( select yid.diancxxb_id,fx.fenx,fx.xuh from\n" + 
		 "                   (select distinct diancxxb_id from  "+yb_dj+" y,yuetjkjb kj\n" + 
		 "                       where y.yuetjkjb_id=kj.id and \n" +
		 "(riq=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd') \n" +
		 "or riq=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12) )\n" + 
		 "                    ) yid," +
		 "(select decode(1, 1, '累计', '') as fenx, 1 as xuh from dual) fx,\n"+
		 "diancxxb dc"+dianckjmx_bm+"\n" + 
		 "               where dc.id=yid.diancxxb_id "+dianckjmx_tj+strGongsID+" ) fx,\n" +
   		 "     (select  decode(1,1,'累计') as fenx,kj.diancxxb_id," +shul+ "\n"+
   		 "           from  "+yb_dj+" y,yuetjkjb kj, "+yb_sl+" sl, "+yb_zl+" zl,diancxxb dc\n" + 
   		 "           where kj.riq=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')  and y.fenx = '累计' and kj.diancxxb_id=dc.id\n" + 
   		 "                 and y.yuetjkjb_id=kj.id and sl.yuetjkjb_id=kj.id and zl.yuetjkjb_id=kj.id "+jihkj+"\n" + 
   		 "                 and y.fenx=sl.fenx(+) and y.fenx=zl.fenx(+) "+zhuangt+shulzt+zhilzt+"\n" + 
   		 "           group by (kj.diancxxb_id)\n" + 
   		 "           ) bq,\n" + 
   		 "   (select  decode(1,1,'累计') as fenx,kj.diancxxb_id," +shul+ "\n"+
   		 "           from  "+yb_dj+" y,yuetjkjb kj, "+yb_sl+" sl, "+yb_zl+" zl,diancxxb dc\n" + 
   		 "           where kj.riq=last_year_today(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')) and y.fenx = '累计' and kj.diancxxb_id=dc.id\n" + 
   		 "                 and y.yuetjkjb_id=kj.id and sl.yuetjkjb_id=kj.id and zl.yuetjkjb_id=kj.id "+jihkj+"\n" + 
   		 "                 and y.fenx=sl.fenx(+) and y.fenx=zl.fenx(+) "+zhuangt+shulzt+zhilzt+"\n" + 
   		 "           group by (kj.diancxxb_id)\n" + 
   		 "           ) tq,"+dianc+"\n" + rucycbb_bq+rucycbb_tq+
   		 " where  dc.id=fx.diancxxb_id and fx.diancxxb_id=tq.diancxxb_id(+)\n" + 
   		 "        and fx.diancxxb_id=bq.diancxxb_id(+)\n" + 
   		 "        and fx.diancxxb_id=dc.id\n" + 
   		 "        and fx.fenx=bq.fenx(+)\n" + 
   		 "        and fx.fenx=tq.fenx(+)\n" + rucycbb_tj+ tiaoj +strGongsID+"  \n"+ fenz+ " )a"+fx;
			
		}else{
			strSQL= biaot0+strzt+"from (select "+tj+biaot+",\n" +
   		 "        decode(1,1,fx.fenx,'') as fenx,\n" + 
   		 "        Round(decode(sum(bq.laimsl),0,0,sum(bq.daoczhj*bq.laimsl)/sum(bq.laimsl)),2) as bq_daoczhj ,\n" + 
   		 "        Round(decode(sum(tq.laimsl),0,0,sum(tq.daoczhj*tq.laimsl)/sum(tq.laimsl)),2) as tq_daoczhj  ,\n" + 
   		 "        Round(decode(sum(bq.laimsl),0,0,sum(bq.chebj*bq.laimsl)/sum(bq.laimsl)),2) as bq_chebj,\n" + 
   		 "        Round(decode(sum(tq.laimsl),0,0,sum(tq.chebj*tq.laimsl)/sum(tq.laimsl)),2) as tq_chebj,\n" + 
   		 "        Round(decode(sum(bq.laimsl),0,0,sum(bq.yunj*bq.laimsl)/sum(bq.laimsl)),2) as bq_yunj,\n" + 
   		 "        Round(decode(sum(tq.laimsl),0,0,sum(tq.yunj*tq.laimsl)/sum(tq.laimsl)),2) as tq_yunj,\n" + 
   		 "        Round(decode(sum(bq.laimsl),0,0,sum(bq.zaf*bq.laimsl)/sum(bq.laimsl)),2) as bq_zaf,\n" + 
   		 "        Round(decode(sum(tq.laimsl),0,0,sum(tq.zaf*tq.laimsl)/sum(tq.laimsl)),2) as tq_zaf,\n" + 
   		 "        Round(decode(sum(bq.farl*bq.laimsl),0,0,round(sum(bq.buhs_daoczhj*bq.laimsl)/sum(bq.laimsl),2)*29.271/round(sum(bq.farl*bq.laimsl)/sum(bq.laimsl),2)),2)  as bq_rucbmdj,\n" + 
   		 "        Round(decode(sum(tq.farl*tq.laimsl),0,0,round(sum(tq.buhs_daoczhj*tq.laimsl)/sum(tq.laimsl),2)*29.271/round(sum(tq.farl*tq.laimsl)/sum(tq.laimsl),2)),2)  as tq_rucbmdj,\n" + 
   		 rucycbb_bt +
   		 " from\n" + 
   		 /*"     ( select yid.diancxxb_id,fx.fenx,fx.xuh from\n" + 
   		 "                   (select distinct diancxxb_id from yuercbmdj y,yuetjkjb kj\n" + 
   		 "                       where y.yuetjkjb_id=kj.id and (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))\n" + 
   		 "                    ) yid,vwfenxYue fx ,diancxxb dc"+dianckjmx_bm+"\n" + 
   		 "               where dc.id=yid.diancxxb_id "+dianckjmx_tj+strGongsID+" ) fx,\n" + */
   		"     ( select yid.diancxxb_id,fx.fenx,fx.xuh from\n" + 
		 "                   (select distinct diancxxb_id from  "+yb_dj+" y,yuetjkjb kj\n" + 
		 "                       where y.yuetjkjb_id=kj.id and \n" +
		 "(    riq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and riq<=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd') \n" +
		 		"or (riq>=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) and riq<=add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12)   )   )\n" + 
		 "                    ) yid," +
		 "(select decode(1, 1, '累计', '') as fenx, 1 as xuh from dual) fx,\n"+
		 "diancxxb dc"+dianckjmx_bm+"\n" + 
		 "               where dc.id=yid.diancxxb_id "+dianckjmx_tj+strGongsID+" ) fx,\n" + 
   		 "     (select  decode(1,1,'累计') as fenx, kj.diancxxb_id," +shul+ "\n"+
   		 "           from  "+yb_dj+" y,yuetjkjb kj, "+yb_sl+" sl, "+yb_zl+" zl,diancxxb dc\n" + 
   		 "           where kj.riq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  and kj.riq<=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')" +
   		 		"and y.fenx = '本月' and kj.diancxxb_id=dc.id\n" + 
   		 "                 and y.yuetjkjb_id=kj.id and sl.yuetjkjb_id=kj.id and zl.yuetjkjb_id=kj.id "+jihkj+"\n" + 
   		 "                 and y.fenx=sl.fenx(+) and y.fenx=zl.fenx(+) "+zhuangt+shulzt+zhilzt+"\n" + 
   		 "           group by (kj.diancxxb_id)\n" + 
   		 ") bq,\n" + 
   		 "   (select  decode(1,1,'累计') as fenx, kj.diancxxb_id," +shul+ "\n"+
   		 "           from  "+yb_dj+" y,yuetjkjb kj, "+yb_sl+" sl, "+yb_zl+" zl,diancxxb dc\n" + 
   		 "           where kj.riq>=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')) and kj.riq<=last_year_today(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'))" +
   		 		"and y.fenx = '本月' and kj.diancxxb_id=dc.id\n" + 
   		 "                 and y.yuetjkjb_id=kj.id and sl.yuetjkjb_id=kj.id and zl.yuetjkjb_id=kj.id "+jihkj+"\n" + 
   		 "                 and y.fenx=sl.fenx(+) and y.fenx=zl.fenx(+) "+zhuangt+shulzt+zhilzt+"\n" + 
   		 "           group by (kj.diancxxb_id)\n" + 
   		 ") tq,"+dianc+"\n" + rucycbb_bq+rucycbb_tq+
   		 " where  dc.id=fx.diancxxb_id and fx.diancxxb_id=tq.diancxxb_id(+)\n" + 
   		 "        and fx.diancxxb_id=bq.diancxxb_id(+)\n" + 
   		 "        and fx.diancxxb_id=dc.id\n" + 
   		 "        and fx.fenx=bq.fenx(+)\n" + 
   		 "        and fx.fenx=tq.fenx(+)\n" + rucycbb_tj+ tiaoj +strGongsID+"  \n"+ fenz+ " )a"+fx+notHuiz;
		}
		
		return strSQL;
	}

	/**
	 * 
	 * 燃料管理11表:发电标煤单价情况表
	 * 
	 * @param biaot
	 * @param dianc
	 * @param tiaoj
	 * @param fenz
	 * @param dianckjmx_bm
	 * @param dianckjmx_tj
	 * @param strGongsID
	 * @param zhuangt
	 * @param intyear
	 * @param intMonth
	 * @param intyear2
	 * @param intMonth2
	 * @return
	 */
	public String getSql11(String biaot, String dianc, String tiaoj,String grouping,String shul,
			String fenz, String dianckjmx_bm, String dianckjmx_tj,
			String strGongsID, String zhuangt,String yb_zb, long intyear, long intMonth,
			long intyear2, long intMonth2) {
		String strSQL="";
		if(intyear2==intyear&&intMonth==1){
			strSQL = "select "+biaot+",\n"
			+ "       fx.fenx,\n"
			+ "       decode(sum(bq.fadhml),0,0,Round(sum(bq.fadtrmdj*bq.fadhml)/sum(bq.fadhml),2)) as bq_fdtrmdj,\n"
			+ "       decode(sum(tq.fadhml),0,0,Round(sum(tq.fadtrmdj*tq.fadhml)/sum(tq.fadhml),2)) as tq_fdtrmdj,\n"
			+ "\n"
			+ "       decode(sum(bq.gongrhml),0,0,Round(sum(bq.gongrtrmdj*bq.gongrhml)/sum(bq.gongrhml),2)) as bq_grtrmdj,\n"
			+ "       decode(sum(tq.gongrhml),0,0,Round(sum(tq.gongrtrmdj*tq.gongrhml)/sum(tq.gongrhml),2)) as tq_grtrmdj,\n"
			+ "\n"
			+ "       decode(sum(bq.fadzhbml),0,0,Round(sum(bq.fadzhbmdj*bq.fadzhbml)/sum(bq.fadzhbml),2))  as bq_fdzhbmdj,\n"
			+ "       decode(sum(tq.fadzhbml),0,0,Round(sum(tq.fadzhbmdj*tq.fadzhbml)/sum(tq.fadzhbml),2))  as tq_fdzhbmdj,\n"
			+ "\n"
			+ "       decode(sum(bq.fadbml),0,0,Round(sum(bq.fdmzbmdj*bq.fadbml)/sum(bq.fadbml),2)) as bq_fadbmdj,\n"
			+ "       decode(sum(tq.fadbml),0,0,Round(sum(tq.fdmzbmdj*tq.fadbml)/sum(tq.fadbml),2)) as tq_fadbmdj,\n"
			+ "\n"
			+ "       decode(sum(bq.fadybml),0,0,Round(sum(bq.fdyzbmdj*bq.fadybml)/sum(bq.fadybml),2)) as bq_fadybmdj,\n"
			+ "       decode(sum(tq.fadybml),0,0,Round(sum(tq.fdyzbmdj*tq.fadybml)/sum(tq.fadybml),2)) as tq_fadybmdj,\n"
			+ "\n"
			+ "       decode(sum(bq.gongrzhbml),0,0,Round(sum(bq.grzhbmdj*bq.gongrzhbml)/sum(bq.gongrzhbml),2)) as bq_grzhbmdj,\n"
			+ "       decode(sum(tq.gongrzhbml),0,0,Round(sum(tq.grzhbmdj*tq.gongrzhbml)/sum(tq.gongrzhbml),2)) as tq_grzhbmdj,\n"
			+ "\n"
			+ "       decode(sum(bq.grhmbml),0,0,Round(sum(bq.grmzbmdj*bq.grhmbml)/sum(bq.grhmbml),2)) as bq_grmbmdj,\n"
			+ "       decode(sum(tq.grhmbml),0,0,Round(sum(tq.grmzbmdj*tq.grhmbml)/sum(tq.grhmbml),2)) as tq_grmbmdj,\n"
			+ "\n"
			+ "       decode(sum(bq.grhybml),0,0,Round(sum(bq.gryzbmdj*bq.grhybml)/sum(bq.grhybml),2)) as bq_grhybmdj,\n"
			+ "       decode(sum(tq.grhybml),0,0,Round(sum(tq.gryzbmdj*tq.grhybml)/sum(tq.grhybml),2)) as tq_grhybmdj\n"
			+" \n         , grouping(dc.mingc) dcmc,grouping(fx.fenx) f"+grouping
			+ "  \n from\n"
			/*+ "          (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n"
			+ "     (select distinct diancxxb_id from yuezbb\n"
			+ "             where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')))\n"
			+ "     ) dcid,(select decode(1,1,'本月','') as fenx,1 as xuh  from dual union select decode(1,1,'累计','')  as fenx,2 as xhu from dual ) fx,diancxxb dc"+dianckjmx_bm+"\n"  
			+ "     where dc.id=dcid.diancxxb_id  "+dianckjmx_tj+strGongsID+"  ) fx,\n" */
			+ " (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n"
			+ "   (select distinct diancxxb_id from  "+yb_zb+"\n"
			+ "where\n"
			+ "(riq=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd') or \n"
			+ "riq=last_year_today(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')))\n"
			+ "     ) dcid,(select decode(1,1,'累计','') as fenx,1 as xuh  from dual \n"
			+ "	) fx,diancxxb dc\n"
			+ dianckjmx_bm
			+ "     where dc.id=dcid.diancxxb_id  "
			+ dianckjmx_tj
			+ strGongsID
			+ "  ) fx,\n"
			+ " ((select decode(1,1,'累计','') as fenx, y.diancxxb_id,"+shul+"\n"
			+ "        from  "+yb_zb+" y,diancxxb dc\n"
			+ "       where y.riq = to_date('"+intyear2+"-"+intMonth2+"-01', 'yyyy-mm-dd')\n"
			+ "         and y.fenx = '累计' "+zhuangt+" and y.diancxxb_id=dc.id\n"
			+ "       group by (y.diancxxb_id))) bq,\n"
			+ "\n"
			+ "((select decode(1,1,'累计','') as fenx, y.diancxxb_id,"+shul+"\n"
			+ "      from  "+yb_zb+" y,diancxxb dc\n"
			+ "     where y.riq = last_year_today(to_date('"+intyear2+"-"+intMonth2+"-01', 'yyyy-mm-dd'))\n"
			+ "       and y.fenx = '累计' "+zhuangt+" and y.diancxxb_id=dc.id\n"
			+ "     group by (y.diancxxb_id))) tq,\n" + dianc
			+ "where fx.diancxxb_id=bq.diancxxb_id(+)\n"
			+ "and   fx.diancxxb_id=tq.diancxxb_id(+)\n"
			+ "and   fx.diancxxb_id=dc.id  "+tiaoj+" \n"
			+ "and   fx.fenx=bq.fenx(+)\n"
			+ "and   fx.fenx=tq.fenx(+)\n"
			+strGongsID+"\n" 
			+  fenz;
			
		}else{
			strSQL = "select "+biaot+",\n"
			+ "       fx.fenx,\n"
			+ "       decode(sum(bq.fadhml),0,0,Round(sum(bq.fadtrmdj*bq.fadhml)/sum(bq.fadhml),2)) as bq_fdtrmdj,\n"
			+ "       decode(sum(tq.fadhml),0,0,Round(sum(tq.fadtrmdj*tq.fadhml)/sum(tq.fadhml),2)) as tq_fdtrmdj,\n"
			+ "\n"
			+ "       decode(sum(bq.gongrhml),0,0,Round(sum(bq.gongrtrmdj*bq.gongrhml)/sum(bq.gongrhml),2)) as bq_grtrmdj,\n"
			+ "       decode(sum(tq.gongrhml),0,0,Round(sum(tq.gongrtrmdj*tq.gongrhml)/sum(tq.gongrhml),2)) as tq_grtrmdj,\n"
			+ "\n"
			+ "       decode(sum(bq.fadzhbml),0,0,Round(sum(bq.fadzhbmdj*bq.fadzhbml)/sum(bq.fadzhbml),2))  as bq_fdzhbmdj,\n"
			+ "       decode(sum(tq.fadzhbml),0,0,Round(sum(tq.fadzhbmdj*tq.fadzhbml)/sum(tq.fadzhbml),2))  as tq_fdzhbmdj,\n"
			+ "\n"
			+ "       decode(sum(bq.fadbml),0,0,Round(sum(bq.fdmzbmdj*bq.fadbml)/sum(bq.fadbml),2)) as bq_fadbmdj,\n"
			+ "       decode(sum(tq.fadbml),0,0,Round(sum(tq.fdmzbmdj*tq.fadbml)/sum(tq.fadbml),2)) as tq_fadbmdj,\n"
			+ "\n"
			+ "       decode(sum(bq.fadybml),0,0,Round(sum(bq.fdyzbmdj*bq.fadybml)/sum(bq.fadybml),2)) as bq_fadybmdj,\n"
			+ "       decode(sum(tq.fadybml),0,0,Round(sum(tq.fdyzbmdj*tq.fadybml)/sum(tq.fadybml),2)) as tq_fadybmdj,\n"
			+ "\n"
			+ "       decode(sum(bq.gongrzhbml),0,0,Round(sum(bq.grzhbmdj*bq.gongrzhbml)/sum(bq.gongrzhbml),2)) as bq_grzhbmdj,\n"
			+ "       decode(sum(tq.gongrzhbml),0,0,Round(sum(tq.grzhbmdj*tq.gongrzhbml)/sum(tq.gongrzhbml),2)) as tq_grzhbmdj,\n"
			+ "\n"
			+ "       decode(sum(bq.grhmbml),0,0,Round(sum(bq.grmzbmdj*bq.grhmbml)/sum(bq.grhmbml),2)) as bq_grmbmdj,\n"
			+ "       decode(sum(tq.grhmbml),0,0,Round(sum(tq.grmzbmdj*tq.grhmbml)/sum(tq.grhmbml),2)) as tq_grmbmdj,\n"
			+ "\n"
			+ "       decode(sum(bq.grhybml),0,0,Round(sum(bq.gryzbmdj*bq.grhybml)/sum(bq.grhybml),2)) as bq_grhybmdj,\n"
			+ "       decode(sum(tq.grhybml),0,0,Round(sum(tq.gryzbmdj*tq.grhybml)/sum(tq.grhybml),2)) as tq_grhybmdj\n"
			+" \n         , grouping(dc.mingc) dcmc,grouping(fx.fenx) f"+grouping
			+ "  \n from\n"
			/*+ "          (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n"
			+ "     (select distinct diancxxb_id from yuezbb\n"
			+ "             where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')))\n"
			+ "     ) dcid,(select decode(1,1,'本月','') as fenx,1 as xuh  from dual union select decode(1,1,'累计','')  as fenx,2 as xhu from dual ) fx,diancxxb dc"+dianckjmx_bm+"\n"  
			+ "     where dc.id=dcid.diancxxb_id  "+dianckjmx_tj+strGongsID+"  ) fx,\n" */
			+ "          (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n"
			+ "     (select distinct diancxxb_id from  "+yb_zb+"\n"
			+ "where (riq>=to_date('"
			+ intyear
			+ "-"
			+ intMonth
			+ "-01','yyyy-mm-dd') and riq<=to_date('"
			+ intyear2
			+ "-"
			+ intMonth2
			+ "-01','yyyy-mm-dd') or (riq>=last_year_today(to_date('"
			+ intyear
			+ "-"
			+ intMonth
			+ "-01','yyyy-mm-dd')) and riq<=last_year_today(to_date('"
			+ intyear2
			+ "-"
			+ intMonth2
			+ "-01','yyyy-mm-dd'))))\n"
			+ "     ) dcid,(select decode(1,1,'累计','') as fenx,1 as xuh  from dual " +
					") fx,diancxxb dc"
			+ dianckjmx_bm
			+ "\n"
			+ "     where dc.id=dcid.diancxxb_id  "
			+ dianckjmx_tj
			+ strGongsID
			+ "  ) fx,\n"
			+ "((select decode(1,1,'累计','') as fenx,y.diancxxb_id,"+shul+"\n"
			+ "        from  "+yb_zb+" y,diancxxb dc\n"
			+ "       where y.riq >= to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd') and y.riq <= to_date('"+intyear2+"-"+intMonth2+"-01', 'yyyy-mm-dd')\n"
			+ "         and y.fenx = '本月' "+zhuangt+" and y.diancxxb_id=dc.id\n"
			+ "       group by (y.diancxxb_id))\n"
			+ " ) bq,\n"
			+ "\n"
			+ "((select decode(1,1,'累计','') as fenx,y.diancxxb_id,"+shul+"\n"
			+ "      from  "+yb_zb+" y,diancxxb dc\n"
			+ "     where y.riq = last_year_today(to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd')) and y.riq <= last_year_today(to_date('"+intyear2+"-"+intMonth2+"-01', 'yyyy-mm-dd'))\n"
			+ "       and y.fenx = '本月' "+zhuangt+" and y.diancxxb_id=dc.id\n"
			+ "     group by (y.diancxxb_id))\n"
			+ " ) tq,\n" + dianc
			+ "where fx.diancxxb_id=bq.diancxxb_id(+)\n"
			+ "and   fx.diancxxb_id=tq.diancxxb_id(+)\n"
			+ "and   fx.diancxxb_id=dc.id  "+tiaoj+" \n"
			+ "and   fx.fenx=bq.fenx(+)\n"
			+ "and   fx.fenx=tq.fenx(+)\n"
			+strGongsID+"\n" 
			+  fenz;
		 }

		return strSQL;
	}

	/**
	 * 
	 * 燃料管理12表:燃料成本费用情况表
	 * 
	 * @param biaot
	 * @param dianc
	 * @param tiaoj
	 * @param fenz
	 * @param dianckjmx_bm
	 * @param dianckjmx_tj
	 * @param strGongsID
	 * @param zhuangt
	 * @param intyear
	 * @param intMonth
	 * @param intyear2
	 * @param intMonth2
	 * @return
	 */
	public String getSql12(String biaot, String dianc, String tiaoj,String strzt,String shul,
			String fenz, String dianckjmx_bm, String dianckjmx_tj,
			String strGongsID, String zhuangt,String yb_zb, long intyear, long intMonth,
			long intyear2, long intMonth2) {
        String strSQL="";
        if(intyear2==intyear&&intMonth==1){
        	strSQL = "select "+biaot+",\n"
			//+ "       --fx.fenx as fenx,\n"
			+ "       Round(sum(bq.ranlzcb),3) as bq_ranlzcb,\n"
			+ "       Round(sum(tq.ranlzcb),3) as tq_ranlzcb,\n"
			+ "       Round(sum(bq.fadmtcb),3) as bq_fadmtcb,\n"
			+ "       Round(sum(tq.fadmtcb),3) as tq_fadmtcb,\n"
			+ "       Round(sum(bq.gongrmtcb),3) as bq_gongrmtcb,\n"
			+ "       Round(sum(tq.gongrmtcb),3) as tq_gongrmtcb,\n"
			+ "       Round(sum(bq.fadycb),3) as bq_fadycb,\n"
			+ "       Round(sum(tq.fadycb),3) as tq_fadycb,\n"
			+ "       Round(sum(bq.gongrycb),3) as bq_gongrycb,\n"
			+ "       Round(sum(tq.gongrycb),3) as tq_gongrycb,\n"
			+ "       decode(sum(bq.gongdl),0,0,Round((sum(bq.danwrlcb*bq.gongdl)/sum(bq.gongdl)),2) ) as bq_danwrlcb,\n"
			+ "       decode(sum(tq.gongdl),0,0,Round((sum(tq.danwrlcb*tq.gongdl)/sum(tq.gongdl)),2) ) as tq_danwrlcb,"+strzt+"\n"
			+ "  from\n"
			/*+ "  (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n"
			+ "     (select distinct diancxxb_id from yuezbb\n"
			+ "             where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')))\n"
			+ "     ) dcid,(select decode(1,1,'本月','') as fenx,1 as xuh  from dual union select decode(1,1,'累计','')  as fenx,2 as xhu from dual ) fx,diancxxb dc"+dianckjmx_bm+"\n"  
			+ "     where dc.id=dcid.diancxxb_id  "+dianckjmx_tj+strGongsID+"  ) fx,\n"*/
			+ "  (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n"
			+ "     (select distinct diancxxb_id from  "+yb_zb+"\n"
			+ " where (riq=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd') or \n"
			+ "riq=last_year_today(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')))\n"
			+ "     ) dcid,(select decode(1,1,'累计','') as fenx,1 as xuh  from dual" +
					") fx,diancxxb dc"
			+ dianckjmx_bm
			+ "\n"
			+ "     where dc.id=dcid.diancxxb_id  "
			+ dianckjmx_tj
			+ strGongsID
			+ "  ) fx,\n"
			+ "   ((select decode(1,1,'累计','') as fenx,y.diancxxb_id,"+shul+"\n"
			+ "     from  "+yb_zb+" y,diancxxb dc\n"
			+ "     where y.riq=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n"
			+ "     and y.fenx='累计' "+zhuangt+" and y.diancxxb_id=dc.id\n"
			+ "     group by (y.diancxxb_id))) bq,\n"
			+ "\n"
			+ "     ((select decode(1,1,'累计','') as fenx,y.diancxxb_id,"+shul+"\n"
			+ "     from  "+yb_zb+" y,diancxxb dc\n"
			+ "     where y.riq=last_year_today(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'))\n"
			+ "     and y.fenx='累计' "+zhuangt+" and y.diancxxb_id=dc.id\n"
			+ "     group by (y.diancxxb_id))) tq,\n" + dianc 
			+ "where fx.diancxxb_id=dc.id(+) "+tiaoj+" \n"
			+ "  and fx.diancxxb_id=bq.diancxxb_id(+)\n"
			+ "  and fx.diancxxb_id=tq.diancxxb_id(+)\n"
			+ "  and fx.fenx=bq.fenx(+)\n"
			+ "  and fx.fenx=tq.fenx(+)  "+strGongsID+"\n" +  fenz;
        	
        }else{
        	strSQL = "select "+biaot+",\n"
			//+ "       fx.fenx as fenx,\n"
			+ "       Round(sum(bq.ranlzcb),3) as bq_ranlzcb,\n"                    
			+ "       Round(sum(tq.ranlzcb),3) as tq_ranlzcb,\n"
			+ "       Round(sum(bq.fadmtcb),3) as bq_fadmtcb,\n"
			+ "       Round(sum(tq.fadmtcb),3) as tq_fadmtcb,\n"
			+ "       Round(sum(bq.gongrmtcb),3) as bq_gongrmtcb,\n"
			+ "       Round(sum(tq.gongrmtcb),3) as tq_gongrmtcb,\n"
			+ "       Round(sum(bq.fadycb),3) as bq_fadycb,\n"
			+ "       Round(sum(tq.fadycb),3) as tq_fadycb,\n"
			+ "       Round(sum(bq.gongrycb),3) as bq_gongrycb,\n"
			+ "       Round(sum(tq.gongrycb),3) as tq_gongrycb,\n"
			+ "       decode(sum(bq.gongdl),0,0,Round((sum(bq.danwrlcb*bq.gongdl)/sum(bq.gongdl)),2) ) as bq_danwrlcb,\n"
			+ "       decode(sum(tq.gongdl),0,0,Round((sum(tq.danwrlcb*tq.gongdl)/sum(tq.gongdl)),2) ) as tq_danwrlcb,"+strzt+"\n"
			+ "  from\n"
			/*+ "  (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n"
			+ "     (select distinct diancxxb_id from yuezbb\n"
			+ "             where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')))\n"
			+ "     ) dcid,(select decode(1,1,'本月','') as fenx,1 as xuh  from dual union select decode(1,1,'累计','')  as fenx,2 as xhu from dual ) fx,diancxxb dc"+dianckjmx_bm+"\n"  
			+ "     where dc.id=dcid.diancxxb_id  "+dianckjmx_tj+strGongsID+"  ) fx,\n"*/ 
			+ "  (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n"
			+ "     (select distinct diancxxb_id from  "+yb_zb+"\n"
			+ "where (riq>=to_date('"
			+ intyear
			+ "-"
			+ intMonth
			+ "-01','yyyy-mm-dd') and riq<=to_date('"
			+ intyear2
			+ "-"
			+ intMonth2
			+ "-01','yyyy-mm-dd') or (riq>=last_year_today(to_date('"
			+ intyear
			+ "-"
			+ intMonth
			+ "-01','yyyy-mm-dd')) and riq<=last_year_today(to_date('"
			+ intyear2
			+ "-"
			+ intMonth2
			+ "-01','yyyy-mm-dd'))))\n"
			+ "     ) dcid,(select decode(1,1,'本月','') as fenx,1 as xuh  from dual" +
					") fx,diancxxb dc"
			+ dianckjmx_bm
			+ "\n"
			+ "     where dc.id=dcid.diancxxb_id  "
			+ dianckjmx_tj
			+ strGongsID
			+ "  ) fx,\n"
			+ "(( select decode(1,1,'本月','') as fenx,y.diancxxb_id,"+shul+"\n"
			+ "     from  "+yb_zb+" y,diancxxb dc\n"
			+ "     where y.riq>=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and y.riq<=to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd')\n"
			+ "     and y.fenx='本月' "+zhuangt+" and y.diancxxb_id=dc.id\n"
			+ "     group by (y.diancxxb_id))\n"
			+ ") bq,\n"
			+ "\n"
			+ "(( select decode(1,1,'本月','') as fenx,y.diancxxb_id,"+shul+"\n"
			+ "     from  "+yb_zb+" y,diancxxb dc\n"
			+ "     where y.riq>=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')) and y.riq<=last_year_today(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'))\n"
			+ "     and y.fenx='本月' "+zhuangt+" and y.diancxxb_id=dc.id\n"
			+ "     group by (y.diancxxb_id))\n"
			+ ") tq,\n" + dianc 
			+ "where fx.diancxxb_id=dc.id(+) "+tiaoj+" \n"
			+ "  and fx.diancxxb_id=bq.diancxxb_id(+)\n"
			+ "  and fx.diancxxb_id=tq.diancxxb_id(+)\n"
			+ "  and fx.fenx=bq.fenx(+)\n"
			+ "  and fx.fenx=tq.fenx(+)  "+strGongsID+"\n" +  fenz;
		}

		return strSQL;
	}

}



