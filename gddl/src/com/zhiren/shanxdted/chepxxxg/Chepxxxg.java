package com.zhiren.shanxdted.chepxxxg;

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
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2009-12-11
 * 内容:加载发货信息，修改煤矿，对应的没有关联供应商，则不操作，给予用户提示信息
 */

/*
 * 修改人：ww
 * 时间：2009-12-26
 * 内容： 添加电厂可修改 (平移三期转二期的数据)
 */
public class Chepxxxg extends BasePage implements PageValidateListener {
	
	public static final String YUNSFS_QY = "QY";// 汽运

	public static final String YUNSFS_HY = "HY";// 火运

	public static final String YUNSFS_All = "ALL";// 全部

	public String getYunsfs(){
		return ((Visit)this.getPage().getVisit()).getString2();
	}
	public void setYunsfs(String yunsfs){
		((Visit)this.getPage().getVisit()).setString2(yunsfs);
	}
	
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
	}

//	 绑定日期
	
	private boolean riqflag1=false;
	public String getRiqi() {
		return ((Visit)this.getPage().getVisit()).getString3();
	}

	public void setRiqi(String riqi) {
		if(((Visit)this.getPage().getVisit()).getString3()==null || !((Visit)this.getPage().getVisit()).getString3().equals(riqi)){
			riqflag1=true;
		}
		((Visit)this.getPage().getVisit()).setString3(riqi);
	}

	private boolean riqflag2=false;
	public String getRiq2() {
		return ((Visit)this.getPage().getVisit()).getString4();
	}

	public void setRiq2(String riq2) {
		if(((Visit)this.getPage().getVisit()).getString4()==null || !((Visit)this.getPage().getVisit()).getString4().equals(riq2)){
			riqflag2=true;
		}
		((Visit)this.getPage().getVisit()).setString4(riq2);
	}

    //页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	
	private String Gysid;
	
	public String getGysid() {
		return Gysid;
	}
	public void setGysid(String gysid) {
		Gysid = gysid;
	}
	
	private String Meikid;
	
	public String getMeikid() {
		return Meikid;
	}
	public void setMeikid(String meikid) {
		Meikid = meikid;
	}

	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _XiugChick = false;
    public void XiugButton(IRequestCycle cycle) {
    	_XiugChick = true;
    }
    
    private boolean _FpXiugChick = false;
    public void FpXiugButton(IRequestCycle cycle) {
    	_FpXiugChick = true;
    }
    
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			
		}
		if(_XiugChick){
			_XiugChick = false;
			GotoShezfa(cycle);
			return ;
		}
		
		if(_FpXiugChick){
			_FpXiugChick = false;
			GotoShezfp(cycle);
			return ;
		}
		
		if(_SaveChick){
			_SaveChick=false;
			Save();
		}
		
		if(riqflag1 || riqflag2){
			riqflag1=false;
			riqflag2=false;
			
			
			setPandValue(null);
			setPandModel(null);
			
			setMeikdqmcValue(null);
			getIMeikdqmcModels();
			
			
		}
		getSelectData();
	}
	
	private void GotoShezfp(IRequestCycle cycle) {
		// TODO 自动生成方法存根
		((Visit) getPage().getVisit()).setString10(this.getParameters());
		cycle.activate("Shulxxxg_dt");
	}
	
	private void Save(){
		
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=null;
		
		ResultSetList mdrsl = this.getExtGrid().getModifyResultSet(this.getChange());
		
		String sql=" begin \n";
		while(mdrsl.next()){
			
			String id=mdrsl.getString("id");
			
			if(id.equals("0")) continue;
			
			String mkid=this.getExtGrid().getColumn("M_MC").combo.getBeanId(mdrsl.getString("M_MC"))+"";
			
			String dcId = this.getExtGrid().getColumn("DIANCXXB_ID").combo.getBeanId(mdrsl.getString("DIANCXXB_ID")) + "";
			
			rsl=con.getResultSetList(" select * from gongysmkglb gm where gm.meikxxb_id="+mkid);
			String gysid="";
			if(rsl.next()){
				gysid=rsl.getString("gongysb_id");
			}else{
				this.setMsg(mdrsl.getString("M_MC")+"：没有对应的供应商，请先配置!");
				return;
			}
			rsl.close();
			
			sql+=" update fahb f set f.gongysb_id="+gysid+" ,f.meikxxb_id="+mkid+",f.diancxxb_id=" + dcId + ",f.fahrq=to_date('" +mdrsl.getString("fahrq")+ "','yyyy-mm-dd') where f.id="+id+";\n";
			sql+=" update jianjghb set meikxxb_id ="+mkid+" where chepb_id in (select id from chepb where fahb_id ="+id+");";
		}
		
		sql+=" end;";
		
		int flag=con.getUpdate(sql);
		
		if(flag>=0){
			this.setMsg("数据操作成功!");
		}else{
			this.setMsg("数据操作失败!");
		}
		
		this.setGysid("0");
		setPandValue(null);
		setPandModel(null);
		
		this.setMeikid("0");
		setMeikdqmcValue(null);
		getIMeikdqmcModels();
		
	}
	private void GotoShezfa(IRequestCycle cycle) {
		Visit visit = (Visit)getPage().getVisit();
		visit.setString1(this.getParameters());
		
//		System.out.print(((Visit) getPage().getVisit()).getString1());
		cycle.activate("ChepxgIndex");
	}
	
	private String Parameters;//记录项目ID

	public String getParameters() {
		
		return Parameters;
	}

	public void setParameters(String value) {
		
		Parameters = value;
	}
	
	private boolean hasDCid(JDBCcon con,String id){
		
		String sql=" select * from diancxxb where fuid="+id;
		
		boolean shifgb=false;
		if(con==null){ 
			con=new JDBCcon();
			shifgb=true;
		}
		
		ResultSetList rsl=con.getResultSetList(sql);
		
		boolean  flag=false;
		if(rsl.next()){
			flag=true;
		}
		
		rsl.close();
		
		if(shifgb){
			con.Close();
		}
		return flag;
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		String ysfs = "";
		if(YUNSFS_QY.equals(getYunsfs())){
			ysfs = " and yunsfsb_id = 2 ";
		}
		if(YUNSFS_HY.equals(getYunsfs())){
			ysfs = " and yunsfsb_id = 1 ";
		}
		
		String dcidStr=" and d.id="+this.getTreeid()+" ";
		if(this.hasDCid(con, this.getTreeid())){
			dcidStr=" and d.fuid="+this.getTreeid()+" ";
		}
		
		String gys_str=" ";
		if(this.getGysid()!=null && !this.getGysid().equals("0")){
			gys_str=" and g.id="+this.getGysid()+" ";
		}
		
		String meik_str=" ";
		if(this.getMeikid()!=null && !this.getMeikid().equals("0")){
			meik_str=" and m.id="+this.getMeikid()+" ";
		}
		
		String sql="select f.id as id,d.mingc diancxxb_id,g.mingc as g_mc,m.mingc as m_mc,p.mingc as p_mc,c.mingc as c_mc,"
						+ " f.fahrq as fahrq,f.daohrq as daohrq,chec,j.mingc as j_mc,biaoz,jingz,ches,y.mingc as y_mc , ys.mingc as ys_mc"
						+ " from fahb f,gongysb g,meikxxb m,pinzb p,chezxxb c,jihkjb j,yunsfsb y, diancxxb d "
						+ " ,yunsdwb ys,(select fahb_id,max(yunsdwb_id) yunsdwb_id from (select fahb_id,yunsdwb_id from chepb where zhongcsj>=to_date('" +getRiqi()+ "','yyyy-mm-dd') and zhongcsj<to_date('" +getRiq2()+ "','yyyy-mm-dd')+1 group by fahb_id,yunsdwb_id) group by fahb_id) cp"
						+ " where g.id = f.gongysb_id and f.diancxxb_id = d.id "
						+ dcidStr+gys_str+meik_str+"\n"
						+ " and m.id = f.meikxxb_id "
						+ " and p.id(+) = f.pinzb_id "
						+ " and c.id(+) = f.faz_id "
						+ " and j.id(+) = f.jihkjb_id "
						+ " and y.id = yunsfsb_id "
						+ " and f.daohrq>=to_date('" +getRiqi()+ "','yyyy-mm-dd') and f.daohrq<to_date('" +getRiq2()+ "','yyyy-mm-dd')+1 "
						+ ysfs+" \n"
						+ "and f.id=cp.fahb_id(+) and cp.yunsdwb_id=ys.id(+)\n"
						+"union \n"
						+" select 0 as id,nvl('合计','') diancxxb_id,'-' as g_mc,'-' as m_mc,'-' as p_mc,'-' as c_mc,"
						+ " null as fahrq,null as daohrq,'-' chec,'-' as j_mc,sum(0) biaoz,0 jingz,0 ches,'-' as y_mc,'-' as ys_mc  "
						+ " from fahb f,gongysb g,meikxxb m,pinzb p,chezxxb c,jihkjb j,yunsfsb y, diancxxb d "
						+ " where g.id = f.gongysb_id and f.diancxxb_id = d.id "
						+ dcidStr+gys_str+meik_str+"\n"
						+ " and m.id = f.meikxxb_id "
						+ " and p.id(+) = f.pinzb_id "
						+ " and c.id(+) = f.faz_id "
						+ " and j.id(+) = f.jihkjb_id "
						+ " and y.id = yunsfsb_id "
						+ " and f.daohrq>=to_date('" +getRiqi()+ "','yyyy-mm-dd') and f.daohrq<to_date('" +getRiq2()+ "','yyyy-mm-dd')+1 "
						+ ysfs
						+ " order by daohrq,diancxxb_id,g_mc,ys_mc";
		
		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl); 
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		
		egu.addPaging(-1);
		
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setWidth(70);
		egu.getColumn("id").editor=null;
		egu.getColumn("id").setHidden(true);
		
		egu.getColumn("diancxxb_id").setHeader("电厂名称");
		egu.getColumn("diancxxb_id").setWidth(90);
//		egu.getColumn("diancxxb_id").editor=null;
		
		egu.getColumn("g_mc").setHeader("供应商");
		egu.getColumn("g_mc").setWidth(70);
		egu.getColumn("g_mc").editor = null;
		egu.getColumn("m_mc").setHeader("煤矿");
		egu.getColumn("m_mc").setWidth(100);
//		egu.getColumn("m_mc").editor = null;
		egu.getColumn("p_mc").setHeader("品种");
		egu.getColumn("p_mc").setWidth(50);
		egu.getColumn("p_mc").editor = null;
		egu.getColumn("c_mc").setHeader("发站");
		egu.getColumn("c_mc").setWidth(50);
		egu.getColumn("c_mc").editor = null;
		egu.getColumn("c_mc").setHidden(true);
		egu.getColumn("fahrq").setHeader("发货日期");
		egu.getColumn("fahrq").setWidth(70);
//		egu.getColumn("fahrq").editor = null;
		egu.getColumn("daohrq").setHeader("到货日期");
		egu.getColumn("daohrq").setWidth(70);
		egu.getColumn("daohrq").editor = null;
		egu.getColumn("chec").setHeader("车次");
		egu.getColumn("chec").setWidth(30);
		egu.getColumn("chec").editor = null;
		egu.getColumn("j_mc").setHeader("计划口径");
		egu.getColumn("j_mc").setWidth(70);
		egu.getColumn("j_mc").editor = null;
		egu.getColumn("biaoz").setHeader("票重");
		egu.getColumn("biaoz").setWidth(70);
		egu.getColumn("biaoz").editor = null;
		egu.getColumn("jingz").setHeader("净重");
		egu.getColumn("jingz").setWidth(70);
		egu.getColumn("jingz").editor = null;
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setWidth(50);
		egu.getColumn("ches").editor = null;
		egu.getColumn("y_mc").setHeader("运输方式");
		egu.getColumn("y_mc").setWidth(50);
		egu.getColumn("y_mc").editor = null;
		
		egu.getColumn("ys_mc").setHeader("运输单位");
		egu.getColumn("ys_mc").setWidth(120);
		egu.getColumn("ys_mc").editor = null;
		
		
		ComboBox dc = new ComboBox();
		egu.getColumn("diancxxb_id").setEditor(dc);
		String dcidStr1=" d.id="+this.getTreeid()+" ";
		if(this.hasDCid(con, this.getTreeid())){
			dcidStr1="  d.fuid="+this.getTreeid()+" ";
		}
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from diancxxb d where " + dcidStr1 + " order by xuh "));
		
		ComboBox mk=new ComboBox();
		egu.getColumn("m_mc").setEditor(mk);
		egu.getColumn("m_mc").setComboEditor(egu.gridId, new IDropDownModel(" select id,mingc from meikxxb "));
		
//		到货日期查询
		egu.addTbarText("到货日期:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setWidth(50);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setWidth(50);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		
		egu.addTbarText("-");
		
		
//		 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		etu.setWidth(50);
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符
		
		

		egu.addTbarText("供应商:");
		ComboBox cobPand = new ComboBox();
		cobPand.setWidth(100);
		cobPand.setTransform("PandDropDown");
		cobPand.setId("PandDropDown");
		cobPand.setLazyRender(true);
		egu.addToolbarItem(cobPand.getScript());
		
		egu.addTbarText("煤矿:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("MeikmcDropDown");
		comb2.setId("gongys");
		comb2.setLazyRender(true);// 动态绑定
		comb2.setWidth(100);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");	
		
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
			
		String str2=
			"   var recs = gridDiv_sm.getSelections(); \n"
	        +"  if(recs!=null && recs.length>0 ){  gridDiv_history='';\n"
	        +"  for(var i=0;i<recs.length;i++){ var rec=recs[i];\n"
	 
	        +"      gridDiv_history += rec.get('ID'); if(i!=recs.length-1) gridDiv_history+=',';\n"
	        +"		}\n"  
	        +"  	document.getElementById('PARAMETERS').value=gridDiv_history; \n"
	        +"      "
	        +"  }else{\n"
	        +"  	Ext.MessageBox.alert('提示信息','请选中一个项目!'); \n"
	        +"  	return;"
	        +"  }"
	        +" document.getElementById('XiugButton').click(); \n";
		
        egu.addToolbarItem("{"+new GridButton("修改","function(){"+str2+"}").getScript()+"}");
        egu.addTbarText("-");// 设置分隔符
        String str3=
			"   var recs = gridDiv_sm.getSelections(); \n"
	        +"  if(recs!=null && recs.length>0 ){  gridDiv_history='';\n"
	        +"  for(var i=0;i<recs.length;i++){ var rec=recs[i];\n"
	        +"      zt = rec.get('ZHUANGT');\n"
	        +"      gridDiv_history += rec.get('ID'); if(i!=recs.length-1) gridDiv_history+=',';\n"
	        +"		}\n"  
	        +"  	document.getElementById('PARAMETERS').value=gridDiv_history; \n"
	        +"      "
	        +"  }else{\n"
	        +"  	Ext.MessageBox.alert('提示信息','请选中一个项目!'); \n"
	        +"  	return;"
	        +"  }"
	        +" document.getElementById('FpXiugButton').click(); \n";
        egu.addToolbarItem("{"+new GridButton("发货分批修改","function(){"+str3+"}").getScript()+"}");
        
        
        egu.addTbarText("-");// 设置分隔符

		Checkbox cbselectlike=new Checkbox();
		cbselectlike.setId("SelectLike");
		egu.addToolbarItem(cbselectlike.getScript());
		egu.addTbarText("多行替换");
		
		
        
        egu.addOtherScript("gongys.on('select',function(){document.all.MEIKID.value=gongys.getValue();document.forms[0].submit();});PandDropDown.on('select',function(){document.all.GYSID.value=PandDropDown.getValue();document.forms[0].submit();});\n");
        egu.addOtherScript("PandDropDown.setValue("+this.getGysid()+");gongys.setValue("+this.getMeikid()+");\n");
		
        egu.addOtherScript("  gridDiv_sm.addListener('beforerowselect',function(model,rowIndex,keepExisting,r){keepExisting=true; if(r.get('ID')=='0'){return false; }     });\n");
        
        egu.addOtherScript(" gridDiv_sm.addListener('selectionchange',function(sm){\n" +
        		
        		" var biaoz=0;var jingz=0; var ches=0;\n"+
        		" for(var i=0;i<sm.getSelections().length;i++){\n" +
        		" var rec=sm.getSelections()[i];\n"+
        		" biaoz+=parseFloat(rec.get('BIAOZ'));\n"+
        		" jingz+=parseFloat(rec.get('JINGZ'));\n"+
        		" ches+=parseFloat(rec.get('CHES'));\n"+
        		"}\n" +
        		" gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('BIAOZ',biaoz);\n"+
        		" gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('JINGZ',jingz);\n"+
        		" gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('CHES',ches);\n"+
        		
        		" } );");
        egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){ if(!SelectLike.checked){return;};for(i=e.row;i<gridDiv_ds.getCount()-1;i++){gridDiv_ds.getAt(i).set(e.field,e.value);}});\n");
        
        
      
        setExtGrid(egu);
		con.Close();

	}
	
	
	// 供应商名称
	
	private IPropertySelectionModel _pandModel;
	
	public void setPandModel(IDropDownModel value) {
		_pandModel = value; 
	}
	
	public IPropertySelectionModel getPandModel() {
		
		if (_pandModel == null) {
			
			String dcidStr=" and d.id="+this.getTreeid()+" ";
			if(this.hasDCid(null, this.getTreeid())){
				dcidStr=" and d.fuid="+this.getTreeid()+" ";
			}
			
			String sql = "";
			sql = " select 0 id ,'全部' mingc from dual union \n select id,mingc from gongysb where id in ( select f.gongysb_id from fahb f,diancxxb d where f.diancxxb_id=d.id \n" +
					" and f.daohrq>=to_date('" +getRiqi()+ "','yyyy-mm-dd') and f.daohrq<to_date('" +getRiq2()+ "','yyyy-mm-dd')+1 \n" +
					dcidStr+") order by id asc";
			
		    _pandModel = new IDropDownModel(sql);
		}
	    return _pandModel;
	}
	
	private IDropDownBean _pandValue;
	
	public void setPandValue(IDropDownBean value) {
		_pandValue = value;
	}
	
	public IDropDownBean getPandValue() {
		
		if(_pandValue==null){
			_pandValue= (IDropDownBean) getPandModel().getOption(0);
		}
		return _pandValue;
	}
	
	//电厂下拉框
	private IDropDownBean _DiancValue;
	
	public IDropDownBean getDiancValue() {
		if (_DiancValue == null) {
			_DiancValue = (IDropDownBean) getIDiancModels().getOption(0);
		}
		return _DiancValue;
	}
	
	public void setDiancValue(IDropDownBean value) {
		_DiancValue=value;
	}
	
	private IDropDownModel _IDiancModel;
	
	public void setIDiancModel(IDropDownModel value) {
		_IMeikdqmcModel = value;
	}

	public IDropDownModel getIDiancModel() {
		if (_IDiancModel == null) {
			getIDiancModels();
		}
		return _IDiancModel;
	}
	
	public IDropDownModel getIDiancModels() {
		JDBCcon con = new JDBCcon();
		try {
			
			String dcidStr=" and d.id="+this.getTreeid()+" ";
			if(this.hasDCid(con, this.getTreeid())){
				dcidStr="  d.fuid="+this.getTreeid()+" ";
			}
			
			String sql = "";
			
			sql = "select id,mingc from diancxxb d where " + dcidStr + " order by xuh ";
			_IDiancModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IDiancModel;
	}
	
	// 煤矿名称
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
			
			String dcidStr=" and d.id="+this.getTreeid()+" ";
			if(this.hasDCid(con, this.getTreeid())){
				dcidStr=" and d.fuid="+this.getTreeid()+" ";
			}
			
			String sql = "";
			sql = " select 0 id ,'全部' mingc from dual union \n select id,mingc from meikxxb where id in ( select f.meikxxb_id from fahb f,diancxxb d where f.diancxxb_id=d.id \n" +
					" and f.daohrq>=to_date('" +getRiqi()+ "','yyyy-mm-dd') and f.daohrq<to_date('" +getRiq2()+ "','yyyy-mm-dd')+1 \n" +
					dcidStr+") order by id asc ";
			_IMeikdqmcModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IMeikdqmcModel;
	}
	

//	树
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
		String reportType = cycle.getRequestContext().getParameter("lx");
		if (reportType != null) {
			setYunsfs(reportType);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			if(!visit.getActivePageName().toString().equals("ChepxgIndex")&&!visit.getActivePageName().toString().equals("Shulxxxg_dt")){
				visit.setString1("");	//向ChepxgIndex里面传fahb_id	
				visit.setString10("");	//向Shulxxxg_dt传发货表id	
				setRiqi(DateUtil.FormatDate(new Date()));
				setRiq2(DateUtil.FormatDate(new Date()));
			}
//			if(!visit.getActivePageName().toString().equals("Shulxxxg_dt")){
//				visit.setString10("");	//向Shulxxxg_dt传发货表id	
//				setRiqi(DateUtil.FormatDate(new Date()));
//				setRiq2(DateUtil.FormatDate(new Date()));
//			}
			visit.setActivePageName(getPageName().toString());
			if(reportType==null){
				setYunsfs(YUNSFS_All);
			}
			
			this.setGysid("0");
			this.setMeikid("0");
			
			setPandModel(null);
			setPandValue(null);
			
			setDiancValue(null);
			setIDiancModel(null);
			
			setMeikdqmcValue(null);
			getIMeikdqmcModels();
			
			
			
			getSelectData();
		}
	}
	
}
