package com.zhiren.dc.hesgl.yufkcd;

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
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;

public class Yufkcd extends BasePage implements PageValidateListener {
	
	private String msg = "";
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		msg = "";
	}

	// 绑定日期

	public String getRiq1() {
		if (((Visit) getPage().getVisit()).getString1() == null 
					|| ((Visit) getPage().getVisit()).getString1().equals("")) {
			((Visit) getPage().getVisit()).setString1(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setRiq1(String riq) {

		if (((Visit) getPage().getVisit()).getString1()!=null&&!((Visit) getPage().getVisit()).getString1().equals(riq)) {
			((Visit) getPage().getVisit()).setString1(riq);
			((Visit) getPage().getVisit()).setboolean2(true);
		}
	}
	

	public String getRiq2() {
		if (((Visit) getPage().getVisit()).getString2() == null 
					|| ((Visit) getPage().getVisit()).getString2().equals("")) {
			((Visit) getPage().getVisit()).setString2(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setRiq2(String riq2) {

		if (((Visit) getPage().getVisit()).getString2() != null 
					&& !((Visit) getPage().getVisit()).getString2().equals(riq2)) {
			((Visit) getPage().getVisit()).setString2(riq2);
			((Visit) getPage().getVisit()).setboolean2(true);
		}

	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
//	west 原则的结算表id
	public String getParameters() {
		return ((Visit) this.getPage().getVisit()).getString3();
	}

	public void setParameters(String value) {
		((Visit) this.getPage().getVisit()).setString3(value);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();		//
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}
	
	private void Save() {
		// TODO 自动生成方法存根
		Visit visit = (Visit) this.getPage().getVisit();
		this.Save1(getChange(), visit);
	}
	
	public void Save1(String strchange,Visit visit) {
		
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ExtGridUtil egu=getExtGrid_Yfk();
		String strJiesb_id="0";
		String strDiancxxb_id="0";
		String sql="";
		boolean Flag=false;
		try {
//		保存逻辑：
//				1、先将yufklsb中的jiesb_id的匹配的信息删除
//				2、向历史表(yufklsb)插入记录
//				3、最后更新yufkb的余额
			if(visit.isFencb()){
				
				strDiancxxb_id=String.valueOf(this.getFencbValue().getId());
			}else{
				
				strDiancxxb_id=String.valueOf(visit.getDiancxxb_id());
			}
			
			if(!this.getParameters().equals("")){
				
				strJiesb_id=this.getParameters().substring(0,getParameters().indexOf(","));
			}
			
//			先删除yufklsb表中信息
			sql=" delete from yufklsb where jiesb_id="+strJiesb_id;
			if(con.getDelete(sql)>=0){
				
				ResultSetList mdrsl = egu.getModifyResultSet(strchange);
				while(mdrsl.next()) {
					
					if(Double.parseDouble(mdrsl.getString("SHIYJE"))>0){
						
						sql="insert into yufklsb (id, yufkb_id, jine, jiesb_id, shiysj, beiz)	\n"
							+" values(getnewid("+strDiancxxb_id+"),"+mdrsl.getString("YFK_ID")+","
								+mdrsl.getString("SHIYJE")+","+strJiesb_id+",sysdate,'"+mdrsl.getString("BEIZ")+" "
								+DateUtil.getYear(new Date())+"年,结算编号为"+Jiesdcz.getJieslxAndJiesbh(strJiesb_id)+",使用编号为"+mdrsl.getString("BIANH")
								+"的预付款"+mdrsl.getString("SHIYJE")+"元')	\n";
						
						if(con.getInsert(sql)>=0){
							
							sql="update yufkb set yue="+CountYfkye(mdrsl.getString("YFK_ID"),con)+"		\n"
								+" where id="+mdrsl.getString("YFK_ID");
							
							if(con.getUpdate(sql)>=0){
								
								Flag=true;
							}else{
								
								Flag=false;
								break;
							}
						}else{
							
							Flag=false;
							break;
						}
					}
				}
				
				mdrsl.close();
			}
			
			if(Flag){
				
				con.commit();
				this.setMsg("保存成功!");
				this.setParameters("");
			}else{
				
				con.rollBack();
				this.setMsg("保存失败!");
			}
		
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}
	
	public String CountYfkye(String Yfkb_id,JDBCcon con){
		
		String yue="0";
		String sql="";
		try{
			
			sql="select nvl(jine-yiyje,0) as yue from yufkb,	\n"
				+ "	(select sum(nvl(jine,0)) as yiyje from yufklsb where yufkb_id="+Yfkb_id+")	\n"
				+ " where yufkb.id="+Yfkb_id;
			
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				yue=rs.getString("yue");
			}
			
			rs.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return yue;
	}

	public void getSelectData() {

		JDBCcon con = new JDBCcon();
		String sql_condition="";
		
		if(getGongysValue().getId()>-1){
			
			sql_condition=" and gongysb_id="+getGongysValue().getId()+"		\n";
		}
//		列出已结算未入账的结算单
		
		String sql= " (select j.id,j.gongysb_id,getHtmlAlert('"+MainGlobal.getHomeContext(this)+"','Jiesdcz','lx','dianc,'||j.bianm||'',j.bianm) as bianm,	\n"
				+ " jieslx,g.mingc as gongys,j.shoukdw,j.kaihyh,j.zhangh,	\n"
				+ " j.jiessl,j.shuik,j.buhsmk as jiak,j.hansmk as zongje	\n" 
				+ " from jiesb j,gongysb g									\n"
				+ " where jiesrq>=to_date('"+getRiq1()+"','yyyy-MM-dd')		\n" 
				+ " and jiesrq<=to_date('"+getRiq2()+"','yyyy-MM-dd')		\n"
				+ " and j.gongysb_id=g.id									\n" 
				+ sql_condition
				+ " and ruzrq is null)										\n"             
				+ " union													\n" 
				+ " (select j.id,j.gongysb_id,getHtmlAlert('"+MainGlobal.getHomeContext(this)+"','Jiesdcz','lx','dianc,'||j.bianm||'',j.bianm) as bianm,	\n" 
				+ " jieslx,g.mingc as gongys,j.shoukdw,j.kaihyh,j.zhangh,	\n"
				+ " j.jiessl,j.shuik,j.buhsyf as jiak,j.hansyf as zongje	\n"
				+ " from jiesyfb j,gongysb g								\n"
				+ " where jiesrq>=to_date('"+getRiq1()+"','yyyy-MM-dd')		\n" 
				+ " and jiesrq<=to_date('"+getRiq2()+"','yyyy-MM-dd')		\n"
				+ " and j.gongysb_id=g.id									\n"
				+ sql_condition
				+ " and ruzrq is null)";										
  
		ResultSetList rsl=con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("yufkb");
//		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyHeight");
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("gongysb_id").setHidden(true);
		egu.getColumn("bianm").setHeader("结算编号");		
		egu.getColumn("jieslx").setHidden(true);
		egu.getColumn("jieslx").setEditor(null);
		egu.getColumn("gongys").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("shoukdw").setHeader("收款单位");
		egu.getColumn("kaihyh").setHeader("开户银行");
		egu.getColumn("zhangh").setHeader("帐号");
		egu.getColumn("jiessl").setHeader("结算数量");
		egu.getColumn("shuik").setHeader("税款");
		egu.getColumn("jiak").setHeader("价款");
		egu.getColumn("zongje").setHeader("总金额");
		egu.getColumn("bianm").setWidth(150);
		egu.getColumn("jiessl").setWidth(80);
		egu.getColumn("shuik").setWidth(80);
		egu.getColumn("jiak").setWidth(80);
		egu.getColumn("zongje").setWidth(80);
		
//		设置文本框_End
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		egu.addPaging(25);
		
		if(((Visit) getPage().getVisit()).isFencb()){
			
			egu.addTbarText("-");
			egu.addTbarText("分厂别");
			ComboBox comb = new ComboBox();
			comb.setTransform("FencbDropDown");
			comb.setId("Fencb");
			comb.setEditable(false);
			comb.setLazyRender(true);// 动态绑定
			comb.setWidth(160);
			comb.setReadOnly(true);
			egu.addToolbarItem(comb.getScript());
			egu.addOtherScript("Fencb.on('select',function(){document.forms[0].submit();});");
		}
		
		egu.addTbarText("-");
		egu.addTbarText("结算日期:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq1");
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("-");
		egu.addTbarText("结算日期:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		
		// 供货单位
		egu.addTbarText("-");
		egu.addTbarText(Locale.gongysb_id_fahb);
		ComboBox comb4 = new ComboBox();
		comb4.setTransform("GongysDropDown");
		comb4.setId("Gongys");
		comb4.setEditable(false);
		comb4.setLazyRender(true);// 动态绑定
		comb4.setWidth(160);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());
//		egu.addOtherScript("Gongys.on('select',function(){document.forms[0].submit();});");

		// 结算单编号
		egu.addTbarText("-");
		egu.addTbarText("结算单编号");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("JiesdbhDropDown");
		comb1.setId("Jiesdbh");
		comb1.setEditable(false);
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(160);
		comb1.setReadOnly(true);
		egu.addToolbarItem(comb1.getScript());
		egu.addOtherScript("Jiesdbh.on('select',function(){document.forms[0].submit();});");

		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");
		
		egu.addOtherScript("gridDiv_grid.on('click',function(){										\n" 
						+ "		var rec = gridDiv_sm.getSelected(); 								\n"
						+ "		if(rec==null){														\n"
						+ " 		return;															\n"
						+ " 	}																	\n"	
						+ " 	document.getElementById('Parameters').value=rec.get('ID')+','+rec.get('GONGYSB_ID');	\n"
						+ "　	document.forms[0].submit();});");
		
		setExtGrid(egu);
		
		con.Close();
	}
	
	
	public void getYfkSelectData() {
//		预付款
		JDBCcon con = new JDBCcon();
//		页面回传的参数
		String condition=this.getParameters();
		String strJiesb_id="0";
		String strGongysb_id="0";
		if(!condition.equals("")){
			
			strJiesb_id=condition.substring(0,condition.indexOf(","));
			strGongysb_id=condition.substring(condition.indexOf(",")+1);
		}
		
		
//		列出已结算未入账的结算单
		
		String sql= " (select lsb.id,yfk.id as yfk_id,jsb.id as jiesb_id,yfk.bianh,yfk.jine,yfk.yue,					\n"
					+ " 		gys.mingc as gongys,skdw.mingc as shoukdw,yfk.kaihyh,yfk.zhangh,	\n"
					+ "			lsb.jine as shiyje,lsb.beiz											\n"
					+ " 		from yufkb yfk,yufklsb lsb,jiesb jsb,gongysb gys,shoukdw skdw		\n"
					+ " 		where yfk.id=lsb.yufkb_id and jsb.id=lsb.jiesb_id					\n"
					+ " 			and yfk.gongysb_id=gys.id and yfk.shoukdwb_id=skdw.id			\n"
					+ " 			and yfk.ruzrq is not null and jsb.id="+strJiesb_id+")			\n"
					+ " union	\n"
					+ " (select lsb.id,yfk.id as yfk_id,jsb.id as jiesb_id,yfk.bianh,yfk.jine,yfk.yue,					\n"
					+ " 		gys.mingc as gongys,skdw.mingc as shoukdw,yfk.kaihyh,yfk.zhangh,	\n"
					+ "			lsb.jine as shiyje,lsb.beiz											\n"
					+ " 		from yufkb yfk,yufklsb lsb,jiesyfb jsb,gongysb gys,shoukdw skdw		\n"
					+ " 		where yfk.id=lsb.yufkb_id and jsb.id=lsb.jiesb_id					\n"
					+ " 			and yfk.gongysb_id=gys.id and yfk.shoukdwb_id=skdw.id			\n"
					+ " 			and yfk.ruzrq is not null and jsb.id="+strJiesb_id+")			\n"
					+ " union	\n"
					+ " (select nvl(lsb.id,0) as id,yfk.id as yfk_id,nvl(lsb.jiesb_id,0) as jiesb_id,yfk.bianh,yfk.jine,yfk.yue,				\n"
					+ " 		gys.mingc as gongys,skdw.mingc as shoukdw,yfk.kaihyh,yfk.zhangh,	\n" 
					+ "			nvl(lsb.jine,0) as shiyje,lsb.beiz									\n"
					+ " 		from yufkb yfk,yufklsb lsb,gongysb gys,shoukdw skdw					\n"                     
					+ " 		where yfk.gongysb_id=gys.id and yfk.shoukdwb_id=skdw.id(+)			\n"
					+ " 			and yfk.id=lsb.yufkb_id(+) and yfk.ruzrq is not null			\n"
					+ " 			and yfk.yue>0 and yfk.gongysb_id="+strGongysb_id+")";											
  
		ResultSetList rsl=con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv_Yfk", rsl);
		egu.setTableName("yufkb");
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		
		egu.getColumn("yfk_id").setHidden(true);
		egu.getColumn("yfk_id").setEditor(null);
		
		egu.getColumn("jiesb_id").setHidden(true);
		egu.getColumn("jiesb_id").setEditor(null);
		
		egu.getColumn("bianh").setHeader("预付款编号");	
		egu.getColumn("bianh").setEditor(null);
		
		egu.getColumn("jine").setHeader("总金额");
		egu.getColumn("jine").setEditor(null);
		
		egu.getColumn("yue").setHeader("余额");
		egu.getColumn("yue").setEditor(null);
		
		egu.getColumn("gongys").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongys").setEditor(null);
		
		egu.getColumn("shoukdw").setHeader("收款单位");
		egu.getColumn("shoukdw").setEditor(null);
		
		egu.getColumn("kaihyh").setHeader("开户银行");
		egu.getColumn("kaihyh").setEditor(null);
		
		egu.getColumn("zhangh").setHeader("帐号");
		egu.getColumn("zhangh").setEditor(null);
		
		egu.getColumn("shiyje").setHeader("使用金额");
		
		egu.getColumn("beiz").setHeader(Locale.beiz_fahb);
		
//		设置文本框_End
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(4, new GridColumn(GridColumn.ColType_Check));
		egu.addPaging(0);
		
		StringBuffer sbCondition=new StringBuffer();
		sbCondition.append("	var rec=gridDiv_Yfk_grid.getSelectionModel().getSelections();	\n")
				   .append("	for(i=0;i<rec.length;i++){	\n")
				   .append("		if(rec[i].get('SHIYJE')<=0){	\n")
				   .append("			Ext.MessageBox.alert('提示信息','第<'+(i+1)+'>行预付款使用金额有误请核对!');	\n")
				   .append("			return;	\n")
				   .append("		}	\n")
				   .append("	}	\n");
				   
		egu.addToolbarButton("保存",GridButton.ButtonType_SubmitSel_condition,"SaveButton",sbCondition.toString());
		 
		setExtGrid_Yfk(egu);
		
		con.Close();
	}
	
	
//	厂别
	public boolean _Fencbchange = false;
	public IDropDownBean getFencbValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getFencbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setFencbValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean5()){
			
			((Visit) getPage().getVisit()).setboolean3(true);
			
		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getFencbModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

			getFencbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setFencbModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getFencbModels() {

			String sql ="select id,mingc from diancxxb d where d.fuid="+
			((Visit) getPage().getVisit()).getDiancxxb_id()+"order by mingc";


			((Visit) getPage().getVisit())
			.setProSelectionModel5(new IDropDownModel(sql, "请选择"));
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}
	
//供货单位
	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setGongysValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean1() != value) {
			
			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean1(value);
		}
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getGongysModels() {
		
		String sql ="";
		long Diancxxb_id=0;
		
		if(((Visit) getPage().getVisit()).isFencb()){
			
//			分厂别
			Diancxxb_id=getFencbValue().getId();
		}else{
			
//			不分厂别
			Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
			sql="(select gongysb.id,gongysb.mingc from jiesb,gongysb 			\n"
				+ " where jiesrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')	\n" 
				+ " 	and jiesrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd')	\n"
				+ "     and jiesb.gongysb_id=gongysb.id							\n"	
				+ "     and gongysb.leix=1 										\n"
				+ " 	and ruzrq is null)	\n"             
				+ " union	\n" 
				+ "(select gongysb.id,gongysb.mingc from jiesyfb,gongysb		\n"	 
				+ " where jiesrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')	\n" 
				+ " 	and jiesrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd')	\n"
				+ "     and jiesyfb.gongysb_id=gongysb_id						\n"
				+ "     and gongysb.leix=1 										\n"
				+ " 	and ruzrq is null)	\n"             
				+ " order by mingc  ";
		  
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, "全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
	
	
//	结算单编号
	public IDropDownBean getJiesdbhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getJiesdbhModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setJiesdbhValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean2()!= value) {

			((Visit) getPage().getVisit()).setDropDownBean2(value);
		}
	}

	public void setJiesdbhModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getJiesdbhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {

			getJiesdbhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getJiesdbhModels() {
		
		String sql_condition="";
		
		if(getGongysValue().getId()>-1){
			
			sql_condition=" and gongysb_id="+getGongysValue().getId();
		}
		
		String sql="(select id,bianm from jiesb 	\n"
					+ " where jiesrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')	\n" 
					+ " 	and jiesrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd')	\n"
					+ " 	and ruzrq is null "+sql_condition+")	\n"             
					+ " union	\n" 
				+ "(select id,bianm from jiesyfb	\n"	 
					+ " where jiesrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')	\n" 
					+ " 	and jiesrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd')	\n"
					+ " 	and ruzrq is null "+sql_condition+")	\n"             
				+ " order by bianm  ";
		  
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql,"全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
//	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	//预付款
	public ExtGridUtil getExtGrid_Yfk() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setExtGrid_Yfk(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridScript_Yfk() {
		return getExtGrid_Yfk().getGridScript();
	}

	public String getGridHtml_Yfk() {
		return getExtGrid_Yfk().getHtml();
	}

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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			
			((Visit) this.getPage().getVisit()).setString1("");	//riq1
			((Visit) this.getPage().getVisit()).setString2("");	//riq2
			((Visit) this.getPage().getVisit()).setString3("");	//jiesb_id,gongysb_id  ,Parameters()

			if(visit.isFencb()){
				
				setFencbValue(null);	//DropDownBean5	
				setFencbModel(null);	//ProSelectionModel5
				getFencbModels();
			}
			
			setGongysValue(null);	//DropDownBean1
			setGongysModel(null);	//ProSelectionModel1
			getGongysModels();
			
			setJiesdbhValue(null);	//DropDownBean2
			setJiesdbhModel(null);	//ProSelectionModel2
			getJiesdbhModels();
			
			((Visit) this.getPage().getVisit()).setboolean1(false);		//供应商
			((Visit) this.getPage().getVisit()).setboolean2(false);		//日期
			
			getSelectData();
		}
		
		if(((Visit) this.getPage().getVisit()).getboolean2()){
			
			((Visit) this.getPage().getVisit()).setboolean2(false);
			getGongysModels();
			getJiesdbhModels();
		}
		
		if(((Visit) this.getPage().getVisit()).getboolean1()){
			
//			供应商变动时要更改结算单编号
			((Visit) this.getPage().getVisit()).setboolean1(false);
			getJiesdbhModels();
		}
		
		getYfkSelectData();
	}

}