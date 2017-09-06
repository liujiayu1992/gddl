package com.zhiren.jt.zdt.shengcdy.hetwcqkb2;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Hetwcqk extends BasePage {
	String jihkjWhere_fh="";
	String jihkjWhere_j="";
	String gongysWhere="";
	String diancWhere="";
	String gongysmc="";
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

	private String getFilerCondtion(int jib){
		String strCondition="";
		
		if (-1!=(getJihkjDropDownValue().getId())){
			strCondition=strCondition+" and jihkjb.id=" +getJihkjDropDownValue().getId();
		}
		return strCondition;
	}
	
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
//		if(getLeixSelectValue().getValue().equals("棋盘表")){
//			return getQibb();
//		} else {
			return getZhiltj();
//		}
	}
	
	private String getQibb(){
		JDBCcon conn= new JDBCcon();
		ChessboardTable cd=new  ChessboardTable();
		String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));
		String riq1=FormatDate(DateUtil.getDate(getEndriqDate()));
//供应商gongysWhere+diancWhere +
		String sql1="select decode( grouping(vwgongys.dqmc),1,'小计',vwgongys.dqmc) as 供应商 from\n" +
		"(select distinct  ht.gongysb_id\n" + 
		"       from niandhtqkb ht,jihkjb j,vwdianc dc\n" + 
		"         where  ht.jihkjb_id=j.id\n" + 
		"         and ht.riq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n" + 
		"         and ht.riq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')"+jihkjWhere_j+"\n" + 
		"         and dc.id=ht.diancxxb_id\n" + 

		"  union select  distinct fh.gongysb_id\n" + 
		"        from fahb  fh,jihkjb j,vwdianc dc\n" + 
		"        where fh.jihkjb_id=j.id and fh.daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n" + 
		"       and fh.daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')"+jihkjWhere_j+"\n" + 
		"       and dc.id=fh.diancxxb_id\n" + 
		"        ) bt,vwgongys \n" + 
		"        where bt.gongysb_id=vwgongys.id\n" + gongysWhere+
		"group by rollup(vwgongys.dqmc)\n" + 
		"order by grouping(vwgongys.dqmc) desc,max(vwgongys.dqxh),vwgongys.dqmc";
//集团用户	
		String sql2="";
		String sql3="";
if(diancWhere.equals("")){
	sql2="select decode( grouping(bt.mingc),1,'小计',bt.mingc) as 电厂 from\n" +
	"(select distinct vwdianc.fgsmc mingc,vwdianc.fgsxh xuh\n" + 
	"       from niandhtqkb ht,jihkjb j,vwdianc\n" + 
	"         where  ht.jihkjb_id=j.id\n" + 
	"         and ht.riq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n" + 
	"         and ht.riq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')"+jihkjWhere_j+"\n" + 
	"         and ht.diancxxb_id=vwdianc.id\n" + 
	diancWhere+
	"  union select   distinct vwdianc.fgsmc mingc,vwdianc.fgsxh xuh\n" + 
	"        from fahb  fh,jihkjb j,vwdianc\n" + 
	"        where fh.jihkjb_id=j.id and fh.daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n" + 
	"       and fh.daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')"+jihkjWhere_j+"\n" + 
	"         and fh.diancxxb_id=vwdianc.id\n" + 
	diancWhere+
	"        ) bt\n" + 
	"group by rollup(mingc)\n" + 
	"order by grouping(bt.mingc) desc,max(bt.xuh),电厂";
//值
	sql3="select decode(grouping(gongysb_id),1,'小计',gongysb_id)供应商,decode(grouping(diancxxb_id),1,'小计',diancxxb_id)电厂,\n" +
	"round(decode(sum(hej),0,0,sum(laiml)/sum(hej))*100,0)到货率\n" + 
	"from(\n" + 
	"select vwgongys.dqmc gongysb_id, vwdianc.fgsmc diancxxb_id, sum(getHetl_Sjd(to_date('"+getBeginriqDate()+"', 'yyyy-mm-dd'),to_date('"+getEndriqDate()+"', 'yyyy-mm-dd'),ht.riq, ht.hej)*10000) hej, 0 laiml\n" + 
	"  from niandhtqkb ht,jihkjb j,vwdianc,vwgongys\n" + 
	" where ht.jihkjb_id =j.id\n" + 
	"   and ht.riq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n" + 
	"         and ht.riq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')"+jihkjWhere_j+"\n" + 
	"         and ht.diancxxb_id=vwdianc.id and ht.gongysb_id=vwgongys.id\n" + 
	diancWhere+gongysWhere+
	" group by vwgongys.dqmc, vwdianc.fgsmc\n" + 
	"union\n" + 
	"select vwgongys.dqmc gongysb_id, vwdianc.fgsmc diancxxb_id,0 hej,sum(laimsl)\n" + 
	"from fahb fh,jihkjb j,vwdianc,vwgongys\n" + 
	" where fh.jihkjb_id =j.id\n" + 
	"   and fh.daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n" + 
	"         and fh.daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')"+jihkjWhere_j+"\n" + 
	"         and fh.diancxxb_id=vwdianc.id and fh.gongysb_id=vwgongys.id\n" + 
	diancWhere+gongysWhere+
	"group by vwgongys.dqmc, vwdianc.fgsmc\n" + 
	")\n" + 
	"group by grouping sets((gongysb_id,diancxxb_id),diancxxb_id,gongysb_id,())";
}else{
	sql2="select decode( grouping(bt.mingc),1,'小计',bt.mingc) as 电厂 from\n" +
	"(select distinct vwdianc.mingc,vwdianc.xuh\n" + 
	"       from niandhtqkb ht,jihkjb j,vwdianc\n" + 
	"         where  ht.jihkjb_id=j.id\n" + 
	"         and ht.riq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n" + 
	"         and ht.riq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')"+jihkjWhere_j+"\n" + 
	"         and ht.diancxxb_id=vwdianc.id\n" + 
	diancWhere+
	"  union select   distinct vwdianc.mingc,vwdianc.xuh\n" + 
	"        from fahb  fh,jihkjb j,vwdianc\n" + 
	"        where fh.jihkjb_id=j.id and fh.daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n" + 
	"       and fh.daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')"+jihkjWhere_j+"\n" + 
	"         and fh.diancxxb_id=vwdianc.id\n" + 
	diancWhere+
	"        ) bt\n" + 
	"group by rollup(mingc)\n" + 
	"order by grouping(bt.mingc) desc,max(bt.xuh),电厂";
//值
	sql3="select decode(grouping(gongysb_id),1,'小计',gongysb_id)供应商,decode(grouping(diancxxb_id),1,'小计',diancxxb_id)电厂,\n" +
	"round(decode(sum(hej),0,0,sum(laiml)/sum(hej))*100,0)到货率\n" + 
	"from(\n" + 
	"select vwgongys.dqmc gongysb_id, vwdianc.mingc diancxxb_id, sum(getHetl_Sjd(to_date('"+getBeginriqDate()+"', 'yyyy-mm-dd'),to_date('"+getEndriqDate()+"', 'yyyy-mm-dd'),ht.riq, ht.hej)*10000) hej, 0 laiml\n" + 
	"  from niandhtqkb ht,jihkjb j,vwdianc,vwgongys\n" + 
	" where ht.jihkjb_id =j.id\n" + 
	"   and ht.riq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n" + 
	"         and ht.riq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')"+jihkjWhere_j+"\n" + 
	"         and ht.diancxxb_id=vwdianc.id and ht.gongysb_id=vwgongys.id\n" + 
	diancWhere+gongysWhere+
	" group by vwgongys.dqmc, vwdianc.mingc\n" + 
	"union\n" + 
	"select vwgongys.dqmc gongysb_id, vwdianc.mingc diancxxb_id,0 hej,sum(laimsl)\n" + 
	"from fahb fh,jihkjb j,vwdianc,vwgongys\n" + 
	" where fh.jihkjb_id =j.id\n" + 
	"   and fh.daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n" + 
	"         and fh.daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')"+jihkjWhere_j+"\n" + 
	"         and fh.diancxxb_id=vwdianc.id and fh.gongysb_id=vwgongys.id\n" + 
	diancWhere+gongysWhere+
	"group by vwgongys.dqmc, vwdianc.mingc\n" + 
	")\n" + 
	"group by grouping sets((gongysb_id,diancxxb_id),diancxxb_id,gongysb_id,())";
}
	

			
//			System.out.println(sbsql);
//			System.out.println(strRow);
//			System.out.println(strCol);
			cd.setRowNames("供应商");
			cd.setColNames("电厂");
			cd.setDataNames("到货率");
			cd.setDataOnRow(true);
			cd.setRowToCol(false);
			cd.setData(sql1,sql2,sql3);
			cd.DataTable.setColAlign(2, Table.ALIGN_CENTER);
			Report rt = new Report();
			int[] ArrWidth;
			ArrWidth =new int[] {120,60,80,80,80,80,80,80};
			ArrWidth =new int[cd.DataTable.getCols()];
			for (int i=1;i<ArrWidth.length;i++){
				ArrWidth[i]=80;
			}
			ArrWidth[0]=120;
			ArrWidth[1]=60;
			//
			rt.setBody(cd.DataTable);
			for(int i=3;i<rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_RIGHT);
			}
			if(rt.body.getRows()>0 && rt.body.getCols()>0){
				rt.body.setCellValue(1, 1, "单位");
//				rt.body.setCellValue(1, 2, "单位");
			}
			rt.body.setWidth(ArrWidth);
			rt.body.mergeFixedRowCol();
			rt.body.ShowZero=false;
			rt.setTitle("合同完成情况"+"(棋盘表)", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 3, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
			rt.setDefaultTitle(3, 3,riq+"-"+riq1,Table.ALIGN_RIGHT);
			rt.body.setPageRows(36);
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 3, "制表时间:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
			rt.setDefautlFooter(7, 1, "单位:车、吨",Table.ALIGN_RIGHT);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		conn.Close();
		return rt.getAllPagesHtml();
	}
	
	
	private StringBuffer strgrouping = new StringBuffer();
	private StringBuffer strwhere = new StringBuffer();
	private StringBuffer strgroupby = new StringBuffer();
	private StringBuffer strsbgroupby = new StringBuffer();//得到数据的分组 cube
	private StringBuffer strhaving = new StringBuffer();
	private StringBuffer strorderby = new StringBuffer();
	private String Rowtitle ="";
	
	public void getTiaoj(){
		strgrouping.setLength(0);
		strwhere.setLength(0);
		strgroupby.setLength(0);
		strsbgroupby.setLength(0);
		strhaving.setLength(0);
		strorderby.setLength(0);
		JDBCcon cn = new JDBCcon();
		int jib=this.getDiancTreeJib();
		
		//***********************************************************************************//
		/*if (getLeixSelectValue().getValue().equals("分厂")){
			Rowtitle="电厂";
			if(jib==1){//选集团时刷新出所有的电厂
				strgrouping.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'合计',1,dc.fgsmc,dc.mingc) as 电厂,\n");
				strwhere.append("");
				strgroupby.append(" group by rollup(dc.fgsmc,dc.mingc)\n");
				strsbgroupby.append(" group by cube(dc.fgsmc,dc.mingc,c.mingc)\n");
				strhaving.append(" having not (grouping(dc.fgsmc) -grouping(dc.mingc)) =1\n");
				strorderby.append(" order by grouping(dc.fgsmc) desc,dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc \n");
			}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
				String ranlgs = " select id from diancxxb where shangjgsid= "+this.getTreeid();
				
				try{
					ResultSet rl = cn.getResultSet(ranlgs);
					if(rl.next()){//燃料公司
						strgrouping.append("decode(grouping(dc.rlgsmc)+grouping(dc.fgsmc)+grouping(dc.mingc),3,'合计',2,dc.rlgsmc,1,dc.fgsmc,dc.mingc) as 电厂,\n");
						strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
						strgroupby.append(" group by rollup(dc.rlgsmc,dc.fgsmc,dc.mingc)\n");
						strsbgroupby.append(" group by cube(dc.rlgsmc,dc.fgsmc,dc.mingc,c.mingc)\n");
						strhaving.append(" having not grouping(dc.rlgsmc)=1\n");
						strorderby.append(" order by grouping(dc.rlgsmc) desc,dc.rlgsmc desc,grouping(dc.fgsmc) desc,dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc \n");
					}else{//分公司
						strgrouping.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'合计',1,dc.fgsmc,dc.mingc) as 电厂,\n");
						strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
						strgroupby.append(" group by rollup(dc.fgsmc,dc.mingc)\n");
						strsbgroupby.append(" group by cube(dc.fgsmc,dc.mingc,c.mingc)\n");
						strhaving.append("");
						strorderby.append(" order by grouping(dc.fgsmc) desc,dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc \n");
					}
					rl.close();
					
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					cn.Close();
				}
			}else if (jib==3){//选电厂只刷新出该电厂
				strgrouping.append("decode(grouping(dc.mingc),1,'合计',dc.mingc) as 电厂,\n");
				strwhere.append(" and dc.id=").append(this.getTreeid());
				strgroupby.append(" group by rollup(dc.mingc)\n");
				strsbgroupby.append(" group by cube(dc.mingc,c.mingc)\n");
				strhaving.append(" having not grouping(dc.mingc)=1\n");
				strorderby.append(" order by grouping(dc.mingc) desc,max(dc.xuh),dc.mingc \n");
			}
		}else if (getLeixSelectValue().getValue().equals("分矿")){

			if (getJihkjDropDownValue().getId()==2){//市场
				strgrouping.append("decode(grouping(g.dqmc),1,'合计','<a target=\"_blank\" href=\""+MainGlobal.getHomeContext(this)+"/app?service=page/Hetwcqkb2_1"+
				"&diancxxb_id="+getTreeid()+
				"&gongysbid='||max(g.dqmc)||'"+
				"&riq1="+getBeginriqDate()+
				"&riq2="+getEndriqDate()+
				"\">'||max(g.dqmc)||'</a>') as 矿别,\n");
			}else{
				strgrouping.append("decode(grouping(g.dqmc),1,'合计',max(g.dqmc)) as 矿别,\n");
			}
			
			Rowtitle="矿别";
			strgroupby.append(" group by rollup(g.dqmc)\n");
			strsbgroupby.append(" group by cube(g.dqmc,c.mingc)\n");
			strorderby.append(" order by grouping(g.dqmc) desc,g.dqmc desc\n");
			 
			if(jib==1){//选集团时刷新出所有的电厂
				strwhere.append("");
			}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
				strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
			}else if (jib==3){//选电厂只刷新出该电厂
				strwhere.append(" and dc.id=").append(this.getTreeid());
			}
			
		}else if (getLeixSelectValue().getValue().equals("分厂分矿")) {*/
			Rowtitle="电厂,矿别";
			if(jib==1){//选集团时刷新出所有的电厂
				strgrouping.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'合计',1,dc.fgsmc,dc.mingc) as 电厂,\n");
				strgrouping.append("decode(grouping(dc.mingc)+grouping(g.dqmc),2,'',1,'小计',g.dqmc) as 矿别,\n");
				strwhere.append("");
				strgroupby.append(" group by rollup(dc.fgsmc,dc.mingc,g.dqmc)\n");
				strsbgroupby.append(" group by cube(dc.fgsmc,dc.mingc,g.dqmc,c.mingc)\n");
				strhaving.append("");
				strorderby.append(" order by grouping(dc.fgsmc) desc,dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc ,grouping(g.dqmc) desc,g.dqmc\n");
			}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
				String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();
				
				try{
					ResultSet rl = cn.getResultSet(ranlgs);
					if(rl.next()){//燃料公司
						strgrouping.append("decode(grouping(dc.rlgsmc)+grouping(dc.fgsmc)+grouping(dc.mingc),3,'合计',2,dc.rlgsmc,1,dc.fgsmc,dc.mingc) as 电厂,\n");
						strgrouping.append("decode(grouping(dc.mingc)+grouping(g.dqmc),2,'',1,'小计',g.dqmc) as 矿别,\n");
						strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
						strgroupby.append(" group by rollup(dc.rlgsmc,dc.fgsmc,dc.mingc,g.dqmc)\n");
						strsbgroupby.append(" group by cube(dc.rlgsmc,dc.fgsmc,dc.mingc,g.dqmc,c.mingc)\n");
						strhaving.append(" having not grouping(dc.rlgsmc)=1\n");
						strorderby.append(" order by grouping(dc.rlgsmc) desc,dc.rlgsmc desc,grouping(dc.fgsmc) desc,dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc ,grouping(g.dqmc) desc,g.dqmc\n");
					}else{//分公司
						strgrouping.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'合计',1,dc.fgsmc,dc.mingc) as 电厂,\n");
						strgrouping.append("decode(grouping(dc.mingc)+grouping(g.dqmc),2,'',1,'小计',g.dqmc) as 矿别,\n");
						strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
						strgroupby.append(" group by rollup(dc.fgsmc,dc.mingc,g.dqmc)\n");
						strsbgroupby.append(" group by cube(dc.fgsmc,dc.mingc,g.dqmc,c.mingc)\n");
						strhaving.append("");
						strorderby.append(" order by grouping(dc.fgsmc) desc,dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc ,grouping(g.dqmc) desc,g.dqmc\n");
					}
					rl.close();
					
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					cn.Close();
				}
			}else if (jib==3){//选电厂只刷新出该电厂
				strgrouping.append("decode(grouping(dc.mingc),1,'合计',dc.mingc) as 电厂,\n");
				strgrouping.append("decode(grouping(dc.mingc)+grouping(g.dqmc),2,'',1,'小计',g.dqmc) as 矿别,\n");
				strwhere.append(" and dc.id=").append(this.getTreeid());
				strgroupby.append(" group by rollup(dc.mingc,g.dqmc)\n");
				strsbgroupby.append(" group by cube(dc.mingc,g.dqmc,c.mingc)\n");
				strhaving.append(" having not grouping(dc.mingc)=1\n");
				strorderby.append(" order by grouping(dc.mingc) desc,max(dc.xuh),dc.mingc ,grouping(g.dqmc) desc,g.dqmc\n");
			}
//			***********************************************************************************//			
		/*}else if(getLeixSelectValue().getValue().equals("分矿分厂")){
			Rowtitle="矿别,电厂";
			if(jib==1){//选集团时刷新出所有的电厂
				strgrouping.append("decode(grouping(g.dqmc),1,'合计',g.dqmc) as 矿别,\n");
				strgrouping.append("decode(grouping(g.dqmc)+grouping(dc.fgsmc)+grouping(dc.mingc),3,'',2,'',1,dc.fgsmc||'小计',dc.mingc) as 电厂,\n");
				strwhere.append("");
				strgroupby.append(" group by rollup(g.dqmc,dc.fgsmc,dc.mingc)\n");
				strsbgroupby.append(" group by cube(g.dqmc,dc.fgsmc,dc.mingc,c.mingc)\n");
				strhaving.append("");
				strorderby.append(" order by grouping(g.dqmc) desc,g.dqmc,grouping(dc.fgsmc) desc,dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc \n");
			}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
				String ranlgs = " select id from diancxxb where shangjgsid= "+this.getTreeid();
				try{
					ResultSet rl = cn.getResultSet(ranlgs);
					if(rl.next()){//燃料公司
						strgrouping.append("decode(grouping(g.dqmc),1,'合计',g.dqmc) as 矿别,\n");
						strgrouping.append("decode(grouping(g.dqmc)+grouping(dc.rlgsmc)+grouping(dc.mingc),3,'',2,'',1,dc.rlgsmc||'小计',dc.mingc) as 电厂,\n");
						strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
						strgroupby.append(" group by rollup(g.dqmc,dc.rlgsmc,dc.fgsmc,dc.mingc)\n");
						strsbgroupby.append(" group by cube(g.dqmc,dc.rlgsmc,dc.fgsmc,dc.mingc,c.mingc)\n");
						strhaving.append(" having not grouping(dc.rlgsmc)=1\n");
						strorderby.append(" order by grouping(g.dqmc) desc,g.dqmc,grouping(dc.rlgsmc) desc,dc.rlgsmc desc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc \n");
					}else{//分公司
						strgrouping.append("decode(grouping(g.dqmc),1,'合计',g.dqmc) as 矿别,\n");
						strgrouping.append("decode(grouping(g.dqmc)+grouping(dc.fgsmc)+grouping(dc.mingc),3,'',2,'',1,dc.fgsmc||'小计',dc.mingc) as 电厂,\n");
						
						strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
						strgroupby.append(" group by rollup(g.dqmc,dc.fgsmc,dc.mingc)\n");
						strsbgroupby.append(" group by cube(g.dqmc,dc.fgsmc,dc.mingc,c.mingc)\n");
						strhaving.append("");
						strorderby.append(" order by grouping(g.dqmc) desc,g.dqmc,grouping(dc.fgsmc) desc,dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc \n");
					}
					rl.close();
					
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					cn.Close();
				}
			}else if (jib==3){//选电厂只刷新出该电厂
				strgrouping.append("decode(grouping(g.dqmc),1,'合计',g.dqmc) as 矿别,\n");
				strgrouping.append("decode(grouping(dc.mingc)+grouping(g.dqmc),2,'',1,'小计',dc.mingc) as 电厂,\n");
				strwhere.append(" and dc.id=").append(this.getTreeid());
				strgroupby.append(" group by rollup(g.dqmc,dc.mingc)\n");
				strsbgroupby.append(" group by cube(g.dqmc,dc.mingc,c.mingc)\n");
				strhaving.append(" having not grouping(g.dqmc)=1\n");
				strorderby.append(" order by grouping(g.dqmc) desc,g.dqmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc \n");
			}
		}*/
	}
	
	
	public String getSQL(String Beginriq,String Endriq,String Jihkj_fh,String Jihkj_j,String Gongsid){//获得主SQL
		StringBuffer row = new StringBuffer();
		row.append("select ");
		row.append(strgrouping);
		row.append(
			"sum(nvl(nianjhl,0)) nianjhl,\n" +
			"round_new(sum(jihl_bq),0) jihl_bq,round_new(sum(jihl_tq),0) jihl_tq,round_new(sum(shidl_bq),0) shidl_bq,\n" + 
			"round_new(sum(shidl_tq),0) shidl_tq,round_new(sum(chaz_bq),0) chaz_bq,round_new(sum(chaz_tq),0) chaz_tq,\n" + 
			"--sum(daohl_bn)daohl_bn,\n" + 
			"round_new(decode(sum(nvl(nianjhl,0)),0,0,sum(shidl_bq)/(sum(nvl(nianjhl,0)))*100),2) as daohl_bn,\n" + 
			"round_new(decode(sum(jihl_bq),0,0,sum(shidl_bq)/sum(jihl_bq)*100),2) as daohl_bq,\n" + 
			"round_new(decode(sum(jihl_tq),0,0,sum(shidl_tq)/sum(jihl_tq)*100),2) as daohl_tq\n" + 
			"--sum(daohl_bq)daohl_bq,sum(daohl_tq)daohl_tq\n" + 
			"from\n" + 
			"------------同期--------------\n" + 
			"(select  nianjhl,biaot.diancxxb_id,biaot.gongysb_id,\n" + 
			"0 jihl_bq,jihlb.jihl jihl_tq,0 shidl_bq,shisl.daohl shidl_tq,0 chaz_bq,\n" + 
			"nvl(jihlb.jihl,0)-nvl(shisl.daohl,0)chaz_tq,\n" + 
			"decode(shisl.daohl,null,0,nianjhl,0,0,round(shisl.daohl/(nianjhl)*100,2)) daohl_bn,0 daohl_bq,\n" + 
			"decode(jihlb.jihl,null,0,0,0,decode(shisl.daohl,null,0,round(shisl.daohl/(jihlb.jihl)*100,2))) daohl_tq\n" + 
			"from\n" + 
			"--实收量\n" + 
			"(select fh.diancxxb_id,nvl(h.hetgysbid,fh.gongysb_id) gongysb_id,sum(fh.daohl) as daohl from (\n" + 
			"select fh.diancxxb_id,fh.gongysb_id,fh.lieid,\n" + 
			"       (round_new(sum(fh.biaoz), 0) + round_new(sum(fh.yingd), 0) -\n" + 
			"       round_new(sum(fh.yingd - fh.yingk), 0)) daohl\n" + 
			"  from fahb fh\n" + 
			" where "+Jihkj_fh+"\n" + 
			"   and fh.daohrq >=ADD_MONTHS(to_date('"+Beginriq+"', 'yyyy-mm-dd'), -12)\n" + 
			"   and fh.daohrq <=ADD_MONTHS(to_date('"+Endriq+"', 'yyyy-mm-dd'), -12)\n" + 
			" group by fh.diancxxb_id,fh.gongysb_id, fh.lieid) fh,rigjb r,hetb h\n" + 
			"  where fh.lieid=r.fahb_id(+) and r.hetxxb_id=h.hetgysbid(+)\n" +
			" group by fh.diancxxb_id,h.hetgysbid,fh.gongysb_id) shisl,\n" + 
			"--计划量\n" + 
			"(select 0 nianjhl,a.diancxxb_id,a.gongysb_id,sum(hetl) as jihl from\n" +
			"  (select\n" + 
			"         hetb.diancxxb_id,\n" + 
			"         hetb.hetgysbid gongysb_id,\n" + 
			"               sum(getHetl_Sjd(ADD_MONTHS(to_date('"+Beginriq+"', 'yyyy-mm-dd'), -12),\n" + 
			"                               ADD_MONTHS(to_date('"+Endriq+"', 'yyyy-mm-dd'), -12),\n" + 
			"                               hetslb.riq,\n" + 
			"                               hetslb.hetl)) hetl\n "+ 
			"    from hetb, hetslb\n" + 
			"   where "+Jihkj_j+" and hetb.id = hetslb.hetb_id \n" + 
			"   and hetb.qiandrq>=ADD_MONTHS(to_date('"+Beginriq+"', 'yyyy-mm-dd'), -12)\n" + 
			"   and hetb.qiandrq<=ADD_MONTHS(to_date('"+Endriq+"', 'yyyy-mm-dd'), -12)\n" + 
			"   group by\n" + 
			"            hetb.diancxxb_id,\n" + 
			"            hetb.hetgysbid) a\n" + 
			"   group by a.diancxxb_id,a.gongysb_id) jihlb," + 
			"--表头\n" + 
			"(select distinct fh.diancxxb_id,nvl(h.hetgysbid,fh.gongysb_id) gongysb_id from fahb fh,rigjb r,hetb h where "+Jihkj_fh+"\n" + 
			"   and fh.lieid=r.fahb_id(+) and r.hetxxb_id=h.hetgysbid(+)\n" +
			"   and fh.daohrq >=ADD_MONTHS(to_date('"+Beginriq+"', 'yyyy-mm-dd'), -12)\n" + 
			"   and fh.daohrq <=ADD_MONTHS(to_date('"+Endriq+"', 'yyyy-mm-dd'), -12)\n" + 
			"union\n" + 
			"select distinct hetb.diancxxb_id,hetb.hetgysbid gongysb_id from hetb, hetslb where "+Jihkj_j+" and hetb.id = hetslb.hetb_id \n" + 
			"   and hetb.qiandrq >= ADD_MONTHS(to_date('"+Beginriq+"', 'yyyy-mm-dd'), -12)\n" + 
			"   and hetb.qiandrq <= ADD_MONTHS(to_date('"+Endriq+"', 'yyyy-mm-dd'), -12)) biaot\n" + 
			"where biaot.diancxxb_id=shisl.diancxxb_id(+) and biaot.diancxxb_id=jihlb.diancxxb_id(+)\n" + 
			"  and biaot.gongysb_id=shisl.gongysb_id(+) and biaot.gongysb_id=jihlb.gongysb_id(+)\n" + 
			"union\n" + 
			"--------------------本期-------------------\n" + 
			"select  nianjhl,biaot.diancxxb_id,biaot.gongysb_id,\n" + 
			"jihlb.jihl  jihl_bq,0 jihl_tq,shisl.daohl shisl_bq,0 shisl_tq,\n" + 
			"nvl(jihlb.jihl,0)-nvl(shisl.daohl,0)chaz_bq,0 chaz_tq,\n" + 
			"decode(shisl.daohl,null,0,nianjhl,0,0,round(shisl.daohl/(nianjhl)*100,2)) daohl_bn,\n" + 
			"decode(jihlb.jihl,null,0,0,0,\n" + 
			"decode(shisl.daohl,null,0,round(shisl.daohl/(jihlb.jihl)*100,2))) daohl_bq,0 daohl_tq\n" + 
			"from\n" + 
			"--实收量\n" + 
			"(select fh.diancxxb_id,nvl(h.hetgysbid,fh.gongysb_id) gongysb_id,sum(fh.daohl) as daohl from (\n" + 
			"select fh.diancxxb_id,fh.gongysb_id,fh.lieid,\n" + 
			"       (round_new(sum(fh.biaoz), 0) + round_new(sum(fh.yingd), 0) -\n" + 
			"       round_new(sum(fh.yingd - fh.yingk), 0)) daohl\n" + 
			"  from fahb fh\n" + 
			" where "+Jihkj_fh+"\n" + 
			"   and fh.daohrq >=to_date('"+Beginriq+"', 'yyyy-mm-dd')\n" + 
			"   and fh.daohrq <=to_date('"+Endriq+"', 'yyyy-mm-dd')\n" + 
			" group by fh.diancxxb_id,fh.gongysb_id, fh.lieid) fh,rigjb r,hetb h\n" + 
			"  where fh.lieid=r.fahb_id(+) and r.hetxxb_id=h.hetgysbid(+)\n" +
			" group by fh.diancxxb_id,h.hetgysbid,fh.gongysb_id) shisl,\n" + 
			"\n" + 
			"--计划\n" + 
			"(select sum(nianjhl) nianjhl,a.diancxxb_id,a.gongysb_id,sum(hetl) as jihl from\n" +
			"  (select 0 nianjhl,\n" + 
			"         hetb.diancxxb_id,\n" + 
			"         hetb.hetgysbid gongysb_id,\n" + 
			"               sum(getHetl_Sjd(to_date('"+Beginriq+"', 'yyyy-mm-dd'),\n" + 
			"                               to_date('"+Endriq+"', 'yyyy-mm-dd'),\n" + 
			"                               hetslb.riq,\n" + 
			"                               hetslb.hetl)) hetl\n "+ 
			"    from hetb, hetslb\n" + 
			"   where "+Jihkj_j+" and hetb.id = hetslb.hetb_id \n" + 
			"   and hetb.qiandrq>=to_date('"+Beginriq+"', 'yyyy-mm-dd')\n" + 
			"   and hetb.qiandrq<=to_date('"+Endriq+"', 'yyyy-mm-dd')\n" + 
			"   group by\n" + 
			"            hetb.diancxxb_id,\n" + 
			"            hetb.hetgysbid\n" +
			"union\n" +
			"select sum(hetslb.hetl) nianjhl,\n" + 
			"      hetb.diancxxb_id,\n" + 
			"      hetb.hetgysbid gongysb_id,\n" + 
			"      0 jihl\n" + 
			" from hetb, hetslb\n" + 
			"where  "+Jihkj_j+" and hetb.id = hetslb.hetb_id\n" + 
			"and to_char(hetb.qiandrq, 'yyyy') =to_char(to_date('"+Beginriq+"', 'yyyy-mm-dd'), 'yyyy')\n" + 
			"group by\n" + 
			"         hetb.diancxxb_id,\n" + 
			"         hetb.hetgysbid" +
			") a\n" + 
			"   group by a.diancxxb_id,a.gongysb_id) jihlb," + 
			"\n" + 
			"--表头\n" + 
			"(select distinct fh.diancxxb_id,nvl(h.hetgysbid,fh.gongysb_id) gongysb_id from fahb fh,rigjb r,hetb h where "+Jihkj_fh+"\n" + 
			"   and fh.lieid=r.fahb_id(+) and r.hetxxb_id=h.hetgysbid(+)\n" +
			"   and fh.daohrq >=to_date('"+Beginriq+"', 'yyyy-mm-dd')\n" + 
			"   and fh.daohrq <=to_date('"+Endriq+"', 'yyyy-mm-dd')\n" + 
			"union\n" + 
			"select distinct hetb.diancxxb_id,hetb.hetgysbid gongysb_id from hetb, hetslb where "+Jihkj_j+" and hetb.id = hetslb.hetb_id \n" + 
			"   and hetb.qiandrq >= to_date('"+Beginriq+"', 'yyyy-mm-dd')\n" + 
			"   and hetb.qiandrq <= to_date('"+Endriq+"', 'yyyy-mm-dd')) biaot\n" + 
			"\n" + 
			"where biaot.diancxxb_id=shisl.diancxxb_id(+) and biaot.diancxxb_id=jihlb.diancxxb_id(+)\n" + 
			"  and biaot.gongysb_id=shisl.gongysb_id(+) and biaot.gongysb_id=jihlb.gongysb_id(+)\n" + 
			") shuj,vwdianc dc,vwgongys g\n" + 
			"where shuj.diancxxb_id=dc.id and shuj.gongysb_id=g.id "+Gongsid+" ");
		row.append(strwhere).append("\n");
		row.append(strgroupby).append("\n");
		row.append(strhaving).append("\n");
		row.append(strorderby).append("\n");
		return row.toString();
	}
	
	private String getZhiltj() {
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("");
		String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));
		String riq1=FormatDate(DateUtil.getDate(getEndriqDate()));
		getTiaoj();//执行条件
		String Beginriq=getBeginriqDate();
		String Endriq=getEndriqDate();
		String jihkj_fh=" 1=1 ";
		String jihkj_j=" 1=1 ";
		long jihkj=getJihkjDropDownValue().getId();//2市场
		if(jihkj!=-1){//不是全部
			jihkj_fh=" fh.jihkjb_id="+jihkj;
			jihkj_j=" hetb.jihkjb_id="+jihkj;
		}
		
		String Gongsid="";
//		if (getGongysDropDownValue().getValue().equals("全部")){
//			Gongsid="";
//		}else {
//			Gongsid=" and g.dqid="+getGongysDropDownValue().getId()+" ";
//		}
		
		sbsql.append(getSQL(Beginriq,Endriq,jihkj_fh,jihkj_j,Gongsid));
		
		System.out.println(sbsql.toString());
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		
		String[][] ArrHeader =null;
		int ArrWidth[] =null;
		String title="";
		int col=1;
		/*if (getLeixSelectValue().getValue().equals("分厂")){
			title="(分厂)";
			ArrHeader=new String[2][11];
			ArrHeader[0]=new String[] {"单位","合同量","合同量","合同量","实收量","实收量","差额","差额","到货率(%)","到货率(%)","到货率(%)"};
			ArrHeader[1]=new String[] {"单位","本年","本期","同期","本期","同期","本期","同期","本年","本期","同期"};
			ArrWidth=new int[] {120,60,60,60,60,60,60,60,60,60,60};
		}else if (getLeixSelectValue().getValue().equals("分矿")){
			title="(分矿)";
			ArrHeader=new String[2][11];
			ArrHeader[0]=new String[] {"矿别","合同量","合同量","合同量","实收量","实收量","差额","差额","到货率(%)","到货率(%)","到货率(%)"};
			ArrHeader[1]=new String[] {"矿别","本年","本期","同期","本期","同期","本期","同期","本年","本期","同期"};
			ArrWidth=new int[] {120,60,60,60,60,60,60,60,60,60,60};
		}else if (getLeixSelectValue().getValue().equals("分厂分矿")){*/
			title="(分厂分矿)";
			col=2;
			ArrHeader=new String[2][12];
			ArrHeader[0]=new String[] {"单位","矿别","合同量","合同量","合同量","实收量","实收量","差额","差额","到货率(%)","到货率(%)","到货率(%)"};
			ArrHeader[1]=new String[] {"单位","矿别","本年","本期","同期","本期","同期","本期","同期","本年","本期","同期"};
			ArrWidth=new int[] {120,100,60,60,60,60,60,60,60,60,60,60};
		/*}else if (getLeixSelectValue().getValue().equals("分矿分厂")){
			title="(分矿分厂)";
			col=2;
			ArrHeader=new String[2][12];
			ArrHeader[0]=new String[] {"矿别","单位","合同量","合同量","合同量","实收量","实收量","差额","差额","到货率(%)","到货率(%)","到货率(%)"};
			ArrHeader[1]=new String[] {"矿别","单位","本年","本期","同期","本期","同期","本期","同期","本年","本期","同期"};
			ArrWidth=new int[] {100,120,60,60,60,60,60,60,60,60,60,60};
		}*/
		
		Table bt=new Table(rs,2,0,col);
		rt.setBody(bt);
		rt.body.setPageRows(22);
		rt.body.ShowZero=false;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRow();//合并行
		rt.body.mergeFixedCols();//和并列
		rt.setTitle("合同完成情况"+title, ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 3, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 4,""+riq+"-"+riq1,Table.ALIGN_CENTER);
		rt.setDefaultTitle(10, 2, "单位:吨" ,Table.ALIGN_RIGHT);
		
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "制表时间:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "审核:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(10, 2,"制表:"+((Visit)getPage().getVisit()).getRenymc(), Table.ALIGN_RIGHT);
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
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	///////
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
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
	}

	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();
		
		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1","forms[0]");// 与html页中的id绑定,并自动刷新
		df.setWidth(70);
//		df.setReadOnly(true);
//		df.listeners=";change:function(){document.forms[0].submit()}";
		tb1.addField(df);
		
		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2","forms[0]");// 与html页中的id绑定,并自动刷新
		df1.setWidth(70);
//		df1.setReadOnly(true);
//		df1.listeners="change:function(){document.forms[0].submit()}";
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

//		tb1.addText(new ToolbarText("统计口径:"));
//		ComboBox cb1 = new ComboBox();
//		cb1.setTransform("LeixSelect");
//		cb1.setWidth(70);
//		cb1.listeners="select :function(){document.forms[0].submit()}";
//		tb1.addField(cb1);
//		tb1.addText(new ToolbarText("-"));
		
		
		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"forms[0]","",getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(80);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("计划口径:"));
		ComboBox tbjhkj = new ComboBox();
		tbjhkj.setTransform("JihkjDropDown");
		tbjhkj.setWidth(80);
		tbjhkj.listeners="select :function(){document.forms[0].submit()}";
		tb1.addField(tbjhkj);
		tb1.addText(new ToolbarText("-"));
		
		/*tb1.addText(new ToolbarText("供货单位:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("GongysDropDown");
		cb2.setEditable(true);
		//meik.setWidth(60);
		cb2.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb2);
		tb1.addText(new ToolbarText("-"));*/
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		ToolbarButton tb_f  = new ToolbarButton(null,"返回","function(){document.location='"+MainGlobal.getHomeContext(this)+"/app?service=page/Hetwcqktb&riq1='+document.getElementById('qiandrq1').value+'&riq2='+document.getElementById('qiandrq2').value"+"}");
		tb1.addItem(tb);
		tb1.addItem(tb_f);
		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
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
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			visit.setDefaultTree(null);
//			visit.setString4(null);
//			visit.setString5(null);
			visit.setString4(cycle.getRequestContext().getParameter("riq1"));
			visit.setString5(cycle.getRequestContext().getParameter("riq2"));
			visit.setString6("");
			((Visit) getPage().getVisit()).setboolean4(false);
		}
		getToolbars();
//    	分矿
		if(getLeixSelectValue().getId()==2){
			int jib11=0,ranlgs11=0;
			String sql="select jib,diancxxb.ranlgs\n" +
			"from diancxxb\n" + 
			"where id="+getTreeid();
			JDBCcon con11=new JDBCcon();
			ResultSet rs11=con11.getResultSet(sql);
			try {
				while(rs11.next()){
					jib11=rs11.getInt("jib") ;
					ranlgs11=rs11.getInt("ranlgs") ;
				}
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
			con11.Close();
		    jihkjWhere_fh="";
		    jihkjWhere_j="";
		    gongysWhere="";
		    diancWhere="";
		    gongysmc=" mingc ";
			long jihkj=getJihkjDropDownValue().getId();//2市场
			if(jihkj!=-1){//不是全部
				jihkjWhere_fh=" and fh.jihkjb_id="+jihkj;
				jihkjWhere_j=" and j.jihkjb_id="+jihkj;
				if(jihkj==2){//市场
					gongysmc="decode(mingc,'总计',mingc,'<a target=\"_blank\" href=\""+MainGlobal.getHomeContext(this)+"/app?service=page/Hetwcqkb2_1"+
					"&diancxxb_id="+getTreeid()+
					"&gongysbid='||mingc||'"+
					"&riq1="+getBeginriqDate()+
					"&riq2="+getEndriqDate()+
					"\">'||mingc||'</a>') mingc";
				}
			}
			if(getGongysDropDownValue().getId()!=-1){//全部
				gongysWhere=" and g.dqid="+getGongysDropDownValue().getId()+" ";
			}
			if(jib11==2&&ranlgs11==1){//燃料公司
				diancWhere=" and diancxxb.shangjgsid="+getTreeid()+" ";
			}else if(jib11==2&&ranlgs11!=1){//分公司
				diancWhere=" and diancxxb.fuid="+getTreeid()+" ";
			}else if(jib11==3){//电厂
				diancWhere=" and diancxxb.id="+getTreeid()+" ";
			}else{//集团
				diancWhere=" ";
			}
		}else if(getLeixSelectValue().getId()==1){//分厂
			int jib11=0,ranlgs11=0;
			String sql="select jib,diancxxb.ranlgs\n" +
			"from diancxxb\n" + 
			"where id="+getTreeid();
			JDBCcon con11=new JDBCcon();
			ResultSet rs11=con11.getResultSet(sql);
			try {
				while(rs11.next()){
					jib11=rs11.getInt("jib") ;
					ranlgs11=rs11.getInt("ranlgs") ;
				}
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
			con11.Close();
			jihkjWhere_fh="";
			jihkjWhere_j="";
		    gongysWhere="";
		    diancWhere="";
		    gongysmc=" mingc ";
			long jihkj=getJihkjDropDownValue().getId();//2市场
			if(jihkj!=-1){//不是全部
				jihkjWhere_fh=" and fh.jihkjb_id="+jihkj;
				jihkjWhere_j=" and j.jihkjb_id="+jihkj;
			}
			if(getGongysDropDownValue().getId()!=-1){//全部
				gongysWhere=" and g.dqid="+getGongysDropDownValue().getId()+" ";
			}
			if(jib11==2&&ranlgs11==1){//燃料公司
				diancWhere=" and diancxxb.shangjgsid="+getTreeid()+" ";
			}else if(jib11==2&&ranlgs11!=1){//分公司
				diancWhere=" and diancxxb.fuid="+getTreeid()+" ";
			}else if(jib11==3){//电厂
				diancWhere=" and diancxxb.id="+getTreeid()+" ";
			}else{//集团
				diancWhere=" ";
			}
		}else{//棋盘表
			int jib11=0,ranlgs11=0;
			String sql="select jib,diancxxb.ranlgs\n" +
			"from diancxxb\n" + 
			"where id="+getTreeid();
			JDBCcon con11=new JDBCcon();
			ResultSet rs11=con11.getResultSet(sql);
			try {
				while(rs11.next()){
					jib11=rs11.getInt("jib") ;
					ranlgs11=rs11.getInt("ranlgs") ;
				}
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
			con11.Close();
		    jihkjWhere_j="";
		    gongysWhere="";
		    diancWhere="";
		    gongysmc=" mingc ";
			long jihkj=getJihkjDropDownValue().getId();//2市场
			if(jihkj!=-1){//不是全部
				jihkjWhere_j=" and j.id="+jihkj;
			}
			if(getGongysDropDownValue().getId()!=-1){//全部
				gongysWhere=" and vwgongys.dqid="+getGongysDropDownValue().getId()+" ";
			}
			if(jib11==2&&ranlgs11==1){//燃料公司
				diancWhere=" and vwdianc.shangjgsid="+getTreeid()+" ";
			}else if(jib11==2&&ranlgs11!=1){//分公司
				diancWhere=" and vwdianc.fuid="+getTreeid()+" ";
			}else if(jib11==3){//电厂
				diancWhere=" and vwdianc.id="+getTreeid()+" ";
			}else{//集团
				diancWhere=" ";
			}
		}
		if(!((Visit) getPage().getVisit()).getboolean4()){//只要不是供应商就刷新供应商和下面的数据，然后通过选择供应商选择数据
			setGongysDropDownValue(null);
			setGongysDropDownModel(null);
			getGongysDropDownModel();
			((Visit) getPage().getVisit()).setboolean4(false);
		}
		blnIsBegin = true;
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
	
//	 供应商
    public IDropDownBean getGongysDropDownValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getGongysDropDownModel().getOption(0));
    	}
       return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }
    public void setGongysDropDownValue(IDropDownBean Value) {
	    	if(Value!=null&&getGongysDropDownValue()!=null){
	    		if(Value.getId()!=getGongysDropDownValue().getId()){
	    			((Visit) getPage().getVisit()).setboolean4(true);
	    		}else{
	    			((Visit) getPage().getVisit()).setboolean4(false);
	    		}
	    	}
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

    	String sql="";
    	if(getLeixSelectValue().getId()==2||getLeixSelectValue().getId()==1){
    		sql="select dqid,dqmc\n" + 
			"from (\n" + 
			"select g.dqid,g.dqmc\n" + 
			"from niandhtqkb j,vwgongys g,diancxxb\n" + 
			//累计日期
			"where j.gongysb_id=g.dqid and j.diancxxb_id=diancxxb.id   "+jihkjWhere_j+gongysWhere+diancWhere+" and (j.riq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and j.riq<= to_date('"+getEndriqDate()+"','yyyy-mm-dd'))union\n" + 
			"select  g.dqid,g.dqmc\n" + 
			"from fahb fh,vwgongys g,diancxxb\n" + 
			"where   fh.gongysb_id=g.dqid and fh.diancxxb_id=diancxxb.id  "+jihkjWhere_fh+gongysWhere+diancWhere+" and (fh.daohrq>= to_date('"+getBeginriqDate()+"','yyyy-mm-dd')  and fh.daohrq<= to_date('"+getEndriqDate()+"','yyyy-mm-dd')))"; 
    	}else{
    		sql="select dqid,dqmc\n" + 
			"from (\n" + 
			"select g.dqid,g.dqmc\n" + 
			"from niandhtqkb ,vwgongys g,vwdianc,jihkjb j\n" + 
			//累计日期
			"where  niandhtqkb.jihkjb_id=j.id and niandhtqkb.gongysb_id=g.dqid and niandhtqkb.diancxxb_id=vwdianc.id   "+jihkjWhere_j+gongysWhere+diancWhere+" and (niandhtqkb.riq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and niandhtqkb.riq<= to_date('"+getEndriqDate()+"','yyyy-mm-dd'))union\n" + 
			"select  g.dqid,g.dqmc\n" + 
			"from fahb fh,vwgongys g,vwdianc ,jihkjb j\n" + 
			"where   fh.jihkjb_id=j.id and fh.gongysb_id=g.dqid and fh.diancxxb_id=vwdianc.id  "+jihkjWhere_j+gongysWhere+diancWhere+" and (fh.daohrq>= to_date('"+getBeginriqDate()+"','yyyy-mm-dd')  and fh.daohrq<= to_date('"+getEndriqDate()+"','yyyy-mm-dd')))"; 

    	}
//    	+"where gongysb.fuid=0";
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"全部")) ;
        return ;
    }
    String treeid="";
	public String getTreeid() {
		treeid=((Visit) this.getPage().getVisit()).getString6();
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
		((Visit) this.getPage().getVisit()).setString6(treeid);
	}
    public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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
		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
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
//		得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
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
		
		 //类型
	    public IDropDownBean getLeixSelectValue() {
	    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
	   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getLeixSelectModel().getOption(0));
	   	}
	       return  ((Visit) getPage().getVisit()).getDropDownBean4();
	    }
	    public void setLeixSelectValue(IDropDownBean Value) {
//	    	if(Value!=null&&getLeixSelectValue()!=null){
//	    		if(Value.getId()!=getLeixSelectValue().getId()){
//	    			((Visit) getPage().getVisit()).setboolean4(true);
//	    		}
//	    	}
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
	        list.add(new IDropDownBean(0,"分厂"));
	        list.add(new IDropDownBean(1,"分矿"));
	        list.add(new IDropDownBean(2,"分厂分矿"));
	        list.add(new IDropDownBean(3,"分矿分厂"));
	        list.add(new IDropDownBean(4,"棋盘表"));
	        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(list)) ;
	        return ;
	    }
	    

//		计划口径
		public IDropDownBean getJihkjDropDownValue() {
			if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
				((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean) getJihkjDropDownModel().getOption(2));
			}
			return ((Visit) getPage().getVisit()).getDropDownBean5();
		}

		public void setJihkjDropDownValue(IDropDownBean Value) {
			((Visit) getPage().getVisit()).setDropDownBean5(Value);
		}

		public void setJihkjfsDropDownModel(IPropertySelectionModel value) {
			((Visit) getPage().getVisit()).setProSelectionModel5(value);
		}

		public IPropertySelectionModel getJihkjDropDownModel() {
			if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
				getJihkjDropDownModels();
			}
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
		}

		public void getJihkjDropDownModels() {
			String sql = "select id,mingc\n" + "from jihkjb \n";
			((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(sql, "全部"));
			return;
		}	
		
		
//		得到电厂树下拉框的电厂名称或者分公司,集团的名称
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
//		 分公司下拉框
		private boolean _fengschange = false;

		public IDropDownBean getFengsValue() {
			if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
				((Visit) getPage().getVisit())
						.setDropDownBean4((IDropDownBean) getFengsModel()
								.getOption(0));
			}
			return ((Visit) getPage().getVisit()).getDropDownBean4();
		}

		public void setFengsValue(IDropDownBean Value) {
			if (getFengsValue().getId() != Value.getId()) {
				_fengschange = true;
			}
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