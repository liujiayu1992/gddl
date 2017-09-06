package com.zhiren.dtrlgs.shoumgl.shoumgs;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dtrlgs.pubclass.FmisInterface;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class ShoumgsReport extends BasePage {
	private String _msg = "";

	public void setMsg(String _value) {
		 _msg = MainGlobal.getExtMessageBox(_value,false);
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	protected void initialize() {
		super.initialize();
		this.setMsg("");
  }
//	绑定日期
//	private String briq;
//
//	public String getBRiq() {
//		return briq;
//	}
//
//	public void setBRiq(String briq) {
//		this.briq = briq;
//	}
	
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
			return getShoumgsReport();
		} else {
			return "无此报表";
		}
	}
	
	private String getShoumgsReport() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
//		String briq=DateUtil.FormatOracleDate(this.getBRiq());
	    String  eriq=DateUtil.FormatOracleDate(this.getERiq());
	   /* String sql = 
	    	"select gmingc,dmingc,chec,to_char(daohrq,'yyyy-mm-dd') daohrq,decode(leix,1,'未化验',2,'化验',3,'重估',4,'已入账') as leix,\n" +
	    	"       jingz,rez, round(meij,2)+round(yunf,2)+round(zaf,2) as zonghj,\n" + 
	    	"       decode(rez,0,0,round_new((meij+yunf+zaf)*7000/rez,2)) as biaomdj,\n" + 
	    	"       heth,hetjg,hetbz,hetzk,meij,meis,yunf,zaf,yunfs,fazzf,\n" + 
	    	"      round_new((meij + yunf + zaf)*jingz,2)  as buhsmk,\n"+
             "round_new(round_new((meij + yunf + zaf)*jingz,2)*0.17,2) as shuik,\n"+
             " round_new(round_new((meij + yunf + zaf)*jingz,2)*1.17,2) as hansmk\n"+ 
	    	"from(\n" + 
	    	"    select decode(grouping(t.gmingc),1,'总计',t.gmingc) as gmingc,\n" + 
	    	"           decode(grouping(t.gmingc)+grouping(t.dmingc),1,'合计',t.dmingc) as dmingc,\n" + 
	    	"           t.chec,\n" + 
	    	"           t.leix,\n" + 
	    	"           t.daohrq,\n" + 
	    	"           --sum(tb.jingz) as jingz,\n" + 
	    	"           decode(sum(t.jingz),0,0,round_new(sum(t.rez) / sum(t.jingz), 0)) as rez,\n" + 
	    	"           t.heth,\n" + 
	    	"           decode(sum(t.jingz),0,0,round_new(sum(t.hetjg) / sum(t.jingz), 2)) as hetjg,\n" + 
	    	"			t.hetbz,\n" +
	    	"           decode(sum(t.jingz),0,0,round_new(sum(t.hetzk) / sum(t.jingz), 2)) as hetzk,"+
	    	"           decode(sum(t.jingz),0,0,round_new(sum(t.meij) / sum(t.jingz), 7)) as meij,\n" + 
	    	"           decode(sum(t.jingz),0,0,round_new(sum(t.meis) / sum(t.jingz), 2)) as meis,\n" + 
	    	"           decode(sum(t.jingz),0,0,round_new(sum(t.yunf) / sum(t.jingz), 2)) as yunf,\n" + 
	    	"           decode(sum(t.jingz),0,0,round_new(sum(t.zaf) / sum(t.jingz), 2)) as zaf,\n" + 
	    	"           decode(sum(t.jingz),0,0,round_new(sum(t.yunfs) / sum(t.jingz), 2)) as yunfs,\n" + 
	    	"           decode(sum(t.jingz),0,0,round_new(sum(t.fazzf) / sum(t.jingz), 2)) as fazzf\n" + 
	    	"            ,sum( t.jingz) jingz"+
	    	"    from(\n" + 
	    	"          select  f.id fahbid, \n" + 
	    	"                 g.mingc gmingc,\n" + 
	    	"                 d.mingc dmingc,\n" + 
	    	"                 f.chec,\n" + 
	    	"                 s.leix,\n" + 
	    	"                 f.daohrq,\n" + 
	    	"                 f.jingz as jingz,\n" + 
	    	"                 round_new(f.jingz,0)*s.rez as rez,\n" + 
	    	"                 s.heth,\n" + 
	    	"                 round_new(f.jingz,0)*s.hetjg as  hetjg,\n" + 
	    	"				  s.hetbz,\n" +
	    	"                 round_new(f.jingz,0)*s.hetzk as hetzk,\n"+
	    	"                 round_new(f.jingz,0)*s.meij as meij,\n" + 
	    	"                 round_new(f.jingz,0)*s.meis as meis,\n" + 
	    	"                 round_new(f.jingz,0)*s.yunf as yunf,\n" + 
	    	"                 round_new(f.jingz,0)*s.zaf as zaf,\n" + 
	    	"                 round_new(f.jingz,0)*s.fazzf as fazzf,\n" + 
	    	"                 round_new(f.jingz,0)*s.yunfs as yunfs\n" + 
	    	"          from shoumgslsb s,\n" + 
	    	"               fahb f,\n" + 
//	    	"               luncxxb l,\n" + 
	    	"               (select max(id) as id,fahbid from shoumgslsb group by (fahbid)) ff,\n" + 
	    	"               (select id,mingc,fuid from diancxxb where cangkb_id<>1) d,\n" + 
	    	"               (select id,mingc from gongysb) g\n" + 
	    	"          where s.id=ff.id and s.fahbid=ff.fahbid \n" + 
	    	"            and s.fahbid=f.id\n" + 
	    	"            and s.leix<>4   \n" +
//	    	"            and (s.leix<>4 or (s.leix=4 and s.fmisjksj is null))  \n" +
	    	"            and f.diancxxb_id=d.id\n" + 
	    	"            and f.gongysb_id=g.id\n" + 
	    	"            and f.FAHRQ<=" + eriq + "\n" ;
	    	if(getTreeJib()==1){
				sql+="	and d.jib=3 \n";
			}else if(getTreeJib()==2){
				sql+="	and (d.fuid=" + getTreeid() + " or d.id="+ getTreeid() +")\n";
			}else{
				sql+="	and (d.id=" + getTreeid() + "or d.fuid=" + getTreeid() + ")\n";
			}
	    	sql+="         -- group by(s.fahbid,s.leix,g.mingc, d.mingc,f.chec,f.daohrq,s.heth,s.hetbz)\n" + 
	    	"    )t\n" + 
	    	"    group by rollup(t.gmingc,(t.fahbid,t.leix,t.dmingc,t.chec,t.daohrq,t.heth,t.hetbz))\n" + 
	    	"    order by grouping(t.gmingc) desc,t.gmingc, grouping(t.dmingc) desc,t.dmingc \n" + 
	    	")";
	    	*/
	    	
	    	
	    String sql=	" select gmingc,dmingc,chec,to_char(daohrq,'yyyy-mm-dd') daohrq, duow ,\n" + 
	       "  jingz,rez, round(meij,2)+round(yunf,2)+round(zaf,2) as zonghj,\n" + 
	       " decode(rez,0,0,round_new((meij+yunf+zaf)*7000/rez,2)) as biaomdj,\n" + 
	        " heth,hetjg,hetbz,hetzk,meij,meis,yunf,zaf,yunfs,fazzf,\n" + 
	       " round_new((meij + yunf + zaf)*jingz,2)  as buhsmk,\n" + 
	  "round_new(round_new((meij + yunf + zaf)*jingz,2)*0.17,2) as shuik,\n" + 
	  " round_new(round_new((meij + yunf + zaf)*jingz,2)*1.17,2) as hansmk\n" + 
	 " from(\n" + 
	    "  select decode(grouping(t.gmingc),1,'总计',t.gmingc) as gmingc,\n" + 
	     "        decode(grouping(t.gmingc)+grouping(t.dmingc),1,'合计',t.dmingc) as dmingc,\n" + 
	     "        t.chec,\n" + 
	      "       --t.leix,\n" + 
	      "       t.daohrq,\n" + 
	     "        --sum(tb.jingz) as jingz,\n" + 
	     "        decode(sum(t.jingz),0,0,round_new(sum(t.rez) / sum(t.jingz), 0)) as rez,\n" + 
	     "        t.heth,\n" + 
	      "       decode(sum(t.jingz),0,0,round_new(sum(t.hetjg) / sum(t.jingz), 2)) as hetjg,\n" + 
	  	"		t.hetbz,\n" + 
	      "       decode(sum(t.jingz),0,0,round_new(sum(t.hetzk) / sum(t.jingz), 2)) as hetzk, \n" +         
	      "         decode(sum(t.jingz),0,0,round_new(sum(t.meij) / sum(t.jingz), 7)) as meij,\n" + 
	      "       decode(sum(t.jingz),0,0,round_new(sum(t.meis) / sum(t.jingz), 2)) as meis,\n" + 
	      "       decode(sum(t.jingz),0,0,round_new(sum(t.yunf) / sum(t.jingz), 2)) as yunf,\n" + 
	     "        decode(sum(t.jingz),0,0,round_new(sum(t.zaf) / sum(t.jingz), 2)) as zaf,\n" + 
	     "        decode(sum(t.jingz),0,0,round_new(sum(t.yunfs) / sum(t.jingz), 2)) as yunfs,\n" + 
	     "        decode(sum(t.jingz),0,0,round_new(sum(t.fazzf) / sum(t.jingz), 2)) as fazzf\n" + 
	     "         ,sum( t.jingz) jingz ,\n" + 
	     "         t.duow\n" + 
	      "          from(\n" + 
	      "    select  f.id fahbid, \n" + 
	     "              g.mingc gmingc,\n" + 
	     "              d.mingc dmingc,\n" + 
	     "              f.chec,\n" + 
	     "              s.leix,\n" + 
	    "               f.daohrq,\n" + 
	     "              f.jingz as jingz,\n" + 
	     "              round_new(f.jingz,0)*s.rez as rez,\n" + 
	     "              s.heth,\n" + 
	     "              round_new(f.jingz,0)*s.hetjg as  hetjg,\n" + 
	  	"			  s.hetbz,\n" + 
	     "              round_new(f.jingz,0)*s.hetzk as hetzk,\n" + 
	     "              round_new(f.jingz,0)*s.meij as meij,\n" + 
	     "              round_new(f.jingz,0)*s.meis as meis,\n" + 
	     "              round_new(f.jingz,0)*s.yunf as yunf,\n" + 
	    "               round_new(f.jingz,0)*s.zaf as zaf,\n" + 
	     "              round_new(f.jingz,0)*s.fazzf as fazzf,\n" + 
	    "               round_new(f.jingz,0)*s.yunfs as yunfs,\n" + 
	   "                substr(dwgs.mingc,0,1)||'-'||m.mingc duow\n" + 
	                 
	"  from\n" + 
	 "   (select id,fahbid,leix,meij,yunf,riq,rez,zaf,heth ,hetjg,hetbz,hetzk,yunfs,fazzf,meis from shoumgslsb where id in (select max(id) from shoumgslsb gs group by gs.fahbid) and leix<>4 ) s,\n" + 
	 "   (select id,mingc,fuid from diancxxb where cangkb_id<>1) d,\n" + 
	 "   (select id,quanc,bianm,mingc from gongysb) g,\n" + 
	 "   fahb f,meicb m,duowgsb dwgs,duimxxb dm,duowkcb kc,yewlxb lx,hetb ht,hetjgb jg\n" +  
	"  where s.fahbid=f.id and f.diancxxb_id=d.id and f.gongysb_id=g.id and dm.fahb_id=f.id and dm.meicb_id=m.id and f.yewlxb_id=lx.id \n" + 
	"    and kc.duiqm_id=dm.id and m.duowgsb_id=dwgs.id(+) and f.daohrq<"+eriq+" and f.hetb_id=ht.id and ht.id=jg.hetb_id \n" ;
	if(getTreeJib()==1){
		sql+="	and d.jib=3 \n";
	}else if(getTreeJib()==2){
		sql+="	and (d.fuid=" + getTreeid() + " or d.id="+ getTreeid() +")\n";
	}else{
		sql+="	and (d.id=" + getTreeid() + "or d.fuid=" + getTreeid() + ")\n";
	}
	sql+= "     )t\n" + 
	 "     group by rollup(t.gmingc,(t.fahbid,t.dmingc,t.chec,t.daohrq,t.heth,t.hetbz,t.duow))\n" + 
	"      order by grouping(t.gmingc) desc,t.gmingc, grouping(t.dmingc) desc,t.dmingc \n" + 
	"  )\n";
		ResultSet rs = con.getResultSet(sql);
		
		Report rt = new Report();
		
		String ArrHeader[][]=new String[1][22];
		ArrHeader[0]=new String[] {"供应商","收货单位","车次/<br>船次","到货<br>日期","垛位","净重",
									"热值","综合价","标煤<br>单价","合同号","合同<br>价格","合同<br>标准","合同<br>增扣",
									"煤价","煤价税","运费","杂费","运费税","发站<br>杂费","不含税煤款"," 税款","含税煤款"};
		int[] ArrWidth = new int[22];
		ArrWidth=new int[] {80,80,40,70,80,50,40,40,40,110,40,70,40,60,40,40,40,40,40,80,80,80};//960/1140

		rt.setTitle("收煤估收查询", ArrWidth);
		rt.setDefaultTitle(1, 5, "制表单位:"
				+ visit.getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(10,4 , "暂估日期:"
//				+ getBRiq() + "至"
				+getERiq(),
				Table.ALIGN_CENTER);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);
		rt.setBody(new Table(rs, 1, 0, 5));//行，0，列 
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);//600/790
		rt.body.setColAlign(4, Table.ALIGN_LEFT);
		rt.body.setColAlign(10, Table.ALIGN_LEFT);
		rt.body.setColAlign(12, Table.ALIGN_LEFT);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(8, 2, "主管:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 2, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(12, 2, "制表人:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(21, 2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );
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

	private boolean _FmisClick = false;

	public void FmisButton(IRequestCycle cycle) {
		_FmisClick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
		if (_FmisClick) {
			_FmisClick = false;
			fmis();
		}
	}
	
	private void fmis(){
		
		FmisInterface fi = new FmisInterface();
		
		String msg = fi.ZgFmis(DateUtil.getDate(getERiq()), FmisInterface.CAIG_DATA);
		
		setMsg(msg);
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
//		tb1.addText(new ToolbarText("到货日期:"));
//		DateField dfb = new DateField();
//		dfb.setValue(getBRiq());
//		dfb.Binding("BRIQ", "");// 与html页中的id绑定
//		dfb.setId("rqb");
//		tb1.addField(dfb);
		
		tb1.addText(new ToolbarText(" 暂估日期 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// 与html页中的id绑定
		dfe.setId("rqe");
		tb1.addField(dfe);
		
		tb1.addText(new ToolbarText("-"));
		
		/*tb1.addText(new ToolbarText("供应商:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("YewlxbDropDown");
		cb2.setEditable(true);
		cb2.setWidth(100);
		tb1.addField(cb2);
		
		tb1.addText(new ToolbarText("-"));*/
		
		ToolbarButton tbb = new ToolbarButton(null,"查询","function(){document.Form0.submit();}");
		tbb.setIcon(SysConstant.Btn_Icon_Print);
		
		String scr=
			"function(){ Ext.MessageBox.confirm('警告', '确定传送fmis接口吗？', function(btn) { if(btn=='yes'){document.getElementById('FmisButton').click();\n" +
			"Ext.MessageBox.show(\n" + 
			"    {msg:'正在处理数据,请稍后...',\n" + 
			"     progressText:'处理中...',\n" + 
			"     width:300,\n" + 
			"     wait:true,\n" + 
			"     waitConfig: {interval:200},\n" + 
			"     icon:Ext.MessageBox.INFO\n" + 
			"     }\n" + 
			");}})" +
//			"Ext.MessageBox.alert('提示信息','成功传入FMIS接口');"+
			"}";
		ToolbarButton tbb2 = new ToolbarButton(null,"传FMIS接口",scr);
		tbb.setIcon(SysConstant.Btn_Icon_Create);
		
		tb1.addItem(tbb);
		tb1.addItem(tbb2);
		
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
//			this.setBRiq(rq);
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
//			setMsg(null);			
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