package com.zhiren.jt.zdt.xitgl.meikxx;

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
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Meikxx extends BasePage implements PageValidateListener {
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


		//--------------------------------
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList drsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		StringBuffer sql_delete = new StringBuffer("begin \n");
		long gongysb_id=this.getMeikdqmcValue().getId();
		while (drsl.next()) {
			//删除月标煤单价表
			sql_delete.append("delete from " ).append(" gongysmkglb").append(" where gongysb_id=").append(gongysb_id)
			          .append(" and meikxxb_id=").append(drsl.getLong("id")).append(";\n");
			sql_delete.append("delete from ").append("meikxxb").append(" where id =").append(drsl.getLong("id")).append(";\n");
			}
		sql_delete.append("end;");
		//System.out.println(sql_delete.length());
		if(sql_delete.length()>11){
			con.getUpdate(sql_delete.toString());
		}
		
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		
		StringBuffer sql = new StringBuffer();
		while (rsl.next()) {
			sql.delete(0, sql.length());
			sql.append("begin \n");
			long id = 0;
			
			
			String ID_1=rsl.getString("ID");

			if ("0".equals(ID_1)||"".equals(ID_1)) {
				id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
				
				
						
				//向煤矿信息表里插入数据
				sql.append("insert into meikxxb("
								+ "id,xuh,bianm,mingc,quanc,piny,shengfb_id,leib,jihkjb_id,leix,beiz,danwdz,chengsb_id)values("
								+ id+ ","
								+rsl.getString("xuh")+",'"
								+rsl.getString("bianm")+"','"
								+rsl.getString("mingc")+"','"
								+rsl.getString("quanc")+"','"
								+rsl.getString("piny")+"',"
								+"getShengfbId('"+rsl.getString("shengfb_id")+"'),'"
								+rsl.getString("leib")+"',"
								+"getJihkjbId('"+rsl.getString("jihkjb_id")+"'),'"
								+rsl.getString("leix")+"','"
								+rsl.getString("beiz")+"','"
								+rsl.getString("danwdz")+"',"
								+"getChengsbId('"+rsl.getString("chengsb_id")+"')"
								+ ");\n");
				sql.append("insert into gongysmkglb (id,gongysb_id,meikxxb_id) values(" +
							+id+","+gongysb_id+","+id+");\n");
				
			} else {
				
				
				//修改煤矿信息表数据
				 sql.append("update meikxxb set xuh="
				 + rsl.getString("xuh")+",bianm='"
				 + rsl.getString("bianm")+"',mingc='"
				 + rsl.getString("mingc")+"',quanc='"
				 + rsl.getString("quanc")+"',piny='"
				 + rsl.getString("piny")+"',shengfb_id="
				 +"getShengfbId('"+rsl.getString("shengfb_id")+"'),leib='"
				 + rsl.getString("leib")+"',jihkjb_id="
				 +"getJihkjbId('"+rsl.getString("jihkjb_id")+"'), leix='"
				 + rsl.getString("leix")+"',beiz='"
				 + rsl.getString("beiz")+"',danwdz='"
				 + rsl.getString("danwdz")+"',chengsb_id="
				 +"getChengsbId('"+rsl.getString("chengsb_id")+"')"
				 + " where id=" + rsl.getLong("id")+";\n");
			
			}
			sql.append("end;");
			con.getUpdate(sql.toString());
		

			
		}
		
		con.Close();
		setMsg("保存成功!");
	}


	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _CopyButton = false;
	public void CopyButton(IRequestCycle cycle) {
		_CopyButton = true;
	}
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	public void submit(IRequestCycle cycle) {
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
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		//-----------------------------------
		String str = "";
		
		
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and dc.id = " + getTreeid() + "";
		
		}
		String gongysmc=this.getMeikdqmcValue().getValue();

		String chaxun = "select m.id,m.xuh,m.bianm,m.mingc,m.quanc, m.piny,sf.quanc as shengfb_id,\n"
				+ "       cs.mingc as chengsb_id,j.mingc as jihkjb_id,m.leib,\n"
				+ "       m.leix,\n"
				+ "       m.danwdz,\n"
				+ "       m.beiz\n"
				+ "  from meikxxb m, shengfb sf, chengsb cs, jihkjb j,gongysmkglb gl\n"
				+ " where m.shengfb_id = sf.id(+)\n"
				+ "   and m.chengsb_id = cs.id(+)\n"
				+ "   and m.jihkjb_id = j.id(+)\n"
				+ "   and m.id=gl.meikxxb_id(+)\n" 
				+ "   and gl.gongysb_id=getGongysId('"+gongysmc+"')" 
				+"    order by m.xuh     ";


		
		
	// System.out.println(chaxun);
	ResultSetList rsl = con.getResultSetList(chaxun);
	
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("meikxxb");
   	
	egu.getColumn("id").setHidden(true);
	egu.getColumn("id").setEditor(null);
	
	egu.getColumn("xuh").setCenterHeader("排序号");
	egu.getColumn("bianm").setCenterHeader("编码");
	egu.getColumn("mingc").setCenterHeader("简称");
	egu.getColumn("quanc").setCenterHeader("全称");
	egu.getColumn("piny").setCenterHeader("拼音");
	egu.getColumn("shengfb_id").setCenterHeader("省份");
	egu.getColumn("chengsb_id").setCenterHeader("所在市");
	egu.getColumn("jihkjb_id").setCenterHeader("计划口径");
	egu.getColumn("leib").setCenterHeader("类别");
	egu.getColumn("leix").setCenterHeader("类型");
	egu.getColumn("danwdz").setCenterHeader("单位地址");
	egu.getColumn("beiz").setCenterHeader("备注");
	//当用户为电厂级别时,煤矿编码不能编辑
	if(((Visit) getPage().getVisit()).isDCUser()){
		egu.getColumn("bianm").setEditor(null);
	}
	//设定列初始宽度
	egu.getColumn("xuh").setWidth(60);
	egu.getColumn("bianm").setWidth(90);
	egu.getColumn("mingc").setWidth(120);
	egu.getColumn("quanc").setWidth(150);
	egu.getColumn("piny").setWidth(80);
	egu.getColumn("shengfb_id").setWidth(80);
	egu.getColumn("chengsb_id").setWidth(80);
	egu.getColumn("jihkjb_id").setWidth(80);
	egu.getColumn("leib").setWidth(70);
	egu.getColumn("leix").setWidth(60);
	egu.getColumn("danwdz").setWidth(100);
	egu.getColumn("beiz").setWidth(100);
	
	egu.setGridType(ExtGridUtil.Gridstyle_Edit);//设定grid可以编辑
	egu.addPaging(22);//设置分页
	egu.setWidth(1000);//设置页面的宽度,当超过这个宽度时显示滚动条
	
	
	
	//*****************************************设置默认值****************************
	
	
	
	
	//设置日期的默认值,
	//egu.getColumn("riq").setDefaultValue(intyear+"-"+StrMonth+"-01");
	
	
	
	
	//*************************下拉框*****************************************88
	//设置省份的下拉框
	ComboBox cb_shengf=new ComboBox();
	egu.getColumn("shengfb_id").setEditor(cb_shengf);
	cb_shengf.setEditable(true);
	String ShengfSql="select id,quanc from shengfb order by mingc";
	egu.getColumn("shengfb_id").setComboEditor(egu.gridId, new IDropDownModel(ShengfSql));
	//设置城市下拉框
	ComboBox cb_chengs=new ComboBox();
	egu.getColumn("chengsb_id").setEditor(cb_chengs);
	cb_chengs.setEditable(true);
	egu.getColumn("chengsb_id").editor.setAllowBlank(true);//设置下拉框是否允许为空
	String jihkjSql="select c.id,c.mingc from chengsb c order by mingc  ";
	egu.getColumn("chengsb_id").setComboEditor(egu.gridId, new IDropDownModel(jihkjSql));
	
	//计划口径下拉框
	ComboBox cb_jhkj=new ComboBox();
	egu.getColumn("jihkjb_id").setEditor(cb_jhkj);
	cb_jhkj.setEditable(true);
	egu.getColumn("jihkjb_id").editor.setAllowBlank(true);//设置下拉框是否允许为空
	String cb_pinzStr="select j.id,j.mingc from jihkjb j order by id  ";
	egu.getColumn("jihkjb_id").setComboEditor(egu.gridId, new IDropDownModel(cb_pinzStr));
	egu.getColumn("jihkjb_id").setDefaultValue("重点订货");
	
//	类别下拉框
	List leib = new ArrayList();
	leib.add(new IDropDownBean(0, "统配矿"));
	leib.add(new IDropDownBean(1, "地方矿"));
	egu.getColumn("leib").setEditor(new ComboBox());
	egu.getColumn("leib").setComboEditor(egu.gridId, new IDropDownModel(leib));
	egu.getColumn("leib").setReturnId(false);
	egu.getColumn("leib").setDefaultValue("统配矿");
	
//	类型下拉框
	List leix = new ArrayList();
	leix.add(new IDropDownBean(0, "煤"));
	leix.add(new IDropDownBean(1, "油"));
	leix.add(new IDropDownBean(2,"气"));
	egu.getColumn("leix").setEditor(new ComboBox());
	egu.getColumn("leix").setComboEditor(egu.gridId, new IDropDownModel(leix));
	egu.getColumn("leix").setReturnId(false);
	egu.getColumn("leix").setDefaultValue("煤");
	
	//********************工具栏************************************************
		
		//设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		egu.addTbarText("-");// 设置分隔符
	
		egu.addTbarText("供应商:");
		ComboBox comb3 = new ComboBox();
		comb3.setTransform("MeikmcDropDown");
		comb3.setId("gongys");
		comb3.setEditable(true);
		comb3.setLazyRender(true);// 动态绑定
		comb3.setWidth(130);
		egu.addToolbarItem(comb3.getScript());

		// 设定工具栏下拉框自动刷新
		egu.addOtherScript("gongys.on('select',function(){document.forms[0].submit();});");
		
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton",MainGlobal.getExtMessageShow("正在保存,请稍后...", "保存中...", 200));
		egu.addTbarText("-");
		//用户是电厂时,不显示查询条件
		if(!((Visit) getPage().getVisit()).isDCUser()){
			egu.addTbarText("查询煤矿编码:");
			TextField tf=new TextField();
			tf.setWidth(130);
			tf.setId("TIAOJ");
			egu.addToolbarItem(tf.getScript());
			//提交
			
			String strs=
	       		" var url = 'http://'+document.location.host+document.location.pathname;"+
	            "var end = url.indexOf(';');"+
				"url = url.substring(0,end);"+
	       	    "url = url + '?service=page/' + 'meikbm_cx&tiaoj='+TIAOJ.getValue();"+
	       	    " window.open(url,'newWin');";
			egu.addToolbarItem("{"+new GridButton("查询","function (){"+strs+"}").getScript()+"}");
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
		if (getExtGrid() == null) {
			return "";
		}
		if (getTbmsg() != null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem(
					"'<marquee width=200 scrollamount=2>" + getTbmsg()
							+ "</marquee>'");
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
			this.setMeikdqmcValue(null);
			this.getIMeikdqmcModel();
			this.setTreeid(null);
		
			setTbmsg(null);
		}
		
			getSelectData();
		
		
	}

//	 供应商名称
	public boolean _meikdqmcchange = false;

	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if (_MeikdqmcValue == null) {
			_MeikdqmcValue = (IDropDownBean) getIMeikdqmcModels().getOption(0);
		}
		return _MeikdqmcValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikdqmcValue != null) {
			id = _MeikdqmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikdqmcchange = true;
			} else {
				_meikdqmcchange = false;
			}
		}
		_MeikdqmcValue = Value;
	}

	private IPropertySelectionModel _IMeikdqmcModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IMeikdqmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		try {
			String sql = "";
			int treejib2 = this.getDiancTreeJib();
			//供应商下拉框内的值和电厂进行关联
			if(treejib2==3){
				sql = 	"select g.id, g.mingc\n" +
				"  from gongysb g, gongysdcglb gl, diancxxb dc\n" + 
				" where gl.gongysb_id = g.id\n" + 
				"   and gl.diancxxb_id = dc.id\n" + 
				"   and dc.id = "+getTreeid()+"";
				
			}else if(treejib2==2){
				sql = 	"select g.id, g.mingc\n" +
				"  from gongysb g, gongysdcglb gl, diancxxb dc\n" + 
				" where gl.gongysb_id = g.id\n" + 
				"   and gl.diancxxb_id = dc.id\n" + 
				"   and dc.fuid = "+getTreeid()+" or dc.id="+getTreeid()+"";
			}else if(treejib2==1){
				sql=
					"select g.id, g.mingc\n" +
					"  from gongysb g, gongysdcglb gl, diancxxb dc\n" + 
					" where gl.gongysb_id = g.id\n" + 
					"   and gl.diancxxb_id = dc.id\n";
				
			}
		
			_IMeikdqmcModel = new IDropDownModel(sql,"请选择");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IMeikdqmcModel;
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
			// TODO 自动生成 catch 块
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
	
	
	private String treeid;
	/*public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
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
}
