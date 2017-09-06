package com.zhiren.dc.gdxw.laimtj;

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
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Laimtj_rz extends BasePage {

	
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

    

	
//	刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("来煤日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "forms[0]");// 与html页中的id绑定,并自动刷新
		dfb.setId("guohrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("至:"));
		DateField df = new DateField();
		df.setValue(getERiq());
		df.Binding("ERIQ", "forms[0]");// 与html页中的id绑定,并自动刷新
		df.setId("eriq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		
		
//	    
		tb1.addText(new ToolbarText("统计口径:"));
		ComboBox Kouj = new ComboBox();
		Kouj.setTransform("KOUD");
		Kouj.setWidth(150);
		Kouj.setListeners("select:function(){document.forms[0].submit();}");
		Kouj.setEditable(true);
		tb1.addField(Kouj);
		tb1.addText(new ToolbarText("-"));
		
		
		
		
		
//	    
		tb1.addText(new ToolbarText("打印选择"));
		ComboBox dayin = new ComboBox();
		dayin.setTransform("MEIK");
		dayin.setWidth(100);
		dayin.setListeners("select:function(){document.forms[0].submit();}");
		dayin.setEditable(true);
		tb1.addField(dayin);
		tb1.addText(new ToolbarText("-"));
		

		
		
//		电厂Tree
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
						: getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		
		
		

		
		//如果是一厂多制就显示电厂树,如果不是就不显示电厂
		if(visit.isFencb()){
			tb1.addText(new ToolbarText("电厂:"));
			tb1.addField(tf1);
			tb1.addItem(tb3);
			tb1.addText(new ToolbarText("-"));
		}
		
		
		
		
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		

		
		
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	
	public String getPrintTable(){
		
		if(this.getKoudValue().getValue().equals("统计片区")){
			return GetPianq();
		}else if(this.getKoudValue().getValue().equals("统计井口")){
			return getJingk();
		}else if(this.getKoudValue().getValue().equals("统计煤矿")){
			return getMeikdq();
		}else if(this.getKoudValue().getValue().equals("统计煤矿,井口")){
			return getMeikdq_meik();
		}else if(this.getKoudValue().getValue().equals("统计片区,井口")){
			return GetPianq_meik();
		}else if(this.getKoudValue().getValue().equals("统计片区,煤矿")){
			return GetPianq_meikdq();
		}else{
			return "无此报表";
		}
			
		
	}
	
	
	
	//统计井口
	public String getJingk(){
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		ResultSetList rs=null;
		String[] strFormat=null;
		sbsql.setLength(0);
		Report rt = new Report(); //报表定义
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
		
		
		String mk_str=" ";
		long mk_id=this.getMeikValue().getId();
		if(mk_id==-1){
			mk_str=" ";
		}else{
			mk_str=" and pq.fenz_id="+mk_id+"\n";
		}
		

			sbsql.append(


					"select\n" +
					"decode(m.mingc,null,'合计',m.mingc) as meik ,\n" + 
					"sum(f.laimsl) as laim,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.had*f.laimsl)/sum(f.laimsl),2) ) as had,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.mt*f.laimsl)/sum(f.laimsl),1) )as mt,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.mad*f.laimsl)/sum(f.laimsl),2)) as mad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum((100*(z.mt-z.mad)/(100-z.mad))*f.laimsl)/sum(f.laimsl),2)) as neis,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.aad*f.laimsl)/sum(f.laimsl),2)) as aad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.ad*f.laimsl)/sum(f.laimsl),2)) as ad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.vad*f.laimsl)/sum(f.laimsl),2) )as vad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.stad*f.laimsl)/sum(f.laimsl),2)) as stad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.qbad*f.laimsl)/sum(f.laimsl),2) )as qbad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.qgrad*f.laimsl)/sum(f.laimsl),2)) as qgrd,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.qnet_ar*f.laimsl)/sum(f.laimsl),2)) as qnet_ar\n" + 
					"from fahb f ,zhilb z ,meikxxb m,\n" + 
					"meikdqb_xw dq,meikdqglb gl,meikpqb pq\n" + 
					"where f.meikxxb_id=m.id\n" + 
					"and m.id=gl.meikxxb_id\n" + 
					"and dq.id=gl.meikdqb_xw_id\n" + 
					"and dq.meikpqb_id=pq.id\n" + 
					""+mk_str+"\n"+
					"and f.daohrq>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
					"and f.daohrq<=to_date('"+jies+"','yyyy-mm-dd')\n" + 
					"and f.zhilb_id=z.id\n" + 
					"group by rollup (m.mingc)\n" + 
					"order by m.mingc"


);
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[1][13];
//				1120
				ArrHeader[0]=new String[] {"井口","煤量","氢","全水","内水","外水","空干灰","干灰","挥发份","全硫","弹筒热","高位热","低位热"};
				//ArrHeader[1]=new String[] {"煤矿地区","名称","车数","吨数","车数","吨数","车数","吨数"};
				int ArrWidth[]=new int[] {130,80,50,50,50,50,50,50,50,50,50,50,50};
				  strFormat = new String[] { "", "0.00","0.00", "0.0","0.00","0.00" , "0.00","0.00","0.00" , "0.00","0.00","0.00" , "0.00"};
			
				// 数据
				rt.setTitle("入厂煤质量统计台账",ArrWidth);
				rt.setDefaultTitle(1, 3, "来煤日期:"+kais, Table.ALIGN_LEFT);
				
				
				rt.setBody(new Table(rs, 1, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(3000);
				
			
				
//				设定小计行的背景色和字体
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 1);
					if((xiaoj.equals("合计"))){
						for (int j=0;j<rt.body.getCols()+1;j++){
							rt.body.getCell(i, j).backColor="silver";
							rt.body.getCell(i, j).fontBold=true;
						}
					}
				}
				
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// 表头数据
				rt.body.ShowZero = true;
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				rt.body.mergeFixedCols();
				rt.body.mergeFixedRow();
				rt.createFooter(1, ArrWidth);
				//rt.setDefautlFooter(1, 3, "打印日期："+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setRowHeight(25);
				con.Close();
				return rt.getAllPagesHtml();// ph;
			
	
		
		
		
	}
	
	//统计煤矿
	public String getMeikdq(){
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		ResultSetList rs=null;
		String[] strFormat=null;
		sbsql.setLength(0);
		Report rt = new Report(); //报表定义
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
		
		String mk_str=" ";
		long mk_id=this.getMeikValue().getId();
		if(mk_id==-1){
			mk_str=" ";
		}else{
			mk_str=" and pq.fenz_id="+mk_id+"\n";
		}

			sbsql.append(


					"select decode(dq.meikdqmc,null,'合计',dq.meikdqmc) as meikdq,\n" +
					"sum(f.laimsl) as laim,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.had*f.laimsl)/sum(f.laimsl),2) ) as had,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.mt*f.laimsl)/sum(f.laimsl),1) )as mt,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.mad*f.laimsl)/sum(f.laimsl),2)) as mad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum((100*(z.mt-z.mad)/(100-z.mad))*f.laimsl)/sum(f.laimsl),2)) as neis,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.aad*f.laimsl)/sum(f.laimsl),2)) as aad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.ad*f.laimsl)/sum(f.laimsl),2)) as ad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.vad*f.laimsl)/sum(f.laimsl),2) )as vad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.stad*f.laimsl)/sum(f.laimsl),2)) as stad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.qbad*f.laimsl)/sum(f.laimsl),2) )as qbad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.qgrad*f.laimsl)/sum(f.laimsl),2)) as qgrd,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.qnet_ar*f.laimsl)/sum(f.laimsl),2)) as qnet_ar\n" + 
					"from fahb f ,zhilb z ,meikxxb m,\n" + 
					"meikdqb_xw dq,meikdqglb gl,meikpqb pq\n" + 
					"where f.meikxxb_id=m.id\n" + 
					"and m.id=gl.meikxxb_id\n" + 
					"and dq.id=gl.meikdqb_xw_id\n" + 
					"and dq.meikpqb_id=pq.id\n" + 
					""+mk_str+"\n"+
					"and f.daohrq>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
					"and f.daohrq<=to_date('"+jies+"','yyyy-mm-dd')\n" + 
					"and f.zhilb_id=z.id\n" + 
					"group by rollup (dq.meikdqmc)\n" + 
					"order by dq.meikdqmc"

);
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[1][13];
//				1120
				ArrHeader[0]=new String[] {"煤矿","煤量","氢","全水","内水","外水","空干灰","干灰","挥发份","全硫","弹筒热","高位热","低位热"};
				int ArrWidth[]=new int[] {130,80,50,50,50,50,50,50,50,50,50,50,50};
				  strFormat = new String[] { "", "0.00","0.00", "0.0","0.00","0.00" , "0.00","0.00","0.00" , "0.00","0.00","0.00" , "0.00"};
			
				// 数据
				rt.setTitle("入厂煤质量统计台账",ArrWidth);
				rt.setDefaultTitle(1, 3, "来煤日期:"+kais, Table.ALIGN_LEFT);
				
				
				rt.setBody(new Table(rs, 1, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(3000);
				
			
				
//				设定小计行的背景色和字体
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 1);
					if((xiaoj.equals("合计"))){
						for (int j=0;j<rt.body.getCols()+1;j++){
							rt.body.getCell(i, j).backColor="silver";
							rt.body.getCell(i, j).fontBold=true;
						}
					}
				}
				
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// 表头数据
				rt.body.ShowZero = true;
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				rt.body.mergeFixedCols();
				rt.body.mergeFixedRow();
				rt.createFooter(1, ArrWidth);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setRowHeight(25);
				con.Close();
				return rt.getAllPagesHtml();// ph;
			
	
		
		
		
	}
	
	
	//煤矿地区,煤矿
	public String getMeikdq_meik(){
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		ResultSetList rs=null;
		String[] strFormat=null;
		sbsql.setLength(0);
		Report rt = new Report(); //报表定义
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
		
		
		String mk_str=" ";
		long mk_id=this.getMeikValue().getId();
		if(mk_id==-1){
			mk_str=" ";
		}else{
			mk_str=" and pq.fenz_id="+mk_id+"\n";
		}

			sbsql.append(


					"select decode(dq.meikdqmc,null,'合计',dq.meikdqmc) as meikdq,\n" +
					"decode(dq.meikdqmc,null,null,decode(m.mingc,null,'小计',m.mingc)) as meik ,\n" + 
					"sum(f.laimsl) as laim,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.had*f.laimsl)/sum(f.laimsl),2) ) as had,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.mt*f.laimsl)/sum(f.laimsl),1) )as mt,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.mad*f.laimsl)/sum(f.laimsl),2)) as mad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum((100*(z.mt-z.mad)/(100-z.mad))*f.laimsl)/sum(f.laimsl),2)) as neis,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.aad*f.laimsl)/sum(f.laimsl),2)) as aad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.ad*f.laimsl)/sum(f.laimsl),2)) as ad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.vad*f.laimsl)/sum(f.laimsl),2) )as vad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.stad*f.laimsl)/sum(f.laimsl),2)) as stad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.qbad*f.laimsl)/sum(f.laimsl),2) )as qbad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.qgrad*f.laimsl)/sum(f.laimsl),2)) as qgrd,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.qnet_ar*f.laimsl)/sum(f.laimsl),2)) as qnet_ar\n" + 
					"from fahb f ,zhilb z ,meikxxb m,\n" + 
					"meikdqb_xw dq,meikdqglb gl,meikpqb pq\n" + 
					"where f.meikxxb_id=m.id\n" + 
					"and m.id=gl.meikxxb_id\n" + 
					"and dq.id=gl.meikdqb_xw_id\n" + 
					"and dq.meikpqb_id=pq.id\n" + 
					""+mk_str+"\n"+
					"and f.daohrq>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
					"and f.daohrq<=to_date('"+jies+"','yyyy-mm-dd')\n" + 
					"and f.zhilb_id=z.id\n" + 
					"group by rollup (dq.meikdqmc,m.mingc)\n" + 
					"order by dq.meikdqmc,m.mingc"


);
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[1][14];
//				1120
				ArrHeader[0]=new String[] {"煤矿地区","井口","煤量","氢","全水","内水","外水","空干灰","干灰","挥发份","全硫","弹筒热","高位热","低位热"};
				//ArrHeader[1]=new String[] {"煤矿地区","名称","车数","吨数","车数","吨数","车数","吨数"};
				int ArrWidth[]=new int[] {130,130,80,50,50,50,50,50,50,50,50,50,50,50};
				  strFormat = new String[] { "","", "0.00","0.00", "0.0","0.00","0.00" , "0.00","0.00","0.00" , "0.00","0.00","0.00" , "0.00"};
				// 数据
				rt.setTitle("入厂煤质量统计台账",ArrWidth);
				rt.setDefaultTitle(1, 3, "来煤日期:"+kais, Table.ALIGN_LEFT);
				
				
				rt.setBody(new Table(rs, 1, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(3000);
				
			
				
//				设定小计行的背景色和字体
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 1);
					String xiaoj2=rt.body.getCellValue(i, 2);
					if((xiaoj.equals("合计")||xiaoj2.equals("小计"))){
						for (int j=0;j<rt.body.getCols()+1;j++){
							rt.body.getCell(i, j).backColor="silver";
							rt.body.getCell(i, j).fontBold=true;
						}
					}
				}
				
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// 表头数据
				rt.body.ShowZero = true;
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				rt.body.mergeFixedCols();
				rt.body.mergeFixedRow();
				rt.createFooter(1, ArrWidth);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setRowHeight(25);
				con.Close();
				return rt.getAllPagesHtml();// ph;
			
	
		
		
		
	}

	public String GetPianq(){

		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer talbe=new StringBuffer();	//报表输出
		ResultSetList rs=null;
		String[] strFormat=null;
		sbsql.setLength(0);
		Report rt = new Report(); //报表定义
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
		
		
		
		String mk_str=" ";
		long mk_id=this.getMeikValue().getId();
		if(mk_id==-1){
			mk_str=" ";
		}else{
			mk_str=" and pq.fenz_id="+mk_id+"\n";
		}
		
		


			sbsql.append(



					"select decode(pq.mingc,null,'合计',pq.mingc) as meikpq,\n" +
					"sum(f.laimsl) as laim,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.had*f.laimsl)/sum(f.laimsl),2) ) as had,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.mt*f.laimsl)/sum(f.laimsl),1) )as mt,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.mad*f.laimsl)/sum(f.laimsl),2)) as mad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum((100*(z.mt-z.mad)/(100-z.mad))*f.laimsl)/sum(f.laimsl),2)) as neis,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.aad*f.laimsl)/sum(f.laimsl),2)) as aad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.ad*f.laimsl)/sum(f.laimsl),2)) as ad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.vad*f.laimsl)/sum(f.laimsl),2) )as vad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.stad*f.laimsl)/sum(f.laimsl),2)) as stad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.qbad*f.laimsl)/sum(f.laimsl),2) )as qbad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.qgrad*f.laimsl)/sum(f.laimsl),2)) as qgrd,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.qnet_ar*f.laimsl)/sum(f.laimsl),2)) as qnet_ar\n" + 
					"from fahb f ,zhilb z ,meikxxb m,\n" + 
					"meikdqb_xw dq,meikdqglb gl,meikpqb pq\n" + 
					"where f.meikxxb_id=m.id\n" + 
					"and m.id=gl.meikxxb_id\n" + 
					"and dq.id=gl.meikdqb_xw_id\n" + 
					"and dq.meikpqb_id=pq.id\n" + 
					""+mk_str+"\n"+
					"and f.daohrq>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
					"and f.daohrq<=to_date('"+jies+"','yyyy-mm-dd')\n" + 
					"and f.zhilb_id=z.id\n" + 
					"group by rollup (pq.mingc)\n" + 
					"order by pq.mingc"


);
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
				String ArrHeader[][]=new String[1][13];
//				1120
				ArrHeader[0]=new String[] {"片区名称","煤量","氢","全水","内水","外水","空干灰","干灰","挥发份","全硫","弹筒热","高位热","低位热"};
		    	int ArrWidth[]=new int[] {130,80,50,50,50,50,50,50,50,50,50,50,50};
				  strFormat = new String[] { "", "0.00","0.00", "0.0","0.00","0.00" , "0.00","0.00","0.00" , "0.00","0.00","0.00" , "0.00"};
			
				// 数据
				rt.setTitle("入厂煤数量统计台账",ArrWidth);
				rt.setDefaultTitle(1, 3, "来煤日期:"+kais, Table.ALIGN_LEFT);
				
				
				rt.setBody(new Table(rs, 1, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(3000);
				
			
				
//				设定小计行的背景色和字体
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 1);
					if((xiaoj.equals("合计"))){
						for (int j=0;j<rt.body.getCols()+1;j++){
							rt.body.getCell(i, j).backColor="silver";
							rt.body.getCell(i, j).fontBold=true;
						}
					}
				}
				
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// 表头数据
				rt.body.ShowZero = true;
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				rt.body.mergeFixedCols();
				rt.body.mergeFixedRow();
				rt.createFooter(1, ArrWidth);
				//rt.setDefautlFooter(1, 3, "打印日期："+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setRowHeight(25);
				con.Close();
				return rt.getAllPagesHtml();// ph;
			
	
		
		
		
	
	}
	
	//煤矿片区,煤矿
	public String GetPianq_meik(){

		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer talbe=new StringBuffer();	//报表输出
		ResultSetList rs=null;
		String[] strFormat=null;
		sbsql.setLength(0);
		Report rt = new Report(); //报表定义
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
		
		
		
		String mk_str=" ";
		long mk_id=this.getMeikValue().getId();
		if(mk_id==-1){
			mk_str=" ";
		}else{
			mk_str=" and pq.fenz_id="+mk_id+"\n";
		}
		
		


			sbsql.append(


					"select decode(pq.mingc,null,'合计',pq.mingc) as meikpq,\n" +
					"decode(pq.mingc,null,null,decode(m.mingc,null,'小计',m.mingc)) as meik ,\n" + 
					"sum(f.laimsl) as laim,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.had*f.laimsl)/sum(f.laimsl),2) ) as had,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.mt*f.laimsl)/sum(f.laimsl),1) )as mt,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.mad*f.laimsl)/sum(f.laimsl),2)) as mad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum((100*(z.mt-z.mad)/(100-z.mad))*f.laimsl)/sum(f.laimsl),2)) as neis,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.aad*f.laimsl)/sum(f.laimsl),2)) as aad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.ad*f.laimsl)/sum(f.laimsl),2)) as ad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.vad*f.laimsl)/sum(f.laimsl),2) )as vad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.stad*f.laimsl)/sum(f.laimsl),2)) as stad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.qbad*f.laimsl)/sum(f.laimsl),2) )as qbad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.qgrad*f.laimsl)/sum(f.laimsl),2)) as qgrd,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.qnet_ar*f.laimsl)/sum(f.laimsl),2)) as qnet_ar\n" + 
					"from fahb f ,zhilb z ,meikxxb m,\n" + 
					"meikdqb_xw dq,meikdqglb gl,meikpqb pq\n" + 
					"where f.meikxxb_id=m.id\n" + 
					"and m.id=gl.meikxxb_id\n" + 
					"and dq.id=gl.meikdqb_xw_id\n" + 
					"and dq.meikpqb_id=pq.id\n" + 
					""+mk_str+"\n"+
					"and f.daohrq>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
					"and f.daohrq<=to_date('"+jies+"','yyyy-mm-dd')\n" + 
					"and f.zhilb_id=z.id\n" + 
					"group by rollup (pq.mingc,m.mingc)\n" + 
					"order by pq.mingc,m.mingc"



);
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			

				String ArrHeader[][]=new String[1][14];
//				1120
				ArrHeader[0]=new String[] {"煤矿片区","井口","煤量","氢","全水","内水","外水","空干灰","干灰","挥发份","全硫","弹筒热","高位热","低位热"};
				int ArrWidth[]=new int[] {130,130,80,50,50,50,50,50,50,50,50,50,50,50};
				  strFormat = new String[] { "","", "0.00","0.00", "0.0","0.00","0.00" , "0.00","0.00","0.00" , "0.00","0.00","0.00" , "0.00"};
				// 数据
				rt.setTitle("入厂煤质量统计台账",ArrWidth);
				rt.setDefaultTitle(1, 3, "来煤日期:"+kais, Table.ALIGN_LEFT);
				
				
				rt.setBody(new Table(rs, 1, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(3000);
				
			
				
//				设定小计行的背景色和字体
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 1);
					String xiaoj2=rt.body.getCellValue(i, 2);
					if((xiaoj.equals("合计")||xiaoj2.equals("小计"))){
						for (int j=0;j<rt.body.getCols()+1;j++){
							rt.body.getCell(i, j).backColor="silver";
							rt.body.getCell(i, j).fontBold=true;
						}
					}
				}
				
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// 表头数据
				rt.body.ShowZero = true;
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				rt.body.mergeFixedCols();
				rt.body.mergeFixedRow();
				rt.createFooter(1, ArrWidth);
				//rt.setDefautlFooter(1, 3, "打印日期："+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setRowHeight(25);
				con.Close();
				return rt.getAllPagesHtml();// ph;
			
	
		
		
		
	
	}
	
//	煤矿片区,煤矿地区
	public String GetPianq_meikdq(){

		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer talbe=new StringBuffer();	//报表输出
		ResultSetList rs=null;
		String[] strFormat=null;
		sbsql.setLength(0);
		Report rt = new Report(); //报表定义
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
		
		
		
		String mk_str=" ";
		long mk_id=this.getMeikValue().getId();
		if(mk_id==-1){
			mk_str=" ";
		}else{
			mk_str=" and pq.fenz_id="+mk_id+"\n";
		}
		
		


			sbsql.append(


					"select decode(pq.mingc,null,'合计',pq.mingc) as meikpq,\n" +
					"decode(pq.mingc,null,null,decode(dq.meikdqmc,null,'小计',dq.meikdqmc)) as meik ,\n" + 
					"sum(f.laimsl) as laim,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.had*f.laimsl)/sum(f.laimsl),2) ) as had,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.mt*f.laimsl)/sum(f.laimsl),1) )as mt,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.mad*f.laimsl)/sum(f.laimsl),2)) as mad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum((100*(z.mt-z.mad)/(100-z.mad))*f.laimsl)/sum(f.laimsl),2)) as neis,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.aad*f.laimsl)/sum(f.laimsl),2)) as aad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.ad*f.laimsl)/sum(f.laimsl),2)) as ad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.vad*f.laimsl)/sum(f.laimsl),2) )as vad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.stad*f.laimsl)/sum(f.laimsl),2)) as stad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.qbad*f.laimsl)/sum(f.laimsl),2) )as qbad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.qgrad*f.laimsl)/sum(f.laimsl),2)) as qgrd,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.qnet_ar*f.laimsl)/sum(f.laimsl),2)) as qnet_ar\n" + 
					"from fahb f ,zhilb z ,meikxxb m,\n" + 
					"meikdqb_xw dq,meikdqglb gl,meikpqb pq\n" + 
					"where f.meikxxb_id=m.id\n" + 
					"and m.id=gl.meikxxb_id\n" + 
					"and dq.id=gl.meikdqb_xw_id\n" + 
					"and dq.meikpqb_id=pq.id\n" + 
					""+mk_str+"\n"+
					"and f.daohrq>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
					"and f.daohrq<=to_date('"+jies+"','yyyy-mm-dd')\n" + 
					"and f.zhilb_id=z.id\n" + 
					"group by rollup (pq.mingc,dq.meikdqmc)\n" + 
					"order by pq.mingc,dq.meikdqmc"



);
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			

				String ArrHeader[][]=new String[1][14];
//				1120
				ArrHeader[0]=new String[] {"煤矿片区","煤矿","煤量","氢","全水","内水","外水","空干灰","干灰","挥发份","全硫","弹筒热","高位热","低位热"};
				int ArrWidth[]=new int[] {130,130,80,50,50,50,50,50,50,50,50,50,50,50};
				  strFormat = new String[] { "","", "0.00","0.00", "0.0","0.00","0.00" , "0.00","0.00","0.00" , "0.00","0.00","0.00" , "0.00"};
				// 数据
				rt.setTitle("入厂煤质量统计台账",ArrWidth);
				rt.setDefaultTitle(1, 3, "来煤日期:"+kais, Table.ALIGN_LEFT);
				
				
				rt.setBody(new Table(rs, 1, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(3000);
				
			
				
//				设定小计行的背景色和字体
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 1);
					String xiaoj2=rt.body.getCellValue(i, 2);
					if((xiaoj.equals("合计")||xiaoj2.equals("小计"))){
						for (int j=0;j<rt.body.getCols()+1;j++){
							rt.body.getCell(i, j).backColor="silver";
							rt.body.getCell(i, j).fontBold=true;
						}
					}
				}
				
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// 表头数据
				rt.body.ShowZero = true;
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				rt.body.mergeFixedCols();
				rt.body.mergeFixedRow();
				rt.createFooter(1, ArrWidth);
				//rt.setDefautlFooter(1, 3, "打印日期："+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setRowHeight(25);
				con.Close();
				return rt.getAllPagesHtml();// ph;
			
	
		
		
		
	
	}
	
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
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getToolbar().getRenderScript();
	}
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			
			setBRiq( DateUtil.FormatDate(DateUtil.AddDate(new Date(), -2, DateUtil.AddType_intDay)));
			setERiq( DateUtil.FormatDate(DateUtil.AddDate(new Date(), -2, DateUtil.AddType_intDay)));
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean6(null);
			visit.setProSelectionModel6(null);

			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
			getSelectData();
		}
	}
	
//	按钮的监听事件
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

//	表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
		
//		}else{
//			this.setMeikValue(null);
//			this.setMeikModel(null);
//		}
		getSelectData();
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
	

	
	
	

	
	
	
	

	
	
	
//	 煤矿下拉框
	public IDropDownBean getMeikValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getMeikModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setMeikValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setMeikModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getMeikModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getMeikModels() {
		
		
		
		
		String sql="select id,mingc from fenzb";
		
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql,"全部"));
		return;
	}
	
	
//	 统计口径状态下拉框
	public IDropDownBean getKoudValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean6((IDropDownBean) getKoudModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setKoudValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean6(Value);
	}

	public void setKoudModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getKoudModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getKoudModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void getKoudModels() {
		
		List list=new ArrayList();
		//list.add(new IDropDownBean("1","全部"));
		list.add(new IDropDownBean("1","统计煤矿,井口"));
		list.add(new IDropDownBean("2","统计煤矿"));
		list.add(new IDropDownBean("3","统计井口"));
		list.add(new IDropDownBean("4","统计片区"));
		list.add(new IDropDownBean("5","统计片区,井口"));
		list.add(new IDropDownBean("6","统计片区,煤矿"));
		
		
		((Visit) getPage().getVisit())
				.setProSelectionModel6(new IDropDownModel(list));
		return;
	}
	
	
}
