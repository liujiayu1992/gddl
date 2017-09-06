package com.zhiren.gs.bjdt.monthreport;

import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.IDropDownModel;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

public class Yuebsjdb extends BasePage {
	public boolean getRaw() {
		return true;
	}
	private String userName="";
	
	public void setUserName(String value) {
		userName=((Visit) getPage().getVisit()).getRenymc();
	}
	public String getUserName() {
		return userName;
	}
	
	private boolean reportShowZero(){
		return ((Visit) getPage().getVisit()).isReportShowZero();
	}
	
	private int _CurrentPage = -1;
	
	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	
	public int getAllPages() {
		return _AllPages;
	}
	
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
//	***************设置消息框******************//
	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	
	
	public String getTianzdwQuanc(){
		return getTianzdwQuanc(getDiancxxbId());
	}
//	 得到单位全称
	public String getTianzdwQuanc(long gongsxxbID) {
		String _TianzdwQuanc = "";
		JDBCcon cn = new JDBCcon();

		try {
			ResultSet rs = cn
					.getResultSet(" select quanc from diancxxb where id="
							+ gongsxxbID);
			while (rs.next()) {
				_TianzdwQuanc = rs.getString("quanc");
			}
			if (_TianzdwQuanc.equals("北京大唐燃料有限公司")) {
				_TianzdwQuanc = "大唐国际发电股份有限公司燃料管理部";
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return _TianzdwQuanc;
	}
	
	public long getDiancxxbId(){
		
		return ((Visit)getPage().getVisit()).getDiancxxb_id();
	}
	
	public boolean isJTUser(){
		return ((Visit)getPage().getVisit()).isJTUser();
	}

	private boolean blnIsBegin = false;
	private String leix="";

//	形成报表的html
	public  String getPrintTable(){
		setMsg(null);
		if(!blnIsBegin){
			return "";
		}
		blnIsBegin=false;
		String _Danwqc=getTianzdwQuanc();
		 long  lngDiancxxbID= ((Visit) getPage().getVisit()).getDiancxxb_id();
		JDBCcon cn = new JDBCcon();
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strHeadDate=getNianfValue().getValue()+"年"+getYuefValue().getValue()+"月";
//		String strYear=getNianfValue().getValue() +"-01-01";
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("select dianc.mingc, nvl(di16meil,0) di16meil,nvl(di01meil,0) di01meil,nvl(di01meil,0)-nvl(di16meil,0) cha1,nvl(di03meil,0) di03meil,nvl(di03meil,0)-nvl(di16meil,0) cha2,nvl(di04meil,0) as di04meil,nvl(di04meil,0)-nvl(di16meil,0) as cha3 from\n");
		sbsql.append("     (select * from diancxxb where diancxxb.fuid=").append(lngDiancxxbID).append(") dianc \n");
		sbsql.append("     ,(select sum(kuangfgyl) di16meil,diancxxb_id from  (select * from diaor16bb where riq=to_date('"+strDate+"','yyyy-MM-dd') and fenx='本月') group by diancxxb_id) di16 \n");
		sbsql.append("     ,(select sum(meitsg) di01meil,diancxxb_id from  (select * from  diaor01bb where riq=to_date('"+strDate+"','yyyy-MM-dd') and fenx='本月') group by diancxxb_id) di01 \n");
		sbsql.append("     ,(select sum(jincsl) di03meil,diancxxb_id from  (select * from diaor03bb where riq=to_date('"+strDate+"','yyyy-MM-dd') and fenx='本月') group by diancxxb_id) di03 \n");
		sbsql.append("     ,(select sum(jincsl) di04meil,diancxxb_id from  (select * from diaor04bb where riq=to_date('"+strDate+"','yyyy-MM-dd') and fenx='本月') group by diancxxb_id) di04 \n");
		sbsql.append("     ,(select * from dianckjpxb where kouj='月报') px \n");
		sbsql.append("where dianc.id=di16.diancxxb_id(+) and dianc.id=di01.diancxxb_id(+)"+
           "and di03.diancxxb_id(+)=dianc.id and  dianc.id=di04.diancxxb_id(+) and px.diancxxb_id(+)=dianc.id order by px.xuh\n");
//		System.out.println(sbsql.toString());
		ResultSet rs=cn.getResultSet(sbsql.toString());
		Report rt=new Report();
		 
		//定义表头数据
		String ArrHeader[][]=null;
		int ArrWidth[]=null;
			ArrHeader=new String[2][8];
			ArrHeader[0]=new String[] {"电厂","电生16-1表","调燃01表","调燃01表","调燃03表","调燃03表","调燃04表","调燃04表"};
			ArrHeader[1]=new String[] {"电厂","煤量 ","煤量","差异","煤量","差异","煤量","差异","煤量","差异"};
			ArrWidth=new int[] {100,90,90,90,90,90,90,90};
			rt.setBody(new Table(rs,ArrHeader.length,0,1));
		
		//设置页标题
		rt.setTitle("月报数量对比",ArrWidth);
		rt.setDefaultTitle(1,3,"填报单位:"+_Danwqc,Table.ALIGN_LEFT);
		rt.setDefaultTitle(4,2,strHeadDate,Table.ALIGN_CENTER);
//		rt.setDefaultTitle(rt.title.getCols()-1,2,"调燃02表",Table.ALIGN_RIGHT);
		
		//数据
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);//表头数据
		
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
//		rt.body.ShowZero=reportShowZero();
		//页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,2,"制表人:"+getUserName(),Table.ALIGN_LEFT);
		rt.setDefautlFooter(4,2,"审核:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );
		
		
		//设置页数
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	private boolean _QueryClick = false;
	
	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
			getPrintTable();
		}
		
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			visit.setString1("");
			visit.setDefaultTree(null);
//			getDiancmcModels();
//			getMeikdqmcModels();
			getSelectData();
		}
		
	
		//mstrReportName="diaor04bb";
		
		if(getYuefValue()!=null){
			String yuef = getYuefValue().getValue();
			if(yuef.length()==1){
				leix=leix+"0"+yuef;
			}else{
				leix=leix+yuef;
			}
			if(! leix.substring(0,1).equals("2")){
				//getDiancmcModels(); 
			}
		}
		setUserName(visit.getRenymc());
//		setYuebmc(mstrReportName);
		blnIsBegin = true;
		
		
	}
//	生成工具栏
	public void getSelectData() {
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		
//		年份
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		//nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		
//		tb1.addText(new ToolbarText("-"));
//		月份
		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		//yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		

		
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
		
	 // 年份下拉框
    private static IPropertySelectionModel _NianfModel;
    public IPropertySelectionModel getNianfModel() {
        if (_NianfModel == null) {
            getNianfModels();
        }
        return _NianfModel;
    }
    
	private IDropDownBean _NianfValue;
	
    public IDropDownBean getNianfValue() {
        if (_NianfValue == null) {
            int _nianf = DateUtil.getYear(new Date());
            int _yuef = DateUtil.getMonth(new Date());
            if (_yuef == 1) {
                _nianf = _nianf - 1;
            }
            for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
                Object obj = getNianfModel().getOption(i);
                if (_nianf == ((IDropDownBean) obj).getId()) {
                    _NianfValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _NianfValue;
    }
	
    public void setNianfValue(IDropDownBean Value) {
    	if  (_NianfValue!=Value){
    		_NianfValue = Value;
    	}
    }

    public IPropertySelectionModel getNianfModels() {
        List listNianf = new ArrayList();
        int i;
        for (i = 2003; i <= DateUtil.getYear(new Date())+1; i++) {
            listNianf.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _NianfModel = new IDropDownModel(listNianf);
        return _NianfModel;
    }

    public void setNianfModel(IPropertySelectionModel _value) {
        _NianfModel = _value;
    }

	// 月份下拉框
	private static IPropertySelectionModel _YuefModel;
	
	public IPropertySelectionModel getYuefModel() {
	    if (_YuefModel == null) {
	        getYuefModels();
	    }
	    return _YuefModel;
	}
	
	private IDropDownBean _YuefValue;
	
	public IDropDownBean getYuefValue() {
	    if (_YuefValue == null) {
	        int _yuef = DateUtil.getMonth(new Date());
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
	        for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
	            Object obj = getYuefModel().getOption(i);
	            if (_yuef == ((IDropDownBean) obj).getId()) {
	                _YuefValue = (IDropDownBean) obj;
	                break;
	            }
	        }
	    }
	    return _YuefValue;
	}
	
	public void setYuefValue(IDropDownBean Value) {
    	if  (_YuefValue!=Value){
    		_YuefValue = Value;
    	}
	}

    public IPropertySelectionModel getYuefModels() {
        List listYuef = new ArrayList();
        for (int i = 1; i < 13; i++) {
            listYuef.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _YuefModel = new IDropDownModel(listYuef);
        return _YuefModel;
    }

    public void setYuefModel(IPropertySelectionModel _value) {
        _YuefModel = _value;
    }
	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_pageLink = "";
	}
}
