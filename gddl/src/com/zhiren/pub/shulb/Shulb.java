//chh 2008-03-17 修改查询语句与表格格式
package com.zhiren.pub.shulb;

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
import com.zhiren.common.ext.ExtTreeUtil;
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

public class Shulb extends BasePage {
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


	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		//if (mstrReportName.equals(REPORT_NAME_Hetltj)) {
		return getHetltj();
		//} else {
		//	return "无此报表";
		//}
	}
	// 合同量分厂分矿分矿分厂统计报表
	private String getHetltj() {
		JDBCcon con = new JDBCcon();
		StringBuffer buffer = new StringBuffer("");
		Visit visit = (Visit) getPage().getVisit();
		
		String gongysCondition="and f.gongysb_id in(select id from gongysb where fuid="+getGongysDropDownValue().getId()+")";
		String diancCondition=
			"and f.diancxxb_id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by fuid=prior id\n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+")" ; 
//		}
		if(getGongysDropDownValue().getId()==-1){
			gongysCondition="";
		}
		//分厂分矿
		if(getLeixSelectValue().getId()==1){
			//"--分厂\n" +
			buffer.append("select decode(grouping(d.mingc),1,'总计',d.mingc) diancmc,  \n");
			buffer.append("decode(grouping(g.mingc)+grouping(d.mingc),2,'',1,'合计',g.mingc) gongys,  \n");
			buffer.append("decode(grouping(g.mingc)+grouping(d.mingc)+grouping(f.id),1,'小计',0,max(m.mingc),'') meik,  \n");
			buffer.append("decode(grouping(f.id),0,max(c.mingc),'') faz , \n");
			buffer.append("decode(grouping(f.id),0,max(p.mingc),'') pinz , \n");
			buffer.append("decode(grouping(f.id),0,to_char(max(f.fahrq),'yyyy-mm-dd'),''), \n");
			buffer.append("decode(grouping(f.id),0,to_char(max(f.daohrq),'yyyy-mm-dd'),''),   \n");
			buffer.append("sum(f.ches) ches, sum(f.biaoz) biaoz, sum(f.jingz) jingz, sum(f.yuns) yuns, sum(f.yingk) yingk, sum(f.zongkd) zongkd   \n");
			buffer.append("from fahb f,diancxxb d,gongysb g,meikxxb m,pinzb p,chezxxb c    \n");
			buffer.append("  where f.diancxxb_id=d.id  \n" );
			buffer.append("  and f.gongysb_id=g.id  \n" );
			buffer.append("  and f.meikxxb_id=m.id  \n" );
			buffer.append("  and f.pinzb_id=p.id  \n" );
			buffer.append("  and f.faz_id=c.id  \n" );
			buffer.append(" and f.daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')");
			buffer.append(" and f.daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')");
			buffer.append(gongysCondition);
			buffer.append(diancCondition);
			buffer.append("  group by rollup(d.mingc,g.mingc,f.id)  \n" );
			buffer.append("  order by grouping(d.mingc) desc,max(d.mingc),grouping(g.mingc) desc,max(g.mingc), grouping(f.id) desc,max(m.xuh) \n" );
		}else {//分矿
			buffer.append("select decode(grouping(g.mingc),1,'总计',g.mingc) gongys,  \n");
			buffer.append("decode(grouping(g.mingc)+grouping(d.mingc),2,'',1,'合计',d.mingc) diancmc,  \n");
			buffer.append("decode(grouping(g.mingc)+grouping(d.mingc)+grouping(f.id),1,'小计',0,max(m.mingc),'') meik,  \n");
			buffer.append("decode(grouping(f.id),0,max(c.mingc),'') faz , \n");
			buffer.append("decode(grouping(f.id),0,max(p.mingc),'') pinz , \n");
			buffer.append("decode(grouping(f.id),0,to_char(max(f.fahrq),'yyyy-mm-dd'),''), \n");
			buffer.append("decode(grouping(f.id),0,to_char(max(f.daohrq),'yyyy-mm-dd'),''),   \n");
			buffer.append("sum(f.ches) ches, sum(f.biaoz) biaoz, sum(f.jingz) jingz, sum(f.yuns) yuns, sum(f.yingk) yingk, sum(f.zongkd) zongkd   \n");
			buffer.append("from fahb f,diancxxb d,gongysb g,meikxxb m,pinzb p,chezxxb c    \n");
			buffer.append("  where f.diancxxb_id=d.id  \n" );
			buffer.append("  and f.gongysb_id=g.id  \n" );
			buffer.append("  and f.meikxxb_id=m.id  \n" );
			buffer.append("  and f.pinzb_id=p.id(+) \n" );
			buffer.append("  and f.faz_id=c.id(+)  \n" );
			buffer.append(" and f.daohrq>=to_date('"+getBeginriqDate()+"','yyyy-mm-dd')");
			buffer.append(" and f.daohrq<=to_date('"+getEndriqDate()+"','yyyy-mm-dd')");
			buffer.append(gongysCondition);
			buffer.append(diancCondition);
			buffer.append("  group by rollup(g.mingc,d.mingc,f.id) \n" );
			buffer.append("  order by grouping(g.mingc) desc,max(g.mingc),grouping(d.mingc) desc,max(d.mingc), grouping(f.id) desc,max(m.xuh)\n" );
		}
		ResultSet rs = con.getResultSet(buffer,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		if(getLeixSelectValue().getId()==1){//分厂
			ArrHeader = new String[1][13];
			ArrWidth = new int[] { 80,120,160,50,50,70,70,50,50,50,40,40,50};
			ArrHeader[0] = new String[] { "电厂", "供应商", "煤矿","发站(港)","品种","发货日期","到货日期","车(船)数","票数","净重","运损","盈亏","扣吨"};
			rt.setBody(new Table(rs, 1, 0,5));
			//
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.setTitle("数 量 表"+"(分厂)", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 2, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
			rt.setDefaultTitle(12, 2, "单位:(吨)" ,Table.ALIGN_RIGHT);
			//rt.setDefaultTitle(3, 2, "年月份:"+getNianfValue().getValue()+MM1+MM2,Table.ALIGN_RIGHT);
			rt.body.setPageRows(21);
			rt.body.mergeCol(1);
			rt.body.mergeCol(2);
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 13, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		}else {//分矿
			ArrHeader = new String[1][13];
			ArrWidth = new int[] {120,80,160,50,50,70,70,50,50,50,40,40,50};
			ArrHeader[0] = new String[] {"供应商","电厂", "煤矿",  "发站(港)","品种","发货日期","到货日期","车(船)数","票数","净重","运损","盈亏","扣吨"};
			rt.setBody(new Table(rs, 1, 0, 5));
			//
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.setTitle("数 量 表"+"(分矿)", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 2, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
			rt.setDefaultTitle(12, 2, "单位:(吨)" ,Table.ALIGN_RIGHT);
			//rt.setDefaultTitle(3, 2, "年月份:"+getNianfValue().getValue()+MM1+MM2,Table.ALIGN_RIGHT);
			rt.body.setPageRows(21);
			rt.body.mergeCol(1);
			rt.body.mergeCol(2);
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 13, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		}

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
	///////
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

	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("查询日期:"));
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
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("统计口径:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("LeixSelect");
		cb1.setWidth(80);
		tb1.addField(cb1);
		tb1.addText(new ToolbarText("-"));
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
		
		tb1.addText(new ToolbarText("供应商:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("GongysDropDown");
		cb2.setEditable(true);
		//meik.setWidth(60);
		//meik.setListeners("select:function(){document.Form0.submit();}");
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
    	String sql="select id,mingc\n" +
    	"from gongysb\n" + 
    	"where gongysb.fuid=0";
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"全部")) ;
        return ;
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
