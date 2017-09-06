package com.zhiren.jt.het.yunsht.yunshtmb;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
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

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Liuc;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author caolin2
 * 
 */
/*
 * 作者：songy
 * 时间：2011-03-23 
 * 描述：修改下拉菜单的排序，要求按照名称进行排序
 */
public class Yunshtmb extends BasePage implements PageValidateListener {
	// protected void initialize() {
	// danwglStr = "";
	// }
	
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		this.setLiucmc("");
		setMsg("");
//		setTbmsg(null);
	}
//	private String tbmsg;
//	public String getTbmsg() {
//		return tbmsg;
//	}
//	public void setTbmsg(String tbmsg) {
//		this.tbmsg = tbmsg;
//	}
	
	private String getDanwglStr() {
		;// 指标单位关联数组
		return ((Visit) getPage().getVisit()).getString3();
	}

	private void setDanwglStr(String value) {
		;// 指标单位关联数组
		((Visit) getPage().getVisit()).setString3(value);
	}

	public String getpageLink() {
		return " var context='" + MainGlobal.getHomeContext(this) + "';"
				+ getDanwglStr();
	}

	public void setTabbarSelect(int value) {
		((Visit) getPage().getVisit()).setInt1(value);
	}

	public int getTabbarSelect() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

//	 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
//	 增扣款页面变化记录
	private String Changek;

	public String getChangek() {
		return Changek;
	}

	public void setChangek(String change) {
		Changek = change;
	}
	
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		
//		结算编号
		tb1.addText(new ToolbarText("选择模板:"));
		ComboBox mobmcDropDown = new ComboBox();
		mobmcDropDown.setId("mobmcSelect");
		mobmcDropDown.setListeners("select:function(){document.Form0.submit();}");
		mobmcDropDown.setWidth(200);
		mobmcDropDown.setLazyRender(true);
		mobmcDropDown.setTransform("mobmcSelect");
		tb1.addField(mobmcDropDown);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("使用流程:"));
		ComboBox liucDropDown = new ComboBox();
		liucDropDown.setId("liucSelect");
		liucDropDown.setWidth(200);
		liucDropDown.setLazyRender(true);
		liucDropDown.setTransform("liucSelect");
		tb1.addField(liucDropDown);
		tb1.addText(new ToolbarText("-"));
		
//		刷新
//		ToolbarButton shuaxbt=new ToolbarButton(null,"刷新","function(){ document.Form0.RetrunsButton.click();}");
//		shuaxbt.setId("Shuaxbt");
//		tb1.addItem(shuaxbt);
//		tb1.addText(new ToolbarText("-"));
//		删除
		ToolbarButton deletebt=new ToolbarButton(null,"删除","function(){ Ext.MessageBox.confirm('提示信息','您确定要删除此合同模板吗？',function(fn){if(fn=='yes'){document.Form0.ShancButton.click();}})}");
		deletebt.setId("ShancButton");
		
		if(this.isBtDisabled()){
			deletebt.setDisabled(true);
		}
		tb1.addItem(deletebt);
		tb1.addText(new ToolbarText("-"));
		
//		保存
		StringBuffer strExt = new StringBuffer();
		strExt.append(" \n");
		strExt.append("if(document.all.item('liucSelect').value==-1){ \n");
		strExt.append("		Ext.MessageBox.alert('提示信息','请选择使用流程！');return ; \n");
		strExt.append("} \n");
		strExt.append("if(document.all.item('mobmc').value==''){ \n");
		strExt.append("		Ext.MessageBox.alert('提示信息','合同模板名称不能为空！');return ;\n"); 
		strExt.append("} \n");
		strExt.append("var Mrcd = gridDiv_ds.getModifiedRecords(); \n");
		strExt.append("for(i = 0; i< Mrcd.length; i++){ \n");
		strExt.append("if(typeof(gridDiv_save)=='function'){  \n");
		strExt.append("		var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;} \n");
		strExt.append("} \n");
//		strExt.append("if(Mrcd[i].get('ZHIBB_ID') == ''){ \n");
//		strExt.append("		Ext.MessageBox.alert('提示信息','字段 指标 不能为空');return; \n");
//		strExt.append("} \n");
//		strExt.append("if(Mrcd[i].get('DANWB_ID') == ''){ \n");
//		strExt.append("		Ext.MessageBox.alert('提示信息','字段 指标单位 不能为空');return; \n");
//		strExt.append("} \n");
		strExt.append("if(Mrcd[i].get('YUNJA')!=0 && Mrcd[i].get('YUNJA') == ''){ \n");
		strExt.append("		Ext.MessageBox.alert('提示信息','字段 运价 不能为空');return; \n");
		strExt.append("} \n");
		strExt.append("if(Mrcd[i].get('YUNJDW_ID') == ''){ \n");
		strExt.append("		Ext.MessageBox.alert('提示信息','字段 运价单位 不能为空');return; \n");
		strExt.append("} \n");
		strExt.append("gridDiv_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">'  \n");
		strExt.append("+ Mrcd[i].get('ID').replace('<','&lt;').replace('>','&gt;')+ '</ID>'+ '<HETYS_ID update=\"true\">'  \n");
		strExt.append("+ Mrcd[i].get('HETYS_ID')+ '</HETYS_ID>'+ '<MEIKXXB_ID update=\"true\">'  \n");
		strExt.append("+ Mrcd[i].get('MEIKXXB_ID').replace('<','&lt;').replace('>','&gt;')+ '</MEIKXXB_ID>'+ '<ZHIBB_ID update=\"true\">'  \n");
		strExt.append("+ Mrcd[i].get('ZHIBB_ID').replace('<','&lt;').replace('>','&gt;')+ '</ZHIBB_ID>'+ '<DANWB_ID update=\"true\">'  \n");
		strExt.append("+ Mrcd[i].get('DANWB_ID').replace('<','&lt;').replace('>','&gt;')+ '</DANWB_ID>'+ '<TIAOJB_ID update=\"true\">'  \n");
		strExt.append("+ Mrcd[i].get('TIAOJB_ID').replace('<','&lt;').replace('>','&gt;')+ '</TIAOJB_ID>'+ '<XIAX update=\"true\">'  \n");
		strExt.append("+ Mrcd[i].get('XIAX')+ '</XIAX>'+ '<SHANGX update=\"true\">'  \n");
		strExt.append("+ Mrcd[i].get('SHANGX')+ '</SHANGX>'+ '<YUNJA update=\"true\">'  \n");
		strExt.append("+ Mrcd[i].get('YUNJA')+ '</YUNJA>'+ '<YUNJDW_ID update=\"true\">'  \n");
		strExt.append("+ Mrcd[i].get('YUNJDW_ID').replace('<','&lt;').replace('>','&gt;')+ '</YUNJDW_ID>' + '</result>' ;  \n");
		strExt.append("} \n");
		
		strExt.append("if(gridDiv_history==''){  \n");
//		strExt.append("// Ext.MessageBox.alert('提示信息','没有进行改动无需保存'); \n");
		strExt.append("}else{ \n");
		strExt.append("var Cobj = document.getElementById('CHANGE'); \n");
		strExt.append("Cobj.value = '<result>'+gridDiv_history+'</result>'; } \n");
		
		 
		strExt.append("var Mrcdk = gridDivk_ds.getModifiedRecords(); \n");
		strExt.append("for(i = 0; i< Mrcdk.length; i++){ \n");
		strExt.append("if(typeof(gridDivk_save)=='function'){ var revalue = gridDivk_save(Mrcdk[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}} \n");
//		strExt.append("if(Mrcdk[i].get('ZHIBB_ID') == ''){Ext.MessageBox.alert('提示信息','字段 项目 不能为空');return;} \n");
		strExt.append("if(Mrcdk[i].get('TIAOJB_ID') == ''){Ext.MessageBox.alert('提示信息','字段 条件 不能为空');return; \n");
//		strExt.append("}if(Mrcdk[i].get('DANWB_ID') == ''){Ext.MessageBox.alert('提示信息','字段 项目单位 不能为空');return; \n");
//		strExt.append("}if(Mrcdk[i].get('JISDWID') == ''){Ext.MessageBox.alert('提示信息','字段 基数单位 不能为空');return; \n");
//		strExt.append("}if(Mrcdk[i].get('KOUJDW') == ''){Ext.MessageBox.alert('提示信息','字段 扣价单位 不能为空');return; \n");
//		strExt.append("}if(Mrcdk[i].get('ZENGFJDW') == ''){Ext.MessageBox.alert('提示信息','字段 增付价单位 不能为空');return; \n");
		strExt.append("}if(Mrcdk[i].get('XIAOSCL') == ''){Ext.MessageBox.alert('提示信息','字段 小数处理方式 不能为空');return; \n");
//		strExt.append("}if(Mrcdk[i].get('CANZXM') == ''){Ext.MessageBox.alert('提示信息','字段 参照项目 不能为空');return; \n");
//		strExt.append("}if(Mrcdk[i].get('CANZXMDW') == ''){Ext.MessageBox.alert('提示信息','字段 参照项目单位 不能为空');return; \n");
		strExt.append("}if(Mrcdk[i].get('HETJSXSB_ID') == ''){Ext.MessageBox.alert('提示信息','字段 加权方式 不能为空');return; \n");
		strExt.append("}if(Mrcdk[i].get('YUNSFSB_ID') == ''){Ext.MessageBox.alert('提示信息','字段 运输方式 不能为空');return; \n");
		strExt.append("}gridDivk_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">'" +
				" + Mrcdk[i].get('ID').replace('<','&lt;').replace('>','&gt;')+ '</ID>'+ '<HETYS_ID update=\"true\">' " +
				" + Mrcdk[i].get('HETYS_ID')+ '</HETYS_ID>'+ '<ZHIBB_ID update=\"true\">'" +
				" + Mrcdk[i].get('ZHIBB_ID').replace('<','&lt;').replace('>','&gt;')+ '</ZHIBB_ID>'+ '<TIAOJB_ID update=\"true\">'" +
				" + Mrcdk[i].get('TIAOJB_ID').replace('<','&lt;').replace('>','&gt;')+ '</TIAOJB_ID>'+ '<XIAX update=\"true\">'" +
				" + Mrcdk[i].get('XIAX')+ '</XIAX>'+ '<SHANGX update=\"true\">'" +
				" + Mrcdk[i].get('SHANGX')+ '</SHANGX>'+ '<DANWB_ID update=\"true\">'" +
				" + Mrcdk[i].get('DANWB_ID').replace('<','&lt;').replace('>','&gt;')+ '</DANWB_ID>'+ '<JIS update=\"true\">'" +
				" + Mrcdk[i].get('JIS')+ '</JIS>'+ '<JISDWID update=\"true\">'" +
				" + Mrcdk[i].get('JISDWID').replace('<','&lt;').replace('>','&gt;')+ '</JISDWID>'+ '<KOUJ update=\"true\">'" +
				" + Mrcdk[i].get('KOUJ')+ '</KOUJ>'+ '<KOUJDW update=\"true\">'" +
				" + Mrcdk[i].get('KOUJDW').replace('<','&lt;').replace('>','&gt;')+ '</KOUJDW>'+ '<ZENGFJ update=\"true\">'" +
				" + Mrcdk[i].get('ZENGFJ')+ '</ZENGFJ>'+ '<ZENGFJDW update=\"true\">'" +
				" + Mrcdk[i].get('ZENGFJDW').replace('<','&lt;').replace('>','&gt;')+ '</ZENGFJDW>'+ '<XIAOSCL update=\"true\">'" +
				" + Mrcdk[i].get('XIAOSCL').replace('<','&lt;').replace('>','&gt;')+ '</XIAOSCL>'+ '<JIZZKJ update=\"true\">'" +
				" + Mrcdk[i].get('JIZZKJ')+ '</JIZZKJ>'+ '<JIZZB update=\"true\">'" +
				" + Mrcdk[i].get('JIZZB')+ '</JIZZB>'+ '<CANZXM update=\"true\">'" +
				" + Mrcdk[i].get('CANZXM').replace('<','&lt;').replace('>','&gt;')+ '</CANZXM>'+ '<CANZXMDW update=\"true\">'" +
				" + Mrcdk[i].get('CANZXMDW').replace('<','&lt;').replace('>','&gt;')+ '</CANZXMDW>'+ '<CANZXX update=\"true\">'" +
				" + Mrcdk[i].get('CANZXX')+ '</CANZXX>'+ '<CANZSX update=\"true\">'" +
				" + Mrcdk[i].get('CANZSX')+ '</CANZSX>'+ '<HETJSXSB_ID update=\"true\">'" +
				" + Mrcdk[i].get('HETJSXSB_ID').replace('<','&lt;').replace('>','&gt;')+ '</HETJSXSB_ID>'+ '<YUNSFSB_ID update=\"true\">'" +
				" + Mrcdk[i].get('YUNSFSB_ID').replace('<','&lt;').replace('>','&gt;')+ '</YUNSFSB_ID>'+ '<BEIZ update=\"true\">'" +
				" + Mrcdk[i].get('BEIZ').replace('<','&lt;').replace('>','&gt;')+ '</BEIZ>' + '</result>' ; } \n");
		
		
		strExt.append("if(gridDivk_history==''){  \n");
//		strExt.append("// Ext.MessageBox.alert('提示信息','没有进行改动无需保存'); \n");
		strExt.append("}else{ \n");
		strExt.append("var Cobjk = document.getElementById('CHANGEK'); \n");
		strExt.append("Cobjk.value = '<result>'+gridDivk_history+'</result>'; } \n");
		
		
		strExt.append("document.Form0.BaocButton.click(); \n");
		strExt.append("Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig:  \n");
		strExt.append("{interval:200},icon:Ext.MessageBox.INFO}); \n");
		strExt.append("} \n");
		
		ToolbarButton savebt=new ToolbarButton(null,"保存","function(){ "+strExt.toString());
		savebt.setId("BaocButton");
		tb1.addItem(savebt);
		
		if(this.isBtDisabled()){
			savebt.setDisabled(true);
		}
		tb1.addText(new ToolbarText("-"));
		
		
		
		String gb3_fs="function(){  \n"
		
			+ " if(!win){	\n" 
			+ "	\tvar form = new Ext.form.FormPanel({	\n" 
			+ " \tbaseCls: 'x-plain',	\n" 		
			+ " \tlabelAlign:'right',	\n" 
			+ " \tdefaultType: 'textfield',	\n"
			+ " \titems: [{		\n"
			+ " \txtype:'fieldset',	\n"
			+ " \ttitle:'请选择流程名称',	\n"
			+ " \tautoHeight:false,	\n"
			+ " \theight:220,	\n"
			+ " \titems:[	\n"
			+ " \tlcmccb=new Ext.form.ComboBox({	\n" 
			+ " \twidth:150,	\n"
			+ " \tid:'lcmccb',	\n"
			+ " \tselectOnFocus:true,	\n"
			+ "	\ttransform:'LiucmcDropDown',	\n"		
			+ " \tlazyRender:true,	\n"	
			+ " \tfieldLabel:'流程名称',		\n" 
			+ " \ttriggerAction:'all',	\n"
			+ " \ttypeAhead:true,	\n"	
			+ " \tforceSelection:true,	\n"
			+ " \teditable:false	\n"					
			+ " \t})	\n"
			
			
			+ " \t]		\n"
			+ " \t}]	\n"		
			+ " \t});	\n"
			+ " \twin = new Ext.Window({	\n"
			+ " \tel:'hello-win',	\n"
			+ " \tlayout:'fit',	\n"
			+ " \twidth:500,	\n"	
			+ " \theight:300,	\n"
			+ " \tcloseAction:'hide',	\n"
			+ " \tplain: true,	\n"
			+ " \ttitle:'流程',	\n"
			+ " \titems: [form],	\n"
			+ " \tbuttons: [{	\n"
			+ " \ttext:'确定',	\n"
			+ " \thandler:function(){	\n"  
			+ " \twin.hide();	\n"
			+ " \tif(lcmccb.getRawValue()=='请选择'){		\n" 
			+ "	\t	alert('请选择流程名称！');		\n"
			+ " \t}else{" 
			+ " \t\t document.getElementById('TEXT_LIUCMCSELECT_VALUE').value=lcmccb.getRawValue();	\n"
			+ " \t\t document.all.item('TijshButton').click();	\n"
//			+" Ext.Msg.progress('提示信息','请等待','数据加载中……');\n"
		//	+" Ext.Msg.show({title: 'Please wait',msg: '正在加载数据...',progressText: '数据加载中...',width:300,progress:true,closable:false});for(var i=0;i<=10;i++)setTimeout(function(){Ext.Msg.updateProgress(i/10,'数据加载中……','正在加载数据');},i*100);"
			+ " \t}	\n"
			+ " \t}	\n"
			+ " \t},{	\n"
			+ " \ttext: '取消',	\n"
			+ " \thandler: function(){	\n"
			+ " \twin.hide();	\n"
			+ " \tdocument.getElementById('TEXT_LIUCMCSELECT_VALUE').value='';	\n"	
			+ " \t}		\n"
			+ " \t}]	\n"
			+ " \t});}	\n" 
			+ " \twin.show(this);	\n"
//
//			+ " \tif(document.getElementById('TEXT_LIUCMCSELECT_VALUE').value!=''){	\n"	
//			//+ " \tChangb.setRawValue(document.getElementById('TEXT_LIUCMCSELECT_VALUE').value);	\n"	
//			+ " \t}	\n"	
			+ " \t}";
		
//		this.setTijsh();
		
		if(this.isTijsh()){
			
		
		ToolbarButton tij=new ToolbarButton(null,"提交进入流程",gb3_fs);
		tij.setId("TijBT");
		if(this.isBtDisabled()){
			tij.setDisabled(true);
		}
		tb1.addItem(tij);
		}
		setToolbar(tb1);
	}
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	// 功能区 新建模板 打开 保存 选择流程 模板名称
	// 模板名称下拉框
	public IDropDownBean getmobmcSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getmobmcSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setmobmcSelectValue(IDropDownBean Value) {
		if (Value != null
				&& ((Visit) getPage().getVisit()).getDropDownBean1() != null) {
			if (Value.getId() != ((Visit) getPage().getVisit())
					.getDropDownBean1().getId()) {
				((Visit) getPage().getVisit()).setboolean1(true);
			} else {
				((Visit) getPage().getVisit()).setboolean1(false);
			}
			((Visit) getPage().getVisit()).setDropDownBean1(Value);

		}
	}

	public void setmobmcSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getmobmcSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getmobmcSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void getmobmcSelectModels() {
		
		String sql="";
//		看到的是本级的、下级的、上级的合同模板
//		if(this.isTijsh()){
//			 sql = "select id,mingc " + "from HETYS_MB "
//			+ "where diancxxb_id="
//			+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//			+" and nvl(liucztb_id,-1)<=0";
//		}else{
			 sql = "select mb.id,mb.mingc from HETYS_MB mb,diancxxb dc	\n"
			+ "where mb.diancxxb_id = dc.id and (diancxxb_id="
			+ ((Visit) getPage().getVisit()).getDiancxxb_id()
			+ "	\n or dc.fuid="
			+((Visit) getPage().getVisit()).getDiancxxb_id()
			+ "	\n or dc.id in (select fuid from diancxxb where id="
			+((Visit) getPage().getVisit()).getDiancxxb_id()+"))	\n"
			+ " order by mb.mingc "
			;
//		}
		
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, "新建模板"));
		return;
	}

	// 合同审核流程下拉框
	public IDropDownBean getliucSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getliucSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setliucSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setliucSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getliucSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getliucSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getliucSelectModels() {
		String sql = "select liucb.id,liucb.mingc\n" + "from liucb,liuclbb\n"
				+ "where liucb.liuclbb_id=liuclbb.id and liuclbb.mingc='合同'  order by liucb.mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, ""));
		return;
	}

	// 新模板名称
	public String getmobmc() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setmobmc(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}

	// 合同信息
	public Hetysmbxxbean gethetysmbxxbean() {
		if (((Visit) getPage().getVisit()).getObject1() == null) {
			((Visit) getPage().getVisit()).setObject1(new Hetysmbxxbean());
		}
		return (Hetysmbxxbean) ((Visit) getPage().getVisit()).getObject1();
	}

	public void sethetysmbxxbean(Hetysmbxxbean value) {
		((Visit) getPage().getVisit()).setObject1(value);
	}

	// 计划口径
	public IDropDownBean getJihxzValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getIJihxzModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setJihxzValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setIJihxzModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getIJihxzModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getIJihxzModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getIJihxzModels() {
		String sql = "select id,mingc\n" + "from jihkjb order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql, ""));
		return;
	}

	// //合同数量信息
	// private int _editTableRows = -1;
	// public int getEditTableRows() {
	// return _editTableRows;
	// }
	// public void setEditTableRows(int _value) {
	// _editTableRows = _value;
	// }
	// public List geteditValuess(){
	// if(((Visit) getPage().getVisit()).getList1()==null){
	// ((Visit) getPage().getVisit()).setList1(new ArrayList());
	// }
	// return ((Visit) getPage().getVisit()).getList1();
	// }
	// private Fahxxbean editValues1;
	// public Fahxxbean geteditValues1(){
	// return editValues1;
	// }
	// public void seteditValues1(Fahxxbean value){
	// this.editValues1=value;
	// }
	// 运输方式
	public IDropDownBean getyunsfsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getyunsfsModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setyunsfsValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setyunsfsModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getyunsfsModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getyunsfsModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getyunsfsModels() {
		String sql = "select id,mingc\n" + "from yunsfsb order by mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql, ""));
		return;
	}

	//
	// 燃料品种
	public IDropDownBean getRanlpzb_idValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getRanlpzb_idModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setRanlpzb_idValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setRanlpzb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getRanlpzb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getRanlpzb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getRanlpzb_idModels() {
		String sql = "select id,mingc\n" + "from pinzb order by mingc ";// where
		// pinzb.zhangt=1
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql, ""));
		return;
	}

	//
	// 车站
	public IDropDownBean getchezValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean6((IDropDownBean) getchezModel().getOption(
							0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setchezValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean6(Value);
	}

	public void setchezModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getchezModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getchezModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void getchezModels() {
		String sql = "select id,mingc\n" + "from chezxxb\n" + "order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel6(new IDropDownModel(sql, ""));
		return;
	}

	//
	// 收货人
	public IDropDownBean getshouhrValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean7((IDropDownBean) getshouhrModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean7();
	}

	public void setshouhrValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean7(Value);
	}

	public void setshouhrModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel7(value);
	}

	public IPropertySelectionModel getshouhrModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {
			getshouhrModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}

	// public void getshouhrModels() {
	// String sql =
	// "select id,mingc\n" +
	// "from diancxxb";
	// ((Visit) getPage().getVisit()).setProSelectionModel7(new
	// IDropDownModel(sql,"")) ;
	// return ;
	// }
	public void getshouhrModels() {
		String sql = "select id,mingc,jib\n" + "from(\n"
				+ " select id,mingc,0 as jib\n" + " from diancxxb\n"
				+ " where id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id() + "\n"
				+ " union\n" + " select *\n" + " from(\n"
				+ " select id,mingc,level as jib\n" + "  from diancxxb\n"
				+ " start with fuid="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id() + "\n"
				+ " connect by fuid=prior id\n" + " order SIBLINGS by  xuh)\n"
				+ " )\n" + " order by jib , mingc";
		List dropdownlist = new ArrayList();
		dropdownlist.add(new IDropDownBean(0, ""));
		JDBCcon con = new JDBCcon();
		try {
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String mc = rs.getString("mingc");
				int jib = rs.getInt("jib");
				String nbsp = String.valueOf((char) 0xA0);
				for (int i = 0; i < jib; i++) {
					mc = nbsp + nbsp + nbsp + nbsp + mc;
				}
				dropdownlist.add(new IDropDownBean(id, mc));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel7(new IDropDownModel(dropdownlist));
		return;
	}

	// 质量
	/*
	 * private int _editTableRowz = -1; public int getEditTableRowz() { return
	 * _editTableRowz; } public void setEditTableRowz(int _value) {
	 * _editTableRowz = _value; } public List geteditValuesz(){ if(((Visit)
	 * getPage().getVisit()).getList2()==null){ ((Visit)
	 * getPage().getVisit()).setList2(new ArrayList()); } return ((Visit)
	 * getPage().getVisit()).getList2(); } public Zhilyqbean editValuez; public
	 * Zhilyqbean geteditValuez(){ return this.editValuez; } public void
	 * seteditValuez(Zhilyqbean value){ this.editValuez=value; } //指标 public
	 * IDropDownBean getZHIBValue() { if( ((Visit)
	 * getPage().getVisit()).getDropDownBean8()==null){ ((Visit)
	 * getPage().getVisit()).setDropDownBean8((IDropDownBean)getZHIBModel().getOption(0)); }
	 * return ((Visit) getPage().getVisit()).getDropDownBean8(); }
	 * 
	 * public void setZHIBValue(IDropDownBean Value) { ((Visit)
	 * getPage().getVisit()).setDropDownBean8(Value); }
	 * 
	 * 
	 * public void setZHIBModel(IPropertySelectionModel value) { ((Visit)
	 * getPage().getVisit()).setProSelectionModel8(value); }
	 * 
	 * public IPropertySelectionModel getZHIBModel() { if (((Visit)
	 * getPage().getVisit()).getProSelectionModel8() == null) { getZHIBModels(); }
	 * return ((Visit) getPage().getVisit()).getProSelectionModel8(); }
	 * 
	 * public void getZHIBModels() { String sql = "select id,mingc\n" + "from
	 * zhibb"; ((Visit) getPage().getVisit()).setProSelectionModel8(new
	 * IDropDownModel(sql,"")) ; return ; } //条件 public IDropDownBean
	 * getTIAOJValue() { if( ((Visit)
	 * getPage().getVisit()).getDropDownBean9()==null){ ((Visit)
	 * getPage().getVisit()).setDropDownBean9((IDropDownBean)getTIAOJModel().getOption(0)); }
	 * return ((Visit) getPage().getVisit()).getDropDownBean9(); }
	 * 
	 * public void setTIAOJValue(IDropDownBean Value) { ((Visit)
	 * getPage().getVisit()).setDropDownBean9(Value); }
	 * 
	 * 
	 * public void setTIAOJModel(IPropertySelectionModel value) { ((Visit)
	 * getPage().getVisit()).setProSelectionModel9(value); }
	 * 
	 * public IPropertySelectionModel getTIAOJModel() { if (((Visit)
	 * getPage().getVisit()).getProSelectionModel9() == null) {
	 * getTIAOJModels(); } return ((Visit)
	 * getPage().getVisit()).getProSelectionModel9(); }
	 * 
	 * public void getTIAOJModels() { String sql = "select id,mingc\n" + "from
	 * tiaojb"; ((Visit) getPage().getVisit()).setProSelectionModel9(new
	 * IDropDownModel(sql,"")) ; return ; }
	 */
	// 单位
	public IDropDownBean getYunjudwValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getYunjudwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setYunjudwValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean10(Value);
	}

	public void setYunjudwModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getYunjudwModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {
			getYunjudwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public void getYunjudwModels() {
		String sql = "select id,mingc\n" + "from danwb where zhibb_id=19 order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, ""));
		return;
	}

	// 增扣价格
	/*
	 * private int _editTableRowj = -1; public int getEditTableRowj() { return
	 * _editTableRowj; } public void setEditTableRowj(int _value) {
	 * _editTableRowj = _value; } public List geteditValuesj(){ if(((Visit)
	 * getPage().getVisit()).getList3()==null){ ((Visit)
	 * getPage().getVisit()).setList3(new ArrayList()); } return ((Visit)
	 * getPage().getVisit()).getList3(); } public Zengkkbean editValuej; public
	 * Zengkkbean geteditValuej(){ return this.editValuej; } public void
	 * seteditValuej(Zengkkbean value){ this.editValuej=value; }
	 */
	// 价格
	private int _editTableRowg = -1;

	public int getEditTableRowg() {
		return _editTableRowg;
	}

	public void setEditTableRowg(int _value) {
		_editTableRowg = _value;
	}

	public List geteditValuesg() {
		if (((Visit) getPage().getVisit()).getList4() == null) {
			((Visit) getPage().getVisit()).setList4(new ArrayList());
		}
		return ((Visit) getPage().getVisit()).getList4();
	}

	public Yunsmbjijbean editValueg;

	public Yunsmbjijbean geteditValueg() {
		return this.editValueg;
	}

	public void seteditValueg(Yunsmbjijbean value) {
		this.editValueg = value;
	}

	// 指标
	public IDropDownBean getZhibSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean11() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean11((IDropDownBean) getZhibSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean11();
	}

	public void setZhibSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean11(Value);
	}

	public void setZhibSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel11(value);
	}

	public IPropertySelectionModel getZhibSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel11() == null) {
			getZhibSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel11();
	}

	public void getZhibSelectModels() {
		String sql = "select id,mingc\n" + "from zhibb\n"
				+ "where zhibb.leib=1 and zhibb.id in (18,19)  order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel11(new IDropDownModel(sql, ""));
		return;
	}

	// 结算用条件
	public IDropDownBean gettiaojjSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean12() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean12((IDropDownBean) gettiaojjSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean12();
	}

	public void settiaojjSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean12(Value);
	}

	public void settiaojjSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel12(value);
	}

	public IPropertySelectionModel gettiaojjSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel12() == null) {
			gettiaojjSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel12();
	}

	public void gettiaojjSelectModels() {
		String sql = "select id,mingc\n" + "from tiaojb\n"
				+ "where tiaojb.leib=1   order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel12(new IDropDownModel(sql, ""));
		return;
	}

	// 价格单位
	public IDropDownBean getYunjiadwValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean13() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean13((IDropDownBean) getYunjiadwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean13();
	}

	public void setYunjiadwValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean13(Value);
	}

	public void setYunjiadwModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel13(value);
	}

	public IPropertySelectionModel getYunjiadwModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel13() == null) {
			getYunjiadwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel13();
	}

	public void getYunjiadwModels() {
		String sql = "select id,mingc\n" + "from danwb\n"
				+ "where danwb.zhibb_id in (18,19)   order by mingc ";// zhibb_id=0为价格单位
		((Visit) getPage().getVisit())
				.setProSelectionModel13(new IDropDownModel(sql, ""));
		return;
	}

	// 指标单位
	public IDropDownBean getzhibdwSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean14() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean14((IDropDownBean) getzhibdwSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean14();
	}

	public void setzhibdwSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean14(Value);
	}

	public void setzhibdwSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel14(value);
	}

	public IPropertySelectionModel getzhibdwSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel14() == null) {
			getzhibdwSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel14();
	}

	public void getzhibdwSelectModels() {// zhibb_id=
		String sql = "select id,mingc " + "from danwb    order by mingc\n";
		((Visit) getPage().getVisit())
				.setProSelectionModel14(new IDropDownModel(sql, ""));
		return;
	}

	/*
	 * // 合同结算方式表 public IDropDownBean getjiesfsgSelectValue() { if( ((Visit)
	 * getPage().getVisit()).getDropDownBean15()==null){ ((Visit)
	 * getPage().getVisit()).setDropDownBean15((IDropDownBean)getjiesfsgSelectModel().getOption(0)); }
	 * return ((Visit) getPage().getVisit()).getDropDownBean15(); }
	 * 
	 * public void setjiesfsgSelectValue(IDropDownBean Value) { ((Visit)
	 * getPage().getVisit()).setDropDownBean15(Value); }
	 * 
	 * 
	 * public void setjiesfsgSelectModel(IPropertySelectionModel value) {
	 * ((Visit) getPage().getVisit()).setProSelectionModel15(value); }
	 * 
	 * public IPropertySelectionModel getjiesfsgSelectModel() { if (((Visit)
	 * getPage().getVisit()).getProSelectionModel15() == null) {
	 * getjiesfsgSelectModels(); } return ((Visit)
	 * getPage().getVisit()).getProSelectionModel15(); }
	 * 
	 * public void getjiesfsgSelectModels() { String sql = "select id,mingc\n" +
	 * "from hetjsfsb"; ((Visit)
	 * getPage().getVisit()).setProSelectionModel15(new IDropDownModel(sql,"")) ;
	 * return ; } // 合同计价方式 public IDropDownBean getjijfsgSelectValue() { if(
	 * ((Visit) getPage().getVisit()).getDropDownBean16()==null){ ((Visit)
	 * getPage().getVisit()).setDropDownBean16((IDropDownBean)getjijfsgSelectModel().getOption(0)); }
	 * return ((Visit) getPage().getVisit()).getDropDownBean16(); }
	 * 
	 * public void setjijfsgSelectValue(IDropDownBean Value) { ((Visit)
	 * getPage().getVisit()).setDropDownBean16(Value); }
	 * 
	 * 
	 * public void setjijfsgSelectModel(IPropertySelectionModel value) {
	 * ((Visit) getPage().getVisit()).setProSelectionModel16(value); }
	 * 
	 * public IPropertySelectionModel getjijfsgSelectModel() { if (((Visit)
	 * getPage().getVisit()).getProSelectionModel16() == null) {
	 * getjijfsgSelectModels(); } return ((Visit)
	 * getPage().getVisit()).getProSelectionModel16(); } public void
	 * getjijfsgSelectModels() { String sql="select id,mingc\n" + "from
	 * hetjjfsb"; ((Visit) getPage().getVisit()).setProSelectionModel16(new
	 * IDropDownModel(sql,"")) ; return ; }
	 */
	// 小数处理
	public IDropDownBean getxiaoswcljSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean17() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean17((IDropDownBean) getxiaoswcljSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean17();
	}

	public void setxiaoswcljSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean17(Value);
	}

	public void setxiaoswcljSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel17(value);
	}

	public IPropertySelectionModel getxiaoswcljSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel17() == null) {
			getxiaoswcljSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel17();
	}

	public void getxiaoswcljSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, " "));
		list.add(new IDropDownBean(1, "进位"));
		list.add(new IDropDownBean(2, "舍去"));
		list.add(new IDropDownBean(3, "四舍五入"));
		list.add(new IDropDownBean(4, "四舍五入(0.1)"));
		list.add(new IDropDownBean(5, "四舍五入(0.01)"));

		((Visit) getPage().getVisit())
				.setProSelectionModel17(new IDropDownModel(list));
		return;
	}

	// 文字
	String buffer;

	public String getWenz() {
		return ((Visit) getPage().getVisit()).getString4();
	}

	public void setWenz(String value) {
		((Visit) getPage().getVisit()).setString4(value);
	}

	// 合同供方
	public IDropDownBean getgongfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean18() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean18((IDropDownBean) getgongfModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean18();
	}

	public void setgongfValue(IDropDownBean Value) {
		if (Value != null
				&& ((Visit) getPage().getVisit()).getDropDownBean18() != null) {
			if (Value.getId() != ((Visit) getPage().getVisit())
					.getDropDownBean18().getId()) {
				((Visit) getPage().getVisit()).setboolean2(true);
			} else {
				((Visit) getPage().getVisit()).setboolean2(false);
			}
			((Visit) getPage().getVisit()).setDropDownBean18(Value);
		}
	}

	public void setgongfModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel18(value);
	}

	public IPropertySelectionModel getgongfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel18() == null) {
			getgongfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel18();
	}

	public void getgongfModels() {
		String sql = "select id,mingc\n" + "from yunsdwb   order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel18(new IDropDownModel(sql, ""));
		return;
	}

	// 需方
	public IDropDownBean getxufValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean19() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean19((IDropDownBean) getxufModel().getOption(
							0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean19();
	}

	public void setxufValue(IDropDownBean Value) {
		if (Value != null
				&& ((Visit) getPage().getVisit()).getDropDownBean19() != null) {
			if (Value.getId() != ((Visit) getPage().getVisit())
					.getDropDownBean19().getId()) {
				((Visit) getPage().getVisit()).setboolean3(true);
			} else {
				((Visit) getPage().getVisit()).setboolean3(false);
			}
			((Visit) getPage().getVisit()).setDropDownBean19(Value);
		}
	}

	public void setxufModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel19(value);
	}

	public IPropertySelectionModel getxufModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel19() == null) {
			getxufModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel19();
	}

	// public void getxufModels() {//显示该用户单位下所有孩子
	// String sql=
	// "select id,mingc " +
	// "from diancxxb\n" +
	// "where diancxxb.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"
	// or id="+((Visit) getPage().getVisit()).getDiancxxb_id();
	// ((Visit) getPage().getVisit()).setProSelectionModel19(new
	// IDropDownModel(sql,"")) ;
	// return ;
	// }
	public void getxufModels() {// 显示该用户单位下所有孩子
		String sql = "select id,mingc " + "from diancxxb\n" + "where  id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id()+" order by xuh ";
		((Visit) getPage().getVisit())
				.setProSelectionModel19(new IDropDownModel(sql, ""));
		return;
	}

	// 供方
	public IDropDownBean getshijgfSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean20() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean20((IDropDownBean) getshijgfSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean20();
	}

	public void setshijgfSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean20(Value);
	}

	public void setshijgfSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel20(value);
	}

	public IPropertySelectionModel getshijgfSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel20() == null) {
			getshijgfSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel20();
	}

	public void getshijgfSelectModels() {
		String sql = "select id,mingc\n" + "from gongysb   order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel20(new IDropDownModel(sql, ""));
		return;
	}

	// 加权方式
	/*
	 * public IDropDownBean getjiaqfsgSelectValue() { if( ((Visit)
	 * getPage().getVisit()).getDropDownBean21()==null){ ((Visit)
	 * getPage().getVisit()).setDropDownBean21((IDropDownBean)getjiaqfsgSelectModel().getOption(0)); }
	 * return ((Visit) getPage().getVisit()).getDropDownBean21(); }
	 * 
	 * public void setjiaqfsgSelectValue(IDropDownBean Value) { ((Visit)
	 * getPage().getVisit()).setDropDownBean21(Value); }
	 * 
	 * 
	 * public void setjiaqfsgSelectModel(IPropertySelectionModel value) {
	 * ((Visit) getPage().getVisit()).setProSelectionModel21(value); }
	 * 
	 * public IPropertySelectionModel getjiaqfsgSelectModel() { if (((Visit)
	 * getPage().getVisit()).getProSelectionModel21() == null) {
	 * getjiaqfsgSelectModels(); } return ((Visit)
	 * getPage().getVisit()).getProSelectionModel21(); }
	 * 
	 * public void getjiaqfsgSelectModels() { String sql= "select id,mingc\n" +
	 * "from hetjsxsb"; ((Visit)
	 * getPage().getVisit()).setProSelectionModel21(new IDropDownModel(sql,"")) ;
	 * return ; }
	 */
	// 拒付亏吨运费
	public IDropDownBean getyingdkfgSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean22() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean22((IDropDownBean) getyingdkfgSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean22();
	}

	public void setyingdkfgSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean22(Value);
	}

	public void setyingdkfgSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel22(value);
	}

	public IPropertySelectionModel getyingdkfgSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel22() == null) {
			getyingdkfgSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel22();
	}

	public void getyingdkfgSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, ""));
		list.add(new IDropDownBean(1, "是"));
		list.add(new IDropDownBean(2, "否"));
		((Visit) getPage().getVisit())
				.setProSelectionModel22(new IDropDownModel(list));
		return;
	}

	// //煤矿
	public IDropDownBean getMeikxxValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean23() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean23((IDropDownBean) getMeikxxModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean23();
	}

	public void setMeikxxValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean23(Value);
	}

	public void setMeikxxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel23(value);
	}

	public IPropertySelectionModel getMeikxxModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel23() == null) {
			getMeikxxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel23();
	}

	public void getMeikxxModels() {
		String sql = "select * from (select id,mingc\n"
				+ "from meikxxb order by mingc)   ";
		((Visit) getPage().getVisit())
				.setProSelectionModel23(new IDropDownModel(sql, ""));
		return;
	}

	// 煤矿
	public IDropDownBean getMeikxx2Value() {
		if (((Visit) getPage().getVisit()).getDropDownBean24() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean24((IDropDownBean) getMeikxx2Model()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean24();
	}

	public void setMeikxx2Value(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean24(Value);
	}

	public void setMeikxx2Model(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel24(value);
	}

	public IPropertySelectionModel getMeikxx2Model() {
		if (((Visit) getPage().getVisit()).getProSelectionModel24() == null) {
			getMeikxx2Models();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel24();
	}

	public void getMeikxx2Models() {
		String sql = "select * from (select id,mingc\n"
				+ "from meikxxb order by mingc) ";
		((Visit) getPage().getVisit())
				.setProSelectionModel24(new IDropDownModel(sql, ""));
		return;
	}
	
    //价格方案
    public IDropDownBean getYunsjgfaValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean25()==null){
    		 ((Visit) getPage().getVisit()).setDropDownBean25((IDropDownBean)getIYunsjgfaModel().getOption(0));
    	}
        return  ((Visit) getPage().getVisit()).getDropDownBean25();
    }

    public void setYunsjgfaValue(IDropDownBean Value) {
    	((Visit) getPage().getVisit()).setDropDownBean25(Value);
    }


    public void setIYunsjgfaModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel25(value);
    }

    public IPropertySelectionModel getIYunsjgfaModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel25() == null) {
            getIYunsjgfaModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel25();
    }

    public void getIYunsjgfaModels() {
        String sql = 
        	"select id,mingc\n" +
        	"from yunsjgfab   order by mingc ";
        ((Visit) getPage().getVisit()).setProSelectionModel25(new IDropDownModel(sql,"")) ;
        return ;
    }
    
    
	public String _liucmc;
	
	public void setLiucmc(String _value) {
		_liucmc = _value;
	}
	
	public String getLiucmc() {
		if (_liucmc == null) {
			_liucmc = "";
		}
		return _liucmc;
	}
	
//	流程名称 DropDownBean26
//  流程名称 ProSelectionModel26
	public IDropDownBean getLiucmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean26() == null) {
			((Visit) getPage().getVisit()).setDropDownBean26((IDropDownBean) getILiucmcModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean26();
	}

	public void setLiucmcValue(IDropDownBean value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean26()!=value){
			
			((Visit) getPage().getVisit()).setDropDownBean26(value);
		}
	}

	public void setILiucmcModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel26(value);
	}

	public IPropertySelectionModel getILiucmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel26() == null) {
			
			getILiucmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel26();
	}

	public IPropertySelectionModel getILiucmcModels() {
		
		String sql="";
		sql="select liucb.id,liucb.mingc from liucb,liuclbb where liucb.liuclbb_id=liuclbb.id and liuclbb.mingc='合同模板' order by mingc";

		((Visit) getPage().getVisit()).setProSelectionModel26(new IDropDownModel(sql,"请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel26();
	}
//   流程名称 end
	
//***************************************************************************//
	
	
    
	private boolean tijsh;//是否添加  提交进入流程审核功能
	
	public boolean isTijsh(){
		
		return tijsh;
	}
	
	public void setTijsh(){
		
		tijsh=false;
		
		String sql=" select * from xitxxb  where mingc='运输合同模板提交审核' and leib='合同模板' and zhi='是' and zhuangt=1 ";
	
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=con.getResultSetList(sql);
		
		if(rsl.next()){
			tijsh=true;
		}
		
	}
	
	private boolean btDisabled=false;
	
	public boolean isBtDisabled(){
		return btDisabled;
	}
	
	public void setBtDisabled(){
		
		try {
			btDisabled=false;
			
			
			 String s=MainGlobal.getTableCol("hetys_mb", "liucztb_id", "id", this.getmobmcSelectValue().getStrId());
			 
			 if(s!=null && !s.equals("") && Integer.parseInt(s)>0){//模板已经进入流程 或者  已经被审核完成，禁用按钮
				 btDisabled=true;
			 }
			 
			 
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		
	}
	

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setString2("");
			visit.setString3("");
			visit.setString4("");
			((Visit) getPage().getVisit()).setDropDownBean1(null);
			((Visit) getPage().getVisit()).setDropDownBean2(null);
			((Visit) getPage().getVisit()).setDropDownBean3(null);
			((Visit) getPage().getVisit()).setDropDownBean4(null);
			((Visit) getPage().getVisit()).setDropDownBean5(null);
			((Visit) getPage().getVisit()).setDropDownBean6(null);
			((Visit) getPage().getVisit()).setDropDownBean7(null);
			((Visit) getPage().getVisit()).setDropDownBean8(null);
			((Visit) getPage().getVisit()).setDropDownBean9(null);
			((Visit) getPage().getVisit()).setDropDownBean10(null);
			((Visit) getPage().getVisit()).setDropDownBean11(null);
			((Visit) getPage().getVisit()).setDropDownBean12(null);
			((Visit) getPage().getVisit()).setDropDownBean13(null);
			((Visit) getPage().getVisit()).setDropDownBean14(null);
			((Visit) getPage().getVisit()).setDropDownBean15(null);
			((Visit) getPage().getVisit()).setDropDownBean16(null);
			((Visit) getPage().getVisit()).setDropDownBean17(null);
			((Visit) getPage().getVisit()).setDropDownBean18(null);
			((Visit) getPage().getVisit()).setDropDownBean19(null);
			((Visit) getPage().getVisit()).setDropDownBean20(null);
			((Visit) getPage().getVisit()).setDropDownBean21(null);
			((Visit) getPage().getVisit()).setDropDownBean22(null);
			((Visit) getPage().getVisit()).setDropDownBean23(null);
			((Visit) getPage().getVisit()).setDropDownBean24(null);
			((Visit) getPage().getVisit()).setDropDownBean25(null);
			((Visit) getPage().getVisit()).setDropDownBean26(null);
			((Visit) getPage().getVisit()).setProSelectionModel1(null);
			((Visit) getPage().getVisit()).setProSelectionModel2(null);
			((Visit) getPage().getVisit()).setProSelectionModel3(null);
			((Visit) getPage().getVisit()).setProSelectionModel4(null);
			((Visit) getPage().getVisit()).setProSelectionModel5(null);
			((Visit) getPage().getVisit()).setProSelectionModel6(null);
			((Visit) getPage().getVisit()).setProSelectionModel7(null);
			((Visit) getPage().getVisit()).setProSelectionModel8(null);
			((Visit) getPage().getVisit()).setProSelectionModel9(null);
			((Visit) getPage().getVisit()).setProSelectionModel10(null);
			((Visit) getPage().getVisit()).setProSelectionModel11(null);
			((Visit) getPage().getVisit()).setProSelectionModel12(null);
			((Visit) getPage().getVisit()).setProSelectionModel13(null);
			((Visit) getPage().getVisit()).setProSelectionModel14(null);
			((Visit) getPage().getVisit()).setProSelectionModel15(null);
			((Visit) getPage().getVisit()).setProSelectionModel16(null);
			((Visit) getPage().getVisit()).setProSelectionModel17(null);
			((Visit) getPage().getVisit()).setProSelectionModel18(null);
			((Visit) getPage().getVisit()).setProSelectionModel19(null);
			((Visit) getPage().getVisit()).setProSelectionModel20(null);
			((Visit) getPage().getVisit()).setProSelectionModel21(null);
			((Visit) getPage().getVisit()).setProSelectionModel22(null);
			((Visit) getPage().getVisit()).setProSelectionModel23(null);
			((Visit) getPage().getVisit()).setProSelectionModel24(null);
			((Visit) getPage().getVisit()).setProSelectionModel25(null);
			((Visit) getPage().getVisit()).setProSelectionModel26(null);
			((Visit) getPage().getVisit()).setObject1(null);
			((Visit) getPage().getVisit()).setList1(null);
			((Visit) getPage().getVisit()).setList2(null);
			((Visit) getPage().getVisit()).setList3(null);
			((Visit) getPage().getVisit()).setList4(null);
			
		//	this.setTijsh();
			
			
			setTabbarSelect(0);
			getDanwGL();
			getHetysjgExt(-1);
//			增扣款
			getYunshtzkkExt(-1);
			
			
			this.setTijsh();
		}
		
		
		if(this.isTijsh()){
			this.setBtDisabled();
		}
		
		getToolbars();
		
		if (((Visit) getPage().getVisit()).getboolean1()) {
			getXuanzmb();
		}
//		if (((Visit) getPage().getVisit()).getboolean2()) {// 合同供方
//			getGongf();
//		}
//		if (((Visit) getPage().getVisit()).getboolean3()) {// 需方
//			getXuf();
//		}
		
	}
	
	private void getDanwGL() {
		String sql = "";
		List list = new ArrayList();
		StringBuffer Tem = new StringBuffer();
		JDBCcon con = new JDBCcon();
		sql = "select zhibb_id zhibid,danwb.id danwid,danwb.mingc,zhibb.mingc\n"
				+ "from danwb,zhibb\n"
				+ "where danwb.zhibb_id=zhibb.id and zhibb_id<>0\n"
				+ "order by zhibb_id ,zhibb.mingc ";
		ResultSet rs = con.getResultSet(sql);
		try {
			while (rs.next()) {
				String[] gl = new String[4];
				gl[0] = rs.getString(1);
				gl[1] = rs.getString(2);
				gl[2] = rs.getString(3);
				gl[3] = rs.getString(4);
				list.add(gl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		Tem.append("var zhib_danw=new Array();");
		for (int i = 0; i < list.size(); i++) {
			Tem.append("zhib_danw[" + i + "]=new Array("
					+ ((String[]) list.get(i))[0] + ","
					+ ((String[]) list.get(i))[1] + ",'"
					+ ((String[]) list.get(i))[2] + "','"
					+ ((String[]) list.get(i))[3] + "');");// +
		}
		setDanwglStr(Tem.toString());
	}

	// 页面判定方法
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

	// 模板管理
	// private boolean _XinjButton = false;
	//
	// public void XinjButton(IRequestCycle cycle) {
	// _XinjButton = true;
	// }
	//
	// private boolean _DakButton = false;
	//
	// public void DakButton(IRequestCycle cycle) {
	// _DakButton = true;
	// }

	private boolean _ShancButton = false;

	public void ShancButton(IRequestCycle cycle) {
		_ShancButton = true;
	}

	private boolean _BaocButton = false;

	public void BaocButton(IRequestCycle cycle) {
		_BaocButton = true;
	}

	// 价格
//	private boolean _InsertButtong = false;

	public void InsertButtong(IRequestCycle cycle) {
//		_InsertButtong = true;
	}

//	private boolean _DeleteButtong = false;

	public void DeleteButtong(IRequestCycle cycle) {
//		_DeleteButtong = true;
	}

//	 增扣款
//	private boolean _InsertButtonk = false;

	public void InsertButtonk(IRequestCycle cycle) {
//		_InsertButtonk = true;
	}

//	private boolean _DeleteButtonk = false;

	public void DeleteButtonk(IRequestCycle cycle) {
//		_DeleteButtonk = true;
	}
	
//	private boolean _SaveChick = false;
//
//	public void SaveButton(IRequestCycle cycle) {
//		_SaveChick = true;
//	}
	
	private boolean tijshButton=false;
	
	public void TijshButton(IRequestCycle cycle){
		tijshButton=true;
	}
	
	public void submit(IRequestCycle cycle) {
		// 模板管理操作
//		 if (_SaveChick) {
//			 _SaveChick = false;
//			 Baoc();
//		 }
		// if (_DakButton) {
		// _DakButton = false;
		// Dak();
		// }
		if (_ShancButton) {
			_ShancButton = false;
			Shanc();
		}
		if (_BaocButton) {
			_BaocButton = false;
			Baoc();
		}

		if(tijshButton){
			tijshButton=false;
			this.tijshlc();
		}
		// 价格
		/*
		if (_InsertButtong) {
			_InsertButtong = false;
			Insertg();
		}
		if (_DeleteButtong) {
			_DeleteButtong = false;
			Deleteg();
		}
		*/
	}
	
	private void tijshlc(){
		String baoccgMsg="";
		if(this.isTijsh()){
			
			
			long hetb_mb_id = getmobmcSelectValue().getId();
			
			Visit visit=(Visit)this.getPage().getVisit(); 
			if(hetb_mb_id==-1){
				baoccgMsg="请选择一个已建模板!";
				this.setMsg(baoccgMsg);
				return;
			}
			
			long Liuc_id=MainGlobal.getProperId(getILiucmcModel(), getLiucmc()); 

			Liuc.tij("HETYS_MB", hetb_mb_id, visit.getRenyID(), "", Liuc_id);
			
			baoccgMsg="数据操作成功!";
			
			this.setMsg(baoccgMsg);
			return;
		}
		baoccgMsg="请在系统中预先设置!";
		
		this.setMsg(baoccgMsg);
	}

	private void Xinj() {
		// 合同信息
		Hetysmbxxbean bean = gethetysmbxxbean();
		setliucSelectValue(getIDropDownBean(getliucSelectModel(), -1));
		// setJihxzValue(getIDropDownBean(getIJihxzModel(),-1));
		setmobmc("新建模板");
		// setshijgfSelectValue(getIDropDownBean(getshijgfSelectModel(),-1));
		setxufValue(getIDropDownBean(getshijgfSelectModel(), -1));
		// setgongfValue(getIDropDownBean(getshijgfSelectModel(),-1));
		setYunsjgfaValue(getIDropDownBean(getIYunsjgfaModel(),-1));
		bean.setGONGFDBGH("");
		bean.setGONGFDH("");
		bean.setGONGFDWDZ("");
		bean.setGONGFDWMC("");
		bean.setGONGFFDDBR("");
		bean.setGONGFKHYH("");
		bean.setGongfsh("");
		bean.setGONGFWTDLR("");
		bean.setGONGFYZBM("");
		bean.setGONGFZH("");
		bean.setGuoqsj(null);
		bean.setHetbh("");
		// bean.setHetyj("");
		bean.setQianddd("");
		bean.setQiandsj(null);
		bean.setShengxsj(null);
		bean.setXUFDBGH("");
		bean.setXUFDH("");
		bean.setXUFDWDZ("");
		bean.setXUFDWMC("");
		bean.setXUFFDDBR("");
		bean.setXUFKHYH("");
		bean.setXufsh("");
		bean.setXUFWTDLR("");
		bean.setXUFYZBM("");
		bean.setXUFZH("");
		// setFahr("");
		// 价格信息
//		geteditValuesg().clear();
		getHetysjgExt(-1);
		
		//增扣款
		getYunshtzkkExt(-1);
		
		// 文字信息
		setWenz("");
	}
	
//	价格
	public ExtGridUtil getExtGrid() {
		if(((Visit) this.getPage().getVisit()).getExtGrid1()==null){
		}
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridScript() {
		if(getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	
//	增扣款
	public ExtGridUtil getExtGridk() {
		if(((Visit) this.getPage().getVisit()).getExtGrid2()==null){
		}
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setExtGridk(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}
	
	public String getGridScriptk() {
		if(getExtGridk() == null) {
			return "";
		}
		return getExtGridk().getGridScript();
	}

	public String getGridHtmlk() {
		if(getExtGridk() == null) {
			return "";
		}
		return getExtGridk().getHtml();
	}
	
public void saveHetyszkk(JDBCcon con,long hetysmb_id){
		
		Visit visit = (Visit)getPage().getVisit();
		StringBuffer sb=new StringBuffer();
		sb.append("begin ");
		int i = 0;
		
		int id = 0;
		
		ResultSetList rssb=getExtGridk().getDeleteResultSet(getChangek());
		if(rssb==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "hetyszkkb.Save 中 getExtGrid().getDeleteResultSet(getChange()) 方法没有正确返回记录集。");
//			setMsg(ErrorMessage.NullResult);
			return;
		}
		
		ResultSetList rsl = getExtGridk().getModifyResultSet(getChangek());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "hetyszkkb.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
//			setMsg(ErrorMessage.NullResult);
			return;
		}
		
		while(rssb.next()){
			id=rssb.getInt("id");
			sb.append("delete from hetyszkkb where id="+id+";\n");
			i++;
		}
		while(rsl.next()) {
			id=rsl.getInt("id");
			if(id==0){
				sb.append("insert into hetyszkkb (id, zhibb_id, tiaojb_id, shangx, xiax, danwb_id, jis, jisdwid,"
						+" kouj, koujdw, zengfj, zengfjdw, xiaoscl, jizzkj, jizzb, canzxm, canzxmdw, canzsx, canzxx, hetjsxsb_id, yunsfsb_id, beiz, hetys_id) \n");
				sb.append("values (getnewid(").append(visit.getDiancxxb_id()).append("),").append((getExtGridk().getColumn("zhibb_id").combo).getBeanId(rsl.getString("zhibb_id"))).append(",");
				sb.append((getExtGridk().getColumn("tiaojb_id").combo).getBeanId(rsl.getString("tiaojb_id"))).append(",");
				sb.append(rsl.getDouble("shangx")).append(",").append(rsl.getDouble("xiax")).append(",").append((getExtGridk().getColumn("danwb_id").combo).getBeanId(rsl.getString("danwb_id"))).append(",");
				sb.append(rsl.getDouble("jis")).append(",").append((getExtGridk().getColumn("jisdwid").combo).getBeanId(rsl.getString("jisdwid"))).append(",");
				sb.append(rsl.getDouble("kouj")).append(",").append((getExtGridk().getColumn("koujdw").combo).getBeanId(rsl.getString("koujdw"))).append(",");
				sb.append(rsl.getDouble("zengfj")).append(",").append((getExtGridk().getColumn("zengfjdw").combo).getBeanId(rsl.getString("zengfjdw"))).append(",");
				sb.append((getExtGridk().getColumn("xiaoscl").combo).getBeanId(rsl.getString("xiaoscl"))).append(",");
				sb.append(rsl.getDouble("jizzkj")).append(",").append(rsl.getDouble("jizzb")).append(",");
				sb.append((getExtGridk().getColumn("canzxm").combo).getBeanId(rsl.getString("canzxm"))).append(",");
				sb.append((getExtGridk().getColumn("canzxmdw").combo).getBeanId(rsl.getString("canzxmdw"))).append(",");
				sb.append(rsl.getDouble("canzsx")).append(",").append(rsl.getDouble("canzxx")).append(",");
				sb.append((getExtGridk().getColumn("hetjsxsb_id").combo).getBeanId(rsl.getString("hetjsxsb_id"))).append(",");
				sb.append((getExtGridk().getColumn("yunsfsb_id").combo).getBeanId(rsl.getString("yunsfsb_id"))).append(",'");
				sb.append(rsl.getString("beiz")).append("',").append(hetysmb_id).append(");\n") ;

			}else{
				
				sb.append("update hetyszkkb\n"); 
				sb.append("   set jis = ").append(rsl.getDouble("jis")).append(",\n");
				sb.append("       jisdwid = ").append((getExtGridk().getColumn("jisdwid").combo).getBeanId(rsl.getString("jisdwid"))).append(",\n");
				sb.append("       kouj = ").append(rsl.getDouble("kouj")).append(",\n");
				sb.append("       koujdw = ").append((getExtGridk().getColumn("koujdw").combo).getBeanId(rsl.getString("koujdw"))).append(",\n");
				sb.append("       zengfj = ").append(rsl.getDouble("zengfj")).append(",\n");
				sb.append("       zengfjdw = ").append((getExtGridk().getColumn("zengfjdw").combo).getBeanId(rsl.getString("zengfjdw"))).append(",\n");
				sb.append("       xiaoscl = ").append((getExtGridk().getColumn("xiaoscl").combo).getBeanId(rsl.getString("xiaoscl"))).append(",\n");
				sb.append("       jizzkj = ").append(rsl.getDouble("jizzkj")).append(",\n");
				sb.append("       jizzb = ").append(rsl.getDouble("jizzb")).append(",\n");
				sb.append("       canzxm = ").append((getExtGridk().getColumn("canzxm").combo).getBeanId(rsl.getString("canzxm"))).append(",\n");
				sb.append("       canzxmdw = ").append((getExtGridk().getColumn("canzxmdw").combo).getBeanId(rsl.getString("canzxmdw"))).append(",\n");
				sb.append("       canzsx = ").append(rsl.getDouble("canzsx")).append(",\n");
				sb.append("       canzxx = ").append(rsl.getDouble("canzxx")).append(",\n");
				sb.append("       hetjsxsb_id = ").append((getExtGridk().getColumn("hetjsxsb_id").combo).getBeanId(rsl.getString("hetjsxsb_id"))).append(",\n");
				sb.append("       yunsfsb_id = ").append((getExtGridk().getColumn("yunsfsb_id").combo).getBeanId(rsl.getString("yunsfsb_id"))).append(",\n");
				sb.append("       beiz = '").append(rsl.getString("beiz")).append("',\n");
				sb.append("       zhibb_id = ").append((getExtGridk().getColumn("zhibb_id").combo).getBeanId(rsl.getString("zhibb_id"))).append(",\n");
				sb.append("       tiaojb_id = ").append((getExtGridk().getColumn("tiaojb_id").combo).getBeanId(rsl.getString("tiaojb_id"))).append(",\n");
				sb.append("       shangx = ").append(rsl.getDouble("shangx")).append(",\n");
				sb.append("       xiax = ").append(rsl.getDouble("xiax")).append(",\n");
				sb.append("       danwb_id = ").append((getExtGridk().getColumn("danwb_id").combo).getBeanId(rsl.getString("danwb_id"))).append("\n");
				sb.append(" where id = "+id+";");

			}
			i++;
		}
		sb.append("end;");
		if(i>0){
			con.getInsert(sb.toString());
		}
		setChangek(null);
	}
	
	
	public void saveHetysjg(JDBCcon con,long hetysmb_id){
		
		Visit visit = (Visit)getPage().getVisit();
		StringBuffer sb=new StringBuffer();
		sb.append("begin ");
		int i = 0;
		
		int id = 0;
		
		ResultSetList rssb=getExtGrid().getDeleteResultSet(getChange());
		if(rssb==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "hetysjgb.Save 中 getExtGrid().getDeleteResultSet(getChange()) 方法没有正确返回记录集。");
//			setMsg(ErrorMessage.NullResult);
			return;
		}
		
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "hetysjgb.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
//			setMsg(ErrorMessage.NullResult);
			return;
		}
		
		while(rssb.next()){
			id=rssb.getInt("id");
			sb.append("delete from hetysjgb where id="+id+";\n");
			i++;
		}
		while(rsl.next()) {
			id=rsl.getInt("id");
			if(id==0){
				sb.append("insert into hetysjgb (id, hetys_id, meikxxb_id, zhibb_id, tiaojb_id, shangx, xiax, danwb_id, yunja, yunjdw_id) \n");
				sb.append("values (getnewid(").append(visit.getDiancxxb_id()).append("),").append(hetysmb_id).append(",").append((getExtGrid().getColumn("meikxxb_id").combo).getBeanId(rsl.getString("meikxxb_id"))).append(",");
				sb.append((getExtGrid().getColumn("zhibb_id").combo).getBeanId(rsl.getString("zhibb_id"))).append(",").append((getExtGrid().getColumn("tiaojb_id").combo).getBeanId(rsl.getString("tiaojb_id"))).append(",");
				sb.append(rsl.getDouble("shangx")).append(",").append(rsl.getDouble("xiax")).append(",").append((getExtGrid().getColumn("danwb_id").combo).getBeanId(rsl.getString("danwb_id"))).append(",");
				sb.append(rsl.getDouble("yunja")).append(",").append((getExtGrid().getColumn("yunjdw_id").combo).getBeanId(rsl.getString("yunjdw_id"))).append(");\n") ;

			}else{
				
				sb.append("update HETYSJGB set meikxxb_id = " + (getExtGrid().getColumn("meikxxb_id").combo).getBeanId(rsl.getString("meikxxb_id")));
				sb.append(",       zhibb_id = " + (getExtGrid().getColumn("zhibb_id").combo).getBeanId(rsl.getString("zhibb_id")));
				sb.append(",       tiaojb_id = " + (getExtGrid().getColumn("tiaojb_id").combo).getBeanId(rsl.getString("tiaojb_id")));
				sb.append(",       shangx = " + rsl.getDouble("shangx") + ",       xiax = "+ rsl.getDouble("xiax")); 
				sb.append(",       danwb_id = " + (getExtGrid().getColumn("danwb_id").combo).getBeanId(rsl.getString("danwb_id")));
				sb.append(",       yunja = " + rsl.getDouble("yunja"));
				sb.append(",       yunjdw_id = " + (getExtGrid().getColumn("yunjdw_id").combo).getBeanId(rsl.getString("yunjdw_id")));
				sb.append(" where id=" + id+";\n");
				
			}
			i++;
		}
		sb.append("end;");
		if(i>0){
			con.getInsert(sb.toString());
		}
		setChange(null);
		
	}
	
	
	public void getHetysjgExt(long hetysmb_id){
		JDBCcon con = new JDBCcon();
		
	   	 String sql="select decode(j.id,null,0,j.id) as id, j.hetys_id, m.mingc as meikxxb_id, z.mingc as zhibb_id, zd.mingc as danwb_id, t.mingc as tiaojb_id, j.xiax, j.shangx,j. yunja, yd.mingc as yunjdw_id \n "
	   		 +" from hetysjgb j,meikxxb m,zhibb z,tiaojb t,danwb zd, danwb yd \n"
	   		 +" where j.meikxxb_id=m.id(+) and j.zhibb_id=z.id(+) and j.tiaojb_id=t.id(+) and j.danwb_id=zd.id(+) and j.yunjdw_id=yd.id(+) and hetys_id="+hetysmb_id+" order by z.mingc,j.xiax";
	   	 ResultSetList rsl=con.getResultSetList(sql);
	   	 
	   	 ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
 		egu.setTableName("hetysjgb");
 		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight-135");
 		egu.getColumn("id").setHeader("ID");
 		egu.getColumn("id").setHidden(true);
 		egu.getColumn("id").setEditor(null);
 		egu.getColumn("hetys_id").setHidden(true);
 		egu.getColumn("hetys_id").setEditor(null);
 		egu.getColumn("meikxxb_id").setHeader("煤矿单位");
 		egu.getColumn("zhibb_id").setHeader("指标");
 		egu.getColumn("danwb_id").setHeader("指标单位");
 		egu.getColumn("tiaojb_id").setHeader("条件");
 		egu.getColumn("xiax").setHeader("下限");
 		egu.getColumn("xiax").setDefaultValue("0");
 		egu.getColumn("shangx").setHeader("上限");
 		egu.getColumn("shangx").setDefaultValue("0");
 		egu.getColumn("yunja").setHeader("运价");
 		egu.getColumn("yunja").setDefaultValue("0");
 		egu.getColumn("yunjdw_id").setHeader("运价单位");
 		

 		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
 		egu.addPaging(30);

//			 设置到站下拉框
		ComboBox c1 = new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(c1);
		c1.setEditable(true);
		String Sql1 = 
			"select id,mingc from\n" +
			"((select 0 as xuh,0 as id,'' as mingc from dual\n" + 
			" union\n" + 
			"select 1 as xuh,id, mingc from meikxxb)\n" + 
			" order by xuh,mingc)";

		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,new IDropDownModel(Sql1));
		egu.getColumn("meikxxb_id").setReturnId(true);
		
		ComboBox c2 = new ComboBox();
		egu.getColumn("zhibb_id").setEditor(c2);
		c2.setEditable(true);
//		c2.setListeners("select:function(e){ \n"
//				
//				+"	document.all.item('cbo_gridDiv_DANWB_ID').length=0;\n"
//				+"	//zhibb_id=oldSelectobjg.options(oldSelectobjg.selectedIndex).value; \n"
//				+"	var j=0; \n"
//				+"	for(i=0;i<zhib_danw.length;i++){ \n"
//				+"		if(zhibb_id==zhib_danw[i][0]){ \n"
//				+"			document.all.item('cbo_gridDiv_DANWB_ID').options[j++]=new  Option(zhib_danw[i][2],zhib_danw[i][1]); \n"
//				+"		} \n"
//				+"	} \n"
//				+"	if(document.all.item('cbo_gridDiv_DANWB_ID').options(0)!=null){ \n"
//				+"		document.all.item('DANWB_ID',oldClickrowg).value=document.all.item('cbo_gridDiv_DANWB_ID').options(0).text; \n"
//				+"	}else{ \n"
//				+"		document.all.item('DANWB_ID',oldClickrowg).value=''; \n"
//				+"	}"	
//				+"}"
//				
//		);
		String Sql2 =
			"select id,mingc from\n" +
			"((select 0 as xuh,0 as id,'' as mingc from dual\n" + 
			"        union\n" + 
			"	select 1 as xuh,id,mingc from zhibb where zhibb.leib=1\n" + 
			"		and zhibb.bianm in ('"+Locale.Yunju_zhibb+"'))\n" + 
			"		order by xuh,mingc)";


		egu.getColumn("zhibb_id").setComboEditor(egu.gridId,new IDropDownModel(Sql2));
		egu.getColumn("zhibb_id").setReturnId(true);
		
		ComboBox c3 = new ComboBox();
		egu.getColumn("danwb_id").setEditor(c3);
		c3.setEditable(true);
		String Sql3 =
			"select id,mingc from \n" +
			"((select 0 as xuh,0 as id,'' as mingc from dual\n" +
			"		union\n" + 
			"	select 1 as xuh,id,mingc from danwb d where zhibb_id\n" + 
			"       in (select id from zhibb where leib=1 and bianm in ('"
			+Locale.Yunju_zhibb+"')))\n" + 
			"order by xuh,mingc)";

		egu.getColumn("danwb_id").setComboEditor(egu.gridId,new IDropDownModel(Sql3));
		egu.getColumn("danwb_id").setReturnId(true);
		
		ComboBox c4 = new ComboBox();
		egu.getColumn("tiaojb_id").setEditor(c4);
		c4.setEditable(true);
		String Sql4 =
			"select id,mingc from \n" +
			"((select 0 as xuh,0 as id,'' as mingc from dual\n" +
			"		union\n" + 
			"	select 1 as xuh,id,mingc from tiaojb where tiaojb.leib=1)\n" + 
			"	order by xuh,mingc)";

		egu.getColumn("tiaojb_id").setComboEditor(egu.gridId,new IDropDownModel(Sql4));
		egu.getColumn("tiaojb_id").setReturnId(true);
		
		ComboBox c5 = new ComboBox();
		egu.getColumn("yunjdw_id").setEditor(c5);
		c5.setEditable(true);
		String Sql5 = 
			"select id,mingc from danwb d where zhibb_id\n" + 
			"       in (select id from zhibb where leib=1 and bianm in ('"
			+Locale.Yunja_zhibb+"'))\n" + 
			"order by mingc";
		egu.getColumn("yunjdw_id").setComboEditor(egu.gridId,new IDropDownModel(Sql5));
		egu.getColumn("yunjdw_id").setReturnId(true);
		
		
		
		
//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		GridButton gb1=new GridButton(GridButton.ButtonType_Insert,egu.gridId,egu.getGridColumns(),null);
		
		if(this.isBtDisabled()){
			gb1.setDisabled(true);
		}
		egu.addTbarBtn(gb1);
		
		egu.addTbarText("-");
//		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		
		GridButton gb2=new GridButton(GridButton.ButtonType_Delete,egu.gridId,egu.getGridColumns(),null);
		if(this.isBtDisabled()){
			gb2.setDisabled(true);
		}
		egu.addTbarBtn(gb2);
//		egu.addTbarText("-");
//		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		 
//		sbsql.append("function changeSelect(){");
//		sbsql.append("if(oldSelectobjg.id=='TiaojSelect'){ \n");
//		sbsql.append("if(objselectColg.value=='小于'||objselectColg.value=='小于等于'){ \n");
//		sbsql.append("document.all.item('xiax',oldClickrowg).disabled=true; \n");
//		sbsql.append("document.all.item('xiax',oldClickrowg).value=0; \n");
//		sbsql.append("document.all.item('shangx',oldClickrowg).disabled=false; \n");
//		sbsql.append("}else if(objselectColg.value=='大于'||objselectColg.value=='大于等于'||objselectColg.value=='等于'){ \n");
//		sbsql.append("document.all.item('shangx',oldClickrowg).disabled=true; \n");
//		sbsql.append("document.all.item('shangx',oldClickrowg).value=0; \n");
//		sbsql.append("document.all.item('xiax',oldClickrowg).disabled=false; \n");
//		sbsql.append("}else{ \n");
//		sbsql.append("document.all.item('xiax',oldClickrowg).disabled=false; \n");
//		sbsql.append("document.all.item('shangx',oldClickrowg).disabled=false; \n");
//		sbsql.append("} \n");
//		sbsql.append("} \n");
//		sbsql.append("if(oldSelectobjg.id=='ZhibSelect'){ \n");
//		sbsql.append("document.all.item('ZhibdwSelect').length=0; \n");
//		sbsql.append("zhibb_id=oldSelectobjg.options(oldSelectobjg.selectedIndex).value; \n");
//		sbsql.append("var j=0; \n");
//		sbsql.append("for(i=0;i<zhib_danw.length;i++){ \n");
//		sbsql.append("if(zhibb_id==zhib_danw[i][0]){ \n");
//		sbsql.append("document.all.item('ZhibdwSelect').options[j++]=new  Option(zhib_danw[i][2],zhib_danw[i][1]); \n");
//		sbsql.append("} \n");
//		sbsql.append("} \n");
//		sbsql.append("if(document.all.item('ZhibdwSelect').options(0)!=null){ \n");
//		sbsql.append("document.all.item('ZHIBDW_ID',oldClickrowg).value=document.all.item('ZhibdwSelect').options(0).text; \n");
//		sbsql.append("}else{ \n");
//		sbsql.append("document.all.item('HIBDW_ID',oldClickrowg).value=''; \n");
//		sbsql.append("} \n");
//		sbsql.append("} \n");
//		sbsql.append("} \n");
		
//		sbsql.append("gridDiv_grid.on('afteredit',function(e){");
//		sbsql.append("	if(e.field == 'zhibb_id'){ \n");
//		sbsql.append("		document.all.item('cbo_gridDiv_DANWB_ID').length=0;\n");
//		sbsql.append("		for(i=0;i<zhib_danw.length;i++){ ");
//		sbsql.append("			document.all.item('cbo_gridDiv_DANWB_ID').options[j++]=new  Option(zhib_danw[i][2],zhib_danw[i][1]); \n");
//		sbsql.append("		} \n");
//		sbsql.append("	} \n");
//		sbsql.append("	if(document.all.item('cbo_gridDiv_DANWB_ID').options(0)!=null){ \n");
//		sbsql.append("		e.record.set('DANWB_ID',document.all.item('cbo_gridDiv_DANWB_ID').options(0).text); \n");
//		sbsql.append("	}else{ \n");
//		sbsql.append("		e.record.set('DANWB_ID',''); \n");
//		sbsql.append("	} \n");
//		sbsql.append("}); \n");
//		
//		egu.addOtherScript(sbsql.toString());
		
		setExtGrid(egu);
		con.Close();
	}
	
	private void Dak(long hetysmb_id){
	    	String sql="";
	    	JDBCcon con=new JDBCcon();
		    try{
		    	// 合同信息
		         sql=
		        	"select ID,HETBH,QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR," +
		        	"GONGFWTDLR,GONGFDBGH,GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR," + 
		        	"XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,QISRQ,GUOQRQ," + 
		        	"LIUCB_ID,MINGC,diancxxb_id,yunsjgfab_id \n" + 
		        	"from hetys_mb" +
		        	" where ID="+hetysmb_id;
		         ResultSet rs=con.getResultSet(sql);
		         if(rs.next()){
			    	 Hetysmbxxbean bean=gethetysmbxxbean();
			    	 setliucSelectValue(getIDropDownBean(getliucSelectModel(),rs.getLong("LIUCB_ID")));
			    	 setYunsjgfaValue(getIDropDownBean(getIYunsjgfaModel(),rs.getLong("yunsjgfab_id")));
		// setJihxzValue(getIDropDownBean(getIJihxzModel(),rs.getLong("JIHKJB_ID")));
			    	 setmobmc(rs.getString("MINGC"));
		// setshijgfSelectValue(getIDropDownBean(getshijgfSelectModel(),rs.getLong("GONGYSB_ID")));
			    	 setxufValue(getIDropDownBean(getxufModel(),rs.getLong("diancxxb_id")));
		// setgongfValue(getIDropDownBean(getgongfModel(),rs.getLong("HETGYSBID")));
			    	 bean.setGONGFDBGH(rs.getString("GONGFDBGH"));
			    	 bean.setGONGFDH(rs.getString("GONGFDH"));
			    	 bean.setGONGFDWDZ(rs.getString("GONGFDWDZ"));
			    	 bean.setGONGFDWMC(rs.getString("GONGFDWMC"));
			    	 bean.setGONGFFDDBR(rs.getString("GONGFFDDBR"));
			    	 bean.setGONGFKHYH(rs.getString("GONGFKHYH"));
			    	 bean.setGongfsh(rs.getString("Gongfsh"));
			    	 bean.setGONGFWTDLR(rs.getString("GONGFWTDLR"));
			    	 bean.setGONGFYZBM(rs.getString("GONGFYZBM"));
			    	 bean.setGONGFZH(rs.getString("GONGFZH"));
			    	 bean.setGuoqsj(rs.getDate("GUOQRQ"));
			    	 bean.setHetbh(rs.getString("Hetbh"));
		// bean.setHetyj(rs.getString("Hetyj"));
			    	 bean.setQianddd(rs.getString("Qianddd"));
			    	 bean.setQiandsj(rs.getDate("QIANDRQ"));
			    	 bean.setShengxsj(rs.getDate("QISRQ"));
			    	 bean.setXUFDBGH(rs.getString("XUFDBGH"));
			    	 bean.setXUFDH(rs.getString("XUFDH"));
			    	 bean.setXUFDWDZ(rs.getString("XUFDWDZ"));
			    	 bean.setXUFDWMC(rs.getString("XUFDWMC"));
			    	 bean.setXUFFDDBR(rs.getString("XUFFDDBR"));
			    	 bean.setXUFKHYH(rs.getString("XUFKHYH"));
			    	 bean.setXufsh(rs.getString("Xufsh"));
			    	 bean.setXUFWTDLR(rs.getString("XUFWTDLR"));
			    	 bean.setXUFYZBM(rs.getString("XUFYZBM"));
			    	 bean.setXUFZH(rs.getString("XUFZH"));
		// setFahr(rs.getString("meikmcs"));
		         }
		    	
		    	// 价格信息
		         getHetysjgExt(hetysmb_id);
		         
		         /*
		    	 List list = geteditValuesg();
		    	 list.clear();
		    	 sql=
		    		 "select * from hetysjgb where hetys_id="+hetysmb_id;
		    	 ResultSet rs3=con.getResultSet(sql);
		    	 
		    	 int i=0;
		    	 while(rs3.next()){
		    		 long id=rs3.getLong("id");
		    		 String meikxxb_id = getProperValue(getMeikxxModel(),rs3.getLong("meikxxb_id"));
		             double yunjia = rs3.getDouble("yunja");
		             String yunjdw_id = getProperValue(getYunjiadwModel(),rs3.getLong("yunjdw_id"));
		             
		             String zhibb_id = getProperValue(getZhibSelectModel(),rs3.getLong("zhibb_id"));
		        	 String tiaoj_id = getProperValue(gettiaojjSelectModel(),rs3.getLong("tiaojb_id"));
		        	 double shangx = rs3.getDouble("shangx");
		        	 double xiax = rs3.getDouble("xiax");
		             String zhibdw_id = getProperValue(getzhibdwSelectModel(),rs3.getLong("danwb_id"));
		             
		    		 list.add(new Yunsmbjijbean(++i,id,meikxxb_id,yunjia,yunjdw_id,zhibb_id,zhibdw_id,tiaoj_id,shangx,xiax));
		    	 }
	*/
		         //增扣款
		         getYunshtzkkExt(hetysmb_id);
		    	 
		    	// 文字信息
		    	 sql= "select id,wenznr\n" +
		    	 "from hetyswzb\n" + 
		    	 "where hetys_id="+hetysmb_id;
		    	 ResultSet rs4=con.getResultSet(sql);
		    	 if(rs4.next()){
		    		 setWenz(rs4.getString("wenznr"));
		    	 }
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}finally{
	    		 con.Close();
	    	}
		 }

	public void getYunshtzkkExt(long hetysmb_id){
		
		JDBCcon con = new JDBCcon();
		
	   	 String sql = "select  decode(z.id,null,0,z.id) as id,z.hetys_id,zhibb.mingc as zhibb_id,tiaojb.mingc as tiaojb_id,z.xiax,z.shangx,zhibdw.mingc as danwb_id,\n"
					+ "       z.jis,jisdw.mingc as jisdwid,z.kouj, koujdw.mingc as koujdw,z.ZENGFJ,zengfjdw.mingc as zengfjdw,\n"
					+ "       decode(z.xiaoscl,1,'进位',2,'舍去',3,'四舍五入',4,'四舍五入(0.1)',5,'四舍五入(0.01)','') as xiaoscl,\n"
					+ "       z.jizzkj,z.jizzb,CANZXM.Mingc as canzxm,CANZXMDW.Mingc as canzxmdw,z.canzxx,z.canzsx,hetjsxsb.mingc as hetjsxsb_id,\n"
					+ "       yunsfsb.mingc as yunsfsb_id, z.BEIZ\n"
					+ "  from hetyszkkb z,zhibb,tiaojb,danwb zhibdw,danwb jisdw,danwb koujdw,danwb zengfjdw,zhibb canzxm,danwb canzxmdw,hetjsxsb,yunsfsb\n"
					+ " where z.zhibb_id=zhibb.id  and z.tiaojb_id=tiaojb.id and z.danwb_id=zhibdw.id and z.jisdwid=jisdw.id(+) and z.koujdw=koujdw.id(+)\n"
					+ "   and z.zengfjdw=zengfjdw.id(+) and z.canzxm=canzxm.id(+) and z.canzxmdw=canzxmdw.id(+) and z.hetjsxsb_id=hetjsxsb.id(+)\n"
					+ "   and z.yunsfsb_id=yunsfsb.id(+) and z.hetys_id="+hetysmb_id+ " order by zhibb.mingc,z.xiax";

	   	 ResultSetList rsl=con.getResultSetList(sql);
	   	 
	   	 ExtGridUtil egu = new ExtGridUtil("gridDivk", rsl);
		egu.setTableName("hetyszkkb");
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight-135");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("hetys_id").setHidden(true);
		egu.getColumn("hetys_id").setEditor(null);
		egu.getColumn("zhibb_id").setHeader("项目");
		egu.getColumn("tiaojb_id").setHeader("条件");
		egu.getColumn("xiax").setHeader("下限");
		egu.getColumn("xiax").setDefaultValue("0");
		egu.getColumn("shangx").setHeader("上限");
		egu.getColumn("shangx").setDefaultValue("0");
		
		egu.getColumn("danwb_id").setHeader("项目单位");
		egu.getColumn("jis").setHeader("基数");
		egu.getColumn("jis").setDefaultValue("0");
		egu.getColumn("jisdwid").setHeader("基数单位");
		
		egu.getColumn("kouj").setHeader("扣价");
		egu.getColumn("kouj").setDefaultValue("0");
		egu.getColumn("koujdw").setHeader("扣价单位");
		
		egu.getColumn("zengfj").setHeader("增付价");
		egu.getColumn("zengfj").setDefaultValue("0");
		egu.getColumn("zengfjdw").setHeader("增付价单位");
		
		egu.getColumn("xiaoscl").setHeader("小数处理方式");
		
		egu.getColumn("jizzkj").setHeader("基准增扣价");
		egu.getColumn("jizzkj").setDefaultValue("0");
		egu.getColumn("jizzb").setHeader("基准指标");
		egu.getColumn("jizzb").setDefaultValue("0");
		egu.getColumn("canzxm").setHeader("参照项目");
		egu.getColumn("canzxmdw").setHeader("参照项目单位");
		egu.getColumn("canzxx").setHeader("下限");
		egu.getColumn("canzxx").setDefaultValue("0");
		egu.getColumn("canzsx").setHeader("上限");
		egu.getColumn("canzsx").setDefaultValue("0");
		
		egu.getColumn("hetjsxsb_id").setHeader("加权方式");
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("beiz").setHeader("备注");
		

		egu.getColumn("zhibb_id").setWidth(100);
		egu.getColumn("tiaojb_id").setWidth(60);
		egu.getColumn("xiax").setWidth(50);
		egu.getColumn("shangx").setWidth(50);
		egu.getColumn("danwb_id").setWidth(70);
		egu.getColumn("jis").setWidth(50);
		egu.getColumn("jisdwid").setWidth(70);
		egu.getColumn("kouj").setWidth(50);
		egu.getColumn("koujdw").setWidth(70);
		egu.getColumn("zengfj").setWidth(60);
		egu.getColumn("zengfjdw").setWidth(80);
		egu.getColumn("xiaoscl").setWidth(80);
		egu.getColumn("jizzkj").setWidth(70);
		egu.getColumn("jizzb").setWidth(60);
		egu.getColumn("canzxm").setWidth(80);
		egu.getColumn("canzxmdw").setWidth(80);
		egu.getColumn("canzxx").setWidth(60);
		egu.getColumn("canzsx").setWidth(60);
		egu.getColumn("hetjsxsb_id").setWidth(70);
		egu.getColumn("yunsfsb_id").setWidth(70);
		egu.getColumn("beiz").setWidth(200);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(30);

//			 设置到站下拉框
		ComboBox c1 = new ComboBox();
		egu.getColumn("zhibb_id").setEditor(c1);
		c1.setEditable(true);
		String Sql1 = "select id,mingc from zhibb order by mingc ";
		egu.getColumn("zhibb_id").setComboEditor(egu.gridId,new IDropDownModel(Sql1));
		egu.getColumn("zhibb_id").setReturnId(true);
		
		ComboBox c2 = new ComboBox();
		egu.getColumn("tiaojb_id").setEditor(c2);
		c2.setEditable(true);
		String Sql2 = "select id,mingc from tiaojb where tiaojb.leib=1 ";
		egu.getColumn("tiaojb_id").setComboEditor(egu.gridId,new IDropDownModel(Sql2));
		egu.getColumn("tiaojb_id").setReturnId(true);
		
		ComboBox c3 = new ComboBox();
		egu.getColumn("danwb_id").setEditor(c3);
		c3.setEditable(true);
		String Sql3 = "select id,mingc from danwb ";
		egu.getColumn("danwb_id").setComboEditor(egu.gridId,new IDropDownModel(Sql3));
		egu.getColumn("danwb_id").setReturnId(true);
		
		ComboBox c4 = new ComboBox();
		egu.getColumn("jisdwid").setEditor(c4);
		c4.setEditable(true);
		String Sql4 = "select id,mingc from danwb ";
		egu.getColumn("jisdwid").setComboEditor(egu.gridId,new IDropDownModel(Sql4));
		egu.getColumn("jisdwid").setReturnId(true);
		
		ComboBox c5 = new ComboBox();
		egu.getColumn("koujdw").setEditor(c5);
		c5.setEditable(true);
		String Sql5 = "select id,mingc from danwb ";
		egu.getColumn("koujdw").setComboEditor(egu.gridId,new IDropDownModel(Sql5));
		egu.getColumn("koujdw").setReturnId(true);
		
		ComboBox c6 = new ComboBox();
		egu.getColumn("zengfjdw").setEditor(c6);
		c6.setEditable(true);
		String Sql6 = "select id,mingc from danwb ";
		egu.getColumn("zengfjdw").setComboEditor(egu.gridId,new IDropDownModel(Sql6));
		egu.getColumn("zengfjdw").setReturnId(true);
		
		ComboBox c7 = new ComboBox();
		egu.getColumn("xiaoscl").setEditor(c7);
		c7.setEditable(true);
		List list=new ArrayList();
		list.add(new IDropDownBean(0," "));
		list.add(new IDropDownBean(1,"进位"));
		list.add(new IDropDownBean(2,"舍去"));
		list.add(new IDropDownBean(3,"四舍五入"));
		list.add(new IDropDownBean(4,"四舍五入(0.1)"));
		list.add(new IDropDownBean(5,"四舍五入(0.01)"));
		egu.getColumn("xiaoscl").setComboEditor(egu.gridId,new IDropDownModel(list));
		egu.getColumn("xiaoscl").setReturnId(true);
		
		ComboBox c8 = new ComboBox();
		egu.getColumn("canzxm").setEditor(c8);
		c8.setEditable(true);
		String Sql8 = "select id,mingc from zhibb order by mingc ";
		egu.getColumn("canzxm").setComboEditor(egu.gridId,new IDropDownModel(Sql8));
		egu.getColumn("canzxm").setReturnId(true);
		
		ComboBox c9 = new ComboBox();
		egu.getColumn("canzxmdw").setEditor(c9);
		c9.setEditable(true);
		String Sql9 = "select id,mingc from danwb  ";
		egu.getColumn("canzxmdw").setComboEditor(egu.gridId,new IDropDownModel(Sql9));
		egu.getColumn("canzxmdw").setReturnId(true);
		
		ComboBox c10 = new ComboBox();
		egu.getColumn("hetjsxsb_id").setEditor(c10);
		c10.setEditable(true);
		String Sql10 = "select id,mingc from hetjsxsb order by mingc ";
		egu.getColumn("hetjsxsb_id").setComboEditor(egu.gridId,new IDropDownModel(Sql10));
		egu.getColumn("hetjsxsb_id").setReturnId(true);
		
		ComboBox c11 = new ComboBox();
		egu.getColumn("yunsfsb_id").setEditor(c11);
		c11.setEditable(true);
		String Sql11 = "select id,mingc from yunsfsb order by mingc ";
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,new IDropDownModel(Sql11));
		egu.getColumn("yunsfsb_id").setReturnId(true);
		
		
//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		
		GridButton gb1=new GridButton(GridButton.ButtonType_Insert,egu.gridId,egu.getGridColumns(),null);
		
		if(this.isBtDisabled()){
			gb1.setDisabled(true);
		}
		egu.addTbarBtn(gb1);
		
		egu.addTbarText("-");
//		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		
		GridButton gb2=new GridButton(GridButton.ButtonType_Delete,egu.gridId,egu.getGridColumns(),null);
		
		if(this.isBtDisabled()){
			gb2.setDisabled(true);
		}
		egu.addTbarBtn(gb2);
		
//		egu.addTbarText("-");
//		egu.addToolbarButton(GridButton.ButtonType_Save, null);
		
		setExtGridk(egu);
		con.Close();
		
	}
	
	
	private void getGongf() {
		Hetysmbxxbean htxxbean = gethetysmbxxbean();
		JDBCcon con = new JDBCcon();
		String sql = "select quanc,danwdz,YOUZBM,shuih,FADDBR,WEITDLR,KAIHYH,ZHANGH,DIANH\n"
				+ "from gongysb where id=" + getgongfValue().getId();
		// "from yunsdwb where id="+getgongfValue().getId();
		ResultSet rs = con.getResultSet(sql);
		try {
			if (rs.next()) {
				String Gongfdn = rs.getString("DIANH");
				String Gongfdwdz = rs.getString("danwdz");
				String Gongfdwmc = rs.getString("quanc");
				String Gongffddbr = rs.getString("FADDBR");
				String Gongfzh = rs.getString("ZHANGH");
				String Gongfkhyh = rs.getString("KAIHYH");
				String Gongfyzbm = rs.getString("YOUZBM");
				String Gongfwtdlr = rs.getString("WEITDLR");
				String shuih = rs.getString("shuih");
				htxxbean.setGONGFDH(Gongfdn);
				htxxbean.setGONGFDWDZ(Gongfdwdz);
				htxxbean.setGONGFDWMC(Gongfdwmc);
				htxxbean.setGONGFFDDBR(Gongffddbr);
				htxxbean.setGONGFZH(Gongfzh);
				htxxbean.setGONGFKHYH(Gongfkhyh);
				htxxbean.setGONGFYZBM(Gongfyzbm);
				htxxbean.setGONGFWTDLR(Gongfwtdlr);
				htxxbean.setGongfsh(shuih);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		// 发货人同时相同
		setshijgfSelectValue(getIDropDownBean(getshijgfSelectModel(),
				getgongfValue().getId()));

	}

	private void getXuf() {
		Hetysmbxxbean htxxbean = gethetysmbxxbean();
		JDBCcon con = new JDBCcon();
		String sql = "select quanc,diz,YOUZBM,shuih,FADDBR,WEITDLR,KAIHYH,ZHANGH,DIANH\n"
				+ "from diancxxb where id=" + getxufValue().getId();
		ResultSet rs = con.getResultSet(sql);
		try {
			if (rs.next()) {
				String XUFDH = rs.getString("DIANH");
				String XUFDWDZ = rs.getString("DIZ");
				String XUFDWMC = rs.getString("QUANC");
				String XUFFDDBR = rs.getString("FADDBR");
				String XUFZH = rs.getString("ZHANGH");
				String XUFKHYH = rs.getString("KAIHYH");
				String XUFYZBM = rs.getString("YOUZBM");
				String XUFWTDLR = rs.getString("WEITDLR");
				htxxbean.setXUFDH(XUFDH);
				htxxbean.setXUFDWDZ(XUFDWDZ);
				htxxbean.setXUFDWMC(XUFDWMC);
				htxxbean.setXUFFDDBR(XUFFDDBR);
				htxxbean.setXUFZH(XUFZH);
				htxxbean.setXUFKHYH(XUFKHYH);
				htxxbean.setXUFYZBM(XUFYZBM);
				htxxbean.setXUFWTDLR(XUFWTDLR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}

	private void getXuanzmb() {
		if (getmobmcSelectValue().getId() == -1) {
			Xinj();
		} else {
			Dak(getmobmcSelectValue().getId());
		}
	}

	private void Shanc() {
		String sql = "";
		JDBCcon con = new JDBCcon();
		long hetysmb_id = getmobmcSelectValue().getId();
		// 删除合同信息表
		con.setAutoCommit(false);
		try {
			sql = "delete\n" + "from hetys_mb\n" + "where id=" + hetysmb_id;
			con.getDelete(sql);

			sql = "delete \n" + "from hetysjgb\n" + "where hetysjgb.hetys_id="
					+ hetysmb_id;
			con.getDelete(sql);
			
			sql = "delete \n" + "from hetyszkkb\n" + "where hetyszkkb.hetys_id="
					+ hetysmb_id;
			con.getDelete(sql);
	
			sql = "delete\n" + "from hetyswzb\n" + "where hetyswzb.hetys_id="
					+ hetysmb_id;
			con.getDelete(sql);
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			con.rollBack();
		} finally {
			con.Close();
		}
		Xinj();
		getmobmcSelectModels();
	}

	private void Baoc() {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		Visit visit = (Visit) this.getPage().getVisit();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Hetysmbxxbean bean = gethetysmbxxbean();
		String sql = "";
		try{
			if (getmobmcSelectValue().getId() == -1) {// 如果是新建模板
				// 插入（新增）保存
				// 保存合同信息（包括供方需方）
	
				long hetysb_mb_id = Long.parseLong(MainGlobal
						.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
				sql = "insert into HETYS_MB(ID,mingc,DIANCXXB_ID,HETBH,QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR,GONGFWTDLR,"
						+ "GONGFDBGH,GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR,XUFDH,XUFDBGH,XUFKHYH,XUFZH,"
						+ "XUFYZBM,XUFSH,QISRQ,GUOQRQ,hetbmb_id,LIUCB_ID,yunsjgfab_id)\n"
						+
	
						"values("
						+ hetysb_mb_id
						+ ",'"
						+ getmobmc()
						+ "',"
						+ visit.getDiancxxb_id()
						+ ",'"
						+ bean.getHetbh()
						+ "',to_date('"
						+ format.format(bean.getQiandsj())
						+ "','YYYY-MM-DD'),'"
						+ bean.getQianddd()
						+ "','"
						+ bean.getGONGFDWMC()
						+ "','"
						+ bean.getGONGFDWDZ()
						+ "','"
						+ bean.getGONGFDH()
						+ "','"
						+ bean.getGONGFFDDBR()
						+ "','"
						+ bean.getGONGFWTDLR()
						+ "','"
						+ bean.getGONGFDBGH()
						+ "','"
						+ bean.getGONGFKHYH()
						+ "','"
						+ bean.getGONGFZH()
						+ "','"
						+ bean.getGONGFYZBM()
						+ "','"
						+ bean.getGongfsh()
						+ "','"
						+ bean.getXUFDWMC()
						+ "','"
						+ bean.getXUFDWDZ()
						+ "','"
						+ bean.getXUFFDDBR()
						+ "','"
						+ bean.getXUFWTDLR()
						+ "','"
						+ bean.getXUFDH()
						+ "','"
						+ bean.getXUFDBGH()
						+ "','"
						+ bean.getXUFKHYH()
						+ "','"
						+ bean.getXUFZH()
						+ "','"
						+ bean.getXUFYZBM()
						+ "','"
						+ bean.getXufsh()
						+ "',"
						+ "to_date('"
						+ format.format(bean.getShengxsj())
						+ "','YYYY-MM-DD'),to_date('"
						+ format.format(bean.getGuoqsj())
						+ "','YYYY-MM-DD'),"
						+ hetysb_mb_id + "," + getliucSelectValue().getId() +","+getYunsjgfaValue().getId()+ ")";
				con.getInsert(sql);
	
				// 保存价格
				if(getChange()!=null && !getChange().equals("")){
					saveHetysjg(con,hetysb_mb_id);
				}
				
				//	保存增扣款
				if(getChangek()!=null && !getChangek().equals("")){
					saveHetyszkk(con,hetysb_mb_id);
				}
				
				/*
				List list = geteditValuesg();
				for (int i = 0; i < list.size(); i++) {// 遍历容器
					long meikxxb_id = getProperId(getMeikxxModel(),
							((Yunsmbjijbean) list.get(i)).getMeikxxb_id());
					long zhibb_id = getProperId(getZhibSelectModel(),
							((Yunsmbjijbean) list.get(i)).getZhibb_id());
					long tiaoj_id = getProperId(gettiaojjSelectModel(),
							((Yunsmbjijbean) list.get(i)).getTiaoj_id());
					double shangx = ((Yunsmbjijbean) list.get(i)).getShangx();
					double xiax = ((Yunsmbjijbean) list.get(i)).getXiax();
					double yunjia = ((Yunsmbjijbean) list.get(i)).getYunjia();
					long zhibdw_id = getProperId(getzhibdwSelectModel(),
							((Yunsmbjijbean) list.get(i)).getZhibdw_id());
					// double yunj = ((Yunsmbjijbean) list.get(i)).getYunj();
					long yunjiadw_id = getProperId(getYunjiadwModel(),
							((Yunsmbjijbean) list.get(i)).getYunjdw_id());
					// long yunjudw_id =
					// getProperId(getYunjudwModel(),((Yunsmbjijbean)
					// list.get(i)).getYunjudw_id());
					sql = "insert into HETYSJGB(id, hetys_id, meikxxb_id, zhibb_id, tiaojb_id, shangx, xiax, danwb_id, yunja, yunjdw_id) values("
							+ "xl_xul_id.nextval"
							+ ","
							+ hetysb_mb_id
							+ ","
							+ meikxxb_id
							+ ","
							+ zhibb_id
							+ ","
							+ tiaoj_id
							+ ","
							+ shangx
							+ ","
							+ xiax
							+ ","
							+ zhibdw_id
							+ ","
							+ yunjia
							+ "," + yunjiadw_id + ")";
					con.getInsert(sql);
				}
	*/
				// 保存文字
				long hetwzb_id = Long.parseLong(MainGlobal
						.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
				sql = "insert into HETYSWZB(id,wenznr,HETYS_id)values(" + hetwzb_id
						+ ",'" + getWenz() + "'," + hetysb_mb_id + ")";
				con.getInsert(sql);
				con.commit();
				// 刷新模板下拉框
				getmobmcSelectModels();
				setmobmcSelectValue(getIDropDownBean(getmobmcSelectModel(),
						hetysb_mb_id));
				
			} else {// 如果不是新建模板则是已经存在的模板，进行更新操作
				// 保存合同信息（包括供方需方）
				long hetb_mb_id = getmobmcSelectValue().getId();
				sql = "update HETYS_MB set HETBH='" + bean.getHetbh()
						+ "',QIANDRQ=to_date('" + format.format(bean.getQiandsj())
						+ "','YYYY-MM-DD'),QIANDDD='" + bean.getQianddd()
						+ "',GONGFDWMC='" + bean.getGONGFDWMC() + "',GONGFDWDZ='"
						+ bean.getGONGFDWDZ() + "',GONGFDH='" + bean.getGONGFDH()
						+ "',GONGFFDDBR='" + bean.getGONGFFDDBR()
						+ "',GONGFWTDLR='" + bean.getGONGFWTDLR() + "',GONGFDBGH='"
						+ bean.getGONGFDBGH() + "',GONGFKHYH='"
						+ bean.getGONGFKHYH() + "',GONGFZH='" + bean.getGONGFZH()
						+ "',GONGFYZBM='" + bean.getGONGFYZBM() + "',GONGFSH='"
						+ bean.getGongfsh() + "',XUFDWMC='" + bean.getXUFDWMC()
						+ "',XUFDWDZ='" + bean.getXUFDWDZ() + "',XUFFDDBR='"
						+ bean.getXUFFDDBR() + "',XUFWTDLR='" + bean.getXUFWTDLR()
						+ "',XUFDH='" + bean.getXUFDH()
						+ "',XUFDBGH='"
						+ bean.getXUFDBGH()
						+ "',XUFKHYH='"
						+ bean.getXUFKHYH()
						+ "',XUFZH='"
						+ bean.getXUFZH()
						+ "',XUFYZBM='"
						+ bean.getXUFYZBM()
						+ "',XUFSH='"
						+ bean.getXufsh()
						// +"',HETGYSBID=" + getgongfValue().getId()
						// +",GONGYSB_ID=" + getshijgfSelectValue().getId()
						+ "',QISRQ=to_date('" + format.format(bean.getShengxsj())
						+ "','YYYY-MM-DD'),GUOQRQ=to_date('"
						+ format.format(bean.getGuoqsj())
						+ "','YYYY-MM-DD')"
						+ ",LIUCB_ID="
						+ getliucSelectValue().getId()
						// +",HETJSFSB_ID=" +0// 暂时为空
						// +",HETJSXSB_ID=" + 0
						// +",HETYJ=" + 0
						+ ",diancxxb_id="
						+ getxufValue().getId()
						+ ",MINGC='" + getmobmc() + "'"
						+",yunsjgfab_id="+getYunsjgfaValue().getId()+"\n"
	
						+ " where id=" + hetb_mb_id;
				con.getUpdate(sql);
	
				// 保存价格
	//			saveHetysjg(con,hetb_mb_id);
				if(getChange()!=null && !getChange().equals("")){
					saveHetysjg(con,hetb_mb_id);
				}
//				保存增扣款
				if(getChangek()!=null && !getChangek().equals("")){
					saveHetyszkk(con,hetb_mb_id);
				}
				/*
				List list = geteditValuesg();
				for (int i = 0; i < list.size(); i++) {// 遍历容器
					long hetjgb_id = ((Yunsmbjijbean) list.get(i)).getId();
					long meikxxb_id = getProperId(getMeikxxModel(),
							((Yunsmbjijbean) list.get(i)).getMeikxxb_id());
					long zhibb_id = getProperId(getZhibSelectModel(),
							((Yunsmbjijbean) list.get(i)).getZhibb_id());
					long tiaoj_id = getProperId(gettiaojjSelectModel(),
							((Yunsmbjijbean) list.get(i)).getTiaoj_id());
					double shangx = ((Yunsmbjijbean) list.get(i)).getShangx();
					double xiax = ((Yunsmbjijbean) list.get(i)).getXiax();
					double yunjia = ((Yunsmbjijbean) list.get(i)).getYunjia();
					long zhibdw_id = getProperId(getzhibdwSelectModel(),
							((Yunsmbjijbean) list.get(i)).getZhibdw_id());
					// double yunj = ((Yunsmbjijbean) list.get(i)).getYunj();
					long yunjiadw_id = getProperId(getYunjiadwModel(),
							((Yunsmbjijbean) list.get(i)).getYunjdw_id());
					if (hetjgb_id == 0) {
						hetjgb_id = Long.parseLong(MainGlobal
								.getNewID(((Visit) getPage().getVisit())
										.getDiancxxb_id()));
						sql = "insert into HETYSJGB(id, hetys_id, meikxxb_id, zhibb_id, tiaojb_id, shangx, xiax, danwb_id, yunja, yunjdw_id) values("
								+ "xl_xul_id.nextval"
								+ ","
								+ hetb_mb_id
								+ ","
								+ meikxxb_id
								+ ","
								+ zhibb_id
								+ ","
								+ tiaoj_id
								+ ","
								+ shangx
								+ ","
								+ xiax
								+ ","
								+ zhibdw_id
								+ ","
								+ yunjia + "," + yunjiadw_id + ")";
						con.getInsert(sql);
						((Yunsmbjijbean) list.get(i)).setId(hetjgb_id);
					} else {
						sql = "update HETYSJGB set meikxxb_id = " + meikxxb_id
								+ ",       zhibb_id = " + zhibb_id
								+ ",       tiaojb_id = " + tiaoj_id
								+ ",       shangx = " + shangx + ",       xiax = "
								+ xiax + ",       danwb_id = " + zhibdw_id
								+ ",       yunja = " + yunjia
								+ ",       yunjdw_id = " + yunjiadw_id
								+ " where id=" + hetjgb_id;
						con.getUpdate(sql);
					}
				}
				*/
				// 保存文字
				sql = "update HETYSWZB\n" + "set wenznr='" + getWenz()
						+ "'where HETYS_id=" + hetb_mb_id;
				con.getUpdate(sql);
				con.commit();
				getmobmcSelectModels();
				setmobmcSelectValue(getIDropDownBean(getmobmcSelectModel(),
						hetb_mb_id));
				
				getHetysjgExt(hetb_mb_id);
				getYunshtzkkExt(hetb_mb_id);
			}
		}catch(Exception e){
			e.printStackTrace();
			con.rollBack();
		}finally{
			con.Close();
		}
	}
/*
	private void Insertg() {
		List _value = geteditValuesg();
		_value.add(new Yunsmbjijbean(_value.size() + 1, ""));
	}

	private void Deleteg() {
		List _value = geteditValuesg();
		if (_editTableRowg != -1) {
			long id = ((Yunsmbjijbean) _value.get(_editTableRowg)).getId();
			if (id != 0) {
				JDBCcon con = new JDBCcon();
				String sql = "delete from hetysjgb where id=" + id;
				con.getDelete(sql);
				con.Close();
			}
			_value.remove(_editTableRowg);
			int c = _value.size();
			for (int a = _editTableRowg; a < c; a++) {
				((Yunsmbjijbean) _value.get(a)).setXuh(((Yunsmbjijbean) _value
						.get(a)).getXuh() - 1);
			}
		}
	}
*/
	private long getProperId(IPropertySelectionModel _selectModel, String value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();

		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
					value)) {
				return ((IDropDownBean) _selectModel.getOption(i)).getId();
			}
		}
		return -1;
	}

	private String getProperValue(IPropertySelectionModel _selectModel,
			long value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
				return ((IDropDownBean) _selectModel.getOption(i)).getValue();
			}
		}
		return null;
	}

	private IDropDownBean getIDropDownBean(IPropertySelectionModel model,
			long id) {
		int OprionCount;
		OprionCount = model.getOptionCount();
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) model.getOption(i)).getId() == id) {
				return (IDropDownBean) model.getOption(i);
			}
		}
		return null;
	}

	public String getFahr() {
		if (((Visit) getPage().getVisit()).getString2() == null) {
			((Visit) getPage().getVisit()).setString2("");
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setFahr(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}

	private String MultiSel;

	public String getMultiSel() {
		return MultiSel;
	}
	// public void setMultiSel(String sql,String[] column1){
	// int l=column1.length;
	// JDBCcon con=new JDBCcon();
	// ResultSet rs=con.getResultSet(sql);
	// StringBuffer buf=new StringBuffer();
	// buf.append( "<div id='MultiSel'>");
	// buf.append("<table>");
	// try{
	// while(rs.next()){//tr
	// buf.append("<tr>");
	// for(int i=1;i<=l;i++){//td
	// buf.append("<td>");
	// buf.append(rs.getString(i));
	// buf.append("</td>");
	// }
	// buf.append("</tr>");
	// }
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	//	    	
	// buf.append("</table>");
	// buf.append("</div>");
	//	    	
	// MultiSel=buf.toString();
	// }

}