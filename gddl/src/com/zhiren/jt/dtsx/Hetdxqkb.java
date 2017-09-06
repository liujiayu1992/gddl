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
 *根据大唐陕西分公司调运部需求制作调运部用日报
 *该报表属于个性报表需求，显示方式按煤矿地区和煤矿单位对应关系进行组织，主要含来煤量计划量和到货率等信息
 */
/**
 * chenzt
 * 2010-04-06
 * 描述：修改河北分公司报表的制表人一项 ，并且设置为没有（null）
 */

public class Hetdxqkb extends BasePage {
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
	private String mstrReportName = "";
	private String QICHCX_ALL="Qichcx_all";//
	private String QICHCH_PRINT="Qichcx_print";//
	private String JILTZ_HC="jiltz_hc";
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
if (mstrReportName.equals(QICHCX_ALL)) {
		return getHetdxqk();
	}else {
		return getHetdxqk();
	}
		
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
	
	private String getHetdxqk() {
		int intHuzStartRow = 0;
		ResultSet rs;
		JDBCcon con = new JDBCcon();
		String changbq = "";
		String changbc = "";
		Visit visit=(Visit) getPage().getVisit();
		String str = "";
//		if (visit.isJTUser()) {
//			str = "";
//		} else {
//			if (visit.isGSUser()) {
//				str = "and (d.id = " + getTreeid() + " or d.fuid = "
//						+ getTreeid() + ")";
//			} else {
//				str = "and d.id = " + getTreeid() + "";
//			}
//		}
//		
//		int treejib = this.getDiancTreeJib();
//		if (treejib == 1) {// 选集团时刷新出所有的电厂
//			str = "";
//		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
//			str = "and (d.id = " + getTreeid() + " or d.fuid = "
//					+ getTreeid() + ")";
//		} else if (treejib == 3) {// 选电厂只刷新出该电厂
//			str = "and d.id = " + getTreeid() + "";
//		}
	//	System.out.println(getTreeid());
		String yunsdwsql="";
		String meicsql="";
		String xiecfsql="";
		String chephsql="";
		String gmsql="";
		String renysql="";
		String zhuangtsql="";
		String shij="";
        if(!getLeixSelectValue().getValue().equals("全部")){
        	yunsdwsql="and nl.yunsfsb_id="+getLeixSelectValue().getId()+"\n";
        }
        if(!getDiancmcValue().getValue().equals("全部")){
        	xiecfsql="and m.jihkjb_id="+getDiancmcValue().getId()+"\n";
        }
        if(!getGongysDropDownValue().getValue().equals("*煤场")){
        	meicsql="and c.meicb_id in (select id from meicb where mingc like '%"+getGongysDropDownValue().getValue()+"%' or beiz like '%"+getGongysDropDownValue().getValue()+"%')\n";
        }
   //     System.out.println(getCheph());
        if(getCheph()!=null&&!getCheph().equals("")){
        	chephsql="and c.cheph like '%"+_cheph+"%'\n";
        }
   //     System.out.println(getTreeid());
        if(getTreeid()!=null&&!getTreeid().equals("0")&&!getTreeid().equals("")){
        	gmsql="and (g.id="+getTreeid()+" or m.id="+getTreeid()+" )\n";
        }

		String sql = 


			"select decode(j.mingc,null,'总计',j.mingc),decode(j.mingc,null,'总计',decode(g.mingc,null,'合计',g.mingc)),\n" +
			"decode(j.mingc,null,'总计',decode(g.mingc,null,'合计',decode(m.mingc,null,g.mingc,m.mingc))),\n" + 
			"decode(j.mingc,null,'总计',decode(g.mingc,null,'合计',decode(m.mingc,null,'小计',decode(ys.mingc,null,'小计',ys.mingc)))),\n" + 
			"n.hej,y.yj,y.rj,round(z.dr,2) as dr,round(z.yl,2) as yl,decode(y.yj,0,0,round(round(z.yl,2)/y.yj,3)*100)as ydh,round(z.nl,2) as nl,decode(n.hej,0,0,round(round(z.nl,2)/n.hej,3)*100) as ndh from\n" + 
			"(\n" + 
			"select grouping (m.jihkjb_id) as xuh1,grouping (g.id) as xuh2,grouping (nl.meikxxb_id) as xuh3,grouping (nl.yunsfsb_id) as xuh4,\n" + 
			"decode(m.jihkjb_id,null,99,m.jihkjb_id) as kj,decode(g.id,null,99,g.id) as gid,decode(nl.meikxxb_id,null,g.id,nl.meikxxb_id) as mid,\n" + 
			"nl.yunsfsb_id as yid,sum(dr.sl) as dr,sum(yl.sl) as yl,sum(nl.sl) as nl from\n" + 
			"(select distinct f.meikxxb_id,f.yunsfsb_id,sum(f.laimsl) as sl from\n" + 
			"fahb f\n" + 
			"where to_char(f.daohrq,'yyyy-mm-dd')='"+getBeginriqDate()+"'\n" + 
			"group by f.meikxxb_id,f.yunsfsb_id)dr,\n" + 
			"(select f.meikxxb_id,f.yunsfsb_id,sum(f.laimsl) as sl from\n" + 
			"fahb f\n" + 
			"where to_char(f.daohrq,'yyyy-mm-dd')>='"+getBeginriqDate().substring(0, 7)+"-01'\n" + 
			"and to_char(f.daohrq,'yyyy-mm-dd')<='"+getBeginriqDate()+"'\n" + 
			"group by f.meikxxb_id,f.yunsfsb_id)yl,\n" + 
			"(select f.meikxxb_id,f.yunsfsb_id,sum(f.laimsl) as sl from\n" + 
			"fahb f\n" + 
			"where to_char(f.daohrq,'yyyy-mm-dd')>='"+getBeginriqDate().substring(0, 4)+"-01-01'\n" + 
			"and to_char(f.daohrq,'yyyy-mm-dd')<='"+getBeginriqDate()+"'\n" + 
			"group by f.meikxxb_id,f.yunsfsb_id)nl,meikxxb m,gongysb g\n" + 
			"where nl.meikxxb_id=yl.meikxxb_id(+)\n" + 
			"and nl.meikxxb_id=dr.meikxxb_id(+)\n" + 
			"and nl.yunsfsb_id=yl.yunsfsb_id(+)\n" + 
			"and nl.yunsfsb_id=dr.yunsfsb_id(+)\n" + 
			"and nl.meikxxb_id=m.id\n" + yunsdwsql+xiecfsql+gmsql+
			"and m.meikdq_id=g.id\n" + 
			"group by rollup(m.jihkjb_id,g.id,nl.meikxxb_id,nl.yunsfsb_id)\n" + 
			"having not (grouping(nl.yunsfsb_id)=1 and grouping (nl.meikxxb_id)=0)\n" + 
			")z,\n" + 
			"(select jihkjb_id,gongysb_id,sum(yuejhcgl) as yj,round(sum(yuejhcgl)/(last_day(to_date('"+getBeginriqDate()+"','yyyy-mm-dd'))-first_day(to_date('"+getBeginriqDate()+"','yyyy-mm-dd')) +1),0) as rj from yuecgjhb\n" + 
			"where to_char(riq,'yyyy-mm-dd')='"+getBeginriqDate().substring(0, 7)+"-01' group by jihkjb_id,gongysb_id\n" + 
			"union\n" + 
			"select jihkjb_id,gongysb_id,sum(hej*10000) as yj,round(sum(hej*10000)/(last_day(to_date('"+getBeginriqDate()+"','yyyy-mm-dd'))-first_day(to_date('"+getBeginriqDate()+"','yyyy-mm-dd')) +1),0) as rj from niandhtqkb\n" + 
			"where to_char(riq,'yyyy-mm-dd')='"+getBeginriqDate().substring(0, 7)+"-01' group by jihkjb_id,gongysb_id\n" + 
			") y,\n" + 
			"(select jihkjb_id,gongysb_id,sum(hej*10000) as hej\n" + 
			"from niandhtqkb\n" + 
			"where to_char(riq,'yyyy')='"+getBeginriqDate().substring(0, 4)+"'\n"+
			"group by jihkjb_id,gongysb_id\n" + 
			") n,jihkjb j,meikxxb m,gongysb g,yunsfsb ys\n" + 
			"where z.kj=j.id(+)\n" + 
			"and z.gid=g.id(+)\n" + 
			"and z.mid=m.id(+)\n" + 
			"and z.yid=ys.id(+)\n" + 
			"and z.kj=n.jihkjb_id(+)\n" + 
			"and z.kj=y.jihkjb_id(+)\n" + 
			"and z.mid=n.gongysb_id(+)\n" + 
			"and z.mid=y.gongysb_id(+)\n" +
			"order by j.xuh,xuh1,g.mingc,xuh2,m.mingc,xuh3,ys.mingc,xuh4";




		rs = con.getResultSet(new StringBuffer(sql),
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String ArrHeader[][] = new String[1][12];
		ArrHeader[0] = new String[] {"计划口径","煤矿地区", "煤矿单位", "运输方式","年计划",
				"月计划", "日计划", "当日","月累计","月到货率<p>(%)","年累计","年到货率<p>(%)"};
		int ArrWidth[] = new int[] {60,120, 120,60,60, 60, 60, 60, 60, 60,60, 60};
		rt.setTitle("燃  料  调  运  日  报<p>"+visit.getDiancmc(),
				//+((getChangbValue().getId() > 0)?"("+getChangbValue().getValue()+")":"")+"", 
				ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitleLeft("日期：" 
				+ getBeginriqDate().substring(0, 4)+"-01-01"+"至"+getBeginriqDate()
				, 3);
		rt.setDefaultTitleRight("单位：吨", 3);
		rt.setBody(new Table(rs, 1, 0, 3));

		// 数据
		rt.body.setWidth(ArrWidth);

		//		
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.ShowZero = true;
//		rt.body.setPageRows(43);
		rt.body.mergeFixedCols();// 合并列标题
		rt.body.mergeFixedRow();//合并行标题
		for(int i = 2; i<= rt.body.getRows() ; i++){
			rt.body.setCells(i, 1, i, 3, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.merge(i, 1, i, 3);
		}
//		rt.body.mergeCell(2, 1, rt.body.getRows(), 3);
//		rt.body.setCells(2, 1, rt.body.getRows(), 3, Table.PER_ALIGN,
//				Table.ALIGN_LEFT);
//		rt.body.setCells(2, 1, rt.body.getRows(), 3, Table.PER_VALIGN,
//				Table.VALIGN_TOP);
//		rt.body.setCells(2, 2, rt.body.getRows(), 3, Table.PER_ALIGN,
//				Table.ALIGN_CENTER);
		rt.body.ShowZero = false;

		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3,
				"报表打印日期:" + DateUtil.FormatDate(new Date()), 0);
		rt.setDefautlFooter(4, 2, "复核：", Table.ALIGN_LEFT);

		if(((Visit) getPage().getVisit()).getDiancmc().equals("河北大唐")){
					
			rt.setDefautlFooter(8, 3, "制表:",Table.ALIGN_RIGHT);
				}else{
					
					rt.setDefautlFooter(8, 3, "制表:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
				}
//		rt.setDefautlFooter(8, 3, "制表：" , Table.ALIGN_LEFT);
		// rt.setDefautlFooter(14, 2, Table.PAGENUMBER_CHINA, 2);
		try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();

		rt.body.setRowHeight(21);
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
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();
		df.setValue(this.getBeginriqDate());
		df.Binding("BeginTime","");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);
		
//		tb1.addText(new ToolbarText("至"));
//		DateField df1 = new DateField();
//		df1.setValue(this.getEndriqDate());
//		df1.Binding("EndTime","");// 与html页中的id绑定,并自动刷新
//		df1.setWidth(80);
//		tb1.addField(df1);
//		tb1.addText(new ToolbarText("-"));
		
//		tb1.addText(new ToolbarText("统计口径:"));
//		ComboBox cb1 = new ComboBox();
//		cb1.setTransform("LeixSelect");
//		cb1.setWidth(80);
//		tb1.addField(cb1);
//		tb1.addText(new ToolbarText("-"));

		long diancid=visit.getDiancxxb_id();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_mkdq_mk_sx,"diancTree",""+diancid,null,null,null);
		visit.setDefaultTree(dt);
		
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setValue("请选择煤矿");
		tf.setWidth(70);
		//dt.getTree().getSelectedNodeid()
		//tf.setValue(dt.getTree().getSelectedNodeid());
		//tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
				
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
	//	tb1.addText(new ToolbarText("发货单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
//		tb1.addText(new ToolbarText("供应商:"));
//		ComboBox cb2 = new ComboBox();
//		cb2.setTransform("GongysDropDown");
//		cb2.setEditable(true);
//		tb1.addField(cb2);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("运输方式:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("LeixSelect");
		cb1.setWidth(80);
		tb1.addField(cb1);
		
		tb1.addText(new ToolbarText("-"));
		
	//	tb1.addText(new ToolbarText("煤场:"));
//		ComboBox cb2 = new ComboBox();
//		cb2.setTransform("GongysDropDown");
//		cb2.setEditable(true);
//		cb2.setWidth(60);
//		tb1.addField(cb2);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("计划口径:"));
		ComboBox cb3 = new ComboBox();
		cb3.setTransform("DanwSelect");
		cb3.setEditable(true);
		cb3.setWidth(80);
		tb1.addField(cb3);
		
//		tb1.addText(new ToolbarText("-"));
//		
//		//	tb1.addText(new ToolbarText("接卸方式:"));
//	    ComboBox cb4 = new ComboBox();
//		cb4.setTransform("JIHKJ");
//		cb4.setEditable(true);
//		cb4.setWidth(80);
//		tb1.addField(cb4);
//		
//		tb1.addText(new ToolbarText("-"));
//		//tb1.addText(new ToolbarText("车号:"));
//		TextField tf1 = new TextField();
//		tf1.setId("cheh");
//		tf1.setListeners("change:function(own,newValue,oldValue) {document.getElementById('CHEPH').value = newValue;}");
//		tf1.setWidth(60);
//		tf1.setValue(getCheph());
//		tb1.addField(tf1);
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
			visit.setboolean1(false);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean6(null);
			visit.setProSelectionModel6(null);
			visit.setDefaultTree(null);
			setTreeid(null);
			setCheph(null);
		}
		//setTreeid(null);
//		if (cycle.getRequestContext().getParameters("lx") != null) {
//			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
//			mstrReportName = visit.getString1();
//		} else {
//			if (visit.getString1().equals("")) {
//				mstrReportName = visit.getString1();
//			}
//
//		}
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
    	String sql="select id,mingc from meicb union select rownum+20 as id,beiz as mingc from (select distinct beiz from meicb)";
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"*煤场")) ;
        return ;
    } 

	// 日期
    public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
//				Calendar stra=Calendar.getInstance();
////				stra.set(DateUtil.getYear(new Date()), 0, 1);
//				stra.setTime(new Date());
			//	stra.add(Calendar.DATE,-1);
			//	((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
				((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(new Date()));
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
    	String sql="select id,mingc from yunsfsb";
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql,"全部")) ;
      //  ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql,"*运输单位")) ;
        return ;
    }

    private String treeid;
	public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String a) {
		treeid=a;
	}
	
	private String _cheph;
	
	public String getCheph(){
		return _cheph;
	}
	
	public void setCheph(String a){
		_cheph=a;
	}
    public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	电厂名称
    public IDropDownBean getDiancmcValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean5()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean)getDiancmcModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean5();
    }
    public void setDiancmcValue(IDropDownBean Value) {

	    	((Visit) getPage().getVisit()).setDropDownBean5(Value);

    }
    public void setDiancmcModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel5(value);
    }

    public IPropertySelectionModel getDiancmcModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
            getDiancmcModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel5();
    }
    public void getDiancmcModels() {
    	String sql="select id,mingc from jihkjb";
        ((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(sql,"全部")) ;
     //   ((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(sql,"*卸车方式")) ;
        return ;
    }
//计划口径
    
    public IDropDownBean getJihkjValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean6()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean6((IDropDownBean)getJihkjModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean6();
    }
    public void setJihkjValue(IDropDownBean Value) {

	    	((Visit) getPage().getVisit()).setDropDownBean6(Value);

    }
    public void setJihkjModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel6(value);
    }

    public IPropertySelectionModel getJihkjModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
            getJihkjModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel6();
    }
    public void getJihkjModels() {
    	String sql="select id,mingc from jihkjb";
     //   ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"全部")) ;
       
    	((Visit) getPage().getVisit()).setProSelectionModel6(new IDropDownModel(sql,"*口径")) ;
        return ;
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
