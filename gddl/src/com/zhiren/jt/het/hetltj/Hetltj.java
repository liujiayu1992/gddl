package com.zhiren.jt.het.hetltj;
/* 
* 时间：2010-01-25
* 作者：刘雨
* 修改内容：合同量统计需要查看合同收货人查询的条件，具体到每个电厂（解决方法：增加收货人）
*/
/* 
* 时间：2009-08-16
* 作者： ll
* 修改内容：“合同统计“改为”合同煤量统计表
* 		   
*/ 
/*
 * 作者：陈泽天
 * 时间：2010-01-20 16：49
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
/*
 * 作者：夏峥
 * 时间：2012-05-23
 * 描述：供应商下拉框中内容为所选年份合同中的供应商合集
 * 		取消类型下拉框和更多条件提示框
 * 		显示计划口径下拉框
 * 		在报表合同编号字段后增加起始日期和截止日期字段
 * 		只显示合同类别为电厂采购合同的内容
 */

/*
* 作者：赵胜男
* 时间：2012-12-17
* 描述：根据需求调整取消合伙收货人列
*/
/*
* 作者：赵胜男
* 时间：2013-03-12
* 描述：调整页面bug
*/
/*
* 作者：王耀霆
* 时间：2014-02-20
* 描述：把签订年份改为启用年份来查询
*/
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

public class Hetltj extends BasePage {
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

	public int getZhuangt() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	public void setZhuangt(int _value) {
		((Visit) getPage().getVisit()).setInt1(_value);
	}

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getHetltj();
	}
	// 合同量分厂分矿分矿分厂统计报表 
	private String getHetltj() {
		JDBCcon con = new JDBCcon();
		StringBuffer buffer = new StringBuffer("");

//		计划口径条件	
		String jihkjWhere="";
		if(getJihxzValue().getId()==-1){
			jihkjWhere="";
		}else{
			jihkjWhere=" and hetb.jihkjb_id= "+getJihxzValue().getId()+"\n";
		}
		
		String gongysmc="";
		if(getLeixSelectValue().getId()==1){
			gongysmc="decode(grouping(gongfmc),1,decode(grouping (quymc),0,decode(grouping(diancmc),0,'小计')),gongfmc)gongfmc1,\n" ;
		}else {
			gongysmc=" decode(grouping (gongfmc),1,'总计',gongfmc)gongfmc1,\n";
		}
		String leib=" and hetb.leib in (0,1) \n";
		
		//分厂分矿
		if(getLeixSelectValue().getId()==1){
			buffer.append("select\n" ); 
			buffer.append("decode(grouping(diancmc),1,decode(grouping (quymc),0,quymc,1,'总计'),diancmc)diancmc1,\n" ); 
			buffer.append(gongysmc); 
			buffer.append("hetbh,qisrq,guoqrq,sum(h1+h2+h3+h4+h5+h6+h7+h8+h9+h10+h11+h12)hetl,sum(h1)h1,\n" ); 
			buffer.append("sum(h2)h2,sum(h3)h3,sum(h4)h4,sum(h5)h5,sum(h6)h6,sum(h7)h7,sum(h8)h8,sum(h9)h9,sum(h10)h10,sum(h11)h11,sum(h12)h12\n" ); 
			buffer.append("from(\n" ); 
			buffer.append(" select quygs.mingc quymc,diancxxb.mingc diancmc,diancxxb.xuh,\n" +
						  "  --nvl(quygs_gf.mingc,gongysb.mingc) gongfmc,nvl(quygs_gf.id,gongysb.id)quygfid,\n" ); 
			buffer.append("  gongysb.mingc gongfmc,hetb.hetbh,hetslb1.mingc,hetslb1.h1,h2,h3,h4,h5,h6,h7,h8,h9,h10,h11,h12,\n" ); 
			buffer.append("  to_char(hetb.qisrq,'yyyy-mm-dd')qisrq,to_char(hetb.guoqrq,'yyyy-mm-dd')guoqrq \n ");
			buffer.append("	  from hetb,\n" ); 
			buffer.append("  (select hetb_id,d.mingc,diancxxb_id,\n" ); 
			buffer.append("      sum(decode(to_char(riq,'MM'),'01',hetl,0)) as h1,\n" ); 
			buffer.append("      sum(decode(to_char(riq,'MM'),'02',hetl,0)) as h2,\n" ); 
			buffer.append("      sum(decode(to_char(riq,'MM'),'03',hetl,0)) as h3,\n" ); 
			buffer.append("      sum(decode(to_char(riq,'MM'),'04',hetl,0)) as h4,\n" ); 
			buffer.append("      sum(decode(to_char(riq,'MM'),'05',hetl,0)) as h5,\n" ); 
			buffer.append("      sum(decode(to_char(riq,'MM'),'06',hetl,0)) as h6,\n" ); 
			buffer.append("      sum(decode(to_char(riq,'MM'),'07',hetl,0)) as h7,\n" ); 
			buffer.append("      sum(decode(to_char(riq,'MM'),'08',hetl,0)) as h8,\n" ); 
			buffer.append("      sum(decode(to_char(riq,'MM'),'09',hetl,0)) as h9,\n" ); 
			buffer.append("      sum(decode(to_char(riq,'MM'),'10',hetl,0)) as h10,\n" ); 
			buffer.append("      sum(decode(to_char(riq,'MM'),'11',hetl,0)) as h11,\n" ); 
			buffer.append("      sum(decode(to_char(riq,'MM'),'12',hetl,0)) as h12\n" ); 
			buffer.append("      from hetslb,diancxxb d where hetslb.diancxxb_id=d.id\n" ); 
			buffer.append("   group by hetb_id,d.mingc,diancxxb_id)\n" ); 
			buffer.append("  hetslb1,diancxxb ,diancxxb quygs,gongysb --,gongysb quygs_gf\n" ); 
			buffer.append("  where (hetb.id=hetslb1.hetb_id or hetb.fuid=hetslb1.hetb_id) \n" +
						  "	 and hetb.diancxxb_id=diancxxb.id and\n " +
						  "diancxxb.fuid=quygs.id(+)\n" ); 
			buffer.append(jihkjWhere);
			if(getGongysDropDownValue().getId()!=-1){
				buffer.append("and gongysb.id ="+getGongysDropDownValue().getId()+"\n" ); 
			}
			buffer.append(leib); 
			buffer.append("  and hetb.gongysb_id=gongysb.id --and gongysb.fuid=quygs_gf.id(+) \n "); 
			buffer.append("  and to_char(hetb.qisrq,'YYYY')="+getNianfValue().getId()+"\n" ); 
			buffer.append("  and hetb.diancxxb_id  in (select id\n" ); 
			buffer.append("   from(\n" ); 
			buffer.append("   select id from diancxxb\n" ); 
			buffer.append("   start with fuid="+getTreeid()+"\n" ); 
			buffer.append("   connect by fuid=prior id\n" ); 
			buffer.append("   )\n" ); 
			buffer.append("   union\n" ); 
			buffer.append("   select id\n" ); 
			buffer.append("   from diancxxb\n" ); 
			buffer.append("   where id="+getTreeid()+")\n" ); 
			buffer.append("  )\n" ); 
			buffer.append("HAVING GROUPING(gongfmc)+GROUPING(guoqrq)=0 OR GROUPING(gongfmc)+GROUPING(guoqrq)=2\n" ); 
			buffer.append("group by rollup ((quymc,diancmc,xuh),gongfmc,mingc,hetbh,qisrq,guoqrq)\n" ); 
			buffer.append("ORDER BY GROUPING(quymc), xuh,grouping(gongfmc),gongfmc1,qisrq"); 

		}else if(getLeixSelectValue().getId()==2){//分矿分厂
			buffer.append(	"select " +gongysmc);  
			buffer.append(	" decode(grouping (gongfmc),0,decode(grouping(quymc),0,decode(grouping(diancmc),1,quymc,diancmc),'小计'))diancmc1,\n" ); 
			buffer.append(	"mingc,hetbh,qisrq,guoqrq,sum(h1+h2+h3+h4+h5+h6+h7+h8+h9+h10+h11+h12)hetl,\n" ); 
			buffer.append(	"sum(h1)h1,sum(h2)h2,sum(h3)h3,sum(h4)h4,sum(h5)h5,sum(h6)h6,sum(h7)h7,sum(h8)h8,sum(h9)h9,sum(h10)h10,sum(h11)h11,sum(h12)h12\n" ); 
			buffer.append(	"from(\n" ); 
			buffer.append(	"    select quygs.mingc quymc,diancxxb.mingc diancmc,diancxxb.xuh,\n" ); 
			buffer.append(	"    hetb.hetbh,hetslb1.mingc,hetslb1.h1,h2,h3,h4,h5,h6,h7,h8,h9,h10,h11,h12,\n" );
			buffer.append(	"    gongysb.mingc gongfmc,to_char(hetb.qisrq,'yyyy-mm-dd')qisrq,to_char(hetb.guoqrq,'yyyy-mm-dd')guoqrq\n" );
			buffer.append(	"    from hetb,(select hetb_id, d.mingc,diancxxb_id,\n" ); 
			buffer.append(	"        sum(decode(to_char(riq,'MM'),'01',hetl,0)) as h1,\n" ); 
			buffer.append(	"        sum(decode(to_char(riq,'MM'),'02',hetl,0)) as h2,\n" ); 
			buffer.append(	"        sum(decode(to_char(riq,'MM'),'03',hetl,0)) as h3,\n" ); 
			buffer.append("        sum(decode(to_char(riq,'MM'),'04',hetl,0)) as h4,\n" ); 
			buffer.append("        sum(decode(to_char(riq,'MM'),'05',hetl,0)) as h5,\n" ); 
			buffer.append("        sum(decode(to_char(riq,'MM'),'06',hetl,0)) as h6,\n" ); 
			buffer.append("        sum(decode(to_char(riq,'MM'),'07',hetl,0)) as h7,\n" ); 
			buffer.append("        sum(decode(to_char(riq,'MM'),'08',hetl,0)) as h8,\n" ); 
			buffer.append("        sum(decode(to_char(riq,'MM'),'09',hetl,0)) as h9,\n" ); 
			buffer.append(	"        sum(decode(to_char(riq,'MM'),'10',hetl,0)) as h10,\n" ); 
			buffer.append("        sum(decode(to_char(riq,'MM'),'11',hetl,0)) as h11,\n" ); 
			buffer.append(	"        sum(decode(to_char(riq,'MM'),'12',hetl,0)) as h12\n" ); 
			buffer.append("        from hetslb,diancxxb d where hetslb.diancxxb_id=d.id\n" ); 
			buffer.append("     group by hetb_id,d.mingc,diancxxb_id)\n" ); 
			buffer.append("    hetslb1,diancxxb ,diancxxb quygs,gongysb \n" ); 
			buffer.append(	"    where (hetb.id=hetslb1.hetb_id or hetb.fuid=hetslb1.hetb_id) and hetb.diancxxb_id=diancxxb.id and diancxxb.fuid=quygs.id(+)\n" ); 
			buffer.append(jihkjWhere);
			if(getGongysDropDownValue().getId()!=-1){
				buffer.append("and gongysb.id ="+getGongysDropDownValue().getId()+"\n" ); 
			}
			buffer.append(leib); 
			buffer.append("    and hetb.gongysb_id=gongysb.id  \n" ); 
			buffer.append("    and to_char(hetb.qisrq,'YYYY')="+getNianfValue().getId()+"\n" ); 
			buffer.append("    and hetb.diancxxb_id  in (select id\n" ); 
			buffer.append("     from(\n" ); 
			buffer.append("     select id from diancxxb\n" ); 
			buffer.append("     start with fuid="+getTreeid()+"\n" ); 
			buffer.append("     connect by fuid=prior id\n" ); 
			buffer.append("     )\n" ); 
			buffer.append("     union\n" ); 
			buffer.append("     select id\n" ); 
			buffer.append("     from diancxxb\n" ); 
			buffer.append("     where id="+getTreeid()+")\n" ); 
			buffer.append("  )\n" ); 
			buffer.append("HAVING GROUPING(quymc)+GROUPING(guoqrq)=0 OR GROUPING(quymc)+GROUPING(guoqrq)=2\n" ); 			
			buffer.append("group by  rollup (gongfmc,(quymc,diancmc,xuh),mingc,hetbh,qisrq,guoqrq)\n" ); 
			buffer.append("ORDER BY GROUPING(gongfmc),gongfmc, xuh,qisrq"); 

		}else if(getLeixSelectValue().getId()==3){//分厂
			buffer.append("select decode(grouping(quymc),1,'总计',diancmc)diancmc1,decode(grouping(mingc)+grouping(quymc),1,'小计',mingc)shouhr,\n");
			buffer.append("hetbh,qisrq,guoqrq,sum(h1+h2+h3+h4+h5+h6+h7+h8+h9+h10+h11+h12)hetl,\n");
			buffer.append("sum(h1)h1,sum(h2)h2,sum(h3)h3,sum(h4)h4,sum(h5)h5,sum(h6)h6,sum(h7)h7,sum(h8)h8,sum(h9)h9,sum(h10)h10,sum(h11)h11,\n");
			buffer.append("sum(h12)h12\n");
			buffer.append("from(\n");
			buffer.append("select quygs.mingc quymc,diancxxb.mingc diancmc,diancxxb.xuh,\n");
			buffer.append("hetb.hetbh,hetslb1.mingc,hetslb1.h1,h2,h3,h4,h5,h6,h7,h8,h9,h10,h11,h12,\n");
			buffer.append("to_char(hetb.qisrq,'yyyy-mm-dd')qisrq,to_char(hetb.guoqrq,'yyyy-mm-dd')guoqrq\n");
			buffer.append("from hetb,(select hetb_id,d.mingc,diancxxb_id,\n");
			buffer.append("    sum(decode(to_char(riq,'MM'),'01',hetl,0)) as h1,\n");
			buffer.append("    sum(decode(to_char(riq,'MM'),'02',hetl,0)) as h2,\n");
			buffer.append("    sum(decode(to_char(riq,'MM'),'03',hetl,0)) as h3,\n");
			buffer.append("    sum(decode(to_char(riq,'MM'),'04',hetl,0)) as h4,\n");
			buffer.append("    sum(decode(to_char(riq,'MM'),'05',hetl,0)) as h5,\n");
			buffer.append("    sum(decode(to_char(riq,'MM'),'06',hetl,0)) as h6,\n");
			buffer.append("    sum(decode(to_char(riq,'MM'),'07',hetl,0)) as h7,\n");
			buffer.append("    sum(decode(to_char(riq,'MM'),'08',hetl,0)) as h8,\n");
			buffer.append("    sum(decode(to_char(riq,'MM'),'09',hetl,0)) as h9,\n");
			buffer.append("    sum(decode(to_char(riq,'MM'),'10',hetl,0)) as h10,\n");
			buffer.append("    sum(decode(to_char(riq,'MM'),'11',hetl,0)) as h11,\n");
			buffer.append("    sum(decode(to_char(riq,'MM'),'12',hetl,0)) as h12\n");
			buffer.append("    from hetslb,diancxxb d where hetslb.diancxxb_id=d.id\n");
			buffer.append(" group by hetb_id,d.mingc,diancxxb_id)\n");
			buffer.append("hetslb1,diancxxb ,diancxxb quygs\n");
			buffer.append("where (hetb.id=hetslb1.hetb_id or hetb.fuid=hetslb1.hetb_id) and hetb.diancxxb_id=diancxxb.id and diancxxb.fuid=quygs.id(+)  \n");
			buffer.append(leib); 
			buffer.append(jihkjWhere);
			buffer.append("and to_char(hetb.qisrq,'YYYY')="+getNianfValue().getId()+"\n" ); 
			buffer.append("and hetb.diancxxb_id  in (select id\n" ); 
			buffer.append(" from(\n" ); 
			buffer.append(" select id from diancxxb\n" ); 
			buffer.append(" start with fuid="+getTreeid()+"\n" ); 
			buffer.append(" connect by fuid=prior id\n" ); 
			buffer.append(" )\n" ); 
			buffer.append(" union\n" ); 
			buffer.append(" select id\n" ); 
			buffer.append(" from diancxxb\n" ); 
			buffer.append(" where id="+getTreeid()+")\n" ); 
			buffer.append(")\n");
			
			buffer.append("HAVING NOT GROUPING(mingc)+GROUPING(guoqrq)=1\n" );
			buffer.append("group by rollup((quymc,diancmc,xuh),mingc,hetbh,qisrq,guoqrq)\n");
			buffer.append("ORDER BY GROUPING(quymc),GROUPING(xuh),xuh,grouping(mingc),mingc,qisrq");
		}else{//分矿
			buffer.append("SELECT decode(grouping (gongfmc),1,'总计',gongfmc)gongfmc1,\n");
			buffer.append("DECODE(GROUPING(gongfmc)+GROUPING(mingc),1,'小计',mingc)shouhr,\n");
			buffer.append("hetbh,qisrq,guoqrq,sum(h1+h2+h3+h4+h5+h6+h7+h8+h9+h10+h11+h12)hetl,\n" );
			buffer.append("sum(h1)h1,sum(h2)h2,sum(h3)h3,sum(h4)h4,sum(h5)h5,sum(h6)h6,sum(h7)h7,sum(h8)h8,sum(h9)h9,sum(h10)h10,sum(h11)h11,sum(h12)h12\n"); 
			buffer.append("from(\n");
			buffer.append("select quygs.mingc quymc,diancxxb.mingc diancmc,diancxxb.xuh,\n" );
			buffer.append("hetb.hetbh,hetslb1.mingc,hetslb1.h1,h2,h3,h4,h5,h6,h7,h8,h9,h10,h11,h12,\n" );
			buffer.append("gys.mingc gongfmc,to_char(hetb.qisrq,'yyyy-mm-dd')qisrq,to_char(hetb.guoqrq,'yyyy-mm-dd')guoqrq\n");
			buffer.append("from hetb,(select hetb_id,d.mingc,diancxxb_id,\n" );
			buffer.append("    sum(decode(to_char(riq,'MM'),'01',hetl,0)) as h1,\n" );
			buffer.append("    sum(decode(to_char(riq,'MM'),'02',hetl,0)) as h2,\n" );
			buffer.append("    sum(decode(to_char(riq,'MM'),'03',hetl,0)) as h3,\n" );
			buffer.append("    sum(decode(to_char(riq,'MM'),'04',hetl,0)) as h4,\n" );
			buffer.append("    sum(decode(to_char(riq,'MM'),'05',hetl,0)) as h5,\n"); 
			buffer.append("    sum(decode(to_char(riq,'MM'),'06',hetl,0)) as h6,\n");
			buffer.append("    sum(decode(to_char(riq,'MM'),'07',hetl,0)) as h7,\n");
			buffer.append("    sum(decode(to_char(riq,'MM'),'08',hetl,0)) as h8,\n" );
			buffer.append("    sum(decode(to_char(riq,'MM'),'09',hetl,0)) as h9,\n" );
			buffer.append("    sum(decode(to_char(riq,'MM'),'10',hetl,0)) as h10,\n");
			buffer.append("    sum(decode(to_char(riq,'MM'),'11',hetl,0)) as h11,\n");
			buffer.append("    sum(decode(to_char(riq,'MM'),'12',hetl,0)) as h12\n" );
			buffer.append("    from hetslb,diancxxb d where hetslb.diancxxb_id=d.id\n");
			buffer.append(" group by hetb_id,d.mingc,diancxxb_id)\n");
			buffer.append("hetslb1,diancxxb ,diancxxb quygs,gongysb gys\n" );
			buffer.append("where (hetb.id=hetslb1.hetb_id or hetb.fuid=hetslb1.hetb_id)\n"); 
			buffer.append("and hetb.diancxxb_id=diancxxb.id and diancxxb.fuid=quygs.id(+)\n"); 
			buffer.append("AND hetb.gongysb_id=gys.id\n");
			buffer.append(leib); 
			buffer.append(jihkjWhere);
			if(getGongysDropDownValue().getId()!=-1){
				buffer.append("and gys.id ="+getGongysDropDownValue().getId()+"\n" ); 
			}
			buffer.append("and to_char(hetb.qisrq,'YYYY')="+getNianfValue().getId()+"\n");
			buffer.append("and hetb.diancxxb_id  in (select id\n");
			buffer.append(" from(\n");
			buffer.append(" select id from diancxxb\n");
			buffer.append(" start with fuid="+getTreeid()+"\n" );
			buffer.append(" connect by fuid=prior id\n");
			buffer.append(" )\n" );
			buffer.append(" union\n");
			buffer.append(" select id\n");
			buffer.append(" from diancxxb\n");
			buffer.append(" where id="+getTreeid()+")\n");
			buffer.append(")\n" ); 
			buffer.append("HAVING GROUPING(mingc)+GROUPING(guoqrq)=0 OR GROUPING(mingc)+GROUPING(guoqrq)=2 \n"); 
			buffer.append("group by rollup(gongfmc,mingc,hetbh,qisrq,guoqrq)\n");
			buffer.append("ORDER BY GROUPING(gongfmc),gongfmc,GROUPING(mingc),mingc,grouping(qisrq),qisrq");


		}
		ResultSet rs = con.getResultSet(buffer,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		Visit visit=((Visit) getPage().getVisit());
		String zhibdw=visit.getDiancqc();
		int aw=rt.paperStyle(visit.getDiancxxb_id(),visit.getString1());//取得报表纸张类型
		if(getLeixSelectValue().getId()==1){
			ArrHeader = new String[1][18];
			ArrWidth = new int[] { 90,120,220,80,80,60,40,40,40,40,40,40,40,40,40,40,40,40};
			ArrHeader[0] = new String[] { "电厂单位", "供应商", "合同编号","起始日期","截止日期", "合计","一月","二月","三月","四月","五月","六月",
					"七月","八月","九月","十月","十一月","十二月"};
			rt.setBody(new Table(rs, 1, 0, 6));

			rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.body.setColAlign(4, Table.ALIGN_LEFT);
			rt.setTitle("煤   炭   采   购   合   同   量   统   计"+"(分厂分矿)", ArrWidth);
			rt.title.setRowHeight(2, 38);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 18);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

			rt.setDefaultTitle(1, 5, "制表单位:" +zhibdw,Table.ALIGN_LEFT);
			rt.setDefaultTitle(8, 8, "单位:(吨)" ,Table.ALIGN_RIGHT);
			rt.setDefaultTitle(6, 2, "年份:"+getNianfValue().getId(),Table.ALIGN_RIGHT);
			rt.body.setPageRows(20);
			rt.body.mergeFixedCols();
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 15, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
			ReportFormat(rt, 18);
		}else if(getLeixSelectValue().getId()==2){//分矿分厂
			ArrHeader = new String[1][19];
			ArrWidth = new int[] { 90, 110, 130,175,80,80,60,40,40,40,40,40,40,40,40,40,40,40,40};
			ArrHeader[0] = new String[] { "供应商", "电厂单位", "合同收货人","合同编号","起始日期","截止日期","合计","一月","二月","三月","四月","五月","六月",
					"七月","八月","九月","十月","十一月","十二月"};
			rt.setBody(new Table(rs, 1, 0, 6));

			rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.body.setColAlign(4, Table.ALIGN_LEFT);
			rt.setTitle("煤   炭   采   购   合   同   量   统   计"+"(分矿分厂)", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 5, "制表单位:" +zhibdw,Table.ALIGN_LEFT);
			rt.setDefaultTitle(8, 8, "单位:(吨)" ,Table.ALIGN_RIGHT);
			rt.setDefaultTitle(6, 2, "年份:"+getNianfValue().getId() ,Table.ALIGN_RIGHT);
			rt.body.setPageRows(18);
			rt.body.mergeFixedCols();
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 15, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
			ReportFormat(rt, 19);
		}else if(getLeixSelectValue().getId()==3){//分厂
			ArrHeader = new String[1][18];
			ArrWidth = new int[] { 70,130,175,80,80,60,40,40,40,40,40,40,40,40,40,40,40,40};
			ArrHeader[0] = new String[] { "电厂单位","合同收货人","合同编号","起始日期","截止日期", "合计","一月","二月","三月","四月","五月","六月",
					"七月","八月","九月","十月","十一月","十二月"};
			rt.setBody(new Table(rs, 1, 0, 5));

			rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.body.setColAlign(3, Table.ALIGN_LEFT);
			rt.setTitle("煤   炭   采   购   合   同   量   统   计"+"(分厂)", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 5, "制表单位:" +zhibdw,Table.ALIGN_LEFT);
			rt.setDefaultTitle(8, 7, "单位:(吨)" ,Table.ALIGN_RIGHT);
			rt.setDefaultTitle(6, 2, "年份:"+getNianfValue().getId() ,Table.ALIGN_RIGHT);
			rt.body.setPageRows(20);
			rt.body.mergeFixedCols();
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 14, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
			ReportFormat(rt, 18);
		}else{//分矿
			ArrHeader = new String[1][18];
			ArrWidth = new int[] { 90,110,175,80,80,60,40,40,40,40,40,40,40,40,40,40,40,40};
			ArrHeader[0] = new String[] { "供应商","合同收货人", "合同编号","起始日期","截止日期", "合计","一月","二月","三月","四月","五月","六月",
					"七月","八月","九月","十月","十一月","十二月"};
			rt.setBody(new Table(rs, 1, 0, 5));

			rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.body.setColAlign(3, Table.ALIGN_LEFT);
			rt.setTitle("煤   炭   采   购   合   同   量   统   计"+"(分矿)", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 5, "制表单位:" +zhibdw,Table.ALIGN_LEFT);
			rt.setDefaultTitle(8, 7, "单位:(吨)" ,Table.ALIGN_RIGHT);
			rt.setDefaultTitle(6, 2, "年份:"+getNianfValue().getId() ,Table.ALIGN_RIGHT);
			rt.body.setPageRows(18);
			rt.body.mergeFixedCols();
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 14, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
			ReportFormat(rt, 18);
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
	
	private void ReportFormat(Report rt, int colnum){
		int rows= rt.body.getRows();
		 for (int i=2;i<=rows;i++){
			 String mingc=rt.body.getCellValue(i, 2);
			 String mingc2=rt.body.getCellValue(i, 1);
			 if (mingc.equals("小计")){
				 for (int j=2;j<=colnum;j++){
					 rt.body.getCell(i, j).backColor=rt.body.getCell(i, j).getBg_Color();
				 }
			 }
			 if (mingc2.equals("总计")){
				 for (int j=1;j<=colnum;j++){
					 rt.body.getCell(i, j).backColor=rt.body.getCell(i, j).getBg_Color();
				 }
			 }
		 } 
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
		
//		单位
		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"forms[0]",null,getTreeid());
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
		
		tb1.addText(new ToolbarText("启用年份:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("NianfDropDown");
		cb.setWidth(60);
		cb.setListeners("select:function(own,newValue,oldValue){document.Form0.submit();}");
		tb1.addField(cb);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("统计类型:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("LeixSelect");
		cb1.setWidth(80);
		cb1.setId("tongjkj");
		cb1.setListeners("select:function(obj,rec,index){if(index==2){gongysid.disable();};if(index==3){} document.Form0.submit(); document.Form0.submit();}");
		tb1.addField(cb1);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("供应商:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("GongysDropDown");
		cb2.setEditable(true);
		cb2.setId("gongysid");
		cb2.setWidth(150);
		//meik.setWidth(60);
		cb2.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb2);
		
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("计划口径:"));
		ComboBox cblex = new ComboBox();
		cblex.setTransform("jihkjSelect");
		cblex.setEditable(true);
		cblex.setWidth(80);
		tb1.addField(cblex);
		
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
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
//计划口径
			visit.setDropDownBean7(null);
			visit.setProSelectionModel7(null);

			visit.setDefaultTree(null);
			setTreeid(null);
			
			//begin方法里进行初始化设置
			visit.setString1(null);
				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {
					visit.setString1(pagewith);
				}
		}
		getToolbars();
		blnIsBegin = true;

	}
	//计划口径
	public IDropDownBean getJihxzValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean7((IDropDownBean) getIJihxzModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean7();
	}

	public void setJihxzValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean7(Value);
	}

	public void setIJihxzModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel7(value);
	}

	public IPropertySelectionModel getIJihxzModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {
			getIJihxzModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}

	public void getIJihxzModels() {
		String sql = "select id,mingc from jihkjb order by xuh";
		((Visit) getPage().getVisit())
				.setProSelectionModel7(new IDropDownModel(sql, "全部"));
		return;
	}
    // 供应商
    public IDropDownBean getGongysDropDownValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getGongysDropDownModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }
    public void setGongysDropDownValue(IDropDownBean Value) {
    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean2()!=null){
	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean2().getId()){
	    		((Visit) getPage().getVisit()).setboolean3(true);
	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean2(Value);
    	}
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
    	String sql=
    		"select distinct g.id,g.mingc\n" +
    		"from hetb,gongysb g\n" + 
    		"where hetb.gongysb_id=g.id and  hetb.diancxxb_id in(\n" + 
    		" select id from diancxxb\n"+
			" start with id="+getTreeid()+
			" connect by fuid=prior id\n"+
			" )\n  and to_char(hetb.qiandrq,'yyyy')='"+this.getNianfValue().getValue()+"' order by g.mingc";
    	
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
		if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean3()!=null){
			if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean3().getId()){
	    		((Visit) getPage().getVisit()).setboolean3(true);
	    	}
			 ((Visit) getPage().getVisit()).setDropDownBean3(Value);
		}
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
				((Visit) getPage().getVisit()).setboolean3(true);
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
//	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
	
//	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}
	
	public void setDiancmcValue(IDropDownBean Value) {
//		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
//			_DiancmcChange = false;
//		}else{
//			_DiancmcChange = true;
//		}
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