package com.zhiren.dc.huaygl.huaybb.baob;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * 作者：王磊
 * 时间：2009-11-25
 * 描述：修改质量报表增加参数修改格式
 */
/*
 * 作者：陈泽天
 * 时间：2010-01-21 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
public class Zhilbb extends BasePage {

	private static final String REPORTNAME_ZHILBB="Zhilbb"; 
	
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
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
	
//	绑定日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
//	绑定日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}
//	页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public boolean getRaw() {
		return true;
	}
//	品种下拉框
	public IDropDownBean getPinzValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getPinzModel().getOptionCount()>0) {
				setPinzValue((IDropDownBean)getPinzModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	public void setPinzValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getPinzModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			setPinzModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	public void setPinzModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	public void setPinzModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(-1,"全部"));
		setPinzModel(new IDropDownModel(list,SysConstant.SQL_Pinz_mei));
	}
//	运输方式下拉框
	public IDropDownBean getYunsfsValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean4()==null) {
			if(getYunsfsModel().getOptionCount()>0) {
				setYunsfsValue((IDropDownBean)getYunsfsModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean4();
	}
	public void setYunsfsValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean4(value);
	}
	
	public IPropertySelectionModel getYunsfsModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel4()==null) {
			setYunsfsModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel4();
	}
	public void setYunsfsModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel4(value);
	}
	public void setYunsfsModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(-1,"全部"));
		setYunsfsModel(new IDropDownModel(list,SysConstant.SQL_yunsfs));
	}
//  获得选择的树节点的对应的电厂名称   
    private String getDcMingc(String id){ 
    	if(id == null || "".equals(id)){
    		return "";
    	}
		JDBCcon con=new JDBCcon();
		String mingc="";
		String sql="select mingc from diancxxb where id = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=rsl.getString("mingc");
		}
		rsl.close();
		con.Close();
		return mingc;
	}
//  获得选择的树节点的对应的供应商名称   
    private String[] getGys(String id){ 
    	String[] gys={"全部","-1"};
    	if(id==null || "".equals(id)){
    		return gys;
    	}
		JDBCcon con=new JDBCcon();
		String sql="select mingc,lx from vwgongysmk where id = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			gys[0]=rsl.getString("mingc");
			gys[1]=rsl.getString("lx");
		}
		rsl.close();
		con.Close();
		return gys;
	}
    
//  判断电厂Tree中所选电厂时候还有子电厂   
    private boolean isParentDc(String id){ 
    	boolean isParent= false;
    	if(id == null || "".equals(id)){
    		return isParent;
    	}
		JDBCcon con=new JDBCcon();
		String sql="select mingc from diancxxb where fuid = " + id;
		if(con.getHasIt(sql)){
			isParent = true;
		}
		con.Close();
		return isParent;
	}
//	取得日期参数SQL
    private String getDateParam(){
//		日期条件
		String rqsql = "";
		if(getBRiq() == null || "".equals(getBRiq())){
			rqsql = "and f.daohrq >= "+DateUtil.FormatOracleDate(new Date())+"\n";
		}else{
			rqsql = "and f.daohrq >= "+DateUtil.FormatOracleDate(getBRiq())+"\n";
		}
		if(getERiq() == null || "".equals(getERiq())){
			rqsql += "and f.daohrq < "+DateUtil.FormatOracleDate(new Date())+"+1\n";
		}else{
			rqsql += "and f.daohrq < "+DateUtil.FormatOracleDate(getERiq())+"+1\n";
		}
		return rqsql;
    }
//  取得供应商参数SQL
    private String getGysParam(){
//		供应商煤矿条件
		String gyssql = "";
		if("1".equals(getGys(getTreeid())[1])){
			gyssql = "and f.gongysb_id = " + getTreeid() + "\n";
		}else if("0".equals(getGys(getTreeid())[1])){
			gyssql = "and f.meikxxb_id = " + getTreeid() + "\n";
		}
		return gyssql;
    }
//  取得品种参数SQL
    private String getPinzParam(){
//		品种sql
		String pzsql = "";
		if(getPinzValue() != null && getPinzValue().getId() != -1){
			pzsql = "and f.pinzb_id = " + getPinzValue().getId() + "\n";
		}
		return pzsql;
    }
    
//  取得运输方式参数SQL
    private String getYunsfsParam(){
		String yunsfssql = "";
		if(getYunsfsValue() != null && getYunsfsValue().getId() != -1){
			yunsfssql = "and f.yunsfsb_id = " + getYunsfsValue().getId() + "\n";
		}
		return yunsfssql;
    }

//	设置制表人默认当前用户
	private String getZhibr(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String zhibr="";
		String zhi="否";
		String sql="select zhi from xitxxb where mingc='月报管理制表人是否默认当前用户' and diancxxb_id="+visit.getDiancxxb_id();
		ResultSet rs=con.getResultSet(sql);
		try{
		  while(rs.next()){
			  zhi=rs.getString("zhi");
		  }
		}catch(Exception e){
			System.out.println(e);
		}
		if(zhi.equals("是")){
			zhibr=visit.getRenymc();
		}	
		return zhibr;
	}
//  取得电厂参数SQL
    private String getDcParam(){
//		电厂sql
		String dcsql = "";
    	if(isParentDc(getTreeid_dc())){
    		dcsql = "and dc.fuid = " + getTreeid_dc() + "\n";
    	}else{
    		dcsql = "and f.diancxxb_id = " + getTreeid_dc() + "\n";
    	}
		return dcsql;
    }

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
	
		return getZhilreport();
	}

	private String getZhilreport() {
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		Visit visit = (Visit) getPage().getVisit();
		String sql=
			"select\n" +
			"      decode(grouping(dc.mingc),1,'总计',dc.mingc )as diancmc,\n" + 
			"      decode(grouping(dc.mingc)+grouping(gs.mingc),1,'合计',2,'',gs.mingc) as gonghdw,\n" + 
			"      decode(grouping(dc.mingc)+grouping(gs.mingc)+grouping(mk.mingc),1,'合计',2,'',3,'',mk.mingc) as meikdw,\n" + 
			"      decode(grouping(dc.mingc)+grouping(gs.mingc)+grouping(mk.mingc)+grouping(to_char(f.daohrq,'yyyy-mm-dd')),1,'合计',3,'',4,'',to_char(f.daohrq,'yyyy-mm-dd')) as daohrq,\n" + 
			"      pz.mingc as pinz,cz.mingc as faz,f.chec as chec,\n" + 
			"      sum(f.ches) as ches,\n" + 
			"      sum(f.jingz ) as jingz,\n" + 
			"      sum(f.biaoz) as biaoz,\n" + 
			"    decode(grouping(dc.mingc)+grouping(gs.mingc)+grouping(mk.mingc)+grouping(to_char(f.daohrq,'yyyy-mm-dd'))+grouping(zl.id),2,'小计',3,'合计',4,'',5,'',GetHuaybmFromZhilId(zl.id))as bianm,\n" + 
			"     to_char(zl.huaysj,'yyyy-mm-dd') as huaysj,\n" + 
			"     decode(sum(f.jingz),0,0,round_new(sum(zl.qnet_ar*f.jingz)/sum(f.jingz),2)) as qnet_ar,\n" + 
			"	  round_new(decode(sum(f.biaoz),0,0,sum(f.biaoz*qnet_ar/0.0041816)/sum(f.biaoz)),0) qnet_ar_k,\n" +
			"     decode(sum(f.jingz),0,0,round_new(sum(zl.mt*f.jingz)/sum(f.jingz),2)) as mt,\n" + 
			"     decode(sum(f.jingz),0,0,round_new(sum(zl. mad*f.jingz)/sum(f.jingz),2))  as mad,\n" + 
			"     decode(sum(f.jingz),0,0,round_new(sum(zl.aad*f.jingz)/sum(f.jingz),2))as aad,\n" + 
			"     decode(sum(f.jingz),0,0,round_new(sum(zl.vad*f.jingz)/sum(f.jingz),2)) as vad,\n" + 
			"     decode(sum(f.jingz),0,0,round_new(sum(zl.vdaf*f.jingz)/sum(f.jingz),2))as vdaf,\n" + 
			"     decode(sum(f.jingz),0,0,round_new(sum(zl.stad*f.jingz)/sum(f.jingz),2))as stad,\n" + 
			"     decode(sum(f.jingz),0,0,round_new(sum(zl.std*f.jingz)/sum(f.jingz),2))as std,\n" + 
			"     decode(sum(f.jingz),0,0,round_new(sum(zl.had*f.jingz)/sum(f.jingz),2)) as had,\n" + 
			"     decode(sum(f.jingz),0,0,round_new(sum(zl.qbad*f.jingz)/sum(f.jingz),2)) as qbad,\n" + 
			"     decode(sum(f.jingz),0,0,round_new(sum(zl.qgrad*f.jingz)/sum(f.jingz),2)) as qgrad,\n" + 
			"     gethuayy(zl.id) as huayy,\n" + 
			"decode(grouping(zl.id),1,null,getHtmlAlert('"+MainGlobal.getHomeContext(this)+"', 'Chehxxz', 'id',max(zl.id), '车号信息')) as chehxx,\n" + 
			"decode(grouping(zl.id),1,null,getHtmlAlert('"+MainGlobal.getHomeContext(this)+"', 'Huayd', 'zhilb_id',max(f.zhilb_id), '化验单')) as huayd,\n" +
			"decode(grouping(zl.id),1,null,getHtmlAlert('"+MainGlobal.getHomeContext(this)+"', 'Duibcx', 'bianm',max(getHuaybh4zl(f.zhilb_id)), '对比查询')) as duibcx\n"+
			" from  fahb f,zhilb zl ,diancxxb dc ,meikxxb mk,gongysb gs,pinzb pz,chezxxb cz\n" + 
			"    where  f.zhilb_id =zl.id\n" + 
			"      and f.gongysb_id=gs.id and f.meikxxb_id=mk.id\n" + 
			"      and f.pinzb_id=pz.id and f.faz_id=cz.id\n" + 
			"      and f.diancxxb_id=dc.id\n" + 
			getDateParam() + getGysParam() + getPinzParam() + getDcParam() + getYunsfsParam() +
			"  group by rollup (dc.mingc ,gs.mingc,mk.mingc,to_char(f.daohrq ,'yyyy-mm-dd') ,pz.mingc ,cz.mingc ,f.chec, zl.id,to_char(zl.huaysj,'yyyy-mm-dd'))\n" + 
			"  having not(grouping(to_char(f.daohrq,'yyyy-mm-dd')) ||grouping(to_char(zl.huaysj,'yyyy-mm-dd'))=1)\n" + 
			"  order by grouping(dc.mingc)desc ,dc.mingc ,grouping(gs.mingc)desc,gs.mingc ,grouping(mk.mingc)desc,mk.mingc,grouping(to_char(f.daohrq ,'yyyy-mm-dd'))desc ,to_char(f.daohrq ,'yyyy-mm-dd'),grouping(pz.mingc)desc,pz.mingc,\n" + 
			"  grouping(cz.mingc)desc,cz.mingc,grouping(f.chec),f.chec,grouping(zl.id)desc,zl.id,grouping(to_char(zl.huaysj,'yyyy-mm-dd'))desc,to_char(zl.huaysj,'yyyy-mm-dd')";
		
		ResultSetList rstmp = con.getResultSetList(sql);
		ResultSetList rs = null;
//		System.out.println(sql);
		String[][] ArrHeader = new String[1][28];
		String[] strFormat=null;
		int[] ArrWidth=null;
		int aw=0;
		String [] Zidm=null;
		StringBuffer sb=new StringBuffer();
		sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='"+(REPORTNAME_ZHILBB)+"' order by xuh");
		ResultSetList rsl=con.getResultSetList(sb.toString());
        if(rsl.getRows()>0){
        	ArrWidth=new int[rsl.getRows()];
        	strFormat=new String[rsl.getRows()];
        	String biaot=rsl.getString(0,1);
        	String [] Arrbt=biaot.split("!@");
        	ArrHeader =new String [Arrbt.length][rsl.getRows()];
        	Zidm=new String[rsl.getRows()];
        	rs = new ResultSetList();
        	while(rsl.next()){
        		Zidm[rsl.getRow()]=rsl.getString("zidm");
        		ArrWidth[rsl.getRow()]=rsl.getInt("kuand");
        		strFormat[rsl.getRow()]=rsl.getString("format")==null?"":rsl.getString("format");
        		String[] title=rsl.getString("biaot").split("!@");
        		for(int i=0;i<title.length;i++){
        			ArrHeader[i][rsl.getRow()]=title[i];
        		}
        	}
        	rs.setColumnNames(Zidm);
        	while(rstmp.next()){
        		rs.getResultSetlist().add(rstmp.getArrString(Zidm));
        	}
        	rstmp.close();
        	rsl.close();
        	rsl=con.getResultSetList("select biaot from baobpzb where guanjz='"+(REPORTNAME_ZHILBB)+"'");
        	String Htitle="煤  质  检  验  台  帐" ;
        	while(rsl.next()){
        		Htitle=rsl.getString("biaot");
        	}
        	aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
    		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
        	rt.setTitle(Htitle, ArrWidth);
        	rsl.close();
        }else{
        	rs = rstmp;
			ArrHeader[0]=new String[] {"电厂名称","供货单位","煤矿单位","到货日期","品种","发站","车次","车数","净重","票重","化验编码","化验时间",
			   "收到基<br>低位发<br>热量(KJ/Kg)<br>Qnet,ar","收到<br>基<br>低位<br>热值<br>(Kcal<br>/Kg)","全水<br>分<br>(%)Mt","空气<br>干燥<br>基水<br>分<br>(%)Mad",
				"空气<br>干燥<br>基灰<br>分<br>(%)Aad", "空气干<br>燥基挥<br>发分<br>(%)Vad","干燥无<br>灰基挥<br>发分<br>(%)Vdaf",
				"空气<br>干燥<br>基硫<br>(%)<br>St,ad","干燥<br>基全<br>硫(%)<br>St,d","空气<br>干燥<br>基氢<br>(%)<br>Had",
				"弹筒发<br>热量<br>(J/g)<br>Qb,ad","空气干燥基<br>高位热值Qgr,ad<br>(KJ/Kg)<br>Qgr,ad","化验人员","车号信息","化验单","对比查询"};
			
			ArrWidth=new int[] {100,100,100,90,45,45,45,45,45,45,85,85,85,45,45,45,45,45,45,45,45,45,45,60,80,60,50,50};
			
	
			aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
			rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
			rt.setTitle("质量报表", ArrWidth);
        }
		rt.title.setRowHeight(1, 40);
		rt.title.setRowCells(1, Table.PER_FONTSIZE, 20);
		rt.title.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitle(1, 2, "制表单位:"+getZhibdwmc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(10, 5, "查询日期:" + getBRiq() + "至" + getERiq(),Table.ALIGN_CENTER);
		rt.setDefaultTitle(26, 2, "单位:吨", Table.ALIGN_RIGHT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(rs, 1, 0, 4));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(rt.PAPER_COLROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.mergeFixedCols();
//		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 28; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "打印日期:"+ DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
		rt.setDefautlFooter(9, 6, "审核:", Table.ALIGN_CENTER);
		rt.setDefautlFooter(26, 2, "制表:"+getZhibr(), Table.ALIGN_RIGHT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);
	//	rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(21);
		RPTInit.getInsertSql(visit.getDiancxxb_id(),sql,rt,"质量台帐",""+REPORTNAME_ZHILBB);
		return rt.getAllPagesHtml();

	}

	

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("查询日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getBRiq());
		df.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("至:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getERiq());
		df1.Binding("ERiq", "");// 与html页中的id绑定,并自动刷新
		df1.setId("after");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
//		电厂树
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(getDcMingc(getTreeid_dc()));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		tb1.addText(new ToolbarText("-"));
//		供应商树
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gongys_win,"gongysTree"
				,""+visit.getDiancxxb_id(),"forms[0]",getTreeid(),getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("gongysTree_text");
		tf.setWidth(100);
		tf.setValue(getGys(getTreeid())[0]);
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
//		品种选项
		tb1.addText(new ToolbarText("品种:"));
		ComboBox pinz = new ComboBox();
		pinz.setTransform("PinzSelect");
		pinz.setWidth(80);
		pinz.setListeners("select:function(own,rec,index){Ext.getDom('PinzSelect').selectedIndex=index}");
		tb1.addField(pinz);
		tb1.addText(new ToolbarText("-"));
		//运输方式
		tb1.addText(new ToolbarText("运输方式:"));
		ComboBox yunsfs = new ComboBox();
		yunsfs.setTransform("YunsfsSelect");
		yunsfs.setWidth(80);
		yunsfs.setListeners("select:function(own,rec,index){Ext.getDom('YunsfsSelect').selectedIndex=index}");
		tb1.addField(yunsfs);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null, "查询",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);
		tb.setIcon(SysConstant.Btn_Icon_Print);
		setToolbar(tb1);

	}

	
	private String getZhibdwmc(){
		Visit visit = (Visit)getPage().getVisit();
		String danwmc = "";
		String sql = "select quanc from diancxxb where id="+visit.getDiancxxb_id();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			danwmc = rsl.getString("quanc");
		}
		return danwmc;
	}
	
//	电厂名称
	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
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
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}
	
//	工具栏使用的方法
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
//			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
			treeid="0";
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("gongysTree_text")).setValue
				(getGys(getTreeid())[0]);
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
//	-------------------------电厂Tree-----------------------------------------------------------------
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
	
//	-------------------------电厂Tree END-------------------------------------------------------------
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	public String getcontext() {
		return "";
		// return "var context='http://"
		// + this.getRequestCycle().getRequestContext().getServerName()
		// + ":"
		// + this.getRequestCycle().getRequestContext().getServerPort()
		// + ((Visit) getPage().getVisit()).get() + "';";
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

	

	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			setPinzValue(null);
			setPinzModel(null);
			setYunsfsValue(null);
			setYunsfsModel(null);
			setTreeid_dc(visit.getDiancxxb_id() + "");
			
			//begin方法里进行初始化设置
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);保存传递的非默认纸张的样式
		}
		blnIsBegin = true;
		getSelectData();

	}

}
