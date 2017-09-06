/*
 * 创建日期 2005-11-9
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.zhiren.pub.sql;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.request.IUploadFile;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;

/**
 * @author wl
 * 
 * TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class Sqlzhix extends BasePage {

	private String _msg;
	private IUploadFile _fileStream;
	
	public IUploadFile getFileStream() {
		return _fileStream;
	}
    
    public void setFileStream(IUploadFile fileStream) {
		_fileStream = fileStream;
	}

	protected void initialize() {
		_msg = "";
		_ShowErr = "";
	}

	public void setMsg(String _value) {
		
		this._msg = MainGlobal.getExtMessageBox(_msg,false);
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	private String _sql;

	public String getSql() {
		if (_sql == null) {
			_sql = "";
		}
		return _sql;
	}

	public void setSql(String value) {
		_sql = value;
	}

	private String _sqlresult;

	public String getSqlResult() {
		if (_sqlresult == null) {
			_sqlresult = "";
		}
		return _sqlresult;
	}

	public void setSqlResult(String value) {
		_sqlresult = value;
	}

	public void submit(IRequestCycle cycle) {
		setSqlResult("");
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_SelectChick) {
			_SelectChick = false;
			Select();
		}/*
		if(_DownErrChick){
			_DownErrChick = false;
			DownErr();
		}
		if(_RestartTomcatChick){
			_RestartTomcatChick = false;
			RestartTomcat();
		}*/
		if(_UploadChick) {
			_UploadChick = false;
			Upload();
		}
		if(_DeleteChick) {
			_DeleteChick = false;
			Delete();
		}
		if(_ExecChick) {
			_ExecChick = false;
			Exec();
		}
		if(_DownFileChick){
			_DownFileChick = false;
			DownFile();
		}
	}

	private boolean _SelectChick = false;

	public void SelectButton(IRequestCycle cycle) {
		_SelectChick = true;
	}

	private void Select() {
		JDBCcon con = new JDBCcon();
		if(getSql().equals("")){
			setMsg("查询SQL为空!");
			return;
		}
		String sqls[] = getSql().split(";");
		int sqlIndex =0;
		for(;sqlIndex<sqls.length;sqlIndex++){
			String sql = sqls[sqlIndex].trim();
			if(sql.equals("")){
				continue;
			}
			if(sql.indexOf("--")!=0){
				ResultSet rs = con.getResultSet(sql);
				if(rs ==null){
					setMsg("查询SQL错误!");
					return;
				}
				int rows = JDBCcon.getRow(rs);
				if(rows==-1){
					setMsg("获取记录行数失败!");
					return;
				}else
					if(rows==0){
						setMsg("查询到0条记录!");
						return;
					}else{
						setMsg("查询到 "+rows+" 条记录!");
					}
				int ShowMaxRow = 100;
				int ShowRow = 1;
				_sqlresult = " <table class=\"TABLEdanj\"> <tr> ";
				int size = JDBCcon.getColumnCount(rs);
				for (int i = 1; i <= size; i++) {
					_sqlresult += " <td> " + JDBCcon.getColumnName(rs, i) + "</td>";
					if (i + 1 > size) {
						_sqlresult += "</tr> \n";
					}
				}
				try {
					while (rs.next()) {
						if(ShowRow++ > ShowMaxRow){
							setMsg("查询的记录数过多,系统截取前100条记录显示!");
							break;
						}
						_sqlresult += " <tr>";
						for (int i = 1; i <= size; i++) {
							_sqlresult += " <td> " + rs.getString(i) + "</td>";
							if (i + 1 > size) {
								_sqlresult += "</tr> \n";
							}
						}
					}
					rs.close();
					_sqlresult += "  </table>";
					return;
				} catch (Exception e) {
					e.printStackTrace();
					setMsg("查询失败！");
				}
			}
		}
		if(sqlIndex==sqls.length){
			setMsg("查询SQL已被注释!");
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private void Save() {
		if(getSql().toLowerCase().indexOf("function")!=-1
				|| getSql().toLowerCase().indexOf("trigger")!=-1
				|| getSql().toLowerCase().indexOf("package")!=-1){
			Create();
		}
		JDBCcon con = new JDBCcon();
		String msg = "执行成功!";
		con.setAutoCommit(false);
		int reValue = -2;
		if (!getSql().equals("")) {
			
			String Sqls[] =  getSql().split(";");
			int i=0;
			for(;i<Sqls.length;i++){
				String sql = Sqls[i].trim();
				if(sql.equals("")){
					continue;
				}
				if(sql.indexOf("--")!=0){
					reValue = con.getUpdate(sql);
					if(reValue == -1){
						msg = "含有不正确的SQL语句！";
						setSql(getSql().replaceAll(sql, "\r#"+sql+"#"));
						con.rollBack();
						con.Close();
						return;
					}
				}
			}
		}else{
			msg ="执行SQL为空！";
			return;
		}
		con.commit();
		con.Close();
		setMsg(msg);
	}
	
	private String _ShowErr;
	public String getShowErrOut(){
		return _ShowErr;
	}
	/*
	private boolean _DownErrChick = false;
	
	public void DownErrButton(IRequestCycle cycle){
		_DownErrChick = true;
	}
	
	private void DownErr() {
		JDBCcon con = new JDBCcon();
		String sql = "select zhi,beiz from xitxxb where duixm='Tomcat日志路径'";
		String fileName="stdout.log";
		String filePath="D:/Tomcat 5.0/logs";
		try{
			ResultSet rs = con.getResultSet(sql);
			while(rs.next()){
				fileName = rs.getString("zhi");
				filePath = rs.getString("beiz");
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		_ShowErr = "window.open('"+MainGlobal.getHomeContext(getPage())+"/downfile.jsp?filename="+fileName+"&filepath="+filePath+"','','')";
	}
	*/
	private void Create(){
		JDBCcon con = new JDBCcon();
		PreparedStatement ps = con.getPresultSet(getSql());
		try {
			ps.execute();
		} catch (SQLException e1) {
			// TODO 自动生成 catch 块
			e1.printStackTrace();
		}
		con.Close();
	}
/*
	private boolean _RestartTomcatChick = false;
	
	public void RestartTomcatButton(IRequestCycle cycle){
		_RestartTomcatChick = true;
	}
	
	private void RestartTomcat(){
		
		Runtime rn=Runtime.getRuntime(); 
		try{ 
			rn.exec("net stop \"Apache Tomcat\"");
			rn.exec("net start \"Apache Tomcat\"");
		}catch(Exception e){ 
			System.out.println("Error exec "); 
		}
	}
	*/
	private boolean _UploadChick = false;
	
	public void UploadButton(IRequestCycle cycle) {
		_UploadChick = true;
	}
	
	private void Upload() {
		IUploadFile _file = getFileStream();
    	if(_file.getFileName() == null || "".equals(_file.getFileName())) {
    		setMsg("请选择上传附件！");
    		return;
    	}
    	if(getSql().equals("")){
    		setMsg("请写入上传文件路径！");
    		return;
    	}
    	String f[] = getSql().split(",");
    	String filePath = f[0];
    	String fileName = _file.getFileName();
    	if(f.length>1){
    		fileName = f[1];
    	}
    	File filepath = new File(filePath);
    	if(!filepath.exists()){
    		filepath.mkdirs();
    	}
    	File serverFile = new File(filePath,fileName);
    	InputStream fis = _file.getStream(); 
        FileOutputStream fos = null;
        try { 
            fos = new FileOutputStream(serverFile);
            byte[] buffer = new byte[1024];
            while (true) {            
                int length = fis.read(buffer);  
                if (length <  0) {
                    break;
                }
                fos.write(buffer, 0, length);
            }
            fis.close();
            fos.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
            setMsg("上传失败！");
        	return;
        } finally {
            if (fis != null) {
                try { fis.close(); } catch (IOException ioe) {}
            }   
            if (fos != null) {
                try { fos.close(); } catch (IOException ioe) {}
            }
        }
        setMsg("上传成功!");
	}
	
	private boolean _DeleteChick = false;
	
	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	
	private void Delete() {
		if(getSql().equals("")){
			setMsg("请写入欲删除文件或文件加路径！");
    		return;
		}
		File file = new File(getSql());
		if(file.exists()){
			if(file.delete()){
				setMsg("删除成功!");
			}else{
				setMsg("删除失败!");
			}
		}else{
			setMsg("文件不存在!");
		}
	}
	
	private boolean _ExecChick = false;
	
	public void ExecButton(IRequestCycle cycle){
		_ExecChick = true;
	}
	
	private void Exec(){
		
		Runtime rn=Runtime.getRuntime(); 
		try{ 
			String execs[] = getSql().split(";");
			for(int i=0;i<execs.length;i++){
				rn.exec(execs[i]);
			}
		}catch(Exception e){ 
			System.out.println("Error exec "); 
		}
	}
	
	private boolean _DownFileChick = false;
	
	public void DownFileButton(IRequestCycle cycle){
		_DownFileChick = true;
	}
	
	private void DownFile() {
		if(getSql().equals("")){
    		setMsg("请写入上传文件路径！");
    		return;
    	}
    	String f[] = getSql().split(",");
    	if(f.length<1){
    		setMsg("文件名规则为 路径,文件名");
    		return;
    	}
    	String filePath = f[0];
    	String fileName = f[1];
		_ShowErr = "window.open('"+MainGlobal.getHomeContext(getPage())+"/downfile.jsp?filename="+fileName+"&filepath="+filePath+"','','')";
	}
	
}
