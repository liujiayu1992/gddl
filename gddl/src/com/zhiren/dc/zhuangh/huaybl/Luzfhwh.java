package com.zhiren.dc.zhuangh.huaybl;


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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.DatetimeField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.ext.tree.TreeOperation;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Luzfhwh extends BasePage implements PageValidateListener {
	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
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

	
	
	private void Save() {
		 Visit visit = (Visit) this.getPage().getVisit();
		 Save1(getChange(), visit);
	}
	private void Save1(String change,Visit visit) {
		String tableName = "LUZFHB";
		JDBCcon con = new JDBCcon();

		ResultSetList delrsl = visit.getExtGrid1()
				.getDeleteResultSet(change);
		while (delrsl.next()) {
			StringBuffer sql = new StringBuffer("begin \n");
			sql.append("delete from ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
			sql.append("end;");
			con.getUpdate(sql.toString());
		}
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(change);
		long Diancxxb_id=0;
		if (visit.isFencb()) {
			Diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo)
					.getBeanId(mdrsl.getString("diancxxb_id"));
		} else {
			Diancxxb_id = visit.getDiancxxb_id();
		}
		StringBuffer sql2 = new StringBuffer("begin \n");
		while (mdrsl.next()) {
			
			String shij="";
			String huasj="";
			if("".equals(mdrsl.getString("caiyrq"))||mdrsl.getString("caiyrq")==null){
				shij="to_date(sysdate,'yyyy-mm-dd hh24:mi:ss')";
			}else{
				shij="to_date('"+mdrsl.getString("caiyrq")+"'"+",'yyyy-mm-dd hh24:mi:ss')";
			}
			if("".equals(mdrsl.getString("huayrq"))||mdrsl.getString("huayrq")==null){
				huasj="to_date(sysdate,'yyyy-mm-dd hh24:mi:ss')";
			}else{
				huasj="to_date('"+mdrsl.getString("huayrq")+"'"+",'yyyy-mm-dd hh24:mi:ss')";
			}
			if ("0".equals(mdrsl.getString("ID"))) {
				
				

				
				sql2.append(" insert into ").append(tableName)
				.append(" (id,caiyrq,huayrq,huayy,beiz,shenhzt,luzjzy,luzjze,feihjzy,feihjze,lury,lursj,ERJSHZT )")
				.append(" values(getnewid("+visit.getDiancxxb_id()+"),").append(shij+",").append(huasj+",'").append(mdrsl.getString("huayy")+"','")
				.append(mdrsl.getString("beiz")+"',3,'").append(mdrsl.getString("luzjzy")+"','").append(mdrsl.getString("luzjze")+"','").append(mdrsl.getString("feihjzy")+"','")
				.append(mdrsl.getString("feihjze")+"','").append(visit.getRenymc()).append("',to_date(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss'),0)").append(";\n");
			} else {
			

				// 先检查然后没有数据再插入
				
				
				sql2.append("update ").append(tableName).append(" set caiyrq=to_date('").append(mdrsl.getString("caiyrq")).append("','YYYY-MM-DD hh24:mi:ss')")
				.append(" , huayrq=to_date('").append(mdrsl.getString("huayrq")).append("','YYYY-MM-DD hh24:mi:ss')").
				append(" ,beiz=' ").append(mdrsl.getString("beiz")).append("'").append(",huayy='").append(mdrsl.getString("huayy")+"'")
				.append(" , luzjzy='"+mdrsl.getString("luzjzy")+"',luzjze='"+mdrsl.getString("luzjze")+"',feihjzy='"+mdrsl.getString("feihjzy")+"',feihjze='"+mdrsl.getString("feihjze"))
				.append("',lury='"+visit.getRenymc()+"',lursj=to_date(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')\n");
				sql2.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
		   }
			sql2.append("end;");
			if(sql2.length()>13){
				
				con.getUpdate(sql2.toString());
			}
		}
	}
	
	
	
	
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if(_RefurbishChick){
		   _RefurbishChick = false;
			getSelectData();
		}
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		String shij="select beiz from rulbzb where id= "+this.getFahrqValue();
		String beiz="";
		String beiz1="";
		String beiz2="";
		ResultSetList rslq1 = con.getResultSetList(shij);
		while(rslq1.next()){
			beiz=rslq1.getString("beiz");
		}
		if("".equals(beiz)){
			beiz="00-23"; 
			beiz1=beiz.substring(0,2)+":00:00";
			beiz2=beiz.substring(3,5)+":59:59";
		}else{ 
			if("24".equals(beiz.substring(3,5))){
				beiz1=beiz.substring(0,2)+":00:00";
				beiz2="23:59:59";
			}else{
				beiz1=beiz.substring(0,2)+":00:00";
				beiz2=beiz.substring(3,5)+":00:00";
			}
			
		}

	sb1.append(
			"SELECT l.id,to_char(l.caiyrq,'yyyy-mm-dd hh24:mi:ss') caiyrq,to_char(l.huayrq,'yyyy-mm-dd hh24:mi:ss') huayrq,l.huayy,l.luzjzy,l.feihjzy,l.luzjze,l.feihjze,l.beiz\n" +
			" FROM LUZFHB l\n"+
			" where shenhzt=1 and " +
			" to_char(l.huayrq,'yyyy-mm-dd') between '"+getRiq1()+"' and '"+getRiq2()+"'"
//			"( bewteen l.huayrq>=to_date('"+getRiq1()+"'||' '||'00:00:00','yyyy-mm-dd hh24:mi:ss')-1 and l.huayrq<=to_date('"+getRiq1()+"'||' '||'23:59:59','yyyy-mm-dd hh24:mi:ss') "
//			+") and ( between  l.huayrq>=to_date('"+getRiq2()+"'||' '||'00:00:00','yyyy-mm-dd hh24:mi:ss')-1 and l.huayrq<=to_date('"+getRiq2()+"'||' '||'23:59:59','yyyy-mm-dd hh24:mi:ss') )"	
			);
		
		// 如果还没有，显示上一个
		// 如果都没有，都不显示，

		ResultSetList rsl = con.getResultSetList(sb1.toString());
		ResultSetList rsl1;
		

		
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		

        
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// egu.setTableName("fahb");
		// 设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// 设置每页显示行数
		egu.addPaging(25);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setWidth(60);
		egu.getColumn("id").editor = null;
		egu.getColumn("caiyrq").setHeader("采样日期");
		egu.getColumn("caiyrq").setWidth(110);
//		egu.getColumn("ZHILB_ID").setDefaultValue(""+getYundxxValue().getId());
		egu.getColumn("huayrq").setHeader("化验日期");//Center
		egu.getColumn("huayrq").setWidth(110);
		
		egu.getColumn("huayy").setCenterHeader("化验员");
		egu.getColumn("huayy").setWidth(150);
		egu.getColumn("huayy").setEditor(null);

		egu.getColumn("luzjzy").setCenterHeader("1#炉渣");
		egu.getColumn("luzjzy").setWidth(90);
		egu.getColumn("luzjze").setCenterHeader("2#炉渣");
		egu.getColumn("luzjze").setWidth(90);
	
//		egu.getColumn("bianm").setEditor(null);
		egu.getColumn("feihjzy").setCenterHeader("1#飞灰");
		egu.getColumn("feihjzy").setWidth(70);
		egu.getColumn("feihjze").setCenterHeader("2#飞灰");
		egu.getColumn("feihjze").setWidth(70);
		egu.getColumn("beiz").setCenterHeader("备注");
		egu.getColumn("beiz").setWidth(70);


		egu.addTbarText("化验开始时间");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq1");
		
		egu.addToolbarItem(df.getScript());



		egu.addTbarText("化验结束时间");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "forms[0]");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		
		egu.addToolbarItem(df1.getScript());


		// 采样日期
		DatetimeField datetime = new DatetimeField();
		egu.getColumn("caiyrq").setEditor(datetime);
		egu.getColumn("caiyrq").setDefaultValue(DateUtil.FormatDateTime(new Date()));
		
		// 化验日期
		DatetimeField date = new DatetimeField();
		egu.getColumn("huayrq").setEditor(date);
		egu.getColumn("huayrq").setDefaultValue(DateUtil.FormatDateTime(new Date()));

		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert,"");
		String treepanel =
			"var navtree = new Ext.tree.TreePanel({\n" +
			//"\t    title: '制样人员',\n" + 
			//"\t    region: 'east',\n " + 
			"\t    autoScroll:true,\n" + 
			"\t    rootVisible:false,\n" + 
			"\t    width: 200,\n" + 
			"\t    autoHeight:true," +
			"\t   \troot:navTree0,\n" + 
			"    \tlisteners : {\n" + 
			"    \t\t'dblclick':function(node,e){\n" + 
			"    \t\t\tif(node.getDepth() == 3){\n" + 
			"    \t\t\t\tvar Cobj = document.getElementById('CHANGE');\n" + 
			"        \t\t\tCobj.value = node.id;\n" + 
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
			"        rec = gridDiv_grid.getSelectionModel().getSelected();\n"+
			"        var tmp=\"\";\n" + 
			"        var tmp2='';\n" + 
			"         if(cs==null){win.hide(this);return;}\n"+
			"        else{for(var i = 0; i< cs.length; i++) {\n" + 
			"        \tif(cs[i].isLeaf()){\n" + 
			"\t\t\t\ttmp = cs[i].text;\n" + 
			"        \t\ttmp2=(tmp2?tmp2+',':'')+tmp;\n" + 
			"\n" + 
			"           }\n" + 
			"        }\n" + 
			"        rec.set('HUAYY',tmp2);\n" + 
			"        win.hide(this);\n"+
			"\n" + 
			"        }}}]\n" + 
			"\t});";
		
		String Strtmpfunction = 

			" win = new Ext.Window({\n" + 
			" title: '制样人员',\n " + 
			"            closable:true,closeAction:'hide',\n" + 
			"            width:200,\n" + 
			"            autoHeight:true,\n" + 
			"            border:0,\n" + 
			"            plain:true,\n" + 
			"            items: [navtree]\n" + 
			" });\n";
		

		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own,irow,icol, e){ "
				+ "row = irow; \n"
				+ "if('HUAYY' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
				+"if(!win){"+treepanel+Strtmpfunction+"}"
				+"win.show(this);}});\n");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.setDefaultsortable(false);
		setExtGrid(egu);
		con.Close();
	}
	
	private String treeid;

	/*
	 * public String getTreeid() { return treeid; } public void setTreeid(String
	 * treeid) { this.treeid = treeid; }
	 */
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
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

	
	
	public String getTreedcScript() {
		return getTree().getWindowTreeScript();
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
		String sql=
			"select 0 id,'根' as mingc,1 jib,-1 fuid,0 checked from dual\n" +
			"union\n" + 
			"select r.id, r.quanc  as mingc,2 jib,0 fuid, 0 checked\n" + 
			"  from diancxxb d,renyxxb r\n" + 
			" where\n" + 
			"r.diancxxb_id=d.id\n" + 
			"and r.bum='化验' and zhuangt=1 and d.id ="+visit.getDiancxxb_id()+"";
			
		TreeOperation dt = new TreeOperation();
		TreeNode node = dt.getTreeRootNode(sql,true);
		
		String treeNodeScript = dt.getTreeNodeScript("navTree", node);
		setNavigetion(treeNodeScript);
	}

//	按照什么时间查条件选择框
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			JDBCcon con=new JDBCcon();
			ResultSetList rsl=con.getResultSetList("select zhi from xitxxb where diancxxb_id="+((Visit)this.getPage().getVisit()).getDiancxxb_id()+" and zhuangt=1 and mingc='海运煤日期查询默认值'");
			String zhi="";
			while(rsl.next()){
				zhi=rsl.getString("zhi");
			}
			if(zhi.equals("离泊日期")){
				((Visit) getPage().getVisit())
				.setDropDownBean2((IDropDownBean) getWeizSelectModel()
						.getOption(2));
			}else if(zhi.equals("到泊日期")){
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getWeizSelectModel()
							.getOption(1));
			}else{
				((Visit) getPage().getVisit())
				.setDropDownBean2((IDropDownBean) getWeizSelectModel()
						.getOption(0));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setWeizSelectValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean2()) {

			((Visit) getPage().getVisit()).setboolean3(true);
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}

	public void setWeizSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getWeizSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getWeizSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getWeizSelectModels() {
		List list = new ArrayList();
		Visit v=(Visit)this.getPage().getVisit();	
		list.add(new IDropDownBean(1, "靠泊日期"));
		list.add(new IDropDownBean(2, "到泊日期"));
		list.add(new IDropDownBean(3, "离泊日期"));
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(list));
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
					"'<marquee width=300 scrollamount=2>" + getTbmsg()
							+ "</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	public String getTreeScript() {
		// System.out.print(((Visit)
		// this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

//	 绑定日期
	public String getRiq1() {
		if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRiq1(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString5() != null && !((Visit) this.getPage().getVisit()).getString5().equals(riq1)) {
			
			((Visit) this.getPage().getVisit()).setString5(riq1);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}
	}
	

	public String getRiq2() {
		if (((Visit) this.getPage().getVisit()).getString6() == null || ((Visit) this.getPage().getVisit()).getString6().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString6(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString6();
	}

	public void setRiq2(String riq2) {

		if (((Visit) this.getPage().getVisit()).getString6() != null && !((Visit) this.getPage().getVisit()).getString6().equals(riq2)) {
			
			((Visit) this.getPage().getVisit()).setString6(riq2);
			((Visit) this.getPage().getVisit()).setboolean1(true);
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

//	 时间段
	public IDropDownBean getFahrqValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null) {
			if(getFahrqModel().getOptionCount()>0) {
				setFahrqValue((IDropDownBean)getFahrqModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	public void setFahrqValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(value);
	}
	
	public IPropertySelectionModel getFahrqModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1()==null) {
			setFahrqModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
	}
	public void setFahrqModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel1(value);
	}
	
	public void setFahrqModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		String sql_yunsfs = "";
		String sql="";
//		if(HAIY_SH.equals(getLeix())){
			
			sql=
"select id,mingc from rulbzb where diancxxb_id="+visit.getDiancxxb_id();
 
		setFahrqModel(new IDropDownModel(sql));
	}

	public boolean _Yundxxchange = false;

	public IDropDownBean getYundxxValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getYundxxModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setYundxxValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean4()) {

			((Visit) getPage().getVisit()).setboolean2(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setYundxxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getYundxxModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getYundxxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public IPropertySelectionModel getYundxxModels() {
		
		String sql="";
		
		switch ((int) this.getWeizSelectValue().getId()) {
		case 1:
			sql="select f.zhilb_id,l.mingc||'_'||f.chec||'_'||p.mingc||'_'||f.biaoz as yundxx from fahb f,luncxxb l,pinzb p \n"+
    		" where f.luncxxb_id=l.id and f.pinzb_id=p.id and f.kaobrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')\n"+
    		"  and f.kaobrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') \n";
			break;// 靠泊日期
		case 2:
			sql="select f.zhilb_id,l.mingc||'_'||f.chec||'_'||p.mingc||'_'||f.biaoz as yundxx from fahb f,luncxxb l,pinzb p \n"+
    		" where f.luncxxb_id=l.id and f.pinzb_id=p.id and f.daobrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')\n"+
    		"  and f.daobrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') \n";
			break;// 到泊日期
		case 3:
			sql="select f.zhilb_id,l.mingc||'_'||f.chec||'_'||p.mingc||'_'||f.biaoz as yundxx from fahb f,luncxxb l,pinzb p \n"+
    		" where f.luncxxb_id=l.id and f.pinzb_id=p.id and f.daohrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')\n"+
    		"  and f.daohrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd') \n";
			break;// 离泊日期
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql,"请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}
	

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setYundxxValue(null);		//4
			setYundxxModel(null);
			setWeizSelectValue(null);
			setWeizSelectModel(null);
			setYundxxModel(null);	
			getYundxxModels();	
			setFahrqValue(null);
			setFahrqModel(null);
			getFahrqModel();
			setTbmsg(null);
			initNavigation();
			visit.setString1(null);
		}
		if(visit.getboolean1()){
			
			visit.setboolean1(false);
//			visit.setString9("");
			visit.setList1(null);
			setYundxxValue(null);		//4
			setYundxxModel(null);
			setWeizSelectValue(null);
			setWeizSelectModel(null);
			visit.setString1(null);
		}
		if(visit.getboolean2()){
			visit.setboolean2(false);
		}
		if(visit.getboolean3()){
		   visit.setboolean3(false);
		   setYundxxValue(null);		//4
		   setYundxxModel(null);
		   visit.setString1(null);
		}
		visit.setboolean4(false);
		initNavigation();
		getSelectData();
	}
}
