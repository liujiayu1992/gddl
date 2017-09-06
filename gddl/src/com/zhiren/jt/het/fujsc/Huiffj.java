package com.zhiren.jt.het.fujsc;
/*
 * 作          者：  xieb
 * 适用范围： 大唐国际及下属单位
 * 时          间：  2012-05-22
 * 内          容：  点击发布文档管理中回复文件按钮，进入附件上传页面上传需要恢复的文件
 * */
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.request.IUploadFile;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public abstract class Huiffj extends BasePage {
	public static final String AttrName_LastPage = "PageName";
	public static final String AttrName_AttachmentType = "AttachmentType";
	public static final String AttrName_RecordID = "RecordID";
	private static final String home="d:/wenjgl/";
	private String msg;
	private String Change;
	private String fabwjb_id;
	private IUploadFile _fileStream;
	private boolean isUpLoad;
	private boolean isDelete;
	private boolean isReturn;
	private boolean isSave;
	private boolean isShow;
	
	// 提示框
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public IUploadFile getFileStream() {
		return _fileStream;
	}
	public void setFileStream(IUploadFile uf) {
		_fileStream = uf;
	}
	// 按钮事件处理
	public void UpLoadButton(IRequestCycle cycle) {
		isUpLoad = true;
	}
	
	public void DeleteButton(IRequestCycle cycle) {
		isDelete = true;
	}

	public void ReturnButton(IRequestCycle cycle) {
		isReturn = true;
	}
	
	public void SaveButton(IRequestCycle cycle) {
		isSave = true;
	}
	
	public void ShowButton(IRequestCycle cycle) {
		isShow = true;
	}
	
	// 用EXT构建页面
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
	
	
	public void initGrid(){
		
		JDBCcon con = new JDBCcon();
		String wenjb_id=((Visit) getPage().getVisit()).getString8();
		long diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
		String huiffjb_id="";
		String fbid="select id from fabwjb where wenjb_id="+wenjb_id+
					" AND DIANCXXB_ID="+diancxxb_id;
		ResultSetList fbrl = con.getResultSetList(fbid);	
		while(fbrl.next()){
			fabwjb_id=fbrl.getString("id");
		}
		String  sql=
		"SELECT FJ.ID,FB.ID AS FBID,FJ.MINGC ,FJ.URL ,FJ.YUANMC  FROM HUIFFJB FJ,FABWJB FB WHERE\n" +
		" FB.WENJB_ID="+wenjb_id+"\n" + 
		" AND FJ.FABWJB_ID=FB.ID " +
		" AND FB.DIANCXXB_ID="+diancxxb_id;
		ResultSetList rsl = con.getResultSetList(sql);	
		while(rsl.next()){
			huiffjb_id=rsl.getString("id");
			fabwjb_id=rsl.getString("fbid");
		}
		rsl.beforefirst();
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("HUIFFJB");
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.getColumn("ID").setHidden(true);
		egu.getColumn("ID").setEditor(null);
		
		egu.getColumn("FBID").setHidden(true);
		egu.getColumn("FBID").setEditor(null);
		
		egu.getColumn("MINGC").setHeader("文件名称");
		egu.getColumn("MINGC").setEditor(null);
		egu.getColumn("MINGC").setWidth(120);
		
		egu.getColumn("URL").setHidden(true);
		egu.getColumn("URL").setEditor(null);
		
		egu.getColumn("YUANMC").setHidden(true);
		egu.getColumn("YUANMC").setEditor(null);
	
		egu.addToolbarButton("添加附件", GridButton.ButtonType_Insert_condition, null, "document.getElementById('FileStream').style.display = ''; this.setDisabled(true);");
		egu.addToolbarButton("上传并保存", GridButton.ButtonType_SubmitSel_condition, "UpLoadButton",
				"  var rec = gridDiv_grid.getSelectionModel().getSelections();	\n" +
				"  if(rec.length==0){	Ext.MessageBox.alert('提示信息','请选择要上传的附件的记录!'); return; }	\n" +
				"  if(rec[0].get('ID')==0){	\n" +
				"  		if(document.getElementById('FileStream').value == ''){\n" +
				"\n" + 
				"    		Ext.MessageBox.alert('提示信息','请选择要上传的附件!');\n" + 
				"    		return;\n" + 
				"  		}else{\n" + 
				"\n" +
				"	 		var FileMaxSize=10240;	//10M	\n" +
				"	 		var fso,f,s;	//10M	\n" +
				"    		var rec = gridDiv_grid.getSelectionModel().getSelections();\n" + 
				"    		if(rec.length>0){\n" + 
				"      				var filename = '';\n" + 
				"      			if(document.getElementById('FileStream').value.lastIndexOf('\\\\')>-1){\n" + 
				"\n" + 
				"        			filename = document.getElementById('FileStream').value.substring(document.getElementById('FileStream').value.lastIndexOf('\\\\')+1);\n" +
				"					document.getElementById('zhuangt').value = 1;	\n" +									
				"      			}else{\n" + 
				"\n" + 
				"        			filename = document.getElementById('FileStream').value\n" + 
				"					document.getElementById('zhuangt').value = 1;	\n" +	
				"     			}\n" + 
				"\n" + 
				"      				gridDiv_ds.getAt(gridDiv_grid.getSelectionModel().lastActive).set('MINGC',filename);\n" + 
				"    		}else{\n" + 
				"\n" + 
				"      			Ext.MessageBox.alert('提示信息','请选择要保存的记录!');\n" + 
				"				return;\n" + 
				"\t\t		}\n" + 
				"\t		}	\n" +
				"	}else{ Ext.MessageBox.alert('提示信息','非新增附件记录，无法重复上传附件!'); return; \n	}	\n"
		);
//		egu.addToolbarButton("保存", GridButton.ButtonType_SubmitSel_condition, "SaveButton", 
//					"var rec = gridDiv_grid.getSelectionModel().getSelections();\n" +
//					"\n" + 
//					"  if(rec.length==0){  Ext.MessageBox.alert('提示信息','请选择要保存的附件的记录!'); return;\n" + 
//					"}\n" + 
//					"if(rec[0].get('MINGC')==''){\n" + 
//					"\n" + 
//					"  Ext.MessageBox.alert('提示信息','如需要上传附件，请点击<上传并保存>按钮!'); return;\n" + 
//					"}" +
//					"document.getElementById('zhuangt').value = '1';" +
//					"");
		
		egu.addToolbarButton("删除", GridButton.ButtonType_SubmitSel_condition, null, 
				"var rec = gridDiv_grid.getSelectionModel().getSelections();\n " +
				"document.getElementById(\"CHANGE\").value=rec[0].get('ID');\n"+
				"  \t  document.getElementById(\"DeleteButton\").click();\n" + 
				"document.getElementById('zhuangt').value = '1';" +
				"");
		
		egu.addToolbarButton("查看附件", GridButton.ButtonType_SubmitSel_condition, null, 
				"var rec = gridDiv_grid.getSelectionModel().getSelections();\n" +
				"if(rec.length>0){\n" + 
				"\n" + 
				"  rec[0].get('ID');\n" + 
				"  window.open(rec[0].get('URL'),'Chak');\n" + 
				"}else{ Ext.MessageBox.alert('提示信息','请选择要查看的附件!');}\n" + 
				"return;");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addOtherScript(
				"gridDiv_ds.on('add',function(){ gridDiv_grid.getSelectionModel().selectRow(gridDiv_ds.getCount()-1);\n" +
				"                gridDiv_grid.getView().focusRow(gridDiv_ds.getCount()-1);\n" + 
				"});");
		setExtGrid(egu);
		con.Close();
	}
	
	
	private void Delete(){
		if (getChange() == null || "".equals(getChange())) {
			setMsg("请选择要删除的附件!");
			return;
		}
		JDBCcon con=new JDBCcon();
		String records=getChange();
		String sql="";
		String[]record=records.split(";");
		con.setAutoCommit(false);
		for(int i=0;i<record.length;i++){
			String[] ids=record[i].split(",");//ids[0]:wenjb_id,ids[1]:huifnr
			
			sql="DELETE FROM HUIFFJB WHERE ID = " + ids[0];
			int flag = con.getDelete(sql);
			if(flag == -1){
				setMsg("文件删除失败!请检查数据!");
				return;
			}
			else
				setMsg("删除成功!");
		}
	con.Close();
	}
	
	
	private void Return(IRequestCycle cycle){
		String strLastActivePageName = (String)cycle.getRequestContext().getSession().getAttribute(AttrName_LastPage);
		cycle.activate(strLastActivePageName);
	}
	
	
	public void submit(IRequestCycle cycle) {
		if (isUpLoad) {
			isUpLoad = false;
			Save();
			initGrid();
		}
		if (isDelete) {
			isDelete = false;
			Delete();
			initGrid();
		}
		if (isReturn) {
			isReturn = false;
			Return(cycle);
		}
		if (isSave) {
			isSave = false;
			Save();
			initGrid();
		}
	}
	
	private void Save(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String cuncm="";//实际存储名称
		String strDateTime=DateUtil.FormatDateTime(new Date());
		
		IUploadFile uploadf=getFileStream();
		InputStream is = uploadf.getStream();//上传文件的流
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(this.getChange());
		con.setAutoCommit(false);
		String huiffjb_id = "0"; 
		cuncm=(new Date().getYear() + 1900) + "/"+Math.abs(new java.util.Random().nextLong())+"_"+uploadf.getFileName();
		File file = new File(home	+ cuncm);//缀上后缀以防止重名
		BufferedOutputStream os = null;
		byte[] buff = new byte[1024];
		System.out.print(file);
		try {
			os = new BufferedOutputStream(new FileOutputStream(file));
			while (is.read(buff) != -1) {
				os.write(buff);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("图片上传出错！");
		} finally {
			try {
				os.close();
				is.close();
			} catch (Exception e) {
				
			}
		}
		String url = "http://"
			+ this.getRequestCycle().getRequestContext()
					.getServerName()
			+ ":"
			+ this.getRequestCycle().getRequestContext()
					.getServerPort() + ""
			+ this.getEngine().getContextPath() + "/app?service=page/downfile"
			+ "&filename=" + cuncm + "'||'&'||'filepath="
			+ home ;
		if (mdrsl.next()) {
			if ("0".equals(mdrsl.getString("ID"))) {//新增
				huiffjb_id = MainGlobal.getNewID(con,((Visit) getPage().getVisit()).getDiancxxb_id());
					 sql.append("INSERT INTO HUIFFJB(ID, FABWJB_ID, MINGC, URL, YUANMC)")
						.append("VALUES(").append(huiffjb_id).append(",")
						.append(fabwjb_id).append(",'").append(mdrsl.getString("mingc")).append("','")
						.append(url).append("','").append(uploadf.getFileName()).append("'").append(");	\n");

					 sql.append("update fabwjb set huifsj=to_date('"+strDateTime+"','YYYY-MM-DD HH24:MI:SS')," )
					    .append("huifr='"+visit.getRenymc()+"',zhuangt=1\n" )
					    .append("where id = "+fabwjb_id+";\n");
			} else {
					 sql.append("UPDATE ").append(this.getExtGrid().tableName).append(" SET ")
						.append("MINGC = '").append(mdrsl.getString("MINGC")).append("',")
						.append("URL").append(mdrsl.getString("url")).append("',")	
						.append("YUANMC = '").append(mdrsl.getString("YUANMC")).append("'")
						.append(" where ID =").append(fabwjb_id).append(";\n");
			}
		}
		sql.append("end;");
		mdrsl.close();
	
		if(con.getUpdate(sql.toString())>=0){		
			this.setMsg("文件保存成功!");
			con.commit();			
		}else{			
			con.rollBack();
		}
		con.Close();
	}
		
	// 初始化函数，每次自动调用，这里只是给提示框初始化
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	// 页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
//		固定用String8 接收id
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			if (cycle.getRequestContext().getParameters("wenjb_id") != null) {
				visit.setString8(cycle.getRequestContext().getParameters("wenjb_id")[0]);
			}
		}
		initGrid();
	}
	// 验证用户名和密码
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
	
	
	public static String[] getServerInfo(String yewdw_id){
		
		String ServerInfo[] = null;
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer("");
		sb.append(
				"SELECT DANWID, MINGC\n" +
				"  FROM SERVERINFO\n" + 
				" WHERE DANWID IN (SELECT ZUZ_ID FROM SHENHZTPZB WHERE DIANCXXB_ID = "+yewdw_id+")\n" + 
				"   AND SERVERINFO.MYID = 0\n" + 
				"   AND SERVERINFO.TONGB = 1");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.getRows()>0){
			
			ServerInfo = new String[rsl.getRows()];
			int i = 0;
			while(rsl.next()){
				
				ServerInfo[i++] = rsl.getString("MINGC");
			}
		}
		rsl.close();
		con.Close();
		return ServerInfo;
	}
	
}