package com.zhiren.dc.gdxw.zhiycx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Cell;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * 作者:tzf
 * 时间:2010-01-04
 * 修改内容:500吨强制制样标签打印(3张制样单) 支持所有桶号标签的全部打印功能。
 */
/*
 * 作者:tzf
 * 时间:2009-12-30
 * 内容:去掉表头，更改颜色字体
 */
/*
 * 作者:tzf
 * 时间:2009-12-26
 * 内容:500吨强制制样查询
 */
public class Zhiycx2 extends BasePage implements PageValidateListener {

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
	
	private String Checkval="FALSE";
	
	
	public String getCheckval() {
		return Checkval;
	}

	public void setCheckval(String checkval) {
		Checkval = checkval;
	}
	
	
	private String Checkval2="FALSE";
	
	
	public String getCheckval2() {
		return Checkval2;
	}

	public void setCheckval2(String checkval2) {
		Checkval2 = checkval2;
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
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("来煤日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		//dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("briq");
		dfb.setListeners("change:function(own,newValue,oldValue){document.getElementById('BRiq').value = newValue.dateFormat('Y-m-d'); " +
		"document.getElementById('Mark_bh').value = 'true';}");

		tb1.addField(dfb);
		
		
		tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		//dfe.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
		dfe.setId("eriq");
		dfe.setListeners("change:function(own,newValue,oldValue){document.getElementById('ERiq').value = newValue.dateFormat('Y-m-d'); " +
		"document.getElementById('Mark_bh').value = 'true'; document.forms[0].submit();}");
		
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
		
		//供应商下拉框
		tb1.addText(new ToolbarText("桶号:"));
		ComboBox CB_GONGYS = new ComboBox();
		CB_GONGYS.setTransform("GONGYS");
		CB_GONGYS.setWidth(170);
		//CB_GONGYS.setListeners("select:function(){document.Form0.submit();}");
		CB_GONGYS.setListeners("select:function(){document.getElementById('Mark_bh').value = 'false'; document.forms[0].submit();}");
		
		CB_GONGYS.setEditable(true);
		tb1.addField(CB_GONGYS);
		tb1.addText(new ToolbarText("-"));
		
		
		//格式下拉框
		tb1.addText(new ToolbarText("格式:"));
		ComboBox GES = new ComboBox();
		GES.setTransform("GES");
		GES.setWidth(100);
		GES.setListeners("select:function(){document.forms[0].submit();}");
		GES.setEditable(false);
		tb1.addField(GES);
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
		if(this.getCheckval().equalsIgnoreCase("TRUE")){
			cbselectlike.setChecked(true);
		}else{
			cbselectlike.setChecked(false);
		}
		cbselectlike.setListeners("change:function(field,newValue,oldValue){document.all.Checkval.value=newValue;} ");
		tb1.addField(cbselectlike);
		tb1.addText(new ToolbarText("打印全部"));
		
		tb1.addText(new ToolbarText("-"));
		
		Checkbox cbselectlike2=new Checkbox();
		cbselectlike2.setId("SelectLike2");
		if(this.getCheckval2().equalsIgnoreCase("TRUE")){
			cbselectlike2.setChecked(true);
		}else{
			cbselectlike2.setChecked(false);
		}
		cbselectlike2.setListeners("change:function(field,newValue,oldValue){document.all.Checkval2.value=newValue;} ");
		tb1.addField(cbselectlike2);
		tb1.addText(new ToolbarText("打印所选桶以后"));
		
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
		
	
			return getYansd();
		
	}
	//总单
	private String getZongd(JDBCcon con,ResultSetList rs,String kais,String jies){

		StringBuffer sbsql = new StringBuffer();
		Report rt = new Report(); //报表定义
		String[] strFormat=null;
		Visit visit = (Visit) this.getPage().getVisit();
		
		sbsql.append(
				"select rownum as xuh, a.* from (\n"+
				"select \n" +
				"       cy.mingc,\n" + 
				
				"       zm.bianm, to_char(zy.zhiyrq,'yyyy-mm-dd') as zhiyrq,ry.quanc\n" + 
				" from zhuanmb zm,zhillsb ls,zhilb zl,caiyb cai,\n" + 
				" cunywzb cy,gdxw_cy gd,zhiyryb zy,renyxxb ry\n" + 
				"where zm.zhillsb_id=ls.id\n" + 
				"and ls.zhilb_id=zl.id(+)\n" + 
				"and ls.zhilb_id=cai.zhilb_id\n" + 
				"and cai.cunywzb_id=cy.id\n" + 
				"and ls.zhilb_id=gd.zhilb_id\n" + 
				"and zm.zhuanmlb_id=100663\n" + 
				" and ls.id=zy.zhillsb_id and zy.renyxxb_id=ry.id\n"+
				"and ls.id in (\n" + 
				"    select ls.id from zhillsb ls where ls.zhilb_id in (\n" + 
				"         select c.zhilb_id from gdxw_cy c\n" + 
				"         where  c.zhuangt = 1\n" + 
				"        and c.zhiyrq >= to_date('"+kais+"', 'yyyy-mm-dd')\n" + 
				"        and c.zhiyrq < to_date('"+jies+"', 'yyyy-mm-dd') + 1\n" + 
				")\n" + 
				") order by cy.xuh,zm.bianm ) a");
			rs=con.getResultSetList(sbsql.toString());
			
			
			
		
			String ArrHeader[][]=new String[1][5];
//			1120
			ArrHeader[0]=new String[] {"序号","桶号","制样编号","制样日期","制样员"};
			int ArrWidth[]=new int[] {40,60,75,75,55};
			  strFormat = new String[] { "", "","","","" };
			// 数据
			rt.setTitle("桶号与制样号对应",ArrWidth);
		
			
			rt.setBody(new Table(rs, 1, 0, 0));
			rt.body.setColFormat(strFormat);
			rt.body.setPageRows(1000);
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.ShowZero = true;
			//  第2列第3列加粗
			for(int i=2;i<=rt.body.getRows();i++){
				rt.body.getCell(i, 2).fontBold=true;
				rt.body.getCell(i, 2).fontSize=13;
				rt.body.getCell(i, 3).fontBold=true;
				rt.body.getCell(i, 3).fontSize=13;
			}
			
			for (int i=1;i<=rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
			rt.createFooter(1, ArrWidth);
			//rt.setDefautlFooter(1, 3, "打印日期："+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
			if(rt.body.getPages()>0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(30);
			return rt.getAllPagesHtml();// ph;
			
		
	}
	
	//交样单
	private String getJiaoyd(JDBCcon con,ResultSetList rs,String kais,String jies){

		StringBuffer sbsql = new StringBuffer();
		Report rt = new Report(); //报表定义
		String[] strFormat=null;
		Visit visit = (Visit) this.getPage().getVisit();
		
		sbsql.append(
				"select rownum as xuh, a.* from (\n"+
				"select \n" +
				
				
				"       zm.bianm, to_char(zy.zhiyrq,'yyyy-mm-dd') as zhiyrq,ry.quanc\n" + 
				" from zhuanmb zm,zhillsb ls,zhilb zl,caiyb cai,\n" + 
				" cunywzb cy,gdxw_cy gd,zhiyryb zy,renyxxb ry\n" + 
				"where zm.zhillsb_id=ls.id\n" + 
				"and ls.zhilb_id=zl.id(+)\n" + 
				"and ls.zhilb_id=cai.zhilb_id\n" + 
				"and cai.cunywzb_id=cy.id\n" + 
				"and ls.zhilb_id=gd.zhilb_id\n" + 
				"and zm.zhuanmlb_id=100663\n" + 
				" and ls.id=zy.zhillsb_id and zy.renyxxb_id=ry.id\n"+
				"and ls.id in (\n" + 
				"    select ls.id from zhillsb ls where ls.zhilb_id in (\n" + 
				"         select c.zhilb_id from gdxw_cy c\n" + 
				"         where  c.zhuangt = 1\n" + 
				"        and c.zhiyrq >= to_date('"+kais+"', 'yyyy-mm-dd')\n" + 
				"        and c.zhiyrq < to_date('"+jies+"', 'yyyy-mm-dd') + 1\n" + 
				")\n" + 
				") order by zm.bianm ) a");
			rs=con.getResultSetList(sbsql.toString());
			
			
			
		
			String ArrHeader[][]=new String[1][4];
//			1120
			ArrHeader[0]=new String[] {"序号","制样编号","制样日期","制样员"};
			int ArrWidth[]=new int[] {40,80,75,55};
			  strFormat = new String[] { "", "","","" };
			// 数据
			rt.setTitle("化验交样单",ArrWidth);
		
			
			rt.setBody(new Table(rs, 1, 0, 0));
			rt.body.setColFormat(strFormat);
			rt.body.setPageRows(1000);
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.ShowZero = true;
			//  第2列第3列加粗
			for(int i=2;i<=rt.body.getRows();i++){
				rt.body.getCell(i, 2).fontBold=true;
				rt.body.getCell(i, 2).fontSize=13;
				
			}
			
			for (int i=1;i<=rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
			rt.createFooter(1, ArrWidth);
			//rt.setDefautlFooter(1, 3, "打印日期："+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
			if(rt.body.getPages()>0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(30);
			return rt.getAllPagesHtml();// ph;
			
		
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
		
		String jies=this.getERiq();
		if(jies.equals("")||jies=="null"){
			jies=DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		
		String tonghtj=this.getGongysValue().getValue();
		
		
		
		if(tonghtj.equals("全部")){
			
			String  ges_id=this.getGesValue().getStrId();
			String biaoStr="";
			
			if(ges_id.equals("1")){
				
				 biaoStr=this.getZongd(con, rs, kais, jies);
				 con.Close();
				 return biaoStr;
				
				
			}else{
				//化验交样单
				 biaoStr=this.getJiaoyd(con, rs, kais, jies);
				 con.Close();
				 return biaoStr;
			}
			
			
		}else{
			
			
			if(this.getCheckval().equalsIgnoreCase("TRUE")){
//				打印所有桶号
				String biaoStr="";
				for(int i=1;i<this.getGongysModel().getOptionCount();i++){
					
					tonghtj=((IDropDownBean)this.getGongysModel().getOption(i)).getValue();
					tonghtj=tonghtj.substring(tonghtj.indexOf(":")+1, tonghtj.length());
					
					for (int ia=0;ia<3;ia++){
						String xiangm="";
						if(ia==0){
							xiangm="全水份";
						}else if(ia==1){
							xiangm="煤样分析";
						}else if (ia==2){
							xiangm="备查";
						}
						biaoStr+=this.getJtbq(con, rs, kais, jies, tonghtj,xiangm);
					}
					
				}
				con.Close();
				
				return biaoStr;
				
			} else if(this.getCheckval2().equalsIgnoreCase("TRUE")){
				//打印所选桶号以后所有的桶号
				
				   int XuanZhong=(int)this.getGongysValue().getId();
				
				String biaoStr="";
				for(int i=XuanZhong;i<this.getGongysModel().getOptionCount();i++){
					
					tonghtj=((IDropDownBean)this.getGongysModel().getOption(i)).getValue();
					tonghtj=tonghtj.substring(tonghtj.indexOf(":")+1, tonghtj.length());
					
					for (int ia=0;ia<3;ia++){
						String xiangm="";
						if(ia==0){
							xiangm="全水份";
						}else if(ia==1){
							xiangm="煤样分析";
						}else if (ia==2){
							xiangm="备查";
						}
						biaoStr+=this.getJtbq(con, rs, kais, jies, tonghtj,xiangm);
					}
					
				}
				con.Close();
				return biaoStr;
				
				
				
			}else{
				
				//截取最后面的制样编码
				String biaoStr="";
				
				
				tonghtj=tonghtj.substring(tonghtj.indexOf(":")+1, tonghtj.length());
				String xiangm="";
				for (int ia=0;ia<3;ia++){
					if(ia==0){
						xiangm="全水份";
					}else if(ia==1){
						xiangm="煤样分析";
					}else if (ia==2){
						xiangm="备查";
					}
					//return this.getJtbq(con, rs, kais, jies, tonghtj,xiangm);
					biaoStr+=this.getJtbq(con, rs, kais, jies, tonghtj,xiangm);
				}
				con.Close();
				return biaoStr;
			}
			
			
		}
		
		
		
	}
	
	
	private String getJtbq(JDBCcon con,ResultSetList rs,String kais,String jies,String meik,String xiangm){
		
		StringBuffer sbsql = new StringBuffer();
		Report rt = new Report(); //报表定义
		
		String sqlth=
		
		

		"select\n" +
		"       cy.mingc,\n" + 
		"       gd.jingz,\n" + 
		"       zm.bianm,\n" + 
		"       ls.zhilb_id,\n" + 
		"       zm.zhillsb_id,\n" + 
		"       ren.quanc as renyxxb_id,\n" + 
		"      to_char(ry.zhiyrq,'yyyy-mm-dd') as zhiyrq,\n" + 
		"       ry.beiz\n" + 
		" from zhuanmb zm,zhillsb ls,zhilb zl,caiyb cai,\n" + 
		" cunywzb cy,gdxw_cy gd,zhiyryb ry,renyxxb ren\n" + 
		"where zm.zhillsb_id=ls.id\n" + 
		"and ls.zhilb_id=zl.id(+)\n" + 
		"and ls.zhilb_id=cai.zhilb_id\n" + 
		"and cai.cunywzb_id=cy.id\n" + 
		"and ls.zhilb_id=gd.zhilb_id\n" + 
		"and zm.zhuanmlb_id=100663\n" + 
		"and ls.id=ry.zhillsb_id(+)\n" + 
		"and zm.bianm='"+meik+"'\n"+
		"and ry.renyxxb_id=ren.id(+)\n" + 
		"and ls.id in (\n" + 
		"    select ls.id from zhillsb ls where ls.zhilb_id in (\n" + 
		"         select c.zhilb_id from gdxw_cy c\n" + 
		"         where  c.zhuangt = 1\n" + 
		"        and c.zhiyrq >= to_date('"+kais+"', 'yyyy-mm-dd')\n" + 
		"        and c.zhiyrq < to_date('"+jies+"', 'yyyy-mm-dd') + 1\n" + 
		")\n" + 
		") order by cy.mingc";

		
		String zhiyy="";
		String zhiyrq="";
		String zhiybh="";
		
		rs=con.getResultSetList(sqlth);
		
		if(rs.next()){
			zhiyy=rs.getString("renyxxb_id");
			zhiyrq=rs.getString("zhiyrq");
			zhiybh=rs.getString("bianm");
			
		}
		
		
		

		String[][] CAIY=new String[][]{
			{"国电宣威公司","国电宣威公司"},
			{"制样日期:",zhiyrq},
			{"制样编号:",zhiybh},
			{"化验项目:",xiangm},
			{"制样员:",zhiyy}
		
		};
		

		
		String[][] ArrHeader = new String[5][2];
		int i=0;
		for(int j=0;j<CAIY.length;j++){
			ArrHeader[i++]=CAIY[j];
		}
		int[] ArrWidth = new int[] {100,170};
		
		Table bt=new Table(5,2);
		rt.setBody(bt);
		
		
	
		rt.body.merge(1, 1, 1, 2);
	
		
		rt.body.getCell(1, 1).fontBold=true;
		rt.body.getCell(1, 1).fontSize=15;
		rt.body.getCell(3, 2).fontBold=true;
		rt.body.getCell(3, 2).fontSize=20;
		rt.body.getCell(4, 2).fontBold=true;
		rt.body.getCell(2, 2).fontBold=true;
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);// 表头数据
	   // rt.title.fontSize=12;
		rt.body.fontSize=12;
		rt.body.setRowHeight(30);
		rt.createFooter(2, ArrWidth);


		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		
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
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
			setERiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
			this.setCheckval("FALSE");
			this.setCheckval2("FALSE");
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
		String jies=this.getERiq();
		String sql_gongys = 
			"select rownum as xuh,bianm from (\n" +
			"select \n" +
			"       cy.mingc||'号桶--制样码:'||zm.bianm as bianm\n" + 
			" from zhuanmb zm,zhillsb ls,zhilb zl,caiyb cai,\n" + 
			" cunywzb cy,gdxw_cy gd\n" + 
			"where zm.zhillsb_id=ls.id\n" + 
			"and ls.zhilb_id=zl.id(+)\n" + 
			"and ls.zhilb_id=cai.zhilb_id\n" + 
			"and cai.cunywzb_id=cy.id\n" + 
			"and ls.zhilb_id=gd.zhilb_id\n" + 
			"and zm.zhuanmlb_id=100663\n" + 
			"and ls.id in (\n" + 
			"    select ls.id from zhillsb ls where ls.zhilb_id in (\n" + 
			"         select c.zhilb_id from gdxw_cy c\n" + 
			"         where  c.zhuangt = 1\n" + 
			"        and c.zhiyrq >= to_date('"+kais+"', 'yyyy-mm-dd')\n" + 
			"        and c.zhiyrq < to_date('"+jies+"', 'yyyy-mm-dd') + 1\n" + 
			")\n" + 
			") order by cy.xuh,zm.bianm)";


		
		
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql_gongys,"全部"));
		return;
	}
	
	
//	 格式下拉框
	public IDropDownBean getGesValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getGesModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setGesValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setGesModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getGesModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getGesModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getGesModels() {
		
		List list=new ArrayList();
		list.add(new IDropDownBean("1","总单"));
		list.add(new IDropDownBean("2","交样单"));
		
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(list));
		return;
	}
	


	
	
}
