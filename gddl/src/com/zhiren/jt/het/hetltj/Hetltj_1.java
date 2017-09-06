package com.zhiren.jt.het.hetltj;
/* 
* 时间：2009-08-16
* 作者： ll
* 修改内容：“合同统计“改为”合同煤量统计表
* 		   
*/ 
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Hetltj_1 extends BasePage {
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

//	private int _Flag = 0;
//
//	public int getFlag() {
//		JDBCcon con = new JDBCcon();
//		String sql = "SELECT ZHUANGT FROM JICXXB WHERE MINGC = '报表中的厂别是否显示'";
//		ResultSet rs = con.getResultSet(sql);
//		try {
//			if (rs.next()) {
//				_Flag = rs.getInt("ZHUANGT");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return _Flag;
//	}
//
//	public void setFlag(int _value) {
//		_Flag = _value;
//	}

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getHetltj();
	}
	private String getHetltj() {
		Visit visit = (Visit) getPage().getVisit();
		String whereShenhzt=" and  (case when h.liucztb_id=1 then 1 else 0 end)="+visit.getString4();
		if(visit.getString4().equals("-1")){
			whereShenhzt="";
		}
		JDBCcon con = new JDBCcon();
		StringBuffer buffer = new StringBuffer("");
		buffer.append("select nvl(hetbh,'小计') het,xufdwmc,gongfdwmc,sum(h1),\n" +
				"sum(h2),sum(h3),sum(h4),sum(h5),sum(h6),sum(h7),sum(h8),sum(h9),sum(h10),sum(h11),sum(h12),\n" + 
				"sum(h1)+sum(h2)+sum(h3)+sum(h4)+sum(h5)+sum(h6)+sum(h7)+sum(h8)+sum(h9)+sum(h10)+sum(h11)+sum(h12)hej\n" + 
				"from (\n" + 
				"select hetbh,h.xufdwmc,h.gongfdwmc,\n" + 
				"sum(decode(to_char(riq,'MM'),'01',hetl,0)) as h1,\n" + 
				"sum(decode(to_char(riq,'MM'),'02',hetl,0)) as h2,\n" + 
				"sum(decode(to_char(riq,'MM'),'03',hetl,0)) as h3,\n" + 
				"sum(decode(to_char(riq,'MM'),'04',hetl,0)) as h4,\n" + 
				"sum(decode(to_char(riq,'MM'),'05',hetl,0)) as h5,\n" + 
				"sum(decode(to_char(riq,'MM'),'06',hetl,0)) as h6,\n" + 
				"sum(decode(to_char(riq,'MM'),'07',hetl,0)) as h7,\n" + 
				"sum(decode(to_char(riq,'MM'),'08',hetl,0)) as h8,\n" + 
				"sum(decode(to_char(riq,'MM'),'09',hetl,0)) as h9,\n" + 
				"sum(decode(to_char(riq,'MM'),'10',hetl,0)) as h10,\n" + 
				"sum(decode(to_char(riq,'MM'),'11',hetl,0)) as h11,\n" + 
				"sum(decode(to_char(riq,'MM'),'12',hetl,0)) as h12\n" + 
				"from hetb h,hetslb hs,diancxxb d,vwgongys g\n" + 
				"where h.id=hs.hetb_id   and h.diancxxb_id=d.id and h.gongysb_id=g.id\n" + 
				"  and h.diancxxb_id  in (" +
				"   select id from diancxxb\n"+
				"   start with id=(select id from diancxxb where mingc='"+visit.getString1()+
				"')   connect by fuid=prior id\n"+
				") and  g.dqmc='"+visit.getString2()+"' \n" + 
				"and to_char(riq,'yyyy')='"+visit.getString3()+"'\n" + whereShenhzt+
				"group by hs.hetb_id,hetbh,h.xufdwmc,h.gongfdwmc\n" + 
				")group by grouping  sets((),(hetbh,xufdwmc,gongfdwmc))order by grouping(hetbh)desc,hetbh");
		ResultSet rs = con.getResultSet(buffer,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		ArrHeader = new String[1][16];
		ArrWidth = new int[] { 120, 120, 120,50,50,50,50,50,50,50,50,50,50,50,50,50};
		ArrHeader[0] = new String[] { "合同号", "需方", "供方","一月","二月","三月","四月","五月","六月",
				"七月","八月","九月","十月","十一月","十二月","合计"};
		rt.setBody(new Table(rs, 1, 0, 2));
		//
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.setTitle("合   同   煤   量   明   细", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		String zhibdw=visit.getDiancqc();
		if(zhibdw.equals("北京大唐燃料有限公司")&&visit.getRenyjb()==2){
			zhibdw="大唐国际发电股份有限公司燃料管理部";
		}
		rt.setDefaultTitle(1, 5, "制表单位:" +zhibdw,Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 9, "单位:(吨)" ,Table.ALIGN_RIGHT);
		rt.setDefaultTitle(6, 2, "年份:"+visit.getString3(),Table.ALIGN_RIGHT);
		rt.body.setPageRows(21);
		rt.body.mergeFixedCols();
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 16, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (cycle.getRequestContext().getParameter("diancxxb_id") != null) {// 
			visit.setString1(cycle.getRequestContext().getParameter("diancxxb_id"));
			visit.setString2(cycle.getRequestContext().getParameter("gongysbid"));
			visit.setString3(cycle.getRequestContext().getParameter("nianf"));//签订日期
			visit.setString4(cycle.getRequestContext().getParameter("shenhzt"));
		}
		blnIsBegin = true;

	}
}