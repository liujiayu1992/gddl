package com.zhiren.pub.jitrbjh;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

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
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public abstract class Jitrbjh extends BasePage implements PageValidateListener {
	//	记录的改变
	private String change;
	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}
	
	//	刷新	
	private boolean _RefurbishClick = false;
    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishClick = true;
    }
    private void Refurbish() {
        //为"刷新"按钮添加处理程序
    }
    
    //保存	
	private boolean _SaveClick = false;
    public void SaveButton(IRequestCycle cycle) {
        _SaveClick = true;
    }
    private void Save() {
        //为"保存"按钮添加处理程序
    	Visit visit = (Visit) this.getPage().getVisit();
    	JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		/**
		 * 删除
		 */
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			if(!(visit.getExtGrid1().mokmc==null)&&!visit.getExtGrid1().mokmc.equals("")){
				String id = delrsl.getString("id");
				//删除时增加日志
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_DEL,visit.getExtGrid1().mokmc,
						"jitrbjhb",id);
			}
			sql.append("delete from jitrbjhb ").append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}
		delrsl.close();
		
		/**
		 * 修改
		 */
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into jitrbjhb (id,xuh,riq,diancxxb_id,jitrbdwb_id,shul,fenl,beiz) values(getnewid("+
						visit.getDiancxxb_id()+
						"),"+Long.parseLong(mdrsl.getString("xuh"))+
						",to_date('"+mdrsl.getString("riq")+"-01-01','YYYY-mm-dd'),"+getTreeid()+
						","+visit.getExtGrid1().getValueSql(visit.getExtGrid1().getColumn("dwMingc"),mdrsl.getString("dwMingc"))+
						","+Float.parseFloat(mdrsl.getString("shul"))+
						","+visit.getExtGrid1().getValueSql(visit.getExtGrid1().getColumn("flMingc"),mdrsl.getString("flMingc"))
						+",'"+mdrsl.getString("beiz")+"');\n");
			} else {
				if(!(visit.getExtGrid1().mokmc==null)&&!visit.getExtGrid1().mokmc.equals("")){
					String id = mdrsl.getString("id");
					//更改时增加日志
					MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
							SysConstant.RizOpType_UP,visit.getExtGrid1().mokmc,
							"jitrbjh",id);
				}
				sql.append("update Jitrbjhb set xuh = "+
						Long.parseLong(mdrsl.getString("xuh"))+
						",riq = to_date('"+mdrsl.getString("riq")+"-01-01','YYYY-mm-dd'),jitrbdwb_id="+
						visit.getExtGrid1().getValueSql(visit.getExtGrid1().getColumn("dwMingc"),mdrsl.getString("dwMingc"))+
						",shul="+Float.parseFloat(mdrsl.getString("shul"))+
						",fenl="+visit.getExtGrid1().getValueSql(visit.getExtGrid1().getColumn("flMingc"),mdrsl.getString("flMingc"))+
						",beiz = '"+mdrsl.getString("beiz")+"' ");
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(
						";\n");
			}
		}
		mdrsl.close();
		sql.append("\n end;");
		con.getUpdate(sql.toString());
		con.Close();
		
		
		
    }
    
    
    //  树ID
	public String getTreeid() {
		if(((Visit) getPage().getVisit()).getString1()==null||((Visit) getPage().getVisit()).getString1().equals("")){
			((Visit) getPage().getVisit()).setString1(String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setTreeid(String treeid) {
		if(!((Visit) getPage().getVisit()).getString1().equals(treeid)){
			((Visit) getPage().getVisit()).setString1(treeid);
		}
	}
    
	//	Grid
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridHtml(){
		return getExtGrid().getHtml();
	}
	
	public String getGridScript(){
		return getExtGrid().getGridScript();
	}
    
	//Tree
	public ExtTreeUtil getExtTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setExtTree(ExtTreeUtil exttree) {
		((Visit) this.getPage().getVisit()).setExtTree1(exttree);
	}

	
	public String getTreeHtml(){
		return getExtTree().getWindowTreeHtml(this);
	}
	
	public String getTreeScript(){
		return getExtTree().getWindowTreeScript();
	}
	
	//	数据提交
	public void submit(IRequestCycle cycle) {
		if(_RefurbishClick){
			_RefurbishClick=false;
			Refurbish();
		}
		if(_SaveClick){
			_SaveClick=false;
			Save();
		}
	}
//	获取数据
	public void getSelectData(){
		getDiancxx();//得到当前电厂的信息
		StringBuffer diancCondition=new StringBuffer("");
		//判断电厂级别以及父类ID，构造sql
		if(getDiancjib()==1){
			diancCondition.append(" and 1=1");
		}else if(getDiancjib()==2){
			diancCondition.append(" and d.id in (select dc.id from diancxxb dc where dc.fuid="+Integer.parseInt(getTreeid())+" and dc.jib=3) ");
		}else if(getDiancjib()==3){
			diancCondition.append(" and d.id ="+Integer.parseInt(getTreeid()));
		}else{
			System.out.println("未知电厂级别");
		}
		String riqCondition=" and to_char(j.riq,'YYYY')="+getRiqValue().getValue();
		String sql="select j.id as id ,j.xuh as xuh , to_char(j.riq,'YYYY') as riq , d.mingc as dcMingc , jdw.mingc as dwMingc , j.shul as shul , i.mingc as flMingc ,j.beiz as beiz\n" +
		"from jitrbjhb j,diancxxb d ,jitrbdwb jdw,item i where j.jitrbdwb_id=jdw.id and d.id=j.diancxxb_id and j.fenl=i.id  "+diancCondition.toString()+riqCondition +"  order by dcMingc desc";
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
		.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("Jitrbjhb");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("riq").setHeader("年份");
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("riq").setDefaultValue(getRiqValue().getValue());
		
		egu.getColumn("dcMingc").setHeader("电厂名称");
		egu.getColumn("dcMingc").setDefaultValue(getDiancmc());
		
		egu.getColumn("dwMingc").setHeader("单位名称");
		egu.getColumn("dwMingc").setWidth(200);
		egu.getColumn("dwMingc").setEditor(new ComboBox());
		egu.getColumn("dwMingc").setComboEditor(egu.gridId,new IDropDownModel(
"select j.id as id ,j.mingc as mingc from jitrbdwb j ,diancgysmykjb dgm ,diancgysglb dg , diancxxb d\n" +
"where dgm.jitrbdwb_id=j.id and dgm.diancgysglb_id=dg.id and dg.diancxxb_id=d.id "+diancCondition.toString()+""));
		
		egu.getColumn("shul").setHeader("数量");
		egu.getColumn("flMingc").setHeader("分类名称");
		egu.getColumn("flMingc").setEditor(new ComboBox());
		egu.getColumn("flMingc").setComboEditor(egu.gridId,new IDropDownModel("select i.id as id , i.mingc as mingc from item i,itemsort items where i.itemsortid=items.id and items.bianm='JTRBHTFL'"));
		
		egu.getColumn("beiz").setHeader("备注");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		
		// 设置树
		egu.addTbarText(Locale.diancxxb_id_fahb);
		
		ExtTreeUtil etu = new ExtTreeUtil("DiancxxTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),"",false);
		setExtTree(etu);//添加至树里
		egu.addTbarTreeBtn("DiancxxTree");//将树添加至grid
		egu.addTbarText("-");// 设置分隔符
		
		ComboBox comb = new ComboBox();
		comb.setTransform("RiqDropDown");
		comb.setId("Riq");
		comb.setEditable(false);
		comb.setLazyRender(true);// 动态绑定
		comb.setWidth(80);
		comb.setReadOnly(true);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("Jieslx.on('select',function(){document.forms[0].submit();});");
		
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addOtherScript(	
				"gridDiv_grid.on('beforeedit',function(e){\n" +
				"   if(e.column==4){\n" + 
				"     e.cancel=true;\n" + 
				"   }\n" + 
				"\n" + 
				"});"
		);
		setExtGrid(egu);
		con.Close();


	}
	
	private int diancjib;
	private int fuid;
	private String diancmc;
	
	public String getDiancmc() {
		return diancmc;
	}

	public void setDiancmc(String diancmc) {
		this.diancmc = diancmc;
	}

	public int getDiancjib() {
		return diancjib;
	}

	public void setDiancjib(int diancjib) {
		this.diancjib = diancjib;
	}

	public int getFuid() {
		return fuid;
	}

	public void setFuid(int fuid) {
		this.fuid = fuid;
	}
/*
	private String getDiancmc(String treeid) {
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
		.getResultSetList("select d.mingc as mingc ,d.jib as jib ,d.fuid as fuid from diancxxb d  where d.id="+Long.parseLong(getTreeid()));
		String diancmc="";
		if(rsl.next()){
			diancmc=rsl.getString("mingc");
			setDiancjib(rsl.getInt("jib"));
		}
		rsl.close();
		con.Close();
		return diancmc;
	}
*/
	
	
	
	private void getDiancxx(){
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
		.getResultSetList("select d.mingc as mingc ,d.jib as jib ,d.fuid as fuid from diancxxb d  where d.id="+Long.parseLong(getTreeid()));
		if(rsl.next()){
			setDiancmc(rsl.getString("mingc"));
			setDiancjib(rsl.getInt("jib"));
			setFuid(rsl.getInt("fuid"));
		}
		rsl.close();
		con.Close();
	}

	//入口
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {//判断当前加载页是不是第一次加载，默认为welcome
			// 在此添加，在页面第一次加载时需要置为空的变量或方法  
			visit.setActivePageName(getPageName().toString());
			//在visit中设置list值为空。
			visit.setList1(null);
			visit.setString1("");//treeID
			setRiqValue(null);
			setRiqModel(null);
		}
		//获取数据
		getSelectData();
	}
	//	验证
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
	
	//combox
	
	public IDropDownBean getRiqValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getRiqModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setRiqValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getRiqModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {

			getRiqModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setRiqModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getRiqModels() {
		
		String sql ="select yvalue,ylabel from nianfb";
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
}
