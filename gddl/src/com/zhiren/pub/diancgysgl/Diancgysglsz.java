package com.zhiren.pub.diancgysgl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
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
import com.zhiren.common.ext.Button;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.Window;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.gs.bjdt.jiesgl.jiesdsh.ILiuc;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Diancgysglsz extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		if(!(this.msg=msg).equals(""))
		this.msg =MainGlobal.getExtMessageBox("'"+msg+"'",true);
	}
	//关键字
//	private String GuanjzTf;
	public void setGuanjzTf(String GuanjzTf){
//		this.GuanjzTf=GuanjzTf;
		((Visit)this.getPage().getVisit()).setString4(GuanjzTf);
	}
	public String getGuanjzTf(){
		return ((Visit)this.getPage().getVisit()).getString4();
	}
	private boolean _LastChick = false;

	public void LastButton(IRequestCycle cycle) {
		_LastChick = true;
	}
	
	private void Last(IRequestCycle cycle){
		cycle.activate("Zidyfapz_sjy");
	}

	private boolean _NextChick = false;

	public void NextButton(IRequestCycle cycle) {
		_NextChick = true;
	}
	private boolean _SaveChick = false;
     public void SaveButton(IRequestCycle cycle){
    	 _SaveChick=true;
	}
	private void Save(IRequestCycle cycle){
		Visit v = (Visit) getPage().getVisit();
		List _MultipleSelectedList = new ArrayList();
		_MultipleSelectedList=this.getMultipleSelected();
		JDBCcon con = new JDBCcon();
		String sql = "select distinct gongysb_id from diancgysglb where diancxxb_id="+this.getTreeid()+"\n";
		 ResultSetList rsl=con.getResultSetList(sql);
		 boolean isdel=true;
		 String delmsg="";
			 while (rsl.next()) {
				 isdel=true;
			for (int i = 0; i < getMultipleSelected().size(); i++) {
				IDropDownBean ib = (IDropDownBean) getMultipleSelected().get(i);
				if (rsl.getLong("gongysb_id") == ib.getId()) {
					_MultipleSelectedList.remove(i);
					isdel = false;
					break;
				}
			}
			// 写属于数据库里有，但没选择的
			if (isdel) {
                     delmsg="有些供应商已经关联,为维护数据的完整性,不能删除";
//                     删除的代码
//				con.getDelete("delete from diancgysglb where gongysb_id="
//						+ rsl.getLong("gongysb_id") + " and diancxxb_id="
//						+ this.getTreeid() + "\n");
//
//				isdel = true;
			}

		}
		 for(int i=0;i<_MultipleSelectedList.size();i++)
		 con.getInsert("insert into diancgysglb values(getnewid("+this.getTreeid()+"),"+this.getTreeid()+","+((IDropDownBean)_MultipleSelectedList.get(i)).getId()+",1) ");
//		String sql = "select * from zidyfapz where zidyfa_id=" + v.getString1();
//		String pz_id = "-1";
//		JDBCcon con = new JDBCcon();
//		ResultSetList rs = con.getResultSetList(sql);
//		if(rs.next()){
//			pz_id = rs.getString("id");
//		}
//		List l = getMultipleSelected();
//		sql = "begin \n";
//		for(int i =0;i <l.size() ; i++){
//			sql += "insert into zidypzms(zidyfapz_id,z_column,z_code,z_value) ( select " 
//				+ pz_id + ",z_column,'showNum'," + (i+1)+" from zidyjcsjyms where id = "
//				+ ((IDropDownBean)l.get(i)).getId() + ");\n";
//		}
//		sql += "end;";
//		con.getDelete("delete from zidypzms where z_code = 'showNum' and zidyfapz_id =" +pz_id);
//		con.getUpdate(sql);
		 con.Close();
		 if(delmsg.equals("")){
		     this.setMsg("设置成功");
		 }else{
			 this.setMsg(delmsg);
		 }
//		cycle.activate("Zidyfapz_qt");
	}
	
	public void submit(IRequestCycle cycle) {
		if (_LastChick) {
			_LastChick = false;
//			刷新windows
			getWindows();
//			Last(cycle);
		}else{
			setWindowsScript("");
			
		}
		if (_NextChick) {
			_NextChick = false;
			Next(cycle);
//			getToolBars();
//			initMultipleSelected(); 
			getToolBars();
		}
		if(_SaveChick){
			_SaveChick=false;
			Save(cycle);
		}else{
			this.setMsg("");
		}
//		setMultipleModel(null);
//		setMultipleSelected(null);
		initMultipleSelected();
//		CreateMultipleModel();
	}
	
	//根据关键字用windows返回查询结果
private void getWindows() {
	 StringBuffer beforesb=new StringBuffer();
	 beforesb.append("var idstr='';\n");
	  StringBuffer button=new StringBuffer();
	  button.append("for (i = 0; i <checka.length; i++){if(checka[i].getValue()){idstr+=checka[i].idvalue+','};}\n");
	  button.append("if(idstr.length==0){alert('请选择');return;}\n");
	  button.append("var idstrarray=idstr.split(','); var mytarget = document.Form0.MultipleSelect$avail;\n");
	  button.append("for(j = 0; j <mytarget.length; j++){mytarget.options[j].selected=false;for(k=0;k<idstrarray.length-1;k++){if(idstrarray[k]==mytarget.options[j].value){mytarget.options[j].selected=true;onChange_MultipleSelect_available();}}}\n");
	  button.append("var mytargetget = document.Form0.MultipleSelect$avail;");
//	  button.append("mytargetget.focus();");
//	  button.append("mytargetget.click();");
	  button.append("select_MultipleSelect();");
      button.append(" Jieg_window.hide();");
      button.append("document.getElementById('SaveButton').click();");
    
	 
	   
//	    StringBuffer sb=new StringBuffer();
	    JDBCcon jcon= new JDBCcon();
	    String sql="select mk.id, nvl(g.quanc,mk.quanc) as gongys,nvl(g.bianm,mk.bianm) bianm,mk.quanc,mk.fuid,mk.mingc from gongysb g,(select gy.id,gy.quanc,gy.fuid,gy.bianm,gy.mingc from gongysb gy where quanc like '%"+getGuanjzTf()+"%') mk where g.id(+)=mk.fuid";
	    if(this.getWeizSelectValue().getId()==2){
	    	sql="select id,quanc,mingc from gongysb where mingc like '%"+getGuanjzTf()+"%'";
	    }
	    ResultSetList rsl= jcon.getResultSetList(sql);
	    String quanc="";
	    String mingc="";
	    String id="";
	    String  bianm="";
	    String gongys="";
	    //check数组
    	beforesb.append("var checka=new Array();\n");
    	 StringBuffer sb=new StringBuffer();
	    while(rsl.next()){	
//	    	 StringBuffer sb=new StringBuffer();
             quanc= rsl.getString("quanc");
             mingc= rsl.getString("mingc");
             
             id=rsl.getString("id");
             bianm=rsl.getString("bianm");
             gongys=rsl.getString("gongys");
             String boxlabel="";
              if(rsl.getLong("fuid")==0){
            	  boxlabel="供应商名:"+quanc+"(编码:"+bianm+")(简称:"+mingc+")";
              }else{
            	  boxlabel="煤矿名:"+quanc+"(简称:"+mingc+")--供应商名:"+gongys+"(编码:"+bianm+")";
              }
             sb.append("checka[checka.length]=check"+rsl.getRow()+"=new Ext.form.Checkbox({boxLabel:'"+boxlabel+"',hideLabel: true,idvalue:"+id+"})\n");
             
	    }
	    beforesb.append(sb.toString());
	    //弹出窗口     
		   StringBuffer w=new StringBuffer();
		    w.append("var Jieg_window =new Ext.Window({\n");
		    w.append("width:650,\n");
		    w.append("height:400,\n");
		    w.append("autoScroll:true, ");
		    w.append("closeAction:'hide',\n");
	        w.append("modal:true,\n");
	        w.append("layout:'form',\n");
	        w.append("title:'查询的类似结果',\n");
	        w.append("buttons :[{text:'确定',handler:function(){"+button.toString()+"}}] ,\n");
		    w.append("items:checka\n");
		    w.append("});\n");

       if(rsl.getRows()>0){
		this.setWindowsScript(beforesb.toString()+w.toString()+"\nJieg_window.show()\n");
       }else{
//    	   this.setWindowsScript("");
    	   this.setWindowsScript(MainGlobal.getExtMessageBox("'没有数据'",true));
       }
		jcon.Close();
	}

private void Next(IRequestCycle cycle) {
	Save(cycle);
		// TODO 自动生成方法存根
	((Visit) this.getPage().getVisit()).setString3(this.getTreeid());
	cycle.activate("Diancgysglsecond");
		
	}

//	MultipleModel
	public SortMode getSort() {
		return SortMode.USER;
	}

	public IPropertySelectionModel getMultipleModel() {
	
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			CreateMultipleModel();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setMultipleModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public List getMultipleSelected() {
		if(((Visit) getPage().getVisit()).getList1()==null){
			initMultipleSelected();
		}
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setMultipleSelected(List ChepSelect) {
		((Visit) getPage().getVisit()).setList1(ChepSelect);
	}

	private void CreateMultipleModel() {
		setMultipleSelected(null);
		Visit v = (Visit) getPage().getVisit();
		List _MultipleList = new ArrayList();
		JDBCcon con = new JDBCcon();
		String sql = " select g.id,g.quanc mingc from gongysb g ";
		ResultSetList rs = con.getResultSetList(sql);
		if(rs.getRows()==0){
//			此处添加 自定义基础数据源描述未配置的错误代码
		}
		while(rs.next()){
			_MultipleList.add(new IDropDownBean(rs.getLong("id"), rs
					.getString("mingc")));
		}
		rs.close();
		con.Close();
		setMultipleModel(new IDropDownModel(_MultipleList));
	}
	
	public void initMultipleSelected(){
		Visit v = (Visit) getPage().getVisit();
		List _MultipleSelectedList = new ArrayList();
		JDBCcon con = new JDBCcon();
		String sql = " select distinct gongysb_id from diancgysglb where diancxxb_id="+this.getTreeid()+"\n ";
		ResultSetList rs = con.getResultSetList(sql);
		while(rs.next()){
			for(int i = 0;i<getMultipleModel().getOptionCount();i++){
//				System.out.println(i);
				IDropDownBean ib = (IDropDownBean)getMultipleModel().getOption(i);
				if(rs.getLong("gongysb_id") == ib.getId()){
					_MultipleSelectedList.add(ib);
					break;
				}
			}
		}
		rs.close();
		con.Close();
		setMultipleSelected(_MultipleSelectedList);
	}
	
//	 电厂名称
	// private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	// private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		// if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
		// _DiancmcChange = false;
		// }else{
		// _DiancmcChange = true;
		// }
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

//	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}

	// 得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
	}

	//
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
//	Toolbar
	public void getToolBars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		
		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", ""+ visit.getDiancxxb_id(), "Form0", null, getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1"
						: getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
//		
//		ToolbarButton lbtn = new ToolbarButton(null,"上一步","function(){document.getElementById('LastButton').click();}");
//		tb1.addItem(lbtn);
		
		if(this.isShezhimeiyuan()){
			ToolbarButton lbtn2 = new ToolbarButton(null,"下一步","function(){document.getElementById('NextButton').click();}");
			tb1.addItem(lbtn2);
		}
	
		//测试时放开
//		tb1.addItem(lbtn2);
		
		ToolbarButton nbtn = new ToolbarButton(null,"保存","function(){document.getElementById('SaveButton').click();}");
		nbtn.setIcon(SysConstant.Btn_Icon_Save);
		tb1.addItem(nbtn);
		tb1.addText(new ToolbarText("关键字:"));
		TextField GuanjzTf = new TextField(); 
		GuanjzTf.setAllowBlank(false);
		GuanjzTf.setId("GuanjzTf");
		GuanjzTf.setWidth(100);
		GuanjzTf.setValue(getGuanjzTf());
		GuanjzTf.setListeners("'change':function(){document.getElementById('GuanjzTf').value=GuanjzTf.getValue();}");
		tb1.addField(GuanjzTf);
		ComboBox WeizSelect = new ComboBox();
		WeizSelect.setId("Weizx");
		WeizSelect.setWidth(100);
		WeizSelect.setLazyRender(true);
		WeizSelect.setTransform("WeizSelectx");
//		tb1.addField(WeizSelect);
//		StringBuffer sb=new StringBuffer();
//		sb.append("alert(GuanjzTf.getValue());\n");
		ToolbarButton Guanjzbtn = new ToolbarButton(null,"查找",
				  "function(){if(GuanjzTf.getValue().toString()==''||GuanjzTf.getValue()==null){return;}document.getElementById('LastButton').click();}");
		
		tb1.addItem(Guanjzbtn);
		
		setToolbar(tb1);
	}
	
	

	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) this.getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField) getToolbar().getItem("diancTree_text"))
						.setValue(((IDropDownModel) getDiancmcModel())
								.getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	public String WindowsScript;
    public String getWindowsScript(){
    	return this.WindowsScript;
    }
    public void setWindowsScript(String WindowsScript){
    	this.WindowsScript=WindowsScript;
    }

    
    public String getGridScript(){
    	
    	return "";
    }
	// 查找方式下拉菜单
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getWeizSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setWeizSelectValue(IDropDownBean Value) {
		if (Value != ((Visit) getPage().getVisit()).getDropDownBean3()) {

			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean3(Value);
		}
	}

	public void setWeizSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getWeizSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getWeizSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getWeizSelectModels() {
		List list = new ArrayList();
		Visit v=(Visit)this.getPage().getVisit();	
		
	
		list.add(new IDropDownBean(1, "按全称查找"));
		
		list.add(new IDropDownBean(2, "按简称查找"));
//		list.add(new IDropDownBean(3, "已审核的任务"));
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(list));
	}
//	查询是否设置煤源
	private boolean isShezhimeiyuan(){
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=con.getResultSetList("select distinct xx.zhi from xitxxb xx where xx.mingc='是否设置供煤类型'\n");
		
		boolean iss= rsl.next();
		con.Close();
		return iss;
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
			if(!visit.getActivePageName().toString().equals("Diancgysglsecond")){
				this.setTreeid(null);
				setDiancmcModel(null);
				this.setDiancmcValue(null);
			}else{
				this.setTreeid(visit.getString3());
			}
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			if("".equals(visit.getString1())){
//				方案ID未得到 的错误代码
			}
			this.setGuanjzTf("");
			//传电厂id的值
//			this.setTreeid(visit.getString3());
			setMultipleModel(null);
	     	setMultipleSelected(null);
//			visit.setDefaultTree(null);
//			getToolBars();
			setWeizSelectValue(null);
			setWeizSelectModel(null);
			setWindowsScript("");
			setMsg("");
		}
		
		getToolBars();
//		initMultipleSelected(); 
	}
}
