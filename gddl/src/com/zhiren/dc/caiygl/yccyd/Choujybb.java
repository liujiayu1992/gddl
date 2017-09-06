package com.zhiren.dc.caiygl.yccyd;

import com.zhiren.common.*;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.dc.diaoygl.AutoCreateDaily_Report_gd;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*
 * 作者：贾少伟
 * 时间：2017-06-23
 * 描述：
 *     根据国电需要新增抽检样月报表
 */

public class Choujybb extends BasePage {

//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}

	public boolean getRaw() {
		return true;
	}
	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}
	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}


	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		setMsg(null);
		return getShouhc();
	}


	private String zhi ="";
	public String getZhi() {
		if(this.zhi.equals("")){
			setZhi();
		}
			return zhi;
	}
	public void setZhi() {
		Visit v = (Visit)getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select zhi from xitxxb where mingc = '日报隐藏电厂名称' and diancxxb_id ="+v.getDiancxxb_id());
		if(rsl.next()){
			this.zhi="  "+rsl.getString("zhi");
		}else{
			this.zhi=" -1 ";
		}
		rsl.close();
		con.Close();
	}

	private String getShouhc() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));
		String riq1=FormatDate(DateUtil.getDate(getEndriqDate()));
		//int jib=this.getDiancTreeJib();
		StringBuffer strSQL = new StringBuffer();
		String beginRiq=this.getBeginriqDate();
		String endRiq=this.getEndriqDate();
		if(beginRiq.equals(endRiq)){
			beginRiq=DateUtil.Formatdate("yyyy-MM-dd", DateUtil.getFirstDayOfMonth(DateUtil.getDate(endRiq)));
		}

		String sql="select * from(\n" +
				"select rownum rm,'原分析日期' riqmc, huaysj,huaybh,mad,aad,ad,ad_cz,vad,vdaf,vadf_cz\n" +
				",stad,std, std_cz,qbad,qgrad, qgrvd_cz, beiz from \n" +
				"\n" +
				"(\n" +
				"select to_char(z.huaysj,'yyyy-mm-dd') huaysj,z.huaybh,z.mad,z.aad,z.ad,c.ad-z.ad ad_cz,z.vad,z.vdaf,c.vdaf-z.vdaf \n" +
				"vadf_cz,z.stad,z.std,c.std-z.std std_cz,z.qbad,z.qgrad,c.qgrad-z.qgrad qgrvd_cz, z.beiz, \n" +
				"to_char(c.huaysj,'yyyy-mm-dd') huaysj_c ,c.huaybh huaybh_c,c.mad mad_c,c.aad aad_c,c.ad ad_c,c.vad vad_c,c.vdaf vdaf_c,c.stad stad_c,c.std std_c,c.qbad qbad_c,c.qgrad qgrad_c, c.beiz \n" +
				"beiz_c from zhuanmldb m\n" +
				"left join zhilb z on  m.meiyybm=z.huaybh\n" +
				"left join zhilb_cj c on m.choujbm=c.huaybh\n" +
				"where z.huaysj between date'"+this.getBeginriqDate()+"' and date'"+this.getEndriqDate()+"'\n" +
				")\n" +
				" \n" +
				"union all\n" +
				" select rownum rm,'抽检日期' riqmc, huaysj,huaybh,mad,aad,ad,ad_cz,vad,vdaf,vadf_cz\n" +
				",stad,std, std_cz,qbad,qgrad, qgrvd_cz, beiz from \n" +
				"(\n" +
				"select to_char(c.huaysj,'yyyy-mm-dd') huaysj,c.huaybh,c.mad,c.aad,c.ad,c.ad-z.ad ad_cz,c.vad,c.vdaf,c.vdaf-z.vdaf \n" +
				"vadf_cz,c.stad,c.std,c.std-z.std std_cz,c.qbad,c.qgrad,c.qgrad-z.qgrad qgrvd_cz, c.beiz from zhuanmldb m\n" +
				"left join zhilb z on  m.meiyybm=z.huaybh\n" +
				"left join zhilb_cj c on m.choujbm=c.huaybh\n" +
				"where z.huaysj between date'"+this.getBeginriqDate()+"' and date'"+this.getEndriqDate()+"'\n" +
				")\n" +
				")\n" +
				"\n" +
				"order by rm,riqmc desc";


		//System.out.println(sbsql);


		ResultSetList rs = con.getResultSetList(sql);
		Report rt = new Report();

		String ArrHeader[][]=new String[3][20];
		ArrHeader[0]=new String[] {"序号","日期","日期","样品编码","分析项目","分析项目","分析项目","分析项目","分析项目","分析项目","分析项目","分析项目","分析项目","分析项目","分析项目","分析项目","分析项目","备注"};
		ArrHeader[1]=new String[] {"序号","日期","日期","样品编码","水分","灰分","灰分","","挥发分","挥发分","","全硫","全硫","","发热量","发热量","","备注"};
		ArrHeader[2]=new String[] {"序号","日期","日期","样品编码","Mad(%)","Aad(%)","Ad(%)","差值","Vad(%)","Vdaf(%)","差值","St,ad(%)","St,d(%)","差值","Qb,ad(MJ/kg)","Qgr,v,d(MJ/kg)","差值","备注"};


		int ArrWidth[]=new int[] {80,90,90,100,65,65,65,65,65,65,65,65,65,65,65,65,65,120};
		//rs.beforefirst();
		rt.setBody(new Table(rs, 3, 0, 0));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.setTitle(getDiancmc(String.valueOf(visit.getDiancxxb_id()))+"",ArrWidth);//获取电厂名称
		rt.setDefaultTitle(1, 3, "填报单位:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 4, riq+"至"+riq1, Table.ALIGN_CENTER);
		//rt.setDefaultTitle(8, 4, "单位:吨、元/吨、MJ/Kg、万千瓦时、吉焦", Table.ALIGN_RIGHT);

		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 1, "主管：", Table.ALIGN_CENTER);
		rt.setDefautlFooter(7, 1, "审核：", Table.ALIGN_CENTER);
		rt.setDefautlFooter(14, 1, "制表：", Table.ALIGN_CENTER);
		//rt.body.setPageRows(21);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
   if (_AllPages == 0) {
			_CurrentPage = 0;
		}
	con.Close();

	return rt.getAllPagesHtml();

}

	private String getShouhcDetail(){
		JDBCcon con = new JDBCcon();
		StringBuffer SQL = new StringBuffer();
		String beginRiq=this.getBeginriqDate();
		String endRiq=this.getEndriqDate();
		String sr="LJ";
//		如果日期为当日，则显示当日的煤价等信息，否则显示累计信息
		if(beginRiq.equals(endRiq)){
			beginRiq=DateUtil.Formatdate("yyyy-MM-dd", DateUtil.getFirstDayOfMonth(DateUtil.getDate(endRiq)));
			sr="DR";
		}
		SQL.append("SELECT DANW,DQ,GONGYS,MEIK,JIHKJ,PINZ,YUNSMC,\n");
		SQL.append("MEIJ,YUNJ,REZ,\n");
		SQL.append("DECODE(REZ,0,0,ROUND((MEIJ+YUNJ)/REZ*29.271,2))HSBMDJ,\n");
		SQL.append("DECODE(REZ,0,0,ROUND((MEIJ+YUNJ-MEIJS-YUNJS)/REZ*29.271,2))BHSBMDJ,DR,LJ\n");
		SQL.append("FROM(SELECT \n");
		if(getBBLXValue().getId()==1){
			SQL.append("	   DECODE(GROUPING(dc.mingc), 1, '总计', dc.mingc) danw,\n");
			SQL.append("	   DECODE(GROUPING(SR.DQ) + GROUPING(dc.mingc), 1, '单位小计', SR.DQ) DQ,\n");
			SQL.append("	   DECODE(GROUPING(SR.DQ) + GROUPING(SR.GONGYS), 1, '小计',SR.GONGYS) GONGYS,\n");
		}else{
			SQL.append("	   DECODE(GROUPING(SR.DQ), 1, '总计', SR.DQ) dq,\n");
			SQL.append("	   DECODE(GROUPING(SR.DQ) + GROUPING(dc.mingc), 1, '地区小计', dc.mingc) danw,\n");
			SQL.append("	   DECODE(GROUPING(dc.mingc) + GROUPING(SR.GONGYS), 1, '小计',SR.GONGYS) GONGYS,\n");
		}

		SQL.append("       SR.MEIK,\n");
		SQL.append("       SR.JIHKJ,\n");
		SQL.append("       SR.PINZ,\n");
		SQL.append("       SR.YUNSMC YUNSMC,\n");
//		SQL.append("       ROUND_NEW(DECODE(SUM(DECODE(SR.MEIJ,0,0,LAIMSLMJ)),0,0,SUM(SR.MEIJ * LAIMSLMJ) /SUM(DECODE(SR.MEIJ,0,0,LAIMSLMJ))),2) MEIJ,\n"); 
//		SQL.append("       ROUND_NEW(DECODE(SUM(DECODE(SR.MEIJ, 0, 0, SR.LAIMSLMJ)),0,0,SUM(DECODE(SR.MEIJ, 0, 0, SR.LAIMSLMJ) * SR.YUNJ) /SUM(DECODE(SR.MEIJ, 0, 0, LAIMSLMJ))),2) YUNJ,\n"); 
		SQL.append("       ROUND_NEW(DECODE(SUM(DECODE(SR.MEIJ,0,DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ),SR.LAIMSLMJ)),0,0,SUM(DECODE(SR.MEIJ, 0, DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ), SR.LAIMSLMJ)* SR.MEIJ) /SUM(DECODE(SR.MEIJ,0,DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ),LAIMSLMJ))),2) MEIJ,\n");
		SQL.append("       ROUND_NEW(DECODE(SUM(DECODE(SR.MEIJ, 0, DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ), SR.LAIMSLMJ)),0,0,SUM(DECODE(SR.MEIJ, 0, DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ), SR.LAIMSLMJ) * SR.YUNJ) /SUM(DECODE(SR.MEIJ,0,DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ),LAIMSLMJ))),2) YUNJ,\n");
		SQL.append("       ROUND_NEW(DECODE(SUM(DECODE(SR.MEIJ,0,DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ),SR.LAIMSLMJ)),0,0,SUM(DECODE(SR.MEIJ, 0, DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ), SR.LAIMSLMJ)* SR.MEIJS) /SUM(DECODE(SR.MEIJ,0,DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ),LAIMSLMJ))),2) MEIJS,\n");
		SQL.append("       ROUND_NEW(DECODE(SUM(DECODE(SR.MEIJ, 0, DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ), SR.LAIMSLMJ)),0,0,SUM(DECODE(SR.MEIJ, 0, DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ), SR.LAIMSLMJ) * SR.YUNJS) /SUM(DECODE(SR.MEIJ,0,DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ),LAIMSLMJ))),2) YUNJS,\n");
		SQL.append("       ROUND_NEW(DECODE(SUM(DECODE(SR.REZ,0,0,LAIMSLREZ)),0,0,SUM(SR.REZ * SR.LAIMSLREZ) /SUM(DECODE(SR.REZ,0,0,LAIMSLREZ))),2) REZ,\n");
		if(MainGlobal.getXitxx_item("收耗存日报", "是否对日报报表汇总信息取整", "0", "否").equals("否")){
			SQL.append("       SUM(SR.DR) DR,\n");
			SQL.append("       SUM(SR.LJ) LJ\n");
		}else{
			SQL.append("       DECODE(GROUPING(SR.DQ) + GROUPING(SR.MEIK), 2,round_new(SUM(SR.DR),0),SUM(SR.DR)) DR,\n");
			SQL.append("       DECODE(GROUPING(SR.DQ) + GROUPING(SR.MEIK), 2,round_new(SUM(SR.LJ),0),SUM(SR.LJ)) lj\n");
		}
		SQL.append("  FROM (SELECT LJ.DIANCID,LJ.DQ,\n");
		SQL.append("               LJ.GONGYS,\n");
		SQL.append("               LJ.MEIK,\n");
		SQL.append("               LJ.JIHKJ,\n");
		SQL.append("               LJ.PINZ,\n");
		SQL.append("               DECODE(LJ.DIANCID,202,'海运',DECODE(LJ.YUNSMC,'', '其它', LJ.YUNSMC)) YUNSMC,\n");
		SQL.append("               "+sr+".MEIJ MEIJ,\n");
		SQL.append("               "+sr+".YUNJ YUNJ,\n");
		SQL.append("               "+sr+".MEIJS MEIJS,\n");
		SQL.append("               "+sr+".YUNJS YUNJS,\n");
		SQL.append("               "+sr+".REZ REZ,\n");
		SQL.append("               "+sr+".LAIMSLMJ LAIMSLMJ,\n");
		SQL.append("               "+sr+".LAIMSLREZ LAIMSLREZ,\n");
		SQL.append("               DR.LAIMSL DR,\n");
		SQL.append("               LJ.LAIMSL LJ\n");
		SQL.append("          FROM (SELECT DECODE(GROUPING(DQ.MINGC),1,'',DECODE(DQ.MINGC, NULL, '其它', DQ.MINGC)) DQ,\n");
		SQL.append("                       DECODE(GROUPING(GYS2.MINGC),1,'',DECODE(GYS2.MINGC, NULL, '其它', GYS2.MINGC)) GONGYS,\n");
		SQL.append("                       DECODE(GROUPING(MK.MINGC),1,'',DECODE(MK.MINGC, NULL, '其它', MK.MINGC)) MEIK,\n");
		SQL.append("                       DECODE(GROUPING(J.MINGC),1,'',DECODE(J.MINGC, NULL, '其它', J.MINGC)) JIHKJ,\n");
		SQL.append("                       DECODE(GROUPING(P.MINGC),1,'',DECODE(P.MINGC, NULL, '其它', P.MINGC)) PINZ,\n");
		SQL.append("                       DECODE(GROUPING(YSFS.MINGC),1,'',DECODE(YSFS.MINGC, NULL, '其它', YSFS.MINGC)) YUNSMC,\n");
		SQL.append("                       DIANC.ID DIANCID,\n");
//		SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL)),0,0,SUM(SHC.MEIJ * SHC.LAIMSL) /SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL))),2) MEIJ,\n"); 
//		SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ, 0, 0,SHC.YUNJ ) *SHC.LAIMSL ) /SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL))),2) YUNJ,\n");
		SQL.append("					   ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL) * SHC.MEIJ) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) MEIJ,\n");
		SQL.append("					   ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL) * SHC.YUNJ) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) YUNJ,\n");
		SQL.append("					   ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL) * SHC.MEIJS) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) MEIJS,\n");
		SQL.append("					   ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL) * SHC.YUNJS) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) YUNJS,\n");
		SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.REZ,0,0,SHC.LAIMSL)),0,0,SUM(SHC.REZ * SHC.LAIMSL) /SUM(DECODE(SHC.REZ,0,0,SHC.LAIMSL))),2) REZ,\n");
		SQL.append("                       SUM(SHC.LAIMSL) LAIMSL,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)) LAIMSLMJ,SUM(DECODE(SHC.REZ,0,0,SHC.LAIMSL))  LAIMSLREZ\n");
		SQL.append("                  FROM SHOUHCFKB SHC,\n");
		SQL.append("                       MEIKXXB MK,\n");
		SQL.append("                       GONGYSB GYS,\n");
		SQL.append("                       GONGYSB GYS2,\n");
		SQL.append("                       YUNSFSB YSFS,\n");
		SQL.append("                       JIHKJB J,\n");
		SQL.append("                       PINZB P,\n");
		SQL.append("                       MEIKDQB DQ,\n");
//		SQL.append("                       (SELECT S.ID, MAX(F.YUNSFSB_ID) YUNSFSB_ID\n"); 
//		SQL.append("                          FROM FAHB F, SHOUHCFKB S\n"); 
//		SQL.append("                         WHERE S.DIANCXXB_ID = F.DIANCXXB_ID\n"); 
//		SQL.append("                           AND S.RIQ = F.DAOHRQ\n"); 
//		SQL.append("                           AND S.MEIKXXB_ID = F.MEIKXXB_ID\n"); 
//		SQL.append("                           AND S.GONGYSB_ID = F.GONGYSB_ID\n"); 
//		SQL.append("                           AND S.JIHKJB_ID = F.JIHKJB_ID\n"); 
//		SQL.append("                           AND S.PINZB_ID = F.PINZB_ID\n"); 
//		SQL.append("                   		   AND S.RIQ = "+DateUtil.FormatOracleDate(endRiq)+"\n"); 
//		SQL.append("                         GROUP BY S.ID) FAH,\n"); 
		SQL.append("                       (SELECT ID\n");
		SQL.append("                          FROM VWDIANC\n");
		SQL.append("                         WHERE ID IN ("+getTreeid()+")\n");
		SQL.append("                           AND VWDIANC.ID NOT IN ("+this.getZhi()+")) DIANC\n");
//		SQL.append("                 WHERE SHC.ID = FAH.ID(+)\n"); 
		SQL.append("                   WHERE SHC.YUNSFSB_ID = YSFS.ID(+)\n");
		SQL.append("                   AND GYS2.ID = SHC.GONGYSB_ID\n");
		SQL.append("                   AND SHC.PINZB_ID = P.ID(+)\n");
		SQL.append("                   AND GYS.FUID = DQ.ID(+)\n");
		SQL.append("                   AND SHC.MEIKXXB_ID = MK.ID(+)\n");
		SQL.append("                   AND SHC.JIHKJB_ID = J.ID(+)\n");
		SQL.append("                   AND MK.MEIKDQ_ID = GYS.ID\n");
		SQL.append("                   AND SHC.DIANCXXB_ID = DIANC.ID\n");
		SQL.append("                   AND SHC.RIQ = "+DateUtil.FormatOracleDate(endRiq)+"\n");
		SQL.append("                 GROUP BY ROLLUP((DQ.MINGC, GYS2.MINGC, MK.MINGC, J.MINGC,P.MINGC, YSFS.MINGC, DIANC.ID))\n");
		SQL.append("                HAVING NOT GROUPING(DQ.MINGC) = 1) DR,\n");
		SQL.append("               (SELECT DECODE(GROUPING(DQ.MINGC),1,'',DECODE(DQ.MINGC, NULL, '其它', DQ.MINGC)) DQ,\n");
		SQL.append("                       DECODE(GROUPING(GYS2.MINGC),1,'',DECODE(GYS2.MINGC, NULL, '其它', GYS2.MINGC)) GONGYS,\n");
		SQL.append("                       DECODE(GROUPING(MK.MINGC),1,'',DECODE(MK.MINGC, NULL, '其它', MK.MINGC)) MEIK,\n");
		SQL.append("                       DECODE(GROUPING(J.MINGC),1,'',DECODE(J.MINGC, NULL, '其它', J.MINGC)) JIHKJ,\n");
		SQL.append("                       DECODE(GROUPING(P.MINGC),1,'',DECODE(P.MINGC, NULL, '其它', P.MINGC)) PINZ,\n");
		SQL.append("                       DECODE(GROUPING(YSFS.MINGC),1,'',DECODE(YSFS.MINGC, NULL, '其它', YSFS.MINGC)) YUNSMC,\n");
		SQL.append("                       DIANC.ID DIANCID,\n");
//		SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL)),0,0,SUM(SHC.MEIJ * SHC.LAIMSL) /SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL))),2) MEIJ,\n"); 
//		SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ, 0, 0,SHC.YUNJ ) *SHC.LAIMSL ) /SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL))),2) YUNJ,\n");
		SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)*SHC.MEIJ) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) MEIJ,\n");
		SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL) * SHC.YUNJ) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) YUNJ,\n");
		SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)*SHC.MEIJS) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) MEIJS,\n");
		SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL) * SHC.YUNJS) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) YUNJS,\n");
		SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.REZ,0,0,SHC.LAIMSL)),0,0,SUM(SHC.REZ * SHC.LAIMSL) /SUM(DECODE(SHC.REZ,0,0,SHC.LAIMSL))),2) REZ,\n");
		SQL.append("                       SUM(SHC.LAIMSL) LAIMSL,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)) LAIMSLMJ,SUM(DECODE(SHC.REZ,0,0,SHC.LAIMSL))  LAIMSLREZ\n");
		SQL.append("                  FROM SHOUHCFKB SHC,\n");
		SQL.append("                       MEIKXXB MK,\n");
		SQL.append("                       GONGYSB GYS,\n");
		SQL.append("                       GONGYSB GYS2,\n");
		SQL.append("                       YUNSFSB YSFS,\n");
		SQL.append("                       JIHKJB J,\n");
		SQL.append("                       PINZB P,\n");
		SQL.append("                       MEIKDQB DQ,\n");
//		SQL.append("                       (SELECT S.ID, MAX(F.YUNSFSB_ID) YUNSFSB_ID\n"); 
//		SQL.append("                          FROM FAHB F, SHOUHCFKB S\n"); 
//		SQL.append("                         WHERE S.DIANCXXB_ID = F.DIANCXXB_ID\n"); 
//		SQL.append("                           AND S.RIQ = F.DAOHRQ\n"); 
//		SQL.append("                           AND S.MEIKXXB_ID = F.MEIKXXB_ID\n"); 
//		SQL.append("                           AND S.GONGYSB_ID = F.GONGYSB_ID\n"); 
//		SQL.append("                           AND S.JIHKJB_ID = F.JIHKJB_ID\n"); 
//		SQL.append("                           AND S.PINZB_ID = F.PINZB_ID\n"); 
//		SQL.append("    			  		   AND S.RIQ <= "+DateUtil.FormatOracleDate(endRiq)+"\n");
//		SQL.append("    			   		   AND S.RIQ >= "+DateUtil.FormatOracleDate(beginRiq)+"\n");
//		SQL.append("                         GROUP BY S.ID) FAH,\n"); 
		SQL.append("                       (SELECT ID\n");
		SQL.append("                          FROM VWDIANC\n");
		SQL.append("                         WHERE ID IN ("+getTreeid()+")\n");
		SQL.append("                           AND VWDIANC.ID NOT IN ("+this.getZhi()+")) DIANC\n");
//		SQL.append("                 WHERE SHC.ID = FAH.ID(+)\n"); 
		SQL.append("                   WHERE SHC.YUNSFSB_ID = YSFS.ID(+)\n");
		SQL.append("                   AND SHC.PINZB_ID = P.ID(+)\n");
		SQL.append("                   AND GYS2.ID = SHC.GONGYSB_ID\n");
		SQL.append("                   AND GYS.FUID = DQ.ID(+)\n");
		SQL.append("                   AND SHC.MEIKXXB_ID = MK.ID(+)\n");
		SQL.append("                   AND SHC.JIHKJB_ID = J.ID(+)\n");
		SQL.append("                   AND MK.MEIKDQ_ID = GYS.ID\n");
		SQL.append("                   AND SHC.DIANCXXB_ID = DIANC.ID\n");
		SQL.append("    			   AND SHC.RIQ <= "+DateUtil.FormatOracleDate(endRiq)+"\n");
		SQL.append("    			   AND SHC.RIQ >= "+DateUtil.FormatOracleDate(beginRiq)+"\n");
		SQL.append("                 GROUP BY ROLLUP((DQ.MINGC, GYS2.MINGC, MK.MINGC, J.MINGC,P.MINGC, YSFS.MINGC, DIANC.ID))\n");
		SQL.append("                HAVING NOT GROUPING(DQ.MINGC) = 1) LJ\n");
		SQL.append("         WHERE DR.MEIK(+) = LJ.MEIK\n");
		SQL.append("           AND DR.GONGYS(+) = LJ.GONGYS\n");
		SQL.append("           AND DR.YUNSMC(+) = LJ.YUNSMC\n");
		SQL.append("           AND DR.JIHKJ(+) = LJ.JIHKJ\n");
		SQL.append("           AND DR.DQ(+) = LJ.DQ\n");
		SQL.append("           AND DR.PINZ(+) = LJ.PINZ\n");
		SQL.append("           AND DR.DIANCID(+) = LJ.DIANCID) SR, diancxxb dc WHERE sr.diancid=dc.id\n");
		if(getBBLXValue().getId()==1){
			SQL.append(" GROUP BY ROLLUP((dc.mingc,dc.xuh),SR.DQ, (SR.GONGYS, SR.MEIK, SR.JIHKJ, SR.PINZ, SR.YUNSMC))\n");
			SQL.append(" ORDER BY GROUPING(dc.xuh) DESC,dc.xuh,GROUPING(SR.DQ) DESC, SR.DQ, GROUPING(SR.GONGYS) DESC, SR.GONGYS");
		}else{
			SQL.append(" GROUP BY ROLLUP(SR.DQ,(dc.mingc,dc.xuh),(SR.GONGYS, SR.MEIK, SR.JIHKJ, SR.PINZ, SR.YUNSMC))\n");
			SQL.append("ORDER BY GROUPING(SR.DQ) DESC, SR.DQ, GROUPING(dc.xuh) DESC,dc.xuh,GROUPING(SR.GONGYS) DESC, SR.GONGYS\n");
		}
		SQL.append(")SR");

		ResultSetList rs = con.getResultSetList(SQL.toString());
		Report rt = new Report();


		//rs.beforefirst();
		rt.setBody(new Table(rs, 3, 0, 0));

		rt.body.mergeFixedRow();
		rt.body.mergeFixedCol(1);
		rt.body.mergeFixedCol(2);
		rt.body.ShowZero=true;
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.setColAlign(4, Table.ALIGN_LEFT);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_CENTER);


		rt.setDefautlFooter(2, 1, "主管：", Table.ALIGN_CENTER);
		rt.setDefautlFooter(3, 1, "审核：", Table.ALIGN_CENTER);
		rt.setDefautlFooter(6, 1, "制表：", Table.ALIGN_CENTER);

		_CurrentPage = 1;
		_AllPages =rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
			+ "';";
		} else {
			return "";
		}
	}
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
//	日期设置开始
	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}

	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			Calendar stra=Calendar.getInstance();
			int T_Date=DateUtil.getDay(new Date());
			if(T_Date<4){
				T_Date=1;
			}else{
				T_Date=T_Date-2;
			}
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), T_Date);
			stra.add(Calendar.MONTH,-1);
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}

	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
	}
//	日期设置结束
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();

		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1","");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);
		tb1.addText(new ToolbarText("至"));
		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2","");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowCheck_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),null,true);

		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		String[] str=getTreeid().split(",");
		if(str.length>1){
			tf.setValue("组合电厂");
		}else{
			tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
		}

		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

		tb1.addText(new ToolbarText("-"));

		//tb1.addText(new ToolbarText("报表类型:"));
		/*ComboBox cbo_bblx = new ComboBox();
		cbo_bblx.setTransform("BBLXDropDown");
		cbo_bblx.setWidth(120);*/
		//tb1.addField(cbo_bblx);
		tb1.addText(new ToolbarText("-"));
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.addItem(tb);

//		再刷新时给用户提示是否存在不合理数据
		setMsg(null);
		Date beginRiq=DateUtil.getDate(this.getBeginriqDate());
		Date endRiq=DateUtil.getDate(this.getEndriqDate());
//		如果日期为当日，则显示当日的煤价等信息，否则显示累计信息
		AutoCreateDaily_Report_gd DR=new AutoCreateDaily_Report_gd();
		JDBCcon con = new JDBCcon();
		String errmsg=DR.RPChk(con, getTreeid(),beginRiq,endRiq );
//		if(errmsg.length()>0){
//			setMsg(errmsg+"信息不完全，请注意");
//		}
		con.Close();
		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			setZhi();
			return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setExtTree1(null);
			visit.setString4(null);
			visit.setString5(null);
			setTreeid(null);
			setBBLXModel(null);
			setBBLXValue(null);
			setZhi();
//			如果登录用户为国电电力，那么单位下拉框默认为全部单位。
			if(visit.getDiancxxb_id()==112){
				initDiancTree();
			}
		}
		getToolbars();
		blnIsBegin = true;

	}

//	新增报表类型下拉框
	// 盘点编号下拉框
	public IDropDownBean getBBLXValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
				((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean) getBBLXModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setBBLXValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setBBLXModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getBBLXModel() {
			getIBBLXModels();
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getIBBLXModels() {
		List list= new ArrayList();
		list.add(new IDropDownBean(1, "按单位汇总"));
		list.add(new IDropDownBean(2, "按煤源地区汇总"));
		((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(list));
	}

	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}
//	电厂名称
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

    private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}
		return jib;
	}
//	得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}
//	分公司下拉框
//	private boolean _fengschange = false;

	public IDropDownBean getFengsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
			.setDropDownBean4((IDropDownBean) getFengsModel()
					.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setFengsValue(IDropDownBean Value) {
//		if (getFengsValue().getId() != Value.getId()) {
//			_fengschange = true;
//		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public IPropertySelectionModel getFengsModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getFengsModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setDiancxxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getFengsModels() {
		String sql;
		sql = "select id ,mingc from diancxxb where jib=2 order by id";
		setDiancxxModel(new IDropDownModel(sql,"中国大唐集团"));
	}

//	得到系统信息表中配置的报表标题的单位名称
	public String getBiaotmc(){
		String biaotmc="";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc="select  zhi from xitxxb where mingc='报表标题单位名称'";
		ResultSet rs=cn.getResultSet(sql_biaotmc);
		try {
			while(rs.next()){
				biaotmc=rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return biaotmc;

	}
	 //得到登陆人员所属电厂或分公司的名称
	public String getDiancmc(String diancId) {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		//long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		cn.Close();
		return diancmc;

	}
	public String getDiancmc(){
		String[] str=getTreeid().split(",");
		if(str.length>1){
			return "组合电厂";
		}else{
			return getDiancmc(str[0]);
		}
	}
	//
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript()+getOtherScript("diancTree");
	}

//	初始化多选电厂树中的默认值
	private void initDiancTree(){
		Visit visit = (Visit) getPage().getVisit();
		String sql="SELECT ID\n" +
			"  FROM DIANCXXB\n" +
			" WHERE JIB > 2\n" +
			" START WITH ID = "+visit.getDiancxxb_id()+"\n" +
			"CONNECT BY FUID = PRIOR ID";

		JDBCcon con=new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		String TreeID="";
		while(rsl.next()){
			TreeID+=rsl.getString("ID")+",";
		}
		if(TreeID.length()>1){
			TreeID=TreeID.substring(0, TreeID.length()-1);
			setTreeid(TreeID);
		}else{
			setTreeid(visit.getDiancxxb_id() + "");
		}
		rsl.close();
		con.Close();
	}

//	增加电厂多选树的级联
	public String getOtherScript(String treeid){
		String str=" var "+treeid+"_history=\"\";\n"+
			treeid+"_treePanel.on(\"checkchange\",function(node,checked){\n" +
				"    if(checked){\n" +
				"      addNode(node);\n" +
				"    }else{\n" +
				"      subNode(node);\n" +
				"    }\n" +
				"    node.expand();\n" +
				"    node.attributes.checked = checked;\n" +
				"    node.eachChild(function(child) {\n" +
				"      if(child.attributes.checked != checked){\n" +
				"        if(checked){\n" +
				"          addNode(child);\n" +
				"        }else{\n" +
				"          subNode(child);\n" +
				"        }\n" +
				"        child.ui.toggleCheck(checked);\n" +
				"              child.attributes.checked = checked;\n" +
				"              child.fireEvent('checkchange', child, checked);\n" +
				"      }\n" +
				"    });\n" +
				"  },"+treeid+"_treePanel);\n" +
				"  function addNode(node){\n" +
				"    var history = '+,'+node.id+\";\";\n" +
				"    writesrcipt(node,history);\n" +
				"  }\n" +
				"\n" +
				"  function subNode(node){\n" +
				"    var history = '-,'+node.id+\";\";\n" +
				"    writesrcipt(node,history);\n" +
				"  }\n" +
				"function writesrcipt(node,history){\n" +
				"\t\tif("+treeid+"_history==\"\"){\n" +
				"\t\t\t"+treeid+"_history = history;\n" +
				//" 	   document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n"	+
				"\t\t}else{\n" +
				"\t\t\tvar his = "+treeid+"_history.split(\";\");\n" +
				"\t\t\tvar reset = false;\n" +
				"\t\t\tfor(i=0;i<his.length;i++){\n" +
				"\t\t\t\tif(his[i].split(\",\")[1] == node.id){\n" +
				"\t\t\t\t\this[i] = \"\";\n" +
				"\t\t\t\t\treset = true;\n" +
				"\t\t\t\t\tbreak;\n" +
				"\t\t\t\t}\n" +
				"\t\t\t}\n" +
				"\t\tif(reset){\n" +
				"\t\t\t  "+treeid+"_history = his.join(\";\");\n" +
				//"      	 document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }else{\n" +
				"      	 "+treeid+"_history += history;\n" +
				//"        document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }\n" +
				"  }\n" +
				"\n" +
				"}";
		return str;
	}
}