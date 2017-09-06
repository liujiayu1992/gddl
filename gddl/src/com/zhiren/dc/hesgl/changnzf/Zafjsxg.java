package com.zhiren.dc.hesgl.changnzf;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author yinjm
 * 2009-06-18
 * 类名：Zafjsxg(杂费结算修改)
 */

public class Zafjsxg extends BasePage implements PageValidateListener {
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
	
//	绑定起始日期
	private String briq = DateUtil.FormatDate(new Date());

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		if (!this.briq.equals(briq)) {
			this.setJiesdbhModel(null);
			this.setJiesdbhValue(null);
		}
		this.briq = briq;
	}
	
//	绑定结束日期
	private String eriq = DateUtil.FormatDate(new Date());

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		if (!this.eriq.equals(eriq)) {
			this.setJiesdbhModel(null);
			this.setJiesdbhValue(null);
		}
		this.eriq = eriq;
	}
	
//	收款单位下拉框
	public IDropDownBean getSoukdwValue() {
		if (((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getSoukdwModel().getOptionCount()>0) {
				setSoukdwValue((IDropDownBean)getSoukdwModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	
	public void setSoukdwValue(IDropDownBean gongysValue) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(gongysValue);
	}
	
	public IPropertySelectionModel getSoukdwModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setSoukdwModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	
	public void setSoukdwModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
//	获取收款单位
	public void setSoukdwModels() {
		String str = "select 0 id, '全部' quamc from dual union select id, quanc " +
				"from shoukdw order by id";
		setSoukdwModel(new IDropDownModel(str));
	}
	
//	结算单编号下拉框
	public IDropDownBean getJiesdbhValue() {
		if (((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getJiesdbhModel().getOptionCount()>0) {
				setJiesdbhValue((IDropDownBean)getJiesdbhModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	
	public void setJiesdbhValue(IDropDownBean gongysValue) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(gongysValue);
	}
	
	public IPropertySelectionModel getJiesdbhModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			setJiesdbhModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	
	public void setJiesdbhModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
//	获取结算单编号
	public void setJiesdbhModels() {
		
		String briq = getBRiq();
		String eriq = getERiq();
		
		String id = getSoukdwValue().getId()+"";
		if (id.equals("0")) {
			id = "";
		} else {
			id = " and shoukdw_id = " + id;
		}
		
		StringBuffer sbstr = new StringBuffer();
		sbstr.append("select 0 id, '请选择' bianm from dual union select id, bianm from zafjsb " +
				"where riq between to_date('"+ briq +"', 'yyyy-MM-dd') and to_date('"+ eriq +"', 'yyyy-MM-dd')");
		sbstr.append(id).append(" order by id");
		setJiesdbhModel(new IDropDownModel(sbstr.toString()));
	}
	
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	private boolean _SaveChick = false;
	
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			if (save()) {
				getSelectData();
			}
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			getSelectData();
		}
	}
	
	public void getSelectData() {
		Visit visit = (Visit)this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		String str = "select * from zafjsb where bianm = '" + getJiesdbhValue().getValue() + "'";
		ResultSetList bmrsl = con.getResultSetList(str);
		
		if (bmrsl.next()) {
			visit.setString1(bmrsl.getString("id"));
			String bianm = bmrsl.getString("bianm");
			visit.setString2(bianm);
			String gongysmc = bmrsl.getString("gongysmc");
			String jiessl = bmrsl.getString("jiessl");
			String danj = bmrsl.getString("danj");
			String buhszf = bmrsl.getString("buhszf");
			String zafsk = bmrsl.getString("zafsk");
			String hej = bmrsl.getString("hej");
			String shoukdw_id = bmrsl.getString("shoukdw_id");
			String qiszyfw = bmrsl.getDateString("qiszyfw");
			String jiezzyfw = bmrsl.getDateString("jiezzyfw");
			String beiz = bmrsl.getString("beiz");
			
			String idstr = "select quanc from shoukdw where id = " +  shoukdw_id;
			ResultSetList rsl = con.getResultSetList(idstr);
			String quanc = "";
			while(rsl.next()) {
				quanc = rsl.getString("quanc");
			}
			
			sbsql.append("select xiangm, zhi from (" +
				"select '结算编号' as xiangm, '"+ bianm +"' as zhi, 1 as num from dual union " +
				"select '供应商名称' as xiangm, '"+ gongysmc +"' as zhi , 2 as num from dual union " +
				"select '结算数量' as xiangm, '"+ jiessl +"' as zhi, 3 as num from dual union " +
				"select '单价' as xiangm, '"+ danj +"' as zhi, 4 as num from dual union " +
				"select '不含税杂费' as xiangm, '"+ buhszf +"' as zhi, 5 as num from dual union " +
				"select '杂费税款' as xiangm, '"+ zafsk +"' as zhi, 6 as num from dual union " +
				"select '费用合计' as xiangm, '"+ hej +"' as zhi, 7 as num from dual union " +
				"select '收款单位' as xiangm, '"+ quanc +"' as zhi, 8 as num from dual union " +
				"select '起始作用范围' as xiangm, '"+ qiszyfw +"' as zhi, 9 as num from dual union " +
				"select '截止作用范围' as xiangm, '"+ jiezzyfw +"' as zhi, 10 as num from dual union " +
				"select '备注' as xiangm, '"+ beiz +"' as zhi, 11 as num from dual ) order by num");

		} else {
			sbsql.append("select '' as xiangm, '' as zhi from zafjsb where id = -1");
		}
		
		ResultSetList rsl = con.getResultSetList(sbsql.toString());
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.setTableName("dual");
		egu.getColumn("xiangm").setHeader("项目名称");
		egu.getColumn("zhi").setHeader("值");
		egu.getColumn("zhi").setWidth(300);
		
		egu.addTbarText("选择日期：");
		DateField bdf = new DateField();
		bdf.setValue(getBRiq());
		bdf.Binding("BRiq", "Form0");
		egu.addToolbarItem(bdf.getScript());
		
		egu.addTbarText(" 至 ");
		DateField edf = new DateField();
		edf.setValue(getERiq());
		edf.Binding("ERiq", "Form0");
		egu.addToolbarItem(edf.getScript());
		egu.addTbarText("-");
		
		egu.addTbarText("收款单位：");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(150);
		comb1.setTransform("Soukdw");
		comb1.setId("Soukdw");
		comb1.setLazyRender(true);
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
		this.setJiesdbhModel(null);
		this.setJiesdbhValue(null);
		egu.addOtherScript("Soukdw.on('select',function(){document.forms[0].submit();});");
		
		egu.addTbarText("-");
		
		egu.addTbarText("结算单编号：");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(100);
		comb2.setTransform("Jiesdbh");
		comb2.setId("Jiesdbh");
		comb2.setLazyRender(true);
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());
		egu.addOtherScript("Jiesdbh.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		egu.addOtherScript("\n gridDiv_grid.on('beforeedit',function(e){ 							\n" +
				"if(e.field=='ZHI') {																	\n" +
				"	if(e.row==4 || e.row==5) {															\n" +
				"		var temp4 = gridDiv_ds.getAt(4).get('ZHI');										\n" +
				"		var temp5 = gridDiv_ds.getAt(5).get('ZHI');										\n" +
				"		gridDiv_grid.on('afteredit',function(a){										\n" +
				"			if ((/^\\d+(\\.\\d+)?$/.test(gridDiv_ds.getAt(4).get('ZHI'))) " +
				"				&& (/^\\d+(\\.\\d+)?$/.test(gridDiv_ds.getAt(5).get('ZHI')))) {			\n" +
				"				temp4 = gridDiv_ds.getAt(4).get('ZHI');									\n" +
				"				temp5 = gridDiv_ds.getAt(5).get('ZHI');									\n" +
				"				var row4 = eval(gridDiv_ds.getAt(4).get('ZHI'));						\n" +
				"				var row5 = eval(gridDiv_ds.getAt(5).get('ZHI'));						\n" +
				"				var result = row4 + row5;												\n" +
				"				gridDiv_ds.getAt(gridDiv_ds.getCount()-5).set('ZHI',result.toString());	\n" +
				"			} else {																	\n" +
				"				Ext.MessageBox.alert('提示信息','请您输入数字！');						\n" +
				"				gridDiv_ds.getAt(gridDiv_ds.getCount()-6).set('ZHI',temp5.toString());	\n" +
				"				gridDiv_ds.getAt(gridDiv_ds.getCount()-7).set('ZHI',temp4.toString());	\n" +
				"			}																			\n" +
				"		}); 																			\n" +
				"	} else if(e.row==0) {																\n" +
				"		e.cancel=false;																	\n" +
				"	} else if(e.row==10) {																\n" +
				"		e.cancel=false;																	\n" +
				"	} else {																			\n" +
				"		e.cancel=true;																	\n" +
				"	}																					\n" +
				"} else {																				\n" +
				"	e.cancel=true;																		\n" +
				"}																						\n" +
				"});																					\n");
		
		String rsb = "function(){document.getElementById('RefreshButton').click();}";
		GridButton gbtn = new GridButton("刷新", rsb);
		gbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbtn);
		
		egu.addToolbarButton("保存", GridButton.ButtonType_SaveAll, "SaveButton");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(0);
		setExtGrid(egu);
		
		rsl.close();
		con.Close();
	}
	
	public boolean save() {
		Visit visit = (Visit)this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		
		String bianm = "";
		String buhszf = "";
		String zafsk = "";
		String feiyhj = "";
		String beiz = "";
		while(mdrsl.next()) {
			if (mdrsl.getString("XIANGM").equals("结算编号")) {
				bianm = mdrsl.getString("ZHI");
			} else if (mdrsl.getString("XIANGM").equals("不含税杂费")) {
				buhszf = mdrsl.getString("ZHI");
			} else if (mdrsl.getString("XIANGM").equals("杂费税款")) {
				zafsk = mdrsl.getString("ZHI");
			} else if (mdrsl.getString("XIANGM").equals("费用合计")) {
				feiyhj = mdrsl.getString("ZHI");
			} else if (mdrsl.getString("XIANGM").equals("备注")) {
				beiz = mdrsl.getString("ZHI");
			}
		}
		
		String sltsql = "select bianm from zafjsb where bianm = '"+ bianm +"'";
		ResultSetList sltrsl = con.getResultSetList(sltsql);
		StringBuffer sbsql = new StringBuffer();
		if (sltrsl.next() && !bianm.equals(visit.getString2())) {
			this.setMsg("结算编号已经存在，请重新输入");
			getJiesdbhValue().setValue(visit.getString2());
			return false;
		} else {
			sbsql.append("update zafjsb set bianm = '").append(bianm).append("', buhszf = ")
			.append(buhszf).append(", zafsk = ").append(zafsk).append(", hej = ")
			.append(feiyhj).append(", beiz = '").append(beiz).append("' where id = ")
			.append(visit.getString1());
			
			con.getUpdate(sbsql.toString());
		}
		
		mdrsl.close();
		sltrsl.close();
		con.Close();
		return true;
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
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
		}
		getSelectData();
	}
}
