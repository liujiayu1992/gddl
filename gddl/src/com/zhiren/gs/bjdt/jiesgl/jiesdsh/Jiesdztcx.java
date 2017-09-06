package com.zhiren.gs.bjdt.jiesgl.jiesdsh;

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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

public class Jiesdztcx extends BasePage {
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
	
	private static long WODRW = 1;// 刚刚提交未选择流程的任务

	private static long LIUCZ = 2;// 未审核任务

	private static long YISH = 3;// 已审核
	
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
	//得到单位全称
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
		}
		cn.Close();
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
		if(_Danwqc.equals("北京大唐燃料有限公司")&&visit.getRenyjb()==2){
			_Danwqc="大唐国际发电股份有限公司燃料管理部";
		}
		JDBCcon cn = new JDBCcon();
		String sql =new String();
		long lngDiancId=getDiancxxbId();//电厂信息表id
		String strDate=this.getStartdate();
		String endDate=this.getEnddate();
		
		String strWeizhitj="";
		String leix = "公司结算";
		long renyxxb_id = -1;
		String liucztb_id="";
		if (getWeizSelectValue().getId() == WODRW) {// 得到刚刚从电厂上传的，还未选择流程的条目
			strWeizhitj = ILiuc.getWeixz("diancjsmkb", renyxxb_id, leix);
		} else if (getWeizSelectValue().getId() == LIUCZ) {// 得到未审核的任务
			strWeizhitj = ILiuc.getWeish("diancjsmkb", renyxxb_id, leix);
		} else {// 得到已审核的任务
			liucztb_id = ILiuc.getYish("diancjsmkb", renyxxb_id, leix);
		}

		sql = "select  dianmc,bianh,fahdw,hetbh,jiessl,guohl,jiesrl,guohl,jiesrl,changfrl,jiesliuf,changflf,danj,hansmk,meikje,shuik,to_char(jiesrq,'yyyy-mm-dd'),zhuangt\n"
			+ "  from ("
			+ "  select js.id,'diancjsmkb' as tabname,di.id as diancid,di.mingc as dianmc,\n"
			+ "				  getHtmlAlert('"
			+ MainGlobal.getHomeContext(this)
			+ "','Showjsd','jiesdbh',js.bianm,js.bianm) as bianh,"
			+ "               --gy.jianc as gongysmc, \n"
			+ "               js.gongysmc as fahdw,he.hetbh hetbh,jiessl,guohl,nvl(changfrl,0) changfrl,nvl(jiesrl,0) jiesrl,nvl(jieslf,0) jiesliuf,nvl(changflf,0) changflf,\n"
			+ "				  hansmk,meikje,shuik,"
			+ "				  buhsdj danj,js.jiesrq, \n"
			+ "				  nvl(jiesrl,0)-nvl(changfrl,0) as rezc,(nvl(jiessl,0)-nvl(guohl,0))-round((nvl(guohl,0)*0.02)) as shulc,	\n"
			+ "               lz.mingc zhuangt,zt.liucb_id,zt.id as liucztb_id\n"
			+ "          from diancjsmkb js , \n"
			+ "		(\n"
			+ "select nvl(rel.jiesdid,quanl.jiesdid) jiesdid,nvl( changfrl,0) changfrl,nvl(jiesrl,0) jiesrl,nvl(changflf,0) changflf,nvl(jieslf,0) jieslf from\n"
			+ "(select ji.jiesdid  jiesdid, ji.changf changfrl,ji.jies jiesrl from  jieszbsjb ji  left join zhibb zhi on(ji.zhibb_id=zhi.id) where zhi.mingc='收到基低位热值') rel\n"
			+ "full outer join\n"
			+ "(select ji.jiesdid jiesdid, ji.changf changflf,ji.jies jieslf from  jieszbsjb ji left join zhibb zhi on(ji.zhibb_id=zhi.id) where zhi.mingc='干燥基全硫') quanl\n"
			+ "on (rel.jiesdid=quanl.jiesdid)\n"
			+ "      )chengf ,\n"
			+ " gongysb gon \n"
			+ " , hetb he \n"
			+ " ,liucztb zt \n"
			+ " ,leibztb lz \n"
			+ " ,liuclbb ll \n"
			+ " ,diancxxb di\n";
			if(getWeizSelectValue().getId() == YISH)
			{sql+= "  where js.liucztb_id in(" + liucztb_id + ")  and chengf.jiesdid(+)=js.id and gon.id(+)=js.gongysb_id\n" ;}
			else{
				sql+= "  where js.id in(" + strWeizhitj + ") and chengf.jiesdid(+)=js.id and gon.id(+)=js.gongysb_id\n" ;
			}
//			+ "  where js.id in(" + strWeizhitj + ") and chengf.jiesdid(+)=js.id and gon.id(+)=js.gongysb_id\n" +
			sql+="and he.id(+)=js.hetb_id and zt.id(+)=js.liucztb_id and lz.id(+)=zt.leibztb_id and ll.id(+)=lz.liuclbb_id\n" +
			"and js.diancxxb_id=di.id(+)\n";
		if(new Long(this.getTreeid()).longValue()==1){   
		sql+="and jiesrq>=to_date('"+strDate+"','yyyy-mm-dd') and jiesrq<=to_date('"+endDate+"','yyyy-mm-dd')\n";
		}else{
			sql+=" and jiesrq>=to_date('"+strDate+"','yyyy-mm-dd') and jiesrq<=to_date('"+endDate+"','yyyy-mm-dd') and di.id="+this.getTreeid()+"\n";
		}
		sql+=")\n";
		ResultSet rs=cn.getResultSet(sql);
		Report rt=new Report();
		
		//定义表头数据
		 String ArrHeader[][]=new String[1][];
		 ArrHeader[0]=new String[] {"电厂名称","结算单编码","发货单位","合同编号","结算数量","过衡数量","结算热值","过衡数量","结算热值","厂方热值","结算硫分","厂方硫分","单价","价税金额","价款金额","税款","结算日期","状态"};
		
		 //		列宽
		 int ArrWidth[]=new int[] {75,75,110,50,50,55,55,55,55,55,55,55,50,55,55,55,70,55};
		 
		
		 //设置页标题
		rt.setTitle("审核状态查询结果表",ArrWidth);
		
		rt.setDefaultTitle(1,5,"填报单位:"+_Danwqc,Table.ALIGN_LEFT);
		String strMonth=DateUtil.FormatDate(new Date());
		rt.setDefaultTitle(7,4,strMonth,Table.ALIGN_CENTER);
//		rt.setDefaultTitle(rt.title.getCols()-1,2,"调燃01表",Table.ALIGN_RIGHT);
		
		//数据
		rt.setBody(new Table(rs,1,0,2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(32);
		rt.body.setHeaderData(ArrHeader);//表头数据
		rt.body.ShowZero=false;
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCol(1);
		rt.body.setColAlign(2,Table.ALIGN_LEFT);
		rt.body.setColAlign(3,Table.ALIGN_LEFT);
		rt.body.setColFormat(4,"0");
		rt.body.setColFormat(5,"0");
		rt.body.setColFormat(6,"0");
		rt.body.setColFormat(7,"0");
		rt.body.setColFormat(8,"0");
		rt.body.setColFormat(9,"0");
		rt.body.setColFormat(10,"0");
		rt.body.setColFormat(11,"0");
		rt.body.setColFormat(12,"0");
		rt.body.setColFormat(13,"0");
		rt.body.setColFormat(14,"0");
		rt.body.setColFormat(15,"0");
		rt.body.setColFormat(16,"0");
		rt.body.setColFormat(17,"0");
//		rt.body.setColFormat(21,"0.0");
//		rt.body.setColFormat(23,"0.0");
//		rt.body.setColFormat(26,"0.00");
//		rt.body.setColFormat(27,"0.00");	
		
		//页脚
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(2,1,"批准:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(6,1,"制表:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(10,1,"审核:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(26,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT);
		
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
			visit.setDefaultTree(null);
			getDiancmcModels();
			getMeikdqmcModels();
			setWeizSelectModel(null);
			setWeizSelectValue(null);
//			getSelectData();
		}
		
		
           this.getSelectData();
		setUserName(visit.getRenymc());
		
		blnIsBegin = true;
		
		
	}
//显示工具栏
//	流程状态下拉框
	public void getSelectData() {
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
			tb1.addText(new ToolbarText("流程状态:"));
			ComboBox WeizSelect = new ComboBox();
			WeizSelect.setId("Weizx");
			WeizSelect.setWidth(120);
			WeizSelect.setLazyRender(true);
			WeizSelect.setTransform("WeizSelectx");
//			WeizSelect.setListeners("'change':function(){document.getElementById('liucfs').value=liucfs.getValue();}");
			tb1.addField(WeizSelect);	
		tb1.addText(new ToolbarText("-"));
//		流程方式下拉框
//		tb1.addText(new ToolbarText("流程方式："));
//		    ComboBox liucfs = new ComboBox();
//		    liucfs.setId("liucfs");
//		    liucfs.setWidth(80);
//		    liucfs.setLazyRender(true);
//		    liucfs.setTransform("liucfs");
//		    liucfs.setListeners("'change':function(){document.getElementById('liucfs').value=liucfs.getValue();}");
//	    	tb1.addField(liucfs);
//		日期范围选择
		 DateField startdf=new DateField();
		 startdf.setValue(this.getStartdate());
		 startdf.Binding("startdate", "");
		 tb1.addText(new ToolbarText("日期："));
		 tb1.addField(startdf);
		 tb1.addText(new ToolbarText("至"));
		 DateField enddf=new DateField();
		 enddf.setValue(this.getEnddate());
		 enddf.Binding("enddate", "");
		 tb1.addField(enddf);
			
		tb1.addText(new ToolbarText("-"));
		
//		电厂树
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
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		ToolbarButton tb = new ToolbarButton(null,"查找","function(){document.Form0.submit();}");
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
		// 为 "刷新" 按钮添加处理程序
	}
	

//	电厂名称
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
		
//		煤矿地区
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
	        
	        if (((Visit) getPage().getVisit()).isDCUser()){
	        	sql="select distinct gys.id,gys.mingc from diaor16bb d,gongysb gys where d.gongysb_id=gys.id and diancxxb_id=" + lngDiancxxbID;
	        }else if(((Visit) getPage().getVisit()).isJTUser()){
	        	sql="select distinct gys.id,gys.mingc from diaor16bb d ,gongysb gys where d.gongysb_id=gys.id  ";
	        }else {
	        	sql="select distinct gys.id,gys.mingc from diaor16bb d,diancxxb dc,gongysb gys where d.gongysb_id=gys.id and d.diancxxb_id=dc.id and dc.fuid=" + lngDiancxxbID;	        	
	        }
	        ((Visit)getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql,"全部"));
	        return ((Visit)getPage().getVisit()).getProSelectionModel3();
	    }
	    
	    public void setMeikdqmcModel(IPropertySelectionModel _value) {
	    	((Visit)getPage().getVisit()).setProSelectionModel3(_value);
	    }
		


	//地区名称
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

	// Page方法
	protected void initialize() {
		_pageLink = "";
	}
	// 开始日期设值
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

	// 结束日期设值
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

	// 位置下拉菜单--流程状态
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
		list.add(new IDropDownBean(1, "未选择流程的任务"));
		list.add(new IDropDownBean(2, "待审核的任务"));
		list.add(new IDropDownBean(3, "已审核的任务"));
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(list));
	}


}
