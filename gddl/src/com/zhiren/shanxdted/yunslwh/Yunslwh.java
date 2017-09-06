package com.zhiren.shanxdted.yunslwh;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yunslwh extends BasePage implements PageValidateListener {
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
        setTbmsg(null);
    }
//    -----------------------------------
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
//    ------------------------------------
//  ҳ��仯��¼
    private String Change;
    public String getChange() {
    	return Change;
    }
    public void setChange(String change) {
    	Change = change;
    }
    
//  ҳ��仯��¼
    private String theKey;
    private boolean Key=false;
    public String gettheKey() {
    	return theKey;
    }
    public void settheKey(String theKey) {
    	this.theKey = theKey;
    }
    
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}
	
	public String getGridHtml() {
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}
	
	public String getTreeHtml() {
		if (getTree() == null){
			return "";
		}else {
			return getTree().getWindowTreeHtml(this);
		}

	}

	public String getTreeScript() {
		if (getTree() == null) {
			return "";
		}else {
			return getTree().getWindowTreeScript();
		}
	}

//	��ť�¼�����

    private boolean _SaveChick = false;
    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }

    
    private boolean _ChazChick = false;
    public void ChazButton(IRequestCycle cycle) {
    	_ChazChick = true;
    }
    
    public void submit(IRequestCycle cycle) {
    	if (_SaveChick) {
    		_SaveChick = false;
    		Save();
        }
    	if (_ChazChick) {
    		_ChazChick = false;
    		Key=true;
        }
    }

//  ȡ��������
    public void getSelectData() {
    	String sql=new String();
    	if(Key){
    		sql = "select id,mingc,yunsl,yunslx " +
		     " from  meikxxb where " +
		     		" mingc like '%" + gettheKey() + "%'" ;
    		Key=false;
    	}else{
    		sql = "select id , mingc ,yunsl, yunslx from meikxxb  ";
    	}
		JDBCcon con = new JDBCcon();
    	ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		egu.setTableName("meikxxb");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("mingc").setHeader("ú��λ��Ϣ");
		egu.getColumn("mingc").editor=null;
		egu.getColumn("mingc").setWidth(250);
		egu.getColumn("yunsl").setHeader("������");
		egu.getColumn("yunsl").setWidth(60);
		egu.getColumn("yunslx").setHeader("����·��");
		egu.getColumn("yunslx").setWidth(450);
	
		egu.addTbarText("����ú��λ��Ϣ��");
		TextField theKey = new TextField();
		theKey.setWidth(100);
		theKey.setId("theKey_text");
		theKey.setListeners("specialkey:function(thi,e){if (e.getKey()==13){var objkey = document.getElementById('theKey');objkey.value = theKey_text.getValue();document.getElementById('ChazButton').click();}}\n");
		egu.addToolbarItem(theKey.getScript());

		String Chaz = "function(){var objkey = document.getElementById('theKey');objkey.value=theKey_text.getValue();document.getElementById('ChazButton').click();}";
		GridButton chaz = new GridButton("����", Chaz, SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(chaz);		
		setExtGrid(egu);
		egu.addTbarText("-");
		GridButton gSave =  new GridButton(GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
		egu.addTbarBtn(gSave);
		egu.addTbarText("   ��1����ú��λ��ϢΪ��ʱ������ʾȫ�����ݣ�2����ҳ����Ҳ���ǰ�뱣�棩");

    }
//	�������ĸĶ�
	private void Save() {
		if(getChange()==null || "".equals(getChange())) {
//			��ʾ��Ϣ
			setMsg("�޸�Ϊ��!");
			return ;
		}
		JDBCcon con = new JDBCcon();
		String id = "";
		String yunsl = "";
		String yunslx = "";
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		while(rsl.next()){
				id = rsl.getString("id");
				yunsl = rsl.getString("yunsl");
				yunslx = rsl.getString("yunslx");
			
				String sql = "update meikxxb set yunsl = "+yunsl+",yunslx = '"+yunslx+"' where id =" + id;
				con.getUpdate(sql);
		}
		
	}


//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
//			setTreeid("");
		}
		init();
	} 
	
	private void init() {
		setExtGrid(null);
		setTree(null);
		getSelectData();
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
