package com.zhiren.jt.zdt.rulgl.rucrlrzc_jb_new;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Rucrlrzc_jb_newreport extends BasePage {
	
	private String diancmc="";
	private String diancxxb_id="";
	private String beginriq="";
	private String endriq="";
	
	public boolean getRaw() {
		return true;
	}

	public void submit(IRequestCycle cycle) {
		
	}
	
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
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return biaotmc;
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
		return getPrintData();
	}

	public Date getBeginriqDate() {
		if (((Visit)getPage().getVisit()).getDate1()==null){
			((Visit)getPage().getVisit()).setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate1();
	}
	public Date getEndriqDate() {
		if (((Visit)getPage().getVisit()).getDate2()==null){
			((Visit)getPage().getVisit()).setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate2();
	}
	
	
	
	private String getPrintData(){
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String strGongsID="";
		String danw="";
		String table="";
		String where="";
		String groupby = "";
		String notHuiz="";
		String ordeby ="";
		int jib=getDiancTreeJib(diancxxb_id);
		if(diancmc.equals("")){
			jib=1;
		}
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";

		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and (dc.fuid=  " +diancxxb_id+"or dc.shangjgsid="+diancxxb_id+") ";
			notHuiz=" and not grouping(gs.mingc)=1 ";//当电厂树是分公司时,去掉集团汇总
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +diancxxb_id;
			notHuiz=" and not  grouping(dc.mingc)=1";//当电厂树是电厂时,去掉分公司和集团汇总
		}else if (jib==-1){
			strGongsID=" and dc.id = "+diancxxb_id;
		}
		

		JDBCcon cn = new JDBCcon();
		if (jib==2){
			String ranlgs = "select id from diancxxb where shangjgsid= "+diancxxb_id;

			try{
				ResultSet rl = cn.getResultSet(ranlgs);
				if(rl.next()){
					danw="select decode(grouping(vdc.rlgsmc)+grouping(vdc.fgsmc)+grouping(vdc.mingc),3,'总计',2,vdc.rlgsmc,1,vdc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||vdc.mingc) as danw,\n";
					table=",vwdianc vdc ";
					where="and dc.id=vdc.id";
					groupby ="group by rollup (fx.fenx,vdc.rlgsmc,vdc.fgsmc,vdc.mingc)\n";
					notHuiz =" and not grouping(vdc.rlgsmc)=1 ";
					ordeby="order by  grouping(vdc.rlgsmc) desc,vdc.rlgsmc,grouping(vdc.fgsmc) desc,vdc.fgsmc,grouping(vdc.mingc) desc ,vdc.mingc,grouping(fx.fenx) desc ,fx.fenx\n";
				}else{
					danw="select decode(grouping(gs.mingc)+grouping(dc.mingc),2,'总计',1,gs.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danw,\n";
					table="";
					where="";
					groupby ="group by rollup (fx.fenx,gs.mingc,dc.mingc)\n";
					ordeby="order by  grouping(gs.mingc) desc,gs.mingc,grouping(dc.mingc) desc ,dc.mingc,grouping(fx.fenx) desc ,fx.fenx\n";
				}
				rl.close();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cn.Close();
			}
		}else{
			danw="select decode(grouping(gs.mingc)+grouping(dc.mingc),2,'总计',1,gs.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danw,\n";
			table="";
			where="";
			groupby ="group by rollup (fx.fenx,gs.mingc,dc.mingc)\n";
			ordeby="order by  grouping(gs.mingc) desc,gs.mingc,grouping(dc.mingc) desc ,dc.mingc,grouping(fx.fenx) desc ,fx.fenx\n";
		}

		 
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select l.danw,l.fenx,l.rc_laimsl,rc_laimzl,l.rc_farl,l.rl_jingz,rl_rulmzl,l.rl_farl, \n");
		sbsql.append("       (l.rc_farl-l.rl_farl) as tiaozq_mj,Round((l.rc_farl-l.rl_farl)*1000/4.1816,0) as tiaozq_dk \n");
		sbsql.append("from("+ danw +"fx.fenx, \n");
		sbsql.append("       Round(sum(rc.laimsl),0) as rc_laimsl,sum(rc.laimzl) as rc_laimzl, \n");
		sbsql.append("       decode(sum(rc.laimzl),0,0,Round(sum(rc.farl*rc.laimzl)/sum(rc.laimzl),2)) as rc_farl, \n");
		sbsql.append("       Round(sum(rl.rulml),0) as rl_jingz,sum(rulmzl) as rl_rulmzl, \n");
		sbsql.append("       decode(sum(rl.rulmzl),0,0,Round(sum(rl.farl*rl.rulmzl)/sum(rl.rulmzl),2)) as rl_farl \n");
		sbsql.append("from  (select diancxxb_id,fx.fenx,fx.xuh from \n");
		sbsql.append("(select distinct f.diancxxb_id from fahb f  where f.daohrq>=to_date('"+beginriq+"','yyyy-mm-dd') and  f.daohrq<=to_date('"+endriq+"','yyyy-mm-dd')   \n");
		sbsql.append("        union      \n");
		sbsql.append("        select distinct hy.diancxxb_id from meihyb hy  where hy.rulrq>=to_date('"+beginriq+"','yyyy-mm-dd') and hy.rulrq<=to_date('"+endriq+"','yyyy-mm-dd')  \n");
		sbsql.append(") dcid,(select decode(1,1,'当日') as fenx,1 as xuh  from dual union select decode(1,1,'累计')  as fenx,2 as xhu from dual ) fx) fx, \n");
		sbsql.append("((select f.diancxxb_id,decode(1,1,'当日') as fenx,sum(f.laimsl) as laimsl,sum(decode(nvl(z.qnet_ar,0),0,0,f.laimzl)) as laimzl, \n");
		sbsql.append("      decode(sum(decode(nvl(z.qnet_ar,0),0,0,f.laimzl)),0,0,sum(round(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")*f.laimzl)/sum(decode(nvl(z.qnet_ar,0),0,0,f.laimzl))) as farl \n");
		sbsql.append("from fahb f,zhilb z \n");
		sbsql.append("where f.zhilb_id=z.id(+) \n");
		sbsql.append(" and f.daohrq=to_date('"+endriq+"','yyyy-mm-dd') \n");
		sbsql.append("group  by f.diancxxb_id) \n");
		sbsql.append("union \n");
		sbsql.append("(select f.diancxxb_id,decode(1,1,'累计') as fenx,sum(f.laimsl) as laimsl,sum(decode(nvl(z.qnet_ar,0),0,0,f.laimzl)) as laimzl, \n");
		sbsql.append("       decode(sum(decode(nvl(z.qnet_ar,0),0,0,f.laimzl)),0,0,sum(round_new(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")*f.laimzl)/sum(decode(nvl(z.qnet_ar,0),0,0,f.laimzl))) as farl \n");
		sbsql.append("from fahb f,zhilb z \n");
		sbsql.append("where f.zhilb_id=z.id(+) \n");
		sbsql.append("and f.daohrq>=to_date('"+beginriq+"','yyyy-mm-dd') \n");
		sbsql.append("and f.daohrq<=to_date('"+endriq+"','yyyy-mm-dd') \n");
		sbsql.append("group  by f.diancxxb_id)) rc, \n");
		sbsql.append("((select hy.diancxxb_id,decode(1,1,'当日') as fenx,sum(hy.fadhy+hy.gongrhy) as rulml,sum(decode(nvl(mz.qnet_ar,0),0,0,hy.fadhy+hy.gongrhy)) as rulmzl, \n");
		sbsql.append("    decode(sum(decode(nvl(mz.qnet_ar,0),0,0,hy.fadhy+hy.gongrhy)),0,0,sum(round_new(mz.qnet_ar,"+visit.getFarldec()+")*(hy.fadhy+hy.gongrhy))/sum(decode(nvl(mz.qnet_ar,0),0,0,hy.fadhy+hy.gongrhy))) as farl \n");
		sbsql.append(" from rulmzlb mz,meihyb hy \n");
		sbsql.append("where hy.rulrq=to_date('"+endriq+"','yyyy-mm-dd') \n");
		sbsql.append("and hy.rulmzlb_id=mz.id(+) \n");
		sbsql.append("group by hy.diancxxb_id) \n");
		sbsql.append("union \n");
		sbsql.append("(select hy.diancxxb_id,decode(1,1,'累计') as fenx,sum(hy.fadhy+hy.gongrhy) as rulml,sum(decode(nvl(mz.qnet_ar,0),0,0,hy.fadhy+hy.gongrhy)) as rulmzl, \n");
		sbsql.append("   decode(sum(decode(nvl(mz.qnet_ar,0),0,0,hy.fadhy+hy.gongrhy)),0,0,sum(round_new(mz.qnet_ar,"+visit.getFarldec()+")*(hy.fadhy+hy.gongrhy))/sum(decode(nvl(mz.qnet_ar,0),0,0,hy.fadhy+hy.gongrhy))) as farl \n");
		sbsql.append(" from rulmzlb mz,meihyb hy \n");
		sbsql.append("where hy.rulrq>=to_date('"+beginriq+"','yyyy-mm-dd') \n");
		sbsql.append("and hy.rulrq<=to_date('"+endriq+"','yyyy-mm-dd') \n");
		sbsql.append("and hy.rulmzlb_id=mz.id(+) \n");
		sbsql.append("group by hy.diancxxb_id)) rl,diancxxb dc, vwfengs gs"+table+" \n");
		sbsql.append("where fx.diancxxb_id=rc.diancxxb_id(+) \n");
		sbsql.append("and   fx.diancxxb_id=rl.diancxxb_id(+) \n");
		sbsql.append("and   fx.diancxxb_id=dc.id \n");
		sbsql.append("and   fx.fenx=rc.fenx(+) \n");
		sbsql.append("and   fx.fenx=rl.fenx(+) \n");
		sbsql.append("and   dc.fuid=gs.id "+where+" "+strGongsID+" \n");
		sbsql.append(""+groupby+" \n");
		sbsql.append("having not grouping (fx.fenx)=1 "+notHuiz+" \n");
		sbsql.append( ""+ordeby+" ) l \n");
			
			ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Report rt = new Report();
			
	    	String ArrHeader[][]=new String[2][10];
			ArrHeader[0]=new String[] {"单位","当日<br>累计","入厂煤质","入厂煤质","入厂煤质","入炉煤质","入炉煤质","入炉煤质","热值差","热值差"};
			ArrHeader[1]=new String[] {"单位","当日<br>累计","实收量<br>(吨)","检质量<br>(吨)","热值<br>(MJ/kg)","入炉煤量<br>(吨)","检质量<br>(吨)","热值<br>(MJ/kg)","(MJ/kg)","(kcal/kg)"};
			int ArrWidth[]=new int[] {150,40,70,70,70,60,70,70,70,60};
			String arrFormat[]=new String[]{"","","0","0","0.00","0","0","0.00","0.00","0"};
			
			Table bt=new Table(rs,2,0,2);
			rt.setBody(bt);
			bt.setColAlign(2, Table.ALIGN_CENTER);
			
			rt.body.setPageRows(22);
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.body.mergeFixedRow();//合并行
			rt.body.mergeFixedCols();//和并列
			rt.setTitle("入厂入炉热值差", ArrWidth);

			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			
						
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 3, "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);

			// 设置页数
			_CurrentPage = 1;
			_AllPages = rt.body.getPages();
			if (_AllPages == 0) {
				_CurrentPage = 0;
			}
			con.Close();
			return rt.getAllPagesHtml();

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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}

		blnIsBegin = true;
		
		//电厂ID
		if(cycle.getRequestContext().getParameters("diancxxb_id") !=null) {
			visit.setString10((cycle.getRequestContext().getParameters("diancxxb_id")[0]));
			diancxxb_id = visit.getString10();
        }else{
        	if(!visit.getString1().equals("")) {
        		diancxxb_id = visit.getString10();
            }
        }
		//开始日期
		if(cycle.getRequestContext().getParameters("beginriq") !=null) {
			visit.setString11((cycle.getRequestContext().getParameters("beginriq")[0]));
			beginriq = visit.getString11();
        }else{
        	if(!visit.getString1().equals("")) {
        		beginriq = visit.getString11();
            }
        }
		//截止日期
		if(cycle.getRequestContext().getParameters("endriq") !=null) {
			visit.setString12((cycle.getRequestContext().getParameters("endriq")[0]));
			endriq = visit.getString12();
        }else{
        	if(!visit.getString1().equals("")) {
        		endriq = visit.getString12();
            }
        }
//		数据类型
		if(cycle.getRequestContext().getParameters("danwmc") !=null) {
			visit.setString9((cycle.getRequestContext().getParameters("danwmc")[0]));
			diancmc = visit.getString9();
        }else{
        	if(!visit.getString1().equals("")) {
        		diancmc = visit.getString9();
            }
        }
		
		}
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib(String DiancTreeJib) {
		JDBCcon con = new JDBCcon();
		int jib = -1;
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
}