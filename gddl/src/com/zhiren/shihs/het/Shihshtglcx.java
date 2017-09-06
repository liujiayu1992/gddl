package com.zhiren.shihs.het;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.jt.het.hetcx.Hetcxbean;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Shihshtglcx extends BasePage implements PageValidateListener {

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
	public int getZhuangt() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	public void setZhuangt(int _value) {
		((Visit) getPage().getVisit()).setInt1(_value);
	}
//	private int _Flag = 0;
//
//	public int getFlag() {
//		JDBCcon con = new JDBCcon();
//		String sql = "SELECT ZHUANGT FROM JICXXB WHERE MINGC = '报表中的厂别是否显示'";
//		ResultSet rs = con.getResultSet(sql);
//		try {
//			if (rs.next()) {
//				_Flag = rs.getInt("ZHUANGT");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return _Flag;
//	}
//
//	public void setFlag(int _value) {
//		_Flag = _value;
//	}

	private String mstrReportName = "";
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		//if (mstrReportName.equals(REPORT_NAME_Hetltj)) {
		return getHetcx();
		//} else {
		//	return "无此报表";getTreeScript
		//}
	}
	
	private String dateformatUtil(String s){
		try{
			SimpleDateFormat sf=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			
			Date d=sf.parse(s);
			
			sf.applyPattern("yyyy-MM-dd");
			String res=sf.format(d);
			
			return res;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "";// 日期转换有误，返回空时间串
	}
	private String getHetcx() {
		
		
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		ArrHeader = new String[1][9];
		ArrWidth = new int[] { 130, 180, 250,60,60,60,60,60,70};
		
		
		
		JDBCcon con = new JDBCcon();
		StringBuffer buffer = new StringBuffer("");
	
		String hetbhCondi="";
		String hetlCondi="";
	//	String weizCondi="";
	//	String leixCondi="";
		String gongfCondi="";
		String xufCondi="";
		
		if(gethetbh()!=null&&!gethetbh().equals("")){
			hetbhCondi=" and  a.bianh like '%"+gethetbh()+"%' ";
		}
		if(gethetl1()!=null&&gethetl2()!=null&&!gethetl1().equals("")&&!gethetl2().equals("")){
			hetlCondi=" and a.hetsl>="+gethetl1()+" and a.hetsl<="+gethetl2();
		}
		//if(getweizSelectValue().getId()!=-1){
		//	weizCondi=" and a.leibztb_id="+getweizSelectValue().getId();
		//}
//		if(getTreeid()==null){
//			setTreeid(String.valueOf(0));
//		}
		if(Long.parseLong(getTreeid())!=-1){
			xufCondi=" and a.diancxxb_id "+
				"in (select id\n" +
				"from(\n" + 
				"select id from diancxxb\n" + 
				"start with (fuid="+Long.parseLong(getTreeid())+" or shangjgsid="+Long.parseLong(getTreeid())+") \n" + 
				"connect by fuid=prior id\n" + 
				")\n" + 
				"union\n" + 
				"select id\n" + 
				"from diancxxb\n" + 
				"where id="+Long.parseLong(getTreeid())+")";
			
			
		}
//		}else{
//			
//		}
		
	//	if(getLeixSelectValue().getId()!=-1){
		//	leixCondi=" and a.leibid="+getLeixSelectValue().getId();
	//	}
		if(getGongysDropDownValue().getId()!=-1){
			gongfCondi=" and a.gongys_id "+
				"in( select id\n" +
				" from shihgysb\n" + 
				" where id="+getGongysDropDownValue().getId()+")";
		}
		
//		buffer.append("select hetb.hetbh,hetb.xufdwmc,hetb.gongfdwmc,sum(hetslb.hetl)hetl,to_char(hetb.qiandrq,'YYYY-MM-DD')qiandrq,jihkjb.mingc jihkj,liucztb.mingc weiz,decode(hetb.leib,0,'电厂合同','区域合同')leib,hetb.id\n");
//		buffer.append("from hetb,hetslb,jihkjb,liucztb\n" );
//		buffer.append("where hetb.id=hetslb.hetb_id(+) and hetb.jihkjb_id=jihkjb.id and hetb.liucztb_id=liucztb.id(+)\n");
//		buffer.append("group by  hetb.hetbh,hetb.xufdwmc,hetb.gongfdwmc,hetb.qiandrq,jihkjb.mingc,liucztb.mingc,hetb.id,hetb.leib");
//		//签订日期为必须
		buffer.append(" select * from( " );
		buffer.append(" select * from( ");
		buffer.append(" select sg.id gongys_id,ht.diancxxb_id, ht.id,ht.bianh,dc.quanc lurry,sg.quanc,ht.hetsl,ht.qiandrq ");
		buffer.append(" , decode(ht.shenhzt,1,'已审核',0,'未审核') shenh ");
		buffer.append(" , getHtmlAlert('"+MainGlobal.getHomeContext(this)+"','Shihsshrz','hetb_id',ht.id,ht.bianh) href ");
		buffer.append(" from shihhtb ht, shihgysb sg,diancxxb dc ");
		buffer.append(" where ht.shihgysb_id=sg.id and ht.diancxxb_id=dc.id  ");
		
		
	
		buffer.append(")a \n" ); 
		buffer.append(" where a.qiandrq>=to_date('"+getBeginriqDate()+"','yyyy-MM-dd')  and a.qiandrq<=to_date('"+getEndriqDate()+"','yyyy-MM-dd') "
				+hetbhCondi+hetlCondi+xufCondi+gongfCondi);
		
		buffer.append(" ) m, ");
		buffer.append(" (  select  zb.mingc bianm,sj.jij,sj.shihhtb_id, ");
		buffer.append(" decode(tj.mingc,'大于等于','>='||sj.xiax,'大于','>'||sj.xiax,'小于等于','<='||sj.shangx,'小于','<'||sj.shangx,'等于','='||sj.xiax,'区间','['||sj.xiax||'-'||sj.shangx||']') fanw ,dw.mingc danwmc");
		buffer.append(" from shihhtb ht,shihhtjg sj,zhibb zb, tiaojb tj ,danwb dw where ");
		buffer.append("  ht.id=sj.shihhtb_id and sj.zhibb_id=zb.id  ");
		buffer.append(" and sj.tiaojb_id=tj.id  and dw.id=sj.danwb_id ) n ");
		buffer.append(" where m.id=n.shihhtb_id(+)");
	
		
		
		
		ResultSet rs = con.getResultSet(buffer,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		
		int rowNum=100;
		if(rs != null){
			try {
				rs.last();
				
				rowNum=rs.getRow();

				rs.beforeFirst();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			
			
			
		}
		
		
		
		rt.setBody(new Table(rowNum+1, 9));
		try{
			
			while(rs.next()){
				
				
				int i=rs.getRow()+1;
				
				
//				rt.body.setCellValue(i, 1, "<a target=_blank href="+MainGlobal.getHomeContext(this)+"/app?service=page/Shenhrz&hetb_id="+rs.getString("id")+"&table_operation=shihsht"+">"+rs.getString("bianh")+"</a>");
			
				rt.body.setCellValue(i, 1, rs.getString("href"));
				rt.body.setCellAlign(i, 1, Table.ALIGN_CENTER);
				
				rt.body.setCellValue(i, 2, rs.getString("lurry"));
				rt.body.setCellAlign(i, 2, Table.ALIGN_CENTER);
			
				rt.body.setCellValue(i, 3, rs.getString("quanc"));
				rt.body.setCellAlign(i, 3, Table.ALIGN_CENTER);
				
				rt.body.setCellValue(i, 4, rs.getString("hetsl"));
				rt.body.setCellAlign(i, 4, Table.ALIGN_CENTER);
				
				
				

				rt.body.setCellValue(i, 5, rs.getString("jij"));
				rt.body.setCellAlign(i, 5, Table.ALIGN_CENTER);
			
			
			
				

				rt.body.setCellValue(i, 6,rs.getString("bianm"));
				rt.body.setCellAlign(i, 6, Table.ALIGN_CENTER);
				
				

				
				if(rs.getString("fanw")==null || rs.getString("danwmc").equals("")){
					
					rt.body.setCellValue(i, 7, "");
				}else{
					
					rt.body.setCellValue(i, 7, rs.getString("fanw")+"("+rs.getString("danwmc")+")");
				}
			//	rt.body.setCellValue(i, 7, rs.getString("fanw")+"("+rs.getString("danwmc")+")");
				rt.body.setCellAlign(i, 7, Table.ALIGN_CENTER);
				
				

				rt.body.setCellValue(i, 8, rs.getString("shenh"));
				rt.body.setCellAlign(i, 8, Table.ALIGN_CENTER);
				
				


				
				rt.body.setCellValue(i, 9, this.dateformatUtil(rs.getString("qiandrq")) );
				rt.body.setCellAlign(i, 9, Table.ALIGN_CENTER);
				
				
				
				
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	
		
		
		ArrHeader[0] = new String[] { "合同号", "需方单位名称", "供方单位名称","合同数量(吨)","价格<br>(元/吨)","指标名称","指标要求","审核状态","签订日期"};
		rt.body.setHeaderData(ArrHeader);
		
		
		rt.setTitle("合   同   查   询", ArrWidth);
		rt.title.setRowHeight(1, 30);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
//		rt.setDefaultTitle(1, 3, "检验日期:" +"",Table.ALIGN_LEFT);
		rt.body.setRowHeight(1, 35);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(21);
		//rt.body.mergeFixedCols();
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 10, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
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

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}
	private void getToolbars(){
		Visit visit = (Visit) getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		//签订日期
		tb1.addText(new ToolbarText("签订日期:"));
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
		//合同编号
		tb1.addText(new ToolbarText("合同编号:"));
		
		//cb.setListeners("select:function(){document.Form0.submit();}");
		TextField hetbh=new TextField();
		hetbh.setAllowBlank(true);
		hetbh.setId("ext_hetId");
		hetbh.setListeners(
				"change:function(own,newValue,oldValue){"+
                "document.all.item('hetbh').value=newValue;}"        		 	
		);
		tb1.addField(hetbh);
		//需方
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree"
				,""+visit.getDiancxxb_id(),"forms[0]",null,getTreeid());
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
		
		ToolbarButton tbb = new ToolbarButton(null,"刷新","function(){document.forms[0].submit()}");
		tb1.addItem(tbb);
		ToolbarButton gengdtj=new ToolbarButton(null,"更多条件","function(){Tiaojsz('xians')}");//更多条件
		tb1.addItem(gengdtj);
		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		
		
		
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel10(null);
			
			visit.setDefaultTree(null);
			visit.setInt1(2);
			visit.setString1("");
			visit.setString2("");
			visit.setString3("");
			visit.setString4("");
			visit.setString5("");
			
//			setTree(null);
			
//			visit.setDate2(new Date());
//			Calendar stra=Calendar.getInstance();
//			stra.set(DateUtil.getYear(new Date()), 0, 1);
//			visit.setDate1(stra.getTime());
		}
		getToolbars();
//		if (cycle.getRequestContext().getParameters("lx") != null) {
//			visit.getQuerylx().clear();
//			visit.getQuerylx().add(cycle.getRequestContext().getParameters("lx")[0]);
//			if (mstrReportName != "") {
//				if (!((String) visit.getQuerylx().get(0))
//						.equals(mstrReportName)) {
//					mstrReportName = (String) visit.getQuerylx().get(0);
//				}
//			}
//			mstrReportName = (String) visit.getQuerylx().get(0);
//		} else {
//			if (visit.getQuerylx().size() > 0) {
//				mstrReportName = (String) visit.getQuerylx().get(0);
//			}
//		}
		// mstrReportName = "Fenxrb";
		blnIsBegin = true;

	}

    // 供应商
    public IDropDownBean getGongysDropDownValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean1()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean1((IDropDownBean)getGongysDropDownModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean1();
    }
    public void setGongysDropDownValue(IDropDownBean Value) {
//    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean1()!=null){
//	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean1().getId()){
//	    		((Visit) getPage().getVisit()).setBooleanData1(true);
//	    	}else{
//	    		((Visit) getPage().getVisit()).setBooleanData1(false);
//	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean1(Value);
//    	}
    }
    public void setGongysDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel1(value);
    }

    public IPropertySelectionModel getGongysDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
            getGongysDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel1();
    }
    public void getGongysDropDownModels() {
    	String sql="select id,quanc\n" +
    	"from shihgysb\n" ;
    	//+ 
    //	"where gongysb.fuid=0";
        ((Visit) getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql,"全部")) ;
        return ;
    }
  
    //类型
    /*
    public IDropDownBean getLeixSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getLeixSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }
    public void setLeixSelectValue(IDropDownBean Value) {
//    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean2()!=null){
//	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean2().getId()){
//	    		((Visit) getPage().getVisit()).setBooleanData1(true);
//	    	}else{
//	    		((Visit) getPage().getVisit()).setBooleanData1(false);
//	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean2(Value);
//    	}
    }
    public void setLeixSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel getLeixSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            getLeixSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }
    public void getLeixSelectModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(-1,"全部"));
        list.add(new IDropDownBean(0,"电厂合同"));
        list.add(new IDropDownBean(1,"区域合同"));
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(list)) ;
        return ;
    }*/
    //需方
//    public IDropDownBean getxufValue() {
//    	if( ((Visit) getPage().getVisit()).getDropDownBean3()==null){
//    		 ((Visit) getPage().getVisit()).setDropDownBean3((IDropDownBean)getxufModel().getOption(0));
//    	}
//        return  ((Visit) getPage().getVisit()).getDropDownBean3();
//    }
//
//    public void setxufValue(IDropDownBean Value) {
//    	((Visit) getPage().getVisit()).setDropDownBean3(Value);
//    }
//
//
//    public void setxufModel(IPropertySelectionModel value) {
//    	((Visit) getPage().getVisit()).setProSelectionModel3(value);
//    }
//
//    public IPropertySelectionModel getxufModel() {
//        if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
//            getxufModels();
//        }
//        return ((Visit) getPage().getVisit()).getProSelectionModel3();
//    }
//
////    public void getxufModels() {//显示该用户单位下所有孩子
////    	String sql=
////    		"select id,mingc " +
////    		"from diancxxb\n" + 
////    		"where diancxxb.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+" or id="+((Visit) getPage().getVisit()).getDiancxxb_id();
////        ((Visit) getPage().getVisit()).setProSelectionModel19(new IDropDownModel(sql,"请选择")) ;
////        return ;
////    }
//    public void getxufModels() {
//   	 String sql = 
//        	"select id,mingc,jib\n" +
//        	"from(\n" + 
//        	" select id,mingc,0 as jib\n" + 
//        	" from diancxxb\n" + 
//        	" where id="+ ((Visit) getPage().getVisit()).getDiancxxb_id()+"\n" + 
//        	" union\n" + 
//        	" select *\n" + 
//        	" from(\n" + 
//        	" select id,mingc,level as jib\n" + 
//        	"  from diancxxb\n" + 
//        	" start with fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"\n" + 
//        	" connect by fuid=prior id\n" + 
//        	" order SIBLINGS by  xuh)\n" + 
//        	" )\n" + 
//        	" order by jib";
//        List dropdownlist = new ArrayList();
//		JDBCcon con = new JDBCcon();
//		try {
//			ResultSet rs = con.getResultSet(sql);
//			while (rs.next()) {
//				int id = rs.getInt("id");
//				String mc = rs.getString("mingc");
//				int jib=rs.getInt("jib");
//				String nbsp=String.valueOf((char)0xA0);
//				for(int i=0;i<jib;i++){
//					mc=nbsp+nbsp+nbsp+nbsp+mc;
//				}
//				dropdownlist.add(new IDropDownBean(id, mc));
//			}
//			rs.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			con.Close();
//		}
//        ((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(dropdownlist)) ;
//        return ;
//   }
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
	    //位置
		 /*    public IDropDownBean getweizSelectValue() {
	    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
	   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getweizSelectModel().getOption(0));
	   	}
	       return  ((Visit) getPage().getVisit()).getDropDownBean4();
	    }
	 
	    public void setweizSelectValue(IDropDownBean Value) {
	    	((Visit) getPage().getVisit()).setDropDownBean4(Value);
	    }
	    public void setweizSelectModel(IPropertySelectionModel value) {
	    	((Visit) getPage().getVisit()).setProSelectionModel4(value);
	    }

	   public IPropertySelectionModel getweizSelectModel() {
	        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
	            getweizSelectModels();
	        }
	        return ((Visit) getPage().getVisit()).getProSelectionModel4();
	    }*/
/*
	    public void getweizSelectModels() {
	    	String sql=
	    		"select *\n" +
	    		"from(\n" + 
	    		"select　leibztb.id,leibztb.mingc\n" + 
	    		"from leibztb,liuclbb\n" + 
	    		"where leibztb.liuclbb_id=liuclbb.id and liuclbb.mingc='合同'\n" + 
	    		"union\n" + 
	    		"select id,mingc\n" + 
	    		"from leibztb\n" + 
	    		"where leibztb.liuclbb_id=0\n" + 
	    		")";
	        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql,"全部")) ;
	        return ;
	    }
	    */
		public String gethetl1(){
			return  ((Visit) getPage().getVisit()).getString1();
		}
		public void sethetl1(String value){
			((Visit) getPage().getVisit()).setString1(value);
		}
		public String gethetl2(){
			return ((Visit) getPage().getVisit()).getString2();
		}
		public void sethetl2(String value){
			((Visit) getPage().getVisit()).setString2(value);
		}
		public String gethetbh(){
			return ((Visit) getPage().getVisit()).getString3();
		}
		public void sethetbh(String value){
			((Visit) getPage().getVisit()).setString3(value);
		}
		public String getBeginriqDate(){
			if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
					Calendar stra=Calendar.getInstance();
					stra.set(DateUtil.getYear(new Date()), 0, 1);
					((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
			}
			return ((Visit) getPage().getVisit()).getString4();
		}
		public void setBeginriqDate(String value){
			((Visit) getPage().getVisit()).setString4(value);
		}
		public String getEndriqDate(){
			if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
				((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
//				if(riqi==null||riqi.equals("")){
//					riqi=DateUtil.FormatDate(new Date());
//				}
//				visit.setDate2(new Date());
//				Calendar stra=Calendar.getInstance();
//				stra.set(DateUtil.getYear(new Date()), 0, 1);
//				visit.setDate1(stra.getTime());
			}
			return ((Visit) getPage().getVisit()).getString5();
		}
		public void setEndriqDate(String value){
			((Visit) getPage().getVisit()).setString5(value);
		}
//		ext 
	    //tree
//	    public String getTreeid() {
//			String treeid=((Visit) getPage().getVisit()).getString2();
//			if(treeid==null||treeid.equals("")){
//				((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
//			}
//			return ((Visit) getPage().getVisit()).getString2();
//		}
//		public void setTreeid(String treeid) {
//			((Visit) getPage().getVisit()).setString2(treeid);
//		}
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
//		public ExtTreeUtil getTree() {
//			return ((Visit) this.getPage().getVisit()).getExtTree1();
//		}
//		public void setTree(ExtTreeUtil etu) {
//			((Visit) this.getPage().getVisit()).setExtTree1(etu);
//		}
//		public String getTreeHtml() {
//			return getTree().getWindowTreeHtml(this);
//		}
		public String getTreeScript() {
			System.out.print(((Visit) this.getPage().getVisit()).getDefaultTree().getScript());
			return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
			
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
		public void pageValidate(PageEvent arg0) {
			// TODO 自动生成方法存根
			
		}

}
