//2008-10-12 chh 
//修改内容 :实际库存的显示反了
package com.zhiren.jt.zdt.shengcdy.diaoymxreport;

/**
 * 运输明细查询
 * @author xzy
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

public class DiaoymxReport extends BasePage {
	
	private String leix="";//限负荷天数,低于警戒存煤量,最长警戒天数
	private String diancxxb_id="";
	private String beginriq="";
	private String endriq="";
	private String tians="";
	
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
		StringBuffer sbsql = new StringBuffer("");
		String tiaoj=" and rb.kuc<dc.jingjcml";
		String kuc1="限负荷库存";
		String kuc2="实际库存";
		String shuz="dc.jingjcml";
		//限负荷天数,低于警戒存煤量,最长警戒天数
		if(leix.equals("xfh")){
			tiaoj=" and rb.kuc<dc.xianfhkc";
			shuz="dc.xianfhkc";
			kuc1="限负荷库存";
			kuc2="实际库存";
			
			sbsql.append(" select dc.mingc,to_char(rb.riq,'yyyy-mm-dd') as riq,rb.kuc as "+kuc2+","+shuz+" as "+kuc1+" \n");
			sbsql.append(" from shouhcrbb rb,diancxxb dc \n");
			sbsql.append(" where rb.diancxxb_id=dc.id "+tiaoj+" and dc.id="+diancxxb_id+" \n");
			sbsql.append(" and rb.riq>=to_date('"+beginriq+"','yyyy-mm-dd') and rb.riq<=to_date('"+endriq+"','yyyy-mm-dd') \n");
			sbsql.append(" order by dc.mingc,to_char(rb.riq,'yyyy-mm-dd') \n");
			
		}else if(leix.equals("zc")){
			tiaoj=" and kuc<jingjcml";
			shuz="dc.jingjcml";
			kuc1="警诫库存";
			kuc2="实际库存";
			
			sbsql.append("  select zb.mingc,to_char(zb.riq,'yyyy-mm-dd') as riq,sj.kuc,sj.jingjcml from \n");
			sbsql.append("(select dc.mingc,dc.id,to_date('"+beginriq+"','yyyy-mm-dd')+rownum as riq \n");
			sbsql.append("  from all_objects,diancxxb dc  \n");
			sbsql.append("  where rownum<=(to_date('"+endriq+"','yyyy-mm-dd')-to_date('"+beginriq+"','yyyy-mm-dd')) \n");
			sbsql.append("  and dc.id="+diancxxb_id+") zb, \n");
			sbsql.append("(select dc.mingc,rb.riq,rb.kuc,dc.jingjcml \n");
			sbsql.append("from shouhcrbb rb, diancxxb dc \n");
			sbsql.append("where rb.diancxxb_id = dc.id \n");
			sbsql.append(" and rb.riq >= to_date('"+beginriq+"','yyyy-mm-dd') \n");
			sbsql.append(" and rb.riq <= to_date('"+endriq+"','yyyy-mm-dd') and dc.id="+diancxxb_id+" order by mingc,riq) sj, \n");
			sbsql.append("(select sj.mingc,sj.jingjrq as riq,sum(1) as tians,sum(jingjcs) as jingjcs from  \n");
			sbsql.append("(select dc.mingc,rb.riq,getJingjkcgj(dc.id,rb.riq) as jingjcs,decode(getJingjkcgjrq(dc.id,rb.riq),null,to_date('"+endriq+"','yyyy-mm-dd'),getJingjkcgjrq(dc.id,rb.riq)) as jingjrq  \n");
			sbsql.append("  from shouhcrbb rb,diancxxb dc  \n");
			sbsql.append(" where rb.diancxxb_id=dc.id and rb.kuc<dc.jingjcml and dc.id="+diancxxb_id+" \n");
			sbsql.append("   and rb.riq>=to_date('"+beginriq+"','yyyy-mm-dd') and rb.riq<=to_date('"+endriq+"','yyyy-mm-dd')) sj  \n");
			sbsql.append(" group by (sj.mingc,sj.jingjrq)) ts \n");
			sbsql.append("where zb.mingc=sj.mingc(+) and zb.riq=sj.riq(+) and ts.mingc=zb.mingc and ts.riq=decode(getJingjkcgjrq(zb.id, zb.riq),null,to_date('"+endriq+"','yyyy-mm-dd'),getJingjkcgjrq(zb.id, zb.riq)) \n");
			sbsql.append("     "+tiaoj+" and tians="+tians+" \n");
		}else if(leix.equals("jj")){
			tiaoj=" and kuc<jingjcml";
			shuz="dc.jingjcml";
			kuc1="警诫库存";
			kuc2="实际库存";
			
			sbsql.append(" select dc.mingc,to_char(rb.riq,'yyyy-mm-dd') as riq,rb.kuc as "+kuc2+","+shuz+" as "+kuc1+" \n");
			sbsql.append(" from shouhcrbb rb,diancxxb dc \n");
			sbsql.append(" where rb.diancxxb_id=dc.id "+tiaoj+" and dc.id="+diancxxb_id+" \n");
			sbsql.append(" and rb.riq>=to_date('"+beginriq+"','yyyy-mm-dd') and rb.riq<=to_date('"+endriq+"','yyyy-mm-dd') \n");
			sbsql.append(" order by dc.mingc,to_char(rb.riq,'yyyy-mm-dd') \n");
		}
		
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		
		String ArrHeader[][]=new String[1][4];
		ArrHeader[0]=new String[] {"电厂名称","日期",kuc2,kuc1};
	
		int ArrWidth[]=new int[] {150,150,150,150};

		Table bt=new Table(rs,1,0,1);
		rt.setBody(bt);
		bt.setColAlign(2, Table.ALIGN_CENTER);
		
		rt.body.setPageRows(15);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRow();//合并行
		rt.body.mergeFixedCols();//和并列
		rt.setTitle("存煤告警统计明细", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 2, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
        rt.setDefaultTitle(3, 2,  FormatDate(DateUtil.getDate(beginriq))+"-"+FormatDate(DateUtil.getDate(endriq)),Table.ALIGN_RIGHT);
       // rt.setDefaultTitle(5, 0, "单位:吨" ,Table.ALIGN_RIGHT);
     	
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 1, "制表时间:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(2, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(3, 2, "制表：", Table.ALIGN_RIGHT);//+((Visit) getPage().getVisit()).getRenymc()
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
			leix = visit.getString9();
        }else{
        	if(!visit.getString9().equals("")) {
        		leix = visit.getString9();
            }
        }
		//电厂ID
		if(cycle.getRequestContext().getParameters("diancxxb_id") !=null) {
			visit.setString10((cycle.getRequestContext().getParameters("diancxxb_id")[0]));
			diancxxb_id = visit.getString10();
        }else{
        	if(!visit.getString10().equals("")) {
        		diancxxb_id = visit.getString10();
            }
        }
		//开始日期
		if(cycle.getRequestContext().getParameters("beginriq") !=null) {
			visit.setString11((cycle.getRequestContext().getParameters("beginriq")[0]));
			beginriq = visit.getString11();
        }else{
        	if(!visit.getString11().equals("")) {
        		beginriq = visit.getString11();
            }
        }
		//结束日期
		if(cycle.getRequestContext().getParameters("endriq") !=null) {
			visit.setString12((cycle.getRequestContext().getParameters("endriq")[0]));
			endriq = visit.getString12();
        }else{
        	if(!visit.getString12().equals("")) {
        		endriq = visit.getString12();
            }
        }
//		电厂ID
		if(cycle.getRequestContext().getParameters("tians") !=null) {
			visit.setString13((cycle.getRequestContext().getParameters("tians")[0]));
			tians = visit.getString13();
        }else{
        	if(!visit.getString13().equals("")) {
        		tians = visit.getString13();
            }
        }
	}

}