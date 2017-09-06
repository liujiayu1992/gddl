package com.zhiren.pub.quanx;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.JsTreeUtil;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Group extends BasePage implements PageValidateListener {
//	����ҳ����ʾ��Ϣ������
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
//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(this.getPageName().toString());
			visit.setList1(null);
			visit.setString1(null);
			visit.setString2(null);
			visit.setLong1(0);
//			setMsg("��ʾ��Ϣ");
		}
	}
//	��ť�¼�����
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
    
    private boolean _ShowPowerChick = false;
    public void ShowPowerButton(IRequestCycle cycle) {
        _ShowPowerChick = true;
    }
    
    private boolean _SavePowerChick = false;
    public void SavePowerButton(IRequestCycle cycle) {
        _SavePowerChick = true;
    }
    
    public void submit(IRequestCycle cycle) {
    	if (_RefurbishChick) {
    		_RefurbishChick = false;
    		RefurbishChick();
    		setArrayScript(-1);
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
    		RefurbishChick();
        }
    	if (_ShowPowerChick) {
    		_ShowPowerChick = false;
    		ShowPower();
        }
    	if (_SavePowerChick) {
    		_SavePowerChick = false;
    		SavePower();
        }
    }
//  ˢ�·���
    private void RefurbishChick() {
    	List editList = new ArrayList();
    	JDBCcon con = new JDBCcon();
    	String sql = "select id,mingc,beiz from zuxxb order by mingc";
    	ResultSetList rsl = con.getResultSetList(sql);
    	if(rsl == null) {
    		setMsg("�������Ϣʧ��");
//    		������Ϣ:��ü�¼��ʧ��.
    		return;
    	}
    	while(rsl.next()) {
    		long id = Long.parseLong(rsl.getString("ID"));
    		String mingc = rsl.getString("mingc");
    		String beiz = rsl.getString("beiz");
    		editList.add(new GroupBean(false,id,mingc,beiz));
    	}
    	setEditValues(editList);
    	con.Close();
    }
//  �½�һ������
	private void Insert() {
		List listtmp = getEditValues();
		if(listtmp == null) {
			listtmp = new ArrayList();
		}
		listtmp.add(new GroupBean(true,0,"",""));
		setEditValues(listtmp);
	}
//	ɾ��һ������
	private void Delete() {
		List listtmp = getEditValues();
		if(listtmp == null) {
			return;
		}
		JDBCcon con = new JDBCcon();
		for(int i =0 ; i< listtmp.size(); i++) {
			if(((GroupBean)listtmp.get(i)).isSelected()) {
				long id = ((GroupBean)listtmp.get(i)).getID();
				if(id != 0) {
					con.getDelete("delete from zuxxb where id="+id);
				}
				listtmp.remove(i--);
			}
		}
		con.Close();
	}
//	�������ĸĶ�
	private void Save() {
		List listtmp = getEditValues();
		if(listtmp == null) {
//			������Ϣ:���豣���¼
			return;
		}
		JDBCcon con = new JDBCcon();
		for(int i =0 ; i< listtmp.size(); i++) {
			if(((GroupBean)listtmp.get(i)).isSelected()) {
				long id = ((GroupBean)listtmp.get(i)).getID();
				String mingc = ((GroupBean)listtmp.get(i)).getGroupName();
				String beiz = ((GroupBean)listtmp.get(i)).getRemarks();
				if(id == 0) {
					String sql = "insert into zuxxb(id,mingc,beiz) \n" +
							"values(getNewID(999),'"+mingc+"','"+beiz+"')";
					con.getInsert(sql);
				}else {
					String sql = "update zuxxb set mingc='"+
					mingc+"',beiz='"+beiz+"' where id="+id;
					con.getUpdate(sql);
				}
			}
		}
		
	}
//	��ʾ���з����Ȩ��
	private void ShowPower() {
		List listtmp = getEditValues();
		if(listtmp == null) {
//			������Ϣ:�޿ɲ鿴Ȩ��
			return;
		}
		int index = -1;
		for(int i =0, j =0; i< listtmp.size(); i++) {
			if(((GroupBean)listtmp.get(i)).isSelected()) {
				if(j++>=1) {
					setMsg("ֻ�ܲ鿴��һ�����Ȩ��");
					setArrayScript(-1);
//					������Ϣ:ֻ�ܲ鿴һ�ַ����Ȩ��
					return;
				}
				index = i;
			}
		}
		if(index == -1) {
			setMsg("��ѡ��һ������鿴Ȩ��");
			setArrayScript(-1);
//			������Ϣ:��ѡ��һ�ַ���鿴Ȩ��
			return;
		}
		long Zuid = ((GroupBean)listtmp.get(index)).getID();
		((Visit) getPage().getVisit()).setLong1(Zuid);
		setArrayScript(Zuid);
	}
//	Ȩ�ޱ仯��¼
	public String getPowerChange(){
		return ((Visit) getPage().getVisit()).getString2();
	}
	
	public void setPowerChange(String value){
		((Visit) getPage().getVisit()).setString2(value);
	}
//	��������Ӧ����Դ
	private void SavePower() {
		String change[] = getPowerChange().split(",");
		long Zuid = ((Visit) getPage().getVisit()).getLong1();
		String sql = "";
		JDBCcon con = new JDBCcon();
		for(int i=0;i<change.length;i++){
			long ziyid = Long.valueOf(change[i].substring(1)).longValue();
			sql = "delete from zuqxb where zuxxb_id="+Zuid+" and ziyxxb_id="+ziyid;
			con.getDelete(sql);
			if(change[i].indexOf("+")!=-1){
				sql = "insert into zuqxb(id,zuxxb_id,ziyxxb_id) values(getNewID(999),"+Zuid+","+ziyid+")";
				con.getInsert(sql);
			}
		}
		con.Close();
		setArrayScript(Zuid);
	}
//	��������list
	public List getEditValues() {
        return ((Visit) getPage().getVisit()).getList1();
    }

    public void setEditValues(List editList) {
        ((Visit) getPage().getVisit()).setList1(editList);
    }
    
//  ����ҳ��Bean
    private GroupBean editValue;
    public GroupBean getEditValue() {
    	return editValue;
    }
    public void setEditValue(GroupBean editValue) {
    	this.editValue = editValue;
    }
//	ȡ������js��
    public String getArrayScript() {
		if(((Visit) getPage().getVisit()).getString1() == null || ((Visit) getPage().getVisit()).getString1().equals("")){
			setArrayScript(-1);
		}
		return ((Visit) getPage().getVisit()).getString1();
	}
//  
    public void setArrayScript(long Zuid){
		JDBCcon con = new JDBCcon();
		JsTreeUtil jtree = new JsTreeUtil(JsTreeUtil.TreeType_ImgCheckTree);
	    jtree.CreateRoot("ȼ�Ϲ�����Դ��Ϣ");
	    if(Zuid != -1 ) {
	    	for(int i = 1; i<5 ;i++) {
		    	String sql = "select z.fuid,z.id,z.mingc,"+
				"nvl((select 'checked' from zuqxb q where q.zuxxb_id = "+Zuid+" and q.ziyxxb_id = z.id),'') checked,"+
				"z.xuh,z.wenjwz from ziyxxb z where z.jib = "+i+" order by z.xuh";
		    	ResultSetList rs = con.getResultSetList(sql);
		    	jtree.addNode(rs);
		    }
	    }
	    con.Close();
	    ((Visit) getPage().getVisit()).setString1(jtree.getTree());
		/*
		StringBuffer array = new StringBuffer();
	    long fuid = 0;
	    long id = 0;
	    String mingc = "";
	    boolean checked = false;
	    long xuh = 0;
	    String wenjwz = "";
	    try {
	        array.append("CheckTree = new Array();\n ");
	        array.append("CheckRoot = new Array(-1,0,\"ȼ�Ϲ�����Դ��Ϣ\",true,0,'#');\n");
	        array.append("CheckTree[0]=CheckRoot;\n");
	        for(int i = 1; i<5 ;i++){
	        	String sql = "select z.fuid,z.id,z.mingc,"+
	        			"nvl((select 'true' from zuqxb q where q.zuxxb_id = "+Zuid+" and q.ziyxxb_id = z.id),'false') checked,"+
	        			"z.xuh,z.wenjwz from ziyxxb z where z.jib = "+i+" order by z.xuh";
	        	ResultSetList rs = con.getResultSetList(sql);
	        	array.append("CheckTreeNode"+i+" = new Array();\n");
	        	int j=0;
	        	while(rs.next()){
	        		fuid = Long.parseLong(rs.getString("fuid"));
	        		id = Long.parseLong(rs.getString("id"));
	        		mingc = rs.getString("mingc");
	        		if(mingc == null) {
	        			mingc = "";
	        		}
	        		checked = Boolean.valueOf(rs.getString("checked")).booleanValue();
	        		xuh = Long.parseLong(rs.getString("xuh"));
	        		wenjwz = rs.getString("wenjwz");
	        		if(wenjwz == null) {
	        			wenjwz = "";
	        		}
	        		array.append("CheckTreeNode"+i+"["+j+++"] = new Array("+fuid+",");
	        		array.append(id+",\"");
	        		array.append(mingc.replaceAll("\n", "").replaceAll("\r", "")+"\",");
	        		array.append(checked+",");
	        		array.append(xuh+",\"");
	        		array.append(wenjwz.replaceAll("\n", "").replaceAll("\r", "")+"\");\n");
	        	}
	        	array.append("CheckTree["+i+"]= CheckTreeNode"+i+";\n");
	        }
	        array.append("");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    con.Close();
	    ((Visit) getPage().getVisit()).setString1(array.toString());
	    */
	}
//	ҳ���ж�����
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
}
