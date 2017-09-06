package com.zhiren.dc.jiek;

import java.sql.ResultSet;
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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Jies extends BasePage implements PageValidateListener {
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
//	绑定日期
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}
//	绑定日期
	private String riq1;

	public String getRiq1() {
		return riq1;
	}

	public void setRiq1(String riq) {
		this.riq1 = riq;
	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _ShenhClick = false;
	public void ShangcButton(IRequestCycle cycle) {
		_ShenhClick = true;
	}
	
	private void Shenh() {
		//1,因为结算时供应商，煤矿编码都验证 
		//2,构造jiesb、jieszbsjb插入语句,执行
		//3，更新fahbtmp.jiesb_id
		//4，写客户端日志jiesb.shangc
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		JDBCcon con = new JDBCcon();
	try{
		String strIn="";
		while(rsl.next()){
			strIn+=rsl.getString("id")+",";
		}
		strIn=strIn.substring(0, strIn.length()-1);
		//1

		String sql="select substr(gongysb.shangjgsbm,1,6)shangjgsbm\n" +
		"from jiesb,gongysb\n" + 
		"where jiesb.gongysb_id=gongysb.id\n" +
		" and jiesb.id in(" + strIn+")" + 
		"and not exists (\n" + 
		"select * from gongysb@gangkjy Ygongysb where Ygongysb.bianm=substr(gongysb.shangjgsbm,1,6)\n" + 
		")";
		ResultSet rs1=con.getResultSet(sql);
		
			if(rs1.next()){
				setMsg(rs1.getString("shangjgsbm")+"供应商编码不能识别！");
				return;
			}
	//1

		sql="select  meikxxb.shangjgsbm\n" +
		"from jiesb,meikxxb\n" + 
		"where jiesb.meikxxb_id=meikxxb.id\n" + 
		" and jiesb.id in(" + strIn+")" + 
		" and not exists (\n" + 
		"select * from meikxxb@gangkjy Ymeikxxb where Ymeikxxb.bianm=meikxxb.shangjgsbm\n" + 
		")";
		ResultSet rs2=con.getResultSet(sql);
		if(rs2.next()){
			setMsg(rs2.getString("shangjgsbm")+"煤矿编码不能识别！");
			return;
		}
		//2
		sql="insert into jiesb@gangkjy\n" +
		"  (id,diancxxb_id,bianm,gongysb_id,gongysmc,yunsfsb_id,yunj, yingd,kuid,faz,fahksrq, fahjzrq,meiz,daibch,\n" + 
		"   yuanshr,xianshr,yansksrq,yansjzrq, yansbh,shoukdw, kaihyh,zhangh,fapbh,fukfs,duifdd, ches,jiessl,guohl,\n" + 
		"   yuns,koud,jiesslcy,hansdj,bukmk,hansmk,buhsmk,meikje,shuik,shuil, buhsdj,jieslx,jiesrq,ruzrq,hetb_id,\n" + 
		"   liucztb_id,liucgzid,ranlbmjbr,ranlbmjbrq,beiz,jiesfrl,jihkjb_id,meikxxb_id,hetj,meikdwmc,zhiljq,qiyf,\n" + 
		"   jiesrl,jieslf,jiesrcrl,ruzry,diancjsmkb_id,xiugzt)\n" + 
		"select jiesb.id,diancxxb_id,(select max(zhi)zhi from xitxxb where mingc='结算单所属单位')||jiesb.bianm,\n" + 
		"(select id from gongysb@gangkjy ygongysb where ygongysb.bianm=substr(gongysb.shangjgsbm,1,6)) gongysb_id,\n" + 
		"gongysmc,yunsfsb_id,yunj, yingd,kuid,faz,fahksrq, fahjzrq,jiesb.meiz,daibch,\n" + 
		"   yuanshr,xianshr,yansksrq,yansjzrq, yansbh,shoukdw, jiesb.kaihyh,jiesb.zhangh,fapbh,fukfs,duifdd, ches,jiessl,guohl,\n" + 
		"   yuns,koud,jiesslcy,hansdj,bukmk,hansmk,buhsmk,meikje,shuik,shuil, buhsdj,jieslx,jiesrq,ruzrq,hetb_id,\n" + 
		"   liucztb_id,liucgzid,ranlbmjbr,ranlbmjbrq,jiesb.beiz,jiesfrl,jihkjb_id,meikxxb_id,hetj,meikdwmc,zhiljq,qiyf,\n" + 
		"   jiesrl,jieslf,jiesrcrl,ruzry,0,0\n" + 
		"from jiesb,gongysb\n" + 
		"where jiesb.gongysb_id=gongysb.id and jiesb.id in(" + strIn+")";
		con.getInsert(sql);

		sql=
			"insert into jieszbsjb@gangkjy\n" +
			"  (yansbhb_id, id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt)\n" + 
			"select yansbhb_id, id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt\n" + 
			"from jieszbsjb\n" + 
			"where jiesdid in(" + strIn+")";
		con.getInsert(sql);
		//3
		rsl.beforefirst();
		while(rsl.next()){
			sql=
				"update fahbtmp@gangkjy f\n" +
				"set f.jiesb_id=\n" + rsl.getString("id")+
				"where f.lieid in(\n" + 
				"select id from fahb where jiesb_id=\n" +rsl.getString("id")+ 
				")";
			con.getUpdate(sql);
		}
		//4
		sql="update jiesb\n" +
		"set shangc=1\n" + 
		"where jiesb.id in(" + strIn+")";
		con.getUpdate(sql);
		setMsg("上传数据成功！");
	}catch(Exception e){
		e.printStackTrace();
		setMsg("上报时发生未知异常！");
		return;
	}finally{
		con.Close();
	}
	}	
	private boolean _QuxshClick = false;
	public void QuxButton(IRequestCycle cycle) {
		_QuxshClick = true;
	}
	
	private void Quxsh() {
		//1,多行取消
		//2,判断jiesb.xiugzt的状态,如果0,直接删除,否则提示不能取消
		//3,写本地日志,jiesb置成未上传状态
		//4,提示取消成功
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		JDBCcon con = new JDBCcon();
		String strIn="";
		while(rsl.next()){
			strIn+=rsl.getString("id")+",";
		}
		strIn=strIn.substring(0, strIn.length()-1);

		String sql=
		"select nvl( max(j.xiugzt),0)\n" +
		"from jiesb@gangkjy j\n" + 
		"where j.id in ("+strIn+")";
		ResultSet rs=con.getResultSet(sql);
	try{
			
		if(rs.next()){
			if(rs.getInt(1)!=0){
				setMsg("数据已经使用无法取消！");
				return;
			}else{
				sql="delete from jiesb@gangkjy j where j.id  in ("+strIn+")";
				con.getDelete(sql);
				sql="delete from jieszbsjb@gangkjy j where j.jiesdid  in ("+strIn+")";
				con.getDelete(sql);
				setMsg("数据取消成功！");
				
				sql="update jiesb\n" +
				"set shangc=0\n" + 
				"where jiesb.id in(" + strIn+")";
				con.getUpdate(sql);
			}
		}
	}catch(Exception e){
		e.printStackTrace();
		setMsg("取消时发生未知异常！");
		return;
	}finally{
		con.Close();
	}
	}
	
	public void submit(IRequestCycle cycle) {
		if (_ShenhClick) {
			_ShenhClick = false;
			Shenh();
		}
		if (_QuxshClick) {
			_QuxshClick = false;
			Quxsh();
		}
	}
	
	private void initGrid() {
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();

		sb.append("select jiesb.id,jiesb.bianm,jiesb.gongysmc,xianshr,jiesb.jiesrq,ches,jiessl,hansdj\n" +
		"from jiesb\n" + 
		"where to_char(jiesb.jiesrq,'yyyy-mm-dd')>='"+getRiq()+"' and to_char(jiesb.jiesrq,'yyyy-mm-dd')<='"+getRiq1()+"' " +
		"and jiesb.shangc="+getLeixSelectValue().getId()+ 
		" and jiesb.meikxxb_id in(select meikxxb_id from shangcsjpz where kaisrq<=jiesb.jiesrq and jiesrq>=jiesb.jiesrq)"
		);

		ResultSetList rsl = con.getResultSetList(sb.toString());
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		//设置多选框
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		egu.getColumn("id").setHeader("标识");
		egu.getColumn("id").setWidth(150);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("id").setHidden(true);
		
		egu.getColumn("bianm").setHeader("编号");
		egu.getColumn("bianm").setWidth(150);
		egu.getColumn("bianm").setEditor(null);
		
		egu.getColumn("gongysmc").setHeader("结算单位");
		egu.getColumn("gongysmc").setWidth(150);
		egu.getColumn("gongysmc").setEditor(null);
		
		egu.getColumn("xianshr").setHeader("收货单位");
		egu.getColumn("xianshr").setWidth(150);
		egu.getColumn("xianshr").setEditor(null);
		
		egu.getColumn("jiesrq").setHeader("结算日期");
		egu.getColumn("jiesrq").setWidth(70);
		egu.getColumn("jiesrq").setEditor(null);
		
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setWidth(70);
		egu.getColumn("ches").setEditor(null);
		
		egu.getColumn("jiessl").setHeader("数量");
		egu.getColumn("jiessl").setWidth(70);
		egu.getColumn("jiessl").setEditor(null);
		
		egu.getColumn("hansdj").setHeader("含税单价");
		egu.getColumn("hansdj").setWidth(70);
		egu.getColumn("hansdj").setEditor(null);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(0);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		//设置每页显示行数
		egu.addPaging(25);
		
		egu.addTbarText("结算日期：");
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "Form0");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		
		DateField df1 = new DateField();
		df1.setValue(getRiq1());
		df1.Binding("RIQ1", "Form0");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df1.getScript());
		
		egu.addTbarText("状态:");
		ComboBox comb2=new ComboBox();
		comb2.setId("zhuangt");
		comb2.setWidth(100);
	
		comb2.setTransform("zhuangtSelect");
		comb2.setLazyRender(true);//动态绑定weizSelect
		egu.addToolbarItem(comb2.getScript());
		egu.addOtherScript("zhuangt.on('select',function(){document.forms[0].submit()});");
		if(getLeixSelectValue().getId()==0){//为提交
			egu.addToolbarButton("上传",GridButton.ButtonType_SubmitSel, "ShangcButton", null);
		}else{
			egu.addToolbarButton("取消",GridButton.ButtonType_SubmitSel, "QuxButton", null);
		}
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
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid()==null){
			return "";
		}
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
//	 类型
	public IDropDownBean getLeixSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getLeixSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setLeixSelectValue(IDropDownBean Value) {
		
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setLeixSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getLeixSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getLeixSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getLeixSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, "未上传"));
		list.add(new IDropDownBean(1,"已上传"));
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(list));
		return;
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			if(getRiq() == null) {
				setRiq(DateUtil.FormatDate(new Date()));
			}
			if(getRiq1() == null) {
				setRiq1(DateUtil.FormatDate(new Date()));
			}
			((Visit) getPage().getVisit())
			.setProSelectionModel3(null);
		}
		initGrid();
	} 
}