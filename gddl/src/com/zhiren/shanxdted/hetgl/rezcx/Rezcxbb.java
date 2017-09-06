package com.zhiren.shanxdted.hetgl.rezcx;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
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
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Rezcxbb extends BasePage implements PageValidateListener {

	private static final String BAOBPZB_GUANJZ = "RECZ_GJZ_ED";// baobpzb中对应的关键字
//	界面用户提示
	private String msg="";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO 自动生成方法存根
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
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


    private String getDate(String date){
    	
    	DateFormat df=null;
    	
    	if(date.indexOf("-")!=-1){
    		df=new SimpleDateFormat("yyyy-MM-dd");
    	}else{
    		df=new SimpleDateFormat("yyyyMMdd");
    	}
    	Date d=null;
    	try {
			d=df.parse(date);
		} catch (ParseException e) {
			// TODO 自动生成 catch 块
			d=new Date();
			e.printStackTrace();
		}
    	return DateUtil.FormatOracleDate(d);
    }
//	获取相关的SQL

	public StringBuffer getBaseSql() {
    	Visit visit = (Visit) this.getPage().getVisit();

	
		StringBuffer sb = new StringBuffer();
		
		sb.append(" select  xx.ysdw, xx.mk,\n");
		
		
		sb.append(" (select sum(round_new(f.laimsl,1)) from fahb f" +
				" where f.diancxxb_id="+this.getDcid()+" " +
				"and f.daohrq="+this.getDate(this.getNodeid())+" and f.yunsfsb_id=2 and\n");
		sb.append("  f.meikxxb_id=xx.mkid \n");
		sb.append("  and f.id in (select c.fahb_id from chepb c where c.yunsdwb_id=xx.ysid)\n");
		sb.append("  ) dangrjml,\n");
		
		
		sb.append(" ( select  decode(sum(round_new(f.laimsl,2)),0,0," +
				" round_new(   round_new ( sum(round_new(z.qnet_ar,2) * round_new(f.laimsl,2)) /\n");
		sb.append("   sum(round_new(f.laimsl,2)),2)* 1000 / 4.1816 , 0) )\n");
		sb.append(" from zhilb z,fahb f where z.id=f.zhilb_id and f.yunsfsb_id=2 and  f.diancxxb_id="+this.getDcid()+" " +
				"and f.daohrq="+this.getDate(this.getNodeid())+" and\n");
		sb.append(" f.meikxxb_id=xx.mkid  and f.id in (select c.fahb_id from chepb c where c.yunsdwb_id=xx.ysid)  )\n");
		sb.append("    dangrqbar,\n");
		
		
		

		sb.append(" (select sum(round_new(f.laimsl,1)) from fahb f" +
				" where f.diancxxb_id="+this.getDcid()+" " +
				"and f.daohrq>="+this.getDate(this.getKaissj())+" " +
						"and f.daohrq<="+this.getDate(this.getNodeid())+"  and\n");
		sb.append("  f.meikxxb_id=xx.mkid and f.yunsfsb_id=2 \n");
		sb.append("  and f.id in (select c.fahb_id from chepb c where c.yunsdwb_id=xx.ysid)\n");
		sb.append("  ) leijjml,\n");
		
		
		sb.append(" ( select  decode(sum(round_new(f.laimsl,2)),0,0," +
		" round_new(  round_new (sum(round_new(z.qnet_ar,2) * round_new(f.laimsl,2)) /\n");
		sb.append("   sum(round_new(f.laimsl,2)),2)* 1000 / 4.1816, 0) )\n");
		sb.append(" from zhilb z,fahb f where z.id=f.zhilb_id and f.yunsfsb_id=2  and  f.diancxxb_id="+this.getDcid()+" " +
		"and f.daohrq>="+this.getDate(this.getKaissj())+" " +
				"and f.daohrq<="+this.getDate(this.nodeid)+" and\n");
		sb.append(" f.meikxxb_id=xx.mkid  and f.id in (select c.fahb_id from chepb c where c.yunsdwb_id=xx.ysid)  )\n");
		sb.append("    leijqbar \n");

		sb.append(" from ( select  y.id ysid,y.mingc ysdw,m.id mkid,m.mingc mk\n");
		sb.append(" from (select f.* from fahb f where f.daohrq="+this.getDate(this.getNodeid())+" " +
				" and f.yunsfsb_id=2 and f.diancxxb_id="+this.getDcid()+") fh,\n");
		sb.append(" chepb c,yunsdwb y,meikxxb m \n");
		sb.append(" where c.fahb_id=fh.id and fh.meikxxb_id=m.id and c.yunsdwb_id=y.id\n");
		sb.append(" group by y.id, y.mingc,m.id ,m.mingc \n");
		sb.append(" order by y.mingc,m.mingc \n");
		sb.append(" ) xx\n");

		
		return sb;
    }
	
	private String getBiaotdate(String date){
		
		DateFormat df=null;
    	
    	if(date.indexOf("-")!=-1){
    		df=new SimpleDateFormat("yyyy-MM-dd");
    	}else{
    		df=new SimpleDateFormat("yyyyMMdd");
    	}
    	Date d=null;
    	try {
			d=df.parse(date);
		} catch (ParseException e) {
			// TODO 自动生成 catch 块
			d=new Date();
			e.printStackTrace();
		}
		
		return DateUtil.Formatdate("yyyy年MM月dd日",d);
	}
//	获取表表标题
	public String getRptTitle() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sb=""+this.getBiaotdate(this.getKaissj())+"至"+this.getBiaotdate(this.getJiessj())+"合同段"
					+"<br>"+this.getBiaotdate(this.getNodeid())+"煤量热值公告";
		return sb;
	}
	
	

	public String getPrintTable(){
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		
		
		String sql="";
			sql=this.getBaseSql().toString();

//		System.out.println(sql);
		ResultSetList rstmp = con.getResultSetList(sql);
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;
		String [] Zidm=null;
		StringBuffer sb=new StringBuffer();
//		sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='"+v.getInt1()+"' order by xuh");
		sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='"+BAOBPZB_GUANJZ+v.getInt1()+"' order by xuh");
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
        	rsl=con.getResultSetList("select biaot from baobpzb where guanjz='"+BAOBPZB_GUANJZ+"'");
        	String Htitle=getRptTitle() ;
        	while(rsl.next()){
        		Htitle=rsl.getString("biaot");
        	}
        	rt.setTitle(Htitle, ArrWidth);
        	rsl.close();
        }else{
        	rs = rstmp;
        	 ArrHeader = new String[][] {{"运输单位","矿名","当日进煤量","当日热值","累计进煤量","加权热值"} };
    
    		 ArrWidth = new int[] {120,120,100,100,100,100 };
    
    		rt.setTitle(getRptTitle(), ArrWidth);
    		rt.title.fontSize=10;
    		rt.title.setRowHeight(2, 50);
    		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
    		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
    		
    		strFormat = new String[] { "", "", "", "", "", "" };
    		
        }
        rt.setTitle(getRptTitle(), ArrWidth);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);

//
		strFormat = new String[] { "", "", "", "", "", "" };
//
		rt.setBody(new Table(rs, 1, 0, 3));
		
		rt.body.setRowHeight(30);
		rt.body.setFontSize(14);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_RIGHT);
		rt.body.setColAlign(12, Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColFormat(strFormat);
		rt.body.setPageRows(40);
//		rt.body.mergeFixedCols();
//		rt.body.mergeFixedRow();
		
	

		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
	//	RPTInit.getInsertSql(v.getDiancxxb_id(),getBaseSqlStr().toString(),rt,getRptTitle(),""+BAOBPZB_GUANJZ+v.getInt1());
     	return rt.getAllPagesHtml();// ph;
	}



	private String nodeid;
	private String dcid;

	
	public String getDcid() {
	return dcid;
	}

	public void setDcid(String dcid) {
		this.dcid = dcid;
	}

	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}
	
	private String kaissj;
	private String jiessj;
	

	public String getJiessj() {
		return jiessj;
	}

	public void setJiessj(String jiessj) {
		this.jiessj = jiessj;
	}

	public String getKaissj() {
		return kaissj;
	}

	public void setKaissj(String kaissj) {
		this.kaissj = kaissj;
	}

	//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
//		Visit visit = (Visit) getPage().getVisit();
		
		this.nodeid=cycle.getRequestContext().getRequest().getParameter("lx");
		this.dcid=cycle.getRequestContext().getRequest().getParameter("ly");
		
		if(this.nodeid==null || this.nodeid.equals("")){
			nodeid=DateUtil.Formatdate("yyyyMMdd", new Date());
		}
		
		String shij=cycle.getRequestContext().getRequest().getParameter("lc");
		
		if(shij!=null && !shij.equals("")){
			
			String[] res=shij.split(",");
			
			this.kaissj=res[0];
			this.jiessj=res[1];
		}else{
			this.kaissj=DateUtil.Formatdate("yyyy-MM-dd", new Date());
			this.jiessj=DateUtil.Formatdate("yyyy-MM-dd", new Date());
		}
		
	}
	

	
//	表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		
	}
//	页面登陆验证
	public void pageValidate(PageEvent arg0) {
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}
}
