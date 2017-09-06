 package com.zhiren.shanxdted.baob.kuidkkb;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;


/*
 * 作者：夏峥
 * 时间：2014-02-28
 * 描述：处理除数为0的Bug
 */
/*
 * 作者：夏峥
 * 时间：2014-04-03
 * 描述：调整排序方式和顺序
 */
public class Chengycdcx_pm extends BasePage implements PageValidateListener {
	
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

	// ***************设置消息框******************//
	private String _msg;
	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	public String getPrintTable(){
			return getKuangbTj();
	}
	

	private String getKuangbTj() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
		String riqi=this.getRiqi();
		String riqi2=this.getRiqi2();
		
//		单位条件
		String diancTiaoj="";
		if(this.getTreeid_dc().equals("300")){
			diancTiaoj="";
		}else{
			diancTiaoj=" and d.id in("+this.getTreeid_dc()+")\n";
		}
		
		
//		运输条件
		String tongjfs="";
		if(this.getTreeid_ysdw().equals("0")){
			tongjfs="";
		}else{
			tongjfs=" and fh.yunsdwb_id in ("+this.getTreeid_ysdw()+")\n";
		}

//		日期条件
		String riqitiaoj="";
		if(this.getRiqiValue().getValue().equals("到货日期")){
			riqitiaoj=" where F.DAOHRQ BETWEEN to_date('"+riqi+"','yyyy-mm-dd')" + 
			          " and to_date('"+riqi2+"','yyyy-mm-dd')) fh" ;
		}else{
			riqitiaoj=" where f.fahrq BETWEEN to_date('"+riqi+"','yyyy-mm-dd')" + 
	          " and to_date('"+riqi2+"','yyyy-mm-dd')) fh" ;
		}
		
//		煤矿条件
		String meiktj="";
		if(this.getTreeid_meik().equals("0")){
			meiktj=" ";
		}else{
			meiktj=" and fh.meikxxb_id in ("+this.getTreeid_meik()+") \n";
		}
		
		StringBuffer buffer = new StringBuffer();
		
		if(this.getTongjfsValue().getValue().equals("不分厂别统计")){
			buffer.append(" SELECT YSDW,MKDW,HETRZ,MEIJ,YUNF,KUANGFL,DAOCL,FLZ,SJYSL,yunsl,YSYXCB,KDFZ,KUANGFRZ,RUCRZ,JIESRZ,JRRZC,KKYXCB,KKFZ,ZFZ\n" +
					" FROM(SELECT hz.YSDW,HZ.MKDW,HETRZ,MEIJ,YUNF,KUANGFL,DAOCL,FLZ,SJYSL,YUNSL,YSYXCB,\n" +
					" ROUND(CASE WHEN YSYXCB<0 THEN 25 ELSE 25-YSYXCB*2 END,2) KDFZ,\n" +
					" KUANGFRZ,RUCRZ,JIESRZ,JRRZC,KKYXCB,ROUND(CASE WHEN KKYXCB<0 THEN 70 ELSE 70-KKYXCB*2 END,2) KKFZ\n" +
					" ,FLZ+ROUND(CASE WHEN YSYXCB<0 THEN 25 ELSE 25-YSYXCB*2 END,2) + ROUND(CASE WHEN KKYXCB<0 THEN 70 ELSE 70-KKYXCB*2 END,2) ZFZ \n" +
					" ,xj.xjz,HZ.ZT1\n" +
					" FROM(SELECT DT.ZT,DT.ZT1,DT.YSDW,DT.MKDW,\n" +
					" HETRZ,\n" +
					" MEIJ,\n" +
					" YUNF,KUANGFL,DAOCL,\n" +
					" (CASE WHEN PCT1>5 THEN 5 ELSE PCT1 END) FLZ,\n" +
					" ROUND((KUANGFL-DAOCL)/KUANGFL*100,2)SJYSL,\n" +
					" YUNSL,ROUND(((KUANGFL-DAOCL)-KUANGFL*YUNSL)*MEIJ/1.17/DAOCL,3)YSYXCB,\n" +
					" KUANGFRZ, \n" +
					" RUCRZ, \n" +
					" JIESRZ,\n" +
					" (JIESRZ-RUCRZ)JRRZC,\n" +
					" ROUND((DECODE(RUCRZ,0,0,(MEIJ/1.17+YUNF/1.11)/RUCRZ)-DECODE(JIESRZ,0,0,((MEIJ/1.17+YUNF/1.11)/JIESRZ)))*RUCRZ,3)KKYXCB \n" +
					" FROM(SELECT GROUPING(SR.YUNSDWB_ID)ZT, GROUPING(SR.MEIKXXB_ID)ZT1,\n" +
					" DECODE(GROUPING(SR.YUNSDWB_ID),1,'总计',SR.YUNSDWB_ID)YSDW,\n" +
					" DECODE(GROUPING(SR.MEIKXXB_ID)+GROUPING(SR.YUNSDWB_ID),1,'小计',SR.MEIKXXB_ID) MKDW,\n" +
					" ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.HETRZ * SR.JINGZ) / SUM(SR.JINGZ)),0)  HETRZ,\n" +
					" ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.MEIJ * SR.JINGZ) / SUM(SR.JINGZ)),2) MEIJ,\n" +
					" ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.YUNF * SR.JINGZ) / SUM(SR.JINGZ)),2) YUNF,\n" +
					" SUM(SR.BIAOZ) KUANGFL, SUM(SR.JINGZ) DAOCL, \n " +
					" ROUND(ratio_to_report(SUM(SR.JINGZ)) OVER (PARTITION BY GROUPING(SR.YUNSDWB_ID))*50,2) PCT1,\n" +
					" ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.YUNSL * SR.JINGZ) / SUM(SR.JINGZ)),3) YUNSL,\n" +
					" ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.KUANGFRZ * SR.JINGZ) / SUM(SR.JINGZ)),0) KUANGFRZ,\n" +
					" ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.RUCRZ * SR.JINGZ) / SUM(SR.JINGZ)),0) AS RUCRZ," +
					" ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.JIESRZ * SR.JINGZ) / SUM(SR.JINGZ)),0) JIESRZ\n" +
					" FROM (SELECT KK.ID, FH.ID AS FID, D.MINGC  AS DIANCXXB_ID,\n" +
					" MK.MINGC AS MEIKXXB_ID,FH.MEIKXXB_ID AS MEIKXXB_ID2,YS.MINGC AS YUNSDWB_ID,\n" +
					" FH.YUNSDWB_ID AS YUNSDWB_ID2,FH.DAOHRQ,FH.FAHRQ,\n" +
					" DECODE(KK.ID, NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetmj'),KK.HETMJ) AS HETMJ,\n" +
					" KUIDKK(MK.ID, FH.DAOHRQ, 'jiesl') AS JIESL,\n" +
					" kuidkk_ysl(MK.ID, FH.DAOHRQ, 'yunsl') AS YUNSL,\n" +
					" DECODE(KK.ID,NULL, KUIDKK_MEIJ(MK.ID,FH.DAOHRQ," +
					" DECODE(KK.ID, NULL, ROUND(ZL.QNET_AR * 1000 / 4.1816, 0),KK.RUCRZ),\n" +
					" DECODE(KK.ID,NULL, ROUND(KF.QNET_AR * 1000 / 4.1816, 0),KK.KUANGFRZ),\n"+
					" DECODE(KK.ID,NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetrz'),KK.HETRZ),FH.BIAOZ,FH.JINGZ),KK.MEIJ) AS MEIJ,\n"+
					" FH.BIAOZ,FH.JINGZ,KK.KUIDJE,KK.KUIK, KK.KUIKJE,KK.KUIDKKHJJE,\n"+
					" DECODE(KK.ID,NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetrz'),KK.HETRZ) AS HETRZ,\n" +
					" DECODE(KK.ID,NULL,ROUND(KF.QNET_AR * 1000 / 4.1816, 0),KK.KUANGFRZ) AS KUANGFRZ,\n" +
					" DECODE(KK.ID,NULL,ROUND(ZL.QNET_AR * 1000 / 4.1816, 0),KK.RUCRZ) AS RUCRZ,\n" +
					" DECODE(KK.ID,NULL,KUIDKK_JIESRZ(MK.ID,FH.DAOHRQ,DECODE(KK.ID,NULL,ROUND(ZL.QNET_AR * 1000 / 4.1816, 0), KK.RUCRZ),\n" +
					" DECODE(KK.ID,NULL,ROUND(KF.QNET_AR * 1000 / 4.1816, 0),KK.KUANGFRZ),\n" +
					" DECODE(KK.ID,NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetrz'),KK.HETRZ),\n" +
					" FH.BIAOZ,FH.JINGZ,'jiesrz'),KK.JIESRZ) AS JIESRZ,\n" +
					" DECODE(YF.ID,NULL,KUIDKK_YUNF(FH.DIANCXXB_ID,FH.MEIKXXB_ID,FH.YUNSDWB_ID,FH.DAOHRQ,'yunf'),YF.YUNF) AS YUNF\n " +
					" FROM (SELECT F.ZHILB_ID, F.KUANGFZLB_ID,F.DIANCXXB_ID, F.MEIKXXB_ID,F.ID, F.JINGZ, F.BIAOZ,F.YUNSFSB_ID,\n" +
					"	F.FAHRQ,F.DAOHRQ, NVL((SELECT (SELECT YS.ID  FROM YUNSDWB YS  WHERE YS.ID = C.YUNSDWB_ID)\n"+
					" FROM CHEPB C WHERE C.FAHB_ID = F.ID  AND ROWNUM = 1),0) YUNSDWB_ID  FROM FAHB F \n"
					+riqitiaoj+",ZHILB ZL,MEIKXXB MK,KUANGFZLB KF,VWDIANC D,KUIDKKB_WH KK, KUIDKKB_WH_YUNF YF,YUNSDWB YS \n" +
							"WHERE FH.ZHILB_ID = ZL.ID(+) AND FH.KUANGFZLB_ID = KF.ID(+) AND FH.DIANCXXB_ID = D.ID \n" +
							"AND FH.MEIKXXB_ID = MK.ID  AND FH.YUNSDWB_ID = YS.ID(+) AND FH.ID = KK.FAHB_ID(+) \n" +
							"AND FH.ID = YF.FAHB_ID(+) "+diancTiaoj+" "+meiktj+" "+tongjfs
							+" ORDER BY FH.YUNSFSB_ID, MK.MINGC, YS.MINGC) SR  \n" +
					" GROUP BY ROLLUP(SR.YUNSDWB_ID, SR.MEIKXXB_ID))DT)HZ,( \n"+
					" SELECT YSDW,MKDW,\n"+
					" nvl(DECODE(ZT,1,NULL,FLZ+ROUND(CASE WHEN YSYXCB<0 THEN 25 ELSE 25-YSYXCB*2 END,2) + ROUND(CASE WHEN KKYXCB<0 THEN 70 ELSE 70-KKYXCB*2 END,2)),0) xjz \n"+
					" from(SELECT DT.ZT,DT.YSDW,DT.MKDW,\n"+
					" DECODE(DT.ZT,1,NULL,HETRZ)HETRZ,\n"+
			        " DECODE(DT.ZT,1,NULL,MEIJ)MEIJ,\n"+
			        " DECODE(DT.ZT,1,NULL,YUNF)YUNF,KUANGFL,DAOCL,\n"+
			        " DECODE(DT.ZT,1,0,CASE WHEN PCT1>5 THEN 5 ELSE PCT1 END) FLZ,\n"+
			        " DECODE(DT.ZT,1,0,DECODE(KUANGFL,0,0,ROUND((KUANGFL-DAOCL)/KUANGFL*100,2)))SJYSL,\n"+
			        " DECODE(DT.ZT,1,0,YUNSL)YUNSL,DECODE(DT.ZT,1,0,CASE WHEN (YUNSL=0 AND DAOCL<>0) THEN 0  ELSE ROUND(((KUANGFL-DAOCL)-KUANGFL*YUNSL)*MEIJ/1.17/DAOCL,3) END) YSYXCB,\n"+
			        " DECODE(DT.ZT,1,NULL,KUANGFRZ) KUANGFRZ,\n"+
			        " DECODE(DT.ZT,1,NULL,RUCRZ)RUCRZ,\n"+
			        " DECODE(DT.ZT,1,NULL,JIESRZ)JIESRZ,\n"+
			        " DECODE(DT.ZT,1,NULL,JIESRZ-RUCRZ)JRRZC,\n"+
			        " DECODE(DT.ZT,1,0,ROUND((DECODE(RUCRZ,0,0,(MEIJ/1.17+YUNF/1.11)/RUCRZ)-DECODE(JIESRZ,0,0,((MEIJ/1.17+YUNF/1.11)/JIESRZ)))*RUCRZ,3)) KKYXCB \n"+
			        " from (SELECT GROUPING(SR.YUNSDWB_ID)ZT,\n"+ 
			        " DECODE(GROUPING(SR.YUNSDWB_ID),1,'总计',SR.YUNSDWB_ID)YSDW, \n"+
			        " DECODE(GROUPING(SR.MEIKXXB_ID)+GROUPING(SR.YUNSDWB_ID),1,'小计',SR.MEIKXXB_ID) MKDW,\n"+
			        " ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.HETRZ * SR.JINGZ) / SUM(SR.JINGZ)),0)  HETRZ,\n"+ 
			        " ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.MEIJ * SR.JINGZ) / SUM(SR.JINGZ)),2) MEIJ,\n"+
			        " ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.YUNF * SR.JINGZ) / SUM(SR.JINGZ)),2) YUNF, \n"+
			        " SUM(SR.BIAOZ) KUANGFL, SUM(SR.JINGZ) DAOCL,\n"+
			        " ROUND(ratio_to_report(SUM(SR.JINGZ)) OVER (PARTITION BY GROUPING(SR.YUNSDWB_ID))*25,2) PCT1,\n"+
			        " ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.YUNSL * SR.JINGZ) / SUM(SR.JINGZ)),3) YUNSL,\n" +
			        " ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.KUANGFRZ * SR.JINGZ) / SUM(SR.JINGZ)),0) KUANGFRZ,\n" +
			        " ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.RUCRZ * SR.JINGZ) / SUM(SR.JINGZ)),0) AS RUCRZ,\n" +
			        " ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.JIESRZ * SR.JINGZ) / SUM(SR.JINGZ)),0) JIESRZ  \n" +
			        " FROM (SELECT KK.ID, FH.ID AS FID, D.MINGC  AS DIANCXXB_ID,MK.MINGC AS MEIKXXB_ID,\n" +
			        " FH.MEIKXXB_ID AS MEIKXXB_ID2,YS.MINGC AS YUNSDWB_ID,FH.YUNSDWB_ID AS YUNSDWB_ID2,\n" +
			        " FH.DAOHRQ,FH.FAHRQ,DECODE(KK.ID, NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetmj'),KK.HETMJ) AS HETMJ,\n"+ 
			        " KUIDKK(MK.ID, FH.DAOHRQ, 'jiesl') AS JIESL,\n"+
			        " kuidkk_ysl(MK.ID, FH.DAOHRQ, 'yunsl') AS YUNSL,\n"+
			        " DECODE(KK.ID,NULL, KUIDKK_MEIJ(MK.ID,FH.DAOHRQ,\n"+
			        " DECODE(KK.ID, NULL, ROUND(ZL.QNET_AR * 1000 / 4.1816, 0),KK.RUCRZ),\n"+
			        " DECODE(KK.ID,NULL, ROUND(KF.QNET_AR * 1000 / 4.1816, 0),KK.KUANGFRZ),\n"+
			        " DECODE(KK.ID,NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetrz'),KK.HETRZ),FH.BIAOZ,FH.JINGZ),KK.MEIJ) AS MEIJ,\n"+
			        " FH.BIAOZ,FH.JINGZ,KK.KUIDJE,KK.KUIK, KK.KUIKJE,KK.KUIDKKHJJE,\n"+
			        " DECODE(KK.ID,NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetrz'),KK.HETRZ) AS HETRZ,\n"+
			        " DECODE(KK.ID,NULL,ROUND(KF.QNET_AR * 1000 / 4.1816, 0),KK.KUANGFRZ) AS KUANGFRZ,\n"+
			        " DECODE(KK.ID,NULL,ROUND(ZL.QNET_AR * 1000 / 4.1816, 0),KK.RUCRZ) AS RUCRZ,\n"+
			        " DECODE(KK.ID,NULL,KUIDKK_JIESRZ(MK.ID,FH.DAOHRQ,DECODE(KK.ID,NULL,ROUND(ZL.QNET_AR * 1000 / 4.1816, 0), KK.RUCRZ),\n"+
			        " DECODE(KK.ID,NULL,ROUND(KF.QNET_AR * 1000 / 4.1816, 0),KK.KUANGFRZ), \n"+
			        " DECODE(KK.ID,NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetrz'),KK.HETRZ),FH.BIAOZ,FH.JINGZ,'jiesrz'),KK.JIESRZ) AS JIESRZ,\n"+
			        " DECODE(YF.ID,NULL,KUIDKK_YUNF(FH.DIANCXXB_ID,FH.MEIKXXB_ID,FH.YUNSDWB_ID,FH.DAOHRQ,'yunf'),YF.YUNF) AS YUNF \n"+
			        " FROM (SELECT F.ZHILB_ID, F.KUANGFZLB_ID,F.DIANCXXB_ID, F.MEIKXXB_ID,F.ID, F.JINGZ, F.BIAOZ,F.YUNSFSB_ID,F.FAHRQ,\n" +
			        " F.DAOHRQ, NVL((SELECT (SELECT YS.ID  FROM YUNSDWB YS  WHERE YS.ID = C.YUNSDWB_ID)\n"+
			        " FROM CHEPB C WHERE C.FAHB_ID = F.ID  AND ROWNUM = 1),0) YUNSDWB_ID  FROM FAHB F \n"
			        +riqitiaoj+",ZHILB ZL,MEIKXXB MK,KUANGFZLB KF,VWDIANC D,KUIDKKB_WH KK, KUIDKKB_WH_YUNF YF,YUNSDWB YS \n" +
					"WHERE FH.ZHILB_ID = ZL.ID(+) AND FH.KUANGFZLB_ID = KF.ID(+) AND FH.DIANCXXB_ID = D.ID \n" +
					"AND FH.MEIKXXB_ID = MK.ID  AND FH.YUNSDWB_ID = YS.ID(+) AND FH.ID = KK.FAHB_ID(+) \n" +
					" AND FH.ID = YF.FAHB_ID(+) \n" +
					""+diancTiaoj+" "+meiktj+" "+tongjfs
					+" ORDER BY FH.YUNSFSB_ID, MK.MINGC, YS.MINGC) SR  \n" +
					" GROUP BY ROLLUP(SR.YUNSDWB_ID, SR.MEIKXXB_ID) \n" +
					" having grouping(SR.YUNSDWB_ID)+grouping(SR.MEIKXXB_ID)=1) DT))XJ\n"+
			        " where HZ.ysdw = xj.ysdw(+)\n"+
			        " ) SR Order by SR.xjz DESC,SR.ysdw,SR.ZT1,SR.ZFZ DESC,SR.mkdw ");
		}
		else{
			buffer.append("  SELECT HZ.DCMC,HZ.YSDW,MKDW,HETRZ,MEIJ,YUNF,KUANGFL,DAOCL,FLZ,SJYSL,yunsl,YSYXCB,\n" +
					"  ROUND(CASE WHEN YSYXCB<0 THEN 25 ELSE 25-YSYXCB*2 END,2) KDFZ,\n" +
					"  KUANGFRZ,RUCRZ,JIESRZ,JRRZC,KKYXCB,\n" +
					"  ROUND(CASE WHEN KKYXCB<0 THEN 70 ELSE 70-KKYXCB*2 END,2) KKFZ,\n" +
					"  FLZ+ROUND(CASE WHEN YSYXCB<0 THEN 25 ELSE 25-YSYXCB*2  END,2) + ROUND(CASE WHEN KKYXCB<0 THEN 70 ELSE 70-KKYXCB*2  END,2) ZFZ \n" +
					"  FROM(SELECT DT.ZT,DT.ZT1,DT.ZT2,DT.DCID,DT.DCMC,DT.YSDW,DT.MKDW,\n" +
					"  HETRZ,\n" +
					"  MEIJ,\n" +
					"  YUNF,KUANGFL,DAOCL,\n" +
					"  (CASE WHEN PCT1>5 THEN 5 ELSE PCT1 END) FLZ,\n" +
					"  ROUND((KUANGFL-DAOCL)/KUANGFL*100,2) SJYSL,\n" +
					"  YUNSL,\n" +
					"  (CASE WHEN (YUNSL=0 AND DAOCL<>0) THEN 0   ELSE \n" +
					"  ROUND(((KUANGFL-DAOCL)-KUANGFL*YUNSL)*MEIJ/1.17/DAOCL,3) END) YSYXCB,\n" +
					"  KUANGFRZ,RUCRZ,\n" +
					"  JIESRZ,(JIESRZ-RUCRZ)JRRZC,\n" +
					"  ROUND((DECODE(RUCRZ,0,0,(MEIJ/1.17+YUNF/1.11)/RUCRZ)-DECODE(JIESRZ,0,0,((MEIJ/1.17+YUNF/1.11)/JIESRZ)))*RUCRZ,3) KKYXCB \n" +
					"  FROM(SELECT GROUPING(SR.DIANCXXB_ID)ZT,GROUPING(SR.MEIKXXB_ID)ZT1,GROUPING(SR.YUNSDWB_ID)ZT2,DCID,\n" +
					"  DECODE(GROUPING(SR.DIANCXXB_ID),1,'总计',SR.DIANCXXB_ID)DCMC,\n" +
					"  DECODE(GROUPING(SR.DIANCXXB_ID)+GROUPING(SR.YUNSDWB_ID),1,'单位小计',SR.YUNSDWB_ID)YSDW,\n" +
					"  DECODE(GROUPING(SR.MEIKXXB_ID)+GROUPING(SR.YUNSDWB_ID),1,'小计',SR.MEIKXXB_ID)MKDW,\n" +
					"  ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.HETRZ * SR.JINGZ) / SUM(SR.JINGZ)),0)  HETRZ, \n" +
					"  ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.MEIJ * SR.JINGZ) / SUM(SR.JINGZ)),2) MEIJ,\n" +
					"  ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.YUNF * SR.JINGZ) / SUM(SR.JINGZ)),2) YUNF,SUM(SR.BIAOZ) KUANGFL,SUM(SR.JINGZ) DAOCL,\n" +
					"  ROUND(ratio_to_report(SUM(SR.JINGZ)) OVER (PARTITION BY GROUPING(SR.YUNSDWB_ID))*50,2) PCT1,\n" +
					"  ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.YUNSL * SR.JINGZ) / SUM(SR.JINGZ)),3) YUNSL,\n" +
					"  ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.KUANGFRZ * SR.JINGZ) / SUM(SR.JINGZ)),0) KUANGFRZ,\n" +
					"  ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.RUCRZ * SR.JINGZ) / SUM(SR.JINGZ)),0) AS RUCRZ,\n" +
					"  ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.JIESRZ * SR.JINGZ) / SUM(SR.JINGZ)),0) JIESRZ  \n " +
					"   FROM (SELECT KK.ID,FH.ID    AS FID, D.ID AS DCID,D.MINGC  AS DIANCXXB_ID,  \n" +
					"   MK.MINGC AS MEIKXXB_ID,FH.MEIKXXB_ID AS MEIKXXB_ID2,YS.MINGC AS YUNSDWB_ID, \n" +
					"   FH.YUNSDWB_ID AS YUNSDWB_ID2, FH.DAOHRQ,FH.FAHRQ,\n" +
					"   DECODE(KK.ID,NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetmj'), KK.HETMJ) AS HETMJ,\n" +
					"   KUIDKK(MK.ID, FH.DAOHRQ, 'jiesl') AS JIESL,kuidkk_ysl(MK.ID, FH.DAOHRQ, 'yunsl') AS YUNSL,\n" +
					"   DECODE(KK.ID,NULL,KUIDKK_MEIJ(MK.ID,FH.DAOHRQ,\n" +
					"   DECODE(KK.ID,NULL,ROUND(ZL.QNET_AR * 1000 / 4.1816, 0),KK.RUCRZ), \n" +
					"   DECODE(KK.ID, NULL,ROUND(KF.QNET_AR * 1000 / 4.1816, 0),KK.KUANGFRZ), \n" +
					"   DECODE(KK.ID, NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetrz'),KK.HETRZ),FH.BIAOZ,FH.JINGZ),KK.MEIJ) AS MEIJ,\n" +
					"   FH.BIAOZ,FH.JINGZ,KK.KUIDJE,KK.KUIK,KK.KUIKJE,KK.KUIDKKHJJE,\n" +
					"   DECODE(KK.ID, NULL, KUIDKK(MK.ID, FH.DAOHRQ, 'hetrz'),KK.HETRZ) AS HETRZ,\n" +
					"   DECODE(KK.ID,NULL,ROUND(KF.QNET_AR * 1000 / 4.1816, 0),KK.KUANGFRZ) AS KUANGFRZ,\n" +
					"   DECODE(KK.ID,NULL,ROUND(ZL.QNET_AR * 1000 / 4.1816, 0),KK.RUCRZ) AS RUCRZ,\n" +
					"   DECODE(KK.ID,NULL,KUIDKK_JIESRZ(MK.ID,FH.DAOHRQ,DECODE(KK.ID,NULL,ROUND(ZL.QNET_AR * 1000 / 4.1816, 0),KK.RUCRZ),\n" +
					"   DECODE(KK.ID,NULL,ROUND(KF.QNET_AR * 1000 / 4.1816, 0),KK.KUANGFRZ),\n" +
					"   DECODE(KK.ID,NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetrz'),KK.HETRZ),FH.BIAOZ,FH.JINGZ,'jiesrz'),KK.JIESRZ) AS JIESRZ,\n" +
					"   DECODE(YF.ID, NULL,KUIDKK_YUNF(FH.DIANCXXB_ID,FH.MEIKXXB_ID,FH.YUNSDWB_ID,FH.DAOHRQ,'yunf'),YF.YUNF) AS YUNF  \n" +
					"   FROM (SELECT F.ZHILB_ID,F.KUANGFZLB_ID,F.DIANCXXB_ID,F.MEIKXXB_ID, F.ID,\n" +
					"   F.JINGZ, F.BIAOZ,F.YUNSFSB_ID, F.FAHRQ,F.DAOHRQ,\n" +
					"   NVL((SELECT (SELECT YS.ID  FROM YUNSDWB YS  WHERE YS.ID = C.YUNSDWB_ID)  FROM CHEPB C \n " +
					"   WHERE C.FAHB_ID = F.ID   AND ROWNUM = 1),0) YUNSDWB_ID  FROM FAHB F \n "
					+riqitiaoj+",  ZHILB ZL,MEIKXXB MK,KUANGFZLB KF,VWDIANC D,KUIDKKB_WH KK,KUIDKKB_WH_YUNF YF,YUNSDWB YS \n" +
					"  WHERE FH.ZHILB_ID = ZL.ID(+) AND FH.KUANGFZLB_ID = KF.ID(+) AND FH.DIANCXXB_ID = D.ID \n" +
					"  AND FH.MEIKXXB_ID = MK.ID AND FH.YUNSDWB_ID = YS.ID(+) \n" +
					"  AND FH.ID = KK.FAHB_ID(+) AND FH.ID = YF.FAHB_ID(+) \n"+diancTiaoj+" "+meiktj+" "+tongjfs+" ) SR  \n" +
					"   GROUP BY ROLLUP((SR.DCID,SR.DIANCXXB_ID),SR.YUNSDWB_ID,( SR.MEIKXXB_ID)) \n" +
					"ORDER  BY GROUPING(SR.DIANCXXB_ID),SR.DCID, GROUPING(SR.YUNSDWB_ID),SR.YUNSDWB_ID,SR.MEIKXXB_ID)DT )HZ,\n"+
					" (SELECT DCMC,YSDW,\n" +
					"  DECODE(ZT,1,NULL,FLZ+ROUND(CASE WHEN YSYXCB<0 THEN 25 ELSE 25-YSYXCB*2 END,2) + ROUND(CASE WHEN KKYXCB<0 THEN 70 ELSE 70-KKYXCB*2 END,2)) XIAOJ \n" +
					"  FROM(SELECT DT.ZT,DT.DCMC,DT.YSDW,DT.MKDW,DECODE(DT.ZT,1,NULL,HETRZ)HETRZ,\n" +
					"  DECODE(DT.ZT,1,NULL,MEIJ)MEIJ,DECODE(DT.ZT,1,NULL,YUNF)YUNF,KUANGFL,DAOCL,\n"+
					"  DECODE(DT.ZT,1,0,CASE WHEN PCT1>5 THEN 5 ELSE PCT1 END) FLZ,\n" +
					"  DECODE(DT.ZT,1,0,ROUND((KUANGFL-DAOCL)/KUANGFL*100,2))SJYSL,\n" +
					"  DECODE(DT.ZT,1,0,YUNSL)YUNSL,\n" +
					"  DECODE(DT.ZT,1,0,CASE WHEN (YUNSL=0 AND DAOCL<>0) THEN 0   ELSE \n" +
					"  ROUND(((KUANGFL-DAOCL)-KUANGFL*YUNSL)*MEIJ/1.17/DAOCL,3) END) YSYXCB,\n" +
					"  DECODE(DT.ZT,1,NULL,KUANGFRZ) KUANGFRZ,DECODE(DT.ZT,1,NULL,RUCRZ)RUCRZ,\n" +
					"  DECODE(DT.ZT,1,NULL,JIESRZ)JIESRZ,DECODE(DT.ZT,1,NULL,JIESRZ-RUCRZ)JRRZC,\n" +
					"  DECODE(DT.ZT,1,0,ROUND((DECODE(RUCRZ,0,0,(MEIJ/1.17+YUNF/1.11)/RUCRZ)-DECODE(JIESRZ,0,0,((MEIJ/1.17+YUNF/1.11)/JIESRZ)))*RUCRZ,3)) KKYXCB \n" +
					"  FROM(SELECT GROUPING(SR.DIANCXXB_ID)ZT,\n"+
				    "  DECODE(GROUPING(SR.DIANCXXB_ID),1,'总计',SR.DIANCXXB_ID)DCMC,\n"+
				    "  DECODE(GROUPING(SR.DIANCXXB_ID)+GROUPING(SR.YUNSDWB_ID),1,'单位小计',SR.YUNSDWB_ID)YSDW,\n"+
				    "  DECODE(GROUPING(SR.MEIKXXB_ID)+GROUPING(SR.YUNSDWB_ID),1,'小计',SR.MEIKXXB_ID)MKDW,\n"+
				    "  ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.HETRZ * SR.JINGZ) / SUM(SR.JINGZ)),0)  HETRZ,\n " +
					"  ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.MEIJ * SR.JINGZ) / SUM(SR.JINGZ)),2) MEIJ,\n" +
					"  ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.YUNF * SR.JINGZ) / SUM(SR.JINGZ)),2) YUNF,SUM(SR.BIAOZ) KUANGFL,SUM(SR.JINGZ) DAOCL,\n" +
					"  ROUND(ratio_to_report(SUM(SR.JINGZ)) OVER (PARTITION BY GROUPING(SR.YUNSDWB_ID))*25,2) PCT1,\n" +
					"  ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.YUNSL * SR.JINGZ) / SUM(SR.JINGZ)),3) YUNSL,\n" +
					"  ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.KUANGFRZ * SR.JINGZ) / SUM(SR.JINGZ)),0) KUANGFRZ,\n" +
					"  ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.RUCRZ * SR.JINGZ) / SUM(SR.JINGZ)),0) AS RUCRZ,\n" +
					"  ROUND(DECODE(SUM(SR.JINGZ), 0, 0, SUM(SR.JIESRZ * SR.JINGZ) / SUM(SR.JINGZ)),0) JIESRZ  \n " +
					"   FROM (SELECT KK.ID,FH.ID    AS FID, D.ID AS DCID,D.MINGC  AS DIANCXXB_ID,  MK.MINGC AS MEIKXXB_ID,\n" +
					"   FH.MEIKXXB_ID AS MEIKXXB_ID2,YS.MINGC AS YUNSDWB_ID,\n" +
					"   FH.YUNSDWB_ID AS YUNSDWB_ID2, FH.DAOHRQ,FH.FAHRQ,\n" +
					"   DECODE(KK.ID,NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetmj'), KK.HETMJ) AS HETMJ,\n" +
					"   KUIDKK(MK.ID, FH.DAOHRQ, 'jiesl') AS JIESL,kuidkk_ysl(MK.ID, FH.DAOHRQ, 'yunsl') AS YUNSL,\n" +
					"   DECODE(KK.ID,NULL,KUIDKK_MEIJ(MK.ID,FH.DAOHRQ,\n" +
					"   DECODE(KK.ID,NULL,ROUND(ZL.QNET_AR * 1000 / 4.1816, 0),KK.RUCRZ), " +
					"   DECODE(KK.ID, NULL,ROUND(KF.QNET_AR * 1000 / 4.1816, 0),KK.KUANGFRZ), " +
					"   DECODE(KK.ID, NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetrz'),KK.HETRZ),FH.BIAOZ,FH.JINGZ),KK.MEIJ) AS MEIJ,\n" +
					"   FH.BIAOZ,FH.JINGZ,KK.KUIDJE,KK.KUIK,KK.KUIKJE,KK.KUIDKKHJJE,\n" +
					"   DECODE(KK.ID, NULL, KUIDKK(MK.ID, FH.DAOHRQ, 'hetrz'),KK.HETRZ) AS HETRZ,\n" +
					"   DECODE(KK.ID,NULL,ROUND(KF.QNET_AR * 1000 / 4.1816, 0),KK.KUANGFRZ) AS KUANGFRZ,\n" +
					"   DECODE(KK.ID,NULL,ROUND(ZL.QNET_AR * 1000 / 4.1816, 0),KK.RUCRZ) AS RUCRZ,\n" +
					"   DECODE(KK.ID,NULL,KUIDKK_JIESRZ(MK.ID,FH.DAOHRQ,DECODE(KK.ID,NULL,ROUND(ZL.QNET_AR * 1000 / 4.1816, 0),KK.RUCRZ),\n" +
					"   DECODE(KK.ID,NULL,ROUND(KF.QNET_AR * 1000 / 4.1816, 0),KK.KUANGFRZ),\n" +
					"   DECODE(KK.ID,NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetrz'),KK.HETRZ),FH.BIAOZ,FH.JINGZ,'jiesrz'),KK.JIESRZ) AS JIESRZ,\n" +
					"   DECODE(YF.ID, NULL,KUIDKK_YUNF(FH.DIANCXXB_ID,FH.MEIKXXB_ID,FH.YUNSDWB_ID,FH.DAOHRQ,'yunf'),YF.YUNF) AS YUNF \n " +
					"   FROM (SELECT F.ZHILB_ID,F.KUANGFZLB_ID,F.DIANCXXB_ID,F.MEIKXXB_ID, F.ID,F.JINGZ, F.BIAOZ,F.YUNSFSB_ID, F.FAHRQ,F.DAOHRQ,\n" +
					"   NVL((SELECT (SELECT YS.ID  FROM YUNSDWB YS  WHERE YS.ID = C.YUNSDWB_ID)  FROM CHEPB C \n " +
					"   WHERE C.FAHB_ID = F.ID   AND ROWNUM = 1),0) YUNSDWB_ID  FROM FAHB F  \n" 	
					+riqitiaoj+",ZHILB ZL,MEIKXXB MK,KUANGFZLB KF,VWDIANC D,KUIDKKB_WH KK,KUIDKKB_WH_YUNF YF,YUNSDWB YS \n" +
					"   WHERE FH.ZHILB_ID = ZL.ID(+) \n" +
					"   AND FH.KUANGFZLB_ID = KF.ID(+) \n" +
					"  AND FH.DIANCXXB_ID = D.ID \n" +
					"  AND FH.MEIKXXB_ID = MK.ID \n" +
					"  AND FH.YUNSDWB_ID = YS.ID(+) \n" +
					"  AND FH.ID = KK.FAHB_ID(+) \n" +
					"  AND FH.ID = YF.FAHB_ID(+) \n"+diancTiaoj+" "+meiktj+" "+tongjfs+" ) SR  \n" +
					"  GROUP BY ROLLUP((SR.DCID,SR.DIANCXXB_ID),SR.YUNSDWB_ID,( SR.MEIKXXB_ID))\n"+
					"  HAVING GROUPING(SR.MEIKXXB_ID)+GROUPING(SR.YUNSDWB_ID)=1\n"+
					"  ORDER  BY GROUPING(SR.DIANCXXB_ID),SR.DCID, GROUPING(SR.YUNSDWB_ID),SR.YUNSDWB_ID,SR.MEIKXXB_ID)DT)HZ)XJ\n"+
					"  WHERE HZ.DCMC=XJ.DCMC(+) AND HZ.YSDW=XJ.YSDW(+)\n"+
					"  ORDER BY HZ.DCID,HZ.DCMC,HZ.ZT2,XJ.XIAOJ DESC,HZ.YSDW,HZ.ZT1,ZFZ DESC,HZ.MKDW ");
		}
		
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		String ArrHeader[][];
		int ArrWidth[];
		if(this.getTongjfsValue().getValue().equals("不分厂别统计")){
			ArrHeader = new String[1][19];
			ArrHeader[0] = new String[] { "运输单位","煤矿","合同<br>热值", "煤价","运价", "矿发<br>数量", "到厂<br>数量", "量分值","实际<br>运损率", "运损率",
					"运损影<br>响成本", "亏吨<br>分值", "矿发<br>热值","到厂<br>热值","结算<br>热值","结算到厂热值差","亏卡<br>影响成本","亏卡<br>分值","总分值"};
			
			 ArrWidth = new int[] {100, 100, 60,60, 60,60, 60, 60, 60, 60, 60,60 , 60, 60,60, 60, 60,60,60};
		}else{
			 ArrHeader= new String[1][20];
			 ArrHeader[0] = new String[] {"厂别", "运输单位","煤矿","合同<br>热值", "煤价","运价", "矿发<br>数量", "到厂<br>数量", "量分值","实际<br>运损率",  "运损率",
					"运损影<br>响成本", "亏吨<br>分值", "矿发<br>热值","到厂<br>热值","结算<br>热值","结算到厂热值差","亏卡<br>影响成本","亏卡<br>分值","总分值"};
			 ArrWidth = new int[] {80,100, 100, 60,60,60, 60, 60, 60, 60, 60, 60,60 , 60, 60,60, 60, 60,60,60};
		}
		
//		调整显示格式	
		String[] strFormat=new String[ArrWidth.length];
		if(ArrWidth.length==19){
		for (int i=0;i<=18;i++){
			if(i==3||i==4||i==5||i==6||i==7||i==8||i==11||i==17||i==18){
				strFormat[i]="0.00";
			}
			if(i==0||i==1){
				strFormat[i]="";
			}
			if(i==2||i==12||i==13||i==14||i==15){
				strFormat[i]="0";
			}
			if(i==9||i==10||i==16){
				strFormat[i]="0.000";
			}
		 }
		}else{
			for (int i=0;i<=19;i++){
				if(i==4||i==5||i==6||i==7||i==8||i==9||i==12||i==18||i==19){
					strFormat[i]="0.00";
				}
				if(i==0||i==1||i==2){
					strFormat[i]="";
				}
				if(i==3||i==13||i==14||i==15||i==16){
					strFormat[i]="0";
				}
				if(i==10||i==11||i==17){
					strFormat[i]="0.000";
				}
			 }
		}
		
		rt.setTitle("承运车队诚信排名", ArrWidth);
		String baot="";
		if(this.getTreeid_dc().equals("300")){
			baot="国电大同发电(1-10)";
		}else if(this.getTreeid_dc().equals("301")){
			baot="国电大同一期(1-6)";
		}else if(this.getTreeid_dc().equals("302")){
			baot="国电大同二期(7-8)";
		}else if(this.getTreeid_dc().equals("303")){
			baot="国电大同三期(9-10)";
		}else if(this.getTreeid_dc().equals("302,303")){
			baot="国电大同发电公司(7-10)";
		}else if(this.getTreeid_dc().equals("301,302,303")){
			baot="国电大同发电(1-10)";
		}
		rt.setDefaultTitle(1, 3, "单位：" + baot, Table.ALIGN_LEFT);
		rt.setDefaultTitle(14, 5, this.getRiqiValue().getValue()+":"+riqi+ "至"+riqi2, Table.ALIGN_LEFT);
		
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(1000);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

		rt.createDefautlFooter(ArrWidth);

//		总计行，精确到行数据赋值为空
		if (rt.body.getRows()>=2){
			if(this.getTongjfsValue().getValue().equals("不分厂别统计")){

				for (int i=8;i<=rt.body.getCols();i++){				
					if(i==8||i==12||i==18||i==19){
						rt.body.setCellValue(2, i, "");
					}					
				}
			}else{

				for (int i=9;i<=rt.body.getCols();i++){
					if(i==9||i==13||i==19||i==20){
					rt.body.setCellValue(rt.body.getRows(), i, "");
					}
				}
			}
		}
		
//		调整显示格式
		rt.body.setColFormat(strFormat);
		rt.body.ShowZero=true;
		
		for(int i=0; i<=rt.body.getCols(); i++){
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			this.setRiqi(null);
			this.setRiqi2(null);
			
			setTreeid_dc(null);
			getDiancmcModels();
			
			setTreeid_ysdw(null);
			getYunsdwmcModels();
		
			this.setTongjfsValue(null);
			this.getTongjfsModels();
			
			this.setRiqiValue(null);
			this.getRiqiModels();
		}
		getSelectData();
	}
	boolean riq=false;
	// 绑定日期
	private String riqi;
	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(DateUtil.AddDate(new Date(), -2, DateUtil.AddType_intDay));
			
		}
		return riqi;
	}

	public void setRiqi(String riqi) {
		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riq=true;
		}
	}

//	 绑定日期
	private String riqi2;
	public String getRiqi2() {
		if (riqi2 == null || riqi2.equals("")) {
			riqi2 = DateUtil.FormatDate(DateUtil.AddDate(new Date(), -2, DateUtil.AddType_intDay));
		}
		return riqi2;
	}

	public void setRiqi2(String riqi2) {
		if (this.riqi2 != null && !this.riqi2.equals(riqi2)) {
			this.riqi2 = riqi2;
		}

	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}
    
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		ComboBox riqtj = new ComboBox();
		riqtj.setTransform("RiqiDropDown");
		riqtj.setListeners("select:function(){document.Form0.submit();}");
		riqtj.setId("Riqi");
		riqtj.setWidth(80);
		tb1.addField(riqtj);
		tb1.addText(new ToolbarText("-"));
		
		//tb1.addText(new ToolbarText("发货日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.setId("riqi");
		df.setListeners("change:function(own,newValue,oldValue){document.getElementById('RIQI').value = newValue.dateFormat('Y-m-d'); " +
		"document.getElementById('Mark_mk').value = 'true'; document.forms[0].submit();}");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		DateField df2 = new DateField();
		df2.setReadOnly(true);
		df2.setValue(this.getRiqi2());
		df2.setId("riqi2");
		df2.setListeners("change:function(own,newValue,oldValue){document.getElementById('RIQI2').value = newValue.dateFormat('Y-m-d'); " +
		"document.getElementById('Mark_mk').value = 'true'; document.forms[0].submit();}");
		tb1.addField(df2);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("-"));
		long diancxxb_id = ((Visit) this.getPage().getVisit()).getDiancxxb_id();
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowCheck_Dianc, diancxxb_id, getTreeid_dc(),null,true);

				setDCTree(etu);
				TextField tf = new TextField();
				tf.setId("diancTree_text");
				tf.setWidth(100);
				String[] str=getTreeid_dc().split(",");
				tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
				ToolbarButton toolb2 = new ToolbarButton(null, null,
						"function(){diancTree_window.show();}");
				toolb2.setIcon("ext/resources/images/list-items.gif");
				toolb2.setCls("x-btn-icon");
				toolb2.setMinWidth(20);
				
				tb1.addText(new ToolbarText("单位:"));
				tb1.addField(tf);
				tb1.addItem(toolb2);
		tb1.addText(new ToolbarText("-"));
		
//		车队
		String yunsdwtj=" where id<>1 and id in (select distinct yunsdwb_id from kuidkkb_wh wh where wh.fahrq>=date'"+this.getRiqi()+"' and wh.fahrq<=date'"+this.getRiqi2()+"')";
		ExtTreeUtil etu2 = new ExtTreeUtil("yunsdwTree",
				ExtTreeUtil.treeWindowCheck_yunsdw_datong, diancxxb_id, "0",yunsdwtj,true);

				setTree2(etu2);
				TextField tf2 = new TextField();
				tf2.setId("yunsdwTree_text");
				tf2.setWidth(100);
				String[] str2=getTreeid_ysdw().split(",");
				if(str2[0].equals("0")){
					tf2.setValue("全部");
				}else{
					tf2.setValue(((IDropDownModel) getYunsdwmcModel()).getBeanValue(Long.parseLong(str2[0])));
				}
				
				ToolbarButton toolb = new ToolbarButton(null, null,
						"function(){yunsdwTree_window.show();}");
				toolb.setIcon("ext/resources/images/list-items.gif");
				toolb.setCls("x-btn-icon");
				toolb.setMinWidth(20);
				
				tb1.addText(new ToolbarText("车队:"));
				tb1.addField(tf2);
				tb1.addItem(toolb);
		        tb1.addText(new ToolbarText("-"));
//		统计
		tb1.addText(new ToolbarText("统计:"));
		ComboBox tj = new ComboBox();
		tj.setTransform("TongjfsDropDown");
		tj.setListeners("select:function(){document.Form0.submit();}");
		tj.setId("Tongjfs");
		tj.setWidth(100);
		tb1.addField(tj);
		tb1.addText(new ToolbarText("-"));
		
//		煤矿
		String meiktiaoj=" where id<>1 and id in (select distinct meikxxb_id from kuidkkb_wh wh where wh.fahrq>=date'"+this.getRiqi()+"' and wh.fahrq<=date'"+this.getRiqi2()+"')";
		ExtTreeUtil etu3 = new ExtTreeUtil("meikTree",
				ExtTreeUtil.treeWindowCheck_meikxx, diancxxb_id, "0",meiktiaoj,true);

				setTree3(etu3);
				TextField tf3 = new TextField();
				tf3.setId("meikTree_text");
				tf3.setWidth(150);
				String[] str3=getTreeid_meik().split(",");
				if(str3[0].equals("0")){
					tf3.setValue("全部");
				}else{
					tf3.setValue(((IDropDownModel) getMeikModel()).getBeanValue(Long.parseLong(str3[0])));
				}
				
				ToolbarButton toolb3 = new ToolbarButton(null, null,
						"function(){meikTree_window.show();}");
				toolb3.setIcon("ext/resources/images/list-items.gif");
				toolb3.setCls("x-btn-icon");
				toolb3.setMinWidth(20);
				
				tb1.addText(new ToolbarText("煤矿:"));
				tb1.addField(tf3);
				tb1.addItem(toolb3);
		        tb1.addText(new ToolbarText("-"));

		//ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);

		setToolbar(tb1);
	}
	
//	单位
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		if(((Visit) getPage().getVisit()).getString3()!=null && !((Visit) getPage().getVisit()).getString3().equals(treeid)){
		}
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	public ExtTreeUtil getDCTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setDCTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeScript() {
		return getDCTree().getWindowTreeScript();
	}
	public String getTreeHtml() {
		return getDCTree().getWindowTreeHtml(this);
	}

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_pageLink = "";
	}

//	页面登陆验证
	public void pageValidate(PageEvent arg0) {
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}

///统计方式
	public boolean _Tongjfschange = false;
	private IDropDownBean _TongjfsValue;

	public IDropDownBean getTongjfsValue() {
		if(_TongjfsValue==null){
			_TongjfsValue=(IDropDownBean)getTongjfsModels().getOption(0);
		}
		return _TongjfsValue;
	}

	public void setTongjfsValue(IDropDownBean Value) {
		long id = -2;
		if (_TongjfsValue != null) {
			id = _TongjfsValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Tongjfschange = true;
			} else {
				_Tongjfschange = false;
			}
		}
		_TongjfsValue = Value;
	}

	private IPropertySelectionModel _TongjfsModel;

	public void setTongjfsModel(IPropertySelectionModel value) {
		_TongjfsModel = value;
	}

	public IPropertySelectionModel getTongjfsModel() {
		if (_TongjfsModel == null) {
			getTongjfsModels();
		}
		return _TongjfsModel;
	}

	public IPropertySelectionModel getTongjfsModels() {
		try{
			List fangs = new ArrayList();
			//fahdwList.add(new IDropDownBean(0,"按厂统计"));
			fangs.add(new IDropDownBean(1,"不分厂别统计"));
			fangs.add(new IDropDownBean(2,"分厂别统计"));
			_TongjfsModel = new IDropDownModel(fangs);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//con.Close();
		}
		return _TongjfsModel;
	}
	
	private String Markmk = "true"; // 
	
	public String getMarkmk() {
		return Markmk;
	}

	public void setMarkmk(String markmk) {
		Markmk = markmk;
	}
	
//	日期方式
		public boolean _Riqichange = false;
		private IDropDownBean _RiqiValue;

		public IDropDownBean getRiqiValue() {
			if(_RiqiValue==null){
				_RiqiValue=(IDropDownBean)getRiqiModels().getOption(0);
			}
			return _RiqiValue;
		}

		public void setRiqiValue(IDropDownBean Value) {
			long id = -2;
			if (_RiqiValue != null) {
				id = _RiqiValue.getId();
			}
			if (Value != null) {
				if (Value.getId() != id) {
					_Riqichange = true;
				} else {
					_Riqichange = false;
				}
			}
			_RiqiValue = Value;
		}

		private IPropertySelectionModel _RiqiModel;

		public void setRiqiModel(IPropertySelectionModel value) {
			_RiqiModel = value;
		}

		public IPropertySelectionModel getRiqiModel() {
			if (_RiqiModel == null) {
				getRiqiModels();
			}
			return _RiqiModel;
		}

		public IPropertySelectionModel getRiqiModels() {
			
			try{
				List fangs = new ArrayList();
				fangs.add(new IDropDownBean(1,"发货日期"));
				fangs.add(new IDropDownBean(2,"到货日期"));
				_RiqiModel = new IDropDownModel(fangs);

			}catch(Exception e){
				e.printStackTrace();
			}finally{
				//con.Close();
			}
			return _RiqiModel;
		}
		
//		煤矿
		public ExtTreeUtil getTree3() {
			return ((Visit) this.getPage().getVisit()).getExtTree3();
		}

		public void setTree3(ExtTreeUtil etu) {
			((Visit) this.getPage().getVisit()).setExtTree3(etu);
		}
		
		public String getTreeHtml3() {
			if (getDCTree() == null){
				return "";
			}else {
				return getTree3().getWindowTreeHtml(this);
			}
		}

		public String getTreeScript3() {
			if (getDCTree() == null) {
				return "";
			}else {
				return getTree3().getWindowTreeScript();
			}
		}
		
		public String getTreeid_meik() {
			String treeid = ((Visit) getPage().getVisit()).getString5();
			if (treeid == null || treeid.equals("")) {
				((Visit) getPage().getVisit()).setString5("0");
			}
			return ((Visit) getPage().getVisit()).getString5();
		}

		public void setTreeid_meik(String treeid) {
			if(((Visit) getPage().getVisit()).getString5()!=null && !((Visit) getPage().getVisit()).getString5().equals(treeid)){
			}
			((Visit) getPage().getVisit()).setString5(treeid);
		}
		
		
		public IPropertySelectionModel getMeikModel() {
			if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
				getMeikModels();
			}
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
		}

		public void setMeikModel(IPropertySelectionModel _value) {
			((Visit) getPage().getVisit()).setProSelectionModel5(_value);
		}

		public void getMeikModels() {
			String sql3 = "select id,mingc from MEIKXXB";
			setMeikModel(new IDropDownModel(sql3));
		}
		
		//以下是新增的车队复选框----开始
		
		public ExtTreeUtil getTree2() {
			return ((Visit) this.getPage().getVisit()).getExtTree2();
		}

		public void setTree2(ExtTreeUtil etu) {
			((Visit) this.getPage().getVisit()).setExtTree2(etu);
		}
		
		public String getTreeHtml2() {
			if (getDCTree() == null){
				return "";
			}else {
				return getTree2().getWindowTreeHtml(this);
			}
		}

		public String getTreeScript2() {
			if (getDCTree() == null) {
				return "";
			}else {
				return getTree2().getWindowTreeScript();
			}
		}
		
		public String getTreeid_ysdw() {
			String treeid = ((Visit) getPage().getVisit()).getString4();
			if (treeid == null || treeid.equals("")) {
				((Visit) getPage().getVisit()).setString4("0");
			}
			return ((Visit) getPage().getVisit()).getString4();
		}

		public void setTreeid_ysdw(String treeid) {
			if(((Visit) getPage().getVisit()).getString4()!=null && !((Visit) getPage().getVisit()).getString4().equals(treeid)){
			}
			((Visit) getPage().getVisit()).setString4(treeid);
		}
		
		
		public IPropertySelectionModel getYunsdwmcModel() {
			if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
				getYunsdwmcModels();
			}
			return ((Visit) getPage().getVisit()).getProSelectionModel4();
		}

		public void setYunsdwmcModel(IPropertySelectionModel _value) {
			((Visit) getPage().getVisit()).setProSelectionModel4(_value);
		}

		public void getYunsdwmcModels() {
			String sql2 = "select id,mingc from yunsdwb";
			setYunsdwmcModel(new IDropDownModel(sql2));
		}
		
		//以上是新增的车队复选框----结束
		
}