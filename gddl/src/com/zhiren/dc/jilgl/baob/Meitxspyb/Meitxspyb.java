package com.zhiren.dc.jilgl.baob.Meitxspyb;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author wangzongbing
 * 类名：销售票月报维护页面
 */

public class Meitxspyb extends BasePage implements PageValidateListener {

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		msg = "";
	}
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	
	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	
//	"生成"按钮
	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
		}
		if (_CreateClick) {
			_CreateClick = false;
			createData();
		}
	}
	
	public void createData() {
		
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sbsql = new StringBuffer("begin\n");
		String riq=this.getNianfValue().getValue()+"-"+this.getYuefValue().getValue()+"-01";
		String sql = 

			"select sum(shul) as benygml,get_xiaosp_leij(to_date('"+riq+"','yyyy-mm-dd')) as leijgml,\n" +
			"get_XiaoSP_kuc(to_date('"+riq+"','yyyy-mm-dd')) as yueckc,'"+visit.getRenymc()+"' as tianbr\n" + 
			"from meitxspjb m\n" + 
			"where m.riq=to_date('"+riq+"','yyyy-mm-dd')";
		//删除
		sbsql.append("delete meitxspybb m where m.riq=to_date('"+riq+"','yyyy-mm-dd');\n");
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.getRows() > 0) {
			while(rsl.next()) {
				sbsql.append("insert into Meitxspybb(id, riq, gouml, leijgml, yueckc,tianbr) values(getnewid(")
				.append(visit.getDiancxxb_id()).append("), ")
				.append("to_date('").append(riq).append("', 'yyyy-mm-dd'), ")
				.append(rsl.getDouble("benygml")).append(", ")
				.append(rsl.getDouble("leijgml")).append(", ")
				.append(rsl.getDouble("yueckc")).append(", '")
				.append(rsl.getString("tianbr")).append("' ")
				.append(");\n");
			}
			sbsql.append("end;\n");
			con.getInsert(sbsql.toString());
		} else {
			setMsg("没有销售票信息，无法生成！");
		}
		rsl.close();
		con.Close();
	}
	
	public void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String riq=this.getNianfValue().getValue()+"-"+this.getYuefValue().getValue()+"-01";
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sbsql.append("delete from meitxspybb where id = ").append(delrsl.getString("id")).append(";\n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			if("0".equals(mdrsl.getString("id"))) {
				sbsql.append("insert into meitxspybb(id, riq, fadl, gouml, leijgml, haoml, yueckc, yuemkc, meih,tianbr,fuzr,beiz) values(getnewid(")
				.append(visit.getDiancxxb_id()).append("), ")
				.append("to_date('").append(""+riq+"").append("', 'yyyy-mm-dd'), ")
				.append(mdrsl.getDouble("fadl")).append(", ")
				.append(mdrsl.getDouble("gouml")).append(", ")
				.append(mdrsl.getDouble("leijgml")).append(", ")
				.append(mdrsl.getDouble("haoml")).append(", ")
				.append(mdrsl.getDouble("yueckc")).append(", ")
				.append(mdrsl.getDouble("yuemkc")).append(", ")
				.append(mdrsl.getDouble("meih")).append(", '")
				.append(mdrsl.getString("tianbr")).append("', '")
				.append(mdrsl.getString("fuzr")).append("', '")
				.append(mdrsl.getString("beiz")).append("');\n");
			} else {
				sbsql.append("update meitxspybb set ")
				.append("fadl = ").append(mdrsl.getDouble("fadl")).append(", ")
				.append("gouml = ").append(mdrsl.getDouble("gouml")).append(", ")
				.append("leijgml = ").append(mdrsl.getDouble("leijgml")).append(", ")
				.append("haoml = ").append(mdrsl.getDouble("haoml")).append(", ")
				.append("yueckc = ").append(mdrsl.getDouble("yueckc")).append(", ")
				.append("yuemkc = ").append(mdrsl.getDouble("yuemkc")).append(", ")
				.append("meih = ").append(mdrsl.getDouble("meih")).append(", ")
				.append("tianbr = '").append(mdrsl.getString("tianbr")).append("', ")
				.append("fuzr = '").append(mdrsl.getString("fuzr")).append("', ")
				.append("beiz = '").append(mdrsl.getString("beiz")).append("' ")
				.append(" where id = ").append(mdrsl.getString("id")).append(";\n");
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
	}
	
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		String riq=this.getNianfValue().getValue()+"-"+this.getYuefValue().getValue()+"-01";
		JDBCcon con = new JDBCcon();
		String sql = 

			"select m.id,m.fadl,m.gouml,m.leijgml,m.haoml,m.yueckc,m.yuemkc,\n" +
			"m.meih,m.tianbr,m.fuzr,m.beiz\n" + 
			"from meitxspybb m\n" + 
			"where m.riq=to_date('"+riq+"','yyyy-mm-dd')";

		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("fadl").setCenterHeader("本月产品产量<br>(亿度)", 100);
		
		egu.getColumn("gouml").setCenterHeader("本月购煤量<br>(吨)",80);
		
		egu.getColumn("leijgml").setCenterHeader("累计购煤量<br>(吨)",90);
		egu.getColumn("haoml").setCenterHeader("耗煤量<br>(吨)");
		egu.getColumn("yueckc").setCenterHeader("月初库存<br>(吨)",70);
		egu.getColumn("yuemkc").setCenterHeader("月末库存<br>(吨)",70);
		egu.getColumn("meih").setCenterHeader("产品转化系数<br>(g/Kwh)",80);
		egu.getColumn("tianbr").setCenterHeader("填报人",70);
		egu.getColumn("tianbr").setDefaultValue(visit.getRenymc());
		egu.getColumn("fuzr").setCenterHeader("负责人",70);
		egu.getColumn("beiz").setCenterHeader("备注",150);
		
		
		
		
		
		
		egu.addTbarText("年份:");
		ComboBox nf_comb = new ComboBox();
		nf_comb.setWidth(60);
		nf_comb.setListWidth(60);
		nf_comb.setTransform("Nianf");
		nf_comb.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		nf_comb.setLazyRender(true);
		egu.addToolbarItem(nf_comb.getScript());
		egu.addTbarText("-");
		
		egu.addTbarText("月份:");
		ComboBox yf_comb = new ComboBox();
		yf_comb.setWidth(60);
		yf_comb.setListWidth(60);
		yf_comb.setTransform("Yuef");
		yf_comb.setListeners("select:function(combo,newValue,oldValue){document.forms[0].submit();}");
		yf_comb.setLazyRender(true);
		egu.addToolbarItem(yf_comb.getScript());
		egu.addTbarText("-");
		
		
		
		GridButton gbt = new GridButton("刷新", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("-");
		
		GridButton copyButton = new GridButton("生成", getButtonHandler(con, "CreateButton"), SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(copyButton);
		
		
		egu.addOtherScript(" gridDiv_grid.on('afteredit',function(e){ " +
				"if(e.field=='GOUML'|| e.field=='HAOML'  || e.field=='YUECKC'){ " +
				"   var jingz_va=eval(e.record.get('YUECKC')||0)+eval(e.record.get('GOUML')||0)-eval(e.record.get('HAOML')||0);" +
				"   e.record.set('YUEMKC',Math.round(jingz_va,0)); " +
				" }  " +
				"if(e.field=='FADL'|| e.field=='HAOML'){ " +
				"   var fadl_va=eval(e.record.get('HAOML')||0)*1000*1000/eval(e.record.get('FADL')||0)/100000000;" +
				"   e.record.set('MEIH',Math.round(fadl_va,0)); " +
				" }  " +
				
				" } );");

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(25);
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}
	
	/**
	 * 返回"生成"按钮的handler。当点击"生成"按钮时判断当前日期是否有数据，
	 * 如果有数据那么给出提示是否要覆盖原有数据，如果没有那么不给提示信息，
	 * 直接进行"生成"操作。
	 * @return
	 */
	public String getButtonHandler(JDBCcon con, String buttonName) {
		String riq=this.getNianfValue().getValue()+"-"+this.getYuefValue().getValue()+"-01";
		String handler = 
			"function (){\n" +
			"    document.getElementById('"+ buttonName +"').click();\n" + 
			"}";
		
		String sql = "select id from caiyhycstjb where riq = to_date('"+ riq +"', 'yyyy-mm-dd')";
		ResultSetList rsl = con.getResultSetList(sql);
		
		if (rsl.next()) {
			handler = 
				"function (){\n" +
				"    Ext.MessageBox.confirm('提示信息','新数据将会覆盖原有数据，是否继续？',\n" + 
				"        function(btn){\n" + 
				"            if(btn == 'yes'){\n" + 
				"                document.getElementById('"+ buttonName +"').click();\n" + 
				"                Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...'," +
				"                width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" + 
				"            };\n" + 
				"        }\n" + 
				"    );\n" + 
				"}";
		}
		rsl.close();
		return handler;
	}
	
	/**
	 * 如果在页面上取到的值为Null或是空串，那么向数据库保存字段的默认值
	 * @param value
	 * @return
	 */
	public String getSqlValue(String value) {
		return value == null || "".equals(value) ? "default" : value;
	}
	
	/**
	 * 获取传入日期的前一天
	 * @param date
	 * @return
	 */
	public static String getYesterday(String date) {
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		if(date == null){
			date = DateUtil.FormatDate(new Date());
		}
		String[] strdate = date.split("-");
		Calendar dateTime = Calendar.getInstance();
		dateTime.set(Integer.parseInt(strdate[0]), Integer.parseInt(strdate[1])-1, Integer.parseInt(strdate[2])-1);
		Date d = dateTime.getTime();
		return dft.format(d);
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	public String getGridScript() {
		return getExtGrid().getGridScript();
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());

			visit.setProSelectionModel2(null); // 年份下拉框
			visit.setDropDownBean2(null);
			visit.setProSelectionModel3(null); // 月份下拉框
			visit.setDropDownBean3(null);
		}
		getSelectData();
	}
	

//	年份下拉框_开始
	public IDropDownBean getNianfValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getNianfModel().getOptionCount() > 0) {
				for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
					if (DateUtil.getYear(new Date()) == ((IDropDownBean)getNianfModel().getOption(i)).getId()) {
						setNianfValue((IDropDownBean)getNianfModel().getOption(i));
						break;
					}
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}

	public void setNianfValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getNianfModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			getNianfModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	
	public void setNianfModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void getNianfModels() {
		String sql = "select nf.yvalue, nf.ylabel from nianfb nf";
		setNianfModel(new IDropDownModel(sql));
	}
//	年份下拉框_结束
	
//	月份下拉框_开始
	public IDropDownBean getYuefValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getYuefModel().getOptionCount() > 0) {
				for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
					if (DateUtil.getMonth(new Date()) == ((IDropDownBean)getYuefModel().getOption(i)).getId()) {
						setYuefValue((IDropDownBean)getYuefModel().getOption(i));
						break;
					}
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	
	public void setYuefValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getYuefModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			getYuefModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	
	public void setYuefModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
	public void getYuefModels() {
		String sql = "select mvalue, mlabel from yuefb";
		setYuefModel(new IDropDownModel(sql));
	}
//	月份下拉框_结束

}