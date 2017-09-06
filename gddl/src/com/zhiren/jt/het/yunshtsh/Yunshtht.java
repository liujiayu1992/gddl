package com.zhiren.jt.het.yunshtsh;
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
	import com.zhiren.common.MainGlobal;
	import com.zhiren.common.ResultSetList;
	import com.zhiren.common.ext.ExtGridUtil;
	import com.zhiren.common.ext.ExtTreeUtil;
	import com.zhiren.common.ext.GridButton;
	import com.zhiren.common.ext.form.ComboBox;
	import com.zhiren.main.Visit;
	public class Yunshtht extends BasePage {
	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean5()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)+ "';";
		} else {
			return "";
		}
	}
	public boolean isQuanxkz(){
		return((Visit) getPage().getVisit()).getboolean4();
	}
	public void setQuanxkz(boolean value){
		((Visit) getPage().getVisit()).setboolean4(value);
	}
	private int _editTableRow = -1;// 编辑框中选中的行
	public int getEditTableRow() {
		return _editTableRow;
	}
	private String _msg;
	public void setMsg(String _value) {
		_msg = _value;
	}
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}
	private boolean TijButton = false;
	public void TijButton(IRequestCycle cycle) {
		TijButton = true;
	}
	private boolean HuitButton = false;
	public void HuitButton(IRequestCycle cycle) {
		HuitButton = true;
	}
	public void submit(IRequestCycle cycle) {
		if (TijButton) {
			TijButton = false;
			tij();
			getSelectData();
		}
		if (HuitButton) {
			HuitButton = false;
			huit();
			getSelectData();
		}
	}
	private Yunshtshbean _EditValue;
	public List getEditValues() {
		if(((Visit) getPage().getVisit()).getList5()==null){
			((Visit) getPage().getVisit()).setList5(new ArrayList());
		}
		return ((Visit) getPage().getVisit()).getList5();
	}
	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList5(editList);
	}
	public Yunshtshbean getEditValue() {
		return _EditValue;
	}
	public void setEditValue(Yunshtshbean EditValue) {
		_EditValue = EditValue;
	}
	public void getSelectData() {
		List list=getEditValues();
		String sql="";
		list.clear();
		JDBCcon con=new JDBCcon();
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),this.getTreeid());
		((Visit) getPage().getVisit()).setExtTree1(etu);
		
		 
		 sql=
			 "select h.id,h.mingc as hetmc,h.hetbh,h.gongfdwmc as yunsdwmc,mk.mingc as meikdwmc,\n" +
			 "jg.yunja,h.xufdwmc,h.qiandrq\n" + 
			 "from hetys h,hetysjgb jg,meikxxb mk\n" + 
			 "where h.id=jg.hetys_id\n" + 
			 "and jg.meikxxb_id=mk.id\n" + 
			 "and h.liucztb_id="+getweizSelectValue().getId()+"\n" +  //我的任务
			 "and to_char(h.qiandrq,'YYYY')="+getNianfValue().getId()+"\n" + 
			 "and h.diancxxb_id in (select id\n" + 
			 " from(\n" + 
			 " select id from diancxxb\n" + 
			 " start with fuid="+getTreeid()+"\n" + 
			 " connect by fuid=prior id\n" + 
			 " )\n" + 
			 " union\n" + 
			 " select id\n" + 
			 " from diancxxb\n" + 
			 " where id="+getTreeid()+")\n" + 
			 "order by  h.gongfdwmc,qiandrq,h.hetbh";

	 			 ResultSetList rs=con.getResultSetList(sql);
				 ExtGridUtil egu = new ExtGridUtil("SelectFrmDiv", rs);
					egu.getColumn("id").setHidden(true);
					egu.getColumn("hetmc").setHeader("运输合同名称");
					egu.getColumn("hetbh").setHeader("运输合同编号");
					egu.getColumn("yunsdwmc").setHeader("运输单位");
					egu.getColumn("meikdwmc").setHeader("煤矿单位");
					egu.getColumn("yunja").setHeader("运价");
					egu.getColumn("xufdwmc").setHeader("需方单位");
					egu.getColumn("qiandrq").setHeader("签订日期");	
					
					egu.addTbarText("签订年份:");
					//egu.addToolbarItem("cbo_NianfDropDown");
					ComboBox comb1=new ComboBox();
					comb1.setId("nianf");
					comb1.setWidth(100);
					comb1.setTransform("NianfDropDown");
					comb1.setLazyRender(true);//动态绑定
					
					egu.addToolbarItem(comb1.getScript());
					//
					egu.addTbarText("状态:");
					ComboBox comb2=new ComboBox();
					comb2.setId("weiz");
					comb2.setWidth(100);
					comb2.setTransform("weizSelect");
					comb2.setLazyRender(true);//动态绑定weizSelect
					egu.addToolbarItem(comb2.getScript());
					egu.addOtherScript("nianf.on('select',function(){document.forms[0].submit();});weiz.on('select',function(){document.forms[0].submit()});");
					egu.addOtherScript("SelectFrmDiv_sm.on('rowselect',function(own,row,record){document.all.item('EditTableRow').value=row;});");
					egu.addTbarTreeBtn("diancTree");
					egu.addToolbarItem("{"+new GridButton("提交","function(){document.getElementById('tijButton').click();}").getScript()+"}");
					egu.addToolbarItem("{"+new GridButton("回退","function(){document.getElementById('huitButton').click();}").getScript()+"}");
					egu.pagsize=0;
					((Visit) this.getPage().getVisit()).setExtGrid1(egu);
		}
	private void tij(){
		if(getEditTableRow()!=-1){
			//更新liucztb_id
			String sql="";
			JDBCcon con=new JDBCcon();
			sql="update hetys\n" +
			"set  hetys.liucztb_id=1\n" + 
			"where hetys.id="+((Visit) getPage().getVisit()).getExtGrid1().griddata[getEditTableRow()][0];
			con.getUpdate(sql);
			
		}
	}
	private void huit(){
		if(getEditTableRow()!=-1){
//			更新liucztb_id
			String sql="";
			JDBCcon con=new JDBCcon();
			sql="update hetys\n" +
			"set  hetys.liucztb_id=0\n" + 
			"where hetys.id="+((Visit) getPage().getVisit()).getExtGrid1().griddata[getEditTableRow()][0];
			con.getUpdate(sql);
		}
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean5(true);
			return;
		} else {
			visit.setboolean5(false);
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			((Visit) getPage().getVisit()).setDropDownBean1(null);
			((Visit) getPage().getVisit()).setProSelectionModel1(null);
			((Visit) getPage().getVisit()).setDropDownBean2(null);
			((Visit) getPage().getVisit()).setProSelectionModel2(null);
			((Visit) getPage().getVisit()).setDropDownBean3(null);
			((Visit) getPage().getVisit()).setProSelectionModel3(null);
			((Visit) getPage().getVisit()).setString2("");
			visit.setboolean4(true);
			setTreeid(null);
			
		}
		getSelectData();
	}
	//单位
	public IDropDownBean getdanwSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean1()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean1((IDropDownBean)getdanwSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean1();
    }
 
    public void setdanwSelectValue(IDropDownBean Value) {
    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean1()!=null){
	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean1().getId()){
	    		((Visit) getPage().getVisit()).setboolean3(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean3(false);
	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean1(Value);
    	}
    }
    public void setdanwSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel1(value);
    }

    public IPropertySelectionModel getdanwSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
            getdanwSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel1();
    }

    public void getdanwSelectModels() {
        String sql = 
        	"select id,mingc,jib\n" +
        	"from(\n" + 
        	" select id,mingc,0 as jib\n" + 
        	" from diancxxb\n" + 
        	" where id="+ ((Visit) getPage().getVisit()).getDiancxxb_id()+"\n" + 
        	" union\n" + 
        	" select *\n" + 
        	" from(\n" + 
        	" select id,mingc,level as jib\n" + 
        	"  from diancxxb\n" + 
        	" start with fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"\n" + 
        	" connect by fuid=prior id\n" + 
        	" order SIBLINGS by  xuh)\n" + 
        	" )\n" + 
        	" order by jib";
        List dropdownlist = new ArrayList();
		JDBCcon con = new JDBCcon();
		try {
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String mc = rs.getString("mingc");
				int jib=rs.getInt("jib");
				String nbsp=String.valueOf((char)0xA0);
				for(int i=0;i<jib;i++){
					mc=nbsp+nbsp+nbsp+nbsp+mc;
				}
				dropdownlist.add(new IDropDownBean(id, mc));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
        ((Visit) getPage().getVisit()).setProSelectionModel1(new IDropDownModel(dropdownlist)) ;
        return ;
    }
    //位置
    public IDropDownBean getweizSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getweizSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }
 
    public void setweizSelectValue(IDropDownBean Value) {
    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean2()!=null){
	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean2().getId()){
	    		((Visit) getPage().getVisit()).setboolean1(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean1(false);
	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean2(Value);
    	}
    }
    public void setweizSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel getweizSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            getweizSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }

    public void getweizSelectModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(0,"发起"));
        list.add(new IDropDownBean(1,"结束"));
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(list)) ;
        return ;
    }
    //年份
	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit()).setDropDownBean3(getIDropDownBean(getNianfModel(),new Date().getYear()+1900));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}
	public void setNianfValue(IDropDownBean Value) {
		if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean3()!=null){
			if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean3().getId()){
	    		((Visit) getPage().getVisit()).setboolean2(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean2(false);
	    	}
			 ((Visit) getPage().getVisit()).setDropDownBean3(Value);
		}
	}
	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
    public void getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = DateUtil.getYear(new Date())-2; i <= DateUtil.getYear(new Date())+2; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		 ((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(listNianf)) ;
	}
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
    //ext代码
	public String getGridHtml() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1().getHtml();
	}
	public String getGridScript() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1().getGridScript();
	}
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
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
}
