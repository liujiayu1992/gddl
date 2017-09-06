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
 * 时间：2014-03-20
 * 描述：标题增加单位描述 
 */

public class Caigzg_qk extends BasePage implements PageValidateListener {
	
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
	
		if(this.getBaoblxValue().getValue().equals("按矿统计")){
			return getKuangbTj();
		}else if(this.getBaoblxValue().getValue().equals("按车队统计")) {
			return getChedTj();
		}else if(this.getBaoblxValue().getValue().equals("按地区统计")){
			return getDiquTj();
		}else{
			return getyunsfsTj();
		}
	}


	private String getChedTj() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
//		电厂条件
		String diancTiaoj="";
		if(this.getTreeid_dc().equals("300")){
			diancTiaoj="";
		}else{
			diancTiaoj=" and  d.id in("+this.getTreeid_dc()+") \n";
		}
//		日期条件
		String riqitiaoj="";
		String tongjfstj=this.getTongjfsValue().getValue();
//		统计条件
		String tongjfs="";
		if(tongjfstj.equals("到货日期")){
			riqitiaoj=" where f.daohrq BETWEEN to_date('"+this.getRiqi()+"','yyyy-mm-dd') \n" +
					  " AND to_date('"+this.getRiqi2()+"','yyyy-mm-dd'))  \n";
			
			tongjfs="  to_char(min(sr.daohrq),'yyyy-mm-dd')||'至'||to_char(max(sr.daohrq),'yyyy-mm-dd') as riq,\n" ;
			
		}else{
			riqitiaoj=" where f.fahrq BETWEEN to_date('"+this.getRiqi()+"','yyyy-mm-dd') \n" +
					  " AND to_date('"+this.getRiqi2()+"','yyyy-mm-dd'))  \n";
			tongjfs="  to_char(min(sr.fahrq),'yyyy-mm-dd')||'至'||to_char(max(sr.fahrq),'yyyy-mm-dd') as riq,\n" ;
		}
	
//		按车队
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT YSDW,MKDW,RIQ,HETMJ,MEIJ,\n" +
				" DECODE(NVL(RUCL,0),0,0,ROUND(MEIJ*KUANGFL/RUCL,2))ZGMJ,YUNF,\n" +
				" ROUND(DECODE(NVL(RUCRZ,0),0,0,(ROUND(DECODE(NVL(RUCL,0),0,0,MEIJ*KUANGFL/RUCL),2)/1.17+YUNF/1.11)*7000/RUCRZ),2)RCBMDJ,\n" +
				" KUANGFL,RUCL,HETRZ,RUCRZ,JIESRZ,MEIJ-HETMJ DUNKKYX,round((MEIJ-HETMJ)*RUCL,2) DUNKKYXCB,\n" +
				" DECODE(NVL(RUCL,0),0,0,round(ROUND(MEIJ*KUANGFL/RUCL,2)*RUCL,2)) MEIK,\n " +
				" round(YUNF*RUCL,2) YUNFEI,(select '' from dual) beiz " +
				" FROM(SELECT \n" +
				" DECODE(GROUPING(YUNSDWB_ID)+GROUPING(MEIKXXB_ID),2,'总计',YUNSDWB_ID)YSDW,\n"+
				" DECODE(GROUPING(MEIKXXB_ID)+GROUPING(YUNSDWB_ID),1,'小计',MEIKXXB_ID)MKDW,FHRQ,RIQ,\n" +
				" decode(sum(jingz),0,0,round_new(sum(HETMJ*jingz)/sum(jingz),2)) as HETMJ, \n" +
				" decode(sum(jingz),0,0,round_new(sum(MEIJ*jingz)/sum(jingz),2)) as MEIJ,\n" +
				" decode(sum(jingz),0,0,round_new(sum(YUNF*jingz)/sum(jingz),2)) as YUNF,\n" +
				" SUM(BIAOZ) KUANGFL,SUM(JINGZ) RUCL," +
				" decode(sum(jingz),0,0,round_new(sum(HETRZ*jingz)/sum(jingz),0)) as HETRZ,\n" +
				" decode(sum(jingz),0,0,round_new(sum(RUCRZ*jingz)/sum(jingz),0)) as RUCRZ,\n" +
				" decode(sum(jingz),0,0,round_new(sum(JIESRZ*jingz)/sum(jingz),0)) as JIESRZ \n" +
				" FROM(SELECT MEIKXXB_ID2,MEIKXXB_ID,YUNSDWB_ID,FHRQ,DAOHRQ,RIQ,HETMJ,YUNF,JINGZ,BIAOZ,HETRZ,KUANGFRZ,RUCRZ,JIESRZ,\n" +
				" CASE WHEN JIESRZ > HETRZ THEN KUIDKK_MJ(HETMJ, HETRZ, JIESRZ, RUCRZ, JIANGMJ) ELSE \n" +
				" KUIDKK_MJ(HETMJ, HETRZ, JIESRZ, RUCRZ, KOUMJ) END MEIJ,JIANGMJ,KOUMJ \n" +
				" FROM (SELECT MEIKXXB_ID2,MEIKXXB_ID,YUNSDWB_ID,FHRQ,DAOHRQ,RIQ,HETMJ,YUNF,JINGZ,BIAOZ,HETRZ,KUANGFRZ,RUCRZ, \n" +
				" KUIDKK_JIESRZ(MEIKXXB_ID2,DAOHRQ,RUCRZ,KUANGFRZ,HETRZ,BIAOZ,JINGZ,'jiesrz') JIESRZ,\n" +
				" KUIDKK_NEW(MEIKXXB_ID2, DAOHRQ, 'jiangmj') JIANGMJ,\n" +
				" KUIDKK_NEW(MEIKXXB_ID2, DAOHRQ, 'koumj') KOUMJ \n" +
				" FROM (SELECT MEIKXXB_ID2,MEIKXXB_ID,YUNSDWB_ID,\n" +
				" GROUPING(YUNSDWB_ID) GYSZT, TO_CHAR(SR.FAHRQ, 'mm') FHRQ,MAX(SR.DAOHRQ) DAOHRQ, \n" +
				tongjfs +" ROUND(DECODE(SUM(SR.JINGZ),0,0,SUM(SR.HETMJ * SR.JINGZ) / SUM(SR.JINGZ)),0) HETMJ, \n" +
				" ROUND(DECODE(SUM(SR.JINGZ),0,0,SUM(SR.YUNF * SR.JINGZ) / SUM(SR.JINGZ)),2) YUNF, \n" +
				" SUM(SR.JINGZ) JINGZ,SUM(SR.BIAOZ) BIAOZ," +
				" ROUND(DECODE(SUM(SR.JINGZ),0,0,SUM(SR.HETRZ * SR.JINGZ) / SUM(SR.JINGZ)),0) HETRZ, \n" +
				" ROUND(DECODE(SUM(SR.JINGZ),0,0,SUM(SR.KUANGFRZ * SR.JINGZ) /SUM(SR.JINGZ)),0) KUANGFRZ, \n" +
				" ROUND(DECODE(SUM(SR.JINGZ),0,0,SUM(SR.RUCRZ * SR.JINGZ) / SUM(SR.JINGZ)),0) RUCRZ \n" +
				" FROM (SELECT KK.ID,FH.ID AS FID,MK.MINGC AS MEIKXXB_ID,FH.MEIKXXB_ID AS MEIKXXB_ID2, \n" +
				" YS.MINGC AS YUNSDWB_ID,FH.YUNSDWB_ID AS YUNSDWB_ID2,FH.DAOHRQ,FH.FAHRQ," +
				" DECODE(KK.ID,NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetmj'),KK.HETMJ) AS HETMJ,FH.BIAOZ,FH.JINGZ, \n" +
				" DECODE(KK.ID,NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetrz'),KK.HETRZ) AS HETRZ, \n" +
				" DECODE(KK.ID,NULL,ROUND(KF.QNET_AR * 1000 / 4.1816, 0),KK.KUANGFRZ) AS KUANGFRZ, \n" +
				" DECODE(KK.ID,NULL,ROUND(ZL.QNET_AR * 1000 / 4.1816, 0),KK.RUCRZ) AS RUCRZ, \n" +
				" DECODE(YF.ID,NULL,KUIDKK_YUNF(FH.DIANCXXB_ID,FH.MEIKXXB_ID,FH.YUNSDWB_ID,FH.DAOHRQ,'yunf'),YF.YUNF) AS YUNF \n" +
				" FROM (SELECT F.ZHILB_ID,F.KUANGFZLB_ID,F.DIANCXXB_ID,F.MEIKXXB_ID, \n" +
				" F.ID,F.JINGZ,F.BIAOZ,F.YUNSFSB_ID,F.FAHRQ,F.DAOHRQ,\n" +
				" NVL((SELECT (SELECT YS.ID FROM YUNSDWB YS WHERE YS.ID = C.YUNSDWB_ID) \n" +
				" FROM CHEPB C WHERE C.FAHB_ID = F.ID AND ROWNUM = 1),0) YUNSDWB_ID FROM FAHB F \n"
				+riqitiaoj+" fh,ZHILB ZL,MEIKXXB MK,KUANGFZLB KF,VWDIANC D,KUIDKKB_WH KK,KUIDKKB_WH_YUNF YF,YUNSDWB YS \n" +
				" WHERE FH.ZHILB_ID = ZL.ID(+)  \n" +
				" AND FH.KUANGFZLB_ID = KF.ID(+) \n" +
				" AND FH.DIANCXXB_ID = D.ID \n" +
				" AND FH.MEIKXXB_ID = MK.ID \n" +
				" AND FH.YUNSDWB_ID = YS.ID(+) \n" +
				" AND FH.ID = KK.FAHB_ID(+) \n" +
				" AND FH.ID = YF.FAHB_ID(+) \n" +
				diancTiaoj+"  ) SR  GROUP BY (MEIKXXB_ID2, MEIKXXB_ID, YUNSDWB_ID,TO_CHAR(SR.FAHRQ, 'mm')))))DR \n" +
			    " GROUP BY ROLLUP(YUNSDWB_ID,MEIKXXB_ID,FHRQ,RIQ) \n" +
			    " HAVING NOT GROUPING(MEIKXXB_ID)+GROUPING(RIQ)=1 \n" +
			    " ORDER BY GROUPING(YUNSDWB_ID) ,YUNSDWB_ID,GROUPING(MEIKXXB_ID),MEIKXXB_ID, FHRQ) \n");
		
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String ArrHeader[][] = new String[2][17];
		
		ArrHeader[0] = new String[] { "运输单位","煤矿",tongjfstj,"价格情况", "价格情况", "价格情况","价格情况", "价格情况", "入厂煤数量热值情况","入厂煤数量热值情况", "入厂煤数量热值情况",
				"入厂煤数量热值情况","入厂煤数量热值情况","亏(盈)卡情况","亏(盈)卡情况","金额(元)","金额(元)","备注"};
		ArrHeader[1] = new String[] { "运输单位","煤矿",tongjfstj, "合同煤价<br>(元/吨)", "煤价<br>(元/吨)", "暂估煤价<br>(元/吨)", "运费<br>(元/吨)","入厂标煤单价<br>(元/吨)",
				"矿发量<br>(吨)","入厂量<br>(吨)","合同热值<br>(Kcal/Kg)",
				"入厂热值<br>(Kcal/Kg)","结算热值<br>(Kcal/Kg)","吨亏卡<br>影响","金额(元)","煤款","运费","备注"};

		int ArrWidth[] = new int[] {100, 100, 180,55, 55, 55, 55, 55, 55, 55,55 , 55,55,55, 100,100, 100,100};

//		调整显示格式	
		String[] strFormat=new String[18];
		for (int i=0;i<=18;i++){
			if(i==3||i==4||i==5||i==6||i==7||i==8||i==9||i==13||i==14||i==15||i==16){
				strFormat[i]="0.00";
			}
			if(i==0||i==1||i==2||i==17){
				strFormat[i]="";
			}
			if(i==10||i==11||i==12){
				strFormat[i]="0";
			}
		}
		
		if(this.getRiqi().equals(this.getRiqi2())){
			rt.setTitle(this.getRiqi()+"结算暂估情况", ArrWidth);
		}else{
			rt.setTitle(this.getRiqi()+"至"+this.getRiqi2()+"结算暂估情况", ArrWidth);
		}
		
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
		//rt.setDefaultTitle(19, 4, tongjfstj+"：" + this.getRiqi()+"至"+this.getRiqi2(), Table.ALIGN_LEFT);
		
		rt.setBody(new Table(rs, 2, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(1000);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.createDefautlFooter(ArrWidth);
		
		
		rt.body.ShowZero=true;
		
//		调整显示格式
		rt.body.setColFormat(strFormat);
		
		//页面为空的时候都显示0,但是运费这一列除外
		for(int ab=1;ab<=rt.body.getRows();ab++){
			if(rt.body.getCellValue(ab, 6).equals("0")){
				rt.body.setCellValue(ab, 6, "");
			}
		}
		
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
	
	
	private String getKuangbTj() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		

//		电厂条件
		String diancTiaoj="";
		if(this.getTreeid_dc().equals("300")){
			diancTiaoj="";
		}else{
			diancTiaoj=" and  d.id in("+this.getTreeid_dc()+")";
		}
//		日期条件
		String riqitiaoj="";
		
//		统计条件
		String tongjsf=this.getTongjfsValue().getValue();
		String tongjxsfs="";
		if(tongjsf.equals("到货日期")){
			riqitiaoj=" where f.daohrq BETWEEN to_date('"+this.getRiqi()+"','yyyy-mm-dd') \n" +
			  " AND to_date('"+this.getRiqi2()+"','yyyy-mm-dd')) \n";
	
			tongjxsfs="  to_char(min(sr.daohrq),'yyyy-mm-dd')||'至'||to_char(max(sr.daohrq),'yyyy-mm-dd') as riq,\n" ;
			
		}else{
			
			riqitiaoj=" where f.fahrq BETWEEN to_date('"+this.getRiqi()+"','yyyy-mm-dd')  \n" +
			  " AND to_date('"+this.getRiqi2()+"','yyyy-mm-dd'))  \n";
	
			tongjxsfs="  to_char(min(sr.fahrq),'yyyy-mm-dd')||'至'||to_char(max(sr.fahrq),'yyyy-mm-dd') as riq,\n" ;
			
		}
		
//		按煤矿
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT MKDW,YSDW,RIQ,HETMJ,MEIJ,\n" +
				" DECODE(NVL(RUCL,0),0,0,ROUND(MEIJ*KUANGFL/RUCL,2))ZGMJ,YUNF, \n" +
				" ROUND(DECODE(NVL(RUCRZ,0),0,0,(ROUND(DECODE(NVL(RUCL,0),0,0,MEIJ*KUANGFL/RUCL),2)/1.17+YUNF/1.11)*7000/RUCRZ),2)RCBMDJ, \n" +
				" KUANGFL,RUCL,HETRZ,RUCRZ,JIESRZ,MEIJ-HETMJ DUNKKYX,round((MEIJ-HETMJ)*RUCL,2) DUNKKYXCB, \n" +
				" DECODE(NVL(RUCL,0),0,0,round(ROUND(MEIJ*KUANGFL/RUCL,2)*RUCL,2)) MEIK, \n" +
				" round(YUNF*RUCL,2) YUNFEI,(select '' from dual) beiz" +
				" FROM(SELECT  \n" +
				" DECODE(GROUPING(MEIKXXB_ID)+GROUPING(YUNSDWB_ID),2,'总计',MEIKXXB_ID)MKDW, \n"+
				" DECODE(GROUPING(YUNSDWB_ID)+GROUPING(MEIKXXB_ID),1,'小计',YUNSDWB_ID)YSDW,FHRQ,RIQ, \n" +
				" decode(sum(jingz),0,0,round_new(sum(HETMJ*jingz)/sum(jingz),2)) as HETMJ, \n" +
				" decode(sum(jingz),0,0,round_new(sum(MEIJ*jingz)/sum(jingz),2)) as MEIJ, \n" +
				" decode(sum(jingz),0,0,round_new(sum(YUNF*jingz)/sum(jingz),2)) as YUNF, \n" +
				" SUM(BIAOZ) KUANGFL,SUM(JINGZ) RUCL," +
				" decode(sum(jingz),0,0,round_new(sum(HETRZ*jingz)/sum(jingz),0)) as HETRZ, \n" +
				" decode(sum(jingz),0,0,round_new(sum(RUCRZ*jingz)/sum(jingz),0)) as RUCRZ, \n" +
				" decode(sum(jingz),0,0,round_new(sum(JIESRZ*jingz)/sum(jingz),0)) as JIESRZ \n" +
				" FROM(SELECT MEIKXXB_ID2,MEIKXXB_ID,YUNSDWB_ID,FHRQ,DAOHRQ,RIQ,HETMJ,YUNF,JINGZ,BIAOZ,HETRZ,KUANGFRZ,RUCRZ,JIESRZ,\n" +
				" CASE WHEN JIESRZ > HETRZ THEN KUIDKK_MJ(HETMJ, HETRZ, JIESRZ, RUCRZ, JIANGMJ) ELSE \n" +
				" KUIDKK_MJ(HETMJ, HETRZ, JIESRZ, RUCRZ, KOUMJ) END MEIJ,JIANGMJ,KOUMJ \n" +
				" FROM (SELECT MEIKXXB_ID2,MEIKXXB_ID,YUNSDWB_ID,FHRQ,DAOHRQ,RIQ,HETMJ,YUNF,JINGZ,BIAOZ,HETRZ,KUANGFRZ,RUCRZ, \n" +
				" KUIDKK_JIESRZ(MEIKXXB_ID2,DAOHRQ,RUCRZ,KUANGFRZ,HETRZ,BIAOZ,JINGZ,'jiesrz') JIESRZ,\n" +
				" KUIDKK_NEW(MEIKXXB_ID2, DAOHRQ, 'jiangmj') JIANGMJ, \n" +
				" KUIDKK_NEW(MEIKXXB_ID2, DAOHRQ, 'koumj') KOUMJ \n" +
				" FROM (SELECT MEIKXXB_ID2,MEIKXXB_ID,YUNSDWB_ID, \n" +
				" GROUPING(YUNSDWB_ID) GYSZT, TO_CHAR(SR.FAHRQ, 'mm') FHRQ,MAX(SR.DAOHRQ) DAOHRQ, \n" +
				tongjxsfs +" ROUND(DECODE(SUM(SR.JINGZ),0,0,SUM(SR.HETMJ * SR.JINGZ) / SUM(SR.JINGZ)),0) HETMJ, \n" +
				" ROUND(DECODE(SUM(SR.JINGZ),0,0,SUM(SR.YUNF * SR.JINGZ) / SUM(SR.JINGZ)),2) YUNF, \n" +
				" SUM(SR.JINGZ) JINGZ,SUM(SR.BIAOZ) BIAOZ, \n" +
				" ROUND(DECODE(SUM(SR.JINGZ),0,0,SUM(SR.HETRZ * SR.JINGZ) / SUM(SR.JINGZ)),0) HETRZ, \n" +
				" ROUND(DECODE(SUM(SR.JINGZ),0,0,SUM(SR.KUANGFRZ * SR.JINGZ) /SUM(SR.JINGZ)),0) KUANGFRZ, \n" +
				" ROUND(DECODE(SUM(SR.JINGZ),0,0,SUM(SR.RUCRZ * SR.JINGZ) / SUM(SR.JINGZ)),0) RUCRZ \n" +
				" FROM (SELECT KK.ID,FH.ID AS FID,MK.MINGC AS MEIKXXB_ID,FH.MEIKXXB_ID AS MEIKXXB_ID2,\n " +
				" YS.MINGC AS YUNSDWB_ID,FH.YUNSDWB_ID AS YUNSDWB_ID2,FH.DAOHRQ,FH.FAHRQ, \n" +
				" DECODE(KK.ID,NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetmj'),KK.HETMJ) AS HETMJ,FH.BIAOZ,FH.JINGZ, \n" +
				" DECODE(KK.ID,NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetrz'),KK.HETRZ) AS HETRZ, \n" +
				" DECODE(KK.ID,NULL,ROUND(KF.QNET_AR * 1000 / 4.1816, 0),KK.KUANGFRZ) AS KUANGFRZ, \n" +
				" DECODE(KK.ID,NULL,ROUND(ZL.QNET_AR * 1000 / 4.1816, 0),KK.RUCRZ) AS RUCRZ, \n" +
				" DECODE(YF.ID,NULL,KUIDKK_YUNF(FH.DIANCXXB_ID,FH.MEIKXXB_ID,FH.YUNSDWB_ID,FH.DAOHRQ,'yunf'),YF.YUNF) AS YUNF \n" +
				" FROM (SELECT F.ZHILB_ID,F.KUANGFZLB_ID,F.DIANCXXB_ID,F.MEIKXXB_ID, \n" +
				" F.ID,F.JINGZ,F.BIAOZ,F.YUNSFSB_ID,F.FAHRQ,F.DAOHRQ, \n" +
				" NVL((SELECT (SELECT YS.ID FROM YUNSDWB YS WHERE YS.ID = C.YUNSDWB_ID) \n" +
				" FROM CHEPB C WHERE C.FAHB_ID = F.ID AND ROWNUM = 1),0) YUNSDWB_ID FROM FAHB F \n"
				+riqitiaoj+" fh,ZHILB ZL,MEIKXXB MK,KUANGFZLB KF,VWDIANC D,KUIDKKB_WH KK,KUIDKKB_WH_YUNF YF,YUNSDWB YS \n" +
				" WHERE FH.ZHILB_ID = ZL.ID(+) \n " +
				" AND FH.KUANGFZLB_ID = KF.ID(+) \n" +
				" AND FH.DIANCXXB_ID = D.ID \n" +
				" AND FH.MEIKXXB_ID = MK.ID \n" +
				" AND FH.YUNSDWB_ID = YS.ID(+) \n" +
				" AND FH.ID = KK.FAHB_ID(+) \n" +
				" AND FH.ID = YF.FAHB_ID(+) \n" +
				diancTiaoj+"  ) SR  GROUP BY (MEIKXXB_ID2, MEIKXXB_ID, YUNSDWB_ID,TO_CHAR(SR.FAHRQ, 'mm')))))DR \n" +
			    " GROUP BY ROLLUP(MEIKXXB_ID,YUNSDWB_ID,FHRQ,RIQ) \n" +
			    " HAVING NOT GROUPING(YUNSDWB_ID)+GROUPING(RIQ)=1  \n" +
			    " ORDER BY GROUPING(MEIKXXB_ID),MEIKXXB_ID, GROUPING(YUNSDWB_ID) ,YUNSDWB_ID,FHRQ) \n");
		
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String ArrHeader[][] = new String[2][17];
		
		ArrHeader[0] = new String[] {"煤矿", "运输单位",tongjsf,"价格情况", "价格情况", "价格情况","价格情况", "价格情况", "入厂煤数量热值情况","入厂煤数量热值情况",
				"入厂煤数量热值情况","入厂煤数量热值情况","入厂煤数量热值情况","亏(盈)卡情况","亏(盈)卡情况","金额(元)","金额(元)","备注"};
		ArrHeader[1] = new String[] {"煤矿", "运输单位",tongjsf,"合同煤价<br>(元/吨)", "煤价<br>(元/吨)", "暂估煤价<br>(元/吨)", "运费<br>(元/吨)",
				"入厂标煤单价<br>(元/吨)","矿发量<br>(吨)","入厂量<br>(吨)","合同热值<br>(Kcal/Kg)","入厂热值<br>(Kcal/Kg)","结算热值<br>(Kcal/Kg)",
				"吨亏卡<br>影响","金额(元)","煤款","运费","备注"};

		int ArrWidth[] = new int[] {100, 100, 180,55, 55, 55, 55, 55, 55, 55,55 ,55,55, 55, 100,100, 100,100};

//		调整显示格式	
		String[] strFormat=new String[18];
		for (int i=0;i<=18;i++){
			if(i==3||i==4||i==5||i==6||i==7||i==8||i==9||i==13||i==14||i==15||i==16){
				strFormat[i]="0.00";
			}
			if(i==0||i==1||i==2||i==17){
				strFormat[i]="";
			}
			if(i==10||i==11||i==12){
				strFormat[i]="0";
			}
		}
		
		
		if(this.getRiqi().equals(this.getRiqi2())){
			rt.setTitle(this.getRiqi()+"结算暂估情况", ArrWidth);
		}else{
			rt.setTitle(this.getRiqi()+"至"+this.getRiqi2()+"结算暂估情况", ArrWidth);
		}
		
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
		//rt.setDefaultTitle(19, 4, tongjsf+"：" + this.getRiqi()+"至"+this.getRiqi2(), Table.ALIGN_LEFT);
		
		rt.setBody(new Table(rs, 2, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(1000);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.createDefautlFooter(ArrWidth);
		
		//----------------
		
		
		rt.body.ShowZero=true;
//		调整显示格式
		rt.body.setColFormat(strFormat);
		
		//页面为空的时候都显示0,但是运费这一列除外
		for(int ab=1;ab<=rt.body.getRows();ab++){
			if(rt.body.getCellValue(ab, 6).equals("0")){
				rt.body.setCellValue(ab, 6, "");
			}
		}
		
		
		//----------------
		
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

	private String getDiquTj() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
//		String riqi=this.getRiqi();
		String diancTiaoj="";
		if(this.getTreeid_dc().equals("300")){
			diancTiaoj="";
		}else{
			diancTiaoj=" and  d.id in("+this.getTreeid_dc()+") \n";
		}
		
		String riqitiaoj="";
		String tongjxsfs="";
		String tongjfstj=this.getTongjfsValue().getValue();
		if(tongjfstj.equals("到货日期")){
			riqitiaoj=" where f.daohrq BETWEEN to_date('"+this.getRiqi()+"','yyyy-mm-dd') \n" +
			  " AND to_date('"+this.getRiqi2()+"','yyyy-mm-dd')) \n";
	
			tongjxsfs="  to_char(min(sr.daohrq),'yyyy-mm-dd')||'至'||to_char(max(sr.daohrq),'yyyy-mm-dd') as riq,\n" ;
		}else{
			riqitiaoj=" where f.fahrq BETWEEN to_date('"+this.getRiqi()+"','yyyy-mm-dd') \n" +
			  " AND to_date('"+this.getRiqi2()+"','yyyy-mm-dd'))  \n";
	
			tongjxsfs="  to_char(min(sr.fahrq),'yyyy-mm-dd')||'至'||to_char(max(sr.fahrq),'yyyy-mm-dd') as riq,\n" ;
		}

//		按地区
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT MKDQMC,MEIKMC,YUNSDWMC,RIQ,HETMJ,MEIJ, \n" +
				" DECODE(NVL(RUCL,0),0,0,ROUND(MEIJ*KUANGFL/RUCL,2))ZGMJ,YUNF, \n" +
				" ROUND(DECODE(NVL(RUCRZ,0),0,0,(ROUND(DECODE(NVL(RUCL,0),0,0,MEIJ*KUANGFL/RUCL),2)/1.17+YUNF/1.11)*7000/RUCRZ),2)RCBMDJ, \n" +
				" KUANGFL,RUCL,HETRZ,RUCRZ,JIESRZ,MEIJ-HETMJ DUNKKYX,round((MEIJ-HETMJ)*RUCL,2) DUNKKYXCB,\n" +
				" DECODE(NVL(RUCL,0),0,0,round(ROUND(MEIJ*KUANGFL/RUCL,2)*RUCL,2)) MEIK, \n" +
				" round(YUNF*RUCL,2) YUNFEI,(select '' from dual) beiz \n" +
				" FROM(SELECT  \n" +
				" DECODE(GROUPING(MEIKXXB_ID)+GROUPING(MKDQ),2,'总计',MKDQ)MKDQMC, \n"+
				" DECODE(GROUPING(MEIKXXB_ID)+GROUPING(MKDQ),1,'合计',MEIKXXB_ID)MEIKMC," +
				" DECODE(GROUPING(YUNSDWB_ID)+GROUPING(MEIKXXB_ID),1,'小计',YUNSDWB_ID)YUNSDWMC,FHRQ,RIQ, \n" +
				" decode(sum(jingz),0,0,round_new(sum(HETMJ*jingz)/sum(jingz),2)) as HETMJ,\n " +
				" decode(sum(jingz),0,0,round_new(sum(MEIJ*jingz)/sum(jingz),2)) as MEIJ, \n" +
				" decode(sum(jingz),0,0,round_new(sum(YUNF*jingz)/sum(jingz),2)) as YUNF, \n" +
				" SUM(BIAOZ) KUANGFL,SUM(JINGZ) RUCL, \n" +
				" decode(sum(jingz),0,0,round_new(sum(HETRZ*jingz)/sum(jingz),0)) as HETRZ, \n" +
				" decode(sum(jingz),0,0,round_new(sum(RUCRZ*jingz)/sum(jingz),0)) as RUCRZ, \n" +
				" decode(sum(jingz),0,0,round_new(sum(JIESRZ*jingz)/sum(jingz),0)) as JIESRZ  \n" +
				" from(SELECT MKDQ,MEIKXXB_ID2,MEIKXXB_ID,YUNSDWB_ID,FHRQ,DAOHRQ,RIQ,HETMJ,YUNF,JINGZ,BIAOZ,HETRZ,KUANGFRZ,RUCRZ,JIESRZ, \n" +
				" CASE WHEN JIESRZ > HETRZ THEN KUIDKK_MJ(HETMJ, HETRZ, JIESRZ, RUCRZ, JIANGMJ) ELSE  \n" +
				" KUIDKK_MJ(HETMJ, HETRZ, JIESRZ, RUCRZ, KOUMJ) END MEIJ,JIANGMJ,KOUMJ \n" +
				" FROM (SELECT MKDQ,MEIKXXB_ID2,MEIKXXB_ID,YUNSDWB_ID,FHRQ,DAOHRQ,RIQ,HETMJ,YUNF,JINGZ,BIAOZ,HETRZ,KUANGFRZ,RUCRZ,\n" +
				" KUIDKK_JIESRZ(MEIKXXB_ID2,DAOHRQ,RUCRZ,KUANGFRZ,HETRZ,BIAOZ,JINGZ,'jiesrz') JIESRZ,\n" +
				" KUIDKK_NEW(MEIKXXB_ID2, DAOHRQ, 'jiangmj') JIANGMJ,\n" +
				" KUIDKK_NEW(MEIKXXB_ID2, DAOHRQ, 'koumj') KOUMJ  \n" +
				" FROM (SELECT MKDQ,MEIKXXB_ID2,MEIKXXB_ID,YUNSDWB_ID, \n" +
				" GROUPING(YUNSDWB_ID) GYSZT, TO_CHAR(SR.FAHRQ, 'mm') FHRQ,MAX(SR.DAOHRQ) DAOHRQ, \n" +
				tongjxsfs +" ROUND(DECODE(SUM(SR.JINGZ),0,0,SUM(SR.HETMJ * SR.JINGZ) / SUM(SR.JINGZ)),0) HETMJ, \n" +
				" ROUND(DECODE(SUM(SR.JINGZ),0,0,SUM(SR.YUNF * SR.JINGZ) / SUM(SR.JINGZ)),2) YUNF, \n" +
				" SUM(SR.JINGZ) JINGZ,SUM(SR.BIAOZ) BIAOZ, \n" +
				" ROUND(DECODE(SUM(SR.JINGZ),0,0,SUM(SR.HETRZ * SR.JINGZ) / SUM(SR.JINGZ)),0) HETRZ, \n" +
				" ROUND(DECODE(SUM(SR.JINGZ),0,0,SUM(SR.KUANGFRZ * SR.JINGZ) /SUM(SR.JINGZ)),0) KUANGFRZ, \n" +
				" ROUND(DECODE(SUM(SR.JINGZ),0,0,SUM(SR.RUCRZ * SR.JINGZ) / SUM(SR.JINGZ)),0) RUCRZ \n" +
				" FROM (SELECT KK.ID,FH.ID AS FID,DQ.MINGC AS MKDQ,MK.MINGC AS MEIKXXB_ID,FH.MEIKXXB_ID AS MEIKXXB_ID2, \n" +
				" YS.MINGC AS YUNSDWB_ID,FH.YUNSDWB_ID AS YUNSDWB_ID2,FH.DAOHRQ,FH.FAHRQ, \n" +
				" DECODE(KK.ID,NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetmj'),KK.HETMJ) AS HETMJ,FH.BIAOZ,FH.JINGZ, \n" +
				" DECODE(KK.ID,NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetrz'),KK.HETRZ) AS HETRZ, \n" +
				" DECODE(KK.ID,NULL,ROUND(KF.QNET_AR * 1000 / 4.1816, 0),KK.KUANGFRZ) AS KUANGFRZ, \n" +
				" DECODE(KK.ID,NULL,ROUND(ZL.QNET_AR * 1000 / 4.1816, 0),KK.RUCRZ) AS RUCRZ, \n" +
				" DECODE(YF.ID,NULL,KUIDKK_YUNF(FH.DIANCXXB_ID,FH.MEIKXXB_ID,FH.YUNSDWB_ID,FH.DAOHRQ,'yunf'),YF.YUNF) AS YUNF  \n" +
				" FROM (SELECT F.ZHILB_ID,F.KUANGFZLB_ID,F.DIANCXXB_ID,F.MEIKXXB_ID, \n" +
				" F.ID,F.JINGZ,F.BIAOZ,F.YUNSFSB_ID,F.FAHRQ,F.DAOHRQ, \n" +
				" NVL((SELECT (SELECT YS.ID FROM YUNSDWB YS WHERE YS.ID = C.YUNSDWB_ID) \n" +
				" FROM CHEPB C WHERE C.FAHB_ID = F.ID AND ROWNUM = 1),0) YUNSDWB_ID FROM FAHB F \n"
				+riqitiaoj+" fh,ZHILB ZL,MEIKXXB MK,KUANGFZLB KF,VWDIANC D,KUIDKKB_WH KK,KUIDKKB_WH_YUNF YF,YUNSDWB YS,gongysb g,meikdqb dq  \n" +
				" WHERE FH.ZHILB_ID = ZL.ID(+)  \n" +
				" AND FH.KUANGFZLB_ID = KF.ID(+) \n" +
				" and mk.meikdq_id=g.id \n" +
				" and g.fuid=dq.id  \n" +
				" AND FH.DIANCXXB_ID = D.ID  \n" +
				" AND FH.MEIKXXB_ID = MK.ID  \n" +
				" AND FH.YUNSDWB_ID = YS.ID(+)  \n" +
				" AND FH.ID = KK.FAHB_ID(+) \n" +
				" AND FH.ID = YF.FAHB_ID(+) \n" +
				diancTiaoj+"  ) SR  GROUP BY (MKDQ,MEIKXXB_ID2, MEIKXXB_ID, YUNSDWB_ID,TO_CHAR(SR.FAHRQ, 'mm')))))DR  \n" +
			    "  GROUP BY ROLLUP(MKDQ,MEIKXXB_ID,YUNSDWB_ID,FHRQ,RIQ) \n" +
			    "  HAVING NOT GROUPING(YUNSDWB_ID)+GROUPING(RIQ)=1  \n" +
			    "  ORDER BY GROUPING(MKDQ),MKDQ,GROUPING(MEIKXXB_ID),MEIKXXB_ID, GROUPING(YUNSDWB_ID) ,YUNSDWB_ID,FHRQ) \n");
		
		
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String ArrHeader[][] = new String[2][18];
		
		ArrHeader[0] = new String[] {"煤矿地区","煤矿", "运输单位",tongjfstj, "价格情况", "价格情况", "价格情况","价格情况", "价格情况", 
				"入厂煤数量热值情况","入厂煤数量热值情况", "入厂煤数量热值情况","入厂煤数量热值情况",
				"入厂煤数量热值情况","亏(盈)卡情况","亏(盈)卡情况","金额(元)","金额(元)","备注"};
		ArrHeader[1] = new String[] {"煤矿地区","煤矿", "运输单位",tongjfstj, "合同煤价<br>(元/吨)", "煤价<br>(元/吨)", 
				"暂估煤价<br>(元/吨)", "运费<br>(元/吨)","入厂标煤单价<br>(元/吨)","矿发量<br>(吨)","入厂量<br>(吨)","合同热值<br>(Kcal/Kg)",
				"入厂热值<br>(Kcal/Kg)","结算热值<br>(Kcal/Kg)","吨亏卡<br>影响","金额(元)","煤款","运费","备注"};

		int ArrWidth[] = new int[] {80,100, 100, 180,55, 55, 55, 55, 55, 55, 55,55 , 55,55,55, 100,100, 100,100};
		
//		调整显示格式	
		String[] strFormat=new String[19];
		for (int i=0;i<=19;i++){
			if(i==4||i==5||i==6||i==7||i==8||i==9||i==10||i==14||i==15||i==16||i==17){
				strFormat[i]="0.00";
			}
			if(i==0||i==1||i==2||i==3||i==18){
				strFormat[i]="";
			}
			if(i==11||i==12||i==13){
				strFormat[i]="0";
			}
		}
		
		if(this.getRiqi().equals(this.getRiqi2())){
			rt.setTitle(this.getRiqi()+"结算暂估情况", ArrWidth);
		}else{
			rt.setTitle(this.getRiqi()+"至"+this.getRiqi2()+"结算暂估情况", ArrWidth);
		}
		
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
		//rt.setDefaultTitle(19, 4, tongjfstj+"：" + this.getRiqi()+"至"+this.getRiqi2(), Table.ALIGN_LEFT);
		
		rt.setBody(new Table(rs, 2, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(1000);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.createDefautlFooter(ArrWidth);
		
		//----------------
		
		
		rt.body.ShowZero=true;
		
//		调整显示格式
		rt.body.setColFormat(strFormat);
		
		//页面为空的时候都显示0,但是运费这一列除外
		for(int ab=1;ab<=rt.body.getRows();ab++){
			if(rt.body.getCellValue(ab, 7).equals("0")){
				rt.body.setCellValue(ab, 7, "");
			}
		}
		
		
		//----------------
		
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
	
	
	private String getyunsfsTj() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
//		String riqi=this.getRiqi();
		String diancTiaoj="";
		if(this.getTreeid_dc().equals("300")){
			diancTiaoj="";
		}else{
			diancTiaoj=" and  d.id in("+this.getTreeid_dc()+") \n";
		}
		
		String riqitiaoj="";
		String tongjxsfs="";
		String tongjfstj=this.getTongjfsValue().getValue();
		if(tongjfstj.equals("到货日期")){
			riqitiaoj=" where f.daohrq BETWEEN to_date('"+this.getRiqi()+"','yyyy-mm-dd') \n" +
			  " AND to_date('"+this.getRiqi2()+"','yyyy-mm-dd')) \n";
	
			tongjxsfs="  to_char(min(sr.daohrq),'yyyy-mm-dd')||'至'||to_char(max(sr.daohrq),'yyyy-mm-dd') as riq,\n" ;
		}else{
			riqitiaoj=" where f.fahrq BETWEEN to_date('"+this.getRiqi()+"','yyyy-mm-dd') \n" +
			  " AND to_date('"+this.getRiqi2()+"','yyyy-mm-dd'))  \n";
	
			tongjxsfs="  to_char(min(sr.fahrq),'yyyy-mm-dd')||'至'||to_char(max(sr.fahrq),'yyyy-mm-dd') as riq,\n" ;
		}

//		按运输方式
		StringBuffer buffer = new StringBuffer();
		buffer.append(
				" SELECT YUNSFSMC, MKDQMC,MEIKMC,YUNSDWMC,RIQ,HETMJ,MEIJ,\n" +
				" DECODE(NVL(RUCL,0),0,0,ROUND(MEIJ*KUANGFL/RUCL,2))ZGMJ,YUNF,\n" + 
				" ROUND(DECODE(NVL(RUCRZ,0),0,0,(ROUND(DECODE(NVL(RUCL,0),0,0,MEIJ*KUANGFL/RUCL),2)/1.17+YUNF/1.11)*7000/RUCRZ),2)RCBMDJ,\n" + 
				" KUANGFL,RUCL,HETRZ,RUCRZ,JIESRZ,MEIJ-HETMJ DUNKKYX,round((MEIJ-HETMJ)*RUCL,2) DUNKKYXCB,\n" + 
				" DECODE(NVL(RUCL,0),0,0,round(ROUND(MEIJ*KUANGFL/RUCL,2)*RUCL,2)) MEIK,\n" + 
				" round(YUNF*RUCL,2) YUNFEI,(select '' from dual) beiz\n" + 
				" FROM(SELECT\n" + 
				"decode(grouping(yunsfs)+grouping(mkdq),1,null,yunsfs)yunsfsmc,\n" + 
				"decode(grouping(yunsfs)+grouping(mkdq),2,'总计',MKDQ) MKDQMC,\n" + 
				"decode(grouping(MEIKXXB_ID)+grouping(mkdq),1,'合计',MEIKXXB_ID) MEIKMC,\n" + 
				"decode(grouping(MEIKXXB_ID)+grouping(YUNSDWB_ID),1,'小计',YUNSDWB_ID) YUNSDWMC,\n" + 
				"  FHRQ,RIQ,\n" + 
				" decode(sum(jingz),0,0,round_new(sum(HETMJ*jingz)/sum(jingz),2)) as HETMJ,\n" + 
				"  decode(sum(jingz),0,0,round_new(sum(MEIJ*jingz)/sum(jingz),2)) as MEIJ,\n" + 
				" decode(sum(jingz),0,0,round_new(sum(YUNF*jingz)/sum(jingz),2)) as YUNF,\n" + 
				" SUM(BIAOZ) KUANGFL,SUM(JINGZ) RUCL,\n" + 
				" decode(sum(jingz),0,0,round_new(sum(HETRZ*jingz)/sum(jingz),0)) as HETRZ,\n" + 
				" decode(sum(jingz),0,0,round_new(sum(RUCRZ*jingz)/sum(jingz),0)) as RUCRZ,\n" + 
				" decode(sum(jingz),0,0,round_new(sum(JIESRZ*jingz)/sum(jingz),0)) as JIESRZ\n" + 
				" from(SELECT yunsfs,MKDQ,MEIKXXB_ID2,MEIKXXB_ID,YUNSDWB_ID,FHRQ,DAOHRQ,RIQ,HETMJ,YUNF,JINGZ,BIAOZ,HETRZ,KUANGFRZ,RUCRZ,JIESRZ,\n" + 
				" CASE WHEN JIESRZ > HETRZ THEN KUIDKK_MJ(HETMJ, HETRZ, JIESRZ, RUCRZ, JIANGMJ) ELSE\n" + 
				" KUIDKK_MJ(HETMJ, HETRZ, JIESRZ, RUCRZ, KOUMJ) END MEIJ,JIANGMJ,KOUMJ\n" + 
				" FROM (SELECT yunsfs,MKDQ,MEIKXXB_ID2,MEIKXXB_ID,YUNSDWB_ID,FHRQ,DAOHRQ,RIQ,HETMJ,YUNF,JINGZ,BIAOZ,HETRZ,KUANGFRZ,RUCRZ,\n" + 
				" KUIDKK_JIESRZ(MEIKXXB_ID2,DAOHRQ,RUCRZ,KUANGFRZ,HETRZ,BIAOZ,JINGZ,'jiesrz') JIESRZ,\n" + 
				" KUIDKK_NEW(MEIKXXB_ID2, DAOHRQ, 'jiangmj') JIANGMJ,\n" + 
				" KUIDKK_NEW(MEIKXXB_ID2, DAOHRQ, 'koumj') KOUMJ\n" + 
				" FROM (SELECT yunsfs, MKDQ,MEIKXXB_ID2,MEIKXXB_ID,YUNSDWB_ID,\n" + 
				" GROUPING(YUNSDWB_ID) GYSZT, TO_CHAR(SR.FAHRQ, 'mm') FHRQ,MAX(SR.DAOHRQ) DAOHRQ,\n" + 
				tongjxsfs+" ROUND(DECODE(SUM(SR.JINGZ),0,0,SUM(SR.HETMJ * SR.JINGZ) / SUM(SR.JINGZ)),0) HETMJ,\n" + 
				" ROUND(DECODE(SUM(SR.JINGZ),0,0,SUM(SR.YUNF * SR.JINGZ) / SUM(SR.JINGZ)),2) YUNF,\n" + 
				" SUM(SR.JINGZ) JINGZ,SUM(SR.BIAOZ) BIAOZ,\n" + 
				" ROUND(DECODE(SUM(SR.JINGZ),0,0,SUM(SR.HETRZ * SR.JINGZ) / SUM(SR.JINGZ)),0) HETRZ,\n" + 
				" ROUND(DECODE(SUM(SR.JINGZ),0,0,SUM(SR.KUANGFRZ * SR.JINGZ) /SUM(SR.JINGZ)),0) KUANGFRZ,\n" + 
				" ROUND(DECODE(SUM(SR.JINGZ),0,0,SUM(SR.RUCRZ * SR.JINGZ) / SUM(SR.JINGZ)),0) RUCRZ\n" + 
				" FROM (SELECT yb.mingc as yunsfs, KK.ID,FH.ID AS FID,DQ.MINGC AS MKDQ,MK.MINGC AS MEIKXXB_ID,FH.MEIKXXB_ID AS MEIKXXB_ID2,\n" + 
				" YS.MINGC AS YUNSDWB_ID,FH.YUNSDWB_ID AS YUNSDWB_ID2,FH.DAOHRQ,FH.FAHRQ,\n" + 
				" DECODE(KK.ID,NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetmj'),KK.HETMJ) AS HETMJ,FH.BIAOZ,FH.JINGZ,\n" + 
				" DECODE(KK.ID,NULL,KUIDKK(MK.ID, FH.DAOHRQ, 'hetrz'),KK.HETRZ) AS HETRZ,\n" + 
				" DECODE(KK.ID,NULL,ROUND(KF.QNET_AR * 1000 / 4.1816, 0),KK.KUANGFRZ) AS KUANGFRZ,\n" + 
				" DECODE(KK.ID,NULL,ROUND(ZL.QNET_AR * 1000 / 4.1816, 0),KK.RUCRZ) AS RUCRZ,\n" + 
				" DECODE(YF.ID,NULL,KUIDKK_YUNF(FH.DIANCXXB_ID,FH.MEIKXXB_ID,FH.YUNSDWB_ID,FH.DAOHRQ,'yunf'),YF.YUNF) AS YUNF\n" + 
				" FROM (SELECT F.ZHILB_ID,F.KUANGFZLB_ID,F.DIANCXXB_ID,F.MEIKXXB_ID,\n" + 
				" F.ID,F.JINGZ,F.BIAOZ,F.YUNSFSB_ID,F.FAHRQ,F.DAOHRQ,\n" + 
				" NVL((SELECT (SELECT YS.ID FROM YUNSDWB YS WHERE YS.ID = C.YUNSDWB_ID)\n" + 
				" FROM CHEPB C WHERE C.FAHB_ID = F.ID AND ROWNUM = 1),0) YUNSDWB_ID FROM FAHB F\n" + 
				riqitiaoj+	" fh,ZHILB ZL,MEIKXXB MK,KUANGFZLB KF,VWDIANC D,KUIDKKB_WH KK,KUIDKKB_WH_YUNF YF,YUNSDWB YS,gongysb g,meikdqb dq,yunsfsb yb\n" + 
				" WHERE FH.ZHILB_ID = ZL.ID(+)\n" + 
				" AND FH.KUANGFZLB_ID = KF.ID(+)\n" + 
				" and mk.meikdq_id=g.id\n" + 
				" and g.fuid=dq.id\n" + 
				" AND FH.DIANCXXB_ID = D.ID\n" + 
				" AND FH.MEIKXXB_ID = MK.ID\n" + 
				" AND FH.YUNSDWB_ID = YS.ID(+)\n" + 
				" AND FH.ID = KK.FAHB_ID(+)\n" + 
				" AND FH.ID = YF.FAHB_ID(+)\n" + 
				" and fh.yunsfsb_id= yb.id\n" + 
				diancTiaoj+"  ) SR  GROUP BY (yunsfs,MKDQ,MEIKXXB_ID2, MEIKXXB_ID, YUNSDWB_ID,TO_CHAR(SR.FAHRQ, 'mm')))))DR\n" + 
				"  GROUP BY ROLLUP(yunsfs,MKDQ,MEIKXXB_ID,YUNSDWB_ID,FHRQ,RIQ)\n" + 
				"\n" + 
				" HAVING not ((GROUPING(yunsfs)+GROUPING(MKDQ)+grouping(MEIKXXB_ID)+grouping(YUNSDWB_ID)+grouping(FHRQ)+grouping(RIQ))=1\n" + 
				"  or  (GROUPING(yunsfs)+GROUPING(MKDQ)+grouping(MEIKXXB_ID)+grouping(YUNSDWB_ID)+grouping(FHRQ)+grouping(RIQ))=2\n" + 
				"  or  (GROUPING(yunsfs)+GROUPING(MKDQ)+grouping(MEIKXXB_ID)+grouping(YUNSDWB_ID)+grouping(FHRQ)+grouping(RIQ))=5)\n" + 
				" ORDER BY grouping(yunsfs),yunsfs, GROUPING(MKDQ),MKDQ,GROUPING(MEIKXXB_ID),MEIKXXB_ID, GROUPING(YUNSDWB_ID) ,YUNSDWB_ID,FHRQ)");
		
		
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String ArrHeader[][] = new String[2][20];
		
		ArrHeader[0] = new String[] {"运输方式","煤矿地区","煤矿", "运输单位",tongjfstj, "价格情况", "价格情况", "价格情况",
				"价格情况", "价格情况", "入厂煤数量热值情况","入厂煤数量热值情况", "入厂煤数量热值情况","入厂煤数量热值情况",
				"入厂煤数量热值情况","亏(盈)卡情况","亏(盈)卡情况","金额(元)","金额(元)","备注"};
		ArrHeader[1] = new String[] {"运输方式","煤矿地区","煤矿", "运输单位",tongjfstj, "合同煤价<br>(元/吨)", "煤价<br>(元/吨)",
				"暂估煤价<br>(元/吨)", "运费<br>(元/吨)","入厂标煤单价<br>(元/吨)","矿发量<br>(吨)","入厂量<br>(吨)",
				"合同热值<br>(Kcal/Kg)","入厂热值<br>(Kcal/Kg)","结算热值<br>(Kcal/Kg)","吨亏卡<br>影响","金额(元)","煤款","运费","备注"};

		int ArrWidth[] = new int[] {80,100, 100, 100,180, 55, 55, 55, 55, 55, 55,55 , 55,55,55, 100,100, 100,100};
		
//		调整显示格式	
		String[] strFormat=new String[20];
		for (int i=0;i<=19;i++){
			if(i==5||i==6||i==7||i==8||i==9||i==10||i==11||i==15||i==16||i==17||i==18){
				strFormat[i]="0.00";
			}
			if(i==4||i==0||i==1||i==2||i==3||i==19){
				strFormat[i]="";
			}
			if(i==13||i==14||i==12){
				strFormat[i]="0";
			}
		}
		
		if(this.getRiqi().equals(this.getRiqi2())){
			rt.setTitle(this.getRiqi()+"结算暂估情况", ArrWidth);
		}else{
			rt.setTitle(this.getRiqi()+"至"+this.getRiqi2()+"结算暂估情况", ArrWidth);
		}
		
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
		//rt.setDefaultTitle(19, 4, tongjfstj+"：" + this.getRiqi()+"至"+this.getRiqi2(), Table.ALIGN_LEFT);
		
		rt.setBody(new Table(rs, 2, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(1000);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.createDefautlFooter(ArrWidth);
		
		//----------------
		
		
		rt.body.ShowZero=true;
		
//		调整显示格式
		rt.body.setColFormat(strFormat);
		
		//页面为空的时候都显示0,但是运费这一列除外
		for(int ab=1;ab<=rt.body.getRows();ab++){
			if(rt.body.getCellValue(ab, 7).equals("0")){
				rt.body.setCellValue(ab, 7, "");
			}
		}
		
		
		//----------------
		
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
			
			this.setBaoblxValue(null);
			this.getIBaoblxModels();
			
			this.setTongjfsValue(null);
			this.getTongjfsModels();
			getSelectData();
		}
		getSelectData();

	}
	
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
			//getPrintTable();
			_RefurbishChick = false;
		}

	}

    
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		
		
	
		ComboBox tj = new ComboBox();
		tj.setTransform("TongjfsDropDown");
		tj.setListeners("select:function(){document.Form0.submit();}");
		tj.setId("Tongjfs");
		tj.setWidth(80);
		tb1.addField(tj);
		tb1.addText(new ToolbarText("-"));
		
		
		//tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("riqi", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		tb1.addField(df);
		tb1.addText(new ToolbarText("至"));
		
		DateField df2 = new DateField();
		df2.setReadOnly(true);
		df2.setValue(this.getRiqi2());
		df2.Binding("riqi2", "");// 与html页中的id绑定,并自动刷新
		df2.setId("riqi2");
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
		
		
		tb1.addText(new ToolbarText("类型:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BaoblxDropDown");
		cb.setListeners("select:function(){document.Form0.submit();}");
		cb.setId("Baoblx");
		cb.setWidth(100);
		tb1.addField(cb);
		tb1.addText(new ToolbarText("-"));
		
		
	
		
		

		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);

		setToolbar(tb1);

	}
	
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
	
	

	public boolean _Baoblxchange = false;
	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if(_BaoblxValue==null){
			_BaoblxValue=(IDropDownBean)getIBaoblxModels().getOption(0);
		}
		return _BaoblxValue;
	}

	public void setBaoblxValue(IDropDownBean Value) {
		long id = -2;
		if (_BaoblxValue != null) {
			id = _BaoblxValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange = true;
			} else {
				_Baoblxchange = false;
			}
		}
		_BaoblxValue = Value;
	}

	private IPropertySelectionModel _IBaoblxModel;

	public void setIBaoblxModel(IPropertySelectionModel value) {
		_IBaoblxModel = value;
	}

	public IPropertySelectionModel getIBaoblxModel() {
		if (_IBaoblxModel == null) {
			getIBaoblxModels();
		}
		return _IBaoblxModel;
	}

	public IPropertySelectionModel getIBaoblxModels() {
		JDBCcon con = new JDBCcon();
		try{
			List fahdwList = new ArrayList();
			fahdwList.add(new IDropDownBean(1,"按矿统计"));
			fahdwList.add(new IDropDownBean(2,"按车队统计"));
			fahdwList.add(new IDropDownBean(3,"按地区统计"));
			fahdwList.add(new IDropDownBean(4,"按运输方式统计"));
			_IBaoblxModel = new IDropDownModel(fahdwList);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
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
			fangs.add(new IDropDownBean(1,"到货日期"));
			fangs.add(new IDropDownBean(2,"发货日期"));
			_TongjfsModel = new IDropDownModel(fangs);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//con.Close();
		}
		return _TongjfsModel;
	}
}