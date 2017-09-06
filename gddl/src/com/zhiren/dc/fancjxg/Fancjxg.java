package com.zhiren.dc.fancjxg;

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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.dc.jilgl.shenh.Shujsh;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Fancjxg extends BasePage implements PageValidateListener {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
//	 页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}
	public String getGridScript() {
		return getExtGrid().getGridScript();
	}
	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	

	
	//翻车机下拉框
	private IPropertySelectionModel _fancjModel;
	public void setFancjModel(IDropDownModel value) {
		_fancjModel = value; 
	}
	public IPropertySelectionModel getFancjModel() {
		if (_fancjModel == null) {
			
			String sql=" select rownum id,mingc from (select distinct to_char(riq,'HH24:mi:ss') mingc from fancjghb \n" +
			"where to_date(to_char(riq,'yyyy-MM-dd'),'yyyy-MM-dd')=to_date('"+this.getRiq1()+"','yyyy-MM-dd')) order by mingc desc";
//			System.out.println(sql);
		    _fancjModel = new IDropDownModel(sql);
		}
	    return _fancjModel;
	}
	private IDropDownBean _fancjValue;
	private boolean _fancboo=false;
	public void setFancjValue(IDropDownBean value) {
		
		if(_fancjValue!=null && value!=null && !_fancjValue.getValue().equals(value.getValue())){
			_fancboo=true;
		}
		_fancjValue = value;
	}
	public IDropDownBean getFancjValue() {
		if(_fancjValue==null && this.getFancjModel()!=null && this.getFancjModel().getOptionCount()>0){
			_fancjValue=(IDropDownBean)this.getFancjModel().getOption(0);
		}
		
		return _fancjValue;
	}
	
	
	
	
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}	
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
   public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			
		} else if (_SaveChick) {
			_SaveChick = false;
			save();
			
		} 
		if(riq1Boo){
			riq1Boo=false;
			this.setFancjValue(null);
			this.setFancjModel(null);
		}
		this.getSelectData();
		
		
	}
   
   
	public void save() {
		
		Visit visit=(Visit)this.getPage().getVisit();
		int flag=this.SaveChange(this.getChange(), visit);
		
		if(flag<0){
			this.setMsg("数据操作失败!");
		}else{
			this.setMsg("数据操作成功!");
		}
		
	}
	
	
	public int SaveChange(String strchange, Visit visit) {
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList delrsl = this.getExtGrid().getDeleteResultSet(strchange);
		while (delrsl.next()) {
			
			sql.append("delete from ").append("fancjghb").append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}
		delrsl.close();
		ResultSetList mdrsl = this.getExtGrid().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append("fancjghb").append("(id");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sql.append(",").append(mdrsl.getColumnNames()[i]);
					
					if(mdrsl.getColumnNames()[i].equalsIgnoreCase("RIQ")){
						sql2.append(",").append("to_date("+
								getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i))+",'yyyy-MM-dd HH24:mi:ss')");
					}else{
						
						sql2.append(",").append(
								getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i)));
					}
						
					
				}
				sql.append(") values(").append(sql2).append(");\n");
			} else {
				sql.append("update ").append("fancjghb").append(" set ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sql.append(mdrsl.getColumnNames()[i]).append(" = ");
					
					if(mdrsl.getColumnNames()[i].equalsIgnoreCase("RIQ")){
						sql.append("to_date("+
								getValueSql(this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i))+",'yyyy-MM-dd HH24:mi:ss')").append(",");
					}else{
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
	
	private String getValueSql(GridColumn gc, String value) {
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
	
	
	public void getSelectData() {
		
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String tm="00:00:00";

		if(this.getFancjValue()!=null && !this.getFancjValue().getStrId().equals("-1")){
			tm=this.getFancjValue().getValue();
		}
		String rq2=" to_date('"+this.getRiq1()+" "+tm+"','yyyy-MM-dd HH24:mi:ss')";

		String sql=" select f.id,m.mingc meikxxb_id,p.mingc pinzb_id,f.cheph,to_char(f.riq,'yyyy-MM-dd HH24:mi:ss') riq,f.maoz,f.piz,(f.maoz-f.piz) jingz,f.beiz from fancjghb f,meikxxb m,pinzb p\n" +
				" where f.meikxxb_id=m.id(+) and f.pinzb_id=p.id(+) and f.riq="+rq2+"  order by f.id";
		
//		System.out.println(sql);
		ResultSetList rsl = con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setTableName("fancjghb");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.addPaging(0);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("meikxxb_id").setHeader("煤矿");
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("cheph").setHeader("车皮号");
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("maoz").setHeader("毛重");
		egu.getColumn("piz").setHeader("皮重");
		egu.getColumn("jingz").setHeader("净重");
		egu.getColumn("beiz").setHeader("备注");

		ComboBox mk=new ComboBox();
		mk.setEditable(false);
		
		egu.getColumn("meikxxb_id").setEditor(mk);
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,new IDropDownModel(" " +
				" select m.id,m.mingc from meikxxb m,kuangzglb k where k.meikxxb_id=m.id and k.chezxxb_id<>1 " +
				""));
		
		ComboBox pz=new ComboBox();
		pz.setEditable(false);
		
		egu.getColumn("pinzb_id").setEditor(pz);
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId, new IDropDownModel("select p.id,p.mingc from pinzb p"));
		
		
		NumberField nf=new NumberField();
		nf.setDecimalPrecision(2l);
		nf.setReadOnly(true);
		egu.getColumn("jingz").setEditor(nf);
		
		egu.getColumn("maoz").setDefaultValue("0");
		egu.getColumn("piz").setDefaultValue("0");
		egu.getColumn("jingz").setDefaultValue("0");
		egu.getColumn("jingz").setUpdate(false);
		
		egu.getColumn("riq").setDefaultValue(this.getRiq1()+" "+tm);
	
		egu.addTbarText("翻车:");
		DateField dEnd = new DateField();
		dEnd.setWidth(60);
		dEnd.Binding("RIQ1","");
		dEnd.setValue(getRiq1());
		egu.addToolbarItem(dEnd.getScript());
		
		egu.addTbarText("");
		
		ComboBox fancsj = new ComboBox();
		fancsj.setWidth(70);
		fancsj.setTransform("FancjDropDown");
		fancsj.setId("FancjDropDown");
		fancsj.setLazyRender(true);
		egu.addToolbarItem(fancsj.getScript());
		
		
		egu.addTbarText("-");// 设置分隔符
		
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		
		egu.addTbarText("-");// 设置分隔符
		
		String condi=" if(FancjDropDown.getValue()==null || FancjDropDown.getValue()==''){" +
				"Ext.Msg.alert('提示信息','当前翻车机时间为空，不可添加!');" +
				"return;\n" +
				"}\n";
		egu.addToolbarButton(GridButton.ButtonType_Insert_condition, "", condi);
		
		egu.addTbarText("-");// 设置分隔符
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, "");
		
		
		egu.addTbarText("-");// 设置分隔符
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
	
		egu.addTbarText("-");// 设置分隔符
		
		egu.addOtherScript(" gridDiv_grid.addListener('afteredit',function(e){\n" +
				"if(e.field!='MAOZ' && e.field!='PIZ'){return;}\n" +
				"var maoz=e.record.get('MAOZ'); if(maoz==null || maoz=='') maoz='0';\n" +
				"var piz=e.record.get('PIZ'); if(piz==null || piz=='') piz='0';\n" +
				" var _value=parseFloat(maoz)-parseFloat(piz);\n" +
				"e.record.set('JINGZ',_value);\n" +
				"});\n");
		
		 egu.addTbarText("输入车号：");
		 TextField theKey=new TextField();
		 theKey.setId("theKey");
//		 System.out.println(theKey);
		 theKey.setListeners("change:function(thi,newva,oldva){  sta='';},specialkey:function(thi,e){if (e.getKey()==13){chaxun();}}\n");
		 egu.addToolbarItem(theKey.getScript());
		 
		 	String color_show="";
			String s="";
				String ts=" select * from xitxxb where mingc='翻车机数量修改颜色显示' and zhi='是' and leib='数量' and zhuangt=1 and diancxxb_id="+visit.getDiancxxb_id();
				
				ResultSetList rt=con.getResultSetList(ts);
				
				if(rt.next()){
					egu.addPaging(0);
					s=" gridDiv_ds.reload();";
					color_show=" colorStr='gridDiv_grid.getView().getRow('+row_num+').style.backgroundColor=\"yellow\";';eval(colorStr);";
//					color_show=" gridDiv_grid.getView().addRowClass(row_num,'row_color_show');";		
					egu.addOtherScript("gridDiv_sm.addListener('rowselect',function(sml,rowIndex,record){row_num=rowIndex;});");
					
				}else{
					
				}
		 
		GridButton chazhao=new GridButton("查找","function(){chaxun();}");
		chazhao.setIcon(SysConstant.Btn_Icon_Search);
		egu.addTbarBtn(chazhao);
		
		String otherscript ="var sta='';function chaxun(){\n"+
		"       if(theKey.getValue().toString()==''){Ext.MessageBox.alert('提示','请输入值');return;}\n"+
        "       var len=gridDiv_data.length;\n"+
        "       var count;\n"+
        "       if(len%"+egu.getPagSize()+"!=0){\n"+
        "        count=parseInt(len/"+egu.getPagSize()+")+1;\n"+
        "        }else{\n"+
        "          count=len/"+egu.getPagSize()+";\n"+
        "        }\n"+
        "        for(var i=0;i<count;i++){\n"+
        "           gridDiv_ds.load({params:{start:i*"+egu.getPagSize()+", limit:"+egu.getPagSize()+"}});\n"+
        "           var rec=gridDiv_ds.getRange();\n "+
        "           for(var j=0;j<rec.length;j++){\n "+
        "               if(rec[j].get('CHEPH').toString().indexOf(theKey.getValue().toString())!=-1){\n"+
        "                 var nw=[rec[j]];\n"+
        "                  if(sta.indexOf(rec[j].get('ID').toString())==-1){\n"+
        "                      gridDiv_sm.selectRecords(nw);\n"+
        "                      sta+=rec[j].get('ID').toString()+';';\n"+
        "                      sta[sta.length]=rec[j].get('ID').toString();\n"+
        "						 theKey.focus(true,true);"+
        "                       return;\n"+
        "                  }\n"+
        "                \n"+
        "               }\n"+
        "           }\n"+
        "        }\n"+
        "        if(sta==''){\n"+
        "          Ext.MessageBox.alert('提示','你要找的车号不存在');\n"+
        "        }else{\n"+
        "           sta='';\n"+
        "           Ext.MessageBox.alert('提示','查找已经到结尾');\n"+
        "         }\n"+
        "   }\n";
	
		
		if(!s.equals("")){
			
			otherscript ="var sta='';function chaxun(){\n"+
			"       if(theKey.getValue().toString()==''){Ext.MessageBox.alert('提示','请输入值');return;}\n"+
	       
	        
	        "           var rec=gridDiv_ds.getRange();\n "+
	        "           for(var j=0;j<rec.length;j++){\n "+
	        "               if(rec[j].get('CHEPH').toString().indexOf(theKey.getValue().toString())!=-1){\n"+
	        "                 var nw=[rec[j]];\n"+
	        "                  if(sta.indexOf(rec[j].get('ID').toString())==-1){\n"+
	        "                      gridDiv_sm.selectRecords(nw);\n"+
	        
	        color_show+
	        "                      sta[sta.length]=rec[j].get('ID').toString();\n"+
	        "						 theKey.focus(true,true);"+
	        "                      sta+=rec[j].get('ID').toString()+';';\n"+
	        "                       return;\n"+
	        "                  }\n"+
	        "                \n"+
	        "               }\n"+
	        "           }\n"+
	
	        "        if(sta==''){\n"+
	        "          Ext.MessageBox.alert('提示','你要找的车号不存在');\n"+
	        "        }else{\n"+
	        "           sta='';\n"+
	        "           Ext.MessageBox.alert('提示','查找已经到结尾');\n"+
	        "         }\n"+
	        "   }\n";
		}
		egu.addTbarText("-");// 设置分隔符
		Checkbox cbdhth = new Checkbox();
		cbdhth.setChecked(false);
		cbdhth.setId("duohth");
		egu.addToolbarItem(cbdhth.getScript());
		egu.addTbarText("多行替换");
		egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){			\n" +
			"	if(!duohth.checked){											\n" +
			"		return;														\n" +
			"	}																\n" +
			"	for(i=e.row; i<gridDiv_ds.getCount(); i++){						\n" +
			"		if(e.field=='CHEPH'||e.field=='MAOZ'||e.field=='PIZ'||e.field=='BEIZ'){	\n" +
			"			return;													\n" +
			"		};															\n" +
			"		gridDiv_ds.getAt(i).set(e.field,e.value);					\n" +
			"	}																\n" +
			"});																\n");
	egu.addOtherScript(otherscript);
	
		setExtGrid(egu);
		con.Close();
	}
	
	
	private String checklc="";
	public void setChecklc(String checklc){
		this.checklc=checklc;
	}
	public String getChecklc(){
		
		if(this.checklc==null ||  this.checklc.equals("")){
			return "false";
		}
		return this.checklc;
	}
	
	
	//-----------------------
	private String riq;
	private boolean riqBoo=false;
	public void setRiq(String value) {
		
		if(riq!=null && !riq.equals(value)){
			riqBoo=true;
		}
		riq = value;
	}
	public String getRiq() {
		if ("".equals(riq) || riq == null) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}
	
	//--------------------
	
	private String riq1;
	private boolean riq1Boo=false;
	public void setRiq1(String value) {
		
		if(riq1!=null && !riq1.equals(value)){
			riq1Boo=true;
		}
		riq1 = value;
	}
	public String getRiq1() {
		if ("".equals(riq1) || riq1 == null) {
			riq1 = DateUtil.FormatDate(new Date());
		}
		return riq1;
	}
	
	
	
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			
			this.setRiq1(null);
			
		
			this.setFancjModel(null);
			this.setFancjValue(null);
			
	
			
			this.getSelectData();
		}
		
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
}
