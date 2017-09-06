package com.zhiren.dc.jilgl.jih;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Jihkxx extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String Changeid;

	public String getChangeid() {
		return Changeid;
	}

	public void setChangeid(String changeid) {
		Changeid = changeid;
	}
	
	private String Parameters;// 记录ID

	public String getParameters() {

		return Parameters;
	}

	public void setParameters(String value) {

		Parameters = value;
	}
	
	
	String tiaoxm;
	
	public String getTiaoxm(){
		
		Date date = new Date();
		long da=date.getTime();
		String j="";
		int[] randoms = {0,1,2,3,4,5,6,7,8,9}; 
		Random rnd = new Random(); 
		int x = 0; 
		int count = 0; 
		while(count != 3){
		x = rnd.nextInt(9); 
		if(randoms[x] != -1){ 
		j=j+""+randoms[x]; 
		randoms[x] = -1; 
		count ++;
		da++;
			} 
		}
		return ""+da;
	}
	
	
	public void SetTiaoxm(String value){
		tiaoxm = value;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
		
	}
	
	public void Save1(String strchange, Visit visit) {
		
		JDBCcon con = new JDBCcon();
		String tableName1 = "jihkxxb";//计划卡信息表
		String tableName2 = "jihkbmb";//计划卡编码表
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList delrsl = visit.getExtGrid1()
				.getDeleteResultSet(strchange);
		while (delrsl.next()) {
			//删除计划卡信息表中的记录
			sql.append("delete from ").append(tableName1).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
			//删除计划卡编码表中jihkxxb_id与计划卡信息表id相同的记录
			sql.append("delete from ").append(tableName2).append(" where jihkxxb_id =")
			.append(delrsl.getString(0)).append(";\n");
			
		}
		//获得序列中的最新ID
		int newId = Integer.parseInt(MainGlobal.getNewID(visit.getDiancxxb_id()));
		

		//对计划卡信息表进行操作
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while (mdrsl.next()) {

		
			StringBuffer sql2 = new StringBuffer();

			sql2.append(newId);
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(tableName1).append("(id");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sql.append(",").append(mdrsl.getColumnNames()[i]);
					
						sql2.append(",").append(
								getValueSql(visit.getExtGrid1().getColumn(
										mdrsl.getColumnNames()[i]), mdrsl
										.getString(i)));
					
				}

				sql.append(") values(").append(sql2).append(");\n");

			} else {

				sql.append("update ").append(tableName1).append(" set ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {

					sql.append(mdrsl.getColumnNames()[i]).append(" = ");
					
						sql.append(
								getValueSql(visit.getExtGrid1().getColumn(
										mdrsl.getColumnNames()[i]), mdrsl
										.getString(i))).append(",");
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(
						";\n");
			}
			//对计划卡信息编码进行操作

			String ches = mdrsl.getString("ches");
			int cs = Integer.parseInt(ches);
			//Date date = new Date();
			for(int k=1;k<=cs;k++) {
				sql.append("insert into ").append(tableName2).append("(id,jihkxxb_id,tiaoxm,fakrq,fakr,jiesr,beiz) ");
				sql.append("");

				sql.append(" values(").append("getnewid(").append(visit.getDiancxxb_id()).append("),").append(newId).append(",gettiaoxm,").append("sysdate").append(",'','',''").
				append("").append(");\n");

			}
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
	}
	
	public String getValueSql(GridColumn gc, String value) {
		if ("string".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return "'" + value + "'";
			}

		} else if ("date".equals(gc.datatype)) {
			return "to_date('" + value + "','yyyy-mm-dd')";
		} else if ("float".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return value == null || "".equals(value) ? "null" : value;
			}
			// return value==null||"".equals(value)?"null":value;
		} else {
			return value;
		}
	}
	

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	
	private boolean _ChakChick = false;

	public void ChakButton(IRequestCycle cycle) {
		_ChakChick = true;
	}
	
	private void Update(IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setString10("Jihkxx");
		visit.setLong1(Integer.parseInt(getChangeid()));
		cycle.activate("Jihkbm");
	}
	
	

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
//		if (_DeleteChick) {
//			_DeleteChick = false;
//			Delete();
//			getSelectData();
//		}
		if (_ChakChick) {
			_ChakChick = false;
			Update(cycle);
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		// 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		
		long intmonth;
		if (getYuefValue() == null) {
			intmonth = DateUtil.getMonth(new Date());
		} else {
			intmonth = getYuefValue().getId();
		}
		String riq = DateUtil.FormatDate(new Date());

		
		ResultSetList rsl = con
				.getResultSetList(
"select jh.id,\n" +
"       jh.riq,\n" + 
"       dc.mingc as diancxxb_id,\n" + 
"       g.mingc as gongysb_id,\n" + 
"       m.mingc as meikxxb_id,\n" + 
"       p.mingc as pinzb_id,\n" + 
"       j.mingc as jihkjb_id,\n" + 
"       jh.kaisrq,\n" + 
"       jh.jiesrq,\n" + 
"       jh.ches,\n" + 
"       jh.lursj,\n" + 
"       jh.caozy,\n" + 
"       jh.zhuangt,\n" + 
"       jh.beiz\n" + 
"  from jihkxxb jh, diancxxb dc,gongysb g,meikxxb m,pinzb p,jihkjb j\n" + 
"where jh.diancxxb_id = dc.id\n" + 
"and jh.gongysb_id = g.id\n" + 
"and jh.meikxxb_id = m.id\n" + 
"and jh.pinzb_id = p.id\n" + 
"and jh.jihkjb_id = j.id\n" +
"and riq = to_date('"+intyear+"-"+intmonth+"-01','yyyy-mm-dd')");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("jihkxxb");
		// /设置显示列名称
		egu.getColumn("id").setHidden(true);
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setDefaultValue(""+intyear+"-"+intmonth+"-01");
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("diancxxb_id").setHeader("电厂");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setDefaultValue(""+visit.getDiancxxb_id());
		egu.getColumn("gongysb_id").setHeader("供应商");
		egu.getColumn("meikxxb_id").setHeader("煤矿单位");
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("kaisrq").setHeader("开始日期");
		egu.getColumn("kaisrq").setDefaultValue(riq);
		egu.getColumn("jiesrq").setHeader("结束日期");
		egu.getColumn("jiesrq").setDefaultValue(riq);
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("lursj").setHeader("录入时间");
		egu.getColumn("lursj").setDefaultValue(riq);
		egu.getColumn("caozy").setHeader("操作员");
		egu.getColumn("caozy").setDefaultValue(visit.getRenymc());
		egu.getColumn("zhuangt").setHeader("状态");
		egu.getColumn("zhuangt").setDefaultValue("1");
		egu.getColumn("beiz").setHeader("备注");

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setWidth(1024);
		
//		*************************下拉框*****************************************88
		ComboBox cb_gongys=new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cb_gongys);
		cb_gongys.setEditable(true);
		String GongysSql="select id,mingc from gongysb ";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(GongysSql));
		egu.getColumn("gongysb_id").setReturnId(true);
		
		ComboBox cb_meik=new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(cb_meik);
		cb_meik.setEditable(true);
		String MeikSql="select id,mingc from meikxxb ";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, new IDropDownModel(MeikSql));
		egu.getColumn("meikxxb_id").setReturnId(true);
		
		ComboBox cb_pinz=new ComboBox();
		egu.getColumn("pinzb_id").setEditor(cb_pinz);
		cb_pinz.setEditable(true);
		String PinzSql="select id,mingc from pinzb ";
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId, new IDropDownModel(PinzSql));
		egu.getColumn("pinzb_id").setReturnId(true);
		
		ComboBox cb_jihkj=new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(cb_jihkj);
		cb_jihkj.setEditable(true);
		String JihkjSql="select id,mingc from jihkjb ";
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId, new IDropDownModel(JihkjSql));
		egu.getColumn("jihkjb_id").setReturnId(true);
		
		


		// ********************工具栏************************************************
		//与页面上的下拉框绑定
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");
		egu.addTbarText("月份:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");// 和自动刷新绑定
		comb2.setLazyRender(true);// 动态绑定
		comb2.setWidth(60);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");

		
	
		// 设定工具栏下拉框自动刷新
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});YUEF.on('select',function(){document.forms[0].submit();});");
		// /设置按钮
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		GridButton gb = new GridButton("计划卡编码", "function(){ "
				+ " var rec = gridDiv_sm.getSelected(); "
				+ " if(rec != null){var id = rec.get('ID');"
				+ " var Cobjid = document.getElementById('CHANGEID');"
				+ " Cobjid.value = id;"
				+ " document.getElementById('ChakButton').click();}}");
		egu.addTbarBtn(gb);
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
			visit.setList1(null);
			this.setNianfValue(null);
			this.getNianfModels();
			this.setYuefValue(null);
			this.getNianfModels();
		}
		getSelectData();
	}

	// 年份
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
	
	
	// 月份下拉框
	
	public IPropertySelectionModel getYuefModel() {
	    if (((Visit)this.getPage().getVisit()).getProSelectionModel4() == null) {
	        getYuefModels();
	    }
	    return ((Visit)this.getPage().getVisit()).getProSelectionModel4();
	}
	public void setYuefModel(IPropertySelectionModel _value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel4(_value);
    }
	
	public void getYuefModels() {
        List listYuef = new ArrayList();
        for (int i = 1; i < 13; i++) {
            listYuef.add(new IDropDownBean(i, String.valueOf(i)));
        }
        setYuefModel(new IDropDownModel(listYuef));
    }
	
	
	public IDropDownBean getYuefValue() {
	    if (((Visit)this.getPage().getVisit()).getDropDownBean4() == null) {
	        int _yuef = DateUtil.getMonth(new Date());
	        if (_yuef == 0) {
	            _yuef = 12;
	        } else {}
	        for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
	            Object obj = getYuefModel().getOption(i);
	            if (_yuef == ((IDropDownBean) obj).getId()) {
	            	setYuefValue((IDropDownBean) obj);
	                break;
	            }
	        }
	    }
	    return ((Visit)this.getPage().getVisit()).getDropDownBean4();
	}
	
	public void setYuefValue(IDropDownBean Value) {
		((Visit)this.getPage().getVisit()).setDropDownBean4(Value);
	}


}
