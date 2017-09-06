package com.zhiren.jt.zdt.xitgl.gongyspj.pingjxxb;

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
import com.zhiren.common.ext.form.Field;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Pingjxxb extends BasePage implements PageValidateListener {
	private String msg = "";

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
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}


	private void Save() {
		
		
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		StringBuffer sb = new StringBuffer("begin \n");
		
		while (rsl.next()) {
			sb.append("update pingjxxb set pingjxmdjb_id=get_Pingjxmdjb_Id('");
			sb.append(rsl.getString("pingjxmdjb_id")).append("'), beiz='"+rsl.getString("beiz")+"'  where id="+rsl.getLong("id")+";\n");
		}
		sb.append("end;");
		con.getUpdate(sb.toString());
	
	
		con.Close();
	
		setMsg("保存成功!");
		
		
	}
	
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	private boolean _InsertButton = false;
	public void InsertButton(IRequestCycle cycle) {
		_InsertButton = true;
	}
	private boolean _DeleteButton = false;
	public void DeleteButton(IRequestCycle cycle) {
		_DeleteButton = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		
			getSelectData();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			this.getGongysModels();
			getSelectData();
		}
		if(_InsertButton){
			_InsertButton=false;
			Insert();
			
			getSelectData();
		}
		if(_DeleteButton){
			_DeleteButton = false;
			Delete();
			
			getSelectData();
		}
		
	}
	
	public void Delete(){
		
		JDBCcon con = new JDBCcon();
		long pingjmc_id=0;
		long diancmc_id=0;
		long gongysmc_id=0;
		pingjmc_id=this.getPingjmcValue().getId();
		diancmc_id=Long.parseLong(this.getTreeid());
		gongysmc_id=this.getGongysValue().getId();
		
		
		String DeleteStr="delete pingjxxb where diancxxb_id="+diancmc_id+" and gongysb_id="+gongysmc_id+" and pingjmcb_id="+pingjmc_id+"";
		con.getDelete(DeleteStr);
		con.Close();
	}
	public void Insert(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long pingjmc_id=0;
		long diancmc_id=0;
		long gongysmc_id=0;
		pingjmc_id=this.getPingjmcValue().getId();
		diancmc_id=Long.parseLong(this.getTreeid());
		gongysmc_id=this.getGongysValue().getId();
		
		
		ResultSetList Re = con.getResultSetList("select * from pingjxxb p where p.diancxxb_id="+diancmc_id+" and p.gongysb_id="+gongysmc_id+" and p.pingjmcb_id="+pingjmc_id+"");
		if(Re.next()){
			con.Close();
			this.setMsg(""+getGongysValue().getValue()+"  <"+getPingjmcValue().getValue()+">系统中已经存在,不允许重复添加! ");
			return;
		}
		
		
		StringBuffer sbInsert = new StringBuffer("begin \n");
		long pingjxmb_id=0;
		
		ResultSetList rsl = con.getResultSetList("select id from pingjxmb ");
		while (rsl.next()){
			pingjxmb_id=rsl.getLong("id");
			long id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
			
			sbInsert.append("insert into pingjxxb (id,diancxxb_id,pingjmcb_id,gongysb_id,pingjxmb_id,pingjxmdjb_id,beiz)")
			.append(" values ("+id+","+diancmc_id+","+pingjmc_id+","+gongysmc_id+","+pingjxmb_id+",null,'');").append("\n");
		}
		
		sbInsert.append("end;");
		if(sbInsert.length()>10){
			con.getInsert(sbInsert.toString());
		}
		con.Close();
	}
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long pingjmc=0;
		long diancid=0;
		long gongsbid=0;
		pingjmc=this.getPingjmcValue().getId();
		diancid=Long.parseLong(this.getTreeid());
		gongsbid=this.getGongysValue().getId();

		String chaxun = "select p.id,dc.mingc as diancxxb_id,g.mingc as gongysb_id,\n"
				+ "       mc.mingc as pingjmcb_id,xm.mingc as pingjxmb_id,\n"
				+ "       dj.mingc as pingjxmdjb_id,p.beiz\n"
				+ "from pingjxxb p ,diancxxb dc,pingjmcb mc,gongysb g,pingjxmb xm,pingjxmdjb dj\n"
				+ "where p.diancxxb_id=dc.id\n"
				+ "and p.pingjmcb_id=mc.id\n"
				+ "and p.gongysb_id=g.id\n"
				+ "and p.pingjxmb_id=xm.id\n"
				+ "and p.pingjxmdjb_id=dj.id(+)\n"
				+ "and dc.id="+diancid+"\n"
				+ "and g.id="+gongsbid+"\n"
				+ "and mc.id="+pingjmc+"\n" 
				+ "order by xm.id";



		
	// System.out.println(chaxun);
	ResultSetList rsl = con.getResultSetList(chaxun);
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("pingjxxb");
   	egu.setWidth("bodyWidth");

	egu.getColumn("id").setHidden(true);
	egu.getColumn("id").setEditor(null);

	egu.getColumn("diancxxb_id").setCenterHeader("单位名称");
	egu.getColumn("diancxxb_id").setHidden(true);
	egu.getColumn("diancxxb_id").setEditor(null);
	egu.getColumn("gongysb_id").setCenterHeader("供应商");
	egu.getColumn("gongysb_id").setHidden(true);
	egu.getColumn("gongysb_id").setEditor(null);
	egu.getColumn("pingjmcb_id").setCenterHeader("评价名称");
	egu.getColumn("pingjmcb_id").setHidden(true);
	egu.getColumn("pingjmcb_id").setEditor(null);
	
	egu.getColumn("pingjxmb_id").setCenterHeader("评价项目");
	egu.getColumn("pingjxmb_id").setEditor(null);
	egu.getColumn("pingjxmdjb_id").setCenterHeader("等级");
	egu.getColumn("beiz").setCenterHeader("备注");
	
//	设定不可编辑列的颜色
	egu.getColumn("pingjxmb_id").setRenderer("function(value,metadata){metadata.css='tdTextext1'; return value;}");
	
	egu.setDefaultsortable(false);//设定页面不自动排序
	
	//设定列初始宽度
	
	egu.getColumn("pingjxmb_id").setWidth(200);
	egu.getColumn("pingjxmdjb_id").setWidth(100);
	
	
//	设定列的小数位
	//((NumberField)egu.getColumn("zhi1").editor).setDecimalPrecision(3);
	
	egu.setGridType(ExtGridUtil.Gridstyle_Edit);//设定grid可以编辑
	egu.addPaging(30);//设置分页
	
	
	

	
	
	//*************************下拉框*****************************************88
	
//	电厂下拉框
	int treejib2 = this.getDiancTreeJib();

	if (treejib2 == 1) {// 选集团时刷新出所有的电厂
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
		egu.getColumn("diancxxb_id").setReturnId(true);
	} else if (treejib2 == 2) {// 选分公司的时候刷新出分公司下所有的电厂
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id,mingc from diancxxb where fuid="+getTreeid()+" order by mingc"));
		egu.getColumn("diancxxb_id").setReturnId(true);
	} else if (treejib2 == 3) {// 选电厂只刷新出该电厂
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc"));
		ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");			
		String mingc="";
		if(r.next()){
			mingc=r.getString("mingc");
		}
		egu.getColumn("diancxxb_id").setDefaultValue(mingc);
		
	}		
	
	//供应商
	ComboBox cb_pingjxmdj=new ComboBox();
	egu.getColumn("pingjxmdjb_id").setEditor(cb_pingjxmdj);
	cb_pingjxmdj.setEditable(true);
	String PingjxmdjSql = "select id,mingc from pingjxmdjb order by xuh";
	egu.getColumn("pingjxmdjb_id").setComboEditor(egu.gridId,new IDropDownModel(PingjxmdjSql));
	
	
	
	//********************工具栏************************************************
		egu.addTbarText("评价名称:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("PINGJMC");
		comb1.setId("PINGJMC");//和自动刷新绑定
		comb1.setLazyRender(true);//动态绑定
		comb1.setWidth(150);
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("-");// 设置分隔符
		
//		设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		
		
		
		
		egu.addTbarText("供应商:");
		ComboBox comb2=new ComboBox();
		comb2.setTransform("GONGYS");
		comb2.setId("GONGYS");//和自动刷新绑定
		comb2.setLazyRender(true);//动态绑定
		comb2.setWidth(150);
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());
		
		egu.addTbarText("-");// 设置分隔符
		
		
		
		
		//设定工具栏下拉框自动刷新
		egu.addOtherScript("PINGJMC.on('select',function(){document.forms[0].submit();});GONGYS.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// 设置分隔符
	
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
//		添加按钮
		StringBuffer ins = new StringBuffer();
		ins.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在添加数据,请稍候！'",true))
		.append("document.getElementById('InsertButton').click();}");
		GridButton gbIns = new GridButton("添加",ins.toString());
		gbIns.setIcon(SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(gbIns);
//		删除按钮

		String ss="Ext.MessageBox.confirm('警告', '你确定要删除本页所有的评价项目吗？', function(btn) { if(btn=='yes'){document.getElementById('DeleteButton').click();}})";
		StringBuffer del = new StringBuffer();
		del.append("function (){")
		.append(""+ss+"}");
		GridButton gbDel = new GridButton("删除",del.toString());
		gbDel.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbDel);
		
		//保存按钮
		
		egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton",MainGlobal.getExtMessageShow("正在保存数据,请稍后...", "保存中...", 200));
		
		egu.addTbarText("->");
		

		
		
		
		
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
		if (getExtGrid() == null) {
			return "";
		}
		
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
			setTbmsg(null);
			this.setGongysValue(null);
			this.getGongysModels();
			this.setPingjmcValue(null);
			this.getIPingjmcModels();
			
		}
		
			getSelectData();
		
		
	}


	
	

	

//	 评价名称
	public boolean _PingjmcChange = false;

	private IDropDownBean _PingjmcValue;

	public IDropDownBean getPingjmcValue() {
		if (_PingjmcValue == null) {
			_PingjmcValue = (IDropDownBean) getIPingjmcModels().getOption(0);
		}
		return _PingjmcValue;
	}

	public void setPingjmcValue(IDropDownBean Value) {
		long id = -2;
		if (_PingjmcValue != null) {
			id = _PingjmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_PingjmcChange = true;
			} else {
				_PingjmcChange = false;
			}
		}
		_PingjmcValue = Value;
	}

	private IPropertySelectionModel _IPingjmcModel;

	public void setIPingjmcModel(IPropertySelectionModel value) {
		_IPingjmcModel = value;
	}

	public IPropertySelectionModel getIPingjmcModel() {
		if (_IPingjmcModel == null) {
			getIPingjmcModels();
		}
		return _IPingjmcModel;
	}

	public IPropertySelectionModel getIPingjmcModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select id,mingc from pingjmcb order by riq";
			_IPingjmcModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IPingjmcModel;
	}
	
///////////////////////////////////////	


	
	
	
	private String treeid;

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
	
	
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
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
	
//	 供应商
	public boolean _gongysChange = false;

	private IDropDownBean _GongysValue;

	public IDropDownBean getGongysValue() {
		if (_GongysValue == null) {
			_GongysValue = (IDropDownBean) getGongysModels().getOption(0);
		}
		return _GongysValue;
	}

	public void setGongysValue(IDropDownBean Value) {
		long id = -2;
		if (_GongysValue != null) {
			id = _GongysValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_gongysChange = true;
			} else {
				_gongysChange = false;
			}
		}
		_GongysValue = Value;
	}

	private IPropertySelectionModel _GongysModel;

	public void setGongysModel(IPropertySelectionModel value) {
		_GongysModel = value;
	}

	public IPropertySelectionModel getGongysModel() {
		if (_GongysModel == null) {
			getGongysModels();
		}
		return _GongysModel;
	}

	public IPropertySelectionModel getGongysModels() {
		JDBCcon con = new JDBCcon();
		String sql = "";
		try {
			
		int jib=this.getDiancTreeJib();
		if(jib==1){
			sql = "select g.id,g.mingc from gongysb g where g.id in (\n" +
			"select distinct  g.fuid from fahb f,gongysb g,diancxxb dc\n" + 
			"where f.gongysb_id=g.id\n" + 
			"and f.diancxxb_id=dc.id)\n" + 
			"order by g.mingc";
		}else if(jib==2){
			sql=
				"select g.id,g.mingc from gongysb g where g.id in (\n" +
				"select distinct  g.fuid from fahb f,gongysb g,diancxxb dc\n" + 
				"where f.gongysb_id=g.id\n" + 
				"and f.diancxxb_id=dc.id\n" + 
				"and dc.fuid="+this.getTreeid()+")\n" + 
				"order by g.mingc";


		}else{
			sql ="select g.id,g.mingc from gongysb g where g.id in (\n" +
				"select distinct  g.fuid from fahb f,gongysb g,diancxxb dc\n" + 
				"where f.gongysb_id=g.id\n" + 
				"and f.diancxxb_id=dc.id\n" + 
				"and dc.id="+this.getTreeid()+")\n" + 
				"order by g.mingc";

		}


			_GongysModel = new IDropDownModel(sql,"请选择");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _GongysModel;
	}
}
