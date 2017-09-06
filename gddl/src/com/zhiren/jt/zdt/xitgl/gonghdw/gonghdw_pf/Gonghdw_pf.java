package com.zhiren.jt.zdt.xitgl.gonghdw.gonghdw_pf;

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

public class Gonghdw_pf extends BasePage implements PageValidateListener {
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
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
	
		StringBuffer sql = new StringBuffer();
		StringBuffer sb=new StringBuffer();
		while (rsl.next()) {
			sql.delete(0, sql.length());
			sb.delete(0, sb.length());
			sql.append("begin \n");
			long id = 0;
			if ("0".equals(rsl.getString("ID"))) {
				/*id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
				sql.append("insert into rucmjgb("
								+ "id,riq,diancxxb_id,gongysb_id,jihkjb_id,daohl,farl,chebj,yunf,zaf,biaomdj,daoczhj,fenx)values("
								+ id
								+ ",to_date('"
								+ rsl.getString("riq")
								+ "','yyyy-mm-dd'),"
								+"getDiancId('"+rsl.getString("diancxxb_id")+"'),"+""
								+"getGongysId('"+rsl.getString("gongysb_id")+"'),"+""
								+"getJihkjbId('"+rsl.getString("jihkjb_id")+"'),"+
								+ rsl.getDouble("daohl") + ","
								+ rsl.getDouble("farl") + ","
								+ rsl.getDouble("chebj") + ","
								+ rsl.getDouble("yunf") + ","
								+ rsl.getDouble("zaf") + ","
								+" Round("+rsl.getDouble("biaomdj")+",2),"
								+ rsl.getDouble("daoczhj") + ",'本月');\n");
				*/
				
			} else {
				 sql.append("update gonghdwdmsqb set xuh=");
				 sql.append(rsl.getLong("xuh")+",bianm='");
				 sql.append(rsl.getString("bianm")+"',mingc='");
				 sql.append(rsl.getString("mingc")+"',quanc='");
				 sql.append(rsl.getString("quanc")+"',shengfb_id=");
				 sql.append("getShengfbId('"+rsl.getString("shengfb_id")+"'),chengsb_id=");
				 sql.append("getChengsbId('"+rsl.getString("chengsb_id")+"'),shenqr='");
				 sql.append(rsl.getString("shenqr")+"'");
				 if(rsl.getString("bianm")!=null&&rsl.getString("bianm")!=""){
					 sql.append(",zhuangt=1 ");
				 }
				 sql.append(" where id=");
				 sql.append(rsl.getLong("id")+";\n");
					 
				 //把申请通过的值插入供应商表里面
				sb.append("insert into gongysb   ");
				sb.append(" select g.id,g.fuid,g.xuh,g.mingc,g.quanc,g.piny,g.bianm,g.danwdz,");
				sb.append(" g.faddbr,g.weitdlr,g.kaihyh,g.zhangh,g.dianh,g.shuih,g.youzbm,g.chuanz,g.meitly,g.meiz,g.chubnl,");
				sb.append(" g.kaicnl,kaicnx,shengcnl,g.gongynl,g.liux,g.yunsfs,g.shiccgl,g.zhongdht,g.yunsnl,g.heznx,g.rongqgx,");
				sb.append(" g.xiny,g.gongsxz,g.kegywfmz,g.kegywfmzzb,g.shengfb_id,g.shifss,g.shangsdz,g.zicbfb,g.shoumbfb,g.qitbfb,g.beiz ");
				sb.append(" from gonghdwdmsqb g where g.id=");
				sb.append(rsl.getLong("id"));
			}
			sql.append("end;");
			con.getUpdate(sql.toString());
			con.getInsert(sb.toString());
			

			
		}
		
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
		String dianc="";
		int jib=this.getDiancTreeJib();
		if(jib==1){
			dianc="";
		}else if(jib==2){
			dianc= "and (dc.id = " + getTreeid() + " or dc.fuid = "+ getTreeid() + ")";
		}else if(jib==3){
			dianc="and dc.id="+getTreeid();
		}
		
		String chaxun = "select g.id,g.xuh,g.bianm,g.mingc,g.quanc,s.quanc as shengfb_id,\n"
				+ "       cs.mingc as chengsb_id,decode(g.fuid,0,'大供货单位','小供货单位') as leib,\n"
				+ "     g.shenqr,  decode(1,1,'代码批复') as xiug\n"
				+ "from gonghdwdmsqb g ,shengfb s,chengsb cs,gongysdcglb gl,diancxxb dc\n"
				+ "where g.shengfb_id=s.id(+)\n"
				+ "and g.chengsb_id=cs.id(+)\n"
				+ "and g.zhuangt=0\n"
				+ "and gl.gongysb_id=g.id\n" 
				+ "and gl.diancxxb_id=dc.id "+dianc+"\n"
				+ "order by xuh,mingc";



		
		
		
	// System.out.println(chaxun);
	ResultSetList rsl = con.getResultSetList(chaxun);
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("gonghdwdmsqb");
   	
	egu.getColumn("xuh").setHeader("序号");
	egu.getColumn("id").setHidden(true);
	egu.getColumn("id").setEditor(null);
	egu.getColumn("bianm").setHeader("编码");
	egu.getColumn("mingc").setHeader("简称");
	egu.getColumn("quanc").setHeader("全称");
	egu.getColumn("shengfb_id").setHeader("省份");
	egu.getColumn("chengsb_id").setHeader("市区");
	egu.getColumn("leib").setHeader("类别");
	egu.getColumn("shenqr").setHeader("申请人");
	egu.getColumn("xiug").setHeader("");
	
	egu.getColumn("xuh").setWidth(40);
	egu.getColumn("bianm").setWidth(100);
	egu.getColumn("mingc").setWidth(100);
	egu.getColumn("quanc").setWidth(130);
	egu.getColumn("shengfb_id").setWidth(80);
	egu.getColumn("chengsb_id").setWidth(80);
	egu.getColumn("leib").setWidth(80);
	String context = MainGlobal.getHomeContext(this);
	egu.getColumn("xiug")
	.setRenderer(
			"function(value,p,record){return String.format('<a href="+context+"/app?service=page/{1}&id={2}&zhuangt=3>{0}</a>',value,'Pf_Chak',record.data['ID']);}");

	//设定列初始宽度
	
	
	egu.setGridType(ExtGridUtil.Gridstyle_Read);//设定grid不可以编辑
	egu.addPaging(25);//设置分页
	egu.setWidth(1000);//设置页面的宽度,当超过这个宽度时显示滚动条
	
	
	
	//*************************下拉框*****************************************88
	//设置省份的下拉框
	ComboBox cb_shengf=new ComboBox();
	egu.getColumn("shengfb_id").setEditor(cb_shengf);
	cb_shengf.setEditable(true);

	String shengfSql="select id,quanc from shengfb order by quanc";
	egu.getColumn("shengfb_id").setComboEditor(egu.gridId, new IDropDownModel(shengfSql));
	//设置城市下拉框
	ComboBox cb_cs=new ComboBox();
	egu.getColumn("chengsb_id").setEditor(cb_cs);
	cb_cs.setEditable(true);
	egu.getColumn("chengsb_id").editor.setAllowBlank(true);//设置下拉框是否允许为空
	String CsSql="select j.id,j.mingc from chengsb j order by id  ";
	egu.getColumn("chengsb_id").setComboEditor(egu.gridId, new IDropDownModel(CsSql));
	
	
	
	//********************工具栏************************************************
	/*egu.addTbarText("年份:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");//和自动刷新绑定
		comb1.setLazyRender(true);//动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("-");// 设置分隔符
		
		egu.addTbarText("月份:");
		ComboBox comb2=new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");//和自动刷新绑定
		comb2.setLazyRender(true);//动态绑定
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");//设置分隔符
		*/
		//设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		
	
		//设定工具栏下拉框自动刷新
		//egu.addOtherScript("YUEF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// 设置分隔符
		
		
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		egu.addTbarText("-");

		/*
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
	
		egu.addTbarText("-");
		egu.addTbarText("查询条件:");
		TextField tf=new TextField();
		tf.setWidth(130);
		tf.setId("TIAOJ");
		
		
		
		egu.addToolbarItem(tf.getScript());
		//提交
		
		String str=
       		" var url = 'http://'+document.location.host+document.location.pathname;"+
            "var end = url.indexOf(';');"+
			"url = url.substring(0,end);"+
       	    "url = url + '?service=page/' + 'pifu_cx&tiaoj='+TIAOJ.getValue();"+
       	    " window.open(url,'newWin');";
		egu.addToolbarItem("{"+new GridButton("查询","function (){"+str+"}").getScript()+"}");
		
		
		*/
		
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
			
			this.setTreeid(null);
			this.setYuefValue(null);
			this.getYuefModels();
			setTbmsg(null);
		}
		
			getSelectData();
		
		
	}
	
	
	private String Tiaoj;

	public String getTiaoj() {
		return Tiaoj;
	}

	public void setTiaoj(String tiaoj) {
		Tiaoj = tiaoj;
	}

	// 月份
	public boolean Changeyuef = false;

	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue!= null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;
		
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

//	 得到登陆用户所在电厂或者分公司的名称
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return diancmc;

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
