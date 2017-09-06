package com.zhiren.pub.taolq;

import java.io.File;
import java.sql.ResultSet;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Taolq extends BasePage implements PageValidateListener {
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
	
	private boolean shuaxButton = false;

	public void shuaxButton(IRequestCycle cycle) {
		shuaxButton = true;
	}
	private boolean zhidButton = false;

	public void zhidButton(IRequestCycle cycle) {
		zhidButton = true;
	}
	private boolean shancButton = false;

	public void shancButton(IRequestCycle cycle) {
		shancButton = true;
	}
	public void submit(IRequestCycle cycle) {
		if (shuaxButton) {
			
			getSelectData();
		}
		if (zhidButton) {
			
			zhid();
		}
		if (shancButton) {
			
			shanc();
		}
	}
	private void shanc(){
		//删除文件
		String cuncm="";//实际存储名称(相对)
		JDBCcon con = new JDBCcon();
		String sql=
			"select mingc\n" +
			"from fujb\n" + 
			"where fujb.wenjb_id in(select id from tiezb where id="+getChange()+" or fuid="+getChange()+")";
		ResultSet rs=con.getResultSet(sql);
		try{
			while(rs.next()){
				cuncm=Xiewj.home+rs.getString(1);
//				先删除文件系统中的文件file
				File file=new File(cuncm);
				if(file.exists()){
					file.delete();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//删除附件表

		sql="delete\n" +
		"from fujb\n" + 
		"where fujb.wenjb_id in(select id from tiezb where id="+getChange()+" or fuid="+getChange()+")";
		con.getDelete(sql);
		//删除主文件
		sql=
			"delete from tiezb where id="+getChange()+" or fuid="+getChange();
		con.getDelete(sql);
		getSelectData();
	}
	private void zhid(){
		JDBCcon con = new JDBCcon();
		String sql="";
		sql=
			"update tiezb\n" +
			"set zhid=(select nvl(max(zhid),0)+1 from tiezb)\n" + 
			"where id="+getChange();
		con.getUpdate(sql);
		getSelectData();
	}
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		String sql="select t.id,biaot,renyxxb.quanc mingc,(select count(*)from tiezb where fuid=t.id)||'/'||t.jisq huifck,\n" +
		"(select max(shij)from tiezb where fuid=t.id)zuihfbsj\n" + 
		"from tiezb t,renyxxb\n" + 
		"where t.renyxxb_id=renyxxb.id and fuid=0 order by t.zhid desc ,t.id";
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// egu.setTitle("车站信息");
//		egu.setTableName("jihkjb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("biaot").setHeader("标题");
		if(((Visit) getPage().getVisit()).getString10().equals("2")){//管理组lx=2
			egu.getColumn("biaot")
			.setRenderer(
					"function(value,p,record){ var url1 = 'http://'+document.location.host+document.location.pathname;var end1 = url1.indexOf(';');url1 = url1.substring(0,end1);url1 = url1 + '?service=page/Taolq2&lx=2&tiezb_id='+record.data['ID']; return String.format('<a href={1} target=_blank>{0}</a>',value,url1);}");
		}else{//普通组lx=1
			egu.getColumn("biaot")
			.setRenderer(
					"function(value,p,record){ var url1 = 'http://'+document.location.host+document.location.pathname;var end1 = url1.indexOf(';');url1 = url1.substring(0,end1);url1 = url1 + '?service=page/Taolq2&lx=1&tiezb_id='+record.data['ID']; return String.format('<a href={1} target=_blank>{0}</a>',value,url1);}");
		}
		
		egu.getColumn("biaot").setWidth(650);
		egu.getColumn("mingc").setHeader("作者");
		egu.getColumn("huifck").setHeader("回复/查看");
		egu.getColumn("zuihfbsj").setHeader("最后回复时间");
//		egu.getColumn("beiz")
//				.setRenderer(
//						"function(value){return String.format('<a href=http://localhost/ext/examples/grid/{1}>{0}</a>',value,'array-grid.html');}");
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		egu.addPaging(18);
		// egu.getColumn("lujxxb_id").setEditor(new ComboBox());
		// egu.getColumn("lujxxb_id").setComboEditor(egu.gridId, new
		// IDropDownModel("select id,mingc from lujxxb"));
		// List l = new ArrayList();
		// l.add(new IDropDownBean(0,"车站"));
		// l.add(new IDropDownBean(1,"港口"));
		// egu.getColumn("leib").setEditor(new ComboBox());
		// egu.getColumn("leib").setComboEditor(egu.gridId, new
		// IDropDownModel(l));
		// egu.getColumn("leib").setReturnId(false);
		// egu.getColumn("leib").setDefaultValue("车站");
//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
//		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarItem("{"+new GridButton("刷新","function (){ document.all.item('shuax').click();}").getScript()+"}");
		//删除置顶功能已经有了暂时注掉
		egu.addToolbarItem("{text: '发布主题', handler:  function(){var url1 = 'http://'+document.location.host+document.location.pathname;var end1 = url1.indexOf(';');url1 = url1.substring(0,end1);window.open(url1+'?service=page/Tiez','ddd','toolbar=0,resizable=0,status=1,width=600,height=430,scrollbar=1,left='+(window.screen.width-600)/2+',top='+(window.screen.height-430)/2);}}");
		if(((Visit) getPage().getVisit()).getString10().equals("2")){//管理组
			egu.addToolbarItem("{"+new GridButton("删除","function (){ if(gridDiv_sm.getSelected()==null){alert('请选择主题！');return;};document.all.item('CHANGE').value=gridDiv_sm.getSelected().get('ID'); document.all.item('shanc').click();}").getScript()+"}");
			egu.addToolbarItem("{"+new GridButton("置顶","function (){if(gridDiv_sm.getSelected()==null){alert('请选择主题！');return;};document.all.item('CHANGE').value=gridDiv_sm.getSelected().get('ID'); document.all.item('zhid').click();}").getScript()+"}");
		
		}
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
//		if (!visit.getActivePageName().toString().equals(
//				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
		if(shuaxButton){
			shuaxButton = false;
		}else if(zhidButton){
			zhidButton = false;
		}else if(shancButton){
			shancButton = false;
		}else{//第一次进来
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString10(cycle.getRequestContext().getParameter("lx")==null?"1":cycle.getRequestContext().getParameter("lx"));//读取权限1普通用户2管理用户
			getSelectData();
		}
	}
}
