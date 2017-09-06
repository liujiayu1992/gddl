package com.zhiren.gs.renwcx;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
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
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * 作者:tzf
 * 时间:2010-01-22
 * 内容:完成 待办工作任务列表
 */
public class Renwcx extends BasePage implements PageValidateListener {

	private final static String Yiwc="已导入";
	private final static String Weiwc="未导入";
	private final static String Weish="未审核";
	private final static String Wei="未";
	
	
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
	
//	绑定日期  当月的第一天
	private boolean briqboo=false;
	private String briq;

	public String getBRiq() {
		
		if(this.briq==null || this.briq.equals("")){
			return DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(new Date()));
		}
		return briq;
	}

	public void setBRiq(String briq) {
		if(this.briq!=null && !this.briq.equals(briq)){
			briqboo=true;
		}
		this.briq = briq;
	}
	
	
//	绑定日期
	private boolean eriqboo=false;
	private String eriq;

	public String getERiq() {
		
		if(this.eriq==null || this.eriq.equals("")){
			return DateUtil.FormatDate(new Date());
		}
		return eriq;
	}

	public void setERiq(String eriq) {
		if(this.eriq!=null && !this.eriq.equals(eriq)){
			eriqboo=true;
		}
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
    
    
//  判断电厂Tree中所选电厂时候还有子电厂   
    private ResultSetList hasDianc(JDBCcon con,String id){ 
		
		String sql="select id, mingc from diancxxb where fuid = " + id +" order by xuh asc ";
		ResultSetList rsl=con.getResultSetList(sql);
		
		if(rsl.getRows()>0){//说明是 父电厂 或者公司级
			return rsl;
		}
		
		sql="select id, mingc from diancxxb where id = " + id ;
		rsl=con.getResultSetList(sql);
		return rsl;
	}
    
//	获取表表标题
	public String getRptTitle(String dcmc) {
		
		return dcmc+"待办工作列表";
		
	}
	
	
	
//	刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con=new JDBCcon();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("guohrqb");
		tb1.addField(dfb);
	
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("至:"));
		DateField dfb2 = new DateField();
		dfb2.setValue(getERiq());
		dfb2.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
		dfb2.setId("guohrqb2");
		tb1.addField(dfb2);
	
		tb1.addText(new ToolbarText("-"));
		
//		电厂Tree
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "forms[0]", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(130);
		tf1.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
						: getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		
		tb1.addText(new ToolbarText("-"));
		
	//-------------------------------------------------------	

		
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		
		tb1.addFill();
		setToolbar(tb1);
	}
	

	
	public String getPrintTable(){
		JDBCcon con = new JDBCcon();
		String html = "";
		if(this.getTreeid_dc().equals( ((Visit)this.getPage().getVisit()).getDiancxxb_id()+"")){
			html = getTableHJ(con);
		}else{
			html = getTableSZ(con)+getTableHJ(con);
		}
		con.Close();
		return html;
		
	}
	
	private ResultSetList getDcMingc(JDBCcon con,String diancxxb_id){
		String sql=" select nvl(z.jiekzhb_id,-1) id,d.id dcid, d.mingc from diancxxb d,dianczhb z where d.id=z.diancxxb_id(+) and  d.id = " + diancxxb_id +" order by xuh asc  ";
		
		ResultSetList rsl=con.getResultSetList(sql);
		
		return rsl;
	}
	
	//得到报表的数量 和质量
	private String getTableSZ(JDBCcon con){
		
		
		Report rt = new Report();
		String[][] ArrHeader = new String[][]{{"项目:数量和质量","项目:数量和质量","项目:数量和质量","项目:数量和质量","项目:数量和质量","项目:数量和质量"},
				{"日期","来煤","入厂质量","入炉质量","收耗存","日生产数据"}};
		
		int[] ArrWidth = new int[]{150,100,100,100,100,100};
		
		ResultSetList rsl=this.getDcMingc(con, this.getTreeid_dc());
//		Table table=new Table(rsl.getRows()+ArrHeader.length,6);
		Table table=new Table(0+ArrHeader.length,6);
		rt.setBody(table);
		
//		String brq=DateUtil.FormatOracleDate(this.getBRiq());
		
		int start_row_index=3;
		int start_col_index=1;
		
		
		String diancxxb_id="";
		String dcid="";
		String dcmc="";
		if(rsl.next()){
			diancxxb_id=rsl.getString("id");
			dcid=rsl.getString("dcid");
			dcmc=rsl.getString("mingc");
		}
			
		
			int day_count=daysOfTwo(DateUtil.getDate(this.getBRiq()),DateUtil.getDate(this.getERiq()));

			for(int i=0;i<=day_count;i++){
				
				String brq=DateUtil.FormatOracleDate(DateUtil.AddDate(DateUtil.getDate(this.getBRiq()+" 00:00:00"), i, DateUtil.AddType_intDay));
				String sql=this.getLZSQL(diancxxb_id, dcid, brq, brq);
				
				rsl=con.getResultSetList(sql);
				
				if(rsl.next()){
					
					String riq=rsl.getString("riq");
					String fah=rsl.getString("fah");
					String zhil=rsl.getString("zhil");
					String rulmzl=rsl.getString("rulmzl");
					String shouhcrb=rsl.getString("shouhcrb");
					String riscsj=rsl.getString("riscsj");
					
					if( (fah!=null && !fah.equals("")) || (zhil!=null && !zhil.equals("")) || (rulmzl!=null && !rulmzl.equals("")) 
							|| (shouhcrb!=null && !shouhcrb.equals("")) || (riscsj!=null && !riscsj.equals("")) ){
						
						table.AddTableRow(1);
						
						table.setCellValue(start_row_index, start_col_index, riq);
						table.getCell(start_row_index, start_col_index).align=Table.ALIGN_CENTER;
						
						table.setCellValue(start_row_index, start_col_index+1, fah);
						table.getCell(start_row_index, start_col_index+1).align=Table.ALIGN_CENTER;
						if(fah.indexOf(Wei)!=-1){
						table.getCell(start_row_index, start_col_index+1).fontBold=true;
						table.getCell(start_row_index, start_col_index+1).foreColor="red";
						}
						
						table.setCellValue(start_row_index, start_col_index+2, zhil);
						table.getCell(start_row_index, start_col_index+2).align=Table.ALIGN_CENTER;
						if(zhil.indexOf(Wei)!=-1){
							table.getCell(start_row_index, start_col_index+2).fontBold=true;
							table.getCell(start_row_index, start_col_index+2).foreColor="red";
						}
						
						table.setCellValue(start_row_index, start_col_index+3, rulmzl);
						table.getCell(start_row_index, start_col_index+3).align=Table.ALIGN_CENTER;
						if(rulmzl.indexOf(Wei)!=-1){
							table.getCell(start_row_index, start_col_index+3).fontBold=true;
							table.getCell(start_row_index, start_col_index+3).foreColor="red";
						}
						
						table.setCellValue(start_row_index, start_col_index+4, shouhcrb);
						table.getCell(start_row_index, start_col_index+4).align=Table.ALIGN_CENTER;
						if(shouhcrb.indexOf(Wei)!=-1){
							table.getCell(start_row_index, start_col_index+4).fontBold=true;
							table.getCell(start_row_index, start_col_index+4).foreColor="red";
						}
						
						table.setCellValue(start_row_index, start_col_index+5, riscsj);
						table.getCell(start_row_index, start_col_index+5).align=Table.ALIGN_CENTER;
						if(riscsj.indexOf(Wei)!=-1){
							table.getCell(start_row_index, start_col_index+5).fontBold=true;
							table.getCell(start_row_index, start_col_index+5).foreColor="red";
						}
						
						
						start_row_index++;
					}
			}
		}
			
		
		
		rt.body.merge(1, 1, 1, 6);
		rt.body.setHeaderData(ArrHeader);
		rt.setTitle(this.getRptTitle(dcmc), ArrWidth);
		rt.title.fontSize=12;
		rt.title.setRowHeight(2, 50);
		rt.setDefaultTitleCenter(this.getBRiq()+"至"+this.getERiq());
		
		rt.body.setFontSize(10);
		rt.body.setRowHeight(25);
		rt.body.setWidth(ArrWidth);
		
		
		return rt.getAllPagesHtml();
	}
	
	
	private String getLZSQL(String diancxxb_id,String dcid,String brq,String qrq){
		
		StringBuffer bf=new StringBuffer();
		bf.append(" select to_char( "+brq+",'yyyy-MM-dd') riq,\n");
		
		bf.append(" decode(  (select j.renwbs from jiekjsrzb j where j.renw='fahb'and j.caoz=0 and to_date(to_char(shij,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq +" and diancxxb_id="+diancxxb_id +" and rownum=1 ), \n");
		bf.append(" '',\n");
		bf.append(" decode ("+dcid+","+((Visit)this.getPage().getVisit()).getDiancxxb_id()+",'/', nvl('未导入','') ),\n");
		bf.append(" decode( (select j.renwbs from jiekjsrzb j where j.renw='fahb'and j.caoz=0 and j.zhixzt=-1 and to_date(to_char(shij,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq +" and diancxxb_id="+diancxxb_id+
				" and j.renwbs not in (select j.renwbs from jiekjsrzb j where j.renw='fahb' and j.caoz=0 and j.zhixzt=1 and to_date(to_char(shij,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq+" and diancxxb_id="+diancxxb_id+") and rownum=1),'','已导入' ,'未完全导入' " +
				") " +
				")  " //为空 说明有些发货数据没有导入成功
				);
		
		bf.append(" fah, \n");
		
		bf.append(" decode(  (select j.renwbs from jiekjsrzb j where j.renw='zhilb'and j.caoz=0 and to_date(to_char(shij,'yyyy-MM-dd'),'yyyy-MM-dd')="+qrq +" and diancxxb_id="+diancxxb_id +" and rownum=1 ), \n");
		bf.append(" '',\n");
		bf.append(" decode ("+dcid+","+((Visit)this.getPage().getVisit()).getDiancxxb_id()+",'/', nvl('未导入','') ),\n");
		bf.append(" decode( (select j.renwbs from jiekjsrzb j where j.renw='zhilb'and j.caoz=0 and j.zhixzt=-1 and to_date(to_char(shij,'yyyy-MM-dd'),'yyyy-MM-dd')="+qrq +" and diancxxb_id="+diancxxb_id+
				" and j.renwbs not in (select j.renwbs from jiekjsrzb j where j.renw='zhilb' and j.caoz=0 and j.zhixzt=1 and to_date(to_char(shij,'yyyy-MM-dd'),'yyyy-MM-dd')="+qrq+" and diancxxb_id="+diancxxb_id+") and rownum=1),'','已导入' ,'未完全导入' " +
				") " +
				")  " //为空 说明有些发货数据没有导入成功
				);
		bf.append(" zhil, \n");
		
		
		bf.append(" decode(  (select j.renwbs from jiekjsrzb j where j.renw='rulmzlb'and j.caoz=0 and to_date(to_char(shij,'yyyy-MM-dd'),'yyyy-MM-dd')="+qrq +" and diancxxb_id="+diancxxb_id +" and rownum=1 ), \n");
		bf.append(" '',\n");
		bf.append(" decode ("+dcid+","+((Visit)this.getPage().getVisit()).getDiancxxb_id()+",'/', nvl('未导入','') ),\n");
		bf.append(" decode( (select j.renwbs from jiekjsrzb j where j.renw='rulmzlb'and j.caoz=0 and j.zhixzt=-1 and to_date(to_char(shij,'yyyy-MM-dd'),'yyyy-MM-dd')="+qrq +" and diancxxb_id="+diancxxb_id+
				" and j.renwbs not in (select j.renwbs from jiekjsrzb j where j.renw='rulmzlb' and j.caoz=0 and j.zhixzt=1 and to_date(to_char(shij,'yyyy-MM-dd'),'yyyy-MM-dd')="+qrq+" and diancxxb_id="+diancxxb_id+") and rownum=1),'','已导入' ,'未完全导入' " +
				") " +
				")  " //为空 说明有些发货数据没有导入成功
				);
		bf.append(" rulmzl, \n");
		
		
		
		bf.append(" decode(  (select j.renwbs from jiekjsrzb j where j.renw='shouhcrbb'and j.caoz=0 and to_date(to_char(shij,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq +" and diancxxb_id="+diancxxb_id +" and rownum=1 ), \n");
		bf.append(" '',\n");
		bf.append(" decode ("+dcid+","+((Visit)this.getPage().getVisit()).getDiancxxb_id()+",'/', nvl('未导入','') ),\n");
		bf.append(" decode( (select j.renwbs from jiekjsrzb j where j.renw='shouhcrbb'and j.caoz=0 and j.zhixzt=-1 and to_date(to_char(shij,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq +" and diancxxb_id="+diancxxb_id+
				" and j.renwbs not in (select j.renwbs from jiekjsrzb j where j.renw='shouhcrbb' and j.caoz=0 and j.zhixzt=1 and to_date(to_char(shij,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq+" and diancxxb_id="+diancxxb_id+") and rownum=1),'','已导入' ,'未完全导入' " +
				") " +
				")  " //为空 说明有些发货数据没有导入成功
				);
		bf.append(" shouhcrb, \n");
		
		
		
		bf.append(" decode(  (select j.renwbs from jiekjsrzb j where j.renw='riscsjb'and j.caoz=0 and to_date(to_char(shij,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq +" and diancxxb_id="+diancxxb_id +" and rownum=1 ), \n");
		bf.append(" '',\n");
		bf.append(" decode ("+dcid+","+((Visit)this.getPage().getVisit()).getDiancxxb_id()+",'/', nvl('未导入','') ),\n");
		bf.append(" decode( (select j.renwbs from jiekjsrzb j where j.renw='riscsjb'and j.caoz=0 and j.zhixzt=-1 and to_date(to_char(shij,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq +" and diancxxb_id="+diancxxb_id+
				" and j.renwbs not in (select j.renwbs from jiekjsrzb j where j.renw='riscsjb' and j.caoz=0 and j.zhixzt=1 and to_date(to_char(shij,'yyyy-MM-dd'),'yyyy-MM-dd')="+brq+" and diancxxb_id="+diancxxb_id+") and rownum=1),'','已导入' ,'未完全导入' " +
				") " +
				")   \n " //为空 说明有些发货数据没有导入成功
				);

	
		
		bf.append(" riscsj from dual \n");
		
		return bf.toString();
		
		
	}
	
	//计算两个日期相差天数
	public static int daysOfTwo(Date fDate, Date oDate) {

	       Calendar aCalendar = Calendar.getInstance();

	       aCalendar.setTime(fDate);

	       int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

	       aCalendar.setTime(oDate);

	       int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

	       return day2 - day1;

	 }
	
	
	private String getHJSql(String dcid){
		
		StringBuffer bf=new StringBuffer();
		bf.append(" select dcmc,leix ,leib ,bianh ,zhuangt   from ( \n");

		bf.append(" select d.xuh,d.mingc dcmc,nvl('合同','') leix,decode(h.leib,0,'电厂采购',1,'公司销售',2,'公司采购') leib, \n");
		bf.append(" h.hetbh bianh, decode(h.leib,0,'"+Weiwc+"',1,'"+Weish+"',2,'"+Weish+"')  zhuangt \n");
		bf.append(" from hetb h ,diancxxb d where h.diancxxb_id=d.id and h.liucztb_id!=1 \n");
		bf.append(" and d.id in ("+dcid+")");
	
		bf.append(" union \n");
		
		bf.append(" select distinct * from ( \n");
		bf.append(" select d.xuh,d.mingc dcmc,nvl('结算单','') leix,nvl('电厂销售','') leib,js.bianm bianh, \n");
		bf.append(" nvl('"+Weiwc+"','')  zhuangt \n");
		bf.append(" from jiesb js,diancxxb d where js.diancxxb_id=d.id and js.liucztb_id=0 \n");
		bf.append(" and d.id in ("+dcid+")");
		bf.append(" union \n");
		bf.append(" select d.xuh,d.mingc dcmc,nvl('结算单','') leix,nvl('电厂销售','') leib,jf.bianm bianh, \n");
		bf.append(" nvl('"+Weiwc+"','')  zhuangt \n");
		bf.append(" from jiesyfb jf,diancxxb d where jf.diancxxb_id=d.id and jf.liucztb_id=0 \n");
		bf.append(" and d.id in ("+dcid+")");
		bf.append(" )\n");
		
		bf.append(" union \n");
		
		bf.append(" select distinct * from ( \n");
		bf.append(" select d.xuh,d.mingc dcmc,nvl('结算单','') leix,nvl('公司采购','') leib,ks.bianm bianh, \n");
		bf.append(" nvl('"+Weish+"','') zhuangt \n");
		bf.append(" from kuangfjsmkb ks,diancxxb d where ks.diancxxb_id=d.id and ks.liucztb_id!=1 \n");
		bf.append(" and d.id in ("+dcid+")");
		bf.append(" union \n");
		bf.append(" select d.xuh,d.mingc dcmc,nvl('结算单','') leix,nvl('公司采购','') leib,kf.bianm bianh, \n");
		bf.append(" nvl('"+Weish+"','') zhuangt \n");
		bf.append(" from kuangfjsyfb kf,diancxxb d where kf.diancxxb_id=d.id and kf.liucztb_id!=1 \n");
		bf.append(" and d.id in ("+dcid+")");
		bf.append(" )\n");
		
		bf.append(" union\n");
		
		bf.append(" select distinct * from ( \n");
		bf.append(" select d.xuh,d.mingc dcmc,nvl('结算单','') leix,nvl('公司销售','') leib,ds.bianm bianh, \n");
		bf.append(" nvl('"+Weish+"','') zhuangt \n");
		bf.append(" from diancjsmkb ds,diancxxb d where ds.diancxxb_id=d.id and ds.liucztb_id!=1 \n");
		bf.append(" and d.id in ("+dcid+")");
		bf.append(" union \n");
		bf.append(" select d.xuh,d.mingc dcmc,nvl('结算单','') leix,nvl('公司销售','') leib,df.bianm bianh,\n");
		bf.append(" nvl('"+Weish+"','') zhuangt \n");
		bf.append(" from diancjsyfb df,diancxxb d where df.diancxxb_id=d.id and df.liucztb_id!=1 \n");
		bf.append(" and d.id in ("+dcid+")");
		bf.append(" ) \n");
		
		bf.append(" ) r \n");
		bf.append(" order by r.xuh asc,r.leix asc,r.leib asc,r.bianh asc,r.zhuangt asc\n");
		return bf.toString();
		
	}
	
//	得到报表的合同 和结算
	private String getTableHJ(JDBCcon con){
		
		
		Report rt = new Report();
		String[][] ArrHeader = new String[][]{{"项目:合同和结算","项目:合同和结算","项目:合同和结算","项目:合同和结算","项目:合同和结算"},
				{"单位","类型","类别","编号","导入/审核"}};
		
		int[] ArrWidth = new int[]{150,100,100,200,100};
		
		ResultSetList rsl=con.getResultSetList(" select * from diancxxb where id="+this.getTreeid_dc() 
				);
		
		String dcid="";
		String dcmc="";
		while(rsl.next()){
			dcid+=rsl.getString("id")+",";
			dcmc=rsl.getString("mingc");
		}
		dcid=dcid.substring(0, dcid.lastIndexOf(","));
		
		rsl=con.getResultSetList(this.getHJSql(dcid));
		rt.setBody(new Table(rsl, 2, 0, 3));
		
		
		for(int i=2;i<rt.body.getRows();i++){
			
				rt.body.getCell(i+1, 5).fontBold=true;
				rt.body.getCell(i+1, 5).foreColor="red";
		}
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		
		rt.body.setHeaderData(ArrHeader);
		rt.setTitle(this.getRptTitle(dcmc), ArrWidth);
		rt.title.fontSize=12;
		rt.title.setRowHeight(2, 50);
		rt.setDefaultTitleCenter(this.getBRiq()+"至"+this.getERiq());
		
		rt.body.setFontSize(10);
		rt.body.setRowHeight(25);
		rt.body.setWidth(ArrWidth);
		
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		
		return rt.getAllPagesHtml();
	}
	

//	工具栏使用的方法

	
//	-------------------------电厂Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}
	
	
	private boolean  dcidboo=false;
	
	public void setTreeid_dc(String treeid) {
		if(((Visit) getPage().getVisit()).getString3()!=null && !((Visit) getPage().getVisit()).getString3().equals(treeid)){
			dcidboo=true;
		}
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
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getToolbar().getRenderScript();
	}
	
	private  String dcid_lx="";
	
	public String getDcid_lx() {
		return dcid_lx;
	}

	public void setDcid_lx(String dcid_lx) {
		this.dcid_lx = dcid_lx;
	}

	//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		
		String dcid=cycle.getRequestContext().getRequest().getParameter("lx");
		
		if(dcid!=null && !dcid.equals("")){
			this.setDcid_lx(dcid);
			setBRiq(null);
			setERiq(null);
			setTreeid_dc(this.getDcid_lx());
			
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setBRiq(null);
			setERiq(null);
		
			getDiancmcModels();
			
			if(this.getDcid_lx()==null || this.getDcid_lx().equals("")){
				this.setDcid_lx(visit.getDiancxxb_id()+"");
			}
			setTreeid_dc(this.getDcid_lx());
			
			

			getSelectData();
		}
		
		
	}
	
	//得到昨天的日期
	private String getYestodayStr(Date date){
		
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		
		cal.add(Calendar.DATE, -1);
		
		return DateUtil.FormatDate(cal.getTime());
	}
//	按钮的监听事件
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	
//	表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		Visit visit=(Visit)this.getPage().getVisit();
		if (_RefurbishChick) {
			_RefurbishChick = false;
			
		}
		getSelectData();
//		System.out.println(this.getTreeid());
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
