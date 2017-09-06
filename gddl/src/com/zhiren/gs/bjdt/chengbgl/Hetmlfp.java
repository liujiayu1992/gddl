package com.zhiren.gs.bjdt.chengbgl;

import com.zhiren.common.Locale;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public  class Hetmlfp extends BasePage implements PageValidateListener {
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

	private void Shez(IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(getChange());
		cycle.activate("Hetmlfpmx");
	}
	
	public void Save() {
		String tableName="caightb";
		Visit visit = (Visit) getPage().getVisit();
		
		
		JDBCcon con = new JDBCcon();
		
		StringBuffer sql = new StringBuffer("begin \n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while(delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =").append(delrsl.getString(0)).append(";\n");
		}
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		
		String beiz="";
		

		while(mdrsl.next()) {
		
			beiz=mdrsl.getString("beiz");
		
			if("0".equals(mdrsl.getString("ID"))) {
				
				
				
				sql.append("insert into ").append(tableName).append("(id,riq,diancxxb_id,gongysb_id,jihkjb_id,hetl,farl," );
				sql.append("jiag,beiz)");
				sql.append("values (getnewid("+visit.getDiancxxb_id()+"),");
				sql.append("to_date('"+getNianfValue().getId()+"','yyyy'),");
				sql.append(visit.getDiancxxb_id());
				sql.append(",").append((getExtGrid().getColumn("gongysb_id").combo).getBeanId(mdrsl.getString("gongysb_id")));
				sql.append(",").append("1").append(",").append(mdrsl.getString("hetl"));
				sql.append(",").append(mdrsl.getString("farl"));
				sql.append(",").append(mdrsl.getString("jiag")).append(",").append("'"+beiz+"'").append(");\n");
								
			}else {
				
				sql.append("update ").append(visit.getExtGrid1().tableName).append(" set ");
				sql.append("riq=").append("to_date('"+getNianfValue().getId()+"','yyyy')").append(",");
                sql.append("gongysb_id=").append((getExtGrid().getColumn("gongysb_id").combo).getBeanId(mdrsl.getString("gongysb_id")));
                sql.append(",").append("jihkjb_id=").append("1");
                sql.append(",").append("hetl=").append(mdrsl.getString("hetl"));
                sql.append(",").append("farl=").append(mdrsl.getString("farl"));
                sql.append(",").append("jiag=").append(mdrsl.getString("jiag"));
                sql.append(",").append("beiz=").append("'"+beiz+"'");
				sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
			}
			
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
	}
	
	public String getValueSql(GridColumn gc, String value) {
		if("string".equals(gc.datatype)) {
			if(gc.combo != null) {
				if(gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				}else {
					return "'"+value+"'";
				}
			}else {
				return "'"+value+"'";
			}
			
		}else if("date".equals(gc.datatype)) {
			return "to_date('"+value+"','yyyy-mm-dd')";
		}else if("float".equals(gc.datatype)){
			if(gc.combo != null) {
				if(gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				}else {
					return "'"+value+"'";
				}
			}
			else {
				return value==null||"".equals(value)?"null":value;
			}
//			return value==null||"".equals(value)?"null":value;
		}else{
			return value;
		}
	}
	
	private boolean _RefurbishChick = false;
    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishChick = true;
    }
    
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ShezChick = false;

	public void ShezButton(IRequestCycle cycle) {
		_ShezChick = true;
	}
	private boolean _CopyButton = false;
	public void CopyButton(IRequestCycle cycle) {
		_CopyButton = true;
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
		
		if (_ShezChick) {
			_ShezChick = false;
			Shez(cycle);
		}
		
		if(_CopyButton){
			_CopyButton=false;
			CoypLastYearData();
		}
	}
	
	public void CoypLastYearData(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		// 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		intyear=intyear-1;
		//供应商条件
		
		//
		String strdiancTreeID = "";
		
		String copyData = "select n.*\n"
				+ "  from niancgjhb n, diancxxb d,gongysb g\n"
				+ " where n.diancxxb_id = d.id(+)\n"
				+ "   and n.gongysb_id=g.id(+)\n"
				+ "   "+strdiancTreeID+"\n"
				
				+ "   and to_char(n.riq, 'yyyy') = '"+intyear+"'";

		//System.out.println("复制去年的数据:"+copyData);
		ResultSetList rslcopy = con.getResultSetList(copyData);
		while(rslcopy.next()){
			
			long gongysb_id=rslcopy.getLong("gongysb_id");
			long diancxxb_id=rslcopy.getLong("diancxxb_id");
			long hej=rslcopy.getLong("hej");
			Date riq=rslcopy.getDate("riq");
			int year=DateUtil.getYear(riq);
			int yue=DateUtil.getMonth(riq);
			int day=DateUtil.getDay(riq);
			
			String strriq=year+1+"-"+yue+"-"+day;
			String _id = MainGlobal.getNewID(((Visit) getPage()
					.getVisit()).getDiancxxb_id());
			con.getInsert("insert into niancgjhb(id,gongysb_id,diancxxb_id,hej,riq) values(" +
					_id+","+gongysb_id +","+ diancxxb_id+","+hej+","+"to_date('"+strriq+"','yyyy-mm-dd'))");
					
		}
		
		con.Close();
		
		
	}
	
	


	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		// 工具栏的年份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}

		
		
		String chaxun = 
			"select c.id,g.mingc gongysb_id,c.hetl,c.farl,c.jiag,c.beiz from caightb c,gongysb g\n" +
			"where c.jihkjb_id=1 and c.gongysb_id=g.id " +
			"and to_char(c.riq,'yyyy')=" +getNianfValue().getId()+
			"and c.diancxxb_id="+visit.getDiancxxb_id();

		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("caightb");
		egu.setWidth("bodyWidth");
        egu.getColumn("id").setHidden(true);
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		ComboBox gongys=new ComboBox();
		egu.getColumn("gongysb_id").setEditor(gongys);
		String gongysSql="select id ,mingc  from gongysb where fuid=0  order by xuh ";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(gongysSql));
        egu.getColumn("gongysb_id").editor.allowBlank = false;
		egu.getColumn("hetl").setHeader("采购量（万吨）");
	    egu.getColumn("hetl").setWidth(90);

		egu.getColumn("farl").setHeader("热值（大卡）");
		egu.getColumn("farl").setWidth(90);

		egu.getColumn("jiag").setHeader("价格");
		egu.getColumn("jiag").setWidth(60);

		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(60);
				

		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(25);// 设置分页

		// ********************工具栏************************************************
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		
		

		// 设定工具栏下拉框自动刷新
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});");
		//egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		//
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addToolbarItem("{"+new GridButton("分配煤量","function(){" +
				"if(gridDiv_sm.getSelections().length <= 0 || gridDiv_sm.getSelections().length > 1){" +
				"Ext.MessageBox.alert('提示信息','请选择供应商！');" +
				"return;}" +
				"grid1_rcd = gridDiv_sm.getSelections()[0];" +
				"if(grid1_rcd.get('ID') == '0'){" +
				"Ext.MessageBox.alert('提示信息','在分配之前请先保存!');" +
				"return;}" +
				"grid1_history = grid1_rcd.get('ID');" +
				"var Cobj = document.getElementById('CHANGE');" +
				"Cobj.value = grid1_history;" +
				"document.getElementById('ShezButton').click();" +
				"}").getScript()+"}");
		

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
		if (getTbmsg() != null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem(
					"'<marquee width=200 scrollamount=2>" + getTbmsg()
							+ "</marquee>'");
		}
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
			this.setNianfValue(null);
			this.getNianfModels();
//			this.setJihkjValue(null);
//			this.getJihkjModels();
			setTbmsg(null);
			visit.setString1(null);
			getSelectData();
		}
		
			getSelectData();
		
		
	}
//	 年份
	private static IPropertySelectionModel _NianfModel;
	
	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
	//计划口径
	public boolean _Jihkjchange = false;
	public IDropDownBean getJihkjValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getJihkjModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setJihkjValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean5()){
			
			((Visit) getPage().getVisit()).setboolean3(true);
			
		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getJihkjModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

			getJihkjModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setJihkjModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getJihkjModels() {
		
			String sql ="select id,mingc from jihkjb ";


			((Visit) getPage().getVisit())
			.setProSelectionModel5(new IDropDownModel(sql));
	return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}


	

//	 得到登陆用户所在电厂或者分公司的名称
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return diancmc;

	}
	//得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getIDropDownDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}
	
}