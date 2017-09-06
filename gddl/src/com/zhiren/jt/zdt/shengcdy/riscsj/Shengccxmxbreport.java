package com.zhiren.jt.zdt.shengcdy.riscsj;

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

public class Shengccxmxbreport extends BasePage {
	
	private String jizbh="";
	private String diancxxb_id="";
	private String diancxxbmc="";
	private String zhuangjrl="";
	private String jizid="";
	private String beginriq="";
	
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
	public String getDiancxxbmc(){
		String diancxxbmc="";
		JDBCcon cn = new JDBCcon();
		String sql_diancxxbmc="select  mingc from diancxxb where id="+diancxxb_id;
		ResultSet rs=cn.getResultSet(sql_diancxxbmc);
		try {
			while(rs.next()){
				 diancxxbmc=rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return diancxxbmc;
	}
	public String getJizbh(){
		String jizubh="";
		JDBCcon cn = new JDBCcon();
		String sql_jizubh="select jizbh from jizb where id="+jizbh;
		ResultSet rs=cn.getResultSet(sql_jizubh);
		try {
			while(rs.next()){
				 jizubh=rs.getString("jizbh");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return jizubh;
	}
	public String getJizrl(){
		String jizrl="";
		JDBCcon cn = new JDBCcon();
		String sql_jizrl="select jizurl from jizb where id="+jizbh;
		ResultSet rs=cn.getResultSet(sql_jizrl);
		try {
			while(rs.next()){
				jizrl=rs.getString("jizurl");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return jizrl;
	}
	public String getZhuangjrl(){
		String str="";
		int zhuangjrl=0;
		JDBCcon cn = new JDBCcon();
		String sql_zhuangjrl="select round(sum(jizurl),0) as jizzrl from jizb where diancxxb_id="+diancxxb_id;
		ResultSet rs=cn.getResultSet(sql_zhuangjrl);
		try {
			if(rs.next()){
				zhuangjrl=rs.getInt("jizzrl");
				str=""+zhuangjrl;
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return str;
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
		StringBuffer sbsql = new StringBuffer("");


		if(jizbh.equals("xj")){

			sbsql.append("select decode(grouping(to_char(rq.riq,'yyyy-mm-dd')),1,'合计',to_char(rq.riq,'yyyy-mm-dd')) as riq,\n");
			sbsql.append("	     decode(sum(rsc.fadl),0,0,round(sum(rsc.fadl)/10000,2)) as fadl,\n");
			sbsql.append("       decode(sum(rsc.gongdl),0,0,round(sum(rsc.gongdl)/10000,2)) as gongdl,\n");
			sbsql.append("       decode(sum(rsc.shangwdl),0,0, round(sum(rsc.shangwdl)/10000,2)) as shangwdl,\n");
			sbsql.append(" 	     decode(sum(rsc.gongrl),0,0,sum(rsc.gongrl)) as gongrl\n");
			sbsql.append("from (select first_day(to_date('"+beginriq+"','yyyy-mm-dd'))+rownum-1 as riq from all_objects \n");
			sbsql.append("      where rownum<=(to_date('"+beginriq+"','yyyy-mm-dd')-first_day(to_date('"+beginriq+"','yyyy-mm-dd'))+1))rq, \n");
			sbsql.append(" 	   (select rs.riq as riq,rs.fadl,rs.gongdl,rs.shangwdl,rs.gongrl\n");
			sbsql.append(" 	    from riscsjb rs\n");
			sbsql.append(" 	    where rs.diancxxb_id="+diancxxb_id+" and rs.riq>=first_day(to_date('"+beginriq+"','yyyy-mm-dd')) and rs.riq<=to_date('"+beginriq+"','yyyy-mm-dd'))rsc\n");
			sbsql.append("where rsc.riq(+)=rq.riq \n");
			sbsql.append("group by rollup(to_char(rq.riq,'yyyy-mm-dd'))\n");
			sbsql.append("order by  grouping(to_char(rq.riq,'yyyy-mm-dd')) desc,to_char(rq.riq,'yyyy-mm-dd')");
			
			ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Report rt = new Report();
			String ArrHeader[][]=new String[1][5];
			ArrHeader[0]=new String[] {"日期","发电量<br>(万千瓦时)","供电量<br>(万千瓦时)","上网电量<br>(万千瓦时)","供热量<br>(吉焦)"};

			int ArrWidth[]=new int[] {150,90,90,90,90};		
			Table bt=new Table(rs,1,0,2);
			rt.setBody(bt);
			bt.setColAlign(2, Table.ALIGN_CENTER);
			
			rt.body.setPageRows(22);
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.body.mergeFixedRow();//合并行
			rt.body.mergeFixedCols();//和并列
			rt.setTitle("机组明细", ArrWidth);

			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 4, "电厂名称:" +this.getDiancxxbmc()+"   装机容量:"+this.getZhuangjrl(),Table.ALIGN_LEFT);
						
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
		}else{

			sbsql.append("select decode(grouping(to_char(jzb.riq,'yyyy-mm-dd')),1,'合计',to_char(jzb.riq,'yyyy-mm-dd')) as riq, \n");
			sbsql.append("       sheb.shebzt,decode(sum(rsc.fadl),0,0,round(sum(rsc.fadl)/10000,2)) as fadl,\n");
			sbsql.append("       decode(sum(rsc.gongdl),0,0,round(sum(rsc.gongdl)/10000,2)) as gongdl, \n");
			sbsql.append("       decode(sum(rsc.shangwdl),0,0, round(sum(rsc.shangwdl)/10000,2)) as shangwdl,\n");
			sbsql.append("		 decode(sum(rsc.gongrl),0,0,sum(rsc.gongrl)) as gongrl,\n");
			sbsql.append("       round(decode(sum(jzb.jizurl/10),0,0,round(sum(rsc.fadl)/10000,2)/(sum(jzb.jizurl/10) * 24)*100),2) as fadfhl\n");
			sbsql.append("from( select rq.riq,jz.id,jz.jizbh,jz.diancxxb_id,jz.jizurl\n");
			sbsql.append("		from (select first_day(to_date('"+beginriq+"','yyyy-mm-dd'))+rownum-1 as riq from all_objects\n");
			sbsql.append("     		  where rownum<=(to_date('"+beginriq+"','yyyy-mm-dd')-first_day(to_date('"+beginriq+"','yyyy-mm-dd'))+1))rq,\n");
			sbsql.append(" 		     (select j.id as id,j.jizbh,j.jizurl,j.diancxxb_id from jizb j where diancxxb_id="+diancxxb_id+" and id="+jizbh+" ) jz\n");
			sbsql.append(" )jzb,(select rs.jizb_id as id,rs.riq as riq,rs.diancxxb_id,rs.fadl,rs.gongdl,rs.shangwdl,rs.gongrl from riscsjb rs \n");
			sbsql.append("       where rs.diancxxb_id="+diancxxb_id+" and rs.jizb_id="+jizbh+"\n");
			sbsql.append("      	   and rs.riq>=first_day(to_date('"+beginriq+"','yyyy-mm-dd')) and rs.riq<=to_date('"+beginriq+"','yyyy-mm-dd'))rsc,\n");
			sbsql.append(" 		(select  rq.riq, decode(a.shebzt,null,'运行',a.shebzt)  as shebzt\n");
			sbsql.append("		 from (select  j.shebzt,riq  from jizyxqkb j\n");
			sbsql.append("			   where j.diancxxb_id="+diancxxb_id+" and j.jizb_id="+jizbh+"\n");
			sbsql.append("				     and j.riq>=first_day(to_date('"+beginriq+"','yyyy-mm-dd')) and j.riq<=to_date('"+beginriq+"','yyyy-mm-dd') ) a,\n");
			sbsql.append("            (select first_day(to_date('"+beginriq+"','yyyy-mm-dd'))+rownum-1 as riq from all_objects\n");
			sbsql.append(" 	     		where rownum<=(to_date('"+beginriq+"','yyyy-mm-dd')-first_day(to_date('"+beginriq+"','yyyy-mm-dd'))+1)) rq\n");
			sbsql.append("		 where  a.riq(+)=rq.riq) sheb\n");
			sbsql.append("where rsc.id(+)=jzb.id  and jzb.diancxxb_id=rsc.diancxxb_id(+) and rsc.riq(+)=jzb.riq and sheb.riq(+)=jzb.riq\n");
			sbsql.append("group by rollup((to_char(jzb.riq,'yyyy-mm-dd'),sheb.shebzt)) \n");
			sbsql.append("order by  grouping(to_char(jzb.riq,'yyyy-mm-dd')) desc,to_char(jzb.riq,'yyyy-mm-dd')");
			ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Report rt = new Report();
			String ArrHeader[][]=new String[1][7];
			ArrHeader[0]=new String[] {"日期","机组状态","发电量<br>(万千瓦时)","供电量<br>(万千瓦时)","上网电量<br>(万千瓦时)","供热量<br>(吉焦)","发电负荷率<br>(%)"};

			int ArrWidth[]=new int[] {150,70,80,90,90,90,90};		
			Table bt=new Table(rs,1,0,2);
			rt.setBody(bt);
			bt.setColAlign(2, Table.ALIGN_CENTER);
			
			rt.body.setPageRows(22);
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.body.mergeFixedRow();//合并行
			rt.body.mergeFixedCols();//和并列
			rt.setTitle("机组明细", ArrWidth);

			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 4, "电厂名称:" +this.getDiancxxbmc()+"    机组号:"+this.getJizbh()+"   机组容量:"+this.getJizrl(),Table.ALIGN_LEFT);

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
		//机组id
		/*if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString9((cycle.getRequestContext().getParameters("lx")[0]));
			jizid = visit.getString9();
        }else{
        	if(!visit.getString1().equals("")) {
        		jizid = visit.getString9();
            }
        }
//		机组编号
		if(cycle.getRequestContext().getParameters("jizbh") !=null) {
			visit.setString11((cycle.getRequestContext().getParameters("jizbh")[0]));
			jizbh = visit.getString11();
        }else{
        	if(!visit.getString1().equals("")) {
        		jizbh = visit.getString11();
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
		//电厂名称
		if(cycle.getRequestContext().getParameters("diancxxbmc") !=null) {
			visit.setString13((cycle.getRequestContext().getParameters("diancxxbmc")[0]));
			diancxxbmc = visit.getString13();
        }else{
        	if(!visit.getString1().equals("")) {
        		diancxxbmc = visit.getString13();
            }
        }
		//装机容量
		if(cycle.getRequestContext().getParameters("zhuangjrl") !=null) {
			visit.setString14((cycle.getRequestContext().getParameters("zhuangjrl")[0]));
			zhuangjrl = visit.getString14();
        }else{
        	if(!visit.getString1().equals("")) {
        		zhuangjrl = visit.getString14();
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
        }*/
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
		
		}

}