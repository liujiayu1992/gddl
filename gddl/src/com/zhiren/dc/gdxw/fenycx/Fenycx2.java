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
import com.zhiren.common.WriteLog;
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

public class Fenycx2 extends BasePage {

	/**
	 * 作者:王总兵
	 * 时间:2009-10-21 20:48:35
	 * 内容:宣威分样查询2
	 */
	
//	界面用户提示
	private String msg="";
	private String fenysj="";
	private String fenyry="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
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
	
//	刷新衡单列表
	public void getSelectData() {
		
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("来煤日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		//dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("briq");
		dfb.setListeners("change:function(own,newValue,oldValue){document.getElementById('BRiq').value = newValue.dateFormat('Y-m-d'); " +
		"document.getElementById('Mark_bh').value = 'true'; document.forms[0].submit();}");

		tb1.addField(dfb);
		
		
		
		
		//供应商下拉框
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("采样号:"));
		ComboBox CB_GONGYS = new ComboBox();
		CB_GONGYS.setTransform("GONGYS");
		CB_GONGYS.setWidth(100);
		CB_GONGYS.setListeners("select:function(){document.getElementById('Mark_bh').value = 'false'; document.forms[0].submit();}");
		CB_GONGYS.setEditable(true);
		tb1.addField(CB_GONGYS);
		tb1.addText(new ToolbarText("-"));
	

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		
		
		

		
		
		//ToolbarButton rbtn = new ToolbarButton(null,"查询","function(){document.getElementById('RefurbishButton').click();}");
		//rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		//tb1.addItem(rbtn);
		

		
		
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
	
		ResultSetList rs=null;
		String[] strFormat=null;
		sbsql.setLength(0);
		Report rt = new Report(); //报表定义
		String kais=this.getBRiq();
		if(kais.equals("")||kais=="null"){
			kais=DateUtil.FormatDate(new Date());
		}
		
	
		String tiaoj="";
		long meik=this.getGongysValue().getId();
		if(meik==-1){
			tiaoj="";
		}else{
			tiaoj=" and cp.id="+this.getGongysValue().getId()+"";
		}
		

		

			sbsql.append("select rownum as xuh,a.piaojh,a.mingc,a.fenyzt,to_char(a.lursj,'yyyy-mm-dd hh24:mi:ss') as caiysj from (\n"+
					"select cp.piaojh,cp.fenyzt,decode(wz.mingc,null,decode(cp.isjus,1,'拒收','正在卸煤'),wz.mingc) as mingc\n" +
					",cp.lursj\n"+
					"from chepbtmp cp,caiyb cy,cunywzb wz\n" + 
					"where cp.zhilb_id=cy.zhilb_id(+)\n" + 
					"and cy.cunywzb_id=wz.id(+)\n" + 
					"and cp.lursj>=to_date('"+kais+"','yyyy-mm-dd')\n"+
					"and cp.lursj<to_date('"+kais+"','yyyy-mm-dd')+1\n"+
					"and (cp.piz>0 or cp.isjus=1)\n"+
					""+tiaoj+"\n"+
					"order by wz.xuh,cp.piaojh) a");
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[1][5];
//				1120
				ArrHeader[0]=new String[] {"序号","采样号","桶号","状态","采样时间"};
				int ArrWidth[]=new int[] {50,170,150,30,120};
				int ArrWidth2[]=new int[] {50,400,400,30,80};
				  strFormat = new String[] {"", "", "",""};
				// 数据
				rt.setTitle(visit.getDiancmc()+"分样查询",ArrWidth);
			
				
				rt.setBody(new Table(rs, 1, 0, 0));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(1000);
				if(meik!=-1){
					
					rt.body.setRowHeight(1,35);
					rt.body.setRowHeight(2,150);
					rt.body.getCell(2, 1).fontSize=12;
					rt.body.getCell(2, 2).fontSize=80;
					rt.body.getCell(2, 3).fontSize=80;
					rt.body.bgColor="red";
					Fenyztcx(con,getGongysValue().getId(),visit.getRenymc());
				}else{
					int rows=rt.body.getRows();
					int cols=rt.body.getCols();
					//System.out.println("row="+rows+"  closl="+cols);
					for (int i=0;i<=rows;i++){
						if(rt.body.getCellValue(i, 4).equals("1")){
							
								rt.body.getCell(i, 1).backColor="red";
								rt.body.getCell(i, 2).backColor="red";
								rt.body.getCell(i, 3).backColor="red";
								rt.body.getCell(i, 4).backColor="red";
								rt.body.getCell(i, 5).backColor="red";
							
							
						}
					}
					
					
					
				}
				
				
				if(meik==-1){
					rt.body.setWidth(ArrWidth);
				}else{
					rt.body.setWidth(ArrWidth2);
				}
				
				rt.body.setHeaderData(ArrHeader);// 表头数据
				rt.body.ShowZero = true;
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				rt.createFooter(1, ArrWidth);
				rt.setDefautlFooter(1, 2, "打印日期："+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				
				
				
				con.Close();
				return rt.getAllPagesHtml();// ph;
			
	
		
		
		
	}
	
	public void Fenyztcx(JDBCcon con,long chepbtmp_ID,String renymc){
		int flag=0;
		String Shiffg="select * from chepbtmp where fenyzt=1 and id="+chepbtmp_ID;
		ResultSetList isfeng=con.getResultSetList(Shiffg);
		if(!isfeng.next()){//如果这个编号没有分过,就更新,分过了,就不再更新.

			String Fenyzt="update chepbtmp set fenyzt=1,fenysj=sysdate,fenyry='"+renymc+"' where id="+chepbtmp_ID;
			
			flag = con.getUpdate(Fenyzt);
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + 
				"\n更新suocztb信息失败!");
				setMsg(this.getClass().getName() + ":更新chepbtmp信息失败!");
				
			}
		}
		
		
	}
	

	
	public boolean IsFeny(long chepbtmp_ID){
		JDBCcon con = new JDBCcon();
		boolean flag=false;
		String Fenyzt="select fenyry,to_char(fenysj,'yyyy-mm-dd hh24:mi:ss') as fenysj from  chepbtmp  where  fenyzt=1 and id="+chepbtmp_ID;
		//判断是否已经分样,对于已经分样的进行弹出框提示
		ResultSetList rs=con.getResultSetList(Fenyzt);
		if(rs.next()){
			fenysj=rs.getString("fenysj");
			fenyry=rs.getString("fenyry");
			flag=true;
		}
		rs.close();
		con.Close();
		return flag;
	}
	
	
	
	
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
			this.setBRiq( DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
			
			
			
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			

			
		}
		if (getMarkbh().equals("true")) { // 判断如果getMarkbh()返回"true"，那么重新初始化编号下拉框
			getGongysModels();
		}
		
//		判断是否已经分过样了
		if(IsFeny(getGongysValue().getId())){
				this.setMsg("警告:  采样号:   "+getGongysValue().getValue()+"  在"+fenysj+"  已经被 "+fenyry+" 分过!");
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
		//this.getSelectData();
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
	

	
	
	

	
	
	
	
//	 编号下拉框
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
			"select cp.id,cp.piaojh\n" +
			"from chepbtmp cp,caiyb cy,cunywzb wz\n" + 
			"where cp.zhilb_id=cy.zhilb_id(+)\n" + 
			"and cy.cunywzb_id=wz.id(+)\n" + 
			"and cp.lursj>=to_date('"+this.getBRiq()+"','yyyy-mm-dd')\n" + 
			"and cp.lursj<to_date('"+this.getBRiq()+"','yyyy-mm-dd')+1\n" + 
			"and (cp.piz>0 or cp.isjus=1)\n"+
			"order by wz.mingc,cp.piaojh";

		
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql_gongys,"全部"));
		return;
	}
	
}
