package com.zhiren.dc.gdxw.fenycx;

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

public class Tonghcx extends BasePage {

	/**
	 * 作者:王总兵
	 * 时间:2009-10-26 14:46:33
	 * 内容:宣威根据桶号查询分样号
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
	
	private String riq; // 保存日期
	
	public String getRiq() {
		return riq;
	}
	
	public void setRiq(String riq) {
		this.riq = riq;
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

	private String Markbh = "true"; // 标记编号下拉框是否被选择
	
	public String getMarkbh() {
		return Markbh;
	}
	public void setMarkbh(String markbh) {
		Markbh = markbh;
	}
	private String Checkval="FALSE";
	
	
	public String getCheckval() {
		return Checkval;
	}

	public void setCheckval(String checkval) {
		Checkval = checkval;
	}
//	刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getRiq());
		dfb.setId("riq");
//		df.Binding("Riq", "forms[0]");
		dfb.setListeners("change:function(own,newValue,oldValue){document.getElementById('Riq').value = newValue.dateFormat('Y-m-d'); " +
			"document.getElementById('Mark_bh').value = 'true'; document.forms[0].submit();}");
		tb1.addField(dfb);
	
		tb1.addText(new ToolbarText("-"));
		
		
		//供应商下拉框
		tb1.addText(new ToolbarText("桶号:"));
		ComboBox CB_GONGYS = new ComboBox();
		CB_GONGYS.setTransform("GONGYS");
		CB_GONGYS.setWidth(100);
		//CB_GONGYS.setListeners("select:function(){document.Form0.submit();}");
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
		
		
		
		Checkbox cbselectlike=new Checkbox();
		cbselectlike.setId("SelectLike");
//		System.out.println(this.getCheckval());
		if(this.getCheckval().equalsIgnoreCase("TRUE")){
			cbselectlike.setChecked(true);
		}else{
			cbselectlike.setChecked(false);
		}
		cbselectlike.setListeners("change:function(field,newValue,oldValue){document.all.Checkval.value=newValue;} ");
		tb1.addField(cbselectlike);
		tb1.addText(new ToolbarText("汇总"));
		
		tb1.addText(new ToolbarText("-"));
		
		
		ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		

		
		
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	
	public String getPrintTable(){
		//判断汇总复选框是否选中
		if(this.getCheckval().equalsIgnoreCase("TRUE")){
			String tonghtj="";
			String biaoStr="";
			JDBCcon con = new JDBCcon();
			ResultSetList rs=null;
			String kais=this.getRiq();
			if(kais.equals("")||kais=="null"){
				kais=DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
			}
			for(int i=1;i<this.getGongysModel().getOptionCount();i++){
				
				tonghtj=((IDropDownBean)this.getGongysModel().getOption(i)).getValue();
				
				
				biaoStr+=this.getHuiz(con, rs, kais,  tonghtj);
			}
			
			return biaoStr;
			
		
		}else{
			return getTongh();
		}
			
		
	}
	
	private String getHuiz(JDBCcon con,ResultSetList rs,String kais,String tonghhz){
		
		StringBuffer sbsql = new StringBuffer();
		Report rt = new Report(); //报表定义
		
		String sqlth=
		

			"select c.mingc,count(*) as shul\n" +
			"from gdxw_cy cy ,cunywzb c,caiyb cai,chepbtmp cp\n" + 
			"where cy.zhuangt=1\n" + 
			" and cy.zhiyrq>=to_date('"+kais+"','yyyy-mm--dd')\n" + 
			" and cy.zhiyrq<to_date('"+kais+"','yyyy-mm--dd')+1\n" + 
			" and cy.zhilb_id=cai.zhilb_id\n" + 
			" and cai.cunywzb_id=c.id\n" + 
			" and cp.zhilb_id=cy.zhilb_id\n" + 
			"  and c.mingc='"+tonghhz+"'\n" + 
			"  group by c.mingc";

		
		String tongh="";
		
		long shul=0;
		rs=con.getResultSetList(sqlth);
		
		if(rs.next()){
			tongh=rs.getString("mingc");
			
			shul=rs.getLong("shul");
		}
		
		
		

		String[][] CAIYtong=new String[][]{
			{"桶号:",tongh},

			{"个数:",shul+""}
		};
		

		
		String[][] ArrHeader = new String[2][2];
		int i=0;
		for(int j=0;j<CAIYtong.length;j++){
			ArrHeader[i++]=CAIYtong[j];
		}
		int[] ArrWidth = new int[] {120,140};
		
		Table bt=new Table(2,2);
		rt.setBody(bt);
		
	
		rt.setTitle(" ",ArrWidth);
	
	
		
		rt.body.getCell(1, 1).fontBold=true;
		rt.body.getCell(1, 1).fontSize=30;
		
		rt.body.getCell(1, 2).fontBold=true;
    	rt.body.getCell(1, 2).fontSize=60;
		
		rt.body.getCell(2, 1).fontBold=true;
		rt.body.getCell(2, 1).fontSize=30;
		
		rt.body.getCell(2, 2).fontBold=true;
		rt.body.getCell(2, 2).fontSize=60;
		
		
	
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(0);
		rt.body.setHeaderData(ArrHeader);// 表头数据
//		rt.title.fontSize=12;
//		rt.body.fontSize=12;
		rt.body.setRowHeight(120);
		

		
		
		
		
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
	
	
	public String getTongh(){
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer talbe=new StringBuffer();	//报表输出
		ResultSetList rs=null;
		String[] strFormat=null;
		sbsql.setLength(0);
		Report rt = new Report(); //报表定义
		String kais=this.getRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
		
		String tiaoj="";
		tiaoj=" and c.mingc='"+this.getGongysValue().getValue()+"'";
		
		
		
		
		

			sbsql.append(
					"select rownum as xuh,a.mingc,a.piaojh,a.qingcsj from (\n"+
					"select c.mingc,cp.piaojh ,to_char(cp.lursj,'yyyy-mm-dd hh24:mi:ss') as qingcsj from gdxw_cy cy ,cunywzb c,caiyb cai,chepbtmp cp\n" +
					"where cy.zhuangt=1\n" + 
					" and cy.zhiyrq>=to_date('"+kais+"','yyyy-mm--dd')\n" + 
					" and cy.zhiyrq<to_date('"+kais+"','yyyy-mm--dd')+1\n" + 
					" and cy.zhilb_id=cai.zhilb_id\n" + 
					" and cai.cunywzb_id=c.id\n" + 
					" and cp.zhilb_id=cy.zhilb_id\n" + 
					" "+tiaoj+"\n" +
					"  order by cp.qingcsj ) a   ");
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[1][4];
//				1120
				ArrHeader[0]=new String[] {"序号","桶号","采样号","采样时间"};
				int ArrWidth[]=new int[] {50,80,80,210};
				  strFormat = new String[] { "", "","","" };
				// 数据
				rt.setTitle(visit.getDiancmc()+"桶号查询",ArrWidth);
			
				
				rt.setBody(new Table(rs, 1, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(500);
				
				
				
				
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
				rt.body.fontSize=13;
				rt.body.setRowHeight(25);
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
			
			
			
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
			//this.getGongysModels();
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
			
			//当天满足制样的桶号列表
			" select c.id,c.mingc from gdxw_cy cy ,cunywzb c,caiyb cai\n" + 
			" where cy.zhuangt=1\n" + 
			"  and cy.zhiyrq>=to_date('"+this.getRiq()+"','yyyy-mm-dd')\n" + 
			"  and cy.zhiyrq<to_date('"+this.getRiq()+"','yyyy-mm-dd')+1\n" + 
			"  and cy.zhilb_id=cai.zhilb_id\n" + 
			"  and cai.cunywzb_id=c.id order by c.xuh";

		
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql_gongys,"请选择"));
		return;
	}
	
}
