package com.zhiren.hebkhh;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.Locale;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Kaohbz extends BasePage implements PageValidateListener {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
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

	private void Save() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
			return;
		}
		StringBuffer sb=new StringBuffer();
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		int flag=0;
		int id = 0;
		ResultSetList rssb=getExtGrid().getDeleteResultSet(getChange());
		if(rssb==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Fahxg.Save 中 getExtGrid().getDeleteResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while(rssb.next()){
			id=rssb.getInt("id");
			//删除时新增日志
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Riscsj,
					"kaohbzb",id+"");
			String sql="delete from kaohbzb where id="+id;
			flag=con.getDelete(sql);
			if(flag==-1){
				con.rollBack();
				con.Close();
				
			}
			con.commit();
		}
		
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Riscsjwh.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		
		sb.append("begin ");
		while(rsl.next()) {
			id=rsl.getInt("id");
			if(id==0){
				sb.append("insert into kaohbzb (id,kaohxm_id,shangx,xiax,chaocjz,biaoz,beiz)");
				sb.append("values (getnewid(").append(visit.getDiancxxb_id()).append("),");
				sb.append((getExtGrid().getColumn("mingc").combo).getBeanId(rsl.getString("mingc"))).append(",").append(rsl.getDouble("shangx")).append(",");
				sb.append(rsl.getDouble("xiax")).append(",").append(rsl.getDouble("chaocjz")).append(",").append(rsl.getDouble("biaoz")).append(",'").append(rsl.getString("beiz")).append("');\n") ;
			}else{
				sb.append("update kaohbzb set kaohxm_id=").append((getExtGrid().getColumn("mingc").combo).getBeanId(rsl.getString("mingc"))).append(",shangx=");
				sb.append(rsl.getDouble("shangx")).append(",xiax=").append(rsl.getDouble("xiax")).append(",chaocjz=").append(rsl.getDouble("chaocjz")).append(",biaoz=").append(rsl.getDouble("biaoz")).append(",beiz='").append(rsl.getString("beiz")).append("'");
				sb.append(" where id=").append(id).append(";\n");
			}
		}
		sb.append("end;");
		flag = con.getInsert(sb.toString());
		if(flag==-1){
			con.rollBack();
			con.Close();
			
		}
		if (flag!=-1){//保存成功
			setMsg(ErrorMessage.SaveSuccessMessage);
		}
		con.commit();
		con.Close();
		//setMsg("保存成功");
	}

	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
	}	
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon(); 
		
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select k.id,i.mingc,k.shangx,k.xiax,k.chaocjz,k.biaoz,k.leib,k.beiz ");
		sbsql.append("from kaohbzb k,item i ");
		sbsql.append("where k.kaohxm_id=i.id ");
		sbsql.append("order by k.id");
		
		ResultSetList rsl = con.getResultSetList(sbsql.toString());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sbsql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("kaohbzb");
		
		egu.getColumn("mingc").setHeader("考核项目");
		egu.getColumn("mingc").setWidth(100);
		egu.getColumn("shangx").setHeader("上限");
		egu.getColumn("shangx").setWidth(100);
		egu.getColumn("xiax").setHeader("下限");
		egu.getColumn("xiax").setWidth(100);
		egu.getColumn("chaocjz").setHeader("超差基准");
		egu.getColumn("chaocjz").setWidth(100);
		egu.getColumn("biaoz").setHeader("标准");
		egu.getColumn("biaoz").setWidth(100);
		egu.getColumn("leib").setHeader("类别");
		egu.getColumn("leib").setWidth(100);
		egu.getColumn("leib").setHidden(true);
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(200);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setWidth(Locale.Grid_DefaultWidth);
//		 设置项目下拉框
		ComboBox c1 = new ComboBox();
		egu.getColumn("mingc").setEditor(c1);
		c1.setEditable(true);
		String Sql = "select it.id,it.mingc from item it where it.itemsortid = (select its.id from itemsort its where its.bianm = 'KAOHXM') and it.zhuangt = 1 order by it.id";
		egu.getColumn("mingc").setComboEditor(egu.gridId,new IDropDownModel(Sql));
		
		
//egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
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
		if(getExtGrid() == null) {
			return "";
		}
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid() == null) {
			return "";
		}
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
			setTbmsg(null);
			getSelectData();
		}
	}
}
