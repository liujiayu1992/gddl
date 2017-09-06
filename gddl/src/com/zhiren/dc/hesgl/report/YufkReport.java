package com.zhiren.dc.hesgl.report;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.PreparedStatement;
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
import com.zhiren.common.ext.ToolbarItem;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.Money;
import com.zhiren.common.ResultSetList;

/*
 * 作者：陈泽天
 * 时间：2010-01-21 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
public class YufkReport extends BasePage{
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
	private int _CurrentPage1 = -1;

	public int getCurrentPage1() {
		return _CurrentPage1;
	}
	public void setCurrentPage1(int _value) {
		_CurrentPage1 = _value;
	}

	private int _AllPages1 = -1;

	public int getAllPages1() {
		return _AllPages1; 
	}

	public void setAllPages1(int _value) {
		_AllPages1 = _value;
	}
	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	
	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			chaxunzt1 = 1;// 查询状态
			zhuangt=2;
			Refurbish();
			_QueryClick = false;
		}
		if (_SaveChick) {
			ruz=true;
			_SaveChick = false;
            Save();

		}
		
	}
	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
//		getYufkReport();
	}
//	private boolean blnIsBegin = false;
//
//	public String getPrintTable() {
//		if (!blnIsBegin) {
//			return "";
//		}
//		blnIsBegin = false;
//		
//		 return getYufk();
//		
//	}
	private String RT_CHANGFJSTJ = "changfjstj";

	private String mstrReportName = "changfjstj";

	public String getPrintTable() {
		if (mstrReportName.equals(RT_CHANGFJSTJ)) {
			return getYufkReport();
		} else {
			return "无此报表";
		}
	}
	private int chaxunzt1 = 0;// 查询状态
	private int zhuangt =1;
	public String getYufkReport(){
		Visit visit = (Visit) getPage().getVisit();
		if(chaxunzt1 == 1){
			chaxunzt1 = 2;
			return "";
		}else if(chaxunzt1 == 2){
		JDBCcon con = new JDBCcon();
		StringBuffer buffer = new StringBuffer("");
		Money money=new Money();
		Report rt = new Report();
		Report rt1 = new Report();
		try{
		buffer.append("select yfk.bianh as bianh,gys.mingc as gongys,gys.bianm as gysbm,skdw.mingc as skdw,nvl(yfk.jine,0) as jine,yfk.kaihyh as kaihyh,yfk.zhangh as zhangh,yfk.beiz as beiz" 
                       +" from yufkb yfk,gongysb gys,shoukdw skdw" 
                       +" where yfk.gongysb_id=gys.id and yfk.shoukdwb_id=skdw.id" );
		buffer.append(" and yfk.bianh='"+getBianhDropDownValue().getValue()+"'");

		ResultSet rs = con.getResultSet(buffer,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		String jine1="";
		String bianh="";
		String gongys="";
		String gysbm="";
		String skdw="";
		String kaihyh="";
		String zhangh="";
		String beiz="";
		String xiao="";
		String da="";
		double jine2=0;
		if(rs.next()){
			 bianh=rs.getString("bianh");
			 gongys=rs.getString("gongys");
			 gysbm=rs.getString("gysbm");
			 skdw=rs.getString("skdw");
			 kaihyh=rs.getString("kaihyh");
			 zhangh=rs.getString("zhangh");
			 beiz=rs.getString("beiz");
			 jine1=rs.getString("jine");
			 if(!jine1.equals("")){
					
					jine2=Double.parseDouble(jine1);
					 da=money.NumToRMBStr(jine2);
					xiao=jine1;
					
			}else{
				da="";
				xiao="";
			}
			 
		 }
		
//		jine2=Double.parseDouble(jine1);
//		xiao=money.PositiveIntegerToHanStr(jine1);
		
		String[][] ArrHeader;
		int[] ArrWidth;
		 ArrHeader=new String[4][6];
		 //ArrHeader[0]=new String[] {"燃料采购统一预付款通知单","","","","",""};
		 //ArrHeader[1]=new String[] {"填制单位:","燃料部","","","编号:"+bianh,""};
		 ArrHeader[0]=new String[] {"供应商",gongys,"供应商编码:"+gysbm,"收款单位:",skdw,"第一联财务部门留存"};
		 ArrHeader[1]=new String[] {"预付金额:","合计(小写):",xiao,"开户银行:",kaihyh,""};
		 ArrHeader[2]=new String[] {"        ","合计(大写):",da,"银行帐号:",zhangh,""};
		 ArrHeader[3]=new String[] {"备注:",beiz,"","","",""};
		 //ArrHeader[4]=new String[] {"经办人","","主管:","","厂长:",""};

		 ArrWidth=new int[] {60,170,225,60,230,20};
          


//		 定义页Title
		 
		 rt.setBody(new Table(ArrHeader,0,0,0));
		 
		 int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString6());//取得报表纸张类型
		 rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		 rt.setTitle("燃料采购统一预付款通知单",ArrWidth);
		 rt.setDefaultTitleLeft("填制单位：燃料部",3);
		 rt.setDefaultTitle(5, 1, "编号: "+bianh, Table.ALIGN_RIGHT);
		 
		// ArrHeader[1]=new String[] {"填制单位:","燃料部","","","编号:"+bianh,""};
		 //rt.setDefaultTitleLeft("日期",1);
		// rt.setDefaultTitleRight("编号：",1);
		    rt.title.setRowHeight(2, 50);
		    rt.title.setWidth(ArrWidth);
		    
		    rt.body.setBorder(0, 0, 0, 0);
		 	rt.body.setCells(4, 2, 4, 2, Table.PER_BORDER_RIGHT, 0);
		 	rt.body.setCells(1, 1, 4, 1, Table.PER_BORDER_LEFT, 2);
		 	rt.body.setCells(1, 1, 1, 5, Table.PER_BORDER_TOP, 2);
		 	rt.body.setCells(1, 5, 4, 5, Table.PER_BORDER_RIGHT, 0);
		 	rt.body.setCells(4, 1, 4, 5, Table.PER_BORDER_BOTTOM, 2);
		 	rt.body.setCells(1, 6, 4, 6, Table.PER_BORDER_BOTTOM, 0);
		 	rt.body.setCells(1, 6, 4, 6, Table.PER_BORDER_LEFT, 2);
			rt.body.setCells(1, 6, 4, 6, Table.PER_BORDER_TOP, 0);
			rt.body.setCells(1, 6, 4, 6, Table.PER_BORDER_RIGHT, 0);
			
			
//			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		    rt.createDefautlFooter(ArrWidth);
		    rt.body.setWidth(ArrWidth);
		    rt.body.setPageRows(21);
		    rt.body.setRowHeight(50);
		    rt.body.setRowHeight(4, 90);
		    rt.body.mergeFixedRowCol();
			
			//rt.setDefautlFooter(1, 25, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
			rt.setDefautlFooter(1, 1, "经办人:", Table.ALIGN_LEFT);
		    rt.setDefautlFooter(3, 1, "主管:", Table.ALIGN_LEFT);
		    rt.setDefautlFooter(5, 1, "厂长:", Table.ALIGN_LEFT);
		   // rt.setDefaultTitleCenter("主管:");
		    //rt.setDefaultTitleRight("厂长:", 3);
//		 合并单元格
		    rt.body.mergeCell(1,6,4,6);
		    rt.body.mergeCell(2,1,3,1);
		    rt.body.mergeCell(4,2,4,5);
		    
		    _CurrentPage = 1;
			_AllPages = rt.body.getPages();
			
			if (_AllPages == 0) {
				_CurrentPage = 0;
			}
		    //////
			String[][] ArrHeader1;
			int[] ArrWidth1;
			 ArrHeader1=new String[4][6];
			 //ArrHeader[0]=new String[] {"燃料采购统一预付款通知单","","","","",""};
			 //ArrHeader[1]=new String[] {"填制单位:","燃料部","","","编号:"+bianh,""};
			 ArrHeader1[0]=new String[] {"供应商",gongys,"供应商编码:"+gysbm,"收款单位:",skdw,"第二联燃料部门留存"};
			 ArrHeader1[1]=new String[] {"预付金额:","合计(小写):",xiao,"开户银行:",kaihyh,""};
			 ArrHeader1[2]=new String[] {"        ","合计(大写):",da,"银行帐号:",zhangh,""};
			 ArrHeader1[3]=new String[] {"备注:",beiz,"","","",""};
			 //ArrHeader[4]=new String[] {"经办人","","主管:","","厂长:",""};

			  ArrWidth1=new int[] {60,170,225,60,230,20};
	          
		    rt1.setBody(new Table(ArrHeader1,0,0,0));
			 
		    int aw1=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString6());//取得报表纸张类型
		    rt1.getArrWidth(ArrWidth1, aw1);//添加报表的A4控制
			 rt1.setTitle("燃料采购统一预付款通知单",ArrWidth1);
			 rt1.setDefaultTitleLeft("填制单位：燃料部",3);
			 rt.setDefaultTitle(5, 1, "编号: "+bianh, Table.ALIGN_RIGHT);
			 
			 
			    rt1.title.setRowHeight(2, 50);
//			    int aw1=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),pagewith);//取得报表纸张类型
//				rt1.getArrWidth(ArrWidth1, aw1);//添加报表的A4控制
			    rt1.title.setWidth(ArrWidth1);
			    
			    rt1.body.setBorder(0, 0, 0, 0);
			 	rt1.body.setCells(4, 2, 4, 2, Table.PER_BORDER_RIGHT, 0);
			 	rt1.body.setCells(1, 1, 4, 1, Table.PER_BORDER_LEFT, 2);
			 	rt1.body.setCells(1, 1, 1, 5, Table.PER_BORDER_TOP, 2);
			 	rt1.body.setCells(1, 5, 4, 5, Table.PER_BORDER_RIGHT, 0);
			 	rt1.body.setCells(4, 1, 4, 5, Table.PER_BORDER_BOTTOM, 2);
			 	rt1.body.setCells(1, 6, 4, 6, Table.PER_BORDER_BOTTOM, 0);
			 	rt1.body.setCells(1, 6, 4, 6, Table.PER_BORDER_LEFT, 2);
				rt1.body.setCells(1, 6, 4, 6, Table.PER_BORDER_TOP, 0);
				rt1.body.setCells(1, 6, 4, 6, Table.PER_BORDER_RIGHT, 0);
				
				
//				rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			
				rt1.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			    rt1.createDefautlFooter(ArrWidth1);
			    rt1.body.setWidth(ArrWidth1);
			    rt1.body.setPageRows(21);
			    rt1.body.setRowHeight(50);
			    rt1.body.setRowHeight(4, 90);
			    rt1.body.mergeFixedRowCol();
				
				//rt.setDefautlFooter(1, 25, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
				rt1.setDefautlFooter(1, 1, "经办人:", Table.ALIGN_LEFT);
			    rt1.setDefautlFooter(3, 1, "主管:", Table.ALIGN_LEFT);
			    rt1.setDefautlFooter(5, 1, "厂长:", Table.ALIGN_LEFT);
			    rt1.body.mergeCell(1,6,4,6);
			    rt1.body.mergeCell(2,1,3,1);
			    rt1.body.mergeCell(4,2,4,5);
			   // rt.setDefaultTitleCenter("主管:");
		 _CurrentPage1 = 1;
		
			_AllPages1 = rt1.body.getPages();
			if (_AllPages1 == 0) {
				_CurrentPage1 = 0;
			}
			//con.Close();
			//return rt.getAllPagesHtml();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return rt.getAllPagesHtml()+"\n"+rt1.getAllPagesHtml();
		}else{
			return "";
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
	public String ruzrq(){
		if(getLeixSelectValue().getId()==1){
			return " is not null";
		}else{
			return " is null";
		}
	}
	
	private boolean ruz=false;//入帐
	private void getToolbars(){
		Visit visit = (Visit) getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		if(isreport){
			
			//日期
			tb1.addText(new ToolbarText("查询日期:"));
			DateField df = new DateField();
			df.setValue(this.getBeginriqDate());
			df.Binding("BeginTime","forms[0]");// 与html页中的id绑定,并自动刷新
			df.setWidth(80);
			tb1.addField(df);
			
			DateField df1 = new DateField();
			df1.setValue(this.getEndriqDate());
			df1.Binding("EndTime","forms[0]");// 与html页中的id绑定,并自动刷新
			df1.setWidth(80);
			tb1.addField(df1);
			tb1.addText(new ToolbarText("-"));
			
			tb1.addText(new ToolbarText("供应商:"));
			ComboBox cb2 = new ComboBox();
			cb2.setId("gongyscb");
			cb2.setTransform("GongysDropDown");
			cb2.setEditable(true);
			cb2.setListeners(
					"select:function(own,newValue,oldValue){"+
	                " document.forms[0].submit();}" );
			tb1.addField(cb2);
			
			tb1.addText(new ToolbarText("-"));
			
			
			
//			tb1.addText(new ToolbarText("入账状态:"));
//			ComboBox cb1 = new ComboBox();
//			cb1.setTransform("LeixSelect");
//			cb1.setId("Leixcb");
//			cb1.setWidth(80);
//			cb1.setListeners(
//					"select:function(own,newValue,oldValue){"+
//	                " document.forms[0].submit();}" );
//			tb1.addField(cb1);
//			
//			tb1.addText(new ToolbarText("-"));
		
			tb1.addText(new ToolbarText("编号:"));
			ComboBox cb3 = new ComboBox();
			cb3.setId("bianhcb");
			cb3.setTransform("BianhDropDown");
			cb3.setEditable(true);
			cb3.setListeners(
					"select:function(own,newValue,oldValue){"+
			        " document.forms[0].submit();}" );
			tb1.addField(cb3);
			
			tb1.addText(new ToolbarText("-"));
			ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
			tb1.addItem(tb);
			setToolbar(tb1);
			if(((Visit) getPage().getVisit()).getboolean4()){
				getBianhDropDownValue().setValue(null);
	        	((Visit) getPage().getVisit()).setboolean4(false);
	        }
			
		}else{
//			isreport=true;
//			ToolbarButton tb2 = new ToolbarButton(null, "入帐",
//			"function(){ document.Form0.SaveButton.click();}");
//	        tb1.addItem(tb2);
//	        setToolbar(tb1);
	        
		}	
		
	}

    private String getid=null;
	private void Save() {  //入帐方法
		Visit visit = new Visit();
		JDBCcon con = new JDBCcon();
		String sql = "";
		try {
			sql = "update yufkb y set ruzrq=to_date('" + DateUtil.FormatDate(new Date())
					+ "','yyyy-mm-dd'),ruzry=" + visit.getRenymc()
					+ " where  y.id=" + getid;
			System.out.println(sql.toString());
			con.getUpdate(sql);
			((Visit) getPage().getVisit()).setProSelectionModel5(null);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}	
	
	
	//得到编号
	private String getBh(String idStr){
		int id=Integer.parseInt(idStr);
        String bianh=null;
		JDBCcon con = new JDBCcon();
		PreparedStatement ps= null;
		ResultSet rs=null;
		ps=con.getPresultSet("select * from  yufkb where id="+id);
		
		try {
			rs = ps.executeQuery();			
            while(rs.next()){
            	bianh=rs.getString("BIANH");
            }

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
		        ps.close();
		        con.Close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
        return bianh;
	}
	
	private boolean isreport=true; //判断是否为预付款通知页
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		
		//begin方法里进行初始化设置
		visit.setString6(null);

			String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
			if (pagewith != null) {

				visit.setString6(pagewith);
			}
		//	visit.setString6(null);保存传递的非默认纸张的样式
			
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			
							
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);			
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);			
			
			visit.setboolean1(false);
			visit.setboolean2(false);
			visit.setboolean3(false);
			visit.setboolean4(false);
			
			((Visit) getPage().getVisit()).setString1("");
			
			zhuangt=visit.getInt1();
			
		}
	
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			getBianhDropDownValue().setValue(getBh(visit.getString1()));
			isreport=false;
			if(((Visit) getPage().getVisit()).getboolean4()==false){
				((Visit) getPage().getVisit()).setboolean4(true);
			}		
        }
		
		getid=visit.getString1();
		
		if(zhuangt==1){//不要动
			visit.setInt1(1);
		}
		if(zhuangt==2){//不要动
			visit.setInt1(2);
		}
		getToolbars();
		//blnIsBegin = true;
		zhuangt=1;
		chaxunzt1 = 2;
		
		
		if(visit.getboolean1()){		//时间
			
			this.setGongysDropDownModel(null);
			this.setGongysDropDownValue(null);
			this.setBianhDropDownModel(null);
			this.setBianhDropDownValue(null);
			this.getGongysDropDownModels();
			this.getBianhDropDownModels();
			visit.setboolean1(false);

		}
		if(visit.getboolean2()){		//供应商

			this.setBianhDropDownModel(null);
			this.setBianhDropDownValue(null);
			this.getBianhDropDownModels();
			visit.setboolean2(false);
		}
		if(visit.getboolean3()){		//入帐状态
				
			this.setBianhDropDownModel(null);
			this.setBianhDropDownValue(null);
			this.getBianhDropDownModels();
			visit.setboolean3(false);
		}
	}

    // 供应商
	
    public IDropDownBean getGongysDropDownValue() {
    	
    	if(((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 	
   		((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getGongysDropDownModel().getOption(0));

    	}
        return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }
    
    public void setGongysDropDownValue(IDropDownBean Value) {
    	
    	if(getGongysDropDownValue()!=Value){
    		
    		((Visit) getPage().getVisit()).setboolean2(true);

    	}

	    ((Visit) getPage().getVisit()).setDropDownBean2(Value);
    }
    
    public void setGongysDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel getGongysDropDownModel() {
    	
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
        	
            getGongysDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }
    public void getGongysDropDownModels() {
    	String sql="select gongysb.id,gongysb.mingc				\n" +
    			" from gongysb,yufkb			\n" + 
    			" where gongysb.id=yufkb.gongysb_id " +
    			" and gongys.leix=1 " +
    			" and riq>=to_date('"+getBeginriqDate()+"','yyyy-MM-dd') and riq<=to_date('"+getEndriqDate()+"','yyyy-MM-dd')	\n" +
    			" order by gongysb.mingc";
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"全部")) ;
        return ;
    } 
    ////编号

	
    public IDropDownBean getBianhDropDownValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean5()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean)getBianhDropDownModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean5();
    }
    public void setBianhDropDownValue(IDropDownBean Value) {
//    	if(getBianhDropDownValue()!=Value){
//    		
//    		System.out.println(getBianhDropDownValue());
//    		System.out.println(Value);
//    	}

	    ((Visit) getPage().getVisit()).setDropDownBean5(Value);

    }
    public void setBianhDropDownModel(IPropertySelectionModel value) {
    	
    	((Visit) getPage().getVisit()).setProSelectionModel5(value);
    }

    public IPropertySelectionModel getBianhDropDownModel() {
        
    	if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
            
        	getBianhDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel5();
    }
    public void getBianhDropDownModels() {
    	
    	String sql="";
    	if(getGongysDropDownValue().getId()==-1){
    		sql="select yufkb.id,yufkb.bianh\n" +
        	" from yufkb,gongysb " +
        	" where gongysb.id=yufkb.gongysb_id " +
    		" and riq>=to_date('"+getBeginriqDate()+"','yyyy-MM-dd') and riq<=to_date('"+getEndriqDate()+"','yyyy-MM-dd')	\n" +
    		" and yufkb.ruzrq "+ruzrq()+
        	" order by yufkb.bianh";
    	}else{
    		sql="select yufkb.id,yufkb.bianh\n" +
        	" from yufkb,gongysb " +
        	" where gongysb.id=yufkb.gongysb_id " +
    		" and riq>=to_date('"+getBeginriqDate()+"','yyyy-MM-dd') and riq<=to_date('"+getEndriqDate()+"','yyyy-MM-dd')	\n" +
        	" and yufkb.gongysb_id="+getGongysDropDownValue().getId()+
        	" and yufkb.ruzrq "+ruzrq()+
    		" order by yufkb.bianh";
    	}
    	
        ((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(sql,"请选择")) ;
        return ;
    } 
	// 日期
    public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
				Calendar stra=Calendar.getInstance();
//				stra.set(DateUtil.getYear(new Date()), 0, 1);
				stra.setTime(new Date());
				stra.add(Calendar.DATE,-1);
				((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		
		if(!((Visit) getPage().getVisit()).getString4().equals(value)){
			
			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setString4(value);
//			this.getGongysDropDownModels();
		}
	}
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));

		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		
		if(!((Visit) getPage().getVisit()).getString5().equals(value)){
			
			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setString5(value);
		}
	}
   
    //类型
    public IDropDownBean getLeixSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getLeixSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean4();
    }
    public void setLeixSelectValue(IDropDownBean Value) {
    	if(getLeixSelectValue()!=Value){
    		
    		((Visit) getPage().getVisit()).setboolean3(true);
    		
    	}
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
        list.add(new IDropDownBean(1,"已入账"));
        list.add(new IDropDownBean(2,"未入账"));
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(list)) ;
        return ;
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

}