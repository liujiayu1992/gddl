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

public  class Shichtmlfp extends BasePage implements PageValidateListener {
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
		String tableName="caightmxb";
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
				
				
				
				sql.append("insert into ").append(tableName).append("(id,riq,diancxxb_id,gongysb_id,jihkjb_id,hetl,caightb_id)" );
				sql.append("values (getnewid("+visit.getDiancxxb_id()+"),");
				sql.append("to_date('"+getNianfValue().getId()+"','yyyy'),");
				sql.append(getDiancValue().getId());
				sql.append(",").append((getExtGrid().getColumn("gongysb_id").combo).getBeanId(mdrsl.getString("gongysb_id")));
				sql.append(",").append("2").append(",").append(mdrsl.getString("hetl")).append(",").append(0).append(");\n");
								
			}else {
				
				sql.append("update ").append(visit.getExtGrid1().tableName).append(" set ");
				sql.append("riq=").append("to_date('"+getNianfValue().getId()+"','yyyy')").append(",");
                sql.append("gongysb_id=").append((getExtGrid().getColumn("gongysb_id").combo).getBeanId(mdrsl.getString("gongysb_id")));
                sql.append(",").append("hetl=").append(mdrsl.getString("hetl"));
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

			"select 1 id, '"+getDiancValue().getValue()+"' diancxxb_id,'总量' as gongysb_id,sum(c.hetl) hetl\n" +
			"from caightmxb c  where c.diancxxb_id="+getDiancValue().getId()+" and c.riq=to_date('"+getNianfValue().getId()+"','yyyy')\n" + 
			"union\n" + 
			"select 1 id,'"+getDiancValue().getValue()+"' diancxxb_id,'合同供煤' as gongysb_id,sum(c.hetl) hetl\n" + 
			"from caightmxb c  where c.diancxxb_id="+getDiancValue().getId()+" and c.jihkjb_id=1 and c.riq=to_date('"+getNianfValue().getId()+"','yyyy')\n" + 
			"union\n" + 
			"select 1 id,'"+getDiancValue().getValue()+"' diancxxb_id,'市场采购' as gongysb_id,sum(c.hetl) hetl\n" + 
			"from caightmxb c  where c.diancxxb_id="+getDiancValue().getId()+" and c.jihkjb_id=2 and c.riq=to_date('"+getNianfValue().getId()+"','yyyy')\n" + 
			"union\n" + 
			"select c.id,d.mingc diancxxb_id,g.mingc gongysb_id,c.hetl\n" + 
			"from caightmxb c,diancxxb d, gongysb g where c.diancxxb_id=d.id and c.gongysb_id=g.id " +
			"and c.diancxxb_id="+getDiancValue().getId()+" and c.riq=to_date('"+getNianfValue().getId()+"','yyyy') and c.jihkjb_id=2";


		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("caightmxb");
		egu.setWidth("bodyWidth");
        egu.getColumn("id").setHidden(true);
        egu.getColumn("diancxxb_id").setHeader("电厂");
        egu.getColumn("diancxxb_id").setWidth(100);
        egu.getColumn("diancxxb_id").setDefaultValue(getDiancValue().getValue());
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		ComboBox gongys=new ComboBox();
		egu.getColumn("gongysb_id").setEditor(gongys);
		String gongysSql="select id ,mingc  from gongysb where fuid=0  order by xuh ";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(gongysSql));
        egu.getColumn("gongysb_id").editor.allowBlank = false;
		egu.getColumn("hetl").setHeader("采购量（万吨）");
	    egu.getColumn("hetl").setWidth(100);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(25);// 设置分页
		
		String zongl="";
		if(this.getHetl()==null){
			zongl="0";
		}else{
			zongl=getHetl();
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){"+

	              "if(e.field == 'HETL'){"+
		
	              "if(e.row>0){"+
					"var oldzongj="+zongl+";"+
					"var tmp = 0;"+
					"var zongj = 0;"+
					"var rec = gridDiv_ds.getRange();"+
					"zongj=eval(rec[1].get('HETL'));"+ 
					"for(var i=3;i<gridDiv_ds.getCount();i++ ){"+
					"tmp+=eval(rec[i].get('HETL'));"+
					"}"+
			        "gridDiv_ds.getAt(1).set('HETL',eval(tmp));"+
			        "gridDiv_ds.getAt(2).set('HETL',eval(oldzongj+tmp));"+
		"}"+
	"}"+

"});");

		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("if(e.record.get('GONGYSB_ID')=='合同供煤'){e.cancel=true;}");//当电厂列的值是"合计"时,这一行不允许编辑
		sb.append("if(e.record.get('GONGYSB_ID')=='市场采购'){e.cancel=true;}");
		sb.append("if(e.record.get('GONGYSB_ID')=='总量'){e.cancel=true;}");
	    
		sb.append("NIANF.on('select',function(){document.forms[0].submit();});");
		sb.append("DIANC.on('select',function(){document.forms[0].submit();});");
		sb.append("});");
		
		egu.addOtherScript(sb.toString());//当电厂列的值是"合计"时,这一行不允许编辑
		//sb.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");//电厂列不允许编辑

		// ********************工具栏************************************************
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("电厂:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("DIANC");
		comb2.setId("DIANC");// 和自动刷新绑定
		comb2.setLazyRender(true);// 动态绑定
		comb2.setWidth(100);
		egu.addToolbarItem(comb2.getScript());
		

		// 设定工具栏下拉框自动刷新
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});");
		egu.addOtherScript("DIANC.on('select',function(){document.forms[0].submit();});");
		//egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		//
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
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
//总量
	public String getHetl() {
		String hetl = "";
		JDBCcon cn = new JDBCcon();
		String sql_hetl = "select sum(hetl) hetl from caightmxb where diancxxb_id="
				+ getDiancValue().getId();
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
            this.setDiancValue(null);
            this.getDiancModels();
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
	//电厂
	public boolean _Diancchange = false;
	public IDropDownBean getDiancValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getDiancModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setDiancValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean5()){
			
			((Visit) getPage().getVisit()).setboolean3(true);
			
		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getDiancModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

			getDiancModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setJihkjModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getDiancModels() {
		
			String sql ="select id,mingc from diancxxb ";


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