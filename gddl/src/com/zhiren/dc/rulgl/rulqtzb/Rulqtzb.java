package com.zhiren.dc.rulgl.rulqtzb;

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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.huaygl.huaybb.feihbgd.Feihbgd;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者:tzf
 * 时间:2009-4-20
 * 修改内容:增加 采样日期  样品来源   机组 字段 
 */
/*
 * 作者:tzf
 * 时间:2009-4-16
 * 内容:入炉其他指标的维护
 */
public class Rulqtzb extends BasePage implements PageValidateListener {
	
	private final static String item_mingc="FH";    //飞灰的编码
	private final static String itemsort_bianm="RLHYQTZB";   //入炉化验其它指标  在itemsort中的编码
	private final static String fhbzitem_bianm="FHBZ";//飞灰班值项目对应的编码
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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
		int a=this.Save1(this.getChange(),visit);
		if(a==-1){
//			this.setMsg("Ext.Msg.alert('提示信息','操作成功!');");
			this.setMsg(MainGlobal.getExtMessageBox("操作失败!",false ));
		}else{
//			this.setMsg("Ext.Msg.alert('提示信息','操作失败!');");
			this.setMsg(MainGlobal.getExtMessageBox("操作成功!",false ));
		}
	}
	
	
	private int Save1(String strchange, Visit visit) {
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		String tableName="rulqtzbb";
		
		ResultSetList delrsl = this.getExtGrid().getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}
		delrsl.close();
		ResultSetList mdrsl =  this.getExtGrid().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(tableName).append("(id");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					if(!mdrsl.getColumnNames()[i].toUpperCase().equals("MINGC")){
						sql.append(",").append(mdrsl.getColumnNames()[i]);
						sql2.append(",").append(
								getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i)));
					}
					
				}
				
				sql.append(" ,diancxxb_id,riq,item_id,leix"); //以所选择的电厂,日期,项目 作为存储的 对象
				sql2.append(" ,").append(this.getTreeid())
				.append(" ,")
				.append(DateUtil.FormatOracleDate(this.getRiqi()))
				.append(",")
				.append(this.getHuaybhValue().getStrId())
				.append(",")
				.append("0");
				sql.append(") values(").append(sql2).append(");\n");
			} else {
				sql.append("update ").append(tableName).append(" set ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					if(!mdrsl.getColumnNames()[i].toUpperCase().equals("MINGC")){
						sql.append(mdrsl.getColumnNames()[i]).append(" = ");
						sql.append(
								getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i))).append(",");
					}
					
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(
						";\n");
			}
		}
		mdrsl.close();
		sql.append("end;"); 
		int flag = con.getUpdate(sql.toString());
		con.Close();
		return flag;
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
		} else if ("float".equals(gc.datatype)) {
			return value == null || "".equals(value) ? "null" : value;
		} else {
			return value;
		}
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _RefreshChick = false;
	
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}else if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		getSelectData();
	}
	
//  判断电厂Tree中所选电厂时候还有子电厂   
    private boolean hasDianc(String id){ 
		JDBCcon con=new JDBCcon();
		boolean mingc= false;
		String sql="select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=true;
		}
		rsl.close();
		return mingc;
	}
    
    private String getItem_mingc(String id){
    	JDBCcon con = new JDBCcon();
    	
    	String s="";
    	String sql=" select bianm from item where id="+id;
    	ResultSetList rsl = con
		.getResultSetList(sql);
    	
    	if(rsl.next()){
    		s=rsl.getString("bianm");
    	}
    	con.Close();
    	return s;
    }
	private boolean isFeiH(){//项目是否是飞灰
		boolean t=false;
		if(this.getItem_mingc(this.getHuaybhValue().getStrId()).equals(item_mingc)){
			t=true;
		}
		return t;
	}
	private StringBuffer getBaseSql(){
		
		StringBuffer bf=new StringBuffer();
		bf.append(" select r.id, it.mingc  \n");
		
		
		if(this.isFeiH()){  //是飞灰  显示班值
			bf.append(",(select it.mingc from item it where it.id=r.banz) banz ");
		}
		bf.append(" ,r.zhi");
		bf.append(",r.caiyrq");
		bf.append(",r.yangply");
		bf.append(" ,j.jizbh jizb_id");
		bf.append(",r.beiz \n");
		bf.append(" from item it,itemsort im ,rulqtzbb r,jizb j  where r.item_id=it.id \n ");
		bf.append(" and it.itemsortid=im.itemsortid \n");
		bf.append(" and it.bianm='"+this.getItem_mingc(this.getHuaybhValue().getStrId())+"' \n");
		bf.append(" and im.bianm='"+itemsort_bianm+"' \n");
		bf.append(" and r.jizb_id=j.id(+) \n");

		
		if(!this.hasDianc(this.getTreeid())){
			bf.append(" and r.diancxxb_id=").append(this.getTreeid()).append(" \n");
		}
		
		bf.append(" and r.riq=").append(DateUtil.FormatOracleDate(this.getRiqi())).append(" \n");
		
		bf.append(" order by id");
		return bf;
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
	
		ResultSetList rsl = con
				.getResultSetList(this.getBaseSql().toString());
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		
		
		// //设置表名称用于保存
		egu.setTableName("rulqtzbb");
		// /设置显示列名称
		egu.getColumn("id").setHidden(true);
		egu.getColumn("mingc").setHeader("项目名");
		egu.getColumn("mingc").setWidth(120);
		egu.getColumn("mingc").setEditor(null);
		
		if(this.isFeiH()){
			egu.getColumn("banz").setHeader("班值");
			egu.getColumn("banz").setWidth(120);
			
			egu.getColumn("banz").setEditor(new ComboBox());
			egu.getColumn("banz").setComboEditor(egu.gridId, new IDropDownModel(" select it.id,it.mingc from item it  where it.itemsortid=(select im.id from itemsort im where im.bianm='"+fhbzitem_bianm+"' )"));
			egu.getColumn("banz").returnId=true;
			egu.getColumn("banz").editor.allowBlank=true;
		}
		
		egu.getColumn("zhi").setHeader("值");
		egu.getColumn("zhi").setWidth(120);
		
		egu.getColumn("caiyrq").setHeader("采样日期");
		egu.getColumn("caiyrq").setDefaultValue(this.getRiqi());
		egu.getColumn("caiyrq").setWidth(120);
		
		egu.getColumn("yangply").setHeader("样品来源");
		egu.getColumn("yangply").setWidth(120);
		
		egu.getColumn("jizb_id").setHeader("机组");
		egu.getColumn("jizb_id").setWidth(120);
		egu.getColumn("jizb_id").setEditor(new ComboBox());
		egu.getColumn("jizb_id").setComboEditor(egu.gridId, new IDropDownModel(" select j.id,j.jizbh  from jizb j"));
		egu.getColumn("jizb_id").returnId=true;
		egu.getColumn("jizb_id").editor.allowBlank=true;
		
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(120);
	
		
		
		// /设置下拉框默认值
	//	egu.addTbarText("-");// 设置分隔符
		// 设置树
		egu.addTbarText("电厂:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符
		
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI","");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("-");
		
		egu.addTbarText("项目：");

		ComboBox comb4 = new ComboBox();
		comb4.setTransform("HuaybhDropDown");
		comb4.setId("Huaybh");
		comb4.setEditable(false);
		comb4.setLazyRender(true);// 动态绑定
		comb4.setWidth(130);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());


		
		egu.addTbarText("-");
		// /设置按钮
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		
		setExtGrid(egu);
		con.Close();
	}

	
	//项目编号

	public IDropDownBean getHuaybhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getHuaybhModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setHuaybhValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setHuaybhModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getHuaybhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getHuaybhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getHuaybhModels() {
		String sql = " select id,mingc from item where itemsortid in (select itemsortid from itemsort where bianm='"+itemsort_bianm+"' and zhuangt=1) order by id";

		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}
	
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
	
	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
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
			

			getHuaybhValue();
			setHuaybhValue(null);
			getHuaybhModels();
			
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		
		getSelectData();
	}
	
	protected void initialize(){
		this.msg="";
	}
}
