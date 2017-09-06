package com.zhiren.jt.jingjhdfx;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IEngine;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.filejx.FileJx;
import com.zhiren.common.filejx.FilePathRead;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;

public class Jingjhdfx extends BasePage {
	
	private String _msg;
	protected void initialize() {
		_msg = "";
	}
	public void setMsg(String _value) {
		_msg = _value;
	}
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	private int mintLeix =0;
		
	private boolean _Riqbchange =false;
    private   Date  _RiqbValue=new Date();
    public Date getRiqb() {
        if (_RiqbValue.equals(null)){
            _RiqbValue = new Date();
            _Riqbchange =true;
        }
        return _RiqbValue;
    }
    public void setRiqb(Date _value) {
        if(_RiqbValue.equals(_value)){
            _Riqbchange =false;
        }else{
            _RiqbValue =_value;
            _Riqbchange =true;
        }
    }
    private boolean _Riqechange =false;
    private   Date  _RiqeValue=new Date();
    public Date getRiqe() {
        if (_RiqeValue.equals(null)){
            _RiqeValue = new Date();
            _Riqechange =true;
        }
        return _RiqeValue;
    }
    public void setRiqe(Date _value) {
        if(_RiqeValue.equals(_value)){
            _Riqechange =false;
        }else{
            _RiqeValue =_value;
            _Riqechange =true;
        }
    }
	public boolean getRaw() {
		return true;
	}
	public String getcontext(){
		return "http://"+this.getRequestCycle().getRequestContext().getServerName()+":"+this.getRequestCycle().getRequestContext().getServerPort() + this.getEngine().getContextPath();
	}
	
	 protected String getpageLink(String Pagename){
    	IRequestCycle cycle =this.getRequestCycle();
        if (cycle.isRewinding())
            return "";
        IEngine engine = cycle.getEngine();
        IEngineService pageService = engine.getService(Tapestry.PAGE_SERVICE);
        ILink link = pageService.getLink(cycle, this, new String[] { Pagename });
        return link.getURL();
    }
	 
	 private boolean _RefurbishChick = false;

     public void RefurbishButton(IRequestCycle cycle) {
         _RefurbishChick = true;
     }
     private boolean _CreateChick = false;

     public void CreateButton(IRequestCycle cycle) {
    	 _CreateChick = true;
     }
     private boolean _SavezdkChick = false;

     public void SavezdkButton(IRequestCycle cycle) {
    	 _SavezdkChick = true;
     }
	 public void submit(IRequestCycle cycle) {
	        if (_RefurbishChick) {
	            _RefurbishChick = false;
	            getShow();
	        }
	        if(_CreateChick){
	        	_CreateChick = false;
	        	Create();
	        }
	        if(_SavezdkChick){
	        	_SavezdkChick = false;
	        	Save();
	        }
	 }
	 public SortMode getSort() {
		return SortMode.USER;
	}
	 
	 public void Create(){
		 Runtime rn=Runtime.getRuntime();
		 Process p=null;
		 String riq = getLastMonths(DateUtil.Formatdate("yyyy-MM-dd",getRiqb()));
		 try{
//			 System.out.println("C:\\jingjhdfx\\Analy.exe "+riq+" ");
		//	 p=rn.exec("C:\\jingjhdfx\\Analy.exe "+riq+" ");
//			 InputStream stderr = p.getErrorStream();
//			 InputStreamReader isr = new InputStreamReader(stderr);
//			 
//			 BufferedReader br = new BufferedReader(isr);
//			 String line = null;
//			 while ( (line = br.readLine()) != null)
//			 System.out.println(line);
//
//			 int exitVal = p.waitFor();
//			 System.out.println("Process exitValue: " + exitVal);
//			 
		 }catch(Exception e){
			 System.out.println("Error exec TTPlayer !");
		 }finally{
		 }

	 }
	 public String getLastMonths(String riq){
		 String date="";
		 String months="";
		 String year="";
		 year = riq.substring(0,4);
		 months = riq.substring(5,7);
		 if(months.equals("01")||months.equals("1")){
			 months="12";
			 year = String.valueOf((Integer.parseInt(year)-1));
		 }else{
			 months = String.valueOf((Integer.parseInt(months)-1));
		 }
		 date = year+"-"+months+"-"+"01";
		 return date;
	 }
	 public String getPageHome() {
	 		return "";
	 	}

	 
	 public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		 Visit visit = (Visit) getPage().getVisit();
		 if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			 visit.setActivePageName(getPageName().toString());
//			 setKuangcModels();
//			 setKuangcSelected(null);
		 }
	 } 
	public String getShow() {
		//调整通知
		return getTable(mintLeix);
	}
	
	public String getFilepath(String str){
//		JDBCcon con=new JDBCcon();
//		String filepath="";
//		 try{
//			 String pathsql = "select zhi from xitxxb where duixm='"+str+"'";
//			 ResultSet rspath = con.getResultSet(pathsql);
//			 if(rspath.next()){
//				 filepath = rspath.getString("zhi");
//			 }
//			 rspath.close();
//		 }catch(Exception e){
//			 e.printStackTrace();
//		 }finally{
//			 con.Close();
//		 }
		return "D:\\Tomcat 5.0\\webapps\\ftp\\jingjhdfx";
	}
	
	private String getTable(int intLeix){
		StringBuffer sb=new StringBuffer();
		String Date = DateUtil.Formatdate("yyyy年MM月",getRiqb());
		String NameYear = Date.substring(0,4);
		String NameMonths = Date.substring(5,7);
		if(NameMonths.equals("01")||NameMonths.equals("1")){
			NameMonths="12";
			NameYear = String.valueOf(Integer.parseInt(NameYear)-1);
		}else{
			NameMonths = String.valueOf(Integer.parseInt(Date.substring(5,7))-1);
		}
		if(NameMonths.length()==1){
			NameMonths = "0"+NameMonths;
		}
		String FileName = NameYear+NameMonths+"JJHDFX";
		int i =1;
		int n =1;

		ArrayList strbf=new ArrayList();
//		ArrayList strbf2=new ArrayList();
		FileJx wjjx=new FileJx();
		String luj = getFilepath("经济活动分析生成路径");
		
		FilePathRead jx=new FilePathRead(FileName,luj);
		strbf=jx.getTxtFileList();//得到文件列表
		boolean bColor = false;

		sb.append("<tr style='HEIGHT: 25px;wdith:720px; BACKGROUND-COLOR: #efefef'>");
		sb.append("<td align=left>序号</td><td align=center>名称</td><td align=center>附件</td><td align=center>时间</td></tr>");
		
		
		for(int j=0;j<strbf.size();j++){
//			 strbf2=wjjx.TextJx(strbf.get(j).toString());//一个文件
			 String shangcsj = wjjx.getWenjrq(strbf.get(j).toString());
			 String filename = strbf.get(j).toString().substring(strbf.get(j).toString().lastIndexOf("\\")+1);

				sb.append("<tr><td>" +  i++  + ("、</td><td><a>"));

				sb.append(filename+"</td><td>");
				
				if (bColor){
					sb.append("</font>");
				}
				sb.append("</a>");

				int m =1;

				if (filename!=null && !"".equals(filename)){
					sb.append(" &nbsp;"+ m++ +":<a href='"+getcontext()+"/downfile.jsp"+"?filename="+filename+"&filepath="+luj+"' target='_blank'>"+filename+"</a>");
				}
				
				sb.append("</td><td>");
				sb.append("(" + shangcsj + ")");
//				if (shangcsj< Days) {
//					sb.append("<img src='"+ getcontext()+"/img/pop/new.gif'" +">");
//				}
				sb.append("</td></tr>") ;
			
		}
		return  sb.toString();
	}
	
	private IPropertySelectionModel _KuangcModel;
    public void setKuangcModel(IPropertySelectionModel value){
    	_KuangcModel = value;
    }
    public IPropertySelectionModel getKuangcModel()
    {
    	if(_KuangcModel == null){
    		setKuangcModels();
    	}
    	return _KuangcModel;
    }
    private void setKuangcModels(){
    	List _KuangcList = new ArrayList();
    	JDBCcon con = new JDBCcon();
    	try{
    		
//    		String sql = "select  id, meikdqmc from meikdqb order by meikdqmc ";
//    		ResultSet rs = con.getResultSet(sql);
//    		while(rs.next()){
//    			_KuangcList.add(new IDropDownBean(rs.getLong("id"),rs.getString("meikdqmc")));
//    		}
//    		rs.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		con.Close();
    		setKuangcModel(new IDropDownModel(_KuangcList));
    	}
      		
    }

	private List _KuangcSelected;
	public List getKuangcSelected(){
		if(_KuangcSelected == null){
			setKuangcSelected(null);
		}
		return _KuangcSelected;
	}

	public void setKuangcSelected(List KuangcSelect)
	{
	    if(_KuangcSelected == null){
			_KuangcSelected = new ArrayList();
			JDBCcon con = new JDBCcon();
			ResultSet rs ;
			String sql = "select meikdqb_id,dq.meikdqmc from zhongdmkb zd,meikdqb dq where zd.meikdqb_id=dq.id and zd.leix=1 ";
			
			try {
				rs = con.getResultSet(sql);	
				while(rs.next()){
					for (int i = 0; i < _KuangcModel.getOptionCount(); i++) {
						if (((IDropDownBean) _KuangcModel.getOption(i)).getId() == rs
								.getLong("meikdqb_id")) {
							_KuangcSelected.add(_KuangcModel.getOption(i));
							break;
						}
					}
				}
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}finally{
				con.Close();
			}
			//setKuangcSelected(_KuangcSelected);
	    }else{
	    	_KuangcSelected = KuangcSelect;
	    }
	}
	
	private void Save() {
		JDBCcon con = new JDBCcon();
		ResultSet rs;
		con.setAutoCommit(false);
		Visit visit = (Visit) getPage().getVisit();
		StringBuffer Sql = new StringBuffer();
		
		int result = -1;
		long meik_id = -1;
		int xuh = -1;
		int leix = -1;
		try{
			if(getKuangcSelected().size() > 0)
			{
				Sql.append("begin ");
				Sql.append("delete from zhongdmkb;");
				
				for(int i=0; i<getKuangcSelected().size(); i++)
				{
					
					String mID = MainGlobal.getNewID(1);
					meik_id = ((IDropDownBean)getKuangcSelected().get(i)).getId();
	
					String selsql = "select dq.id,dq.xuh,lb.id as leix from meikdqb dq,meiklbb lb where dq.meiklbb_id=lb.id and dq.id="+meik_id;
					rs=con.getResultSet(selsql);
					while(rs.next()){
						xuh = rs.getInt("xuh");
						leix = rs.getInt("leix");
					}
					rs.close();
					String sql = "insert into zhongdmkb (id, meikdqb_id, xuh, leix, beiz) values("
						+ mID
						+","
						+ meik_id
						+","
						+ xuh
						+","
						+ leix
						+",'');";
					Sql.append(sql);
				}
				Sql.append(" end; ");
				String sql1 = Sql.toString();
				result = con.getInsert(sql1);
				if(result<0){
					con.rollBack();
					setMsg("重点矿设置失败！");
				}else{
					con.commit();
					setMsg("重点矿设置成功！");
				}
			}else{
				setMsg("请选择一个煤矿!");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
	}
	
}