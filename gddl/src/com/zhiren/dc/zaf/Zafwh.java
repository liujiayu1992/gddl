package com.zhiren.dc.zaf;

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
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：夏峥
 * 时间：2012-02-07
 * 描述: 增加提交后不可修改的功能，并且名称字段不能为空
 */
/*
 * 作者：夏峥
 * 时间：2012-02-17
 * 描述:界面打开时，如果杂费合计表中没有当月的信息系统将不能填报。
 * 当用户点击保存时，系统将自动计算前台合计值是否与杂费合计表中的本月信息相等。
 * 如果数据不相等，系统将给出具体提示（明细合计信息与杂费合计表中信息的比值，
 * 并提示用户是否以明细数据为主。如果用户点击‘是’，
 * 系统将保存明细信息并将明细信息的合计值更新至该单位对应月份的合计表中。
 * 如果用户点击‘否’，系统将返回填报界面并不做任何操作）
 */
/*
 * 作者：夏峥
 * 时间：2012-02-20
 * 描述:杂费填报时在明细界面汇总行先录入本月杂费合计值，然后再填写明细信息。
 * 		在保存时，如果明细信息的合计值不等于先前填报的合计值则提示用户明细合计值是否正确，
 * 		如果正确则允许用户进行保存操作，否则返回至填报界面并让用户继续修改原有数据。
 */
/*
 * 作者：夏峥
 * 时间：2012-05-02
 * 描述: 复制上月数据时，应默认状态为0
 */
/*
 * 作者：夏峥
 * 时间：2012-08-16
 * 描述: 杂费名称从ITEM表中获取，但在保存时仍按照文字保存。
 */
public  class Zafwh extends BasePage implements PageValidateListener {
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
	
	private void copy(){
		JDBCcon con = new JDBCcon();
		String strDate = getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01";
		String sql = "";
//		//删除本月数据
//		sql = "delete zafb where riq="+DateUtil.FormatOracleDate(strDate)+" and diancxxb_id=" + getTreeid();
//		int flag = con.getDelete(sql);
//		if(flag==-1)setMsg("本月数据删除失败");
		//复制上月数据
		String strDate_sy = DateUtil.FormatDate(DateUtil.AddDate(DateUtil.FormatDateTime(DateUtil.getDate(strDate)), -1, DateUtil.AddType_intMonth));
		sql = "insert into zafb (select getnewid("+getTreeid()+"),"+getTreeid()+",mingc,0 as jine,beiz,0 as zhuangt ,add_months(riq,1) from zafb where riq="+DateUtil.FormatOracleDate(strDate_sy)+" and diancxxb_id="+getTreeid()+")";
		int flag = con.getInsert(sql);
		if(flag==-1)
			setMsg("复制上月数据失败");
		else
			setMsg("复制上月数据已完成");
	}

	private void Save()	{
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		int flag=-1;
		ResultSetList drsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		StringBuffer sql_delete = new StringBuffer("begin \n");
		while (drsl.next()) {
			long id=drsl.getLong("id");
			////删除数据
			sql_delete.append("delete   ").append("zafb").append(
			" where id = ").append(id).append(";\n");
		}

		sql_delete.append("end;");
		if(sql_delete.length()>11){
			flag=con.getUpdate(sql_delete.toString());
		}

//		保存更新或新增 
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		StringBuffer sql = new StringBuffer();
		String	strriq="date'"+getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01'";
		sql.append("begin \n");
		while (rsl.next()) {
			String ID_1=rsl.getString("ID");
			String mingc=rsl.getString("mingc");
			double jine=rsl.getDouble("jine");
			String beiz=rsl.getString("beiz");
			if("-1".equals(ID_1)){
				continue;
			}
			if ("0".equals(ID_1)||"".equals(ID_1)) {
				//插入数据
				sql.append("insert into zafb("
						+ "id,riq,diancxxb_id,mingc,jine,beiz)values("
						+ "getnewid("
						+this.getTreeid()
						+")," 
						+strriq+","
						+this.getTreeid()+",'"
						+ mingc+ "',"
						+ jine + ",'"
						+ beiz + "');");		

			}else {	
				//修改数据
				sql.append("update zafb set mingc='"+ mingc+"',jine="+ jine+",beiz='"+ beiz+ "'" +
						"  where id=" + rsl.getLong("id")+";\n");
			}
		}
		sql.append("end;");
		if(sql.length()>11){
			flag=con.getUpdate(sql.toString());
		}
		
		if (flag>-1){
			setMsg("保存成功！");
			con.commit();
		}else{
			setMsg("保存失败！");
		}
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _CopyChick = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if(_RefurbishChick){
			_RefurbishChick=false;
			getSelectData();
		}
		if(_CopyChick){
			_CopyChick=false;
			copy();
			getSelectData();
		}
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		// 工具栏的年份下拉框
		long intyear;
	
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
//		 工具栏的月份下拉框
		long intmonth;
		if (getYuefValue()==null){
			intmonth =DateUtil.getMonth(new Date());
		}else{
			intmonth= getYuefValue().getId();
		}
		
		String strdiancTreeID = " and dc.id= " +this.getTreeid();
		
		String chaxun ="select z.id,dc.mingc diancxxb_id,z.mingc,z.jine,z.beiz from zafb  z  ," +
				"diancxxb dc where z.diancxxb_id=dc.id and riq=date'"+intyear+"-"+intmonth+"-01' "+
		               "\n   "+strdiancTreeID;
	    chaxun = 
	    	"select -1 id,'' diancxxb_id,'合计' mingc,nvl(sum(jine),0) jine,'' beiz from zafb z, diancxxb dc\n" +
	    	"where z.diancxxb_id = dc.id\n" + 
	    	"   and riq = date '"+intyear+"-"+intmonth+"-01'\n" + 
	    	"   "+strdiancTreeID+"\n" + 
	    	"union all\n" +
	    	chaxun;
		
//		System.out.println(chaxun);
		//System.out.println("----------------------------------------");
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("zafb");
		egu.setWidth("bodyWidth");
		egu.getColumn("diancxxb_id").setHeader("电厂名称");
		egu.getColumn("diancxxb_id").setDefaultValue(getIDropDownDiancmc(this.getTreeid()));
		egu.getColumn("diancxxb_id").setWidth(100);
		egu.getColumn("diancxxb_id").setEditor(null);
		
		egu.getColumn("mingc").setHeader("杂费名称");
		egu.getColumn("mingc").editor.setAllowBlank(false);
		egu.getColumn("mingc").setWidth(200);
//		杂费类型下拉框
		String cbosql="SELECT ID, MINGC\n" +
			"  FROM ITEM\n" + 
			" WHERE ITEMSORTID IN (SELECT ID FROM ITEMSORT WHERE BIANM = 'ZAFMC')\n" + 
			" ORDER BY XUH";
		egu.getColumn("mingc").setEditor(new ComboBox());
		egu.getColumn("mingc").setComboEditor(egu.gridId, new IDropDownModel(cbosql));
		egu.getColumn("mingc").setReturnId(false);//绑定了，页面显示汉字，库里显示数字。
		
		
		egu.getColumn("jine").setHeader("当月金额");
//		设定小数位数
		((NumberField)egu.getColumn("jine").editor).setDecimalPrecision(2);
		egu.getColumn("jine").setDefaultValue("0");
		egu.getColumn("jine").editor.setAllowBlank(false);
		egu.getColumn("jine").setWidth(100);
		
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(100);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(23);// 设置分页

	
		// ********************工具栏************************************************
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");
		egu.addTbarText("月份:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");// 和自动刷新绑定
		comb2.setLazyRender(true);// 动态绑定
		comb2.setWidth(60);
		egu.addToolbarItem(comb2.getScript());
		
		// 设置树
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), this.getTreeid());
		setTree(etu);
		egu.addTbarText("-");// 设置分隔符
		
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");
		
		egu.addTbarText("-");
		egu.addToolbarButton("刷新", GridButton.ButtonType_Refresh, "RefurbishButton");
		
//		分厂将不显示增删改按钮
		if(visit.isFencb() && isParentDc(con) && MainGlobal.getXitxx_item("月报", "杂费分厂别总厂显示按钮", this.getTreeid(), "否").equals("否")){
			
		}else{
//			判断数据是否已经上传 如果已上传 则不能修改 删除 保存操作
			if(getZhangt(con)){
				setMsg("数据已经上传，请先联系上级单位回退之后才能操作！");
			}else{
				egu.addToolbarButton(GridButton.ButtonType_Insert, null);
				String delscript="{"+
					new GridButton("删除","function() {for(i=0;i<gridDiv_sm.getSelections().length;i++){\n" +
					"record = gridDiv_sm.getSelections()[i]; if(record.get('ID')=='-1'){continue;}\n"+
					"gridDiv_history += '<result>' + '<sign>D</sign>' + '<ID update=\"true\">' \n" +
					"+ record.get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'\n"+
					"+ '</result>' ;\n"+
					"gridDiv_ds.remove(gridDiv_sm.getSelections()[i--]);\n",SysConstant.Btn_Icon_Delete).getScript()+
					"}}}\n";
				egu.addToolbarItem(delscript);
				
				String savescrpit="function(){"+
					 "var gridDivsave_history = '';\n" +
					 "var Mrcd = gridDiv_ds.getModifiedRecords();\n"+
					 "for(i = 0; i< Mrcd.length; i++){ "+
					 "if(typeof(gridDiv_save)=='function'){ var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}}\n"+
					 "if(Mrcd[i].get('MINGC') == ''){Ext.MessageBox.alert('提示信息','字段 杂费名称 不能为空');return;\n"+
					 "}if(Mrcd[i].get('JINE') < -100000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段当月金额 低于最小值 -100000000');return;\n"+
					 "}if( Mrcd[i].get('JINE') >100000000000){Ext.MessageBox.alert('提示信息',' 第 '+(i+1)+'行,字段 当月金额 高于最大值 100000000000');return;\n"+
					 "}if(Mrcd[i].get('JINE')!=0 && Mrcd[i].get('JINE') == ''){Ext.MessageBox.alert('提示信息','字段 当月金额 不能为空');return;\n"+
					 "}gridDivsave_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'\n"+
					 "+ '<DIANCXXB_ID update=\"true\">' + Mrcd[i].get('DIANCXXB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</DIANCXXB_ID>'\n"+
					 "+ '<MINGC update=\"true\">' + Mrcd[i].get('MINGC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MINGC>'\n"+
					 "+ '<JINE update=\"true\">' + Mrcd[i].get('JINE')+ '</JINE>'\n"+
					 "+ '<BEIZ update=\"true\">' + Mrcd[i].get('BEIZ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BEIZ>'\n"+
					 " + '</result>' ; }\n"+
					 "if(gridDiv_history=='' && gridDivsave_history==''){\n"+ 
					 "Ext.MessageBox.alert('提示信息','没有进行改动无需保存');\n"+
					 "}else{\n"+
					"	var jine=0;\n" +
					"	var rec=gridDiv_ds.getRange();\n" +
					"	for(i = 1; i< rec.length; i++){\n" +
					"		jine+=eval(rec[i].get('JINE'));\n" +
					"	}\n" +
					 "var zafhjje=eval(rec[0].get('JINE'));\n"+
					 "var zafmxje=eval(jine);\n"+
					 "var strmsg='';\n"+
					 "if(zafhjje-zafmxje!=0){\n"+
					 "strmsg='杂费合计金额：'+zafhjje+'<br> 杂费明细合计金额：'+zafmxje+'<br><br>相差：'+(zafhjje-zafmxje)+'<br>合计与明细合计金额不符，<br>是否以明细合计金额为准？';\n"+
					 "Ext.Msg.confirm('提示信息',strmsg,function(btn){\n"+
					 "	if(btn=='yes'){\n"+
					 "		var Cobj = document.getElementById('CHANGE');\n"+
					 "		Cobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';\n"+
					 "		document.getElementById('SaveButton').click();\n"+
					 "		Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n"+
					 "	}\n"+
					 "	if(btn=='no'){\n"+
					 "		return;\n"+
					 "	}\n"+
					 "});\n"+
					 "}else{\n"+
					 "var Cobj = document.getElementById('CHANGE');\n"+
					 "Cobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';\n"+
					 "document.getElementById('SaveButton').click();\n"+
					 "Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n"+
					 "}\n"+
					 "}"+
					 "}";
//					
				GridButton gbt = new GridButton("保存",savescrpit);
				gbt.setIcon(SysConstant.Btn_Icon_Save);
				egu.addTbarBtn(gbt);
				
				String copyscript = "{"+ //复制按钮
			    new GridButton("复制","function(){document.getElementById('CopyButton').click();\n"
			    		,SysConstant.Btn_Icon_Copy).getScript()+  "}}";
				egu.addToolbarItem(copyscript);
			}
		}
		
		String script = 
			"gridDiv_grid.on('beforeedit',function(e){\n" +
			"    if(e.record.get('ID')=='-1' && e.field!='JINE'){e.cancel=true;}\n" + 
			"});\n";
		
		script+="gridDiv_grid.on('afteredit',function(e){\n" +
		"  if(e.field=='JINE'){\n" +
		"	if(e.record.get('JINE')==''){e.record.set('JINE',0);}\n" + 
		"  }\n" + 
		"});\n";
		egu.addOtherScript(script);

		setExtGrid(egu);
		con.Close();
		
	}

	private boolean getZhangt(JDBCcon con){
// 		工具栏的年份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
//		 工具栏的月份下拉框
		long intmonth;
		if (getYuefValue()==null){
			intmonth =DateUtil.getMonth(new Date());
		}else{
			intmonth= getYuefValue().getId();
		}
		String CurrODate =  DateUtil.FormatOracleDate(intyear+"-"+intmonth+"-01");
		
		String sql=
			"select zhuangt\n" +
			"  from zafb\n" + 
			" where diancxxb_id = "+getTreeid()+"\n" + 
			"   and riq = "+CurrODate;
		ResultSetList rs=con.getResultSetList(sql);
		boolean zt=true;
		if(con.getHasIt(sql)){
			while(rs.next()){
				if(rs.getInt("zhuangt")==0||rs.getInt("zhuangt")==2){
					zt=false;
				}
			}
		}else{
			zt=false;
		}
		return zt;
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
			this.setNianfValue(null);
			this.getNianfModels();
			this.setYuefValue(null);
			this.getYuefModels();
			this.setTreeid(null);
			setTbmsg(null); 
			getSelectData();
		}
		getSelectData();
	}
//	 年份
	private static IPropertySelectionModel _NianfModel;
	
	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
//				判断如果当前是1月份，年份为去年
				int year = DateUtil.getYear(new Date());
				if(DateUtil.getMonth(new Date())==1){
					year -= 1;
				}
//				-------
				if (year == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2010; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	/**
	 * 月份
	 */
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
	        int _yuef = DateUtil.getMonth(new Date());
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
	        for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
	            Object obj = getYuefModel().getOption(i);
	            if (_yuef == ((IDropDownBean) obj).getId()) {
	                _YuefValue = (IDropDownBean) obj;
	                break;
	            }
	        }
	    }
	    return _YuefValue;
	}
	 
	public boolean yuefchanged = false;
	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			yuefchanged = true;
		}
		_YuefValue = Value;
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
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
	
//	private String treeid;
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
	
	private boolean isParentDc(JDBCcon con){
		String sql = "select * from diancxxb where fuid = " + this.getTreeid();
		return con.getHasIt(sql);
	}
	
}