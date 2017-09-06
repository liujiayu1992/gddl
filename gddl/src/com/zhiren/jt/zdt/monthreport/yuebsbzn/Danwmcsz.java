package com.zhiren.jt.zdt.monthreport.yuebsbzn;

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

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/* 
 * 时间：2009-08-3
 * 作者： sy
 * 修改内容：1、增加判断若cpi01表时阜新的编码是212118，若调燃报表时阜新编码是212105
 * 		   
 */
public class Danwmcsz extends BasePage implements PageValidateListener {
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

		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		int flag=0;
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		
		StringBuffer sql = new StringBuffer();
		sql.append("begin \n");
		while (rsl.next()) {
			
			long id = 0;
						
			String ID_1=rsl.getString("ID");

			long leix=0;
			if(rsl.getString("leix").equals("否")){
				leix=0;
			}else if(rsl.getString("leix").equals("是")){
				leix=1;
			}
								
			if ("0".equals(ID_1)||"".equals(ID_1)) {
				id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
				
				//向上报中能口径定义表表里插入数据
				sql.append("insert into koujdyb("
								+ "id,xuh,diancxxb_id,koujmcb_id,leix,beiz)values("
								+ id + ","
								+ rsl.getLong("xuh")+ ","
								+ rsl.getLong("diancxxb_id") + ","
								+ getLeixSelectValue().getId() + ","
								+ leix + ",'');\n");
			
			} else {
				
				//修改上报中能口径定义表表数据
				 sql.append("update koujdyb set leix=" + leix 
				 +" where id=" + rsl.getLong("id")+";\n");
			}
			
		}
		sql.append("end;");
		flag=con.getUpdate(sql.toString());
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail);
			setMsg(ErrorMessage.UpdateDatabaseFail);
			return;
		}
		
		if(flag!=-1){
			setMsg(ErrorMessage.SaveSuccessMessage);
		}
		con.Close();
		setMsg("保存成功!");
	}
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _ReturnChick = false;
    public void ReturnButton(IRequestCycle cycle) {
        _ReturnChick = true;
    }
	public void submit(IRequestCycle cycle) {
		if (_Refreshclick) {
			_Refreshclick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_ReturnChick) {
    		_ReturnChick = false;
    		cycle.activate("Yuebsbzn");
        }
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long baoblx=0;
		String dcbianm="";
		baoblx = getLeixSelectValue().getId();
		if(baoblx==1){
			dcbianm="dc.bianm ";
		}else 
		    dcbianm="decode(dc.bianm,'212105','212118',dc.bianm) as bianm ";
		
		System.out.println("*********************:"+baoblx);
		String aaa="select nvl(dy.id,0) as id,dc.xuh as xuh,dc.id as diancxxb_id,"+dcbianm+",dc.mingc as mingc,decode(nvl(dy.leix,0), 0, '否', '是') as leix \n" +
		"from diancxxb dc,(select kj.id ,kj.diancxxb_id,kj.leix from koujdyb kj\n" +
		" 	 where  kj.koujmcb_id="+baoblx+") dy \n" +
		"where dc.jib=3 and dy.diancxxb_id(+)=dc.id \n" +
		" order by dc.xuh";
		
		
		ResultSetList rsl = con.getResultSetList(aaa);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("koujdyb");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("xuh").setHidden(true);
		egu.getColumn("diancxxb_id").setHeader("diancxxb_id");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("bianm").setHeader("电厂编码");
		egu.getColumn("bianm").setWidth(90);
		egu.getColumn("bianm").setUpdate(false);
		egu.getColumn("mingc").setHeader("电厂名称");
		egu.getColumn("mingc").setWidth(150);
		egu.getColumn("leix").setHeader("上报中能");
		egu.getColumn("leix").setWidth(100);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "否"));
		l.add(new IDropDownBean(1, "是"));
		egu.getColumn("leix").setEditor(new ComboBox());
		egu.getColumn("leix").setComboEditor(egu.gridId,
				new IDropDownModel(l));
		egu.getColumn("leix").setReturnId(true);
		egu.getColumn("leix").setDefaultValue("否");

		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		egu.addToolbarItem("{"+new GridButton("返回","function(){"+
				
				"document.getElementById('ReturnButton').click();" +
				"}").getScript()+"}");
		
		
		// 供货单位
		egu.addTbarText("报表类型:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("Baoblx");
		comb1.setId("Baoblx");//和自动刷新绑定
		comb1.setLazyRender(true);//动态绑定
		comb1.setWidth(150);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("-");// 设置分隔符
		//设定工具栏下拉框自动刷新
		egu.addOtherScript("Baoblx.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// 设置分隔符
		
		
//		刷新按钮
//		StringBuffer rsb = new StringBuffer();
//		rsb.append("function (){")
//		.append("document.getElementById('RefreshButton').click();}");
//		GridButton gbr = new GridButton("刷新",rsb.toString());
//		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
//		egu.addTbarBtn(gbr);
		
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
			this.setLeixSelectValue(null);
			this.getLeixSelectModels();
		}
		getSelectData();
	}
	
	
// 报表类型下拉框
	public boolean _LeixSelectChange = false;

	private IDropDownBean _LeixSelectValue;

	public IDropDownBean getLeixSelectValue() {
		if (_LeixSelectValue == null) {
			_LeixSelectValue = (IDropDownBean) getLeixSelectModels().getOption(0);
		}
		return _LeixSelectValue;
	}

	public void setLeixSelectValue(IDropDownBean Value) {
		long id = -2;
		if (_LeixSelectValue != null) {
			id = _LeixSelectValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_LeixSelectChange = true;
			} else {
				_LeixSelectChange = false;
			}
		}
		_LeixSelectValue = Value;
	}

	private IPropertySelectionModel _LeixSelectModel;

	public void setLeixSelectModel(IPropertySelectionModel value) {
		_LeixSelectModel = value;
	}

	public IPropertySelectionModel getLeixSelectModel() {
		if (_LeixSelectModel == null) {
			getLeixSelectModels();
		}
		return _LeixSelectModel;
	}

	public IPropertySelectionModel getLeixSelectModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select id,koujmc as mingc from koujmcb";
			_LeixSelectModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _LeixSelectModel;
	}

}
