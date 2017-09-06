package com.zhiren.dc.gdxw.guohcx;
/*
 * 作者：songy
 * 时间：2011-03-23 
 * 描述：修改下拉菜单的排序，要求按照名称进行排序
 */
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

public class Changnbd_cx extends BasePage {

	/**
	 * 作者:王总兵
	 * 时间:2010-12-14 23:04:00
	 * 内容:宣威厂内煤场搬倒过衡查询
	 */
	
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
		dfb.setId("guohrqb");
		dfb.setListeners("change:function(own,newValue,oldValue){document.getElementById('BRIQ').value = newValue.dateFormat('Y-m-d'); " +
		" document.getElementById('Mark_bh').value = 'true'; document.forms[0].submit();}");
		tb1.addField(dfb);
		
		
		tb1.addText(new ToolbarText("时:"));
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(50);
		comb1.setTransform("HOUR");
		comb1.setId("HOUR");//和自动刷新绑定
		//comb1.setLazyRender(true);//动态绑定
		comb1.setListeners("select:function(){document.Form0.submit();}");
		comb1.setEditable(true);
		tb1.addField(comb1);

		tb1.addText(new ToolbarText("分:"));
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("MIN");
		comb2.setId("MIN");//和自动刷新绑定
		//comb2.setLazyRender(true);//动态绑定
		comb1.setListeners("select:function(){document.Form0.submit();}");
		comb2.setEditable(true);
		tb1.addField(comb2);
		
		
		
		
		
		tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		//dfe.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
		dfe.setListeners("change:function(own,newValue,oldValue){document.getElementById('ERIQ').value = newValue.dateFormat('Y-m-d'); " +
		" document.getElementById('Mark_bh').value = 'true'; document.forms[0].submit();}");
		dfe.setId("guohrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
		
		tb1.addText(new ToolbarText("时:"));
		ComboBox comb3 = new ComboBox();
		comb3.setWidth(50);
		comb3.setTransform("HOUR2");
		comb3.setId("HOUR2");//和自动刷新绑定
		//comb1.setLazyRender(true);//动态绑定
		comb3.setListeners("select:function(){document.Form0.submit();}");
		comb3.setEditable(true);
		tb1.addField(comb3);

		tb1.addText(new ToolbarText("分:"));
		ComboBox comb4 = new ComboBox();
		comb4.setWidth(50);
		comb4.setTransform("MIN2");
		comb4.setId("MIN2");//和自动刷新绑定
		//comb2.setLazyRender(true);//动态绑定
		comb4.setListeners("select:function(){document.Form0.submit();}");
		comb4.setEditable(true);
		tb1.addField(comb4);
		tb1.addText(new ToolbarText("-"));
		
		
		//供应商下拉框
		tb1.addText(new ToolbarText("地点:"));
		ComboBox CB_GONGYS = new ComboBox();
		CB_GONGYS.setTransform("GONGYS");
		CB_GONGYS.setWidth(100);
		CB_GONGYS.setListeners("select:function(){document.forms[0].submit();}");
		CB_GONGYS.setEditable(true);
		tb1.addField(CB_GONGYS);
		tb1.addText(new ToolbarText("-"));
		
		/*Checkbox chk=new Checkbox();
		chk.setId("CHECKED");
		if(this.getCheck().equals("true")){
			chk.setChecked(true);
			
		}else{
			chk.setChecked(false);
		}
		chk.setListeners("check:function(own,checked){if(checked){document.all.CHECKED.value='true';}else{document.all.CHECKED.value='false'}}");
		tb1.addField(chk);
		tb1.addText(new ToolbarText("空衡人员"));*/
		
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
		
		
			
//			车号下拉框
			tb1.addText(new ToolbarText("车号:"));
			ComboBox cheh = new ComboBox();
			cheh.setTransform("CHEH");
			cheh.setWidth(100);
			cheh.setListeners("select:function(){document.getElementById('Mark_bh').value = 'false';document.forms[0].submit();}");
			cheh.setEditable(true);
			tb1.addField(cheh);
			tb1.addText(new ToolbarText("-"));
			
		
		

		
		
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
		leix.setWidth(130);
		leix.setListeners("select:function(){document.forms[0].submit();}");
		//leix.setEditable(true);
		tb1.addField(leix);
		
		
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	
	public String getPrintTable(){
		
	
			
			
			if (getJieslbValue().getValue().equals("汇总(按卸煤地点)")){
				return getChangnbdHz();
			}else if(getJieslbValue().getValue().equals("汇总(按装煤地点)")){
				return getChangnbdHz_zhuangmdd();
			}else if(getJieslbValue().getValue().equals("明细(按煤场排序)")){
				return getChangnbd_mx();
			}else if(getJieslbValue().getValue().equals("明细(按回皮时间排序)")){
				return getChangnbd_HpSjmx();
			}else if(getJieslbValue().getValue().equals("人员倒短统计")){
				return getRenyddtj();
			}else if(getJieslbValue().getValue().equals("车号查询")){
				return getChehcx();
			}
			else{
				return "无此报表";
			}
		
	}
	
	
	public String getChangnbdHz(){


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
		
		
		String MEIC_tiaoj="";
		long meicb_id=this.getGongysValue().getId();
		if(meicb_id!=-1){
			MEIC_tiaoj=" and b.go_meicb_id="+meicb_id;
		}
		
		
		Report rt = new Report(); //报表定义
		
			sbsql.setLength(0);
					sbsql.append(

							"select decode(b.daohrq,null,'总计',to_char(b.daohrq,'yyyy-mm-dd')) as daohrq,\n" +
							"decode(mcc.mingc,null,decode(b.daohrq,null,null,'当日合计'),mcc.mingc) as  xiemmc,\n" + 
							"decode(mc.mingc,null,decode(mcc.mingc,null,null,'小计'),mc.mingc) as zhungmmc,sum(b.maoz)  as maoz,sum(b.piz) as piz,\n" + 
							"sum(maoz-piz) as jingz,count(b.id) as ches\n" + 
							"from changnbdb b,meicb mc,meicb mcc\n" + 
							"where b.come_meicb_id=mc.id\n" + 
							"and b.go_meicb_id=mcc.id\n" + 
							"and b.qingcsj>=to_date('"+kais+" "+this.getHourValue()+":"+this.getMinValue()+"','yyyy-mm-dd hh24:mi')\n" + 
							"and b.qingcsj<=to_date('"+jies+" "+this.getHour2Value()+":"+this.getMin2Value()+"','yyyy-mm-dd hh24:mi')\n" + 
							"and b.piz>0\n" + 
							""+MEIC_tiaoj+"\n"+
							"group by rollup (b.daohrq,mcc.mingc,mc.mingc)\n" + 
							"order by b.daohrq,mcc.mingc,mc.mingc\n" 

					);
					rs=con.getResultSetList(sbsql.toString());
					
					
					String ArrHeader[][]=new String[1][7];
		
					ArrHeader[0]=new String[] {"日期","卸煤地点","装煤地点","毛重","皮重","净重","车数"};
					int ArrWidth[]=new int[] {110,135,135,80,80,80,60};
					  strFormat = new String[] { "","", 
			    				"", "0.0", "0.0", "0.0", ""};
					// 数据
					rt.setTitle("厂内倒短汇总(按卸煤地点)",ArrWidth);
					rt.setDefaultTitle(1, 3, "搬倒日期:"+this.getBRiq()+"至"+this.getERiq(), Table.ALIGN_LEFT);
					rt.setDefaultTitle(5, 2, "单位:吨", Table.ALIGN_RIGHT);
					rt.setBody(new Table(rs, 1, 0, 2));
					rt.body.setFontSize(12);
					rt.body.setColFormat(strFormat);
					rt.body.setWidth(ArrWidth);
					rt.body.setPageRows(500);
					rt.body.setHeaderData(ArrHeader);// 表头数据
					rt.createFooter(1, ArrWidth);
					//rt.setDefautlFooter(1, 5, "打印日期:"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
					rt.body.ShowZero = true;
		
					for (int i=2;i<=rt.body.getRows();i++){
						String xiaoj=rt.body.getCellValue(i, 3);
						if(xiaoj.equals("小计")||xiaoj.equals("")){
							for (int j=0;j<rt.body.getCols()+1;j++){
								rt.body.getCell(i, j).backColor="silver";
								rt.body.getCell(i, j).fontBold=true;
							}
						}
					}
		
					rt.body.mergeFixedCols();
					rt.body.mergeFixedRow();
		
		
			con.Close();
			for (int i=1;i<=rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
			
			
		
		
			if(rt.body.getPages()>0) {
				setCurrentPage(1);
				setAllPages(rt.body.getPages());
			}
			rt.body.setRowHeight(30);
			
	     	return rt.getAllPagesHtml();// ph;
	
	
		
	}
	
	public String getChehcx(){

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
				
			tiaoj="and b.go_meicb_id="+meik;
						
					
		}
		
		String chehtj="";
		long cheh=this.getChehValue().getId();
		if(cheh==-1){
			chehtj="";
		}else{
			chehtj=" and b.cheph='"+this.getChehValue().getValue()+"'\n";
		}
		
		
		

			sbsql.append(


					"select decode(mcc.mingc,null,'总计:'||count(b.id)||'车',mcc.mingc) as xiem,\n" +
					"decode(mc.mingc,null,decode(mcc.mingc,null,'','合计:'||count(*)||'车'),mc.mingc) as zhuangm,\n" + 
					"decode(b.cheph,null,decode(mc.mingc,null,null,'小计:'||count(b.id)||'车'),b.cheph) as cheph,\n" + 
					"sum(b.maoz) as maoz,sum(b.piz) as piz,sum(maoz-piz) as jizng,\n" + 
					"to_char(b.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcsj,decode(b.zhongcsj,null,null,to_char(max(b.qingcsj),'yyyy-mm-dd hh24:mi:ss')) as qingcsj,\n" + 
					"decode(b.zhongcsj,null,null,max(b.zhongcjjy)) as zhonghjjy,\n" + 
					"decode(b.zhongcsj,null,null,max(b.qingcjjy)) as qingcjjy,\n" + 
					"decode(b.zhongcsj,null,null,max(ys.mingc)) as ysdw\n" + 
					"from changnbdb b,meicb mc,meicb mcc,yunsdwb ys\n" + 
					"where b.come_meicb_id=mc.id\n" + 
					"and b.go_meicb_id=mcc.id\n" + 
					"and b.qingcsj>=to_date('"+kais+" "+this.getHourValue()+":"+this.getMinValue()+"','yyyy-mm-dd hh24:mi')\n" + 
					"and b.qingcsj<=to_date('"+jies+" "+this.getHour2Value()+":"+this.getMin2Value()+"','yyyy-mm-dd hh24:mi')\n" + 
					"and b.piz>0\n" + 
					""+chehtj+"\n"+
					"and b.yunsdwb_id=ys.id\n" + 
					""+tiaoj+""+
					"group by rollup (mcc.mingc,mc.mingc,b.cheph,b.zhongcsj)\n" + 
					"having not grouping(mcc.mingc)+grouping(mc.mingc)+grouping(b.cheph)+grouping(b.zhongcsj)=1\n" + 
					"order by mcc.mingc,mc.mingc");
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[1][11];
//				1120
				ArrHeader[0]=new String[] {"卸煤煤场","装煤煤场","车号","毛重","皮重","净重","过重时间","过空时间","过重人员","过空人员","运输单位"};
				int ArrWidth[]=new int[] {100,100,70,55,50,55,140,140,60,60,100};
				  strFormat = new String[] { "", "","","0.00","0.00","0.00","","","","",""};
				// 数据
				rt.setTitle("厂内倒短_(车号查询)",ArrWidth);
			
				
				rt.setBody(new Table(rs, 1, 0,2));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(500);
				rt.setDefaultTitle(1, 4, "倒短日期:"+kais+"至"+jies, Table.ALIGN_LEFT);
				
				
//				设定小计行的背景色和字体
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 7);
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
				
				
				rt.body.mergeFixedCols();
				rt.body.mergeFixedRow();
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
	
	
	//人员倒短统计
	public String getRenyddtj(){

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
		
		
		
		

			sbsql.append(

					"select decode(a.reny,null,b.reny,a.reny) as reny,decode(a.ches,null,0,a.ches) as zhongc,\n" +
					"decode(b.ches,null,0,b.ches) as qingc,\n" + 
					"'"+this.getBRiq()+"' as daoh1,\n" + 
					"'"+this.getERiq()+"' as daoh2\n" + 
					" from\n" + 
					" (select c.zhongcjjy as reny from changnbdb c\n" + 
					"where c.daohrq>=to_date('"+this.getBRiq()+"','yyyy-mm-dd')\n" + 
					"and c.daohrq<=to_date('"+this.getERiq()+"','yyyy-mm-dd')\n" + 
					"union\n" + 
					"select c.qingcjjy as reny from changnbdb c\n" + 
					"where c.daohrq>=to_date('"+this.getBRiq()+"','yyyy-mm-dd')\n" + 
					"and c.daohrq<=to_date('"+this.getERiq()+"','yyyy-mm-dd') and c.qingcjjy is not null) reny,\n" + 
					"(select c.zhongcjjy as reny,count(*) as ches ,min(c.daohrq) as daoh from changnbdb c\n" + 
					"where c.daohrq>=to_date('"+this.getBRiq()+"','yyyy-mm-dd')\n" + 
					"and c.daohrq<=to_date('"+this.getERiq()+"','yyyy-mm-dd')\n" + 
					"group by  (c.zhongcjjy)) a,\n" + 
					"(select c.qingcjjy as reny,count(*) as ches,max(c.daohrq) as daoh  from changnbdb c\n" + 
					"where c.daohrq>=to_date('"+this.getBRiq()+"','yyyy-mm-dd')\n" + 
					"and c.daohrq<=to_date('"+this.getERiq()+"','yyyy-mm-dd')\n" + 
					"and c.qingcjjy is not null \n"+
					"group by  (c.qingcjjy)) b\n" + 
					"where reny.reny=a.reny(+)\n" + 
					"and reny.reny=b.reny(+)");
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[1][5];
//				1120
				ArrHeader[0]=new String[] {"人员名称","过重车数","回皮车数","开始日期","截止日期"};
				int ArrWidth[]=new int[] {150,150,150,150,150};
				  strFormat = new String[] { "", "","","",""};
				// 数据
				rt.setTitle("磅房倒短统计",ArrWidth);
			
				
				rt.setBody(new Table(rs, 1, 0,2));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(500);
				rt.body.setFontSize(12);
				rt.setDefaultTitle(1, 4, "倒短日期:"+kais+"至"+jies, Table.ALIGN_LEFT);
				
			/*	
//				设定小计行的背景色和字体
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 2);
					if((xiaoj.equals(""))){
						for (int j=0;j<rt.body.getCols()+1;j++){
							rt.body.getCell(i, j).backColor="silver";
							rt.body.getCell(i, j).fontBold=true;
						}
					}
				}*/
				
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// 表头数据
				rt.body.ShowZero = true;
				
				
				rt.body.mergeFixedCols();
				rt.body.mergeFixedRow();
				for (int i=1;i<=rt.body.getCols();i++){
					rt.body.setColAlign(i, Table.ALIGN_CENTER);
				}
				rt.createFooter(1, ArrWidth);
				rt.setDefautlFooter(1, 3, "打印日期："+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
				if(rt.body.getPages()>0) {
					setCurrentPage(1);
					setAllPages(rt.body.getPages());
				}
				rt.body.setRowHeight(30);
				con.Close();
				return rt.getAllPagesHtml();// ph;
			
	
		
		
		
	
	}
	
	
	
	public String getChangnbd_mx(){
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
				
			tiaoj="and b.go_meicb_id="+meik;
						
					
		}
		
		
		
		

			sbsql.append(


					"select decode(mcc.mingc,null,'总计:'||count(b.id)||'车',mcc.mingc) as xiem,\n" +
					"decode(mc.mingc,null,decode(mcc.mingc,null,'','合计:'||count(*)||'车'),mc.mingc) as zhuangm,\n" + 
					"decode(b.cheph,null,decode(mc.mingc,null,null,'小计:'||count(b.id)||'车'),b.cheph) as cheph,\n" + 
					"sum(b.maoz) as maoz,sum(b.piz) as piz,sum(maoz-piz) as jizng,\n" + 
					"to_char(b.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcsj,decode(b.zhongcsj,null,null,to_char(max(b.qingcsj),'yyyy-mm-dd hh24:mi:ss')) as qingcsj,\n" + 
					"decode(b.zhongcsj,null,null,max(b.zhongcjjy)) as zhonghjjy,\n" + 
					"decode(b.zhongcsj,null,null,max(b.qingcjjy)) as qingcjjy,\n" + 
					"decode(b.zhongcsj,null,null,max(ys.mingc)) as ysdw\n" + 
					"from changnbdb b,meicb mc,meicb mcc,yunsdwb ys\n" + 
					"where b.come_meicb_id=mc.id\n" + 
					"and b.go_meicb_id=mcc.id\n" + 
					"and b.qingcsj>=to_date('"+kais+" "+this.getHourValue()+":"+this.getMinValue()+"','yyyy-mm-dd hh24:mi')\n" + 
					"and b.qingcsj<=to_date('"+jies+" "+this.getHour2Value()+":"+this.getMin2Value()+"','yyyy-mm-dd hh24:mi')\n" + 
					"and b.piz>0\n" + 
					"and b.yunsdwb_id=ys.id\n" + 
					""+tiaoj+"\n"+
					"group by rollup (mcc.mingc,mc.mingc,b.cheph,b.zhongcsj)\n" + 
					"having not grouping(mcc.mingc)+grouping(mc.mingc)+grouping(b.cheph)+grouping(b.zhongcsj)=1\n" + 
					"order by mcc.mingc,mc.mingc");
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[1][11];
//				1120
				ArrHeader[0]=new String[] {"卸煤煤场","装煤煤场","车号","毛重","皮重","净重","过重时间","过空时间","过重人员","过空人员","运输单位"};
				int ArrWidth[]=new int[] {100,100,70,55,50,55,140,140,60,60,100};
				  strFormat = new String[] { "", "","","0.00","0.00","0.00","","","","",""};
				// 数据
				rt.setTitle("厂内倒短_(煤场明细)",ArrWidth);
			
				
				rt.setBody(new Table(rs, 1, 0,2));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(500);
				rt.setDefaultTitle(1, 4, "倒短日期:"+kais+"至"+jies, Table.ALIGN_LEFT);
				
				
//				设定小计行的背景色和字体
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 7);
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
				
				
				rt.body.mergeFixedCols();
				rt.body.mergeFixedRow();
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
	
	
	
    //厂内倒短汇总--按装煤地点查询
	public String getChangnbdHz_zhuangmdd(){


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
		
		
		String MEIC_tiaoj="";
		long meicb_id=this.getGongysValue().getId();
		if(meicb_id!=-1){
			MEIC_tiaoj=" and b.come_meicb_id="+meicb_id;
		}
		
		
		Report rt = new Report(); //报表定义
		
			sbsql.setLength(0);
					sbsql.append(

							"select decode(b.daohrq,null,'总计',to_char(b.daohrq,'yyyy-mm-dd')) as daohrq,\n" +
							"decode(mc.mingc,null,decode(b.daohrq,null,null,'当日合计'),mc.mingc) as  xiemmc,\n" + 
							"decode(mcc.mingc,null,decode(mc.mingc,null,null,'小计'),mcc.mingc) as zhungmmc,sum(b.maoz)  as maoz,sum(b.piz) as piz,\n" + 
							"sum(maoz-piz) as jingz,count(b.id) as ches\n" + 
							"from changnbdb b,meicb mc,meicb mcc\n" + 
							"where b.come_meicb_id=mc.id\n" + 
							"and b.go_meicb_id=mcc.id\n" + 
							"and b.qingcsj>=to_date('"+kais+" "+this.getHourValue()+":"+this.getMinValue()+"','yyyy-mm-dd hh24:mi')\n" + 
							"and b.qingcsj<=to_date('"+jies+" "+this.getHour2Value()+":"+this.getMin2Value()+"','yyyy-mm-dd hh24:mi')\n" + 
							"and b.piz>0\n" + 
							""+MEIC_tiaoj+"\n"+
							"group by rollup (b.daohrq,mc.mingc,mcc.mingc)\n" + 
							"order by b.daohrq,mc.mingc,mcc.mingc"


					);
					rs=con.getResultSetList(sbsql.toString());
					
					
					String ArrHeader[][]=new String[1][7];
		
					ArrHeader[0]=new String[] {"日期","装煤地点","卸煤地点","毛重","皮重","净重","车数"};
					int ArrWidth[]=new int[] {110,135,135,80,80,80,60};
					  strFormat = new String[] { "","", 
			    				"", "0.0", "0.0", "0.0", ""};
					// 数据
					rt.setTitle("厂内倒短汇总(按装煤地点)",ArrWidth);
					rt.setDefaultTitle(1, 3, "搬倒日期:"+this.getBRiq()+"至"+this.getERiq(), Table.ALIGN_LEFT);
					rt.setDefaultTitle(5, 2, "单位:吨", Table.ALIGN_RIGHT);
					rt.setBody(new Table(rs, 1, 0, 2));
					rt.body.setFontSize(12);
					rt.body.setColFormat(strFormat);
					rt.body.setWidth(ArrWidth);
					rt.body.setPageRows(500);
					rt.body.setHeaderData(ArrHeader);// 表头数据
					rt.createFooter(1, ArrWidth);
					//rt.setDefautlFooter(1, 5, "打印日期:"+DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
					rt.body.ShowZero = true;
		
					for (int i=2;i<=rt.body.getRows();i++){
						String xiaoj=rt.body.getCellValue(i, 3);
						if(xiaoj.equals("小计")||xiaoj.equals("")){
							for (int j=0;j<rt.body.getCols()+1;j++){
								rt.body.getCell(i, j).backColor="silver";
								rt.body.getCell(i, j).fontBold=true;
							}
						}
					}
		
					rt.body.mergeFixedCols();
					rt.body.mergeFixedRow();
		
		
			con.Close();
			for (int i=1;i<=rt.body.getCols();i++){
				rt.body.setColAlign(i, Table.ALIGN_CENTER);
			}
			
			
		
		
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
			
			
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean12(null);
			visit.setProSelectionModel12(null);
			visit.setDropDownBean7(null);
			visit.setProSelectionModel7(null);
			visit.setDropDownBean8(null);
			visit.setProSelectionModel8(null);

			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
			
		}
		if (getMarkbh().equals("true")) { // 判断如果getMarkbh()返回"true"，那么重新初始化编号下拉框
			getChehModels();
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
	


	
//	 类型
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
		list.add(new IDropDownBean(1, "汇总(按卸煤地点)"));
		list.add(new IDropDownBean(2, "汇总(按装煤地点)"));
		list.add(new IDropDownBean(3, "明细(按煤场排序)"));
		list.add(new IDropDownBean(4, "明细(按回皮时间排序)"));
		list.add(new IDropDownBean(5, "人员倒短统计"));
		list.add(new IDropDownBean(6, "车号查询"));
		
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(list));
		return;
	}
	
	

	
	
	
	
//	 卸煤地点下拉框
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
		
		String sql_gongys = "select id ,mingc from meicb order by mingc";

		
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql_gongys,"全部"));
		return;
	}
	

	
//	 车号下拉框
	public IDropDownBean getChehValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getChehModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setChehValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setChehModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getChehModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getChehModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getChehModels() {
		String kais=this.getBRiq();
		String jies=this.getERiq();
		
		String sql_gongys =
			"select rownum as xuh,cheph from (\n" +
			"select distinct c.cheph from changnbdb c\n" + 
			"where c.daohrq>=to_date('"+kais+"','yyyy-mm-dd')\n" + 
			"and c.daohrq<to_date('"+jies+"','yyyy-mm-dd')+1)  ";

		
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql_gongys,"全部"));
		return;
	}
	
	
	

	
	public String getChangnbd_HpSjmx(){
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
				
			tiaoj="and b.go_meicb_id="+meik;
						
					
		}
		
		
		
		

			sbsql.append(
					"select rownum as xuh,a.* from (\n" +
					"select decode(b.qingcsj,null,'合计',max(b.cheph)) as cheph,\n" + 
					"      sum(b.maoz),sum(b.piz),sum(b.maoz-b.piz) as jingz,\n" + 
					"      decode(b.qingcsj,null,null,max(b.meigy)) as meigy,\n" + 
					"       decode(b.qingcsj,null,null,max(mc.mingc)) as laim,\n" + 
					"       decode(b.qingcsj,null,null,max(mcc.mingc)) as xiem,\n" + 
					"       decode(b.qingcsj,null,null,to_char(max(b.zhongcsj),'yyyy-mm-dd hh24:mi:ss')) as zhongcsj,\n" + 
					"       to_char(b.qingcsj,'yyyy-mm-dd hh24:mi:ss') as qingcsj,\n" + 
					"       decode(b.qingcsj,null,null,max(ys.mingc)) as yunsdw,\n" + 
					"       decode(b.qingcsj,null,null,max(b.zhongcjjy)) as zhongcy,\n" + 
					"       decode(b.qingcsj,null,null,max(b.qingcjjy)) as kongcy\n" + 
					"from changnbdb b,meicb mc,meicb mcc,yunsdwb ys\n" + 
					"where b.come_meicb_id=mc.id\n" + 
					"and b.go_meicb_id=mcc.id\n" + 
					"and b.qingcsj>=to_date('"+kais+" "+this.getHourValue()+":"+this.getMinValue()+"','yyyy-mm-dd hh24:mi')\n" + 
					"and b.qingcsj<=to_date('"+jies+" "+this.getHour2Value()+":"+this.getMin2Value()+"','yyyy-mm-dd hh24:mi')\n" + 
					""+tiaoj+""+
					"and b.piz>0\n" + 
					"and b.yunsdwb_id=ys.id\n" + 
					"group by rollup (b.qingcsj)\n" + 
					"order by b.qingcsj) a");
			
			
			
				rs=con.getResultSetList(sbsql.toString());
				
				
				
			
				String ArrHeader[][]=new String[1][13];
//				1120
				ArrHeader[0]=new String[] {"序号","车号","毛重","皮重","净重","验煤员","装煤煤场","卸煤煤场","过重时间","过空时间","运输单位","过重人员","过空人员"};
				int ArrWidth[]=new int[] {45,70,50,50,50,50,80,80,135,135,65,60,60};
				  strFormat = new String[] { "", "","0.0","0.0","0.0","","","","","","","",""};
				// 数据
				rt.setTitle("厂内倒短_(煤场明细)",ArrWidth);
			
				
				rt.setBody(new Table(rs, 1, 0,2));
				rt.body.setColFormat(strFormat);
				rt.body.setPageRows(500);
				rt.setDefaultTitle(1, 4, "倒短日期:"+kais+"至"+jies, Table.ALIGN_LEFT);
		
				
				rt.body.setWidth(ArrWidth);
				rt.body.setHeaderData(ArrHeader);// 表头数据
				rt.body.ShowZero = true;
				
				
				
				
//				设定小计行的背景色和字体
				for (int i=2;i<=rt.body.getRows();i++){
					String xiaoj=rt.body.getCellValue(i, 2);
					if((xiaoj.equals("合计"))){
						rt.body.setCellValue(i, 1, "");
						for (int j=0;j<rt.body.getCols()+1;j++){
							rt.body.getCell(i, j).backColor="silver";
							rt.body.getCell(i, j).fontBold=true;
						}
					}
				}
		
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
	
	
	
//	设置小时下拉框
	public IDropDownBean getHourValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			for (int i = 0; i < getHourModel().getOptionCount(); i++) {
				Object obj = getHourModel().getOption(i);
				if (DateUtil.getHour(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setHourValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setHourValue(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getHourModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			getHourModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setHourModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void getHourModels() {
		List listHour = new ArrayList();
		for (int i = 0; i < 24; i++) {
			if (i < 10)
				listHour.add(new IDropDownBean(i, "0" + i));
			else
				listHour.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setHourModel(new IDropDownModel(listHour));
	}

	//	 设置分钟下拉框
	public IDropDownBean getMinValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean12() == null) {
			for (int i = 0; i < getMinModel().getOptionCount(); i++) {
				Object obj = getMinModel().getOption(i);
				if (DateUtil.getMinutes(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setMinValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean12();
	}

	public void setMinValue(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean12(Value);
	}

	public IPropertySelectionModel getMinModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel12() == null) {
			getMinModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel12();
	}

	public void setMinModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel12(_value);
	}

	public void getMinModels() {
		List listMin = new ArrayList();
		for (int i = 0; i < 60; i++) {
			if (i < 10)
				listMin.add(new IDropDownBean(i, "0" + i));
			else
				listMin.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setMinModel(new IDropDownModel(listMin));
	}
	
	
	
	

	
	
//	设置第2小时下拉框
	public IDropDownBean getHour2Value() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean7() == null) {
			for (int i = 0; i < getHour2Model().getOptionCount(); i++) {
				Object obj = getHour2Model().getOption(i);
				if (DateUtil.getHour(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setHour2Value((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean7();
	}

	public void setHour2Value(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean7(Value);
	}

	public IPropertySelectionModel getHour2Model() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel7() == null) {
			getHour2Models();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel7();
	}

	public void setHour2Model(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel7(_value);
	}

	public void getHour2Models() {
		List listHour2 = new ArrayList();
		for (int i = 0; i < 24; i++) {
			if (i < 10)
				listHour2.add(new IDropDownBean(i, "0" + i));
			else
				listHour2.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setHour2Model(new IDropDownModel(listHour2));
	}

	//	 设置第2个分钟下拉框
	public IDropDownBean getMin2Value() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean8() == null) {
			for (int i = 0; i < getMin2Model().getOptionCount(); i++) {
				Object obj = getMin2Model().getOption(i);
				if (DateUtil.getMinutes(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setMin2Value((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean8();
	}

	public void setMin2Value(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean8(Value);
	}

	public IPropertySelectionModel getMin2Model() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel8() == null) {
			getMin2Models();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel8();
	}

	public void setMin2Model(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel8(_value);
	}

	public void getMin2Models() {
		List listMin2 = new ArrayList();
		for (int i = 0; i < 60; i++) {
			if (i < 10)
				listMin2.add(new IDropDownBean(i, "0" + i));
			else
				listMin2.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setMin2Model(new IDropDownModel(listMin2));
	}
	
	
	
}
