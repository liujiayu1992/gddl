package com.zhiren.jt.het.hetspyj;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Cell;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者：夏峥
 * 时间：2013-09-18
 * 描述：增加总经理审核流程及数字签名显示。
 */

public class Hetspyjb_v2  extends BasePage implements PageValidateListener{
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
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}

//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setNianfValue(null);
			setYuefValue(null);
			setHetbhValue(null);
			getNianfModels();
			getYuefModels();
			getHetbhModels();
			setToolbar(null);
		}
//		当日期发生变化时重置合同编号下拉框
		if(nianfchanged || yuefchanged){
			nianfchanged=false;
			yuefchanged=false;
			setHetbhValue(null);
			getHetbhModels();
		}
		getToolBars() ;
		getSelectData();
	}
	
	public String getPrintTable(){
		return getSelectData();
	}

	private String getSelectData(){
			return getHuiz();
	}
	
	private String getHuiz(){
//		如果没有选择正确的合同编号那么直接显示空白页
		if(getHetbhValue().getId()==-1){
			return "";			
		}
		
		_CurrentPage=1;
		_AllPages=1;
		
		String SQL="SELECT TO_CHAR(QIANDRQ, 'yyyy\"年\"mm\"月\"dd\"日\"') QIANDRQ, HETBH, GONGFDWMC, JG.JIJ\n" +
			"  FROM HETB,\n" + 
			"       (SELECT HETB_ID, JIAG,JIJ\n" + 
			"          FROM (SELECT JG.HETB_ID, FS.MINGC||'：' || JG.JIJ || D.MINGC JIAG, JG.JIJ || D.MINGC JIJ\n" + 
			"                  FROM HETJGB JG, HETJSFSB FS, DANWB D\n" + 
			"                 WHERE JG.HETB_ID = "+getHetbhValue().getId()+"\n" + 
			"                   AND JG.HETJSFSB_ID = FS.ID\n" + 
			"                   AND JG.JIJDWID = D.ID\n" + 
			"                 ORDER BY JG.JIJ DESC)\n" + 
			"         WHERE ROWNUM = 1) JG\n" + 
			" WHERE ID = "+getHetbhValue().getId()+"\n" + 
			"   AND HETB.ID = JG.HETB_ID(+)";

		JDBCcon con = new JDBCcon();
		
		ResultSetList rsl=con.getResultSetList(SQL);
		
		String qiandrq="";
		String hetbh="";
		String gfdw="";
		String jiag="";
		while(rsl.next()){
//			qiandrq=rsl.getString("QIANDRQ");
			hetbh=rsl.getString("HETBH");
			gfdw=rsl.getString("GONGFDWMC");
			jiag="一票到厂含税价："+rsl.getString("JIJ");
		}
		String EL1=getNianfValue().getId()+"年煤炭供需合同";
		String EL2=hetbh;
		String EL3=gfdw;
		String EL4=jiag;
		
		
		SQL="SELECT TO_CHAR(RZB.SHIJ, 'yyyy\"年\"mm\"月\"dd\"日\"') SHIJ,\n" +
			"       (SELECT MAX(MINGC) FROM RENYXXB WHERE QUANC = RZB.CAOZY) CAORY\n" + 
			"  FROM (SELECT MAX(RZB.ID) ID\n" + 
			"          FROM HETB, LIUCGZB RZB\n" + 
			"         WHERE HETB.LIUCGZID = RZB.LIUCGZID\n" + 
			"           AND HETB.ID = "+getHetbhValue().getId()+"\n" + 
			"         GROUP BY QIANQZTMC) RZ,\n" + 
			"       LIUCGZB RZB\n" + 
			" WHERE RZ.ID = RZB.ID\n" + 
			" ORDER BY RZB.ID";
		rsl = con.getResultSetList(SQL);
		
		String JBRY="";
		String SHRY="";
		String BMZGSH="";
		String BMZGSHYJ="";
		String BMZGSHRQ="";
		String RLBSHYJ="";
		String RLBSHRY="";
		String RLBSHRQ="";
		String CWSHYJ="";
		String CWSHRY="";
		String CWSHRQ="";
		String JCSHYJ="";
		String JCSHRY="";
		String JCZG="";
		String JCSHRQ="";
		String LDYJ="";
		String LDQM="";
		String SHRQ="";
		
		String ZJLYJ="";
		String ZJLQM="";
		String ZJLRQ="";

		String qianm[][]=new String[rsl.getRows()][2];
		for(int i=0;i<rsl.getRows();i++){
			rsl.next();
			qianm[i][0]=rsl.getString(0);
			qianm[i][1]=rsl.getString(1);
		}
		
		if(qianm.length>1){
			JBRY=FileChk(qianm[0][1]);
			qiandrq=qianm[0][0];
		}
		if(qianm.length>2){
			SHRY=FileChk(qianm[1][1]);
		}
		if(qianm.length>3){
			BMZGSHYJ=FileChk(qianm[2][1]+"_qm");
			BMZGSH=FileChk(qianm[2][1]);
			BMZGSHRQ=FileChk(qianm[2][0]);
		}
		
		if(qianm.length>4){
			RLBSHYJ=FileChk(qianm[3][1]+"_qm");
			RLBSHRY=FileChk(qianm[3][1]);
			RLBSHRQ=qianm[3][0];
		}
		if(qianm.length>5){
			CWSHYJ=FileChk(qianm[4][1]+"_qm");
			CWSHRY=FileChk(qianm[4][1]);
			CWSHRQ=qianm[4][0];
		}
		if(qianm.length>6){
			JCSHRY=FileChk(qianm[5][1]);
			JCSHRQ=qianm[5][0];
		}

		if(qianm.length>7){
			JCSHYJ=FileChk(qianm[6][1]+"_qm");
			JCZG=FileChk(qianm[6][1]);
			LDYJ=FileChk(qianm[7][1]+"_qm");
			LDQM=FileChk(qianm[7][1]);
			SHRQ=qianm[7][0];
		}
		
		if(qianm.length>8){
			ZJLYJ=FileChk(qianm[8][1]+"_qm");
			ZJLQM=FileChk(qianm[8][1]);
			ZJLRQ=qianm[8][0];
		}
		
		String ArrHeader[][]=new String[23][11];
		ArrHeader[0]=new String[] {"合同（协议）名称","",EL1,"","","","","合同编号",EL2,"",""};
		ArrHeader[1]=new String[] {"承包商/供应商","",EL3,"","","","","","","",""};
		ArrHeader[2]=new String[] {"概算范围（元）","","","","","","合同金额（元）","",EL4,"",""};
		ArrHeader[3]=new String[] {"承&nbsp办","计划营销部","","&nbsp审核人：",SHRY,"","","&nbsp经办人：",JBRY,"",""};
		ArrHeader[4]=new String[] {"部&nbsp门","","","","","","","","","",""};
		ArrHeader[5]=new String[] {"项&nbsp目","列支燃料费用","","","","","","","","",""};
		ArrHeader[6]=new String[] {"说&nbsp明","","","","","","","","","",""};
		ArrHeader[7]=new String[] {"承&nbsp办","","","","","","","","","",""};
		ArrHeader[8]=new String[] {"说&nbsp明","","","","","","","","","",""};
		ArrHeader[9]=new String[] {"需参加评审部门","","","","","","","","","",""};
		ArrHeader[10]=new String[] {"&nbsp&nbsp&nbsp&nbsp&nbsp■计划营销部&nbsp&nbsp&nbsp&nbsp&nbsp■财务产权部&nbsp&nbsp&nbsp&nbsp&nbsp■监察审计部&nbsp&nbsp&nbsp&nbsp&nbsp■燃料管理部","","","","","","","","","",""};
		ArrHeader[11]=new String[] {"","审核意见：","",BMZGSHYJ,"","","","","","",""};
		ArrHeader[12]=new String[] {"","部门：计划营销部：","","审核人：","","","部门主管：",BMZGSH,"","日期:"+BMZGSHRQ,""};
		ArrHeader[13]=new String[] {"评","审核意见： ","",RLBSHYJ,"","","","","","",""};
		ArrHeader[14]=new String[] {"审","部门：燃料管理部：","","审核人：","","","部门主管：",RLBSHRY,"","日期:"+RLBSHRQ,""};
		ArrHeader[15]=new String[] {"意","审核意见：","",CWSHYJ,"","","","","","",""};
		ArrHeader[16]=new String[] {"见","部门：财务产权部：","","审核人：","","","部门主管：",CWSHRY,"","日期:"+CWSHRQ,""};
		ArrHeader[17]=new String[] {"","审核意见：","",JCSHYJ,"","","","","","",""};
		ArrHeader[18]=new String[] {"","部门：监察审计部：","","审核人：",JCSHRY,"","部门主管：",JCZG,"","日期:"+JCSHRQ,""};
		ArrHeader[19]=new String[] {"承办部门分管领导意见","审核意见： ",LDYJ,"","签名：",LDQM,"","","","日期："+SHRQ,""};
		ArrHeader[20]=new String[] {"","","","","","","","","","",""};
		ArrHeader[21]=new String[] {"总经理审批","审核意见： ",ZJLYJ,"","签名：",ZJLQM,"","","","日期："+ZJLRQ,""};
		ArrHeader[22]=new String[] {"","","","","","","","","","",""};
		int ArrWidth[]=new int[] {45,70,54,70,54,54,80,60,54,70,70};

		Report rt=new Report();
//		 定义页Title
		Cell c = new Cell();
		c.setBorderNone();
		Table title = new Table(4, ArrWidth.length, c);
		title.setWidth(ArrWidth);
		title.setBorderNone();
		title.setCellValue(2, 1, "国电内蒙古东胜热电有限公司");
		title.setCellAlign(2, 1, Table.ALIGN_CENTER);
		title.setCellFont(2, 1, "", 16, true);
		title.mergeRowCells(2);
		
		title.setCellValue(3, 1, "<br><u>合同评审意见表</u><br>");
		title.setCellAlign(3, 1, Table.ALIGN_CENTER);
		title.setCellFont(3, 1,"宋体", 11, true);
		title.mergeRowCells(3);
		
		title.setCellValue(4, 1, "日期:"+qiandrq);
		title.setCellAlign(4, 1, Table.ALIGN_RIGHT);
		title.setCellFont(4, 1, "", 10, false);
		title.mergeRowCells(4);

		rt.setTitle(title);			
		rt.setBody(new Table(ArrHeader,0,0,0));
		rt.body.setWidth(ArrWidth);

//		合并单元格
		rt.body.mergeCell(1,1,1,2);
		rt.body.mergeCell(1,3,1,7);
		rt.body.mergeCell(1,9,1,11);
		rt.body.mergeCell(2,1,2,2);
		rt.body.mergeCell(2,3,2,11);
		rt.body.mergeCell(3,1,3,2);
		rt.body.mergeCell(3,3,3,6);
		rt.body.mergeCell(3,7,3,8);
		rt.body.mergeCell(3,9,3,11);
		rt.body.mergeCell(4,2,5,3);
		rt.body.mergeCell(4,4,5,4);
		rt.body.mergeCell(4,5,5,7);
		rt.body.mergeCell(4,8,5,8);
		rt.body.mergeCell(4,9,5,11);
		rt.body.mergeCell(6,2,7,11);
		rt.body.mergeCell(8,2,9,11);
		rt.body.mergeCell(10,1,10,11);
		rt.body.mergeCell(11,1,11,11);
		
		rt.body.mergeCell(12,2,12,3);
		rt.body.mergeCell(12,4,12,11);
//		
		rt.body.mergeCell(13,2,13,3);
		rt.body.mergeCell(13,5,13,6);
		rt.body.mergeCell(13,8,13,9);
		rt.body.mergeCell(13,10,13,11);
//		
		rt.body.mergeCell(14,2,14,3);
		rt.body.mergeCell(14,4,14,11);
//		
		rt.body.mergeCell(15,2,15,3);
		rt.body.mergeCell(15,5,15,6);
		rt.body.mergeCell(15,8,15,9);
		rt.body.mergeCell(15,10,15,11);
//		
		rt.body.mergeCell(16,2,16,3);
		rt.body.mergeCell(16,4,16,11);
//		
		rt.body.mergeCell(17,2,17,3);
		rt.body.mergeCell(17,5,17,6);
		rt.body.mergeCell(17,8,17,9);
		rt.body.mergeCell(17,10,17,11);
//		
		rt.body.mergeCell(18,2,18,3);
		rt.body.mergeCell(18,4,18,11);
//		
		rt.body.mergeCell(19,2,19,3);
		rt.body.mergeCell(19,5,19,6);
		rt.body.mergeCell(19,8,19,9);
		rt.body.mergeCell(19,10,19,11);
		
		rt.body.mergeCell(20,3,20,4);
		rt.body.mergeCell(20,6,20,9);
		rt.body.mergeCell(20,10,20,11);
		
		rt.body.mergeCell(20,1,21,1);
		rt.body.mergeCell(20,2,21,2);
		rt.body.mergeCell(20,3,21,4);
		rt.body.mergeCell(20,5,21,5);
		rt.body.mergeCell(20,6,21,9);
		rt.body.mergeCell(20,10,21,11);
		
		rt.body.mergeCell(22,3,22,4);
		rt.body.mergeCell(22,6,22,9);
		rt.body.mergeCell(22,10,22,11);
		
//		统一高度40
		rt.body.setRowHeight(45);
//		
//		单独格式化特殊行
		
		rt.body.setRowHeight(4,20);
		rt.body.setRowHeight(5,20);
		rt.body.setRowHeight(6,20);
		rt.body.setRowHeight(7,20);
		rt.body.setRowHeight(8,20);
		rt.body.setRowHeight(9,20);
		rt.body.setRowHeight(10,20);
		rt.body.setRowHeight(20,80);
		rt.body.setRowHeight(22,70);
		rt.body.setRowHeight(23,1);

//		设置特殊单元格边框
		rt.body.setCellBorderbottom(4, 1, 0);
		rt.body.setCellBorderbottom(6, 1, 0);
		rt.body.setCellBorderbottom(8, 1, 0);
		rt.body.setCellBorderbottom(12, 1, 0);
		rt.body.setCellBorderbottom(13, 1, 0);
		rt.body.setCellBorderbottom(14, 1, 0);
		rt.body.setCellBorderbottom(15, 1, 0);
		rt.body.setCellBorderbottom(16, 1, 0);
		rt.body.setCellBorderbottom(17, 1, 0);
		rt.body.setCellBorderbottom(18, 1, 0);
		
		rt.body.setCellBorderRight(12, 2, 0);
		rt.body.setCellBorderRight(13, 4, 0);
		rt.body.setCellBorderRight(13, 7, 0);
		rt.body.setCellBorderRight(14, 2, 0);
		rt.body.setCellBorderRight(15, 4, 0);
		rt.body.setCellBorderRight(15, 7, 0);
		rt.body.setCellBorderRight(16, 2, 0);
		rt.body.setCellBorderRight(17, 4, 0);
		rt.body.setCellBorderRight(17, 7, 0);
		rt.body.setCellBorderRight(18, 2, 0);
		rt.body.setCellBorderRight(19, 4, 0);
		rt.body.setCellBorderRight(19, 7, 0);
		
		rt.body.setCellBorderRight(20, 2, 0);
		rt.body.setCellBorderRight(20, 3, 0);
		rt.body.setCellBorderRight(20, 4, 0);
		rt.body.setCellBorderRight(20, 5, 0);
		rt.body.setCellBorderRight(20, 6, 0);
		rt.body.setCellBorderRight(20, 7, 0);
		rt.body.setCellBorderRight(20, 8, 0);
		rt.body.setCellBorderRight(20, 9, 0);
//		
		rt.body.setCellBorderbottom(22, 1, 0);
		rt.body.setCellBorderRight(22, 2, 0);
		rt.body.setCellBorderbottom(22, 2, 0);
		rt.body.setCellBorderRight(22, 3, 0);
		rt.body.setCellBorderbottom(22, 3, 0);
		rt.body.setCellBorderRight(22, 4, 0);
		rt.body.setCellBorderbottom(22, 4, 0);
		rt.body.setCellBorderRight(22, 5, 0);
		rt.body.setCellBorderbottom(22, 5, 0);
		rt.body.setCellBorderRight(22, 6, 0);
		rt.body.setCellBorderbottom(22, 6, 0);
		rt.body.setCellBorderRight(22, 7, 0);
		rt.body.setCellBorderbottom(22, 7, 0);
		rt.body.setCellBorderRight(22, 8, 0);
		rt.body.setCellBorderbottom(22, 8, 0);
		rt.body.setCellBorderRight(22, 9, 0);
		rt.body.setCellBorderbottom(22, 9, 0);
		rt.body.setCellBorderbottom(22, 10, 0);
		
		rt.body.setCellBorderRight(23, 2, 0);
		rt.body.setCellBorderRight(23, 2, 0);
		rt.body.setCellBorderRight(23,3, 0);
		rt.body.setCellBorderRight(23, 4, 0);
		rt.body.setCellBorderRight(23, 5, 0);
		rt.body.setCellBorderRight(23, 6, 0);
		rt.body.setCellBorderRight(23, 7, 0);
		rt.body.setCellBorderRight(23, 8, 0);
		rt.body.setCellBorderRight(23, 9, 0);
		rt.body.setCellBorderRight(23, 10, 0);
		
//		设置特殊单元格对其方式
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setCellAlign(1, 3, Table.ALIGN_CENTER);
		rt.body.setCellAlign(3, 7, Table.ALIGN_CENTER);
		rt.body.setCellAlign(4, 2, Table.ALIGN_CENTER);
		rt.body.setCellAlign(11, 1, Table.ALIGN_LEFT);
		
		rt.body.setCellAlign(13, 4, Table.ALIGN_RIGHT);
		rt.body.setCellAlign(15, 4, Table.ALIGN_RIGHT);
		rt.body.setCellAlign(17, 4, Table.ALIGN_RIGHT);
		rt.body.setCellAlign(19, 4, Table.ALIGN_RIGHT);
		
		rt.body.setCellAlign(13, 7, Table.ALIGN_RIGHT);
		rt.body.setCellAlign(15, 7, Table.ALIGN_RIGHT);
		rt.body.setCellAlign(17, 7, Table.ALIGN_RIGHT);
		rt.body.setCellAlign(19, 7, Table.ALIGN_RIGHT);
		
	
//		设置特殊单元格字体
		rt.body.setCellFont(1, 3, "", 12, false);
		rt.body.setCellFont(2, 3, "", 12, false);
		rt.body.setCellFont(6, 2, "", 12, false);
			
//		页脚 
		rt.createFooter(4,ArrWidth);
		 
		rt.footer.setCellValue(1, 1, "说明: 1、承办部门必须填写全部表头，并将应评审计部门打■");
		rt.footer.setCellAlign(1, 1, Table.ALIGN_LEFT);
		rt.footer.mergeCell(1, 1, 1, 10);
		
		rt.footer.setCellValue(2, 1, "&nbsp&nbsp&nbsp&nbsp&nbsp 2、承办部门主管和承办人栏，必须签字，不得打印，评审栏不需承办部门再次填写");
		rt.footer.setCellAlign(2, 1, Table.ALIGN_LEFT);
		rt.footer.mergeCell(2, 1, 2, 10);
		
		rt.footer.setCellValue(3, 1, "&nbsp&nbsp&nbsp&nbsp&nbsp 3、评审部门名称由各部门填写，除监察审计部为最终评审外，其他部门顺序不限");
		rt.footer.setCellAlign(3, 1, Table.ALIGN_LEFT);
		rt.footer.mergeCell(3, 1, 3, 10);
			 
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	private String FileChk(String fileName){
		//判断网络资源是否存在
		if(isNetFileAvailable(MainGlobal.getHomeContext(getPage())+"/imgs/dsqm/"+fileName+".gif"+"")){
			return "<image src='"+MainGlobal.getHomeContext(getPage())+"/imgs/dsqm/"+fileName+".gif"+"' width=\"100\" height=\"40\" align=\"left\"/>";
		}else{
			return fileName;
		}
	}
	
//	检查网络资源是否存在
	public static boolean isNetFileAvailable(String netFileUrl) {
		InputStream netFileInputStream = null;
		try {
			URL url = new URL(netFileUrl);
			URLConnection urlConn = url.openConnection();
			netFileInputStream = urlConn.getInputStream();
		} catch (IOException e) {
			return false;
		}
		if (null != netFileInputStream) {
			return true;
		} else {
			return false;
		}
	}
	
//	开始构造下拉框
//	合同编号
	public IDropDownBean getHetbhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
		   ((Visit) getPage().getVisit()).setDropDownBean3((IDropDownBean) getHetbhModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setHetbhValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setHetbhModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getHetbhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getHetbhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getHetbhModels() {
		String beginsj=getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01";
 		Date lastday=DateUtil.getLastDayOfMonth(DateUtil.getDate(beginsj));
		String endsj=DateUtil.Formatdate("yyyy-MM-dd", lastday);
		String sql = "SELECT H.ID,H.HETBH FROM HETB H, LIUCGZB G \n"
			+ "WHERE H.LIUCGZID = G.LIUCGZID AND H.LIUCZTB_ID=1 AND H.QIANDRQ BETWEEN DATE'"+beginsj+"' AND DATE'"+endsj+"'\n"
			+ " GROUP BY H.ID, H.HETBH\n"
			+ " HAVING MAX(G.SHIJ) > DATE '2012-12-30'\n"
			+ "ORDER BY H.HETBH";
		((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql,"请选择合同编号"));
	}	
	
//	年份
	public boolean nianfchanged = false;
	
	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			int _nianf = DateUtil.getYear(new Date());
			for (int i = 0; i <getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					((Visit) getPage().getVisit()).setDropDownBean1((IDropDownBean) getNianfModel().getOption(i));
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setNianfValue(IDropDownBean Value) {
		if(getNianfValue()!=Value){
			nianfchanged=true;
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setNianfModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2012; i <= DateUtil.getYear(new Date())+1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit()).setProSelectionModel1(new IDropDownModel(listNianf));
	}	
	
//	月份
	public boolean yuefchanged = false;
	
	public IDropDownBean getYuefValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			int _yuef = DateUtil.getMonth(new Date());
			for (int i = 0; i <getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean) getYuefModel().getOption(i));
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setYuefValue(IDropDownBean Value) {
		if(getYuefValue()!=Value){
			yuefchanged=true;
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setYuefModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getYuefModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getYuefModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(listYuef));
	}	
	
//	下拉框构造完成

	public SortMode getSort() {
		return SortMode.USER;
	}
	
	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}

	
//	***************************报表初始设置***************************//
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

	
	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}
	 //	页面判定方法
    public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}

	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("合同编号:"));
		ComboBox hetbh = new ComboBox();
		hetbh.setTransform("HETBH");
		hetbh.setWidth(300);
		tb1.addField(hetbh);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"查询","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(tb);
		setToolbar(tb1);
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
}