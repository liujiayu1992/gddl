package  com.zhiren.jt.zdt.yansgl.rucmzbmcj;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Rucmzbmcj  extends BasePage {
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
//		return getQibb();
//		if (mstrReportName.equals(REPORT_NAME_Hetltj)) {
		return getZhiltj();
//		} else {
//			return "无此报表";
//		}
	}
	// 合同量分厂分矿分矿分厂统计报表
	private String getZhiltj() {
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("");
		String riq1=getBeginriqDate();
		String riq2=getEndriqDate();
		int jib=this.getDiancTreeJib();
		String diancCondition=
			"and dc.id in (select id\n" + 
			" from(\n" + 
			" select id from diancxxb\n" + 
			" start with id="+getTreeid()+"\n" + 
			" connect by fuid=prior id\n" + 
			" )\n" + 
			" union\n" + 
			" select id\n" + 
			" from diancxxb\n" + 
			" where id="+getTreeid()+") " ; 
		
		sbsql.append("select decode(dc.mingc,null,'总计',dc.mingc) as diancmc,  \n");
		sbsql.append("decode(grouping(dc.mingc)+grouping(mk.mingc),2,'',1,'小计',mk.mingc) as meikmc,  \n");
		sbsql.append("sum(fah.jingz) as jingz,sum(bum1.jingz) as choujzl,   \n");
		sbsql.append("sum(fah.ches) as ruccs,sum(bum1.ches) as choujcs,  \n");
		sbsql.append("decode(sum(fah.jingz),0,0,round(sum(bum1.mt*fah.jingz)/sum(fah.jingz),2)) as symt,  \n");
		sbsql.append("decode(sum(fah.jingz),0,0,round(sum(bum1.ad*fah.jingz)/sum(fah.jingz),2)) as syad,  \n");
		sbsql.append("decode(sum(fah.jingz),0,0,round(sum(bum1.vdaf*fah.jingz)/sum(fah.jingz),2)) as syvdaf,  \n");
		sbsql.append("decode(sum(fah.jingz),0,0,round(sum(bum1.std*fah.jingz)/sum(fah.jingz),2)) as systd,  \n");
		sbsql.append("decode(sum(fah.jingz),0,0,round(sum(bum1.qnet_ar*fah.jingz)/sum(fah.jingz),2)) as syqnet_ar,  \n");
		sbsql.append("decode(sum(fah.jingz),0,0,round(sum(bum2.mt*fah.jingz)/sum(fah.jingz),2)) as xymt,  \n");
		sbsql.append("decode(sum(fah.jingz),0,0,round(sum(bum2.ad*fah.jingz)/sum(fah.jingz),2)) as xyad,  \n");
		sbsql.append("decode(sum(fah.jingz),0,0,round(sum(bum2.vdaf*fah.jingz)/sum(fah.jingz),2)) as xyvdaf,  \n");
		sbsql.append("decode(sum(fah.jingz),0,0,round(sum(bum2.std*fah.jingz)/sum(fah.jingz),2)) as xystd,  \n");
		sbsql.append("decode(sum(fah.jingz),0,0,round(sum(bum2.qnet_ar*fah.jingz)/sum(fah.jingz),2)) as xyqnet_ar,  \n");
		sbsql.append("(decode(sum(fah.jingz),0,0,round(sum(bum1.qnet_ar*fah.jingz)/sum(fah.jingz),2))-  \n");
		sbsql.append("decode(sum(fah.jingz),0,0,round(sum(bum2.qnet_ar*fah.jingz)/sum(fah.jingz),2))) as rezc,  \n");
		sbsql.append("round((decode(sum(fah.jingz),0,0,round(sum(bum1.qnet_ar*fah.jingz)/sum(fah.jingz),2))-  \n");
		sbsql.append("decode(sum(fah.jingz),0,0,round(sum(bum2.qnet_ar*fah.jingz)/sum(fah.jingz),2)))/0.0041816) as rezc1, \n");
		sbsql.append("decode(sum(fah.jingz),0,0,round(sum(z.qnet_ar*fah.jingz)/sum(fah.jingz),2)) as qnet_ar, \n");
		sbsql.append("decode(sum(fah.jingz),0,0,round(sum(z.qnet_ar*fah.jingz)/sum(fah.jingz)/0.0041816)) as qnet_ar1 \n");
		sbsql.append("from   \n");
		sbsql.append("(select fh.diancxxb_id,fh.meikxxb_id,zl.id,sum(fh.jingz) as jingz,sum(fh.ches) as ches  \n");
		sbsql.append("from fahb fh ,zhilb zl  \n");
		sbsql.append("where fh.zhilb_id=zl.id and zl.huaysj>=to_date('"+riq1+"','yyyy-mm-dd')  \n");
		sbsql.append("and zl.huaysj<=to_date('"+riq2+"','yyyy-mm-dd')  \n");
		sbsql.append("group by fh.diancxxb_id,fh.meikxxb_id,zl.id) fah,  \n");
		sbsql.append("(select zl.id,sum(fh.jingz) as jingz,sum(fh.ches) as ches,  \n");
		sbsql.append("decode(sum(fh.jingz),0,0,sum(zlls.mt*fh.jingz)/sum(fh.jingz)) as mt,  \n");
		sbsql.append("decode(sum(fh.jingz),0,0,sum(zlls.ad*fh.jingz)/sum(fh.jingz)) as ad,  \n");
		sbsql.append("decode(sum(fh.jingz),0,0,sum(zlls.vdaf*fh.jingz)/sum(fh.jingz)) as vdaf,  \n");
		sbsql.append("decode(sum(fh.jingz),0,0,sum(zlls.std*fh.jingz)/sum(fh.jingz)) as std,  \n");
		sbsql.append("decode(sum(fh.jingz),0,0,sum(zlls.qnet_ar*fh.jingz)/sum(fh.jingz)) as qnet_ar   \n");
		sbsql.append("from zhilb zl,zhillsb zlls,fahb fh,bumb  \n");
		sbsql.append("where zlls.zhilb_id=zl.id and fh.zhilb_id=zl.id and zlls.bumb_id=bumb.id \n");
		sbsql.append("and zl.huaysj>=to_date('"+riq1+"','yyyy-mm-dd')  \n");
		sbsql.append("and zl.huaysj<=to_date('"+riq2+"','yyyy-mm-dd')  \n");
		sbsql.append("and bumb.mingc='燃料部门' group by zl.id) bum1,  \n");
		sbsql.append("(select zl.id,sum(fh.jingz) as jingz,sum(fh.ches) as ches,  \n");
		sbsql.append("decode(sum(fh.jingz),0,0,sum(zlls.mt*fh.jingz)/sum(fh.jingz)) as mt,  \n");
		sbsql.append("decode(sum(fh.jingz),0,0,sum(zlls.ad*fh.jingz)/sum(fh.jingz)) as ad,  \n");
		sbsql.append("decode(sum(fh.jingz),0,0,sum(zlls.vdaf*fh.jingz)/sum(fh.jingz)) as vdaf,  \n");
		sbsql.append("decode(sum(fh.jingz),0,0,sum(zlls.std*fh.jingz)/sum(fh.jingz)) as std,  \n");
		sbsql.append("decode(sum(fh.jingz),0,0,sum(zlls.qnet_ar*fh.jingz)/sum(fh.jingz)) as qnet_ar   \n");
		sbsql.append("from zhilb zl,zhillsb zlls,fahb fh,bumb  \n");
		sbsql.append("where zlls.zhilb_id=zl.id and fh.zhilb_id=zl.id and bumb.id=zlls.bumb_id \n");
		sbsql.append("and zl.huaysj>=to_date('"+riq1+"','yyyy-mm-dd')  \n");
		sbsql.append("and zl.huaysj<=to_date('"+riq2+"','yyyy-mm-dd')  \n");
		sbsql.append("and bumb.mingc='监督部门' group by zl.id) bum2,  \n");
		sbsql.append("diancxxb dc,meikxxb mk,zhilb z  \n");
		sbsql.append("where fah.diancxxb_id=dc.id and fah.meikxxb_id=mk.id  \n");
		sbsql.append("and bum1.id=z.id and bum2.id=z.id \n");
		sbsql.append("and fah.id=bum1.id and fah.id=bum2.id  \n");
		sbsql.append("group by rollup(dc.mingc,mk.mingc) \n");
		sbsql.append("order by diancmc desc,meikmc desc   \n");
		
//		System.out.println(sbsql);
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		
		 String ArrHeader[][]=new String[2][20];
		 ArrHeader[0]=new String[] {"电厂","供货单位","净重(吨)","抽检数量(吨)","入厂车数(车）","抽检车数(车）","燃料部门","燃料部门","燃料部门","燃料部门","燃料部门","监督部门","监督部门","监督部门","监督部门","监督部门","差值","差值","采用值","采用值"};
		 ArrHeader[1]=new String[] {"电厂","供货单位","净重(吨)","抽检数量(吨)","入厂车数(车）","抽检车数(车）","水分Mt(%)","灰分Ad(%)","挥发分Vdaf(%)","硫分St,d(%)","低位发热量MJ/kg","水分Mt(%)","灰分Ad(%)","挥发分Vdaf(%)","硫分St,d(%)","低位发热量MJ/kg","MJ/kg","kcal/kg","MJ/kg","kcal/kg"};

		 int ArrWidth[]=new int[] {100,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54,54};

			Table bt=new Table(rs,2,0,2);
			rt.setBody(bt);
			bt.setColAlign(2, Table.ALIGN_CENTER);
			
			if(rt.body.getRows()>2){
				rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
			}
			//
			rt.body.ShowZero=false;
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.body.mergeFixedRow();//合并行
			rt.body.mergeFixedCols();//和并列
			rt.setTitle("入厂质量部门抽检情况", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 4, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
//			rt.setDefaultTitle(12, 2, "单位:(吨)" ,Table.ALIGN_RIGHT);
			rt.setDefaultTitle(9,3,riq1+"至"+riq2,Table.ALIGN_LEFT);
			rt.body.setPageRows(21);
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 3, "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
			rt.setDefautlFooter(15, 3, "审核人：", Table.ALIGN_CENTER);
			rt.setDefautlFooter(18, 3, "填报人：", Table.ALIGN_CENTER);
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
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	///////

//	开始日期v
	private boolean _BeginriqChange=false;
	/*public Date getBeginriqDate() {
		if (((Visit)getPage().getVisit()).getDate2()==null){
			((Visit)getPage().getVisit()).setDate2(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
		}
		return ((Visit)getPage().getVisit()).getDate2();
	}
	
	public void setBeginriqDate(Date _value) {
		if (DateUtil.FormatDate(((Visit)getPage().getVisit()).getDate2()).equals(DateUtil.FormatDate(_value))) {
			_BeginriqChange=false;
		} else {
			((Visit)getPage().getVisit()).setDate2(_value);
			_BeginriqChange=true;
		}
	}*/

	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
//				Calendar stra=Calendar.getInstance();
//				stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date())-1, 1);
				((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay))));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}

	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
//			Calendar stra=Calendar.getInstance();
//			stra.set(DateUtil.getYear(new Date()), 0, 1);
//			stra.setTime(new Date());
//			stra.add(Calendar.DATE,-1);
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
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
		
		tb1.addText(new ToolbarText("至"));
		tb1.addText(new ToolbarText("-"));
		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2","");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		tb1.addField(df1);
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
			
			this.setTreeid(null);
			this.getTreeid();
			visit.setString4(null);
			visit.setString5(null);
		}
		getToolbars();

		blnIsBegin = true;
 
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
//		得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
		public int getDiancTreeJib() {
			JDBCcon con = new JDBCcon();
			int jib = -1;
			String DiancTreeJib = this.getTreeid();
			//System.out.println("jib:" + DiancTreeJib);
			if (DiancTreeJib == null || DiancTreeJib.equals("")) {
				DiancTreeJib = "0";
			}
			String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
			ResultSet rs = con.getResultSet(sqlJib.toString());
			
			try {
				while (rs.next()) {
					jib = rs.getInt("jib");
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}finally{
				con.Close();
			}

			return jib;
		}
//		得到电厂树下拉框的电厂名称或者分公司,集团的名称
		public String getTreeDiancmc(String diancmcId) {
			if(diancmcId==null||diancmcId.equals("")){
				diancmcId="1";
			}
			String IDropDownDiancmc = "";
			JDBCcon cn = new JDBCcon();
			
			String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
			ResultSet rs = cn.getResultSet(sql_diancmc);
			try {
				while (rs.next()) {
					IDropDownDiancmc = rs.getString("mingc");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				cn.Close();
			}
			return IDropDownDiancmc;
		}
//		 分公司下拉框
		private boolean _fengschange = false;

		public IDropDownBean getFengsValue() {
			if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
				((Visit) getPage().getVisit())
						.setDropDownBean4((IDropDownBean) getFengsModel()
								.getOption(0));
			}
			return ((Visit) getPage().getVisit()).getDropDownBean4();
		}

		public void setFengsValue(IDropDownBean Value) {
			if (getFengsValue().getId() != Value.getId()) {
				_fengschange = true;
			}
			((Visit) getPage().getVisit()).setDropDownBean4(Value);
		}

		public IPropertySelectionModel getFengsModel() {
			if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
				getFengsModels();
			}
			return ((Visit) getPage().getVisit()).getProSelectionModel4();
		}

		public void setDiancxxModel(IPropertySelectionModel value) {
			((Visit) getPage().getVisit()).setProSelectionModel4(value);
		}

		public void getFengsModels() {
			String sql;
			sql = "select id ,mingc from diancxxb where jib=2 order by id";
			setDiancxxModel(new IDropDownModel(sql,"中国大唐集团"));
		}
	
//		得到系统信息表中配置的报表标题的单位名称
		public String getBiaotmc(){
			String biaotmc="";
			JDBCcon cn = new JDBCcon();
			String sql_biaotmc="select  zhi from xitxxb where mingc='报表标题单位名称'";
			ResultSet rs=cn.getResultSet(sql_biaotmc);
			try {
				while(rs.next()){
					 biaotmc=rs.getString("zhi");
				}
				rs.close();
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}finally{
				cn.Close();
			}
				
			return biaotmc;
			
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
