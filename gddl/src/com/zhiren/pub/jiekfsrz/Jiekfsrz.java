package com.zhiren.pub.jiekfsrz;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Jiekfsrz extends BasePage {
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
	private int _Flag = 0;

	public int getFlag() {
		JDBCcon con = new JDBCcon();
		String sql = "SELECT ZHUANGT FROM JICXXB WHERE MINGC = '报表中的厂别是否显示'";
		ResultSet rs = con.getResultSet(sql);
		try {
			if (rs.next()) {
				_Flag = rs.getInt("ZHUANGT");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return _Flag;
	}

	public void setFlag(int _value) {
		_Flag = _value;
	}
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		//if (mstrReportName.equals(REPORT_NAME_Hetltj)) {
		return getRiz();
		//} else {
		//	return "无此报表";
		//}
	}
	// 合同量分厂分矿分矿分厂统计报表
	private String getRiz() {
		JDBCcon con = new JDBCcon();
		StringBuffer buffer = new StringBuffer("");
		String Strzhixzt="";
		String Strrenwmc="";
		if(getLeixSelectValue().getId()!=-2){//全部
			Strzhixzt="jiekrwb.zhixzt="+getLeixSelectValue().getId()+" and";
		}
		if(getrenwValue().getId()!=-1){
			Strrenwmc="and  renwmcb.renwms='"+getrenwValue().getValue()+"'";
		}
		String sql=
			"select renwms,renwbs,decode(renllx,0,'增加',1,'删除','修改')caoz,decode(zhixzt,-1,'失败',1,'成功','未执行')zhixzt,zhixsj,cuowlb,mingllx,zhixbz\n" +
			"from jiekrwb,renwmcb\n" + 
			"where "+Strzhixzt+" to_char(zhixsj,'YYYY-MM-DD')>='"+getRiq1()+"'and jiekrwb.renwmc=renwmcb.renw  and to_char(zhixsj,'YYYY-MM-DD')<='"+getRiq2()+"'"+Strrenwmc;

		ResultSet rs = con.getResultSet(new StringBuffer(sql),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		
		ArrHeader = new String[1][8];
		ArrWidth = new int[] {100,50,50,50,100,50,50,350};
		ArrHeader[0] = new String[] { "任务名称", "标识","操作","执行状态","执行时间","错误代码","类别","错误消息"};
		rt.setBody(new Table(rs, 1, 0, 1));
		//
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.setTitle("发 送 日 志 查 看", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1,3, "制表单位:" +((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_LEFT);
		rt.body.setPageRows(21);
		rt.body.mergeFixedCols();
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 6, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		

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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1((null));
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			setrenwModel(null);
		}
		blnIsBegin = true;
		if(chang1){
			getrenwModels();
			chang1=false;
		}
		if(chang2){
			getrenwModels();
			chang2=false;
		}
	}
	//电厂名称
	public IPropertySelectionModel getrenwModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getrenwModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
	
	public IDropDownBean getrenwValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getrenwModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}
	
	public void setrenwValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getrenwModels() {
		List list=new ArrayList();
		list.add(new IDropDownBean(-1,"全部") );
		String sql = 
			"select rownum,renwms\n" +
			"from(\n" + 
			"select distinct renwms\n" + 
			"from jiekrwb,renwmcb\n" + 
			"where jiekrwb.renwmc=renwmcb.renw and to_char(jiekrwb.zhixsj,'YYYY-MM-DD')>='"+getRiq1()+"' and to_char(jiekrwb.zhixsj,'YYYY-MM-DD')<='"+getRiq2()+
			"')";
		 setrenwModel(new IDropDownModel(list,sql)) ;
		 return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setrenwModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}
	private String riq1;
	private boolean chang1,chang2;
	public String getRiq1(){
		if(riq1==null||riq1.equals("")){
			riq1=DateUtil.FormatDate(new Date());
		}
		return riq1;
	}
	public void setRiq1(String value){
		if(!value.equals(riq1)){
			chang1=true;
		}
		riq1=value;
	}
	private String riq2;
	public String getRiq2(){
		if(riq2==null||riq2.equals("")){
			riq2=DateUtil.FormatDate(new Date());
		}
		return riq2;
	}
	public void setRiq2(String value){
		if(!value.equals(riq2)){
			chang2=true;
		}
		riq2=value;
	}
  
    //类型
    public IDropDownBean getLeixSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getLeixSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean4();
    }
    public void setLeixSelectValue(IDropDownBean Value) {
	    	((Visit) getPage().getVisit()).setDropDownBean4(Value);
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
        list.add(new IDropDownBean(-2,"全部"));
        list.add(new IDropDownBean(0,"未执行"));
        list.add(new IDropDownBean(-1,"失败"));
        list.add(new IDropDownBean(1,"成功"));
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(list)) ;
        return ;
    }
   
}
