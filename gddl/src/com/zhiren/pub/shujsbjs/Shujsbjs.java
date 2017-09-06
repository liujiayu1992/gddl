package com.zhiren.pub.shujsbjs;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.webservice.InterCom;

public class Shujsbjs extends BasePage {
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
	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getJianc();//检查结果
	}
	private String getJianc() {
		JDBCcon con = new JDBCcon();
		String Strrenwmc="";
		String Strzhuangt="";
		
		if(getshujValue().getId()!=-1){
			Strrenwmc="and  renwmcb.renwms='"+getshujValue().getValue()+"'";
		}
		if(getzhuangtValue().getId()!=-1){
			Strzhuangt=" where zhuangt='"+getzhuangtValue().getValue()+"'";
		}
		
		String mothStr="getZhuangt(to_date('"+getRiq1().substring(0, 7)+"-01"+"','yyyy-mm-dd'),leix,diancxxb_id,s.pinl,s.helys)";//2008-08-01
		String yearStr="getZhuangt(to_date('"+getRiq1().substring(0, 4)+"-01-01"+"','yyyy-mm-dd'),leix,diancxxb_id,s.pinl,s.helys)";//2008-01-01
		String Str="getZhuangt(to_date('"+getRiq1()+"','yyyy-mm-dd'),leix,diancxxb_id,s.pinl,s.helys)";
		
		String sql=
			"select * from ("+
			"select diancxxb.mingc,renwmcb.renwms,s.pinl,decode(s.pinl,'月',"+mothStr+",'年',"+yearStr+","+Str+")zhuangt,''beiz--,vwZhuangt(s.helys,diancxxb_id,leix,riq),vwBeiz(s.helys,diancxxb_id,leix,riq)\n" +
			"from renwmcb,diancxxb,shangcsjbzb s\n" + 
			"where renwmcb.renw=s.leix and diancxxb.id=s.diancxxb_id and diancxxb_id in("+
			"select id from diancxxb\n" +
			"start with id="+getDiancmcValue().getId()+"\n" + 
			"connect by fuid=prior id"+
			")" + Strrenwmc+
			")"+Strzhuangt;
		ResultSet rs = con.getResultSet(new StringBuffer(sql),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		
		ArrHeader = new String[1][7];
		ArrWidth = new int[] {150,100,100,100,350};//单位", "任务名称","频率","状态","备注"
		ArrHeader[0] = new String[] { "单位","任务名称","交换频率","上报数据情况","备注"};
		rt.setBody(new Table(rs, 1, 0, 1));
		//
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.setTitle("上　报　数　据　检　查", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 3, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.body.setPageRows(21);
		rt.body.mergeFixedCols();
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 5, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		

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
	private boolean ShangcButton = false;

	public void ShangcButton(IRequestCycle cycle) {
		ShangcButton = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
		if (ShangcButton) {
//			ShangcButton = false;
//			InterCom jiek=new InterCom();
//			JDBCcon con=new JDBCcon();
//			String jiekzhb_id="";
//			String sql="select dianczhb.jiekzhb_id\n" +
//			"from dianczhb\n" + 
//			"where dianczhb.diancxxb_id="+getDiancmcValue().getId();
//			try {
//				ResultSet rs1=con.getResultSet(sql);
//				if(rs1.next()){
//					jiekzhb_id=rs1.getString(1);
//				}else{
//					return;
//				}
//				if(getshujValue().getId()==-1){//全部
////					jiek.getRenwall(jiekzhb_id,String.valueOf(getDiancmcValue().getId()));
//				}else{
//					String renwmc="";
//				
//					ResultSet rs=con.getResultSet("select renw from renwmcb where renwms='"+getshujValue().getValue()+"'");
//					
//						if(rs.next()){
//							renwmc=rs.getString(1);
//						}
//					jiek.getRenw(jiekzhb_id,String.valueOf(getDiancmcValue().getId()),renwmc );
//					
//				}
//			} catch (SQLException e) {
//				// TODO 自动生成 catch 块
//				e.printStackTrace();
//			}finally{
//				con.Close();
//			}
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
		public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
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
			visit.setDefaultTree(null);
		}
		blnIsBegin = true;
		if(chang1){
//			getrenwModels();
			chang1=false;
		}
		if(chang2){
//			getrenwModels();
			chang2=false;
		}
		if(_DiancmcChange){
//			getrenwModels();
			_DiancmcChange=false;
		}

	}
		public IPropertySelectionModel getshujModel() {
			if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
				getshujModels();
			}
			return ((Visit)getPage().getVisit()).getProSelectionModel1();
		}
		
		public IDropDownBean getshujValue() {
			if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
				((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getshujModel().getOption(0));
			}
			return ((Visit)getPage().getVisit()).getDropDownBean1();
		}
		
		public void setshujValue(IDropDownBean Value) {
			((Visit)getPage().getVisit()).setDropDownBean1(Value);
		}

		public IPropertySelectionModel getshujModels() {
			List list=new ArrayList();
			list.add(new IDropDownBean(-1,"全部") );
			String sql = 
				"select id, renwms\n" +
				"from renwmcb\n" + 
				"where fuid=0\n" + 
				"order by id";
			setrenwModel(new IDropDownModel(list,sql)) ;
			return ((Visit) getPage().getVisit()).getProSelectionModel1();
		}

		public void setrenwModel(IPropertySelectionModel _value) {
			((Visit)getPage().getVisit()).setProSelectionModel1(_value);
		}
		//电厂名称
		private IPropertySelectionModel _IDiancModel;
		public IPropertySelectionModel getDiancmcModel() {
			if (((Visit)getPage().getVisit()).getProSelectionModel2() == null) {
				getDiancmcModels();
			}
			return ((Visit)getPage().getVisit()).getProSelectionModel2();
		}
		
		private boolean _DiancmcChange=false;
		public IDropDownBean getDiancmcValue() {
			if(((Visit)getPage().getVisit()).getDropDownBean2()==null){
				((Visit)getPage().getVisit()).setDropDownBean2((IDropDownBean)getDiancmcModel().getOption(0));
			}
			return ((Visit)getPage().getVisit()).getDropDownBean2();
		}
		
		public void setDiancmcValue(IDropDownBean Value) {
			if (((Visit)getPage().getVisit()).getDropDownBean2()==Value) {
				_DiancmcChange = false;
			}else{
				_DiancmcChange = true;
			}
			((Visit)getPage().getVisit()).setDropDownBean2(Value);
		}

		public IPropertySelectionModel getDiancmcModels() {
			String sql = 
				"select id,mingc from diancxxb where id in(\n" +
				"   select id from diancxxb\n" + 
				"    start with id=" +((Visit) getPage().getVisit()).getDiancxxb_id()+ 
				"     connect by fuid=prior id\n" + 
				")";
//				"select id,mingc\n" +
//				"from diancxxb\n" + 
//				"where jib=3\n" + 
//				"order by diancxxb.fuid";
//			    List list=new ArrayList();
//		        list.add(new IDropDownBean(-1,"全部"));
			 setDiancmcModel(new IDropDownModel(sql)) ;
			 return ((Visit) getPage().getVisit()).getProSelectionModel2();
		}

		public void setDiancmcModel(IPropertySelectionModel _value) {
			((Visit)getPage().getVisit()).setProSelectionModel2(_value);
		}
		private String riq1;
		private boolean chang1,chang2;
		public String getRiq1(){
			if(riq1==null||riq1.equals("")){
				riq1=DateUtil.FormatDate(new Date());
			}
			return riq1;
		}
		public void setRiq1(String value){
			if(!value.equals(riq1)){
				chang1=true;
			}
			riq1=value;
		}
  
		 //类型
	    public IDropDownBean getzhuangtValue() {
	    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
	   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getzhuangtModel().getOption(0));
	   	}
	       return  ((Visit) getPage().getVisit()).getDropDownBean4();
	    }
	    public void setzhuangtValue(IDropDownBean Value) {
		    	((Visit) getPage().getVisit()).setDropDownBean4(Value);
	    }
	    public void setzhuangtModel(IPropertySelectionModel value) {
	    	((Visit) getPage().getVisit()).setProSelectionModel4(value);
	    }

	    public IPropertySelectionModel getzhuangtModel() {
	        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
	            getzhuangtModels();
	        }
	        return ((Visit) getPage().getVisit()).getProSelectionModel4();
	    }
	    public void getzhuangtModels() {
	        List list=new ArrayList();
	        list.add(new IDropDownBean(-1,"全部"));
	        list.add(new IDropDownBean(0,"成功"));
	        list.add(new IDropDownBean(1,"出错"));
	        list.add(new IDropDownBean(2,"待收"));
	        list.add(new IDropDownBean(3,"无数据"));
	        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(list)) ;
	        return ;
	    }
   
}
