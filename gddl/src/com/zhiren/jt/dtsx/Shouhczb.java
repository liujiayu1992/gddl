package com.zhiren.jt.dtsx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
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
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/**
 * 
 * @author huochaoyuan
 * 陕西分公司日报程序用
 *（收耗存周报）
 */
/**
 * chenzt
 * 2010-04-06
 * 描述：修改河北分公司报表的制表人一项 ，并且设置为没有（null）
 */

public class Shouhczb extends BasePage {
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
//		Visit visit = (Visit) getPage().getVisit();
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
	
//	private String FormatDate(Date _date) {
//		if (_date == null) {
//			return "";
//		}
//		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
//	}
	
	private String getZhilcx() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String str = "";
//		String strgys="";
//		if(!getGongysDropDownValue().getValue().equals("全部")){
//			strgys="                   and g.id="+getGongysDropDownValue().getId()+"\n";
//		}
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
	    buffer.append("select * from(select to_char(s.riq,'mm')||'月'||to_char(s.riq,'dd')||'日' as riq,decode(d.mingc,null,'小计',d.mingc) as dc,(sum(nvl(s.kuc,0))-(sum(nvl(s.dangrgm,0))-sum(nvl(s.haoyqkdr,0)))) as qrkc,\n");
	    buffer.append("sum(s.dangrgm) as drlm,(sum(s.gongry)+sum(s.fady)+sum(s.qity)+sum(s.cuns)+sum(s.shuifctz)+sum(s.panyk)+sum(s.feiscy)+sum(s.diaoc)+sum(s.tiaozl)) as drhy,sum(s.kuc) as jrkc,sum(s.shangbkc) as sbkc\n");
	    		buffer.append("from shouhcrbb s,diancxxb d\n");
	    		buffer.append("where to_char(s.riq,'yyyy-mm-dd')>='"+getBeginriqDate()+"'\n");
	    		buffer.append("and to_char(s.riq,'yyyy-mm-dd')<='"+getEndriqDate()+"'\n");
	    		buffer.append("and s.diancxxb_id=d.id(+)\n");
	    		buffer.append(str+"\n");
	    		buffer.append("group by s.riq,rollup(d.mingc)\n");
	    		buffer.append("order by s.riq,d.mingc)\n");
	    		buffer.append("union\n");
	    		buffer.append("select * from (select '"+getBeginriqDate()+"'||'<p>至<p>'||'"+getEndriqDate()+"' as riq,d.mingc,sum(qr.kuc) as qrkuc,sum(lj.lm) as ljlm,sum(lj.hm) as ljhm,sum(dr.kuc) as drkuc,sum(dr.sbkc) as sbkc\n");
				buffer.append("from"); 
				buffer.append("(select grouping(s.diancxxb_id) as id,decode(s.diancxxb_id,null,0.1,s.diancxxb_id) as dcid,(sum(s.gongry)+sum(s.fady)+sum(s.qity)+sum(s.cuns)+sum(s.shuifctz)+sum(s.panyk)+sum(s.feiscy)+sum(s.diaoc)+sum(tiaozl)) as hm,sum(s.dangrgm) as lm\n"); 
				buffer.append("from shouhcrbb s,diancxxb d\n"); 
				buffer.append("where to_char(s.riq,'yyyy-mm-dd')>='"+getBeginriqDate()+"'\n"); 
				buffer.append("and to_char(s.riq,'yyyy-mm-dd')<='"+getEndriqDate()+"'\n"); 
				buffer.append("and s.diancxxb_id=d.id(+)\n" + str+"\n");
				buffer.append("group by s.diancxxb_id\n");
				buffer.append(")lj,\n"); 
				buffer.append("(select grouping(s.diancxxb_id) as id,decode(s.diancxxb_id,null,0.1,s.diancxxb_id) as dcid,sum(s.kuc) as kuc\n");
				buffer.append("from shouhcrbb s ,diancxxb d\n"); 
				buffer.append("where s.riq=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')-1\n"); 
				buffer.append("and s.diancxxb_id=d.id(+)\n" +  str+"\n");
				buffer.append("group by s.diancxxb_id\n");
				buffer.append(")qr,\n"); 
				buffer.append("(select grouping(s.diancxxb_id) as id,decode(s.diancxxb_id,null,0.1,s.diancxxb_id) as dcid,sum(s.kuc) as kuc,sum(s.shangbkc) as sbkc\n"); 
				buffer.append("from shouhcrbb s ,diancxxb d\n"); 
				buffer.append("where to_char(s.riq,'yyyy-mm-dd')='"+getEndriqDate()+"'\n"); 
				buffer.append("and s.diancxxb_id=d.id(+)\n" +  str+"\n");
				buffer.append("group by s.diancxxb_id\n"); 
				buffer.append(")dr,diancxxb d\n"); 
				buffer.append("where lj.dcid=qr.dcid(+)\n");
				buffer.append("and lj.dcid=dr.dcid(+)\n"); 
				buffer.append("and lj.dcid=d.id(+)\n"); 
				buffer.append("group by rollup(d.mingc)\n");
				buffer.append("order by d.mingc)");
		ResultSet rs = con.getResultSet(buffer,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
			//分厂
			ArrHeader=new String[1][7];
			ArrHeader[0]=new String[] {"日期","单位","前日库存","收煤","耗用","当前库存","外报库存"};
			

			ArrWidth=new int[] {70,160,60,60,60,60,60};

			rt.setBody(new Table(rs, 1, 0, 1));
			rt.setTitle("收耗存周报<p>("+getBeginriqDate()+"至"+getEndriqDate()+")", ArrWidth);
			rt.setDefaultTitle(1, 2, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
			//rt.setDefaultTitle(3,3, "制表时间："+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
		//	rt.setDefaultTitle(10, 3, "单位:(吨)" ,Table.ALIGN_RIGHT);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.createDefautlFooter(ArrWidth);
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.body.setCells(2, 2, rt.body.getRows(), 2, Table.PER_ALIGN, Table.ALIGN_LEFT);
//			System.out.println(getTreeid());
			rt.body.setPageRows(21);
//
//			int qrkuc = 0;
//			int ljlm = 0;
//			int ljhm = 0;
//			int drkuc=0;
//			int drsbkc=0;
//
//			String shcsql = 
//
//				"select aa as riq,d.mingc,qr.kuc as qrkuc,lj.lm as ljlm,lj.hm as ljhm,dr.kuc as drkuc,dr.sbkc as sbkc\n" +
//				"from\n" + 
//				"(select grouping(s.diancxxb_id) as id,decode(s.diancxxb_id,null,0.1,s.diancxxb_id) as dcid,sum(s.haoyqkdr) as hm,sum(s.dangrgm) as lm\n" + 
//				"from shouhcrbb s,diancxxb d\n" + 
//				"where to_char(s.riq,'yyyy-mm-dd')>='"+getBeginriqDate()+"'\n" + 
//				"and to_char(s.riq,'yyyy-mm-dd')<='"+getEndriqDate()+"'\n" + 
//				"and s.diancxxb_id=d.id(+)\n" + str+"\n"+
//				"group by rollup(s.diancxxb_id)\n" + 
//				"having not grouping(s.diancxxb_id)=0)lj,\n" + 
//				"(select grouping(s.diancxxb_id) as id,decode(s.diancxxb_id,null,0.1,s.diancxxb_id) as dcid,sum(s.kuc) as kuc\n" + 
//				"from shouhcrbb s ,diancxxb d\n" + 
//				"where s.riq=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')-1\n" + 
//				"and s.diancxxb_id=d.id(+)\n" +  str+"\n"+
//				"group by rollup(s.diancxxb_id)\n" + 
//				"having not grouping(s.diancxxb_id)=0)qr,\n" + 
//				"(select grouping(s.diancxxb_id) as id,decode(s.diancxxb_id,null,0.1,s.diancxxb_id) as dcid,sum(s.kuc) as kuc,sum(s.shangbkc) as sbkc\n" + 
//				"from shouhcrbb s ,diancxxb d\n" + 
//				"where to_char(s.riq,'yyyy-mm-dd')='"+getEndriqDate()+"'\n" + 
//				"and s.diancxxb_id=d.id(+)\n" +  str+"\n"+
//				"group by rollup(s.diancxxb_id)\n" + 
//				"having not grouping(s.diancxxb_id)=0)dr,diancxxb d\n" + 
//				"where lj.id=dr.id(+)\n" + 
//				"and lj.id=qr.id(+)"+
//				"and lj.dcid=qr.dcid(+)\n" +
//				"and lj.dcid=dr.dcid(+)\n" + 
//				"and lj.dcid=d.id(+)\n" + 
//				"order by d.mingc";
//;
//
//
//			try{
//
//				ResultSet shc = con.getResultSet(shcsql);
//				if(shc.next()){
//					qrkuc=shc.getInt("qrkuc");
//					ljlm=shc.getInt("ljlm");
//					ljhm=shc.getInt("ljhm");
//					drkuc=shc.getInt("drkuc");
//					drsbkc=shc.getInt("sbkc");
//				
//				}
//				shc.close();
//			}catch(Exception e){
//				e.printStackTrace();
//			}finally{
//				con.Close();
//			}
////			rt.body.merge(rt.body.getRows(), 1, rt.body.getRows(), 2);
//			rt.body.setCells(rt.body.getRows(), 1, rt.body.getRows(), 1, Table.PER_ALIGN, Table.ALIGN_CENTER);
//			rt.body.setCells(rt.body.getRows(), 3, rt.body.getRows(), 7, Table.PER_ALIGN, Table.ALIGN_RIGHT);
//			rt.body.setCellValue(rt.body.getRows(), 1, getBeginriqDate()+"至"+getEndriqDate());
//	//		rt.body.setCellValue(rt.body.getRows(), 2, "总计");
//			rt.body.setCellValue(rt.body.getRows(), 3,String.valueOf(qrkuc));
//
//			rt.body.setCellValue(rt.body.getRows(), 4, String.valueOf(ljlm));
//			rt.body.setCellValue(rt.body.getRows(), 5, String.valueOf(ljhm));
//			rt.body.setCellValue(rt.body.getRows(), 6, String.valueOf(drkuc));
//			rt.body.setCellValue(rt.body.getRows(), 7, String.valueOf(drsbkc));

			rt.body.mergeFixedRowCol();
			rt.body.mergeFixedCols();
			rt.setDefautlFooter(1, 2, "制表时间："+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
			rt.setDefautlFooter(3, 2, "审批:",Table.ALIGN_LEFT);
	//		Visit visit=(Visit) getPage().getVisit();

			if(((Visit) getPage().getVisit()).getDiancmc().equals("河北大唐")){
						
					rt.setDefautlFooter(5, 3, "制表:",Table.ALIGN_RIGHT);
					}else{
						
					rt.setDefautlFooter(5, 3, "制表:"+((Visit) getPage().getVisit()).getDiancmc(),Table.ALIGN_RIGHT);
					}
//			rt.setDefautlFooter(5,3,"制表：",Table.ALIGN_LEFT);
		

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
		
		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("EndTime","");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		tb1.addField(df1);
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
			visit.setboolean1(false);
			visit.setDropDownBean1(null);
			visit.setString4(null);
			visit.setString5(null);
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
