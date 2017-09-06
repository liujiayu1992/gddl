package com.zhiren.jt.zdt.shengcdy.yunsjhwcqk;
/*
 * 作者：songy
 * 时间：2011-03-23 
 * 描述：修改下拉菜单的排序，要求按照名称进行排序
 */
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

public class Yunsjhwcqk extends BasePage {
	public boolean getRaw() {
		return true;
	}
	
	private StringBuffer strgrouping = new StringBuffer();
	private StringBuffer strwhere = new StringBuffer();
	private StringBuffer strgroupby = new StringBuffer();
	private StringBuffer strsbgroupby = new StringBuffer();//得到数据的分组 cube
	private StringBuffer strhaving = new StringBuffer();
	private StringBuffer strorderby = new StringBuffer();
	private String Rowtitle ="";

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
		// return getQibb();
		if (getLeixSelectValue().getValue().equals("分厂")) {
			return getSelectDate();//getSelectData_Fenc();
		} else if (getLeixSelectValue().getValue().equals("分厂分矿")) {
			return getSelectDate();//getSelectData_Fencfk();
		} else if (getLeixSelectValue().getValue().equals("分矿")) {
			return getSelectDate();//getSelectData_Fenk();
		} else if (getLeixSelectValue().getValue().equals("分矿分厂")) {
			return getSelectDate();//getSelectData_Fenkfc();
		} else if (getLeixSelectValue().getValue().equals("棋盘表")) {
			return getQibb();
		} else {
			return "无此报表";
		}
	}
	
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
		if (getLeixSelectValue().getValue().equals("分厂")){
			Rowtitle="电厂";
			if(jib==1){//选集团时刷新出所有的电厂
				strgrouping.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,'&nbsp;&nbsp;'||dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as 电厂,\n");
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
						strgrouping.append("decode(grouping(dc.rlgsmc)+grouping(dc.fgsmc)+grouping(dc.mingc),3,'总计',2,dc.rlgsmc,1,'&nbsp;&nbsp;'||dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as 电厂,\n");
						strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
						strgroupby.append(" group by rollup(dc.rlgsmc,dc.fgsmc,dc.mingc)\n");
						strsbgroupby.append(" group by cube(dc.rlgsmc,dc.fgsmc,dc.mingc,c.mingc)\n");
						strhaving.append(" having not grouping(dc.rlgsmc)=1\n");
						strorderby.append(" order by grouping(dc.rlgsmc) desc,dc.rlgsmc desc,grouping(dc.fgsmc) desc,dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc \n");
					}else{//分公司
						strgrouping.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,'&nbsp;&nbsp;'||dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as 电厂,\n");
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
				strgrouping.append("decode(grouping(dc.mingc),1,'总计','&nbsp;&nbsp;'||dc.mingc) as 电厂,\n");
				strwhere.append(" and dc.id=").append(this.getTreeid());
				strgroupby.append(" group by rollup(dc.mingc)\n");
				strsbgroupby.append(" group by cube(dc.mingc,c.mingc)\n");
				strhaving.append(" having not grouping(dc.mingc)=1\n");
				strorderby.append(" order by grouping(dc.mingc) desc,max(dc.xuh),dc.mingc \n");
			}
		}else if (getLeixSelectValue().getValue().equals("分矿")){
			Rowtitle="矿别";
			strgrouping.append("decode(grouping(g.dqmc),1,'总计','&nbsp;&nbsp;'||max(g.dqmc)) as 矿别,\n");
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
			
		}else if (getLeixSelectValue().getValue().equals("分厂分矿")) {
			Rowtitle="电厂,矿别";
			if(jib==1){//选集团时刷新出所有的电厂
				strgrouping.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,'&nbsp;&nbsp;'||dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as 电厂,\n");
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
						strgrouping.append("decode(grouping(dc.rlgsmc)+grouping(dc.fgsmc)+grouping(dc.mingc),3,'总计',2,dc.rlgsmc,1,'&nbsp;&nbsp;'||dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as 电厂,\n");
						strgrouping.append("decode(grouping(dc.mingc)+grouping(g.dqmc),2,'',1,'小计',g.dqmc) as 矿别,\n");
						strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
						strgroupby.append(" group by rollup(dc.rlgsmc,dc.fgsmc,dc.mingc,g.dqmc)\n");
						strsbgroupby.append(" group by cube(dc.rlgsmc,dc.fgsmc,dc.mingc,g.dqmc,c.mingc)\n");
						strhaving.append(" having not grouping(dc.rlgsmc)=1\n");
						strorderby.append(" order by grouping(dc.rlgsmc) desc,dc.rlgsmc desc,grouping(dc.fgsmc) desc,dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc ,grouping(g.dqmc) desc,g.dqmc\n");
					}else{//分公司
						strgrouping.append("decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,'&nbsp;&nbsp;'||dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as 电厂,\n");
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
				strgrouping.append("decode(grouping(dc.mingc),1,'总计','&nbsp;&nbsp;'||dc.mingc) as 电厂,\n");
				strgrouping.append("decode(grouping(dc.mingc)+grouping(g.dqmc),2,'',1,'小计',g.dqmc) as 矿别,\n");
				strwhere.append(" and dc.id=").append(this.getTreeid());
				strgroupby.append(" group by rollup(dc.mingc,g.dqmc)\n");
				strsbgroupby.append(" group by cube(dc.mingc,g.dqmc,c.mingc)\n");
				strhaving.append(" having not grouping(dc.mingc)=1\n");
				strorderby.append(" order by grouping(dc.mingc) desc,max(dc.xuh),dc.mingc ,grouping(g.dqmc) desc,g.dqmc\n");
			}
		}else if(getLeixSelectValue().getValue().equals("分矿分厂")){
			Rowtitle="矿别,电厂";
			if(jib==1){//选集团时刷新出所有的电厂
				strgrouping.append("decode(grouping(g.dqmc),1,'总计',g.dqmc) as 矿别,\n");
				strgrouping.append("decode(grouping(g.dqmc)+grouping(dc.fgsmc)+grouping(dc.mingc),3,'',2,'',1,'&nbsp;&nbsp;'||dc.fgsmc||'小计','&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as 电厂,\n");
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
						strgrouping.append("decode(grouping(g.dqmc),1,'总计',g.dqmc) as 矿别,\n");
						strgrouping.append("decode(grouping(g.dqmc)+grouping(dc.rlgsmc)+grouping(dc.mingc),3,'',2,'',1,'&nbsp;&nbsp;'||dc.rlgsmc||'小计','&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as 电厂,\n");
						strwhere.append(" and (dc.fgsid="+this.getTreeid()+" or dc.rlgsid= "+this.getTreeid()+") \n");
						strgroupby.append(" group by rollup(g.dqmc,dc.rlgsmc,dc.fgsmc,dc.mingc)\n");
						strsbgroupby.append(" group by cube(g.dqmc,dc.rlgsmc,dc.fgsmc,dc.mingc,c.mingc)\n");
						strhaving.append(" having not grouping(dc.rlgsmc)=1\n");
						strorderby.append(" order by grouping(g.dqmc) desc,g.dqmc,grouping(dc.rlgsmc) desc,dc.rlgsmc desc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc \n");
					}else{//分公司
						strgrouping.append("decode(grouping(g.dqmc),1,'总计',g.dqmc) as 矿别,\n");
						strgrouping.append("decode(grouping(g.dqmc)+grouping(dc.fgsmc)+grouping(dc.mingc),3,'',2,'',1,'&nbsp;&nbsp;'||dc.fgsmc||'小计','&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as 电厂,\n");
						
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
				strgrouping.append("decode(grouping(g.dqmc),1,'总计','&nbsp;&nbsp;'||g.dqmc) as 矿别,\n");
				strgrouping.append("decode(grouping(dc.mingc)+grouping(g.dqmc),2,'',1,'小计',dc.mingc) as 电厂,\n");
				strwhere.append(" and dc.id=").append(this.getTreeid());
				strgroupby.append(" group by rollup(g.dqmc,dc.mingc)\n");
				strsbgroupby.append(" group by cube(g.dqmc,dc.mingc,c.mingc)\n");
				strhaving.append(" having not grouping(g.dqmc)=1\n");
				strorderby.append(" order by grouping(g.dqmc) desc,g.dqmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc \n");
			}
		}
	}
	
	public String getSQL(String Beginriq,String Endriq,String Gongsid){//获得主SQL
		StringBuffer row = new StringBuffer();
		row.append("select ");
		row.append(strgrouping);
		row.append("Round(sum(yunbq.pid),0) as bqjh ,Round(sum(yuntq.pid),0) as tqjh,\n" );
		row.append("Round(sum(shijbq.laimsl),0) as shibq,Round(sum(shijtq.laimsl),0) as shitq,\n" ); 
		row.append("(Round(sum(nvl(shijbq.laimsl,0)),0) - Round(sum(nvl(yunbq.pid,0)),0) ) as clbq,\n" ); 
		row.append("(Round(sum(nvl(shijtq.laimsl,0)),0)-Round(sum(nvl(yuntq.pid,0)),0)) as cltq,\n" ); 
		row.append("decode(Round(sum(yunbq.pid),0),0,0,Round((Round(sum(shijbq.laimsl),0)*100/Round(sum(yunbq.pid),0)),2))as wclbq,\n" ); 
		row.append("decode( Round(sum(yuntq.pid),0),0,0,Round((Round(sum(shijtq.laimsl),0)*100/Round(sum(yuntq.pid),0)),2)) as wcltq\n" ); 
		row.append("from\n" ); 
		row.append("(select distinct diancxxb_id,gongysb_id from fahb f \n");
		row.append("   where f.daohrq>=to_date('"+Beginriq+"','yyyy-mm-dd') \n");
		row.append("    and f.daohrq<=to_date('"+Endriq+"','yyyy-mm-dd') \n");
		row.append("   union \n");
		row.append("  select distinct diancxxb_id,gongysb_id from yunsjhb ys \n");
		row.append("   where ys.riq>=to_date('"+Beginriq+"','yyyy-mm-dd') \n");
		row.append("    and ys.riq<=to_date('"+Endriq+"','yyyy-mm-dd')) fx, \n");
		row.append("--运输计划本期\n" ); 
		row.append("(select ys.diancxxb_id,ys.gongysb_id,sum(ys.pid) as pid from yunsjhb ys\n" ); 
		row.append("where ys.riq>=to_date('"+Beginriq+"','yyyy-mm-dd')\n" ); 
		row.append("and ys.riq<=to_date('"+Endriq+"','yyyy-mm-dd')\n" ); 
		row.append("group by ys.diancxxb_id,ys.gongysb_id) yunbq,\n" ); 
		row.append("--运输计划同期\n" ); 
		row.append("(select ys.diancxxb_id,ys.gongysb_id,sum(ys.pid) as pid from yunsjhb ys\n" ); 
		row.append("where ys.riq>=add_months(to_date('"+Beginriq+"','yyyy-mm-dd'),-12)\n" ); 
		row.append("and ys.riq<=add_months(to_date('"+Endriq+"','yyyy-mm-dd'),-12)\n" ); 
		row.append("group by ys.diancxxb_id,ys.gongysb_id) yuntq,\n" ); 
		row.append("--完成量本期\n" ); 
		row.append("(select f.diancxxb_id,f.gongysb_id,sum(f.laimsl) as laimsl from fahb f\n" ); 
		row.append("where f.daohrq>=to_date('"+Beginriq+"','yyyy-mm-dd')\n" ); 
		row.append("and f.daohrq<=to_date('"+Endriq+"','yyyy-mm-dd')\n" ); 
		row.append("group by f.diancxxb_id,f.gongysb_id) shijbq,\n" ); 
		row.append("--完成量同期\n" ); 
		row.append("(select f.diancxxb_id,f.gongysb_id,sum(f.laimsl) as laimsl from fahb f\n" ); 
		row.append("where f.daohrq>=add_months(to_date('"+Beginriq+"','yyyy-mm-dd'),-12)\n" ); 
		row.append("and f.daohrq<=add_months(to_date('"+Endriq+"','yyyy-mm-dd'),-12)\n" ); 
		row.append("group by f.diancxxb_id,f.gongysb_id) shijtq,vwdianc dc,vwgongys g\n");
		row.append("where fx.diancxxb_id=yunbq.diancxxb_id(+) and fx.diancxxb_id=yuntq.diancxxb_id(+) \n");
		row.append("and fx.diancxxb_id=shijbq.diancxxb_id(+) and fx.diancxxb_id=shijtq.diancxxb_id(+) \n");
		row.append("and fx.gongysb_id=yunbq.gongysb_id(+) and fx.gongysb_id=yuntq.gongysb_id(+) \n");
		row.append("and fx.gongysb_id=shijbq.gongysb_id(+) and fx.gongysb_id=shijtq.gongysb_id(+) \n");
		row.append("and fx.diancxxb_id=dc.id(+) and fx.gongysb_id=g.id(+)\n");
		row.append(Gongsid);
		row.append(strwhere).append("\n");
		row.append(strgroupby).append("\n");
		row.append(strhaving).append("\n");
		row.append(strorderby).append("\n");
		return row.toString();
	}
	
	//主SQL 分厂，分矿，分厂分矿，分矿分厂
	public String getSelectDate(){
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("");
		String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));
		String riq1=FormatDate(DateUtil.getDate(getEndriqDate()));
		getTiaoj();//执行条件
		String Beginriq=getBeginriqDate();
		String Endriq=getEndriqDate();
		
		String Gongsid="";
		if (getGongysDropDownValue().getValue().equals("全部")){
			Gongsid="";
		}else {
			Gongsid=" and g.dqid="+getGongysDropDownValue().getId()+"\n";
		}
		
		sbsql.append(getSQL(Beginriq,Endriq,Gongsid));
		
//		System.out.println(sbsql.toString());
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		
		String[][] ArrHeader =null;
		int ArrWidth[] =null;
		String title="";
		int col=1;
		String[] arrFormat =null;
		if (getLeixSelectValue().getValue().equals("分厂")){
			title="(分厂)";
			ArrHeader = new String[2][9];
			ArrHeader[0] = new String[] { "单位", "运输计划", "运输计划", "完成量", "完成量","差量", "差量", "完成率(%)", "完成率(%)" };
			ArrHeader[1] = new String[] { "单位", "本期", "同期", "本期", "同期", "本期","同期", "本期", "同期" };
	
			ArrWidth = new int[] { 120, 60, 60, 60, 60, 60, 60, 60, 60 };
			arrFormat = new String[] { "", "0", "0", "0", "0", "0","0", "0", "0.0" };
		}else if (getLeixSelectValue().getValue().equals("分矿")){
			title="(分矿)";
			ArrHeader = new String[2][9];
			ArrHeader[0] = new String[] { "矿别", "运输计划", "运输计划", "完成量", "完成量","差量", "差量", "完成率(%)", "完成率(%)" };
			ArrHeader[1] = new String[] { "矿别", "本期", "同期", "本期", "同期", "本期","同期", "本期", "同期" };

			ArrWidth = new int[] { 100, 60, 60, 60, 60, 60, 60, 60, 60 };
			arrFormat = new String[] { "", "0", "0", "0", "0", "0","0", "0", "0.0" };
		}else if (getLeixSelectValue().getValue().equals("分厂分矿")){
			title="(分厂分矿)";
			col=2;
			ArrHeader = new String[2][10];
			ArrHeader[0] = new String[] { "单位", "矿别", "运输计划", "运输计划", "完成量", "完成量","差量", "差量", "完成率(%)", "完成率(%)" };
			ArrHeader[1] = new String[] { "单位", "矿别", "本期", "同期", "本期", "同期", "本期","同期", "本期", "同期" };

			ArrWidth = new int[] { 120, 100, 60, 60, 60, 60, 60, 60, 60, 60 };
			arrFormat = new String[] { "", "", "0", "0", "0", "0", "0","0", "0", "0.0" };
		}else if (getLeixSelectValue().getValue().equals("分矿分厂")){
			title="(分矿分厂)";
			col=2;
			ArrHeader = new String[2][10];
			ArrHeader[0] = new String[] { "矿别", "单位", "运输计划", "运输计划", "完成量", "完成量","差量", "差量", "完成率(%)", "完成率(%)" };
			ArrHeader[1] = new String[] { "矿别", "单位", "本期", "同期", "本期", "同期", "本期","同期", "本期", "同期" };

			ArrWidth = new int[] { 100, 120, 60, 60, 60, 60, 60, 60, 60, 60 };
			arrFormat = new String[] { "", "", "0", "0", "0", "0", "0","0", "0", "0.0" };
		}
		
		Table bt=new Table(rs,2,0,col);
		rt.setBody(bt);
		rt.body.setPageRows(22);
		rt.body.ShowZero=false;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRow();//合并行
		rt.body.mergeFixedCols();//和并列
		rt.body.setColFormat(arrFormat);
		if (rt.body.getRows() > 2) {
			rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		}
		rt.setTitle("运输计划完成情况"+title, ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 2, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 4,""+riq+"-"+riq1,Table.ALIGN_CENTER);
		rt.setDefaultTitle(9, 2, "单位:吨" ,Table.ALIGN_RIGHT);
		
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "制表时间:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 3, "审核:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(8, 3,"制表:"+((Visit)getPage().getVisit()).getRenymc(), Table.ALIGN_RIGHT);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}

	private String getQibb() {
		JDBCcon conn = new JDBCcon();
		ChessboardTable cd = new ChessboardTable();
		String riq = FormatDate(DateUtil.getDate(getBeginriqDate()));
		String riq1 = FormatDate(DateUtil.getDate(getEndriqDate()));
		// 电厂条件
		int jib = this.getDiancTreeJib();
		String diancCondition = "";
		if (jib == 1) {
			diancCondition = ",diancxxb gs where gs.fuid=dc.id and gs.id=fh.diancxxb_id \n ";
			// }else if(){

		} else {
			diancCondition = " where dc.id=fh.diancxxb_id and fh.diancxxb_id in (select id\n"
					+ " from(\n"
					+ " select id from diancxxb\n"
					+ " start with id="
					+ getTreeid()
					+ "\n"
					+ " connect by (fuid=prior id or shangjgsid=prior id)\n"
					+ " )\n"
					+ " union\n"
					+ " select id\n"
					+ " from diancxxb\n"
					+ " where id=" + getTreeid() + ")";
		}
		// 供应商条件
		// String gongysCondition=" and fh.gongysb_id in(select id from gongysb
		// where fuid="+getGongysDropDownValue().getId() +"\n)";
		// String gongysid=" and fh.gongysb_id
		// ="+getGongysDropDownValue().getId()+"\n";
		String gongysfuid = "(select fuid from gongysb where id="
				+ getGongysDropDownValue().getId() + "\n)";
		long fuid = 0;
		ResultSet rs1 = conn.getResultSet(gongysfuid);
		try {
			if (rs1.next()) {
				fuid = rs1.getLong("fuid");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		ArrWidth = new int[] { 120, 60, 80, 80, 80, 80, 80, 80 };

		String gongystj = "";
		if (getGongysDropDownValue().getId() == -1) {
			gongystj = "(select xgys.id as id,xgys.xuh,decode(xgys.fuid,0,xgys.mingc,dgys.mingc) as mingc\n"
					+ "  from gongysb xgys,gongysb dgys\n"
					+ "  where xgys.fuid=dgys.id(+))gys";

		} else {
			gongystj = "(select gys.id,gys.xuh,gys.mingc from gongysb gys  where (gys.id="
					+ getGongysDropDownValue().getId()
					+ " or  gys.fuid="
					+ getGongysDropDownValue().getId() + "))gys ";

		}
		StringBuffer strRow = new StringBuffer();
		strRow
				.append("select decode(grouping(gys.mingc),1,'小计',gys.mingc) as 供应商 \n");
		strRow.append("from  \n");
		strRow.append("yunsjhb jh,fahb fh,diancxxb dc, \n");
		strRow.append(gongystj);
		strRow.append(diancCondition);
		strRow
				.append("and jh.diancxxb_id=fh.diancxxb_id and jh.gongysb_id=fh.gongysb_id and jh.faz_id=fh.faz_id \n");
		strRow.append("and gys.id=jh.gongysb_id \n");
		strRow.append("and jh.riq>=to_date('" + getBeginriqDate()
				+ "','yyyy-mm-dd') \n");
		strRow.append("and jh.riq<=to_date('" + getEndriqDate()
				+ "','yyyy-mm-dd') \n");
		strRow.append("group by rollup(gys.mingc) \n");
		strRow
				.append("order by grouping(gys.mingc) desc,min(gys.xuh),gys.mingc \n");

		StringBuffer strCol = new StringBuffer();
		strCol
				.append("select decode(grouping(dc.mingc),1,'小计',dc.mingc) as 电厂 \n");
		strCol.append("from  \n");
		strCol.append("yunsjhb jh,diancxxb dc,fahb fh, \n");
		strCol.append(gongystj);
		strCol.append(diancCondition);
		strCol
				.append("and jh.diancxxb_id=fh.diancxxb_id and jh.gongysb_id=fh.gongysb_id and jh.faz_id=fh.faz_id \n");
		strCol.append("and jh.riq>=to_date('" + getBeginriqDate()
				+ "','yyyy-mm-dd') \n");
		strCol.append("and jh.riq<=to_date('" + getEndriqDate()
				+ "','yyyy-mm-dd') \n");
		strCol.append("group by rollup(dc.mingc) \n");
		strCol
				.append("order by grouping(dc.mingc) desc,max(dc.xuh),dc.mingc \n");

		StringBuffer sbsql = new StringBuffer();
		sbsql
				.append("select decode(grouping(fgs.mingc),1,'小计',fgs.mingc) as 电厂, \n");
		sbsql.append("decode(grouping(gys.mingc),1,'小计',gys.mingc) as 供应商, \n");
		sbsql
				.append("round(decode(sum(nvl(drjh,0)),0,0,sum(nvl(drlm,0))/sum(nvl(drjh,0)))*100,2) as 当日, \n");
		sbsql
				.append("round(decode(sum(nvl(ljjh,0)),0,0,sum(nvl(ljlm,0))/sum(nvl(ljjh,0)))*100,2) as 累计 \n");
		sbsql.append("from \n");
		sbsql
				.append("(select jh.diancxxb_id,jh.gongysb_id,round(sum(getjihl_sjd(to_date('"
						+ getBeginriqDate() + "','yyyy-mm-dd'), \n");
		sbsql.append("to_date('" + getEndriqDate()
				+ "','yyyy-mm-dd'),jh.riq,pid)),2) as ljjh, \n");
		sbsql.append("round(sum(getjihl_sjd(to_date('" + getEndriqDate()
				+ "','yyyy-mm-dd'), \n");
		sbsql.append("to_date('" + getEndriqDate()
				+ "','yyyy-mm-dd'),jh.riq,pid)),2) as drjh \n");
		sbsql.append("from yunsjhb jh,fahb fh \n");
		sbsql.append("where jh.riq>=to_date('" + getBeginriqDate()
				+ "','yyyy-mm-dd') \n");
		sbsql.append("and jh.riq<=to_date('" + getEndriqDate()
				+ "','yyyy-mm-dd') \n");
		sbsql
				.append("and fh.gongysb_id=jh.gongysb_id and fh.faz_id=jh.faz_id \n");
		sbsql.append("and fh.diancxxb_id=jh.diancxxb_id \n");
		sbsql.append("group by jh.diancxxb_id,jh.gongysb_id) jh, \n");
		sbsql
				.append("(select fh.diancxxb_id,fh.gongysb_id,sum(round(fh.laimsl)) as ljlm, \n");
		sbsql.append("sum(decode(daohrq,to_date('" + getEndriqDate()
				+ "','yyyy-mm-dd'),round(fh.laimsl),0)) as drlm \n");
		sbsql.append("from fahb fh,yunsjhb jh \n");
		sbsql.append("where fh.daohrq>=to_date('" + getBeginriqDate()
				+ "','yyyy-mm-dd') \n");
		sbsql.append("and fh.daohrq<=to_date('" + getEndriqDate()
				+ "','yyyy-mm-dd') \n");
		sbsql
				.append("and fh.gongysb_id=jh.gongysb_id and fh.faz_id=jh.faz_id \n");
		sbsql.append("and fh.diancxxb_id=jh.diancxxb_id \n");
		sbsql.append("group by fh.diancxxb_id,fh.gongysb_id) fh, \n");
		sbsql
				.append("(select xgys.id,xgys.xuh,decode(xgys.fuid,0,xgys.mingc,dgys.mingc) as mingc \n");
		sbsql.append("from gongysb xgys,gongysb dgys \n");
		sbsql
				.append("where xgys.fuid=dgys.id(+)) gys,diancxxb dc,vwfengs fgs \n");
		sbsql.append("where jh.gongysb_id=gys.id and fh.gongysb_id=gys.id \n");
		sbsql
				.append("and jh.diancxxb_id=dc.id and fh.diancxxb_id=dc.id and fgs.id=dc.fuid \n");
		sbsql.append("group by cube(fgs.mingc,gys.mingc) \n");

		// System.out.println(sbsql);
		// System.out.println(strRow);
		// System.out.println(strCol);
		cd.setRowNames("供应商");
		cd.setColNames("电厂");
		cd.setDataNames("当日,累计");
		cd.setDataOnRow(true);
		cd.setRowToCol(false);
		cd.setData(strRow.toString(), strCol.toString(), sbsql.toString());
		cd.DataTable.setColAlign(2, Table.ALIGN_CENTER);
		ArrWidth = new int[cd.DataTable.getCols()];
		for (int i = 1; i < ArrWidth.length; i++) {
			ArrWidth[i] = 80;
		}
		ArrWidth[0] = 120;
		ArrWidth[1] = 120;
		//
		rt.setBody(cd.DataTable);
		rt.body.setWidth(ArrWidth);
		rt.body.mergeFixedRowCol();
		rt.body.ShowZero = false;
		for (int i = 3; i < rt.body.getCols(); i++) {
			rt.body.setColAlign(i, Table.ALIGN_RIGHT);
		}

		if (rt.body.getRows() > 0 && rt.body.getCols() > 0) {
			rt.body.setCellValue(1, 1, "单位");
			rt.body.setCellValue(1, 2, "单位");
		}
		rt.setTitle("运输计划完成情况" + "(棋盘表)", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt
				.setDefaultTitle(1, 2, "制表单位:"
						+ ((Visit) getPage().getVisit()).getDiancqc(),
						Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 3, riq + "-" + riq1, Table.ALIGN_RIGHT);
		rt.body.setPageRows(22);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3,
				"制表时间:"
						+ FormatDate(DateUtil.getDate(DateUtil
								.FormatDate(new Date()))), Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 1, "单位:车、吨", Table.ALIGN_RIGHT);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		conn.Close();
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

	
	public String getBeginriqDate() {
		if (((Visit) getPage().getVisit()).getString4() == null
				|| ((Visit) getPage().getVisit()).getString4() == "") {
			Calendar stra = Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil
					.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH, -1);
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra
					.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}

	public void setBeginriqDate(String value) {
		((Visit) getPage().getVisit()).setString4(value);
	}

	public String getEndriqDate() {
		if (((Visit) getPage().getVisit()).getString5() == null
				|| ((Visit) getPage().getVisit()).getString5() == "") {
			((Visit) getPage().getVisit()).setString5(DateUtil
					.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}

	public void setEndriqDate(String value) {
		((Visit) getPage().getVisit()).setString5(value);
	}

	private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("查询日期:"));
		DateField df = new DateField();

		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1", "");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);

		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2", "");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("统计口径:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("LeixSelect");
		cb1.setWidth(80);
		tb1.addField(cb1);
		tb1.addText(new ToolbarText("-"));
		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1"
						: getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

		tb1.addText(new ToolbarText("供货单位:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("GongysDropDown");
		cb2.setEditable(true);
		// meik.setWidth(60);
		// meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb2);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);

		setToolbar(tb1);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
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
			visit.setDefaultTree(null);
			visit.setString4(null);
			visit.setString5(null);
			this.setBeginriqDate(DateUtil.FormatDate(visit.getMorkssj()));
			this.setEndriqDate(DateUtil.FormatDate(visit.getMorjssj()));
		}
		getToolbars();

		blnIsBegin = true;
	}

	// 供应商
	public IDropDownBean getGongysDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getGongysDropDownModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
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
		String sql = "select distinct g.dqid,g.dqmc from vwgongys g  order by g.dqmc";
		// +"where gongysb.fuid=0";
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, "全部"));
		return;
	}

	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) this.getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField) getToolbar().getItem("diancTree_text"))
						.setValue(((IDropDownModel) getDiancmcModel())
								.getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	// 电厂名称
	private IPropertySelectionModel _IDiancModel;

	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	private boolean _DiancmcChange = false;

	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == Value) {
			_DiancmcChange = false;
		} else {
			_DiancmcChange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb order by  xuh";
		setDiancmcModel(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}

	// 类型
	public IDropDownBean getLeixSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getLeixSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
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
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "分厂"));
		list.add(new IDropDownBean(2, "分矿"));
		list.add(new IDropDownBean(3, "分厂分矿"));
		list.add(new IDropDownBean(4, "分矿分厂"));
		list.add(new IDropDownBean(5, "棋盘表"));
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(list));
		return;
	}

	// 得到系统信息表中配置的报表标题的单位名称
	public String getBiaotmc() {
		String biaotmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc = "select  zhi from xitxxb where mingc='报表标题单位名称'";
		ResultSet rs = cn.getResultSet(sql_biaotmc);
		try {
			while (rs.next()) {
				biaotmc = rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return biaotmc;

	}

	// 得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
	}

	// 分公司下拉框
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
		setDiancxxModel(new IDropDownModel(sql, "中国大唐集团"));
	}

	//
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
