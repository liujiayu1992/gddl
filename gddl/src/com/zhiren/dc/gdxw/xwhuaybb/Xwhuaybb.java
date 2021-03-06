package com.zhiren.dc.gdxw.xwhuaybb;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.Locale;
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
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Xwhuaybb extends BasePage {
	

	private String msg="";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO 自动生成方法存根
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
	private int _CurrentPage = -1;
	public int getCurrentPage() {
		return _CurrentPage;
	}
	public void setCurrentPage(int _value) {
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
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

//	页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public boolean getRaw() {
		return true;
	}
//	厂别下拉框
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id());
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}

    
//  获得选择的树节点的对应的电厂名称   
    private String getMingc(String id){ 
		JDBCcon con=new JDBCcon();
		String mingc=null;
		String sql="select mingc from diancxxb where id = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=rsl.getString("mingc");
			
		}
		rsl.close();
		return mingc;
	}
    
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
    



	
//	获取表表标题
	public String getRptTitle() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sb=visit.getDiancqc()+"燃料验收单";
		
		return sb;
	}
	
//	刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

//		供应商下拉框
		
		ComboBox cb_leib = new ComboBox();
		cb_leib.setTransform("LEIB");
		cb_leib.setWidth(100);
		cb_leib.setListeners("select:function(){document.forms[0].submit();}");
		cb_leib.setEditable(true);
		tb1.addField(cb_leib);
		tb1.addText(new ToolbarText("-"));
		
		
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		//dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setListeners("change:function(own,newValue,oldValue){document.getElementById('BRIQ').value = newValue.dateFormat('Y-m-d'); }");
		dfb.setId("guohrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		//dfe.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
		dfe.setListeners("change:function(own,newValue,oldValue){document.getElementById('ERIQ').value = newValue.dateFormat('Y-m-d'); " +
		" document.forms[0].submit();}");
		dfe.setId("guohrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
		
		//供应商下拉框
		tb1.addText(new ToolbarText("煤矿单位:"));
		ComboBox CB_GONGYS = new ComboBox();
		CB_GONGYS.setTransform("GONGYS");
		CB_GONGYS.setWidth(150);
		CB_GONGYS.setListeners("select:function(){document.forms[0].submit();}");
		CB_GONGYS.setEditable(true);
		tb1.addField(CB_GONGYS);
		tb1.addText(new ToolbarText("-"));
		
		
//		电厂Tree
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
		
		

		
		//如果是一厂多制就显示电厂树,如果不是就不显示电厂
		if(hasDianc(String.valueOf(visit.getDiancxxb_id()))){
			tb1.addText(new ToolbarText("电厂:"));
			tb1.addField(tf1);
			tb1.addItem(tb3);
			tb1.addText(new ToolbarText("-"));
		}
		
		
		
		

		tb1.setWidth("bodyWidth");
		
		

		
		
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	
	public String getPrintTable(){
		
		
			return getYansd();
		
	}
	

	
	public String getYansd(){
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer talbe=new StringBuffer();	//报表输出
		ResultSetList rs=null;
		String[] strFormat=null;
		sbsql.setLength(0);
		
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
		
		long meik=this.getGongysValue().getId();
		String meiktiaoj="";
		if(meik==-1){
			meiktiaoj="";
		}else{
			meiktiaoj=" and f.meikxxb_id= "+meik;
		}
		
		String riqitiaoj="";
		String riqixiala=this.getLeibValue().getValue();
		if(riqixiala.endsWith("化验日期")){
			riqitiaoj="and z.huaysj>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
			"and z.huaysj<=to_date('"+jies+"','yyyy-mm-dd')\n" ;
		}else{
			riqitiaoj="and f.daohrq>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
			"and f.daohrq<=to_date('"+jies+"','yyyy-mm-dd')\n" ;
			
		}
			// 带有汇总的报表
		sbsql.append(
					"select z.huaybh,max(m.mingc) as meik,to_char(min(f.daohrq),'yyyy-mm-dd') as daohrq,round_new(sum(f.laimsl),2) as laiml,\n" +
					"decode( sum(f.laimsl),0,0,round_new(sum(z.had*f.laimsl)/sum(f.laimsl),2) ) as had,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.mt*f.laimsl)/sum(f.laimsl),2) )as mt,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.mad*f.laimsl)/sum(f.laimsl),2)) as mad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum((100*(z.mt-z.mad)/(100-z.mad))*f.laimsl)/sum(f.laimsl),2)) as neis,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.aad*f.laimsl)/sum(f.laimsl),2)) as aad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.ad*f.laimsl)/sum(f.laimsl),2)) as ad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.vad*f.laimsl)/sum(f.laimsl),2) )as vad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.stad*f.laimsl)/sum(f.laimsl),2)) as stad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.qbad*f.laimsl)/sum(f.laimsl),2) )as qbad,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.qgrad*f.laimsl)/sum(f.laimsl),2)) as qgrd,\n" + 
					"decode( sum(f.laimsl),0,0,round_new(sum(z.qnet_ar*f.laimsl)/sum(f.laimsl),2)) as qnet_ar,\n" + 
					"to_char(z.huaysj,'yyyy-mm-dd') as huaysj,max(z.huayy) as huayy\n" + 
					" from zhilb z,fahb f,meikxxb m\n" + 
					"where f.meikxxb_id=m.id\n" + 
					"and f.zhilb_id=z.id\n" + 
					""+riqitiaoj+"\n" + 
					
					" "+meiktiaoj+"\n"+
					"group by rollup (z.huaysj,z.huaybh)\n" + 
					"having not (grouping(z.huaybh)=1 and grouping(z.huaysj)=0) \n"+
					"order by  z.huaysj,max(m.mingc),z.huaybh"
			);
	
		
		
		
			rs=con.getResultSetList(sbsql.toString());
			
			
			
			Report rt = new Report(); //报表定义
			String ArrHeader[][]=new String[1][17];
//			1120
			ArrHeader[0]=new String[] {"编号","煤矿名称","来煤日期","煤量","氢","全水","内水","外水","空干灰","干灰","挥发份","全硫","弹筒热","高位热","低位热","化验日期","化验员"};
			int ArrWidth[]=new int[] {50,100,100,55,45,45,45,45,45,45,45,45,45,45,45,70,45};
			  strFormat = new String[] { "", "","",
	    				"0.00", "0.00", "0.00", "0.00", "0.00","0.00", "0.00",  "0.00", 
	    				"0.00", "0.00","0.00", "0.00","","" };
			// 数据
			rt.setTitle("煤质化验中心煤分析报表",ArrWidth);
			rt.setDefaultTitle(1,3,"单位:"+visit.getDiancqc(),Table.ALIGN_LEFT);
			
			rt.setBody(new Table(rs, 1, 0, 0));
			rt.body.setColFormat(strFormat);
			rt.body.setPageRows(23);
			
			
		
			
				int iLastRow=rt.body.getRows();
				int yangges=iLastRow-2;
				if(iLastRow>1){
					rt.body.setCellValue(iLastRow, 2, "合计:"+yangges+"个样");
					rt.body.setCellValue(iLastRow, 3, "");
					rt.body.setCellValue(iLastRow, 16, "");
					rt.body.setCellValue(iLastRow, 17, "");
					
				}
	//			设定小计行的背景色和字体
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 1);
					if((xiaoj.equals(""))){
						for (int j=0;j<rt.body.getCols()+1;j++){
							rt.body.getCell(i, j).backColor="silver";
							rt.body.getCell(i, j).fontBold=true;
						}
					}
				}
			
			
			
			rt.body.setWidth(ArrWidth);

			rt.body.setHeaderData(ArrHeader);// 表头数据

			rt.body.ShowZero = true;
			
			for (int i=1;i<=rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
			
			rt.body.setColAlign(2, Table.ALIGN_LEFT);

			rt.createFooter(1, ArrWidth);
			rt.setDefautlFooter(1, 4, "打印日期："+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
		
		
			if(rt.body.getPages()>0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(21);
			con.Close();
	     	return rt.getAllPagesHtml();// ph;
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
		String sql = "select id,mingc from diancxxb";
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
	
//	-------------------------电厂Tree END-------------------------------------------------------------
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getToolbar().getRenderScript();
	}
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			setChangbValue(null);
			setChangbModel(null);

			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
			
			
		}
		
		
		getSelectData();
	}
	
//	按钮的监听事件
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

//	表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		
		if (_RefurbishChick) {
			_RefurbishChick = false;
		
			
		}
		
	}
//	页面登陆验证
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
	
	
	
	
	
	
	
//	 供应商下拉框
	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setGongysValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setGongysModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getGongysModels() {
		
		String sql_gongys = "select g.id,g.mingc from meikxxb g order by g.mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql_gongys,"全部"));
		return;
	}
	
	
	
//	 类别(化验日期,来煤日期)
	public IDropDownBean getLeibValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getLeibModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setLeibValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setLeibModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getLeibModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getLeibModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getLeibModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "化验日期"));
		list.add(new IDropDownBean(2, "来煤日期"));
		
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(list));
		return;
	}
}
