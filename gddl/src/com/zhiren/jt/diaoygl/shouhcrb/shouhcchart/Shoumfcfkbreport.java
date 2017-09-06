package com.zhiren.jt.diaoygl.shouhcrb.shouhcchart;

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

public class Shoumfcfkbreport extends BasePage {
	
	private String jizbh="";
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
		String strCondtion="";
		String Having="";
		int jib=getDiancTreeJib(diancxxb_id);
		if (jib==2){
			strCondtion=" and (dc.fgsid=" +diancxxb_id +" or dc.rlgsid="+diancxxb_id +")"; 
			Having=" having not grouping(fgsmc)=1";
		}else if (jib==3){
			strCondtion=" and dc.id=" +diancxxb_id;
			Having=" having not grouping(fgsmc)=1 and grouping(dc.mingc)=0";
		}else if (jib==-1){
			strCondtion=" and dc.id=" +diancxxb_id;
		}
 
	   StringBuffer sbsql = new StringBuffer();
			sbsql.append("select decode(grouping(fgsmc)+grouping(dc.mingc)+grouping(dqmc),3,'总计',2,fgsmc,1,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'||dqmc) as mingc, \n");
			sbsql.append(" sum(round_new(sj.ches,0)) as ches,sum(round_new(sj.laimsl,0)) as laimsl, \n");
			sbsql.append(" sum(round_new(sj.biaoz,0)) as biaoz,sum(round_new(sj.jingz,0)) as jingz,  \n");
			sbsql.append(" sum(round_new(sj.yuns,0)) as yuns,sum(round_new(sj.yingd,0)) as yingd, \n");
			sbsql.append(" sum(round_new(sj.kuid,0)) as kuid, sum(round_new(sj.koud,0)) as koud  \n");
			sbsql.append(" from  \n");
			sbsql.append("       (select fh.diancxxb_id,fh.gongysb_id, sum(fh.ches) as ches,  \n");
			sbsql.append("        (round_new(sum(fh.biaoz),0)+round_new(sum(fh.yingd),0)-round_new(sum(fh.yingd-fh.yingk),0))  as laimsl, \n");
			sbsql.append("        round_new(sum(fh.biaoz),0) as biaoz,round_new(sum(fh.jingz),0) as jingz,   \n");
			sbsql.append("              round_new(sum(fh.yuns),0) as yuns,round_new(sum(fh.yingd),0) as yingd,  \n");
			sbsql.append("              round_new(sum(fh.yingd-fh.yingk),0) as kuid, round_new(sum(fh.koud),0) as koud  \n");
			sbsql.append("       from fahb fh,vwdianc dc,vwgongys y   \n");
			sbsql.append("       where fh.gongysb_id=y.id   \n");
			sbsql.append("and fh.daohrq>=to_date('"+beginriq+"','yyyy-mm-dd')  \n");
			sbsql.append("and fh.daohrq<=to_date('"+endriq+"','yyyy-mm-dd')  \n");
			sbsql.append("         and fh.diancxxb_id=dc.id     \n");
			sbsql.append(strCondtion+"\n");
			sbsql.append("         and fh.gongysb_id=y.id  \n");
			sbsql.append("         group by fh.diancxxb_id,fh.gongysb_id,fh.lieid ) sj,vwdianc dc,vwgongys y   \n");
			sbsql.append(" where  sj.diancxxb_id=dc.id and sj.gongysb_id=y.id   \n");
			sbsql.append("group by rollup(fgsmc,dc.mingc,dqmc)    \n");
			sbsql.append("  "+Having+"    \n");
			sbsql.append(" order by grouping(fgsmc) desc,max(fgsxh) ,fgsmc,  \n");
			sbsql.append("          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,  \n");
			sbsql.append("          grouping(dqmc) desc,max(dqxh) ,dqmc  \n");
			
			ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Report rt = new Report();
			
			
			String ArrHeader[][]= new String[2][9];
			
			ArrHeader[0] = new String[] { "单位", "车数","实收量<br>(吨)", "票重<br>(吨)", "净重<br>(吨)", "运损<br>(吨)","盈吨<br>(吨)", "亏吨<br>(吨)", "扣吨<br>(吨)" };
			ArrHeader[1] = new String[] { "单位", "车数","实收量<br>(吨)", "票重<br>(吨)", "净重<br>(吨)", "运损<br>(吨)","盈吨<br>(吨)", "亏吨<br>(吨)", "扣吨<br>(吨)" };
			int ArrWidth[] = new int[] { 150, 80, 80, 80, 80, 80, 80, 80, 80 };
			
			Table bt=new Table(rs,2,0,2);
			rt.setBody(bt);
			bt.setColAlign(2, Table.ALIGN_CENTER);
			
			rt.body.setPageRows(22);
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.body.mergeFixedRow();//合并行
			rt.body.mergeFixedCols();//和并列
			rt.setTitle("煤收耗存日报(分厂分矿表)", ArrWidth);

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
		
		//数据类型
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString9((cycle.getRequestContext().getParameters("lx")[0]));
			jizbh = visit.getString9();
        }else{
        	if(!visit.getString1().equals("")) {
        		jizbh = visit.getString9();
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
		//截止日期
		if(cycle.getRequestContext().getParameters("endriq") !=null) {
			visit.setString12((cycle.getRequestContext().getParameters("endriq")[0]));
			endriq = visit.getString12();
        }else{
        	if(!visit.getString1().equals("")) {
        		endriq = visit.getString12();
            }
        }
		
		}
//	 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib(String DiancTreeJib) {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		
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
}