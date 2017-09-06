package com.zhiren.shanxdted.baob.kuidkkb;

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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Kuidkkb_wh_yunf extends BasePage implements PageValidateListener {

//	日期
	public String getRiq() {
		if (((Visit) this.getPage().getVisit()).getString1() == null || ((Visit) this.getPage().getVisit()).getString1().equals("")) {
			((Visit) this.getPage().getVisit()).setString1(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -2, DateUtil.AddType_intDay)));
		}
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRiq(String riq) {
		if (((Visit) this.getPage().getVisit()).getString1() != null && !((Visit) this.getPage().getVisit()).getString1().equals(riq)) {
			((Visit) this.getPage().getVisit()).setString1(riq);
		}
	}
	//新添加的日期字段
	public String getRiq2() {
		if (((Visit) this.getPage().getVisit()).getString3() == null || ((Visit) this.getPage().getVisit()).getString3().equals("")) {
			((Visit) this.getPage().getVisit()).setString3(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString3();
	}

	public void setRiq2(String riq2) {
		if (((Visit) this.getPage().getVisit()).getString3() != null && !((Visit) this.getPage().getVisit()).getString3().equals(riq2)) {
			((Visit) this.getPage().getVisit()).setString3(riq2);
		}
	}
	//结束
	protected void initialize() {
		msg = "";
	}

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public void Save() {
		String strchange = getChange();
		JDBCcon con = new JDBCcon();
		String strId="";
		
		StringBuffer sql = new StringBuffer();
		ResultSetList rssb=getExtGrid().getDeleteResultSet(getChange());
		while(rssb.next()){
			String id=rssb.getString("id");
			if(id.equals("")||id==null){
				id="0";
			}
			sql.append("delete kuidkkb_wh_yunf where id ="+id+";\n");
			
		}
		rssb.close();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			if ("".equals(mdrsl.getString("ID"))) {
//				新增id
				strId=MainGlobal.getNewID(con, getChangbValue().getId());
				sql.append("insert into kuidkkb_wh_yunf(ID,DIANCXXB_ID,MEIKXXB_ID,YUNSDWB_ID,DAOHRQ,YUNF, fahb_id) values(\n" +
							strId + ",\n" +
							"(select max(id) from diancxxb where mingc ='"+mdrsl.getString("diancxxb_id")+"')" + ",\n" +
							"(select max(id) from meikxxb where mingc ='" + mdrsl.getString("meikxxb_id") + "')" + ",\n" +
							""+ mdrsl.getString("yunsdwb_id2")+ " ," +
							"to_date('"+mdrsl.getString("daohrq")+"','yyyy-mm-dd') ," +
							""+mdrsl.getString("yunf")+","+
							""+mdrsl.getString("fahb_id")+");\n");
				
			} else {
				sql.append("update kuidkkb_wh_yunf set " +
						   "	yunf="+mdrsl.getDouble("yunf")+" ," +
						   "	diancxxb_id=(select max(id) from diancxxb where mingc ='"+mdrsl.getString("diancxxb_id")+"') ,\n" +
						   "	meikxxb_id=(select max(id) from meikxxb where mingc ='" + mdrsl.getString("meikxxb_id") + "') ,\n" +
						   "	yunsdwb_id="+mdrsl.getString("yunsdwb_id2")+" ,\n" +
						   "	fahb_id="+mdrsl.getString("fahb_id")+" ,\n" +
						   "	daohrq=to_date('"+mdrsl.getString("daohrq")+"','yyyy-mm-dd')\n" +
						   " where id=" + mdrsl.getString("id") + ";\n");
				
				
				
			}
		}
		
		mdrsl.close();
		if(sql.length()>0){
			if(con.getUpdate("begin\n" + sql.toString() + "end;") >= 0){
				//删除没有关联发货的kuidkkb_wh_yunf
				con.getDelete("delete kuidkkb_wh_yunf where daohrq=date'"+this.getRiq()+"' and fahb_id not in (select id from fahb f where f.daohrq=date'"+this.getRiq()+"')");
				setMsg("保存成功！");
			}else {
				setMsg("保存失败！");
			}
		}else {
			setMsg("保存失败！");
		}
		con.Close();
	}
	
	
	

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	
	
	private boolean _CopyClick = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		} else if(_RefreshChick){
			_RefreshChick=false;
			getSelectData();
		}else if (_CopyClick) {
			_CopyClick = false;
			 Copysykj();//锁定数据
			 getSelectData();
		}
	}
	
	
	public void Copysykj(){
		
		
		JDBCcon con = new JDBCcon();
		String updatesql="update kuidkkb_wh w set w.zhuangt=1 where daohrq=date'"+this.getRiq()+"'";
		int falg=con.getUpdate(updatesql);
		if(falg!=-1){
		    	setMsg("数据锁定成功！");
	     }else{
		    	setMsg("数据锁定失败！");
		 }
		con.Close();
		
	}
	
	public void deleteFahb_null_date(JDBCcon con ,String riq){
		//这个方法的作用是删除发货表中没有和车皮表关联的空发货的数据.
		String sql="delete fahb f where f.daohrq=date'"+riq+"' and f.id not in (select distinct fahb_id from chepb c)";
		con.getDelete(sql);
	}
	

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();

		// 日期条件
		String Riq = this.getRiq();
		
		
		deleteFahb_null_date(con,Riq);
		
		String dianctiaoj="";
		if(this.getChangbValue().getId()==300){
			dianctiaoj="  ";
		}else {
			dianctiaoj=" and fh.diancxxb_id="+this.getChangbValue().getId();
		}
		
		
		String sql =
			"select yf.id,fh.id as fahb_id,dc.mingc as diancxxb_id,mk.mingc as meikxxb_id,ysd.mingc as yunsdwb_id,fh.yunsdwb_id as yunsdwb_id2, \n" +
			"fh.fahrq,fh.daohrq,fh.biaoz,fh.jingz,\n" + 
			"decode(yf.id,null,kuidkk_yunf(fh.diancxxb_id,fh.meikxxb_id,fh.yunsdwb_id,to_date('"+Riq+"','yyyy-mm-dd'),'yunf'),yf.yunf) as yunf,\n" + 
			"decode(yf.id,null,'','已保存')as zhuangt\n"+
			"from\n" + 
			"(select * from ( select f.* ,\n" + 
			"    nvl((select (select ys.id  from yunsdwb ys where ys.id=c.yunsdwb_id )\n" + 
			"    from chepb c where c.fahb_id=f.id and rownum=1),0)  yunsdwb_id\n" + 
			"    from fahb f where f.daohrq>=to_date('"+Riq+"','yyyy-mm-dd')\n" + 
			"    and  f.daohrq<=to_date('"+Riq+"','yyyy-mm-dd')\n" + 
			"))fh,meikxxb mk,yunsdwb ysd,diancxxb dc,kuidkkb_wh_yunf yf\n" + 
			" where fh.meikxxb_id=mk.id\n" + 
			" and fh.yunsdwb_id=ysd.id\n" + 
			" and fh.diancxxb_id=dc.id\n" + 
			" and fh.id=yf.fahb_id(+)\n" + 
			""+dianctiaoj+"\n"+
			" order by dc.id,mk.mingc,ysd.mingc,fh.fahrq";


		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("kuidkkb_wh_yunf");
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);//设定grid可以编辑
		//设置多选框
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
//		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		// /设置显示列名称
		
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setEditor(null);
		egu.getColumn("id").setHidden(true);
		
		egu.getColumn("fahb_id").setHeader("fahb_id");
		egu.getColumn("fahb_id").setEditor(null);
		egu.getColumn("fahb_id").setHidden(true);
		
		egu.getColumn("diancxxb_id").setHeader("电厂");
		egu.getColumn("diancxxb_id").setWidth(100);
		egu.getColumn("diancxxb_id").setEditor(null);
		
		egu.getColumn("meikxxb_id").setHeader("煤矿");
		egu.getColumn("meikxxb_id").setWidth(150);
		egu.getColumn("meikxxb_id").setEditor(null);
		
		egu.getColumn("yunsdwb_id").setHeader("运输单位");
		egu.getColumn("yunsdwb_id").setWidth(150);
		egu.getColumn("yunsdwb_id").setEditor(null);
		
		egu.getColumn("yunsdwb_id2").setHeader("运输单位id");
		egu.getColumn("yunsdwb_id2").setHidden(true);
		egu.getColumn("yunsdwb_id2").setEditor(null);
		
		egu.getColumn("biaoz").setHeader("矿发量");
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("biaoz").setEditor(null);
		
		egu.getColumn("jingz").setHeader("入厂量");
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("jingz").setWidth(60);  
		
		egu.getColumn("daohrq").setHeader("到货日期");
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("daohrq").setWidth(90);
		
		egu.getColumn("fahrq").setHeader("发货日期");
		egu.getColumn("fahrq").setEditor(null);
		egu.getColumn("fahrq").setWidth(90);
		
		
		egu.getColumn("yunf").setHeader("运价");
		egu.getColumn("yunf").setWidth(80);
		((NumberField)egu.getColumn("yunf").editor).setDecimalPrecision(2);
		
		egu.getColumn("zhuangt").setHeader("是否已保存");
		egu.getColumn("zhuangt").setEditor(null);
		egu.getColumn("zhuangt").setWidth(80);
		
		
		egu.addPaging(1000);
		
		

		// ********************工具栏************************************************

		egu.addTbarText("到货日期:");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		//df.Binding("RIQ", "");
		df.Binding("RIQ", "forms[0]");
		egu.addToolbarItem(df.getScript());
		
		
		egu.addTbarText("-");
		// 设置树
		//egu.addTbarText(Locale.gongysb_id_fahb);
		String condition=" and daohrq=to_date('"+Riq+"','yyyy-MM-dd') \n " ;		
		ExtTreeUtil etu = new ExtTreeUtil("gongysTree",
				ExtTreeUtil.treeWindowCheck_gongys, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),condition);
		setTree(etu);
		//egu.addTbarTreeBtn("gongysTree");
		
//		分厂别
		egu.addTbarText("-");
		egu.addTbarText("单位:");
		ComboBox comb = new ComboBox();
		comb.setTransform("ChangbDropDown");
		comb.setId("Changb");
		comb.setEditable(false);
		comb.setLazyRender(true);// 动态绑定
		comb.setWidth(100);
		comb.setReadOnly(true);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("Changb.on('select',function(){document.forms[0].submit();});");
		
	
		
		

//		设置Toolbar按钮	
		egu.addTbarText("-");
		GridButton gRefresh = new GridButton("刷新",
				"function(){document.getElementById('RefreshButton').click();}");
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		
		
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		
		
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_SaveAll, "SaveButton");
			
		egu.addTbarText("-");
		egu.addTbarText("-");
			
		
		
		
		
		
		setExtGrid(egu);
		con.Close();
	}
	
	public boolean getShujzt(JDBCcon con){
		boolean isShenh=false;
		String sql="select zhuangt from kuidkkb_wh where daohrq=date'"+this.getRiq()+"' order by zhuangt desc ";
		ResultSetList rs2=con.getResultSetList(sql);
		if(rs2.next()){
			if(rs2.getInt("zhuangt")==1){
				isShenh=true;
			}
		}
		return isShenh;
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
			init();
		}
		getSelectData();
	}
	
	private void init() {
		
		((Visit) getPage().getVisit()).setString1("");			//riq1
		((Visit) getPage().getVisit()).setString3("");			//riq2
		((Visit) getPage().getVisit()).setString2("");			//Treeid
		((Visit) getPage().getVisit()).setboolean1(false);		//厂别
		this.setChangbModel(null);		//IPropertySelectionModel1
		this.setChangbValue(null);		//IDropDownBean1
		this.getChangbModels();
		
		this.setTree(null);
		this.setTreeid("0");
	}
	
//	厂别
	public IDropDownBean getChangbValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getChangbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}
	public void setChangbValue(IDropDownBean Value) {
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean1()){
			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}
	public IPropertySelectionModel getChangbModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getChangbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}
	public IPropertySelectionModel getChangbModels() {
		String sql ="select id,mingc from diancxxb d order by id";
		((Visit) getPage().getVisit())
		.setProSelectionModel1(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
//	厂别_End
	
	public String getTreeid() {
		if(((Visit) getPage().getVisit()).getString2()==null||((Visit) getPage().getVisit()).getString2().equals("")){
			((Visit) getPage().getVisit()).setString2("0");
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		if(!((Visit) getPage().getVisit()).getString2().equals(treeid)){
			((Visit) getPage().getVisit()).setString2(treeid);
			((Visit) getPage().getVisit()).setboolean2(true);
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
}
