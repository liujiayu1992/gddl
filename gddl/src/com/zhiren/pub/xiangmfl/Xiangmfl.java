package com.zhiren.pub.xiangmfl;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Xiangmfl extends BasePage implements PageValidateListener{
	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	
	private String Parameters;//��¼��ĿID
	public String getParameters() {
		return Parameters;
	}
	public void setParameters(String value) {
		Parameters = value;
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _DeleteChick = false;
	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	
	private boolean _ZafxmwhChick = false;
	public void ZafxmwhButton(IRequestCycle cycle) {
		_ZafxmwhChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			delete();
			getSelectData();
		}
		if (_ZafxmwhChick){
			_ZafxmwhChick = false;
			activate(cycle);
		}
	}
	
	private void activate(IRequestCycle cycle) {
		cycle.getRequestContext().getSession().setAttribute("bianm",getParameters().trim());//�ӷ�
		cycle.activate("Zafxmwh");//��תҳ����Դ��
	}
	
	private void save() {
		JDBCcon con=new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		StringBuffer sql = new StringBuffer();
		sql.append("begin\n");
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult
					+ "Xiangmfl.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		String xuh=new String();
		xuh="";
		boolean a=false;
		while (rsl.next()) {
			a=true;
			if("0".equals(rsl.getString("id"))){//����
				
				String zxm=new String();
				ResultSetList rs=new ResultSetList();
				zxm="select xuh from itemsort where (bianm='" + rsl.getString("bianm") + "' or mingc ='" + rsl.getString("mingc") + "') and id<>" + rsl.getString("id");
				rs = con.getResultSetList(zxm);
				if(rs.next()){
					xuh += "(" + rs.getString("xuh") + ")";
				}else{
					String id=MainGlobal.getNewID(visit.getDiancxxb_id());
					sql.append(
							"insert into itemsort(id,itemsortid,xuh,bianm,mingc,zhuangt,beiz)\n" + 
							"values(\n" + 
							id + ",\n" + 
							id + ",\n" + 
							"'" + rsl.getString("xuh") + "',\n" + 
							"'" + rsl.getString("bianm") + "',\n" + 
							"'" + rsl.getString("mingc") + "',\n" + 
							(getExtGrid().getColumn("zhuangt").combo).getBeanId(rsl.getString("zhuangt")) + ",\n" + 
							"'"+rsl.getString("beiz") + "');\n");
				}
			}else{	//�޸�
				String zxm=new String();
				ResultSetList rs=new ResultSetList();
				zxm="select xuh from itemsort where (bianm='" + rsl.getString("bianm") + "' or mingc ='" + rsl.getString("mingc") +"') and id<>" + rsl.getString("id");
				rs = con.getResultSetList(zxm);
				if(rs.next()){
					xuh += "(" + rs.getString("xuh") + ")";
				}else{
					sql.append(
							"update itemsort set\n" + 
							"xuh = '" + rsl.getString("xuh") + "',\n" + 
							"bianm = '" + rsl.getString("bianm") + "',\n" + 
							"mingc = '" + rsl.getString("mingc") + "',\n" + 
							"zhuangt = " + (getExtGrid().getColumn("zhuangt").combo).getBeanId(rsl.getString("zhuangt")) + ",\n" + 
							"beiz = '" + rsl.getString("beiz") + "'\n" + 
							"where id=" + rsl.getString("id") + ";\n");
				}
			}
		}
		sql.append("end;");
		if(a){
			con.getInsert(sql.toString());
			if(xuh.equals("")){
				setMsg("����ɹ���");
			}else{
				setMsg("&nbsp;&nbsp;&nbsp;�����" + xuh + "�ı�����������ظ���������ά����&nbsp;&nbsp;&nbsp;");
			}
		}
	}
	
	private void delete(){
		JDBCcon con=new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append("begin\n");
		String zxm=new String();
		ResultSetList rs=new ResultSetList();
		zxm="select * from item where itemsortid=" + getParameters();
		rs = con.getResultSetList(zxm);
		if(rs.next()){
			this.setMsg("������Ŀ����ɾ����");
		}else{
			sql.append("delete from itemsort where id=" + getParameters() + ";");
			sql.append("end;");
			con.getDelete(sql.toString());
			this.setMsg("ɾ�������ɹ���");
		}
	}

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList(
						"select id,xuh,bianm,mingc,decode(zhuangt,1,'ʹ��','δʹ��') zhuangt,beiz\n" +
						"from itemsort\n" + 
						"order by bianm");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("xuh").setWidth(40);
		egu.getColumn("bianm").setHeader("����");
		egu.getColumn("mingc").setHeader("����");
		egu.getColumn("mingc").setWidth(120);
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "δʹ��"));
		l.add(new IDropDownBean(1, "ʹ��"));
		egu.getColumn("zhuangt").setEditor(new ComboBox());
		egu.getColumn("zhuangt").setComboEditor(egu.gridId,
				new IDropDownModel(l));
		egu.getColumn("zhuangt").setReturnId(true);	
		egu.getColumn("zhuangt").setDefaultValue(String.valueOf(((IDropDownBean)l.get(0)).getValue()));
		egu.getColumn("zhuangt").setHeader("״̬");
		egu.getColumn("beiz").setHeader("��ע");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(-1);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarBtn(new GridButton("ɾ��","function(){" 
				+"if(gridDiv_sm.getSelected()== null){"
	        	+"	Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����¼');"
	        	+"	return;"
	        	+"}"
	        	+"var grid_rcd = gridDiv_sm.getSelected();\n"+ 
	        	"var flagd=1;"+
	        	"grid_history = grid_rcd.get('ID');" +
				"document.getElementById('PARAMETERS').value = grid_history;" +
				"document.getElementById('DeleteButton').click();" +
				"}",SysConstant.Btn_Icon_Delete));
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String sPowerHandler = "function(){\n"
			+"if(gridDiv_sm.getSelected()== null){"
        	+"	Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����¼');"
        	+"	return;"
        	+"}"
        	+"var grid_rcd = gridDiv_sm.getSelected();\n"
        	+"if(flagd =0 && grid_rcd.get('ID') == '0'){ \n"
        	+"	Ext.MessageBox.alert('��ʾ��Ϣ','�������ӷ���Ŀ֮ǰ���ȱ���!');\n"
        	+"	return;\n"
        	+"} \n"
        	+"flagd=0;"
        	+"grid_history = grid_rcd.get('BIANM'); \n"
			+"document.getElementById('PARAMETERS').value = grid_history; \n"
			+"document.getElementById('ZafxmwhButton').click();"
			+"}\n";
		egu.addTbarBtn(new GridButton("ά����ǰ����Ŀ",sPowerHandler,SysConstant.Btn_Icon_SelSubmit));
		
//		StringBuffer script = new StringBuffer();
//		script.append( "gridDiv_grid.on('afteredit', function(e) { \n") 
//				.append("if(e.field=='BIANM'){\n")
//				.append("	var ided=gridDiv_sm.getSelected().get('ID');\n")
//				.append("	var bianmed=gridDiv_sm.getSelected().get('BIANM');\n")
//				.append("   for(var i=0;i<gridDiv_grid.getStore().getCount();i++){ \n")
//				.append("		if(gridDiv_grid.getStore().getAt(i).get('BIANM')==bianmed){ \n")
//				.append("			if(gridDiv_grid.getStore().getAt(i).get('ID')!=ided){ \n")
//				.append("				gridDiv_sm.getSelected().set('BIANM',''); \n")
//				.append("				Ext.Msg.alert('��ʾ','&nbsp;&nbsp;&nbsp;�����Ϊ' + gridDiv_grid.getStore().getAt(i).get('XUH') + '�ļ�¼��ͬ��������ά��&nbsp;&nbsp;&nbsp;');\n")
//				.append("			}")
//				.append("		}")
//				.append("	}")
//				.append("}")
//				
//				.append("if(e.field=='MINGC'){\n")
//				.append("	var ided=gridDiv_sm.getSelected().get('ID');\n")
//				.append("	var mingced=gridDiv_sm.getSelected().get('MINGC');\n")
//				.append("   for(var i=0;i<gridDiv_grid.getStore().getCount();i++){ \n")
//				.append("		if(gridDiv_grid.getStore().getAt(i).get('MINGC')==mingced){ \n")
//				.append("			if(gridDiv_grid.getStore().getAt(i).get('ID')!=ided){ \n")
//				.append("				gridDiv_sm.getSelected().set('MINGC',''); \n")
//				.append("				Ext.Msg.alert('��ʾ','&nbsp;&nbsp;&nbsp;�����Ϊ' + gridDiv_grid.getStore().getAt(i).get('XUH') + '�ļ�¼��ͬ��������ά��&nbsp;&nbsp;&nbsp;');\n")
//				.append("			}")
//				.append("		}")
//				.append("	}")
//				.append("}")
//				.append("});");
//		egu.addOtherScript(script.toString());
		
		
		setExtGrid(egu);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}

	public void pageValidate(PageEvent arg0) {
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setMsg("");
			getSelectData();			
		}
		
	}
}
