package com.zhiren.jt.het.hetsh;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.tapestry.request.IUploadFile;

import com.zhiren.common.DateUtil;
import com.zhiren.common.FileUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


public class HetFujUpLoad extends BasePage implements PageValidateListener {
	// 界面用户提示
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
	}

	private IUploadFile _fileStream;

	public IUploadFile getFileStream() {
		return _fileStream;
	}

	public void setFileStream(IUploadFile uf) {
		_fileStream = uf;
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public IDropDownBean getFileValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean4() == null) {
			if (getFileModel().getOptionCount() > 0) {
				setFileValue((IDropDownBean) getFileModel().getOption(0));
			} 
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean4();
	}

	public void setFileValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean4(value);
	}

	public IPropertySelectionModel getFileModel() {
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setFileModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(value);
	}

	
	public void initCans(String id){
		JDBCcon con = new JDBCcon();
		String dSql = "SELECT diancxxb_id FROM HETB where id=" +id;
		 ResultSetList Irslgd = con.getResultSetList(dSql);
		 while(Irslgd.next()) {
			 setDcId(Irslgd.getString("diancxxb_id"));
		 }
		 Irslgd.close();
		 con.Close();
	}
	
	public String getDcId() {
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setDcId(String zhi) {
		((Visit) this.getPage().getVisit()).setString5(zhi);
	}
	
	public String getId() {
		return ((Visit) this.getPage().getVisit()).getString4();
	}

	public void setId(String zhi) {
		((Visit) this.getPage().getVisit()).setString4(zhi);
	}
	
	public File getImageFilePath() {
		String Imagelj=MainGlobal.getXitxx_item("合同", "合同附件路径", "0", "D:/zhiren/het");
		return new File(Imagelj + "/" + getDcId() + "/");
	}

	public File getImageTmpPath() {
		return new File(MainGlobal.getWebAbsolutePath().getParentFile()+ "/img/tmp/het/" + getDcId() + "/");
	}

	public void init() {
		JDBCcon con = new JDBCcon();
		String sql = "select mingc, to_char(riq,'yyyymmdd')riq,xuh from tupccb where bianm = '" + getId() + "' and mokmc='HET' order by xuh";
		ResultSetList rsl = con.getResultSetList(sql);
		List fileModel = new ArrayList();
		while (rsl.next()) {
			String srcFileName = getId() + "_"+ rsl.getString("riq") + FileUtil.getSuffix(rsl.getString("mingc"));
			File srcFile = new File(getImageFilePath(), srcFileName);
			try {
				fileModel.add(new IDropDownBean(rsl.getString("xuh"), rsl.getString("mingc")));
				FileUtil.copy(srcFile, getImageTmpPath().getPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		setFileModel(new IDropDownModel(fileModel));
		rsl.close();
	}

	private void Save(IUploadFile uploadf) {
		InputStream is = uploadf.getStream();// 上传文件的流

		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		
		String sql = "select nvl(max(xuh)+1,1) xuh from tupccb where bianm ='"+ getId() + "' and mokmc='HET'";
		ResultSetList rsl = con.getResultSetList(sql);
		int xuh = 1;
		
		Date riq=new Date();
		String fileDate=DateUtil.Formatdate("yyyyMMdd",riq);
		String sqlDate=DateUtil.FormatOracleDateTime(riq);
		
		if (rsl.next()) {
			xuh = rsl.getInt("xuh");
		}
		rsl.close();
		
		String oldFileName = uploadf.getFileName();
		
		sql = "begin \n" +
		"insert into tupccb(id,diancxxb_id,riq,xuh,bianm,mingc,mokmc) " +
		"values(getnewid("+getDcId()+ "),"+ getDcId()+ ","+sqlDate+","
		+ xuh+ ",'"+ getId()+ "','"	+ oldFileName+ "','HET');\n" 
		+"end;";
		con.getUpdate(sql);

		String newFileName = getId() + "_" + fileDate+ FileUtil.getSuffix(oldFileName);

		File file = new File(getImageFilePath(), newFileName);

		BufferedOutputStream os = null;
		byte[] buff = new byte[1024];
		// System.out.print(file);
		try {
			os = new BufferedOutputStream(new FileOutputStream(file));
			while (is.read(buff) != -1) {
				os.write(buff);
			}
			os.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
			con.rollBack();
			setMsg("附件上传出错！");
		} finally {
			con.commit();
			con.Close();
		}
	}

	private void Delete() {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		if (getFileValue() != null && !"".equals(getFileValue().getValue())) {
//			取得文件名称
			String sqlFilename = "select BIANM || '_' || TO_CHAR(RIQ,'yyyymmdd') || SUBSTR(MINGC, INSTR(MINGC, '.', -1, 1)) FILENAME \n" +
								 " from tupccb where bianm ='"+ getId() + "' \n" +
								 "and mokmc='HET' and xuh="+getFileValue().getStrId();
			ResultSetList rsl = con.getResultSetList(sqlFilename);
			String fileName="";
			if (rsl.next()) {
				fileName = rsl.getString("FILENAME");
			}
			rsl.close();
			
			String sql = "begin \n" +
						 "delete from tupccb where bianm='" + getId()+ "' and mokmc='HET' and xuh="+getFileValue().getStrId()+";\n"+
						 "end;";
			con.getUpdate(sql);
			try {
				File file = new File(getImageFilePath(), fileName);
				file.delete();
				File tmpfile = new File(getImageTmpPath(), fileName);
				tmpfile.delete();
			} catch (Exception e) {
				e.printStackTrace();
				con.rollBack();
				setMsg("附件删除出错！");
			} finally {
				con.commit();
				con.Close();
			}
			
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			IUploadFile uploadf = getFileStream();
			if (uploadf.getFileName() == null || uploadf.getFileName().equals("")) {
				setMsg("请选择要上传的文件!");
				return;
			}
			Delete();
			Save(uploadf);
			init();
		}
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
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);

			visit.setString3(null);
			visit.setString4(null);
			visit.setString5(null);
			String id = cycle.getRequestContext().getParameter("id");
			if (id != null) {
				setId(id);
				initCans(id);
			}
			
			File tmp = getImageTmpPath();
			File pth = getImageFilePath();
			if (!tmp.exists()) {
				tmp.mkdirs();
			}
			if (!pth.exists()) {
				pth.mkdirs();
			}
			init();
		}
	}

}