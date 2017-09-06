package com.zhiren.dc.feiyglb;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者:zsj
 * 通过煤矿关联所有费用
*/
public class MeikFeiyglb extends BasePage implements PageValidateListener {
	
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	
	protected void initialize() {
		super.initialize();
		msg = "";
	}

	public IDropDownBean getMeikxxValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			
			((Visit) getPage().getVisit())
				.setDropDownBean2((IDropDownBean) getIMeikxxModels().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setMeikxxValue(IDropDownBean Value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean2()!=Value){
			
//			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}

	public void setIMeikxxModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getIMeikxxModel() {
		
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			
			getIMeikxxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getIMeikxxModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "select id,mingc from meikxxb order by mingc";
			((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
	
//	tree_begin
	public String getTreeid() {
		
		if(((Visit) getPage().getVisit()).getString1()==null||((Visit) getPage().getVisit()).getString1().equals("")){
			
			((Visit) getPage().getVisit()).setString1(String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setTreeid(String treeid) {
		
		if(!((Visit) getPage().getVisit()).getString1().equals(treeid)){
			
			((Visit) getPage().getVisit()).setString1(treeid);
			((Visit) getPage().getVisit()).setboolean1(true);
		}
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
//	tree_end
	
	/////
	public boolean _shoukdwchange = false;

	private IDropDownBean _ShoukdwValue;

	public IDropDownBean getShoukdwValue() {
		if (_ShoukdwValue == null) {
			_ShoukdwValue = (IDropDownBean) getIShoukdwModels().getOption(0);
		}
		return _ShoukdwValue;
	}

	public void setShoukdwValue(IDropDownBean Value) {
		long id = -2;
		if (_ShoukdwValue != null) {
			id = _ShoukdwValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_shoukdwchange = true;
			} else {
				_shoukdwchange = false;
			}
		}
		_ShoukdwValue = Value;
	}

	private IPropertySelectionModel _IShoukdwModel;

	public void setIShoukdwModel(IPropertySelectionModel value) {
		_IShoukdwModel = value;
	}

	public IPropertySelectionModel getIShoukdwModel() {
		if (_IShoukdwModel == null) {
			getIShoukdwModels();
		}
		return _IShoukdwModel;
	}

	public IPropertySelectionModel getIShoukdwModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select id,quanc from shoukdw order by mingc";
			_IShoukdwModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IShoukdwModel;
	}
	
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}

		if (_RefreshChick) {
			_RefreshChick = false;
		}
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
	}
	
	public void Save1(String strchange, Visit visit) {

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		String tableName1 = "feiyglb";
		String tableName2 = "feiyxmmkglb";
		String str_Feiyglb_ID = "";
		
		ResultSetList delrsl = this.getExtGrid().getDeleteResultSet(strchange);
		while(delrsl.next()) {
			// 删除操作
			sql.append("delete from ").append(tableName1).append(" where id = ").append(delrsl.getString("ID")).append(";\n");
			sql.append("delete from ").append(tableName2).append(" where feiyglb_id = ").append(delrsl.getString("ID")).append(";\n");
		}
		
		ResultSetList mdrsl = this.getExtGrid().getModifyResultSet(strchange);
		while(mdrsl.next()) {
			
			StringBuffer sql2 = new StringBuffer(); // 保存要插入到feiyglb表的values值
			StringBuffer sql3 = new StringBuffer(); // 保存要插入到feiyxmmkglb表的字段
			StringBuffer sql4 = new StringBuffer(); // 保存要插入到feiyxmmkglb表的values值
			
			if("0".equals(mdrsl.getString("ID"))) {
				// 插入操作
//				插入 feiyglb_begin
				sql.append("insert into ").append(tableName1).append("(id, diancxxb_id, meikxxb_id, feiylbb_id, shoukdwb_id, meikyfxtgys");
				str_Feiyglb_ID = MainGlobal.getNewID(Long.parseLong(this.getTreeid()));
				sql2.append(str_Feiyglb_ID).append(",").append(this.getTreeid()).append(",").append(this.getMeikxxValue().getId()).append(",")
					.append(this.getFeiylxValue().getId()).append(",0,1");
				sql.append(") values(").append(sql2).append(");	\n");
//				插入 feiyglb_end
				
//				插入 feiyxmmkglb_begin
				sql3.append("insert into ").append(tableName2).append("(id, feiyglb_id, feiyxmb_id, shifsy");
				sql4.append("getnewid(").append(this.getTreeid()).append("), ").append(str_Feiyglb_ID).append(", ")
					.append(getExtGrid().getValueSql(getExtGrid().getColumn("FEIYMC"),mdrsl.getString("FEIYMC")))
					.append(",").append(getExtGrid().getValueSql(getExtGrid().getColumn("SHIFSY"),mdrsl.getString("SHIFSY")));
				sql3.append(") values(").append(sql4).append(");	\n");
//				插入 feiyxmmkglb_end
				sql.append(sql3).append("\n");
				
				
			}else {
				// 更新操作
				sql.append("update ").append(tableName2).append(" set feiyxmb_id = ")
					.append(getExtGrid().getValueSql(getExtGrid().getColumn("FEIYMC"),mdrsl.getString("FEIYMC")))
					.append(",").append("shifsy = ")
					.append(getExtGrid().getValueSql(getExtGrid().getColumn("SHIFSY"),mdrsl.getString("SHIFSY")));
				
				sql.append(" where feiyglb_id = ").append(mdrsl.getString("ID")).append("; \n");
			}
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
		con.Close();
	}
	
	public void getSelectData() {
	    
		JDBCcon con = new JDBCcon();
		
		try{
			
			String str = 
				"select fygl.id, mkxx.mingc meikxxb_id,\n" +
				"  fylb.mingc as feiylbb_id,\n" + 
				"  fymc.mingc||' '||fyxm.gongs  as feiymc,\n" + 
				"  decode(fyxmmkgl.shifsy, 1, '是', 0, '否') shifsy \n" + 
				"from feiyxmb fyxm, feiymcb fymc, feiyxmmkglb fyxmmkgl, feiyglb fygl, meikxxb mkxx, feiylbb fylb\n" + 
				"where fyxm.feiymcb_id = fymc.id\n" + 
				"  and fyxmmkgl.feiyxmb_id = fyxm.id\n" + 
				"  and fyxmmkgl.feiyglb_id = fygl.id\n" +
				"  and fygl.diancxxb_id = fyxm.diancxxb_id\n " +
				"  and fygl.meikxxb_id = mkxx.id\n" + 
				"  and fygl.feiylbb_id = fylb.id\n" + 
				"  and mkxx.id = "+ this.getMeikxxValue().getId() +"\n" + 
				"  and fyxm.feiylbb_id = "+this.getFeiylxValue().getId()+"\n" + 	 
				"order by fyxm.id";
			
		ResultSetList rsl = con.getResultSetList(str);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		
		egu.setTableName("feiyglb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader("矿别");	
		egu.getColumn("meikxxb_id").setWidth(150);
		egu.getColumn("feiylbb_id").setHeader("费用类别");
		egu.getColumn("feiymc").setHeader("费用项目");
	
		egu.getColumn("shifsy").setHeader("是否使用");
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setDefaultValue(this.getMeikxxValue().getValue());
		egu.getColumn("feiylbb_id").setEditor(null);
		egu.getColumn("feiylbb_id").setDefaultValue(this.getFeiylxValue().getValue());
		
		egu.getColumn("feiymc").setEditor(new ComboBox());
		str = "select xm.id,mc.mingc||' '||xm.gongs as mingc from feiymcb mc,feiyxmb xm\n" +
			"       where mc.id = xm.feiymcb_id\n" + 
			"             and (xm.diancxxb_id = "+this.getTreeid()+" or xm.diancxxb_id in\n" + 
			"             (select id from diancxxb where id in (\n" + 
			"             select fuid from diancxxb where id= "+this.getTreeid()+") and jib=3))\n" + 
			"        order by mingc";
		egu.getColumn("feiymc").setComboEditor(egu.gridId, new IDropDownModel(str));
		egu.getColumn("feiymc").setWidth(450);
		egu.getColumn("feiymc").setReturnId(true);
		
		List shifsy = new ArrayList();
		shifsy.add(new IDropDownBean(1, "是"));
		shifsy.add(new IDropDownBean(0, "否"));
		
		egu.getColumn("shifsy").setEditor(new ComboBox());
		egu.getColumn("shifsy").setComboEditor(egu.gridId,	new IDropDownModel(shifsy));
		egu.getColumn("shifsy").setDefaultValue("是");
		egu.getColumn("shifsy").setReturnId(true);
		egu.getColumn("shifsy").setWidth(60);
		
//		设置树_Begin
		egu.addTbarText("单位：");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		egu.addTbarText("-");
//		煤矿单位
		egu.addTbarText("煤矿单位：");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("MeikxxDropDown");
		comb1.setId("meikxxdpd");
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(130);
		comb1.setListWidth(150);
		egu.addToolbarItem(comb1.getScript());
		egu.addOtherScript("meikxxdpd.on('select',function(){document.forms[0].submit();});");
//		费用类型
		egu.addTbarText("费用类型：");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("FeiylxDropDown");
		comb2.setId("feiylxdpd");
		comb2.setLazyRender(true);// 动态绑定
		comb2.setWidth(130);
		comb2.setListWidth(150);
		egu.addToolbarItem(comb2.getScript());
		egu.addOtherScript("feiylxdpd.on('select',function(){document.forms[0].submit();});");
		
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
	    egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
	    
		setExtGrid(egu);
		rsl.close();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
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
			visit.setList1(null);
			setFeiylxValue(null);	//费用类别DropDownBean1
			setIFeiylxModel(null);	//费用类别ProSelectionModel1
			
			setMeikxxValue(null);	//煤矿单位DropDownBean2
			setIMeikxxModel(null);	//煤矿单位ProSelectionModel2
			
			setTreeid("");
			setTree(null);
			
			getIMeikxxModels();
			getIFeiylxModels();
			
		}
		getSelectData();
	}
	
//	 费用类别
	
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
	public IDropDownBean getFeiylxValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
				.setDropDownBean1((IDropDownBean) getIFeiylxModels().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setFeiylxValue(IDropDownBean Value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean1()!=Value){
			
//			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean1(Value);
		}
	}

	public void setIFeiylxModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getIFeiylxModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getIFeiylxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getIFeiylxModels() {
		
		
		String sql = "select id,mingc from FEIYLBB where leib>=1 and leib<10 order by id ";
		((Visit) getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
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
	
}