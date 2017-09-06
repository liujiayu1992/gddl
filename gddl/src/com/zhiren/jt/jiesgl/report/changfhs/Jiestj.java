package com.zhiren.jt.jiesgl.report.changfhs;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.text.DecimalFormat;
//import java.util.Calendar;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import org.apache.tapestry.contrib.palette.SortMode;

public class Jiestj extends BasePage {
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
	public boolean isEditable() {
		return ((Visit) getPage().getVisit()).getboolean4();
	}

	public void setEditable(boolean editable) {
		((Visit) getPage().getVisit()).setboolean4(editable);
	}
	
	public boolean isEditable2() {
		return ((Visit) getPage().getVisit()).getboolean5();
	}

	public void setEditable2(boolean editable) {
		((Visit) getPage().getVisit()).setboolean5(editable);
	}
	
	//格式化
	public String format(double dblValue,String strFormat){
		 DecimalFormat df = new DecimalFormat(strFormat);
		return df.format(dblValue);
		 
	}
//****************判断页面是否是第一次调用**************//
	public int getZhuangt() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	public void setZhuangt(int _value) {
		((Visit) getPage().getVisit()).setInt1(_value);
	}
	
	public String getJiesdbh() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setJiesdbh(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	
	public String getJieslx() {
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setJieslx(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}
	
//*****************************************条件设置开始******************************************//	
//****************设置日期*******************//
	//开始日期
	private Date _BeginriqValue =getMonthFirstday(new Date());
	private boolean beginRiqChange = false;
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue=getMonthFirstday(new Date());
		}
//		beginRiqChange = true;
		return _BeginriqValue;
	}
	
	public void setBeginriqDate(Date _value) {
		if (FormatDate(_BeginriqValue).equals(FormatDate(_value))) {
		} else {
			_BeginriqValue = _value;
			beginRiqChange = true;
		}
	}
	//结束日期
	private Date _EndriqValue =new Date();
	private boolean endRiqChange = false;
	public Date getEndriqDate() {
		if (_EndriqValue==null){
			_EndriqValue=new Date();
		}
		return _EndriqValue;
	}
	
	public void setEndriqDate(Date _value) {
		if (FormatDate(_EndriqValue).equals(FormatDate(_value))) {
		} else {
			_EndriqValue = _value;
			endRiqChange = true;
//			getIFahdwModels();
		}
	}
	
	//公司名称
	public IDropDownBean getGongsmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean2()==null){
			((Visit)getPage().getVisit()).setDropDownBean2((IDropDownBean)getIGongsmcModels().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean2();
	}

	public void setGongsmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean2() != null) {
			
			((Visit)getPage().getVisit()).setDropDownBean2(Value);
			getIDiancmcModels();
		}
	}

	public void setIGongsmcModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getIGongsmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel2() == null) {
			getIGongsmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getIGongsmcModels() {
		
		String sql="";
		sql = "select id,mingc from diancxxb where jib=2 order by mingc";
		((Visit)getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"全部"));
		return ((Visit)getPage().getVisit()).getProSelectionModel2();
	}
	
	
	//电厂名称
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean3()==null){
			((Visit)getPage().getVisit()).setDropDownBean3((IDropDownBean)getIDiancmcModels().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean3();
	}

	public void setDiancmcValue(IDropDownBean value) {

		if(((Visit)getPage().getVisit()).getDropDownBean3()!=value){
			
			((Visit)getPage().getVisit()).setboolean2(true);
			((Visit)getPage().getVisit()).setDropDownBean3(value);
		}
	}

	public void setIDiancmcModel(IPropertySelectionModel value) {

		((Visit)getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel3() == null) {
			
			getIDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel3();
	}

	public IPropertySelectionModel getIDiancmcModels() {
		
		String sql="";
		
		if(((Visit)getPage().getVisit()).getRenyjb()==2){
//			分公司
			sql = "select d.id,d.mingc from diancxxb d where fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" order by d.mingc";
		}else{
//			集团
			if(getGongsmcValue().getId()>-1){
				
				sql = "select d.id,d.mingc from diancxxb d where fuid="+getGongsmcValue().getId()+" order by d.mingc";
			}else{
				
				sql = "select d.id,d.mingc from diancxxb d where jib=3 order by d.mingc";
			}
		}
		
		((Visit)getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql,"全部"));
		return ((Visit)getPage().getVisit()).getProSelectionModel3();
	}
	
	
//******************发货单位设置*****************//
	public IDropDownBean getGongysValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean4()==null){
			((Visit)getPage().getVisit()).setDropDownBean4((IDropDownBean)getIGongysModels().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean4();
	}

	public void setGongysValue(IDropDownBean value) {
		
		if(((Visit)getPage().getVisit()).getDropDownBean4()==value){
			
			((Visit)getPage().getVisit()).setboolean2(true);
			((Visit)getPage().getVisit()).setDropDownBean4(value);
		}
	}

	public void setIGongysModel(IPropertySelectionModel value) {

		((Visit)getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getIGongysModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel4() == null) {
			getIGongysModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel4();
	}

	public IPropertySelectionModel getIGongysModels() {
		
		String sql="";
		if(((Visit)getPage().getVisit()).getRenyjb()==3){
//			电厂
			sql = "select distinct gongysb_id,gongysmc from diancjsmkb,gongysb where diancjsmkb.gongysb_id=gongysb.id and fuid<>0 and diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" order by gongysmc";
		}else if(((Visit)getPage().getVisit()).getRenyjb()==2){
//			公司
			sql = "select distinct gongysb_id,gongysmc from diancjsmkb,gongysb where diancjsmkb.gongysb_id=gongysb.id and fuid<>0 and diancxxb_id in (select id from diancxxb where fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+") order by gongysmc";
		}else if(((Visit)getPage().getVisit()).getRenyjb()==1){
//			集团
			sql = "select distinct gongysb_id,gongysmc from diancjsmkb,gongysb where diancjsmkb.gongysb_id=gongysb.id and fuid<>0 order by gongysmc";
		}
		((Visit)getPage().getVisit()).setProSelectionModel4( new IDropDownModel(sql,"全部"));
		return ((Visit)getPage().getVisit()).getProSelectionModel4();
	}
	
//	煤矿地区
	public IDropDownBean getMeikdqValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getIMeikdqModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setMeikdqValue(IDropDownBean value) {
		
		if(((Visit)getPage().getVisit()).getDropDownBean1()==value){
			
			((Visit)getPage().getVisit()).setboolean2(true);
		}
		((Visit)getPage().getVisit()).setDropDownBean1(value);
	}

	public void setIMeikdqModel(IPropertySelectionModel value) {

		((Visit)getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getIMeikdqModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getIMeikdqModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getIMeikdqModels() {
		
		String sql="";
		if(((Visit)getPage().getVisit()).getRenyjb()==3){
//			电厂
			sql = "select distinct gongysb_id,gongysmc from diancjsmkb,gongysb where diancjsmkb.gongysb_id=gongysb.id and fuid=0 and diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" order by gongysmc";
		}else if(((Visit)getPage().getVisit()).getRenyjb()==2){
//			公司
			sql = "select distinct gongysb_id,gongysmc from diancjsmkb,gongysb where diancjsmkb.gongysb_id=gongysb.id and fuid=0 and diancxxb_id in (select id from diancxxb where fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+") order by gongysmc";
		}else if(((Visit)getPage().getVisit()).getRenyjb()==1){
//			集团
			sql = "select distinct gongysb_id,gongysmc from diancjsmkb,gongysb where diancjsmkb.gongysb_id=gongysb.id and fuid=0 order by gongysmc";
		}
		((Visit)getPage().getVisit()).setProSelectionModel1( new IDropDownModel(sql,"全部"));
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
//	煤矿地区_end

//	厂方入帐状态
	public boolean _ruzChange = false;
	private IDropDownBean _RuzValue;

	public IDropDownBean getRuzValue() {
		if(_RuzValue==null){
			_RuzValue=(IDropDownBean)getIRuzModels().getOption(0);
		}
		return _RuzValue;
	}

	public void setRuzValue(IDropDownBean Value) {
		long id = -2;
		if (_RuzValue != null) {
			id = _RuzValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_ruzChange = true;
			} else {
				_ruzChange = false;
			}
		}
		_RuzValue = Value;
	}

	private IPropertySelectionModel _IRuzModel;

	public void setIRuzModel(IPropertySelectionModel value) {
		_IRuzModel = value;
	}

	public IPropertySelectionModel getIRuzModel() {
		if (_IRuzModel == null) {
			getIRuzModels();
		}
		return _IRuzModel;
	}

	public IPropertySelectionModel getIRuzModels() {
		
		List ruzlist = new ArrayList();
		ruzlist.add(new IDropDownBean(-1,"请选择"));
		ruzlist.add(new IDropDownBean(0,"未审核"));
		ruzlist.add(new IDropDownBean(1,"未出发票"));
		ruzlist.add(new IDropDownBean(2,"已出发票(待入帐)"));
		ruzlist.add(new IDropDownBean(3,"未入账"));
		ruzlist.add(new IDropDownBean(4,"已入账"));
		
		_IRuzModel = new IDropDownModel(ruzlist);
		return _IRuzModel;
	}
	
//	矿方入帐状态
	public boolean _KfruzChange = false;
	private IDropDownBean _KfruzValue;

	public IDropDownBean getKfruzValue() {
		if(_KfruzValue==null){
			_KfruzValue=(IDropDownBean)getIKfruzModels().getOption(0);
		}
		return _KfruzValue;
	}

	public void setKfruzValue(IDropDownBean Value) {
		long id = -2;
		if (_KfruzValue != null) {
			id = _KfruzValue.getId();
		}
		if (Value!= null) {
			if (Value.getId()!= id) {
				_KfruzChange = true;
			} else {
				_KfruzChange = false;
			}
		}
		_KfruzValue = Value;
	}

	private IPropertySelectionModel _IKfruzModel;

	public void setIKfruzModel(IPropertySelectionModel value) {
		_IKfruzModel = value;
	}

	public IPropertySelectionModel getIKfruzModel() {
		if (_IKfruzModel == null) {
			getIKfruzModels();
		}
		return _IKfruzModel;
	}

	public IPropertySelectionModel getIKfruzModels() {
		
		List ruzlist = new ArrayList();
		ruzlist.add(new IDropDownBean(-1,"请选择"));
		ruzlist.add(new IDropDownBean(1,"未出发票"));
		ruzlist.add(new IDropDownBean(2,"已出发票(待入帐)"));
		ruzlist.add(new IDropDownBean(3,"未入账"));
		ruzlist.add(new IDropDownBean(4,"已入账"));
		
		_IKfruzModel = new IDropDownModel(ruzlist);
		return _IKfruzModel;
	}
	
//	统计方法
	public boolean _tongjChange = false;
	private IDropDownBean _TongjValue;

	public IDropDownBean getTongjValue() {
		if(_TongjValue==null){
			_TongjValue=(IDropDownBean)getITongjModels().getOption(0);
		}
		return _TongjValue;
	}

	public void setTongjValue(IDropDownBean Value) {
		long id = -2;
		if (_TongjValue != null) {
			id = _TongjValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_tongjChange = true;
			} else {
				_tongjChange = false;
			}
		}
		_TongjValue = Value;
	}

	private IPropertySelectionModel _ITongjModel;

	public void setITongjModel(IPropertySelectionModel value) {
		_ITongjModel = value;
	}

	public IPropertySelectionModel getITongjModel() {
		if (_ITongjModel == null) {
			getITongjModels();
		}
		return _ITongjModel;
	}

	public IPropertySelectionModel getITongjModels() {
		
		List tongjlist = new ArrayList();
		tongjlist.add(new IDropDownBean(1,"按电厂"));
		tongjlist.add(new IDropDownBean(2,"按煤矿"));
		
		_ITongjModel = new IDropDownModel(tongjlist);
		return _ITongjModel;
	}
	
//*****************************************条件设置结束******************************************//		
	
//***************设置消息框******************//
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
//******************按钮设置****************//
	private boolean _QuedChick = false;

	public void QuedButton(IRequestCycle cycle) {
		_QuedChick = true;
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {

		if (_QuedChick) {
			chaxunzt1 = 1;// 查询状态
			zhuangt=2;
			Refurbish();
			_QuedChick = false;
		}
		
		if(_RefurbishChick){
			chaxunzt1 = 1;// 查询状态
			zhuangt=2;
			Refurbish();
			_RefurbishChick = false;
		}
	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		 getSelectData();
	}
	
//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);//MissSession
			return;
		} else {
			visit.setboolean1(false);//MissSession
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setInt1(2);//是第一次显示
			visit.setString1("");//结算编号
			visit.setString2("");//结算类型
			visit.setboolean4(false);//公司显示
			visit.setboolean5(false);//电厂显示
			zhuangt=visit.getInt1();
			chaxunzt1 = 2;
//*************条件设置开始***************//
			_BeginriqValue=getMonthFirstday(new Date());
			_EndriqValue=new Date();
//			公司
			if(visit.isJTUser()){
				
				visit.setboolean4(true);//公司显示
				setGongsmcValue(null);//drop2
				setIGongsmcModel(null);
				getIGongsmcModels();
			}
//			电厂
			if(visit.isGSUser()||visit.isJTUser()){
				
				visit.setboolean5(true);//电厂显示
				setDiancmcValue(null);//drop3
				setIDiancmcModel(null);
				getIDiancmcModels();
			}
			//煤款地区
			setMeikdqValue(null);//drop1
			setIMeikdqModel(null);
			getIMeikdqModels();
			//供应商
			setGongysValue(null);//drop4
			setIGongysModel(null);
			getIGongysModels();
			
//			入帐状态
			setRuzValue(null);
			getIRuzModels();
			getITongjModels();
		}
		if(zhuangt==1){//不要动
			visit.setInt1(1);
		}
		if(zhuangt==2){//不要动
			visit.setInt1(2);
		}
		if(beginRiqChange ){
			beginRiqChange = false;
			
			setGongysValue(null);
			setIGongysModel(null);
		}
		if(endRiqChange ){
			endRiqChange = false;
			
			setGongysValue(null);
			setIGongysModel(null);
		}
		zhuangt=1;
 //*************条件设置结束***************//
		
	}

//*****************************报表数据设置*****************************//
	private int chaxunzt1 = 0;// 查询状态
	private int zhuangt =1;
	
	public String getWhere() {//查询条件
//		Visit visit = (Visit) getPage().getVisit();
		String chaxunsql = "";
		
		//编号条件
		String Fahdw="";
		if(getGongsmcValue()!=null && getGongsmcValue().getId()!=-1){
			//'Fahdw=" where Fahdw='"+getFahdwValue().getValue()+"'";
			Fahdw=getGongsmcValue().getValue();
		}else{
			Fahdw="";
		}
		StringBuffer r_sql = new StringBuffer();
		r_sql.append(Fahdw);
		chaxunsql = r_sql.toString();
		return chaxunsql;
	}
	
	private String nvlStr(String strValue){
		if (strValue==null) {
			return "";
		}else if(strValue.equals("null")){
			return "";
		}
		
		return strValue;
	}
	
	public String getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		if (chaxunzt1 == 1) {// 查询状态
			chaxunzt1 = 2;
			return "";
		} else if (chaxunzt1 == 2) {
			if(getWhere()==""){
				return "";
			}else{
				visit.setInt1(2);//是第一次显示
				chaxunzt1=0;
				zhuangt=1;
				JDBCcon cn = new JDBCcon();
				String strDate =  FormatDate(_BeginriqValue)+" 至 "+FormatDate(_EndriqValue);
			  try{
				  StringBuffer wsql = new StringBuffer();
				  String ArrHeader[][]=new String[3][32];
				  int whith1=0,whith3=0;
				  String beginriq="";
				  String endriq="";
				  String gongsid="";
				  String diancid="";
				  String ruzzt="";
				  String kfruzzt="";
				  String fahdw="";
				  String meikdqbm="";
				  String tongjff="";
				  String tongjzd="";
				  String dianctjtab="";
				  String dianctjtj="";
				  beginriq=" and mk.jiesrq>="+OraDate(_BeginriqValue);//开始日期
				  endriq=" and mk.jiesrq<="+OraDate(_EndriqValue);//结束日期
				  wsql.append(beginriq);
				  wsql.append(endriq);
				  if(getGongsmcValue()!=null && getGongsmcValue().getId()!=-1){//公司名称
					  gongsid=" and mk.gongsxxb_id="+getGongsmcValue().getId();
					  wsql.append(gongsid);
			   	  }
				  if(getDiancmcValue()!=null && getDiancmcValue().getId()!=-1){//电厂名称
					  diancid=" and mk.diancxxb_id="+getDiancmcValue().getId();
					  wsql.append(diancid);
				  }
//				  if(getRuzValue()!=null && getRuzValue().getId()!=-1){//厂方入帐状态
//					  if(getRuzValue().getId()==0){
//						  ruzzt=" and mk.shenhjb=0 and mk.zhuangt=1 ";
//					  }else if(getRuzValue().getId()==1){
//						  ruzzt=" and mk.shenhjb>=1 and mk.shenhjb<=6 and mk.zhuangt=1 and mk.fapbh is null ";
//					  }else if(getRuzValue().getId()==2){
//						  ruzzt=" and mk.shenhjb>=1 and mk.shenhjb<6 and mk.zhuangt=1 and mk.fapbh is not null ";
//					  }else if(getRuzValue().getId()==3){
//						  ruzzt=" and mk.shenhjb>=1 and mk.shenhjb<6 and mk.zhuangt=1 ";
////						  ruzzt=" and mk.shenhjb>=1 and mk.shenhjb<6 and mk.zhuangt=1 and mk.fapbh is not null and mk.ruzrq is null ";
//					  }else if(getRuzValue().getId()==4){
//						  ruzzt=" and mk.shenhjb>=6 and mk.zhuangt=1 and mk.ruzrq is not null ";
//					  }
//					  wsql.append(ruzzt);
//				  }
//				  if(getKfruzValue()!=null && getKfruzValue().getId()!=-1){//矿方入帐状态
//					  if(getKfruzValue().getId()==1){
//						  kfruzzt=" and kmk.shenhjb>=1 and kmk.shenhjb<=6 and kmk.zhuangt=1 and kmk.fapbh is null";
//					  }else if(getKfruzValue().getId()==2){
//						  kfruzzt=" and kmk.shenhjb>=1 and kmk.shenhjb<6 and kmk.zhuangt=1 and kmk.fapbh is not null ";
//					  }else if(getKfruzValue().getId()==3){
//						  kfruzzt=" and kmk.shenhjb>=1 and kmk.shenhjb<6 and kmk.zhuangt=1 ";
//					  }else if(getKfruzValue().getId()==4){
//						  kfruzzt=" and kmk.shenhjb>=6 and kmk.zhuangt=1 and kmk.ruzrq is not null ";
//					  }
//					  wsql.append(kfruzzt);
//				  }
				  if(getGongsmcValue()!=null && getGongsmcValue().getId()!=-1){//公司名称
					  fahdw=" and dq.mingc='"+getGongsmcValue().getValue()+"' ";
					  wsql.append(fahdw);
				  }
//					统计方法				  
				  if(getTongjValue()!=null){//统计方法
					  if(getTongjValue().getId()==1){
						  tongjzd=" select decode(grouping(dc.mingc)+grouping(lx.mingc),2,'总计',1,lx.mingc||'合计',dc.mingc) as diancmc,decode(grouping(dc.mingc)+grouping(dq.quanc),1,'电厂小计',2,'',dq.quanc) as meikdqmc,decode(grouping(dq.quanc)+grouping(mk.gongysmc),1,'地区小计',mk.gongysmc) as fahdw,decode(grouping(a.lb),0,a.lb) as kouj,";//decode(grouping(mk.gongysmc)+grouping(a.lb),0,a.lb) as kouj,";
						  tongjff=" group by rollup(a.lx,a.lb,lx.mingc,dc.mingc,dq.quanc,(mk.gongysmc,mk.bianm,kmk.bianm,mk.ruzrq,kmk.ruzrq,mk.daibch,kmk.daibch))" +
						  		" having not grouping(lb)=1 \n "
							  + " order by grouping(lx.mingc) desc,max(lx.xuh),grouping(dc.mingc) desc,max(px.xuh),grouping(dq.quanc) desc,dq.quanc, grouping(mk.gongysmc) desc,mk.gongysmc,a.lx ";
						  dianctjtab=",dianclbb lx,dianckjpxb px";
						  dianctjtj=" and dc.dianclbb_id=lx.id and dc.id=px.diancxxb_id and px.kouj='月报' ";
						  
						  ArrHeader[0]=new String[] {"电厂名称","煤矿地区","发货单位","类型","结算单编号<br>(代表车号)","入账日期","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","国铁运杂费","国铁运杂费","国铁运杂费","国铁运杂费","国铁运杂费","结算金额","结算金额","结算金额","结算金额","结算金额"};
						  ArrHeader[1]=new String[] {"电厂名称","煤矿地区","发货单位","类型","结算单编号<br>(代表车号)","入账日期","车数","供方数量","验收数量","过衡量","运损","盈亏数量","盈亏金额","热量","亏卡金额","硫","硫折金额","结算数量"," 单价","价款合计","价款税款","价税合计","运费","计税扣除","不含税运费","税款","运杂费合计","亏吨拒费","结算总金额","综合价","标煤单价","标煤单价不含税"};
						  ArrHeader[2]=new String[] {"电厂名称","煤矿地区","发货单位","类型","结算单编号<br>(代表车号)","入账日期","(车)",    " (吨)",   "(吨) ",  " (吨)","(吨) "," (吨)",    "(元)","(Kcal/kg)","(元)","(%)", "(元)",   "(吨)", " (元)",    "(元) ",   " (元)",    "(元) "," (元)","(元) ",			" (元)","(元) ",	   " (元)","(元) "," (元)","(元) "," (元)",         "(元) "};
						  whith1=80;
						  whith3=150;
						  
					  }else if(getTongjValue().getId()==2){
						  dianctjtab="";
						  dianctjtj="";
						  tongjzd=" select decode(grouping(dq.quanc),1,'总计',dq.quanc) as meikdqqc,decode(grouping(mk.gongysmc)+grouping(dq.quanc),1,'小计',2,'',mk.gongysmc) as fahdw,dc.mingc as diancmc,decode(grouping(a.lb),0,a.lb) as kouj, ";
					   // tongjff=" group by rollup(dq.quanc,(mk.gongysmc,dc.mingc,mk.bianm,kmk.bianm,mk.ruzrq,kmk.ruzrq,mk.daibch)) \n order by grouping(dq.quanc) desc,dq.quanc, grouping(mk.gongysmc) desc,mk.gongysmc,grouping(dc.mingc) desc,dc.mingc,a.lb as kouj,";
						  tongjff=" group by rollup(lx,lb,dq.quanc,(mk.gongysmc,dc.mingc,mk.bianm,kmk.bianm,mk.ruzrq,kmk.ruzrq,mk.daibch,kmk.daibch)) " +
						  		"  having not grouping(lb)=1 \n" +
						  		" order by grouping(dq.quanc) desc,dq.quanc, grouping(mk.gongysmc) desc,mk.gongysmc,grouping(dc.mingc) desc,dc.mingc,a.lx";
						  ArrHeader[0]=new String[] {"煤矿地区","发货单位","电厂名称","类型","结算单编号<br>(代表车号)","入账日期","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","燃煤费用","国铁运杂费","国铁运杂费","国铁运杂费","国铁运杂费","国铁运杂费","结算金额","结算金额","结算金额","结算金额","结算金额"};
						  ArrHeader[1]=new String[] {"煤矿地区","发货单位","电厂名称","类型","结算单编号<br>(代表车号)","入账日期","车数","供方数量","验收数量","过衡量","运损","盈亏数量","盈亏金额","热量","亏卡金额","硫","硫折金额","结算数量"," 单价","价款合计","价款税款","价税合计","运费","计税扣除","不含税运费","税款","运杂费合计","亏吨拒费","结算总金额","综合价","标煤单价","标煤单价不含税"};
						  ArrHeader[2]=new String[] {"煤矿地区","发货单位","电厂名称","类型","结算单编号<br>(代表车号)","入账日期","(车)",    " (吨)",   "(吨) ",  " (吨)","(吨) "," (吨)",    "(元)","(Kcal/kg)","(元)","(%)", "(元)",   "(吨)", " (元)",    "(元) ",   " (元)",    "(元) "," (元)","(元) ",			" (元)","(元) ",	   " (元)","(元) "," (元)","(元) "," (元)",         "(元) "};
						  
						 whith1=150;
						 whith3=80;
					  }
				  }
				  
				 StringBuffer sbsql = new StringBuffer();
				 sbsql.append("select * from ( "+tongjzd+" \n");
				 sbsql.append("         decode(lx,1,decode(grouping(mk.bianm),1,'',getHtmlAlert('"+MainGlobal.getHomeContext(this)+"','Jiesdcz','changf_bianm',mk.bianm,mk.bianm||'('||mk.daibch||')')),2,decode(grouping(kmk.bianm),1,'',getHtmlAlert('"+MainGlobal.getHomeContext(this)+"','Jiesdcz','kuangf_bianm',kmk.bianm,kmk.bianm||'('||kmk.daibch||')')),3,'') as bianh,decode(lx,1,to_char(mk.ruzrq,'yyyy-mm-dd'),2,to_char(mk.ruzrq,'yyyy-mm-dd'),3,'') as ruzrq,\n");
//				 sbsql.append("         decode(lx,1,sum(nvl(mk.ches,0)),2,sum(nvl(kmk.ches,0)),3,sum(mk.ches-kmk.ches)) as ches,sum(decode(lx,1,getjiesdzb('diancjsmkb',mk.id,'数量','gongf'),2,getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf'),3,getjiesdzb('diancjsmkb',mk.id,'数量','gongf')-getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf'))) as gongfsl, \n");
//				 sbsql.append("			sum(decode(lx,1,getjiesdzb('diancjsmkb',mk.id,'数量','changf'),2,getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','changf'),3,getjiesdzb('diancjsmkb',mk.id,'数量','changf')-getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','changf'))) as yanssl,sum(decode(lx,1,nvl(mk.guohl,0),2,nvl(kmk.guohl,0),3,mk.guohl-kmk.guohl)) as guohl, sum(0) as yuns, \n");
////	燃料费用
//				 sbsql.append("         sum(decode(lx,1,getjiesdzb('diancjsmkb',mk.id,'数量','yingk'),2,getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','yingk'),3,getjiesdzb('diancjsmkb',mk.id,'数量','yingk')-getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','yingk'))) as yingk,sum(decode(lx,1,getjiesdzb('diancjsmkb',mk.id,'数量','zhejje'),2,getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','zhejje'),3,getjiesdzb('diancjsmkb',mk.id,'数量','zhejje')-getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','zhejje'))) as shulzjje, \n");//加入结算指标表
//				 sbsql.append("         decode(lx,1,decode(sum(mk.jiessl),0,0,round(sum(mk.jiessl*getjiesdzb('diancjsmkb',mk.id,'Qnetar','changf'))/sum(mk.jiessl))),2,decode(sum(kmk.jiessl),0,0,round(sum(kmk.jiessl*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Qnetar','changf'))/sum(kmk.jiessl))),3,decode(sum(mk.jiessl),0,0,round(sum(mk.jiessl*getjiesdzb('diancjsmkb',mk.id,'Qnetar','changf'))/sum(mk.jiessl)))-decode(sum(kmk.jiessl),0,0,round(sum(kmk.jiessl*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Qnetar','changf'))/sum(kmk.jiessl)))) as yansrl,  \n");
//				 sbsql.append("         sum(decode(lx,1,getjiesdzb('diancjsmkb',mk.id,'Qnetar','zhejje'),2,getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Qnetar','zhejje'),3,getjiesdzb('diancjsmkb',mk.id,'Qnetar','zhejje')-getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Qnetar','zhejje'))) as relzjje,  \n");
//				 
//				 sbsql.append("         decode(lx,1,decode(sum(mk.jiessl),0,0,round(sum(mk.jiessl*getjiesdzb('diancjsmkb',mk.id,'Std','changf'))/sum(mk.jiessl),2)),2,decode(sum(kmk.jiessl),0,0,round(sum(kmk.jiessl*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Std','changf'))/sum(kmk.jiessl),2)),3,decode(sum(mk.jiessl),0,0,round(sum(mk.jiessl*getjiesdzb('diancjsmkb',mk.id,'Std','changf'))/sum(mk.jiessl),2))-decode(sum(kmk.jiessl),0,0,round(sum(kmk.jiessl*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Std','changf'))/sum(kmk.jiessl),2))) as liu,  \n");
//				 
//				 sbsql.append("         sum(decode(lx,1,getjiesdzb('diancjsmkb',mk.id,'Std','zhejje'),2,getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Std','zhejje'),3,getjiesdzb('diancjsmkb',mk.id,'Std','zhejje')-getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Std','zhejje'))) as liuyxje,sum(decode(lx,1,nvl(mk.jiessl,0),2,nvl(kmk.jiessl,0),3,mk.jiessl-kmk.jiessl)) as jiessl,\n");
//				 sbsql.append("         decode(lx,1,decode(sum(getjiesdzb('diancjsmkb',mk.id,'数量','jies')),0,0,round(sum(getjiesdzb('diancjsmkb',mk.id,'数量','jies')*getjiesdzb('diancjsmkb',mk.id,'数量','zhejbz'))/sum(getjiesdzb('diancjsmkb',mk.id,'数量','jies')),2)),2,decode(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','jies')),0,0,round(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','jies')*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','zhejbz'))/sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','jies')),2)),"); 
//				 sbsql.append("			3,decode(sum(getjiesdzb('diancjsmkb',mk.id,'数量','jies')),0,0,round(sum(getjiesdzb('diancjsmkb',mk.id,'数量','jies')*getjiesdzb('diancjsmkb',mk.id,'数量','zhejbz'))/sum(getjiesdzb('diancjsmkb',mk.id,'数量','jies')),2))-decode(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','jies')),0,0,round(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','jies')*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','zhejbz'))/sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','jies')),2))) as danj,  \n");
//				 sbsql.append("         sum(decode(lx,1,nvl(mk.buhsmk,0),2,nvl(kmk.buhsmk,0),3,mk.buhsmk-kmk.buhsmk)) as jiakhj,sum(decode(lx,1,nvl(mk.shuik,0),2,nvl(kmk.shuik,0),3,mk.shuik-kmk.shuik)) as jiaksk,sum(decode(lx,1,nvl(mk.hansmk,0),2,nvl(kmk.hansmk,0),3,mk.hansmk-kmk.hansmk)) as jiashj, \n");
////	国铁运杂费
//				 sbsql.append("         sum(decode(lx,1,nvl(yf.guotyf,0),2,nvl(kyf.guotyf,0),3,yf.guotyf-kyf.guotyf)) as yunf,sum(decode(lx,1,nvl(yf.jiskc,0),2,nvl(kyf.jiskc,0),3,yf.jiskc-kyf.jiskc)) as jiskc,sum(decode(lx,1,nvl(yf.buhsyf,0),2,nvl(kyf.buhsyf,0),3,yf.buhsyf-kyf.buhsyf)) as buhsyf,sum(decode(lx,1,nvl(yf.shuik,0),2,nvl(kyf.shuik,0),3,yf.shuik-kyf.shuik)) as shuik, \n");
//				 sbsql.append("         sum(decode(lx,1,nvl(yf.hansyf,0),2,nvl(kyf.hansyf,0),3,yf.hansyf-kyf.hansyf)) as yunzfhj,0 as kundjf, \n");
//				 
//				sbsql.append("         sum(decode(lx,1,nvl(mk.hansmk,0)+nvl(yf.hansyf,0),2,nvl(kmk.hansmk,0)+nvl(kyf.hansyf,0),3,mk.hansmk+nvl(yf.hansyf,0)-kmk.hansmk+nvl(kyf.hansyf,0))) as cfjieszje, \n");
//				
//				sbsql.append("         decode(lx,1,decode(sum(mk.jiessl),0,0,round(sum(mk.hansmk)/sum(mk.jiessl),2))+nvl(decode(sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),0,0,round(sum(nvl(getjiesdzb('diancjsmkb',mk.id,'数量','gongf'),0))/sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),2)),0)  \n" );
//				sbsql.append("			,2,decode(sum(kmk.jiessl),0,0,round(sum(kmk.hansmk)/sum(kmk.jiessl),2))+nvl(decode(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),0,0,round(sum(nvl(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf'),0))/sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),2)),0) \n" );
//				sbsql.append("			,3,(decode(sum(mk.jiessl),0,0,round(sum(mk.hansmk)/sum(mk.jiessl),2))+nvl(decode(sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),0,0,round(sum(nvl(getjiesdzb('diancjsmkb',mk.id,'数量','gongf'),0))/sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),2)),0))\n");
//				sbsql.append("		   -(decode(sum(kmk.jiessl),0,0,round(sum(kmk.hansmk)/sum(kmk.jiessl),2))+nvl(decode(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),0,0,round(sum(nvl(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf'),0))/sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),2)),0))) as cfzonghj, \n" );
//				
//				sbsql.append("         decode(lx,1,(decode(decode(sum(mk.jiessl),0,0,round(sum(mk.jiessl*getjiesdzb('diancjsmkb',mk.id,'Qnetar','changf'))/sum(mk.jiessl))),0,0, \n");
//				sbsql.append("                round((decode(sum(mk.jiessl),0,0,round(sum(mk.hansmk)/sum(mk.jiessl),2))+nvl(decode(sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),0,0,round(sum(nvl(yf.hansyf,0))/sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),2)),0))*max(7000) \n");
//				sbsql.append("                /decode(sum(mk.jiessl),0,0,round(sum(mk.jiessl*getjiesdzb('diancjsmkb',mk.id,'数量','jies'))/sum(mk.jiessl))),2))),2,(decode(decode(sum(kmk.jiessl),0,0,round(sum(kmk.jiessl*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Qnetar','changf'))/sum(kmk.jiessl))),0,0, \n");
//				sbsql.append("                round((decode(sum(kmk.jiessl),0,0,round(sum(kmk.hansmk)/sum(kmk.jiessl),2))+nvl(decode(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),0,0,round(sum(nvl(kyf.hansyf,0))/sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),2)),0))*max(7000) \n");
//				sbsql.append("                /decode(sum(kmk.jiessl),0,0,round(sum(kmk.jiessl*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','jies'))/sum(kmk.jiessl))),2))),3,(decode(decode(sum(mk.jiessl),0,0,round(sum(mk.jiessl*getjiesdzb('diancjsmkb',mk.id,'Qnetar','changf'))/sum(mk.jiessl))),0,0, \n");
//				sbsql.append("                round((decode(sum(mk.jiessl),0,0,round(sum(mk.hansmk)/sum(mk.jiessl),2))+nvl(decode(sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),0,0,round(sum(nvl(yf.hansyf,0))/sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),2)),0))*max(7000) \n");
//				sbsql.append("                /decode(sum(mk.jiessl),0,0,round(sum(mk.jiessl*getjiesdzb('diancjsmkb',mk.id,'数量','jies'))/sum(mk.jiessl))),2)))-(decode(decode(sum(kmk.jiessl),0,0,round(sum(kmk.jiessl*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Qnetar','changf'))/sum(kmk.jiessl))),0,0, \n");
//				sbsql.append("                round((decode(sum(kmk.jiessl),0,0,round(sum(kmk.hansmk)/sum(kmk.jiessl),2))+nvl(decode(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),0,0,round(sum(nvl(kyf.hansyf,0))/sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),2)),0))*max(7000) \n");
//				sbsql.append("                /decode(sum(kmk.jiessl),0,0,round(sum(kmk.jiessl*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','jies'))/sum(kmk.jiessl))),2)))) as cfbiaomdj, \n");
//				
//				sbsql.append("         decode(lx,1,decode(decode(sum(mk.jiessl),0,0,round(sum(mk.jiessl*getjiesdzb('diancjsmkb',mk.id,'Qnetar','jies'))/sum(mk.jiessl))),0,0, \n");
//				sbsql.append("                round((decode(sum(mk.jiessl),0,0,round(sum(mk.hansmk)/sum(mk.jiessl),2))+nvl(decode(sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),0,0,round(sum(nvl(yf.hansyf,0))/sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),2)),0) \n");
//				sbsql.append("                -decode(sum(mk.jiessl),0,0,round(sum(mk.shuik)/sum(mk.jiessl),2)) \n");
//				sbsql.append("                -nvl(decode(sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),0,0,round(sum(nvl(yf.shuik,0))/sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),2)),0))*7000 \n");
//				sbsql.append("                /decode(sum(mk.jiessl),0,0,round(sum(mk.jiessl*getjiesdzb('diancjsmkb',mk.id,'Qnetar','jies'))/sum(mk.jiessl))),2)),2,decode(decode(sum(kmk.jiessl),0,0,round(sum(kmk.jiessl*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Qnetar','jies'))/sum(kmk.jiessl))),0,0, \n");
//				sbsql.append("                round((decode(sum(kmk.jiessl),0,0,round(sum(kmk.hansmk)/sum(kmk.jiessl),2))+nvl(decode(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),0,0,round(sum(nvl(kyf.hansyf,0))/sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),2)),0) \n");
//				sbsql.append("                -decode(sum(kmk.jiessl),0,0,round(sum(kmk.shuik)/sum(kmk.jiessl),2)) \n");
//				sbsql.append("                -nvl(decode(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),0,0,round(sum(nvl(kyf.shuik,0))/sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),2)),0))*7000 \n");
//				sbsql.append("                /decode(sum(kmk.jiessl),0,0,round(sum(kmk.jiessl*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Qnetar','jies'))/sum(kmk.jiessl))),2)),3,decode(decode(sum(mk.jiessl),0,0,round(sum(mk.jiessl*getjiesdzb('diancjsmkb',mk.id,'Qnetar','jies'))/sum(mk.jiessl))),0,0, \n");
//				sbsql.append("                round((decode(sum(mk.jiessl),0,0,round(sum(mk.hansmk)/sum(mk.jiessl),2))+nvl(decode(sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),0,0,round(sum(nvl(yf.hansyf,0))/sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),2)),0) \n");
//				sbsql.append("                -decode(sum(mk.jiessl),0,0,round(sum(mk.shuik)/sum(mk.jiessl),2)) \n");
//				sbsql.append("                -nvl(decode(sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),0,0,round(sum(nvl(yf.shuik,0))/sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),2)),0))*7000 \n");
//				sbsql.append("                /decode(sum(mk.jiessl),0,0,round(sum(mk.jiessl*getjiesdzb('diancjsmkb',mk.id,'Qnetar','jies'))/sum(mk.jiessl))),2))-decode(decode(sum(kmk.jiessl),0,0,round(sum(kmk.jiessl*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Qnetar','jies'))/sum(kmk.jiessl))),0,0, \n");
//				sbsql.append("                round((decode(sum(kmk.jiessl),0,0,round(sum(kmk.hansmk)/sum(kmk.jiessl),2))+nvl(decode(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),0,0,round(sum(nvl(kyf.hansyf,0))/sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),2)),0) \n");
//				sbsql.append("                -decode(sum(kmk.jiessl),0,0,round(sum(kmk.shuik)/sum(kmk.jiessl),2)) \n");
//				sbsql.append("                -nvl(decode(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),0,0,round(sum(nvl(kyf.shuik,0))/sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),2)),0))*7000 \n");
//				sbsql.append("                /decode(sum(kmk.jiessl),0,0,round(sum(kmk.jiessl*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Qnetar','jies'))/sum(kmk.jiessl))),2))) as cfbiaomdjbhs \n");
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
				 sbsql.append("         decode(lx,1,sum(nvl(mk.ches,0)),2,sum(nvl(kmk.ches,0)),3,sum(nvl(mk.ches,0)-nvl(kmk.ches,0))) as ches,sum(decode(lx,1,getjiesdzb('diancjsmkb',mk.id,'数量','gongf'),2,getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf'), 3,getjiesdzb('diancjsmkb',mk.id,'数量','gongf')-getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf'))) as gongfsl,  \n");
				 sbsql.append("			sum(decode(lx,1,getjiesdzb('diancjsmkb',mk.id,'数量','changf'), 2,getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','changf'), 3,getjiesdzb('diancjsmkb',mk.id,'数量','changf')-getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','changf'))) as yanssl,sum(decode(lx,1,nvl(mk.guohl,0),2,nvl(kmk.guohl,0),3,nvl(mk.guohl,0)-nvl(kmk.guohl,0))) as guohl,sum(0) as yuns,  \n");
//	燃料费用
				 sbsql.append("         sum(decode(lx,1,getjiesdzb('diancjsmkb',mk.id,'数量','yingk'),2,getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','yingk'),3,getjiesdzb('diancjsmkb',mk.id,'数量','yingk')-getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','yingk'))) as yingk,sum(decode(lx,1,getjiesdzb('diancjsmkb',mk.id,'数量','zhejje'),2,getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','zhejje'),3,getjiesdzb('diancjsmkb',mk.id,'数量','zhejje')-getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','zhejje'))) as shulzjje,  \n");//加入结算指标表
				 sbsql.append("         decode(lx,1,decode(sum(nvl(mk.jiessl,0)),0,0,round(sum(nvl(mk.jiessl,0)*getjiesdzb('diancjsmkb',mk.id,'Qnetar','changf'))/sum(nvl(mk.jiessl,0)))),2,decode(sum(nvl(kmk.jiessl,0)),0,0,round(sum(nvl(kmk.jiessl,0)*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Qnetar','changf'))/sum(nvl(kmk.jiessl,0)))),3,decode(sum(nvl(mk.jiessl,0)),0,0,round(sum(nvl(mk.jiessl,0)*getjiesdzb('diancjsmkb',mk.id,'Qnetar','changf'))/sum(nvl(mk.jiessl,0))))-decode(sum(nvl(kmk.jiessl,0)),0,0,round(sum(nvl(kmk.jiessl,0)*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Qnetar','changf'))/sum(nvl(kmk.jiessl,0))))) as yansrl,  \n");
				 sbsql.append("         sum(decode(lx,1,getjiesdzb('diancjsmkb',mk.id,'Qnetar','zhejje'), 2,getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Qnetar','zhejje'),3,getjiesdzb('diancjsmkb',mk.id,'Qnetar','zhejje')-getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Qnetar','zhejje'))) as relzjje,  \n");
				 sbsql.append("         decode(lx,1,decode(sum(nvl(mk.jiessl,0)),0,0,round(sum(nvl(mk.jiessl,0)*getjiesdzb('diancjsmkb',mk.id,'Std','changf'))/sum(nvl(mk.jiessl,0)),2)), 2,decode(sum(nvl(kmk.jiessl,0)),0,0,round(sum(nvl(kmk.jiessl,0)*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Std','changf'))/sum(nvl(kmk.jiessl,0)),2)),3,decode(sum(nvl(mk.jiessl,0)),0,0,round(sum(nvl(mk.jiessl,0)*getjiesdzb('diancjsmkb',mk.id,'Std','changf'))/sum(nvl(mk.jiessl,0)),2))-decode(sum(nvl(kmk.jiessl,0)),0,0,round(sum(nvl(kmk.jiessl,0)*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Std','changf'))/sum(nvl(kmk.jiessl,0)),2))) as liu,  \n");
				 sbsql.append("         sum(decode(lx,1,getjiesdzb('diancjsmkb',mk.id,'Std','zhejje'),2,getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Std','zhejje'), 3,getjiesdzb('diancjsmkb',mk.id,'Std','zhejje')-getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Std','zhejje'))) as liuyxje,sum(decode(lx,1,nvl(mk.jiessl,0),2,nvl(kmk.jiessl,0),3,nvl(mk.jiessl,0)-nvl(kmk.jiessl,0))) as jiessl,\n");
				 
				 sbsql.append("         decode(lx,1,decode(sum(getjiesdzb('diancjsmkb',mk.id,'数量','jies')),0,0,round(sum(getjiesdzb('diancjsmkb',mk.id,'数量','jies')*getjiesdzb('diancjsmkb',mk.id,'数量','zhejbz'))/sum(getjiesdzb('diancjsmkb',mk.id,'数量','jies')),2)),2,decode(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','jies')),0,0,round(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','jies')*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','zhejbz'))/sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','jies')),2)),"); 
				 sbsql.append("			3,decode(sum(getjiesdzb('diancjsmkb',mk.id,'数量','jies')),0,0,round(sum(getjiesdzb('diancjsmkb',mk.id,'数量','jies')*getjiesdzb('diancjsmkb',mk.id,'数量','zhejbz'))/sum(getjiesdzb('diancjsmkb',mk.id,'数量','jies')),2))-decode(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','jies')),0,0,round(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','jies')*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','zhejbz'))/sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','jies')),2))) as danj, \n");
				 sbsql.append("         sum(decode(lx,1,nvl(mk.buhsmk,0),2,nvl(kmk.buhsmk,0),3,nvl(mk.buhsmk,0)-nvl(kmk.buhsmk,0))) as jiakhj,sum(decode(lx,1,nvl(mk.shuik,0),2,nvl(kmk.shuik,0),3,nvl(mk.shuik,0)-nvl(kmk.shuik,0))) as jiaksk,sum(decode(lx,1,nvl(mk.hansmk,0),2,nvl(kmk.hansmk,0),3,nvl(mk.hansmk,0)-nvl(kmk.hansmk,0))) as jiashj, \n");
//	国铁运杂费
				 sbsql.append("         sum(decode(lx,1,nvl(yf.guotyf,0),2,nvl(kyf.guotyf,0),3,nvl(yf.guotyf,0)-nvl(kyf.guotyf,0))) as yunf,sum(decode(lx,1,nvl(yf.jiskc,0),2,nvl(kyf.jiskc,0),3,nvl(yf.jiskc,0)-nvl(kyf.jiskc,0))) as jiskc,sum(decode(lx,1,nvl(yf.buhsyf,0),2,nvl(kyf.buhsyf,0),3,nvl(yf.buhsyf,0)-nvl(kyf.buhsyf,0))) as buhsyf,sum(decode(lx,1,nvl(yf.shuik,0),2,nvl(kyf.shuik,0),3,nvl(yf.shuik,0)-nvl(kyf.shuik,0))) as shuik, \n");
				 sbsql.append("         sum(decode(lx,1,nvl(yf.hansyf,0),2,nvl(kyf.hansyf,0),3,nvl(yf.hansyf,0)-nvl(kyf.hansyf,0))) as yunzfhj,0 as kundjf,  \n");
				 sbsql.append("         sum(decode(lx,1,nvl(mk.hansmk,0)+nvl(yf.hansyf,0),2,nvl(kmk.hansmk,0)+nvl(kyf.hansyf,0),3,nvl(mk.hansmk,0)+nvl(yf.hansyf,0)-nvl(kmk.hansmk,0)+nvl(kyf.hansyf,0))) as cfjieszje, \n");
				
				sbsql.append("         decode(lx,1,decode(sum(nvl(mk.jiessl,0)),0,0,round(sum(nvl(mk.hansmk,0))/sum(nvl(mk.jiessl,0)),2))+nvl(decode(sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),0,0,round(sum(nvl(getjiesdzb('diancjsmkb',mk.id,'数量','gongf'),0))/sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),2)),0)  \n" );
				sbsql.append("		   ,2,decode(sum(nvl(kmk.jiessl,0)),0,0,round(sum(nvl(kmk.hansmk,0))/sum(nvl(kmk.jiessl,0)),2))+nvl(decode(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),0,0,round(sum(nvl(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf'),0))/sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),2)),0) \n" );
				sbsql.append("		   ,3,(decode(sum(nvl(mk.jiessl,0)),0,0,round(sum(nvl(mk.hansmk,0))/sum(nvl(mk.jiessl,0)),2))+nvl(decode(sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),0,0,round(sum(nvl(getjiesdzb('diancjsmkb',mk.id,'数量','gongf'),0))/sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),2)),0))\n");
				sbsql.append("		    -(decode(sum(nvl(kmk.jiessl,0)),0,0,round(sum(nvl(kmk.hansmk,0))/sum(nvl(kmk.jiessl,0)),2))+nvl(decode(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),0,0,round(sum(nvl(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf'),0))/sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),2)),0))) as cfzonghj, \n" );
				
				sbsql.append("         decode(lx,1,(decode(decode(sum(nvl(mk.jiessl,0)),0,0,round(sum(nvl(mk.jiessl,0)*getjiesdzb('diancjsmkb',mk.id,'Qnetar','changf'))/sum(nvl(mk.jiessl,0)))),0,0, \n");
				sbsql.append("                round((decode(sum(nvl(mk.jiessl,0)),0,0,round(sum(nvl(mk.hansmk,0))/sum(nvl(mk.jiessl,0)),2))+nvl(decode(sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),0,0,round(sum(nvl(yf.hansyf,0))/sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),2)),0))*max(7000) \n");
				sbsql.append("                /decode(sum(nvl(mk.jiessl,0)),0,0,round(sum(nvl(mk.jiessl,0)*getjiesdzb('diancjsmkb',mk.id,'数量','jies'))/sum(nvl(mk.jiessl,0)))),2))), 2,(decode(decode(sum(nvl(kmk.jiessl,0)),0,0,round(sum(nvl(kmk.jiessl,0)*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Qnetar','changf'))/sum(nvl(kmk.jiessl,0)))),0,0, \n");
				sbsql.append("                round((decode(sum(nvl(kmk.jiessl,0)),0,0,round(sum(nvl(kmk.hansmk,0))/sum(nvl(kmk.jiessl,0)),2))+nvl(decode(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),0,0,round(sum(nvl(kyf.hansyf,0))/sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),2)),0))*max(7000) \n");
				sbsql.append("                /decode(sum(nvl(kmk.jiessl,0)),0,0,round(sum(nvl(kmk.jiessl,0)*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','jies'))/sum(nvl(kmk.jiessl,0)))),2))),3,(decode(decode(sum(nvl(mk.jiessl,0)),0,0,round(sum(nvl(mk.jiessl,0)*getjiesdzb('diancjsmkb',mk.id,'Qnetar','changf'))/sum(nvl(mk.jiessl,0)))),0,0, \n");
				sbsql.append("                round((decode(sum(nvl(mk.jiessl,0)),0,0,round(sum(nvl(mk.hansmk,0))/sum(nvl(mk.jiessl,0)),2))+nvl(decode(sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),0,0,round(sum(nvl(yf.hansyf,0))/sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),2)),0))*max(7000) \n");
				sbsql.append("                /decode(sum(nvl(mk.jiessl,0)),0,0,round(sum(nvl(mk.jiessl,0)*getjiesdzb('diancjsmkb',mk.id,'数量','jies'))/sum(nvl(mk.jiessl,0)))),2)))-(decode(decode(sum(nvl(kmk.jiessl,0)),0,0,round(sum(nvl(kmk.jiessl,0)*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Qnetar','changf'))/sum(nvl(kmk.jiessl,0)))),0,0, \n");
				sbsql.append("                round((decode(sum(nvl(kmk.jiessl,0)),0,0,round(sum(nvl(kmk.hansmk,0))/sum(nvl(kmk.jiessl,0)),2))+nvl(decode(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),0,0,round(sum(nvl(kyf.hansyf,0))/sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),2)),0))*max(7000) \n");
				sbsql.append("                /decode(sum(nvl(kmk.jiessl,0)),0,0,round(sum(nvl(kmk.jiessl,0)*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','jies'))/sum(nvl(kmk.jiessl,0)))),2)))) as cfbiaomdj, \n");
				
				sbsql.append("        decode(lx,1,decode(decode(sum(nvl(mk.jiessl,0)),0,0,round(sum(nvl(mk.jiessl,0)*getjiesdzb('diancjsmkb',mk.id,'Qnetar','jies'))/sum(nvl(mk.jiessl,0)))),0,0, \n");
				sbsql.append("                round((decode(sum(nvl(mk.jiessl,0)),0,0,round(sum(nvl(mk.hansmk,0))/sum(nvl(mk.jiessl,0)),2))+nvl(decode(sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),0,0,round(sum(nvl(yf.hansyf,0))/sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),2)),0) \n");
				sbsql.append("                -decode(sum(nvl(mk.jiessl,0)),0,0,round(sum(nvl(mk.shuik,0))/sum(nvl(mk.jiessl,0)),2)) \n");
				sbsql.append("                -nvl(decode(sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),0,0,round(sum(nvl(yf.shuik,0))/sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),2)),0))*7000 \n");
				sbsql.append("                /decode(sum(nvl(mk.jiessl,0)),0,0,round(sum(nvl(mk.jiessl,0)*getjiesdzb('diancjsmkb',mk.id,'Qnetar','jies'))/sum(nvl(mk.jiessl,0)))),2)),2,decode(decode(sum(nvl(kmk.jiessl,0)),0,0,round(sum(nvl(kmk.jiessl,0)*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Qnetar','jies'))/sum(nvl(kmk.jiessl,0)))),0,0, \n");
				sbsql.append("                 round((decode(sum(nvl(kmk.jiessl,0)),0,0,round(sum(nvl(kmk.hansmk,0))/sum(nvl(kmk.jiessl,0)),2))+nvl(decode(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),0,0,round(sum(nvl(kyf.hansyf,0))/sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),2)),0) \n");
				sbsql.append("                -decode(sum(nvl(kmk.jiessl,0)),0,0,round(sum(nvl(kmk.shuik,0))/sum(nvl(kmk.jiessl,0)),2))  \n");
				sbsql.append("                 -nvl(decode(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),0,0,round(sum(nvl(kyf.shuik,0))/sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),2)),0))*7000 \n");
				sbsql.append("                /decode(sum(nvl(kmk.jiessl,0)),0,0,round(sum(nvl(kmk.jiessl,0)*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Qnetar','jies'))/sum(nvl(kmk.jiessl,0)))),2)),3,decode(decode(sum(nvl(mk.jiessl,0)),0,0,round(sum(nvl(mk.jiessl,0)*getjiesdzb('diancjsmkb',mk.id,'Qnetar','jies'))/sum(nvl(mk.jiessl,0)))),0,0, \n");
				sbsql.append("                round((decode(sum(nvl(mk.jiessl,0)),0,0,round(sum(nvl(mk.hansmk,0))/sum(nvl(mk.jiessl,0)),2))+nvl(decode(sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),0,0,round(sum(nvl(yf.hansyf,0))/sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),2)),0) \n");
				sbsql.append("                -decode(sum(nvl(mk.jiessl,0)),0,0,round(sum(nvl(mk.shuik,0))/sum(nvl(mk.jiessl,0)),2)) \n");
				sbsql.append("                -nvl(decode(sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),0,0,round(sum(nvl(yf.shuik,0))/sum(getjiesdzb('diancjsmkb',mk.id,'数量','gongf')),2)),0))*7000 \n");
				sbsql.append("               /decode(sum(nvl(mk.jiessl,0)),0,0,round(sum(nvl(mk.jiessl,0)*getjiesdzb('diancjsmkb',mk.id,'Qnetar','jies'))/sum(nvl(mk.jiessl,0)))),2))-decode(decode(sum(nvl(kmk.jiessl,0)),0,0,round(sum(nvl(kmk.jiessl,0)*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Qnetar','jies'))/sum(nvl(kmk.jiessl,0)))),0,0,  \n");
				sbsql.append("                 round((decode(sum(nvl(kmk.jiessl,0)),0,0,round(sum(nvl(kmk.hansmk,0))/sum(nvl(kmk.jiessl,0)),2))+nvl(decode(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),0,0,round(sum(nvl(kyf.hansyf,0))/sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),2)),0) \n");
				sbsql.append("                -decode(sum(nvl(kmk.jiessl,0)),0,0,round(sum(nvl(kmk.shuik,0))/sum(nvl(kmk.jiessl,0)),2)) \n");
				sbsql.append("                -nvl(decode(sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),0,0,round(sum(nvl(kyf.shuik,0))/sum(getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'数量','gongf')),2)),0))*7000 \n");
				sbsql.append("                 /decode(sum(nvl(kmk.jiessl,0)),0,0,round(sum(nvl(kmk.jiessl,0)*getjiesdzb('kuangfjsmkb',nvl(kmk.id,0),'Qnetar','jies'))/sum(nvl(kmk.jiessl,0)))),2))) as cfbiaomdjbhs \n");
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
				  
				sbsql.append("    from diancjsmkb mk,diancjsyfb yf,diancxxb dc,kuangfjsmkb kmk,kuangfjsyfb kyf,gongysb dq, \n");
				sbsql.append("		(select '电厂' as lb,1 as lx from dual \n");
				sbsql.append("			union \n");
				sbsql.append("				select '矿方' as lb,2 as lx from dual \n");
				sbsql.append("          union \n");
				sbsql.append("         		select '比较' as lb,3 as lx from dual \n");
				sbsql.append("			) a "+dianctjtab+" \n");
				sbsql.append("   where yf.diancjsmkb_id(+)= mk.id and mk.diancxxb_id = dc.id  and mk.gongysb_id=dq.id "+dianctjtj+" \n");
				sbsql.append("     and kmk.diancjsmkb_id(+) = mk.id and kmk.id = kyf.kuangfjsmkb_id(+) \n");
				sbsql.append(     wsql.toString()+" "+ tongjff + " ) \n");

				 int ArrWidth[]=new int[] {whith1,150,whith3,100,65,50,60,60,60,45,60,65,55,65,75,65,60,55,75,75,80,70,70,70,65,75,55,80,50,50,50};
				 ResultSet rs = cn.getResultSet(sbsql.toString());
				 Report rt = new Report();
				 
//				设置页标题
					rt.setTitle("结算统计台帐",ArrWidth);
					rt.setDefaultTitle(1,4,"填制单位:"+MainGlobal.getTableCol("diancxxb", "quanc", "id", String.valueOf(visit.getDiancxxb_id())),Table.ALIGN_LEFT);
					rt.setDefaultTitle(14,4,strDate,Table.ALIGN_CENTER);
					rt.createDefautlFooter(ArrWidth);
					rt.setDefautlFooter(32-3,3,"打印日期:"+FormatDate(new Date()),Table.ALIGN_RIGHT);
					
					//数据
					rt.setBody(new Table(rs,3,0,5));
					rt.body.setHeaderData(ArrHeader);//表头数据
					rt.body.setWidth(ArrWidth);
					rt.body.setPageRows(24);
					rt.body.mergeFixedRow();
					rt.body.mergeFixedCol(3);
					// 设置页数
					_CurrentPage = 1;
					_AllPages = rt.body.getPages();
					if (_AllPages == 0) {
						_CurrentPage = 0;
					}
					// System.out.println(rt.getAllPagesHtml());
					rs.close();
					return rt.getAllPagesHtml();
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					cn.Close();
				}
				return "";
			}
		}else {
			return "";
		}
	}
//***************************报表初始设置***************************//
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

	private String RT_CHANGFJSTJ = "changfjstj";

	private String mstrReportName = "changfjstj";

	public String getPrintTable() {
		if (mstrReportName.equals(RT_CHANGFJSTJ)) {
			return getSelectData();
		} else {
			return "无此报表";
		}
	}
	
//******************************其他*******************************//
	
//	public String getcontext() {
//		return "var  context='http://"
//				+ this.getRequestCycle().getRequestContext().getServerName()
//				+ ":"
//				+ this.getRequestCycle().getRequestContext().getServerPort()
//				+ ((Visit) getPage().getVisit()).getContextPath() + "';";
//	}

	public SortMode getSort() {
		return SortMode.USER;
	}
	
	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			// _pageLink = "window.location.target = '_blank';"+_pageLink;
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

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.opener=null;self.close();window.parent.close();open('"
					+ getpageLinks() + "','');";
		} else {
			return "";
		}
	}
	
	public String getTianzdw(long diancxxb_id) {
		String Tianzdw="";
		JDBCcon con=new JDBCcon();
		try{
			String sql="select quanc from diancxxb where id="+diancxxb_id;
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				Tianzdw=rs.getString("quanc");
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return Tianzdw;
	}
	public Date getMonthFirstday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}
	private String FormatDate(Date _date) {
		if (_date == null) {
//			return MainGlobal.Formatdate("yyyy年 MM月 dd日", new Date());
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	
	private String FormatDate2(Date _date) {
		if (_date == null) {
			return DateUtil.Formatdate("yyyy年 MM月 dd日", new Date());
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
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
	
}