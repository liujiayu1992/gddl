package com.zhiren.dc.zhuangh.caiygl;


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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.ExtTreeUtil;
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



public class Rucrlmqkreport extends BasePage {
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
		
		
		String c_sql=
			"select *  from (select rc.yundh,rc.chuanm,rc.gongys,rc.meikmc,rc.pinz,rc.faz,rc.yundl,rc.xiehl,rc.shuicl,to_char(rc.daohrq,'yyyy-mm-dd') as daohrq," +
			"rc.qnet_ar_k as qnet_ar_k_rc,rc.qnet_ar as qnet_ar_rc,\n" +
			" rc.mt as mt_rc,rc.mad as mad_rc,rc.aad as aad_rc,rc.vad as vad_rc,rc.stad as stad_rc,to_char(rc.xiemkssj,'yyyy-mm-dd hh24'),\n" +
			" to_char(rc.xiemjssj,'yyyy-mm-dd hh24'),rc.meic,rc.duowmc,rc.meicml,\n" +
			" to_char(rl.shangmkssj,'yyyy-mm-dd hh24'),to_char(rl.shangmjssj,'yyyy-mm-dd hh24'),rl.jizlh,rl.meich,rl.meil ,\n" +
			" rl.qnet_ar_k as qnet_ar_k_rl,rl.qnet_ar as qnet_ar_rl,\n" +
			" rl.mt as mt_rl,rl.mad as mad_rl,rl.aad as aad_rl,\n" +
			" rl.vad as vad_rl,rl.stad as stad_rl\n" +
			"  from \n" +
			" (select yundh,l.mingc as chuanm,g.mingc as gongys,m.mingc as meikmc,p.mingc as pinz,\n" +
			" c.mingc as faz,sum(biaoz) as yundl,sum(maoz-piz) as xiehl,sum(sanfsl) as shuicl,to_date(to_char(daohrq,'yyyy-mm-dd'),'yyyy-mm-dd') as daohrq,\n" +
			" xiemkssj,xiemjssj,mc.meic,mc.duowmc,sum(mc.meil) as meicml,\n" +
			" decode(sum(laimsl),0,0,round_new(sum(laimsl*round_new(qnet_ar,2))/sum(laimsl)/0.0041816,0)) as qnet_ar_k,\n" +
			" decode(sum(laimsl),0,0,round_new(sum(laimsl*round_new(qnet_ar,2))/sum(laimsl),2)) as qnet_ar,\n" +
			" decode(sum(laimsl),0,0,round_new(sum(laimsl*round_new(mt,1))/sum(laimsl),1)) as mt,\n" +
			" decode(sum(laimsl),0,0,round_new(sum(laimsl*round_new(mad,2))/sum(laimsl),2)) as mad,\n" +
			" decode(sum(laimsl),0,0,round_new(sum(laimsl*round_new(aad,2))/sum(laimsl),2)) as aad,\n" +
			" decode(sum(laimsl),0,0,round_new(sum(laimsl*round_new(vad,2))/sum(laimsl),2)) as  vad,\n" +
			" decode(sum(laimsl),0,0,round_new(sum(laimsl*round_new(stad,2))/sum(laimsl),2)) as stad\n" +
			"  from fahb,gongysb g,meikxxb m,pinzb p,chezxxb c,luncxxb l,zhilb,\n" +
			"  (select fahmcb.*,meicb.mingc as meic from fahmcb,meicb where fahmcb.meicb_id=meicb.id) mc\n" +
			" where fahb.gongysb_id=g.id and fahb.meikxxb_id=m.id \n" +
			" and fahb.pinzb_id=p.id and fahb.faz_id=c.id and fahb.luncxxb_id=l.id \n" +
			" and mc.fahb_id=fahb.id and fahb.zhilb_id=zhilb.id(+)\n" +
			" group by yundh,l.mingc,g.mingc,m.mingc,p.mingc,c.mingc,daohrq,xiemkssj,xiemjssj,mc.meic,mc.duowmc)rc,\n" +
			" (select rl.shangmkssj,rl.shangmjssj,meizmc,meicmc,meidmc,jizlh,meich,mx.meil,round_new(round_new(mx.qnet_ar,2)/0.0041816 ,0) as qnet_ar_k,\n" +
			" round_new(mx.qnet_ar,2) as qnet_ar,round_new(mx.mt,1) as mt,round_new(mx.mad,2) as mad,round_new(mx.aad,2) as aad,round_new(mx.vad,2) as vad,\n" +
			" round_new(mx.stad,2) as stad\n" +
			" from rulmzlzb rl,rulmzlzmxb mx \n" +
			" where rl.id=mx.rulmzlzb_id)rl\n" +
			" where rc.pinz=rl.meizmc(+) and rc.meic=rl.meicmc(+)\n" +
			" and rc.duowmc=rl.meidmc(+) and rc.xiemkssj<=rl.shangmkssj(+) and rc.daohrq >=to_date('"+getRiq()+"','yyyy-mm-dd') " +
			"and rc.daohrq <=to_date('"+getRiq1()+"','yyyy-mm-dd') order by rc.yundh,rc.xiemkssj,rl.shangmkssj)";//2010-08-06
 

		
		String ArrHeader[][]=new String[1][17];
		ArrHeader[0]=new String[] {"运单号","船名","供应商","煤矿名称","品种","发站港",
				"运单量","卸货量","水尺量","到货日期"
				,"Qnet,ar(Kcal/kg)","Qnet,ar(MJ/kg)"
				,"Mt(%)","Mad(%)","Aad(%)","Vad(%)","Stad(%)","卸煤开始时间","卸煤结束时间","煤场"
				,"煤堆","卸煤量"
				,"上煤开始时间","上煤结束时间","机组炉号","煤场号","煤量"
				,"Qnet,ar(Kcal/kg)","Qnet,ar(MJ/kg)"
				,"Mt(%)","Mad(%)","Aad(%)","Vad(%)","Stad(%)"};

		int ArrWidth[]=new int[] {70,80,80,60,45,75,
				45,45,45,90
				,45,45
				,45,45,45,45,45,90,
				90,45
				,45,45
				,90,90,55,45,45
				,45,45
				,45,45,45,45,45};
		
		
		//产生记录集数据
		ResultSet rs = cn.getResultSet(c_sql);
		int s=0;
		

		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//取得报表纸张类型
		
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		
		rt.setTitle("入厂入炉煤情况",ArrWidth);
		//footer
		
	
		rt.setDefaultTitle(1, 7, "制表单位:"+getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(8, 2, "", Table.ALIGN_RIGHT);
		rt.setDefaultTitle(13, 6, " ",Table.ALIGN_RIGHT);
		
		
		
		rt.setBody(new Table(rs,1,0,24));
		rt.body.setHeaderData(ArrHeader);
		rt.body.setWidth(ArrWidth);
		rt.body.ShowZero = false;
		rt.body.mergeFixedRow();
		
		rt.body.mergeFixedCols();
		//rt.body.mergeCol();
//		for (int i=1;i<=22;i++){
//			rt.body.mergeCol(i);
//		}
		
		
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		
		
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
	public static boolean isNetFileAvailable(String netFileUrl)
	{
	   InputStream   netFileInputStream =null;
	    try{
	     URL   url   =   new   URL(netFileUrl);   
	     URLConnection   urlConn   =   url.openConnection();   
	     netFileInputStream   =   urlConn.getInputStream(); 
	    }catch (IOException e)
	    {
	     return false;
	    }
	     if(null!=netFileInputStream)
	     {
	      return true;
	     }else
	     {
	     return false;
	     }
	}
	
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}


	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
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
			setRiq(DateUtil.FormatDate(new Date()));
			setRiq1(DateUtil.FormatDate(new Date()));
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
		
//		tb1.addText(new ToolbarText("条件选择:"));
//		ComboBox cb = new ComboBox();
//		cb.setTransform("BaoblxDropDown");
//		cb.setListeners("select:function(){document.Form0.submit();}");
//		cb.setId("Baoblx");
//		cb.setWidth(120);
//		tb1.addField(cb);
//		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("到货日期:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("Riq", "");// 与html页中的id绑定,并自动刷新
		df.setId("rulrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("至："));
		
		DateField df1 = new DateField();
		df1.setValue(getRiq1());
		df1.Binding("Riq1", "Form0");// 与html页中的id绑定,并自动刷新
		df1.setId("rulrq1");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
//		ToolbarButton tbLink = new ToolbarButton(null,"打印","function(){}");
		
		tb1.addItem(tb);
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
	
	// 日期是否变化
	private boolean riq1change = false;
	// 绑定日期
	private String riq1;

	public String getRiq1() {
		return riq1;
	}

	public void setRiq1(String riq1) {
		if (this.riq1 != null) {
			if (!this.riq1.equals(riq1))
				riq1change = true;
		}
		this.riq1 = riq1;
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
	
}
