package com.zhiren.dc.zhuangh.shulgl;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;



public class Zijjsdspreport extends BasePage {
//	 判断是否是集团用户
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团
	}
	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
	}
	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
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
	private String _msg;

	protected void initialize() {
		_msg = "";
	}

	public void setMsg(String _value) {
		_msg = _value;
	}
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
//	开始日期v
//	private Date _BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (((Visit)getPage().getVisit()).getDate1()==null){
			((Visit)getPage().getVisit()).setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate1();
	}
	
	public void setBeginriqDate(Date _value) {
		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate1()).equals(DateUtil.FormatDate(_value))) {
			_BeginriqChange=false;
		} else {
			((Visit)getPage().getVisit()).setDate1(_value);
			_BeginriqChange=true;
		}
	}
	
	private boolean _EndriqChange=false;
	public Date getEndriqDate() {
		if (((Visit)getPage().getVisit()).getDate2()==null){
			((Visit)getPage().getVisit()).setDate2(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate2();
	}
	
	public void setEndriqDate(Date _value) {
		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate1()).equals(DateUtil.FormatDate(_value))) {
			_EndriqChange=false;
		} else {
			((Visit)getPage().getVisit()).setDate2(_value);
			_EndriqChange=true;
		}
	}
	
	public boolean getRaw() {
		return true;
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
	
	private String REPORT_JIZYXQK="Report";
	private String REPORT_QUEMTJBG="quemtjbg";
	private String mstrReportName="";
	
	//得到登陆人员所属电厂或分公司的名称
	public String getDiancmc(){
		String diancmc="";
		JDBCcon cn = new JDBCcon();
		long diancid=((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc="select d.quanc from diancxxb d where d.id="+diancid;
		ResultSet rs=cn.getResultSet(sql_diancmc);
		try {
			while(rs.next()){
				 diancmc=rs.getString("quanc");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		cn.Close();
		if(diancmc.equals("大唐国际发电股份有限公司燃料管理部")){
	          return "大唐国际燃料管理部";
		}else{
			return diancmc;
		}
		
	}
	
	private boolean isBegin=false;
	public String getPrintTable(){
		if(mstrReportName.equals(REPORT_JIZYXQK)){
			return getJizyxqkreport();
		}else{
			return getJizyxqkreport();
		}
	}

	public String getJizyxqkreport(){
		Visit visit = ((Visit) this.getPage().getVisit());
		_CurrentPage=1;
		_AllPages=1;
		Date dat=new Date();//getBeginriqDate();//日期
		String strDate=DateUtil.FormatDate(dat);//日期字符
		Report rt=new Report();
		
		JDBCcon cn = new JDBCcon();

		String str = "";
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = " and dc.jib=3 ";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = " and  (dc.fuid = "+ getTreeid() + " or dc.shangjgsid= "+this.getTreeid()+")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = " and dc.id = " + getTreeid() + "";
		}
		
		//取得页面查询方式
		String zhuangt=this.getBaoblxValue().getStrId();
		
		//存放构造的部分sql语句
		String id="0";
		
		//进行语句的构造
		if(visit.getStringBuffer2().equals("")||visit.getStringBuffer2()==null){
			
		}else{
			id=visit.getStringBuffer2().toString();
		}
		String c_sql=

			" select shenqbm,shoukdw,fukfs,shenqzjxx,shenqzjdx,shenqrq,shiyzjjjnr from zijsyspb where id="+id;

		//产生记录集数据
		ResultSet rs = cn.getResultSet(c_sql);
		String shenqbm="";
		String shoukdw="";
		String fukfs="";
		String shenqzjxx="";
		String shenqzjdx="";
		
		String xianj="";
		String zhip="";
		String dianh="";
		String huip="";
		String shenqrq="";
		String shiyzjjjnr="";
		try {
			while(rs.next()){
				
				shenqbm=rs.getString("shenqbm");
				shoukdw=rs.getString("shoukdw");
				fukfs=rs.getString("fukfs");
				shenqrq=rs.getDate("shenqrq").toString();
				shiyzjjjnr=rs.getString("shiyzjjjnr");
				if("现金".equals(rs.getString("fukfs"))){
					xianj="√";
				}
				if("支票".equals(rs.getString("fukfs"))){
					zhip="√";
				}
				if("电汇".equals(rs.getString("fukfs"))){
					dianh="√";
				}
				if("汇票".equals(rs.getString("fukfs"))){
					huip="√";
				}
				shenqzjxx=rs.getString("shenqzjxx");
				shenqzjdx=rs.getString("shenqzjdx");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		
		 int ArrWidth[]=new int[] {125,70,65,65,65,65,73,70,86,75,87,59};
		 
		 String ArrHeader[][]=new String[13][12];
		 ArrHeader[0]=new String[] {"申请部门:"+shenqbm,"","","","收款单位:"+shoukdw,"","","","","会计机构负责人:","",""};
		 ArrHeader[1]=new String[] {"使用资金经济内容或经济事项:<br>  "+shiyzjjjnr,"","","","","","","","","","",""};
		 ArrHeader[2]=new String[] {"","","","","","","","","","","",""};
		 ArrHeader[3]=new String[] {" ","","","","","","","","","公司(厂)分管领导:","",""};
		 ArrHeader[4]=new String[] {"","","","","","","","","","","",""};
		 ArrHeader[5]=new String[] {""," ","","","","","","","","","",""};
		 ArrHeader[6]=new String[] {""," ","","","","","","","","总会计师批示:","",""};
		 ArrHeader[7]=new String[] {"","","","","","","","","","","",""};
		 ArrHeader[8]=new String[] {"","","","","","","","","","","",""};
		 ArrHeader[9]=new String[] {"","","","","","","","","","总经理(厂长)批示:","",""};
		 ArrHeader[10]=new String[] {"付款方式:","现金",""+xianj,"支票",""+zhip,"电汇",""+dianh,"汇票",""+huip,"","",""};
		 ArrHeader[11]=new String[] {"申请金额(大写):"+shenqzjdx,"","","","","小写:"+shenqzjxx,"","","","","",""};
		 ArrHeader[12]=new String[] {"部门负责人:"+"","","","","经办人:","","","","","","",""};
		 
		 
		
	 
		

		
		rt.setTitle("资金使用审批单<hr width=300/><hr width=300/>",ArrWidth);
		//footer
		 String ArrHeader1[][]=new String[1][12];
		 ArrHeader1[0]=new String[] {"注：根据本企业资金管理制度规定权限履行审批程序，审批人应写明意见、签名和日期。"+"","","","","","","","","","","",""};
//		rt1.setDefaultTitle(1, 8, "", Table.ALIGN_LEFT);
		
		rt.setDefaultTitle(1, 4, "制表单位:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(6, 2, shenqrq.replaceFirst("-", "年").replaceFirst("-", "月")+"日", Table.ALIGN_CENTER);
//		rt.setDefaultTitle(13, 6, " ",Table.ALIGN_RIGHT);
		
		String ArrFooter[][]=new String[1][20];
					
			
		rt.setBody(new Table(ArrHeader,0,0,0));
		 rt.body.setWidth(ArrWidth);	

		rt.body.ShowZero = false;

		rt.body.setCellBorder(1, 10, 0, 0, 0, 0);
		rt.body.setCellBorder(1, 11, 0, 0, 0, 0);
		rt.body.setCellBorder(1, 12, 0, 1, 0, 0);
		
		rt.body.setCellBorder(2, 1, 0, 0, 0, 0);
		rt.body.setCellBorder(2, 2, 0, 0, 0, 0);
		rt.body.setCellBorder(2, 3, 0, 0, 0, 0);
		rt.body.setCellBorder(2, 4, 0, 0, 0, 0);
		rt.body.setCellBorder(2, 5, 0, 0, 0, 0);
		rt.body.setCellBorder(2, 6, 0, 0, 0, 0);
		rt.body.setCellBorder(2, 7, 0, 0, 0, 0);
		rt.body.setCellBorder(2, 8, 0, 0, 0, 0);
		rt.body.setCellBorder(2, 9, 0, 1, 0, 0);
		rt.body.setCellBorder(2, 10, 0, 0, 0, 0);
		rt.body.setCellBorder(2, 11, 0, 0, 0, 0);
		rt.body.setCellBorder(2, 12, 0, 1, 0, 0);
		
		rt.body.setCellBorder(3, 10, 0, 0, 0, 1);
		rt.body.setCellBorder(3, 11, 0, 0, 0, 1);
		rt.body.setCellBorder(3, 12, 0, 1, 0, 1);
		rt.body.setCellBorder(4, 10, 0, 0, 0, 0);
		rt.body.setCellBorder(4, 11, 0, 0, 0, 0);
		rt.body.setCellBorder(4, 12, 0, 1, 0, 0);
		
		rt.body.setCellBorder(5, 10, 0, 0, 0, 0);
		rt.body.setCellBorder(5, 11, 0, 0, 0, 0);
		rt.body.setCellBorder(5, 12, 0, 1, 0, 0);
		
		rt.body.setCellBorder(6, 10, 0, 0, 0, 1);
		rt.body.setCellBorder(6, 11, 0, 0, 0, 1);
		rt.body.setCellBorder(6, 12, 0, 1, 0, 1);
		
		rt.body.setCellBorder(7, 10, 0, 0, 0, 0);
		rt.body.setCellBorder(7, 11, 0, 0, 0, 0);
		rt.body.setCellBorder(7, 12, 0, 1, 0, 0);
		
		rt.body.setCellBorder(8, 10, 0, 0, 0, 0);
		rt.body.setCellBorder(8, 11, 0, 0, 0, 0);
		rt.body.setCellBorder(8, 12, 0, 1, 0, 0);
		
		rt.body.setCellBorder(9, 10, 0, 0, 0, 1);
		rt.body.setCellBorder(9, 11, 0, 0, 0, 1);
		rt.body.setCellBorder(9, 12, 0, 1, 0, 1);
		
		rt.body.setCellBorder(10, 10, 0, 0, 0, 0);
		rt.body.setCellBorder(10, 11, 0, 0, 0, 0);
		rt.body.setCellBorder(10, 12, 0, 1, 0, 0);
		
		rt.body.setCellBorder(11, 10, 0, 0, 0, 0);
		rt.body.setCellBorder(11, 11, 0, 0, 0, 0);
		rt.body.setCellBorder(11, 12, 0, 1, 0, 0);
		
		rt.body.setCellBorder(12, 10, 0, 0, 0, 0);
		rt.body.setCellBorder(12, 11, 0, 0, 0, 0);
		rt.body.setCellBorder(12, 12, 0, 1, 0, 0);
		rt.body.setCellBorder(13, 10, 0, 0, 0, 1);
		rt.body.setCellBorder(13, 11, 0, 0, 0, 1);
		rt.body.setCellBorder(13, 12, 0, 1, 0, 1);
		for (int i=0;i<7;i++){
			
			rt.body.setCellBorder(3+i, 1, 0, 0, 0, 0);
			rt.body.setCellBorder(3+i, 2, 0, 0, 0, 0);
			rt.body.setCellBorder(3+i, 3, 0, 0, 0, 0);
			rt.body.setCellBorder(3+i, 4, 0, 0, 0, 0);
			rt.body.setCellBorder(3+i, 5, 0, 0, 0, 0);
			rt.body.setCellBorder(3+i, 6, 0, 0, 0, 0);
			rt.body.setCellBorder(3+i, 7, 0, 0, 0, 0);
			rt.body.setCellBorder(3+i, 8, 0, 0, 0, 0);
			rt.body.setCellBorder(3+i, 9, 0, 1, 0, 0);
			 rt.body.setRowHeight(3+i, 39);
		}
		
		rt.body.setCellBorder(10, 1, 0, 0, 0, 1);
		rt.body.setCellBorder(10, 2, 0, 0, 0, 1);
		rt.body.setCellBorder(10, 3, 0, 0, 0, 1);
		rt.body.setCellBorder(10, 4, 0, 0, 0, 1);
		rt.body.setCellBorder(10, 5, 0, 0, 0, 1);
		rt.body.setCellBorder(10, 6, 0, 0, 0, 1);
		rt.body.setCellBorder(10, 7, 0, 0, 0, 1);
		rt.body.setCellBorder(10, 8, 0, 0, 0, 1);
		rt.body.setCellBorder(10, 9, 0, 1, 0, 1);
		

		 rt.body.mergeCell(1,1,1,4);
		 rt.body.mergeCell(1,5,1,9);
		 rt.body.mergeCell(1,10,1,11);

		 rt.body.mergeCell(2,1,2,8);

		 rt.body.setCellBorder(2, 9, 0, 1, 0, 0);

		 rt.body.mergeCell(4,10,4,11);
		 
		 rt.body.mergeCell(7,10,7,11);

		 rt.body.mergeCell(10,10,10,11);
		 rt.body.mergeCell(12,1,12,5);
		 rt.body.mergeCell(12,6,12,9);
		 rt.body.mergeCell(13,1,13,4);
		 rt.body.mergeCell(13,5,13,9);
		rt.body.setColAlign(10, Table.ALIGN_LEFT);
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.setColAlign(4, Table.ALIGN_LEFT);
		rt.body.setColAlign(5, Table.ALIGN_LEFT);
		
		 rt.body.setRowHeight(13, 40);
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1,12,"注：根据本企业资金管理制度规定权限履行审批程序，审批人应写明意见、签名和日期。",Table.ALIGN_LEFT);
		 rt.footer.setBorder(0);
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

//	
	//


	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	
	private boolean _ReturnChick = false;
	
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
 

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Return(cycle);
		}
		
		if (_ReturnChick) {
			_ReturnChick = false;
			Return(cycle);
		}
	}

	private void Return(IRequestCycle cycle) {
		Visit visit = (Visit) this.getPage().getVisit();
	
			
			cycle.activate("Zijjsdsp_zh");
		
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			this.setBaoblxValue(null);
			this.getIBaoblxModels();
			setRiq(DateUtil.FormatDate(new Date()));
			this.setTreeid(null);
			
			//begin方法里进行初始化设置
			visit.setString4(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString4(pagewith);
				}
			//	visit.setString1(null);保存传递的非默认纸张的样式
				
		}
		
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			
			if(!visit.getString1().equals("")){
				if(!visit.getString1().equals(cycle.getRequestContext().getParameters("lx")[0])){
					visit.setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
					visit.setDropDownBean1(null);
					visit.setProSelectionModel1(null);
					visit.setDropDownBean4(null);
					visit.setProSelectionModel4(null);
					this.setTreeid(null);
				}
			}
			visit.setString1((cycle.getRequestContext().getParameters("lx")[0]));
			mstrReportName = visit.getString1();
        }else{
        	if(!visit.getString1().equals("")) {
        		mstrReportName = visit.getString1();
            }
        }

		if(_Baoblxchange){
			_Baoblxchange=false;
			getJizyxqkreport();
		}
		getToolBars();
		isBegin=true;
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
		setDiancxxModel(new IDropDownModel(sql,"中电投"));
	}
	
	
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
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
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;
	}
	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
//		tb1.addText(new ToolbarText("日期:"));
//		DateField df = new DateField();
//		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
//		df.Binding("riqDateSelect","forms[0]");// 与html页中的id绑定,并自动刷新
//		df.setWidth(100);
//		//df.setListeners("select:function(){document.Form0.submit();}");
//		tb1.addField(df);
//		
//		tb1.addText(new ToolbarText("-"));
		
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

		 
		ToolbarButton tb = new ToolbarButton(null,"返回","function(){document.getElementById('RefurbishButton').click();}");
//		ToolbarButton tbb = new ToolbarButton(null,"返回","function(){document.Form0.submit();}");
 
		
		tb1.addItem(tb);
//		tb1.addItem(tbb);
//		tb1.addItem(tbLink);
		setToolbar(tb1);
	}
	
	// 日期是否变化
	private boolean riqchange = false;

	// 绑定日期
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		if (this.riq != null) {
			if (!this.riq.equals(riq))
				riqchange = true;
		}
		this.riq = riq;
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
		sql = "select d.id,d.mingc from diancxxb d order by d.mingc desc";
		
		_IDiancmcModel = new IDropDownModel(sql);
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

	private String treeid;

	/*public String getTreeid() {
		if (treeid == null || "".equals(treeid)) {
			return "-1";
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
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
	
//  Day下拉框
	private boolean _Day = false;
	private IDropDownBean _DayValue;
	private IPropertySelectionModel _DayModel;

	public IDropDownBean getDayValue() {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean) getDayModel().getOption(14));
    	}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}
	public void setDayValue(IDropDownBean Value) {
//		if(((Visit)getPage().getVisit()).getDropDownBean1()!=null){
			if(((Visit)getPage().getVisit()).getDropDownBean1().getId()!=Value.getId()){
				_Day=true;
			}
//		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setDayModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
    }
    public IPropertySelectionModel getDayModel() {
        if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
            getDayModels();
        }
        return ((Visit)getPage().getVisit()).getProSelectionModel1();
    }
    public void getDayModels() {
        List listDay = new ArrayList();
//        listDay.add(new IDropDownBean(-1, "请选择"));
    	for (int i = 1; i < 32; i++) {
            listDay.add(new IDropDownBean(i, String.valueOf(i)+"天"));
        }
    	((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(listDay));
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
	
	public boolean _Baoblxchange = false;
	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if(_BaoblxValue==null){
			_BaoblxValue=(IDropDownBean)getIBaoblxModels().getOption(0);
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
		try{
			List fahdwList = new ArrayList();
			fahdwList.add(new IDropDownBean(0,"今天(昨天14点到今天14点)"));
			fahdwList.add(new IDropDownBean(1,"明天(今天14点到现在)"));
			fahdwList.add(new IDropDownBean(2,"未完成取样(所有)"));
			fahdwList.add(new IDropDownBean(3,"未完成取样(今天)"));
			fahdwList.add(new IDropDownBean(4,"所有"));
			_IBaoblxModel = new IDropDownModel(fahdwList);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
	}
}
