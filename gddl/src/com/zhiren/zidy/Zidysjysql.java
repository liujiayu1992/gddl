package com.zhiren.zidy;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;

public abstract class Zidysjysql extends BasePage {
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

    public String getBiaot() {
		return ((Visit)this.getPage().getVisit()).getString1();
	}
	public void setBiaot(String title) {
		((Visit)this.getPage().getVisit()).setString1(title);
	}
	public String getGongs() {
		return ((Visit)this.getPage().getVisit()).getString2();
	}
	public void setGongs(String gongs) {
		((Visit)this.getPage().getVisit()).setString2(gongs);
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
		if(getGongs() == null || "".equals(getGongs())) {
			RefurbishChick();
		}
	}
	//刷新
	 private void RefurbishChick() {
		 JDBCcon con = new JDBCcon();
		 StringBuffer sb = new StringBuffer();
		 sb.append("select * from zidysjysql where zidysjy_id = ")
		 .append(((Visit)this.getPage().getVisit()).getLong1())
		 .append(" order by xuh");
		 ResultSetList rs = con.getResultSetList(sb.toString());
		 String gongs = "";
		 while(rs.next()){
			 gongs += rs.getString("z_query");
		 }
		 setGongs(gongs);
		 con.Close();
    }
	 
//	返回
	private void Return(IRequestCycle cycle) {
		setBiaot(null);
		setGongs(null);
		cycle.activate("Zidysjy");
	}
	
//  取得初始化数据
    public String getInitData() {
    	String initData = "";
    	RefurbishChick();
    	initData = "intitle ='"+getBiaot()+"';";
    	return initData;
    }
//	按钮事件处理
	private boolean _SaveChick = false;
    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }

    private boolean _ReturnChick = false;
    public void ReturnButton(IRequestCycle cycle) {
    	_ReturnChick = true;
    }
    
    public void submit(IRequestCycle cycle) {
    	if (_SaveChick) {
    		_SaveChick = false;
    		Save();
        }
    	if (_ReturnChick) {
    		_ReturnChick = false;                 
    		Return(cycle);
        }
    }
	
//	确认
	private void Save() {
		Visit v = ((Visit)this.getPage().getVisit());
		if(getGongs()==null || v.getLong1()<=0) {
			return;
		}
		
		String[] gongs = MainGlobal.getSplitStringArray(getGongs().replaceAll("'", "''"), 2000);
		StringBuffer sb = new StringBuffer();
		sb.append("begin\n");
		sb.append("delete from zidysjysql where zidysjy_id = "+v.getLong1()+";\n");
		for(int i = 0; i< gongs.length; i++){
			sb.append("insert into zidysjysql(id,zidysjy_id,xuh,z_query) values(")
			.append("getnewId(").append(v.getDiancxxb_id()).append("),").append(v.getLong1())
			.append(",").append(i+1).append(",'").append(gongs[i]).append("');");
		}
		sb.append("end;");
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		//con.getDelete("delete from zidysjysql where zidysjy_id = "+v.getLong1());
		con.getUpdate(sb.toString());
		con.commit();
		con.Close();
		setMsg("保存成功！");
	}
	
}

