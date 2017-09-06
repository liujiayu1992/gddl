package com.zhiren.common.filejx;

/* 
 * 创建日期 2009-1-5
 * 大唐下属电厂月报文件上传状态
 * 
 */

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.form.IPropertySelectionModel;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;

/**
 * @author Administrator
 * 
 * TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class FileDropDownModel implements IPropertySelectionModel, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7682850099219428033L;

    private List DropDownList;

    // private String[] options;

    public FileDropDownModel(List dropdownlist) {
        this.DropDownList = dropdownlist;
    }

    public FileDropDownModel(String[] options) {
        // this.options = options;
    }

    public FileDropDownModel(String sql,String filename,String filepath,String date) {
        
    	JDBCcon con = new JDBCcon();
    	
        ArrayList strbf=new ArrayList();
        FileJx wjjx=new FileJx();
        String name=filename;
        if(filename.indexOf("rbmdj")!=-1){
        	name=filename.replaceAll("-", "");
        }
		FilePathRead jx=new FilePathRead(name,filepath,true);
		strbf=jx.getTxtFileList();//得到文件列表
		
//		wjjx.getWenjrq(filename);
		List dropdownlist = new ArrayList();
        dropdownlist.add(new IDropDownBean(-1,"全部"));
        
        try {
            ResultSetList rs = con.getResultSetList(sql);
            while (rs.next()) {
                int id = rs.getInt(0);
                String mc = rs.getString(1);
                String bm = rs.getString(2);
                
                String value = mc;
                String zhuangt = "未上传";
                String biaoz = "X ";

                for(int i=0;i<strbf.size();i++){
                	
                	String file = jx.getTxtFileList().get(i).toString();
                	String bianm = file.substring(file.lastIndexOf("\\")+4, file.lastIndexOf("\\")+8);
                	
                	String bmsx = bm.substring(0, 1)+bm.substring(3);
                	
    				String table="";
    				String where = "";
    				
					if(bianm.equals(bmsx)){
						  
						if(filename.substring(0, 1).equals("1")){
    					
    					table = "diaor01bb";
    					where = "fenx='本月' and tb.riq";
        				}else if(filename.substring(0, 1).equals("3")){
        					
        					table = "diaor03bb";
        					where = "fenx='本月' and tb.riq";
        				}else if(filename.substring(0, 1).equals("4")){
        					
        					table = "diaor04bb";
        					where = "fenx='本月' and tb.riq";
        				}else if(filename.substring(0, 1).equals("8")){
        					
        					table = "diaor08bb";
        					where = "fenx='本月' and tb.riq";
        					
        				}else if(filename.substring(0, 1).equals("0")){
        					
        					table = "diaor08bb_new";
        					where = "fenx='本月' and tb.riq";
        					
        				}else if(filename.substring(0, 1).equals("6")){
        					
        					table = "diaor16bb";
        					where = "fenx='本月' and tb.riq";
        				}else if(filename.substring(0, 1).equals("z")){
        					
        					table = "zhibwcqkyb";
        					where = "fenx='本月' and tb.riq";
        				}
						
					}else if(filename.indexOf("rbmdj") != -1){
        					
    					table = "ribmdjb";
    					where = "tb.riq";
					}else{
						continue;
					}
					
    				String riq = wjjx.getWenjrq(file);
    				String ztsql = "select to_char(tb.diancscsj,'yyyy-mm-dd HH24:mi:ss') as diancscsj from "+table+" tb,diancxxb dc "
    							 + " where tb.diancxxb_id=dc.id and dc.bianm='"+bm+"' and "+where+"=to_date('"+date+"','yyyy-mm-dd')";
    				
    				ResultSet ztrs = con.getResultSet(ztsql);
    				if(ztrs.next()){
    					String diancscsj = ztrs.getString("diancscsj");
    					
    					if(diancscsj==null){
    						if(DateUtil.getYear(DateUtil.getDateTime(riq))==DateUtil.getYear(new Date())){
    							zhuangt="已上传";
    							biaoz = "O ";
    						}else{
    							zhuangt="未上传";
    							biaoz = "X ";
    						}
    						
    					}else if(!diancscsj.equals(riq)){
    						zhuangt="待更新";
    						biaoz = "Q ";
    					}else{
    						zhuangt="已接收";
    						biaoz = "Y ";
    					}
    				}else if(filename.indexOf("rbmdj")!=-1){
    					if(filename.substring(5).equals(date.substring(0,10))){
    						zhuangt="已上传";
    						biaoz = "O ";
    					}else{
    						zhuangt="未上传";
    						biaoz = "X ";
    					}
					}else{
						zhuangt="已上传";
						biaoz = "O ";
					}
    			}
//        		}

                value = biaoz+" "+mc+"	"+zhuangt;
                dropdownlist.add(new IDropDownBean(id, value));
            }
//            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        this.DropDownList = dropdownlist;
    }
    
    public FileDropDownModel(String sql,boolean hasSelect) {
        List dropdownlist = new ArrayList();
        if(hasSelect){
        	dropdownlist.add(new IDropDownBean(-1,"请选择"));
        }
        //dropdownlist.add(new IDropDownBean(-1,"请选择"));
        JDBCcon con = new JDBCcon();
        try {
            ResultSet rs = con.getResultSet(sql);
            while (rs.next()) {
                int id = rs.getInt(1);
                String mc = rs.getString(2);
                dropdownlist.add(new IDropDownBean(id, mc));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        this.DropDownList = dropdownlist;
    }

    public FileDropDownModel(List title, String sql) {
        List dropdownlist = new ArrayList();
        dropdownlist.addAll(title);
        JDBCcon con = new JDBCcon();
        try {
            ResultSet rs = con.getResultSet(sql);
            while (rs.next()) {
                int id = rs.getInt(1);
                String mc = rs.getString(2);
                dropdownlist.add(new IDropDownBean(id, mc));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        this.DropDownList = dropdownlist;
    }

    public int getOptionCount() {
        return DropDownList.size();
    }

    public Object getOption(int index) {
        return (IDropDownBean) DropDownList.get(index);
    }

    public String getLabel(int index) {
        return ((IDropDownBean) DropDownList.get(index)).getValue();
    }

    public String getValue(int index) {
        return String
                .valueOf(((IDropDownBean) DropDownList.get(index)).getId());
    }

    public Object translateValue(String value) {
        int i = 0;
        for (; i < DropDownList.size(); i++) {
            if (value.equalsIgnoreCase(String
                    .valueOf(((IDropDownBean) DropDownList.get(i)).getId())))
                break;
        }
        return getOption(i);
    }

}
