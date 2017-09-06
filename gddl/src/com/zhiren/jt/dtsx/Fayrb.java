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
 *(发运日报<外报数据>)计划量取自年计划
 */
public class Fayrb extends BasePage {
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
		
		return getFaybb();
		
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
	
	//发运日报表，取实际数据fahb jingz(数量),ches(车数),where beiz='人工上报'
	private String getFaybb() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer buffer = new StringBuffer("");
		String str = "";

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
	    buffer.append("select decode(g.mingc,null,'总计',g.mingc),decode(y.mingc,null,decode(g.mingc,null,'总计','合计'),y.mingc),\n");
	            buffer.append("decode(d.mingc,null,decode(g.mingc,null,'总计',decode(y.mingc,null,'合计','小计')),d.mingc),\n");
	    		buffer.append("sum(jh.hej)as htl,0 as zb,sum(jh.rjl)as rjl,sum(jh.yflj)as yflj,\n");
	    		buffer.append("sum(dr.ches)as ches,sum(dr.lm)as drlm,sum(lj.lm)as ljlm,(sum(nvl(lj.lm,0))-sum(nvl(jh.yflj,0)) )as cq,\n");
	    		buffer.append("decode(sum(nvl(jh.yflj,0)),0,0,round(sum(nvl(lj.lm,0))/sum(nvl(jh.yflj,0)),2)*100)||'%' as dxl\n");
	    		buffer.append("from\n");
	    		buffer.append("(select  f.gongysb_id as gysid,f.yunsfsb_id as fsid, f.diancxxb_id as id,\n");
	    		buffer.append("sum(f.jingz) as lm,sum(f.ches) as ches\n");
	    		buffer.append("from fahb f,diancxxb d\n");
	    		buffer.append("where to_char(f.daohrq,'yyyy-mm-dd')='"+getBeginriqDate()+"'\n");
	    		buffer.append("and f.diancxxb_id=d.id(+) and f.beiz='人工上报'\n");
	    		buffer.append(str+"\n");
	    		buffer.append("group by f.gongysb_id,f.yunsfsb_id,f.diancxxb_id\n");
	    		buffer.append(")dr,\n");
	    		buffer.append("(select  f.gongysb_id as gysid,f.yunsfsb_id as fsid, f.diancxxb_id as id,\n");
	    		buffer.append("sum(f.jingz) as lm\n");
	    		buffer.append("from fahb f,diancxxb d\n");
	    		buffer.append("where to_char(f.daohrq,'yyyy-mm-dd')>='"+getBeginriqDate().substring(0, 7)+"-01'\n");
	    		buffer.append("and to_char(f.daohrq,'yyyy-mm-dd')<='"+getBeginriqDate()+"'\n");
	    		buffer.append("and f.diancxxb_id=d.id(+) and f.beiz='人工上报'\n");
	    		buffer.append("group by f.gongysb_id,f.yunsfsb_id,f.diancxxb_id\n");
	    		buffer.append(")lj,\n");
	    		buffer.append("(select n.diancxxb_id as id,n.gongysb_id as gysid,n.yunsfsb_id as fsid,n.hej*10000 as hej,round(n.hej/daycount(to_date('"+getBeginriqDate()+"','yyyy-mm-dd')),2)*10000 as rjl,round((n.hej/daycount(to_date('"+getBeginriqDate()+"','yyyy-mm-dd')))*"+getBeginriqDate().substring(8, 10)+",2)*10000 as yflj\n");
	    		buffer.append("from niandhtqkb n\n");
	    		buffer.append("where to_char(n.riq,'yyyy-mm')='"+getBeginriqDate().substring(0, 7)+"'\n");
	    		buffer.append(")jh,diancxxb d,gongysb g,yunsfsb y\n");
	    		buffer.append("where dr.id=lj.id(+)\n");
	    		buffer.append("and dr.id=jh.id(+)\n");
	    		buffer.append("and dr.id=d.id(+)\n");
	    		buffer.append("and dr.gysid=lj.gysid(+)\n");
	    		buffer.append("and dr.gysid=jh.gysid(+)\n");
	    		buffer.append("and dr.gysid=g.id(+)\n");
	    		buffer.append("and dr.fsid=lj.fsid(+)\n");
	    		buffer.append("and dr.fsid=jh.fsid(+)\n");
	    		buffer.append("and dr.fsid=y.id(+)\n");
	    		buffer.append("group by rollup(g.mingc,y.mingc,d.mingc)\n");
	    		buffer.append("having not (grouping(d.mingc)=1 and grouping(y.mingc)=0)\n");
	    		buffer.append("order by g.mingc,g.mingc\n");


		ResultSet rs = con.getResultSet(buffer,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
			//分厂
			ArrHeader=new String[2][12];
			ArrHeader[0]=new String[] {"煤炭来源","方式","电厂","月计划","月计划","月计划","月计划","到煤量","到煤量","到煤量","累计超欠<p>(+)(-)","合同兑现率<p>(%)"};
			ArrHeader[1]=new String[] {"煤炭来源","方式","电厂","合同量","追补量","日均量","应发累计","车数","当日到煤","月累计","累计超欠<p>(+)(-)","合同兑现率<p>(%)"};

			ArrWidth=new int[] {120,40,160,40,40,40,50,40,50,40,50,65};

			rt.setBody(new Table(rs, 2, 0, 3));
			rt.setTitle("煤炭发运情况日报<P>("+getBeginriqDate()+")", ArrWidth);
			rt.setDefaultTitle(1, 3, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
			//rt.setDefaultTitle(4,4, "制表时间："+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
			rt.setDefaultTitle(10, 3, "单位:(吨)" ,Table.ALIGN_RIGHT);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.createDefautlFooter(ArrWidth);
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			//rt.body.mergeCell(3, 1, rt.body.getRows(), 3);
//			System.out.println(getTreeid());
			rt.body.setPageRows(21);
			//rt.body.mergeCell(2, 1, rt.body.getRows(), 3);
			rt.body.mergeFixedRowCol();
			rt.body.mergeFixedCols();
			//rt.body.mergeCell(3, 1, rt.body.getRows(), 3);
			rt.setDefautlFooter(1, 2, "制表时间："+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			rt.setDefautlFooter(3, 2, "审批:",Table.ALIGN_LEFT);
	//		Visit visit=(Visit) getPage().getVisit();
			rt.setDefautlFooter(10,3,"制表："+visit.getRenymc(),Table.ALIGN_LEFT);
		

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
		tb1.addText(new ToolbarText("查询日期:"));
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
//		tb1.addText(new ToolbarText("-"));

		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"form0",null,getTreeid());
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
		
//		tb1.addText(new ToolbarText("供应商:"));
//		ComboBox cb2 = new ComboBox();
//		cb2.setTransform("GongysDropDown");
//		cb2.setEditable(true);
//		tb1.addField(cb2);
		
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
    	String sql="select id,mingc\n" +
    	"from gongysb\n" + 
    	"where gongysb.fuid=0";
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
