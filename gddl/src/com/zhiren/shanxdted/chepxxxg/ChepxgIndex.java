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
 * 内容:根据选择的发货信息 加载车皮信息 修改运输单位，实时刷新运输单位下拉框
 */
public class ChepxgIndex extends BasePage implements PageValidateListener {
	
	
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
	
	
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			
		}

		if(_SaveChick){
			_SaveChick=false;
			Save();
		}
		
		if (_ReturnChick) {
			_ReturnChick = false;
			Return(cycle);
		}
		
		
		getSelectData();
	}
	
	private void Return(IRequestCycle cycle){
		cycle.activate("Chepxxxg");
	}
	
	private void Save(){
		
		JDBCcon con=new JDBCcon();
		
		ResultSetList mdrsl = this.getExtGrid().getModifyResultSet(this.getChange());
		
		String sql=" begin \n";
		while(mdrsl.next()){
			
			String id=mdrsl.getString("id");
			
			if(id.equals("0")) continue;
			
			String ysid=this.getExtGrid().getColumn("YUNSDWB_ID").combo.getBeanId(mdrsl.getString("YUNSDWB_ID"))+"";
	
			
			sql+=" update chepb set YUNSDWB_ID="+ysid+" where id="+id+";\n";
			sql+=" update jianjghb set YUNSDWB_ID="+ysid+" where chepb_id="+id+";\n";
		}
		
		sql+=" end;";
		
		int flag=con.getUpdate(sql);
		
		if(flag>=0){
			this.setMsg("数据操作成功!");
		}else{
			this.setMsg("数据操作失败!");
		}
		
		this.setGysid("0");
		setPandModel(null);
		setPandValue(null);
		
	}
	
	
	public void getSelectData() {
		Visit v = ((Visit) this.getPage().getVisit());
		JDBCcon con = new JDBCcon();
	
		
		String gys_str=" ";
		if(this.getGysid()!=null && !this.getGysid().equals("0")){
			gys_str=" and y.id="+this.getGysid()+" ";
		}
		
		String sql=" select c.id,c.cheph,c.biaoz,c.maoz-c.piz-c.zongkd jingz,c.maoz,c.piz,c.zongkd, y.mingc yunsdwb_id,\n" +
				" c.zhongcsj,c.qingcsj from chepb c,yunsdwb y \n" +
				" where c.yunsdwb_id=y.id and c.fahb_id in("+v.getString1()+")"+gys_str+" \n" +
				" union \n" +
				" select 0 id, nvl('合计','') cheph,sum(c.biaoz) biaoz,sum(c.maoz-c.piz-c.zongkd) jingz,sum(c.maoz) maoz,sum(c.piz) piz,sum(c.zongkd) zongkd, '-' yunsdwb_id,\n" +
				" null zhongcsj,null qingcsj from chepb c,yunsdwb y  \n" +
				" where c.yunsdwb_id=y.id and c.fahb_id in ("+v.getString1()+") "+gys_str+" \n" +
				" order by  zhongcsj asc,id desc ";
		
		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl); 
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
//		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		
//		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		
		egu.addPaging(-1);
		
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setWidth(70);
		egu.getColumn("id").editor=null;
		egu.getColumn("id").setHidden(true);
		
		
		egu.getColumn("cheph").setHeader("车号");
		egu.getColumn("cheph").setWidth(70);
		egu.getColumn("cheph").editor = null;
		
		egu.getColumn("biaoz").setHeader("票重");
		egu.getColumn("biaoz").setWidth(100);
		egu.getColumn("biaoz").editor = null;
		
		egu.getColumn("jingz").setHeader("净重");
		egu.getColumn("jingz").setWidth(70);
		egu.getColumn("jingz").editor = null;
		
		egu.getColumn("maoz").setHeader("毛重");
		egu.getColumn("maoz").setWidth(70);
		egu.getColumn("maoz").editor = null;
		
		egu.getColumn("piz").setHeader("皮重");
		egu.getColumn("piz").setWidth(70);
		egu.getColumn("piz").editor = null;
		
		egu.getColumn("zongkd").setHeader("总扣杂");
		egu.getColumn("zongkd").setWidth(70);
		egu.getColumn("zongkd").editor = null;
		
		egu.getColumn("yunsdwb_id").setHeader("运输单位");
		egu.getColumn("yunsdwb_id").setWidth(130);
		
		egu.getColumn("zhongcsj").setHeader("重车时间");
		egu.getColumn("zhongcsj").setWidth(70);
		egu.getColumn("zhongcsj").editor = null;
		
		egu.getColumn("qingcsj").setHeader("轻车时间");
		egu.getColumn("qingcsj").setWidth(70);
		egu.getColumn("qingcsj").editor = null;
		
		
		String Dicid = "";
		Dicid = " where yunsdwb.diancxxb_id in (select diancxxb_id from fahb where id=" + ((Visit)getPage().getVisit()).getString1() + ")";
		ComboBox mk=new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(mk);
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId, new IDropDownModel(" select id,mingc from yunsdwb " + Dicid));		
		

		egu.addTbarText("运输单位:");
		
		ComboBox cobPand = new ComboBox();
		cobPand.setWidth(130);
		cobPand.setTransform("PandDropDown");
		cobPand.setId("PandDropDown");
		cobPand.setLazyRender(true);
		egu.addToolbarItem(cobPand.getScript());
		
		egu.addTbarText("-");

		
		GridButton btnreturn = new GridButton("返回",
		"function (){document.getElementById('ReturnButton').click()}");
		btnreturn.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(btnreturn);
		
		
//		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
//		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
//		egu.addTbarBtn(gbt);
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
        
        egu.addTbarText("-");// 设置分隔符

		Checkbox cbselectlike=new Checkbox();
		cbselectlike.setId("SelectLike");
		egu.addToolbarItem(cbselectlike.getScript());
		egu.addTbarText("多行替换");
		
		
        
        egu.addOtherScript("PandDropDown.on('select',function(){document.all.GYSID.value=PandDropDown.getValue();document.forms[0].submit();});\n");
        egu.addOtherScript("PandDropDown.setValue("+this.getGysid()+");\n");
		
        egu.addOtherScript("  gridDiv_sm.addListener('beforeedit',function(e){if(e.record.get('ID')=='0'){return false; }     });\n");
        
//        egu.addOtherScript(" gridDiv_sm.addListener('selectionchange',function(sm){\n" +
//        		
//        		" var biaoz=0;var jingz=0; var maoz=0; var piz=0; var zongkd=0; \n"+
//        		" for(var i=0;i<sm.getSelections().length;i++){\n" +
//        		" var rec=sm.getSelections()[i];\n"+
//        		" biaoz+=parseFloat(rec.get('BIAOZ'));\n"+
//        		" jingz+=parseFloat(rec.get('JINGZ'));\n"+
//        		" maoz+=parseFloat(rec.get('MAOZ'));\n"+
//        		" piz+=parseFloat(rec.get('PIZ'));\n"+
//        		" zongkd+=parseFloat(rec.get('ZONGKD'));\n"+
//        		"}\n" +
//        		" gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('BIAOZ',biaoz);\n"+
//        		" gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('JINGZ',jingz);\n"+
//        		" gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('MAOZ',maoz);\n"+
//        		" gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('PIZ',piz);\n"+
//        		" gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('ZONGKD',zongkd);\n"+
//        		
//        		" } );");
        
        
        egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){ if(!SelectLike.checked){return;};for(i=e.row;i<gridDiv_ds.getCount()-1;i++){gridDiv_ds.getAt(i).set(e.field,e.value);}});\n");
        
        setExtGrid(egu);
		con.Close();

	}
	
	// 运输单位
	
	private IPropertySelectionModel _pandModel;
	
	public void setPandModel(IDropDownModel value) {
		_pandModel = value; 
	}
	
	public IPropertySelectionModel getPandModel() {
	
		if (_pandModel == null) {
			
			String sql = " select 0 id,'全部' mingc from dual union select id,mingc from yunsdwb \n" +
					"where id in ( select yunsdwb_id from chepb where fahb_id in(" + ((Visit)getPage().getVisit()).getString1() + ") ) \n" +
							" order by id asc ";
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

			visit.setActivePageName(getPageName().toString());
			
			this.setGysid("0");
			
			setPandModel(null);
			setPandValue(null);
			
			getSelectData();
		}
	}
	
}
