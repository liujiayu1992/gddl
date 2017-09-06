package com.zhiren.shanxdted.chelysext;

import java.util.ArrayList;
import java.util.List;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2009-09-09
 * 内容:实现车辆运输单位的维护功能
 */
public class Chelys extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		setMsg(null);
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
		int flag=visit.getExtGrid1().Save(getChange(), visit);
		
		if(flag>=0){
			this.setMsg("数据操作成功!");
		}else{
			this.setMsg("数据操作失败!");
		}
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
		
		if(diancFlag){
			diancFlag=false;
			this.setYunsdwModel(null);
			this.setYunsdwValue(null);
		}
		
		
		if(_Refreshclick){
			_Refreshclick=false;
			
		}
		
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			
		}
		
		getSelectData();
	}

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList(" select c.id,c.diancxxb_id, c.cheph,c.chex, decode(c.tingy,0,'未停运',1,'停运')  tingy,m.mingc meikxxb_id ,y.mingc yunsdwb_id,c.piz \n" +
						" from chelxxb_dt  c,meikxxb m,yunsdwb y \n" +
						" where c.meikxxb_id=m.id(+) \n" +
						" and c.yunsdwb_id=y.id \n" +
						" and c.diancxxb_id="+this.getTreeid()+" \n"+
						" and c.yunsdwb_id="+this.getYunsdwValue().getStrId()+" \n"+
						" order by c.cheph asc,c.chex asc ");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(-1);
		egu.setTableName("chelxxb_dt");
		
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("cheph").setHeader("车号");
		egu.getColumn("chex").setHeader("车型");
		egu.getColumn("tingy").setHeader("停运");
		egu.getColumn("meikxxb_id").setHeader("煤矿");
		egu.getColumn("yunsdwb_id").setHeader("运输单位");
		egu.getColumn("piz").setHeader("自重");
		egu.getColumn("diancxxb_id").setHidden(true);
		
		egu.getColumn("meikxxb_id").setWidth(120);
		egu.getColumn("yunsdwb_id").setWidth(120);
		
		
		ComboBox chex=new ComboBox();
		chex.setAllowBlank(false);
		egu.getColumn("chex").setEditor(chex);
		egu.getColumn("chex").setComboEditor(egu.gridId, new IDropDownModel(" select x.id,x.mingc from xiecfsb x where x.diancxxb_id= "+this.getTreeid()));
		egu.getColumn("chex").setReturnId(false);
		
		
		ComboBox tingy=new ComboBox();
		tingy.setAllowBlank(false);
		egu.getColumn("tingy").setEditor(tingy);
		List list=new ArrayList();
		list.add(new IDropDownBean("0","未停运"));
		list.add(new IDropDownBean("1","停运"));
		egu.getColumn("tingy").setComboEditor(egu.gridId, new IDropDownModel(list));
		egu.getColumn("tingy").setReturnId(true);
		
		ComboBox meik=new ComboBox();
		meik.setAllowBlank(true);
		egu.getColumn("meikxxb_id").setEditor(meik);
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, new IDropDownModel(" select m.id,m.mingc from meikxxb m union select 0 id,'' mingc from dual order by id asc "));
		egu.getColumn("meikxxb_id").setReturnId(true);
		
		ComboBox yunsdw=new ComboBox();
		yunsdw.setAllowBlank(false);
		yunsdw.setListeners("change:function(field,newValue,oldValue){\n" +
				"if(SelectLike.checked){\n" +
				" var _count=gridDiv_ds.getCount();\n" +
				" var rec=gridDiv_sm.getSelected();\n"+
//				"alert(row_num);"+
				"for(i=row_num;i<_count;i++){\n" +
				"gridDiv_ds.getAt(i).set('YUNSDWB_ID',newValue); \n" +
				"" +
				"}\n" +
				
				"" +
				"}\n" +
				"" +
				"}");
		egu.getColumn("yunsdwb_id").setEditor(yunsdw);
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId, new IDropDownModel(" select y.id,y.mingc from yunsdwb y where y.diancxxb_id="+this.getTreeid()+" order by y.id asc "));
		egu.getColumn("yunsdwb_id").setReturnId(true);
		
//		设置树
		egu.addTbarText("电厂:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		egu.addTbarText("-");
		
		
		egu.addTbarText("运输单位:");
		ComboBox comb3 = new ComboBox();
		comb3.setTransform("YUNSDW");
		comb3.setId("YUNSDW");
		comb3.setLazyRender(true);// 动态绑定
		comb3.setWidth(120);
		egu.addToolbarItem(comb3.getScript());
		egu.addTbarText("-");
		
		
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){");
		
		rsb.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		egu.addTbarText("-");
		
		
		String initysdw="";
		if(!this.getYunsdwValue().getStrId().equals("-1")){
			initysdw=this.getYunsdwValue().getValue();
		}
		String in1Str=" Ext.MessageBox.prompt('提示', '请输入添加记录数', function(btn,text){\n" +
				"if(btn=='ok'){\n" +
				
				
				"if(text>0){\n" +
				
				"for(i=0;i<text;i++){\n" +
				"var plant = new gridDiv_plant({ID:'0',DIANCXXB_ID:'"+this.getTreeid()+"',CHEPH:'',CHEX:'',TINGY:'未停运',MEIKXXB_ID:'',YUNSDWB_ID:'"+initysdw+"',PIZ:'0' });\n" +
				" var posi=gridDiv_ds.getCount();\n" +
				"gridDiv_ds.insert(posi,plant);\n" +
				"}\n" +
				
				" }\n"+
				
				
				
				"}\n" +
				
				
				"});" ;
		
		
		GridButton in1=new GridButton("添加","function(){"+in1Str+"}");
		in1.setIcon(SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(in1);
		egu.addTbarText("-");
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("-");
		
		egu.addTbarText("输入车号：");
		TextField theKey = new TextField();
		theKey.setWidth(80);
		theKey.setId("theKey");
		// System.out.println(theKey);
		theKey
				.setListeners("change:function(thi,newva,oldva){  sta='';},specialkey:function(thi,e){if (e.getKey()==13){chaxun();}}\n");
		egu.addToolbarItem(theKey.getScript());

		GridButton chazhao = new GridButton("查找",
				"function(){chaxun();}");
		chazhao.setIcon(SysConstant.Btn_Icon_Search);
		egu.addTbarBtn(chazhao);
		egu.addTbarText("-");
		
		
		Checkbox cbselectlike=new Checkbox();
		
		cbselectlike.setId("SelectLike");
		egu.addToolbarItem(cbselectlike.getScript());
		egu.addTbarText("多行替换");
		
		
		
		
		String color_show = " colorStr='gridDiv_grid.getView().getRow('+row_num+').style.backgroundColor=\"red\";';eval(colorStr);";
		String otherscript = "var sta='';function chaxun(){\n"
			+ "       if(theKey.getValue().toString()==''){Ext.MessageBox.alert('提示','请输入值');return;}\n"
			+"		  gridDiv_grid.getView().refresh();\n"			
			+
			
			"           var rec=gridDiv_ds.getRange();\n "
			+ "           for(var j=0;j<rec.length;j++){\n "
			+ "               if(rec[j].get('CHEPH').toString().indexOf(theKey.getValue().toString())!=-1){\n"
			+ "                 var nw=[rec[j]];\n"
			+ "                  if(sta.indexOf(rec[j].get('ID').toString())==-1){\n"
			+ "                      gridDiv_sm.selectRecords(nw);\n"
			+

			color_show
			
			+ "                      gridDiv_grid.getView().focusRow(row_num);\n"
			
			
			+ "                      sta[sta.length]=rec[j].get('ID').toString();\n"
			+ "						 theKey.focus(true,true);"
			+ "                      sta+=rec[j].get('ID').toString()+';';\n"
			+ "                       return;\n"
			+ "                  }\n" + "                \n"
			+ "               }\n" + "           }\n" +

			"        if(sta==''){\n"
			+ "          Ext.MessageBox.alert('提示','你要找的车号不存在');\n"
			+ "        }else{\n" + "           sta='';\n"
			+ "           Ext.MessageBox.alert('提示','查找已经到结尾');\n"
			+ "         }\n" + "   }\n";
		egu.addOtherScript(otherscript);
	
		egu.addOtherScript("gridDiv_sm.addListener('rowselect',function(sml,rowIndex,record){row_num=rowIndex;});");
		
		setExtGrid(egu);
		con.Close();
	}
	

	
//运输单位--------------------
	
	private IDropDownBean YunsdwValue;

	public IDropDownBean getYunsdwValue() {
		if (YunsdwValue == null) {
			YunsdwValue = (IDropDownBean) getYunsdwModel().getOption(0);
		}
		return YunsdwValue;
	}

	public void setYunsdwValue(IDropDownBean Value) {
		if (!(YunsdwValue == Value)) {
			YunsdwValue = Value;
		//	falg1 = true;
		}
	}

	private IPropertySelectionModel YunsdwModel;

	public void setYunsdwModel(IPropertySelectionModel value) {
		YunsdwModel = value;
	}

	public IPropertySelectionModel getYunsdwModel() {
		if (YunsdwModel == null) {
			getYunsdwModels();
		}
		return YunsdwModel;
	}

	public IPropertySelectionModel getYunsdwModels() {
		
		String sql=" select y.id,y.mingc from yunsdwb y where y.diancxxb_id="+this.getTreeid()+" order by id asc ";
		YunsdwModel = new IDropDownModel(sql,"请选择");
		return YunsdwModel;
	}
	
	
	//------------------------String sql=" select y.id,y.mingc from yunsdwb y where y.diancxxb_id="+this.getTreeid()+" order by id asc ";
//	-----电厂tree
	private String treeid;
	private boolean diancFlag=false;
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
		if(((Visit) getPage().getVisit()).getString2()!=null && !((Visit) getPage().getVisit()).getString2().equals(treeid)){
			diancFlag=true;
		}
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
	
	//--------------------------------
	
	

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
			visit.setString2(null);
			
			this.setTreeid(visit.getDiancxxb_id()+"");
			
			this.setYunsdwModel(null);
			this.setYunsdwValue(null);
			
			
			getSelectData();
		}
	}
}
