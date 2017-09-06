package com.zhiren.dtrlgs.fkgl;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.text.DecimalFormat;
//import java.util.Calendar;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
//import com.zhiren.liuc.Liuc;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.Money;
import org.apache.tapestry.contrib.palette.SortMode;

public class Fuktzdcx extends BasePage {
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
//	格式化
	public String format(double dblValue,String strFormat){
		 DecimalFormat df = new DecimalFormat(strFormat);
		 return formatq(df.format(dblValue));
		 
	}
	
	public String formatq(String strValue){//加千位分隔符
		String strtmp="",xiaostmp="",tmp="";
		int i=3;
		if(strValue.lastIndexOf(".")==-1){
			
			strtmp=strValue;
			if(strValue.equals("")){
				
				xiaostmp="";
			}else{
				
				xiaostmp=".00";
			}
			
		}else {
			
			strtmp=strValue.substring(0,strValue.lastIndexOf("."));
			
			if(strValue.substring(strValue.lastIndexOf(".")).length()==2){
				
				xiaostmp=strValue.substring(strValue.lastIndexOf("."))+"0";
			}else{
				
				xiaostmp=strValue.substring(strValue.lastIndexOf("."));
			}
			
		}
		tmp=strtmp;
		
		while(i<tmp.length()){
			strtmp=strtmp.substring(0,strtmp.length()-(i+(i-3)/3))+","+strtmp.substring(strtmp.length()-(i+(i-3)/3),strtmp.length());
			i=i+3;
		}
		
		return strtmp+xiaostmp;
	}
//****************判断页面是否是第一次调用**************//
	int zhuangt=0;
	public int getZhuangt() {
//		return ((Visit) getPage().getVisit()).getZhuangt();
		return zhuangt;
	}

	public void setZhuangt(int _value) {
//		((Visit) getPage().getVisit()).setZhuangt(_value);
		this.zhuangt=_value;
	}
	
//*****************************************条件设置开始******************************************//	
//****************设置日期*******************//
	//开始日期
	private Date _BeginriqValue =new Date();
	
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue=new Date();
		}
		return _BeginriqValue;
	}
	
	public void setBeginriqDate(Date _value) {
		if (FormatDate(_BeginriqValue).equals(FormatDate(_value))) {
		} else {
			_BeginriqValue = _value;
		}
	}
	//结束日期
	private Date _EndriqValue =new Date();
	
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
		}
	}
	
	//电厂名称
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
			_DiancmcValue=(IDropDownBean)getIDiancmcModels().getOption(0);
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

	public IPropertySelectionModel getIDiancmcModels() {
		
		String sql="";
		
		sql = "select d.id,d.jianc from diancxxb d order by d.id desc";
//		System.out.println(sql);
		
		_IDiancmcModel = new IDropDownModel(sql);
		return _IDiancmcModel;
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
	//返回按钮
	private boolean _FanhChick = false;

	public void FanhButton(IRequestCycle cycle) {
		_FanhChick = true;
	}
	
//	private boolean _PrintChick = false;
//
//	public void PrintButton(IRequestCycle cycle) {
//		_PrintChick = true;
//	}
	
	public void submit(IRequestCycle cycle) {

		if (_FanhChick) {
//			chaxunzt1 = 1;// 查询状态
//			zhuangt=2;
			Return(cycle);
			_FanhChick = false;
		}
		
//		if(_PrintChick){
//			_PrintChick=false;
//			Tij();
//		}
		
	}
/*
	private void PrintState(){
		JDBCcon con=new JDBCcon();
		String sql = "update kuangfjsmkb set CAIWCDYZT=1 where bianh='"+((Visit) getPage().getVisit()).getJiesdbh()+"'";
		con.getUpdate(sql);
		con.Close();
	}
*/	
	private void Return(IRequestCycle cycle) {
		// 为 "刷新" 按钮添加处理程序
		/* ((Visit) getPage().getVisit()).setTiaozzt(1);
		 cycle.activate(((Visit)getPage().getVisit()).getStringData1());*/
//		 cycle.activate("Kuangfjscw");
	}
//******************页面初始设置********************//
	private String v_fukdbh="";
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
//			visit.setMissSession(true);
			return;
		} else {
//			visit.setMissSession(false);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
//			visit.setZhuangt(1);//是第一次显示

			getSelectData();
		}
		if(cycle.getRequestContext().getParameters("fukdbh") !=null) {
//			visit.getQuerylx().clear();
//			visit.getQuerylx().add(cycle.getRequestContext().getParameters("fukdbh")[0]);
//			v_fukdbh = (String)visit.getQuerylx().get(0);
			v_fukdbh=cycle.getRequestContext().getParameters("fukdbh")[0];
        }/*else{
        	if(visit.getQuerylx().size()>0) {
        		v_fukdbh = (String)visit.getQuerylx().get(0);
            }
        }*/
	}
	
//	public void Tij(){
//		
//		long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();
//		
//		long fuktzb_id = getFuktzbID();
//		if(fuktzb_id!=-1){
//			Liuc.tij("fuktzb", fuktzb_id, renyxxb_id, "");
//		}
//	}
//	
	public long getFuktzbID(){
		JDBCcon cn = new JDBCcon();
		ResultSet rs;
		long lngfuktbid = -1;
		String strzhuangt = "";
	
		String sql = "select fk.id,lz.mingc as zhuangt,fk.liucztb_id,fk.liucgzid from fuktzb fk,liucztb zt,leibztb lz where fk.liucztb_id=zt.id and zt.leibztb_id=lz.id and fukdbh='"+v_fukdbh+"'";
		try{
			rs = cn.getResultSet(sql);
			while(rs.next()){
				lngfuktbid = rs.getLong("id");
				strzhuangt = rs.getString("zhuangt");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		if(! strzhuangt.equals("打印")){
			lngfuktbid = -1;
		}
		return lngfuktbid;
	}
	
	public String getDXMoney(double _Money ){
		Money money=new Money();
		return money.NumToRMBStr(_Money);
	}
	
	public String getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		Report rt=new Report();
		  try{
			 String sql=""; 
			 long id=-1;
			 String strzhibrq="";//制表日期
			 String strfukdbh="";
			 String strfukdlx="";
			 String strfapje="";
			 double dbshijfk=0;
			 
			 String strhexyfk="";
			 String strkouyf="";
			 String strkouhkf="";
			 String strshijfkdx="";
			 String strfapbhs="";
			 String strshoukdw="";
			 String strgongysbm="";
			 String strdiz="";
			 String strkaihyh="";
			 String strzhangh="";
			 String strxiangmmc="";
			 String strxiangmbh="";
			 String strcaiwtxxx="";
			 String strcaiwtxbz="";
			 String strtianzdw="";
			 
			 String strshijfk="";
			 
				
			 sql = "select fk.*,gy.quanc,gy.bianm,gy.danwdz diz,gy.kaihyh,gy.zhangh "
				 + "  from fuktzb fk,gongysb gy where fk.gongysb_id=gy.id and fukdbh='"+v_fukdbh+"'"; 
			 ResultSet rs = cn.getResultSet(sql);
			 
			 if(rs.next()){
				 id=rs.getLong("id");
				 strzhibrq=FormatDate(rs.getDate("riq"));
				 
				 strfukdbh=rs.getString("fukdbh");
				 strfukdlx=rs.getString("fukdlx");
				 
				 strfapje=new DecimalFormat("0.00").format(rs.getDouble("fapje"));
				 dbshijfk = rs.getDouble("shijfk");
				 strhexyfk=new DecimalFormat("0.00").format(rs.getDouble("hexyfk"));
				 strkouyf=new DecimalFormat("0.00").format(rs.getDouble("kouyf"));
				 strkouhkf=new DecimalFormat("0.00").format(rs.getDouble("kouhkf"));
				
				 strshijfkdx=getDXMoney(dbshijfk);
				 strfapbhs=rs.getString("fapbhs");
				 if(strfapbhs!=""&&strfapbhs!=null){
				
				 String[] fabh2=strfapbhs.split(",");
				 strfapbhs="";
				  int j=0;
				 for(int i=0;i<fabh2.length;i++){
					 j++;
					 strfapbhs+=fabh2[i]+",";
					 if(j%10==0){
						 
						 strfapbhs+="<br>";
					 }
							 
				 }
				 strfapbhs= strfapbhs.substring(0,strfapbhs.lastIndexOf(",")-1);
				 }
				 strshoukdw=rs.getString("quanc");
				 strgongysbm=rs.getString("bianm");
				 strdiz=rs.getString("diz");
				 strkaihyh=rs.getString("kaihyh");
				 strzhangh=rs.getString("zhangh");
				 strxiangmmc=rs.getString("xiangmmc");
				 strxiangmbh=rs.getString("xiangmbh");
				 strcaiwtxxx=rs.getString("caiwtxxx");
				 strcaiwtxbz=nvlStr(rs.getString("caiwtxbz"));
				 strtianzdw=nvlStr(rs.getString("tianzdw"));
				 
//				********************人员日期*****************	//
			 }
			 String shenhxx[] = {"","","","","",""};
//			 查出流程状态
			 String sql_tr = "select fk.id,lz.mingc as zhuangt,fk.liucztb_id,fk.liucgzid from fuktzb fk,liucztb zt,leibztb lz where fk.liucztb_id=zt.id and zt.leibztb_id=lz.id and fukdbh='"+v_fukdbh+"'";
				long liucgzid=0;
				long liucztb_id=0;
			ResultSet	rs_liuczt = cn.getResultSet(sql_tr);
				while(rs_liuczt.next()){
					liucgzid=rs_liuczt.getLong("liucztb_id");
					liucztb_id=rs_liuczt.getLong("liucgzid");
				}
				String kfsql="select *  from liucgzb where id in(\n"+
					   " select max(gz.id) gz_id from liucgzb gz,liucdzb dz,liucztb zt1,liucztb zt2 ,liucztb zt3\n"+
					   " where gz.liucdzb_id=dz.id  and gz.liucgzid="+liucztb_id+" and dz.liucztqqid=zt1.id and dz.liuczthjid=zt2.id and zt1.xuh<zt2.xuh \n"+
					   " and zt2.xuh<=zt3.xuh \n"+
					   "    and zt3.id="+liucgzid+"\n"+
//					   " and gz.liucgzid="+liucgzid+"\n"+
					   "   group by gz.liucdzb_id\n"+
					  "  ) order by shij";
//			 sql = "select * from (select to_char(max(gz.shij),'yyyy-mm-dd') as caozrq,ry.quanm "
//				 + "  from liucgzb gz,fuktzb fk,renyxxb ry where fk.id=gz.liucgzid and gz.caozy=ry.yonghm "
//				 + "   and fk.fukdbh='"+v_fukdbh+"' group by ry.quanm order by max(gz.shij) ) ";
			  ResultSet rs_quanm = cn.getResultSet(kfsql);
			 for(int i=0;rs_quanm.next();i++){
				 shenhxx[i] = rs_quanm.getString("caozy");
			 }
			 rs.close();
			 rs_liuczt.close();
			 rs_quanm.close();
			 
			 strshijfk = new DecimalFormat("0.00").format(dbshijfk);
			 String strmoney = new DecimalFormat("0").format(dbshijfk*100);
			 String money[] = {"","","","","","","","","","",""};
			 for(int i=0;i<strmoney.length();i++){
				 if(i<10){
					 money[10-i] = strmoney.substring(strmoney.length()-(i+1), strmoney.length()-i);
				 }else{
					 money[0] = strmoney.substring(0, strmoney.length()-i);
				 }
			 }
			 String strshenhr = "";
			 String biaot="";
			 if(strfukdlx.equals("预付款")){
				 strshenhr = "申请人";
				 biaot="预付款通知单";
			 }else{
				 strshenhr = "领款人";
				 biaot="付款通知单";
			 }
			 String ArrHeader[][]=new String[14][21];
			 ArrHeader[0]=new String[] {"付款事由","",strfukdlx,"","","","","","","","","","","","","","","","","",""};
			 ArrHeader[1]=new String[] {"发票金额","","扣运费","","扣回空费","","核销预付款","","实际付款","","实际付款金额","","","","","","","","","",""};
			 ArrHeader[2]=new String[] {"","","","","","","","","","","亿","千","百","十","万","千","百","十","元","角","分"};
			 ArrHeader[3]=new String[] {formatq(strfapje),"",formatq(strkouyf),"",formatq(strkouhkf),"",formatq(strhexyfk),"",formatq(strshijfk),"",money[0],money[1],money[2],money[3],money[4],money[5],money[6],money[7],money[8],money[9],money[10]};
			 ArrHeader[4]=new String[] {"大写","",strshijfkdx,"","","","","","","","","","","","","","","","","",""};
			 ArrHeader[5]=new String[] {"发票编号","",strfapbhs,"","","","","","","","","","","","","","","","","",""};
			 ArrHeader[6]=new String[] {"收款单位信息","","名称","",strshoukdw,"","","","","","","","","","","","","","","",""};
			 ArrHeader[7]=new String[] {"","","编码","",strgongysbm,"","","","","地址","",strdiz,"","","","","","","","",""};
			 ArrHeader[8]=new String[] {"","","开户银行","",strkaihyh,"","","","","银行帐号","",strzhangh,"","","","","","","","",""};
			 ArrHeader[9]=new String[] {"项目信息","","项目名称","",strxiangmmc,"","","","","项目编号","",strxiangmbh,"","","","","","","","",""};
			 if(strfukdlx.equals("预付款")){
				 ArrHeader[10]=new String[] {"单位主管",shenhxx[3],"会计主管",shenhxx[2],"会计","","出纳","","部门主管",shenhxx[1],"经办人",shenhxx[0],"","","",strshenhr,"","","","",""};
			 }else if(dbshijfk==0){
				 ArrHeader[10]=new String[] {"单位主管",shenhxx[3],"会计主管",shenhxx[2],"会计","","出纳","","部门主管",shenhxx[1],"经办人",shenhxx[0],"","","",strshenhr,"","","","",""};
			 }else{
				 ArrHeader[10]=new String[] {"单位主管",shenhxx[3],"会计主管",shenhxx[2],"会计","","出纳","","部门主管",shenhxx[1],"经办人",shenhxx[0],"","","",strshenhr,"","","","",""};
			 }
			 ArrHeader[11]=new String[] {"以下内容有财务部填写","","","","","","","","","","","","","","","","","","","",""};
			 ArrHeader[12]=new String[] {"总帐日记账名称/应付付款凭证号","","","","","","","","备注","","","","","","","","","","","",""};
			 ArrHeader[13]=new String[] {strcaiwtxxx,"","","","","","","",strcaiwtxbz,"","","","","","","","","","","",""};

			 int ArrWidth[]=new int[] {23,110,23,110,23,110,23,110,23,110,23,23,23,23,23,23,23,23,23,23,23};

//			 String logoPath = MainGlobal.getHomeContext(this)+"/img/report/logo.gif";
//			 定义页Title
			 
			 rt.setTitle(biaot,ArrWidth);
//			 rt.title.setCellImage(2,3,35,32,logoPath);
			 rt.setDefaultTitleLeft("填制单位："+strtianzdw,5);
			 rt.setDefaultTitle(6,5,"制表日期："+strzhibrq,Table.ALIGN_CENTER);
			 rt.setDefaultTitle(16,6,"编号:"+strfukdbh,Table.ALIGN_RIGHT);
//			 Table table=new Table(rs,0, 0, 0);
			 rt.setBody(new Table(ArrHeader,0,0,0));
//			 rt.setBody(table);
			 rt.body.setRowHeight(25);
			 rt.body.setRowHeight(11,60);
			 rt.body.setRowHeight(14, 80);
			 rt.body.setWidth(ArrWidth);	
			 rt.body.ShowZero=true;
//			 合并单元格
			 rt.body.mergeCell(1,1,1,2);
			 rt.body.mergeCell(1,3,1,21);
			 rt.body.mergeCell(2,1,3,2);
			 rt.body.mergeCell(2,3,3,4);
			 rt.body.mergeCell(2,5,3,6);
			 rt.body.mergeCell(2,7,3,8);
			 rt.body.mergeCell(2,9,3,10);
			 rt.body.mergeCell(2,11,2,21);
			 rt.body.mergeCell(4,1,4,2);
			 rt.body.mergeCell(4,3,4,4);
			 rt.body.mergeCell(4,5,4,6);
			 rt.body.mergeCell(4,7,4,8);
			 rt.body.mergeCell(4,9,4,10);
			 rt.body.mergeCell(5,1,5,2);
			 rt.body.mergeCell(5,3,5,21);
			 rt.body.mergeCell(6,1,6,2);
			 rt.body.mergeCell(6,3,6,21);
			 rt.body.mergeCell(7,1,9,2);
			 rt.body.mergeCell(7,3,7,4);
			 rt.body.mergeCell(7,5,7,21);
			 rt.body.mergeCell(8,3,8,4);
			 rt.body.mergeCell(8,5,8,9);
			 rt.body.mergeCell(8,10,8,11);
			 rt.body.mergeCell(8,12,8,21);
			 rt.body.mergeCell(9,3,9,4);
			 rt.body.mergeCell(9,5,9,9);
			 rt.body.mergeCell(9,10,9,11);
			 rt.body.mergeCell(9,12,9,21);
			 rt.body.mergeCell(10,1,10,2);
			 rt.body.mergeCell(10,3,10,4);
			 rt.body.mergeCell(10,5,10,9);
			 rt.body.mergeCell(10,10,10,11);
			 rt.body.mergeCell(10,12,10,21);
			 rt.body.mergeCell(11,12,11,15);
			 rt.body.mergeCell(11,17,11,21);
			 rt.body.mergeCell(12,1,12,21);
			 rt.body.mergeCell(13,1,13,8);
			 rt.body.mergeCell(13,9,13,21);
			 rt.body.mergeCell(14,1,14,8);
			 rt.body.mergeCell(14,9,14,21);
			 
			 rt.body.setCells(1,1,3,21,Table.PER_ALIGN,Table.ALIGN_CENTER);
			 rt.body.setCells(1,3,1,21,Table.PER_ALIGN,Table.ALIGN_LEFT);
			 rt.body.setCells(4,1,4,21,Table.PER_ALIGN,Table.ALIGN_RIGHT);
			 rt.body.setCells(5, 1, 12, 1, Table.PER_ALIGN, Table.ALIGN_CENTER);
			 rt.body.setCells(7, 3, 13, 3, Table.PER_ALIGN, Table.ALIGN_CENTER);
			 rt.body.setCells(8, 10, 10, 11, Table.PER_ALIGN, Table.ALIGN_CENTER);
			 rt.body.setCells(13, 1, 13, 21, Table.PER_ALIGN, Table.ALIGN_CENTER);
			 rt.body.setCellAlign(11, 5, Table.ALIGN_CENTER);
			 rt.body.setCellAlign(11, 7, Table.ALIGN_CENTER);
			 rt.body.setCellAlign(11, 9, Table.ALIGN_CENTER);
			 rt.body.setCellAlign(11, 11, Table.ALIGN_CENTER);
			 rt.body.setCellAlign(11, 16, Table.ALIGN_CENTER);
			 
				// 设置页数
				_CurrentPage = 0;
//				_AllPages = rt.body.getPages();
				
				if (_AllPages == 0) {
					_CurrentPage = 0;
				}
				return rt.getAllPagesHtml(0);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cn.Close();
			}
			return rt.getAllPagesHtml(0);
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

	private String RT_FUKD = "fukd";

	private String mstrReportName = "fukd";

	public String getPrintTable() {
		if (mstrReportName.equals(RT_FUKD)) {
			return getSelectData();
		} else {
			return "无此报表";
		}
	}
	
//******************************其他*******************************//
	
	public String getcontext() {
		/*return "var  context='http://"
				+ this.getRequestCycle().getRequestContext().getServerName()
				+ ":"
				+ this.getRequestCycle().getRequestContext().getServerPort()
				+ ((Visit) getPage().getVisit()).getContextPath() + "';";*/
		return MainGlobal.getHomeContext(this);
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

//	public String getPageHome() {
//		if (((Visit) getPage().getVisit()).getMissSession()) {
//			return "window.opener=null;self.close();window.parent.close();open('"
//					+ getpageLinks() + "','');";
//		} else {
//			return "";
//		}
//	}
	
	private String FormatDate(Date _date) {
		if (_date == null) {
//			return MainGlobal.Formatdate("yyyy年 MM月 dd日", new Date());
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	
	private String nvlStr(String strValue){
		if (strValue==null) {
			return "";
		}else if(strValue.equals("null")){
			return "";
		}
		
		return strValue;
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
