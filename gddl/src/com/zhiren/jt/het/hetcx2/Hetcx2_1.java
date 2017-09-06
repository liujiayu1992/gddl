package com.zhiren.jt.het.hetcx2;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Hetcx2_1 extends BasePage {
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
	public int getZhuangt() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	public void setZhuangt(int _value) {
		((Visit) getPage().getVisit()).setInt1(_value);
	}

	private String mstrReportName = "";
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		//if (mstrReportName.equals(REPORT_NAME_Hetltj)) {
		return getHetcx();
		//} else {
		//	return "无此报表";getTreeScript
		//}
	}
	private String getHetcx() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer buffer = new StringBuffer("");
		List list=new ArrayList();
		String strWhere="";
		if(visit.getString2()==null||visit.getString2().equals("null")||visit.getString2().equals("小计")){
			strWhere="";
		}else{
			try {
				String a=new String(visit.getString2().getBytes("Iso8859-1"),   "GB2312");
			strWhere="and jihkjb.mingc='"+a+"'\n";
//				System.out.println("a"+a);
			} catch (UnsupportedEncodingException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}

//			  String   tmp   =   new   String(strUnicode.getBytes("UTF-16BE"),   "GB2312");   
//			System.out.println(strWhere);
		}
//审核条件
		String shenhWhere="";
		String leibWhere="";
//		StringBuffer ber = new StringBuffer("");
		String youx="true";//or (to_char(hetb.guoqrq,'yyyy-mm-dd')<='"+visit.getString6()+"'  and to_char(hetb.guoqrq,'yyyy-mm-dd')>='"+visit.getString5()+"')
//		System.out.println(visit.getString9()+"9999");
//		if(visit.getString9()!=null&&visit.getString9().equals(youx)){
//			ber.append(" and (to_char(hetb.guoqrq,'yyyy-mm-dd')<='"+visit.getString6()+"'  and to_char(hetb.qisrq,'yyyy-mm-dd')>='"+visit.getString5()+"')");
//		}
		
		if(visit.getString7().equals("-1")){//全部
			shenhWhere="";
			
		}else if(visit.getString7().equals("0")){//未审核
			shenhWhere=" and hetb.liucztb_id=0 ";
		}else{//已审核
			shenhWhere=" and hetb.liucztb_id=1 ";
		}
//		System.out.println("leibWhere"+visit.getString8());
		//visit.getString8().equals("-1")||
		if(visit.getString8()==null){//全部
			leibWhere="";
			
		}else if(visit.getString8().equals("0")){//未审核
			leibWhere=" and hetb.leib=0 ";
		}else if(visit.getString8().equals("1")){//已审核
			leibWhere=" and hetb.leib=1 ";
		}else if(visit.getString8().equals("2")){
			leibWhere=" and hetb.leib=2";
		}
		if(visit.getString3()!=null){//如果是签订日期
		buffer.append("select hetb.id,hetb.hetbh,hetb.xufdwmc,hetb.diancxxb_id,hetb.gongfdwmc,jihkjb.mingc jihkj,sum(hetslb.hetl/10000)hetl,gethetcxRel(hetb.id)rel,gethetcxJiag(hetb.id)jiag,\n" +
				"to_char(hetb.qiandrq,'YYYY-MM-DD')qiandrq,to_char(hetb.qisrq,'YYYY-MM-DD')qisrq,to_char(hetb.guoqrq,'YYYY-MM-DD')guoqrq,b.id fid,b.hetbh fbh\n" + 
				"from hetb,hetslb,jihkjb,hetb b\n" + 
				"where hetb.diancxxb_id||hetb.fuid=b.id(+) and hetb.id=hetslb.hetb_id(+)  and hetb.jihkjb_id=jihkjb.id\n" + 
				"and hetb.diancxxb_id =" +visit.getString1()+
//				"in(\n" + 
//				"select id\n" + 
//				" from diancxxb\n" + 
//				" start with id="+visit.getString1()+"\n" + 
//				" connect by  fuid= prior id\n" + 
//				")" +
				"\n" + 
				strWhere+ shenhWhere+leibWhere+
				"and to_char(hetb.qiandrq,'yyyy-mm-dd')>='"+visit.getString3()+"'\n" + 
				"and to_char(hetb.qiandrq,'yyyy-mm-dd')<='"+visit.getString4()+"'\n" + 
				"group by jihkjb.mingc,hetb.diancxxb_id,hetb.id,hetb.hetbh,hetb.qiandrq,hetb.qisrq,hetb.guoqrq,hetb.xufdwmc,hetb.gongfdwmc,b.id ,b.hetbh");
		}else{
		buffer.append("select hetb.id,hetb.hetbh,hetb.xufdwmc,hetb.diancxxb_id,hetb.gongfdwmc,jihkjb.mingc jihkj,sum(hetslb.hetl/10000)hetl,gethetcxRel(hetb.id)rel,gethetcxJiag(hetb.id)jiag,\n" +
				"to_char(hetb.qiandrq,'YYYY-MM-DD')qiandrq,to_char(hetb.qisrq,'YYYY-MM-DD')qisrq,to_char(hetb.guoqrq,'YYYY-MM-DD')guoqrq,b.id fid,b.hetbh fbh\n" + 
				"from hetb,hetslb,jihkjb,hetb b\n" + 
				"where  hetb.diancxxb_id||hetb.fuid=b.id(+) and hetb.id=hetslb.hetb_id(+)  and hetb.jihkjb_id=jihkjb.id\n" + 
				"and hetb.diancxxb_id =" +visit.getString1()+
//				"in(\n" + 
//				"select id\n" + 
//				" from diancxxb\n" + 
//				" start with id="+visit.getString1()+"\n" + 
//				" connect by  fuid= prior id\n" + 
//				")" +
				"\n" + 
				strWhere+ shenhWhere+leibWhere+
				"and ((to_char(hetb.guoqrq,'yyyy-mm-dd')>='"+visit.getString5()+"'  and to_char(hetb.guoqrq,'yyyy-mm-dd')<='"+visit.getString6()+"')\n" + 
				")\n" + 
				"group by jihkjb.mingc,hetb.diancxxb_id,hetb.id,hetb.hetbh,hetb.qiandrq,hetb.qisrq,hetb.guoqrq,hetb.xufdwmc,hetb.gongfdwmc,b.id ,b.hetbh ");
				
		}
//		System.out.println(buffer.toString());
			ResultSet rs = con.getResultSet(buffer,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		try{
			while(rs.next()){
				list.add(new Hetcxbean2_1(
					rs.getString("id"),
					rs.getString("hetbh"),
					rs.getString("xufdwmc"),
					rs.getString("gongfdwmc"),
					rs.getString("hetl"),
					rs.getString("qiandrq"),
					rs.getString("jihkj"),
					"",
					"",
					rs.getString("rel"),
					rs.getString("jiag"),
					rs.getString("qisrq"),
					rs.getString("guoqrq"),
					rs.getString("fid"),
					rs.getString("fbh")
				));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		ArrHeader = new String[1][11];
		ArrWidth = new int[] { 130, 180, 180,60,60,60,70,70,70,70,70};
		ArrHeader[0] = new String[] { "合同号", "需方单位名称", "供方单位名称","合同量(万吨)","热量(千卡/千克)","价格(元/吨)","计划口径","签订日期","起始日期","过期日期","主合同号"};
		rt.setBody(new Table(list.size()+1, 11));
		rt.body.setHeaderData(ArrHeader);
		for(int i=2;i<=list.size()+1;i++){
			Hetcxbean2_1 bean=(Hetcxbean2_1)list.get(i-2);
			rt.body.setCellValue(i, 1, "<a target=_blank href="+MainGlobal.getHomeContext(this)+"/app?service=page/Shenhrz&hetb_id="+bean.getId()+">"+bean.getHetbh()+"</a>");
			rt.body.setCellValue(i, 2, bean.getXuf());
			rt.body.setCellValue(i, 3, bean.getGongf());
			rt.body.setCellValue(i, 4, bean.getHetl());
			rt.body.setCellValue(i, 5, bean.getRel());
			rt.body.setCellValue(i, 6, bean.getJiag());
			rt.body.setCellValue(i, 7, bean.getJihkj());
			rt.body.setCellValue(i, 8, bean.getQiandrq());
			rt.body.setCellValue(i, 9, bean.getQisrq());
			rt.body.setCellValue(i, 10, bean.getGuoqrq());
			if(bean.getFbh()==null||bean.getFbh().equals("")){
				rt.body.setCellValue(i, 11, "");
			}else{
				rt.body.setCellValue(i, 11, "<a target=_blank href="
						+ MainGlobal.getHomeContext(this)
						+ "/app?service=page/Shenhrz&hetb_id=" + bean.getFid() + ">"
						+ bean.getFbh() + "</a>");
			}
		}
		rt.setTitle("合   同   查   询", ArrWidth);
//		rt.title.setRowHeight(2, 30);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
//		rt.setDefaultTitle(1, 3, "检验日期:" +"",Table.ALIGN_LEFT);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(21);
		//rt.body.mergeFixedCols();
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 10, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);

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
		if (cycle.getRequestContext().getParameter("diancxxb_id") != null) {//六个参数+一个参数
			visit.setString1(cycle.getRequestContext().getParameter("diancxxb_id"));
			visit.setString2(cycle.getRequestContext().getParameter("jihkjmc"));
			
			visit.setString3(cycle.getRequestContext().getParameter("riq1"));//签订日期
			visit.setString4(cycle.getRequestContext().getParameter("riq2"));
			visit.setString5(cycle.getRequestContext().getParameter("riq3"));//有效日期
			visit.setString6(cycle.getRequestContext().getParameter("riq4"));
			visit.setString7(cycle.getRequestContext().getParameter("liucztb_id"));
			
			visit.setString8(cycle.getRequestContext().getParameter("leib"));
			visit.setString9(cycle.getRequestContext().getParameter("youx"));
			
		}else{
			blnIsBegin = false;
			return;
		}
		blnIsBegin = true;
	}
}
