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
 * ���ߣ�������
 * ʱ�䣺2010-01-29 
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */

public class Tongpmdhl  extends BasePage implements PageValidateListener{

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
	
	
//	 ���������
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



//******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString3(null);
			visit.setProSelectionModel2(null);
			
			//begin��������г�ʼ������
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
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
		String strDate = getNianfValue().getValue();
		String sql = "";
		if(getDiancTreeJib()==2){
			diancxxbid = " d.fuid = "+getTreeid_dc()+"\n";
			
			sql = 
				"\n" +
				"select decode(d.quanc,null,'"+getDiancmc()+"',d.quanc) as diancxxb_id,\n" + 
				"       x.mc,\n" + 
				"       sum(x.hej),\n" + 
				"       sum(x.yiy),\n" + 
				"       sum(x.ery),\n" + 
				"       sum(x.sany),\n" + 
				"       sum(x.siy),\n" + 
				"       sum(x.wuy),\n" + 
				"       sum(x.liuy),\n" + 
				"       sum(x.qiy),\n" + 
				"       sum(x.bay),\n" + 
				"       sum(x.jiuy),\n" + 
				"       sum(x.shiy),\n" + 
				"       sum(x.shiyy),\n" + 
				"       sum(x.shiry)\n" + 
				"from diancxxb d,\n" + 
				"(select\n" + 
				"d.id as diancxxb_id,\n" + 
				"m.mc,\n" + 
				"sum(decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw)) hej,\n" + 
				"sum(decode(yuef,1,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) yiy,\n" + 
				"sum(decode(yuef,2,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) ery,\n" + 
				"sum(decode(yuef,3,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) sany,\n" + 
				"sum(decode(yuef,4,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) siy,\n" + 
				"sum(decode(yuef,5,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) wuy,\n" + 
				"sum(decode(yuef,6,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) liuy,\n" + 
				"sum(decode(yuef,7,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) qiy,\n" + 
				"sum(decode(yuef,8,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) bay,\n" + 
				"sum(decode(yuef,9,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) jiuy,\n" + 
				"sum(decode(yuef,10,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) shiy,\n" + 
				"sum(decode(yuef,11,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) shiyy,\n" + 
				"sum(decode(yuef,12,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) shiry\n" + 
				"from\n" + 
				"diancxxb d,\n" + 
				"(select al.id, al.yuef, nvl(ht.hetl,0) jh, nvl(htx.hetl,0) jhw,\n" + 
				"(nvl(ht.hetl,0)+nvl(htx.hetl,0)) hej,\n" + 
				"nvl(jhn.laimsl,0) laimsl,\n" + 
				" decode(nvl(ht.hetl,0)+nvl(htx.hetl,0),0,decode(nvl(jhn.laimsl,0),0,0,100),round(nvl(jhn.laimsl,0)/(nvl(ht.hetl,0)+nvl(htx.hetl,0)),4)*100) as daohl_jhn,\n" + 
				" nvl(jhw.laimsl,0) shihl_jhw,\n" + 
				"decode(nvl(ht.hetl,0)+nvl(htx.hetl,0),0,decode(nvl(jhn.laimsl,0),0,0,100),round(nvl(jhw.laimsl,0)/(nvl(ht.hetl,0)+nvl(htx.hetl,0)),4)*100) as daohl_jhw\n" + 
				"  from (select dc.id, yf.yuef\n" + 
				"          from (select rownum yuef from all_objects where rownum <= 12) yf,\n" + 
				"               (select id from diancxxb d\n" + 
				"               where  "+diancxxbid+"\n" + 
				"        ) dc) al,\n" + 
				"        --�ƻ���\n" + 
				"       (select h.diancxxb_id, to_char(s.riq, 'mm') yuef, sum(s.hetl) hetl\n" + 
				"          from hetb h, hetslb s,diancxxb d\n" + 
				"        where h.id = s.hetb_id\n" + 
				"           and h.jihkjb_id = 1\n" + 
				"           and to_char(s.riq,'yyyy') = '"+strDate+"'\n" + 
				"           and h.diancxxb_id = d.id\n" + 
				"           and "+diancxxbid+"\n" + 
				"\n" + 
				"         group by h.diancxxb_id, to_char(s.riq,'mm')) ht,\n" + 
				"        --(��������)�ƻ���\n" + 
				"       (select h2.diancxxb_id, to_char(h2.riq,'mm') yuef, hetl\n" + 
				"          from hlj_tpmdhqkb h2,diancxxb d\n" + 
				"          where to_char(h2.riq,'yyyy') = '"+strDate+"'\n" + 
				"                and h2.diancxxb_id = d.id\n" + 
				"                and "+diancxxbid+"\n" + 
				"       ) htx,\n" + 
				"        --ʵ����(�ƻ���)\n" + 
				"       (select f.diancxxb_id,\n" + 
				"               to_char(f.daohrq, 'mm') yuef,\n" + 
				"               sum(round_new(laimsl,0)) laimsl\n" + 
				"         from fahb f,diancxxb d\n" + 
				"         where f.jihkjb_id <> 1\n" + 
				"               and to_char(f.daohrq,'yyyy') = '"+strDate+"'\n" + 
				"               and f.diancxxb_id = d.id\n" + 
				"               and "+diancxxbid+"\n" + 
				"\n" + 
				"         group by f.diancxxb_id, to_char(f.daohrq, 'mm')) jhn,\n" + 
				"         --ʵ����(���ƻ���)\n" + 
				"         (select f.diancxxb_id,\n" + 
				"               to_char(f.daohrq, 'mm') yuef,\n" + 
				"               sum(round_new(laimsl,0)) laimsl\n" + 
				"          from fahb f,diancxxb d\n" + 
				"       where (f.jihkjb_id = 1 or f.jihkjb_id = 2)\n" + 
				"             and to_char(f.daohrq,'yyyy') = '"+strDate+"'\n" + 
				"             and f.diancxxb_id = d.id\n" + 
				"             and "+diancxxbid+"\n" + 
				"\n" + 
				"         group by f.diancxxb_id, to_char(f.daohrq, 'mm')) jhw\n" + 
				"where al.id = ht.diancxxb_id(+)\n" + 
				"   and al.id = htx.diancxxb_id(+)\n" + 
				"   and al.id = jhn.diancxxb_id(+)\n" + 
				"   and al.id = jhw.diancxxb_id(+)\n" + 
				"   and al.yuef = ht.yuef(+)\n" + 
				"   and al.yuef = htx.yuef(+)\n" + 
				"   and al.yuef = jhn.yuef(+)\n" + 
				"   and al.yuef = jhw.yuef(+)\n" + 
				" order by al.id, al.yuef) z,\n" + 
				" (select 1 xuh,'�ƻ���' mc from dual\n" + 
				" union select 2,'(��������)�ƻ���' mc from dual\n" + 
				" union select 3,'�ƻ��ںϼ�' mc from dual\n" + 
				" union select 4,'ʵ����(�ƻ���)' mc from dual\n" + 
				" union select 5,'������(%)' mc from dual\n" + 
				" union select 6,'ʵ����(���ƻ���)' mc from dual\n" + 
				" union select 7,'������(%)(���ƻ���)' mc from dual) m\n" + 
				" where z.id = d.id\n" + 
				"      and "+diancxxbid+"\n" + 
				"group by d.id,m.mc\n" + 
				") x\n" + 
				"where x.diancxxb_id = d.id\n" + 
				"group by grouping sets (x.mc,(d.quanc,d.xuh,x.mc))\n" + 
				"order by grouping(d.quanc) desc,d.xuh,\n" + 
				"      decode(x.mc,'�ƻ���',1,'(��������)�ƻ���',2,'�ƻ��ںϼ�',3,'ʵ����(�ƻ���)',4,'������(%)',5,'ʵ����(���ƻ���)',6,7)\n";

			
		}else if(getDiancTreeJib()==3){
			diancxxbid = " d.id = "+getTreeid_dc()+"\n";
			
			sql = 
				"select\n" +
				"d.quanc,\n" + 
				"m.mc,\n" + 
				"sum(decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw)) hej,\n" + 
				"sum(decode(yuef,1,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) yiy,\n" + 
				"sum(decode(yuef,2,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) ery,\n" + 
				"sum(decode(yuef,3,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) sany,\n" + 
				"sum(decode(yuef,4,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) siy,\n" + 
				"sum(decode(yuef,5,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) wuy,\n" + 
				"sum(decode(yuef,6,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) liuy,\n" + 
				"sum(decode(yuef,7,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) qiy,\n" + 
				"sum(decode(yuef,8,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) bay,\n" + 
				"sum(decode(yuef,9,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) jiuy,\n" + 
				"sum(decode(yuef,10,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) shiy,\n" + 
				"sum(decode(yuef,11,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) shiyy,\n" + 
				"sum(decode(yuef,12,decode(m.mc,'�ƻ���',jh,'(��������)�ƻ���',jhw,'�ƻ��ںϼ�',hej,'ʵ����(�ƻ���)',laimsl,\n" + 
				"'������(%)',daohl_jhn,'ʵ����(���ƻ���)',shihl_jhw,'������(%)(���ƻ���)',daohl_jhw),0)) shiry\n" + 
				"from\n" + 
				"diancxxb d,\n" +
				"(select al.id, al.yuef, nvl(ht.hetl,0) jh, nvl(htx.hetl,0) jhw,\n" + 
				"(nvl(ht.hetl,0)+nvl(htx.hetl,0)) hej,\n" + 
				"nvl(jhn.laimsl,0) laimsl,\n" + 
				" decode(nvl(ht.hetl,0)+nvl(htx.hetl,0),0,decode(nvl(jhn.laimsl,0),0,0,100),round(nvl(jhn.laimsl,0)/(nvl(ht.hetl,0)+nvl(htx.hetl,0)),4)*100) as daohl_jhn,\n" + 
				" nvl(jhw.laimsl,0) shihl_jhw,\n" + 
				"decode(nvl(ht.hetl,0)+nvl(htx.hetl,0),0,decode(nvl(jhn.laimsl,0),0,0,100),round(nvl(jhw.laimsl,0)/(nvl(ht.hetl,0)+nvl(htx.hetl,0)),4)*100) as daohl_jhw\n" + 
				"  from (select dc.id, yf.yuef\n" + 
				"          from (select rownum yuef from all_objects where rownum <= 12) yf,\n" + 
				"               (select id from diancxxb d\n" + 
				"               where "+diancxxbid+") dc) al,\n" + 
				"        --�ƻ���\n" + 
				"       (select h.diancxxb_id, to_char(s.riq, 'mm') yuef, sum(s.hetl) hetl\n" + 
				"          from hetb h, hetslb s,diancxxb d\n" + 
				"        where h.id = s.hetb_id\n" + 
				"           and h.jihkjb_id = 1\n" + 
				"           and to_char(s.riq,'yyyy') = '"+strDate+"'\n" + 
				"           and h.diancxxb_id = d.id\n" + 
				"           and "+diancxxbid+"\n" + 
				"         group by h.diancxxb_id, to_char(s.riq,'mm')) ht,\n" + 
				"        --(��������)�ƻ���\n" + 
				"       (select h2.diancxxb_id, to_char(h2.riq,'mm') yuef, hetl\n" + 
				"          from hlj_tpmdhqkb h2,diancxxb d\n" + 
				"          where to_char(h2.riq,'yyyy') = '"+strDate+"'\n" + 
				"                and h2.diancxxb_id = d.id\n" + 
				"                and "+diancxxbid+") htx,\n" + 
				"        --ʵ����(�ƻ���)\n" + 
				"       (select f.diancxxb_id,\n" + 
				"               to_char(f.daohrq, 'mm') yuef,\n" + 
				"               sum(round_new(laimsl,0)) laimsl\n" + 
				"         from fahb f,diancxxb d\n" + 
				"         where f.jihkjb_id <> 1\n" + 
				"               and to_char(f.daohrq,'yyyy') = '"+strDate+"'\n" + 
				"               and f.diancxxb_id = d.id\n" + 
				"               and "+diancxxbid+"\n" + 
				"         group by f.diancxxb_id, to_char(f.daohrq, 'mm')) jhn,\n" + 
				"         --ʵ����(���ƻ���)\n" + 
				"         (select f.diancxxb_id,\n" + 
				"               to_char(f.daohrq, 'mm') yuef,\n" + 
				"               sum(round_new(laimsl,0)) laimsl\n" + 
				"          from fahb f,diancxxb d\n" + 
				"       where (f.jihkjb_id = 1 or f.jihkjb_id = 2)\n" + 
				"             and to_char(f.daohrq,'yyyy') = '"+strDate+"'\n" + 
				"             and f.diancxxb_id = d.id\n" + 
				"             and "+diancxxbid+"\n" + 
				"         group by f.diancxxb_id, to_char(f.daohrq, 'mm')) jhw\n" + 
				" where al.id = ht.diancxxb_id(+)\n" + 
				"   and al.id = htx.diancxxb_id(+)\n" + 
				"   and al.id = jhn.diancxxb_id(+)\n" + 
				"   and al.id = jhw.diancxxb_id(+)\n" + 
				"   and al.yuef = ht.yuef(+)\n" + 
				"   and al.yuef = htx.yuef(+)\n" + 
				"   and al.yuef = jhn.yuef(+)\n" + 
				"   and al.yuef = jhw.yuef(+)\n" + 
				" order by al.id, al.yuef) z,\n" + 
				" (select 1 xuh,'�ƻ���' mc from dual\n" + 
				" union select 2,'(��������)�ƻ���' mc from dual\n" + 
				" union select 3,'�ƻ��ںϼ�' mc from dual\n" + 
				" union select 4,'ʵ����(�ƻ���)' mc from dual\n" + 
				" union select 5,'������(%)' mc from dual\n" + 
				" union select 6,'ʵ����(���ƻ���)' mc from dual\n" + 
				" union select 7,'������(%)(���ƻ���)' mc from dual) m\n" + 
				" where z.id = d.id \n" +
				"      and "+diancxxbid+"\n" +
				"group by d.quanc,d.xuh,m.mc\n" + 
				"order by d.xuh,max(m.xuh)\n";
		}

		ResultSet rs = cn.getResultSet(sql);
		// �����ͷ����
		String ArrHeader[][]=new String[1][15];
		ArrHeader[0]=new String[] {"��λ","","ȫ��ϼ�","1��","2��","3��","4��","5��","6��","7��","8��","9��","10��","11��","12��"};

		int ArrWidth[]=new int[] {190,110,80,50,50,50,50,50,50,50,50,50,50,50,50};

		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		
		// ����ҳ����
		rt.setTitle(getDiancmc()+getNianfValue().getValue()+"��ͳ��ú̿���������(��������)", ArrWidth);
		rt.setDefaultTitle(1, 2, "�����:", Table.ALIGN_LEFT);
		rt.setDefaultTitle(13, 3, "��λ:", Table.ALIGN_LEFT);
//		rt.setDefaultTitle(6, 24, getNianfValue().getValue()+"��"+getYuefValue().getValue()+"��", Table.ALIGN_CENTER);

		// ����
		rt.setBody(new Table(rs, 1, 0, 1));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_COLROWS);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
//		rt.body.mergeFixedRow();
//		rt.body.mergeFixedCol(4);
		rt.body.mergeFixedRowCol();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_LEFT);
		rt.body.setColAlign(3, Table.ALIGN_RIGHT);
		rt.body.setColAlign(4, Table.ALIGN_RIGHT);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);
		rt.body.setColAlign(13, Table.ALIGN_RIGHT);
		rt.body.setColAlign(14, Table.ALIGN_RIGHT);
		rt.body.setColAlign(15, Table.ALIGN_RIGHT);

//		 //ҳ��
//		 rt.createDefautlFooter(ArrWidth);
//		 rt.setDefautlFooter(11,4,"���:",Table.ALIGN_LEFT);
//		 rt.setDefautlFooter(17,3,"�Ʊ�:",Table.ALIGN_LEFT);
//		 rt.setDefautlFooter(20,9,"��������:",Table.ALIGN_LEFT);
		
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

	// Page����
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}


	
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}
	
//	***************************�����ʼ����***************************//
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

		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		// nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);

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
		
		tb1.addText(new ToolbarText("�糧:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null, "��ѯ",
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

//	-------------------------�糧Tree-----------------------------------------------------------------
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
	
//	-------------------------�糧Tree END----------
	
//�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
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
	
}