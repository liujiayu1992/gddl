package com.zhiren.pub.hetgl;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Hetgl extends BasePage implements PageValidateListener {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
    //页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}

	private boolean _RefurbishChick = false;
    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishChick = true;
    }
    private boolean _GuanlChick = false;
    public void GuanlButton(IRequestCycle cycle) {
    	_GuanlChick = true;
    }
    private void Refurbish() {
            //为  "刷新"  按钮添加处理程序
            getSelectData();
    } 

	public void submit(IRequestCycle cycle) {
		
		if(_RefurbishChick){
			_RefurbishChick = false;
			Refurbish();
		}
		if(_GuanlChick){
			_GuanlChick = false;
			GotoShezfa(cycle);
		}
		
//		cycle.activate("12321");
	}
	
	private void GotoShezfa(IRequestCycle cycle) {
		// TODO 自动生成方法存根
		((Visit) getPage().getVisit()).setString10(this.getParameters());
		((Visit) getPage().getVisit()).setString11(String.valueOf(this.getGuanllxValue().getId()));
		cycle.activate("Guanl");
	}
	
	private String Parameters;//记录项目ID

	public String getParameters() {
		
		return Parameters;
	}

	public void setParameters(String value) {
		
		Parameters = value;
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		//日期控件
//		String riqTiaoj=this.getRiqi();
//		if(riqTiaoj==null||riqTiaoj.equals("")){
//			riqTiaoj=DateUtil.FormatDate(new Date());	
//		}
		
		//关联类型条件
		String guanllxValue = this.getGuanllxValue().getValue();
		String dat = DateUtil.FormatDate(new Date());
		ResultSetList rsl = null;
		ExtGridUtil egu = null;
		if(guanllxValue.equals("煤款合同关联运费合同")){
			rsl = con
			.getResultSetList("select h.id,h.hetbh,g.mingc as gongysmc,h.qisrq,h.guoqrq from hetb h,gongysb g "
					+ " where gongysb_id = g.id "
					+ " and h.qisrq <= to_date('"+dat+"','yyyy-mm-dd')"
					+ " and h.guoqrq >= to_date('"+dat+"','yyyy-mm-dd')"
					+ " order by h.id");
			
			egu = new ExtGridUtil("gridDiv", rsl);
			egu.setTableName("hetgl");
			
			//设置显示列名称
			egu.getColumn("id").setHidden(true);
			egu.getColumn("hetbh").setHeader("合同编号");
			egu.getColumn("gongysmc").setHeader("供应商名称");
			egu.getColumn("qisrq").setHeader("起始日期");
			egu.getColumn("guoqrq").setHeader("截止日期");		
			 //设置列宽度
			 egu.getColumn("gongysmc").setWidth(200);

		}else{
			rsl = con
			.getResultSetList("select h.id,h.hetbh,y.mingc as chengydwmc,h.qisrq,h.guoqrq from hetys h,yunsdwb y " 
					+ " where h.yunsdwb_id = y.id"
					+ " and h.qisrq <= to_date('"+dat+"','yyyy-mm-dd')"
					+ " and h.guoqrq >= to_date('"+dat+"','yyyy-mm-dd')"
					+ " order by h.id");
			egu = new ExtGridUtil("gridDiv", rsl);
			egu.setTableName("hetgl");
			
			//设置显示列名称
			egu.getColumn("id").setHidden(true);
			egu.getColumn("hetbh").setHeader("合同编号");
			egu.getColumn("chengydwmc").setHeader("承运单位名称");
			egu.getColumn("qisrq").setHeader("起始日期");
			egu.getColumn("guoqrq").setHeader("截止日期");	
			 //设置列宽度
			 egu.getColumn("chengydwmc").setWidth(200);
		}
		
//		//设置当前grid是否可编辑
//		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//设置分页行数（缺省25行可不设）
		egu.addPaging(25);
		
//********************工具栏************************************************
		// 拒付类型
		egu.addTbarText("关联类型:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("GuanllxDropDown");
		comb1.setId("guanllx");
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(200);
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");
//		egu.getColumn("guanllx").setDefaultValue("煤款合同关联运费合同");

//		 /设置按钮
//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarItem("{"+new GridButton("刷新","function(){document.getElementById('RefurbishButton').click();}").getScript()+"}");
		
		String str2=
			"   var rec = gridDiv_sm.getSelected(); \n"
	        +"  if(rec!=null){\n"
	        +"  	gridDiv_history = rec.get('ID');\n"
	        +"  	document.getElementById('PARAMETERS').value=gridDiv_history; \n"
	        +"  }else{\n"
	        +"  	Ext.MessageBox.alert('提示信息','请选中一个项目!'); \n"
	        +"  	return;"
	        +"  }"
	        +" document.getElementById('GuanlButton').click(); \n";
        egu.addToolbarItem("{"+new GridButton("关联","function(){"+str2+"}").getScript()+"}");
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
			visit.setString10("");	//传合同id	
			visit.setString11("");	//传关联类型id
			
			if(((Visit) this.getPage().getVisit()).getString2()==null||
					!((Visit) this.getPage().getVisit()).getString2().equals("GUANL")) {
				
				this.setGuanllxValue(null);
				setGuanllxModel(null);
	        }
//			
		}
		getSelectData();
	}

	// 关联类型
	public boolean _guanllxchange = false;

	private IDropDownBean _GuanllxValue;

	public IDropDownBean getGuanllxValue() {
		if (_GuanllxValue == null) {
			_GuanllxValue = (IDropDownBean) getGuanllxModel().getOption(0);
		}
		return _GuanllxValue;
	}

	public void setGuanllxValue(IDropDownBean Value) {
		long id = -2;
		if (_GuanllxValue != null) {
			id = _GuanllxValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_guanllxchange = true;
			} else {
				_guanllxchange = false;
			}
		}
		_GuanllxValue = Value;
	}
	
	public void setGuanllxModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getGuanllxModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getGuanllxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getGuanllxModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "煤款合同关联运费合同"));
		list.add(new IDropDownBean(0, "运费合同关联煤款合同"));
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(list));

		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
}

