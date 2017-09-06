package com.zhiren.dc.huaygl.huayzfhf;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public abstract class Huayzfhf extends BasePage implements PageValidateListener{
	//记录的改变
	private String change;
	public String getChange() {
		return change;
	}
	public void setChange(String change) {
		this.change = change;
	}
	
	//	刷新	
	private boolean _RefurbishClick = false;
    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishClick = true;
    }
    private void Refurbish() {
        //为"刷新"按钮添加处理程序
    }
    
    //废除	
	private boolean _CancelClick = false;
    public void CancelButton(IRequestCycle cycle) {
    	_CancelClick = true;
    }
    private void Cancel(){
    	//为"废除"按钮添加处理程序
    	//将选中的数据行的状态改为1，并将当前操作人添加到注销人字段中
    	Visit visit = (Visit) this.getPage().getVisit();
    	ResultSetList cancelrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		if(cancelrsl.next()){
			JDBCcon con = new JDBCcon();
			StringBuffer cancelsql = new StringBuffer("begin \n");
			do{
				if(cancelrsl.getString("ZHUANGT")!="已作废"){
					cancelsql.append("update kuangfzlb set zhilzt=1 , zhuxry='"+visit.getRenymc()+"'").append(" where id =").append(cancelrsl.getString("ID")).append(";\n");;
				}
			}while (cancelrsl.next());
			cancelrsl.close();
			cancelsql.append("\n end;");
			con.getUpdate(cancelsql.toString());
			con.Close();
		}
    }
	
    //恢复
    private boolean _ResumeClick = false;
    public void ResumeButton(IRequestCycle cycle) {
    	_ResumeClick = true;
    }
    private void Resume(){
    	//为"恢复"按钮添加处理程序
    	//将选中的数据行的状态改为0，并将其注销人字段中的名称去掉
    	Visit visit = (Visit) this.getPage().getVisit();
    	ResultSetList resumersl = visit.getExtGrid1().getModifyResultSet(getChange());
		if(resumersl.next()){
			JDBCcon con = new JDBCcon();
			StringBuffer resumesql = new StringBuffer("begin \n");
			do{
				if(resumersl.getString("ZHUANGT")!="正常"){
					resumesql.append("update kuangfzlb set zhilzt=0 , zhuxry=''").append(" where id =").append(resumersl.getString("ID")).append(";\n");;
				}
			}while (resumersl.next());
			resumersl.close();
			resumesql.append("\n end;");
			con.getUpdate(resumesql.toString());
			con.Close();
		}
    	
    }
   
    
    //Grid
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridHtml(){
		return getExtGrid().getHtml();
	}
	
	public String getGridScript(){
		return getExtGrid().getGridScript();
	}
	
	//Dropdown
	public IDropDownBean getHuayjgValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getHuayjgModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setHuayjgValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getHuayjgModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getHuayjgModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setHuayjgModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getHuayjgModels() {
		String sql ="select h.id,h.mingc from item h,itemsort items where h.itemsortid=items.id and items.bianm='DISFHYJG'";
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql,"全部机构"));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
	
	//绑定日期
	public String getRiq1() {
		if (((Visit) this.getPage().getVisit()).getString1() == null || ((Visit) this.getPage().getVisit()).getString1().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString1(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRiq1(String riq1) {
		if (((Visit) this.getPage().getVisit()).getString1() != null && !((Visit) this.getPage().getVisit()).getString1().equals(riq1)) {
			((Visit) this.getPage().getVisit()).setString1(riq1);
		}
	}
	
	public String getRiq2() {
		if (((Visit) this.getPage().getVisit()).getString2() == null || ((Visit) this.getPage().getVisit()).getString2().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString2(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString2();
	}

	public void setRiq2(String riq2) {
		if (((Visit) this.getPage().getVisit()).getString2() != null && !((Visit) this.getPage().getVisit()).getString2().equals(riq2)) {
			((Visit) this.getPage().getVisit()).setString2(riq2);
		}
	}
	
	
	
	//	数据提交
	public void submit(IRequestCycle cycle) {
		if(_RefurbishClick){
			_RefurbishClick=false;
			Refurbish();
		}
		if(_CancelClick){
			_CancelClick=false;
			Cancel();
		}
		if(_ResumeClick){
			_ResumeClick=false;
			Resume();
		}
	}
	
	//	获取数据
	public void getSelectData(){
		StringBuffer condition=new StringBuffer("");
		if(getHuayjgValue().getId()==-1){
			condition.append(" ");
		}else{
			condition.append(" and k.item_id="+getHuayjgValue().getId());//机构条件搜索
		}
		condition.append(" and to_char(k.Lurrq,'YYYY-MM-DD')>= '"+getRiq1()+"' and to_char(k.Lurrq,'YYYY-MM-DD')<= '"+getRiq2()+"'");
		String sql="select k.id as ID ,decode(k.zhilzt,0,'正常','已废除') as Zhuangt,i.mingc as Mingc ,k.lurrq as Lurrq, k.huaybm as Huaybm,k.huayrq as huayrq,k.qnet_ar as Qnet,\n" +
			"k.aar as Aar,k.ad as Ad , k.vdaf as Vdaf , k.mt as Mt ,k.stad as Stad ,k.aad as Aad,\n" + 
			"k.mad as Mad, k.qbad as Qbad ,k.had as Had , k.vad as Vad ,k.fcad as Fcad ,k.std as Std,\n" + 
			"k.qgrad as Qgrad ,k.hdaf as Hdaf,k.qgrad_daf as Qgradaf ,k.sdaf as Sdaf , k.var as Var , k.star as Star , k.t1 as T1,k.t2 as T2,k.t3 as T3,k.t4 as T4,\n" + 
			"k.leib as Leib,k.lury as lury ,k.huayy as Huayy ,k.gongysb_id as gongys ,k.meikxxb_id as meikxx\n" + 
			"from kuangfzlb k , item i where k.item_id=i.id "+condition.toString();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		//设置多选框
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		
		egu.addPaging(25);
		egu.setTableName("Kuangfzl");
//		egu.getColumn(1).setHidden(true);
		egu.getColumn("ID").setHeader("ID");
		egu.getColumn("ID").setHidden(true);
		egu.getColumn("ID").setEditor(null);
		egu.getColumn("ID").setWidth(10);
		
		egu.getColumn("Zhuangt").setHeader("状态");
		egu.getColumn("Zhuangt").setEditor(null);
		
		egu.getColumn("Mingc").setHeader("机构名称");
		egu.getColumn("Mingc").setEditor(null);
		
		egu.getColumn("Lurrq").setHeader("录入日期");
		egu.getColumn("Lurrq").setEditor(null);
		
		egu.getColumn("Huaybm").setHeader("化验编码");
		egu.getColumn("Huaybm").setEditor(null);
		
		egu.getColumn("Huayrq").setHeader("化验日期");
		egu.getColumn("Huayrq").setEditor(null);
		
		egu.getColumn("Qnet").setHeader("收到基低位热量<p>Qnet,ar(Mj/kg)</p>");
		egu.getColumn("Qnet").setEditor(null);
		
		egu.getColumn("Aar").setHeader("收到基灰分<p>Aar(%)</p");
		egu.getColumn("Aar").setEditor(null);
		
		egu.getColumn("Ad").setHeader("干燥基灰分<p>Ad(%)</p>");
		egu.getColumn("Ad").setEditor(null);
		
		egu.getColumn("Vdaf").setHeader("干燥无灰基挥发分<p>Vdaf(%)</p>");
		egu.getColumn("Vdaf").setEditor(null);
		
		egu.getColumn("Mt").setHeader("全水分<p>Mt(%)</p>");
		egu.getColumn("Mt").setEditor(null);
		
		egu.getColumn("Stad").setHeader("空气干燥基全硫<p>St,ad(%)</p>");
		egu.getColumn("Stad").setEditor(null);
		
		egu.getColumn("Aad").setHeader("空气干燥基灰分<p>Aad(%)</p>");
		egu.getColumn("Aad").setEditor(null);
		
		egu.getColumn("Mad").setHeader("空气干燥基水分<p>Mad(%)</p>");
		egu.getColumn("Mad").setEditor(null);
		
		egu.getColumn("Qbad").setHeader("空气干燥基弹筒热值<p>Qb,ad(Mj/kg)</p>");
		egu.getColumn("Qbad").setEditor(null);
		
		egu.getColumn("Had").setHeader("空气干燥基氢<p>Had(%)</p>");
		egu.getColumn("Had").setEditor(null);
		
		egu.getColumn("Vad").setHeader("空气干燥基挥发分<p>Vad(%)</p>");
		egu.getColumn("Vad").setEditor(null);
		
		egu.getColumn("Fcad").setHeader("固定碳<p>FCad(%)</p>");
		egu.getColumn("Fcad").setEditor(null);
		
		egu.getColumn("Std").setHeader("干燥基全硫<p>St,d(%)</p>");
		egu.getColumn("Std").setEditor(null);
		
		egu.getColumn("Qgrad").setHeader("空气干燥基高位热值<p>Qgr,ad(Mj/kg)</p>");
		egu.getColumn("Qgrad").setEditor(null);
		
		egu.getColumn("Hdaf").setHeader("干燥无灰基氢<p>Hdaf(%)</p>");
		egu.getColumn("Hdaf").setEditor(null);
		
		egu.getColumn("Qgradaf").setHeader("干燥无灰基高位热值<p>Qgr,daf(Mj/kg)</p>");
		egu.getColumn("Qgradaf").setEditor(null);
		
		egu.getColumn("Sdaf").setHeader("干燥无灰基全硫<p>Sdaf(%)</p>");
		egu.getColumn("Sdaf").setEditor(null);
		
		egu.getColumn("Var").setHeader("收到基挥发分<p>Var(%)</p>");
		egu.getColumn("Var").setEditor(null);
		
		egu.getColumn("Star").setHeader("收到基全硫<p>St,ar(%)</p>");
		egu.getColumn("Star").setEditor(null);
		
		egu.getColumn("T1").setHeader("T1(℃)");
		egu.getColumn("T1").setEditor(null);
		
		egu.getColumn("T2").setHeader("T2(℃)");
		egu.getColumn("T2").setEditor(null);
		
		egu.getColumn("T3").setHeader("T3(℃)");
		egu.getColumn("T3").setEditor(null);
		
		egu.getColumn("T4").setHeader("T4(℃)");
		egu.getColumn("T4").setEditor(null);
		
		egu.getColumn("Leib").setHeader("类别");
		egu.getColumn("Leib").setEditor(null);
		
		egu.getColumn("lury").setHeader("录入员");
		egu.getColumn("lury").setHidden(true);
		egu.getColumn("lury").setEditor(null);
		
		
		egu.getColumn("Huayy").setHeader("化验员");
		egu.getColumn("Huayy").setEditor(null);
		
		egu.getColumn("gongys").setHeader("供应商");
		egu.getColumn("gongys").setHidden(true);
		egu.getColumn("gongys").setEditor(null);
		
		egu.getColumn("meikxx").setHeader("煤矿");
		egu.getColumn("meikxx").setHidden(true);
		egu.getColumn("meikxx").setEditor(null);
		

		
		ComboBox comb = new ComboBox();
		comb.setTransform("HuayjgDropDown");
		comb.setId("Huayjg");
		comb.setEditable(false);
		comb.setLazyRender(true);// 动态绑定
		comb.setWidth(80);
		comb.setReadOnly(true);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("Huayjg.on('select',function(){document.forms[0].submit();});");
		
		
		egu.addTbarText("录入日期：");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq1());
		df1.Binding("RIQ1", "forms[0]");// 与html页中的id绑定,并自动刷新
		df1.setId("Riq1");
		egu.addToolbarItem(df1.getScript());
		egu.addTbarText("至");
		DateField df2 = new DateField();
		df2.setReadOnly(true);
		df2.setValue(this.getRiq2());
		df2.Binding("RIQ2", "forms[0]");// 与html页中的id绑定,并自动刷新
		df2.setId("Riq2");
		egu.addToolbarItem(df2.getScript());
		
		StringBuffer cancel_Condition=new StringBuffer("");
		cancel_Condition.append(
								"var selCheck = gridDiv_sm.getSelections();\n" +
								"var isDo=0;\n" + 
								"for(i=0;i<selCheck.length;i++){\n" + 
								"  if(selCheck[i].get('ZHUANGT')=='正常'){\n" + 
								"      isDo=1;\n" + 
								"      break;\n" + 
								"  }\n" + 
								"}\n" + 
								"if(selCheck.length>0 && isDo==0){\n" + 
								"  Ext.MessageBox.alert('提示信息','选中信息都已经被废除');\n" + 
								"  return ;\n" + 
								"}\n"
								);
		
		
		
		StringBuffer resume_Condition=new StringBuffer("");
		resume_Condition.append(
							"var selCheck = gridDiv_sm.getSelections();\n" +
							"var isDo=0;\n" + 
							"for(i=0;i<selCheck.length;i++){\n" + 
							"  if(selCheck[i].get('ZHUANGT')=='已废除'){\n" + 
							"      isDo=1;\n" + 
							"      break;\n" + 
							"  }\n" + 
							"}\n" + 
							"if(selCheck.length>0 &&  isDo==0){\n" + 
							"  Ext.MessageBox.alert('提示信息','选中信息都为正常信息');\n" + 
							"  return ;\n" + 
							"}"
							);
		
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");

		GridButton cancelGb =new GridButton("作废",GridButton.ButtonType_SubmitSel_condition,egu.gridId,egu.getGridColumns(), "CancelButton",cancel_Condition.toString());
		GridButton resumeGb =new GridButton("恢复",GridButton.ButtonType_SubmitSel_condition,egu.gridId,egu.getGridColumns(), "ResumeButton",resume_Condition.toString());
		egu.addTbarBtn(cancelGb);
		egu.addTbarBtn(resumeGb);
		
		setExtGrid(egu);
		con.Close();

	}
//	
	//	入口
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {//判断当前加载页是不是第一次加载，默认为welcome
			// 在此添加，在页面第一次加载时需要置为空的变量或方法  
			visit.setActivePageName(getPageName().toString());
			//在visit中设置list值为空。
			visit.setList1(null);
			visit.setString1("");//日期1
			visit.setString2("");//日期2
			setHuayjgModel(null);//第三方化验机构
			setHuayjgValue(null);
		}
		//获取数据
		getSelectData();
	}
	//	验证
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
