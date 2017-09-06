package com.zhiren.shanxdted.diqmkext;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.main.validate.Validate;
/*
 * ����:tzf
 * ʱ��:2009-11-03
 * ����:ú���������
 */
public class Diqmkext extends BasePage implements PageValidateListener {
//	����ҳ����ʾ��Ϣ������
	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
//	    _msg = _value;
		_msg=MainGlobal.getExtMessageBox(_value,false);
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
//  ҳ��仯��¼
    private String Change;
    public String getChange() {
    	return Change;
    }
    public void setChange(String change) {
    	Change = change;
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
	
	
	private String treeid = "";

	public String getTreeid() {

		if (treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {

		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
	}
	

//	��ť�¼�����

    private boolean _SaveChick = false;
    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }
    
    private boolean _PowerChick = false;
    public void PowerButton(IRequestCycle cycle) {
        _PowerChick = true;
    }
    
   
    
    public void submit(IRequestCycle cycle) {
    	if (_SaveChick) {
    		_SaveChick = false;
    		Save();
        }
    	if (_PowerChick) {
    		_PowerChick = false;
    		Power(cycle);
        }
    }

//  ȡ��������
    public void getSelectData() {
    	Visit visit = (Visit) getPage().getVisit();
    	String sql = " select id,mingc,beiz from diqmkgroup ";

		JDBCcon con = new JDBCcon();
    	ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		egu.setTableName("diqmkgroup");
		egu.setWidth("bodyWidth");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("id").setHeader("���");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor=null;
		
		egu.getColumn("mingc").setHeader("����ú������");
		egu.getColumn("beiz").setHeader("��ע");
//
//		//�Ƿ���ʾѡ��糧��
//		if (visit.isFencb()) {
//			egu.addTbarText("��λ����:");
//			ExtTreeUtil etu = new ExtTreeUtil("diancTree",
//					ExtTreeUtil.treeWindowType_Dianc,
//					((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
//			setTree(etu);
//			egu.addTbarTreeBtn("diancTree");
//			egu.addTbarText("-");
//		}
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
//		GridButton gSave =  new GridButton(GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
//		egu.addTbarBtn(gSave);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		
		String sPowerHandler = "function(){"
					+"if(gridDiv_sm.getSelected()== null){"
		        	+"	Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����������!');"
		        	+"	return;"
		        	+"}"
		        	+"var grid_rcd = gridDiv_sm.getSelected();"
		        	+"if(grid_rcd.get('ID') == '0'){"
		        	+"	Ext.MessageBox.alert('��ʾ��Ϣ','�����÷���֮ǰ���ȱ���!');"
		        	+"	return;"
		        	+"}"
		        	+"grid_history = grid_rcd.get('ID');"
					+"var Cobj = document.getElementById('CHANGE');"
					+"Cobj.value = grid_history;"
					+"document.getElementById('PowerButton').click();"
					+"}";
		
		egu.addTbarBtn(new GridButton("����ú��",sPowerHandler));
		
		setExtGrid(egu);

    }
//	�������ĸĶ�
	private void Save() {
		Visit visit=(Visit)this.getPage().getVisit();
		int flag=this.getExtGrid().Save(this.getChange(), visit);
		
		if(flag>=0)
			this.setMsg("���ݲ����ɹ�!");
		else
			this.setMsg("���ݲ���ʧ��!");
	}
//	���÷���
	private void Power(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("��ѡ��һ����������!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		visit.setString12(getChange());
		cycle.activate("Diqmkgroup");
	}

//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setTreeid("");
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
