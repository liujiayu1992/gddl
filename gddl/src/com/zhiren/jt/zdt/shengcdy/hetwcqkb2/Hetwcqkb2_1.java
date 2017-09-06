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

public class Hetwcqkb2_1 extends BasePage {
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
		return getZhiltj();
	}
	private String getZhiltj() {
//		分矿
		Visit visit = (Visit) getPage().getVisit();
		StringBuffer sbsql = new StringBuffer("");
		int jib11=0,ranlgs11=0;
		String sql="select jib,diancxxb.ranlgs\n" +
		"from diancxxb\n" + 
		"where id="+visit.getString1();
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
			return "";
		}
		con11.Close();
		String gongysWhere=" and g.dqid="+visit.getString2();
		String diancWhere="";
		gongysWhere=" and g.dqmc='"+visit.getString2()+"' ";
		
		if(jib11==2&&ranlgs11==1){//燃料公司
			diancWhere=" and d.shangjgsid="+visit.getString1()+" ";
		}else if(jib11==2&&ranlgs11!=1){//分公司
			diancWhere=" and d.fuid="+visit.getString1()+" ";
		}else if(jib11==3){//电厂
			diancWhere=" and d.id="+visit.getString1()+" ";
		}else{//集团
			diancWhere=" ";
		}

		sbsql.append(
		"select decode(grouping(mingc),1,'总计',mingc)mingc1,round_new(sum(benqjhl),0)benqjhl,round_new(sum(tongqjhl),0)tongqjhl,sum(benqssl)benqssl,\n" +
		"round_new(sum(tongqssl),0)tongqssl,round_new(sum(benqjhl)-sum(benqssl),0) cha1,\n" + 
		"round_new(sum(tongqjhl)-sum(tongqssl),0) cha2,\n" + 
		"round_new(decode(sum(benqjhl),0,0,sum(benqssl)/sum(benqjhl))*100,2) benqdhl,\n" + 
		"round_new(decode(sum(tongqjhl),0,0,sum(tongqssl)/sum(tongqjhl))*100,2)tongqdhl\n" + 
		"from("+

		"select  mingc,sum(benqjhl)benqjhl,sum(tongqjhl)tongqjhl,sum(benqssl)benqssl,sum(tongqssl)tongqssl,\n" +
		"sum(benqjhl)-sum(benqssl) cha1,sum(tongqjhl)-sum(tongqssl) cha1,\n" + 
		"round_new(decode(sum(benqjhl),0,0,sum(benqssl)/sum(benqjhl))*100,2) benqdhl,\n" + 
		"round_new(decode(sum(tongqjhl),0,0,sum(tongqssl)/sum(tongqjhl))*100,2)tongqdhl\n" + 
		"from(\n" + 
		"--本期\n" + 
		"select mingc,sum(benqjhl)benqjhl,sum(benqsls)benqssl,0 tongqjhl,0 tongqssl\n" + 
		"from(\n" + 
		"--计划to_date('\"+getBeginriqDate()+\"', 'yyyy-mm-dd'),to_date('\"+getEndriqDate()+\"', 'yyyy-mm-dd'),j.riq, j.hej\n" + 
		"select g.mingc,sum(getHetl_Sjd(to_date('"+visit.getString3()+"','yyyy-mm-dd'),to_date('"+visit.getString4()+"','yyyy-mm-dd'),n.riq,n.hej*10000)) benqjhl,0  benqsls\n" + 
		"from niandhtqkb n,vwgongys g,vwdianc d\n" + 
		"where n.gongysb_id=g.id and n.diancxxb_id=d.id\n" + 
		" and n.jihkjb_id=2 and n.riq>=to_date('"+visit.getString3()+"', 'yyyy-mm-dd')  and n.riq<=to_date('"+visit.getString4()+"', 'yyyy-mm-dd')\n" + 
		diancWhere + gongysWhere+
		"group by g.mingc\n" + 
		"--实收\n" + 
		"union\n" + 
		"select g.mingc,0 jihl,sum(f.laimsl) shisl\n" + 
		"from fahb f,vwgongys g,vwdianc d\n" + 
		"where f.gongysb_id=g.id and f.diancxxb_id=d.id\n" + 
		" and f.jihkjb_id=2 and f.daohrq>=to_date('"+visit.getString3()+"', 'yyyy-mm-dd')  and f.daohrq<=to_date('"+visit.getString4()+"', 'yyyy-mm-dd')\n" + 
		diancWhere + gongysWhere+
		"group by g.mingc\n" + 
		")group by mingc\n" + 
		"union\n" + 
		"--同期\n" + 
		"select mingc,0 benqjhl,0 benqssl,sum(tongqjhl)tongqjhl,sum(tongqssl)tongqssl\n" + 
		"from(\n" + 
		"select g.mingc,sum(getHetl_Sjd(ADD_MONTHS(to_date('"+visit.getString3()+"','yyyy-mm-dd'),-12),ADD_MONTHS(to_date('"+visit.getString4()+"','yyyy-mm-dd'),-12),n.riq,n.hej*10000))  tongqjhl,0  tongqssl\n" + 
		"from niandhtqkb n,vwgongys g,vwdianc d\n" + 
		"where n.gongysb_id=g.id and n.diancxxb_id=d.id\n" + 
		" and n.jihkjb_id=2 and n.riq>=ADD_MONTHS(to_date('"+visit.getString3()+"', 'yyyy-mm-dd'),-12) and n.riq<=ADD_MONTHS(to_date('"+visit.getString4()+"', 'yyyy-mm-dd'),-12)\n" + 
		diancWhere + gongysWhere+
		"group by g.mingc\n" + 
		"union\n" + 
		"select g.mingc,0 tongqjhl,sum(f.laimsl) tongqssl\n" + 
		"from fahb f,vwgongys g,vwdianc d\n" + 
		"where f.gongysb_id=g.id and f.diancxxb_id=d.id\n" + 
		" and f.jihkjb_id=2 and  f.daohrq>=ADD_MONTHS(to_date('"+visit.getString3()+"', 'yyyy-mm-dd'),-12) and  f.daohrq<=ADD_MONTHS(to_date('"+visit.getString4()+"', 'yyyy-mm-dd'),-12)\n" + 
		diancWhere + gongysWhere+
		"group by g.mingc\n" + 
		")group by mingc)group by mingc"+")group by rollup(mingc) order by  grouping(mingc) desc");

		JDBCcon con = new JDBCcon();
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		
		String ArrHeader[][]=new String[2][9];
		ArrHeader[0]=new String[] {"单位","合同量","合同量","实收量","实收量","差额","差额","到货率(%)","到货率(%)"};
	    ArrHeader[1]=new String[] {"单位","本期","同期","本期","同期","本期","同期","本期","同期"};
	 	int ArrWidth[]=new int[] {150,60,80,80,80,80,80,80,80,80};
		Table bt = new Table(rs,2,0,2);
		rt.setBody(bt);
		bt.setColAlign(2, Table.ALIGN_CENTER);
		if(rt.body.getRows()>2){
			rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		}
		//
		rt.body.ShowZero=false;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRow();//合并行
		rt.body.mergeFixedCols();//和并列
		rt.setTitle("合同完成情况(分矿)", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 3, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
//				rt.setDefaultTitle(12, 2, "单位:(吨)" ,Table.ALIGN_RIGHT);
		rt.setDefaultTitle(4, 4,visit.getString3()+"-"+visit.getString4(),Table.ALIGN_CENTER);
		rt.body.setPageRows(22);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 2, "审核人：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(9, 1, "填报人：", Table.ALIGN_LEFT);
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
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (cycle.getRequestContext().getParameter("diancxxb_id") != null) {//六个参数
			visit.setString1(cycle.getRequestContext().getParameter("diancxxb_id"));
			visit.setString2(cycle.getRequestContext().getParameter("gongysbid"));
			
			visit.setString3(cycle.getRequestContext().getParameter("riq1"));//签订日期
			visit.setString4(cycle.getRequestContext().getParameter("riq2")); 
			//市场采购、分矿
			
		}else{
			blnIsBegin = false;
			return;
		}
		blnIsBegin = true;
	}
}