package com.zhiren.pub.qichjcsjpp;

import java.sql.ResultSet;
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

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Qichysdwpp extends BasePage implements PageValidateListener {
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
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefurbishChick = false;
    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishChick = true;
    }
    private void Refurbish() {
            //为  "刷新"  按钮添加处理程序
    	getSelectData();
    }

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			if(Savepd()){
				Save();
			}else{
				setMsg("保存时发现重复的记录，保存失败！");
			}
			getSelectData();
		}
		if (_RefurbishChick) {
            _RefurbishChick = false;
            Refurbish();
        }
	}
	
//	保存条件判断:是否有重复的数据
	public boolean Savepd() {
		JDBCcon con = new JDBCcon();
        StringBuffer sb = new StringBuffer();
        boolean sv = true;
        String sql = "";
        int i = 0;
        String[][] rec = null;
        try {
        	sql = 
        		"select q.id,q.diancxxb_id,q.yunsdw,\n" +
    			"       y.mingc as yunsdwb_id \n" + 
    			"from qichysdwppb q,yunsdwb y \n" + 
    			"where q.yunsdwb_id = y.id\n" + 
    			"order by q.id";
        	sb.append(sql);
            ResultSet rs=con.getResultSet(sb.toString());
            ResultSetList rsl=this.getExtGrid().getModifyResultSet(this.getChange());
            while(rsl.next()){
            	while(rs.next()){
            		if(rsl.getString("DIANCXXB_ID").equals(rs.getString("DIANCXXB_ID"))&&rsl.getString("YUNSDW").equals(rs.getString("YUNSDW"))&&rsl.getString("YUNSDWB_ID").equals(rs.getString("YUNSDWB_ID"))){
            			sv = false;
            		}
            	}
            }
            rs.close();
        }catch (Exception e) {
        	e.printStackTrace();
        }         
		con.Close();
        return sv;
	}
	
//	长别下拉框取值

	public String getChangb() {
		return ((Visit) this.getPage().getVisit()).getString3();
	}
	public void setChangb(String changb) {
		((Visit) this.getPage().getVisit()).setString3(changb);
	}

	public void getSelectData() {
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String diancxxbid = "";
		if(((Visit) getPage().getVisit()).isFencb()){
			diancxxbid = ""+MainGlobal.getProperId(getFencbModel(),this.getChangb());
		}else{
			diancxxbid = ""+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		String sql = 
			"select q.id,q.diancxxb_id,q.yunsdw,\n" +
			"       y.mingc as yunsdwb_id \n" + 
			"from qichysdwppb q,yunsdwb y \n" + 
			"where q.yunsdwb_id = y.id\n" + 
			"	   and q.diancxxb_id="+diancxxbid+"\n" + 
			"order by q.id";
		
		ResultSetList rsl = con
				.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setTableName("qichysdwppb");		
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		egu.getColumn("id").setWidth(70);
		egu.getColumn("diancxxb_id").setHeader("DIANCXXB_ID");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").editor = null;
		egu.getColumn("diancxxb_id").setWidth(70);
		egu.getColumn("diancxxb_id").setDefaultValue(diancxxbid);
		egu.getColumn("yunsdw").setHeader("汽车衡运输单位");
		egu.getColumn("yunsdw").setWidth(120);		
		egu.getColumn("yunsdwb_id").setHeader("系统运输单位");
		egu.getColumn("yunsdwb_id").setWidth(120);

//		 设置运输单位下拉框
		ComboBox c4 = new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(c4);
		c4.setEditable(true);
		String ysdwSql = "select id,mingc from yunsdwb order by mingc";
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(ysdwSql));
		
		egu.addToolbarItem("{"+new GridButton("刷新","function(){document.getElementById('RefurbishButton').click();}").getScript()+"}");
		if(((Visit) getPage().getVisit()).isFencb()){
			egu.addToolbarButton(GridButton.ButtonType_Insert_condition,"","if(FencbDropDown.getRawValue()=='请选择'){Ext.MessageBox.alert('提示信息','请选择电厂名称'); return;}");
		}else{
			egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		}
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton",
				"var flag=true;\n"
				+ " var rec=gridDiv_ds.getRange();\n"
				+ "for(var i=0;i<rec.length;i++){\n"
				+ "    for(var j=i+1;j<rec.length;j++){\n"
				+ "        if(rec[i].get('DIANCXXB_ID')==rec[j].get('DIANCXXB_ID')&&rec[i].get('YUNSDW')==rec[j].get('YUNSDW')&&rec[i].get('YUNSDWB_ID')==rec[j].get('YUNSDWB_ID')){\n"
				+ "            Ext.MessageBox.alert('提示信息','无需重复保存！');\n"
				+ "            flag=false;\n"
				+ "            break;\n"
				+ "        }\n"
				+ "    }\n"
				+ "}\n"
				+ "if(!flag){	\n"
				+ "		return;	\n"
				+ "}	\n");
		
		if(((Visit) getPage().getVisit()).isFencb()){
			egu.addTbarText("-");
			
			egu.addTbarText("厂别:");
			ComboBox comb5 = new ComboBox();
			comb5.setTransform("FencbDropDown");
			comb5.setId("FencbDropDown");
			comb5.setEditable(false);
			comb5.setLazyRender(true);// 动态绑定
			comb5.setWidth(135);
			comb5.setReadOnly(true);
			egu.addToolbarItem(comb5.getScript());
		}
		
		
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
			visit.setString3("");	//厂別
			visit.setList1(null);
			setFencbValue(null);	//5
			setFencbModel(null);
			getFencbModels();		//5
			getSelectData();
		}
	}
	
//	厂别
	public boolean _Fencbchange = false;
	public IDropDownBean getFencbValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getFencbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setFencbValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean5()){
			
			((Visit) getPage().getVisit()).setboolean3(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getFencbModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

			getFencbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setFencbModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getFencbModels() {
		
			String sql ="select id,mingc from diancxxb d where d.fuid="+
			((Visit) getPage().getVisit()).getDiancxxb_id()+"order by mingc";


			((Visit) getPage().getVisit())
			.setProSelectionModel5(new IDropDownModel(sql,"请选择"));
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}
}
