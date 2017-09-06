package com.zhiren.jt.diaoygl.fenkshcrb_dtjt;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.TreeButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public  class Fenkshcrb_dtjt extends BasePage implements PageValidateListener {
	
	
	private boolean returnMsg=false;
	private boolean hasSaveMsg=false;
	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = MainGlobal.getExtMessageBox(_value,false);
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

  	private void Save() {
  		Visit visit = (Visit) this.getPage().getVisit();
 		visit.getExtGrid1().Save(getChange(), visit);
 	}
	

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}


	private boolean _Refreshclick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		returnMsg=false;
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_Refreshclick) {
			_Refreshclick = false;
			
			getSelectData();
		}
	}

	public void getSelectData() {
		
		if(!this.isZuorkc()){
			this.setMsg("昨日数据没有填写,请先填写昨日数据!");
			returnMsg=true;
		}else{
			this.setMsg("");
		}
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = null;
		String riqTiaoj=this.getRiqi();
		String chaxun="";
		if(riqTiaoj==null||riqTiaoj.equals("")){
			riqTiaoj=DateUtil.FormatDate(new Date());
		}
		String strdiancTreeID = "";
		String strDiancId = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strdiancTreeID="";
			strDiancId = "";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strdiancTreeID=" and dc.id= " +this.getTreeid();
			strDiancId = " and dc.id= " +this.getTreeid();
//			strdiancTreeID = " and  (dc.fuid = "+ getTreeid() + " or dc.shangjgsid= "+this.getTreeid()+")";
//			strDiancId = " and  (dc.fuid = "+ getTreeid() + " or dc.shangjgsid= "+this.getTreeid()+")";
		}else if (jib==3){//选电厂只刷新出该电厂
			strdiancTreeID=" and dc.id= " +this.getTreeid();
			strDiancId = " and dc.id= " +this.getTreeid();
		} 
//		if(getGongmLxValue().getId()==1){
//			strdiancTreeID = strdiancTreeID +" and vw.gongyskjb_id=1 ";
//		}else{
//			strdiancTreeID = strdiancTreeID +" and vw.gongyskjb_id=2 ";
//		}

//	合计行的ID默认为-1，保存的时候不保存合计行

		chaxun = 

			"select nvl(id,-1) as id,diancxxb_id,\n" +
			"       decode(grouping(gongysbid),1,'合计',gongysbid) as gongysb_id,\n" + 
			"       nvl(riq,to_date('"+this.getRiqi()+"','yyyy-mm-dd')) as riq,nvl(leix,'重点') as leix,\n" + 
			"       sum(ches) as ches,sum(duns) as duns,nvl(shangbzt,0) as shangbzt\n" + 
			"  from (\n" + 
			"        select nvl(fk.id,0) as id,dc.mingc as diancxxb_id,vw.gongysmc as gongysbid,to_date('"+this.getRiqi()+"', 'yyyy-mm-dd') as riq,\n" + 
			"               decode(1,1,'重点','重点') leix,\n" + 
			"               nvl(fk.ches,0) as ches,nvl(fk.duns,0) as duns,nvl(fk.shangbzt,0) shangbzt,dc.xuh\n" + 
			"        from(select nvl(fk.id,0) as id,fk.diancxxb_id,fk.gongysb_id,fk.riq,fk.leix,fk.ches,fk.duns,fk.shangbzt\n" + 
			"              from fenkshcrb fk\n" + 
			"              where fk.riq=to_date('"+this.getRiqi()+"', 'yyyy-mm-dd') and fk.leix='重点') fk,\n" + 
			"             ( select distinct rb.diancxxb_id,rb.gongysb_id,rb.ches,rb.duns\n" + 
			"                from fenkshcrb rb\n" + 
			"               where  rb.riq>=to_date('"+DateUtil.getYear(DateUtil.getDate(getRiqi()))+"-01-01','yyyy-mm-dd') and rb.riq<=to_date('"+this.getRiqi()+"','yyyy-mm-dd') and rb.leix='重点'\n" + 
			"               group by rb.diancxxb_id,rb.gongysb_id  ) jh,\n" + 
			"              (select kj.id as id,gy.quanc as gongysmc,gl.diancxxb_id as diancxxb_id\n" + 
			"               from diancgysmykjb kj,diancgysglb gl,gongysb gy\n" + 
			"               where kj.diancgysglb_id=gl.id and gl.gongysb_id=gy.id and gl.shiyzt=1 and kj.gongyskjb_id=1) vw,diancxxb dc\n" + 
			"        where fk.gongysb_id(+)=jh.gongysb_id and fk.diancxxb_id(+)=jh.diancxxb_id\n" +  strdiancTreeID+
			"              and vw.diancxxb_id=dc.id and fk.gongysb_id=vw.id and fk.diancxxb_id=vw.diancxxb_id\n" + 
			"        order by dc.xuh,vw.gongysmc\n" + 
			"        )\n" + 
			"  group by rollup((diancxxb_id,xuh),(gongysbid,riq,leix,id,shangbzt))\n" + 
			"  having not grouping(diancxxb_id)=1\n" + 
			"  order by xuh,grouping(gongysbid) desc,gongysbid";

			
			
			
			/*
			"select nvl(id,-1) as id,diancxxb_id,\n" +
			"       decode(grouping(gongysbid),1,'合计',gongysbid) as gongysb_id,\n" + 
			"       nvl(riq,to_date('"+this.getRiqi()+"','yyyy-mm-dd')) as riq,nvl(leix,'"+getGongmLxValue().getValue()+"') as leix,\n" + 
			"       sum(rijjhl) as rijjhl,sum(ches) as ches,sum(duns) as duns,nvl(shangbzt,0) as shangbzt\n" + 
			"  from (\n" + 
			"select nvl(fk.id,0) as id,dc.mingc as diancxxb_id,vw.gongysmy as gongysbid,to_date('"+this.getRiqi()+"', 'yyyy-mm-dd') as riq,\n" + 
			"       decode(1,1,'"+getGongmLxValue().getValue()+"','"+getGongmLxValue().getValue()+"') leix,\n" + 
			"       decode(nvl(jh.rijjhl,0),0,nvl(fk.rijjhl,0),nvl(jh.rijjhl,0)) as rijjhl,\n" + 
			"       nvl(fk.ches,0) as ches,nvl(fk.duns,0) as duns,nvl(fk.shangbzt,0) shangbzt,px.xuh\n" + 
			" from\n" + 
			"     (select nvl(fk.id,0) as id,fk.diancxxb_id,fk.gongysb_id,fk.riq,fk.leix,\n" + 
			"             nvl(fk.rijjhl,0) as rijjhl,fk.ches,fk.duns,fk.shangbzt\n" + 
			"     from fenkshcrb fk\n" + 
			"     where fk.riq=to_date('"+this.getRiqi()+"', 'yyyy-mm-dd') ) fk,\n" + 
			"\n" + 
			"    ( select distinct rb.diancxxb_id,rb.gongysb_id,max(rb.rijjhl) as rijjhl\n" + 
			"        from fenkshcrb rb\n" + 
			"       where  rb.riq>=to_date('"+DateUtil.getYear(DateUtil.getDate(getRiqi()))+"-01-01','yyyy-mm-dd') and rb.riq<=to_date('"+this.getRiqi()+"','yyyy-mm-dd') and rb.leix='"+getGongmLxValue().getValue()+"'\n" + 
			"       group by rb.diancxxb_id,rb.gongysb_id  ) jh,\n" + 
			"      diancxxb dc,dianckjpxb px, vwdcgysmykjgl vw\n" + 
			"\n" + 
			"   where fk.gongysb_id(+)=jh.gongysb_id and fk.diancxxb_id(+)=jh.diancxxb_id\n" + strdiancTreeID+
			" and vw.diancxxb_id=dc.id and jh.gongysb_id=vw.id and jh.diancxxb_id=vw.diancxxb_id\n" + 
			"\t   and dc.id=px.diancxxb_id and px.kouj='月报' order by px.xuh,vw.gongysmy\n" + 
			"  )\n" + 
			"  group by rollup((diancxxb_id,xuh),(gongysbid,riq,leix,id,shangbzt))\n" + 
			"  having not grouping(diancxxb_id)=1\n" + 
			"  order by xuh,grouping(gongysbid) desc,gongysbid";*/

		
		   rsl = con.getResultSetList(chaxun);
	 

			
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("fenkshcrb");//将上面的sql查询出来的数据存储到fenkshcrb这张表中。
			
		egu.getColumn("diancxxb_id").setHeader("电厂名称");
		egu.getColumn("diancxxb_id").setDefaultValue(this.getDiancmc());
		egu.getColumn("gongysb_id").setHeader("煤矿名称");
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("leix").setHeader("供煤类型");
		egu.getColumn("leix").setDefaultValue(getGongmLxValue().getValue());
		egu.getColumn("leix").setEditor(null);
//		egu.getColumn("leix").setHidden(true);
		
//		egu.getColumn("rijjhl").setHeader("日均计划量");
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("duns").setHeader("吨数");
		egu.getColumn("shangbzt").setHeader("公司上报状态");
		egu.getColumn("shangbzt").setDefaultValue("0");
		
		egu.getColumn("diancxxb_id").setEditor(null);
		
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("riq").setHidden(true);
		
		egu.getColumn("shangbzt").setEditor(null);
		egu.getColumn("shangbzt").setHidden(true);
		

		//设定列的初始宽度
		
		egu.getColumn("diancxxb_id").setWidth(110);
		egu.getColumn("gongysb_id").setWidth(350);
		egu.getColumn("riq").setWidth(80);
		egu.getColumn("leix").setWidth(80);
//		egu.getColumn("rijjhl").setWidth(70);
		egu.getColumn("ches").setWidth(80);
		egu.getColumn("duns").setWidth(60);
		egu.getColumn("shangbzt").setWidth(70);
		
		//各种属性还没有设置呢。
		
		//设定不可编辑列的颜色
		egu.getColumn("diancxxb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("riq").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("shangbzt").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(50);
//		egu.setWidth(1000);
		egu.setDefaultsortable(false);//设定页面不自动排序
		// *****************************************设置默认值****************************
		egu.getColumn("riq").setDefaultValue(this.getRiqi());
		
		
		
		//*************************下拉框*****************************************88
		//电厂下拉框
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from diancxxb order by mingc"));
	    egu.getColumn("diancxxb_id").setReturnId(true);
		
        //供应商信息表下拉框
		ComboBox gongysb= new ComboBox();
		egu.getColumn("gongysb_id").setEditor(gongysb);
		gongysb.setEditable(true);
	
		String GongysSql = "select kj.id as id,gy.quanc as gongysmc\n" + 
		"               from diancgysmykjb kj,diancgysglb gl,gongysb gy,diancxxb dc\n" + 
		"               where kj.diancgysglb_id=gl.id and gl.gongysb_id=gy.id and gl.shiyzt=1 and kj.gongyskjb_id=1 and gl.diancxxb_id=dc.id "+strDiancId+
		"				order by gy.quanc" ;
		
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,new IDropDownModel(GongysSql));
		egu.getColumn("gongysb_id").setReturnId(true);
		
		
		//供煤类型 
		List list = new ArrayList();
		list.add(new IDropDownBean(1,"重点"));
//		list.add(new IDropDownBean(2,"补充"));
		egu.getColumn("leix").setEditor(new ComboBox());
		egu.getColumn("leix").setComboEditor(egu.gridId, new IDropDownModel(list));
		egu.getColumn("leix").setReturnId(false);
		
		// 工具栏,时间选择框
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
//		df.Binding("RIQI","forms[0]");// 与html页中的id绑定,并自动刷新
		df.listeners="change:function(own,newValue,oldValue) {document.getElementById('RIQI').value = newValue.dateFormat('Y-m-d');document.forms[0].submit();"+MainGlobal.getExtMessageShow("正在处理请稍等...", "处理中...", 200)+"}";
		egu.addToolbarItem(df.getScript());
		
		// 电厂树
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),getTreeid());
		
	       
//		TreeButton tbt=new TreeButton(TreeButton.ButtonType_Window_Ok,etu.treeId,null);
//		tbt.handler=tbt.handler.substring(0, tbt.handler.length()-2)+";"+MainGlobal.getExtMessageShow("正在处理请稍等...", "处理中...", 200)+"}\n";
//		etu.addBbarButton(tbt);
		
		 
		setTree(etu);
		egu.addTbarText("-");// 设置分隔符 
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");

		/*egu.addTbarText("类型：");
		ComboBox leix=new ComboBox();
		leix.setTransform("GongmLxSelect");
		leix.setListeners("select:function(){document.Form0.submit();"+MainGlobal.getExtMessageShow("正在处理请稍等...", "处理中...", 200)+"}");
//		egu.addField(leix);
		leix.setWidth(60);
		egu.addToolbarItem(leix.getScript());*/
		  
//		//---------------页面js的计算开始------------------------------------------
		StringBuffer sb = new StringBuffer();
//		
		sb.append("\ngridDiv_grid.on('beforeedit',function(e){");
		sb.append("if(e.record.get('GONGYSB_ID')=='合计'){e.cancel=true;}");//当供应商列的值是"合计"时,这一行不允许编辑
		sb.append(" if(e.field=='DIANCXXB_ID' || e.field=='LEIX'){ e.cancel=true;}");//电厂列和类型不允许编辑
		
		if(((Visit)getPage().getVisit()).getRenyjb()==3){//数据上报后，电厂用户不可以修改数据
			sb.append(" if(e.record.get('SHANGBZT')==1){ e.cancel=true;}");//电厂列和类型不允许编辑
		}
		sb.append("});");
//		
		sb.append("gridDiv_grid.on('afteredit',function(e){");
			
		//当日供煤发生变化时,合计行的(当日供煤,累计供煤,库存)发生计算
//			sb.append("rec = gridDiv_ds.getAt(0);if(!(rec.get('GONGYSB_ID')=='合计')){return;}").append("if(e.field=='RIJJHL'){").append("rec.set('RIJJHL', eval(rec.get('RIJJHL')||0) + eval(e.value||0) - eval(e.originalValue||0));").append("}");
			sb.append("rec = gridDiv_ds.getAt(0);if(!(rec.get('GONGYSB_ID')=='合计')){return;}").append("if(e.field=='CHES'){").append("rec.set('CHES', eval(rec.get('CHES')||0) + eval(e.value||0) - eval(e.originalValue||0));").append("}");
			sb.append("rec = gridDiv_ds.getAt(0);if(!(rec.get('GONGYSB_ID')=='合计')){return;}").append("if(e.field=='DUNS'){").append("rec.set('DUNS', eval(rec.get('DUNS')||0) + eval(e.value||0) - eval(e.originalValue||0));").append("}");
				
		sb.append("});");
//		
//		 
		egu.addOtherScript(sb.toString());
//		//---------------页面js计算结束--------------------------
		
		egu.addTbarText("-");
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageShow("正在处理请稍等...", "刷新中...", 200))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, "InsertButton");
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, "DeleteButton");
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_SaveAll, "SaveButton",""+MainGlobal.getExtMessageShow("正在处理请稍等...", "处理中...", 200));
		setExtGrid(egu);
		con.Close();
	}

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
			this.setTreeid(null);
			this.setRiqi(null);
		}
		
		getSelectData();
		if (!returnMsg){
			setMsg("");
		}
		returnMsg=false;
	
		
	}

//	 得到登陆用户所在电厂或者分公司的名称
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ getTreeid();
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return diancmc;

	}
//	 得到是否包含运损系统设置参数
	private String getBaohys() {
		String baohys = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql= "select zhi from xitxxb where mingc='是否包含运损' and diancxxb_id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql);
		try {
			while (rs.next()) {
				baohys = rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return baohys;

	}
	
	//得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getIDropDownDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}
	
	//得到电厂的默认到站
	public String getDiancDaoz(){
		String daoz = "";
		String treeid=this.getTreeid();
		if(treeid==null||treeid.equals("")){
			treeid="1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = "+treeid+"");

			ResultSet rs = con.getResultSet(sql.toString());
			
			while (rs.next()) {
				 daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		return daoz;
	}
	
	boolean treechange=false;
	private String treeid;
//	public String getTreeid() {
//		if(treeid==null||treeid.equals("")){
//			treeid=String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
//		}
//		return treeid;
//	}
//	public void setTreeid(String treeid) {
//		this.treeid = treeid;
//		treechange=true;
//	}
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}
	
	
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}
	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}
	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}
	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}

	//日期控件
	boolean riqichange=false;
	private String riqi;
	public String getRiqi() {
		if(riqi==null||riqi.equals("")){
			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay));
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		
		if(this.riqi!=null &&!this.riqi.equals(riqi)){
			this.riqi = riqi;
			riqichange=true;
		}
		 
	}
	//得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			
			con.Close();
		}

		return jib;
	}
	public boolean isZuorkc(){//判断昨日库存是否有数据
		boolean isZuorkc=false;
		int treejib = this.getDiancTreeJib();
		
		if (treejib == 3) {
			JDBCcon con = new JDBCcon();
			String riqTiaoj=DateUtil.FormatDate(DateUtil.AddDate(DateUtil.getDate(getRiqi()), -1, DateUtil.AddType_intDay));
			
			String sqlJib = "select s.diancxxb_id from fenkshcrb s where s.riq=to_date('"+riqTiaoj +"','yyyy-mm-dd') and s.diancxxb_id="+this.getTreeid()+"";
			ResultSet rs = con.getResultSet(sqlJib.toString());

			try {
				while (rs.next()) {
					isZuorkc = true;
				}
				rs.close();
			} catch (SQLException e) {

				e.printStackTrace();
			} finally {
				con.Close();
			}

		}else{
			isZuorkc=true;
		}
		
		return isZuorkc;
	}
	
	private static IPropertySelectionModel _GongmLxModel;//供煤类型
	
	public IPropertySelectionModel getGongmLxModel() {
		if (_GongmLxModel == null) {
			getGongmLxModels();
		}
		return _GongmLxModel;
	}

	private IDropDownBean _GongmLxValue;

	public IDropDownBean getGongmLxValue() {
		if(_GongmLxValue==null){
			setGongmLxValue((IDropDownBean)getGongmLxModel().getOption(0));
		}
		return _GongmLxValue;
	}
	
	private boolean _GongmLxChange=false;
	public void setGongmLxValue(IDropDownBean Value) {
		if (_GongmLxValue==Value) {
			_GongmLxChange = false;
		}else{
			_GongmLxValue = Value;
			_GongmLxChange = true;
		}
	}

	public IPropertySelectionModel getGongmLxModels() {
		List listGongmLx=new ArrayList();
		listGongmLx.add(new IDropDownBean(1, "重点"));
		listGongmLx.add(new IDropDownBean(2, "补充"));
		
		_GongmLxModel = new IDropDownModel(listGongmLx);
		return _GongmLxModel;
	}

	public void setGongmLxModel(IPropertySelectionModel _value) {
		_GongmLxModel = _value;
	}
	
}
