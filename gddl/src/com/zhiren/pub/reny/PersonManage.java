package com.zhiren.pub.reny;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DataBassUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.JsTreeUtil;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.main.validate.Validate;

public abstract class PersonManage extends BasePage implements
		PageValidateListener {
//	电厂信息表的ID （通过树得到）
	private long ID;
//	页面上树的操作步骤记录
	private String OperateNotes;
	public long getID() {
		return ID;
	}
	public void setID(long id) {
		ID = id;
	}
	public String getOperateNotes() {
		return OperateNotes;
	}
	public void setOperateNotes(String operateNotes) {
		OperateNotes = operateNotes;
	}
//	进行页面提示信息的设置
	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = _value;
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getActivePageName().toString().equals(
				this.getPage().getPageName())) {
			visit.setActivePageName(this.getPage().getPageName());
			visit.setList1(null);
			visit.setString1("");
		}
	}
//  保存页面Bean
	private PersonBean editValue;
	public PersonBean getEditValue() {
		return editValue;
	}
	public void setEditValue(PersonBean pmb) {
		editValue = pmb;
	}
//	保存数据list
	public List getEditValues() {
        return ((Visit) getPage().getVisit()).getList1();
    }

    public void setEditValues(List editList) {
        ((Visit) getPage().getVisit()).setList1(editList);
    }
//	按钮事件处理
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
    
    private boolean _ReSetPwdChick = false;
    public void ReSetPwdButton(IRequestCycle cycle) {
    	_ReSetPwdChick = true;
    }

    private boolean _SaveChick = false;
    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }
    
    private boolean _PowerChick = false;
    public void PowerButton(IRequestCycle cycle) {
        _PowerChick = true;
    }
    public void submit(IRequestCycle cycle) {
    	if (_RefurbishChick) {
    		_RefurbishChick = false;
    		RefurbishChick();
        }
    	if (_InsertChick) {
    		_InsertChick = false;
    		Insert();
        }
    	if (_DeleteChick) {
    		_DeleteChick = false;
    		Delete();
        }
    	if (_ReSetPwdChick) {
    		_ReSetPwdChick = false;
    		Save();
    		ReSetPwd();
        }
    	if (_SaveChick) {
    		_SaveChick = false;
    		Save();
    		RefurbishChick();
        }
    	if (_PowerChick) {
    		_PowerChick = false;
    		Power(cycle);
        }
    }
//  刷新用户
    private void RefurbishChick() {
    	List editList = new ArrayList();
    	JDBCcon con = new JDBCcon();
    	String sql = "select id,mingc,piny,quanc,diancxxb_id,xingb,decode(zhuangt,1,'是','否') zhuangt \n" +
    			"from renyxxb where diancxxb_id= "+getID()+" order by mingc";
    	ResultSetList rsl = con.getResultSetList(sql);
    	if(rsl == null) {
//    		错误信息:获得记录集失败.
    		return;
    	}
    	while(rsl.next()) {
    		long id = Long.parseLong(rsl.getString("ID"));
    		String mingc = rsl.getString("mingc");
    		String piny = rsl.getString("piny");
    		String quanc = rsl.getString("quanc");
    		long diancxxb_id = Long.parseLong(rsl.getString("diancxxb_id"));
    		String xingb = rsl.getString("xingb");
    		String zhuangt = rsl.getString("zhuangt");
//    		String beiz = rsl.getString("beiz");
    		editList.add(new PersonBean(false,id,mingc,piny,quanc,diancxxb_id,xingb,zhuangt));
    	}
    	setEditValues(editList);
    	con.Close();
    }
//  新建一个用户
	private void Insert() {
		List listtmp = getEditValues();
		if(listtmp == null || listtmp.isEmpty()) {
			listtmp = new ArrayList();
		}else {
			long diancxxid = ((PersonBean)listtmp.get(0)).getDiancxxbId();
			if(diancxxid != getID() || getID()==0) {
				setMsg("请刷新后单击添加!");
				return ;
			}
		}
		listtmp.add(new PersonBean(true,0,"","","",getID(),"","是"));
		setEditValues(listtmp);
	}
//	作废或还原一个用户
	private void Delete() {
		List listtmp = getEditValues();
		if(listtmp == null) {
			return;
		}
		JDBCcon con = new JDBCcon();
		for(int i =0 ; i< listtmp.size(); i++) {
			PersonBean pb = ((PersonBean)listtmp.get(i));
			if(pb.getSelected()) {
				int zhuangt = 1;
				if("否".equals(pb.getZhuangt())) {
					pb.setZhuangt("是");
				}else {
					pb.setZhuangt("否");
					zhuangt = 0;
				}
				if(pb.getID() != 0) {
					con.getUpdate("update renyxxb set zhuangt="+zhuangt+" where id="+pb.getID());
				}
			}
		}
		con.Close();
	}
//	重新设置密码
	private void ReSetPwd() {
		List listtmp = getEditValues();
		if(listtmp == null) {
			return;
		}
		JDBCcon con = new JDBCcon();
		for(int i =0 ; i< listtmp.size(); i++) {
			PersonBean pb = ((PersonBean)listtmp.get(i));
			if(pb.getSelected()) {
				if(pb.getID() == 0) {
					return ;
				}else {
					DataBassUtil dbu = new DataBassUtil();
					try {
						dbu.UpdateBlob("renyxxb", "mim", pb.getID(), pb.getMingc(),true);
						setMsg(dbu.GetStrBlob("renyxxb", "mim", pb.getID(),true));
					}catch(Exception e) {
						e.printStackTrace();
						con.rollBack();
						return;
					}
				}
			}
		}
		con.Close();
	}
//	保存用户的改动
	private void Save() {
		List listtmp = getEditValues();
		if(listtmp == null) {
//			错误信息:无需保存记录
			return;
		}
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		for(int i =0 ; i< listtmp.size(); i++) {
			PersonBean pb = ((PersonBean)listtmp.get(i));
			if(pb.getSelected()) {
				String UserName = pb.getMingc();
				if(UserName==null || UserName.equals("")) {
					setMsg("用户名不能为空!");
					con.rollBack();
					return;
				}
				String quanc = pb.getQuanc();
				if(quanc==null || quanc.equals("")) {
					setMsg("用户 "+UserName+" 缺少姓名!");
					con.rollBack();
					return;
				}
				long diancxxb_id = pb.getDiancxxbId();
				if(diancxxb_id == 0) {
					setMsg("请选择用户单位!");
					con.rollBack();
					return;
				}
				int zhuangt = 1;
				if("否".equals(pb.getZhuangt())) {
					zhuangt = 0;
				}
				if(pb.getID() == 0) {
					if(Validate.UserExists(UserName,"0")) {
						setMsg("用户 "+UserName+" 已存在!");
						con.rollBack();
						return;
					}
					String id = MainGlobal.getNewID(diancxxb_id);
					String sql = "insert into renyxxb(id, mingc, quanc, mim, diancxxb_id, zhuangt) values("+id+",'"
						+ UserName + "','"+quanc+"',empty_blob(),"+diancxxb_id+","+zhuangt+")";
					con.getInsert(sql);
					DataBassUtil dbu = new DataBassUtil();
					try {
						dbu.UpdateBlob("renyxxb", "mim", Long.parseLong(id), UserName,true);
					}catch(Exception e) {
						e.printStackTrace();
						con.rollBack();
						return;
					}
				}else {
					if(Validate.UserExists(UserName,""+pb.getID())) {
						setMsg("用户 "+UserName+" 已存在!");
						con.rollBack();
						return;
					}
					String sql = "update renyxxb set mingc='"+UserName+"',quanc='"+quanc+"' where id =" + pb.getID();
					con.getUpdate(sql);
				}
			}
		}
		con.commit();
	}
//	跳转至设置权限页面
	private void Power(IRequestCycle cycle){
		List listtmp = getEditValues();
		if(listtmp == null) {
//			错误信息:无需保存记录
			return;
		}
		int m = 0;
		for(int i =0 ; i< listtmp.size(); i++) {
			PersonBean pb = ((PersonBean)listtmp.get(i));
			if(pb.getSelected()) {
				m++;
				if(m>1) {
					setMsg("不能同时对多个用户设置权限");
					return;
				}
				((Visit)this.getPage().getVisit()).setString1(String.valueOf(pb.getID()));
			}
		}
		if(m==0) {
			setMsg("请选中一个用户");
			return;
		}
		cycle.activate("PersonPower");
	}
//	页面判定方法
    public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}
//  得到电厂信息树的array
    public String getArrayScript() {
		JDBCcon con = new JDBCcon();
		JsTreeUtil jtree = new JsTreeUtil(JsTreeUtil.TreeType_ImgTree);
    	jtree.CreateRoot("人员信息");
    	for(int i = 1; i < 4 ; i++) {
        	ResultSetList rs = con.getResultSetList(
        			"select fuid,id,mingc,xuh,quanc from diancxxb where jib = "
        			+i+" order by xuh");
        	jtree.addNode(rs);
    	}
    	con.Close();
        return jtree.getTree();
    }
//  根据操作列表得到 JS 代码
    public String getOprateScript(){
    	String script =	JsTreeUtil.
    		getOprateScript(getOperateNotes(),false,getID());
    	setOperateNotes(null);
    	return script;
    }
}
