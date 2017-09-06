package com.zhiren.jt.zdt.jiesgl.chengbjk;

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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.jt.zdt.monthreport.tianb.shul.Slbbean;
import com.zhiren.jt.zdt.monthreport.tianb.zhil.Yuezlb;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Jiescbjksz extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
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

//	 private void Save() {
//	 Visit visit = (Visit) this.getPage().getVisit();
//	 visit.getExtGrid1().Save(getChange(), visit);
//	 }
	 private void Save() {
			
			List list=new ArrayList();
			//--------------------------------
			JDBCcon con = new JDBCcon();
			Visit visit = (Visit) this.getPage().getVisit();
			ResultSetList drsl = visit.getExtGrid1().getDeleteResultSet(getChange());
			
			//***********************删除*****************************//
			StringBuffer sql_delete = new StringBuffer("begin \n");
			while (drsl.next()) {
				sql_delete.append("delete from ").append("jiescbjkb").append(
						" where id =").append(drsl.getLong("id")).append(";\n");
			}
			sql_delete.append("end;");
			if(sql_delete.length()>11){
				con.getUpdate(sql_delete.toString());
			}

			//***********************保存*******************************//
			ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
			StringBuffer sql = new StringBuffer();
			String xiaogysbid="";
			while (rsl.next()) {
				sql.delete(0, sql.length());
				sql.append("begin \n");
				long id = 0;
				if(rsl.getString("xiaogysb_id").equals("全部")){
					 xiaogysbid=rsl.getString("gongysb_id");
				}else{
					xiaogysbid=rsl.getString("xiaogysb_id");
				}
				if ("0".equals(rsl.getString("ID"))) {//判断是否是新增数据
					id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
					sql.append("insert into jiescbjkb("
									+ "id,diancxxb_id,gongysb_id,jihkjb_id,jiankmjsx,jiankmjxx,jiankrlsx,jiankrlxx,xiaogysb_id,kaisrq,jiesrq)values("
									+ id
									+ ",(select id from diancxxb where mingc='"
									+rsl.getString("diancxxb_id")
									+"'),(select id from gongysb where mingc='"+rsl.getString("gongysb_id")
									+"'),(select id from jihkjb where mingc='"
									+rsl.getString("jihkjb_id")+"'),"
									+rsl.getDouble("jiankmjsx")+","
									+rsl.getDouble("jiankmjxx")+","
									+rsl.getDouble("jiankrlsx")+","
									+rsl.getDouble("jiankrlxx")
									+",(select id from gongysb where mingc='"+xiaogysbid+"'),to_date('"+rsl.getString("kaisrq")+"','yyyy-mm-dd'),to_date('"+rsl.getString("jiesrq")+"','yyyy-mm-dd'));\n");
				} else {//修改
					sql.append("update jiescbjkb set diancxxb_id=(select id from diancxxb where mingc='"
						 + rsl.getString("diancxxb_id")+"'),gongysb_id=(select id from gongysb where mingc='"
						 + rsl.getString("gongysb_id")+"'),jihkjb_id=(select id from jihkjb where mingc='"
						 + rsl.getString("jihkjb_id")+"'),jiankmjsx="
						 + rsl.getDouble("jiankmjsx")+",jiankmjxx="
						 + rsl.getDouble("jiankmjxx")+",jiankrlsx="
						 + rsl.getDouble("jiankrlsx")+",jiankrlxx="
						 + rsl.getDouble("jiankrlxx")+",xiaogysb_id=(select id from gongysb where mingc='"
						 + xiaogysbid +"'),kaisrq=to_date('"+rsl.getString("kaisrq")+"','yyyy-mm-dd'),jiesrq=to_date('"+rsl.getString("jiesrq")+"','yyyy-mm-dd') where id=" + rsl.getLong("id")+";\n");

				}
				sql.append("end;\n");
				con.getUpdate(sql.toString());	
				
			}
			con.Close();
		}
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}
	public String getDanwmc(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String dwmc="";
		String sql="select mingc from diancxxb where id="+visit.getDiancxxb_id();
		ResultSetList rsld=con.getResultSetList(sql);
		try{
			while(rsld.next()){
				dwmc=rsld.getString("mingc");
			}
		}catch(Exception e){
			e.getStackTrace();
		}finally{
			con.Close();
		}
		return dwmc;
	}
	
	public int getDiancJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;

		String sqlJib = "select d.jib from diancxxb d where d.id="+ ((Visit)getPage().getVisit()).getDiancxxb_id();
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
	
	public void getSelectData() {
		 Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		if(getDiancJib()==1){
			
		}
		String strdiancID = "";
		int jib=this.getDiancJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strdiancID="";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strdiancID = " and d.fuid= " +visit.getDiancxxb_id();
			
		}else if (jib==3){//选电厂只刷新出该电厂
			strdiancID=" and d.id= " +visit.getDiancxxb_id();
		}
		String sql = "select j.id,d.mingc as diancxxb_id,g.mingc as gongysb_id,xg.mingc as xiaogysb_id," 
				+ "	nvl(kaisrq,to_date('"+this.getRiqi()+"','yyyy-mm-dd ')) as  kaisrq," 
				+ " nvl(jiesrq,to_date('"+this.getRiqi()+"','yyyy-mm-dd ')) as  jiesrq,"
				+ " k.mingc as jihkjb_id,j.jiankmjsx,j.jiankmjxx,j.jiankrlsx,j.jiankrlxx \n"
				+ "  from jiescbjkb j,diancxxb d,gongysb g,jihkjb k,gongysb xg\n"
				+ " where j.diancxxb_id=d.id and j.gongysb_id=g.id and j.xiaogysb_id=xg.id and j.jihkjb_id=k.id\n"+strdiancID;

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("jiescbjkb");
//		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setDefaultValue("0");
		egu.getColumn("diancxxb_id").setHeader("单位名称");
//		egu.getColumn("diancxxb_id").setHidden(true);
		//大供货单位
		egu.getColumn("gongysb_id").setHeader("大供应商");
		egu.getColumn("gongysb_id").setWidth(100);
		egu.getColumn("gongysb_id").setEditor(null);
		//小供货单位
		egu.getColumn("xiaogysb_id").setHeader("小供应商");
		egu.getColumn("xiaogysb_id").setWidth(100);
		egu.getColumn("xiaogysb_id").setEditor(null);
		egu.getColumn("kaisrq").setHeader("开始日期");
		egu.getColumn("kaisrq").setWidth(80);
		egu.getColumn("jiesrq").setHeader("结束日期");
		egu.getColumn("jiesrq").setWidth(80);
		egu.getColumn("jihkjb_id").setDefaultValue("");
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("jiankmjsx").setHeader("监控煤价<br>上限");
		egu.getColumn("jiankmjsx").setDefaultValue("0");
		egu.getColumn("jiankmjxx").setHeader("监控煤价<br>下限");
		egu.getColumn("jiankmjxx").setDefaultValue("0");
		egu.getColumn("jiankrlsx").setHeader("监控热量<br>上限");
		egu.getColumn("jiankrlsx").setDefaultValue("0");
		egu.getColumn("jiankrlxx").setHeader("监控热量<br>下限");
		egu.getColumn("jiankrlxx").setDefaultValue("0");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

//		*****************************************设置默认值****************************
		//	电厂下拉框

		if (jib == 1) {// 选集团时刷新出所有的电厂
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (jib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
					new IDropDownModel("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id()+" order by mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (jib == 3) {// 选电厂只刷新出该电厂
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
					new IDropDownModel("select id,mingc from diancxxb where id="+visit.getDiancxxb_id()+" order by mingc"));
			ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+visit.getDiancxxb_id()+" order by mingc");			
			String mingc="";
			if(r.next()){
				mingc=r.getString("mingc");
			}
			egu.getColumn("diancxxb_id").setDefaultValue(mingc);
			egu.getColumn("diancxxb_id").setReturnId(true);
		}		
//		//大供货单位
//		ComboBox cbgys=new ComboBox();
//		egu.getColumn("gongysb_id").setEditor(cbgys);
//		cbgys.setEditable(true);
//		egu.getColumn("gongysb_id").editor.setAllowBlank(true);//设置下拉框是否允许为空
//		String GysSql="select id,mingc from gongysb ";
//		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(GysSql));
//		egu.getColumn("gongysb_id").setReturnId(true);
//		//小供货单位
//		ComboBox xgys=new ComboBox();
//		egu.getColumn("xiaogysb_id").setEditor(xgys);
//		cbgys.setEditable(true);
//		egu.getColumn("xiaogysb_id").editor.setAllowBlank(true);//设置下拉框是否允许为空
//		String XgysSql="select id,mingc from gongysb  order by mingc";
//		egu.getColumn("xiaogysb_id").setComboEditor(egu.gridId, new IDropDownModel(XgysSql));
//		egu.getColumn("xiaogysb_id").setReturnId(true);
		//设置计划口径下拉框
		ComboBox kjcb=new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(kjcb);
		kjcb.setEditable(true);
		egu.getColumn("jihkjb_id").editor.setAllowBlank(true);//设置下拉框是否允许为空
		String JihkjSql="select j.id,j.mingc from jihkjb j order by id  ";
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId, new IDropDownModel(JihkjSql));
		egu.getColumn("jihkjb_id").setReturnId(true);
		
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
				+"row = irow; \n"
				+"if('GONGYSB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
				+"gongysTree_window.show();}});");
		
		setExtGrid(egu);
		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dgys_xgys,"gongysTree"
				,""+visit.getDiancxxb_id(),null,null,null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		StringBuffer handler = new StringBuffer();
		handler.append("function() { \n")
		.append("var cks = gongysTree_treePanel.getSelectionModel().getSelectedNode(); \n")
		.append("if(cks==null){gongysTree_window.hide();returnR;} \n")
		.append("if(cks.getDepth() < 2){ \n")
		.append("Ext.MessageBox.alert('提示信息','请选择对应的小供货单位！');")
		.append("return; } \n")
		.append("rec = gridDiv_grid.getSelectionModel().getSelected(); \n")
		.append("rec.set('GONGYSB_ID', cks.parentNode.text); \n")
		.append("rec.set('XIAOGYSB_ID', cks.text); \n")
		.append("gongysTree_window.hide(); \n")
		.append("return;")
		.append("}");
		ToolbarButton btn = new ToolbarButton(null, "确认", handler.toString());
		bbar.addItem(btn);
		setDefaultTree(dt);
		
		
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
	
	public DefaultTree getDefaultTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}
	
	public void setDefaultTree(DefaultTree dftree1) {
		((Visit) this.getPage().getVisit()).setDefaultTree(dftree1);
	}
	
	public String getTreeScript() {
//		System.out.print(((Visit) this.getPage().getVisit()).getDefaultTree().getScript());
		return getDefaultTree().getScript();
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
			setGongysModels();
			setXiaogysb_idModels();
			setExtGrid(null);
			setDefaultTree(null);
			this.setRiqi(null);
			getSelectData();
		}
	}
//	日期控件
	boolean riqichange=false;
	private String riqi;
	public String getRiqi() {
		if(riqi==null||riqi.equals("")){
			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),0,DateUtil.AddType_intDay));
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		
		if(this.riqi!=null &&!this.riqi.equals(riqi)){
			this.riqi = riqi;
			riqichange=true;
		}
		 
	}
	public String getValueSql(GridColumn gc, String value) {
		if ("string".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return "'" + value + "'";
			}

		} else if ("date".equals(gc.datatype)) {
			return "to_date('" + value + "','yyyy-mm-dd')";
		} else {
			return value;
		}
	}
	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb where fuid=0 or fuid=-1 order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
	}
	public IPropertySelectionModel getXiaogysb_idModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setXiaogysb_idModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setXiaogysb_idModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setXiaogysb_idModels() {
		String sql = "select id,mingc from gongysb where fuid!=0 order by xuh,mingc";
		setXiaogysb_idModel(new IDropDownModel(sql));
	}
}
