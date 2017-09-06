package com.zhiren.gs.bjdt.jiesgl;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
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
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

public class Jiesdtjcx extends BasePage {
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
	
//	***************������Ϣ��******************//
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
	
	private static long WODRW = 1;// �ո��ύδѡ�����̵�����

	private static long LIUCZ = 2;// δ�������

	private static long YISH = 3;// �����
	
	private String mstrReportName="";
	
	public String getTianzdwQuanc(){
		return getTianzdwQuanc(getDiancxxbId());
	}
	
	public long getDiancxxbId(){
		
		return ((Visit)getPage().getVisit()).getDiancxxb_id();
	}
	
	public boolean isJTUser(){
		return ((Visit)getPage().getVisit()).isJTUser();
	}
	//�õ���λȫ��
	public String getTianzdwQuanc(long gongsxxbID){
		String _TianzdwQuanc="";  
		JDBCcon cn = new JDBCcon();
		
		try {
			ResultSet rs=cn.getResultSet(" select quanc from diancxxb where id="+gongsxxbID);
			while (rs.next()){
				_TianzdwQuanc=rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return _TianzdwQuanc;
	}
	private boolean blnIsBegin = false;
	private String leix="";
	public String getPrintTable(){
		setMsg(null);
		if(!blnIsBegin){
			return "";
		}
		blnIsBegin=false;
	    return  getDiaor01();
	
	}
	
	private String getDiancCondition(){
		JDBCcon cn = new JDBCcon();
		String diancxxb_id=getTreeid();
		String condition ="";
		ResultSet rs=cn.getResultSet("select jib,id,fuid from diancxxb where id=" +diancxxb_id);
		try {
			if (rs.next()){
				if( rs.getLong("jib")==SysConstant.JIB_JT){
					condition="";
				}else if(rs.getLong("jib")==SysConstant.JIB_GS){
					condition=" and dc.fuid=" +diancxxb_id;
				}else {
					condition=" and dc.id=" +diancxxb_id;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return condition;
	}
	
	private String getGongysCondition(){
		if (getMeikdqmcValue().getId()==-1){
			return "";
		}else{
			return " and dq.id=" +getMeikdqmcValue().getId();
		}
	}
	
	private String getDiaor01(){
		String _Danwqc=getTianzdwQuanc();
		 Visit visit=((Visit) getPage().getVisit());
		if(_Danwqc.equals("��������ȼ�����޹�˾")&&visit.getRenyjb()==2){
			_Danwqc="���ƹ��ʷ���ɷ����޹�˾ȼ�Ϲ���";
		}
		JDBCcon cn = new JDBCcon();
		String sql =new String();
		long lngDiancId=getDiancxxbId();//�糧��Ϣ��id
		String strDate=this.getStartdate();
		String endDate=this.getEnddate();
		
		 String condition=new String();
		 String groupbysql=new String();
		 String biaot=new String();
		 if(this.getRiqLxValue().getId()==1){
		 condition="and js.jiesrq>=to_date('"+strDate+"','yyyy-mm-dd') and js.jiesrq<to_date('"+endDate+"','yyyy-mm-dd')";
		 }else{
		 condition="and js.fahksrq>=to_date('"+strDate+"','yyyy-mm-dd') and js.fahksrq<to_date('"+endDate+"','yyyy-mm-dd')";
		 }
		  int jib=this.getJib(this.getTreeid());
		 if(jib==1){
			 
		 }else if(jib==2){
			 condition+=" and (di.id="+this.getTreeid()+" or di.fuid="+this.getTreeid()+")";
		 }else if(jib==3){
		       condition+=" and di.id="+this.getTreeid();	 
		 }
		 if(this.getMeikdqmcValue().getId()!=-1){
			 condition+=" and gon.id="+this.getMeikdqmcValue().getId();
		 }
		
		 if(this.getWeizSelectValue().getValue().equals("�ֳ�")){
			 biaot = "  select decode(grouping(di.mingc)+grouping(dilb.mingc),2,'�ֳ�����',1,dilb.mingc||'����',di.mingc)mingc,gon.quanc,getHtmlAlert('"
					+ MainGlobal.getHomeContext(this)
					+ "','Showjsd','jiesdbh',js.bianm,js.bianm),js.hetbh,decode(grouping(js.fahksrq),1,'',case when js.fahksrq=js.fahjzrq then to_char(js.fahksrq,'yyyy-mm-dd') else to_char(js.fahksrq,'yyyy-mm-dd')||'��'||to_char(js.fahjzrq,'yyyy-mm-dd') end)  fahrq,\n"
					+ " decode(grouping(js.yansjzrq),1,'',case when js.yansksrq=js.yansjzrq then to_char(js.yansksrq,'yyyy-mm-dd') else to_char(js.yansksrq,'yyyy-mm-dd')||'��'||to_char(js.yansjzrq,'yyyy-mm-dd') end) yansrq,to_char(js.jiesrq,'yyyy-mm-dd'),sum(js.ches) as ches,\n"
					+ " sum( js.jiessl) jiessl,sum(jieszbsl.gongfsl) gongfsl, sum(jieszbsl.jieszbsl) yanssl,sum(jieszbsl.slyingk) slyingk ,sum(jieszbsl.slzhejje) slzhejje,\n"
					+ "  decode(sum(js.jiessl),0,0,round(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl),0)) jiesrl,decode(sum(js.jiessl),0,0,round(sum(jieszbsl.changfrl*js.jiessl)/sum(js.jiessl),0)) yansrl,\n"
	
					+ "	 decode(sum(decode(js.jiessl*substr(jieszbsl.hetbzrl,0,4),0,0,js.jiessl)),0,0,round(sum(js.jiessl*substr(jieszbsl.hetbzrl,0,4))/sum(decode(js.jiessl*substr(jieszbsl.hetbzrl,0,4),0,0,js.jiessl)),0)) as hetrl,\n"
	
					+ "  decode(sum(js.jiessl),0,0,round(sum(jieszbsl.rlzhejje*js.jiessl)/sum(js.jiessl),2),2) rlzhejje,\n"
					+ "  decode(sum(js.jiessl),0,0,round(sum(jieszbsl.jieslf*js.jiessl)/sum(js.jiessl),2)) jieslf,sum(jieszbsl.lfzhejje) jieszbsl,\n"
					+ "  sum(js.jiessl) jiessl,decode(sum(js.jiessl),0,0,round(sum(js.hansdj*js.jiessl)/sum(js.jiessl),2)) hansdj,sum(js.shuik) buhsmk,sum(js.meikje)shuik,sum(js.shuik+js.meikje) hansmk\n"
					+ "  ,decode(decode(sum(js.jiessl),0,0,(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl))),0,0,round(decode(sum(js.jiessl),0,0,round(sum(js.hansdj*js.jiessl)/sum(js.jiessl)))*7000/decode(sum(js.jiessl),0,0,(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl))),2)) biaomdjhs,\n"
					+ "  decode(decode(sum(js.jiessl),0,0,(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl))),0,0,round(round(decode(sum(js.jiessl),0,0,sum(js.hansdj*js.jiessl)/sum(js.jiessl))/1.17,2)*7000/decode(sum(js.jiessl),0,0,(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl))),2)) biaomdjbhs\n";
			
			 groupbysql = "group by rollup((dilb.mingc,dilb.xuh),(di.mingc,gon.quanc,js.bianm,js.fahksrq,js.fahjzrq,js.yansksrq,js.yansjzrq,js.jiesrq,dipx.xuh,js.hetbh))"
						+ " \n order by grouping(dilb.mingc) desc ,dilb.xuh,grouping(di.mingc) desc,dipx.xuh";
		
		 }else if(this.getWeizSelectValue().getValue().equals("�ֿ�")){
			 biaot = "  select decode(grouping(gon.quanc),1,'�ֿ����',gon.quanc) quanc,di.mingc,getHtmlAlert('"
					+ MainGlobal.getHomeContext(this)
					+ "','Showjsd','jiesdbh',js.bianm,js.bianm),js.hetbh,decode(grouping(js.fahksrq),1,'',case when js.fahksrq=js.fahjzrq then to_char(js.fahksrq,'yyyy-mm-dd') else to_char(js.fahksrq,'yyyy-mm-dd')||'��'||to_char(js.fahjzrq,'yyyy-mm-dd') end)  fahrq,\n"
					+ " decode(grouping(js.yansjzrq),1,'',case when js.yansksrq=js.yansjzrq then to_char(js.yansksrq,'yyyy-mm-dd') else to_char(js.yansksrq,'yyyy-mm-dd')||'��'||to_char(js.yansjzrq,'yyyy-mm-dd') end) yansrq,to_char(js.jiesrq,'yyyy-mm-dd'),sum(js.ches) as ches,\n"
					+ " sum( js.jiessl) jiessl,sum(jieszbsl.gongfsl) gongfsl, sum(jieszbsl.jieszbsl) yanssl,sum(jieszbsl.slyingk) slyingk ,sum(jieszbsl.slzhejje) slzhejje,\n"
					+ "  decode(sum(js.jiessl),0,0,round(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl),0)) jiesrl,decode(sum(js.jiessl),0,0,round(sum(jieszbsl.changfrl*js.jiessl)/sum(js.jiessl),0)) yansrl,\n"
					
					+ "	 decode(sum(decode(js.jiessl*substr(jieszbsl.hetbzrl,0,4),0,0,js.jiessl)),0,0,round(sum(js.jiessl*substr(jieszbsl.hetbzrl,0,4))/sum(decode(js.jiessl*substr(jieszbsl.hetbzrl,0,4),0,0,js.jiessl)),0)) as hetrl,\n"
					
					+ "	 decode(sum(js.jiessl),0,0,round(sum(jieszbsl.rlzhejje*js.jiessl)/sum(js.jiessl),2)) rlzhejje,\n"
					+ "  decode(sum(js.jiessl),0,0,round(sum(jieszbsl.jieslf*js.jiessl)/sum(js.jiessl),2)) jieslf,sum(jieszbsl.lfzhejje) jieszbsl,\n"
					+ " sum(js.jiessl) jiessl,decode(sum(js.jiessl),0,0,round(sum(js.hansdj*js.jiessl)/sum(js.jiessl),2)) hansdj,sum(js.shuik) buhsmk,sum(js.meikje)shuik,sum(js.shuik+js.meikje) hansmk\n"
					+ "  ,decode(decode(sum(js.jiessl),0,0,(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl))),0,0,round(decode(sum(js.jiessl),0,0,round(sum(js.hansdj*js.jiessl)/sum(js.jiessl)))*7000/decode(sum(js.jiessl),0,0,(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl))),2)) biaomdjhs,\n"
					+ "  decode(decode(sum(js.jiessl),0,0,(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl))),0,0,round(round(decode(sum(js.jiessl),0,0,sum(js.hansdj*js.jiessl)/sum(js.jiessl))/1.17,2)*7000/decode(sum(js.jiessl),0,0,(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl))),2)) biaomdjbhs\n";
			 groupbysql = "group by rollup((gon.quanc,di.mingc,js.bianm,js.fahksrq,js.fahjzrq,js.yansksrq,js.yansjzrq,js.jiesrq,js.hetbh))\n"
					+ "order by grouping(gon.quanc) desc,gon.quanc";
		
		 }else if(this.getWeizSelectValue().getValue().equals("�ֳ��ֿ�")){
			 biaot = "  select decode(grouping(di.mingc)+grouping(dilb.mingc)+grouping(gon.quanc),3,'�ֳ��ֿ����',2,dilb.mingc,di.mingc),decode(grouping(gon.quanc)+grouping(di.mingc)+grouping(js.bianm),2,di.mingc||'С��',1,gon.quanc||'С��',gon.quanc),getHtmlAlert('"
					+ MainGlobal.getHomeContext(this)
					+ "','Showjsd','jiesdbh',js.bianm,js.bianm),js.hetbh,decode(grouping(js.fahksrq),1,'',case when js.fahksrq=js.fahjzrq then to_char(js.fahksrq,'yyyy-mm-dd') else to_char(js.fahksrq,'yyyy-mm-dd')||'��'||to_char(js.fahjzrq,'yyyy-mm-dd') end ) fahrq,\n"
					+ " decode(grouping(js.yansjzrq),1,'',case when js.yansksrq=js.yansjzrq then to_char(js.yansksrq,'yyyy-mm-dd') else to_char(js.yansksrq,'yyyy-mm-dd')||'��'||to_char(js.yansjzrq,'yyyy-mm-dd') end) yansrq,to_char(js.jiesrq,'yyyy-mm-dd'),sum(js.ches) as ches,\n"
					+ " sum( js.jiessl) jiessl,sum(jieszbsl.gongfsl) gongfsl, sum(jieszbsl.jieszbsl) yanssl,sum(jieszbsl.slyingk) slyingk ,sum(jieszbsl.slzhejje) slzhejje,\n"
					+ "  decode(sum(js.jiessl),0,0,round(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl),0)) jiesrl,decode(sum(js.jiessl),0,0,round(sum(jieszbsl.changfrl*js.jiessl)/sum(js.jiessl),0)) yansrl,\n"
					
					+ "	 decode(sum(decode(js.jiessl*substr(jieszbsl.hetbzrl,0,4),0,0,js.jiessl)),0,0,round(sum(js.jiessl*substr(jieszbsl.hetbzrl,0,4))/sum(decode(js.jiessl*substr(jieszbsl.hetbzrl,0,4),0,0,js.jiessl)),0)) as hetrl,\n"
					
					+ "	 decode(sum(js.jiessl),0,0,round(sum(jieszbsl.rlzhejje*js.jiessl)/sum(js.jiessl),2)) rlzhejje,\n"
					+ "  decode(sum(js.jiessl),0,0,round(sum(jieszbsl.jieslf*js.jiessl)/sum(js.jiessl),2)) jieslf,sum(jieszbsl.lfzhejje) jieszbsl,\n"
					+ " sum(js.jiessl) jiessl,decode(sum(js.jiessl),0,0,round(sum(js.hansdj*js.jiessl)/sum(js.jiessl),2)) hansdj,sum(js.shuik) buhsmk,sum(js.meikje)shuik,sum(js.shuik+js.meikje) hansmk\n"
					+ "  ,decode(decode(sum(js.jiessl),0,0,(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl))),0,0,round(decode(sum(js.jiessl),0,0,round(sum(js.hansdj*js.jiessl)/sum(js.jiessl)))*7000/decode(sum(js.jiessl),0,0,(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl))),2)) biaomdjhs,\n"
					+ "  decode(decode(sum(js.jiessl),0,0,(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl))),0,0,round(round(decode(sum(js.jiessl),0,0,sum(js.hansdj*js.jiessl)/sum(js.jiessl))/1.17,2)*7000/decode(sum(js.jiessl),0,0,(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl))),2)) biaomdjbhs\n";
			 groupbysql = "group by rollup((dilb.mingc,dilb.xuh),di.mingc,gon.quanc,(js.bianm,js.fahksrq,js.fahjzrq,js.yansksrq,js.yansjzrq,js.jiesrq,js.hetbh,dipx.xuh)) "
					+ " order by grouping(dilb.mingc) desc,dilb.xuh,grouping(di.mingc) desc,di.mingc,grouping(gon.quanc)desc,gon.quanc,grouping(js.bianm)desc";
		 
		 }else if(this.getWeizSelectValue().getValue().equals("�ֿ�ֳ�")){
			 biaot = "  select decode(grouping(di.mingc)+grouping(gon.quanc),2,'�ֿ�ֳ�����',1,gon.quanc||'����',gon.quanc),decode(grouping(js.bianm),1,di.mingc||'����',di.mingc),getHtmlAlert('"
					+ MainGlobal.getHomeContext(this)
					+ "','Showjsd','jiesdbh',js.bianm,js.bianm),js.hetbh,decode(grouping(js.fahksrq),1,'',case when js.fahksrq=js.fahjzrq then to_char(js.fahksrq,'yyyy-mm-dd') else to_char(js.fahksrq,'yyyy-mm-dd')||'��'||to_char(js.fahjzrq,'yyyy-mm-dd') end ) fahrq,\n"
					+ " decode(grouping(js.yansjzrq),1,'',case when js.yansksrq=js.yansjzrq then to_char(js.yansksrq,'yyyy-mm-dd') else to_char(js.yansksrq,'yyyy-mm-dd')||'��'||to_char(js.yansjzrq,'yyyy-mm-dd') end) yansrq,to_char(js.jiesrq,'yyyy-mm-dd'),sum(js.ches) as ches,\n"
					+ " sum( js.jiessl) jiessl,sum(jieszbsl.gongfsl) gongfsl, sum(jieszbsl.jieszbsl) yanssl,sum(jieszbsl.slyingk) slyingk ,sum(jieszbsl.slzhejje) slzhejje,\n"
					+ "  decode(sum(js.jiessl),0,0,round(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl),0)) jiesrl,decode(sum(js.jiessl),0,0,round(sum(jieszbsl.changfrl*js.jiessl)/sum(js.jiessl),0)) yansrl,\n"
					
					+ "	 decode(sum(decode(js.jiessl*substr(jieszbsl.hetbzrl,0,4),0,0,js.jiessl)),0,0,round(sum(js.jiessl*substr(jieszbsl.hetbzrl,0,4))/sum(decode(js.jiessl*substr(jieszbsl.hetbzrl,0,4),0,0,js.jiessl)),0)) as hetrl,\n"
					
					+ "	 decode(sum(js.jiessl),0,0,round(sum(jieszbsl.rlzhejje*js.jiessl)/sum(js.jiessl),2)) rlzhejje,\n"
					+ "  decode(sum(js.jiessl),0,0,round(sum(jieszbsl.jieslf*js.jiessl)/sum(js.jiessl),2)) jieslf,sum(jieszbsl.lfzhejje) jieszbsl,\n"
					+ " sum(js.jiessl) jiessl,decode(sum(js.jiessl),0,0,round(sum(js.hansdj*js.jiessl)/sum(js.jiessl),2)) hansdj,sum(js.shuik) buhsmk,sum(js.meikje)shuik,sum(js.shuik+js.meikje) hansmk\n"
					+ "  ,decode(decode(sum(js.jiessl),0,0,(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl))),0,0,round(decode(sum(js.jiessl),0,0,round(sum(js.hansdj*js.jiessl)/sum(js.jiessl)))*7000/decode(sum(js.jiessl),0,0,(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl))),2)) biaomdjhs,\n"
					+ "  decode(decode(sum(js.jiessl),0,0,(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl))),0,0,round(round(decode(sum(js.jiessl),0,0,sum(js.hansdj*js.jiessl)/sum(js.jiessl))/1.17,2)*7000/decode(sum(js.jiessl),0,0,(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl))),2)) biaomdjbhs\n";
			 groupbysql = "group by rollup(gon.quanc,di.mingc,(js.bianm,js.fahksrq,js.fahjzrq,js.yansksrq,js.yansjzrq,js.jiesrq,js.hetbh,dipx.xuh)) "
					+ " order by grouping(gon.quanc) desc,gon.quanc,grouping(di.mingc) desc,di.mingc,grouping(js.bianm) desc";
			 
		 }
	
		
//	sql="  select decode(grouping(di.mingc),1,'"+this.getWeizSelectValue().getValue()+"����',di.mingc),gon.quanc,js.bianm,case when js.fahksrq=js.fahjzrq then to_char(js.fahksrq,'yyyy-mm-dd') else to_char(js.fahksrq,'yyyy-mm-dd')||'--'||to_char(js.fahjzrq,'yyyy-mm-dd') end  fahrq,\n"+
//	" case when js.yansksrq=js.yansjzrq then to_char(js.yansksrq,'yyyy-mm-dd') else to_char(js.yansksrq,'yyyy-mm-dd')||'--'||to_char(js.yansjzrq,'yyyy-mm-dd') end yansrq,js.jiesrq,js.ches,\n"+
//	" sum( js.jiessl) jiessl,sum(jieszbsl.gongfsl) gongfsl, sum(jieszbsl.jieszbsl) yanssl,sum(jieszbsl.slyingk) slyingk ,sum(jieszbsl.slzhejje) slzhejje,\n"+
//	"  decode(sum(js.jiessl),0,0,round(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl),2)) jiesrl,decode(sum(js.jiessl),0,0,round(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl),2)) yansrl,'' hetrl,decode(sum(js.jiessl),0,0,round(sum(jieszbsl.rlzhejbz*js.jiessl)/sum(js.jiessl),2)) rlzhejbz,\n"+
//	"  decode(sum(js.jiessl),0,0,round(sum(jieszbsl.jieslf*js.jiessl)/sum(js.jiessl),2)) jieslf,sum(jieszbsl.lfzhejje) jieszbsl,\n"+
//	" sum(js.jiessl) jiessl,decode(sum(js.jiessl),0,0,round(sum(js.hansdj*js.jiessl)/sum(js.jiessl),2)) hansdj,sum(js.hansmk) hansmk,sum(js.shuik)shuik\n"+
//	"  ,decode(decode(sum(js.jiessl),0,0,(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl))),0,0,round(decode(sum(js.jiessl),0,0,round(sum(js.hansdj*js.jiessl)/sum(js.jiessl)))*7000/decode(sum(js.jiessl),0,0,(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl))),2)) biaomdjhs,\n"+
//	"  decode(decode(sum(js.jiessl),0,0,(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl))),0,0,round(round(decode(sum(js.jiessl),0,0,sum(js.hansdj*js.jiessl)/sum(js.jiessl))/1.17,2)*7000/decode(sum(js.jiessl),0,0,(sum(jieszbsl.jiesrl*js.jiessl)/sum(js.jiessl))),2)) biaomdjbhs\n"+
	sql = biaot
				+ "    from diancjsmkb js , \n"
				+ "		(\n"
				+ "select nvl(rel.jiesdid,quanl.jiesdid) jiesdid,nvl(gongfrl,0) gongfrl,nvl( changfrl,0) changfrl,nvl(jiesrl,0) jiesrl,nvl(rlyingk,0) rlyingk,nvl(rlzhejbz,0)rlzhejbz ,nvl(rlzhejje,0) rlzhejje,\n"
				+ "nvl(gongflf,0) gongflf,nvl( changflf,0) changflf,nvl(jieslf,0) jieslf,nvl(lfyingk,0) lfyingk,nvl(lfzhejbz,0)lfzhejbz ,nvl(lfzhejje,0) lfzhejje,\n"
				+ "nvl(gongfsl,0) gongfsl,nvl( changfsl,0) changfsl,nvl(jieszbsl,0) jieszbsl,nvl(slyingk,0) slyingk,nvl(slzhejbz,0)slzhejbz ,nvl(slzhejje,0) slzhejje,nvl(hetbzrl,0)hetbzrl\n"
				+ " from\n"
				+ "(select ji.jiesdid  jiesdid,ji.hetbz hetbzrl,ji.gongf gongfrl, ji.changf changfrl,ji.jies jiesrl,ji.yingk rlyingk,ji.zhejbz rlzhejbz,ji.zhejje rlzhejje from  jieszbsjb ji  left join zhibb zhi on(ji.zhibb_id=zhi.id) where zhi.mingc='�յ�����λ��ֵ') rel\n"
				+ "full outer join\n"
				+ "(select ji.jiesdid jiesdid,ji.gongf gongflf, ji.changf changflf,ji.jies jieslf,ji.yingk lfyingk,ji.zhejbz lfzhejbz,ji.zhejje lfzhejje from  jieszbsjb ji left join zhibb zhi on(ji.zhibb_id=zhi.id) where zhi.mingc='�����ȫ��') quanl\n"
				+ "on (rel.jiesdid=quanl.jiesdid)\n"
				+ "full outer join\n"
				+ "(select ji.jiesdid jiesdid,ji.gongf gongfsl,ji.changf changfsl,ji.jies jieszbsl,ji.yingk slyingk,ji.zhejbz slzhejbz,ji.zhejje slzhejje from  jieszbsjb ji left join zhibb zhi on(ji.zhibb_id=zhi.id) where zhi.mingc='����' ) sl on(quanl.jiesdid=sl.jiesdid)\n"
				+ "      ) jieszbsl\n"
				+ "  , gongysb gon \n"
				+ "  , hetb he \n"
				+
				// " , liucztb zt \n"+
				// " , leibztb lz \n"+
				// " , liuclbb ll \n"+
				"  , diancxxb di \n"
				+ "  ,dianckjpxb dipx\n"
				+ " ,dianclbb dilb"
				+ "  where  jieszbsl.jiesdid(+)=js.id and gon.id(+)=js.gongysb_id \n"
				+ "and he.id(+)=js.hetb_id \n"
				+ "and js.diancxxb_id=di.id(+) and di.id=dipx.diancxxb_id and dipx.kouj='�±�' and dilb.id=di.dianclbb_id\n"
				+ condition + "\n" +
	groupbysql;
		ResultSet rs=cn.getResultSet(sql);
		Report rt=new Report();
		
		//�����ͷ����
		 String ArrHeader[][]=new String[2][];
		 if(this.getWeizSelectValue().getValue().equals("�ֳ�")){
			 ArrHeader[0]=new String[] {"�糧����","��Ӧ������","���㵥��","��ͬ���","��������","��������","��������","����",
					 "��������","��������","��������","ӯ������","ӯ�����","����<br>����","����<br>����","��ͬ<br>����","�������","��","���۽��"
					 ,"��������","��˰<br>����","�ۿ�ϼ�","�ۿ�˰��","��˰�ϼ�","��ú����","��ú����"};
			 
			 ArrHeader[1]=new String[]{"�糧����","��Ӧ������","���㵥��","��ͬ���","��������","��������","��������","����",
					 "��������","��������","��������","ӯ������","ӯ�����","����<br>����","����<br>����","��ͬ<br>����","�������","��","���۽��"
					 ,"��������","��˰<br>����","�ۿ�ϼ�","�ۿ�˰��","��˰�ϼ�","��˰","����˰"};
		
		 }else if(this.getWeizSelectValue().getValue().equals("�ֿ�")){
			 ArrHeader[0]=new String[] {"������λ","�糧����","���㵥��","��ͬ���","��������","��������","��������","����",
					 "��������","��������","��������","ӯ������","ӯ�����","����<br>����","����<br>����","��ͬ<br>����","�������","��","���۽��"
					 ,"��������","��˰<br>����","�ۿ�ϼ�","�ۿ�˰��","��˰�ϼ�","��ú����","��ú����"};
			 
			 ArrHeader[1]=new String[]{"������λ","�糧����","���㵥��","��ͬ���","��������","��������","��������","����",
					 "��������","��������","��������","ӯ������","ӯ�����","����<br>����","����<br>����","��ͬ<br>����","�������","��","���۽��"
					 ,"��������","��˰<br>����","�ۿ�ϼ�","�ۿ�˰��","��˰�ϼ�","��˰","����˰"};
		 
		 }else if(this.getWeizSelectValue().getValue().equals("�ֳ��ֿ�")){
			 ArrHeader[0]=new String[] {"�糧����","������λ","���㵥��","��ͬ���","��������","��������","��������","����",
					 "��������","��������","��������","ӯ������","ӯ�����","����<br>����","����<br>����","��ͬ<br>����","�������","��","���۽��"
					 ,"��������","��˰<br>����","�ۿ�ϼ�","�ۿ�˰��","��˰�ϼ�","��ú����","��ú����"};
			 
			 ArrHeader[1]=new String[]{"�糧����","������λ","���㵥��","��ͬ���","��������","��������","��������","����",
					 "��������","��������","��������","ӯ������","ӯ�����","����<br>����","����<br>����","��ͬ<br>����","�������","��","���۽��"
					 ,"��������","��˰<br>����","�ۿ�ϼ�","�ۿ�˰��","��˰�ϼ�","��˰","����˰"};
		 
		 }else if(this.getWeizSelectValue().getValue().equals("�ֿ�ֳ�")){
			 ArrHeader[0]=new String[] {"������λ","�糧����","���㵥��","��ͬ���","��������","��������","��������","����",
					 "��������","��������","��������","ӯ������","ӯ�����","����<br>����","����<br>����","��ͬ<br>����","�������","��","���۽��"
					 ,"��������","��˰<br>����","�ۿ�ϼ�","�ۿ�˰��","��˰�ϼ�","��ú����","��ú����"};
			 
			 ArrHeader[1]=new String[]{"������λ","�糧����","���㵥��","��ͬ���","��������","��������","��������","����",
					 "��������","��������","��������","ӯ������","ӯ�����","����<br>����","����<br>����","��ͬ<br>����","�������","��","���۽��"
					 ,"��������","��˰<br>����","�ۿ�ϼ�","�ۿ�˰��","��˰�ϼ�","��˰","����˰"};
			 
		 }
//		 ArrHeader[0]=new String[] {"�糧����","������λ","�������㵥����","��ͬ���","��������","��������","��������","����",
//				 "��������","��������","��������","ӯ������","ӯ�����","��������","��������","��ͬ����","�������","��","���۽��"
//				 ,"��������","��˰����","�ۿ�ϼ�","��ú����","��ú����"};
//		 ArrHeader[1]=new String[]{"�糧����","������λ","�������㵥����","��ͬ���","��������","��������","��������","����",
//				 "��������","��������","��������","ӯ������","ӯ�����","��������","��������","��ͬ����","�������","��","���۽��"
//				 ,"��������","��˰����","�ۿ�ϼ�","��˰","����˰"};
		
		 //		�п�
		 int ArrWidth[];
		 String ArrFormat[]=new String[]{"","","","","","","","","","","","","0.00","","","","","0.00","","0.00","0.00","0.00","0.00","0.00","0.00","0.00"};
		 if (this.getWeizSelectValue().getValue().equals("�ֿ�")) {
			ArrWidth = new int[] { 200, 80, 100, 160, 150, 150, 70, 40, 80, 80, 80, 60, 80, 40, 40, 40, 60, 30, 70, 80, 45, 90, 90, 90, 45, 45 };
		    ArrFormat=new String[]{"","","","","","","","","","","","","0.00","","","","","0.00","","0.00","0.00","0.00","0.00","0.00","0.00","0.00"};
		} else if (this.getWeizSelectValue().getValue().equals("�ֳ�")) {
			ArrWidth = new int[] { 80, 200, 100, 160, 150, 150, 70, 40, 80, 80, 80, 60, 80, 40, 40, 40, 60, 30, 70, 80, 45, 90, 90, 90, 45, 45 };
			 ArrFormat=new String[]{"","","","","","","","","","","","","0.00","","","","","0.00","","0.00","0.00","0.00","0.00","0.00","0.00","0.00"};
		} else if (this.getWeizSelectValue().getValue().equals("�ֳ��ֿ�")) {
			ArrWidth = new int[] { 80, 200, 100, 160, 150, 150, 70, 40, 80, 80, 80, 60, 80, 40, 40, 40, 60, 30, 70, 80, 45, 90, 90, 90, 45, 45 };
			 ArrFormat=new String[]{"","","","","","","","","","","","","0.00","","","","","0.00","","0.00","0.00","0.00","0.00","0.00","0.00","0.00"};
		} else if (this.getWeizSelectValue().getValue().equals("�ֿ�ֳ�")) {
			ArrWidth = new int[] { 200, 80, 100, 160, 150, 150, 70, 40, 80, 80, 80, 60, 80, 40, 40, 40, 60, 30, 70, 80, 45, 90, 90, 90, 45, 45 };
			 ArrFormat=new String[]{"","","","","","","","","","","","","0.00","","","","","0.00","","0.00","0.00","0.00","0.00","0.00","0.00","0.00"};
		}else{
			ArrWidth = new int[] { 80, 200, 100, 160, 150, 150, 70, 40, 80, 80, 80, 60, 80, 40, 40, 40, 60, 70, 70, 90, 45, 90, 90, 90, 45, 45 };
		}
		
		 // ����ҳ����
		rt.setTitle("���㵥ͳ�Ʋ�ѯ",ArrWidth);
		
		rt.setDefaultTitle(1,5,"���λ:"+_Danwqc,Table.ALIGN_LEFT);
		String strMonth=DateUtil.FormatDate(new Date());
		rt.setDefaultTitle(9,3,strMonth,Table.ALIGN_CENTER);
//		rt.setDefaultTitle(rt.title.getCols()-1,2,"��ȼ01��",Table.ALIGN_RIGHT);
		
		//����
		rt.setBody(new Table(rs,2,0,7));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(32);
		rt.body.setColFormat(ArrFormat);
		rt.body.setHeaderData(ArrHeader);//��ͷ����
		rt.body.ShowZero=false;
//		rt.body.mergeFixedRow();
		rt.body.mergeFixedRowCol();
//		rt.body.mergeFixedCols();
	  if(this.getWeizSelectValue().getValue().equals("�ֳ�")){
		  rt.body.setFixedCols(2);
	 }else if(this.getWeizSelectValue().getValue().equals("�ֿ�")){
	 }else if(this.getWeizSelectValue().getValue().equals("�ֳ��ֿ�")){
		 
	 }else if(this.getWeizSelectValue().getValue().equals("�ֿ�ֳ�")){
	 }
		rt.body.setColAlign(2,Table.ALIGN_LEFT);
		rt.body.setColAlign(3,Table.ALIGN_LEFT);
		rt.body.setColAlign(4,Table.ALIGN_LEFT);
		rt.body.setColAlign(5,Table.ALIGN_LEFT);
		rt.body.setColAlign(6,Table.ALIGN_LEFT);
		rt.body.setColAlign(7,Table.ALIGN_LEFT);
		rt.body.setColAlign(8,Table.ALIGN_RIGHT);
	
		rt.body.setColAlign(9,Table.ALIGN_RIGHT);
		rt.body.setColAlign(10,Table.ALIGN_RIGHT);
		rt.body.setColAlign(11,Table.ALIGN_RIGHT);
		rt.body.setColAlign(12,Table.ALIGN_RIGHT);
		rt.body.setColAlign(13,Table.ALIGN_RIGHT);
		rt.body.setColAlign(14,Table.ALIGN_RIGHT);	
		rt.body.setColAlign(15,Table.ALIGN_RIGHT);
		rt.body.setColAlign(16,Table.ALIGN_RIGHT);
		rt.body.setColAlign(17,Table.ALIGN_RIGHT);
		rt.body.setColAlign(18,Table.ALIGN_RIGHT);
		rt.body.setColAlign(19,Table.ALIGN_RIGHT);
		rt.body.setColAlign(20,Table.ALIGN_RIGHT);
		rt.body.setColAlign(21,Table.ALIGN_RIGHT);
		rt.body.setColAlign(22,Table.ALIGN_RIGHT);
		rt.body.setColAlign(23,Table.ALIGN_RIGHT);
		rt.body.setColAlign(24,Table.ALIGN_RIGHT);
		rt.body.setColAlign(25,Table.ALIGN_RIGHT);
		/*rt.body.setColFormat(4,"0");
		rt.body.setColFormat(5,"0");
		rt.body.setColFormat(6,"0.00");
		rt.body.setColFormat(7,"0.00");
		rt.body.setColFormat(8,"0.00");
		rt.body.setColFormat(9,"0.00");
		rt.body.setColFormat(10,"0.00");
		rt.body.setColFormat(11,"0.00");
		rt.body.setColFormat(12,"0.00");
		rt.body.setColFormat(13,"0.00");
		rt.body.setColFormat(14,"0.00");
		rt.body.setColFormat(15,"0.00");
		rt.body.setColFormat(16,"0.00");
		rt.body.setColFormat(17,"0.00");
		rt.body.setColFormat(21,"0.00");
		rt.body.setColFormat(22,"0.00");
		rt.body.setColFormat(23,"0.00");
		rt.body.setColFormat(24,"0.00");*/
//		rt.body.setColFormat(26,"0.00");
//		rt.body.setColFormat(27,"0.00");	
		
		//ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(2,1,"��׼:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(6,1,"�Ʊ�:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(10,1,"���:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(26,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT);
		
		//����ҳ��
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

	private boolean _CreateChick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateChick = true;
	}
	
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
			getPrintTable();
		}
		if(_CreateChick){
			_CreateChick=false;
			Create();
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
			visit.setboolean1(true);
			visit.setDefaultTree(null);
			getDiancmcModels();
			setMeikdqmcValue(null);
			this.setMeikdqmcModel(null);
			getMeikdqmcModels();
			setWeizSelectModel(null);
			setWeizSelectValue(null);
//			getSelectData();
		}
		 if(visit.getboolean1()){//���ھ�ѡ��仯ʱ��ʼ��һ�¶���
			visit.setboolean1(false);
			 this.setTreeid(null);
			 this.setMeikdqmcModel(null);
			 this.setMeikdqmcValue(null);
		 }
		    this.getToolbar();
           this.getSelectData();
		setUserName(visit.getRenymc());
		
		blnIsBegin = true;
		
		
	}
//��ʾ������
//	����״̬������
	public void getSelectData() {
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
			tb1.addText(new ToolbarText("ͳ�ƿھ�:"));
			ComboBox WeizSelect = new ComboBox();
			WeizSelect.setId("Weizx");
			WeizSelect.setWidth(80);
			WeizSelect.setLazyRender(true);
			WeizSelect.setTransform("WeizSelectx");
			WeizSelect.setListeners("'change':function(){"+MainGlobal.getExtMessageShow("��ȴ�....", "��ȴ�....", 200)+"document.Form0.submit();}");
			tb1.addField(WeizSelect);	
		tb1.addText(new ToolbarText("-"));
//		���̷�ʽ������
//		tb1.addText(new ToolbarText("���̷�ʽ��"));
//		    ComboBox liucfs = new ComboBox();
//		    liucfs.setId("liucfs");
//		    liucfs.setWidth(80);
//		    liucfs.setLazyRender(true);
//		    liucfs.setTransform("liucfs");
//		    liucfs.setListeners("'change':function(){document.getElementById('liucfs').value=liucfs.getValue();}");
//	    	tb1.addField(liucfs);
//		���ڷ�Χѡ��
		 DateField startdf=new DateField();
		 startdf.setValue(this.getStartdate());
		 startdf.Binding("startdate", "");
//		 tb1.addText(new ToolbarText("�������ڣ�"));
		 ComboBox cb=new ComboBox();
		  cb.setWidth(85);
		  cb.setTransform("RiqLx") ;
		 tb1.addField(cb);
		 tb1.addField(startdf);
		 tb1.addText(new ToolbarText("��"));
		 DateField enddf=new DateField();
		 enddf.setValue(this.getEnddate());
		 enddf.Binding("enddate", "");
		 tb1.addField(enddf);
			
		tb1.addText(new ToolbarText("-"));
		
//		�糧��
//		if(!this.getWeizSelectValue().getValue().equals("�ֿ�")){
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree"
				,""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
//	}
//		if(!this.getWeizSelectValue().getValue().equals("�ֳ�")){
//      ��Ӧ��
			tb1.addText(new ToolbarText("��Ӧ��:"));
			ComboBox GongysComboBox=new ComboBox();
			GongysComboBox.setId("GongysComboBox");
			GongysComboBox.setWidth(200);
			GongysComboBox.setListWidth(250);
			GongysComboBox.setTransform("MeikdqmcDropDown");
			tb1.addField(GongysComboBox);
			tb1.addText(new ToolbarText("-"));
//		}
		
		ToolbarButton tb = new ToolbarButton(null,"��ѯ","function(){"+MainGlobal.getExtMessageShow("��ȴ�....", "��ȴ�....", 200)+"document.Form0.submit();}");
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
	
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	// �õ�����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
    private int getJib(String dcid){
    	int jib=0;
    	JDBCcon con=new JDBCcon();
    	ResultSetList rsl=con.getResultSetList(" select jib from diancxxb where id="+dcid+"\n");
    	if(rsl.next()){
    		jib=rsl.getInt("jib");
    	}
    	con.Close();
    	return jib;
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
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
	
	
	private String m_yuebmc;
	public void setYuebmc(String yuebmc){
		m_yuebmc=yuebmc;
	}
	public String getYuebmc(){
		return m_yuebmc;
	}
	
	private void Create() {
		// Ϊ "ˢ��" ��ť��Ӵ������
	}
	

//	�糧����
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel10() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel10();
	}
	
	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
			((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean10();
	}
	
	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean10()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean10(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		 setDiancmcModel(new IDropDownModel(sql)) ;
		 return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

		public void setDiancmcModel(IPropertySelectionModel _value) {
			((Visit)getPage().getVisit()).setProSelectionModel10(_value);
		}
		
//		ú�����
		private boolean _meikdqmc = false;
	    public IPropertySelectionModel getMeikdqmcModel() {
	    	if(((Visit)getPage().getVisit()).getProSelectionModel3() == null){
	    		getMeikdqmcModels();
	    	}
	    	return ((Visit)getPage().getVisit()).getProSelectionModel3();
	    }

	    public IDropDownBean getMeikdqmcValue() {
	    	if(((Visit)getPage().getVisit()).getDropDownBean3() == null){
				((Visit)getPage().getVisit()).setDropDownBean3((IDropDownBean)getMeikdqmcModel().getOption(0));
			}
			return ((Visit)getPage().getVisit()).getDropDownBean3();
	    }
	    
	    public void setMeikdqmcValue(IDropDownBean Value){
	    	if (Value==null){
	    		((Visit)getPage().getVisit()).setDropDownBean3(Value);
				return;
			}
	    	if (((Visit)getPage().getVisit()).getDropDownBean3().getId()==Value.getId()) {
	    		_meikdqmc = false;
			}else{
				_meikdqmc = true;
			}
	    	((Visit)getPage().getVisit()).setDropDownBean3(Value);
	    }
	    
	    public IPropertySelectionModel getMeikdqmcModels(){
	        long  lngDiancxxbID= ((Visit) getPage().getVisit()).getDiancxxb_id();
	        String sql="";
	        
//	        if (((Visit) getPage().getVisit()).isDCUser()){
//	        	sql="select distinct gys.id,gys.mingc from diaor16bb d,gongysb gys where d.gongysb_id=gys.id and diancxxb_id=" + lngDiancxxbID;
//	        }else if(((Visit) getPage().getVisit()).isJTUser()){
//	        	sql="select distinct gys.id,gys.mingc from diaor16bb d ,gongysb gys where d.gongysb_id=gys.id  ";
//	        }else {
//	        	sql="select distinct gys.id,gys.mingc from diaor16bb d,diancxxb dc,gongysb gys where d.gongysb_id=gys.id and d.diancxxb_id=dc.id and dc.fuid=" + lngDiancxxbID;	        	
//	        }
	        sql=" select g.id,g.quanc  from gongysb g,diancjsmkb d where d.gongysb_id=g.id";
	        ((Visit)getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql,"ȫ��"));
	        return ((Visit)getPage().getVisit()).getProSelectionModel3();
	    }
	    
	    public void setMeikdqmcModel(IPropertySelectionModel _value) {
	    	((Visit)getPage().getVisit()).setProSelectionModel3(_value);
	    }
		


	//��������
	public boolean _diqumcchange = false;
	private IDropDownBean _DiqumcValue;

	public IDropDownBean getDiqumcValue() {
		if(_DiqumcValue==null){
			_DiqumcValue=(IDropDownBean)getIDiqumcModels().getOption(0);
		}
		return _DiqumcValue;
	}

	public void setDiqumcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiqumcValue != null) {
			id = _DiqumcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diqumcchange = true;
			} else {
				_diqumcchange = false;
			}
		}
		_DiqumcValue = Value;
	}

	private IPropertySelectionModel _IDiqumcModel;

	public void setIDiqumcModel(IPropertySelectionModel value) {
		_IDiqumcModel = value;
	}

	public IPropertySelectionModel getIDiqumcModel() {
		if (_IDiqumcModel == null) {
			getIDiqumcModels();
		}
		return _IDiqumcModel;
	}

	public IPropertySelectionModel getIDiqumcModels() {
		String sql="";
		
		sql = "select mk.meikdqbm,mk.meikdqmc from meikdqb mk order by meikdqmc";
//		System.out.println(sql);
		
		_IDiqumcModel = new IDropDownModel(sql);
		return _IDiqumcModel;
	}
	

	
	public String getcontext() {
			return "";
//		return "var  context='http://"
//				+ this.getRequestCycle().getRequestContext().getServerName()
//				+ ":"
//				+ this.getRequestCycle().getRequestContext().getServerPort()
//				+ ((Visit) getPage().getVisit()).get() + "';";
	}

	public SortMode getSort() {
		return SortMode.USER;
	}
	
	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page����
	protected void initialize() {
		_pageLink = "";
	}
	// ��ʼ������ֵ
	private String startdate;

	public void setStartdate(String startdate) {

		this.startdate = startdate;
	}

	public String getStartdate() {
		if (startdate == null || startdate.equals("")) {
			Date today = new Date();
			today.setDate(today.getDate());
			this.setStartdate(DateUtil.FormatDate(today));
		}
		return this.startdate;
	}

	// ����������ֵ
	private String enddate;

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getEnddate() {
		if (enddate == null || enddate.equals("")) {
			this.setEnddate(DateUtil.FormatDate(new Date()));
		}
		return this.enddate;
	}

	// λ�������˵�--�ھ����������
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getWeizSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setWeizSelectValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean2()) {

			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
			
			 
		}
	}

	public void setWeizSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getWeizSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getWeizSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getWeizSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "�ֳ�"));
		list.add(new IDropDownBean(2, "�ֿ�"));
		list.add(new IDropDownBean(3, "�ֳ��ֿ�"));
		list.add(new IDropDownBean(4, "�ֿ�ֳ�"));
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(list));
	}

//	 ��������������
	
	public IDropDownBean getRiqLxValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getRiqLxModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setRiqLxValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean5()) {

			((Visit) getPage().getVisit()).setboolean5(true);
			((Visit) getPage().getVisit()).setDropDownBean5(Value);
			
			 
		}
	}

	public void setRiqLxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getRiqLxModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getRiqLxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getRiqLxModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "��������"));
		list.add(new IDropDownBean(2, "��������"));
//		list.add(new IDropDownBean(3, "�ֳ��ֿ�"));
//		list.add(new IDropDownBean(4, "�ֿ�ֳ�"));
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(list));
	}
}
