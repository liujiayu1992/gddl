package com.zhiren.dc.hesgl.changnzf;
/*
 * 上一页应存入下面两个值
 * cycle.getRequestContext().getSession().setAttribute("bianm","itemsort表中的bianm");
 * 对上一页转来的值做判断，如果为空，可以对各项目进行操作
 * 如果有值，只能对该项目做操作
 * */
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
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
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zafxmwh extends BasePage {
	//是否显示工具栏的下拉框
	private boolean bianms = false;
	//  上页传来的值bianm
	private String bianm = null;
	public String getBianm(){
		return this.bianm;
	}
	public void setBianm(String bianm){
		this.bianm=bianm;
	}
	private String ziym="";
	
//	 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	private String riq;

	public void setRiq(String value) {
		riq = value;
	}

	public String getRiq() {
		if ("".equals(riq) || riq == null) {
			ResultSetList rs = new JDBCcon()
					.getResultSetList("select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') time from dual");
			rs.next();
			riq = rs.getString("time");
		}
		return riq;
	}

	public void setOriRiq(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}

	public String getOriRiq() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}
	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	// 按钮
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ReturnChick = false;
    public void ReturnButton(IRequestCycle cycle) {
        _ReturnChick = true;
    }

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		}
		if (_ReturnChick) {
    		_ReturnChick = false;
    		cycle.activate(ziym);//Changnzf
        }
	}

	private void save() {

		Visit visit = (Visit) this.getPage().getVisit();		
		StringBuffer sql = new StringBuffer();
		sql.append("begin\n");
		ResultSetList rsl1 = visit.getExtGrid1().getDeleteResultSet(getChange());
		JDBCcon con=new JDBCcon();
//		String Mname="";
		while (rsl1.next()){//删除 
			sql.append("delete from item where\n");
			sql.append("id=" + rsl1.getString("id"));
			sql.append(";\n");
//			Mname="删除";
		}
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult
					+ "ShujblH.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		String xuh=new String();
		xuh="";
		while (rsl.next()) {
			ResultSetList rs2=new JDBCcon().getResultSetList("select itemsortid from itemsort where mingc='" + rsl.getString("smingc")+"'");
			rs2.next();
			int itemsortid=rs2.getInt("itemsortid");
						
			if("0".equals(rsl.getString("id"))){//增加
				String zxm=new String();
				ResultSetList rs=new ResultSetList();
				zxm="select rownum from item where (bianm='" + rsl.getString("bianm") + "' or mingc ='" + rsl.getString("imingc") + "') and id<>" + rsl.getString("id");
				rs = con.getResultSetList(zxm);
				if(rs.next()){
					xuh += "(" + rs.getString("rownum") + ")";
				}else{
				
					sql.append("insert into item(\n");
					sql.append("id,itemsortid,bianm,mingc,zhuangt,beiz,xuh");
					sql.append(") values(");
					sql.append("getnewid("+visit.getDiancxxb_id()+"),");
					sql.append(itemsortid+",'");
					sql.append(rsl.getString("bianm")+"','");
					sql.append(rsl.getString("imingc")+"',");
					sql.append((getExtGrid().getColumn("zhuangt").combo).getBeanId(rsl
							.getString("zhuangt"))+",'");
					sql.append(rsl.getString("beiz"));
					sql.append("',"+1);
					sql.append(");\n");
//				Mname="添加";
				}
			}else{	//修改
				String zxm=new String();
				ResultSetList rs=new ResultSetList();
				zxm="select rownum from item where (bianm='" + rsl.getString("bianm") + "' or mingc ='" + rsl.getString("imingc") + "') and id<>" + rsl.getString("id");
				rs = con.getResultSetList(zxm);
				if(rs.next()){
					xuh += "(" + rs.getString("rownum") + ")";
				}else{
				
					sql.append("update ").append("item").append(" set ");
					sql.append("bianm='").append(rsl.getString("bianm")+"',");
					sql.append("mingc='").append(rsl.getString("imingc")+"',");
					sql.append("zhuangt=").append((getExtGrid().getColumn("zhuangt").combo).getBeanId(rsl
							.getString("zhuangt"))+",");
					sql.append("beiz='").append(rsl.getString("beiz"));
					sql.append("' where id="+rsl.getString("id"));
					sql.append(";\n");
//				Mname="修改";
				}
			}
		}
		sql.append("end;");
		if(!(con.getUpdate(sql.toString())>-1)){
			this.setMsg("该记录已被使用，无法删除");
		}
		if(xuh.equals("")){
			setMsg("保存成功！");
		}else{
			setMsg("&nbsp;&nbsp;&nbsp;与第" + xuh + "行的编码或名称有重复，请重新维护！&nbsp;&nbsp;&nbsp;");
		}
	}

	private void getSelectData() {
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		
//		if(){
//			编码为空时，MainGlobale.getTalbeCol
//			sttr=MainGlobal.getTableCol("itemsort", "bianm", "mingc", this.getItemsortSelectValue().getValue());
//			str=this.getItemsortSelectValue().getValue();
//		}

		sql.append("select i.id, s.mingc smingc,i.bianm bianm,i.mingc imingc,decode(i.zhuangt,1,'可用','不可用') zhuangt, i.beiz");
		sql.append(" from (select mingc,itemsortid from itemsort where bianm='"+getBianm()+"') s,item i");
		sql.append(" where s.itemsortid=i.itemsortid order by bianm asc");
	
		ResultSetList rsl = con.getResultSetList(sql.toString());
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
//		
		
		egu.getColumn("smingc").setHeader("项目名称");
		ResultSetList rsl1 = con.getResultSetList("select mingc from itemsort where bianm='"+getBianm()+"'");
		rsl1.next();
		egu.getColumn("smingc").setDefaultValue(rsl1.getString("mingc"));
	
		egu.getColumn("smingc").setEditor(null);
		
		egu.getColumn("bianm").setHeader("编码");
		
		egu.getColumn("imingc").setHeader("名称");
		
		egu.getColumn("zhuangt").setHeader("状态");
		List zhuangt = new ArrayList();
		zhuangt.add(new IDropDownBean("1","可用"));
		zhuangt.add(new IDropDownBean("0","不可用"));
		egu.getColumn("zhuangt").setEditor(new ComboBox());
		egu.getColumn("zhuangt").setComboEditor(egu.gridId,
				new IDropDownModel(zhuangt));
		
		egu.getColumn("beiz").setHeader("备注");
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, "InsertButton");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String sPowerHandler = "function(){"
			+"document.getElementById('ReturnButton').click();"
			+"}";
		egu.addTbarBtn(new GridButton("返回",sPowerHandler,SysConstant.Btn_Icon_Return));
		
		
//		工具栏下拉框
		if(bianms){
			egu.addTbarText("-");
			egu.addTbarText("请选择项目:");
			ComboBox comb1=new ComboBox();
			comb1.setTransform("ItemsortDropDown");
			comb1.setId("Itemsort");
			comb1.setLazyRender(true);//动态绑定
			comb1.setWidth(120);
			egu.addToolbarItem(comb1.getScript());
			egu.addOtherScript("Itemsort.on('select',function(){document.forms[0].submit();});");
		}
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		setExtGrid(egu);
		
		rsl.close();
		con.Close();
		
	}
	
	//itemsort
	public IDropDownBean getItemsortSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getItemsortSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}
	public void setItemsortSelectValue(IDropDownBean Value) {
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean3()){
			
			((Visit) getPage().getVisit()).setDropDownBean3(Value);
		}
	}
	public void setItemsortSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}
	public IPropertySelectionModel getItemsortSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getItemsortSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
	public void getItemsortSelectModels() {
		StringBuffer sql=new StringBuffer();
		sql.append("select id,mingc from itemsort order by mingc asc");
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql));
	}
	
	
	
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			ziym = null;
			ziym = visit.getActivePageName();//保存上一页的资源名
			visit.setActivePageName(getPageName().toString());
			
			setItemsortSelectValue(null);
			setItemsortSelectModel(null);
			getItemsortSelectModels();
			setBianm(null);
			bianms=false;
			String bianm = (String) cycle.getRequestContext().getSession().getAttribute("bianm");
			if(bianm == null){
				bianms=true;
			}
			setBianm(bianm);//上页传来的值
//			System.out.println(getBianm());
		}
		cycle.getRequestContext().getSession().setAttribute("bianm",null);
		
		if(bianms){
			JDBCcon con=new JDBCcon();
			String mingc = getItemsortSelectValue().getValue().toString();//下拉框的值
			ResultSetList rsl = con.getResultSetList("select bianm from itemsort where mingc='"+mingc+"'");
			rsl.next();
			setBianm(rsl.getString("bianm"));
			rsl.close();
			con.Close();
		}
		getSelectData();		
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
}
