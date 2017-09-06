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
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public  class Meilfpmx extends BasePage implements PageValidateListener {
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

	public void Save() {
		String tableName="caigysmxb";
		Visit visit = (Visit) getPage().getVisit();
		
		
		JDBCcon con = new JDBCcon();
		
		StringBuffer sql = new StringBuffer("begin \n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while(delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =").append(delrsl.getString(0)).append(";\n");
		}
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		

		while(mdrsl.next()) {
		
			if("0".equals(mdrsl.getString("ID"))) {
				
				sql.append("insert into ").append(tableName).append("(id,riq,diancxxb_id,gongysb_id,jihkjb_id,hetl,yushtb_id,beiz)" );
				sql.append("values (getnewid("+visit.getDiancxxb_id()+"),");
				sql.append("to_date((select to_char(riq,'yyyy') from caigysb where id ="+visit.getString1()+"),'yyyy'),");
				sql.append((getExtGrid().getColumn("diancxxb_id").combo).getBeanId(mdrsl.getString("diancxxb_id")));
				sql.append(",").append("(select gongysb_id from caigysb where id="+visit.getString1()+")");
				sql.append(",").append("1").append(",").append(mdrsl.getString("hetl"));
				sql.append(",").append(visit.getString1());
				sql.append(",").append("'"+mdrsl.getString("beiz")+"'");
				sql.append(");\n");
								
			}else {
				
				sql.append("update ").append(visit.getExtGrid1().tableName).append(" set ");
                sql.append("diancxxb_id=").append((getExtGrid().getColumn("diancxxb_id").combo).getBeanId(mdrsl.getString("diancxxb_id")));
                sql.append(",").append("hetl=").append(mdrsl.getString("hetl"));
                sql.append(",").append("beiz=").append("'"+mdrsl.getString("beiz")+"'");
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
	private boolean _CopyButton = false;
	public void CopyButton(IRequestCycle cycle) {
		_CopyButton = true;
	}
	
	private boolean _ReturnChick = false;
    public void ReturnButton(IRequestCycle cycle) {
        _ReturnChick = true;
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
		if (_ReturnChick) {
    		_ReturnChick = false;
    		cycle.activate("Meilfp");
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
			"select c.id, '剩余煤量'as diancxxb_id,(c.hetl-nvl((select sum(hetl)  from caigysmxb where yushtb_id="+visit.getString1() +
			"),0)) hetl,beiz from caigysb c\n" + 
			"where id=" + visit.getString1()+"\n"+
			"union\n" + 
			"select cm.id,d.mingc ,cm.hetl,cm.beiz from caigysmxb cm,diancxxb d where d.id=cm.diancxxb_id and" +
			" cm.yushtb_id="+visit.getString1()+"\n" ;

		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("caigysmxb");
		egu.setWidth("bodyWidth");
        egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
		ComboBox dc=new ComboBox();
		egu.getColumn("diancxxb_id").setEditor(dc);
		String dcSql="select id ,mingc  from diancxxb order by xuh ";
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcSql));
		
		egu.getColumn("hetl").setHeader("采购量（万吨）");
	    egu.getColumn("hetl").setWidth(100);

		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(60);
				

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(25);// 设置分页

		// /设置按钮
		
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){"+

	              "if(e.field == 'HETL'){"+
		
	              "if(e.row>0){"+
					"var oldzongj="+getHetl()+";"+
					"var tmp = 0;"+
					"var zongj = 0;"+
					"var rec = gridDiv_ds.getRange();"+
					"zongj=eval(rec[0].get('HETL'));"+ 
					"for(var i=1;i<gridDiv_ds.getCount();i++ ){"+
					"tmp+=eval(rec[i].get('HETL'));"+
					"}"+
					"if(eval(oldzongj-tmp)<0){Ext.MessageBox.alert('超出剩余量');}else{"+
			        "gridDiv_ds.getAt(0).set('HETL',eval(oldzongj-tmp));}"+
		"}"+
	"}"+

"});");


		
		
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("if(e.record.get('DIANCXXB_ID')=='剩余煤量'){e.cancel=true;}");//当电厂列的值是"合计"时,这一行不允许编辑
		//sb.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");//电厂列不允许编辑
		sb.append("});");
		
		egu.addOtherScript(sb.toString());
		
		// *****************************************设置默认值****************************
		


			//egu.getColumn("diancxxb_id").setEditor(new ComboBox());
//			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
//					new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc"));
//			ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");			
//			String mingc="";
//			if(r.next()){
//				mingc=r.getString("mingc");
//			}
//			egu.getColumn("diancxxb_id").setDefaultValue(mingc);
			

		
		//
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		GridButton btnreturn = new GridButton("返回",
		"function (){document.getElementById('ReturnButton').click()}");
		btnreturn.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(btnreturn);
//		egu.addTbarText("->");
//		egu.addTbarText("<font color=\"#EE0000\">单位:万吨</font>");
		
		
//		String BgColor="";
//		String sql_color="select zhi from xitxxb where mingc ='总计小计行颜色' and zhuangt=1 ";
//		
//		rsl = con.getResultSetList(sql_color);
//		
//		if (rsl.next()){
//			BgColor=rsl.getString("zhi");
//		}
//		rsl.close();
		
		//---------------页面js的计算开始------------------------------------------
//		StringBuffer sb = new StringBuffer();
//		sb.append("gridDiv_grid.on('afteredit',function(e){");
//			sb.append("e.record.set('DAOCJ',Round(parseFloat(e.record.get('CHEBJG')==''?0:e.record.get('CHEBJG'))+parseFloat(e.record.get('YUNF')==''?0:e.record.get('YUNF'))+parseFloat(e.record.get('ZAF')==''?0:e.record.get('ZAF')),2));\n"
//					+ "if (e.record.get('REZ')!=0){e.record.set('BIAOMDJ',Round(eval(Round((parseFloat(e.record.get('CHEBJG')==''?0:e.record.get('CHEBJG'))/1.17+parseFloat(e.record.get('YUNF')==''?0:e.record.get('YUNF'))*(1-0.07)+eval(e.record.get('ZAF')||0))*29.271/parseFloat(e.record.get('ZAF')==''?0:e.record.get('ZAF')),2)||0),2));}\n"
//					+ "\n");
//			sb.append("if(!(e.field == 'DIANCXXB_ID'||e.field == 'GONGYSB_ID'||e.field == 'JIHKJB_ID'||e.field == 'FAZ_ID'||e.field == 'DAOZ_ID')){e.record.set('QUANN',parseFloat(e.record.get('Y1')==''?0:e.record.get('Y1'))+parseFloat(e.record.get('Y2')==''?0:e.record.get('Y2'))+parseFloat(e.record.get('Y3')==''?0:e.record.get('Y3'))+parseFloat(e.record.get('Y4')==''?0:e.record.get('Y4'))" +
//					" +parseFloat(e.record.get('Y5')==''?0:e.record.get('Y5'))+parseFloat(e.record.get('Y6')==''?0:e.record.get('Y6'))+parseFloat(e.record.get('Y7')==''?0:e.record.get('Y7'))+parseFloat(e.record.get('Y8')==''?0:e.record.get('Y8'))+parseFloat(e.record.get('Y9')==''?0:e.record.get('Y9'))" +
//					"  +parseFloat(e.record.get('Y10')==''?0:e.record.get('Y10'))+parseFloat(e.record.get('Y11')==''?0:e.record.get('Y11'))+parseFloat(e.record.get('Y12')==''?0:e.record.get('Y12')) )};");
//			
//		sb.append("});");
//		
//		egu.addOtherScript(sb.toString());
		//---------------页面js计算结束--------------------------
		

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
			
			setTbmsg(null);
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
    //合同量
	public String getHetl() {
		String hetl = "";
		JDBCcon cn = new JDBCcon();
		String hetlid = ((Visit) getPage().getVisit()).getString1();
		String sql_hetl = "select id,hetl from caigysb where id="
				+ hetlid;
		ResultSet rs = cn.getResultSet(sql_hetl);
		try {
			while (rs.next()) {
				hetl = rs.getString("hetl");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return hetl;

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