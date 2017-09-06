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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.main.validate.Validate;

public class Renyxxfj extends BasePage implements PageValidateListener {
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
    
    private boolean _ReSetPwdChick = false;
    public void ReSetPwdButton(IRequestCycle cycle) {
    	_ReSetPwdChick = true;
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
    	if (_ReSetPwdChick) {
    		_ReSetPwdChick = false;
    		ReSetPwd();
        }
    	if (_PowerChick) {
    		_PowerChick = false;
    		Power(cycle);
        }
    	if (_ChazChick) {
    		_ChazChick = false;
    		Key=true;
        }
    }

//  ȡ��������
    public void getSelectData() {
    	Visit visit = (Visit) getPage().getVisit();
    	String sql=new String();
    	if(Key){
    		sql = "select id,mingc,quanc,xingb,getrenyfz(id) zu,bum,zhiw,decode(zhuangt,1,'��','��') zhuangt,yiddh,guddh,chuanz,youzbm,email,lianxdz " +
		     " from  renyxxb where diancxxb_id = " + getTreeid() +"" +
		     		" and (mingc like '%" + gettheKey() + "%' or quanc like '%" + gettheKey() + "%')" ;
    		Key=false;
    	}else{
    		sql = "select id,mingc,quanc,xingb,getrenyfz(id) zu,bum,zhiw,decode(zhuangt,1,'��','��') zhuangt,yiddh,guddh,chuanz,youzbm,email,lianxdz " +
    			     " from  renyxxb where diancxxb_id = "
    					+ getTreeid() +" order by mingc";
    	}
		JDBCcon con = new JDBCcon();
    	ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		egu.setTableName("renyxxb");
		egu.setWidth("bodyWidth");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("id").setHeader("���");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor=null;
		egu.getColumn("mingc").setHeader("�û���");
		egu.getColumn("quanc").setHeader("����");
		egu.getColumn("xingb").setHeader("�Ա�");
		egu.getColumn("bum").setHeader("����");
		egu.getColumn("zu").setHeader("Ȩ����");
		egu.getColumn("zu").setEditor(null);
		egu.getColumn("zu").setUpdate(false);
		egu.getColumn("zhiw").setHeader("ְλ");
		egu.getColumn("zhuangt").setHeader("�Ƿ�ɵ�½");
		egu.getColumn("yiddh").setHeader("�ƶ��绰");
		egu.getColumn("guddh").setHeader("�̶��绰");
		egu.getColumn("chuanz").setHeader("����");
		egu.getColumn("youzbm").setHeader("��������");
		egu.getColumn("email").setHeader("Email");
		egu.getColumn("lianxdz").setHeader("��ϵ��ַ");
		
		egu.getColumn("xingb").setDefaultValue( "��");
		egu.getColumn("zhuangt").setDefaultValue("��");
		
		ComboBox combSex= new ComboBox();
		egu.getColumn("xingb").setEditor(combSex);
		combSex.setEditable(true);
		List lSex = new ArrayList();
		lSex.add(new IDropDownBean(0, "��"));
		lSex.add(new IDropDownBean(1, "Ů"));
		egu.getColumn("xingb").setComboEditor(egu.gridId, new IDropDownModel(lSex));
		egu.getColumn("xingb").returnId = false;
		
		ComboBox combisLogin = new ComboBox();
		egu.getColumn("zhuangt").setEditor(combisLogin);
		combisLogin.setEditable(true);
		List lisLogin = new ArrayList();
		lisLogin.add(new IDropDownBean(0, "��"));
		lisLogin.add(new IDropDownBean(1, "��"));
		egu.getColumn("zhuangt").setComboEditor(egu.gridId, new IDropDownModel(lisLogin));
		//�Ƿ���ʾѡ��糧��
		if (visit.isFencb()) {
			egu.addTbarText("��λ����:");
			ExtTreeUtil etu = new ExtTreeUtil("diancTree",
					ExtTreeUtil.treeWindowType_Dianc,
					((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
			setTree(etu);
			egu.addTbarTreeBtn("diancTree");
			egu.addTbarText("-");
		}
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		GridButton gSave =  new GridButton(GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
		egu.addTbarBtn(gSave);
		egu.addToolbarButton(GridButton.ButtonType_Cancel, null);
		
		String sFn = 	"if(gridDiv_sm.getSelected() == null){"
						+"	 Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����Ա��������');"
						+"	 return;"
						+"}"
						+"var grid_rcd = gridDiv_sm.getSelected();"
						+"if(grid_rcd.get('ID') == '0'){"
						+"	 Ext.MessageBox.alert('��ʾ��Ϣ','����������֮ǰ���ȱ���!');"
						+"	 return;"
						+"}"
						+"Ext.MessageBox.confirm('��ʾ��Ϣ','������������뽫����Ϊ���û�����ͬ,ȷ����?',function(btn){"
						+"	 if(btn == 'yes'){"
						+"		    grid_history = grid_rcd.get('ID') + ',' + grid_rcd.get('MINGC');"
						+"			var Cobj = document.getElementById('CHANGE');"
						+"			Cobj.value = grid_history;"
						+"			document.getElementById('ReSetPwdButton').click();"
						+"	       	}"
						+"	  })";
		
		String sPwHandler = "function(){"
					+"var grid_Mrcd = gridDiv_ds.getModifiedRecords();"
					+"if(grid_Mrcd.length > 0){"
					+"	Ext.MessageBox.confirm('��ʾ��Ϣ', '����������������ĸ��Ľ���������,�Ƿ����?', function(btn){"
					+"		if(btn == 'yes'){"
					+ sFn 
					+		"}"
					+"	});"
					+"}else{"
					+ sFn
					+"}"
					+"}";
					
		egu.addTbarBtn(new GridButton("��������",sPwHandler));
		
		String sPowerHandler = "function(){"
					+"if(gridDiv_sm.getSelected()== null){"
		        	+"	Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����Ա���÷���');"
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
		
		egu.addTbarBtn(new GridButton("���÷���",sPowerHandler));
		String sPrintHandler = "function(){"
						+" var url = 'http://'+document.location.host+document.location.pathname;" 
						+" var end = url.indexOf(';');url = url.substring(0,end);" 
						+" url = url + '?service=page/' + 'Renyxxreport&lx=rezc'; window.open(url,'newWin');}";
		
		egu.addTbarBtn(new GridButton("��ӡ",sPrintHandler));
		egu.addTbarText("-");
		egu.addTbarText("�����û�����������");
		TextField theKey = new TextField();
		theKey.setWidth(80);
		theKey.setId("theKey_text");
		theKey.setListeners("specialkey:function(thi,e){if (e.getKey()==13){var objkey = document.getElementById('theKey');objkey.value = theKey_text.getValue();document.getElementById('ChazButton').click();}}\n");
		egu.addToolbarItem(theKey.getScript());

		String Chaz = "function(){var objkey = document.getElementById('theKey');objkey.value=theKey_text.getValue();document.getElementById('ChazButton').click();}";
		GridButton chaz = new GridButton("����", Chaz, SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(chaz);

		
		setExtGrid(egu);

    }
//	�������ĸĶ�
	private void Save() {
		if(getChange()==null || "".equals(getChange())) {
//			��ʾ��Ϣ
			setMsg("�޸�Ϊ��!");
			return ;
		}
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String id = "";
		String mingc = "";
		String quanc = "";
		String xingb = "";
		String bum   = "";
		String zhiw  = "";
		String zhuangt = "";
		String yiddh = "";
		String guddh = "";
		String chuanz = "";
		String youzbm = "";
		String Email = "";
		String lianxdz = "";
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		while(rsl.next()){
			id = rsl.getString("id");
			mingc = rsl.getString("mingc");
			quanc = rsl.getString("quanc");
			xingb = rsl.getString("xingb");
			bum = rsl.getString("bum");
			zhiw = rsl.getString("zhiw");
			zhuangt = rsl.getString("zhuangt").equals("��") ? "1" : "0";
			yiddh = rsl.getString("yiddh");
			guddh = rsl.getString("guddh");
			chuanz = rsl.getString("chuanz");
			youzbm = rsl.getString("youzbm");
			Email = rsl.getString("Email");
			lianxdz = rsl.getString("lianxdz");
			if(id.equals("0")) {
				if(Validate.UserExists(mingc,"0")) {
					setMsg("�û� "+mingc+" �Ѵ���!");
					continue;
				}
				id = MainGlobal.getNewID(visit.getDiancxxb_id());
				String sql = "insert into renyxxb(id, mingc, quanc, mim, xingb,bum,zhiw,yiddh,guddh,chuanz,lianxdz,youzbm,email, diancxxb_id, zhuangt) values("+id+",'"
				+ mingc + "','"+quanc+"',empty_blob(),'"+xingb+"','"+bum+"','"+zhiw+"','"+yiddh+"','"+guddh+"','"+chuanz+"','"+lianxdz+"','"+youzbm+"','"+Email+"',"+ getTreeid() +","+zhuangt+")";
				con.getInsert(sql);
				DataBassUtil dbu = new DataBassUtil();
				try {
					dbu.UpdateBlob("renyxxb", "mim", Long.parseLong(id), mingc,true);
				}catch(Exception e) {
					e.printStackTrace();
					continue;
				}
			}else {
				if(Validate.UserExists(mingc,id)) {
					setMsg("�û� "+mingc+" �Ѵ���!");
					continue;
				}
				String sql = "update renyxxb set mingc='"+mingc+"',quanc='"+quanc
				+"',xingb='"+xingb+"',bum='"+bum+"',zhiw='"+zhiw+"',zhuangt='"+zhuangt
				+"',yiddh='"+yiddh+"',guddh='"+guddh+"',chuanz='"+chuanz+"',lianxdz='"+lianxdz
				+"',youzbm='"+youzbm+"',email='"+Email+"' where id =" + id;
				con.getUpdate(sql);
			}
		}
		
		rsl = getExtGrid().getDeleteResultSet(getChange());
		while (rsl.next()) {
			id = rsl.getString("id"); 
			String sql = "delete from renyxxb where id =" + id;
			con.getDelete(sql);
		}
	}
//	���÷���
	private void Power(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("��ѡ��һ����Ա���÷���!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(getChange());
		cycle.activate("Renyz");
	}
//	��������
	private void ReSetPwd() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("��ѡ��һ����Ա��������!");
			return;
		}
		String c[]  = getChange().split(",");
		long id = Long.parseLong(c[0]);
		String mingc = c[1];
		if(id == 0) {
			return ;
		}else {
			DataBassUtil dbu = new DataBassUtil();
			try {
				dbu.UpdateBlob("renyxxb", "mim", id, mingc,true);
//				setMsg(dbu.GetStrBlob("renyxxb", "mim", id,true));
			}catch(Exception e) {
				e.printStackTrace();
				return;
			}
		}
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
