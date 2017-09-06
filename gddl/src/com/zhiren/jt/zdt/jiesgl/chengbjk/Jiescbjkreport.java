//chh 2008-03-17 修改查询语句与表格格式
package com.zhiren.jt.zdt.jiesgl.chengbjk;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.jt.zdt.monthreport.tianb.shul.Slbbean;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Jiescbjkreport extends BasePage {
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

	private boolean blnIsBegin = false;

	private String RT_NAME_JIESCBJK="jiescbjk";
	private String mstrReportName="jiescbjk";
	public String getPrintTable(){
		if(!blnIsBegin){
			return "";
		}
		blnIsBegin=false;
		if (mstrReportName.equals(RT_NAME_JIESCBJK)){
			return getJiescbjk();
		}else{
			return "无此报表";
		}
	}
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	//结算列表
	private String getJiescbjk() {
		JDBCcon con = new JDBCcon();
		
		Visit visit = (Visit) getPage().getVisit();
		String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));
		String riq1=FormatDate(DateUtil.getDate(getEndriqDate()));
		String strDate=OraDate(DateUtil.getDate(getBeginriqDate()));//当前日期
		String strDate2=OraDate(DateUtil.getDate(getEndriqDate()));//当前日期
		//电厂条件
		String strGongsID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = " and (dc.fuid=  " +this.getTreeid()+"or dc.shangjgsid="+this.getTreeid()+")";
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();

		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
//		判断供应商下拉框取值在监控指标中有对应的值
		String jiankgys="";
		String strName="";
		strName="全部";
		
		jiankgys=" and (gy.dqid=jk.xiaogysb_id or gy.id=jk.xiaogysb_id )";
		if (!getBigGongysDropDownValue().getValue().equals("全部")){//大供应商不为空，取大供应商
			strName=getBigGongysDropDownValue().getValue();
			jiankgys=" and gy.dqid=jk.xiaogysb_id";
		}
		
		if (!getGongysDropDownValue().getValue().equals("全部")){
			strName=getGongysDropDownValue().getValue();//小供应商不为空，取小供应商
			jiankgys=" and gy.id=jk.xiaogysb_id";
		}
		
		String gongys = "select j.id as id from jiescbjkb j,diancxxb dc where j.diancxxb_id=dc.id"+strGongsID+" and xiaogysb_id=(select id from gongysb where mingc='"+strName+"')";
		String gysid="";
		ResultSet rs1 = con.getResultSet(gongys);
		try {
			if(rs1.next()){
				gysid=rs1.getString("id");
			}
		} catch (SQLException e) {
		// TODO 自动生成 catch 块
			e.printStackTrace();
		}

		if(!gysid.equals("")){
			jiankgys=jiankgys +" and jk.id=" +gysid;
			//jiankgys=" and gy.id=jk.xiaogysb_id";
		}else{
			//jiankgys=" and gy.dqid=jk.xiaogysb_id";
		}
		
		
		
		
		String jsrq="";
		String jsrq1="";
		String jsrq2="";
		if(getLeixSelectValue().getValue().equals("结算日期")){
			jsrq="js.jiesrq,";
			jsrq1="and js.jiesrq>="+ strDate + " and js.jiesrq<="+ strDate2+ " \n";
			jsrq2="and jsb.jiesrq>=jk.kaisrq and jsb.jiesrq<=jk.jiesrq";
		}else{
			jsrq="js.yansjzrq,";
			jsrq1="and js.yansjzrq>="+ strDate + " and js.yansjzrq<="+ strDate2+ " \n";
			jsrq2="and jsb.yansjzrq>=jk.kaisrq and jsb.yansjzrq<=jk.jiesrq";
		}

//		"select dc.mingc,gy.mingc,kj.mingc,js.bianm,js.jiessl,\n"
//			+ "       js.hansdj,nvl(jk.jiankmjxx,nvl(jk.jiankmjsx,0))||'～'||nvl(jk.jiankmjsx,0) as jiankmj,\n"
//			+ "       case when nvl(js.hansdj,0)>nvl(jk.jiankmjsx,0) then nvl(js.hansdj,0)-nvl(jk.jiankmjsx,0)\n"
//			+ "            else case when nvl(js.hansdj,0)<nvl(jk.jiankmjxx,0) then nvl(js.hansdj,0)-nvl(jk.jiankmjxx,0) else 0 end end as meijcc,\n"
//			+ "       rz.jies,nvl(jk.jiankrlxx,nvl(jk.jiankrlsx,0))||'～'||nvl(jk.jiankrlsx,0) as jiankrl,\n"
////			+ "       case when nvl(rz.jies,0)>nvl(jk.jiankrlsx,0) then nvl(rz.jies,0)-nvl(jk.jiankrlsx,0)\n"
////			+ "            else case when nvl(rz.jies,0)<nvl(jk.jiankrlsx,0) then nvl(rz.jies,0)-nvl(jk.jiankrlsx,0) else 0 end end as relcc\n"

//			+ "       case when nvl(rz.jies,0)>nvl(jk.jiankrlxx,0) then nvl(rz.jies,0)-nvl(jk.jiankrlxx,0)\n"
//			+ "            else case when nvl(rz.jies,0)<nvl(jk.jiankrlxx,0) then nvl(rz.jies,0)-nvl(jk.jiankrlxx,0) else 0 end end as relcc\n"
//			+ "  from jiesb js,jiescbjkb jk,diancxxb dc,vwgongys gy,jihkjb kj,jieszbsjb rz,zhibb zb\n"
//			+ "  where ((js.hansdj<jk.jiankmjxx or js.hansdj>jk.jiankmjsx) or (rz.jies<jk.jiankrlxx or rz.jies>jk.jiankrlsx))\n"
//			+ "   and js.diancxxb_id=jk.diancxxb_id "+jiankgys+"   \n"
//			+ "   and js.jihkjb_id=jk.jihkjb_id(+) and js.diancxxb_id=dc.id\n"
//			+ "   and js.gongysb_id=gy.id and js.jihkjb_id=kj.id\n"
//			+ "	  and rz.jiesdid=js.id and rz.zhibb_id=zb.id and zb.bianm='收到基低位热值Qnetar(MJ/Kg)' \n"
//			+ "   and js.jiesrq>="+ strDate	+ "\n"
//			+ "   and js.jiesrq<="+strDate2+" "+strGongsID;
//		------------------------------------按照日期类型进行查询----------------------------------------------------------

		String sql = "select jsb.dcmc,jsb.shoukdw,jsb.kjmc,jsb.bianm,jsb.jiessl,jsb.biaomdj,\n" +
			"               nvl(jk.jiankmjxx,nvl(jk.jiankmjsx,0))||'～'||nvl(jk.jiankmjsx,0) as jiankmj,\n" + 
			"               case when nvl(jsb.biaomdj,0)>nvl(jk.jiankmjsx,0) then nvl(jsb.biaomdj,0)-nvl(jk.jiankmjsx,0)\n" + 
			"               else case when nvl(jsb.biaomdj,0)<nvl(jk.jiankmjxx,0) then nvl(jsb.biaomdj,0)-nvl(jk.jiankmjxx,0) else 0 end end as meijcc,\n" +
////		-----------------------------用监控热值下限进行比较-------------------------------------
			"               jsb.jies,nvl(jk.jiankrlxx,nvl(jk.jiankrlsx,0))||'～'||nvl(jk.jiankrlsx,0) as jiankrl,\n" + 
			"               case when nvl(jsb.jies,0)>nvl(jk.jiankrlsx,0) then nvl(jsb.jies,0)-nvl(jk.jiankrlxx,0)\n" + 
			"                  else case when nvl(jsb.jies,0)<nvl(jk.jiankrlxx,0) then nvl(jsb.jies,0)-nvl(jk.jiankrlxx,0) else 0 end end as relcc\n" + 
			"        from jiescbjkb jk,\n" + 
			"        ( select js.id,js.bianm,js.jiessl,dc.mingc as dcmc,kj.mingc as kjmc,js.gongysb_id,js.shoukdw,"+jsrq+"js.diancxxb_id,js.jihkjb_id,\n" + 
			"                 round(decode(rz.jies,0,0,decode(js.jiessl,0,0,(js.buhsdj*js.jiessl+jyf.buhsyf)/js.jiessl) *7000/rz.jies),2) as biaomdj\n" + 
			"                 ,rz.jies as jies\n" + 
			"          from jiesb js,jiesyfb jyf, jieszbsjb rz,zhibb zb,jihkjb kj,diancxxb dc\n" + 
			"          where js.id=rz.jiesdid and js.diancxxb_id=dc.id and js.jihkjb_id=kj.id and rz.zhibb_id=zb.id\n" + 
			"               and zb.bianm='收到基低位热值Qnetar(MJ/Kg)' and jyf.diancjsmkb_id=js.id \n" + jsrq1+  
			"			  "+strGongsID+"\n" + 
			"         ) jsb,vwgongys gy\n" + 
			"          where jk.diancxxb_id=jsb.diancxxb_id and jk.jihkjb_id(+)=jsb.jihkjb_id and jsb.gongysb_id=gy.id\n" + 
			"			   "+jiankgys+"   \n"+jsrq2;

		if(getGongysDropDownValue().getId()!=-1){
			sql=sql+" and gy.mingc='" +getGongysDropDownValue().getValue()+"'";
		}

		String ArrHeader[][]=new String[1][11];
		ArrHeader[0]=new String[] {"电厂","供货单位","计划口径","结算单编号","结算数量","到厂不含税标煤单价","监控价格","价格超差","结算热量","监控热量","热差超差"};
	
		int ArrWidth[]=new int[] {80,120,60,100,65,60,80,50,60,80,50};
	
		ResultSet rs = con.getResultSet(sql);
		
		Report rt = new Report();

		rt.setBody(new Table(rs, 1, 0, 4));
		//
		rt.setTitle("结算指标监控", ArrWidth);
		rt.setDefaultTitle(1, 2, "制表单位:"+((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 4,getBeginriqDate()+"至"+getEndriqDate(), Table.ALIGN_LEFT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		
//		价格有超差或热值有超差的数据,到厂不含税标煤单价和结算热量为红色字体
		String color="#FF0000";
		for(int i=2;i<=rt.body.getRows();i++){
			if(!rt.body.getCellValue(i, 8).equals("0")){
				rt.body.setCellFontColor(i, 6, color);
			}
			if(!rt.body.getCellValue(i, 11).equals("0")){
				rt.body.setCellFontColor(i, 9, color);
			}
		}
		
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,2,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(4,2,"审核:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(7,2,"制表:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
	// 	设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}
//	________________________________________________________

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
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	///////
	private boolean BeginriqChange = false;
	public String getBeginriqDate() {
		if (((Visit) getPage().getVisit()).getString4() == null
				|| ((Visit) getPage().getVisit()).getString4() == "") {
			Calendar stra = Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil
					.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH, -1);
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}

	public void setBeginriqDate(String value) {
		if(((Visit) getPage().getVisit()).getString4()!=null ){
		if(((Visit) getPage().getVisit()).getString4().equals(value)){
			BeginriqChange=false;
		}else{
			BeginriqChange=true;
		}
		}
		((Visit) getPage().getVisit()).setString4(value);
	}

	private boolean EndriqChange = false; 
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		if(((Visit) getPage().getVisit()).getString5()!=null)
		if(((Visit) getPage().getVisit()).getString5().equals(value)){
			EndriqChange=false;
		}else{
			EndriqChange=true;
		}
		((Visit) getPage().getVisit()).setString5(value);
	}

	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
	
		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("结算日期:"));
		DateField df = new DateField();
		
		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1","");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);
		
		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2","");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("地区:"));
		ComboBox cb3 = new ComboBox();
		cb3.setTransform("BigGongysDropDown");
		cb3.setEditable(true);
		cb3.setWidth(120);
		tb1.addField(cb3);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("供应商:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("GongysDropDown");
		cb2.setEditable(true);
		tb1.addField(cb2);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("类型:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("LeixSelect");
		cb1.setWidth(80);
		tb1.addField(cb1);
		tb1.addText(new ToolbarText("-"));
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
//			visit.setString1(null);
			visit.setString4(null);
			visit.setString5(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel10(null);
			visit.setDropDownBean11(null);
			visit.setProSelectionModel11(null);
			visit.setDefaultTree(null);
			this.setBeginriqDate(DateUtil.FormatDate(visit.getMorkssj()));
			this.setEndriqDate(DateUtil.FormatDate(visit.getMorjssj()));
			setTreeid(null);
		}
		if(BeginriqChange || EndriqChange || treeChange){
			BeginriqChange =false;
			EndriqChange=false;
			treeChange = false; 
			visit.setDropDownBean11(null);
			visit.setProSelectionModel11(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel10(null);
		}
		
		if (BigGongysDropChange){
			visit.setDropDownBean10(null);
			visit.setProSelectionModel10(null);
		}

		getToolbars();
		blnIsBegin = true;
	}
    
	private boolean BigGongysDropChange =false;
	    public IDropDownBean getBigGongysDropDownValue() {
	    	if( ((Visit) getPage().getVisit()).getDropDownBean11()==null){
	   		 ((Visit) getPage().getVisit()).setDropDownBean11((IDropDownBean)getBigGongysDropDownModel().getOption(0));
	   	}
	       return  ((Visit) getPage().getVisit()).getDropDownBean11();
	    }
	    public void setBigGongysDropDownValue(IDropDownBean Value) {
	    	long id = -2;
			if (((Visit)getPage().getVisit()).getDropDownBean11()!= null) {
				id = getBigGongysDropDownValue().getId();
			}
			if (Value != null) {
				if (Value.getId() != id) {
					BigGongysDropChange = true;
				} else {
					BigGongysDropChange = false;
				}
			}
	    	((Visit) getPage().getVisit()).setDropDownBean11(Value);
	    }
	    public void setBigGongysDropDownModel(IPropertySelectionModel value) {
	    	((Visit) getPage().getVisit()).setProSelectionModel11(value);
	    }

	    public IPropertySelectionModel getBigGongysDropDownModel() {
	        if (((Visit) getPage().getVisit()).getProSelectionModel11() == null) {
	            getBigGongysDropDownModels();
	        }
	        return ((Visit) getPage().getVisit()).getProSelectionModel11();
	    }
	    public void getBigGongysDropDownModels() {
	    	String  sql ="select distinct gy.dqid as id,gy.dqmc as mingc from jiesb j,diancxxb dc,vwgongys gy  \n" +
					"where j.diancxxb_id in (select id \n"+
					"from( select id from diancxxb start with id="+this.getTreeid()+" connect by (fuid=prior id or shangjgsid=prior id))"+
					"union"+
					" select id"+
					" from diancxxb"+
					" where id="+this.getTreeid()+")"+
					" and gy.id=j.gongysb_id and j.jiesrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and j.jiesrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n"+
			        " order by gy.dqmc";

	    	((Visit) getPage().getVisit()).setProSelectionModel11(new IDropDownModel(sql,"全部")) ;
	        return ;
	    }
	    
	    
//	 供应商
	private boolean GongysDropChange =false;
    public IDropDownBean getGongysDropDownValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean10()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean10((IDropDownBean)getGongysDropDownModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean10();
    }
    public void setGongysDropDownValue(IDropDownBean Value) {
    	long id = -2;
		if (((Visit)getPage().getVisit()).getDropDownBean10()!= null) {
			id = getGongysDropDownValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				GongysDropChange = true;
			} else {
				GongysDropChange = false;
			}
		}
    	((Visit) getPage().getVisit()).setDropDownBean10(Value);
    }
    public void setGongysDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel10(value);
    }

    public IPropertySelectionModel getGongysDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {
            getGongysDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel10();
    }
    public void getGongysDropDownModels() {
		String strCondtion="";
		if (!getBigGongysDropDownValue().getValue().equals("全部")){
			strCondtion=" and gy.dqid="+getBigGongysDropDownValue().getId();
		}
    	String  sql ="";
//	    	sql="select distinct max(j.id) as id,j.shoukdw as mingc from jiesb j,diancxxb dc \n" +
//				"where j.diancxxb_id in (select id \n"+
//				 "from( select id from diancxxb start with id="+this.getTreeid()+" connect by (fuid=prior id or shangjgsid=prior id))"+
//				 "union"+
//				" select id"+
//				" from diancxxb"+
//				" where id="+this.getTreeid()+")"+
//				 "and j.jiesrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and j.jiesrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n"+
//		        "group by (j.shoukdw) order by j.shoukdw";
    		sql="select distinct gy.id as id,gy.mingc as mingc from jiesb j,diancxxb dc,vwgongys gy  \n" +
				"where j.diancxxb_id in (select id \n"+
				"from( select id from diancxxb start with id="+this.getTreeid()+" connect by (fuid=prior id or shangjgsid=prior id))"+
				"union"+
				" select id"+
				" from diancxxb"+
				" where id="+this.getTreeid()+")"+
				" and gy.id=j.gongysb_id and j.jiesrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd') and j.jiesrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')\n"+
				strCondtion+
		        " order by gy.mingc";

    	((Visit) getPage().getVisit()).setProSelectionModel10(new IDropDownModel(sql,"全部")) ;
        return ;
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
        list.add(new IDropDownBean(0,"入厂日期"));
        list.add(new IDropDownBean(1,"结算日期"));
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(list)) ;
        return ;
    }
    private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	private boolean treeChange = false;
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
				treeChange = true;
			}else{
				treeChange = false;
			}
		}
		this.treeid = treeid;
	}
    public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	电厂名称
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
	
	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}
	
	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		 setDiancmcModel(new IDropDownModel(sql)) ;
		 return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

		public void setDiancmcModel(IPropertySelectionModel _value) {
			((Visit)getPage().getVisit()).setProSelectionModel1(_value);
		}
//		得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
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
			} catch (SQLException e) {

				e.printStackTrace();
			}finally{
				con.Close();
			}

			return jib;
		}
//		得到电厂树下拉框的电厂名称或者分公司,集团的名称
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
				e.printStackTrace();
			}finally{
				cn.Close();
			}
			return IDropDownDiancmc;
		}
		
		//
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}
