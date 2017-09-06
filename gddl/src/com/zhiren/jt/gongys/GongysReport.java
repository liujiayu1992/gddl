package com.zhiren.jt.gongys;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class GongysReport extends BasePage {
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

	private String REPORT_NAME_Hetltj = "Hetltj";// 合同量统计
	private String mstrReportName = "";
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		//if (mstrReportName.equals(REPORT_NAME_Hetltj)) {
		return getGongys();
		//} else {
		//	return "无此报表";
		//}
	}
	// 合同量分厂分矿分矿分厂统计报表
	private String getGongys() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer buffer = new StringBuffer("");
		Report rt = new Report();
		if(visit.getLong1()==0){//无参数--打印供应商列表
			buffer.append("select");
			buffer.append("       gongysb.xuh as xuh,\n");
			buffer.append("       gongysb.bianm as bianm,\n");
			buffer.append("       gongysb.quanc as quanc,\n");
			buffer.append("       gongysb.mingc as mingc,\n");
			buffer.append("       shengfb.quanc as shengf,\n");
			buffer.append("       decode(fuid, 0, '大供应商', '小供应商') as leib\n");
			buffer.append("  from gongysb, shengfb\n");
			buffer.append(" where shengfb.id = gongysb.shengfb_id\n");
			buffer.append(" order by gongysb.xuh, gongysb.mingc");
			ResultSet rs = con.getResultSet(buffer,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			String[][] ArrHeader;
			int[] ArrWidth;
			ArrHeader = new String[1][6];
			ArrWidth = new int[] { 80, 100, 200,200,80,80};
			ArrHeader[0] = new String[] { "序号", "编码", "全称","名称","省份","类别"};
			rt.setBody(new Table(rs, 0, 0, 2));
			rt.body.setCells(1, 1, 20, 6, Table.PER_ALIGN, Table.ALIGN_LEFT);
			//
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.setTitle("供　应　商　查　询", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
//			rt.setDefaultTitle(1, 5, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
//			rt.setDefaultTitle(8, 8, "单位:(吨)" ,Table.ALIGN_RIGHT);
//			rt.setDefaultTitle(6, 2, "年份:"+getNianfValue().getId(),Table.ALIGN_RIGHT);
			rt.body.setPageRows(21);
//			rt.body.mergeFixedCols();
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 6, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		}else{//有参数gongysb_id--打印供应商明细
			int FixRows=12,meizRows=0,meikRows=0;
			Map map=new HashMap();
			try{

				buffer.append("select g.quanc,g.mingc,g.bianm,decode(g.fuid, 0, '大供应商', '小供应商') leib,shengfb.mingc shengf,gzb.mingc lisgys,\n");
				buffer.append("decode(g.xiny,1,'好',2,'中',3,'差')xinydj,g.youzbm,g.danwdz,g.dianh,g.faddbr,g.weitdlr,g.gongsxz,decode(g.shifss,1,'是','否')shifss\n" ); 
				buffer.append(",g.shangsdz,g.kaihyh,g.zhangh,g.shuih,g.meitly,g.CHUBNL,g.meiz,g.KAICNL,g.KAICNX,g.SHENGCNL,g.GONGYNL,g.LIUX,g.YUNSFS,g.YUNSNL\n" );
				buffer.append(",g.HEZNX,decode(g.RONGQGX,1,'好',2,'中',3,'差')RONGQGX,'市场量'||nvl(g.shiccgl,0)||'计划量'||nvl(g.zhongdht,0) hezqk,g.beiz\n" );
				buffer.append("from gongysb g,shengfb,gongysb gzb\n");
				buffer.append("where g.shengfb_id=shengfb.id(+) and g.fuid=gzb.id(+) and g.id="+visit.getLong1());
				ResultSet rs = con.getResultSet(buffer,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				//
				buffer.setLength(0);
				buffer.append("select mingc,zhi\n");
				buffer.append("from gongysmzb\n");
				buffer.append("where gongysb_id="+visit.getLong1());
				ResultSet rs2 = con.getResultSet(buffer,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs2.last();
				meizRows=rs2.getRow();
				rs2.beforeFirst();
				if(rs2.next()){
					map.put(rs2.getString("mingc"), rs2.getString("zhi"));
				}
				rs2.beforeFirst();
				//
				buffer.setLength(0);
				buffer.append("select meikxxb.id,meikxxb.mingc,meikxxb.bianm,meikxxb.leib\n");
				buffer.append("from gongysmkglb,meikxxb\n");
				buffer.append("where gongysmkglb.meikxxb_id=meikxxb.id and gongysmkglb.gongysb_id="+visit.getLong1()+"\n" );
				ResultSet rs1= con.getResultSet(buffer,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs1.last();
				meikRows=rs1.getRow(); 
				rs1.beforeFirst();
				String[][] ArrHeader;
				int[] ArrWidth;
//				ArrHeader = new String[1][7];
				ArrWidth = new int[] { 90, 110, 120,110,110,110,120};
				rt.setTitle("供　应　商　查　询", ArrWidth);
				rt.title.setRowHeight(2, 50);
				rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
				rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
				
				rt.setBody(new Table(FixRows+meizRows+meikRows,7));
				rt.body.setWidth(ArrWidth);
				rt.body.setPageRows(21);
//				rt.body.mergeFixedCols();
				rt.createDefautlFooter(ArrWidth);
				rt.setDefautlFooter(1, 7, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
				if(rs.next()){//固定信息
						rt.body.setCellValue(1, 1, "供应商基本信息");
						rt.body.setCellValue(1, 2, "全称:");
						rt.body.setCellValue(1, 3, rs.getString("quanc"));
						rt.body.setCellValue(1, 4, "简称:");
						rt.body.setCellValue(1, 5, rs.getString("mingc"));
						rt.body.setCellValue(1, 6, "编码:");
						rt.body.setCellValue(1, 7, rs.getString("bianm"));
						//
						rt.body.setCellValue(2, 1, "供应商基本信息");
						rt.body.setCellValue(2, 2, "类别:");
						rt.body.setCellValue(2, 3, rs.getString("leib"));
						rt.body.setCellValue(2, 4, "省份:");
						rt.body.setCellValue(2, 5, rs.getString("shengf"));
						rt.body.setCellValue(2, 6, "隶属关系:");
						rt.body.setCellValue(2, 7, rs.getString("lisgys"));
						//
						rt.body.setCellValue(3, 1, "公司整体状况");
						rt.body.setCellValue(3, 2, "信用等级:");
						rt.body.setCellValue(3, 3, rs.getString("xinydj"));
						rt.body.setCellValue(3, 4, "邮政编码:");
						rt.body.setCellValue(3, 5, rs.getString("youzbm"));
						rt.body.setCellValue(3, 6, "详细地址:");
						rt.body.setCellValue(3, 7, rs.getString("danwdz"));
						//
						rt.body.setCellValue(4, 1, "公司整体状况");
						rt.body.setCellValue(4, 2, "联系电话/传真:");
						rt.body.setCellValue(4, 3, rs.getString("dianh"));
						rt.body.setCellValue(4, 4, "法定代表人:");
						rt.body.setCellValue(4, 5, rs.getString("faddbr"));
						rt.body.setCellValue(4, 6, "委托代理人:");
						rt.body.setCellValue(4, 7, rs.getString("weitdlr"));
						//
						rt.body.setCellValue(5, 1, "公司整体状况");
						rt.body.setCellValue(5, 2, "公司性质:");
						rt.body.setCellValue(5, 3, rs.getString("gongsxz"));
						rt.body.setCellValue(5, 4, "是否为上市公司:");
						rt.body.setCellValue(5, 5, rs.getString("shifss"));
						rt.body.setCellValue(5, 6, "上市地址:");
						rt.body.setCellValue(5, 7, rs.getString("shangsdz"));
						//
						rt.body.setCellValue(6, 1, "公司整体状况");
						rt.body.setCellValue(6, 2, "开户银行:");
						rt.body.setCellValue(6, 3, rs.getString("kaihyh"));
						rt.body.setCellValue(6, 4, "帐号:");
						rt.body.setCellValue(6, 5, rs.getString("zhangh"));
						rt.body.setCellValue(6, 6, "税号:");
						rt.body.setCellValue(6, 7, rs.getString("shuih"));
						//
						rt.body.setCellValue(7, 1, "资源基本状况");
						rt.body.setCellValue(7, 2, "煤炭来源:(%)");
						rt.body.setCellValue(7, 3, rs.getString("meitly"));
						rt.body.setCellValue(7, 4, "储备能力:(亿吨)");
						rt.body.setCellValue(7, 5, rs.getString("CHUBNL"));
						rt.body.setCellValue(7, 6, "主要煤种:");
						rt.body.setCellValue(7, 7, rs.getString("meiz"));
						//
						rt.body.setCellValue(8, 1, "资源基本状况");
						rt.body.setCellValue(8, 2, "可开采能力:(亿吨)");
						rt.body.setCellValue(8, 3, rs.getString("KAICNL"));
						rt.body.setCellValue(8, 4, "可开采年限:");
						rt.body.setCellValue(8, 5, rs.getString("KAICNX"));
						rt.body.setCellValue(8, 6, "生产能力:(万吨/年)");
						rt.body.setCellValue(8, 7, rs.getString("SHENGCNL"));
						//
						rt.body.setCellValue(9, 1, "资源基本状况");
						rt.body.setCellValue(9, 2, "供应能力:(万吨/年)");
						rt.body.setCellValue(9, 3, rs.getString("GONGYNL"));
						rt.body.setCellValue(9, 4, "主要流向:");
						rt.body.setCellValue(9, 5, rs.getString("LIUX"));
						rt.body.setCellValue(9, 6, "主要运输方式:");
						rt.body.setCellValue(9, 7, rs.getString("YUNSFS"));
						//
						rt.body.setCellValue(10, 1, "供应商基本信息");
						rt.body.setCellValue(10, 2, "运输能力:");
						rt.body.setCellValue(10, 3, rs.getString("YUNSNL"));
						rt.body.setCellValue(10, 4, "");
						rt.body.setCellValue(10, 5,"");
						rt.body.setCellValue(10, 6, "");
						rt.body.setCellValue(10, 7, "");
						//
						rt.body.setCellValue(11, 1, "合作情况");
						rt.body.setCellValue(11, 2, "合作年限:");
						rt.body.setCellValue(11, 3, rs.getString("HEZNX"));
						rt.body.setCellValue(11, 4, "融洽关系:");
						rt.body.setCellValue(11, 5, rs.getString("RONGQGX"));
						rt.body.setCellValue(11, 6, "合作情况:");
						rt.body.setCellValue(11, 7, rs.getString("hezqk"));
						//
						rt.body.setCellValue(12, 1, "备注");
						rt.body.setCellValue(12, 2, rs.getString("beiz"));
						rt.body.setCellValue(12, 3, "");
						rt.body.setCellValue(12, 4, "");
						rt.body.setCellValue(12, 5, "");
						rt.body.setCellValue(12, 6, "");
						rt.body.setCellValue(12, 7, "");
						//
						rs.close();
				}
			
				if(rs2.next()){//煤质信息
						rt.body.setCellValue(1+FixRows, 1, "煤种");
						rt.body.setCellValue(1+FixRows, 2, "名称:");
						rt.body.setCellValue(1+FixRows, 3,  map.get("").toString());
						rt.body.setCellValue(1+FixRows, 4, "全水分Mt:(%)");
						rt.body.setCellValue(1+FixRows, 5,  map.get("全水分").toString());
						rt.body.setCellValue(1+FixRows, 6, "灰分Ad:(%)");
						rt.body.setCellValue(1+FixRows, 7,  map.get("灰分").toString());
						
						rt.body.setCellValue(2+FixRows, 1, "煤种");
						rt.body.setCellValue(2+FixRows, 2, "挥发分Vdaf:(%)");
						rt.body.setCellValue(2+FixRows, 3,  map.get("挥发分").toString());
						rt.body.setCellValue(2+FixRows, 4, "全硫分St,d:(%)");
						rt.body.setCellValue(2+FixRows, 5,  map.get("硫分").toString());
						rt.body.setCellValue(2+FixRows, 6, "发热量Qnet,ar:(Kal/kg)");
						rt.body.setCellValue(2+FixRows, 7,  map.get("发热量").toString());
						
						rt.body.setCellValue(3+FixRows, 1, "煤种");
						rt.body.setCellValue(3+FixRows, 2, "粒度:(mm)");
						rt.body.setCellValue(3+FixRows, 3,  map.get("粒度").toString());
						rt.body.setCellValue(3+FixRows, 4, "哈氏可磨系数:");
						rt.body.setCellValue(3+FixRows, 5,  map.get("哈氏可磨系数").toString());
						rt.body.setCellValue(3+FixRows, 6, "灰熔点T2:(℃)");
						rt.body.setCellValue(3+FixRows, 7,  map.get("灰熔点").toString());
						
						rt.body.setCellValue(4+FixRows, 1, "煤种");
						rt.body.setCellValue(4+FixRows, 2, "碳Cdaf:(%)");
						rt.body.setCellValue(4+FixRows, 3,  map.get("碳").toString());
						rt.body.setCellValue(4+FixRows, 4, "氢Hdaf:(%)");
						rt.body.setCellValue(4+FixRows, 5,  map.get("氢").toString());
						rt.body.setCellValue(4+FixRows, 6, "氧Odaf:(%)");
						rt.body.setCellValue(4+FixRows, 7,  map.get("氧").toString());
						
						rt.body.setCellValue(5+FixRows, 1, "煤种");
						rt.body.setCellValue(5+FixRows, 2, "氮Ndaf:(%)");
						rt.body.setCellValue(5+FixRows, 3,  map.get("氮").toString());
						rt.body.setCellValue(5+FixRows, 4, "");
						rt.body.setCellValue(5+FixRows, 5, "");
						rt.body.setCellValue(5+FixRows, 6, "");
						rt.body.setCellValue(5+FixRows, 7, "");
						rs2.close();
				}
				
				if(rs1.next()){//煤矿信息
					for(int i=1;i<=meikRows;i++){
						rt.body.setCellValue(FixRows+meizRows+i, 1, "矿点");
						rt.body.setCellValue(FixRows+meizRows+i, 2, "名称:");
						rt.body.setCellValue(FixRows+meizRows+i, 3, rs1.getString("mingc"));
						
						rt.body.setCellValue(FixRows+meizRows+i, 4, "编码:");
						rt.body.setCellValue(FixRows+meizRows+i, 5, rs1.getString("bianm"));
						
						rt.body.setCellValue(FixRows+meizRows+i, 6, "类别:");
						rt.body.setCellValue(FixRows+meizRows+i, 7, rs1.getString("leib"));
					}
					rs1.close();
				}
				rt.body.mergeFixedCol(1);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				con.Close();
			}
//			rt.body.setHeaderData(ArrHeader);
			
			
//			rt.setDefaultTitle(1, 5, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
//			rt.setDefaultTitle(8, 8, "单位:(吨)" ,Table.ALIGN_RIGHT);
//			rt.setDefaultTitle(6, 2, "年份:"+getNianfValue().getId(),Table.ALIGN_RIGHT);
		}
		
		

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
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
//		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
//			visit.setActivePageName(getPageName().toString());
////			visit.setList1(null);
////			visit.setDropDownBean1(null);
////			visit.setProSelectionModel1(null);
////			visit.setDropDownBean2(null);
////			visit.setProSelectionModel2(null);
////			visit.setDropDownBean3(null);
////			visit.setProSelectionModel3(null);
////			visit.setDropDownBean4(null);
////			visit.setProSelectionModel4(null);
////			setTreeid("");
//		}
		// mstrReportName = "Fenxrb";
		visit.setLong1(0);
		if (cycle.getRequestContext().getParameter("gongysb_id") != null) {
			visit.setLong1(Long.parseLong(cycle.getRequestContext().getParameter("gongysb_id")));
		}
		blnIsBegin = true;

	}
	//单位
    public IDropDownBean getDanwSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean1()==null){
    		 ((Visit) getPage().getVisit()).setDropDownBean1((IDropDownBean)getDanwSelectModel().getOption(0));
    	}
        return  ((Visit) getPage().getVisit()).getDropDownBean1();
    }

    public void setDanwSelectValue(IDropDownBean Value) {
    	((Visit) getPage().getVisit()).setDropDownBean1(Value);
    }


    public void setDanwSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel1(value);
    }

    public IPropertySelectionModel getDanwSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
            getDanwSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel1();
    }

    public void getDanwSelectModels() {
    	 String sql = 
         	"select id,mingc,jib\n" +
         	"from(\n" + 
         	" select id,mingc,0 as jib\n" + 
         	" from diancxxb\n" + 
         	" where id="+ ((Visit) getPage().getVisit()).getDiancxxb_id()+"\n" + 
         	" union\n" + 
         	" select *\n" + 
         	" from(\n" + 
         	" select id,mingc,level as jib\n" + 
         	"  from diancxxb\n" + 
         	" start with fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"\n" + 
         	" connect by fuid=prior id\n" + 
         	" order SIBLINGS by  xuh)\n" + 
         	" )\n" + 
         	" order by jib";
         List dropdownlist = new ArrayList();
 		JDBCcon con = new JDBCcon();
 		try {
 			ResultSet rs = con.getResultSet(sql);
 			while (rs.next()) {
 				int id = rs.getInt("id");
 				String mc = rs.getString("mingc");
 				int jib=rs.getInt("jib");
 				String nbsp=String.valueOf((char)0xA0);
 				for(int i=0;i<jib;i++){
 					mc=nbsp+nbsp+nbsp+nbsp+mc;
 				}
 				dropdownlist.add(new IDropDownBean(id, mc));
 			}
 			rs.close();
 		} catch (Exception e) {
 			e.printStackTrace();
 		} finally {
 			con.Close();
 		}
         ((Visit) getPage().getVisit()).setProSelectionModel1(new IDropDownModel(dropdownlist)) ;
         return ;
    }
    // 供应商
    public IDropDownBean getGongysDropDownValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getGongysDropDownModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }
    public void setGongysDropDownValue(IDropDownBean Value) {
//    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean2()!=null){
//	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean2().getId()){
//	    		((Visit) getPage().getVisit()).setBooleanData1(true);
//	    	}else{
//	    		((Visit) getPage().getVisit()).setBooleanData1(false);
//	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean2(Value);
//    	}
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
    	String sql="select id,mingc\n" +
    	"from gongysb\n" + 
    	"where gongysb.fuid=0";
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"全部")) ;
        return ;
    }
    //年份
	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit()).setDropDownBean3(getIDropDownBean(getNianfModel(),new Date().getYear()+1900));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}
	public void setNianfValue(IDropDownBean Value) {
//		if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean3()!=null){
//			if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean3().getId()){
//	    		((Visit) getPage().getVisit()).setBooleanData2(true);
//	    	}else{
//	    		((Visit) getPage().getVisit()).setBooleanData2(false);
//	    	}
			 ((Visit) getPage().getVisit()).setDropDownBean3(Value);
//		}
	}
	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
    public void getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = DateUtil.getYear(new Date())-2; i <= DateUtil.getYear(new Date()); i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		 ((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(listNianf)) ;
	}
    private IDropDownBean getIDropDownBean(IPropertySelectionModel model,long id) {
        int OprionCount;
        OprionCount = model.getOptionCount();
        for (int i = 0; i < OprionCount; i++) {
            if (((IDropDownBean) model.getOption(i)).getId() == id) {
                return (IDropDownBean) model.getOption(i);
            }
        }
        return null;
    }
    //类型
    public IDropDownBean getLeixSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getLeixSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean4();
    }
    public void setLeixSelectValue(IDropDownBean Value) {
//    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean4()!=null){
//	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean4().getId()){
//	    		((Visit) getPage().getVisit()).setBooleanData1(true);
//	    	}else{
//	    		((Visit) getPage().getVisit()).setBooleanData1(false);
//	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean4(Value);
//    	}
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
        List list=new ArrayList();
        list.add(new IDropDownBean(1,"分厂分矿"));
        list.add(new IDropDownBean(2,"分矿分厂"));
        list.add(new IDropDownBean(3,"分厂"));
        list.add(new IDropDownBean(4,"分矿"));
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(list)) ;
        return ;
    }
    //ext 
    //tree
    public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}
	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}
	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}
	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
	//
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}
