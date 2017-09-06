package com.zhiren.shanxdted.rulmfxbb;

import java.util.ArrayList;
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
import com.zhiren.report.Row;
import com.zhiren.report.Table;
/*
 * 作者:tzf
 * 时间:2009-09-19
 * 内容:入炉煤分析报表，包括细度，煤灰，煤质分析报表
 */
public class Rulmfxbb extends BasePage implements PageValidateListener {


	
	private static final String BAOBPZB_GUANJZ = "Jinmrbb_GJZ_ED";// baobpzb中对应的关键字
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
	private boolean briqboo=false;
	private String briq;

	public String getBRiq() {
		
		if(this.briq==null || this.briq.equals("")){
			return DateUtil.FormatDate(new Date());
		}
		return briq;
	}

	public void setBRiq(String briq) {
		if(this.briq!=null && !this.briq.equals(briq)){
			briqboo=true;
		}
		this.briq = briq;
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


//  获得选择的树节点的对应的电厂名称   
    private String getMingc(String id){ 
		JDBCcon con=new JDBCcon();
		String mingc=null;
		String sql="select mingc from diancxxb where id = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=rsl.getString("mingc");
			
		}
		rsl.close();
		return mingc;
	}
    
    
//  判断电厂Tree中所选电厂时候还有子电厂   
    private ResultSetList hasDianc(String id){ 
		JDBCcon con=new JDBCcon();
		String sql="select id, mingc from diancxxb where fuid = " + id +" union  select id,mingc from diancxxb where id="+id+" order by id asc ";
		ResultSetList rsl=con.getResultSetList(sql);
		con.Close();
		return rsl;
	}
    
//	获取表表标题
	public String getRptTitle(JDBCcon con, int i) {
		Visit visit = (Visit) this.getPage().getVisit();
		String sql ="select quanc from diancxxb where id=" + getTreeid_dc();
		String sb=visit.getDiancqc();
		if (getTreeid_dc()!=null && !"".equals(getTreeid_dc())) {
			ResultSetList rsl = con.getResultSetList(sql);
			if (rsl.next()) {
				sb = rsl.getString("quanc");
			} 
		}
		switch(i){
		case 1:sb+="煤粉细度报表";break;
		case 2:sb+="煤灰分析报表";break;
		case 3:sb+="煤质分析报表";break;
		}
		return sb;
	}
	
	
	
//	刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con=new JDBCcon();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("入炉日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("guohrqb");
		tb1.addField(dfb);
	
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
		
		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		
		tb1.addText(new ToolbarText("-"));
		
	//-------------------------------------------------------	
		
		
//		报表格式
		tb1.addText(new ToolbarText("格式:"));
		ComboBox ges = new ComboBox();
		ges.setTransform("GesSelect");
		ges.setEditable(true);
		ges.setWidth(120);
//		meik.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(ges);	
		
		tb1.addText(new ToolbarText("-"));	
		
		

		
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		
		tb1.addFill();
		setToolbar(tb1);
	}
	

	
	public String getPrintTable(){
		
		int i=Integer.parseInt(this.getGesValue().getStrId());
		
		switch (i){
		case 0:return this.getAllTableHt();
		case 1:return this.getTableXid(); 
		case 2:return this.getTableMeih();
		case 3:return this.getTableMeiz();
		default:return this.getTableXid();
		}
		
	}
	
	private String getAllTableHt(){
		
		String html="";
		
		html=this.getTableXid()+this.getTableMeih()+this.getTableMeiz();
		
		return html;
	}

	private StringBuffer getXidsql(String jizb_id,String shebb_id){
		
		StringBuffer bf=new StringBuffer();
		bf.append(" select x.xidz,b.mingc banz,b.xuh \n");
		bf.append("  from xidxxb x,rulbzb b \n");
		bf.append(" where x.rulbzb_id(+)=b.id\n");
		bf.append(" and x.diancxxb_id="+this.getTreeid_dc()+"\n");
		bf.append(" and x.riq="+DateUtil.FormatOracleDate(this.getBRiq())+"\n");
		bf.append(" and  x.jizfzb_id=").append(jizb_id).append(" \n");
		bf.append(" and x.rulsbb_id=").append(shebb_id).append(" \n");
		
		bf.append(" union \n");
		
		bf.append(" select avg(nvl(x.xidz,0)) xidz,'平均值' banz,99999 xuh \n");
		bf.append("  from xidxxb x ,rulbzb b \n");
		bf.append(" where x.rulbzb_id(+)=b.id \n");
		bf.append(" and x.diancxxb_id="+this.getTreeid_dc()+"\n");
		bf.append(" and x.riq="+DateUtil.FormatOracleDate(this.getBRiq())+"\n");
		bf.append(" and  x.jizfzb_id=").append(jizb_id).append(" \n");
		bf.append(" and x.rulsbb_id=").append(shebb_id).append(" \n");
		
		bf.append(" order by xuh asc\n");
		
		return bf;
	}
	
	private StringBuffer getJizfzbsql(){
		
		StringBuffer bf=new StringBuffer();
		bf.append("  select j.id,j.mingc from jizfzb  j where j.mingc not like '%皮带%' \n");
		bf.append("  and j.mingc not in (select zhi from xitxxb where mingc='灰分机组过滤项' and leib='入炉' and zhuangt=1 and diancxxb_id="+this.getTreeid_dc()+") \n" );
		bf.append("  and j.diancxxb_id="+this.getTreeid_dc()+" order by xuh asc \n");
	
		return bf;
	}
	
	private StringBuffer getRulsbbsql(){
		
		StringBuffer bf=new StringBuffer();
		bf.append("  select * from rulsbb r \n");
		bf.append("  where r.diancxxb_id="+this.getTreeid_dc()+"\n");
	
		return bf;
	}

	private StringBuffer getBancSql(){
		
		StringBuffer bf=new StringBuffer();
		bf.append("  select id,mingc,xuh, rownum+2 linem from (select id,mingc,xuh from rulbzb r \n");
		bf.append("  where r.diancxxb_id="+this.getTreeid_dc()+" union select 0 id,'平均值' mingc,99999 xuh from dual  order by xuh  asc  )  order by xuh  asc \n");
	
		return bf;
	}
	
	private StringBuffer getMeihSql(String jizb_id){
		
		StringBuffer bf=new StringBuffer();
		bf.append(" select x.zhi,b.mingc banz,b.xuh,x.xiangm  \n");
		bf.append("  from rulfhb x,rulbzb b \n");
		bf.append(" where x.rulbzb_id(+)=b.id\n");
		bf.append(" and x.diancxxb_id="+this.getTreeid_dc()+"\n");
		bf.append(" and x.riq="+DateUtil.FormatOracleDate(this.getBRiq())+"\n");
		bf.append(" and  x.jizfzb_id=").append(jizb_id).append(" \n");
		
		bf.append(" union \n");
		
		bf.append(" select avg(nvl(x.zhi,0)) zhi,'平均值' banz,99999 xuh,x.xiangm \n");
		bf.append("  from rulfhb x ,rulbzb b \n");
		bf.append(" where x.rulbzb_id(+)=b.id \n");
		bf.append(" and x.diancxxb_id="+this.getTreeid_dc()+"\n");
		bf.append(" and x.riq="+DateUtil.FormatOracleDate(this.getBRiq())+"\n");
		bf.append(" and  x.jizfzb_id=").append(jizb_id).append(" \n");
		bf.append(" group by x.xiangm ");

		
		bf.append(" order by xuh asc,xiangm asc \n");
		
		return bf;
	}
	
	private String getValue(String value,String fat){
		if(value==null || value.equals("") || (Double.valueOf(value).doubleValue()==0 ))
			return fat;
		
		double v=Double.parseDouble(value);
		
		return CustomMaths.Round_new(v, 1)+"";
	}
	//煤粉细度表
	private String getTableXid(){


		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		
		
		String sql="";
		
		ResultSetList rstmp =null;
		ResultSetList rs = null;
		String[] strFormat=null;
		int[] ArrWidth=null;
	
        
        rs=con.getResultSetList(getJizfzbsql().toString());
        rstmp=con.getResultSetList(getRulsbbsql().toString());
        int count_jiz=rs.getRows();//总共的机组数目
        int count_sheb=rstmp.getRows();//总共的设备数
        
        String[][] ArrHeader=new String[2][4+count_jiz*count_sheb];
   	
        	 ArrHeader[0][0]="炉号";
        	 ArrHeader[0][1]="炉号";
        	 ArrHeader[0][count_jiz*count_sheb+2]="分析员";
        	 ArrHeader[0][count_jiz*count_sheb+3]="录入员";
        	 
        	 ArrHeader[1][0]="班次";
        	 ArrHeader[1][1]="设备号";
        	 ArrHeader[1][count_jiz*count_sheb+2]="分析员";
        	 ArrHeader[1][count_jiz*count_sheb+3]="录入员";
        	 
        	 rs.beforefirst();
        	 rstmp.beforefirst();
        	 
        	 int start_col=2;//从第二列开始赋值(下表索引为2)
        	 while(rs.next() && count_sheb>0){
        		 
        		 for(int i=0;i<count_sheb;i++){
        			 ArrHeader[0][start_col+i]=rs.getString("mingc");//机组表头
        		 }
        		 start_col+=count_sheb;
        	 }
        	 
        	
        	 
        	 start_col=2;//从第二列开始赋值(下表索引为2)
        	 for(int i=0;i<count_jiz;i++){
        		 
        		 rstmp.beforefirst();
        		 while(rstmp.next()){
        			 
        			 ArrHeader[1][start_col]=rstmp.getString("mingc");//设备表头
        			 start_col++;
        			 
        		 }
    			
    		 }
        	 
        	 
     		strFormat = new String[4+count_jiz*count_sheb];
     		strFormat[0]="";
     		strFormat[1]="";
     		
     		strFormat[count_jiz*count_sheb+2]="";
     		strFormat[count_jiz*count_sheb+3]="";
     		
        	
        	ArrWidth = new int[4+count_jiz*count_sheb];
        	ArrWidth[0]=50;
        	ArrWidth[1]=50;
        	
        	for(int i=0;i<count_jiz*count_sheb;i++){
        		ArrWidth[i+2]=45;
        		strFormat[i+2]="0.0";
        	}
        	ArrWidth[count_jiz*count_sheb+2]=50;
        	ArrWidth[count_jiz*count_sheb+3]=50;
        	
    
        	
    		rt.setTitle(getRptTitle(con, 1), ArrWidth);
    		rt.title.fontSize=12;
    		rt.title.setRowHeight(2, 50);

    	
    		rt.setDefaultTitle(1, count_jiz*count_sheb+4, getBRiq() ,
    				Table.ALIGN_CENTER);
    		
        
    	ResultSetList banrs=con.getResultSetList(getBancSql().toString());//班次总共
    	Table bt=new Table(banrs.getRows()+2,4+count_jiz*count_sheb);
    	rt.setBody(bt);
    	rt.body.setHeaderData(ArrHeader);
    	
    	int count_banz=banrs.getRows();
    	banrs.beforefirst();
    	rs.beforefirst();
    	rstmp.beforefirst();
    	
    	ResultSetList resu=null;
    	
    	int row_line=3;//记录行
    	start_col=2;//从第二列开始赋值(下表索引为2)
    	while(banrs.next()){
    		
    	
    				
    		rt.body.setCellValue(row_line, 1, banrs.getString("mingc"));
    		rt.body.setCellValue(row_line, 2, banrs.getString("mingc"));
    		
    		rt.body.setCellAlign(row_line, 1, Table.ALIGN_CENTER);
    		rt.body.setCellAlign(row_line, 2, Table.ALIGN_CENTER);
    		
    		row_line++;
    	}
    	
    	row_line=3;
    	banrs.beforefirst();
    	
    	while(rs.next()){
//    		String jiz=rs.getString("mingc");
    		String jizb_id=rs.getString("id");
    		rstmp.beforefirst();
    		while(rstmp.next()){
    			
//    			String sheb=rstmp.getString("mingc");
    			String shebb_id=rstmp.getString("id");
    			
    			resu=con.getResultSetList(this.getXidsql(jizb_id, shebb_id).toString());
    			
    			while(resu.next()){
    				
    				banrs.beforefirst();
    				boolean flag=false;
    				while(banrs.next()){
    					
    					if(resu.getString("banz").equals(banrs.getString("mingc"))){
    						flag=true;
    						rt.body.setCellValue(banrs.getInt("linem"),row_line,resu.getString("xidz"));
    						rt.body.setCellAlign(banrs.getInt("linem"), row_line, Table.ALIGN_CENTER);
    					}
    				}
    				
//    				if(flag){
//    					
//    					rt.body.setCellValue(banrs.getInt("linem"),row_line,getValue("0") );
//						rt.body.setCellAlign(banrs.getInt("linem"), row_line, Table.ALIGN_CENTER);
//    				}
    				
    			}
    			
    			row_line++;//记录列号
    		}
    	}
    	
    	banrs.beforefirst();
    	
    	while(banrs.next()){
    		
    		resu=con.getResultSetList(" select fenxy,lury from xidxxb x where x.diancxxb_id="+this.getTreeid_dc()
    				+" and x.riq="+DateUtil.FormatOracleDate(this.getBRiq())+" and x.rulbzb_id="+banrs.getString("id"));
    		
    		String fenxy="";
    		String lury="";
    		
    		if(resu.next()){
    			fenxy=resu.getString("fenxy");
    			lury=resu.getString("lury");
    		}
    		
    		rt.body.setCellValue(banrs.getInt("linem"),count_jiz*count_sheb+3,fenxy);
    		rt.body.setCellValue(banrs.getInt("linem"),count_jiz*count_sheb+4,lury);
    		
    		rt.body.setCellAlign(banrs.getInt("linem"), count_jiz*count_sheb+3, Table.ALIGN_CENTER);
    		rt.body.setCellAlign(banrs.getInt("linem"), count_jiz*count_sheb+4, Table.ALIGN_CENTER);
    	}
    	
    	
    	for(int i=0;i<count_banz;i++){
    		
    		for(int j=0;j<count_jiz*count_sheb;j++){
    			
    			rt.body.setCellValue(i+3, j+3, getValue(rt.body.getCellValue(i+3, j+3),"00.0"));
    			rt.body.setCellAlign(i+3,j+3, Table.ALIGN_CENTER);
    		}
    	}
		
		rt.body.setFontSize(10);
		
		rt.body.setWidth(ArrWidth);
	
//		rt.body.setColFormat(strFormat);
	
		rt.body.setPageRows(-1);
		rt.body.ShowZero=true;
		
		
//		for(int i=0;i<4;i++){
//			
//			if(count_jiz*count_sheb>0)
//			for(int j=3;j<3+count_jiz*count_sheb;j++){
//				
//				rt.body.setCellValue(i+3, j, this.getValue(rt.body.getCellValue(i+3, j)));
//			}
//		}

		rt.body.merge(1, 1, 2, 4+count_jiz*count_sheb);
		
		if(count_banz>0)
		rt.body.merge(3, 1, count_banz+2, 2);
		
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 2, "校阅：" ,
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(5, 2, "班长：", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
//		rt.footer.setRowHeight(1, 1);
		rs.close();
		rstmp.close();
		resu.close();
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(25);

		return rt.getAllPagesHtml();// ph;
	
	
	}
	
	//煤灰表
	private String getTableMeih(){


		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		
		
		String sql="";
		
		ResultSetList rstmp =null;
		ResultSetList rs = null;
		String[] strFormat=null;
		int[] ArrWidth=null;
	
        
        rs=con.getResultSetList(getJizfzbsql().toString());
        int count_jiz=rs.getRows();//总共的机组数目
        
        String[][] ArrHeader=new String[2][5+count_jiz];
   	
        	 ArrHeader[0][0]="炉号";
        	 ArrHeader[0][1]="炉号";
        	 ArrHeader[0][count_jiz+2]="化验员";
        	 ArrHeader[0][count_jiz+3]="录入员";
        	 ArrHeader[0][count_jiz+4]="备注";
        	 
        	 ArrHeader[1][0]="班次";
        	 ArrHeader[1][1]="项目";
        	 ArrHeader[1][count_jiz+2]="化验员";
        	 ArrHeader[1][count_jiz+3]="录入员";
        	 ArrHeader[1][count_jiz+4]="备注";
        	 
        	 rs.beforefirst();
//        	 rstmp.beforefirst();
        	 
        	 int start_col=2;//从第二列开始赋值(下表索引为2)
        	 while(rs.next()){
        		 
        		 
        		 ArrHeader[0][start_col]=rs.getString("mingc");//机组表头
        		 ArrHeader[1][start_col]=rs.getString("mingc");//机组表头
        		 start_col++;
        	 }        	         	      
        	         	 
     		strFormat = new String[5+count_jiz];
     		strFormat[0]="";
     		strFormat[1]="";
     		
     		strFormat[count_jiz+2]="";
     		strFormat[count_jiz+3]="";
     		strFormat[count_jiz+4]="";
     		
        	
        	ArrWidth = new int[5+count_jiz];
        	ArrWidth[0]=70;
        	ArrWidth[1]=70;
        	
        	int iWidth = 60;
        	if (count_jiz<3) {
        		iWidth = 80;
        	}
        	for(int i=0;i<count_jiz;i++){
        		ArrWidth[i+2]=iWidth;
        		strFormat[i+2]="0.00";
        	}
        	ArrWidth[count_jiz+2]=60;
        	ArrWidth[count_jiz+3]=60;
        	ArrWidth[count_jiz+4]=100;
        	
    
        	
    		rt.setTitle(getRptTitle(con, 2), ArrWidth);
    		rt.title.fontSize=12;
    		rt.title.setRowHeight(2, 50);

    	
    		rt.setDefaultTitle(1, count_jiz+5, getBRiq() ,
    				Table.ALIGN_CENTER);
    		
        
    	ResultSetList banrs=con.getResultSetList(getBancSql().toString());//班次总共
    	Table bt=new Table(banrs.getRows()*2+2,5+count_jiz);
    	rt.setBody(bt);
    	rt.body.setHeaderData(ArrHeader);
    	
    	int count_banz=banrs.getRows();
    	banrs.beforefirst();
    	rs.beforefirst();
//    	rstmp.beforefirst();
    	
    	ResultSetList resu=null;
    	
    	int row_line=3;//记录行
    	start_col=2;//从第二列开始赋值(下表索引为2)
    	while(banrs.next()){
    		
    	
    				
    		rt.body.setCellValue(row_line, 1, banrs.getString("mingc"));
    		rt.body.setCellValue(row_line, 2, "灰分");
    		
    		rt.body.setCellValue(row_line+1, 1, banrs.getString("mingc"));
    		rt.body.setCellValue(row_line+1, 2, "可燃物%");
    		
    		rt.body.setCellAlign(row_line, 1, Table.ALIGN_CENTER);
    		rt.body.setCellAlign(row_line, 2, Table.ALIGN_CENTER);
    		
    		rt.body.setCellAlign(row_line+1, 1, Table.ALIGN_CENTER);
    		rt.body.setCellAlign(row_line+1, 2, Table.ALIGN_CENTER);
    		
    		row_line+=2;
    	}
    	
    	row_line=3;
    	banrs.beforefirst();
    	
    	while(rs.next()){

    		String jizb_id=rs.getString("id");
    			
	    	resu=con.getResultSetList(this.getMeihSql(jizb_id).toString());
    			
    			while(resu.next()){
    				
    				banrs.beforefirst();
    				
    				while(banrs.next()){
    					
    					if(resu.getString("banz").equals(banrs.getString("mingc"))){
    						
    						if(resu.getString("xiangm").equals("灰分")){
    							rt.body.setCellValue(banrs.getInt("linem")*2-3,row_line,resu.getString("zhi"));
//        						rt.body.setCellAlign(banrs.getInt("linem")*2-3, row_line, Table.ALIGN_CENTER);
    						}
    						if(resu.getString("xiangm").equals("可燃物")){
    							rt.body.setCellValue(banrs.getInt("linem")*2-2,row_line,resu.getString("zhi"));
//        						rt.body.setCellAlign(banrs.getInt("linem")*2-2, row_line, Table.ALIGN_CENTER);
    						}
    					
    					}
    				}
    				
    			}
    			
    			row_line++;//记录列号
    		
    	}
    	
    	banrs.beforefirst();
    	
    	while(banrs.next()){
    		
    		resu=con.getResultSetList(" select nvl(max(x.fenxy),'') fenxy,nvl(max(x.lury),'') lury,nvl(max(x.beiz),'') beiz from rulfhb x where x.diancxxb_id="+this.getTreeid_dc()
    				+" and x.riq="+DateUtil.FormatOracleDate(this.getBRiq())+" and x.rulbzb_id="+banrs.getString("id")
    				+" order by x.xiangm asc " );
    		
    		String fenxy="";
    		String lury="";
    		String beiz="";
    		
    		if(resu.next()){
    			fenxy=resu.getString("fenxy");
    			lury=resu.getString("lury");
    			beiz=resu.getString("beiz");
    		}
    		
    		rt.body.setCellValue(banrs.getInt("linem")*2-3,count_jiz+3,fenxy);
    		rt.body.setCellValue(banrs.getInt("linem")*2-3,count_jiz+4,lury);
    		rt.body.setCellValue(banrs.getInt("linem")*2-3,count_jiz+5,beiz);
    		
    		rt.body.setCellValue(banrs.getInt("linem")*2-2,count_jiz+3,fenxy);
    		rt.body.setCellValue(banrs.getInt("linem")*2-2,count_jiz+4,lury);
    		rt.body.setCellValue(banrs.getInt("linem")*2-2,count_jiz+5,beiz);
    		
    		rt.body.setCellAlign(banrs.getInt("linem")*2-3, count_jiz+3, Table.ALIGN_CENTER);
    		rt.body.setCellAlign(banrs.getInt("linem")*2-3, count_jiz+4, Table.ALIGN_CENTER);
    		rt.body.setCellAlign(banrs.getInt("linem")*2-3, count_jiz+5, Table.ALIGN_CENTER);
    		
    		rt.body.setCellAlign(banrs.getInt("linem")*2-2, count_jiz+3, Table.ALIGN_CENTER);
    		rt.body.setCellAlign(banrs.getInt("linem")*2-2, count_jiz+4, Table.ALIGN_CENTER);
    		rt.body.setCellAlign(banrs.getInt("linem")*2-2, count_jiz+5, Table.ALIGN_CENTER);
    		
    		rt.body.merge(banrs.getInt("linem")*2-3, count_jiz+3,banrs.getInt("linem")*2-2, count_jiz+3);
    		rt.body.merge(banrs.getInt("linem")*2-3, count_jiz+4,banrs.getInt("linem")*2-2, count_jiz+4);
    		rt.body.merge(banrs.getInt("linem")*2-3, count_jiz+5,banrs.getInt("linem")*2-2, count_jiz+5);
    	}
    	
		
		rt.body.setFontSize(10);
		
		rt.body.setWidth(ArrWidth);
	
//		rt.body.setColFormat(strFormat);
		
		
		for(int i=0;i<count_banz;i++){
			
			for(int j=0;j<count_jiz;j++){
				
				rt.body.setCellValue(i*2+3, j+3, this.getValue( rt.body.getCellValue(i*2+3, j+3), "0.00"));
				rt.body.setCellValue(i*2+4, j+3, this.getValue( rt.body.getCellValue(i*2+4, j+3), "0.00"));
				
				rt.body.setCellAlign(i*2+3, j+3, Table.ALIGN_CENTER);
				rt.body.setCellAlign(i*2+4, j+3, Table.ALIGN_CENTER);
			}
		}
	
		rt.body.setPageRows(-1);
		rt.body.ShowZero=true;

		rt.body.merge(1, 1, 2, 5+count_jiz);
		
		
		if(count_banz>0){
			rt.body.merge(3, 1, count_banz*2+2, 1);
			
		}
		
		
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 2, "校阅：" ,
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 2, "班长：", Table.ALIGN_CENTER);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
//		rt.footer.setRowHeight(1, 1);
		rs.close();
//		rstmp.close();
		resu.close();
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(25);

		return rt.getAllPagesHtml();// ph;
	
	
	}
	
	private StringBuffer getMeizsql(){
		
		StringBuffer bf=new StringBuffer();
		bf.append(" select   jiz, banz,mad,aad,vad,mt,qbad,stad,had,vdaf,qnet_ar,huayy,lury from ( \n");
		
		bf.append(" select  j.mingc jiz,b.mingc banz,b.xuh,r.mad,r.aad,r.vad,r.mt,r.qbad,r.stad,r.had,r.vdaf,r.qnet_ar,r.huayy,r.lury\n");
		bf.append(" from rulmzlb r ,jizfzb j,rulbzb b\n");
		bf.append(" where r.rulbzb_id=b.id and r.jizfzb_id=j.id\n");
		bf.append(" and r.rulrq="+DateUtil.FormatOracleDate(this.getBRiq())+"\n");
		bf.append(" and r.diancxxb_id="+this.getTreeid_dc()+"\n");
		
		bf.append(" union\n");
		
		bf.append(" select  decode(grouping(j.mingc),0,j.mingc,1,'总均值') jiz,decode(grouping(j.mingc),0,'均值',1,'')  banz,99999 xuh,avg(r.mad) mad,avg(r.aad) aad,avg(r.vad) vad,\n");
		bf.append(" avg(r.mt) mt,avg(r.qbad) qbad,avg(r.stad) stad,\n");
		bf.append(" avg(r.had) had,avg(r.vdaf) vdaf,avg(r.qnet_ar) qnet_ar,'' huayy,'' lury\n");
		bf.append(" from rulmzlb r ,jizfzb j,rulbzb b\n");
		bf.append(" where r.rulbzb_id=b.id and r.jizfzb_id=j.id \n");
		bf.append(" and r.rulrq="+DateUtil.FormatOracleDate(this.getBRiq())+"\n");
		bf.append(" and r.diancxxb_id="+this.getTreeid_dc()+"\n");
		bf.append(" group by grouping sets((),j.mingc) \n");
		
		bf.append(" ) order by jiz asc,xuh asc\n");
	
		return bf;
	}
	//煤质表
	private String getTableMeiz(){
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		ResultSetList rstmp = con.getResultSetList(getMeizsql().toString());
		Report rt = new Report();
		ResultSetList rs = null;
		String[][] ArrHeader = null;
		String[] strFormat = null;
		int[] ArrWidth = null;
		
		rs = rstmp;

		ArrHeader = new String[][] { {"皮带/炉号","班次","Mad","Aad","Vad","Mt","Qb","Stad","Had","Vdaf","Qnet","化验员","录入员"} };

		ArrWidth = new int[] { 80, 60, 55,55, 55,  55, 55, 55, 55, 55, 55,55,55 };
		strFormat=new String[]{"","","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.000","",""};
		rt.setTitle(getRptTitle(con, 3), ArrWidth);
//		rt.title.fontSize = 12;
//		rt.title.setRowHeight(2, 50);
//		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
////		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
//		rt.setDefaultTitle(1, 13, this.getBRiq(), Table.ALIGN_CENTER);
		
		rt.title.fontSize=12;
		rt.title.setRowHeight(2, 50);
		rt.setDefaultTitle(1, 13, getBRiq() , Table.ALIGN_CENTER);

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.setColAlign(7, Table.ALIGN_CENTER);
		rt.body.setColAlign(8, Table.ALIGN_CENTER);
		rt.body.setColAlign(9, Table.ALIGN_CENTER);
		rt.body.setColAlign(10, Table.ALIGN_CENTER);
		rt.body.setColAlign(11, Table.ALIGN_CENTER);
		rt.body.setColAlign(12, Table.ALIGN_CENTER);
		rt.body.setColAlign(13, Table.ALIGN_CENTER);

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setColFormat(strFormat);
		rt.body.setPageRows(-1);
//		rt.body.mergeFixedCols();
//		rt.body.mergeFixedRow();


		rt.createFooter(1, ArrWidth);
		rt.setDefautlFooter(1, 2, "校阅：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 2, "班长：", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize = 10;
		// rt.footer.setRowHeight(1, 1);
		con.Close();
		if (rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setFontSize(10);
		rt.body.setRowHeight(25);
		return rt.getAllPagesHtml();// ph;
	}
	
//	格式下拉框
	public IDropDownBean getGesValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean4()==null) {
			if(getGesModel().getOptionCount()>0) {
				setGesValue((IDropDownBean)getGesModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean4();
	}
	public void setGesValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean4(value);
	}
	
	public IPropertySelectionModel getGesModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel4()==null) {
			setGesModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel4();
	}
	public void setGesModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel4(value);
	}
	
	public void setGesModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		
		List list=new ArrayList();
//		list.add(new IDropDownBean("1","全部"));
		list.add(new IDropDownBean("0","全部"));
		list.add(new IDropDownBean("1","煤粉细度"));
		list.add(new IDropDownBean("2","煤灰分析"));
		list.add(new IDropDownBean("3","煤质分析"));
		setGesModel(new IDropDownModel(list));
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
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
		
			this.setGesValue(null);
			this.setGesModel(null);
			
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
