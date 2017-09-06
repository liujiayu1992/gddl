package com.zhiren.jt.hebfgskh;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.Money;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;

import org.apache.tapestry.contrib.palette.SortMode;


public class Yuedkhmrzcreport_hb  extends BasePage implements PageValidateListener{
	
//	 判断是否是集团用户
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团
	}
//	 判断是否是公司用户
	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
	}
//	 判断是否是电厂用户
	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
	}
	//开始日期
	private Date _BeginriqValue = new Date();
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue = new Date();
		}
		return _BeginriqValue;
	}
	public void setBeginriqDate(Date _value) {
		if (_BeginriqValue.equals(_value)) {
		} else {
			_BeginriqValue = _value;
		}
	}
	// 消息框
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
	
	//按钮事件－“刷新”
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
	}
	
	//页面初始化刷新事件
	private void Refurbish() {
        //为 "刷新" 按钮添加处理程序
		isBegin=true;
		getSelectData();
	}
	
	/**
	 * 页面开始时初始化方法
	 */
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setNianfValue(null);
			setYuefValue(null);
			getNianfModels();
			getYuefModels();
			this.setTreeid(null);
			this.getTree();
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			
            //	begin方法里进行初始化设置
			visit.setString1(null);

			String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
			if (pagewith != null) {

				visit.setString1(pagewith);
			}
//			visit.setString1(null);保存传递的非默认纸张的样式
			isBegin=true;
			this.getSelectData();
		}
		if(nianfchanged){
			nianfchanged=false;
			Refurbish();
		}
		if(yuefchanged){
			yuefchanged=false;
			Refurbish();
		}
		if(_fengschange){
			_fengschange=false;
			Refurbish();
		}
		getToolBars() ;
		Refurbish();
	}
	
	/*
	 * 自定义报表
	 * @RT_HET=yunsjhcx
	 */
	private String RT_HET="yunsjhcx";
	private String mstrReportName="yunsjhcx";
	
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		if (mstrReportName.equals(RT_HET)){
			return getSelectData();
		}else{
			return "无此报表";
		}
	}
	
	public int getZhuangt() {
		return 1;
	}

	private int intZhuangt=0;
	public void setZhuangt(int _value) {
		intZhuangt=1;
	}

	private boolean isBegin=false;
	
	/**
	 * 查询数据
	 * @author chenzt
	 * @return
	 */
	private String getSelectData(){
		StringBuffer strSQL= new StringBuffer();
		_CurrentPage=1;
		_AllPages=1;
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		//判断用户进入系统后数据的初始状态
		String zhuangt="";
		if(visit.getRenyjb()==3){
			zhuangt="";
		}else if(visit.getRenyjb()==2){
			zhuangt=" and (sl.zhuangt=1 or sl.zhuangt=2)";
		}else if(visit.getRenyjb()==1){
			zhuangt=" and sl.zhuangt=2";
		}
		//得到时间条件的值
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		long intMonth;
		if(getYuefValue() == null){
			intMonth = DateUtil.getMonth(new Date());
		}else{
			intMonth = getYuefValue().getId();
		}

		String strGongsID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String arrFormat[] =null;
		String titlename="月度考核明细热值差表";
		//报表内容
		String yue="";
		if(getYuefValue().toString().length()==1){
			yue="0"+getYuefValue();
		}
		
		//获得cg
//		
		
		String dianwmc="";
		String groupby="";
		String orderby="";
		String havingnot="";
	
		
		String ss=
			" select mingc, dangy,cquannh,cr,lquannh,lr,rezcx,rezcd,jiz,chaz,beiz, decode(sign(jif),-1,'<font color=red >'||Formatxiaosws(jif,2)||'</font>',1,'<font color=blue >'\n" + 
	"||Formatxiaosws(jif,2)||'</font>',0,'0')  jif \n" + " FROM (\n" + 
			"select d.mingc,decode(dangy,'1','当月','2','累计') dangy,\n" + 
			"   cquannh,round_new(nvl(cquannh,0) * 1000 / 4.1816, 0) cr,\n" + 
			"   lquannh,round_new(nvl(lquannh,0) * 1000 / 4.1816, 0) lr ,\n" + 
			"   (cquannh-lquannh) rezcx,\n" + 

			" round_new(nvl(cquannh,0) * 1000 / 4.1816, 0)-round_new(nvl(lquannh,0) * 1000 / 4.1816, 0) rezcd,\n" +
			"  decode(dangy,'1',getjiz(round_new(nvl(cquannh,0) * 1000 / 4.1816, 0)\n" + 
			" -round_new(nvl(lquannh,0) * 1000 / 4.1816, 0),'KHXM_RZCBY','原煤'),'2',getjiz(round_new(nvl(cquannh,0) * 1000 / 4.1816, 0)\n" + 
			" -round_new(nvl(lquannh,0) * 1000 / 4.1816, 0),'KHXM_RZCLJ','原煤'))  jiz,\n" + 
			"decode(dangy,'1',getchaz(round_new(nvl(cquannh,0) * 1000 / 4.1816, 0)\n" + 
			" -round_new(nvl(lquannh,0) * 1000 / 4.1816, 0),'KHXM_RZCBY','原煤'),'2',getchaz(round_new(nvl(cquannh,0) * 1000 / 4.1816, 0)\n" + 
			" -round_new(nvl(lquannh,0) * 1000 / 4.1816, 0),'KHXM_RZCLJ','原煤'))\n" + 
			"chaz\n" + 
			"  ,decode(dangy,'1',getbiaoz(round_new(nvl(cquannh,0) * 1000 / 4.1816, 0)\n" + 
			" -round_new(nvl(lquannh,0) * 1000 / 4.1816, 0),'KHXM_RZCBY','原煤'),'2',getbiaoz(round_new(nvl(cquannh,0) * 1000 / 4.1816, 0)\n" + 
			" -round_new(nvl(lquannh,0) * 1000 / 4.1816, 0),'KHXM_RZCLJ','原煤'))  beiz,\n" + 
			"\n" + 
			"decode(dangy,'1', round_new( nvl(getbiaoz(round_new(nvl(cquannh,0) * 1000 / 4.1816, 0)\n" + 
			" -round_new(nvl(lquannh,0) * 1000 / 4.1816, 0),'KHXM_RZCBY','原煤')*\n" + 
			"         getchaocjz(abs(getchaz(round_new(nvl(cquannh,0) * 1000 / 4.1816, 0)\n" + 
			" -round_new(nvl(lquannh,0) * 1000 / 4.1816, 0),'KHXM_RZCBY','原煤')),round_new(nvl(cquannh,0) * 1000 / 4.1816, 0)\n" + 
			" -round_new(nvl(lquannh,0) * 1000 / 4.1816, 0),'KHXM_RZCBY')\n" + 
			"\n" + 
			"          ,0),3),'2', round_new( nvl(getbiaoz(round_new(nvl(cquannh,0) * 1000 / 4.1816, 0)\n" + 
			" -round_new(nvl(lquannh,0) * 1000 / 4.1816, 0),'KHXM_RZCLJ','原煤')*\n" + 
			"         getchaocjz(abs(getchaz(round_new(nvl(cquannh,0) * 1000 / 4.1816, 0)\n" + 
			" -round_new(nvl(lquannh,0) * 1000 / 4.1816, 0),'KHXM_RZCLJ','原煤')),round_new(nvl(cquannh,0) * 1000 / 4.1816, 0)\n" + 
			" -round_new(nvl(lquannh,0) * 1000 / 4.1816, 0),'KHXM_RZCLJ')\n" + 
			"\n" + 
			"          ,0),3))\n" + 
			" jif"+

			"   from (\n" + 
			"select ruc.id as id ,ruc.dangy dangy, ruc.quannh cquannh,rul.quannh lquannh from\n" + 
			"(\n" + 
			"\n" + 
			"select fdl.id as id,'1' dangy\n" + 
			", decode(sum(ah), 0, 0, round_new(sum(qnet_ar * ah) / sum(ah), 2)) as quannh\n" + 
			"from\n" + 
			"    (select\n" + 
			"    f.diancxxb_id as id, z.qnet_ar from zhilb z,\n" + 
			"    fahb f\n" + 
			"    where to_char(z.huaysj,'yyyymmdd')='"+getNianfValue()+yue+"01'\n" + 
			"\n" + 
			"    and z.id=f.zhilb_id\n" + 
			"\n" + 
			"and f.diancxxb_id in ( select id from diancxxb d where d.fuid= "+getTreeid()+" or d.id="+getTreeid()+")\n" + 
			"    group by f.diancxxb_id,z.qnet_ar\n" + 
			"    ) fdl ,\n" + 
			"  (\n" + 
			"      select\n" + 
			"      f.diancxxb_id as id, f.laimsl as ah from zhilb z,\n" + 
			"      fahb f\n" + 
			"      where to_char(z.huaysj,'yyyymmdd')='"+getNianfValue()+yue+"01'\n" + 
			"\n" + 
			"      and z.id=f.zhilb_id\n" + 
			"\n" + 
			"and f.diancxxb_id in ( select id from diancxxb d where d.fuid= "+getTreeid()+" or d.id="+getTreeid()+")\n" + 
			"      group by f.diancxxb_id,f.laimsl\n" + 
			"    )mh\n" + 
			"\n" + 
			"\n" + 
			"  where fdl.id=mh.id\n" + 
			"  group by fdl.id\n" + 
			"\n" + 
			")ruc ----入厂\n" + 
			",\n" + 
			"( select fdl.id as id,'1' dangy,decode(sum(ah), 0, 0, round_new(sum(qnet_ar * ah) / sum(ah), 2)) as quannh from\n" + 
			"(\n" + 
			"select r.diancxxb_id as id, r.qnet_ar from rulmzlb r\n" + 
			"where\n" + 
			"to_char(r.rulrq,'yyyymmdd')='"+getNianfValue()+yue+"01'\n" + 
			"\n" + 
			"and r.diancxxb_id in ( select id from diancxxb d where d.fuid= "+getTreeid()+" or d.id="+getTreeid()+")\n" + 
			"group by r.diancxxb_id,r.qnet_ar\n" + 
			")fdl,(\n" + 
			"select r.diancxxb_id as id, r.meil as ah from rulmzlb r\n" + 
			"where\n" + 
			"to_char(r.rulrq,'yyyymmdd')='"+getNianfValue()+yue+"01'\n" + 
			"\n" + 
			"and r.diancxxb_id in ( select id from diancxxb d where d.fuid= "+getTreeid()+" or d.id="+getTreeid()+")\n" + 
			"group by r.diancxxb_id,r.meil\n" + 
			")mh\n" + 
			"where fdl.id=mh.id\n" + 
			"group by fdl.id\n" + 
			")rul\n" + 
			"where ruc.id=rul.id\n" + 
			"\n" + 
			"union\n" + 
			"--累计\n" + 
			"\n" + 
			"select ruc.id,ruc.dangy, ruc.quannh cquannh,rul.quannh lquannh from\n" + 
			"(\n" + 
			"\n" + 
			"select fdl.id as id,'2' dangy\n" + 
			", decode(sum(ah), 0, 0, round_new(sum(qnet_ar * ah) / sum(ah), 2)) as quannh\n" + 
			"from\n" + 
			"    (select\n" + 
			"    f.diancxxb_id as id, z.qnet_ar from zhilb z,\n" + 
			"    fahb f\n" + 
			"    where  to_char(z.huaysj,'yyyymmdd')>='"+getNianfValue()+"0101' and to_char(z.huaysj,'yyyymmdd')<='"+getNianfValue()+yue+"01'\n" + 
			"\n" + 
			"    and z.id=f.zhilb_id\n" + 
			"\n" + 
			"and f.diancxxb_id in ( select id from diancxxb d where d.fuid= "+getTreeid()+" or d.id="+getTreeid()+")\n" + 
			"    group by f.diancxxb_id,z.qnet_ar\n" + 
			"    ) fdl ,\n" + 
			"  (\n" + 
			"      select\n" + 
			"      f.diancxxb_id as id, f.laimsl as ah from zhilb z,\n" + 
			"      fahb f\n" + 
			"      where  to_char(z.huaysj,'yyyymmdd')>='"+getNianfValue()+"0101' and to_char(z.huaysj,'yyyymmdd')<='"+getNianfValue()+yue+"01'\n" + 
			"\n" + 
			"      and z.id=f.zhilb_id\n" + 
			"\n" + 
			"and f.diancxxb_id in ( select id from diancxxb d where d.fuid= "+getTreeid()+" or d.id="+getTreeid()+")\n" + 
			"      group by f.diancxxb_id,f.laimsl\n" + 
			"    )mh\n" + 
			"\n" + 
			"\n" + 
			"  where fdl.id=mh.id\n" + 
			"  group by fdl.id\n" + 
			"\n" + 
			")ruc ----入厂\n" + 
			",\n" + 
			"( select fdl.id as id,'2' dangy,decode(sum(ah), 0, 0, round_new(sum(qnet_ar * ah) / sum(ah), 2)) as quannh from\n" + 
			"(\n" + 
			"select r.diancxxb_id as id, r.qnet_ar from rulmzlb r\n" + 
			"where  to_char(r.rulrq,'yyyymmdd')>='"+getNianfValue()+"0101' and\n" + 
			"to_char(r.rulrq,'yyyymmdd')<='"+getNianfValue()+yue+"01'\n" + 
			"\n" + 
			"and r.diancxxb_id in ( select id from diancxxb d where d.fuid= "+getTreeid()+" or d.id="+getTreeid()+")\n" + 
			"group by r.diancxxb_id,r.qnet_ar\n" + 
			")fdl,(\n" + 
			"select r.diancxxb_id as id, r.meil as ah from rulmzlb r\n" + 
			"where  to_char(r.rulrq,'yyyymmdd')>='"+getNianfValue()+"0101' and\n" + 
			"to_char(r.rulrq,'yyyymmdd')<='"+getNianfValue()+yue+"01'\n" + 
			"\n" + 
			"and r.diancxxb_id in ( select id from diancxxb d where d.fuid= "+getTreeid()+" or d.id="+getTreeid()+")\n" + 
			"group by r.diancxxb_id,r.meil\n" + 
			")mh\n" + 
			"where fdl.id=mh.id\n" + 
			"group by fdl.id\n" + 
			")rul\n" + 
			"where ruc.id=rul.id\n" + 
			") zong,diancxxb d\n" + 
			"where zong.id=d.id\n" + 
			"order by zong.id,dangy\n" + 
			"--where ruc.id=d.id\n" + 
			"\n" + 
			"\n" + 
			"\n" + 
			"\n" + 
			"\n" + 
			")";



		ArrHeader=new String[2][12];
		ArrHeader[0]=new String[]{"单位","当月或累计","入厂热值","入厂热值","入炉热值","入炉热值","热值差","热值差","考核基准","差值","扣分标准<br>(分/列)","计分"};
	    ArrHeader[1]=new String[] {"单位","当月或累计","Mj/kg","Kcal/Kg","Mj/kg","Kcal/Kg","Mj/kg","Kcal/Kg","考核基准","差值","扣分标准<br>(分/列)","计分"};
		ArrWidth =new int[] {110,60,60,60,60,60,60,60,60,60,60,60};//60,60,60
		ResultSet rs = cn.getResultSet(ss);
//		System.out.println(ss);
		// 数据
		rt.setBody(new Table(rs,2, 0, 3));
		
		rt.setTitle(intyear+"年"+intMonth+"月"+titlename, ArrWidth);
		rt.setDefaultTitle(1, 2, "填报单位:"+((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 1, "填报日期:"+intyear+"年"+intMonth+"月", Table.ALIGN_CENTER);
		rt.body.setWidth(ArrWidth);
	
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		for(int i=1;i<13;i++){
			
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		rt.body.ShowZero = false;
//		rt.body.merge(3, 1, 3, 2);
//		rt.body.setCells(3, 1, 3, 2, Table.PER_ALIGN, Table.ALIGN_CENTER);
		//页脚 
		
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1,2,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		 rt.setDefautlFooter(5,1,"审核:",Table.ALIGN_LEFT);
		 rt.setDefautlFooter(8,1,"制表:",Table.ALIGN_RIGHT);
		
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	

//	电厂名称
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
			_DiancmcValue=(IDropDownBean)getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		
		String sql="";
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);
		

	}
	
	
//	矿别名称
	public boolean _meikdqmcchange = false;
	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if(_MeikdqmcValue==null){
			_MeikdqmcValue=(IDropDownBean)getIMeikdqmcModels().getOption(0);
		}
		return _MeikdqmcValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikdqmcValue != null) {
			id = _MeikdqmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikdqmcchange = true;
			} else {
				_meikdqmcchange = false;
			}
		}
		_MeikdqmcValue = Value;
	}

	private IPropertySelectionModel _IMeikdqmcModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		try{
		
		String sql="";
		sql = "select id,mingc from gongysb order by mingc";
		_IMeikdqmcModel = new IDropDownModel(sql);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IMeikdqmcModel;
	}
	

//	年份
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
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}
	public boolean nianfchanged = false;
	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2000; i <= DateUtil.getYear(new Date())+1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
	
	/**
	 * 月份
	 */
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
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}
	public boolean yuefchanged = false;
	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			yuefchanged = true;
		}
		_YuefValue = Value;
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

	/*private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}*/
	
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
	 //	页面判定方法
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
//	 分公司下拉框
	private boolean _fengschange = false;

	public IDropDownBean getFengsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getFengsModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setFengsValue(IDropDownBean Value) {
		if (getFengsValue().getId() != Value.getId()) {
			_fengschange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public IPropertySelectionModel getFengsModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getFengsModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setDiancxxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getFengsModels() {
		String sql;
		sql = "select id ,mingc from diancxxb where jib=2 order by id";
		setDiancxxModel(new IDropDownModel(sql,"中国大唐集团"));
	}
	
	
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
//	得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}
	//条件工具
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
//		tb1.addText(new ToolbarText("统计口径:"));
//		ComboBox cb = new ComboBox();
//		cb.setTransform("BaoblxDropDown");
//		cb.setWidth(120);
//		cb.setListeners("select:function(){document.Form0.submit();}");
//		tb1.addField(cb);
//		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
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
	//电厂树
	private String treeid;
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
//	 报表类型
	public boolean _Baoblxchange = false;

	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if (_BaoblxValue == null) {
			_BaoblxValue = (IDropDownBean) getIBaoblxModels().getOption(0);
		}
		return _BaoblxValue;
	}

	public void setBaoblxValue(IDropDownBean Value) {
		long id = -2;
		if (_BaoblxValue != null) {
			id = _BaoblxValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange = true;
			} else {
				_Baoblxchange = false;
			}
		}
		_BaoblxValue = Value;
	}

	private IPropertySelectionModel _IBaoblxModel;

	public void setIBaoblxModel(IPropertySelectionModel value) {
		_IBaoblxModel = value;
	}

	public IPropertySelectionModel getIBaoblxModel() {
		if (_IBaoblxModel == null) {
			getIBaoblxModels();
		}
		return _IBaoblxModel;
	}

	public IPropertySelectionModel getIBaoblxModels() {
		JDBCcon con = new JDBCcon();
		try {
			List fahdwList = new ArrayList();
			fahdwList.add(new IDropDownBean(0, "分厂"));
			fahdwList.add(new IDropDownBean(1, "分矿"));
			fahdwList.add(new IDropDownBean(2, "分厂分矿"));
			fahdwList.add(new IDropDownBean(3, "分矿分厂"));
			_IBaoblxModel = new IDropDownModel(fahdwList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IBaoblxModel;
	}

	
}