package com.zhiren.jt.dtsx;

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
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/**
 * 
 * @author huochaoyuan
 * 陕西分公司日报程序用
 *（各厂来煤热值情况）
 */
/**
 * chenzt
 * 2010-04-06
 * 描述：修改河北分公司报表的制表人一项 ，并且设置为没有（null）
 */
/*
 * 作者：songy
 * 时间：2011-03-23 
 * 描述：修改下拉菜单的排序，要求按照名称进行排序
 */
public class Laimrzqk_fk extends BasePage {
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
		
		return getZhilcx();
		
	}

	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}
	
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	
	private String getZhilcx() {
		Visit visit=(Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String str = "";
		String strgys="";
		if(!getGongysDropDownValue().getValue().equals("全部")){
			strgys="and g.id="+getGongysDropDownValue().getId()+"\n";
		}
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (d.id = " + getTreeid() + " or d.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and d.id = " + getTreeid() + "";
			}
		}
		
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (d.id = " + getTreeid() + " or d.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and d.id = " + getTreeid() + "";
		}
		StringBuffer buffer = new StringBuffer("");


	    		buffer.append("select decode(g.mingc,null,'总计',g.mingc) as gysmc,decode(g.mingc,null,'总计',decode(m.mingc,null,'小计',m.mingc)) as mkmc,round(sum(dr.sldr),0)as lml,round(sum(dr.sldr*dr.rldr)/sum(dr.sldr),2) as frl,\n");
	    		buffer.append("round(sum(lj.sllj),0),round(sum(lj.sllj*lj.rllj)/sum(lj.sllj),2)\n"); 
	    		buffer.append("from\n"); 
	    		buffer.append("(select f.gongysb_id,f.meikxxb_id,sum(f.laimsl)as sldr,(sum(f.laimsl*z.qnet_ar)/sum(f.laimsl))as rldr\n"); 
	    		buffer.append("from fahb f,zhilb z,diancxxb d\n"); 
	    		buffer.append("where f.zhilb_id=z.id\n"); 
	    		buffer.append("and to_char(z.huaysj,'yyyy-mm-dd')='"+getBeginriqDate()+"'\n"); 
	    		buffer.append("and f.diancxxb_id=d.id(+)");
	    		buffer.append(str);
	    		buffer.append("group by f.gongysb_id,f.meikxxb_id\n"); 
	    		buffer.append(")dr,\n"); 
	    		buffer.append("(select f.gongysb_id,f.meikxxb_id,sum(f.laimsl)as sllj,(sum(f.laimsl*z.qnet_ar)/sum(f.laimsl))as rllj\n"); 
	    		buffer.append("from fahb f,zhilb z,diancxxb d\n"); 
	    		buffer.append("where f.zhilb_id=z.id\n"); 
	    		buffer.append("and f.diancxxb_id=d.id(+)");
	    		buffer.append(str);
	    		buffer.append("and to_char(z.huaysj,'yyyy-mm-dd')>='"+getBeginriqDate().substring(0, 7)+"-01'\n"); 
	    		buffer.append("and to_char(z.huaysj,'yyyy-mm-dd')<='"+getBeginriqDate()+"'\n"); 
	    		buffer.append("group by f.gongysb_id,f.meikxxb_id\n"); 
	    		buffer.append(")lj,\n"); 
	    		buffer.append("gongysb g,meikxxb m\n"); 
	    		buffer.append("where  \n"); 
	    		buffer.append("dr.gongysb_id=lj.gongysb_id(+)\n"); 
	    		buffer.append("and dr.meikxxb_id=lj.meikxxb_id(+)\n"); 
	    		buffer.append("and dr.gongysb_id=g.id(+)\n"); 
	    		buffer.append("and dr.meikxxb_id=m.id(+)\n"); 
	    		buffer.append(strgys);
	    		buffer.append("group by rollup (g.mingc,m.mingc)\n"); 
	    		

		ResultSet rs = con.getResultSet(buffer,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
			//分厂
			ArrHeader=new String[2][6];
			ArrHeader[0]=new String[] {"供应商","煤矿单位","当日","当日","月累计","月累计"};
			ArrHeader[1]=new String[] {"供应商","煤矿单位","数量<p>吨","热值<p>MJ/kg","数量<p>吨","热值<p>MJ/kg"};

			ArrWidth=new int[] {120,160,70,70,70,70};

			rt.setBody(new Table(rs, 2, 0, 0));
			rt.setTitle("煤量、煤质情况日报<p>("+getBeginriqDate()+")", ArrWidth);
			rt.setDefaultTitle(1, 2, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
			rt.setDefaultTitle(3, 2,  FormatDate(DateUtil.getDate(getBeginriqDate())), Table.ALIGN_CENTER);
			rt.setDefaultTitle(5, 2, "单位:吨、MJ/kg" ,Table.ALIGN_RIGHT);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.createDefautlFooter(ArrWidth);
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
//			System.out.println(getTreeid());
			rt.body.setPageRows(21);
//			rt.body.mergeCell(iStartRow, iStartCol, iEndRow, iEndCol)
			rt.body.mergeFixedRowCol();
			
			rt.body.merge(3, 1, rt.body.getRows(), 1);
			rt.body.setCells(3, 1, rt.body.getRows(), 2 ,Table.PER_ALIGN,Table.ALIGN_LEFT);
			rt.setDefautlFooter(1, 2, "制表时间："+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
			rt.setDefautlFooter(3, 2, "审核:",Table.ALIGN_CENTER);

			if(((Visit) getPage().getVisit()).getDiancmc().equals("河北大唐")){
						
					rt.setDefautlFooter(5, 2, "制表:",Table.ALIGN_RIGHT);
					}else{
						
					rt.setDefautlFooter(5, 2, "制表:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
					}
			
//			rt.setDefautlFooter(5, 2, "制表:",Table.ALIGN_RIGHT);

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
		//日期
		tb1.addText(new ToolbarText("化验日期:"));
		DateField df = new DateField();
		df.setValue(this.getBeginriqDate());
		df.Binding("BeginTime","");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);
		
//		DateField df1 = new DateField();
//		df1.setValue(this.getEndriqDate());
//		df1.Binding("EndTime","");// 与html页中的id绑定,并自动刷新
//		df1.setWidth(80);
//		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		
//		tb1.addText(new ToolbarText("统计口径:"));
//		ComboBox cb1 = new ComboBox();
//		cb1.setTransform("LeixSelect");
//		cb1.setWidth(80);
//		tb1.addField(cb1);
		tb1.addText(new ToolbarText("-"));

		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"form0",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
//		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
//		
//		tb1.addText(new ToolbarText("单位:"));
//		tb1.addField(tf);
//		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("供应商:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("GongysDropDown");
		cb2.setEditable(true);
		tb1.addField(cb2);
		
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
		getToolbars();
		blnIsBegin = true;

	}

    // 供应商
	
    public IDropDownBean getGongysDropDownValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getGongysDropDownModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }
    public void setGongysDropDownValue(IDropDownBean Value) {
//    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean2()!=null){
//	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean2().getId()){
//	    		((Visit) getPage().getVisit()).setBooleanData1(true);
//	    	}else{
//	    		((Visit) getPage().getVisit()).setBooleanData1(false);
//	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean2(Value);
//    	}
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
    	String sql=
    		"select g.id,g.mingc\n" +
    		"from gongysb g,gongysdcglb gl\n" + 
    		"where gl.diancxxb_id="+getTreeid()+"\n" + 
    		"and gl.gongysb_id=g.id  order by g.mingc";
 //   	System.out.println(((Visit) getPage().getVisit()).getDiancxxb_id());
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"全部")) ;
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
		((Visit) getPage().getVisit()).setString4(value);
	}
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));

		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
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
        list.add(new IDropDownBean(1,"分厂"));
        list.add(new IDropDownBean(2,"分矿"));
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
