package com.zhiren.dtrlgs.shoumgl;

import java.sql.ResultSet;
import java.util.Date;

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

public class DuowkcblcxReport extends BasePage {
	
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

	private String REPORT_NAME_KEHXQYJH = "duowkc";//DuowkcbReport&lx=duowkc
	private String mstrReportName = "duowkc";
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		if (mstrReportName.equals(REPORT_NAME_KEHXQYJH)) {
			return getKehxqyjhReport();
		} else {
			return "无此报表";
		}
	}
	
	private String getKehxqyjhReport() {
		JDBCcon con = new JDBCcon();
		StringBuffer sql=new StringBuffer();
		String kaisShij=DateUtil.FormatOracleDate(this.getBRiq());
	    String  jiesShij=DateUtil.FormatOracleDate(this.getERiq());
	  sql.append("  select decode(grouping(weih.dc),1,'总计', weih.dc) dc,decode(grouping(weih.dc)+grouping(weih.meicmc),1,'小计',weih.meicmc) meicmc,weih.pz,weih.sj,\n");
	   sql.append("   sum(weih.kl),decode(sum(weih.kl),0,0,round(sum(weih.kl*weih.fl)/sum(weih.kl),0)),sum(weih.zaitml) from(\n");
	   sql.append("  select di.mingc dc, dw.mingc ||'-'||me.mingc meicmc,xh.pinz pz,to_char(xh.shij,'yyyy-mm-dd hh24:mi:ss') sj,nvl(round(xh.kucl,0),0) kl,nvl(round(xh.farl,0),0) fl,xh.ml zaitml   from\n") 
	       .append("( select du.*,ke.ml from\n")
	       .append("         (select meicb_id,max(shij)sj, sum(zaitml) ml from duowkcb where  leix<>2  and leib<>3 and   shij<"+kaisShij+"+1 group by meicb_id) ke,duowkcb du\n")
	       .append("       where ke.meicb_id =du.meicb_id and du.shij=ke.sj) xh ,meicb me,diancxxb di,duowgsb dw\n")
	       .append("      where  me.id=xh.meicb_id(+) and di.id=me.diancxxb_id and me.id<>3 and dw.id=me.duowgsb_id\n");
	       if(getTreeJib()==1){
				sql.append("	and di.jib=3 ");
			}else if(getTreeJib()==2){
				sql.append("	and di.fuid=" + getTreeid());
			}else{
				sql.append("	and di.id=" + getTreeid());
			}
	       sql.append(" and  (nvl(xh.kucl,0)<>0 or xh.ml>0)  order by di.xuh, me.xuh\n");
	   sql.append(" )  weih\n").
	   append("group by rollup(weih.dc,(weih.meicmc,weih.pz,weih.sj,weih.fl))\n").
			   append("order by grouping(weih.dc)desc,weih.dc desc,grouping(weih.meicmc)desc,weih.meicmc,weih.sj\n");
	        
		       
		ResultSet rs = con.getResultSet(sql.toString());
		
		Report rt = new Report();
		
		String ArrHeader[][]=new String[1][7];
		ArrHeader[0]=new String[] {"存煤地点","煤场","品种","最后入库出库时间","库存量","库存热量","在途煤量"};
		int[] ArrWidth = new int[6];
		ArrWidth=new int[] {80,100,60,120,60,80,80};

		rt.setTitle("垛位库存量表", ArrWidth);

		rt.setDefaultTitle(5,2,"查询日期:"+getBRiq() ,Table.ALIGN_CENTER);
//		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(rs, 1, 0, 3));//行，0，列 
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.setColAlign(4, Table.ALIGN_LEFT);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);//第五列
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "打印日期:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(12,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT );
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
		Toolbar tb1 = new Toolbar("tbdiv");
		
		//树
		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
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
		
		tb1.addText(new ToolbarText("-"));
		
		//日期
		tb1.addText(new ToolbarText("日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("rqb");
		tb1.addField(dfb);
	/*	tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
		dfe.setId("rqe");
		tb1.addField(dfe);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("供应商:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("YewlxbDropDown");
		cb2.setEditable(true);
		cb2.setWidth(100);
		tb1.addField(cb2);*/
		
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"查询","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Print);
		tb1.addItem(tb);
		
		setToolbar(tb1);
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

    // 业务类型
    public IDropDownBean getYewlxbDropDownValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getYewlxbDropDownModel().getOption(0));
    	}
       return  ((Visit) getPage().getVisit()).getDropDownBean4();
    }
    public void setYewlxbDropDownValue(IDropDownBean Value) {
	    	((Visit) getPage().getVisit()).setDropDownBean4(Value);
    }
    public void setYewlxbDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }
    public IPropertySelectionModel getYewlxbDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getYewlxbDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }
    public void getYewlxbDropDownModels() {
    	String sql="select id,mingc\n" +
    	"from yewlxb\n"+
    	"";
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql,"全部"));
        return ;
    }
   

//树
    private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	private int getTreeJib(){
		JDBCcon con=new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select jib from diancxxb where id="+getTreeid());
		rsl.next();
		con.Close();
		return rsl.getInt("jib");
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
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			String rq=DateUtil.FormatDate(new Date());
			this.setBRiq(rq);
			this.setERiq(rq);
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
			setTreeid(null);
			
		}
		getToolbars();
		if(cycle.getRequestContext().getParameter("lx")!=null){
			mstrReportName = cycle.getRequestContext().getParameter("lx");
		}
		blnIsBegin = true;
	}
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
