package com.zhiren.dc.meic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.request.IUploadFile;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;

public class Meic extends BasePage {

	private static int _editTableRow = -1;// 编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}

	public static boolean isNull(String value) {
		if (value == null) {
			return true;
		} else if (value.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		getSelectData();
	}

	private void Insert() {
		List _value = getEditValues();
		if (_value == null) {
			_value = new ArrayList();
		}
		long mxuh = _value.size() + 1;
		_value.add(new Meicbean(mxuh));
		setEditValues(_value);
		getSrcs().add("1211121211");
	}

	private void Delete() {
		// 为 "删除" 按钮添加处理程序
		List _list = ((Visit) getPage().getVisit()).getList1();
		int introw = getEditTableRow();
		// String severPath="";
		// severPath=getsever_path();
		if (sever_path.equals("")) {
			// meg="系统基础信息不能为空!";
			return;
		}
		if (!((Meicbean) _list.get(introw)).getMeict().equals("")) {
			File serverFile_old = new File(sever_path, ((Meicbean) _list
					.get(introw)).getMeict());// 区别
			File serverFile_back = new File(sever_pathback, ((Meicbean) _list
					.get(introw)).getMeict());// 区别
			if (serverFile_old.exists()) {
				serverFile_old.delete();// 删除工作目录中文件
			}
			if (serverFile_back.exists()) {
				serverFile_back.delete();// 删除备份目录中文件
			}
		}

		if (introw != -1) {
			List _value = getEditValues();
			if (_value != null) {
				JDBCcon con = new JDBCcon();
				String sql = " Delete  From meicb Where id="
						+ ((Meicbean) _list.get(introw)).getId();
				con.getDelete(sql);
				_value.remove(introw);
				Refurbish();
			}
		}
	}

	// ID MEICMC MEICQ ZUIDKC BEIZ ZHENGCCB XIANFHKC ZUIDGD ZUIDTJ MEICMJ FUJ
	private String getsever_path(int flag) {// 0:文件真实绝对路径1:相对路径
		// 这个目录相对重要
		// 高度注意服务器目录中一定设置一个存储用户文件的目录
		String path = "";
		// // getPage("");
		String pathall = MainGlobal.getServletPath(this);
//		JDBCcon con = new JDBCcon();
//		String sql = "select zhi from xitxxb where duixm='煤场示意图形'and diancxxb_id="
//				+ ((Visit) getPage().getVisit()).getDiancxxb_id();
//		ResultSet rs = con.getResultSet(sql);
//		try {
//			if (rs.next()) {
				path = "D:";
//					rs.getString("zhi");

//			}
//		} catch (Exception e) {
//
//		}
		if (path != null) {
			path = "/" + path;
		}
		path = "/img/shangcwj" + path;
		if (flag == 1) {
			return path;
		}
		return pathall + path;
	}

	private String getsever_pathback() {
		String path = "";
//		JDBCcon con = new JDBCcon();
//		String sql = "select zhi from xitxxb where duixm='煤场示意图形备份' and rownum=1 and diancxxb_id="
//				+ ((Visit) getPage().getVisit()).getDiancxxb_id();
//		ResultSet rs = con.getResultSet(sql);
//		try {
//			if (rs.next()) {
				path = "D:";
//					rs.getString("zhi");
//			}
//		} catch (Exception e) {
//
//		}
		return path;
	}

	private void Save() {

		if (sever_path.equals("")) {
			// meg="系统基础信息不能为空!";
			return;
		}
		File file = new File(sever_path);
		if (!file.exists()) {
			file.mkdir();
		}

		List _list = ((Visit) getPage().getVisit()).getList1();
		JDBCcon con = new JDBCcon();
		try {
			for (int i = 0; i < _list.size(); i++) {
				long _id = ((Meicbean) _list.get(i)).getId();
				String Sql = "";

				IUploadFile _file = ((Meicbean) _list.get(i)).getFilepath();
				//
				if (_id == 0) {
					// 保存新图片
					// IUploadFile _file = ((Meicbean)
					// _list.get(i)).getFilepath();
					// if(_file.getFileName().equals("")){//不输入图片
					// fuj=
					// }
					InputStream fis = _file.getStream();
					FileOutputStream fos = null;
					File serverFile = new File(sever_path, _file.getFileName());
					try {
						fos = new FileOutputStream(serverFile);
						byte[] buffer = new byte[1024];
						while (true) {
							int length = fis.read(buffer);
							if (length < 0) {
								break;
							}
							fos.write(buffer, 0, length);
						}
						fis.close();
						fos.close();

					} catch (IOException ioe) {
						ioe.printStackTrace();
					} finally {
						if (fis != null) {
							try {
								fis.close();
							} catch (IOException ioe) {
							}
						}
						if (fos != null) {
							try {
								fos.close();
							} catch (IOException ioe) {
							}
						}
					}
					Sql = " insert  into meicb (ID,DIANCXXB_ID,XUH,MINGC,PINY,KUC,CHANGD,KUAND,MIANJ,GAOD,TIJ,MEICT,DIANDML,BEIZ) values("
							+ Long.parseLong(MainGlobal
									.getNewID(((Visit) getPage().getVisit())
											.getDiancxxb_id()))
							+ ","
							+ ((Visit) getPage().getVisit()).getDiancxxb_id()
							+ ","
							+ ((Meicbean) _list.get(i)).getXuh()
							+ ",'"
							+ ((Meicbean) _list.get(i)).getMingc()
							+ "','"
							+ ((Meicbean) _list.get(i)).getPiny()
							+ "',"
							+ ((Meicbean) _list.get(i)).getKuc()
							+ ","
							+ ((Meicbean) _list.get(i)).getChangd()
							+ ","
							+ ((Meicbean) _list.get(i)).getKuand()
							+ ","
							+ ((Meicbean) _list.get(i)).getMianj()
							+ ","
							+ ((Meicbean) _list.get(i)).getGaod()
							+ ","
							+ ((Meicbean) _list.get(i)).getTij()
							+ ",'"
							+ _file.getFileName()
							+ "',"
							+ ((Meicbean) _list.get(i)).getDiandml()
							+ ",'"
							+ ((Meicbean) _list.get(i)).getBeiz() + "')";
					con.getInsert(Sql);
				} else {

					// 更新老图片//
					// IUploadFile _file = ((Meicbean)
					// _list.get(i)).getFilepath();
					String fuj = "";
					fuj = _file.getFileName();
					InputStream fis = _file.getStream();
					FileOutputStream fos = null;
					// if(!((Meicbean)
					// _list.get(i)).getMEICSYTX().equals("")){//更新图片
					// File serverFile_old= new File(sever_path,((Meicbean)
					// _list.get(i)).getMEICSYTX());//区别
					// if(serverFile_old.exists()&&fuj.equals("")){
					// serverFile_old.delete();
					// }
					// }
					if (fuj.equals("")) {// 不更新图片
						fuj = ((Meicbean) _list.get(i)).getMeict();
					} else {// 更新图片
						if (!((Meicbean) _list.get(i)).getMeict().equals("")) {// 更新图片
							File serverFile_old = new File(sever_path,
									((Meicbean) _list.get(i)).getMeict());// 区别
							if (serverFile_old.exists()) {
								serverFile_old.delete();
							}
						}
						File serverFile = new File(sever_path, _file
								.getFileName());
						try {
							fos = new FileOutputStream(serverFile);
							byte[] buffer = new byte[1024];
							while (true) {
								int length = fis.read(buffer);
								if (length < 0) {
									break;
								}
								fos.write(buffer, 0, length);
							}
							fis.close();
							fos.close();

						} catch (IOException ioe) {
							ioe.printStackTrace();
						} finally {
							if (fis != null) {
								try {
									fis.close();
								} catch (IOException ioe) {
								}
							}
							if (fos != null) {
								try {
									fos.close();
								} catch (IOException ioe) {
								}
							}
						}
					}
					Sql = "update meicb\n" + "    set XUH     ="
							+ ((Meicbean) _list.get(i)).getXuh() + ",\n"
							+ "        MINGC   = '"
							+ ((Meicbean) _list.get(i)).getMingc() + "',\n"
							+ "        PINY    = '"
							+ ((Meicbean) _list.get(i)).getPiny() + "',\n"
							+ "        KUC     = "
							+ ((Meicbean) _list.get(i)).getKuc() + ",\n"
							+ "        CHANGD  = "
							+ ((Meicbean) _list.get(i)).getChangd() + ",\n"
							+ "        KUAND   = "
							+ ((Meicbean) _list.get(i)).getKuand() + ",\n"
							+ "        MIANJ   = "
							+ ((Meicbean) _list.get(i)).getMianj() + ",\n"
							+ "        GAOD    = "
							+ ((Meicbean) _list.get(i)).getGaod() + ",\n"
							+ "        TIJ     = "
							+ ((Meicbean) _list.get(i)).getTij() + ",\n"
							+ "        MEICT   = '" + fuj + "',\n"
							+ "        DIANDML = "
							+ ((Meicbean) _list.get(i)).getDiandml() + ",\n"
							+ "        BEIZ    = '"
							+ ((Meicbean) _list.get(i)).getBeiz() + "'\n"
							+ "  where ID =" + _id + "\n"
							+ "    and diancxxb_id ="
							+ ((Visit) getPage().getVisit()).getDiancxxb_id();

					con.getUpdate(Sql);
				}
			}
			// 备份数据
			File file_back = new File(sever_pathback);
			deleteFilesAndDirector(file_back);
			file_back.mkdirs();
			try {
				// FileOutputStream outs=new FileOutputStream(file_back);
				// FileInputStream ins=new FileInputStream(file);
				// 文件夹下的一层拷贝
				byte[] buffer = new byte[1024];
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isFile()) {
						FileInputStream ins = new FileInputStream(files[i]);
						FileOutputStream outs = new FileOutputStream(file_back
								+ "/" + files[i].getName());
						while (ins.read(buffer) != -1) {
							outs.write(buffer);
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			con.Close();
			getSelectData();
		}
	}

	public void deleteFilesAndDirector(File file) {// 删除以file为根的目录树
		File f = file;
		if (f.isDirectory()) {
			File files[] = f.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFilesAndDirector(files[i]);
			}
			f.delete();
		} else {
			f.delete();
		}
	}

	public void copytree(File filesouce, File fileto) {// 先根遍历
		byte[] buffer = new byte[1024];
		File files[] = filesouce.listFiles();
		// fileto.mkdirs();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				File Filse_order = new File(fileto.getParent() + "/"
						+ fileto.getName() + "/" + files[i].getName());
				Filse_order.mkdirs();

				copytree(files[i], Filse_order);
			} else {
				FileInputStream ins = null;
				FileOutputStream outs = null;
				try {

					ins = new FileInputStream(files[i]);
					outs = new FileOutputStream(fileto.getParent() + "/"
							+ fileto.getName() + "/" + files[i].getName());
					while (ins.read(buffer) != -1) {
						outs.write(buffer);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (ins != null) {
						try {
							ins.close();
						} catch (IOException ioe) {
						}
					}
					if (outs != null) {
						try {
							outs.close();
						} catch (IOException ioe) {
						}
					}

				}
			}
		}
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}

	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		setMeg("");
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		if (_InsertChick) {
			_InsertChick = false;
			Insert();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			Delete();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
	}

	private Meicbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Meicbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Meicbean EditValue) {
		_EditValue = EditValue;
	}

	public List getSelectData() {
		List _editvalues = new ArrayList();
		JDBCcon JDBCcon = new JDBCcon();
		try {

			String sql = "select * from meicb where diancxxb_id = "
					+ ((Visit) getPage().getVisit()).getDiancxxb_id();
			ResultSet rs = JDBCcon.getResultSet(sql);
			String path = getsever_path(1);
			getSrcs().clear();
			while (rs.next()) {
				long mid = rs.getLong("ID");
				long mxuh = rs.getLong("xuh");
				String mmingc = rs.getString("MINGC");
				String mpiny = rs.getString("PINY");
				double mkuc = rs.getDouble("KUC");
				double mchangd = rs.getDouble("CHANGD");
				double mkuangd = rs.getDouble("KUANGD");
				double mmianj = rs.getDouble("MIANJ");
				double mgaod = rs.getDouble("GAOD");
				double mtij = rs.getDouble("TIJ");
				String mmeict = rs.getString("MEICT");
				double mdiandml = rs.getDouble("DIANDML");
				String mbeiz = rs.getString("BEIZ");
				if (mmeict == null) {
					getSrcs().add("1211121211");
					mmeict = "";
				} else {
					getSrcs().add(
							this.getEngine().getContextPath() + path + "/"
									+ mmeict);
				}

				_editvalues.add(new Meicbean(mid, mxuh, mmingc, mpiny, mkuc,
						mchangd, mkuangd, mmianj, mgaod, mtij, mmeict,
						mdiandml, mbeiz));
			}

			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Meicbean());
		}
		_editTableRow = -1;
		((Visit) getPage().getVisit()).setList1(_editvalues);
		return ((Visit) getPage().getVisit()).getList1();
	}

	private String sever_path;

	private String sever_pathback;

	private String closecode;

	public String getClosecode() {
		return closecode;
	}

	private String meg = "";// msg

	public String getMeg() {
		return meg;
	}

	protected void initialize() {
		meg = "";
		closecode = "";
	}

	public void setMeg(String meg) {
		this.meg = meg;
	}

	public void setClosecode(String closecode) {
		this.closecode = closecode;
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法

			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			// 获取备份
			// filesouce
			sever_path = getsever_path(0);
			sever_pathback = getsever_pathback();
			File severdir = new File(sever_path);// web服务中的目录
			deleteFilesAndDirector(severdir);// 连带此目录一起删掉
			// if(!severdir.exists()){
			severdir.mkdirs();
			// }
			File backdir = new File(sever_pathback);// 备份目录
			if (!backdir.exists()) {
				backdir.mkdirs();
			}
			copytree(backdir, severdir);// 把dirsouce目录拷贝到dirtom
			Refurbish();
		}
	}

	private List srcs;

	public List getSrcs() {
		if (srcs == null) {
			srcs = new ArrayList();
		}
		return srcs;
	}

	public void setSrcs(List srcs) {
		this.srcs = srcs;
	}

	//
	// private Object srcobj;
	// public Object getSrcobj() {
	// return srcobj;
	// }
	// public void setSrcobj(Object srcobj) {
	// this.srcobj = srcobj;
	// }
	//	
	private String src;

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

}
