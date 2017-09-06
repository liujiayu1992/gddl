package com.zhiren.shanxdted.diqmkext;

import java.text.SimpleDateFormat;

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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2009-11-03
 * 内容:煤矿地区组合设置
 */
public class Diqmkgroup extends BasePage implements PageValidateListener {
//	进行页面提示信息的设置
	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
//	    _msg = _value;
		_msg=MainGlobal.getExtMessageBox(_value,false);
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
//  页面变化记录
    private String Change;
    public String getChange() {
    	return Change;
    }
    public void setChange(String change) {
    	Change = change;
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
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}
	
	public String getTreeHtml() {
		if (getTree() == null){
			return "";
		}else {
			return getTree().getWindowTreeHtml(this);
		}

	}

	public String getTreeScript() {
		if (getTree() == null) {
			return "";
		}else {
			return getTree().getWindowTreeScript();
		}
	}
	
	
	private String treeid = "";

	public String getTreeid() {

		if (treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {

		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
	}
	

//	按钮事件处理

    private boolean _SaveChick = false;
    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }
    
    private boolean _PowerChick = false;
    public void PowerButton(IRequestCycle cycle) {
        _PowerChick = true;
    }
    
   
    
    public void submit(IRequestCycle cycle) {
    	if (_SaveChick) {
    		_SaveChick = false;
    		Save();
        }
    	if (_PowerChick) {
    		_PowerChick = false;
    		Power(cycle);
        }
    }

//  取得组数据
    public void getSelectData() {
    	Visit visit = (Visit) getPage().getVisit();
    	String DIQMKGROUP_ID=visit.getString12();
    	String sql = "";
    	
//    	sql = " select id,mingc,nvl((select quanc from shengfb where id=g.shengfb_id),'') shengfb_id,\n" +
//    			" decode( ( select kaissj from DIQMKZH where DIQMKGROUP_ID="+DIQMKGROUP_ID+" and MEIKXXB_ID=g.id),'',to_char(sysdate,'yyyy-MM-dd'), ( select to_char(kaissj,'yyyy-MM-dd') from DIQMKZH where DIQMKGROUP_ID="+DIQMKGROUP_ID+" and MEIKXXB_ID=g.id) )  kaissj,\n" +
//    			" decode( ( select jiessj from DIQMKZH where DIQMKGROUP_ID="+DIQMKGROUP_ID+" and MEIKXXB_ID=g.id),'',to_char(add_months(sysdate,600),'yyyy-MM-dd'), ( select to_char(jiessj,'yyyy-MM-dd') from DIQMKZH where DIQMKGROUP_ID="+DIQMKGROUP_ID+" and MEIKXXB_ID=g.id) )  jiessj,\n" +
//    			"decode( (select id from DIQMKZH where DIQMKGROUP_ID="+DIQMKGROUP_ID+" and MEIKXXB_ID=g.id),'',0,1) shifxz from meikxxb g order by g.mingc asc ";
    	
    	sql = 
    		"SELECT zh.ID, mk.mingc,\n" +
    		"nvl((select quanc from shengfb where id=mk.shengfb_id),'') shengfb_id,\n" + 
    		"kaissj,jiessj\n" + 
    		"FROM diqmkzh zh,meikxxb mk\n" + 
    		"WHERE zh.meikxxb_id=mk.id AND diqmkgroup_id=" + DIQMKGROUP_ID + "\n" +
    		"ORDER BY mk.mingc,zh.kaissj";
		JDBCcon con = new JDBCcon();
    	ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
//		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		egu.setTableName("DIQMKZH");
		egu.setWidth("bodyWidth");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
//		egu.addColumn(1, new GridColumn(GridColumn.ColType_Check));
		egu.getColumn("id").setHeader("编号");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor=null;
		
		egu.getColumn("mingc").setHeader("煤矿名称");
		egu.getColumn("mingc").setWidth(200);
		egu.getColumn("mingc").setDefaultValue("请选择");
		egu.getColumn("mingc").editor=null;
		
		egu.getColumn("shengfb_id").setHeader("省份");
		egu.getColumn("shengfb_id").editor=null;
		
		egu.getColumn("kaissj").setHeader("开始时间");
		egu.getColumn("kaissj").setDefaultValue(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
		egu.getColumn("jiessj").setHeader("结束时间");
		egu.getColumn("jiessj").setDefaultValue(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
		
		egu.getColumn("mingc").setEditor(new ComboBox());
		String mkSql = "select id,mingc from meikxxb order by mingc";
		egu.getColumn("mingc").setComboEditor("gridDiv", new IDropDownModel(mkSql));
		
		DateField kaissj=new DateField();
		egu.getColumn("kaissj").editor=kaissj;
		egu.getColumn("kaissj").setRenderer(" function(value){ return (value==null || value=='')?'':('object' != typeof(value)?value:value.dateFormat('Y-m-d'));} ");
		
		DateField jiessj=new DateField();
		egu.getColumn("jiessj").editor=jiessj;
		egu.getColumn("jiessj").setRenderer(" function(value){ return (value==null || value=='')?'':('object' != typeof(value)?value:value.dateFormat('Y-m-d'));} ");
		
		
		egu.addPaging(-1);
//
//		//是否显示选择电厂树
//		if (visit.isFencb()) {
//			egu.addTbarText("单位名称:");
//			ExtTreeUtil etu = new ExtTreeUtil("diancTree",
//					ExtTreeUtil.treeWindowType_Dianc,
//					((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
//			setTree(etu);
//			egu.addTbarTreeBtn("diancTree");
//			egu.addTbarText("-");
//		}
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		GridButton retu=new GridButton("返回","function(){document.all.PowerButton.click();} ");
		retu.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(retu);
		
		setExtGrid(egu);

    }
//	保存分组的改动
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();

		JDBCcon con=new JDBCcon();
		ResultSetList rsl = null;
		String sql = "";
		rsl = getExtGrid().getDeleteResultSet(getChange());
		if (rsl==null) {
			return;
		}
		while (rsl.next()) {
			sql += "delete from diqmkzh where id=" + rsl.getString("id") + ";\n";
		}
		
		rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl==null) {
			return;
		}
		
		while (rsl.next()) {
			if ("0".equals(rsl.getString("id"))) {
				sql += 
					"insert into diqmkzh (ID,diqmkgroup_id,meikxxb_id,kaissj,jiessj)\n" +
					"VALUES(getnewid(" + visit.getDiancxxb_id() + "), " 
					+ visit.getString12() + 
					"," + getExtGrid().getColumn("mingc").combo.getBeanId(rsl.getString("mingc")) + 
					",to_date('" + rsl.getString("kaissj") + "','yyyy-mm-dd')" + 
					",to_date('" + rsl.getString("jiessj") + "','yyyy-mm-dd')" +
					");\n";

			} else {
				sql += "UPDATE  diqmkzh SET  meikxxb_id=" + getExtGrid().getColumn("mingc").combo.getBeanId(rsl.getString("mingc")) 
				+ ",kaissj=to_date('" + rsl.getString("kaissj") + "','yyyy-mm-dd'),jiessj=to_date('" + rsl.getString("jiessj") + "','yyyy-mm-dd')" 
				+ " where id=" + rsl.getString("id") + ";\n";
			}
		}
		

		sql=" begin \n"+sql+" end;";
		
		int flag=0;
		if (sql.length()>13) {
			flag=con.getUpdate(sql);
		}
		 
		if(flag<0){
			this.setMsg("数据操作失败!");
		}else{
			this.setMsg("数据操作成功!");
		}
		con.Close();
	}
//	设置分组
	private void Power(IRequestCycle cycle) {
		
		cycle.activate("Diqmkext");
	}

//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setTreeid("");
		}
		init();
	} 
	
	private void init() {
		setExtGrid(null);
		setTree(null);
		getSelectData();
	}
//	页面判定方法
    public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}
}
