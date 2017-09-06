package com.zhiren.jt.zdt.jiesgl.jiesdb;


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

public class Jiesdbgybreport extends BasePage {
	
	private String gongysid="";
	private String diancxxb_id="";
	private String beginriq="";
	private String endriq="";
	private String jihkjbid="";
	private String yunsfsid="";
	private String riqlx="";
	
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
	
	
	private String getPrintData(){
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String sqlJib = "select d.jib from diancxxb d where d.id="+ diancxxb_id;
		ResultSet rs1 = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs1.next()) {
				jib = rs1.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}
		String strGongsID = "";
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = " and (dc.fuid=  " +diancxxb_id+"or dc.shangjgsid="+diancxxb_id+")";
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +diancxxb_id;

		}
		
		
		String strCondition="";
		
		if (yunsfsid.equals("")||yunsfsid.equals(null)){
			strCondition =" and ys.id=" +yunsfsid ;//选择运输方式
		}
		
		if (jihkjbid.equals("")||jihkjbid.equals(null)){
			strCondition=strCondition+" and jh.id=" +jihkjbid;
		}
		StringBuffer sbsql = new StringBuffer("");
		if(riqlx.equals("0")){
			sbsql.append("select mingc,jiessl,rucsl,jiessl-rucsl as meilc, \n");
			sbsql.append("     round(rez_js*4.1816/1000,2) as rezhi_js,rez_js as rez_jsdk,round(rez_rc*4.1816/1000,2) as rezhi_rc,rez_rc as rez_rcdk,  \n");
			sbsql.append("     round(rez_js*4.1816/1000,2)-round(rez_rc*4.1816/1000,2) as rezc, rez_js-rez_rc as  rezcdk,   \n");
			sbsql.append("     round(decode(rez_js,0,0,(jiesbmdj-js_shuik-yf_shuik)*7000/rez_js),2) as jiesbmdj_b,  \n");
			sbsql.append("     round(decode(rez_rc,0,0,(rucbmdj-js_shuik-yf_shuik)*7000/rez_rc),2)  as rucbmdj_b, \n");
			sbsql.append("     round(decode(rez_js,0,0,(jiesbmdj-js_shuik-yf_shuik)*7000/rez_js),2)-round(decode(rez_rc,0,0,(rucbmdj-js_shuik-yf_shuik)*7000/rez_rc),2) as biaomdjc   \n");
			sbsql.append(" from ( \n");
			sbsql.append("select decode(grouping(gy.mingc),1,'合计','&nbsp;&nbsp;'||gy.mingc) as mingc \n");
			sbsql.append("       ,sum(js.jiessl) as jiessl,sum(cf.rucsl) as rucsl  \n");
			sbsql.append("       ,decode(sum(decode(rz.jiesrz,'',0,js.jiessl)),0,0,round_new(sum(js.jiessl*nvl(rz.jiesrz,0))/sum(decode(rz.jiesrz,'',0,js.jiessl)),0)) as rez_js  \n");
			sbsql.append("       ,decode(sum(cf.rucsl),0,0,round_new(sum(cf.rucsl*nvl(rz.rucrz,0))/sum(cf.rucsl),0)) as rez_rc  \n");
			sbsql.append("       ,decode(sum(js.jiessl),0,0,round(sum( js.hansmk+nvl(js.bukmk,0)+nvl(yf.guotyf,0)+nvl(yf.dityf,0)+nvl(yf.qiyf,0)+nvl(yf.bukyf,0)-nvl(yf.guotyfjf,0) +nvl(yf.guotzf,0)+nvl(yf.ditzf,0)-nvl(yf.guotzfjf,0))/sum(js.jiessl),2)) as jiesbmdj   \n");
			sbsql.append("       ,decode(sum(js.jiessl),0,0,round(sum(js.shuik)/sum(js.jiessl),2)) as js_shuik,decode(sum(js.jiessl),0,0,round(sum(yf.shuik)/sum(js.jiessl),2)) as yf_shuik  \n");
			sbsql.append("       ,decode(sum(cf.rucsl),0,0,round(sum( js.hansmk+nvl(js.bukmk,0)+nvl(yf.guotyf,0)+nvl(yf.dityf,0)+nvl(yf.qiyf,0)+nvl(yf.bukyf,0)-nvl(yf.guotyfjf,0) +nvl(yf.guotzf,0)+nvl(yf.ditzf,0)-nvl(yf.guotzfjf,0))/sum(cf.rucsl),2)) as rucbmdj    \n");
			sbsql.append("       --,decode(sum(js.jiessl),0,0,round(sum(js.hansmk+yf.hansyf-js.shuik-yf.shuik)/sum(js.jiessl),2)) as jiesbmdj \n");
			sbsql.append("       --,decode(sum(js.jiessl),0,0,round(sum(js.hansmk+yf.hansyf-js.shuik-yf.shuik)/sum(js.jiessl),2)) as rucbmdj \n");
			sbsql.append(" from jiesb js,jiesyfb yf,vwdianc dc,vwgongys gy ,yunsfsb ys,jihkjb jh, \n");
			sbsql.append("       (select jszb.jiesdid as id ,jszb.changf as rucsl from jieszbsjb jszb,zhibb zb,jiesb jsb \n");
			sbsql.append("               where jszb.zhibb_id = zb.id and zb.bianm='数量' and jszb.jiesdid= jsb.id \n");
			sbsql.append("                     and jsb.jiesrq >=to_date('"+beginriq+"','yyyy-mm-dd')and jsb.jiesrq<=to_date('"+endriq+"','yyyy-mm-dd'))cf, \n");
			sbsql.append("       (select jszb.jiesdid as id ,jszb.jies as jiesrz,jszb.changf as rucrz from jieszbsjb jszb,zhibb zb,jiesb jsb \n");
			sbsql.append("               where jszb.zhibb_id = zb.id and zb.bianm='收到基低位热值Qnetar(MJ/Kg)' and jszb.jiesdid= jsb.id \n");
			sbsql.append("                     and jsb.jiesrq >=to_date('"+beginriq+"','yyyy-mm-dd')and jsb.jiesrq<=to_date('"+endriq+"','yyyy-mm-dd'))rz \n");
			sbsql.append("  where js.diancxxb_id=dc.id    "+strGongsID+"  and js.gongysb_id=gy.id   and gy.dqid="+gongysid+" \n");
			sbsql.append("        and yf.diancjsmkb_id=js.id  and js.id = cf.id(+) and js.id = rz.id(+) and js.yunsfsb_id=ys.id and js.jihkjb_id=jh.id(+) \n");
			sbsql.append("        and js.jiesrq >=to_date('"+beginriq+"','yyyy-mm-dd')and js.jiesrq<=to_date('"+endriq+"','yyyy-mm-dd') \n");
			sbsql.append("      "+strCondition+"  \n");
			sbsql.append("group by rollup(gy.mingc) \n");
			sbsql.append("order by grouping(gy.mingc)desc,min(gy.xuh)  ) \n");
		}else{
			sbsql.append("select mingc,jiessl,rucsl,jiessl-rucsl as meilc, \n");
			sbsql.append(" round(rez_js*4.1816/1000,2) as rezhi_js,rez_js as rez_jsdk,round(rez_rc*4.1816/1000,2) as rezhi_rc,rez_rc as rez_rcdk,  \n");
			sbsql.append("       round(rez_js*4.1816/1000,2)-round(rez_rc*4.1816/1000,2) as rezc, rez_js-rez_rc as  rezcdk,   \n");
			sbsql.append("       round(decode(rez_js,0,0,(jiesbmdj-js_shuik-yf_shuik)*7000/rez_js),2) as jiesbmdj_b,  \n");
			sbsql.append("       round(decode(rez_rc,0,0,(rucbmdj-js_shuik-yf_shuik)*7000/rez_rc),2)  as rucbmdj_b, \n");
			sbsql.append("       round(decode(rez_js,0,0,(jiesbmdj-js_shuik-yf_shuik)*7000/rez_js),2)-round(decode(rez_rc,0,0,(rucbmdj-js_shuik-yf_shuik)*7000/rez_rc),2) as biaomdjc   \n");
			sbsql.append(" from (  \n");
			sbsql.append("select decode(grouping(gy.mingc),1,'合计','&nbsp;&nbsp;'||gy.mingc) as mingc \n");
			sbsql.append("        ,sum(cf.jiessl) as jiessl,sum(cf.rucsl) as rucsl    \n");
			sbsql.append("         ,decode(sum(decode(rz.jiesrz,'',0,cf.jiessl)),0,0,round_new(sum(cf.jiessl*nvl(rz.jiesrz,0))/sum(decode(rz.jiesrz,'',0,cf.jiessl)),0)) as rez_js     \n");
			sbsql.append("         ,decode(sum(cf.rucsl),0,0,round_new(sum(cf.rucsl*nvl(rz.rucrz,0))/sum(cf.rucsl),0)) as rez_rc    \n");
			sbsql.append("         --,decode(sum(cf.jiessl),0,0,round(sum( js.hansmk+nvl(js.bukmk,0)+nvl(yf.guotyf,0)+nvl(yf.dityf,0)+nvl(yf.qiyf,0)+nvl(yf.bukyf,0)-nvl(yf.guotyfjf,0) +nvl(yf.guotzf,0)+nvl(yf.ditzf,0)-nvl(yf.guotzfjf,0))/sum(cf.jiessl),2)) as jiesbmdj   \n");
			sbsql.append("      ,decode(sum(cf.jiessl),0,0,round(sum(js.zongdj*cf.jiessl )/sum(cf.jiessl),2)) as jiesbmdj   \n");
			sbsql.append("         ,decode(sum(cf.jiessl),0,0,round(sum(js.shuikdj_js*cf.jiessl)/sum(cf.jiessl),2)) as js_shuik \n");
			sbsql.append("         ,decode(sum(cf.jiessl),0,0,round(sum(shuikdj_yf*cf.jiessl)/sum(cf.jiessl),2)) as yf_shuik \n");
			sbsql.append("         --,decode(sum(cf.jiessl),0,0,round(sum( js.hansmk+nvl(js.bukmk,0)+nvl(yf.guotyf,0)+nvl(yf.dityf,0)+nvl(yf.qiyf,0)+nvl(yf.bukyf,0)-nvl(yf.guotyfjf,0) +nvl(yf.guotzf,0)+nvl(yf.ditzf,0)-nvl(yf.guotzfjf,0))/sum(cf.rucsl),2)) as rucbmdj    \n");
			sbsql.append("         ,decode(sum(cf.jiessl),0,0,round(sum(js.zongdj*cf.rucsl)/sum(cf.rucsl),2)) as rucbmdj    \n");
			sbsql.append("  from vwdianc dc,vwgongys gy ,yunsfsb ys,jihkjb jh, \n");
			sbsql.append("       (select js.id as id,case when js.ches=sm.ches then jszb.changf else sm.shisl  end as rucsl, \n");
			sbsql.append("      					 case when js.ches = sm.ches then js.jiessl else round_new(sm.shisl/jszb.changf*js.jiessl,0) end as jiessl \n");
			sbsql.append("        from  \n");
			sbsql.append("        (select jiesb_id,sum(shisl) as shisl,sum(ches) as ches \n");
			sbsql.append("                   from (select lieid, jiesb_id,sum(ches) as ches, \n");
			sbsql.append("                                round_new (sum(biaoz),0)+round_new (sum(yingk),0)+round_new(sum(yingk),0) shisl \n");
			sbsql.append("                          from fahb  fh \n");
			sbsql.append("                          where fh.daohrq>=to_date('"+beginriq+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+endriq+"','yyyy-mm-dd') \n");
			sbsql.append("                          group by lieid,jiesb_id) \n");
			sbsql.append("                      group by jiesb_id \n");
			sbsql.append("                ) sm,jiesb js,jieszbsjb jszb,zhibb zb \n");
			sbsql.append("         where sm.jiesb_id=js.id and jszb.jiesdid=js.id and jszb.zhibb_id=zb.id  \n");
			sbsql.append("               and zb.bianm='数量' \n");
			sbsql.append("        )cf,   \n");
			sbsql.append("      ( select js.id as id,jszb.jies as jiesrz,jszb.changf as rucrz \n");
			sbsql.append("        from  \n");
			sbsql.append("        (select jiesb_id,sum(ches) as ches \n");
			sbsql.append("           from (select lieid, jiesb_id,sum(ches) as ches \n");
			sbsql.append("                 from fahb  fh \n");
			sbsql.append("                 where fh.daohrq>=to_date('"+beginriq+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+endriq+"','yyyy-mm-dd') \n");
			sbsql.append("                 group by lieid,jiesb_id) \n");
			sbsql.append("              group by jiesb_id \n");
			sbsql.append("        ) sm,jiesb js,jieszbsjb jszb,zhibb zb \n");
			sbsql.append("        where sm.jiesb_id=js.id and jszb.jiesdid=js.id and jszb.zhibb_id=zb.id  \n");
			sbsql.append("       and zb.bianm='收到基低位热值Qnetar(MJ/Kg)'  \n");
			sbsql.append("     )rz , \n");
			sbsql.append("     (select  js.id ,js.diancxxb_id as diancxxb_id,js.gongysb_id as gongysb_id,js.yunsfsb_id as yunsfsb_id,js.jihkjb_id as jihkjb_id,js.shoukdw as shoukdw,js.jiessl, \n");
			sbsql.append("          (js.hansmk/js.jiessl+js.bukmk/js.jiessl+yf.guotyf/js.jiessl+yf.dityf/js.jiessl+yf.qiyf/js.jiessl \n");
			sbsql.append("          +yf.bukyf/js.jiessl-yf.guotyfjf/js.jiessl+yf.guotzf/js.jiessl+yf.ditzf/js.jiessl-yf.guotzfjf/js.jiessl) as zongdj, \n");
			sbsql.append("          (js.shuik/js.jiessl) as shuikdj_js,(yf.shuik/js.jiessl) as shuikdj_yf  \n");
			sbsql.append("       from jiesb js,jiesyfb yf, \n");
			sbsql.append("          (select jiesb_id,sum(ches) as ches \n");
			sbsql.append("           from (select lieid, jiesb_id,sum(ches) as ches \n");
			sbsql.append("                 from fahb  fh \n");
			sbsql.append("                 where fh.daohrq>=to_date('"+beginriq+"','yyyy-mm-dd') and fh.daohrq<=to_date('"+endriq+"','yyyy-mm-dd') \n");
			sbsql.append("                 group by lieid,jiesb_id) \n");
			sbsql.append("              group by jiesb_id \n");
			sbsql.append("          ) sm \n");
			sbsql.append("       where yf.diancjsmkb_id=js.id  and  sm.jiesb_id=js.id  \n");
			sbsql.append("    )js \n");
			sbsql.append(" where js.diancxxb_id=dc.id  "+strGongsID+"  and js.gongysb_id=gy.id and js.yunsfsb_id=ys.id  and gy.dqid="+gongysid+"  \n");
			sbsql.append("        and  js.jihkjb_id=jh.id and js.id = cf.id(+) and js.id = rz.id(+) \n");
			sbsql.append("      "+strCondition+" \n");
			sbsql.append("group by rollup(gy.mingc) \n");
			sbsql.append("order by grouping(gy.mingc)desc,min(gy.xuh)  ) \n");
		}	
		
		
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		ArrHeader=new String[2][13];
	    ArrHeader[0]=new String[] {"单位","结算煤量<br>(吨)","入厂煤量<br>(吨)","煤量差<br>(吨)","结算热值","结算热值","入厂热值","入厂热值","热值差","热值差","结算不含税标煤单价<br>(元/吨)","入厂不含税标煤单价<br>(元/吨)","标煤单价差<br>(元/吨)"};
	    ArrHeader[1]=new String[] {"单位","结算煤量<br>(吨)","入厂煤量<br>(吨)","煤量差<br>(吨)","(MJ/kg)","(Kcal/kg)","(MJ/kg)","(Kcal/kg)","(MJ/kg)","(Kcal/kg)","结算不含税标煤单价<br>(元/吨)","入厂不含税标煤单价<br>(元/吨)","标煤单价差<br>(元/吨)"};
     	  	
	    ArrWidth =new int[] {150,70,70,70,70,70,70,70,70,70,70,70,70};

		Table bt=new Table(rs, 2, 0, 1);
		rt.setBody(bt);
		bt.setColAlign(2, Table.ALIGN_CENTER);
		
		rt.body.setPageRows(22);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRow();//合并行
		rt.body.mergeFixedCols();//和并列
		rt.setTitle("燃料结算对比表", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 2, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);

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
		//数据类型
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString9((cycle.getRequestContext().getParameters("lx")[0]));
			gongysid = visit.getString9();
        }else{
        	if(!visit.getString1().equals("")) {
        		gongysid = visit.getString9();
            }
        }
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
		//结束日期
		if(cycle.getRequestContext().getParameters("endriq") !=null) {
			visit.setString12((cycle.getRequestContext().getParameters("endriq")[0]));
			endriq = visit.getString12();
        }else{
        	if(!visit.getString1().equals("")) {
        		endriq = visit.getString12();
            }
        }
		//计划口径id
		if(cycle.getRequestContext().getParameters("jihkjbid") !=null) {
			visit.setString13((cycle.getRequestContext().getParameters("jihkjbid")[0]));
			jihkjbid = visit.getString13();
        }else{
        	if(!visit.getString1().equals("")) {
        		jihkjbid = visit.getString13();
            }
        }
		//运输方式id
		if(cycle.getRequestContext().getParameters("yunsfsid") !=null) {
			visit.setString14((cycle.getRequestContext().getParameters("yunsfsid")[0]));
			yunsfsid = visit.getString14();
        }else{
        	if(!visit.getString1().equals("")) {
        		yunsfsid = visit.getString14();
            }
        }
//		查询时间类型
		if(cycle.getRequestContext().getParameters("riqlx") !=null) {
			visit.setString15((cycle.getRequestContext().getParameters("riqlx")[0]));
			riqlx = visit.getString15();
        }else{
        	if(!visit.getString1().equals("")) {
        		riqlx = visit.getString15();
            }
        }
	}

}