package com.zhiren.jt.jiesgl.report.changfhs;

import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;

import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.Locale;
import com.zhiren.common.CustomMaths;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;

/*
 * 修改人：    王伟
 * 修改时间： 2009-10-31
 * 修改内容： 添加判断不同集团报表显示的不同的表头名称，而不是固定的中国大唐
 */
/*
 * 作者：陈泽天
 * 时间：2010-01-26 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */

/*  修改人：   王伟
 *  修改时间： 2010-06-29
 *  修改内容： 1、热值小数位取系统配置,默认值为2
 *  		2、硫指标系统信息表配置
 *  
 */
/*  修改人：   赵胜男
 *  修改时间： 2011-11-26
 *  修改内容：
 *  		修正页面bug
 *  
 */
/*  作者：夏峥
 *  时间： 2013-01-09
 *  内容： 修改获取硫份名称的方法。
 */
/*  作者：夏峥
 *  时间： 2013-03-29
 *  内容： 在xitxxb中添加参数设置，验收明细中显示灰的指标
 */
/*  修改人：   赵胜男
*  修改时间： 2013-7-30
*  修改内容：去掉大唐字样的判断 
*/
/*  作者：夏峥
 *  时间： 2013-10-10
 *  内容： 增加青铝验收明细特殊配置
 *  	 MainGlobal.getXitxx_item(con, "结算明细", "青铝结算明细特殊设置", "0", "否")
 */
/*
 * 作者：夏峥
 * 时间：2013-12-06
 * 描述：修正酒泉结算显示BUG
 */
/*
 * 作者:lil
 * 时间:2014-05-20
 * 修改内容:修改青铝电厂燃料验收单报表表头内容，改为“燃料验收单”
 */
/*
 * 作者：夏峥
 * 时间：2015-03-23
 * 描述：修正青铝结算明细加权显示BUG
 */
public class Yansmxreport extends BasePage {
	
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

	public String getDiancQuanc() {
		return getDiancQuanc(getDiancId());
	}
	
	public long getDiancId() {
		/*
		 * if (isGongsUser()){ return getDiancmcValue().getId(); }else{ return
		 * ((Visit)getPage().getVisit()).getDiancxxbId(); }
		 */
		return getDiancId();
	}

	// 得到单位全称
	public String getDiancQuanc(long diancxxbID) {
		String _DiancQuanc = "";
		JDBCcon cn = new JDBCcon();

		try {
			ResultSet rs = cn
					.getResultSet(" select quanc from diancxxb where id="
							+ diancxxbID);
			while (rs.next()) {
				_DiancQuanc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cn.Close();
		return _DiancQuanc;
	}

	public String getPrintTable() {
		
		return getYansmx(); 
	}

	public long getJiesbid(String Type,long id){
		
		long jiesbid = 0;
		String sql = "";
		if(Type.equals("dianc")){
			jiesbid=id;

		}else if(Type.equals("changf")){
			sql = "select j.id\n" +
				  "from jiesb j,diancjsmkb d\n" + 
				  "where j.diancjsmkb_id = d.id\n" + 
				  "      and d.id ="+id;

		}else if(Type.equals("kuangf")){
			sql = "select j.id\n" +
			  	  "from jiesb j,kuangfjsmkb m\n" + 
				  "where j.diancjsmkb_id = m.diancjsmkb_id\n" + 
				  "      and m.id ="+id;

		}else if(Type.equals("fengscg")){
			jiesbid=id;
		}
		if(!sql.equals("")){
			
			JDBCcon con = new JDBCcon();
			ResultSetList rsl=con.getResultSetList(sql);
			if(rsl.next()){
				jiesbid = rsl.getLong("id");
			}
			rsl.close();
			con.Close();
		}
		
		return jiesbid;
	}
	
	private String getStmc(String strSt) {
		if ("Std".equals(strSt)) {
			return "St,d";
		} else if ("Star".equals(strSt)) {
			return "St,ar";
		} else if ("Stad".equals(strSt)) {
			return "St,ad";
		}
		return strSt;
	}
	private String getAmc(String strA) {
		if ("Ad".equals(strA)) {
			return "A,d";
		} else if ("Aar".equals(strA)) {
			return "A,ar";
		} else if ("Aad".equals(strA)) {
			return "A,ad";
		}
		return strA;
	}
	
	private String getVmc(String strV) {
		if ("Vad".equals(strV)) {
			return "V,ad";
		} else if ("Var".equals(strV)) {
			return "V,ar";
		} else if ("Vdaf".equals(strV)) {
			return "V,daf";
		}
		return strV;
	}
	
	private String getYansmx() {
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		StringBuffer talbe=new StringBuffer();	//报表输出
		long Jiesb_id=0;
		String Diancqc="";
		String Meikdwmc="";
		String Diancxxb_id="0";
		String Gongysb_id="0";
		String Hetb_id="0";
		String Jiesrq="";
		
		String sql = "";
		String tiaoj = "diancjsmkb_id";
		String tiaoj2 = "";
		String tiaoj3 = "";
		String danw = "元/千卡(元/吨,元/兆焦)";
		
		String table1 = "";
		String table2 = ""; 
		
		String table1_cg = "";
		String table2_cg = "";
		String tmp = "否";	//判断系统信息中
		
//		表名
		String biaom = ((Visit)getPage().getVisit()).getString5().substring(0, ((Visit)getPage().getVisit()).getString5().indexOf(";"));
		table1 = biaom.substring(0,biaom.indexOf(","));
		table2 = biaom.substring(biaom.indexOf(",")+1);
//		编码+类型
		String xinx = ((Visit)getPage().getVisit()).getString5().substring(((Visit)getPage().getVisit()).getString5().indexOf(";")+1);
		String bianm = xinx.substring(0,xinx.indexOf(","));
		String Type = xinx.substring(xinx.indexOf(",")+1);
		sql="select * from "+table1+" where bianm='"+bianm+"'"; 
		ResultSet res = con.getResultSet(sql);
		try {
		if(res.next()){//煤款结算明细
				Diancxxb_id = MainGlobal.getTableCol(table1, "diancxxb_id", "bianm", bianm);
				tmp = MainGlobal.getXitxx_item("结算", Locale.yansmxxsmk, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), tmp);
				
				if(tmp.equals("是")){
					
					sql="select d.quanc,j.id,j.meikdwmc,j.bianm,j.diancxxb_id,j.gongysb_id,j.hetb_id,to_char(j.jiesrq,'yyyy-MM-dd') as jiesrq from " +table1+ " j,diancxxb d where j.diancxxb_id=d.id and j.bianm='"+bianm+"'";
				}else{
					
					sql="select d.quanc,j.id,j.gongysmc as meikdwmc,j.bianm,j.diancxxb_id,j.gongysb_id,j.hetb_id,to_char(j.jiesrq,'yyyy-MM-dd') as jiesrq from " +table1+ " j,diancxxb d where j.diancxxb_id=d.id and j.bianm='"+bianm+"'";
				}
				
				sbsql.append(sql);
				ResultSetList rsl=con.getResultSetList(sbsql.toString());
				if(rsl.next()){
					
					Jiesb_id=rsl.getLong("id");
					Diancqc=rsl.getString("quanc");
					Meikdwmc=rsl.getString("meikdwmc");
					Diancxxb_id=rsl.getString("diancxxb_id");
					Gongysb_id=rsl.getString("gongysb_id");
					Hetb_id=rsl.getString("hetb_id");
					Jiesrq=rsl.getString("jiesrq");
				}
				
				//单位
//		sql = "select max(mingc) as mingc from(\n" +
//				"select j.bianm,d.mingc\n" + 
//				"from kuangfjsmkb j,hetjgb h,danwb d\n" + 
//				"where j.hetb_id = h.hetb_id\n" + 
//				"      and h.jijdwid = d.id\n" + 
//				"union\n" + 
//				"select j.bianm,d.mingc\n" + 
//				"from kuangfjsyfb j,hetjgb h,danwb d\n" + 
//				"where j.hetb_id = h.hetb_id\n" + 
//				"      and h.jijdwid = d.id\n" + 
//				")\n" + 
//			  "where bianm ='"+bianm+"'\n";		
//		ResultSetList rsl_dw=con.getResultSetList(sql);	
//		while(rsl_dw.next()){
//			danw = rsl_dw.getString("mingc");
//		}
//		所用变量的定义
				String jies_Jqsl="jingz";								//结算加权数量
//				String jies_Qnetarblxs="2";
				//热值小数位取系统配置默认值为2
				String jies_Qnetarblxs = "" + ((Visit)getPage().getVisit()).getFarldec();
				String jies_Stdblxs="2";
				String jies_Mtblxs="1";
				String jies_Madblxs="2";
				String jies_Aarblxs="2";
				String jies_Aadblxs="2";
				String jies_Adblxs="2";
				String jies_Vadblxs="2";
				String jies_Vdafblxs="2";
				String jies_Stadblxs="2";
				String jies_Starblxs="2";
				String jies_Hadblxs="2";
				String jies_Qbadblxs="2";
				String jies_Qgradblxs="2";
				String jies_T2blxs="2";
				
//		得到系统信息中设置的值
				String XitxxArrar[][]=null;	
				XitxxArrar=MainGlobal.getXitxx_items("结算",	"select mingc from xitxxb where leib='结算'"
						,String.valueOf(Diancxxb_id));
				
//		分析取得的值，然后对变量进行赋值
				if(XitxxArrar!=null){
					
					for(int i=0;i<XitxxArrar.length;i++){
						
						if(XitxxArrar[i][0]!=null){
							
							if(XitxxArrar[i][0].trim().equals(Locale.jiaqsl_xitxx)){
//						加权数量
								jies_Jqsl=XitxxArrar[i][1].trim();
							}
						}
					}
				}
				
//		取结算设置中的配置值
				if(Jiesdcz.getJiessz_items(Long.parseLong(Diancxxb_id),Long.parseLong(Gongysb_id),Long.parseLong(Hetb_id))!=null){
					
					String JiesszArray[][]=null;
					
					JiesszArray=Jiesdcz.getJiessz_items(Long.parseLong(Diancxxb_id),Long.parseLong(Gongysb_id),Long.parseLong(Hetb_id));
					
					for(int i=0;i<JiesszArray.length;i++){
						
						if(JiesszArray[i][0]!=null){
							
							if(JiesszArray[i][0].equals(Locale.jiesjqsl_jies)){
//						结算加权数量
								jies_Jqsl=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.mtblxsw_jies)){	//保留小数位开始
								
								jies_Mtblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.madblxsw_jies)){
								
								jies_Madblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.aarblxsw_jies)){
								
								jies_Aarblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.aadblxsw_jies)){
								
								jies_Aadblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.adblxsw_jies)){
								
								jies_Adblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.vadblxsw_jies)){
								
								jies_Vadblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.vdafblxsw_jies)){
								
								jies_Vdafblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.stadblxsw_jies)){
								
								jies_Stadblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.starblxsw_jies)){
								
								jies_Starblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.stdblxsw_jies)){
								
								jies_Stdblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.hadblxsw_jies)){
								
								jies_Hadblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.Qnetarblxsw_jies)){
								
								jies_Qnetarblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.Qbadblxsw_jies)){
								
								jies_Qbadblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.Qgradblxsw_jies)){	//保留小数位结束
								
								jies_Qgradblxs=JiesszArray[i][1];
							}
						}
					}
				}
				
				//ww 2010-07-06 在xitxxb中添加参数设置，验收明细中显示硫的指标
				String strSt = "Std";
				strSt = MainGlobal.getXitxx_item("结算", "验收明细硫", Diancxxb_id , "Std");
				//夏峥 2013-03-29 在xitxxb中添加参数设置，验收明细中显示灰的指标
				String strA = "Aar";
				strA = MainGlobal.getXitxx_item("结算", "验收明细灰", Diancxxb_id , "Aar");
				//赵胜男 2013-04-8 在xitxxb中添加参数设置，验收明细中显示挥发分的指标
				String strV = "Vad";
				strV = MainGlobal.getXitxx_item("结算", "验收明细挥发分", Diancxxb_id , "Vad");
				
				sbsql.setLength(0);
				if(Type.equals("dianc")){
					tiaoj = "diancjsmkb_id";
				}else if(Type.equals("changf")){
					tiaoj = "diancjsmkb_id";
				}else if(Type.equals("kuangf")){
					tiaoj = "kuangfjsmkb_id";
				}else if(Type.equals("fengscg")){
					tiaoj = "kuangfjsmkb_id"; 
				}
				
				String table_gj = "jiesb";	//结算关键表，该表是为了与发货表向关联的表
				
				if(Type.equals("fengscg")){
					
					table_gj = "kuangfjsmkb";
				}
				
				String m_Jiesb_id="";	//用于往下个页面传递jiesb_id
				long Jiesb_id_type=0;	//局部变量
				
				sbsql.append("select id from "+table1+" where fuid=").append(Jiesb_id);
				rsl=con.getResultSetList(sbsql.toString());
				ResultSet rs=null;
				if(rsl.getRows()>0){
					
//			有子结算单
					while(rsl.next()){
						
						Jiesb_id_type=getJiesbid(Type,rsl.getLong("id"));
						m_Jiesb_id+=Jiesb_id_type+",";
						
						sbsql.setLength(0);
						sbsql.append(
								"select decode(riq,'合计','合计',rownum) as xuh,\n" +
								"       riq,pinz,yunsfs,biaoz,jingz,yingk,yuns,zongkd,qnet_ar,round_new(qnet_ar*1000/4.1816,0) as qnet_ar_k,\n" + 
								"       aar,vdaf,mt,std,'' as beiz\n" + 
								"       from\n" + 
								"   (select f.lie_id as id,\n" + 
								"       decode(f.daohrq,null,'合计',to_char(f.daohrq,'yyyy-MM-dd')) as riq,\n" + 
								"       decode(p.mingc,null,'合计',p.mingc) as pinz,\n" + 
								"       decode(ysfs.mingc,null,'合计',ysfs.mingc) as yunsfs,\n" + 
								"       sum(f.biaoz) as biaoz,sum(round_new(f.jingz,2)) as jingz,sum(f.yingk) as yingk,sum(f.yuns) as yuns,\n" + 
								"       sum(f.zongkd) as zongkd,\n" + 
								"       round_new(sum(decode("+jies_Jqsl+",0,0,"+jies_Jqsl+"*z.qnet_ar))\n" + 
								"                     /sum(decode("+jies_Jqsl+",0,1,"+jies_Jqsl+")),"+jies_Qnetarblxs+") as qnet_ar,\n" + 
								"       round_new(sum(decode("+jies_Jqsl+",0,0,"+jies_Jqsl+"*z."+strA+"))\n" + 
								"                     /sum(decode("+jies_Jqsl+",0,1,"+jies_Jqsl+")),"+jies_Aarblxs+") as aar,\n" + 
								"       round_new(sum(decode("+jies_Jqsl+",0,0,"+jies_Jqsl+"*z."+strV+"))\n" + 
								"                     /sum(decode("+jies_Jqsl+",0,1,"+jies_Jqsl+")),"+jies_Vdafblxs+") as vdaf,\n" + 
								"       round_new(sum(decode("+jies_Jqsl+",0,0,"+jies_Jqsl+"*z.mt))\n" + 
								"                     /sum(decode("+jies_Jqsl+",0,1,"+jies_Jqsl+")),"+jies_Mtblxs+") as mt,\n" + 
								"       round_new(sum(decode("+jies_Jqsl+",0,0,"+jies_Jqsl+"*z." + strSt + "))\n" +   // 取参数配置中的硫
								"                     /sum(decode("+jies_Jqsl+",0,1,"+jies_Jqsl+")),"+jies_Stdblxs+") as std\n" + 
								"       from fahb f,zhilb z,pinzb p,yunsfsb ysfs,\n" + 
								"            (select id from "+table_gj+" where id="+Jiesb_id_type+") j\n" + 
								"       where f.zhilb_id=z.id\n" + 
								"             and f.pinzb_id=p.id\n" + 
								"             and f.jiesb_id=j.id\n" + 
								"             and f.yunsfsb_id = ysfs.id\n" + 
//					"             and z.liucztb_id=1\n" + 
								"       group by rollup(f.lie_id,f.daohrq,p.mingc,ysfs.mingc)\n" + 
								"       having not (grouping(ysfs.mingc)=1 and grouping(f.lie_id)=0)\n" + 
						"    )");
						rs=con.getResultSet(sbsql.toString());
						Report rt = new Report(); //报表定义
						String ArrHeader[][]=new String[3][16];
						ArrHeader[0]=new String[] {"序号","日期","品种","运输方式","数量验收","数量验收","数量验收","数量验收","数量验收","质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","备注"};
						ArrHeader[1]=new String[] {"序号","日期","品种","运输方式","矿发量","验收量","盈亏量","途损量","扣减量","Qnet,ar","Qnet,ar",getAmc(strA),getVmc(strV),"Mar",getStmc(strSt),"备注"};
						ArrHeader[2]=new String[] {"序号","日期","品种","运输方式","吨","吨","吨","吨","吨","MJ/kg","kcal/kg","%","%","%","%","备注"};
						int ArrWidth[]=new int[] {60,90,70,75,75,75,75,75,75,75,75,75,75,75,75,75};
						// 数据
//						int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
//						rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
						if(rsl.getRow()==0){
							
//					如果是子结算单第一行就加标题，否则不加标题
							Visit visit = (Visit) getPage().getVisit();
//							String type=
									MainGlobal.getXitxx_item("结算", "结算单所属单位", String.valueOf(visit.getDiancxxb_id()), "ZGDT");
//							if (type.equals("GD")) {
//								rt.setTitle("国电电力发展股份有限公司燃料验收单", ArrWidth);
								rt.setTitle("燃料入库验收单", ArrWidth);
//							} else {
//								rt.setTitle("中国大唐集团公司燃料验收单",ArrWidth);
//							}
//					rt.setTitle("中国大唐集团公司燃料验收单",ArrWidth);
							rt.setDefaultTitleLeft("供货单位:"+Meikdwmc+"<br>收货单位:"+Diancqc+"",12);
							rt.setDefaultTitleRight("结算单号:"+bianm+"<br>日期:"+Jiesrq, 4);
						}
						rt.setBody(new Table(rs, 3, 0, 0));
						rt.body.setWidth(ArrWidth);
//				rt.body.setPageRows(20);
						rt.body.setHeaderData(ArrHeader);// 表头数据
						rt.body.mergeCell(rt.body.getRows(), 1, rt.body.getRows(),4);
						rt.body.setCells(rt.body.getRows(), 1, rt.body.getRows(), 3, Table.PER_ALIGN, Table.ALIGN_CENTER);
						rt.body.mergeFixedRow();
						rt.body.ShowZero = false;
						if(rsl.getRow()!=0){
							
							rt.body.setRowCells(1, Table.PER_BORDER_TOP, 0);
						}
//				
						rt.body.setRowCells(rt.body.getRows(), Table.PER_BORDER_BOTTOM , 0);
						
//						rt.createFooter(1, ArrWidth);
//						rt.setDefautlFooter(2, 2, "数量经办人：", Table.ALIGN_LEFT);
//						rt.setDefautlFooter(5, 2, "数量负责人：", Table.ALIGN_LEFT);
//						rt.setDefautlFooter(8, 2, "质量经办人：", Table.ALIGN_LEFT);
//						rt.setDefautlFooter(11, 2, "质量负责人：", Table.ALIGN_LEFT);
//						rt.setDefautlFooter(14, 2, "数量质量复审员：", Table.ALIGN_LEFT);
//						rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
//						rt.footer.fontSize=10;
						
						//			构造表
						talbe.append(rt.getAllPagesHtml()+"\n");
						
						sbsql.setLength(0);
						
						if(table1.equals("diancjsmkb")){
							
							table1_cg = "kuangfjsmkb";
							table2_cg = "kuangfjsyfb";
							tiaoj2 = "diancjsmkb_id";
							tiaoj3 ="diancjsmkb_id";
						}else{
							
							table1_cg = table1;
							table2_cg = table2;
							tiaoj3 ="id";
							
							if(table1.equals("kuangfjsmkb")){
								
								tiaoj2 = "kuangfjsmkb_id";
							}else{
								
								tiaoj2 = "diancjsmkb_id";
							}
						}
						
//				汇总数据
						sbsql.append(
								"select * from\n" +
								"       (select\n" + 
								"              nvl(j.hetj,0) as farldj_jjq,\n" +
								"              nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.Qnetar_zhibb+"','zhejbz'),0) as qnetarzj_jjq,\n" +
								"              nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.Aar_zhibb+"','zhejbz'),0) as aarzj_jjq,\n" + 
								"              nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.Vdaf_zhibb+"','zhejbz'),0) as vdafzj_jjq,\n" + 
								"              nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.Yunju_zhibb+"','zhejbz'),0) as yunjzj_jjq,\n" + 
								"			   nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.Shul_zhibb+"','zhejbz'),0) as shulzbzj_jjq,\n" +
								"              round_new(nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.Qnetar_zhibb+"','jies'),0)*4.1816/1000," + jies_Qnetarblxs + ") as farlj_jjq,\n" + 
								"              nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.Qnetar_zhibb+"','jies'),0) as farlk_jjq,\n" + 
								"              nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.jiessl_zhibb+"','jies'),0) as jiessl_jjq,\n" + 
								"              nvl(j.hansdj,0) as hansdj_jjq,\n" + 
								"              nvl(j.hansmk,0) as hansmk_jjq,\n" + 
								"              nvl(j.yunj,'') as yunj_jjq,\n" + 
								"              nvl(j.Yunfhsdj,0) as hansdjyf_jjq,\n" + 
								"              nvl(j.hansyf,0) as hansyf_jjq,\n" + 
								"              nvl(j.hansmk,0)+nvl(j.hansyf,0) as zongje_jjq\n" + 
								"              from "+table1_cg+" j,"+table2_cg+" jy\n" + 
								"              where j.id=jy."+tiaoj+"(+)\n" + 
								"                    and j."+tiaoj3+"="+rsl.getLong("id")+"),\n" + 
								"\n" + 
								"       (select\n" + 
								"              nvl(j.hetj,0) as farldj,\n" +
								"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Qnetar_zhibb+"','zhejbz'),0) as qnetarzj,\n" +
								"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Aar_zhibb+"','zhejbz'),0) as aarzj,\n" + 
								"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Vdaf_zhibb+"','zhejbz'),0) as vdafzj,\n" + 
								"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Yunju_zhibb+"','zhejbz'),0) as yunjzj,\n" + 
								"			   nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Shul_zhibb+"','zhejbz'),0) as shulzbzj,\n" +
								"              round_new(nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Qnetar_zhibb+"','jies'),0)*4.1816/1000," + jies_Qnetarblxs + ") as farlj,\n" + 
								"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Qnetar_zhibb+"','jies'),0) as farlk,\n" + 
								"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.jiessl_zhibb+"','jies'),0) as jiessl,\n" + 
								"              nvl(j.hansdj,0) as hansdj,nvl(j.hansmk,0) as hansmk,nvl(j.yunj,'') as yunj,nvl(j.Yunfhsdj,0) as hansdjyf,\n" + 
								"              nvl(j.hansyf,0) as hansyf,(nvl(j.hansmk,0)+nvl(j.hansyf,0)) as zongje\n" + 
								"              from "+table1+" j,"+table2+" jy\n" + 
								"              where j.id=jy."+tiaoj+"(+)\n" + 
								"                    and j.id="+rsl.getLong("id")+")");
						
						rs=con.getResultSet(sbsql);
						String farldj="";
						String aarzj="";
						String vdafzj="";
						String yunjzj="";
						String shulzj="";
						String farlj="";
						String farlk="";
						String jiessl="";
						String hansdj="";
						String hansmk="";
						String yunj="";
						String hansdjyf="";
						String hansyf="";
						String zongje="";
						
						String farldj_jjq="";
						String aarzj_jjq="";
						String vdafzj_jjq="";
						String yunjzj_jjq="";
						String shulzj_jjq="";
						String farlj_jjq="";
						String farlk_jjq="";
						String jiessl_jjq="";
						String hansdj_jjq="";
						String hansmk_jjq="";
						String yunj_jjq="";
						String hansdjyf_jjq="";
						String hansyf_jjq="";
						String zongje_jjq="";
						
						try {
							if(rs.next()){
								
//						加价前
								farldj_jjq=rs.getString("farldj_jjq");
								aarzj_jjq=rs.getString("aarzj_jjq");
								vdafzj_jjq=rs.getString("vdafzj_jjq");
								yunjzj_jjq=rs.getString("yunjzj_jjq");
								shulzj_jjq=rs.getString("shulzbzj_jjq");
								farlj_jjq=rs.getString("farlj_jjq");
								farlk_jjq=rs.getString("farlk_jjq");
								jiessl_jjq=rs.getString("jiessl_jjq");
								hansdj_jjq=rs.getString("hansdj_jjq");
								hansmk_jjq=rs.getString("hansmk_jjq");
								yunj_jjq=rs.getString("yunj_jjq");
								hansdjyf_jjq=rs.getString("hansdjyf_jjq");
								hansyf_jjq=rs.getString("hansyf_jjq");
								zongje_jjq=rs.getString("zongje_jjq");
								
								
//						加价后
								farldj=rs.getString("farldj");
								aarzj=rs.getString("aarzj");
								vdafzj=rs.getString("vdafzj");
								yunjzj=rs.getString("yunjzj");
								shulzj=rs.getString("shulzbzj");
								farlj=rs.getString("farlj");
								farlk=rs.getString("farlk");
								jiessl=rs.getString("jiessl");
								hansdj=rs.getString("hansdj");
								hansmk=rs.getString("hansmk");
								yunj=rs.getString("yunj");
								hansdjyf=rs.getString("hansdjyf");
								hansyf=rs.getString("hansyf");
								zongje=rs.getString("zongje");
							}
						} catch (SQLException e) {
							// TODO 自动生成 catch 块
							e.printStackTrace();
						} catch (Exception e) {
							
							e.printStackTrace();
						}
						
						
						String ArrHeader2[][]=null;
						
						if(("是").equals(MainGlobal.getXitxx_item(con, "结算明细", "青铝结算明细特殊设置", "0", "否"))){
							farldj_jjq="0";
							shulzj_jjq="0";
							aarzj_jjq="0";
							vdafzj_jjq="0";
							yunjzj_jjq="0";
						}
						
						if(!table1.equals("diancjsmkb")){
//					如果不是销售单不显示“销售”栏
							ArrHeader2=new String[4][16];
							ArrHeader2[0]=new String[] {"结算数据","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算质量","结算质量","结算煤量","煤款","煤款","运费","运费","运费","总金额"};
							ArrHeader2[1]=new String[] {"结算数据","Qnet,ar","数量","Aar ","Vdaf","运距调价","合计","Qnet,ar","Qnet,ar","结算煤量","煤价","煤款","运距","单价","金额","总金额"};
							ArrHeader2[2]=new String[] {"结算数据",danw,"数量","Aar","Vdaf","元/吨","合计","MJ/kg","kcal/kg","吨","元/吨","元","千米","元/吨","元","元"};
							ArrHeader2[3]=new String[] {"采购",String.valueOf(Double.parseDouble(farldj_jjq)),shulzj_jjq,aarzj_jjq,vdafzj_jjq,yunjzj_jjq,
									String.valueOf(CustomMaths.Round_new(Double.parseDouble(farldj_jjq)+Double.parseDouble(shulzj_jjq)
											+Double.parseDouble(aarzj_jjq)+Double.parseDouble(vdafzj_jjq)+Double.parseDouble(yunjzj_jjq),4)),
											farlj_jjq,farlk_jjq,jiessl_jjq,hansdj_jjq,hansmk_jjq,yunj_jjq,hansdjyf_jjq,hansyf_jjq,zongje_jjq};
						}else{
							
							ArrHeader2=new String[5][16];
							ArrHeader2[0]=new String[] {"结算数据","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算质量","结算质量","结算煤量","煤款","煤款","运费","运费","运费","总金额"};
							ArrHeader2[1]=new String[] {"结算数据","Qnet,ar","数量","Aar ","Vdaf","运距调价","合计","Qnet,ar","Qnet,ar","结算煤量","煤价","煤款","运距","单价","金额","总金额"};
							ArrHeader2[2]=new String[] {"结算数据",danw,"数量","Aar","Vdaf","元/吨","合计","MJ/kg","kcal/kg","吨","元/吨","元","千米","元/吨","元","元"};
							ArrHeader2[3]=new String[] {"采购",String.valueOf(Double.parseDouble(farldj_jjq)),shulzj_jjq,aarzj_jjq,vdafzj_jjq,yunjzj_jjq,
									String.valueOf(CustomMaths.Round_new(Double.parseDouble(farldj_jjq)+Double.parseDouble(shulzj_jjq)
											+Double.parseDouble(aarzj_jjq)+Double.parseDouble(vdafzj_jjq)+Double.parseDouble(yunjzj_jjq),4)),
											farlj_jjq,farlk_jjq,jiessl_jjq,hansdj_jjq,hansmk_jjq,yunj_jjq,hansdjyf_jjq,hansyf_jjq,zongje_jjq};
							ArrHeader2[4]=new String[] {"销售",String.valueOf(Double.parseDouble(farldj)),shulzj,aarzj,vdafzj,yunjzj,
									String.valueOf(CustomMaths.Round_new(Double.parseDouble(farldj)+Double.parseDouble(shulzj)
											+Double.parseDouble(aarzj)+Double.parseDouble(vdafzj)
											+Double.parseDouble(yunjzj),4)),farlj,farlk,jiessl,hansdj,hansmk,yunj,hansdjyf,hansyf,zongje};
						}
						
						int ArrWidth2[]=new int[] {60,90,70,75,75,75,75,75,75,75,75,75,75,75,75,75};
						//			 定义页Title
						Report rt2=new Report();
//						int aw2=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
//						rt2.getArrWidth(ArrWidth2, aw2);//添加报表的A4控制
						rt2.setBody(new Table(ArrHeader2,0,0,0));
						rt2.body.setWidth(ArrWidth2);
						
						//			 合并单元格
						rt2.body.mergeCell(1,1,3,1);
						rt2.body.mergeCell(1,2,1,7);
						rt2.body.mergeCell(1,8,1,9);
						rt2.body.mergeCell(1,10,2,10);
						rt2.body.mergeCell(1,11,1,12);
						rt2.body.mergeCell(1,13,1,15);
						rt2.body.mergeCell(1,16,2,16);
						rt2.body.mergeCell(2,3,3,3);
						rt2.body.mergeCell(2,4,3,4);
						rt2.body.mergeCell(2,5,3,5);
						rt2.body.mergeCell(2,7,3,7);
						rt2.body.mergeCell(2,8,2,9);
						rt2.body.setBorder(2, 1, 0, 1);
						rt2.body.setCells(1, 1, 3, rt2.body.getCols(), Table.PER_ALIGN, Table.ALIGN_CENTER);
						rt2.body.setCells(4, 1, ArrHeader2.length, 1, Table.PER_ALIGN, Table.ALIGN_CENTER);
						rt2.body.setCells(4, 2, ArrHeader2.length, rt2.body.getCols(), Table.PER_ALIGN, Table.ALIGN_RIGHT);
						rt2.body.setRowCells(rt2.body.getRows(), Table.PER_BORDER_BOTTOM , 0);
						rt2.body.setBorder(2, 2, 0, 0);
						rt2.body.mergeFixedRow();
						
						for(int i=1;i<=16;i++){
							rt2.body.setColAlign(i, Table.ALIGN_CENTER);
						}
						
						rt.createFooter(1, ArrWidth);
						if(MainGlobal.getXitxx_item("结算",	"东胜结算明细样式", "0", "否").equals("是")){

						}else{
							rt.setDefautlFooter(2, 2, "数量经办人：", Table.ALIGN_LEFT);
							rt.setDefautlFooter(5, 2, "数量负责人：", Table.ALIGN_LEFT);
							rt.setDefautlFooter(8, 2, "质量经办人：", Table.ALIGN_LEFT);
							rt.setDefautlFooter(11, 2, "质量负责人：", Table.ALIGN_LEFT);
							rt.setDefautlFooter(14, 2, "数量质量复审员：", Table.ALIGN_LEFT);
							rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
							rt.footer.fontSize=10;
						}
						talbe.append(rt2.getAllPagesHtml()+"\n");
					}
					
//			在有子结算单的情况下增加父结算单的汇总行
//			父结算单的汇总数据
					sbsql.setLength(0);
					
					sbsql.append(
							"select * from\n" +
							"       (select\n" + 
							"              nvl(j.hetj,0) as farldj_jjq,\n" +
							"              nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.Qnetar_zhibb+"','zhejbz'),0) as qnetarzj_jjq,\n" +
							"              nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.Aar_zhibb+"','zhejbz'),0) as aarzj_jjq,\n" + 
							"              nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.Vdaf_zhibb+"','zhejbz'),0) as vdafzj_jjq,\n" + 
							"              nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.Yunju_zhibb+"','zhejbz'),0) as yunjzj_jjq,\n" + 
							"			   nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.Shul_zhibb+"','zhejbz'),0) as shulzbzj_jjq,\n" +
							"              round_new(nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.Qnetar_zhibb+"','jies'),0)*4.1816/1000," + jies_Qnetarblxs + ") as farlj_jjq,\n" + 
							"              nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.Qnetar_zhibb+"','jies'),0) as farlk_jjq,\n" + 
							"              nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.jiessl_zhibb+"','jies'),0) as jiessl_jjq,\n" + 
							"              nvl(j.hansdj,0) as hansdj_jjq,\n" + 
							"              nvl(j.hansmk,0) as hansmk_jjq,\n" + 
							"              nvl(j.yunj,'') as yunj_jjq,\n" + 
							"              nvl(j.Yunfhsdj,0) as hansdjyf_jjq,\n" + 
							"              nvl(j.hansyf,0) as hansyf_jjq,\n" + 
							"              nvl(j.hansmk,0)+nvl(j.hansyf,0) as zongje_jjq\n" + 
							"              from "+table1_cg+" j,"+table2_cg+" jy\n" + 
							"              where j.id=jy."+tiaoj+"(+)\n" + 
							"                    and j."+tiaoj3+"="+Jiesb_id+"),\n" + 
							"\n" + 
							"       (select\n" + 
							"              nvl(j.hetj,0) as farldj,\n" +
							"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Qnetar_zhibb+"','zhejbz'),0) as qnetarzj,\n" +
							"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Aar_zhibb+"','zhejbz'),0) as aarzj,\n" + 
							"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Vdaf_zhibb+"','zhejbz'),0) as vdafzj,\n" + 
							"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Yunju_zhibb+"','zhejbz'),0) as yunjzj,\n" + 
							"			   nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Shul_zhibb+"','zhejbz'),0) as shulzbzj,\n" +
							"              round_new(nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Qnetar_zhibb+"','jies'),0)*4.1816/1000," + jies_Qnetarblxs + ") as farlj,\n" + 
							"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Qnetar_zhibb+"','jies'),0) as farlk,\n" + 
							"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.jiessl_zhibb+"','jies'),0) as jiessl,\n" + 
							"              nvl(j.hansdj,0) as hansdj,nvl(j.hansmk,0) as hansmk,nvl(j.yunj,'') as yunj,nvl(j.Yunfhsdj,0) as hansdjyf,\n" + 
							"              nvl(j.hansyf,0) as hansyf,(nvl(j.hansmk,0)+nvl(j.hansyf,0)) as zongje\n" + 
							"              from "+table1+" j,"+table2+" jy\n" + 
							"              where j.id=jy."+tiaoj+"(+)\n" + 
							"                    and j.id="+Jiesb_id+")");
					
					rs=con.getResultSet(sbsql);
					String farldj="";
					String aarzj="";
					String vdafzj="";
					String yunjzj="";
					String shulzj="";
					String farlj="";
					String farlk="";
					String jiessl="";
					String hansdj="";
					String hansmk="";
					String yunj="";
					String hansdjyf="";
					String hansyf="";
					String zongje="";
					
					String farldj_jjq="";
					String aarzj_jjq="";
					String vdafzj_jjq="";
					String yunjzj_jjq="";
					String shulzj_jjq="";
					String farlj_jjq="";
					String farlk_jjq="";
					String jiessl_jjq="";
					String hansdj_jjq="";
					String hansmk_jjq="";
					String yunj_jjq="";
					String hansdjyf_jjq="";
					String hansyf_jjq="";
					String zongje_jjq="";
					
					try {
						if(rs.next()){
							
//					加价前
							farldj_jjq=rs.getString("farldj_jjq");
							aarzj_jjq=rs.getString("aarzj_jjq");
							vdafzj_jjq=rs.getString("vdafzj_jjq");
							yunjzj_jjq=rs.getString("yunjzj_jjq");
							shulzj_jjq=rs.getString("shulzbzj_jjq");
							farlj_jjq=rs.getString("farlj_jjq");
							farlk_jjq=rs.getString("farlk_jjq");
							jiessl_jjq=rs.getString("jiessl_jjq");
							hansdj_jjq=rs.getString("hansdj_jjq");
							hansmk_jjq=rs.getString("hansmk_jjq");
							yunj_jjq=rs.getString("yunj_jjq");
							hansdjyf_jjq=rs.getString("hansdjyf_jjq");
							hansyf_jjq=rs.getString("hansyf_jjq");
							zongje_jjq=rs.getString("zongje_jjq");
							
							
//					加价后
							farldj=rs.getString("farldj");
							aarzj=rs.getString("aarzj");
							vdafzj=rs.getString("vdafzj");
							yunjzj=rs.getString("yunjzj");
							shulzj=rs.getString("shulzbzj");
							farlj=rs.getString("farlj");
							farlk=rs.getString("farlk");
							jiessl=rs.getString("jiessl");
							hansdj=rs.getString("hansdj");
							hansmk=rs.getString("hansmk");
							yunj=rs.getString("yunj");
							hansdjyf=rs.getString("hansdjyf");
							hansyf=rs.getString("hansyf");
							zongje=rs.getString("zongje");
						}
					} catch (SQLException e) {
						// TODO 自动生成 catch 块
						e.printStackTrace();
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					
					if(("是").equals(MainGlobal.getXitxx_item(con, "结算明细", "青铝结算明细特殊设置", "0", "否"))){
						farldj_jjq="0";
						shulzj_jjq="0";
						aarzj_jjq="0";
						vdafzj_jjq="0";
						yunjzj_jjq="0";
					}
					
					String ArrHeaderhj[][]=null;
					if(!table1.equals("diancjsmkb")){
//				如果不是销售单不显示“销售”栏
						ArrHeaderhj=new String[4][16];
						ArrHeaderhj[0]=new String[] {"结算合计","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算质量","结算质量","结算煤量","煤款","煤款","运费","运费","运费","总金额"};
						ArrHeaderhj[1]=new String[] {"结算合计","Qnet,ar","数量","Aar ","Vdaf","运距调价","合计","Qnet,ar","Qnet,ar","结算煤量","煤价","煤款","运距","单价","金额","总金额"};
						ArrHeaderhj[2]=new String[] {"结算合计",danw,"数量","Aar","Vdaf","元/吨","合计","MJ/kg","kcal/kg","吨","元/吨","元","千米","元/吨","元","元"};
						ArrHeaderhj[3]=new String[] {"采购",String.valueOf(Double.parseDouble(farldj_jjq)),shulzj_jjq,aarzj_jjq,vdafzj_jjq,yunjzj_jjq,
								String.valueOf(CustomMaths.Round_new(Double.parseDouble(farldj_jjq)+Double.parseDouble(shulzj_jjq)
										+Double.parseDouble(aarzj_jjq)+Double.parseDouble(vdafzj_jjq)+Double.parseDouble(yunjzj_jjq),4)),
										farlj_jjq,farlk_jjq,jiessl_jjq,hansdj_jjq,hansmk_jjq,yunj_jjq,hansdjyf_jjq,hansyf_jjq,zongje_jjq};
					}else{
						
						ArrHeaderhj=new String[5][16];
						ArrHeaderhj[0]=new String[] {"结算合计","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算质量","结算质量","结算煤量","煤款","煤款","运费","运费","运费","总金额"};
						ArrHeaderhj[1]=new String[] {"结算合计","Qnet,ar","数量","Aar ","Vdaf","运距调价","合计","Qnet,ar","Qnet,ar","结算煤量","煤价","煤款","运距","单价","金额","总金额"};
						ArrHeaderhj[2]=new String[] {"结算合计",danw,"数量","Aar","Vdaf","元/吨","合计","MJ/kg","kcal/kg","吨","元/吨","元","千米","元/吨","元","元"};
						ArrHeaderhj[3]=new String[] {"采购",String.valueOf(Double.parseDouble(farldj_jjq)),shulzj_jjq,aarzj_jjq,vdafzj_jjq,yunjzj_jjq,
								String.valueOf(CustomMaths.Round_new(Double.parseDouble(farldj_jjq)+Double.parseDouble(shulzj_jjq)
										+Double.parseDouble(aarzj_jjq)+Double.parseDouble(vdafzj_jjq)+Double.parseDouble(yunjzj_jjq),4)),
										farlj_jjq,farlk_jjq,jiessl_jjq,hansdj_jjq,hansmk_jjq,yunj_jjq,hansdjyf_jjq,hansyf_jjq,zongje_jjq};
						ArrHeaderhj[4]=new String[] {"销售",String.valueOf(Double.parseDouble(farldj)),shulzj,aarzj,vdafzj,yunjzj,
								String.valueOf(CustomMaths.Round_new(Double.parseDouble(farldj)+Double.parseDouble(shulzj)
										+Double.parseDouble(aarzj)+Double.parseDouble(vdafzj)
										+Double.parseDouble(yunjzj),4)),farlj,farlk,jiessl,hansdj,hansmk,yunj,hansdjyf,hansyf,zongje};
					}
					
					
					
					
					int ArrWidthhj[]=new int[] {60,90,70,75,75,75,75,75,75,75,75,75,75,75,75,75};
//			 定义页Title
					Report rthj=new Report();
//					int aw=rthj.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
//					rthj.getArrWidth(ArrWidthhj, aw);//添加报表的A4控制
					rthj.setBody(new Table(ArrHeaderhj,0,0,0));
					rthj.body.setWidth(ArrWidthhj);
					
//			 合并单元格
					rthj.body.mergeCell(1,1,3,1);
					rthj.body.mergeCell(1,2,1,7);
					rthj.body.mergeCell(1,8,1,9);
					rthj.body.mergeCell(1,10,2,10);
					rthj.body.mergeCell(1,11,1,12);
					rthj.body.mergeCell(1,13,1,15);
					rthj.body.mergeCell(1,16,2,16);
					rthj.body.mergeCell(2,3,3,3);
					rthj.body.mergeCell(2,4,3,4);
					rthj.body.mergeCell(2,5,3,5);
					rthj.body.mergeCell(2,7,3,7);
					rthj.body.mergeCell(2,8,2,9);
					rthj.body.setBorder(2, 1, 0, 1);
					rthj.body.setCells(1, 1, 3, rthj.body.getCols(), Table.PER_ALIGN, Table.ALIGN_CENTER);
					rthj.body.setCells(4, 1, ArrHeaderhj.length, 1, Table.PER_ALIGN, Table.ALIGN_CENTER);
					rthj.body.setCells(4, 2, ArrHeaderhj.length, rthj.body.getCols(), Table.PER_ALIGN, Table.ALIGN_RIGHT);
					rthj.body.setRowCells(1, Table.PER_BORDER_TOP, 2);
					rthj.body.mergeFixedRow();
					
					rthj.createFooter(1, ArrWidthhj);
					if(MainGlobal.getXitxx_item("结算",	"东胜结算明细样式", "0", "否").equals("是")){

					}else{
						rthj.setDefautlFooter(2, 2, "数量经办人：", Table.ALIGN_LEFT);
						rthj.setDefautlFooter(5, 2, "数量负责人：", Table.ALIGN_LEFT);
						rthj.setDefautlFooter(8, 2, "质量经办人：", Table.ALIGN_LEFT);
						rthj.setDefautlFooter(11, 2, "质量负责人：", Table.ALIGN_LEFT);
						rthj.setDefautlFooter(14, 2, "数量质量复审员：", Table.ALIGN_LEFT);
						rthj.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
						rthj.footer.fontSize=10;
					}
					
					talbe.append(rthj.getAllPagesHtml()+"\n");
					
				}else{
//			没有子结算单
					
					Jiesb_id_type=getJiesbid(Type,Jiesb_id);
					m_Jiesb_id+=Jiesb_id_type+",";
					
					sbsql.setLength(0);
					sbsql.append(
							"select decode(riq,'合计','合计',rownum) as xuh,\n" +
							"       riq,pinz,yunsfs,biaoz,jingz,yingk,yuns,zongkd,qnet_ar,round_new(qnet_ar*1000/4.1816,0) as qnet_ar_k,\n" + 
							"       aar,vdaf,mt,std,'' as beiz\n" + 
							"       from\n" + 
							"   (select f.lie_id as id,\n" + 
							"       decode(f.daohrq,null,'合计',to_char(f.daohrq,'yyyy-MM-dd')) as riq,\n" + 
							"       decode(p.mingc,null,'合计',p.mingc) as pinz,\n" + 
							"		decode(ysfs.mingc,null,'合计',ysfs.mingc) as yunsfs,\n" + 
							"       sum(f.biaoz) as biaoz,sum(f.jingz) as jingz,sum(f.yingk) as yingk,sum(f.yuns) as yuns,\n" + 
							"       sum(f.zongkd) as zongkd,\n" + 
							"       round_new(decode(sum("+jies_Jqsl+"),0,0, sum("+jies_Jqsl+"*z.qnet_ar)/sum("+jies_Jqsl+")),"+jies_Qnetarblxs+") as qnet_ar,\n" + 
							"       round_new(decode(sum("+jies_Jqsl+"),0,0, sum("+jies_Jqsl+"*z."+strA+")/sum("+jies_Jqsl+")),"+jies_Aarblxs+") as aar,\n" + 
							"       round_new(decode(sum("+jies_Jqsl+"),0,0, sum("+jies_Jqsl+"*z."+strV+")/sum("+jies_Jqsl+")),"+jies_Vdafblxs+") as vdaf,\n" + 
							"       round_new(decode(sum("+jies_Jqsl+"),0,0, sum("+jies_Jqsl+"*z.mt)/sum("+jies_Jqsl+")),"+jies_Mtblxs+") as mt,\n" + 
							"        round_new(decode(sum("+jies_Jqsl+"),0,0, sum("+jies_Jqsl+"*z."+strSt+")/sum("+jies_Jqsl+")),"+jies_Stdblxs+") as std\n" + 
							"       from fahb f,zhilb z,pinzb p,yunsfsb ysfs,\n" +  
							"            (select id from "+table_gj+" where id="+Jiesb_id_type+") j\n" + 
							"       where f.zhilb_id=z.id\n" + 
							"             and f.pinzb_id=p.id\n" + 
							"             and f.jiesb_id=j.id\n" + 
							"             and f.yunsfsb_id=ysfs.id\n" + 
							"       group by rollup(f.lie_id,f.daohrq,p.mingc,ysfs.mingc)\n" + 
							"       having not (grouping(ysfs.mingc)=1 and grouping(f.lie_id)=0)\n order by riq" + 
					"    )");
					rs=con.getResultSet(sbsql.toString());
					Report rt = new Report(); //报表定义
					String ArrHeader[][]=new String[3][16];
					ArrHeader[0]=new String[] {"序号","日期","品种","运输方式","数量验收","数量验收","数量验收","数量验收","数量验收","质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","备注"};
					ArrHeader[1]=new String[] {"序号","日期","品种","运输方式","矿发量","验收量","盈亏量","途损量","扣减量","Qnet,ar","Qnet,ar",getAmc(strA),getVmc(strV),"Mar",this.getStmc(strSt),"备注"};
					ArrHeader[2]=new String[] {"序号","日期","品种","运输方式","吨","吨","吨","吨","吨","MJ/kg","kcal/kg","%","%","%","%","备注"};
					int ArrWidth[]=new int[] {60, 80, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63};
					// 数据
					Visit visit = (Visit) getPage().getVisit();
//					String type=
							MainGlobal.getXitxx_item("结算", "结算单所属单位", String.valueOf(visit.getDiancxxb_id()), "ZGDT");
					
//					int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
//					rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
//					if (type.equals("GD")) {
//						rt.setTitle("国电电力发展股份有限公司燃料验收单", ArrWidth);
						rt.setTitle("燃料入库验收单", ArrWidth);
//					} else {
//						rt.setTitle("中国大唐集团公司燃料验收单",ArrWidth);
//					}
//				rt.setTitle("中国大唐集团公司燃料验收单",ArrWidth);
					rt.setDefaultTitleLeft("供货单位:"+Meikdwmc+"<br>收货单位:"+Diancqc+"",12);
					rt.setDefaultTitleRight("结算单号:"+bianm+"<br>日期:"+Jiesrq, 4);
					rt.setBody(new Table(rs, 3, 0, 0));
					rt.body.setWidth(ArrWidth);
//				rt.body.setPageRows(20);
					rt.body.setHeaderData(ArrHeader);// 表头数据
					rt.body.mergeCell(rt.body.getRows(), 1, rt.body.getRows(),4);
					rt.body.setCells(rt.body.getRows(), 1, rt.body.getRows(), 3, Table.PER_ALIGN, Table.ALIGN_CENTER);
					rt.body.mergeFixedRow();
					rt.body.ShowZero = false;
					rt.body.setRowCells(rt.body.getRows(), Table.PER_BORDER_BOTTOM , 0);
					
					for(int i=1;i<=16;i++){
						rt.body.setColAlign(i, Table.ALIGN_CENTER);
					}
//					rt.createFooter(1, ArrWidth);
//					rt.setDefautlFooter(2, 2, "数量经办人：", Table.ALIGN_LEFT);
//					rt.setDefautlFooter(5, 2, "数量负责人：", Table.ALIGN_LEFT);
//					rt.setDefautlFooter(8, 2, "质量经办人：", Table.ALIGN_LEFT);
//					rt.setDefautlFooter(11, 2, "质量负责人：", Table.ALIGN_LEFT);
//					rt.setDefautlFooter(14, 2, "数量质量复审员：", Table.ALIGN_LEFT);
//					rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
//					rt.footer.fontSize=10;
					
					//			构造表
					talbe.append(rt.getAllPagesHtml()+"\n"); 
					
					sbsql.setLength(0);
					
					if(table1.equals("diancjsmkb")){
						
						table1_cg = "kuangfjsmkb";
						table2_cg = "kuangfjsyfb";
						tiaoj2 = "diancjsmkb_id";
						tiaoj3 = "diancjsmkb_id";
					}else{
						
						table1_cg = table1;
						table2_cg = table2;
						tiaoj3="id";
						if(table1.equals("kuangfjsmkb")){
							
							tiaoj2 = "kuangfjsmkb_id";
						}else{
							
							tiaoj2 = "diancjsmkb_id";
						}
					}
					
//				汇总数据
					String yunfzongje="";
					boolean flag=false;//标记是不是酒泉的两票结算，货票核对状态是已核对的情况的结算单
					if(visit.getDiancxxb_id()==323){//酒泉电厂时
						String jiesbh="";
						//从结算表得到yunfhsdj,若为0，表明是两票结算，已核对货票；若不为0表明是两票结算未核对货票
						//厂方业务可参看程序修改说明2012-4-11(玉沙沙),只能根据jiesb.yunfhsdj是否为0区分开是哪种形式的重点煤
						String sql_jiesb="select yunfhsdj,bianm from jiesb where id="+Jiesb_id;
						ResultSetList rsl_jiesb=con.getResultSetList(sql_jiesb);
						rsl_jiesb.next();
						if(rsl_jiesb.getString("yunfhsdj").equals("0")){//当jiesb.yunfhsdj=0时，说明是有货票核对的两票结算
							flag=true;
							jiesbh=rsl_jiesb.getString("bianm");
							String sql_jiesyfb="select id from jiesyfb where bianm='"+jiesbh+"'";
							ResultSetList rsl_jiesyfb=con.getResultSetList(sql_jiesyfb);
							while(rsl_jiesyfb.next()){
								String yunfjsb_id=rsl_jiesyfb.getString("id");
								String sql_yunfje=
									"select sum(zongje) zongje\n" +
									"  from yunfdjb\n" + 
									" where id in \n" + 
									"       (select distinct yunfdjb_id from danjcpb where yunfjsb_id = "+yunfjsb_id+")";
								ResultSetList rsl_yunfje=con.getResultSetList(sql_yunfje);
								while(rsl_yunfje.next()){
									yunfzongje=rsl_yunfje.getString("zongje");
								}
								rsl_yunfje.close();
							}
							rsl_jiesyfb.close();
						}
					}
					sbsql.append(
							"select * from\n" +
							"       (select\n" + 
							"			   nvl(j.hetj,0) as farldj_jjq,\n" +
							"              nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.Qnetar_zhibb+"','zhejbz'),0) as qnetarzj_jjq,\n" +
							"              nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.Aar_zhibb+"','zhejbz'),0) as aarzj_jjq,\n" + 
							"              nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.Vdaf_zhibb+"','zhejbz'),0) as vdafzj_jjq,\n" + 
							"              nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.Yunju_zhibb+"','zhejbz'),0) as yunjzj_jjq,\n" + 
							"			   nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.Shul_zhibb+"','zhejbz'),0) as shulzbzj_jjq,\n" +
//							"              round_new(nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.Qnetar_zhibb+"','jies'),0)*4.1816/1000," + jies_Qnetarblxs + ") as farlj_jjq,\n" + //热量修约
//							"              nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.Qnetar_zhibb+"','jies'),0) as farlk_jjq,\n" + 
							"              nvl(getjiesdzb('"+table1_cg+"',j.id,'"+Locale.jiessl_zhibb+"','jies'),0) as jiessl_jjq,\n" + 
							"              nvl(j.hansdj,0) as hansdj_jjq,\n" + 
							"              nvl(j.hansmk,0) as hansmk_jjq,\n" + 
							"              nvl(j.yunj,'') as yunj_jjq,\n" + 
							"              nvl(j.Yunfhsdj,0) as hansdjyf_jjq,\n" + 
							"              nvl(j.hansyf,0) as hansyf_jjq,\n" + 
							"              nvl(j.hansmk,0)+nvl(j.hansyf,0) as zongje_jjq\n" + 
							"              from "+table1_cg+" j,"+table2_cg+" jy\n" + 
							"              where j.id=jy."+tiaoj+"(+)\n" + 
							"                    and j."+tiaoj3+"="+Jiesb_id+"),\n" + 
							"\n" + 
							"       (select\n" + 
							"              nvl(j.hetj,0) as farldj,\n" + 
							"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Qnetar_zhibb+"','zhejbz'),0) as qnetarzj,\n" + 
							"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Aar_zhibb+"','zhejbz'),0) as aarzj,\n" + 
							"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Vdaf_zhibb+"','zhejbz'),0) as vdafzj,\n" + 
							"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Yunju_zhibb+"','zhejbz'),0) as yunjzj,\n" + 
							"			   nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Shul_zhibb+"','zhejbz'),0) as shulzbzj,\n" +
							"              round_new(nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Qnetar_zhibb+"','jies'),0)*4.1816/1000," + jies_Qnetarblxs + ") as farlj,\n" + 
							"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.Qnetar_zhibb+"','jies'),0) as farlk,\n" + 
							"              nvl(getjiesdzb('"+table1+"',j.id,'"+Locale.jiessl_zhibb+"','jies'),0) as jiessl,\n" + 
							"              nvl(j.hansdj,0) as hansdj,nvl(j.hansmk,0) as hansmk,nvl(j.yunj,'') as yunj,nvl(j.Yunfhsdj,0) as hansdjyf,\n" + 
							"              nvl(j.hansyf,0) as hansyf,(nvl(j.hansmk,0)+nvl(j.hansyf,0)) as zongje\n" + 
							"              from "+table1+" j,"+table2+" jy\n" + 
							"              where j.id=jy."+tiaoj+"(+)\n" + 
							"                    and j.id="+Jiesb_id+"),"+
							"(SELECT to_char(QNET_AR,'fm99999999999999.00') farlj_jjq, ROUND_NEW(QNET_AR * 1000 / 4.1816, 0) AS farlk_jjq\n" +
							"  FROM (SELECT DECODE(YSFS.MINGC, NULL, '合计', YSFS.MINGC) AS YUNSFS,\n" + 
							"       round_new(decode(sum("+jies_Jqsl+"),0,0, sum("+jies_Jqsl+"*z.qnet_ar)/sum("+jies_Jqsl+")),"+jies_Qnetarblxs+") as qnet_ar\n" + 						
							"          FROM FAHB F,\n" + 
							"               ZHILB Z,\n" + 
							"               PINZB P,\n" + 
							"               YUNSFSB YSFS,\n" + 
							"               (SELECT ID FROM "+table_gj+" WHERE ID = "+Jiesb_id_type+") J\n" + 
							"         WHERE F.ZHILB_ID = Z.ID\n" + 
							"           AND F.PINZB_ID = P.ID\n" + 
							"           AND F.JIESB_ID = J.ID\n" + 
							"           AND F.YUNSFSB_ID = YSFS.ID\n" + 
							"         GROUP BY ROLLUP((F.LIE_ID, F.DAOHRQ, P.MINGC, YSFS.MINGC))\n" + 
							"        HAVING GROUPING(F.LIE_ID) = 1))" );
//					to_char(round(JS.JIESSL, 2),'fm99999999999999.00')
					rs=con.getResultSet(sbsql);
					String farldj="";
					String aarzj="";
					String vdafzj="";
					String yunjzj="";
					String shulzj="";
					String farlj="";
					String farlk="";
					String jiessl="";
					String hansdj="";
					String hansmk="";
					String yunj="";
					String hansdjyf="";
					String hansyf="";
					String zongje="";
					
					String farldj_jjq="";
					String aarzj_jjq="";
					String vdafzj_jjq="";
					String yunjzj_jjq="";
					String shulzj_jjq="";
					String farlj_jjq="";
					String farlk_jjq="";
					String jiessl_jjq="";
					String hansdj_jjq="";
					String hansmk_jjq="";
					String yunj_jjq="";
					String hansdjyf_jjq="";
					String hansyf_jjq="";
					String zongje_jjq="";
					
					try {
						if(rs.next()){
							
							//					加价前
							farldj_jjq=rs.getString("farldj_jjq");
							aarzj_jjq=rs.getString("aarzj_jjq");
							vdafzj_jjq=rs.getString("vdafzj_jjq");
							yunjzj_jjq=rs.getString("yunjzj_jjq");
							shulzj_jjq=rs.getString("shulzbzj_jjq");
							farlj_jjq=rs.getString("farlj_jjq");
							farlk_jjq=rs.getString("farlk_jjq");
							jiessl_jjq=rs.getString("jiessl_jjq");
							hansdj_jjq=rs.getString("hansdj_jjq");
							hansmk_jjq=rs.getString("hansmk_jjq");
							yunj_jjq=rs.getString("yunj_jjq");
							hansdjyf_jjq=rs.getString("hansdjyf_jjq");
							hansyf_jjq=rs.getString("hansyf_jjq");
							zongje_jjq=rs.getString("zongje_jjq");
							
							
							//					加价后
							farldj=rs.getString("farldj");
							aarzj=rs.getString("aarzj");
							vdafzj=rs.getString("vdafzj");
							yunjzj=rs.getString("yunjzj");
							shulzj=rs.getString("shulzbzj");
							farlj=rs.getString("farlj");
							farlk=rs.getString("farlk");
							jiessl=rs.getString("jiessl");
							hansdj=rs.getString("hansdj");
							hansmk=rs.getString("hansmk");
							yunj=rs.getString("yunj");
							hansdjyf=rs.getString("hansdjyf");
							hansyf=rs.getString("hansyf");
							zongje=rs.getString("zongje");
						}
					} catch (SQLException e) {
						// TODO 自动生成 catch 块
						e.printStackTrace();
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					
					if(("是").equals(MainGlobal.getXitxx_item(con, "结算明细", "青铝结算明细特殊设置", "0", "否"))){
						farldj_jjq="0";
						shulzj_jjq="0";
						aarzj_jjq="0";
						vdafzj_jjq="0";
						yunjzj_jjq="0";
					}
					
					String ArrHeader2[][]=null;
					if(!table1.equals("diancjsmkb")){
//					如果不是销售单不显示“销售”栏
						ArrHeader2=new String[4][16];
						ArrHeader2[0]=new String[] {"结算数据","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算质量","结算质量","结算煤量","煤款","煤款","运费","运费","运费","总金额"};
						ArrHeader2[1]=new String[] {"结算数据","Qnet,ar","数量","Aar ","Vdaf","运距调价","合计","Qnet,ar","Qnet,ar","结算煤量","煤价","煤款","运距","单价","金额","总金额"};
						ArrHeader2[2]=new String[] {"结算数据",danw,"数量","Aar","Vdaf","元/吨","合计","MJ/kg","kcal/kg","吨","元/吨","元","千米","元/吨","元","元"};
						if(flag){//酒泉电厂处理
							ArrHeader2[3]=new String[] {"采购",String.valueOf(Double.parseDouble(farldj_jjq)),shulzj_jjq,aarzj_jjq,vdafzj_jjq,yunjzj_jjq,
									String.valueOf(CustomMaths.Round_new(Double.parseDouble(farldj_jjq)+Double.parseDouble(shulzj_jjq)
											+Double.parseDouble(aarzj_jjq)+Double.parseDouble(vdafzj_jjq)+Double.parseDouble(yunjzj_jjq),4)),
											//farlj_jjq,farlk_jjq,jiessl_jjq,hansdj_jjq,hansmk_jjq,yunj_jjq,hansdjyf_jjq,hansyf_jjq,zongje_jjq};
											farlj_jjq,farlk_jjq,jiessl_jjq,hansdj_jjq,hansmk_jjq,yunj_jjq,hansdjyf_jjq,yunfzongje,Double.parseDouble(hansmk_jjq)+Double.parseDouble(yunfzongje.equals("")?"0":yunfzongje)+""};
						
						}else{
							ArrHeader2[3]=new String[] {"采购",String.valueOf(Double.parseDouble(farldj_jjq)),shulzj_jjq,aarzj_jjq,vdafzj_jjq,yunjzj_jjq,
									String.valueOf(CustomMaths.Round_new(Double.parseDouble(farldj_jjq)+Double.parseDouble(shulzj_jjq)
											+Double.parseDouble(aarzj_jjq)+Double.parseDouble(vdafzj_jjq)+Double.parseDouble(yunjzj_jjq),4)),
											farlj_jjq,farlk_jjq,jiessl_jjq,hansdj_jjq,hansmk_jjq,yunj_jjq,hansdjyf_jjq,hansyf_jjq,zongje_jjq};
						
						}
					}else{
						
						ArrHeader2=new String[5][16];
						ArrHeader2[0]=new String[] {"结算数据","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算系数(价格目录)","结算质量","结算质量","结算煤量","煤款","煤款","运费","运费","运费","总金额"};
						ArrHeader2[1]=new String[] {"结算数据","Qnet,ar","数量","Aar ","Vdaf","运距调价","合计","Qnet,ar","Qnet,ar","结算煤量","煤价","煤款","运距","单价","金额","总金额"};
						ArrHeader2[2]=new String[] {"结算数据",danw,"数量","Aar","Vdaf","元/吨","合计","MJ/kg","kcal/kg","吨","元/吨","元","千米","元/吨","元","元"};
						ArrHeader2[3]=new String[] {"采购",String.valueOf(Double.parseDouble(farldj_jjq)),shulzj_jjq,aarzj_jjq,vdafzj_jjq,yunjzj_jjq,
								String.valueOf(CustomMaths.Round_new(Double.parseDouble(farldj_jjq)+Double.parseDouble(shulzj_jjq)
										+Double.parseDouble(aarzj_jjq)+Double.parseDouble(vdafzj_jjq)+Double.parseDouble(yunjzj_jjq),4)),
										farlj_jjq,farlk_jjq,jiessl_jjq,hansdj_jjq,hansmk_jjq,yunj_jjq,hansdjyf_jjq,hansyf_jjq,zongje_jjq};
						ArrHeader2[4]=new String[] {"销售",String.valueOf(Double.parseDouble(farldj)),shulzj,aarzj,vdafzj,yunjzj,String.valueOf(CustomMaths.Round_new(
								Double.parseDouble(farldj)+Double.parseDouble(shulzj)+Double.parseDouble(aarzj)+Double.parseDouble(vdafzj)
								+Double.parseDouble(yunjzj),4)),farlj,farlk,jiessl,hansdj,hansmk,yunj,hansdjyf,hansyf,zongje};
					}
					int ArrWidth2[]=new int[] {60, 80, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63};
					
					//			 定义页Title
					Report rt2=new Report();
//					int aw2=rt2.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
//					rt2.getArrWidth(ArrWidth2, aw2);//添加报表的A4控制
					rt2.setBody(new Table(ArrHeader2,0,0,0));
					rt2.body.setWidth(ArrWidth2);
					
					//			 合并单元格
					rt2.body.mergeCell(1,1,3,1);
					rt2.body.mergeCell(1,2,1,7);
					rt2.body.mergeCell(1,8,1,9);
					rt2.body.mergeCell(1,10,2,10);
					rt2.body.mergeCell(1,11,1,12);
					rt2.body.mergeCell(1,13,1,15);
					rt2.body.mergeCell(1,16,2,16);
					rt2.body.mergeCell(2,3,3,3);
					rt2.body.mergeCell(2,4,3,4);
					rt2.body.mergeCell(2,5,3,5);
					rt2.body.mergeCell(2,7,3,7);
					rt2.body.mergeCell(2,8,2,9);
					rt2.body.setBorder(2, 1, 0, 1);
					rt2.body.setCells(1, 1, 3, rt2.body.getCols(), Table.PER_ALIGN, Table.ALIGN_CENTER);
					rt2.body.setCells(4, 1, ArrHeader2.length, 1, Table.PER_ALIGN, Table.ALIGN_CENTER);
					rt2.body.setCells(4, 2, ArrHeader2.length, rt2.body.getCols(), Table.PER_ALIGN, Table.ALIGN_RIGHT);
					rt2.body.mergeFixedRow();
					
					for(int i=1;i<=16;i++){
						rt2.body.setColAlign(i, Table.ALIGN_CENTER);
					}
					
					rt2.createFooter(1, ArrWidth2);
					if(visit.getDiancxxb_id()==323){//酒泉电厂
						/*rt2.setDefautlFooter(2, 2, "司磅班长:", Table.ALIGN_LEFT);
						rt2.setDefautlFooter(6, 2, "化验班长:", Table.ALIGN_LEFT);
						rt2.setDefautlFooter(10, 2, "燃料部：", Table.ALIGN_LEFT);
						rt2.setDefautlFooter(14, 2, "制表：", Table.ALIGN_LEFT);*/
					}else{
						if(MainGlobal.getXitxx_item("结算",	"东胜结算明细样式", "0", "否").equals("是")){

						}else{
							//rt2.setDefautlFooter(2, 2, "数量经办人：", Table.ALIGN_LEFT);
							rt2.setDefautlFooter(1, 2, "数量负责人：", Table.ALIGN_LEFT);
							//rt2.setDefautlFooter(8, 2, "质量经办人：", Table.ALIGN_LEFT);
							rt2.setDefautlFooter(8, 2, "质量负责人：", Table.ALIGN_LEFT);
							rt2.setDefautlFooter(14, 2, "数量质量复审员：", Table.ALIGN_LEFT);
						}
					}
					
					rt2.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
					rt2.footer.fontSize=10;
					
					talbe.append(rt2.getAllPagesHtml()+"\n");
				}
				
//		if(!m_Jiesb_id.equals("")){
//			
//			m_Jiesb_id = m_Jiesb_id.substring(0, m_Jiesb_id.lastIndexOf(","));
//			((Visit) getPage().getVisit()).setString7(m_Jiesb_id);
//		}
				
				// 设置页数
				_CurrentPage = 1;
				_AllPages = 0;
				if (_AllPages == 0) {
					_CurrentPage = 0;
				}
				
		}else{//运费结算明细
				Diancxxb_id = MainGlobal.getTableCol(table2, "diancxxb_id", "bianm", bianm);
			    sql="select d.quanc,j.id,j.shoukdw,j.bianm,j.diancxxb_id,j.gongysb_id,j.hetb_id,to_char(j.jiesrq,'yyyy-MM-dd') as jiesrq from " +table2+ " j,diancxxb d where j.diancxxb_id=d.id and j.bianm='"+bianm+"'";
		
				sbsql.append(sql);
				ResultSetList rsl=con.getResultSetList(sbsql.toString());
				if(rsl.next()){
					
					Jiesb_id=rsl.getLong("id");
					Diancqc=rsl.getString("quanc");
					Meikdwmc=rsl.getString("shoukdw");
					Diancxxb_id=rsl.getString("diancxxb_id");
					Gongysb_id=rsl.getString("gongysb_id");
					Hetb_id=rsl.getString("hetb_id");
					Jiesrq=rsl.getString("jiesrq");
				}
				String jies_Jqsl="jingz";								//结算加权数量
				String jies_Qnetarblxs="2";
				String jies_Stdblxs="2";
				String jies_Mtblxs="1";
				String jies_Madblxs="2";
				String jies_Aarblxs="2";
				String jies_Aadblxs="2";
				String jies_Adblxs="2";
				String jies_Vadblxs="2";
				String jies_Vdafblxs="2";
				String jies_Stadblxs="2";
				String jies_Starblxs="2";
				String jies_Hadblxs="2";
				String jies_Qbadblxs="2";
				String jies_Qgradblxs="2";
				String jies_T2blxs="2";
				
//		得到系统信息中设置的值
				String XitxxArrar[][]=null;	
				XitxxArrar=MainGlobal.getXitxx_items("结算",	"select mingc from xitxxb where leib='结算'"
						,String.valueOf(Diancxxb_id));
				
//		分析取得的值，然后对变量进行赋值
				if(XitxxArrar!=null){
					
					for(int i=0;i<XitxxArrar.length;i++){
						
						if(XitxxArrar[i][0]!=null){
							
							if(XitxxArrar[i][0].trim().equals(Locale.jiaqsl_xitxx)){
//						加权数量
								jies_Jqsl=XitxxArrar[i][1].trim();
							}
						}
					}
				}
				
//		取结算设置中的配置值
				if(Jiesdcz.getJiessz_items(Long.parseLong(Diancxxb_id),Long.parseLong(Gongysb_id),Long.parseLong(Hetb_id))!=null){
					
					String JiesszArray[][]=null;
					
					JiesszArray=Jiesdcz.getJiessz_items(Long.parseLong(Diancxxb_id),Long.parseLong(Gongysb_id),Long.parseLong(Hetb_id));
					
					for(int i=0;i<JiesszArray.length;i++){
						
						if(JiesszArray[i][0]!=null){
							
							if(JiesszArray[i][0].equals(Locale.jiesjqsl_jies)){
//						结算加权数量
								jies_Jqsl=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.mtblxsw_jies)){	//保留小数位开始
								
								jies_Mtblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.madblxsw_jies)){
								
								jies_Madblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.aarblxsw_jies)){
								
								jies_Aarblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.aadblxsw_jies)){
								
								jies_Aadblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.adblxsw_jies)){
								
								jies_Adblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.vadblxsw_jies)){
								
								jies_Vadblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.vdafblxsw_jies)){
								
								jies_Vdafblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.stadblxsw_jies)){
								
								jies_Stadblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.starblxsw_jies)){
								
								jies_Starblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.stdblxsw_jies)){
								
								jies_Stdblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.hadblxsw_jies)){
								
								jies_Hadblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.Qnetarblxsw_jies)){
								
								jies_Qnetarblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.Qbadblxsw_jies)){
								
								jies_Qbadblxs=JiesszArray[i][1];
							}else if(JiesszArray[i][0].equals(Locale.Qgradblxsw_jies)){	//保留小数位结束
								
								jies_Qgradblxs=JiesszArray[i][1];
							}
						}
					}
				}
				
				
				sbsql.setLength(0);
				if(Type.equals("dianc")){
					tiaoj = "danpcjsmxb";
				}
				String m_Jiesb_id="";	//用于往下个页面传递jiesb_id
				long Jiesb_id_type=0;	//局部变量
				
				sbsql.append("select id from "+table2+" where fuid=").append(Jiesb_id);
				rsl=con.getResultSetList(sbsql.toString());
				ResultSet rs=null;
//			没有子结算单
					
					Jiesb_id_type=getJiesbid(Type,Jiesb_id);
					m_Jiesb_id+=Jiesb_id_type+",";
					
					sbsql.setLength(0);
					sbsql.append(
							"select decode(riq,'合计','合计',rownum) as xuh,meikxxb_id,\n" +
							"       riq,pinz,yunsfs,biaoz,jingz,yingk,yuns,zongkd,qnet_ar,round_new(qnet_ar*1000/4.1816,0) as qnet_ar_k,\n" + 
							"       '' as beiz\n" + 
							"       from\n" + 
							"   (select f.lie_id as id,\n" + 
							"       decode(m.mingc,null,'合计', m.mingc) as meikxxb_id,\n"+
							"       decode(f.daohrq,null,'合计',to_char(f.daohrq,'yyyy-MM-dd')) as riq,\n" + 
							"       decode(p.mingc,null,'合计',p.mingc) as pinz,\n" + 
							"		decode(ysfs.mingc,null,'合计',ysfs.mingc) as yunsfs,\n" + 
							"       sum(f.biaoz) as biaoz,sum(f.jingz) as jingz,sum(f.yingk) as yingk,sum(f.yuns) as yuns,\n" + 
							"       sum(f.zongkd) as zongkd,\n" + 
							"       round_new(sum(decode("+jies_Jqsl+",0,0,"+jies_Jqsl+"*z.qnet_ar))\n" + 
							"                     /sum(decode("+jies_Jqsl+",0,1,"+jies_Jqsl+")),"+jies_Qnetarblxs+") as qnet_ar\n" +
							"       from fahb f,zhilb z,pinzb p,yunsfsb ysfs,meikxxb m,\n" +  
							"            (select distinct fahb_id from chepb where id in(select chepb_id from danjcpb where yunfjsb_id="+Jiesb_id_type+")) j\n" + 
							"       where f.zhilb_id=z.id\n" + 
							"             and f.pinzb_id=p.id\n" + 
							"             and f.id=j.fahb_id\n" + 
							"             and f.yunsfsb_id=ysfs.id\n" + 
							"             and f.meikxxb_id=m.id\n"+
//					"             and z.liucztb_id=1\n" + 
							"       group by rollup(f.lie_id,m.mingc,f.daohrq,p.mingc,ysfs.mingc)\n" + 
							"       having not (grouping(ysfs.mingc)=1 and grouping(f.lie_id)=0)\n" + 
					"    )");
					rs=con.getResultSet(sbsql.toString());
					Report rt = new Report(); //报表定义
					String ArrHeader[][]=new String[3][13];
					ArrHeader[0]=new String[] {"序号","煤矿单位","日期","品种","运输方式","数量验收","数量验收","数量验收","数量验收","数量验收","质量验收","质量验收","备注"};
					ArrHeader[1]=new String[] {"序号","煤矿单位","日期","品种","运输方式","矿发量","验收量","盈亏量","途损量","扣减量","Qnet,ar","Qnet,ar","备注"};
					ArrHeader[2]=new String[] {"序号","煤矿单位","日期","品种","运输方式","吨","吨","吨","吨","吨","MJ/kg","kcal/kg","备注"};
					int ArrWidth[]=new int[] {60,90,70,75,75,75,75,75,75,75,75,75,75};
					// 数据
					Visit visit = (Visit) getPage().getVisit();
//					String type=
							MainGlobal.getXitxx_item("结算", "结算单所属单位", String.valueOf(visit.getDiancxxb_id()), "ZGDT");
//					int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
//					rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
//					if (type.equals("GD")) {
//						rt.setTitle("国电电力发展股份有限公司燃料验收单", ArrWidth);
						rt.setTitle("燃料入库验收单", ArrWidth);
//					} else {
//						rt.setTitle("中国大唐集团公司燃料验收单",ArrWidth);
//					}
//				rt.setTitle("中国大唐集团公司燃料验收单",ArrWidth);
					rt.setDefaultTitleLeft("运输单位:"+Meikdwmc+"<br>收货单位:"+Diancqc+"",12);
					rt.setDefaultTitleRight("结算单号:"+bianm+"<br>日期:"+Jiesrq, 4);
					rt.setBody(new Table(rs, 3, 0, 0));
					rt.body.setWidth(ArrWidth);
//				rt.body.setPageRows(20);
					rt.body.setHeaderData(ArrHeader);// 表头数据
					rt.body.mergeCell(rt.body.getRows(), 1, rt.body.getRows(),5);
					rt.body.setCells(rt.body.getRows(), 1, rt.body.getRows(), 3, Table.PER_ALIGN, Table.ALIGN_CENTER);
					rt.body.mergeFixedRow();
					rt.body.ShowZero = false;
					rt.body.setRowCells(rt.body.getRows(), Table.PER_BORDER_BOTTOM , 0);
					//			构造表
					talbe.append(rt.getAllPagesHtml()+"\n"); 
					
					sbsql.setLength(0);
					
//				汇总数据
					sbsql.append(
							"select decode(mingc, '合计', '合计', rownum) as xuh,\n" +
							"                     mingc,\n" + 
							"                     jies,\n" + 
							"                     jiessl,\n" + 
							"                     koud,\n" + 
							"                     kous,\n" + 
							"                     ches,\n" + 
							"                     jingz,\n" + 
							"                     jiesdj,\n" + 
							"                     jiakhj,\n" + 
							"                     jiashj,\n" + 
							"                     hetj,\n" + 
							"                     zongje\n" + 
							"                from (select decode(m.mingc, null, '合计', m.mingc) as mingc,\n" + 
							"                             round_new(sum(d.hetj*d.jies)/sum(d.jies),2) as hetj,\n" + 
							"                             round_new(sum(d.jies*d.jiessl)/sum(d.jiessl),0) as jies,\n" + 
							"                             sum(d.jiessl) as jiessl,\n" + 
							"                             sum(d.koud) as koud,\n" + 
							"                             sum(d.kous) as kous,\n" + 
							"                             sum(d.ches) as ches,\n" + 
							"                             sum(d.jingz) as jingz,\n" + 
							"                             round_new(sum(d.jiesdj*d.jiessl)/sum(d.jiessl),2) as jiesdj,\n" + 
							"                             sum(d.jiakhj) as jiakhj,\n" + 
							"                             sum(d.jiashj) as jiashj,\n" + 
							"                             sum(d.zongje) zongje\n" + 
							"                        from danpcjsmxb d, jiesyfb j, meikxxb m\n" + 
							"                       where j.id = d.jiesdid(+)\n" + 
							"                         and d.meikxxb_id = m.id\n" + 
							"                         and j.id ="+Jiesb_id+"\n" + 
							"                       group by rollup(m.mingc))");
					
					rs=con.getResultSet(sbsql.toString());
					String ArrHeader2[][]=null;
				
						ArrHeader2=new String[1][13];
						ArrHeader2[0]=new String[] {"序号","煤矿单位","运距（千米）","结算数量（吨）","扣吨（吨）","扣水（吨）","车数","净重","运费单价（元/吨）","不含税金额（元）","含税金额（元）","合同价（吨/Km）","总金额（元）"};
					    int ArrWidth2[]=new int[] {60,90,70,75,75,75,75,75,75,75,75,75,75};
					
					//			 定义页Title
					Report rt2=new Report();
					
					rt2.setBody(new Table(rs, 1, 0, 0));
//					int aw2=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
//					rt2.getArrWidth(ArrWidth2, aw2);//添加报表的A4控制
					rt2.body.setHeaderData(ArrHeader2);// 表头数据
					rt2.body.setWidth(ArrWidth2);
					rt.body.mergeCell(rt2.body.getRows(), 1, rt2.body.getRows(),2);
					rt2.body.setCells(rt2.body.getRows(), 0, rt2.body.getRows(), 0, Table.PER_ALIGN, Table.ALIGN_CENTER);
					rt2.body.setRowCells(1, Table.PER_BORDER_TOP, 0);
					rt2.body.mergeFixedRow();
					rt2.body.ShowZero = false;
					rt2.body.setRowCells(rt2.body.getRows(), Table.PER_BORDER_BOTTOM , 1);
					talbe.append(rt2.getAllPagesHtml()+"\n");
				
				// 设置页数
				_CurrentPage = 1;
				_AllPages = 0;
				if (_AllPages == 0) {
					_CurrentPage = 0;
				}
		 }
		}catch (Exception e1) {
			// TODO 自动生成 catch 块
			e1.printStackTrace();
		}
		return talbe.toString();
	}

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}
	
//	private boolean _GuohdClick = false;
//
//	public void GuohdButton(IRequestCycle cycle) {
//		_GuohdClick = true;
//	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
		
//		if(_GuohdClick){
//			_GuohdClick = false;
//			Guohddy();
//		}
	}
	
//	窗口弹出js_begin
	public String getWindowScript(){
		
		return ((Visit) getPage().getVisit()).getString6();
	}
	
	public void setWindowScript(String value){
		
		((Visit) getPage().getVisit()).setString6(value);
	}
//	窗口弹出js_end
	
//	private void Guohddy(){
//		
//		String str = " var url = 'http://'+document.location.host+document.location.pathname;"
//			+ "var end = url.indexOf(';');"
//			+ "url = url.substring(0,end);"
//			+ "url = url + '?service=page/' + 'Guohd&lx="
//			+((Visit) getPage().getVisit()).getString7()+"';"
//			+ " window.open(url,'Guohd');";
//		
//		this.setWindowScript(str);
//	}
	
	public boolean isLx(String a,String b){
		boolean lx = false;
		if(b.indexOf(a)>-1){
			
			lx = true;
		}
		return lx;
	}
	
	public String getType(String value){
//		得到表属于的结算类型
//		value: jiesb,jiesyfb;结算编号;分公司id（可能是空的）
		String Type = "dianc";
		
		if(value.indexOf(";")==value.lastIndexOf(";")){
//			说明从上一个页面没有传来分公司id，说明不是分公司采购结算
			if(value.indexOf("diancjsmkb")>-1){
				
				Type = "changf";
			}else if(value.indexOf("kuangfjsmkb")>-1){
				
				Type = "kuangf";
			}
		}else if(value.indexOf("kuangfjsmkb")>-1){
//			说明从上一个页面已经传来分公司id，说明分公司采购结算
			Type = "fengscg";
		}
		
		return Type;
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setString6("");	//要弹出的页面
//			visit.setString7("");	//页面参数传递——要传到guohd页面的jiesb_id
			
			visit.setboolean1(false);	//是否显示过衡单
			
			if(visit.isDCUser()){
//				如果是电厂用户，显示过衡单按钮，否则不显示该按钮
				visit.setboolean1(true);
			}
			
			if (cycle.getRequestContext().getParameters("lx") != null) {
		    	if (!isLx(";",cycle.getRequestContext().getParameters("lx")[0])){
					visit.setString5("jiesb,jiesyfb"+";"+cycle.getRequestContext().getParameters("lx")[0]+","+"dianc");
				}else{
					visit.setString5(cycle.getRequestContext().getParameters("lx")[0]);
				}
			}
			
			//begin方法里进行初始化设置
//			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);保存传递的非默认纸张的样式
				
		}
		
		if (cycle.getRequestContext().getParameters("lx") != null && !cycle.getRequestContext().getParameters("lx")[0].equals("")) {
	    	if (!isLx(";",cycle.getRequestContext().getParameters("lx")[0])){
				visit.setString5("jiesb,jiesyfb"+";"+cycle.getRequestContext().getParameters("lx")[0]+","+"dianc");
			}else{
				visit.setString5(cycle.getRequestContext().getParameters("lx")[0]);
			}
		}
		visit.setString4("");	//清空上一个页面的跳转页面语句
	}
}