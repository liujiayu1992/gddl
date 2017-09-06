package com.zhiren.gs.bjdt.diaoygl;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import org.apache.tapestry.form.IPropertySelectionModel;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.filejx.FileJx;
import com.zhiren.common.filejx.FilePathRead;
import com.zhiren.gs.bjdt.monthreport.CreateDataFormat;

public class ReaddcDayFile {

	public void readFileDayData() {
		// 收数处理程序
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSet rs;
		String leix = "HC";
		 ArrayList strbf=new ArrayList();
		 ArrayList strbf2=new ArrayList();
		 CreateDataFormat cd = new CreateDataFormat();
		 FileJx wjjx=new FileJx();
		 try{ 
			 String shangblj = cd.getFilepath("日报文件上报路径");
			 String benflj = cd.getFilepath("日报文件备份路径");				 
			 
			 FilePathRead jx=new FilePathRead(leix,shangblj);
			 strbf=jx.getTxtFileList();//得到文件列表
			 int result = -1;
			 
			 for(int i=0;i<strbf.size();i++){
				 strbf2=wjjx.TextJx(strbf.get(i).toString());//一个文件
				 String fileName= strbf.get(i).toString().substring(strbf.get(i).toString().lastIndexOf("\\")+1);
				 String shangcsj = wjjx.getWenjrq(strbf.get(i).toString());
				 
				 
				 String cksql = "select distinct diancscwjm,to_char(diancscsj,'yyyy-mm-dd HH24:mi:ss') as diancscsj from shouhcrbb rb where rb.diancscwjm='"+fileName+"'";
				 rs = con.getResultSet(cksql);
				 if(rs.next()){
					 if(rs.getString("diancscsj").equals(shangcsj)){//已经接收过数据并且文件没有修改
						 continue;
					 }else{//已经接收数据，但文件做了修改
						 
					 }
				 }else{
				 
					 String value[] = new String[]{}; 
					 value = cd.getData(strbf2.get(0).toString(), leix);
//					 for(int r=0;r<value.length;r++){
						 if(leix.equals("HC")){
							 int danwjb = 0;//单位级别
							 String gssql = "select d.jib from diancxxb d where d.bianm='"+value[1]+"'";
							 rs = con.getResultSet(gssql);
							 if(rs.next()){
								 danwjb = rs.getInt("jib");
							 }
							 if(danwjb==3){
								 long zuorkc = 0;//昨日库存
								 String ssql = "select kuc from shouhcrbb rb where rb.diancxxb_id="+getProperId(getIDiancbmModel(),value[1],-1)+" and rb.riq=to_date('"+value[0]+"','yyyy-mm-dd')-1";
								 rs = con.getResultSet(ssql);
								 while(rs.next()){
									 zuorkc = rs.getLong("kuc");
								 }
								 String sql = "insert into shouhcrbb(id, riq, diancxxb_id, dangrgm, dangrfdl, haoyqkdr, kuc, diancscsj, beiz, tiaozl, shangbkc, diancscwjm) values (" 
									 		+"getnewid("+getProperId(getIDiancbmModel(),value[1],-1)+"),to_date('"+value[0]+"','yyyy-mm-dd'),"+getProperId(getIDiancbmModel(),value[1],-1)+", 0, "+value[2]+","+value[4]+","
									 		+value[6]+","
									 		+"to_date('"+shangcsj+"','yyyy-mm-dd HH24:mi:ss'),'',0,0,'"+fileName+"')";
								 result = con.getInsert(sql);
								 
								 if(result<0){
									 con.rollBack();
									 System.out.println(fileName+"数据插入失败!"+DateUtil.FormatDateTime(new Date()));
									 break;
								 }
							 }
//						 }
					 }
					 con.commit();
				 }
				 
				 if(result>=0 || strbf2.size()==0){
					 File fl=new File(strbf.get(i).toString());
					 File f2=new File(benflj+"/"+strbf.get(i).toString().substring(strbf.get(i).toString().lastIndexOf("\\")+1));
					 if(f2.exists()){
						 f2.delete();
						 fl.renameTo(new File(benflj,strbf.get(i).toString().substring(strbf.get(i).toString().lastIndexOf("\\")+1)));
						
					 }else{
						 fl.renameTo(new File(benflj,strbf.get(i).toString().substring(strbf.get(i).toString().lastIndexOf("\\")+1)));
					 }
					 fl.delete();
				 }
			 }
		 }catch(Exception e){
			 con.rollBack();
	 		 e.printStackTrace();
		 }finally{
			 con.Close();
		 }
	}
	
//	电厂编码
	public boolean _diancbmchange = false;
	private IDropDownBean _DiancbmValue;

	public IDropDownBean getDiancbmValue() {
		if(_DiancbmValue==null){
			_DiancbmValue=(IDropDownBean)getIDiancbmModels().getOption(0);
		}
		return _DiancbmValue;
	}

	public void setDiancbmValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancbmValue != null) {
			id = _DiancbmValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancbmchange = true;
			} else {
				_diancbmchange = false;
			}
		}
		_DiancbmValue = Value;
	}

	private IPropertySelectionModel _IDiancbmModel;

	public void setIDiancbmModel(IPropertySelectionModel value) {
		_IDiancbmModel = value;
	}

	public IPropertySelectionModel getIDiancbmModel() {
		if (_IDiancbmModel == null) {
			getIDiancbmModels();
		}
		return _IDiancbmModel;
	}

	public IPropertySelectionModel getIDiancbmModels() {
		
		String sql="";
		
		sql = "select d.id,d.bianm from diancxxb d order by d.mingc";
		
		_IDiancbmModel = new IDropDownModel(sql);
		return _IDiancbmModel;
	}
	
	private long getProperId(IPropertySelectionModel _selectModel, String value,long diancID) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();
		
		if(value.equals("111311")&&diancID==211){
			value="111306";
		}
		
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
					value)) {
				return ((IDropDownBean) _selectModel.getOption(i)).getId();
			}
		}
		return -1;
	}
	
}
