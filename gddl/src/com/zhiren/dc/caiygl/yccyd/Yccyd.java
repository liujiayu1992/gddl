package com.zhiren.dc.caiygl.yccyd;

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

public class Yccyd extends BasePage {

	/**
	 * 作者:王总兵
	 * 时间:2010-3-30 18:31:37
	 * 内容:阳城采样单
	 */
	private static final String BAOBPZB_GUANJZ = "JILTZ_GJZ";// baobpzb中对应的关键字
//	界面用户提示
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
	private String Markbh = "true"; // 标记编号下拉框是否被选择
	
	public String getMarkbh() {
		return Markbh;
	}

	public void setMarkbh(String markbh) {
		Markbh = markbh;
	}
//	绑定日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
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
		dfb.setId("BRiq");
		dfb.setListeners("change:function(own,newValue,oldValue){document.getElementById('BRiq').value = newValue.dateFormat('Y-m-d'); " +
		"document.getElementById('Mark_bh').value = 'true'; document.forms[0].submit();}");
	    tb1.addField(dfb);
		
		
		
		//供应商下拉框
		tb1.addText(new ToolbarText("采样信息:"));
		ComboBox CB_GONGYS = new ComboBox();
		CB_GONGYS.setTransform("GONGYS");
		CB_GONGYS.setWidth(200);
		CB_GONGYS.setListeners("select:function(){document.getElementById('Mark_bh').value = 'false'; document.forms[0].submit();}");
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
		Report rt = new Report(); //报表定义
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		
		
		
		String  bianh=this.getGongysValue().getValue();
		String newBianh="";
		if(bianh.equals("-")){
			newBianh="";
		}else{
			newBianh=bianh.substring(0, bianh.indexOf(","));
		}
		
		
			
			
			
			String sqlth=

				"select mk.mingc,zm.bianm,to_char(f.daohrq,'yyyy-mm-dd') as daohrq from zhuanmb zm,zhuanmlb lb ,fahb f,zhillsb ls,meikxxb mk\n" +
				"where f.zhilb_id=ls.zhilb_id\n" + 
				"and f.meikxxb_id=mk.id\n" + 
				"and zm.zhillsb_id=ls.id\n" + 
				"and zm.zhuanmlb_id=lb.id\n" + 
				"and lb.jib=1\n" + 
				"and zm.bianm='"+newBianh+"'";

			
			String meik="";
			String caiybm="";
			String caiyrq="";
			
			rs=con.getResultSetList(sqlth);
			
			if(rs.next()){
				meik=rs.getString("mingc");
				caiybm=rs.getString("bianm");
				caiyrq=rs.getString("daohrq");
				
			}
			
			
			
			
			
			

			String[][] CAIY=new String[][]{
					
			   {"煤样编码卡(化验室)","煤样编码卡(化验室)","","煤样编码卡(化验室)","煤样编码卡(化验室)"},
			   {"采样日期:",caiyrq,"","采样日期:",caiyrq},
			   {"编码:",caiybm,"","编码:",caiybm},
			   {"","","","",""},	
					
				{"煤样编码卡(供应商分析样)","煤样编码卡(供应商分析样)","","煤样编码卡(供应商全水)","煤样编码卡(供应商全水)"},
				{"采样日期:",caiyrq,"","采样日期:",caiyrq},
				{"矿名:",meik,"","矿名:",meik},
				{"","","","",""},
				
				
				{"煤样编码卡(比重)","煤样编码卡(比重)","","煤样编码卡(原煤存样)","煤样编码卡(原煤存样)"},
				{"采样日期:",caiyrq,"","采样日期:",caiyrq},
				{"矿名:",meik,"","矿名:",meik}
			
			};
			

			
			String[][] ArrHeader = new String[11][5];
			int i=0;
			for(int j=0;j<CAIY.length;j++){
				ArrHeader[i++]=CAIY[j];
			}
			int[] ArrWidth = new int[] { 120,185,60,120,185};
			
			Table bt=new Table(11,5);
			rt.setBody(bt);
			
		
			rt.setTitle("制样单", ArrWidth);
		
			rt.body.merge(1, 1, 1, 2);
			rt.body.merge(5, 1, 5, 2);
			rt.body.merge(9, 1, 9, 2);
			
			rt.body.merge(1, 4, 1, 5);
			rt.body.merge(5, 4, 5, 5);
			rt.body.merge(9, 4, 9, 5);
			
			//
			rt.body.merge(4, 1, 4, 5);
			rt.body.merge(8, 1, 8, 5);
			
			rt.body.merge(1, 3, 3, 3);
			rt.body.merge(5, 3, 7, 3);
			rt.body.merge(9, 3, 11, 3);
			rt.body.setWidth(ArrWidth);
			
			rt.body.setPageRows(25);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.title.fontSize=14;
			rt.body.fontSize=14;
			rt.body.setRowHeight(30);
			for(int a=1;a<=rt.body.getRows();a++){
				if(a==4||a==8){
					rt.body.setRowHeight(a, 60);
				}else{
					rt.body.setRowHeight(a, 85);
				}
					
				
				
			}
			
			rt.body.setCellFont(1, 1,"",18, true);
			rt.body.setCellFont(1, 4,"",18, true);
			rt.body.setCellFont(5, 1, "",18,true);
			rt.body.setCellFont(5, 4,"",18, true);
			rt.body.setCellFont(9, 1,"",18, true);
			rt.body.setCellFont(9, 4, "",18,true);
			
			
			rt.body.setCellFont(2, 2, "",22,true);
			rt.body.setCellFont(2, 5, "",22,true);
			
			rt.body.setCellFont(3, 2, "",23,true);
			rt.body.setCellFont(3, 5, "",23,true);
			
			
			rt.body.setCellFont(6, 2, "",22,true);
			rt.body.setCellFont(6, 5, "",22,true);
			rt.body.setCellFont(7, 2, "",23,true);
			rt.body.setCellFont(7, 5, "",23,true);
			
			rt.body.setCellFont(10, 2, "",22,true);
			rt.body.setCellFont(10, 5, "",22,true);
			rt.body.setCellFont(11, 2, "",22,true);
			rt.body.setCellFont(11, 5, "",22,true);
			
			
			
			
		rt.createFooter(1, ArrWidth);


			rt.footer.fontSize=11;
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			rt.body.ShowZero = true;
			con.Close();
			return rt.getAllPagesHtml();
			
			
			
			
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
		String reportType = cycle.getRequestContext().getParameter("lx");
		if(reportType != null) {
			setBRiq(DateUtil.FormatDate(new Date()));
		
			visit.setString15(reportType);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
			
			
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);

			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
			
		}
		if (getMarkbh().equals("true")) { // 判断如果getMarkbh()返回"true"，那么重新初始化编号下拉框
			getGongysModels();
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
			getGongysModels();
			getSelectData();
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
		String kais=this.getBRiq();
		
		String sql_gongys = 

			"select rownum as xuh,caiybm||','||meik from (select distinct\n" +
			" (select mingc from meikxxb where id=s.meikxxb_id) meik,c.caiybm\n" + 
			"  from (select bianm as caiybm, zhillsb_id\n" + 
			"          from zhuanmb\n" + 
			"         where zhillsb_id in\n" + 
			"               (select zm.zhillsb_id as id\n" + 
			"                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n" + 
			"                 where zm.zhuanmlb_id = lb.id\n" + 
			"                   and lb.jib = 1\n" + 
			"                   and y.zhilblsb_id = zm.zhillsb_id\n" + 
			"                   and f.zhilb_id = z.zhilb_id\n" + 
			"                   and z.id = zm.zhillsb_id)\n" + 
			"           and zhuanmlb_id =\n" + 
			"               (select id from zhuanmlb where mingc = '采样编码')) c,\n" + 
			"       (select bianm as zhiybm, zhillsb_id,(select huaylb from zhillsb where id=zhillsb_id) as leib\n" + 
			"          from zhuanmb\n" + 
			"         where zhillsb_id in\n" + 
			"               (select zm.zhillsb_id as id\n" + 
			"                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n" + 
			"                 where zm.zhuanmlb_id = lb.id\n" + 
			"                   and lb.jib = 2\n" + 
			"                   and y.zhilblsb_id = zm.zhillsb_id\n" + 
			"                   and f.zhilb_id = z.zhilb_id\n" + 
			"                   and z.id = zm.zhillsb_id)\n" + 
			"           and zhuanmlb_id =\n" + 
			"               (select id from zhuanmlb where mingc = '制样编码')) z,\n" + 
			"       (select bianm as huaybm, zhillsb_id\n" + 
			"          from zhuanmb\n" + 
			"         where zhillsb_id in\n" + 
			"               (select zm.zhillsb_id as id\n" + 
			"                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n" + 
			"                 where zm.zhuanmlb_id = lb.id\n" + 
			"                   and lb.jib = 3\n" + 
			"                   and y.zhilblsb_id = zm.zhillsb_id\n" + 
			"                   and f.zhilb_id = z.zhilb_id\n" + 
			"                   and z.id = zm.zhillsb_id)\n" + 
			"           and zhuanmlb_id =\n" + 
			"               (select id from zhuanmlb where mingc = '化验编码')) h,\n" + 
			"       (select distinct f.id,f.pinzb_id,\n" + 
			"                        f.diancxxb_id,\n" + 
			"                        f.gongysb_id,\n" + 
			"                        f.meikxxb_id,\n" + 
			"                        z.id as zid,\n" + 
			"                         f.yunsfsb_id\n" + 
			"          from zhillsb z, fahb f, chepb c,pinzb p\n" + 
			"         where f.zhilb_id = z.zhilb_id\n" + 
			"            and f.pinzb_id=p.id\n" + 
			"           and c.fahb_id = f.id\n" + 
			"           and f.daohrq=to_date('"+kais+"', 'yyyy-mm-dd')) s\n" + 
			" where c.zhillsb_id = z.zhillsb_id\n" + 
			"   and h.zhillsb_id = c.zhillsb_id\n" + 
			"   and z.zhillsb_id = h.zhillsb_id\n" + 
			"   and c.zhillsb_id = s.zid\n" + 
			"   and h.zhillsb_id = s.zid\n" + 
			"   and z.zhillsb_id = s.zid\n" + 
			" and  s.diancxxb_id = 264 order by caiybm)";


		
		
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql_gongys,"-"));
		return;
	}
	
}
