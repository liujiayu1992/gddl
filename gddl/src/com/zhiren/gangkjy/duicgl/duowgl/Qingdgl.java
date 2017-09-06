package com.zhiren.gangkjy.duicgl.duowgl;


/**
 * @author 张琦
 */


import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


public class Qingdgl extends BasePage implements PageValidateListener {
	public List gridColumns;
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
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
	private String Changeid;

	public String getChangeid() {
		return Changeid;
	}

	public void setChangeid(String changeid) {
		Changeid = changeid;
	}
	boolean riqichange = false;
	private String riqi; //页面起始日期日期选择
	
	
	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}
	}

	private void Save() {
		if(getChange() == null || "".equals(getChange())){
			setMsg("error,修改记录为空！");
			return;
		}
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		int flag =0;
		con.setAutoCommit(false);
//		StringBuffer sql = new StringBuffer("begin \n");
		
		String sqldel ="";
		//删除
		ResultSetList rsldel = getExtGrid().getDeleteResultSet(getChange());
		while (rsldel.next()) {
			sqldel ="delete from bowxxb where id = "+rsldel.getString(0)+";\n";
		}
		
		
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		
		long chezxxb_id=0;
		String sql = "begin\n";
		sql+=sqldel;
		
		while (rsl.next()) {
					int id = rsl.getInt("id");
					sql += "update duowpdb set "
						+ " zuorkc = " + rsl.getString("zuorkc")
						+ ",jinrxm = " + rsl.getString("jinrxm")
						+ ",jinrzc = " + rsl.getString("jinrzc")
						+ ",pandml = "+rsl.getString("pandml")
						+ ",panyk = "+rsl.getString("panyk")
						+ ",beiz = '" + rsl.getString("beiz")
						+ "' where id = " + id + ";\n";
				
		}
		if(rsl.getRows()>0 || rsldel.getRows()>0){
			sql += "end;\n";
			flag = con.getUpdate(sql);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
						+ sql);
				setMsg(ErrorMessage.DeleteDatabaseFail);
				return;
			}
			if (flag !=-1){
				setMsg("保存成功！");
			}
		}
		rsldel.close();
		rsl.close();
		con.Close();
	
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	

	private boolean _CreateButton = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateButton = true;
	}

	private boolean _RefreshChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	public void submit(IRequestCycle cycle) {

		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_CreateButton) {
			_CreateButton = false;
			createData();
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
	}

	public void createData(){
		Visit v = (Visit)getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String strDianc = "";
		if(getTreeJib()==1){//集团
			strDianc = "";
		}else if(getTreeJib()==2){//公司
			strDianc = " and (dc.id="+this.getTreeid()+" or dc.fuid="+this.getTreeid()+" ) ";
		}else{
			strDianc = " and dc.id="+this.getTreeid();
		}
		
		String sql ="";
 		sql = 
 			"select t.id id,nvl(a1.ruksl, 0) as jinrzc,nvl(b1.kucl, 0) as zuorkc,nvl(c1.chuksl, 0) as jinrzc\n" +
 			"  from\n" + 
 			"       (select d.meicb_id id\n" + 
 			"          from duimxxb d, meicb m, diancxxb dc\n" + 
 			"         where m.id = d.meicb_id\n" +strDianc+"\n" + 
 			"           and m.diancxxb_id = dc.id\n" + 
 			"           and d.ruksj >= to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
 			"           and d.ruksj < to_date('"+getRiqi()+"', 'yyyy-mm-dd') + 1\n" + 
 			"         group by d.meicb_id\n" + 
 			"        union\n" + 
 			" select u.meicb_id id\n" + 
 			"          from meicb m, duowkcb u,\n" + 
 			"             (select max(u.riq) as riq,max(u.id) as id,u.meicb_id\n" + 
 			"                  from duowkcb u ,diancxxb dc\n" + 
 			"                 where u.riq < to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
 			"           and u.diancxxb_id = dc.id "+strDianc+" group by u.meicb_id) kl\n" + 
 			"           where u.riq = kl.riq and m.id = kl.meicb_id and u.id=kl.id\n" + 
 			"        union\n" + 
 			"select q.meicb_id id\n" + 
 			"          from qumxxb q, meicb m, diancxxb dc\n" + 
 			"         where m.id = q.meicb_id\n" + 
 			"           and m.diancxxb_id = dc.id "+strDianc+"\n" + 
 			"           and q.chukkssj >= to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
 			"           and q.chukkssj < to_date('"+getRiqi()+"', 'yyyy-mm-dd') + 1\n" + 
 			"         group by q.meicb_id) t,\n" + 
 			" (select d.meicb_id id, to_char(sum(d.ruksl)) ruksl\n" + 
 			"          from duimxxb d, meicb m, diancxxb dc\n" + 
 			"         where m.id = d.meicb_id  and m.diancxxb_id=dc.id "+strDianc+"\n" + 
 			"           and d.ruksj >= to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
 			"           and d.ruksj < to_date('"+getRiqi()+"', 'yyyy-mm-dd') + 1\n" + 
 			"         group by d.meicb_id) a1,\n" + 
 			" (select u.meicb_id id, to_char(u.kucl) as kucl\n" + 
 			"          from meicb m, duowkcb u,\n" + 
 			"               (select max(u.riq) riq,u.meicb_id,max(u.id) as id from duowkcb u, diancxxb dc\n" + 
 			"                 where  u.riq < to_date('"+getRiqi()+"', 'yyyy-mm-dd') and u.diancxxb_id=dc.id "+strDianc+"\n" + 
 			"                 group by u.meicb_id) kc\n" + 
 			"         where u.riq = kc.riq and m.id=kc.meicb_id and u.id=kc.id ) b1,\n" + 
 			"(select q.meicb_id id, to_char(sum(q.chuksl)) as chuksl\n" + 
 			"          from qumxxb q, meicb m, diancxxb dc\n" + 
 			"         where m.id = q.meicb_id   and m.diancxxb_id=dc.id "+strDianc+"\n" + 
 			"           and q.chukkssj >= to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
 			"           and q.chukkssj < to_date('"+getRiqi()+"', 'yyyy-mm-dd') + 1\n" + 
 			"         group by q.meicb_id) c1\n" + 
 			" where t.id = a1.id(+) and t.id = b1.id(+) and t.id = c1.id(+)";


		try{
			
			ResultSetList rsl = con.getResultSetList(sql);
			StringBuffer sb = new StringBuffer("begin \n");
			
			sb.append("delete from duowpdb where riq=to_date('"+getRiqi()+"', 'yyyy-mm-dd') and diancxxb_id= "+getTreeid()+";");			
			int i=0;
			while(rsl.next()){
				i++;
				long new_id = Long.parseLong(MainGlobal.getNewID(v.getDiancxxb_id()));
				sb.append("insert into duowpdb(id ,diancxxb_id,riq,meicb_id,zuorkc,jinrxm,jinrzc,pandml,panyk,pandr,leib,zhuangt,beiz)");
				sb.append(" values(").append(new_id).append(",").append(v.getDiancxxb_id()).append(",to_date('");
				sb.append(getRiqi()).append("','yyyy-mm-dd'),").append(rsl.getLong("id")).append(",");
				sb.append(rsl.getDouble("zuorkc")).append(",").append(rsl.getDouble("jinrxm")).append(",");
				sb.append(rsl.getDouble("jinrzc")).append(",0,0,'',0,1,'');");
			}
			sb.append("end;\n ");
			int flag = -1;
			if(i>0){
				flag = con.getUpdate(sb.toString());
			}else if (i==0){
				flag = 0;
			}
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
						+ sql);
				setMsg(ErrorMessage.DeleteDatabaseFail);
				return;
			}else if(flag == 0){
				setMsg("没有数据可生成！");
			}

		}catch(Exception e){
			e.printStackTrace();
			con.rollBack();
			con.Close();
		}finally{
			con.Close();
			
		}
		
			
		
	}
	public void getSelectData() {
		StringBuffer sql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();
		String sql1 = "";
		JDBCcon con = new JDBCcon();
		
		String strDianc = "";
		if(getTreeJib()==1){//集团
			strDianc = "";
		}else if(getTreeJib()==2){//电厂
			strDianc = " and (dc.id="+this.getTreeid()+" or dc.fuid="+this.getTreeid()+" ) ";
		}else{
			strDianc = " and dc.id="+this.getTreeid();
		}
			 sql1=
				 "select d.id,\n" +
				 "         d.diancxxb_id,\n" + 
				 "         d.riq,\n" + 
				 "         m.mingc,\n" + 
				 "         d.zuorkc,\n" + 
				 "         d.jinrxm,\n" + 
				 "         d.jinrzc,\n" + 
				 "         d.pandml,\n" + 
				 "		   0 zhangm,\n"+
				 "         d.panyk,\n" + 
				 "         d.pandr,\n" + 
				 "         d.beiz\n" + 
				 "    from duowpdb d,  meicb m ,diancxxb dc\n" + 
				 "   where d.meicb_id = m.id and d.diancxxb_id=dc.id "+strDianc+" \n" + 
				 "     and d.riq =to_date('"+getRiqi()+"', 'yyyy-mm-dd') and m.mingc<>'直达煤场'";
		
		ResultSetList rsl = con.getResultSetList(sql1);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("duowpdb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("panyk").setHeader(Local.panyk);
		egu.getColumn("panyk").setWidth(80);
		egu.getColumn("panyk").setEditor(null);
		egu.getColumn("zhangm").setHeader(Local.zhangmcm);
		egu.getColumn("zhangm").setWidth(80);
		egu.getColumn("zhangm").setEditor(null);
		egu.getColumn("pandml").setHeader(Local.pandml);
		egu.getColumn("pandml").setWidth(80);
		egu.getColumn("jinrzc").setHeader(Local.zhuangcl_zhuangcb);
		egu.getColumn("jinrzc").setWidth(80);
		egu.getColumn("jinrxm").setHeader(Local.jinrxm);
		egu.getColumn("jinrxm").setWidth(80);
		egu.getColumn("zuorkc").setHeader(Local.zuorkc);
		egu.getColumn("zuorkc").setWidth(80);
		egu.getColumn("riq").setHeader(Local.riq);
		egu.getColumn("riq").setWidth(80);
		egu.getColumn("mingc").setHeader(Local.duow);
		egu.getColumn("mingc").setWidth(80);
		egu.getColumn("mingc").setEditor(null);
		egu.getColumn("pandr").setHeader(Local.pandr);
		egu.getColumn("pandr").setWidth(80);
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(120);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		
		// 工具日期下拉框
		egu.addTbarText("时间:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.setReadOnly(true);
		df.Binding("RIQI", "forms[0]");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());
		
//		工具栏			
//		 树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
											ExtTreeUtil.treeWindowType_Dianc,
											((Visit) this.getPage()	.getVisit()).getDiancxxb_id(), 
											getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText(" ");
		egu.addTbarText("-");// 设置分隔符
		
		//查询按钮
		GridButton gbtr = new GridButton("查询","function(){document.getElementById('RefurbishButton').click();}");
		gbtr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbtr);
		//生成按钮
		GridButton gbt = new GridButton("生成","function(){document.getElementById('CreateButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbt);
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		String script = "gridDiv_grid.on('afteredit',function(e){ " +
			"if(e.field == 'ZUORKC'||e.field == 'JINRXM'||e.field == 'JINRZC'){ " + 
			"var record = gridDiv_ds.getAt(e.row);\n" + 
			" var zuorkc = eval(record.get('ZUORKC')||0);\n" + 
			"var jinrxm = eval(record.get('JINRXM')||0);\n" + 
			"var jinrzc = eval(record.get('JINRZC')||0);\n" + 
			"var zhangm = zuorkc+jinrxm-jinrzc;\n" + 
			"record.set('ZHANGM',zhangm);\n" + 
			"}\n" + 
			"if(e.field == 'PANDML'){\n" + 
			"var record = gridDiv_ds.getAt(e.row);\n" + 
			"var pandml = eval(record.get('PANDML')||0);\n" + 
			"var zhangm = eval(record.get('ZHANGM')||0);\n" + 
			"var panyk = pandml-zhangm;\n" + 
			"record.set('PANYK',panyk);\n" + 
			"}\n" + 
			"}\n" + 
			");";
		
		egu.addOtherScript(script);
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

//	树
	private String treeid;
	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}
	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}
	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}
	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
	
//	private String getTreeValue(){
//		JDBCcon con=new JDBCcon();
//		ResultSetList rsl = con.getResultSetList("select mingc from diancxxb where id="+getTreeid());
//		rsl.next();
//		con.Close();
//		return rsl.getString("mingc");
//}
	private int getTreeJib(){
		JDBCcon con=new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select jib from diancxxb where id="+getTreeid());
		rsl.next();
		con.Close();
		return rsl.getInt("jib");
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
			visit.setDefaultTree(null);
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		getSelectData();
	}

}




