/*
 * �������� 2005-11-9
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת�� ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
			setMsg("��ѯSQLΪ��!");
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
					setMsg("��ѯSQL����!");
					return;
				}
				int rows = JDBCcon.getRow(rs);
				if(rows==-1){
					setMsg("��ȡ��¼����ʧ��!");
					return;
				}else
					if(rows==0){
						setMsg("��ѯ��0����¼!");
						return;
					}else{
						setMsg("��ѯ�� "+rows+" ����¼!");
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
							setMsg("��ѯ�ļ�¼������,ϵͳ��ȡǰ100����¼��ʾ!");
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
					setMsg("��ѯʧ�ܣ�");
				}
			}
		}
		if(sqlIndex==sqls.length){
			setMsg("��ѯSQL�ѱ�ע��!");
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
		String msg = "ִ�гɹ�!";
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
						msg = "���в���ȷ��SQL��䣡";
						setSql(getSql().replaceAll(sql, "\r#"+sql+"#"));
						con.rollBack();
						con.Close();
						return;
					}
				}
			}
		}else{
			msg ="ִ��SQLΪ�գ�";
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
		String sql = "select zhi,beiz from xitxxb where duixm='Tomcat��־·��'";
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
			// TODO �Զ����� catch ��
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
    		setMsg("��ѡ���ϴ�������");
    		return;
    	}
    	if(getSql().equals("")){
    		setMsg("��д���ϴ��ļ�·����");
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
            setMsg("�ϴ�ʧ�ܣ�");
        	return;
        } finally {
            if (fis != null) {
                try { fis.close(); } catch (IOException ioe) {}
            }   
            if (fos != null) {
                try { fos.close(); } catch (IOException ioe) {}
            }
        }
        setMsg("�ϴ��ɹ�!");
	}
	
	private boolean _DeleteChick = false;
	
	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	
	private void Delete() {
		if(getSql().equals("")){
			setMsg("��д����ɾ���ļ����ļ���·����");
    		return;
		}
		File file = new File(getSql());
		if(file.exists()){
			if(file.delete()){
				setMsg("ɾ���ɹ�!");
			}else{
				setMsg("ɾ��ʧ��!");
			}
		}else{
			setMsg("�ļ�������!");
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
    		setMsg("��д���ϴ��ļ�·����");
    		return;
    	}
    	String f[] = getSql().split(",");
    	if(f.length<1){
    		setMsg("�ļ�������Ϊ ·��,�ļ���");
    		return;
    	}
    	String filePath = f[0];
    	String fileName = f[1];
		_ShowErr = "window.open('"+MainGlobal.getHomeContext(getPage())+"/downfile.jsp?filename="+fileName+"&filepath="+filePath+"','','')";
	}
	
}
