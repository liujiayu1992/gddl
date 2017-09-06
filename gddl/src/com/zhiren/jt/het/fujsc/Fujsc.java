package com.zhiren.jt.het.fujsc;


/*
 * ���ߣ��»���
 * ʱ�䣺2010-12-25
 * ��������ͬ�ļ���Ϊǩ���ļ�������ǩ���ļ����ı���
 */

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import com.zhiren.common.*;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.request.IUploadFile;

import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import org.apache.tools.ant.util.FileUtils;
import org.springframework.util.FileCopyUtils;

/**
 * 
 * @author Ray
 * @since 2011-09-24
 * @discription �����ϴ����ܸİ�
 */
public abstract class Fujsc extends BasePage implements PageValidateListener  {
	public static final String AttrName_LastPage = "PageName";
	public static final String AttrName_AttachmentType = "AttachmentType";
	public static final String AttrName_RecordID = "RecordID";
	private static final String home="c:/wenjgl/";
	private String msg;
	private String Change;
	private IUploadFile _fileStream;
	private boolean isUpLoad;
	private boolean isDelete;
	private boolean isReturn;
	private boolean isSave;
	private boolean isShow;
	
	// ��ʾ��
	
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
	// ��ť�¼�����
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
	
	
	// ��EXT����ҳ��
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
	
//	private boolean canDelete(JDBCcon cn,String strID){
//		//����˻ص���ʼ״̬����ʼ��֯����ɾ��,���ߴ�δ�ύ
//		ResultSetList rs = cn.getResultSetList("select diancxxb_id,zhuangt,jib from hetshzt where hetid="+((Visit) getPage().getVisit()).getString8());
//		Visit visit=(Visit) getPage().getVisit();
//		if (rs.getRows()>0){
//			while (rs.next()){
//				if (rs.getString("jib").equals("1") && rs.getString("diancxxb_id").equals(String.valueOf(visit.getDiancxxb_id()))){
//					if (rs.getString("zhuangt").equals("-1")){
//						return true;
//					}
//				}
//			}
//		}else{
//			return true;
//		}
//		return false;
//	}
	
	private boolean hetIsTuih(JDBCcon cn,String strID){
		//������˻�״̬�������Ӹ���
		ResultSetList rs = cn.getResultSetList("select diancxxb_id,zhuangt,jib from hetshzt where hetid="+((Visit) getPage().getVisit()).getString8());
		Visit visit=(Visit) getPage().getVisit();
		if (rs.getRows()>0){
			while (rs.next()){
				if (rs.getString("diancxxb_id").equals(String.valueOf(visit.getDiancxxb_id()))){
					if (rs.getString("zhuangt").equals("-1")){
						return true;
					}
				}
			}
		}else{
			return true;
		}
		return false;
	}
	
	public void initGrid(){
        JDBCcon con = new JDBCcon();
	    try{

//		boolean Flag = false;	//��˱�־
//		String sql = "SELECT MAX(SHENPR) AS SHENPR FROM SHENPGZB WHERE YEWID = "+((Visit) getPage().getVisit()).getString8()+"";
//		ResultSetList rsl = con.getResultSetList(sql);
//		if(rsl.next()&&(null!=rsl.getString("SHENPR")&&!rsl.getString("SHENPR").equals(""))){
//			//������˻ص���ʼ״̬��Ҳ�������Ӻ�ͬ�ļ�
//
//			Flag = true;
//		}
//
//		if (hetIsTuih (con,((Visit) getPage().getVisit()).getString8())){
//			Flag=false;
//		}


            //�Ƿ�����ɾ������
//		boolean blnCanDelete=canDelete(con,((Visit) getPage().getVisit()).getString8());
            String sql =
                    "SELECT FJ.ID,\n" +
                            "		FJ.HETID,\n" +
                            "       DECODE(FENL, 0, '����ļ�', 1, 'ǩ���ļ�', 2, '�м����ļ�', 'δ֪�ļ�') AS FENL,\n" +
                            "       MINGC,\n" +
                            "       BANB,\n" +
                            "       MIAOS,\n" +
                            "       WENJID,\n" +
                            "       url,\n" +
                            "       nvl(getHetfj_Shenpzt("+((Visit) getPage().getVisit()).getString8()+","
                            +((Visit) getPage().getVisit()).getDiancxxb_id()+"),'') AS SHENPR \n" +
                            "  FROM HETFJB FJ  \n" +
                            " WHERE FJ.HETID = "+((Visit) getPage().getVisit()).getString8()+"\n" +
                            "	ORDER BY FENL desc,BANB";
            ResultSetList rsl = con.getResultSetList(sql);
            ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
            egu.setTableName("HETFJB");
            egu.setWidth(Locale.Grid_DefaultWidth);
            egu.getColumn("ID").setHidden(true);
            egu.getColumn("ID").setEditor(null);
            egu.getColumn("HETID").setHidden(true);
            egu.getColumn("HETID").setEditor(null);
            egu.getColumn("HETID").setDefaultValue(((Visit) getPage().getVisit()).getString8());
//		��������
            egu.getColumn("FENL").setHeader("�ļ�����");
            egu.getColumn("FENL").setEditor(new ComboBox());

            List fenl = new ArrayList();
//		if(Flag){
//
			fenl.add(new IDropDownBean(0, "��ͬ"));
//			fenl.add(new IDropDownBean(2, SysConstant.Hetfjfl_Zhongjs));
//		}else{
//
//			fenl.add(new IDropDownBean(0, SysConstant.Hetfjfl_Shenhwj));
//			fenl.add(new IDropDownBean(1, SysConstant.Hetfjfl_Hetwj));
//			fenl.add(new IDropDownBean(2, SysConstant.Hetfjfl_Zhongjs));
//		}
            egu.getColumn("FENL").setComboEditor(egu.gridId, new IDropDownModel(fenl));
            egu.getColumn("FENL").setReturnId(true);
            egu.getColumn("FENL").setDefaultValue(((IDropDownBean)fenl.get(0)).getValue());
            egu.getColumn("FENL").setWidth(80);

            egu.getColumn("MINGC").setHeader("�ļ�����");
            egu.getColumn("MINGC").setEditor(null);
            egu.getColumn("MINGC").setWidth(120);

            egu.getColumn("BANB").setHeader("�ļ��汾");
            egu.getColumn("BANB").setEditor(null);
            egu.getColumn("BANB").setWidth(60);

            egu.getColumn("MIAOS").setHeader("����˵��");
            egu.getColumn("MIAOS").setWidth(250);

            egu.getColumn("WENJID").setHidden(true);
            egu.getColumn("WENJID").setEditor(null);
            egu.getColumn("WENJID").setDefaultValue("0");

            egu.getColumn("URL").setHidden(true);
            egu.getColumn("URL").setEditor(null);

            egu.getColumn("SHENPR").setHidden(true);
            egu.getColumn("SHENPR").setEditor(null);

            egu.addToolbarButton("��Ӹ���", GridButton.ButtonType_Insert_condition, null, "document.getElementById('FileStream').style.display = ''; this.setDisabled(true);");
            egu.addToolbarButton("�ϴ�������", GridButton.ButtonType_SubmitSel_condition, "UpLoadButton",
                    "  var rec = gridDiv_grid.getSelectionModel().getSelections();	\n" +
                            "  if(rec.length==0){	Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��Ҫ�ϴ��ĸ����ļ�¼!'); return; }else{}\n" +
                            "  if(rec[0].get('ID')==0){	\n" +
                            "  		if(document.getElementById('FileStream').value == ''){\n" +
                            "\n" +
                            "    		Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��Ҫ�ϴ��ĸ���!');\n" +
                            "    		return;\n" +
                            "  		}else{\n" +
                            "\n" +
                            "	 		//�ж��ļ���С	\n" +
                            "	 		var FileMaxSize=10240;	//10M	\n" +
                            "	 		var fso,f,s;	//10M	\n" +
                            "	 		fso = new   ActiveXObject('Scripting.FileSystemObject');	\n" +
                            "	 		f  =  fso.GetFile(document.getElementById('FileStream').value); 	\n" +
                            "	 		if(Math.round(f.Size/1024)> FileMaxSize){	\n"	+
                            "				Ext.MessageBox.alert('��ʾ��Ϣ','��ǰ�ļ�Ϊ'+Math.round(f.Size/1048576)+'M,�벻Ҫ����'+FileMaxSize/1048576+'M���봦������ϴ�.');	\n" +
                            "				return;\n" +
                            "	 		}	\n" +
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
                            "      			Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��Ҫ����ļ�¼!');\n" +
                            "				return;\n" +
                            "\t\t		}\n" +
                            "\t		}	\n" +
                            "	}else{ Ext.MessageBox.alert('��ʾ��Ϣ','������������¼���޷��ظ��ϴ�����!'); return; \n	}	\n"
            );
            egu.addToolbarButton("����", GridButton.ButtonType_SubmitSel_condition, "SaveButton",
                    "var rec = gridDiv_grid.getSelectionModel().getSelections();\n" +
                            "\n" +
                            "  if(rec.length==0){  Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��Ҫ����ĸ����ļ�¼!'); return;\n" +
                            "}\n" +
                            "if(rec[0].get('MINGC')==''){\n" +
                            "\n" +
                            "  Ext.MessageBox.alert('��ʾ��Ϣ','����Ҫ�ϴ�����������<�ϴ�������>��ť!'); return;\n" +
                            "}" +
                            "document.getElementById('zhuangt').value = '1';" +
                            "");
            String delScirpt="";
//		if (!blnCanDelete){
            delScirpt="if(rec.length>0){\n" +
                    "   if(rec[0].get('SHENPR')!=''){\n" +
                    "     Ext.MessageBox.alert('��ʾ��Ϣ','�ú�ͬ�����Ѿ������ɣ�����ɾ��!');\n" +
                    "	  return;	\n" +
                    "   }\n" +
                    "}" ;
//		}

            egu.addToolbarButton("ɾ��", GridButton.ButtonType_SubmitSel_condition, "DeleteButton",
                    "var rec = gridDiv_grid.getSelectionModel().getSelections();\n " +delScirpt+
                            "document.getElementById('zhuangt').value = '1';" +
                            "");

            String FileServerDownloadAddress = "";
            try {
                FileServerDownloadAddress = MainGlobal.getTableCol("fileserverinfo", "url", "danwid = "+((Visit) getPage().getVisit()).getDiancxxb_id());
                if(null!=FileServerDownloadAddress&&!"".equals(FileServerDownloadAddress)){

                    FileServerDownloadAddress = FileServerDownloadAddress.substring(0, FileServerDownloadAddress.length()-16);
                }else{

                    FileServerDownloadAddress = "";
                }
            } catch (Exception e) {
                // TODO �Զ����� catch ��
                e.printStackTrace();
            }
            egu.addToolbarButton("�鿴����", GridButton.ButtonType_SubmitSel_condition, null,
                    "var rec = gridDiv_grid.getSelectionModel().getSelections();\n" +
                            "if(rec.length>0){\n" +
                            "\n" +
                            "  rec[0].get('WENJID');\n" +
                            "  window.open(rec[0].get('URL'),'Fujck');\n" +
                            "}else{ Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��Ҫ�鿴�ĸ���!');}\n" +
                            "return;");
            egu.setGridType(ExtGridUtil.Gridstyle_Edit);
            egu.addOtherScript(
                    "gridDiv_ds.on('add',function(){ gridDiv_grid.getSelectionModel().selectRow(gridDiv_ds.getCount()-1);\n" +
                            "                gridDiv_grid.getView().focusRow(gridDiv_ds.getCount()-1);\n" +
                            "});");
            egu.addOtherScript(
                    "gridDiv_grid.on('beforeedit',function(e){ if(e.record.get('SHENPR')!=''){ e.cancel=true; }});");
            setExtGrid(egu);
        }catch (Exception e){
	        e.printStackTrace();
        }finally {
            con.Close();
        }


		con.Close();
	}
	
	private long UpLoad(JDBCcon con,String OperationID,String Miaos){
        IUploadFile uploadf=getFileStream();
//        String filename=uploadf.getFileName();
		try{

            if (uploadf.getFileName() != null && !uploadf.getFileName().equals("")) {

                String wenjid = "0";
                InputStream in = uploadf.getStream();//�ϴ��ļ�����
//			�õ������ļ�����Ŀ¼
                String homeserveraddress = MainGlobal.getXitsz("�����ļ�����Ŀ¼", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), home);
                File path = new File(homeserveraddress + "/");
                if (!path.exists()) {
                    path.mkdirs();
                }
                String strFileName = uploadf.getFileName();
                FileOutputStream outputStream = new FileOutputStream(homeserveraddress + strFileName);
                File file = new File(path, strFileName);//׺�Ϻ�׺�Է�ֹ����
                file.delete();
                FileCopyUtils.copy(in, outputStream);
            }
        }catch(Exception e){
		    e.printStackTrace();
            setMsg("�ϴ�"+uploadf.getFileName()+"�ļ�ʧ��!");
            return 0;
//				e.printStackTrace();
        }

//			�õ����ŷ�������ڵ�ַ
//			String Fileserveraddress = "";
//			try {
//				Fileserveraddress = MainGlobal.getTableCol("fileserverinfo", "url", "danwid = "+String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()));
//			} catch (Exception e1) {
//				// TODO �Զ����� catch ��
//				e1.printStackTrace();
//			}
//			if(null==Fileserveraddress || "".equals(Fileserveraddress)){
//
//				this.setMsg("û�еõ��ļ��������ӿڵ�ַ,����������Ϣ!");
//				return 0;
//			}
//
//			ByteArrayOutputStream out = new ByteArrayOutputStream(10240);
//			byte[] buff = new byte[10240];
//			int size = 0;
//	        try {
//				while ((size = in.read(buff)) != -1) {
//				    out.write(buff, 0, size);
//				}
//				in.close();
//			} catch (IOException e) {
//				// TODO �Զ����� catch ��
//				this.setMsg("�ϴ�"+uploadf.getFileName()+"�ļ�ʧ��!");
//				e.printStackTrace();
//				return 0;
//			}
//
//			Service service = new Service();
//			Call call;
//			try {
//				call = (Call) service.createCall();
//				call.setTargetEndpointAddress(new java.net.URL(Fileserveraddress));
//				System.out.println("fileserveraddress:"+Fileserveraddress);
//				call.setOperationName("upLoadFile");
//				call.addParameter("AppID", 		 XMLType.SOAP_STRING,ParameterMode.IN);
//				call.addParameter("OperationID", XMLType.SOAP_STRING,ParameterMode.IN);
//				call.addParameter("FileName", 	 XMLType.SOAP_STRING,ParameterMode.IN);
//				call.addParameter("Description", XMLType.SOAP_STRING,ParameterMode.IN);
//				call.addParameter("FileData", 	 XMLType.SOAP_BASE64BINARY,ParameterMode.IN);
//				call.setReturnType(XMLType.SOAP_STRING);
////				wenjid = (String)call.invoke(new Object[] {SysConstant.FileServerAppID_Ranlht,OperationID,uploadf.getFileName(),Miaos,out.toByteArray()});//
//				out.close();
//			} catch (ServiceException e) {
//				// TODO �Զ����� catch ��
//				setMsg("�ϴ�"+uploadf.getFileName()+"�ļ�ʧ��!");
//				e.printStackTrace();
//			}//Զ�̵�����
//			catch (MalformedURLException e) {
//				// TODO �Զ����� catch ��
//				setMsg("�ϴ�"+uploadf.getFileName()+"�ļ�ʧ��!");
//				e.printStackTrace();
//			} catch (RemoteException e) {
//				// TODO �Զ����� catch ��
//				setMsg("�ϴ�"+uploadf.getFileName()+"�ļ�ʧ��!");
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO �Զ����� catch ��
//				setMsg("�ϴ�"+uploadf.getFileName()+"�ļ�ʧ��!");
//				e.printStackTrace();
//			}
//
//			if(null!=wenjid /*&& MainGlobal.isNumeric(wenjid) && Integer.parseInt(wenjid)>0*/){
//
//				return Long.parseLong(wenjid);
//			}else{
//
//				setMsg("�ϴ�"+uploadf.getFileName()+"�ļ�ʧ��!");
//				return 0;
//			}
//		}
		return 1;
	}
	
	private void Delete(){
		
		if (getChange() == null || "".equals(getChange())) {
			setMsg("��ѡ��Ҫɾ���ĸ���!");
			return;
		}

		ResultSetList rs = getExtGrid().getModifyResultSet(getChange());
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		if(rs.next()){
			
			String wenjid = rs.getString("WENJID");
			String sql = "DELETE FROM hetfjb WHERE ID = " + rs.getString("ID");
			int flag = con.getDelete(sql);
			if(flag == -1){
				setMsg("����" + rs.getString("MINGC") + "ɾ��ʧ��!��������!");
				return;
			}else{
                FileUtil.deleteFile(home+rs.getString("MINGC"));
//				�õ����ŷ�������ڵ�ַ
				String Fileserveraddress = "";
//				try {
//					Fileserveraddress = MainGlobal.getTableCol("fileserverinfo", "url", "danwid = "+String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()));
//				} catch (Exception e1) {
//					// TODO �Զ����� catch ��
//					e1.printStackTrace();
//				}
//				if(null==Fileserveraddress || "".equals(Fileserveraddress)){
//
//					this.setMsg("û�еõ��ļ��������ӿڵ�ַ,����������Ϣ!");
//					return;
//				}
//
//				Service service = new Service();
//				Call call;
//				try {
//					call = (Call) service.createCall();
//					call.setTargetEndpointAddress(new java.net.URL(Fileserveraddress));
//					call.setOperationName("delFileForID");
//					call.addParameter("strFileID", 	 XMLType.SOAP_STRING,ParameterMode.IN);
//					call.setReturnType(XMLType.SOAP_STRING);
//					wenjid = (String)call.invoke(new Object[] {wenjid});//
//				} catch (ServiceException e) {
//					// TODO �Զ����� catch ��
//					e.printStackTrace();
//					setMsg("����" + rs.getString("MINGC") + "ɾ��ʧ��!��������!");
//
//				}//Զ�̵�����
//				catch (MalformedURLException e) {
//					// TODO �Զ����� catch ��
//					e.printStackTrace();
//					setMsg("����" + rs.getString("MINGC") + "ɾ��ʧ��!��������!");
//
//				} catch (RemoteException e) {
//					// TODO �Զ����� catch ��
//					e.printStackTrace();
//					setMsg("����" + rs.getString("MINGC") + "ɾ��ʧ��!��������!");
//				} catch (IOException e) {
//					// TODO �Զ����� catch ��
//					e.printStackTrace();
//					setMsg("����" + rs.getString("MINGC") + "ɾ��ʧ��!��������!");
//				}
//
//				if(!wenjid.equals("success")){
//
//					setMsg("����" + rs.getString("MINGC") + "ɾ��ʧ��!��������!");
//					con.rollBack();
//				}else{
					
					setMsg("����" + rs.getString("MINGC") + "ɾ���ɹ�!");
					con.commit();
//				}
			}
		}
		
		rs.close();
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

		JDBCcon con = new JDBCcon();
		boolean Flag = false;
		String strsql = "";
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(this.getChange());
		con.setAutoCommit(false);
		long wenjid = 0;
		String hetfjb_id = "0";
		String hetb_id = "0";
		
		if (mdrsl.next()) {
			String lngFenl="0";
//			if (mdrsl.getString("FENL").equals(SysConstant.Hetfjfl_Hetwj)){
//				lngFenl="1";
//			}else if(mdrsl.getString("FENL").equals(SysConstant.Hetfjfl_Zhongjs)){
//
//				lngFenl="2";
//			}
            String mingc=mdrsl.getString("MINGC");
			if ("0".equals(mdrsl.getString("ID"))) {
				hetfjb_id = MainGlobal.getNewID(con,((Visit) getPage().getVisit()).getDiancxxb_id());
				hetb_id = mdrsl.getString("HETID");
				//---------
                String url=  "http://"+ this.getRequestCycle().getRequestContext().getServerName()+ ":"
                        + this.getRequestCycle().getRequestContext().getServerPort() +
                        this.getEngine().getContextPath() + "/app?service=page/downfile"+ "&filename=" + mingc + "'||'&'||'filepath="+ home ;
                //---------
				sql.append("insert INTO hetfjb(ID, hetid, fenl, mingc, banb, miaos, wenjid,url)")
					.append("VALUES(").append(hetfjb_id).append(",")
					.append(mdrsl.getString("HETID")).append(",").append(lngFenl).append(",'")
//					.append("257416548").append(",").append(lngFenl).append(",'")
					.append(mdrsl.getString("MINGC")).append("','").append(getHetbb(con,mdrsl.getString("HETID"),
                        lngFenl,mdrsl.getString("MINGC"),""))
					.append("','").append(mdrsl.getString("MIAOS")).append("',").append(mdrsl.getString("WENJID")).append("," +
                        "'"+url+"');\n");
				wenjid = UpLoad(con,mdrsl.getString("HETID"),mdrsl.getString("MIAOS"));
				if(wenjid>0){
					sql.append("update hetfjb set wenjid = "+wenjid+" where id = "+hetfjb_id+";	\n");
					strsql = "select * from shenpgzb where yewid = "+wenjid+" and shenpr is not null and zhuangt = 1";
					ResultSetList rec = con.getResultSetList(strsql);
					if(rec.getRows()>0){
						Flag = true;
					}
					rec.close();
				}else{
					this.setMsg("�ļ�����ʧ��!");
					return;
				}
			} else {
				
				sql.append("update ").append(this.getExtGrid().tableName).append(" set ")
					.append("FENL = ").append(lngFenl).append(",")
					.append("MINGC = '").append(mdrsl.getString("MINGC")).append("',")
					.append("BANB = '").append(getHetbb(con,mdrsl.getString("HETID"),lngFenl,mdrsl.getString("MINGC"),mdrsl.getString("BANB"))).append("',")
					.append("MIAOS = '").append(mdrsl.getString("MIAOS")).append("'")				
					.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
			}
		}
		mdrsl.close();
		sql.append("end;");
		if(con.getUpdate(sql.toString())>=0){
			
			this.setMsg("�ļ�����ɹ�!");
			con.commit();
			
//			�õ�ͬ��������fileserver
			if(wenjid>0&&Flag){
				
//				������ϴ��˸�������
				sql.setLength(0);
				String FileServerInfo[] = null;
				String FileServerInfoList_Order = "";	//��Ҫͬ�����ļ��������б��������𼶴���ʱʹ�ã�
				String FileServerInfoList_Order_Desc = "";	//��Ҫͬ�����ļ��������б������𼶴���ʱʹ�ã�
				String ServerInfo[] = null;				// ��Ҫͬ����ҵ�����ݷ�����
				String ParentServerInfo[] = null;		// ��Ҫͬ�����ϼ�ҵ�����ݷ�����
				String yewdw_id = "0";
				int fangx = 0;	// ����ǰ���ķ���-1���ˡ�0�㲥��1ǰ����
				try {
					yewdw_id = MainGlobal.getTableCol("hetb", "diancxxb_id", "id", ((Visit) getPage().getVisit()).getString8());
				} catch (Exception e) {
					// TODO �Զ����� catch ��
					e.printStackTrace();
				}
				
				boolean bol_Hetcdfs_Zhujcd = false;	//�Ƿ�Ϊ�𼶴���
				
//				if("��".equals(MainGlobal.getXitsz(SysConstant.Hetcdfs_zjcd, String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "��"))){
//
//					bol_Hetcdfs_Zhujcd = true;
//				}
//
				if(bol_Hetcdfs_Zhujcd){
//					�𼶴���
					fangx = 1;
					ServerInfo = Fujsc.getNextServerInfo(String.valueOf(yewdw_id),String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()),fangx);
					ParentServerInfo = Fujsc.getNextServerInfo(String.valueOf(yewdw_id),String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()),-1);
//					FileServerInfo = Fujsc.getNextFileServerInfo(String.valueOf(yewdw_id),String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()),fangx);
					FileServerInfoList_Order = Fujsc.getFileServerInfoListByOrder(String.valueOf(yewdw_id),String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), 1);
					FileServerInfoList_Order_Desc = Fujsc.getFileServerInfoListByOrder(String.valueOf(yewdw_id),String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), -1);
					
				}else{
//					�㲥����
					ServerInfo = Fujsc.getServerInfo(String.valueOf(yewdw_id));
					FileServerInfo = Fujsc.getFileServerInfo(String.valueOf(yewdw_id));
				}
				
				sql.setLength(0);
				sql.append("select * from hetfjb where id = ").append(hetfjb_id);
				if(con.getHasIt(sql.toString())){
					
					sql.setLength(0);
					sql.append("begin	\n");
					if(ServerInfo!=null){
						
						for (int i = 0; i < ServerInfo.length; i++) {
								// ������Ϣ
								sql.append("INSERT INTO fuwqtbrwb(ID, danwid, renwmc, shiwid, renwbs, renwsj, renwlx, huanczx, mingllx, minglcs, fuwqmc, chuandfx) VALUES (getnewid("
											+ ((Visit) getPage().getVisit())
													.getDiancxxb_id()
											+ "), "
											+ yewdw_id
											+ ", 'hetfjb', 0, "
											+ hetb_id
											+ ", "
											+ DateUtil.FormatOracleDateTime(new Date())
											+ ", 0, 0, 'xml', '', '"
											+ ServerInfo[i]
											+ "', "+fangx+");	\n");
						}
					}
					
					if(ParentServerInfo!=null){
						
						for (int i = 0; i < ParentServerInfo.length; i++) {
							
								// ������Ϣ
								sql.append("INSERT INTO fuwqtbrwb(ID, danwid, renwmc, shiwid, renwbs, renwsj, renwlx, huanczx, mingllx, minglcs, fuwqmc, chuandfx) VALUES (getnewid("
											+ ((Visit) getPage().getVisit())
													.getDiancxxb_id()
											+ "), "
											+ yewdw_id
											+ ", 'hetfjb', 0, "
											+ hetb_id
											+ ", "
											+ DateUtil.FormatOracleDateTime(new Date())
											+ ", 0, 0, 'xml', '', '"
											+ ParentServerInfo[i]
											+ "', "+fangx+");	\n");
						}
					}
				}
				
				sql.append("end;");
				if(sql.length()>13){
					
					if(con.getInsert(sql.toString())>=0){
						
						con.commit();
					}else{
						
						con.rollBack();
					}
				}
				
				sql.setLength(0);
				sql.append("select wenjid from hetfjb where hetid=").append(((Visit) getPage().getVisit()).getString8());
				ResultSetList rec = con.getResultSetList(sql.toString());
				while(rec.next()){
					
					if(bol_Hetcdfs_Zhujcd){
//						�𼶴���
						
						if(null!=FileServerInfoList_Order && !FileServerInfoList_Order.equals("")){
//							���ϴ���
							Fujsc.addSendFileTask(rec.getString("wenjid"),DateUtil.FormatDateTime(new Date()),FileServerInfoList_Order,String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()));
						}
						
						if(null!=FileServerInfoList_Order_Desc && !FileServerInfoList_Order_Desc.equals("")){
//							���´���
							Fujsc.addSendFileTask(rec.getString("wenjid"),DateUtil.FormatDateTime(new Date()),FileServerInfoList_Order_Desc,String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()));
						}
						
					}else{
//						�㲥����
						
						if(FileServerInfo!=null){
								
							for(int i= 0;i<FileServerInfo.length;i++){
								
								Fujsc.addSendFileTask(rec.getString("wenjid"), DateUtil.FormatDateTime(new Date()), FileServerInfo[i], String.valueOf(((Visit) getPage().getVisit())
										.getDiancxxb_id()));
							}
						}
					}
				}
				rec.close();
			}
		}else{
			
			con.rollBack();
		}
		con.Close();
	}
	
	private String getHetbb(JDBCcon con,String Hetb_id,String Fenl,String wenjm,String Banb){
//		�õ���ͬ�汾��
//		1��������������ļ���ͨ��hetbid��fenl�ҵ� hetfjb ���汾��1
//		2��������޸ĵ��ļ����ж�fenl���ļ����Ƿ���ڣ���������򷵻�ԭ�汾���������fenl�޸İ汾��
		String sql = "",final_banb = "";
		ResultSetList rsl = null;
		if(Banb.equals("")){
//			����
			sql = "select decode(max(banb),null,decode("+Fenl+",0,'V001','F001'),SUBSTR(max(banb), 1, 1) || TRIM(TO_CHAR(TO_NUMBER(SUBSTR(MAX(BANB), 2)) + 1, '000'))) as banb from hetfjb where hetid ="+Hetb_id+" and fenl = "+Fenl+"";
			rsl = con.getResultSetList(sql);
			if(rsl.next()){
				
				final_banb = rsl.getString("banb");
			}
		}else{
//			�޸�
			sql = "select * from hetfjb where hetid ="+Hetb_id+" and fenl = "+Fenl+" and mingc = '"+wenjm+"'";
			rsl = con.getResultSetList(sql);
			if(rsl.next()){
//				����
				final_banb = rsl.getString("banb");
			}else{
//				������
				sql = "select decode(max(banb),null,decode("+Fenl+",0,'V001','F001'),SUBSTR(max(banb), 1, 1) || TRIM(TO_CHAR(TO_NUMBER(SUBSTR(MAX(BANB), 2)) + 1, '000'))) as banb from hetfjb where hetid ="+Hetb_id+" and fenl = "+Fenl+"";
				rsl = con.getResultSetList(sql);
				if(rsl.next()){
					
					final_banb = rsl.getString("banb");
				}
			}
		}
		if(rsl!=null){
			
			rsl.close();
		}
		return final_banb;
	}
	
	// ��ʼ��������ÿ���Զ����ã�����ֻ�Ǹ���ʾ���ʼ��
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	// ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
        String[] p=cycle.getRequestContext().getParameters("yewid");
        System.out.println(p);
		if (cycle.getRequestContext().getParameters("yewid") != null) {
			visit.setString8(cycle.getRequestContext().getParameters("yewid")[0]);
		}
//		�̶���String8 ����ҵ��id
		if (!visit.getActivePageName().equals(this.getPageName())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName());
		}
		initGrid();
	}
	// ��֤�û���������
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
	
	public static long addSendFileTask(String FileId, String TaskTime, String ServerName, String Diancxxb_id){
		
		String renwid = "0";
		
//		�õ����ŷ�������ڵ�ַ
		String Fileserveraddress = "";
		try {
			Fileserveraddress = MainGlobal.getTableCol("fileserverinfo", "url", "danwid = "+Diancxxb_id);
		} catch (Exception e1) {
			// TODO �Զ����� catch ��
			e1.printStackTrace();
		}
		if(null==Fileserveraddress || "".equals(Fileserveraddress)){
			
//			this.setMsg("û�еõ��ļ��������ӿڵ�ַ,����������Ϣ!");
			return 0;
		}
		
			Service service = new Service();
			Call call;
			try {
				call = (Call) service.createCall();
				call.setTargetEndpointAddress(new java.net.URL(Fileserveraddress));
				call.setOperationName("addSendFileTask");
				call.addParameter("FileID", 	 XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("TaskTime",    XMLType.SOAP_STRING,ParameterMode.IN);
				call.addParameter("ServerName",  XMLType.SOAP_STRING,ParameterMode.IN);
				call.setReturnType(XMLType.SOAP_STRING);
				renwid = (String)call.invoke(new Object[] {FileId,TaskTime,ServerName});//
			} catch (ServiceException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}//Զ�̵�����
			catch (MalformedURLException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			} 
		
		if(null!=renwid /*&& MainGlobal.isNumeric(renwid) && Integer.parseInt(renwid)>0*/){
			
			return Long.parseLong(renwid);
		}else{
			
			return 0;
		}
	}
	
	public static String[] getFileServerInfo(String yewdw_id){
		
		String FileServerInfo[] = null;
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer("");
		sb.append("SELECT DANWID, MINGC\n" +
				"  FROM FILESERVERINFO\n" + 
				" WHERE DANWID IN (SELECT ZUZ_ID FROM SHENHZTPZB WHERE DIANCXXB_ID = "+yewdw_id+")\n" + 
				"   AND FILESERVERINFO.MYID = 0\n" + 
				"   AND FILESERVERINFO.TONGB = 1");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.getRows()>0){
			
			FileServerInfo = new String[rsl.getRows()];
			int i = 0;
			while(rsl.next()){
				
				FileServerInfo[i++] = rsl.getString("MINGC");
			}
		}
		rsl.close();
		con.Close();
		return FileServerInfo;
	}
	
	public static String getFileServerMs(String yewdw_id){
		
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer("");
		sb.append("SELECT DANWID, MINGC\n" +
				"  FROM FILESERVERINFO\n" + 
				" WHERE DANWID IN (SELECT ZUZ_ID FROM SHENHZTPZB WHERE DIANCXXB_ID = "+yewdw_id+")\n" + 
				"   AND FILESERVERINFO.MYID = 0\n" + 
				"   AND FILESERVERINFO.TONGB = 1");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.getRows()>0){
			
			sb.setLength(0);
			while(rsl.next()){
				
				sb.append(rsl.getString("MINGC")).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		rsl.close();
		con.Close();
		return sb.toString();
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
	
	public static String getServerMs(String yewdw_id){
		
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
			
			sb.setLength(0);
			while(rsl.next()){
				
				sb.append(rsl.getString("MINGC")).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		rsl.close();
		con.Close();
		return sb.toString();
	}
	
	public static String[] getNextServerInfo(String yewdw_id, String diancxxb_id, int fangx){
//		�õ���һ���ķ�������ַ
		String NextServerInfo[] = null;
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer("");
		int shunxh = 0;
//		�õ���ǰ��λ��ԭҵ�������е�˳���
		sb.append("SELECT SHUNX\n" +
				"  FROM SHENHZTPZB\n" + 
				" WHERE DIANCXXB_ID = "+yewdw_id+"\n" + 
				"   AND ZUZ_ID = ").append(diancxxb_id);
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.next()){
			
			shunxh = rsl.getInt("SHUNX")+fangx;
			
			sb.setLength(0);
			sb.append(
					"SELECT DANWID, MINGC\n" +
					"  FROM SERVERINFO\n" + 
					" WHERE DANWID IN (SELECT ZUZ_ID FROM SHENHZTPZB WHERE DIANCXXB_ID = "+yewdw_id+" AND SHUNX = "+shunxh+")	\n" + 
					"   AND SERVERINFO.MYID = 0	\n" + 
					"   AND SERVERINFO.TONGB = 1	");
			rsl = con.getResultSetList(sb.toString());
			if(rsl.getRows()>0){
				NextServerInfo = new String[rsl.getRows()];
				int i=0;
				while(rsl.next()){
					
					NextServerInfo[i++] = rsl.getString("MINGC");
				}
			}
		}
		
		rsl.close();
		con.Close();
		return NextServerInfo;
	}
	
	public static String[] getNextFileServerInfo(String yewdw_id, String diancxxb_id, int fangx){
//		�õ���һ�����ļ���������ַ
		String NextFileServer[] = null;
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer("");
		int shunxh = 0;
//		�õ���ǰ��λ��ԭҵ�������е�˳���
		sb.append("SELECT SHUNX\n" +
				"  FROM SHENHZTPZB\n" + 
				" WHERE DIANCXXB_ID = "+yewdw_id+"\n" + 
				"   AND ZUZ_ID = ").append(diancxxb_id);
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.next()){
			
			shunxh = rsl.getInt("SHUNX")+fangx;
			
			sb.setLength(0);
			sb.append(
					"SELECT DANWID, MINGC\n" +
					"  FROM FILESERVERINFO\n" + 
					" WHERE DANWID IN (SELECT ZUZ_ID FROM SHENHZTPZB WHERE DIANCXXB_ID = "+yewdw_id+" AND SHUNX = "+shunxh+")	\n" + 
					"   AND FILESERVERINFO.MYID = 0	\n" + 
					"   AND FILESERVERINFO.TONGB = 1	");
			rsl = con.getResultSetList(sb.toString());
			if(rsl.getRows()>0){
				NextFileServer = new String[rsl.getRows()];
				int i = 0;
				while(rsl.next()){
					
					NextFileServer[i++] = rsl.getString("MINGC");
				}
			}
		}
		
		rsl.close();
		con.Close();
		return NextFileServer;
	}
	
	public static String getFileServerInfoListByOrder(String yewdw_id, String diancxxb_id, int fangx){
//		���ݷ���õ� FileServerInfoList �б�,fangx==1����fangx==-1����
		StringBuffer FileServerInfoList = new StringBuffer("");
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer("");
		int shunxh = 0;
//		�õ���ǰ��λ��ԭҵ�������е�˳���
		sb.append("SELECT SHUNX\n" +
				"  FROM SHENHZTPZB\n" + 
				" WHERE DIANCXXB_ID = "+yewdw_id+"\n" + 
				"   AND ZUZ_ID = ").append(diancxxb_id);
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.next()){
			
			shunxh = rsl.getInt("SHUNX");
			
			sb.setLength(0);
			
			if(fangx==1){
//				����
				sb.append(
						"select  * from (SELECT distinct DANWID, MINGC,SHENHZTPZB.SHUNX \n" +
						"  FROM FILESERVERINFO,SHENHZTPZB \n" + 
						" WHERE FILESERVERINFO.DANWID IN (SELECT ZUZ_ID FROM SHENHZTPZB WHERE DIANCXXB_ID = "+yewdw_id+" AND SHUNX > "+shunxh+")	\n" +
						"	AND FILESERVERINFO.DANWID = SHENHZTPZB.ZUZ_ID \n" +
						"	AND SHENHZTPZB.DIANCXXB_ID = "+yewdw_id+" \n" +
						"   AND FILESERVERINFO.MYID = 0	\n" + 
						"   AND FILESERVERINFO.TONGB = 1  )\n"+
						"	ORDER BY SHUNX");
				
			}else if(fangx==-1){
//				����
				sb.append(
						"SELECT DANWID, MINGC\n" +
						"  FROM FILESERVERINFO,SHENHZTPZB \n" + 
						" WHERE FILESERVERINFO.DANWID IN (SELECT ZUZ_ID FROM SHENHZTPZB WHERE DIANCXXB_ID = "+yewdw_id+" AND SHUNX < "+shunxh+")	\n" +
						"	AND FILESERVERINFO.DANWID = SHENHZTPZB.ZUZ_ID \n" +
						"	AND SHENHZTPZB.DIANCXXB_ID = "+yewdw_id+" \n" +
						"   AND FILESERVERINFO.MYID = 0	\n" + 
						"   AND FILESERVERINFO.TONGB = 1 \n"+
						"	ORDER BY SHENHZTPZB.SHUNX DESC");
			}
			
			rsl = con.getResultSetList(sb.toString());
			if(rsl.getRows()>0){
				
				while(rsl.next()){
					
					FileServerInfoList.append(rsl.getString("MINGC")).append(",");
				}
				if(FileServerInfoList.length()>0){
					
					FileServerInfoList.deleteCharAt(FileServerInfoList.length()-1);
				}
			}
		}
		
		rsl.close();
		con.Close();
		return FileServerInfoList.toString();
	}
}