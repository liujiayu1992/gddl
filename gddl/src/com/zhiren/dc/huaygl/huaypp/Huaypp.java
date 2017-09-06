package com.zhiren.dc.huaygl.huaypp;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Huaypp extends BasePage implements PageValidateListener {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}

//	绑定日期
	public String getBeginRiq() {
		return ((Visit) getPage().getVisit()).getString3();
	}
	public void setBeginRiq(String riq) {
		((Visit) getPage().getVisit()).setString3(riq);
	}
	
	public String getEndRiq() {
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setEndRiq(String riq) {
		((Visit) getPage().getVisit()).setString2(riq);
	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _ZuofChick = false;

	public void ZuofButton(IRequestCycle cycle) {
		_ZuofChick = true;
	}
	
	private void Save() {
		StringBuffer sql = new StringBuffer();
		sql.append("begin\n");

		String tmpSQL = "";
		ResultSetList rs = null;
		int flag = 0;
		if(getChange()==null || "".equals(getChange())) {
			setMsg("请选择一行数据进行查看！");
			return;
		}
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Caizhbmxg.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while(rsl.next()){
			long hytmpID = getExtGrid().getColumn("linsbm").combo.getBeanId(rsl.getString("linsbm"));
			if(hytmpID==-1){				
//              StringBuffer bianhsql=new StringBuffer();
//				bianhsql.append("select bianm from zhuanmb where zhuanmlb_id=(select id from zhuanmlb where jib = (select nvl(min(jib),0) from zhuanmlb)) \n");
//				bianhsql.append("and id=(select zhuanmb.id from zhillsb,zhuanmb,zhuanmlb where 1=1 and zhillsb.id=zhuanmb.zhillsb_id and zhuanmb.zhuanmlb_id=zhuanmlb.id \n");
//				bianhsql.append("and zhillsb.id=").append(rsl.getString("id")).append(" \n");
//				bianhsql.append("and zhuanmlb_id=(select id from zhuanmlb where jib = (select nvl(min(jib),0) from zhuanmlb))) \n");
//				ResultSetList bianmrsl=con.getResultSetList(bianhsql.toString());
//				if(bianmrsl.next()){
				sql.append("update zhillsb set ");
				sql.append("huaysj=to_date('").append(DateUtil.DATE_FORMAT.format(new Date())).append("','yyyy-mm-dd'),\n");
				sql.append("mt=0,\n");
				sql.append("mad=0,\n");
				sql.append("stad=0,\n");
				sql.append("std=0,\n");
//				sql.append("star=").append(rs.getString("st_ar")).append(",\n");
				sql.append("had=0,\n");
				sql.append("har=0,\n");
				sql.append("aad=0,\n");
				sql.append("ad=0,\n");
				sql.append("aar=0,\n");
				sql.append("vad=0,\n");
				sql.append("vdaf=0,\n");
				sql.append("qbad=0,\n");
				sql.append("qgrad=0,\n");
				sql.append("qgrd=0,\n");
				sql.append("qgrad_daf=0,\n");
				sql.append("qnet_ar=0,\n");
				sql.append("shenhzt=0,\n");
//				sql.append("beiz='-1',\n");
				sql.append("fcad=0,\n");
				sql.append("hdaf=0,\n");
				sql.append("sdaf=0,\n");
				sql.append("t1=0,\n");
				sql.append("t2=0,\n");
				sql.append("t3=0,\n");
				sql.append("t4=0 \n");
				sql.append("where id=").append(rsl.getString("id")).append(";\n");
				sql.append("update huaytmpb set zhillsb_id=0 where zhillsb_id=").append(rsl.getString("id")).append(";\n");
//				zhuanmsql.append("update zhuanmb set bianm='AAA").append(bianmrsl.getString("bianm")).append("' \n");
//				zhuanmsql.append("where 1=1 \n");
//				zhuanmsql.append("and zhillsb_id=").append(rsl.getString("id")).append(" \n");
//				zhuanmsql.append("and zhuanmlb_id=").append("(select id from zhuanmlb where jib = (select nvl(max(jib),0) from zhuanmlb));\n ");
//				}
			}else{
				if(con.getHasIt("select * from zhillsb where id =" + rsl.getString("id") + " and shenhzt>=0 and shenhzt<4")){
		//				long hytmpID = getExtGrid().getColumn("linsbm").combo.getBeanId(rsl.getString("linsbm"));
						tmpSQL = "SELECT * FROM huaytmpb WHERE ID=" + hytmpID;
						rs = con.getResultSetList(tmpSQL);
						if (rs.next()) {
							sql.append("update zhillsb set ");
							sql.append("huaysj=to_date('").append(DateUtil.DATE_FORMAT.format(rs.getDate("huaysj"))).append("','yyyy-mm-dd'),\n");
							sql.append("mt=").append(rs.getString("mt")).append(",\n");
							sql.append("mad=").append(rs.getString("mad")).append(",\n");
							sql.append("stad=").append(rs.getString("st_ad")).append(",\n");
							sql.append("std=").append(rs.getString("st_d")).append(",\n");
		//					sql.append("star=").append(rs.getString("st_ar")).append(",\n");
							sql.append("had=").append(rs.getString("had")).append(",\n");
							sql.append("har=").append(rs.getString("har")).append(",\n");
							sql.append("aad=").append(rs.getString("aad")).append(",\n");
							sql.append("ad=").append(rs.getString("ad")).append(",\n");
							sql.append("aar=").append(rs.getString("aar")).append(",\n");
							sql.append("vad=").append(rs.getString("vad")).append(",\n");
							sql.append("vdaf=").append(rs.getString("vdaf")).append(",\n");
							sql.append("qbad=").append(rs.getString("qb_ad")).append(",\n");
							sql.append("qgrad=").append(rs.getString("qgr_ad")).append(",\n");
							sql.append("qgrd=").append(rs.getString("qgr_d")).append(",\n");
							sql.append("qgrad_daf=").append(rs.getString("qgrdaf")).append(",\n");
							sql.append("qnet_ar=").append(rs.getString("qnet_ar")).append(",\n");
							sql.append("shenhzt=3,\n");
//							sql.append("beiz='").append(rsl.getString("linsbm")).append("',\n");
							sql.append("fcad=0,\n");
							sql.append("hdaf=0,\n");
							sql.append("sdaf=0,\n");
							sql.append("t1=0,\n");
							sql.append("t2=0,\n");
							sql.append("t3=0,\n");
							sql.append("t4=0, \n");
							sql.append("huayy='").append(rs.getString("huayy")).append("'\n");
							sql.append("where id=").append(rsl.getString("id")).append(";\n");
							
							sql.append("update zhuanmb set bianm='").append(rsl.getString("linsbm")).append("' \n");
							sql.append(" where 1=1 \n");
							sql.append("and zhillsb_id=").append(rsl.getString("id")).append(" \n");
							sql.append("and zhuanmlb_id=").append("(select id from zhuanmlb where jib = (select nvl(max(jib),0) from zhuanmlb));\n ");
							sql.append("update huaytmpb set zhillsb_id=0 where zhillsb_id=").append(rsl.getString("id")).append(";\n");
							sql.append("update huaytmpb set zhillsb_id=").append(rsl.getString("id")).append(" where id=").append(hytmpID).append(";\n");
							
						}
					}
			}
		}
		if(rsl.getRows()>0){
			sql.append("end;");
			flag = con.getUpdate(sql.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				setMsg("化验值匹配失败！");
				return;
			}
		}
		con.commit();
		rsl.close();
		con.Close();
	}
	public void Save3(String strchange, Visit visit) {
		String tableName = "zhillsb";

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");//更改审核状态sql
        StringBuffer zsql= new StringBuffer();//判断数据是否已审核
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		
		while (mdrsl.next()) {
			zsql.append("select * from zhillsb where id=")
			.append(mdrsl.getString("id"))
			.append(" and shenhzt>=5");
			if(con.getHasIt(zsql.toString())){
				setMsg("临时编码为:"+mdrsl.getString("linsbm")+"已审核，作废失败");
				return;
			}else{
				zsql.delete(0,zsql.length());
				sql.append("update ").append(tableName).append(" set ");
				sql.append("shenhzt").append("=-1 ");
				sql.append("where id =").append(mdrsl.getString("ID"))
						.append(";\n");
		    }
		}
		sql.append(" end;");
		con.getUpdate(sql.toString());
	}
	private void Zuof() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save3(getChange(), visit);
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_ZuofChick) {
			_ZuofChick = false;
			Zuof();
			getSelectData();

		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = null;
		String sql = "";
		sql = 
			"select d.zhilblsb_id id,getfahxx4zl(c.zhilb_id) fahxx,to_char(c.caiyrq,'yyyy-mm-dd') caiyrq,\n" +
			"bianm jincbm,b.mingc bum,d.leib,\n" + 
//			"(select bianm from zhuanmb where zhillsb_id = d.zhilblsb_id and zhuanmlb_id = 100661) caiybm,\n" + 
//			"(select bianm from zhuanmb where zhillsb_id = d.zhilblsb_id and zhuanmlb_id = 100663) huaybm,\n" + 
			"decode(huaytmpb.bianh,null,'',huaytmpb.bianh) AS linsbm,\n" + 
			"(case when z.shenhzt>=4 then nvl('已审核','') else nvl('未审核','') end) as shenhzt\n" +
			"from caiyb c,yangpdhb d,bumb b,zhillsb z,huaytmpb\n" + 
			"where c.caiyrq = " + DateUtil.FormatOracleDate(getBeginRiq()) + "\n" +
//			"and c.caiyrq <" + DateUtil.FormatOracleDate(getEndRiq()) + "+1\n" +
			"and c.id = d.caiyb_id\n" + 
			"and d.bumb_id = b.id\n" + 
			"and d.zhilblsb_id = z.id\n" + 
			"and z.id = huaytmpb.zhillsb_id(+)\n" + 
			"and z.shenhzt>-1\n" +
			"order by c.caiyrq";

		rsl = con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
//		设置grid为可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
//		设置为grid数据不分页
		egu.addPaging(0);
//		设置grid宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		设置grid列信息
		egu.getColumn("shenhzt").setHeader("化验状态");
		egu.getColumn("shenhzt").setRenderer("ztChange");
		egu.getColumn("shenhzt").setEditor(null);
		egu.getColumn("shenhzt").setWidth(60);
		egu.getColumn("fahxx").setHeader("发货信息");
		egu.getColumn("fahxx").setEditor(null);
		egu.getColumn("fahxx").setWidth(340);
		egu.getColumn("caiyrq").setHeader("采样日期");
		egu.getColumn("caiyrq").setEditor(null);
		egu.getColumn("caiyrq").setWidth(70);
		egu.getColumn("jincbm").setHeader("进厂批号");
		egu.getColumn("jincbm").setEditor(null);
		egu.getColumn("jincbm").setWidth(70);
		egu.getColumn("bum").setHeader("化验部门");
		egu.getColumn("bum").setEditor(null);
		egu.getColumn("bum").setWidth(60);
		egu.getColumn("leib").setHeader("化验类别");
		egu.getColumn("leib").setEditor(null);
		egu.getColumn("leib").setWidth(60);
//		egu.getColumn(7).setHeader("采样编码");
//		egu.getColumn(7).setEditor(null);
//		egu.getColumn(8).setHeader("化验编码");
//		egu.getColumn(8).setEditor(null);
		egu.getColumn("linsbm").setHeader("临时编码");
		egu.getColumn("linsbm").setEditor(new ComboBox());
		String tmpSQL = "SELECT ID,bianh FROM huaytmpb \n" +
			"WHERE caiysj = " + DateUtil.FormatOracleDate(getBeginRiq()) + "\n";
//			"AND caiysj <" + DateUtil.FormatOracleDate(getEndRiq()) + "+1";
		egu.getColumn("linsbm").setComboEditor(egu.gridId, new IDropDownModel(tmpSQL,"请选择"));
		egu.getColumn("linsbm").setDefaultValue("请选择");
		
//		增加grid中Toolbar显示日期参数
		egu.addTbarText("采样时间:");
		DateField dStart = new DateField();
		dStart.Binding("BeginRq","");
		dStart.setValue(getBeginRiq());
		egu.addToolbarItem(dStart.getScript());
//		egu.addTbarText(" 至 ");
//		DateField dEnd = new DateField();
//		dEnd.Binding("EndRq","");
//		dEnd.setValue(getEndRiq());
//		egu.addToolbarItem(dEnd.getScript());
		egu.addTbarText("-");
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("if(e.record.get('SHENHZT')=='已审核'){e.cancel=true;}");
		sb.append("});");
		egu.addOtherScript(sb.toString());
//		设置grid按钮
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");
		
        egu.addToolbarItem("{text:'作废',icon:'imgs/btnicon/show.gif',cls:'x-btn-text-icon',minWidth:75,handler:function(){var gridDivsave_history = '';var Mrcd = gridDiv_grid.getSelectionModel().getSelections();for(i = 0; i< Mrcd.length; i++){if(typeof(gridDiv_save)=='function'){ var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}}gridDivsave_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'+ '<FAHXX update=\"true\">' + Mrcd[i].get('FAHXX').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</FAHXX>'+ '<CAIYRQ update=\"true\">' + Mrcd[i].get('CAIYRQ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</CAIYRQ>'+ '<JINCBM update=\"true\">' + Mrcd[i].get('JINCBM').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</JINCBM>'+ '<BUM update=\"true\">' + Mrcd[i].get('BUM').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BUM>'+ '<LEIB update=\"true\">' + Mrcd[i].get('LEIB').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</LEIB>'+ '<LINSBM update=\"true\">' + Mrcd[i].get('LINSBM').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</LINSBM>'+ '<SHENHZT update=\"true\">' + Mrcd[i].get('SHENHZT').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</SHENHZT>' + '</result>' ; }if(gridDiv_history=='' && gridDivsave_history==''){ Ext.MessageBox.alert('提示信息','没有选择数据信息');}else{var Cobj = document.getElementById('CHANGE');Cobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';document.getElementById('ZuofButton').click();Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});}}}");
        
        String script ="var flag=true;\n"
			+ " var rec=gridDiv_ds.getRange();\n"
			+ "for(var i=0;i<rec.length;i++){\n"
			+ "if (rec[i].get('LINSBM') != '' && rec[i].get('LINSBM') != '请选择') {"
			+ "    for(var j=i+1;j<rec.length;j++){\n"
			+ "        if(rec[i].get('LINSBM')==rec[j].get('LINSBM')){\n"
			+ "            Ext.MessageBox.alert('提示信息','临时编码重复！');\n"
			+ "            flag=false;\n"
			+ "            break;\n"
			+ "        }\n"
			+ "    }\n"
			+ "}"
			+ "}\n"
			+ "if(!flag){	\n"
			+ "		return;	\n"
			+ "}	\n";
		egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton",script);
		
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		setExtGrid(egu);
		con.Close();
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
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
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
			if(!visit.getActivePageName().toString().equals(this.getPageName())){
				setBeginRiq(DateUtil.FormatDate(new Date()));
				setEndRiq(DateUtil.FormatDate(new Date()));
				init();
			}
			getSelectData();
			visit.setActivePageName(getPageName().toString());
		}
	} 
	
	private void init() {
		setExtGrid(null);
		getSelectData();
	}
}
