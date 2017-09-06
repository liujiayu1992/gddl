package com.zhiren.jt.het.hetdy;
/*
 * 作者：songy
 * 时间：2011-03-23 
 * 描述：修改下拉菜单的排序，要求按照名称进行排序
 */
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;

public class Hetdy extends BasePage {

	public boolean getRaw() {
		return true;
	}

	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}
	public void submit(IRequestCycle cycle) {
		getHetbhModels();
	}
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		//签订年份
		tb1.addText(new ToolbarText("签订年份:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("NianfDropDown");
		cb.setWidth(70);
		cb.setListeners(
				"select:function(own,newValue,oldValue){"+
                " document.Form0.submit();}" );    
		tb1.addField(cb);
//		//需方单位
//		tb1.addText(new ToolbarText("需方单位:"));
//		ComboBox cb = new ComboBox();
//		cb.setTransform("NianfDropDown");
//		cb.setWidth(80);
		//cb.setListeners("select:function(){document.Form0.submit();}");
		Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree"
				,""+visit.getDiancxxb_id(),"forms[0]",null,getTreeid());
		visit.setDefaultTree(dt);
		if(MainGlobal.getXitxx_item("合同", "显示需方", "0", "是").equals("是")){//

			
			TextField tf = new TextField();
			tf.setId("diancTree_text");
			tf.setWidth(100);
			tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
			
			ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
			tb2.setIcon("ext/resources/images/list-items.gif");
			tb2.setCls("x-btn-icon");
			tb2.setMinWidth(20);
			tb1.addText(new ToolbarText("单位:"));
			tb1.addField(tf);
			tb1.addItem(tb2);
		}
//		Visit visit = (Visit) getPage().getVisit();
//		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"forms[0]",null,getTreeid());
//		visit.setDefaultTree(dt);
//		TextField tf = new TextField();
//		tf.setId("diancTree_text");
//		tf.setWidth(100);
//		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
//		
//		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
//		tb2.setIcon("ext/resources/images/list-items.gif");
//		tb2.setCls("x-btn-icon");
//		tb2.setMinWidth(20);
//		
//		tb1.addText(new ToolbarText("-"));
//		tb1.addText(new ToolbarText("单位:"));
//		tb1.addField(tf);
//		tb1.addItem(tb2);
		//供方单位
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("供方单位:"));
		ComboBox cb1 = new ComboBox();
		cb1.setListeners(
				"select:function(own,newValue,oldValue){"+
                " document.Form0.submit();}" );  
		cb1.setTransform("gongfSelect");
		cb1.setWidth(100);
		tb1.addField(cb1);
		//合同状态
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("合同状态:"));
		ComboBox cb2 = new ComboBox();
		cb2.setListeners(
				"select:function(own,newValue,oldValue){"+
                " document.Form0.submit();}" );  
		cb2.setTransform("weiz");
		cb2.setWidth(80);
		tb1.addField(cb2);
		
		if(!visit.isDCUser()){
			
//			合同类型
			tb1.addText(new ToolbarText("-"));
			tb1.addText(new ToolbarText("合同类型:"));
			ComboBox cbLeix = new ComboBox();
			cbLeix.setListeners(
					"select:function(own,newValue,oldValue){"+
	                " document.Form0.submit();}" );  
			cbLeix.setTransform("LEIX");
			cbLeix.setWidth(80);
			tb1.addField(cbLeix);
		}
		
		//合同编号
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("合同编号:"));
		ComboBox cb3 = new ComboBox();
		cb3.setTransform("HetbhDropDown");
		cb3.setWidth(150);
		tb1.addField(cb3);
		//刷新
		tb1.addText(new ToolbarText("-"));
		ToolbarButton shuax=new ToolbarButton(null,"查询","function(){chax();}");
		shuax.setIcon(SysConstant.Btn_Icon_Print);
		tb1.addItem(shuax);
		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(this.getPageName().toString());
			visit.setString1("");
			visit.setDropDownBean1(null);
			visit.setDropDownBean2(null);
			visit.setDropDownBean3(null);
			visit.setDropDownBean4(null);
			visit.setDropDownBean5(null);
			setTreeid(null);
			visit.setProSelectionModel1(null);
			visit.setProSelectionModel2(null);
			visit.setProSelectionModel3(null);
			visit.setProSelectionModel4(null);
			visit.setProSelectionModel5(null);
//			合同类型
			visit.setDropDownBean6(null);
			visit.setProSelectionModel6(null);
		}
		getToolbars();
	}


//	// 厂别表下拉框
//	private boolean falg = false;
//
//	private IDropDownBean ChangbValue;
//
//	public IDropDownBean getChangbValue() {
//		if (ChangbValue == null) {
//			ChangbValue = (IDropDownBean) ChangbModel.getOption(0);
//		}
//		return ChangbValue;
//	}
//
//	public void setChangbValue(IDropDownBean Value) {
//		if (!(ChangbValue == Value)) {
//			ChangbValue = Value;
//			falg = true;
//		}
//
//	}
//
//	private IPropertySelectionModel ChangbModel;
//
//	public void setChangbModel(IPropertySelectionModel value) {
//		ChangbModel = value;
//	}
//
//	public IPropertySelectionModel getChangbModel() {
//		if (ChangbModel == null) {
//			getChangbModels();
//		}
//		return ChangbModel;
//	}
//
//	public IPropertySelectionModel getChangbModels() {
//		StringBuffer sql = new StringBuffer("SELECT ID,MINGC FROM CHANGBB");
//		ChangbModel = new IDropDownSelectionModel(sql);
//		return ChangbModel;
//	}

	// 下拉框
	public IDropDownBean getHetbhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit()).setDropDownBean1((IDropDownBean)getHetbhModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1() ;
	}

	public void setHetbhValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setHetbhModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getHetbhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getHetbhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void getHetbhModels() {
		String gongysb_idCon="";
		String leix_Con="";
		String liucztb_idCon="";
		String xufCondi="";
		if(getGongysDropDownValue().getId()!=-1){
			 gongysb_idCon=" and hetb.gongysb_id " +
				"in( select id\n" +
				" from gongysb\n" + 
				" where gongysb.id="+getGongysDropDownValue().getId()+" or gongysb.fuid="+getGongysDropDownValue().getId()+")";
		}
		if(this.getLeixSelectValue().getId()!=-1){
			
			leix_Con=" and hetb.leib="+this.getLeixSelectValue().getId();
		}
		if(getweizSelectValue().getId()!=-1){
			liucztb_idCon=
		   " where a.leibztb_id="+getweizSelectValue().getId();
		}
		if(Long.parseLong(getTreeid())!=-1&&MainGlobal.getXitxx_item("合同", "显示需方", "0", "是").equals("是")){//){
			xufCondi=" and hetb.diancxxb_id "+
				"in (select id\n" +
				"from(\n" + 
				"select id from diancxxb\n" + 
				"start with (fuid="+Long.parseLong(getTreeid())+" or shangjgsid="+Long.parseLong(getTreeid())+") \n" + 
				"connect by fuid=prior id\n" + 
				")\n" + 
				"union\n" + 
				"select id\n" + 
				"from diancxxb\n" + 
				"where id="+Long.parseLong(getTreeid())+")";
		}
		String sql =
			"select a.id,hetbh\n" +
			"from (\n" + 
			"select hetb.id,hetbh,decode(liucztb.leibztb_id,null,hetb.liucztb_id,leibztb_id)leibztb_id\n" + 
			"from hetb,liucztb\n" + 
			"where hetb.liucztb_id=liucztb.id(+)"+
			xufCondi+
			" and to_char(hetb.qiandrq,'YYYY')='"+getNianfValue().getId()+"'"+gongysb_idCon+leix_Con+
			" )a\n"+liucztb_idCon + " ORDER BY HETBH DESC";
//
//			"select id,hetbh\n" +
//			"from hetb\n" + 
//			"where hetb.diancxxb_id "+
//			"in (select id\n" +
//			" from(\n" + 
//			" select id from diancxxb\n" + 
//			" start with fuid="+getTreeid()+"\n" + 
//			" connect by fuid=prior id\n" + 
//			" )\n" + 
//			" union\n" + 
//			" select id\n" + 
//			" from diancxxb\n" + 
//			" where id="+getTreeid()+")"+gongysb_idCon+liucztb_idCon+" and to_char(hetb.qiandrq,'YYYY')="+getNianfValue().getId();
			
		setHetbhModel( new IDropDownModel(sql,"请选择"));
		return ;
	}
    //年份
	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit()).setDropDownBean2(getIDropDownBean(getNianfModel(),DateUtil.getYear(new Date())));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}
	public void setNianfValue(IDropDownBean Value) {
		if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean2()!=null){
			if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean2().getId()){
	    		((Visit) getPage().getVisit()).setboolean4(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean4(false);
	    	}
			 ((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}
	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
    public void getNianfModels() {
		List listNianf = new ArrayList();
		
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date())+2; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		 ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(listNianf)) ;
	}

    //位置
    public IDropDownBean getweizSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getweizSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean4();
    }
 
    public void setweizSelectValue(IDropDownBean Value) {
    	((Visit) getPage().getVisit()).setDropDownBean4(Value);
    }
    public void setweizSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }

    public IPropertySelectionModel getweizSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getweizSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }

    public void getweizSelectModels() {
    	String sql=
    		"select *\n" +
    		"from(\n" + 
    		"select　leibztb.id,leibztb.mingc\n" + 
    		"from leibztb,liuclbb\n" + 
    		"where leibztb.liuclbb_id=liuclbb.id and liuclbb.mingc='合同'\n" + 
    		"union\n" + 
    		"select id,mingc\n" + 
    		"from leibztb\n" + 
    		"where leibztb.liuclbb_id=0\n" + 
    		") order by mingc ";
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql,"全部")) ;
        return ;
    }
    // 供应商
    public IDropDownBean getGongysDropDownValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean5()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean)getGongysDropDownModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean5();
    }
    public void setGongysDropDownValue(IDropDownBean Value) {
//    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean5()!=null){
//	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean5().getId()){
//	    		((Visit) getPage().getVisit()).setBooleanData5(true);
//	    	}else{
//	    		((Visit) getPage().getVisit()).setBooleanData5(false);
//	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean5(Value);
//    	}
    }
    public void setGongysDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel5(value);
    }

    public IPropertySelectionModel getGongysDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
            getGongysDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel5();
    }
    public void getGongysDropDownModels() {
    	String sql=
    		"select distinct g.id,g.mingc from hetb h,gongysb g\n" +
    		"       where h.gongysb_id=g.id\n" + 
    		"             and g.leix=1 and to_char(h.qiandrq,'yyyy')='"+this.getNianfValue().getValue()+"' order by g.mingc";

        ((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(sql,"全部")) ;
        return ;
    }
   /////////////
    private IDropDownBean getIDropDownBean(IPropertySelectionModel model,long id) {
        int OprionCount;
        OprionCount = model.getOptionCount();
        for (int i = 0; i < OprionCount; i++) {
            if (((IDropDownBean) model.getOption(i)).getId() == id) {
                return (IDropDownBean) model.getOption(i);
            }
        }
        return null;
    }
//    合同类型_Begin
	public IDropDownBean getLeixSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean6((IDropDownBean) getLeixSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setLeixSelectValue(IDropDownBean Value) {
		
		((Visit) getPage().getVisit()).setDropDownBean6(Value);
	}

	public void setLeixSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getLeixSelectModel() {
		
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getLeixSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void getLeixSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(-1, "全部"));
		list.add(new IDropDownBean(0, Locale.dianccg_hetlx));
		list.add(new IDropDownBean(1, Locale.quyxs_hetlx));
		list.add(new IDropDownBean(2, Locale.quycg_hetlx));
		((Visit) getPage().getVisit())
				.setProSelectionModel6(new IDropDownModel(list));
	}
    
//    合同类型_End
    //ext
    private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
    public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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
	
	//
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
//	电厂名称
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel3() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel3();
	}
	
	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean3()==null){
			((Visit)getPage().getVisit()).setDropDownBean3((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean3();
	}
	
	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean3()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb  order by xuh";
		 setDiancmcModel(new IDropDownModel(sql)) ;
		 return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

		public void setDiancmcModel(IPropertySelectionModel _value) {
			((Visit)getPage().getVisit()).setProSelectionModel3(_value);
		}
	//
}
