package com.zhiren.heiljkhh;

import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
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
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * 作者:tzf
 * 时间:2009-5-8
 * 内容:实现 黑龙江 省公司个性化盘点报告
 */
public class Pandbg extends BasePage implements PageValidateListener {

	public boolean getRaw() {
		return true;
	}

	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
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

	public String getPrintTable() {
		return getHuaybgd();
	}
	
	private StringBuffer getMeicSql(){

		StringBuffer bf = new StringBuffer();
		Visit v = (Visit) getPage().getVisit();
		bf.append(" select mc.mingc mingc,pt.dingc dingc,pt.dingk dingk,pt.dingc*pt.dingk dingmj,pt.duig duig,\n")
		  .append(" pt.dic dic,pt.dik dik,pt.dic*pt.dik dimj,pt.tij tij,pt.mid mid,pt.cunml cunml \n")
		  .append("  from pandtjb pt,pandb pd,meicb mc \n")
		  .append(" where pt.pandb_id=pd.id and pt.meicb_id=mc.id \n")
		  .append(" and pd.bianm='"+this.getBianmValue().getValue()+"' \n")
		  .append(" and pd.diancxxb_id=").append(this.getTreeid_dc())
		  .append(" and pd.id=").append(this.getBianmValue().getStrId())
		  .append(" and mc.diancxxb_id=").append(this.getTreeid_dc())
		  .append(" group by  mc.mingc ,pt.dingc ,pt.dingk ,pt.duig , \n")
		  .append(" pt.dic ,pt.dik  ,pt.tij ,pt.mid ,pt.cunml \n")
		  .append(" order by mc.mingc ");
		return bf;
	}
	
	private String getPandZmmSql(){
	String	sSql = " select id,diancxxb_id,zhangmkc,laimwsbkc,cuns,fadh,zhangmkc+laimwsbkc-cuns-fadh as zhangmhj,panyk,riq from (select pd.id id,pa.diancxxb_id diancxxb_id,nvl((select sum(nvl(p1.zhangmkc,0)) from pandzmm p1, pandb p where p.riq+1=pa.riq and p1.pandb_id=p.id and p.id="+this.getBianmValue().getStrId()+"  and p.diancxxb_id="+this.getTreeid_dc()+"),0) as zhangmkc,\n" 
			+ " nvl((select nvl(sh.kuc,0)+nvl(sh.dangrgm,0)-nvl(sh.shangbkc,0)  from shouhcrbb sh where sh.riq=pa.riq and  sh.diancxxb_id="+this.getTreeid_dc()+"),0) as laimwsbkc,\n"
			+ " nvl(pd.cuns,0) cuns,nvl(pd.fadh,0) fadh,nvl(pd.zhangmkc,0) zhangmhj,pd.zhangmkc-pd.shijkc as panyk,pa.riq as riq \n"
			+" from pandzmm pd,pandb pa where pd.pandb_id=pa.id and pa.diancxxb_id="+this.getTreeid_dc()+" and pa.bianm='"+this.getBianmValue().getValue()+"' and pa.id="+this.getBianmValue().getStrId()+")";
	
	return sSql;
	}
	// 燃料采购部指标完成情况日报
	private String getHuaybgd() {
		Report rt = new Report();
		Visit visit=(Visit)this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String sqlZmm=this.getPandZmmSql();
//		System.out.println(sqlHuaybgd.toString());
		ResultSetList rs = con.getResultSetList(sqlZmm);
		String zhangmkc = "";
		String laimwsbkc = "";
		String qucs="";
		String fadh="";
		String zhangmhj="";
		String panyk="";
		Date riq=new Date();
	//	String pandrq="";
		if(rs.next()){
			zhangmkc=rs.getString("zhangmkc");
			laimwsbkc=rs.getString("laimwsbkc");
			qucs=rs.getString("qucs");
			fadh=rs.getString("fadh");
			zhangmhj=rs.getString("zhangmhj");
			panyk=rs.getString("panyk");
			riq=rs.getDate("riq");
	//		pandrq=DateUtil.Formatdate("yyyy-MM-dd HH:mm", rs.getDate("riq"));
		}
	
		String[][] zmm=new String[][]{
				{"","","","","","","","","","",""},
				{"前一日帐面煤量","","","","","","","","","",zhangmkc},
				{"加来煤未报","","","","","","","","","",laimwsbkc},
				{"去储损","","","","","","","","","",qucs},
				{"去盘点前当日耗","","","","","","","","","",fadh},
				{"盘点时账面合计","","","","","","","","","",zhangmhj},
				{"盘点盈、亏量（+/－）","","","","","","","","","",panyk}};
		rs.close();
		
		StringBuffer sqlmeic = new StringBuffer();
		sqlmeic=this.getMeicSql();
		rs=con.getResultSetList(sqlmeic.toString());
		
		int rows=rs.getRows();
		int table_wid=rows+zmm.length+1;
		String[][] ArrHeader = new String[table_wid][11];
		 ArrHeader[0] = new String[]{"项目分类","上长度","上宽", "上底面积","高度","下底长","下底宽","下底面积","体积","含水密度","煤量"};
		
		int i=1;
		while(rs.next()){
			
			ArrHeader[i]=new String[]{rs.getString("mingc"),rs.getString("dingc"),rs.getString("dingk"),rs.getString("dingmj"),rs.getString("duig"),rs.getString("dic"),rs.getString("dik"),rs.getString("dimj"),rs.getString("tij"),rs.getString("mid"),rs.getString("cunml")};
			i++;
		}
		
		for(int j=0;j<zmm.length;j++){
			ArrHeader[i++]=zmm[j];
		}

		
		int[] ArrWidth = new int[] { 130, 80, 80, 80, 80, 80 ,80 ,80 ,80 ,80 ,80 };
		
		
		Table bt=new Table(table_wid,11);
		rt.setBody(bt);
		
		String[][] ArrHeader1 = new String[1][11];
		ArrHeader1[0] = ArrHeader[0];
		rt.body.setHeaderData(ArrHeader1);// 表头数据
		
		
		String title=visit.getDiancqc()+DateUtil.Formatdate("yyyy年MM月", riq)+"月现场盘点报告";
		rt.setTitle(title, ArrWidth);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 14);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		
		rt.setDefaultTitle(1, 3, "填报单位：" + visit.getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 2, "" + DateUtil.Formatdate("yyyy年MM月dd日",new Date()),
				Table.ALIGN_CENTER);
		rt.setDefaultTitle(9, 3, "盘点时间:"+DateUtil.Formatdate("MM月dd日HH时mm分", riq), Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		
		
		String[][] ArrFooter=new String[][]{{"主管领导:","监察审计部:","","经营管理部:" ,"","设备部:","","财务部:","","燃料管理部:",""},
				{"省公司计划营销部:","","","燃料管理中心:","","安生部:" ,"","财务部:","","",""}};
		
		Table footer=new Table(ArrFooter,0,0,0);
		footer.setWidth(ArrWidth);
	
		footer.setBorderNone();
		footer.setBorder(0);
		for(i=0;i<ArrFooter.length+1;i++)
			for(int j=0;j<12;j++){
				footer.setCellAlign(i, j, Table.ALIGN_LEFT);
				footer.setCellBorder(i, j, 0, 0, 0, 0);
			}
		rt.setFooter(footer);

		
		for ( i = 1; i < table_wid; i++) {
			for (int j = 0; j < 11; j++) {
				
				if (ArrHeader[i][j] == null || ArrHeader[i][j].length() == 0) {
					
					if(i<rows){
						ArrHeader[i][j] = "0.00";
					}else if(j==10 && i>rows+1){
						ArrHeader[i][j] = "0.00";
					}
					
				}
				
					ArrHeader[i][j] = rt.body.format(ArrHeader[i][j], "0.00");
				
				
				rt.body.setCellValue(i + 1, j + 1, ArrHeader[i][j]);
				
//				if(j!=0){
//					rt.body.setCellAlign(i+1, j+1, Table.ALIGN_RIGHT);
//				}
				
			}
		}
//		for (i = 1; i <= table_wid; i++) {
//			rt.body.setRowCells(i, Table.PER_FONTSIZE, 9);
//		}
		
		rt.body.setCells(2, 2, table_wid,11, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.ShowZero = false;

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		//rt.body.setRowHeight(43);

		return rt.getAllPagesHtml();

	}

	// 按钮的监听事件
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Visit visit=(Visit)this.getPage().getVisit();
			if(!this.getTreeid_dc().equals(visit.getString13())){
				this.setBianmModel(null);
				this.setBianmValue(null);
				this.setBianmModels();
			}
			getSelectData();
		}
	}
	
	
//	-------------------------电厂Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		Visit visit=(Visit)this.getPage().getVisit();
		String sql = " select d.id,d.mingc from diancxxb d where d.id="+visit.getDiancxxb_id()+" \n";
		sql+=" union \n";
		sql+="  select d.id,d.mingc from diancxxb d where d.fuid="+visit.getDiancxxb_id()+" \n";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	DefaultTree dc ;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc= etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	
//	-------------------------电厂Tree END----------
	
	
//  判断电厂Tree中所选电厂时候还有子电厂   
    private boolean hasDianc(String id){ 
		JDBCcon con=new JDBCcon();
		boolean mingc= false;
		String sql="select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=true;
		}
		rsl.close();
		return mingc;
	}
	
	// 刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("单位名称:"));
		
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
						: getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		tb1.addField(tf1);
		tb1.addItem(tb3);
		
		
		tb1.addText(new ToolbarText("盘点编号:"));
		ComboBox shij = new ComboBox();
		shij.setTransform("BianmSelect");
		shij.setWidth(130);
		shij
				.setListeners("select:function(own,rec,index){Ext.getDom('BianmSelect').selectedIndex=index}");
		tb1.addField(shij);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "查询",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		// tb1.addText(new ToolbarText("<marquee width=300
		// scrollamount=2></marquee>"));
		setToolbar(tb1);
	}


	
	// 工具栏使用的方法
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		// if(getTbmsg()!=null) {
		// getToolbar().deleteItem();
		// getToolbar().addText(new ToolbarText("<marquee width=300
		// scrollamount=2>"+getTbmsg()+"</marquee>"));
		// }
	//	((DateField) getToolbar().getItem("huayrq")).setValue(getRiq());
		
		return getToolbar().getRenderScript();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
		//	setRiq(DateUtil.FormatDate(new Date()));
			visit.setString3(null);
			visit.setProSelectionModel2(null);
			setBianmValue(null);
			setBianmModel(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean1(null);
			visit.setString13("");
		}
		//this.setBianmModels();
		visit.setString13(this.getTreeid_dc());//电厂id
		getSelectData();
		
		//if (riqchange) {
	//		riqchange = false;
		//	setBianmValue(null);
		//	setBianmModel(null);
		//}
		
	}


	
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public IDropDownBean getBianmValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			if (getBianmModel().getOptionCount() > 0) {
				setBianmValue((IDropDownBean) getBianmModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setBianmValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(value);
	}

	public IPropertySelectionModel getBianmModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setBianmModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setBianmModel(IPropertySelectionModel model) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(model);
	}

	private void setBianmModels() {
		Visit visit=(Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		String diancxxb_id=this.getTreeid_dc();
		sb
				.append(
						"select distinct pd.id id,pd.bianm bianm from pandtjb pt ,pandb pd where pt.pandb_id=pd.id and pd.diancxxb_id="+diancxxb_id+" order by pd.bianm desc \n");
//			System.out.println(sb.toString());
				
			
		setBianmModel(new IDropDownModel(sb.toString(), "请选择"));
	}


	public void pageValidate(PageEvent arg0) {
		// TODO 自动生成方法存根
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
