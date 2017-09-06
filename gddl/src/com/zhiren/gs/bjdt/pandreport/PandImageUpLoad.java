package com.zhiren.gs.bjdt.pandreport;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.tapestry.request.IUploadFile;

import com.zhiren.common.FileUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 修改人：ww
 * 修改时间：2010-09-17
 * 修改内容：添加处理国电格式盘点查询电厂id查询
 */

public class PandImageUpLoad extends BasePage implements PageValidateListener {
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

	public String getMk() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setMk(String leix) {
		((Visit) this.getPage().getVisit()).setString1(leix);
	}

	public String getId() {
		return ((Visit) this.getPage().getVisit()).getString2();
	}

	public void setId(String leix) {
		((Visit) this.getPage().getVisit()).setString2(leix);
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
			} else {
				// setFileValue(new IDropDownBean());
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


	public String getdiancxxbId(){
		String diancxxbId = "";
		String dSql = "";

		JDBCcon con = new JDBCcon();
		try {
			dSql = "select p.diancxxb_id from meicdxcsb pt,pand_gd p where pt.pand_gd_id = p.id and pt.id ="+getId();
			ResultSetList Irsl = con.getResultSetList(dSql);
			if (Irsl.next()) {
				diancxxbId = Irsl.getString("diancxxb_id");
			}
			//2010-09-16 ww 添加处理国电盘点查询电厂id查询程序
			else {
				dSql = "SELECT p.diancxxb_id FROM pand_gd p,  MEICDXCSB mc WHERE p.id=mc.pand_gd_id AND mc.id=" +getId();
				ResultSetList Irslgd = con.getResultSetList(dSql);
				if (Irslgd.next()) {
					diancxxbId = Irslgd.getString("diancxxb_id");
				}
				Irslgd.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return diancxxbId;
	}

	public String getImagextlj() {
		String Imagelj = "D:/zhiren";
		String iSql = "";
		JDBCcon con = new JDBCcon();
		try {
			iSql = "select zhi from xitxxb where mingc='盘煤图路径' and zhuangt=1";
			ResultSetList Irsl = con.getResultSetList(iSql);
			if (Irsl.next()) {
				Imagelj = Irsl.getString("zhi");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return Imagelj;
	}

	public File getImageFilePath() {
		return new File(MainGlobal.getXitsz(null,
				""+ ((Visit) getPage().getVisit()).getDiancxxb_id(),
				getImagextlj() + "/" + getMk() + "/" + getdiancxxbId()));
	}

	public File getImageTmpPath() {
		return new File(MainGlobal.getWebAbsolutePath().getParentFile()
				+ "/img/tmp/" + getMk() + "/" + getdiancxxbId() + "/");
	}

	public String getImagePath() {
		String imgPath = "#";
		if (getFileValue() != null && getFileValue().getStrId() != null
				&& !"".equals(getFileValue().getStrId())) {
			imgPath = "img/tmp/" + getMk() + "/" +getdiancxxbId() + "/" + getId() + "_"
					+ getFileValue().getStrId()
					+ FileUtil.getSuffix(getFileValue().getValue());
		}
		return imgPath;// "imgs/login/spacer.gif";
	}

	public void init() {
		JDBCcon con = new JDBCcon();
		String sql = "select * from tupccb where bianm = '" + getId() + "'";
		ResultSetList rsl = con.getResultSetList(sql);
		List fileModel = new ArrayList();
		while (rsl.next()) {
			String srcFileName = getId() + "_" + rsl.getInt("xuh")
					+ FileUtil.getSuffix(rsl.getString("mingc"));
			File srcFile = new File(getImageFilePath(), srcFileName);
			try {
				fileModel.add(new IDropDownBean(rsl.getString("xuh"), rsl
						.getString("mingc")));
				FileUtil.copy(srcFile, getImageTmpPath().getPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		setFileModel(new IDropDownModel(fileModel));
		rsl.close();
	}

	private void Save() {
		Visit visit = (Visit) getPage().getVisit();
		IUploadFile uploadf = getFileStream();
		if (uploadf.getFileName() == null || uploadf.getFileName().equals("")) {
			setMsg("请选择要上传的图片!");
			return;
		}
		InputStream is = uploadf.getStream();// 上传文件的流

		JDBCcon con = new JDBCcon();
		String sql = "select nvl(max(xuh)+1,1) xuh from tupccb where bianm ='"
				+ getId() + "'";
		ResultSetList rsl = con.getResultSetList(sql);
		int xuh = 1;
		if (rsl.next()) {
			xuh = rsl.getInt("xuh");
		}
		rsl.close();
		String oldFileName = uploadf.getFileName();
		String newFileName = getId() + "_" + xuh
				+ FileUtil.getSuffix(oldFileName);

		File file = new File(getImageFilePath(), newFileName);

		BufferedOutputStream os = null;
		byte[] buff = new byte[1024];
		// System.out.print(file);
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
				sql = "insert into tupccb(id,diancxxb_id,riq,xuh,bianm,mingc,mokmc) values("
						+ "getnewid("
						+ visit.getDiancxxb_id()
						+ "),'"
						+ visit.getDiancxxb_id()
						+ "',sysdate,"
						+ xuh
						+ ",'"
						+ getId()
						+ "','"
						+ oldFileName
						+ "','"
						+ getMk()
						+ "')";
				con.getInsert(sql);
			} catch (Exception e) {

			}
		}
	}

	private void Delete() {
		JDBCcon con = new JDBCcon();

		if (getFileValue() != null && !"".equals(getFileValue().getValue())) {
			String sql = "delete from tupccb where bianm='" + getId()
					+ "' and xuh=" + getFileValue().getStrId();
			con.getDelete(sql);
			String fileName = getId() + "_" + getFileValue().getStrId()
					+ FileUtil.getSuffix(getFileValue().getValue());
			File file = new File(getImageFilePath(), fileName);
			file.delete();
			File tmpfile = new File(getImageTmpPath(), fileName);
			tmpfile.delete();
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _ShowChick = false;

	public void ShowButton(IRequestCycle cycle) {
		_ShowChick = true;
	}

	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_ShowChick) {
			_ShowChick = false;
			// Save();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			init();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			Delete();
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
		String mk = cycle.getRequestContext().getParameter("mk");
		if (mk != null) {
			setMk(mk);
			init();
		}
		String id = cycle.getRequestContext().getParameter("id");
		if (id != null) {
			setId(id);
			init();
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			File tmp = getImageFilePath();
			if (!tmp.exists()) {
				tmp.mkdirs();
			}
			init();
		}
	}

}