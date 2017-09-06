/*chh 2009-09-25 
 *问题1:二级公司没有显示自己增加的信息
 *处理:修改查询过滤条件，按电厂树过滤选定单位的数据,不显示子单位的信息
 *处理:修改工具栏默认结束日期为当天
 *
 *问题2：录入人员超过5个汉字不能保存
 *处理:  alter table DUANXFSB modify LURY VARCHAR2(40);
 */

package com.zhiren.pub.duanxin;

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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.ext.tree.TreeOperation;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Duanxfsext extends BasePage implements PageValidateListener {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
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
	
	//接收人
	private String Jiesr;

	public String getJiesr() {
		return Jiesr;
	}

	public void setJiesr(String jiesr) {
		Jiesr = jiesr;
	}
	
	
	//附加电话
	private String Fujdh;

	public String getFujdh() {
		return Fujdh;
	}

	public void setFujdh(String fujdh) {
		Fujdh = fujdh;
	}
	
	//内容
	private String Neir;

	public String getNeir() {
		return Neir;
	}

	public void setNeir(String neir) {
		Neir = neir;
	}
//	一次发生小时 
	private String Xiaos;

	public String getXiaos() {
		return Xiaos;
	}

	public void setXiaos(String xiaos) {
		Xiaos = xiaos;
	}
	
//	一次发生秒
	private String Miaoz;

	public String getMiaoz() {
		return Miaoz;
	}

	public void setMiaoz(String miaoz) {
		Miaoz = miaoz;
	}
	//发送时间
	private boolean _FassjChange=false;
	public String getFassj() {
		if (((Visit)getPage().getVisit()).getString12()==null){
			((Visit)getPage().getVisit()).setString12(DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(),0, DateUtil.AddType_intDay))));
		}
		return ((Visit)getPage().getVisit()).getString12();
	}
	
	public void setFassj(String _value) {
		if (((Visit)getPage().getVisit()).getString12().equals(_value)) {
			_FassjChange=false;
		} else {
			((Visit)getPage().getVisit()).setString12(_value);
			_FassjChange=true;
		}
	}

	//保存
	private void Save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String biaot = "";
		String fujdh = getFujdh();
		String lury = visit.getRenymc();
		Date date = new Date();
		String lurysj = DateUtil.FormatDateTime(date);//录入时间（自动获得系统时间）
		String riq = getFassj()+" "+ getXiaos()+":"+getMiaoz();//用户选择的时间－发送时间
		String duanxfsb_id = MainGlobal.getNewID(visit.getDiancxxb_id());
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("insert into duanxfsb (id,diancxxb_id,riq,biaot,neir,fujdh,lury,lurysj,zhuangt,leib,beiz) values( \n");
		sql.append(duanxfsb_id).append(",").append(getTreeid()).append(",to_date('"+riq+"','yyyy-mm-dd hh24:mi'),'"+biaot+"','");
		sql.append(getNeir()).append("','").append(fujdh).append("','").append(lury);
		sql.append("',to_date('").append(lurysj).append("','yyyy-mm-dd hh24:mi:ss'),0,0,'')\n");
		
		con.getInsert(sql.toString());
		
		StringBuffer sql1 = new StringBuffer();
		String jiesr = getJiesr();
		String[] lurytemp = jiesr.split(",");
		StringBuffer str_lury = new StringBuffer();
		int size=lurytemp.length;
		//获得接收人的gid
		for (int i=0;i<size;i++){
			String lurystr="select id from lianxrb where xingm ='"+lurytemp[i]+"'";
			try{
				ResultSetList rl = con.getResultSetList(lurystr);
				if(rl.next()){
					str_lury.append(rl.getString("id")).append(",");
				}
				rl.close();
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				con.Close();
			}
		}
		if(str_lury.length()!=0){
			str_lury.setLength(str_lury.length()-1);
		}
		sql1.append("insert into duanxjsrb (id,duanxfsb_id,jiesr) values ( \n ");
		sql1.append("getnewid(").append(visit.getDiancxxb_id()).append("),").append(duanxfsb_id).append(",'");
		sql1.append(str_lury.toString()).append("')");
		
		con.getInsert(sql1.toString());
		
		con.Close();
		
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _Save1Chick = false;

	public void Save1Button(IRequestCycle cycle) {
		_Save1Chick = true;
	}
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
			
		}
		
		if (_Save1Chick) {
			Visit visit = (Visit) this.getPage().getVisit();
			_Save1Chick = false;
			Save1(getChange(), visit);
			getSelectData();
			
		}
		
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
	}

//	页面保存
	private void Save1(String strchange, Visit visit){
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu = getExtGrid();
		try {
			
			StringBuffer sql = new StringBuffer("begin \n");
			
			//要删除的数据
			ResultSetList delrsl = egu.getDeleteResultSet(strchange);
			
			while (delrsl.next()) {
				String duanxfsbid=delrsl.getString(0);//短信发送表id
				if(duanxfsbid==null){
					duanxfsbid="0";
				}
				String duanxlb=delrsl.getString(1);//短信类别
				if(duanxlb==null){
					duanxlb="0";
				}
//				String duanxjsrbid=delrsl.getString(2);//短信接收人
//				if(duanxjsrbid==null){
//					duanxjsrbid="0";
//				}
				String duanxdyb_id=delrsl.getString(2);//短信自定义表id
				if(duanxdyb_id==null){
					duanxdyb_id="0";
				}
				String duanxdypzb_id=delrsl.getString(3);//短信配置表id
				if(duanxdypzb_id==null){
					duanxdypzb_id="0";
				}
				//短信人员表
				sql.append(" delete from duanxjsrb where duanxfsb_id =").append(duanxfsbid).append(";\n");
				if (duanxlb.equals("0")){//手工加入的短信
					//短信发送表
					sql.append(" delete from duanxfsb where id=").append(duanxfsbid).append(";\n");
				}else{//自动配置的短信
//					短信自定义表
					sql.append(" delete from duanxdyb where id=").append(duanxdyb_id).append(";\n");
					//短信配置表
					sql.append(" delete from duanxdypzb where id=").append(duanxdypzb_id).append(";\n");
//					短信发送表
					sql.append(" delete from duanxfsb where id=").append(duanxfsbid).append(";\n");
				}
			}
			
			ResultSetList mdrsl = egu.getModifyResultSet(strchange);
			while (mdrsl.next()) {
				if ("0".equals(mdrsl.getString("ID"))) {
					
				}else{
					String beiz="";
				
					beiz=egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[mdrsl.getColumnCount()-1]),
								mdrsl.getString(mdrsl.getColumnCount()-1));
					
					sql.append("  update duanxfsb set beiz=").append(beiz);
					sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");

				}
				
				
			}
			//要修改的数据
			//ResultSetList mdrsl = egu.getModifyResultSet(strchange);
			
			sql.append("end;");
			con.getUpdate(sql.toString());
			
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		setJiesr("");
		setNeir("");
		setFujdh("");
		setFassj("");
		JDBCcon con = new JDBCcon(); 
		String riqTiaoj=this.getRiqi();
		String riq1Tiaoj=this.getRiqi1();
		if(riqTiaoj==null||riqTiaoj.equals("")){
			riqTiaoj=DateUtil.FormatDate(new Date());
		}
		if(riq1Tiaoj==null||riq1Tiaoj.equals("")){
			riq1Tiaoj=DateUtil.FormatDate(new Date());
		}
		
		String str = "";
//		if (visit.isJTUser()) {
//			str = "";
//		} else {
//			if (visit.isGSUser()) {
//				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
//						+ getTreeid() + ")";
//			} else {
//				str = "and dc.id = " + getTreeid() + "";
//			}
//		}
//		int treejib = this.getDiancTreeJib();
//		if (treejib == 1) {// 选集团时刷新出所有的电厂
//			str = "";
//		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
//			str = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
//		} else if (treejib == 3) {// 选电厂只刷新出该电厂
//			str = "and dc.id = " + getTreeid() + "";
//			
//		}
		//chh 2009-09-25 按电厂树选择刷出信息，不显示所选电厂树的子信息
		str = "and dc.id = " + getTreeid() + "";
		
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select ds.id,ds.leib,ds.duanxdyb_id,ds.duanxdypzb_id,to_char(riq,'yyyy-mm-dd hh24:mi') as riq,lury, \n");
		sbsql.append("neir,GetJiesr(dr.jiesr) as jiesr,to_char(lurysj,'yyyy-mm-dd hh24:mi') as lurysj, \n");
		sbsql.append("decode(dr.zhuangt,0,'未执行',1,'执行成功') as zhuangt,ds.beiz \n");
		sbsql.append("from duanxfsb ds,duanxjsrb dr,diancxxb dc where dr.duanxfsb_id=ds.id and ds.diancxxb_id=dc.id  \n");
		sbsql.append("and ds.riq>=to_date('"+riqTiaoj+"','yyyy-mm-dd') and ds.riq<=to_date('"+riq1Tiaoj+"','yyyy-mm-dd')+1 \n");
		sbsql.append(str);
		ResultSetList rsl = con.getResultSetList(sbsql.toString());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sbsql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("duanxfsb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("leib").setHidden(true);
		egu.getColumn("leib").setEditor(null);
		egu.getColumn("duanxdyb_id").setHidden(true);
		egu.getColumn("duanxdyb_id").setEditor(null);
		egu.getColumn("duanxdypzb_id").setHidden(true);
		egu.getColumn("duanxdypzb_id").setEditor(null);
		
		egu.getColumn("riq").setHeader("发送时间");
		egu.getColumn("riq").setWidth(100);
		egu.getColumn("riq").setDefaultValue(riqTiaoj);
		egu.getColumn("riq").setEditor(null);
		
		egu.getColumn("lury").setHeader("发送人");
		egu.getColumn("lury").setWidth(100);
		egu.getColumn("lury").setEditor(null);
		
		egu.getColumn("neir").setHeader("发送内容");
		egu.getColumn("neir").setWidth(100);
		egu.getColumn("neir").setEditor(null);
		
		egu.getColumn("jiesr").setHeader("接收人");
		egu.getColumn("jiesr").setWidth(100);
		egu.getColumn("jiesr").setEditor(null);
		
//		egu.getColumn("fujdh").setHeader("附加电话");
//		egu.getColumn("fujdh").setWidth(110);
//		egu.getColumn("fujdh").setEditor(null);
		
		egu.getColumn("lurysj").setHeader("录入时间");
		egu.getColumn("lurysj").setWidth(100);
		egu.getColumn("lurysj").setEditor(null);
		
		egu.getColumn("zhuangt").setHeader("状态");
		egu.getColumn("zhuangt").setWidth(100);
		
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(100);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("zhuangt").setEditor(null);
		
		egu.addPaging(25);
		egu.setWidth(Locale.Grid_DefaultWidth);
		
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI","");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("至");
		DateField df1 = new DateField();
		df1.setValue(this.getRiqi1());
		df1.Binding("RIQI1","");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df1.getScript());
		
		egu.addTbarText("-");
		
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		
		egu.addTbarText("-");
		
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		egu.addTbarText("-");
		//egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);

		String treepanel =
			"var navtree = new Ext.tree.TreePanel({\n" +
			"\t    title: '接收人',\n" + 
			"\t    region: 'east',\n " + 
			"\t    autoScroll:true,\n" + 
			"\t\trootVisible:false,\n" + 
			"\t    split: true,\n" + 
			"\t    width: 200,\n" + 
			"\t    minSize: 160,\n" + 
			"\t    maxSize: 240,\n" + 
			"\t    collapsible: true,\n" + 
			"\t    margins:'1 0 1 1',\n" + 
			"\t    cmargins:'1 1 1 1',\n" + 
			"\t   \troot:navTree0,\n" + 
			"    \tlisteners : {\n" + 
			"    \t\t'dblclick':function(node,e){\n" + 
			"    \t\t\tif(node.getDepth() == 3){\n" + 
			"    \t\t\t\tvar Cobj = document.getElementById('CHANGE');\n" + 
			"        \t\t\tCobj.value = node.id;\n" + 
			"    \t\t\t\ttmp = Ext.getDom('jiesr1').value;\n" + 
			"\t        \t\tExt.getDom('jiesr1').value=(tmp?tmp+',':'')+node.text;\n" + 
			"\t\t\t\t\tdocument.getElementById('JIESR').value=Ext.getDom('jiesr1').value;\n" + 
			"    \t\t\t}else{\n" + 
			"    \t\t\t\te.cancel = true;\n" + 
			"    \t\t\t}\n" + 
			"    \t\t},'checkchange': function(node,checked){\n" + 
			"\t\t\t\t\t/*if(checked){\n" + 
			"\t\t\t\t\t\taddNode(node);\n" + 
			"\t\t\t\t\t}else{\n" + 
			"\t\t\t\t\t\tsubNode(node);\n" + 
			"\t\t\t\t\t}*/\n" + 
			"\t\t\t\t\tnode.expand();\n" + 
			"\t\t\t\t\tnode.attributes.checked = checked;\n" + 
			"\t\t\t\t\tnode.eachChild(function(child) {\n" + 
			"\t\t\t\t\t\tif(child.attributes.checked != checked){\n" + 
			"\t\t\t\t\t\t\t/*if(checked){\n" + 
			"\t\t\t\t\t\t\t\taddNode(child);\n" + 
			"\t\t\t\t\t\t\t}else{\n" + 
			"\t\t\t\t\t\t\t\tsubNode(child);\n" + 
			"\t\t\t\t\t\t\t}*/\n" + 
			"\t\t\t\t\t\t\tchild.ui.toggleCheck(checked);\n" + 
			"\t\t\t            \tchild.attributes.checked = checked;\n" + 
			"\t\t\t            \tchild.fireEvent('checkchange', child, checked);\n" + 
			"\t\t\t\t\t\t}\n" + 
			"\t\t\t\t\t});\n" + 
			"\t\t\t\t}\n" + 
			"    \t},\n" + 
			"\t   \ttbar:[{text:'确定',handler:function(){\n" + 
			"        var cs = navtree.getChecked();\n" + 
			"        var tmp=\"\";\n" + 
			"        var tmp2='';\n" + 
			"        for(var i = 0; i< cs.length; i++) {\n" + 
			"        \tif(cs[i].isLeaf()){\n" + 
			"\t\t\t\ttmp = cs[i].text;\n" + 
			"        \t\ttmp2=(tmp2?tmp2+',':'')+tmp;\n" + 
			"\n" + 
			"           }\n" + 
			"        }\n" + 
			"        Ext.getDom('jiesr1').value=tmp2;\n" + 
			"\t\tdocument.getElementById('JIESR').value=Ext.getDom('jiesr1').value;\n" + 
			"\n" + 
			"        }}]\n" + 
			"\t});";
		
		String Strtmpfunction = 

			"var Xiaos = new Ext.form.ComboBox({\n" +
			"\twidth:60,\n" + 
			"\t//fieldLabel:'时',\n" + 
			"\thideLabel : true,\n" + 
			"    selectOnFocus:true,\n" + 
			" \ttransform:'XiaosDropDown',\n" + 
			"\tlazyRender:true,\n" + 
			"\ttriggerAction:'all',\n" + 
			"\ttypeAhead:true,\n" + 
			"    forceSelection:true,\n" + 
			"\teditable:false,\n" + 
			"listeners:{\n" +
			"    change:function(own,newValue,oldValue) {\n" + 
			"\t\tdocument.getElementById('XIAOS').value = newValue;}},\n" + 
			"  \tvalue:document.all.item('XIAOS').value"+
			"});\n" + 
			"\n" + 
			"var Miaoz = new Ext.form.ComboBox({\n" + 
			"  \t width:60,\n" + 
			"\t //fieldLabel:'分',\n" + 
			"\t hideLabel : true,\n" + 
			"     selectOnFocus:true,\n" + 
			" \t transform:'MiaozDropDown',\n" + 
			"\t lazyRender:true,\n" + 
			"\t triggerAction:'all',\n" + 
			"\t typeAhead:true,\n" + 
			"     forceSelection:true,\n" + 
			"\t editable:false,\n" +
			"listeners:{\n" +
			"    change:function(own,newValue,oldValue) {\n" + 
			"\t\tdocument.getElementById('MIAOZ').value = newValue;}},\n" + 
			"  \tvalue:document.all.item('MIAOZ').value"+
			"});\n" + 
			"\n" + 
			"var hour_pan = new Ext.Panel({\n" + 
			"\t//baseCls: 'x-plain',\n" + 
			"\theight:40,\n" + 
			"\tlayout:'table',\n" + 
			"\titems : [{html:'&nbsp;<font size=\"2\">发送时间:</font>&nbsp;' },{\n" + 
			"\t\t\t xtype:'datefield',\n" + 
			"\t\t     name:'fassj',\n" + 
			"\t\t     anchor: '90%',\n" + 
			"    	 listeners:{change:function(own,newValue,oldValue) {\n document.getElementById('FASSJ').value = newValue.dateFormat('Y-m-d');}},\n" +
			"     	 value:document.all.item('FASSJ').value \n }"+
			"\t\t\t,{html:'&nbsp;时&nbsp;'},Xiaos,{html:'&nbsp;分&nbsp;'},Miaoz]\n" + 
			"});"+
			"\t        var nav = new Ext.Panel({\n" + 
			"\t            title: '短信内容',\n" + 
			"\t            region: 'center',\n" + 
			"\t            autoScroll: true,\n" +
			"\t            split: true,\n" + 
			"\t            width: '100%',\n" + 
			//"\t            collapsible: true,\n" + 
			"\t            margins:'1 0 1 1',\n"+
			"\t            cmargins:'1 1 1 1',\n  frame: true, \n"+
			"\t            items: [new Ext.form.FormPanel({\n" + 
			"\t\t\t\t\t        baseCls: 'x-plain',\n" + 
			"\t\t\t\t\t        width:'100%',\n" + 
			"\t\t\t\t\t        labelWidth: 60,\n" + 
			"\t\t\t\t\t        labelAlign: 'right',\n" + 
			"\t\t\t\t\t        //url:'save-form.php',\n" + 
			"\t\t\t\t\t        defaultType: 'textfield',\n" + 
			"\n" + 
			"\t\t\t\t\t        items: [{\n" + 
			"\t\t\t\t\t            fieldLabel: '接收人',\n id: 'jiesr1',\n disabled: true,\n" + 
			"\t\t\t\t\t            name: 'jiesr',\n" + 
			"\t\t\t\t\t            anchor:'95%',\n" +
			"\t\t\t\t\t            listeners:{ \n" +
			"\t\t\t\t\t            change:function(own,newValue,oldValue) {\n document.getElementById('JIESR').value = newValue;}\n"+
			"\t\t\t\t\t            },"+
			"\t\t\t\t\t            value:document.all.item('JIESR').value \n"+
			"\t\t\t\t\t        }," +
//			"{\n" + 
//			"\t\t\t\t\t            fieldLabel: '附加电话',\n" + 
//			"\t\t\t\t\t            name: 'fujdh',\n" + 
//			"\t\t\t\t\t            anchor: '95%',\n" +
//			"\t\t\t\t\t            listeners:{ \n" +
//			"\t\t\t\t\t            change:function(own,newValue,oldValue) {\n document.getElementById('FUJDH').value = newValue;}\n"+
//			"\t\t\t\t\t            },"+
//			"\t\t\t\t\t            value:document.all.item('FUJDH').value \n"+
//			"\t\t\t\t\t        }, " +
			"{\n" + 
			"\t\t\t\t\t            fieldLabel: '内容',\n id: 'neir1',\n" + 
			"\t\t\t\t\t            xtype: 'textarea',\n maxLength :180,\n" + 
			"\t\t\t\t\t            height: 180,\n" + 
			"\t\t\t\t\t            name: 'neir',\n" + 
			"\t\t\t\t\t            anchor: '94%',\n" +
			"\t\t\t\t\t            listeners:{ \n" +
			"\t\t\t\t\t            change:function(own,newValue,oldValue) {\n "+
			"                      if(newValue.length>180){   \n" +
			"                      Ext.MessageBox.alert('提示信息','短信内容最多为180个字符,如果超出系统会自动截取前180个字符！'); \n" +
			"                      Ext.getDom('neir1').value=newValue.substr(0,180);  \n" +
			"                      }\n" +
			"\t\t\t\t\t            document.getElementById('NEIR').value = Ext.getDom('neir1').value;}\n },"+
			"\t\t\t\t\t            value:document.all.item('NEIR').value \n"+
			"\t\t\t\t\t        },hour_pan" +
			
//			"{\n" + 
//			"						xtype:'datefield', \n"   
//			+ "						fieldLabel:'发送时间', \n"   
//			+ "						name:'fassj', \n" 
//			+ "\t\t\t\t\t            anchor: '57%',\n" 
//			+ "    	 				listeners:{change:function(own,newValue,oldValue) {\n document.getElementById('FASSJ').value = newValue.dateFormat('Y-m-d');}},\n"
//			+ "     				value:document.all.item('FASSJ').value \n }"+
			"\t\t\t\t\t        ]\n" + 
			"\t\t\t\t\t    })\n" + 
			"\t\t\t\t]\n" + 
			"\t        });\n" + 
			"\n" + 
			" win = new Ext.Window({\n" + 
			" title: '添加新信息',\n " + 
			"            closable:true,\n" + 
			"            width:600,\n" + 
			"            height:400,\n" + 
			"            border:0,\n" + 
			"            plain:true,\n" + 
			"            layout: 'border',\n" + 
			"            items: [nav,navtree],\n" + 
			"             buttons: [{\n" + 
			"   text:'保存',\n" + 
			"\t   \thandler:function(){\n" + 
			"\t   \tif (document.getElementById('NEIR').value==''){\n"+
			"\t   \t    Ext.MessageBox.alert('提示信息','短信内容不能为空！'); \n" +
			
			"\t   \t}else if (document.getElementById('JIESR').value==''){\n"+
			"\t   \t    Ext.MessageBox.alert('提示信息','接收人不能为空，请选择接收人！'); \n" +
			
			"\t   \t}else if (document.getElementById('FASSJ').value==''){\n"+
			"\t   \t    Ext.MessageBox.alert('提示信息','发送时间不能为空！'); \n" +
			
			"\t   \t    }else{ \n"+
			"\t\t  \twin.hide();\n" + 
			//"\t\t\tdocument.getElementById('TEXT_RADIO_SELECT_VALUE').value=document.getElementById('rbvalue').value;\n" + 
			"\t\t\tdocument.getElementById('XIAOS').value=Xiaos.value;\n" +
			"\t\t\tdocument.getElementById('MIAOZ').value=Miaoz.value;\n" +
			"\t\t\tdocument.getElementById('SaveButton').click();\n" + 
			"\t  \t}}\n" + 
			"\t},{\n" + 
			"\t   text: '取消',\n" + 
			"\t   handler: function(){\n" + 
			"\t     win.hide();\n" + 
			"\t   }\n" + 
			"\t}]\n" + 
			" });\n";
		
		StringBuffer cpb = new StringBuffer();
		cpb.append("function(){ if(!win){"+treepanel+Strtmpfunction+"}").append(
				"win.show(this);	\n}");
		GridButton cpr = new GridButton("添加", cpb.toString());
		cpr.setIcon(SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(cpr);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "Save1Button");
		setExtGrid(egu);
		con.Close();
	}
	
	public String getNavigetion(){
		return ((Visit)this.getPage().getVisit()).getString3();
	}
	
	public void setNavigetion(String nav){
		((Visit)this.getPage().getVisit()).setString3(nav);
	}
	
	public void initNavigation(){
		Visit visit = (Visit) getPage().getVisit();
		setNavigetion("");
		
//		导航栏树的查询SQL
		String sql ="";
		
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			sql="select id,decode(jib,1,'根',mingc) as mingc,jib,fuid,checked from (\n" +
			"select decode(id,"+getTreeid()+",0,id) as id,mingc as mingc,\n" + 
			"level as jib,decode(level,1,-1,2,0,fuid) as fuid,0 checked from\n" + 
			"(select id,mingc,fuid from diancxxb\n" + 
			"union\n" + 
			"select id,xingm as mingc,diancxxb_id as fuid from lianxrb) a\n" + 
			"start with id ="+getTreeid()+"\n" + 
			"connect by fuid=prior id order SIBLINGS by mingc )";

		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			sql="select id,decode(jib,1,'根',mingc) as mingc,jib,fuid,checked from (\n" +
			"select decode(id,"+getTreeid()+",0,id) as id,mingc as mingc,\n" + 
			"level as jib,decode(level,1,-1,2,0,fuid) as fuid,0 checked from\n" + 
			"(select id,mingc,fuid,shangjgsid from diancxxb\n" + 
			"union\n" + 
			"select id,xingm as mingc,diancxxb_id as fuid,0 as shangjgsid from lianxrb) a\n" + 
			"start with id ="+getTreeid()+"\n" + 
			"connect by (fuid=prior id or shangjgsid=prior id) order SIBLINGS by mingc )";

		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			sql="select id,decode(jib,1,'根',mingc) as mingc,jib,fuid,checked from (\n" +
			"select decode(id,"+getTreeid()+",0,id) as id,mingc as mingc,\n" + 
			"level as jib,decode(level,1,-1,2,0,fuid) as fuid,0 checked from\n" + 
			"(select id,mingc,fuid from diancxxb\n" + 
			"union\n" + 
			"select id,xingm as mingc,diancxxb_id as fuid from lianxrb) a\n" + 
			"start with id ="+getTreeid()+"\n" + 
			"connect by fuid=prior id order SIBLINGS by mingc )";
		}
		
		TreeOperation dt = new TreeOperation();
		TreeNode node = dt.getTreeRootNode(sql,true);
		sql = 
			"select id, mingc, fuid, 0 dc\n" +
			"  from diancxxb\n" + 
			" where\n" + 
			"  id not in (select distinct diancxxb_id\n" + 
			"  from lianxrb l, diancxxb d\n" + 
			" where l.diancxxb_id = d.id and d.id in (select id\n" + 
			"\t\t\t from(\n" + 
			"\t\t\t select id from diancxxb\n" + 
			"\t\t\t start with id="+getTreeid()+"\n" + 
			"\t\t\t connect by (fuid=prior id or shangjgsid=prior id)\n" + 
			"\t\t\t )\n" + 
			"\t\t\t union\n" + 
			"\t\t\t select id\n" + 
			"\t\t\t from diancxxb\n" + 
			"\t\t\t where id="+getTreeid()+"))\n" + 
			" order by fuid desc";


		
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		TreeNode tmp;
		while(rsl.next()){
			tmp = (TreeNode)node.getNodeById(rsl.getString("id"));
			if(tmp!=null && tmp.isLeaf()){
				tmp.remove();
			}
		}
		rsl.close();
		
		String treeNodeScript = dt.getTreeNodeScript("navTree", node);
		setNavigetion(treeNodeScript);
	}

	/*public void getToolBars() {
		
	}
	
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}*/
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if(getExtGrid() == null) {
			return "";
		}
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid() == null) {
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

	boolean riqichange=false;
	private String riqi;
	public String getRiqi() {
		if(riqi==null||riqi.equals("")){
			riqi=DateUtil.FormatDate(DateUtil.AddDate(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)), 0, DateUtil.AddType_intDay));
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		
		if(this.riqi!=null &&!this.riqi.equals(riqi)){
			
			riqichange=true;
		}
		this.riqi = riqi;
	}
	
	boolean riqi1change=false;
	private String riqi1;
	public String getRiqi1() {
		if(riqi1==null||riqi1.equals("")){
			riqi1=DateUtil.FormatDate(DateUtil.AddDate(new Date(),0,DateUtil.AddType_intDay));
		}
		return riqi1;
	}
	public void setRiqi1(String riqi1) {
		
		if(this.riqi1!=null &&!this.riqi1.equals(riqi1)){
			
			riqi1change=true;
		}
		this.riqi1 = riqi1;
	}
	
//	小时
	private static IPropertySelectionModel _XiaosDownModel;
    public IPropertySelectionModel getXiaosDownModel() {
        if (_XiaosDownModel == null) {
            getXiaosDownModels();
        }
        return _XiaosDownModel;
    }
    
	private IDropDownBean _XiaosDownValue;
	
    public IDropDownBean getXiaosDownValue() {
        if (_XiaosDownValue == null) {
            int _xiaos = DateUtil.getHour(new Date());
            for (int i = 0; i < getXiaosDownModel().getOptionCount(); i++) {
                Object obj = getXiaosDownModel().getOption(i);
                if (_xiaos == ((IDropDownBean) obj).getId()) {
                    _XiaosDownValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _XiaosDownValue;
    }
	
    public void setXiaosDownValue(IDropDownBean Value) {
    	if  (_XiaosDownValue!=Value){
    		_XiaosDownValue = Value;
    	}
    }

    public IPropertySelectionModel getXiaosDownModels() {
        List listXiaosDown = new ArrayList();
        int i;
        for (i = 0; i <= 23; i++) {
            listXiaosDown.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _XiaosDownModel = new IDropDownModel(listXiaosDown);
        return _XiaosDownModel;
    }

    public void setXiaosDownModel(IPropertySelectionModel _value) {
        _XiaosDownModel = _value;
    }
    
//  分钟
	private static IPropertySelectionModel _MiaozDownModel;
    public IPropertySelectionModel getMiaozDownModel() {
        if (_MiaozDownModel == null) {
            getMiaozDownModels();
        }
        return _MiaozDownModel;
    }
    
	private IDropDownBean _MiaozDownValue;
	
    public IDropDownBean getMiaozDownValue() {
        if (_MiaozDownValue == null) {
            int _Miaoz = DateUtil.getMinutes(new Date());
            for (int i = 0; i < getMiaozDownModel().getOptionCount(); i++) {
                Object obj = getMiaozDownModel().getOption(i);
                if (_Miaoz == ((IDropDownBean) obj).getId()) {
                    _MiaozDownValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _MiaozDownValue;
    }
	
    public void setMiaozDownValue(IDropDownBean Value) {
    	if  (_MiaozDownValue!=Value){
    		_MiaozDownValue = Value;
    	}
    }

    public IPropertySelectionModel getMiaozDownModels() {
        List listMiaozDown = new ArrayList();
//        int i;
//        for (i = 0; i <= 59; i++) {
//            listMiaozDown.add(new IDropDownBean(i, String.valueOf(i)));
//        }
        listMiaozDown.add(new IDropDownBean(0, String.valueOf(0)));
        listMiaozDown.add(new IDropDownBean(10, String.valueOf(10)));
        listMiaozDown.add(new IDropDownBean(20, String.valueOf(20)));
        listMiaozDown.add(new IDropDownBean(30, String.valueOf(30)));
        listMiaozDown.add(new IDropDownBean(40, String.valueOf(40)));
        listMiaozDown.add(new IDropDownBean(50, String.valueOf(50)));
        _MiaozDownModel = new IDropDownModel(listMiaozDown);
        return _MiaozDownModel;
    }

    public void setMiaozDownModel(IPropertySelectionModel _value) {
        _MiaozDownModel = _value;
    }
    
    
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString12("");
			this.setTreeid(null);
			setJiesr("");
			setNeir("");
			setFujdh("");
			setFassj("");
			setTbmsg(null);
			initNavigation();
			this.setRiqi(DateUtil.FormatDate(visit.getMorkssj()));
			this.setRiqi1(DateUtil.FormatDate(new Date()));
		}
		initNavigation();
		getSelectData();
	}
	
	private String treeid;
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
}