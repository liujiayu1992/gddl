package com.zhiren.dc.gdxw.guohcx;

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
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Guohcx extends BasePage {

	/**
	 * 作者:王总兵
	 * 时间:2009-9-29 17:15:06
	 * 内容:宣威chepbtmp过衡查询
	 */
	private static final String BAOBPZB_GUANJZ = "JILTZ_GJZ";// baobpzb中对应的关键字
//	界面用户提示
	private String msg="";

	public String getMsg() {
		return msg;
	}
	private String check="false";
	
	public String getCheck() {
		return check;
	}
	public void setCheck(String check) {
		this.check = check;
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

    

	
//	刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("来煤日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		//dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("guohrqb");
		dfb.setListeners("change:function(own,newValue,oldValue){document.getElementById('BRIQ').value = newValue.dateFormat('Y-m-d'); }");
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
		tb1.addText(new ToolbarText("过衡人员:"));
		ComboBox CB_GONGYS = new ComboBox();
		CB_GONGYS.setTransform("GONGYS");
		CB_GONGYS.setWidth(100);
		CB_GONGYS.setListeners("select:function(){document.forms[0].submit();}");
		CB_GONGYS.setEditable(true);
		tb1.addField(CB_GONGYS);
		tb1.addText(new ToolbarText("-"));
		
		Checkbox chk=new Checkbox();
		chk.setId("CHECKED");
		if(this.getCheck().equals("true")){
			chk.setChecked(true);
			
		}else{
			chk.setChecked(false);
		}
		chk.setListeners("check:function(own,checked){if(checked){document.all.CHECKED.value='true';}else{document.all.CHECKED.value='false'}}");
		tb1.addField(chk);
		tb1.addText(new ToolbarText("空衡人员"));
		
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
		if(visit.isFencb()){
			tb1.addText(new ToolbarText("电厂:"));
			tb1.addField(tf1);
			tb1.addItem(tb3);
			tb1.addText(new ToolbarText("-"));
		}
		
		
		
		
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("-"));
		//汇总明细
		tb1.addText(new ToolbarText("-"));
		ComboBox leix = new ComboBox();
		leix.setTransform("JIESLB");
		leix.setWidth(90);
		leix.setListeners("select:function(){document.forms[0].submit();}");
		//leix.setEditable(true);
		tb1.addField(leix);
		
		
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	
	public String getPrintTable(){
		
	
			
			
			if (getJieslbValue().getValue().equals("汇总")){
				return getMeikbbhz();
			}else if(getJieslbValue().getValue().equals("明细")){
				return getYansd();
			}else{
				return "无此报表";
			}
		
	}
	
	
	
	
	public String getYansd(){
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer talbe=new StringBuffer();	//报表输出
		ResultSetList rs=null;
		String[] strFormat=null;
		sbsql.setLength(0);
		Report rt = new Report(); //报表定义
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
		String tiaoj="";
		long meik=this.getGongysValue().getId();
		if(meik==-1){
			tiaoj="";
		}else{
					if(this.getCheck().equals("true")){
						tiaoj="and c.qingcjjy='"+this.getGongysValue().getValue()+"'\n";
						
					}else{
						tiaoj="and c.zhongcjjy='"+this.getGongysValue().getValue()+"'\n";
						
					}
		}
		
		
		
		

			sbsql.append(

					"select decode(c.meikdwmc,null,'总计:'||count(c.id)||'车',c.meikdwmc) as meikdwmc,\n" +
					"decode(grouping(c.cheph)+grouping(c.meikdwmc) ,1,'小计:'||count(c.id)||'车',c.cheph) as cheph,\n" + 
					"sum(c.maoz) as maoz,sum(c.piz) as piz,\n" + 
					"sum(c.maoz-c.piz-c.koud) as jingz,sum(c.koud) as koud,\n" + 
					"max(c.zhongcjjy) as zhongh,max(c.qingcjjy) as qingh,max(c.meigy) as meiyg,\n" + 
					"to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcsj,to_char(c.qingcsj,'yyyy-mm-dd hh24:mi:ss') as qingcsj\n"+
					"from chepbtmp c\n" + 
					"where c.qingcsj>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
					"and c.qingcsj<to_date('"+jies+"','yyyy-mm-dd')+1\n" + 
					""+tiaoj+"\n"+
					"group by rollup (c.meikdwmc,c.zhongcsj,c.cheph,c.qingcsj)\n" + 
					"having not (grouping(c.cheph)+grouping(c.zhongcsj)+grouping(c.qingcsj)=2 or grouping(c.zhongcsj)+grouping(c.qingcsj)=1)\n" + 
					"order by c.meikdwmc,zhongcsj"
);
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[1][11];
//				1120
				ArrHeader[0]=new String[] {"煤矿名称","车号","毛重","皮重","净重","扣吨","过重人员","过空人员","验煤章","过重时间","过空时间",};
				int ArrWidth[]=new int[] {100,65,60,55,50,55,70,70,50,120,120};
				  strFormat = new String[] { "", "","0.00","0.00","0.00","0.00","","","","",""};
				// 数据
				rt.setTitle("汽车煤进煤报告_(供煤矿点明细)",ArrWidth);
			
				
				rt.setBody(new Table(rs, 1, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(25);
				rt.setDefaultTitle(1, 4, "单位:"+visit.getDiancqc(), Table.ALIGN_LEFT);
				
				//清空小计行和总计行最后3列的数据
				int iLastRow=rt.body.getRows();
				int a=0;
				if(iLastRow>1){
					for(a=0;a<=iLastRow;a++){
						if(rt.body.getCellValue(a, 10).equals("")){
							rt.body.setCellValue(a, 7, "");
							rt.body.setCellValue(a, 8, "");
							rt.body.setCellValue(a, 9, "");
						}
						
					}
					
					
				}
//				设定小计行的背景色和字体
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 10);
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
				rt.createFooter(1, ArrWidth);
				rt.setDefautlFooter(1, 3, "打印日期："+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setRowHeight(21);
				con.Close();
				return rt.getAllPagesHtml();// ph;
			
	
		
		
		
	}
	
	
	

	public String getMeikbbhz(){


		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		StringBuffer talbe=new StringBuffer();	//报表输出
		Visit v = (Visit) getPage().getVisit();
		ResultSetList rs=null;
		String[] strFormat=null;
		
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(new Date());
		}
		
		
		
		Report rt = new Report(); //报表定义
		
			sbsql.setLength(0);
					sbsql.append(
							
							"select rownum as xuh,meikdwmc,ches,maoz,piz,koud,jingz ,koudl_sj,koudl_sz from (\n" +
							"select decode(c.meikdwmc,null,'总计',c.meikdwmc) as meikdwmc,\n" + 
							"count(*) as ches,sum(c.maoz) as maoz,\n" + 
							"sum(c.piz) as piz ,sum(c.koud) as koud, sum(c.maoz-c.piz-c.koud) as jingz,\n" + 
							"round(decode(sum(c.maoz-c.piz-c.koud),0,0,sum(c.koud)*100/sum(c.maoz-c.piz-c.koud)),2)  as koudl_sj,\n" + 
							"decode(max(sz.koudl),null,(select max(koudl) from meikkdlsz where meikmc='全部'), decode( meikdwmc,null,'',max(sz.koudl))) as koudl_sz\n" + 
							"from chepbtmp c ,meikkdlsz sz where\n" + 
							"c.daohrq>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
							"and c.daohrq<=to_date('"+jies+"','yyyy-mm-dd')\n" + 
							"and c.piz>0\n" + 
							"and c.meikdwmc=sz.meikmc(+)\n" + 
							"group by rollup (c.meikdwmc)\n" + 
							"order by c.meikdwmc)"


					);
					rs=con.getResultSetList(sbsql.toString());
					
					
					String ArrHeader[][]=new String[1][9];
		
					ArrHeader[0]=new String[] {"序号","供煤矿点","车数","毛重","皮重","扣吨","净重","累计<br>扣吨率(%)","系统设定<br>扣吨率(%)"};
					int ArrWidth[]=new int[] {50,160,60,80,80,80,80,80,80};
					  strFormat = new String[] { "","", 
			    				"", "0.0", "0.0", "0.0", "0.0","0.00","0.00"};
					// 数据
					rt.setTitle("汽车煤进煤报告_(供煤矿点汇总)",ArrWidth);
					rt.setDefaultTitle(1, 3, "来煤日期:"+this.getBRiq()+"至"+this.getERiq(), Table.ALIGN_LEFT);
					rt.setDefaultTitle(7, 2, "单位:吨", Table.ALIGN_RIGHT);
					rt.setBody(new Table(rs, 1, 0, 0));
					rt.body.setFontSize(12);
					rt.body.setColFormat(strFormat);
					rt.body.setWidth(ArrWidth);
					rt.body.setPageRows(500);
					rt.body.setHeaderData(ArrHeader);// 表头数据
					rt.createFooter(1, ArrWidth);
					//rt.setDefautlFooter(1, 5, "打印日期:"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
					rt.body.ShowZero = true;
		
					for (int i=2;i<=rt.body.getRows();i++){
						String xiaoj=rt.body.getCellValue(i, 2);
						if((xiaoj.equals("总计"))){
							for (int j=0;j<rt.body.getCols()+1;j++){
								rt.body.getCell(i, j).backColor="silver";
								rt.body.getCell(i, j).fontBold=true;
							}
						}
						//对比实际的扣吨率>设置的扣吨率,颜色变红
						if(i<=rt.body.getRows()-1){
							double koudl_sj=Double.parseDouble(rt.body.getCellValue(i, 8));
							String  koudl_shiji=rt.body.getCellValue(i, 9);
							if(koudl_shiji.equals("")){
								koudl_shiji="100";//如果扣吨率是空,设置成100，这样实际扣吨率就不会比设置扣吨率大,颜色就不会变红了
							}
							double koudl_sz=Double.parseDouble(koudl_shiji);
							if(koudl_sj>koudl_sz){
								for (int j=8;j<rt.body.getCols()+1;j++){
									rt.body.getCell(i, j).backColor="red";
									
								}
							}
						}
						
						
					}
		
		
		
		
			con.Close();
			for (int i=1;i<=rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
			
			rt.createFooter(1, ArrWidth);
			rt.setDefautlFooter(2, 2, "批准:", Table.ALIGN_LEFT);
			rt.setDefautlFooter(5, 2, "审核:", Table.ALIGN_LEFT);
			rt.setDefautlFooter(7, 2, "制表:", Table.ALIGN_CENTER);
		
		
			if(rt.body.getPages()>0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(30);
			
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
			
			//getSelectData();
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
	


	
//	 结算类别(验收单,拒付单,开票单)
	public IDropDownBean getJieslbValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getJieslbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setJieslbValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setJieslbModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getJieslbModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getJieslbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getJieslbModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "汇总"));
		list.add(new IDropDownBean(2, "明细"));
		
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(list));
		return;
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
		
		String sql_gongys = 
			"select rownum as id,a.jianjy from\n" +
			"(select distinct c.zhongcjjy  as jianjy  from chepbtmp c\n" + 
			"union\n" + 
			"select distinct c.qingcjjy   as jianjy  from chepbtmp c) a";

		
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql_gongys,"全部"));
		return;
	}
	
	
	
}