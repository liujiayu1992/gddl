package com.zhiren.jt.het.hetdxl;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者：陈泽天
 * 时间：2010-01-20 17：02
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
/*
 * 作者：songy
 * 时间：2011-03-23 
 * 描述：修改下拉菜单的排序，要求按照名称进行排序
 */
/*
 * 作者：夏峥
 * 时间：2012-05-23 
 * 描述：将电厂树前置
 * 		只显示合同类别为电厂采购合同的内容
 * 		调整合同量字段的显示位置，并在合同量字段前增加合同总量字段
 * 		调整供应商下拉框及月份下拉框
 */
/*
 * 作者：夏峥
 * 时间：2012-10-15
 * 描述：调整界面显示BUG
 */
/*
 * 作者：赵胜男
 * 时间：2012-12-20
 * 描述：根据需求变更表列名称和表头名称,取消超欠供列，调整sql，
 */
/*
 * 作者：夏峥
 * 时间：2013-07-25
 * 描述：处理类型为1的合同不显示的问题
 */
public class Hetdxl extends BasePage {
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
		return getHetltj();
	}
	
//	合同量分厂分矿分矿分厂统计报表
	private String getHetltj() {
		JDBCcon con = new JDBCcon();
		StringBuffer buffer = new StringBuffer("");
		Visit visit = (Visit) getPage().getVisit();
		String gongysCondition="and fahb.gongysb_id ="+getGongysDropDownValue().getId()+"\n";
		String diancCondition=
			"and fahb.diancxxb_id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with fuid="+getTreeid()+"\n" + 
			" connect by fuid=prior id\n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+")" ; 
		String gongysConditionH="and HTSL.gongysb_id ="+getGongysDropDownValue().getId()+"\n";
		String diancConditionH=
			"and HTSL.diancxxb_id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with fuid="+getTreeid()+"\n" + 
			" connect by fuid=prior id\n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+")" ; 
		String MM1=getYuef1Value().getValue();
		if(getYuef1Value().getId()<10){
			MM1="0"+getYuef1Value().getValue();
		}
		String MM2=getYuef2Value().getValue();
		if(getYuef2Value().getId()<10){
			MM2="0"+getYuef2Value().getValue();
		}
		String YYYYMMCondi1=getNianfValue().getValue()+MM1;
		String YYYYMMCondi2=getNianfValue().getValue()+MM2;
		
		if(visit.getRenyjb()==1){
			diancConditionH="";
			diancCondition="";
		}
		if(getGongysDropDownValue().getId()==-1){
			gongysCondition="";
			gongysConditionH="";
		}
		//分厂分矿
		/*
		 * 修改收煤量的取值 由发货表的maoz-piz改为laimsl 并且使用round_new 和系统设置的小数位 对每个fah进行修约后再sum
		 * 修改时间：2008-12-04
		 * 修改人：王磊
		 */
		if(getLeixSelectValue().getId()==1){
			//"--分厂分矿\n" +
			buffer.append("select decode(grouping(dcmc),1,'总计',dcmc)dcmc1 ,\n" );
			buffer.append("decode(grouping(dcmc),0,decode(grouping(dagys),1,'小计',dagys),null)gysmc,sum(hetzl)hetzl,sum(hetl)hetl,sum(shoum)shoum,\n" );
			buffer.append("decode(sum(hetl),0,0,null,0,round(sum(shoum)/sum(hetl)*100))daohl\n" );
			buffer.append("from (\n" );
			buffer.append("    select diancxxb.mingc dcmc,diancxxb.xuh xuh,gongysb.mingc dagys,shoum,hetl,hetzl\n" );
			buffer.append("     from (\n" );
			buffer.append("     select nvl(sm.diancxxb_id,ht.diancxxb_id) as diancxxb_id, nvl(sm.gongysb_id,ht.gongysb_id) as gongysb_id, shoum,hetl,hetzl\n" );
			buffer.append("      from\n" );
			buffer.append("         (select fahb.diancxxb_id,fahb.gongysb_id,sum(round_new(laimsl,"+visit.getShuldec()+")) as shoum\n" );
			buffer.append("                from fahb\n" );
			buffer.append("                where to_char(fahb.daohrq,'YYYYMM')>='"+YYYYMMCondi1+"'\n" );
			buffer.append("                and to_char(fahb.daohrq,'YYYYMM')<='"+YYYYMMCondi2+"'\n" );
			buffer.append(gongysCondition);
			buffer.append(diancCondition);
			
			buffer.append("         group by fahb.diancxxb_id,fahb.gongysb_id) sm\n" );
			buffer.append("       right join\n" );
			
			buffer.append("(SELECT HTSL.DIANCXXB_ID,\n" +
							"       HTSL.GONGYSB_ID,\n" + 
							"       SUM(HTSL.HETL) HETL,\n" + 
							"       SUM(HTZL.HETZL) HETZL\n" + 
							"  FROM (SELECT H.ID, H.DIANCXXB_ID, H.GONGYSB_ID, SUM(SL.HETL) AS HETL\n" + 
							"          FROM HETSLB SL, HETB H\n" + 
							"         WHERE SL.HETB_ID = H.ID\n" + 
							"           AND TO_CHAR(SL.RIQ, 'YYYYMM') >= '"+YYYYMMCondi1+"'\n" + 
							"           AND TO_CHAR(SL.RIQ, 'YYYYMM') <='"+YYYYMMCondi2+"'\n" + 
							"           AND (H.LEIB = 0 or H.LEIB = 1) GROUP BY H.ID, H.DIANCXXB_ID, H.GONGYSB_ID) HTSL,\n" +
							" (SELECT HETB_ID, SUM(HETL) HETZL FROM HETSLB GROUP BY HETB_ID) HTZL\n"+
							"WHERE HTSL.ID = HTZL.HETB_ID\n" );
			buffer.append(gongysConditionH);
			buffer.append(diancConditionH);
			buffer.append("               GROUP BY HTSL.DIANCXXB_ID, HTSL.GONGYSB_ID) ht\n" );
			buffer.append("       on sm.diancxxb_id=ht.diancxxb_id and sm.gongysb_id=ht.gongysb_id\n" );
			buffer.append("     )smht,diancxxb , gongysb \n" );
			buffer.append("     where smht.diancxxb_id=diancxxb.id  ");
			buffer.append("		  and smht.gongysb_id=gongysb.id \n" );
			buffer.append(" )\n" );
			buffer.append(" group by ROLLUP((dcmc,xuh),dagys)\n" );
			buffer.append(" order by grouping(xuh)desc,xuh,grouping(dagys)desc,dagys");
			
		}else if(getLeixSelectValue().getId()==2){//分矿分厂
			buffer.append("select decode(grouping(dagys),1,'总计',dagys)dagys1 ,\n");
			buffer.append("decode(grouping(dagys),0,decode(grouping(dcmc),1,'小计',dcmc),'')dcmc1 ,\n"); 
			buffer.append("sum(hetzl)hetzl,sum(hetl)hetl,sum(shoum)shoum,\n"); 
			buffer.append("decode(sum(hetl),0,0,null,0,round(sum(shoum)/sum(hetl)*100))daohl\n"); 
			buffer.append("from (\n"); 
			buffer.append("    select diancxxb.mingc dcmc, gongysb.mingc dagys,shoum,hetl,hetzl\n"); 
			buffer.append("     from (\n"); 
			buffer.append("     select nvl(sm.diancxxb_id,ht.diancxxb_id) as diancxxb_id, nvl(sm.gongysb_id,ht.gongysb_id) as gongysb_id, shoum,hetl,hetzl\n"); 
			buffer.append("      from\n"); 
			buffer.append("         (select fahb.diancxxb_id,fahb.gongysb_id,sum(round_new(laimsl,"+visit.getShuldec()+")) as shoum\n"); 
			buffer.append("                from fahb\n"); 
			buffer.append("                where to_char(fahb.daohrq,'YYYYMM')>='"+YYYYMMCondi1+"'\n"); 
			buffer.append("                and to_char(fahb.daohrq,'YYYYMM')<='"+YYYYMMCondi2+"'\n"); 
			buffer.append(gongysCondition);
			buffer.append(diancCondition);
			buffer.append("         group by fahb.diancxxb_id,fahb.gongysb_id) sm\n"); 
			buffer.append("       right join\n"); 
			
			
			buffer.append("(SELECT HTSL.DIANCXXB_ID,\n" +
					"       HTSL.GONGYSB_ID,\n" + 
					"       SUM(HTSL.HETL) HETL,\n" + 
					"       SUM(HTZL.HETZL) HETZL\n" + 
					"  FROM (SELECT H.ID, H.DIANCXXB_ID, H.GONGYSB_ID, SUM(SL.HETL) AS HETL\n" + 
					"          FROM HETSLB SL, HETB H\n" + 
					"         WHERE SL.HETB_ID = H.ID\n" + 
					"           AND TO_CHAR(SL.RIQ, 'YYYYMM') >= '"+YYYYMMCondi1+"'\n" + 
					"           AND TO_CHAR(SL.RIQ, 'YYYYMM') <='"+YYYYMMCondi2+"'\n" + 
					"           AND (H.LEIB = 0 or H.LEIB = 1) GROUP BY H.ID, H.DIANCXXB_ID, H.GONGYSB_ID) HTSL,\n" +
					" (SELECT HETB_ID, SUM(HETL) HETZL FROM HETSLB GROUP BY HETB_ID) HTZL\n"+
					"WHERE HTSL.ID = HTZL.HETB_ID\n" );
			buffer.append(gongysConditionH);
			buffer.append(diancConditionH);
			buffer.append("               GROUP BY HTSL.DIANCXXB_ID, HTSL.GONGYSB_ID) ht\n" );
			
			
			buffer.append("       on sm.diancxxb_id=ht.diancxxb_id and sm.gongysb_id=ht.gongysb_id\n"); 
			buffer.append("     )smht,diancxxb ,gongysb\n"); 
			buffer.append("     where smht.diancxxb_id=diancxxb.id and smht.gongysb_id=gongysb.id\n"); 
			buffer.append(" )\n"); 
			buffer.append(" group by rollup(dagys,dcmc )\n"); 
			buffer.append(" order by grouping(dagys)desc,dagys,grouping(dcmc)desc,dcmc");

		}else if(getLeixSelectValue().getId()==3){//分厂

			buffer.append("select decode(grouping(dcmc),1,'总计',dcmc) dcmc1,\n");
			buffer.append(" sum(hetzl)hetzl,sum(hetl)hetl,sum(shoum)shoum,\n"); 
			buffer.append(" decode(sum(hetl),0,0,null,0,round(sum(shoum)/sum(hetl)*100))daohl\n"); 
			buffer.append(" from (\n"); 
			buffer.append("     select diancxxb.mingc dcmc,diancxxb.xuh xuh,shoum,hetl,hetzl\n"); 
			buffer.append("      from (\n"); 
			buffer.append("      select nvl(sm.diancxxb_id,ht.diancxxb_id) as diancxxb_id, shoum,hetl,hetzl\n"); 
			buffer.append("       from\n"); 
			buffer.append("          (select fahb.diancxxb_id,sum(round_new(laimsl,"+visit.getShuldec()+")) as shoum\n"); 
			buffer.append("                 from fahb\n"); 
			buffer.append("                 where to_char(fahb.daohrq,'YYYYMM')>='"+YYYYMMCondi1+"'\n"); 
			buffer.append("                 and to_char(fahb.daohrq,'YYYYMM')<='"+YYYYMMCondi2+"'\n");
//			buffer.append(gongysCondition);
			buffer.append(diancCondition);
			buffer.append(	"          group by fahb.diancxxb_id) sm\n"); 
			buffer.append("        right join\n"); 
			
			buffer.append("(SELECT HTSL.DIANCXXB_ID,\n" +
					"       SUM(HTSL.HETL) HETL,\n" + 
					"       SUM(HTZL.HETZL) HETZL\n" + 
					"  FROM (SELECT H.ID, H.DIANCXXB_ID, H.GONGYSB_ID, SUM(SL.HETL) AS HETL\n" + 
					"          FROM HETSLB SL, HETB H\n" + 
					"         WHERE SL.HETB_ID = H.ID\n" + 
					"           AND TO_CHAR(SL.RIQ, 'YYYYMM') >= '"+YYYYMMCondi1+"'\n" + 
					"           AND TO_CHAR(SL.RIQ, 'YYYYMM') <='"+YYYYMMCondi2+"'\n" + 
					"           AND (H.LEIB = 0 or H.LEIB = 1) GROUP BY H.ID, H.DIANCXXB_ID, H.GONGYSB_ID) HTSL,\n" +
					" (SELECT HETB_ID, SUM(HETL) HETZL FROM HETSLB GROUP BY HETB_ID) HTZL\n"+
					"WHERE HTSL.ID = HTZL.HETB_ID\n" );
			buffer.append(diancConditionH);
			buffer.append("               GROUP BY HTSL.DIANCXXB_ID) ht\n" );
			
			buffer.append("        on sm.diancxxb_id=ht.diancxxb_id\n"); 
			buffer.append("      )smht,diancxxb  \n"); 
			buffer.append("      where smht.diancxxb_id=diancxxb.id \n"); 
			buffer.append("  )\n"); 
			buffer.append("  group by ROLLUP((dcmc,xuh))\n"); 
			buffer.append("  ORDER BY GROUPING(xuh)DESC,xuh");

		}else{//分矿
			buffer.append("select decode(grouping(dagys),1,'总计',dagys)dagys1 ,\n");
			buffer.append(" sum(hetzl)hetzl,sum(hetl)hetl,sum(shoum)shoum,\n"); 
			buffer.append(" decode(sum(hetl),0,0,null,0,round(sum(shoum)/sum(hetl)*100))daohl\n"); 
			buffer.append(" from (\n"); 
			buffer.append("     select gongysb.mingc dagys, shoum,hetl,hetzl\n"); 
			buffer.append("      from (\n"); 
			buffer.append("      select  nvl(sm.gongysb_id,ht.gongysb_id) as gongysb_id, shoum,hetl,hetzl\n"); 
			buffer.append("       from\n"); 
			buffer.append("          (select fahb.gongysb_id,sum(round_new(laimsl,"+visit.getShuldec()+")) as shoum\n"); 
			buffer.append("                 from fahb\n"); 
			buffer.append("                 where to_char(fahb.daohrq,'YYYYMM')>='"+YYYYMMCondi1+"'\n"); 
			buffer.append("                 and to_char(fahb.daohrq,'YYYYMM')<='"+YYYYMMCondi2+"'\n");
			buffer.append(gongysCondition);
//			buffer.append(diancCondition);
			buffer.append("          group by fahb.gongysb_id) sm\n"); 
			buffer.append("        right join\n"); 
			
			buffer.append("(SELECT  HTSL.GONGYSB_ID,\n" + 
					"       SUM(HTSL.HETL) HETL,\n" + 
					"       SUM(HTZL.HETZL) HETZL\n" + 
					"  FROM (SELECT H.ID, H.DIANCXXB_ID, H.GONGYSB_ID, SUM(SL.HETL) AS HETL\n" + 
					"          FROM HETSLB SL, HETB H\n" + 
					"         WHERE SL.HETB_ID = H.ID\n" + 
					"           AND TO_CHAR(SL.RIQ, 'YYYYMM') >= '"+YYYYMMCondi1+"'\n" + 
					"           AND TO_CHAR(SL.RIQ, 'YYYYMM') <='"+YYYYMMCondi2+"'\n" + 
					"           AND (H.LEIB = 0 or H.LEIB = 1) GROUP BY H.ID, H.DIANCXXB_ID, H.GONGYSB_ID) HTSL,\n" +
					" (SELECT HETB_ID, SUM(HETL) HETZL FROM HETSLB GROUP BY HETB_ID) HTZL\n"+
					"WHERE HTSL.ID = HTZL.HETB_ID\n" );
			buffer.append(gongysConditionH);
			buffer.append("               GROUP BY HTSL.GONGYSB_ID) ht\n" );

			buffer.append("        on sm.gongysb_id=ht.gongysb_id\n"); 
			buffer.append("      )smht ,gongysb \n"); 
			buffer.append("      where  smht.gongysb_id=gongysb.id \n"); 
			buffer.append("  )\n"); 
			buffer.append("  group by rollup(dagys )\n"); 
			buffer.append("  order by grouping(dagys)desc,dagys");
		}
		ResultSet rs = con.getResultSet(buffer,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		if(getLeixSelectValue().getId()==1){
			ArrHeader = new String[1][6];
			ArrWidth = new int[] { 100, 150, 100,100,100,100};
			ArrHeader[0] = new String[] { "电厂单位", "供应商","年度合同量(吨)",MM1+"月至"+MM2+"月<br>合同量(吨)","到货量(吨)","兑现率(%)"};
			rt.setBody(new Table(rs, 1, 0, 2));
			//
			int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
			rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.setTitle("煤   炭   采   购   合   同   到   货   情   况"+"(分厂分矿)", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 2, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
			rt.setDefaultTitle(3, 2, getNianfValue().getValue()+"年"+MM1+"月至"+MM2+"月",Table.ALIGN_CENTER);
//			rt.body.setPageRows(rt.PAPER_ROWS);
			rt.body.mergeFixedCols();
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 6, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		}else if(getLeixSelectValue().getId()==2){//分矿分厂
			ArrHeader = new String[1][6];
			ArrWidth = new int[] { 150, 100, 100,100,100,100};
			ArrHeader[0] = new String[] { "供应商", "电厂单位","年度合同量(吨)",MM1+"月至"+MM2+"月<br>合同量(吨)","到货量(吨)","兑现率(%)"};
			rt.setBody(new Table(rs, 1, 0, 2));
			//
			int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
			rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.setTitle("煤   炭   采   购   合   同   到   货   情   况"+"(分矿分厂)", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 2, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
			rt.setDefaultTitle(3, 2, getNianfValue().getValue()+"年"+MM1+"月至"+MM2+"月",Table.ALIGN_CENTER);
//			rt.body.setPageRows(rt.PAPER_ROWS);
			rt.body.mergeFixedCols();
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 6, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		}else if(getLeixSelectValue().getId()==3){//分厂
			ArrHeader = new String[1][5];
			ArrWidth = new int[] { 100,100,100,100,100};
			ArrHeader[0] = new String[] {"电厂单位","年度合同量(吨)",MM1+"月至"+MM2+"月<br>合同量(吨)","到货量(吨)","兑现率(%)"};
			rt.setBody(new Table(rs, 1, 0, 1));
			//
			int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
			rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.setTitle("煤   炭   采   购   合   同   到   货   情   况"+"(分厂)", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 2, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
			rt.setDefaultTitle(3, 2, getNianfValue().getValue()+"年"+MM1+"月至"+MM2+"月",Table.ALIGN_CENTER);
//			rt.body.setPageRows(rt.PAPER_ROWS);
			rt.body.mergeFixedCols();
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 5, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		}else{//分矿
			ArrHeader = new String[1][5];
			ArrWidth = new int[] { 100,100,100,100,100};
			ArrHeader[0] = new String[] {"供应商","年度合同量(吨)",MM1+"月至"+MM2+"月<br>合同量(吨)","到货量(吨)","兑现率(%)"};
			rt.setBody(new Table(rs, 1, 0, 1));
			//
			int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
			rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.setTitle("煤   炭   采   购   合   同   到   货   情   况"+"(分矿)", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 2, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
			rt.setDefaultTitle(3, 2, getNianfValue().getValue()+"年"+MM1+"月至"+MM2+"月",Table.ALIGN_CENTER);
//			rt.body.setPageRows(rt.PAPER_ROWS);
			rt.body.mergeFixedCols();
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 5, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		}
		
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
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
	private void getToolbars(){
		getGongysDropDownModels();
		setGongysDropDownValue(getIDropDownBean(getGongysDropDownModel(),getGongysDropDownValue().getId()));
		
		Toolbar tb1 = new Toolbar("tbdiv");
//		需方
		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("NianfDropDown");
		cb.setWidth(60);
		tb1.addField(cb);
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("月份:"));
		ComboBox cb0 = new ComboBox();
		cb0.setTransform("Yuef1");
		cb0.setWidth(40);
		tb1.addField(cb0);
		tb1.addText(new ToolbarText("至"));
		ComboBox cb3 = new ComboBox();
		cb3.setTransform("Yuef2");
		cb3.setWidth(40);
		tb1.addField(cb3);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("统计类型:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("LeixSelect");
		cb1.setWidth(80);
		cb1.setId("tongjkj");
		cb1.setListeners("select:function(obj,rec,index){if(index==2){gongysid.disable();};if(index==3){}}");
		tb1.addField(cb1);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("供应商:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("GongysDropDown");
		cb2.setEditable(true);
		cb2.setIsMohcx(true);
		cb2.setId("gongysid");
		cb2.setWidth(150);
		//meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb2);
		
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			
//			统计类型
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
//			初始化年份
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
//			初始化月份
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean6(null);
			visit.setProSelectionModel6(null);
//			初始化供应商
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDefaultTree(null);
			setTreeid(null);
			
			//begin方法里进行初始化设置
			visit.setString1(null);
			String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
			if (pagewith != null) {	visit.setString1(pagewith);	}
		}
		getToolbars();
		blnIsBegin = true;
	}
    // 供应商
	
    public IDropDownBean getGongysDropDownValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getGongysDropDownModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }
    public void setGongysDropDownValue(IDropDownBean Value) {
	    	((Visit) getPage().getVisit()).setDropDownBean2(Value);
    }
    public void setGongysDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel getGongysDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            getGongysDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }
    public void getGongysDropDownModels() {
		String MM1=getYuef1Value().getValue();
		if(getYuef1Value().getId()<10){
			MM1="0"+getYuef1Value().getValue();
		}
		String MM2=getYuef2Value().getValue();
		if(getYuef2Value().getId()<10){
			MM2="0"+getYuef2Value().getValue();
		}
		String YYYYMMCondi1=getNianfValue().getValue()+"-"+MM1;
		String YYYYMMCondi2=getNianfValue().getValue()+"-"+MM2;
		
    	String sql="select distinct g.id,g.mingc\n" +
    		"from hetb,gongysb g\n" + 
    		"where hetb.gongysb_id=g.id and  hetb.diancxxb_id in(\n" + 
    		" select id from diancxxb\n"+
			" start with id="+getTreeid()+
			" connect by fuid=prior id\n"+
			" )\n and (hetb.leib=0 or hetb.LEIB = 1) and hetb.qiandrq between to_date('"+YYYYMMCondi1+"-01','yyyy-mm-dd') \n" +
			" and add_months(to_date('"+YYYYMMCondi2+"-01','yyyy-mm-dd'),1)-1  order by g.mingc";
    	
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"全部")) ;
        return ;
    }
    //年份
	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit()).setDropDownBean3(getIDropDownBean(getNianfModel(),DateUtil.getYear(new Date())));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}
	public void setNianfValue(IDropDownBean Value) {
		
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}
	public IPropertySelectionModel getNianfModel() {
		
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
    public void getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = DateUtil.getYear(new Date())-2; i <= DateUtil.getYear(new Date())+2; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		 ((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(listNianf)) ;
	}

	// 月份
	public IPropertySelectionModel getYuef1Model() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getYuef1Models();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public IDropDownBean getYuef1Value() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean)getYuef1Model().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setYuef1Value(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void getYuef1Models() {
		List listYuef1 = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef1.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(listYuef1)) ;
	}
	
//  月份
	public IDropDownBean getYuef2Value() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			for (int i = 0; i < getYuef2Model().getOptionCount(); i++) {
				Object obj = getYuef2Model().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj).getId()) {
					((Visit) getPage().getVisit()).setDropDownBean6((IDropDownBean)getYuef2Model().getOption(i));
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setYuef2Value(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean6(Value);
	}
	
	public IPropertySelectionModel getYuef2Model() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getYuef2Models();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}
	
	public void getYuef2Models() {
		List listYuef2 = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef2.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit()).setProSelectionModel6(new IDropDownModel(listYuef2)) ;
	}


    private IDropDownBean getIDropDownBean(IPropertySelectionModel model,long id) {
        int OprionCount;
        OprionCount = model.getOptionCount();
        for (int i = 0; i < OprionCount; i++) {
            if (((IDropDownBean) model.getOption(i)).getId() == id) {
                return (IDropDownBean) model.getOption(i);
            }
        }
        return null;
    }
    

    //类型
    public IDropDownBean getLeixSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getLeixSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean4();
    }
    public void setLeixSelectValue(IDropDownBean Value) {
	    	((Visit) getPage().getVisit()).setDropDownBean4(Value);
    }
    public void setLeixSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }

    public IPropertySelectionModel getLeixSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getLeixSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }
    public void getLeixSelectModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(1,"分厂分矿"));
        list.add(new IDropDownBean(2,"分矿分厂"));
        list.add(new IDropDownBean(3,"分厂"));
        list.add(new IDropDownBean(4,"分矿"));
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(list)) ;
        return ;
    }
    
    private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
    public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	电厂名称
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
	
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
		String sql = "select id,mingc from diancxxb   order by xuh";
		 setDiancmcModel(new IDropDownModel(sql)) ;
		 return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

		public void setDiancmcModel(IPropertySelectionModel _value) {
			((Visit)getPage().getVisit()).setProSelectionModel1(_value);
		}
	//
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}