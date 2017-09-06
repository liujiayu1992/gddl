package com.zhiren.heiljkhh;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.gangkjy.Local;

import org.apache.tapestry.contrib.palette.SortMode;


/**
 * @author ly
 *
 */
/*
 * 作者：陈泽天
 * 时间：2010-01-29 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */

public class Zigmtmx  extends BasePage implements PageValidateListener{

	public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	} 
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
	
//	 年份下拉框
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
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
	
//	 月份起始
	public boolean Changeyuef = false;

	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel5() == null) {
			getYuefModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel5();
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (((Visit)getPage().getVisit()).getDropDownBean5() == null) {
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit)getPage().getVisit()).setDropDownBean5((IDropDownBean) obj );
					break;
				}
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean5();
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit)getPage().getVisit()).getDropDownBean5()!= null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		((Visit)getPage().getVisit()).setDropDownBean5(Value);
		
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit)getPage().getVisit()).setProSelectionModel5(new IDropDownModel(listYuef));
		return ((Visit)getPage().getVisit()).getProSelectionModel5();
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel5(_value);
	}
	
//	月份结束
	public boolean Changebyuef = false;

	private static IPropertySelectionModel _bYuefModel;

	public IPropertySelectionModel getbYuefModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel4() == null) {
			getbYuefModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel4();
	}

	private IDropDownBean _bYuefValue;

	public IDropDownBean getbYuefValue() {
		if (((Visit)getPage().getVisit()).getDropDownBean4() == null) {
			for (int i = 0; i < getbYuefModel().getOptionCount(); i++) {
				Object obj = getbYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit)getPage().getVisit()).setDropDownBean4((IDropDownBean) obj );
					break;
				}
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean4();
	}

	public void setbYuefValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit)getPage().getVisit()).getDropDownBean4()!= null) {
			id = getbYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changebyuef = true;
			} else {
				Changebyuef = false;
			}
		}
		((Visit)getPage().getVisit()).setDropDownBean4(Value);
		
	}

	public IPropertySelectionModel getbYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit)getPage().getVisit()).setProSelectionModel4(new IDropDownModel(listYuef));
		return ((Visit)getPage().getVisit()).getProSelectionModel4();
	}

	public void setbYuefModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel4(_value);
	}
	
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
	
	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
			getPrintTable();
			getSelectData();
		}
		
	}



//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString3(null);
			visit.setProSelectionModel2(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean4(null);
			setJihkjValue(null);
			setJihkjModel(null);
			
			//begin方法里进行初始化设置
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);保存传递的非默认纸张的样式
			this.getSelectData();
		}
		isBegin=true;
		getToolBars();
		
	}

	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
	
		return getSelectData();
	
	}

	private boolean isBegin=false;
	
	private String getDiancmc(){
		JDBCcon cn = new JDBCcon();
		String sql_dc = "";
		String qc = "";
		sql_dc = "select quanc from diancxxb where id = " + getTreeid_dc();
		ResultSetList rsl = cn.getResultSetList(sql_dc);
		if(rsl.next()){
			qc = rsl.getString("QUANC");
		}
		cn.Close();
		return qc;
	}

	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		StringBuffer strSQL = new StringBuffer();
		String diancxxbid = " ";
		String jihkj = "";
		String hr = "";
		if(getDiancTreeJib()==2){
			diancxxbid = "and (d.id = "+getTreeid_dc()+" or d.fuid = "+getTreeid_dc()+")\n";
		}else if(getDiancTreeJib()==3){
			diancxxbid = "and d.id = "+getTreeid_dc()+"\n";
		}
		
		if(getJihkjValue().getValue().equals("全部")){
			jihkj = " ";
		}else{
			jihkj = "and f.jihkjb_id = " + getJihkjValue().getId() + "\n";
		}
//		diancxxbid = getTreeid_dc();// 电厂信息表id
		String strDate = getNianfValue().getValue();
//		String strDate1 =Integer.valueOf( getNianfValue().getValue()).intValue()+1+"-"+getYuefValue().getValue();
		int iy = (int)getYuefValue().getId();
		int by = (int)getbYuefValue().getId();
		//合计的日期条件
		if(by != 12){
			hr = "      and f.daohrq>=to_date('"+strDate+"-"+iy+"-01','yyyy-MM-dd')\n" + 
			"      			   and f.daohrq<to_date('"+strDate+"-"+(by+1)+"-01','yyyy-MM-dd')\n";
		} else {
			hr = "      and f.daohrq>=to_date('"+strDate+"-"+iy+"-01','yyyy-MM-dd')\n" + 
			"      			   and f.daohrq<=to_date('"+strDate+"-12-31','yyyy-MM-dd')\n";
		}
		
		String a = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x";
		String[] b = a.split(",");
		String xuanzxs = "";
//		Integer.parseInt(Long.toString(iy));
//		int d = (int)iy*2-2;
		for(int i=iy*2-2;i<by*2;i++ ){
			xuanzxs += b[i]+",";
		}
		
		String sql = 
			"select decode(x.quanc,null,null,rownum),\n" +
			"       decode(x.quanc,null,'合计',x.quanc),\n" + 
			xuanzxs + "y,z\n" + 

			"from (select distinct f.quanc,\n" +
			"                       nvl(sum(yi.shul), 0) as a,\n" + 
			"                       nvl(decode(sum(yi.shul),0,0,round_new(sum(yi.rez*yi.shul)/sum(yi.shul),2)), 0) as b,\n" + 
			"                       nvl(sum(er.shul), 0) as c,\n" + 
			"                       nvl(decode(sum(er.shul),0,0,round_new(sum(er.rez*er.shul)/sum(er.shul),2)), 0) as d,\n" + 
			"                       nvl(sum(san.shul), 0) as e,\n" + 
			"                       nvl(decode(sum(san.shul),0,0,round_new(sum(san.rez*san.shul)/sum(san.shul),2)), 0) as f,\n" + 
			"                       nvl(sum(si.shul), 0) as g,\n" + 
			"                       nvl(decode(sum(si.shul),0,0,round_new(sum(si.rez*si.shul)/sum(si.shul),2)), 0) as h,\n" + 
			"                       nvl(sum(wu.shul), 0) as i,\n" + 
			"                       nvl(decode(sum(wu.shul),0,0,round_new(sum(wu.rez*wu.shul)/sum(wu.shul),2)), 0) as j,\n" + 
			"                       nvl(sum(liu.shul), 0) as k,\n" + 
			"                       nvl(decode(sum(liu.shul),0,0,round_new(sum(liu.rez*liu.shul)/sum(liu.shul),2)), 0) as l,\n" + 
			"                       nvl(sum(qi.shul), 0) as m,\n" + 
			"                       nvl(decode(sum(qi.shul),0,0,round_new(sum(qi.rez*qi.shul)/sum(qi.shul),2)), 0) as n,\n" + 
			"                       nvl(sum(ba.shul), 0) as o,\n" + 
			"                       nvl(decode(sum(ba.shul),0,0,round_new(sum(ba.rez*ba.shul)/sum(ba.shul),2)), 0) as p,\n" + 
			"                       nvl(sum(jiu.shul), 0) as q,\n" + 
			"                       nvl(decode(sum(jiu.shul),0,0,round_new(sum(jiu.rez*jiu.shul)/sum(jiu.shul),2)), 0) as r,\n" + 
			"                       nvl(sum(shi.shul), 0) as s,\n" + 
			"                       nvl(decode(sum(shi.shul),0,0,round_new(sum(shi.rez*shi.shul)/sum(shi.shul),2)), 0) as t,\n" + 
			"                       nvl(sum(shiy.shul), 0) as u,\n" + 
			"                       nvl(decode(sum(shiy.shul),0,0,round_new(sum(shiy.rez*shiy.shul)/sum(shiy.shul),2)), 0) as v,\n" + 
			"                       nvl(sum(shie.shul), 0) as w,\n" + 
			"                       nvl(decode(sum(shie.shul),0,0,round_new(sum(shie.rez*shie.shul)/sum(shie.shul),2)), 0) as x,\n" + 
			"                       nvl(sum(hj.shul), 0) as y,\n" + 
			"                       nvl(decode(sum(hj.shul),0,0,round_new(sum(hj.rez*hj.shul)/sum(hj.shul),2)), 0) as z\n" +
			"from \n" + 
			"(select f.meikxxb_id,\n" + 
			"       sum(round_new(f.laimsl,0)) as shul,\n" + 
			"       decode(sum(round_new(f.laimsl,0)),0,0,round_new(sum(round_new(f.laimsl,0)*z.qnet_ar)/sum(round_new(f.laimsl,0)),2)) as rez\n" + 
			"from fahb f,zhilb z,diancxxb d\n" + 
			"where f.zhilb_id = z.id\n" + 
			"      and f.daohrq>=to_date('"+strDate+"-01-01','yyyy-MM-dd')\n" + 
			"      and f.daohrq<to_date('"+strDate+"-02-01','yyyy-MM-dd')\n" + 
			"      and f.diancxxb_id = d.id\n" + 
			jihkj +
			diancxxbid +
			"group by f.meikxxb_id) yi,\n" + 
			
			
			"(select f.meikxxb_id,\n" + 
			"       sum(round_new(f.laimsl,0)) as shul,\n" + 
			"       decode(sum(round_new(f.laimsl,0)),0,0,round_new(sum(round_new(f.laimsl,0)*z.qnet_ar)/sum(round_new(f.laimsl,0)),2)) as rez\n" + 
			"from fahb f,zhilb z,diancxxb d\n" + 
			"where f.zhilb_id = z.id\n" + 
			"      and f.daohrq>=to_date('"+strDate+"-02-01','yyyy-MM-dd')\n" + 
			"      and f.daohrq<to_date('"+strDate+"-03-01','yyyy-MM-dd')\n" + 
			"      and f.diancxxb_id = d.id\n" + 
			jihkj +
			diancxxbid +
			"group by f.meikxxb_id) er,\n" + 
			
			
			"(select f.meikxxb_id,\n" + 
			"       sum(round_new(f.laimsl,0)) as shul,\n" + 
			"       decode(sum(round_new(f.laimsl,0)),0,0,round_new(sum(round_new(f.laimsl,0)*z.qnet_ar)/sum(round_new(f.laimsl,0)),2)) as rez\n" + 
			"from fahb f,zhilb z,diancxxb d\n" + 
			"where f.zhilb_id = z.id\n" + 
			"      and f.daohrq>=to_date('"+strDate+"-03-01','yyyy-MM-dd')\n" + 
			"      and f.daohrq<to_date('"+strDate+"-04-01','yyyy-MM-dd')\n" + 
			jihkj +
			diancxxbid +
			"group by f.meikxxb_id) san,\n" + 
			
			
			"(select f.meikxxb_id,\n" + 
			"       sum(round_new(f.laimsl,0)) as shul,\n" + 
			"       decode(sum(round_new(f.laimsl,0)),0,0,round_new(sum(round_new(f.laimsl,0)*z.qnet_ar)/sum(round_new(f.laimsl,0)),2)) as rez\n" + 
			"from fahb f,zhilb z,diancxxb d\n" + 
			"where f.zhilb_id = z.id\n" + 
			"      and f.daohrq>=to_date('"+strDate+"-04-01','yyyy-MM-dd')\n" + 
			"      and f.daohrq<to_date('"+strDate+"-05-01','yyyy-MM-dd')\n" + 
			"      and f.diancxxb_id = d.id\n" + 
			jihkj +
			diancxxbid +
			"group by f.meikxxb_id) si,\n" + 
			
			
			"(select f.meikxxb_id,\n" + 
			"       sum(round_new(f.laimsl,0)) as shul,\n" + 
			"       decode(sum(round_new(f.laimsl,0)),0,0,round_new(sum(round_new(f.laimsl,0)*z.qnet_ar)/sum(round_new(f.laimsl,0)),2)) as rez\n" +  
			"from fahb f,zhilb z,diancxxb d\n" + 
			"where f.zhilb_id = z.id\n" + 
			"      and f.daohrq>=to_date('"+strDate+"-05-01','yyyy-MM-dd')\n" + 
			"      and f.daohrq<to_date('"+strDate+"-06-01','yyyy-MM-dd')\n" + 
			"      and f.diancxxb_id = d.id\n" + 
			jihkj +
			diancxxbid +
			"group by f.meikxxb_id) wu,\n" + 
			
			
			"(select f.meikxxb_id,\n" + 
			"       sum(round_new(f.laimsl,0)) as shul,\n" + 
			"       decode(sum(round_new(f.laimsl,0)),0,0,round_new(sum(round_new(f.laimsl,0)*z.qnet_ar)/sum(round_new(f.laimsl,0)),2)) as rez\n" + 
			"from fahb f,zhilb z,diancxxb d\n" + 
			"where f.zhilb_id = z.id\n" + 
			"      and f.daohrq>=to_date('"+strDate+"-06-01','yyyy-MM-dd')\n" + 
			"      and f.daohrq<to_date('"+strDate+"-07-01','yyyy-MM-dd')\n" + 
			"      and f.diancxxb_id = d.id\n" + 
			jihkj +
			diancxxbid +
			"group by f.meikxxb_id) liu,\n" + 
			
			
			"(select f.meikxxb_id,\n" + 
			"       sum(round_new(f.laimsl,0)) as shul,\n" + 
			"       decode(sum(round_new(f.laimsl,0)),0,0,round_new(sum(round_new(f.laimsl,0)*z.qnet_ar)/sum(round_new(f.laimsl,0)),2)) as rez\n" + 
			"from fahb f,zhilb z,diancxxb d\n" + 
			"where f.zhilb_id = z.id\n" + 
			"      and f.daohrq>=to_date('"+strDate+"-07-01','yyyy-MM-dd')\n" + 
			"      and f.daohrq<to_date('"+strDate+"-08-01','yyyy-MM-dd')\n" + 
			"      and f.diancxxb_id = d.id\n" + 
			jihkj +
			diancxxbid +
			"group by f.meikxxb_id) qi,\n" + 
			
			
			"(select f.meikxxb_id,\n" + 
			"       sum(round_new(f.laimsl,0)) as shul,\n" + 
			"       decode(sum(round_new(f.laimsl,0)),0,0,round_new(sum(round_new(f.laimsl,0)*z.qnet_ar)/sum(round_new(f.laimsl,0)),2)) as rez\n" + 
			"from fahb f,zhilb z,diancxxb d\n" + 
			"where f.zhilb_id = z.id\n" + 
			"      and f.daohrq>=to_date('"+strDate+"-08-01','yyyy-MM-dd')\n" + 
			"      and f.daohrq<to_date('"+strDate+"-09-01','yyyy-MM-dd')\n" + 
			"      and f.diancxxb_id = d.id\n" + 
			jihkj +
			diancxxbid +
			"group by f.meikxxb_id) ba,\n" + 
			
			
			"(select f.meikxxb_id,\n" + 
			"       sum(round_new(f.laimsl,0)) as shul,\n" + 
			"       decode(sum(round_new(f.laimsl,0)),0,0,round_new(sum(round_new(f.laimsl,0)*z.qnet_ar)/sum(round_new(f.laimsl,0)),2)) as rez\n" + 
			"from fahb f,zhilb z,diancxxb d\n" + 
			"where f.zhilb_id = z.id\n" + 
			"      and f.daohrq>=to_date('"+strDate+"-09-01','yyyy-MM-dd')\n" + 
			"      and f.daohrq<to_date('"+strDate+"-10-01','yyyy-MM-dd')\n" + 
			"      and f.diancxxb_id = d.id\n" + 
			jihkj +
			diancxxbid +
			"group by f.meikxxb_id) jiu,\n" + 
			
			
			"(select f.meikxxb_id,\n" + 
			"       sum(round_new(f.laimsl,0)) as shul,\n" + 
			"       decode(sum(round_new(f.laimsl,0)),0,0,round_new(sum(round_new(f.laimsl,0)*z.qnet_ar)/sum(round_new(f.laimsl,0)),2)) as rez\n" + 
			"from fahb f,zhilb z,diancxxb d\n" + 
			"where f.zhilb_id = z.id\n" + 
			"      and f.daohrq>=to_date('"+strDate+"-10-01','yyyy-MM-dd')\n" + 
			"      and f.daohrq<to_date('"+strDate+"-11-01','yyyy-MM-dd')\n" + 
			"      and f.diancxxb_id = d.id\n" + 
			jihkj +
			diancxxbid +
			"group by f.meikxxb_id) shi,\n" + 
			
			
			"(select f.meikxxb_id,\n" + 
			"       sum(round_new(f.laimsl,0)) as shul,\n" + 
			"       decode(sum(round_new(f.laimsl,0)),0,0,round_new(sum(round_new(f.laimsl,0)*z.qnet_ar)/sum(round_new(f.laimsl,0)),2)) as rez\n" + 
			"from fahb f,zhilb z,diancxxb d\n" + 
			"where f.zhilb_id = z.id\n" + 
			"      and f.daohrq>=to_date('"+strDate+"-11-01','yyyy-MM-dd')\n" + 
			"      and f.daohrq<to_date('"+strDate+"-12-01','yyyy-MM-dd')\n" + 
			"      and f.diancxxb_id = d.id\n" + 
			jihkj +
			diancxxbid +
			"group by f.meikxxb_id) shiy,\n" + 
			
			
			"(select f.meikxxb_id,\n" + 
			"       sum(round_new(f.laimsl,0)) as shul,\n" + 
			"       decode(sum(round_new(f.laimsl,0)),0,0,round_new(sum(round_new(f.laimsl,0)*z.qnet_ar)/sum(round_new(f.laimsl,0)),2)) as rez\n" + 
			"from fahb f,zhilb z,diancxxb d\n" + 
			"where f.zhilb_id = z.id\n" + 
			"      and f.daohrq>=to_date('"+strDate+"-12-01','yyyy-MM-dd')\n" + 
			"      and f.daohrq<=to_date('"+strDate+"-12-31','yyyy-MM-dd')\n" + 
			"      and f.diancxxb_id = d.id\n" + 
			jihkj +
			diancxxbid +
			"group by f.meikxxb_id) shie,\n" + 
			
			
			"(select f.meikxxb_id,\n" + 
			"       sum(round_new(f.laimsl,0)) as shul,\n" + 
			"       decode(sum(round_new(f.laimsl,0)),0,0,round_new(sum(round_new(f.laimsl,0)*z.qnet_ar)/sum(round_new(f.laimsl,0)),2)) as rez\n" + 
			"from fahb f,zhilb z,diancxxb d\n" + 
			"where f.zhilb_id = z.id\n" + 
			hr + 
			"      and f.diancxxb_id = d.id\n" + 
			jihkj +
			diancxxbid +
			"group by f.meikxxb_id) hj,\n" + 
			
			
			"(select distinct m.quanc as quanc,\n" + 
			"                 f.meikxxb_id\n" + 
			"from fahb f,meikxxb m,diancxxb d\n" + 
			"where f.meikxxb_id = m.id\n" + 
			"      and f.diancxxb_id = d.id\n" + 
			jihkj +
			diancxxbid +
			") f\n" + 
			"\n" + 
			"where  " +
			"	   f.meikxxb_id = yi.meikxxb_id(+)\n" + 
			"      and f.meikxxb_id = er.meikxxb_id(+)\n" + 
			"      and f.meikxxb_id = san.meikxxb_id(+)\n" + 
			"      and f.meikxxb_id = si.meikxxb_id(+)\n" + 
			"      and f.meikxxb_id = wu.meikxxb_id(+)\n" + 
			"      and f.meikxxb_id = liu.meikxxb_id(+)\n" + 
			"      and f.meikxxb_id = qi.meikxxb_id(+)\n" + 
			"      and f.meikxxb_id = ba.meikxxb_id(+)\n" + 
			"      and f.meikxxb_id = jiu.meikxxb_id(+)\n" + 
			"      and f.meikxxb_id = shi.meikxxb_id(+)\n" + 
			"      and f.meikxxb_id = shiy.meikxxb_id(+)\n" + 
			"      and f.meikxxb_id = shie.meikxxb_id(+)\n" + 
			"      and f.meikxxb_id = hj.meikxxb_id\n" + 
			"group by rollup(f.quanc)\n" + 
			"order by f.quanc\n" + 
			") x";

		ResultSet rs = cn.getResultSet(sql);
		
		// 定义表头数据
		String ArrHeader[][]=new String[2][(by-iy+3)*2];
		String h = "序号,矿名,";
		String h1 = "序号,矿名,";
		
		String[] header = {"序号","矿名","1月","1月","2月","2月","3月","3月","4月","4月","5月","5月","6月","6月","7月","7月","8月","8月","9月","9月","10月","10月","11月","11月","12月","12月","合计","合计"};
		String[] header1 = {"序号","矿名","数量(吨)","热值(MJ/kg)","数量(吨)","热值(MJ/kg)","数量(吨)","热值(MJ/kg)","数量(吨)","热值(MJ/kg)","数量(吨)","热值(MJ/kg)","数量(吨)","热值(MJ/kg)","数量(吨)","热值(MJ/kg)","数量(吨)","热值(MJ/kg)","数量(吨)","热值(MJ/kg)","数量(吨)","热值(MJ/kg)","数量(吨)","热值(MJ/kg)","数量(吨)","热值(MJ/kg)"," 数量(吨)","热值(MJ/kg)"};
		for(int i=iy*2;i<by*2+2;i++){
			h+=header[i]+",";
			h1+=header1[i]+",";
		}
		h += "合计,合计";
		h1 += "数量(吨),热值(MJ/kg)";
		ArrHeader[0]=h.split(",");
		ArrHeader[1]=h1.split(",");

		int ArrWidth[]=new int[(by-iy)*2+6];
		ArrWidth[0]=30;
		ArrWidth[1]=200;
		for(int i=2;i<(by-iy)*2+6;i++){
			ArrWidth[i] = 45;
		}	
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		// 设置页标题
		rt.setTitle(getDiancmc()+getNianfValue().getValue()+"年自购煤炭明细表", ArrWidth);
//		rt.setDefaultTitle(1, 5, "报送单位:", Table.ALIGN_LEFT);
//		rt.setDefaultTitle(6, 24, getNianfValue().getValue()+"年"+getYuefValue().getValue()+"月", Table.ALIGN_CENTER);

		// 数据
		rt.setBody(new Table(rs, 2, 0, 1));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_ROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.ShowZero = true;//设置可以显示0
//		rt.body.mergeFixedRow();
//		rt.body.mergeFixedCol(4);
		rt.body.mergeFixedRowCol();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_LEFT);
		
		for(int i = 3;i<(by-iy)*2+5;i++){
			rt.body.setColAlign(i, Table.ALIGN_RIGHT);
		}
//		rt.body.setColAlign(3, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(4, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(13, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(14, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(15, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(16, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(17, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(18, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(19, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(20, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(21, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(22, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(23, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(24, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(25, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(26, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(27, Table.ALIGN_RIGHT);
//		rt.body.setColAlign(28, Table.ALIGN_RIGHT);

//		 //页脚
//		 rt.createDefautlFooter(ArrWidth);
//		 rt.setDefautlFooter(11,4,"审核:",Table.ALIGN_LEFT);
//		 rt.setDefautlFooter(17,3,"制表:",Table.ALIGN_LEFT);
//		 rt.setDefautlFooter(20,9,"报送日期:",Table.ALIGN_LEFT);
		
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rt.getAllPagesHtml();
//		
	}
//	******************************************************************************
	
	
	public SortMode getSort() {
		return SortMode.USER;
	}
	
	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}


	
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	
//	***************************报表初始设置***************************//
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
		
	public Date getYesterday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE,-1);
		return cal.getTime();
	}
	
	public Date getMonthFirstday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}
	
	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}

	
	public void getToolBars() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		// nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
//		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		
		tb1.addText(new ToolbarText("至:"));
		ComboBox byuef = new ComboBox();
		byuef.setTransform("BYUEF");
		byuef.setWidth(60);
//		byuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(byuef);

		tb1.addText(new ToolbarText("-"));
		
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
						: getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("计划口径:"));
		ComboBox fh = new ComboBox();
		fh.setTransform("JihkjSelect");
		fh.setWidth(130);
		fh.setListeners("select:function(own,rec,index){Ext.getDom('JihkjSelect').selectedIndex=index}");
		tb1.addField(fh);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "查询",
				"function(){document.getElementById('QueryButton').click();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(tb);
		setToolbar(tb1);
	}
	
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

//	-------------------------电厂Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		Visit visit=(Visit)this.getPage().getVisit();
		String sql = " select d.id,d.mingc from diancxxb d where d.id="+visit.getDiancxxb_id()+" \n";
		sql+=" union \n";
		sql+="  select d.id,d.mingc from diancxxb d where d.fuid="+visit.getDiancxxb_id()+" \n";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	DefaultTree dc ;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc= etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	
//	-------------------------电厂Tree END----------
	
//得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid_dc();
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
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		return jib;
	}
	
//	计划口径下拉框
	public IDropDownBean getJihkjValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getJihkjModel().getOptionCount()>0) {
				setJihkjValue((IDropDownBean)getJihkjModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	public void setJihkjValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getJihkjModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			setJihkjModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	public void setJihkjModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
	public IPropertySelectionModel setJihkjModels() {
		String sql = "select id,mingc from jihkjb";
		((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql, "全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

}