package com.zhiren.pub.shulzlzh;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Shulzlzh extends BasePage implements PageValidateListener {
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

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}
	
	
	private boolean _CheckChick = false;

	public void CheckButton(IRequestCycle cycle) {
		_CheckChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			
		}	
		if(_CheckChick)	{
			_CheckChick= false;
			create();
		}
			
//		getSelectData();
	}
private void create(){
	JDBCcon con = new JDBCcon();
	//工具栏的年份和月份下拉框
	long intyear;
	if (getNianfValue() == null) {
		intyear = DateUtil.getYear(new Date());
	} else {
		intyear = getNianfValue().getId();
	}
	long intMonth;
	if (getYuefValue() == null) {
		intMonth = DateUtil.getMonth(new Date());
	} else {
		intMonth = getYuefValue().getId();
	}
	//当月份是1的时候显示01,
	String StrMonth="";
	if(intMonth<10){
		
		StrMonth="0"+intMonth;
	}else{
		StrMonth=""+intMonth;
	}
	//delete from shulzlzhb where riq='200701'
	
	con.getDelete("delete from shulzlzhb where to_char(riq,'yyyy-mm')='"+ intyear + "-" + StrMonth+"'");
	
	String  insert ="insert into shulzlzhb(id,riq,gongysb_id,yunsfsb_id,pinzb_id,jihkjb_id,biaoz,jingz,yingd,kuid,yuns,koud,farl,huiff,liuf,quansf,shuif,huif)"
		    + "(select f.id as id,f.daohrq as riq,g.id as gongysb_id,y.id as yunsfsb_id,p.id as pinzb_id,\n" 
			+"j.id as jihkjb_id,f.biaoz as biaoz, f.jingz as jingz,f.yingd as yingd,\n" 
			+"f.yingk as kuid, f.yuns as yuns, f.koud as koud,\n"
			+"z.qnet_ar as farl,z.vad as huiff,z.std as liuf,z.mt as quansf,z.mad as shuif,z.aar as huif \n"
			+"from gongysb g,yunsfsb y,jihkjb j,fahb f,zhilb z,pinzb p \n"				
			+ "where f.gongysb_id=g.id(+) and f.jihkjb_id = j.id(+) and f.yunsfsb_id= y.id(+) \n" 
			+"and f.zhilb_id=z.id(+)  and f.pinzb_id=p.id and to_char(f.daohrq,'yyyy-mm') ='" 
			+ intyear + "-" + StrMonth+"')";
	
     con.getInsert(insert);

		con.Close();
}
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		//工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		//当月份小于10的时候，在其前面加“0”
		String StrMonth="";
		if(intMonth<10){
			
			StrMonth="0"+intMonth;
		}else{
			StrMonth=""+intMonth;
		}
		String chaxun;
		
		chaxun ="select distinct s.id as id,s.riq as riq,g.mingc as gongysb_id,y.mingc as yunsfsb_id, \n" 
			+"p.mingc as pinzb_id,j.mingc as jihkjb_id,s.biaoz,s.jingz,s.yingd,s.kuid,s.yuns,s.koud,s.farl,\n" 
			+"s.huiff,s.liuf,s.quansf,s.shuif,s.huif \n" 
			+"from shulzlzhb s, gongysb g,yunsfsb y,jihkjb j,fahb f,zhilb z,pinzb p \n" 
			+"where s.gongysb_id=g.id(+) and s.yunsfsb_id=y.id(+) and s.pinzb_id=p.id(+) and s.jihkjb_id=j.id(+) \n" 
			+"and to_char(riq,'yyyy-mm') ='"+ intyear + "-" + StrMonth+"' order by id";
	//System.out.println(chaxun);	
	ResultSetList rsl = con.getResultSetList(chaxun);
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("shulzlzhb");
   	
	egu.getColumn("riq").setHeader("日期");
	egu.getColumn("gongysb_id").setHeader("供应商");
	egu.getColumn("yunsfsb_id").setHeader("运输方式");
	egu.getColumn("pinzb_id").setHeader("品种");
	egu.getColumn("jihkjb_id").setHeader("计划口径");
	egu.getColumn("biaoz").setHeader("票重");
	egu.getColumn("biaoz").setDefaultValue("0");
	egu.getColumn("jingz").setHeader("净重");
	egu.getColumn("jingz").setDefaultValue("0");
	egu.getColumn("yingd").setHeader("赢吨");
	egu.getColumn("yingd").setDefaultValue("0");
	egu.getColumn("kuid").setHeader("亏吨");
	egu.getColumn("kuid").setDefaultValue("0");
	egu.getColumn("yuns").setHeader("运损");
	egu.getColumn("yuns").setDefaultValue("0");
	egu.getColumn("koud").setHeader("扣吨");
	egu.getColumn("koud").setDefaultValue("0");
	egu.getColumn("farl").setHeader("发热量");
	egu.getColumn("farl").setDefaultValue("0");
	egu.getColumn("huiff").setHeader("挥发分");
	egu.getColumn("huiff").setDefaultValue("0");
	egu.getColumn("liuf").setHeader("硫分");
	egu.getColumn("liuf").setDefaultValue("0");
	egu.getColumn("quansf").setHeader("全水分");
	egu.getColumn("quansf").setDefaultValue("0");
	egu.getColumn("shuif").setHeader("水分");
	egu.getColumn("shuif").setDefaultValue("0");
	egu.getColumn("huif").setHeader("灰分");
	egu.getColumn("huif").setDefaultValue("0");
	egu.getColumn("riq").setHidden(true);
	egu.getColumn("riq").setEditor(null);
	
	
	//设定列初始宽度
	egu.getColumn("riq").setWidth(80);
	egu.getColumn("gongysb_id").setWidth(100);
	egu.getColumn("yunsfsb_id").setWidth(80);
	egu.getColumn("pinzb_id").setWidth(60);
	egu.getColumn("jihkjb_id").setWidth(60);
	egu.getColumn("biaoz").setWidth(60);
	egu.getColumn("jingz").setWidth(60);
	egu.getColumn("yingd").setWidth(60);
	egu.getColumn("kuid").setWidth(60);
	egu.getColumn("yuns").setWidth(60);
	egu.getColumn("koud").setWidth(60);
	egu.getColumn("farl").setWidth(60);
	egu.getColumn("huiff").setWidth(60);
	egu.getColumn("liuf").setWidth(60);
	egu.getColumn("quansf").setWidth(60);
	egu.getColumn("shuif").setWidth(60);
	egu.getColumn("huif").setWidth(60);
	
	
	
	egu.setGridType(ExtGridUtil.Gridstyle_Edit);//设定grid可以编辑
	egu.addPaging(100);//设置分页
	egu.setWidth(1000);//设置页面的宽度,当超过这个宽度时显示滚动条
	
	
	
	//*****************************************设置默认值****************************
	
	//设置日期的默认值,
	egu.getColumn("riq").setDefaultValue(intyear+"-"+StrMonth+"-01");
	
	//*************************下拉框*****************************************88
	//设置供应商的下拉框
	egu.getColumn("gongysb_id").setEditor(new ComboBox());
	
	String GongysSql="select id,mingc from gongysb order by id";
	egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(GongysSql));
	//设置运输方式下拉框
	ComboBox yunsfs=new ComboBox();
	egu.getColumn("yunsfsb_id").setEditor(yunsfs);
	yunsfs.setEditable(true);
	String yunsfsSql="select id ,mingc from yunsfsb order by id";;
	egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId, new IDropDownModel(yunsfsSql));
	//设置计划口径下拉框
	ComboBox jihkj=new ComboBox();
	egu.getColumn("jihkjb_id").setEditor(jihkj);
	jihkj.setEditable(true);
	String jihkjSql="select id,mingc from jihkjb order by id";
	egu.getColumn("jihkjb_id").setComboEditor(egu.gridId, new IDropDownModel(jihkjSql));
	
//	设置品种下拉框
	ComboBox pinz=new ComboBox();
	egu.getColumn("pinzb_id").setEditor(pinz);
	pinz.setEditable(true);
	String pinzSql="select id,mingc from pinzb order by id";
	egu.getColumn("pinzb_id").setComboEditor(egu.gridId, new IDropDownModel(pinzSql));

	
	//********************工具栏************************************************
		egu.addTbarText("年份:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");//和自动刷新绑定
		comb1.setLazyRender(true);//动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("-");// 设置分隔符
		
		egu.addTbarText("月份:");
		ComboBox comb2=new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");//和自动刷新绑定
		comb2.setLazyRender(true);//动态绑定
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");//设置分隔符
		
		
		//设定工具栏下拉框自动刷新
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});YUEF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// 设置分隔符
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addToolbarItem("{"+new GridButton("生成","function(){document.all.item('CheckButton').click()}").getScript()+"}");		
		
		String str=" var url = 'http://'+document.location.host+document.location.pathname;"+
        "var end = url.indexOf(';');"+
                 "url = url.substring(0,end);"+
   	    "url = url + '?service=page/Shulzlzhreport&lx='+NIANF.getValue()+'&lx='+YUEF.getValue();" +
   	    " window.open(url,'newWin');";
	egu.addToolbarItem("{"+new GridButton("打印","function (){"+str+"}").getScript()+"}");
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
			this.getYuefModels();
			
			
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

	// 月份
	public boolean Changeyuef = false;

	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue!= null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;
		
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

}
