package com.zhiren.dc.pandgd;


import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.main.Visit;

/*
 * 修改人：ww
 * 修改时间：2010-09-19
 * 修改内容：添加复制上一次盘点说明内容按钮
 */

public abstract class Pandsx extends BasePage {
//	进行页面提示信息的设置
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
    
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
//  页面变化记录
    private String Change;
    public String getChange() {
    	return Change;
    }
    public void setChange(String change) {
    	Change = change;
    }
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(this.getPageName().toString());
			this.setPinzValue(null);
			this.setPinzModel(null);
		}
		getInitData();
	}
//  取得初始化数据
    public String getInitData() {

    	String initData = "";
    	JDBCcon con = new JDBCcon();
    	
    	String sql = "select b.pandff,b.shiyyq,b.meitcfqk,b.midcd,b.rulmjljsfctz,b.yingkqkfx from pandsxb b  where b.pand_gd_id="+this.getPinzValue()+"";
    	ResultSetList rsl = con.getResultSetList(sql);
    	if(rsl == null) {
//    		错误信息:获得记录集失败.
    		return initData;
    	}
    	if(rsl.next()) {
    		initData =	"inpandff = '"+rsl.getString("pandff")+
    					"';inshiyyq='"+rsl.getString("shiyyq")+
    					"';inmeitcfqk='"+rsl.getString("meitcfqk")+
    					"';inmidcd='"+rsl.getString("midcd")+
    					"';inrulmjljsfctz='"+rsl.getString("rulmjljsfctz")+
    					"';inyingkqkfx='"+rsl.getString("yingkqkfx")+
    					"';";
    	}
    	con.Close();
    	return initData;
    }
//	按钮事件处理
	private boolean _SaveChick = false;
    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }
    
    private boolean _CopyChick = false;
    public void CopyButton(IRequestCycle cycle) {
    	_CopyChick = true;
    }

    
    public void submit(IRequestCycle cycle) {
    	if (_SaveChick) {
    		_SaveChick = false;
    		Save();
        }
    	if (_CopyChick) {
    		_CopyChick = false;
    		Copy();
    	}
    	getInitData();
    }
    
    private void Copy() {
    	String pandID = this.getPandid();
    	StringBuffer sql = new StringBuffer();
    	JDBCcon con = new JDBCcon();
    	Visit visit = (Visit)this.getPage().getVisit();
    	int flag = 0;
    	con.setAutoCommit(false);
    	sql.append("select ID from pand_gd where riq<(select riq from pand_gd where bianm='").append(pandID).append("')")
    			.append("and diancxxb_id=(select diancxxb_id from pand_gd where bianm='").append(pandID).append("')")
    			.append("order by kaisrq DESC");
    	ResultSetList rsl = con.getResultSetList(sql.toString());
    	if (rsl.next()) {
    		sql.delete(0, sql.length());
    		sql.append("SELECT * FROM pandsxb WHERE pand_gd_id=" + rsl.getString("id"));
    		
    		ResultSetList rs = con.getResultSetList(sql.toString());
    		if (rs.next()) {
	    		flag = con.getDelete("DELETE  FROM  pandsxb WHERE pand_gd_id=(select id from pand_gd where bianm='" + pandID + "')");
	    		
	    		if (flag==-1) {
	    			con.rollBack();
	    		}
	    		sql.delete(0, sql.length());
	    		sql.append("INSERT INTO pandsxb (ID,pand_gd_id,pandff,shiyyq,meitcfqk,midcd,rulmjljsfctz,yingkqkfx) \n");
	    		sql.append("VALUES(getnewid(").append(visit.getDiancxxb_id()).append("), \n");
	    		sql.append("(SELECT ID  FROM pand_gd WHERE bianm='").append(pandID).append("'), \n");
	    		sql.append("'").append(rs.getString("pandff")).append("', \n");
	    		sql.append("'").append(rs.getString("shiyyq")).append("', \n");
	    		sql.append("'").append(rs.getString("meitcfqk")).append("', \n");
	    		sql.append("'").append(rs.getString("midcd")).append("', \n");
	    		sql.append("'").append(rs.getString("rulmjljsfctz")).append("', \n");
	    		sql.append("'").append(rs.getString("yingkqkfx")).append("' \n");
	    		sql.append(")");
	    		flag = con.getInsert(sql.toString());
	    		if (flag==-1) {
	    			con.rollBack();
	    		}
    		}
    		rs.close();
    	}
    	
    	rsl.close();
    	con.commit();
    	con.Close();
    }
    
//	保存分组的改动
	private void Save() {
		long id=0;
		if(getChange()==null || "".equals(getChange())) {
//			提示信息
			setMsg("修改为空!");
			return ;
		}
		String change[] = getChange().split(";",10);
		Visit visit = (Visit)this.getPage().getVisit();
    	JDBCcon con = new JDBCcon();
		String sql1="select p.pandff,p.shiyyq,p.meitcfqk,p.midcd,p.rulmjljsfctz,p.yingkqkfx from pandsxb p where p.pand_gd_id = "+change[0]+"";
		
		ResultSetList rs=con.getResultSetList(sql1);
		
		if(rs.getRows()==0)
		{
			String sql="insert into pandsxb values(getNewId(" + visit.getDiancxxb_id() + "),'"
			+ change[0]+"','"
			+ change[1]+"','"
			+ change[2]+"','"
			+ change[3]+"','"
			+ change[4]+"','" 
			+ change[5]+"','"
			+ change[6]+"')";
			con.getInsert(sql);
			con.Close();
		}
		else{

		String sql2="select p.id,p.pandff,p.shiyyq,p.meitcfqk,p.midcd,p.rulmjljsfctz,p.yingkqkfx from pandsxb p where p.pand_gd_id = "+change[0]+" ";
		ResultSetList rs1=con.getResultSetList(sql2);
		getInitData();
		while(rs1.next())
			{
				id = rs1.getLong("id");
			}
		String sql = "update pandsxb set pand_gd_id='"+change[0]+"', pandff='"+change[1]+"',shiyyq='"
    	+change[2]+"',meitcfqk='"+change[3]+"',midcd='"+change[4]+"',rulmjljsfctz='"+change[5]
    	+"',yingkqkfx='"+change[6]+"' where id="+ id;
		con.getUpdate(sql);
		con.Close();
		}
		this.setMsg("保存成功！");
//		getInitData();
	}
//盘点id
	 private String pdid;
	 public String getPandid() {
	    	return pdid;
	    }
	 public void setPandid(String pdid) {
		 this.pdid = pdid;
	 }   
	

//	盘点编码
	public IDropDownBean getPinzValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getPinzModel().getOptionCount()>0) {
				setPinzValue((IDropDownBean)getPinzModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	public void setPinzValue(IDropDownBean value) {
		
		if(getPandid()!=null&&null==value){
			String[] pd = getPandid().split(",");
			
			for (int i = 0; i < getPinzModel().getOptionCount(); i++) {
				if (((IDropDownBean) getPinzModel().getOption(i)).getValue().equals(
						pd[0])) {
					
					((Visit)this.getPage().getVisit()).setDropDownBean3(((IDropDownBean) getPinzModel().getOption(i)));
				}
			}
			
		}else{
			
			((Visit)this.getPage().getVisit()).setDropDownBean3(value);
		}
		
	}
	
	public IPropertySelectionModel getPinzModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			setPinzModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	public void setPinzModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
		
	} 
	
	public IPropertySelectionModel setPinzModels() {
		((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(SysConstant.SQL_Pdcx));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
	
	
}





