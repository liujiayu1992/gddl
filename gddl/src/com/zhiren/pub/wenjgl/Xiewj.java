package com.zhiren.pub.wenjgl;

/*
 * ���ߣ����
 * ʱ�䣺2013-01-25
 * ������ʹ��MDAS�����е�Դ���������BUG 
 */

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.request.IUploadFile;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;

public class Xiewj extends BasePage  {
	private boolean xinjwj = false;//����
	public static final String home="d:/wenjgl/";
	public void xinjwj(IRequestCycle cycle) {
		xinjwj = true;
	}
	private boolean baoc = false;//����
	public void baoc(IRequestCycle cycle) {
		baoc = true;
	}
	private boolean shangc = false;//�ϴ�
	public void shangc(IRequestCycle cycle) {
		shangc = true;
	}
	private boolean shanc = false;//ɾ��
	public void shanc(IRequestCycle cycle) {
		shanc = true;
	}
	private String value;
	public String getValue() {
		return value;
	}
	public void submit(IRequestCycle cycle) {
		if (baoc) {
			baoc = false;
			save();
		}
		if(shangc) {
//			shangc = false;
			shangc(); 
		}
		if(shanc) {
//			shanc = false;
			shanc(); 
		}
		if(xinjwj) {
			xinjwj = false;
			xinjwj(); 
		}
	}
	private void xinjwj(){
		zhut="";
		leix="";
		neir="";
		id="0";
		setFlag("0");
		id_yx="0";
		id="0";
		getfujbSelectModels();
	}
	private void shanc(){
		String cuncm="";//ʵ�ʴ洢����(���)
		JDBCcon con=new JDBCcon();
		String sql=
		"select mingc\n" +
		"from fujb\n" + 
		"where id='"+fujidSelected+"'";
		ResultSet rs=con.getResultSet(sql);
		try{
			while(rs.next()){
				cuncm=home+rs.getString(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//��ɾ���ļ�ϵͳ�е��ļ�file
		File file=new File(cuncm);
		if(file.exists()){
			file.delete();
		}
		//����ɾ���������ݿ�
		 sql=
		"delete\n" +
		"from fujb\n" + 
		"where id='"+fujidSelected+"'";
		con.getDelete(sql);
		con.Close();
		getfujbSelectModels();
	}
	private void shangc(){
		Visit visit = (Visit) getPage().getVisit();
		String cuncm="";//ʵ�ʴ洢����
		IUploadFile uploadf=getFileStream();
		if (uploadf.getFileName() != null&& !uploadf.getFileName().equals("")) {// &&!Filepath.getFileName().endsWith("")
			InputStream is = uploadf.getStream();//�ϴ��ļ�����
			
			
			// Calendar qissj=Calendar.getInstance();
			// qissj.setTime(hetbxx.getQissj());
			
			File path = new File(home+ (new Date().getYear() + 1900));
			if (!path.exists()) {
				path.mkdirs();
			}
			cuncm=(new Date().getYear() + 1900) + "/"+Math.abs(new java.util.Random().nextLong())+"_"+uploadf.getFileName();
			File file = new File(home	+ cuncm);//׺�Ϻ�׺�Է�ֹ����
			// downjspģ��Ҫ���������������ļ�����·�����ļ�ȫ�ƣ�
			
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
				System.out.print("ͼƬ�ϴ�����");
			} finally {
				try {
					os.close();
					is.close();
				} catch (Exception e) {
					
				}
			}
			if(id_yx.equals("0")){//���Ϊ¼����棬���������ļ�������ǰ����һ���ļ���id
				id_yx=MainGlobal.getNewID(visit.getDiancxxb_id());
			}
			
			//���ɸ�����д���ݿ�
//			String url = "http://"
//				+ this.getRequestCycle().getRequestContext()
//						.getServerName()
//				+ ":"
//				+ this.getRequestCycle().getRequestContext()
//						.getServerPort() + "/"
//				+ this.getEngine().getContextPath() + "/downfile.jsp"
//				+ "?filename=" + cuncm + "'||'&'||'filepath="
//				+ home ;
			String url = "http://"
			+ this.getRequestCycle().getRequestContext()
					.getServerName()
			+ ":"
			+ this.getRequestCycle().getRequestContext()
					.getServerPort() + ""
			+ this.getEngine().getContextPath() + "/app?service=page/downfile"
			+ "&filename=" + cuncm + "'||'&'||'filepath="
			+ home ;
			String sql=
						"insert into fujb (id,wenjb_id,mingc,url,yuanmc)values(\n" +
						MainGlobal.getNewID(visit.getDiancxxb_id())+","+id_yx+",'"+
						cuncm+"','"+url+"','"+uploadf.getFileName()+
						"')";
			JDBCcon con=new JDBCcon();
			con.getInsert(sql);
			getfujbSelectModels();
			con.Close();
		}
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
//		if (!visit.getActivePageName().toString().equals(
//				this.getPageName().toString())) {//��ʾ
//			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�0�½�2�޸�1�½�
			visit.setActivePageName(getPageName().toString());
			if(shanc){//ɾ��
				shanc=false;
			}else if(shangc){//�ϴ�
				shangc=false;
			}else{//
				setfujbSelectModel(null);
				if (cycle.getRequestContext().getParameter("wenjb_id")
						!= null&&!cycle.getRequestContext().getParameter("wenjb_id").equals("0")) {//���ӵ���
					if(cycle.getRequestContext().getParameter("flag").equals("2")){
						setFlag("2");
					}else{//������2�����޸�
						setFlag("1");
					}
					JDBCcon con=new JDBCcon();
					String sql=
						"select id,biaot,neir,leix\n" +
						"from wenjb\n" + 
						"where id="+cycle.getRequestContext().getParameter("wenjb_id");
					ResultSet rs=con.getResultSet(sql);
					try{
						if(rs.next()){
							id=rs.getString("id");
							zhut=rs.getString("biaot");
							leix=rs.getString("leix");
							neir=rs.getString("neir");
							id_yx=id;
						}
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						con.Close();
					}
				}else{
					id_yx="0";
					zhut="";
					leix="";
					neir="";
					id="0";
					setFlag("0");
				}
			}
	}
//	 Page����
	protected void initialize() {
		value="";
	}
	private void save(){
		Visit visit = (Visit) getPage().getVisit();
		String sql="";
		JDBCcon con=new JDBCcon();
		if(id.equals("0")){// ����������ļ�idΪ0����˵��û�н��й�������ʱ���������a��û���ϴ�����û�н���Ԥ�ȷ��䣬������idb�������ϴ�����Ԥ�ȷ����id
			if(id_yx.equals("0")){//û�ϴ�����
				id=MainGlobal.getNewID(visit.getDiancxxb_id());
				id_yx=id;
			}else{//�Ѿ��ϴ�����
				id=id_yx;
			}
			
			sql="insert into wenjb(id,diancxxb_id,biaot,neir,leix,shij,reny)values(\n" +
			id+","+visit.getDiancxxb_id()+",'"+zhut+"','"+neir+"','"+leix+"',sysdate,'"+visit.getRenymc()+"'\n" + 
			")";
			con.getInsert(sql);
		}else{
			sql="update wenjb\n" +
			"set biaot='"+zhut+"',\n" + 
			"neir='"+neir+"',\n" + 
			"leix='"+leix+"',\n" + 
			"shij=sysdate,\n" + 
			"reny='"+visit.getRenymc()+"'\n" + 
			"where id="+id;
			con.getUpdate(sql);
		}
		con.Close();
		value="window.opener.document.getElementById('shuax').click();window.opener=null;window.close();";
	}
	private String zhut;
	private String leix;
	private String neir;
	private String id="0";
	private String id_yx="0";//Ԥ��wenjb_id, ֻҪ�ϴ�������Ԥ�ȷ���wenjb_id
	private String flag="0";//���壺0:д�ļ��н���� 1: �ļ��޸��е��õģ��鿴��2: �ļ��޸��е��õģ��޸ģ�
//	private String fujbDate;
	private IUploadFile _fileStream;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLeix() {
		return leix;
	}
	public void setLeix(String leix) {
		this.leix = leix;
	}
	public String getNeir() {
		return neir;
	}
	public void setNeir(String neir) {
		this.neir = neir;
	}
	public String getZhut() {
		return zhut;
	}
	public void setZhut(String zhut) {
		this.zhut = zhut;
	}
	public IUploadFile getFileStream() {
		return _fileStream;
	}
	
	public void setFileStream(IUploadFile uf) {
		_fileStream = uf;
	}

//	public String getFujbDate() {
//		if(fujbDate==null){
//			fujbDate="";
//		}
//		return fujbDate;
//	}
	private String fujmcSelected;
	public String getfujmcSelected() {
		return fujmcSelected;
	}
	public void setfujmcSelected(String fujmcSelected) {
		this.fujmcSelected = fujmcSelected;
	}
	private String fujidSelected;
	public String getfujidSelected() {
		return fujidSelected;
	}
	public void setfujidSelected(String fujidSelected) {
		this.fujidSelected = fujidSelected;
	}
	
//	public void setFujbDate(String fujbDate) {
//		this.fujbDate = fujbDate;
//	}
//	������
    public IDropDownBean getfujbSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean3()==null){
    		 ((Visit) getPage().getVisit()).setDropDownBean3((IDropDownBean)getfujbSelectModel().getOption(0));
    	}
        return  ((Visit) getPage().getVisit()).getDropDownBean3();
    }

    public void setfujbSelectValue(IDropDownBean Value) {
    	((Visit) getPage().getVisit()).setDropDownBean3(Value);
    }


    public void setfujbSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel3(value);
    }

    public IPropertySelectionModel getfujbSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
            getfujbSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel3();
    }
    public void getfujbSelectModels() {
        String sql = 
        	"select id,yuanmc\n" +
        	"from fujb where wenjb_id='"+id_yx+"'";
        ((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql,"")) ;
        return ;
    }
}
