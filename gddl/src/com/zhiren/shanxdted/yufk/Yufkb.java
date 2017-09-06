package com.zhiren.shanxdted.yufk;

import java.sql.PreparedStatement;
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

public class Yufkb extends BasePage implements PageValidateListener {
	private String msg = "";
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg =  MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		msg = "";
	}

	// 绑定日期
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}
	boolean riq2change = false;

	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setRiq2(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
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
	
	//页面编号是否已存在
	private boolean getChangeBh(String changebh,StringBuffer sql){
		boolean forchange=false;
		JDBCcon con = new JDBCcon();
		PreparedStatement ps= null;
		ResultSet rs=null;
		
		sql.append("and bianh='"+changebh+"'");
		ps=con.getPresultSet(sql.toString());
		
		try {
			rs = ps.executeQuery();			
			while(rs.next()){
				if(rs!=null){
					forchange=true;			
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
		        ps.close();
		        con.Close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
        
		return forchange;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
	}
	
	public void Save1(String strchange,Visit visit) {
		String tableName="yufkb";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(strchange);
		while(delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =").append(delrsl.getString(0)).append(";\n");
		}
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);

		while(mdrsl.next()) {
		
			StringBuffer sql2 = new StringBuffer();
			StringBuffer sql5 = new StringBuffer();
			StringBuffer sql6 = new StringBuffer();		
			
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if("0".equals(mdrsl.getString("ID"))) {
				
				StringBuffer bsql=new StringBuffer();
				bsql.append("select * from  yufkb where 1=1 ");
				if(getChangeBh(mdrsl.getString("BIANH"),bsql)){
					this.setMsg("编号"+mdrsl.getString("BIANH")+"已存在!");
					return;
				}
				
				sql.append("insert into ").append(tableName).append("(id");

				sql5.append(",yue");//可变
				sql6.append(",").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[5]),mdrsl.getString(5)));				
				for(int i=1;i<mdrsl.getColumnCount();i++) {
					if(!mdrsl.getColumnNames()[i].equals("ZT")){
						sql.append(",").append(mdrsl.getColumnNames()[i]);
					}
					if(!mdrsl.getColumnNames()[i].equals("ZT")){
						sql2.append(",").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i)));}
					}
				sql.append(sql5).append(") values(").append(sql2).append(sql6).append(");\n");
			}else {
				
				StringBuffer bsql=new StringBuffer();
				bsql.append("select * from  yufkb where 1=1 ");
				bsql.append("and id<>'"+mdrsl.getString("ID")+"' ");
				if(getChangeBh(mdrsl.getString("BIANH"),bsql)){
					this.setMsg("编号"+mdrsl.getString("BIANH")+"已存在!");
					return;
				}
				
				sql.append("update ").append(visit.getExtGrid1().tableName).append(" set ");
				for(int i=1;i<mdrsl.getColumnCount();i++) {
					if(!mdrsl.getColumnNames()[i].equals("ZT")){
						sql.append(mdrsl.getColumnNames()[i]).append(" = ");
					}
					if(!mdrsl.getColumnNames()[i].equals("ZT")){
						sql.append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");
					}
				}
				sql.append("yue=").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[5]),mdrsl.getString(5)));
				//sql.deleteCharAt(sql.length()-1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
			}
			
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
	}
	
	private String getValueSql(GridColumn gc, String value) {
		if("string".equals(gc.datatype)) {
			if(gc.combo != null) {
				if(gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				}else {
					return "'"+value+"'";
				}
			}else {
				return "'"+value+"'";
			}
			
		}else if("date".equals(gc.datatype)) {
			return "to_date('"+value+"','yyyy-mm-dd')";
		}else if("float".equals(gc.datatype)){
			if(gc.combo != null) {
				if(gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				}else {
					return "'"+value+"'";
				}
			}
			else {
				return value==null||"".equals(value)?"null":value;
			}
//			return value==null||"".equals(value)?"null":value;
		}else{
			return value;
		}
	}
	
	private boolean _shuaxin = false;
	
	public void ShuaxinButton(IRequestCycle cycle) {
		_shuaxin = true;
	}
		
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_InsertChick) {
			_InsertChick = false;
			
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			
		}
		if (_shuaxin){
			_shuaxin=false;
		}
	}

	public String getGongysxx() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setGongysxx(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		
		String gyssql = "";
		if(getGongysValue().getId()!=-1){
			gyssql = "and y.gongysb_id="+getGongysValue().getId();
		}
		
		String dcsql = "";
		if(getTreeid()=="300"){
			dcsql = " and d.id in(301,302,303) ";
		}
		
		String sql = 
			"select y.id,d.mingc diancxxb_id,y.riq,y.bianh,g.mingc as gongysb_id ,y.jine,y.kaihyh,y.zhangh,\n" +
			"		decode(leib,1,'煤款',2,'运费',3,'杂费',4,'其他')as leib,y.fapbh as fapbh,\n" +
			"		y.jingbr as jingbr,y.beiz,decode(ruzrq,null,'未审核','已审核') zt\n" +
			"  from yufkb y , gongysb g ,diancxxb d\n" +
			" where y.riq>=to_date('"+getRiqi()+"','yyyy-mm-dd')and y.riq<=to_date('"+getRiq2()+"','yyyy-mm-dd')\n" +
			"	and g.id=y.gongysb_id "+gyssql+" and y.diancxxb_id=d.id " + dcsql;
		
		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("yufkb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHeader("厂别");
//		egu.getColumn("diancxxb_id").setDefaultValue(getDcMingc(con, getTreeid()));
//		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("bianh").setHeader("编号");
		egu.getColumn("bianh").setWidth(120);
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("jine").setHeader("金额");
		egu.getColumn("jine").setWidth(80);
		egu.getColumn("jine").setDefaultValue("0");
		egu.getColumn("kaihyh").setHeader("开户银行");	
		egu.getColumn("kaihyh").setWidth(200);
		egu.getColumn("zhangh").setHeader("帐号");
		egu.getColumn("zhangh").setWidth(130);
		egu.getColumn("leib").setHeader("类别");
		egu.getColumn("fapbh").setHeader("发票编号");
		egu.getColumn("fapbh").setWidth(120);
		egu.getColumn("jingbr").setHeader("经办人");
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("zt").setHeader("状态");
		
		egu.getColumn("gongysb_id").setEditor(new ComboBox());
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from gongysb"));
		egu.getColumn("gongysb_id").setReturnId(true); 
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("gongysb_id").setDefaultValue(getGongysValue().getValue());
		egu.getColumn("gongysb_id").setReturnId(true); 
		
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "请选择"));
		l.add(new IDropDownBean(1, "煤款"));
		l.add(new IDropDownBean(2, "运费"));
		l.add(new IDropDownBean(3, "杂费"));
		l.add(new IDropDownBean(4, "其他"));
		egu.getColumn("leib").setEditor(new ComboBox());
		egu.getColumn("leib").setComboEditor(egu.gridId,
				new IDropDownModel(l));
		egu.getColumn("leib").setDefaultValue("请选择");
		egu.getColumn("leib").setReturnId(true);
		egu.getColumn("leib").setWidth(70);

//		egu.getColumn("shoukdwb_id").setEditor(new ComboBox());
//		egu.getColumn("shoukdwb_id").setComboEditor(egu.gridId,
//				new IDropDownModel("select id, mingc from shoukdw"));
//		egu.getColumn("shoukdwb_id").setDefaultValue("请选择");
//		egu.getColumn("shoukdwb_id").setReturnId(true);
		
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from diancxxb"));
		egu.getColumn("diancxxb_id").setDefaultValue("请选择");
		egu.getColumn("diancxxb_id").setReturnId(true);

		egu.getColumn("jingbr").setEditor(null);
		egu.getColumn("jingbr").setDefaultValue(((Visit) getPage().getVisit()).getRenymc());

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);		
//		egu.setWidth(1000);
		egu.setWidth("bodyWidth");
	
		
		egu.addTbarText("起始日期:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());

		egu.addTbarText("截止日期:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		String kaihyh="";
		String zhangh="";
		String gongys="";
		//根据供应商刷新开户银行和账户信息
		if(getGongysValue().getId()>-1){
				ResultSetList rs = con.getResultSetList("select kaihyh,zhangh from gongysb where id="+getGongysValue().getId());
                while(rs.next()){
                   setGongysxx(rs.getString("kaihyh")+","+rs.getString("zhangh"));
                }
                
                kaihyh=getGongysxx().substring(0,this.getGongysxx().lastIndexOf(','));
                zhangh=getGongysxx().substring(getGongysxx().lastIndexOf(',')+1); 
                gongys=getGongysValue().getValue();
		}
		

		// 供货单位
		egu.addTbarText(Locale.gongysb_id_fahb);
		ComboBox comb4 = new ComboBox();
		comb4.setTransform("GongysDropDown");
		comb4.setId("Gongys");
		comb4.setEditable(true);
		comb4.setLazyRender(true);// 动态绑定
		comb4.setWidth(130);
		comb4.setReadOnly(true);
		comb4.setListeners("beforequery:function(e){" +
                "var combo = e.combo;" +
                "if(!e.forceAll){" +
                "var value = e.query;" +
                "combo.store.filterBy(function(record,id){" +
                "var text = record.get(combo.displayField);" +
                "return (text.indexOf(value)!=-1);" +
                "});" +
                "combo.expand();" +
                "return false; " +
                " } " +
                "}");
		egu.addToolbarItem(comb4.getScript());
		egu.addOtherScript("Gongys.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		// 电厂树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		
		StringBuffer sb=new StringBuffer();
		sb.append("{text:' 添加',icon:'imgs/btnicon/insert.gif',cls:'x-btn-text-icon',minWidth:75,handler:function() {	\n")
			.append("if(Gongys.getRawValue()=='请选择'){		\n")
			.append("alert('请选择供应商');	\n")
			.append("return;}	\n")
			
			.append("var plant = new gridDiv_plant({ID: '0',DIANCXXB_ID: '"+getDcMingc(con,getTreeid())+"',RIQ: '"+this.getRiq2()+"',BIANH: '',GONGYSB_ID: '"+gongys+"',JINE: '',KAIHYH: '"+kaihyh+"',ZHANGH: '"+zhangh+"',LEIB: '请选择',FAPBH: '',JINGBR: '"+((Visit) getPage().getVisit()).getRenymc()+"',BEIZ: '',ZT:''});	\n")
			.append("gridDiv_ds.insert(gridDiv_ds.getCount(),plant);	\n}}");
		
		egu.addTbarText("-");
		egu.addToolbarItem("{text:' 刷新',icon:'imgs/btnicon/refurbish.gif',cls:'x-btn-text-icon',minWidth:75,handler:function(){document.getElementById('ShuaxinButton').click();}}");
			
		egu.addToolbarItem(sb.toString());
//		egu.addToolbarButton(GridButton.ButtonType_Insert_condition,"","if(Gongys.getRawValue()=='请选择'){alert('请选择供应商'); return;}");
		egu.addToolbarButton(GridButton.ButtonType_Delete,"");
	    egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton","var Mr = gridDiv_ds.getModifiedRecords();  \n" +
                                                                                "for(i = 0; i< Mr.length; i++){  \n"+
                                                                                "	for(j=i+1; j< Mr.length; j++){  \n"+
	                                                                            "		if(Mr[i].get('BIANH')==Mr[j].get('BIANH')){  \n"+
        	                                                                    "       	Ext.MessageBox.alert('提示信息','编号重复!');  \n"+
        	                                                                    "         	return;"+			
  	                                                                            "       }  \n"+        
                                                                                "   }  \n"+  
                                                                                "}");
	    egu.addOtherScript("gridDiv_grid.on('beforeedit',function(e){if(e.record.get('ZT')=='已审核'){e.cancel=true;}});");
		setExtGrid(egu);
		
		con.Close();
	}
	
//供货单位
	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setGongysValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getGongysModels() {
		
		String  sql ="select gys.id,gys.mingc from gongysdcglb glb,diancxxb dc,gongysb gys\n" +
			"where glb.diancxxb_id=dc.id and gys.leix=1 and gys.zhuangt=1 and glb.gongysb_id=gys.id and dc.id="+((Visit) getPage().getVisit()).getDiancxxb_id()+" order by gys.mingc";
		  
		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
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
//			visit.isFencb();
			((Visit) getPage().getVisit()).setString1("");
//			getFencbValue();
//			setFencbValue(null);			
//			getFencbModels();
			getGongysValue();
			setGongysValue(null);
			getGongysModels();
			
//			getSelectData();
		}
		getSelectData();
	}

	private String treeid = "";

	public String getTreeid() {

		if (treeid == null || treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {

		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
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
	
	private String getDcMingc(JDBCcon con, String id){
		ResultSetList rsl = con.getResultSetList("select mingc from diancxxb where id=" + id);
		if(rsl.next()){
			return rsl.getString("mingc");
		}else{
			return "";
		}
	}
}