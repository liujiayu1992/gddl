package com.zhiren.pub.jitrbdwb;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Jitrbdwb extends BasePage implements PageValidateListener{
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		super.initialize();
		setMsg(null);

	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}
	//js�ж�ҳ�����ƣ�ȫ�ƣ������ֶε�ֵ�����ظ���
	String condition = 
		"\n" +
		"var allr = gridDiv_ds.getRange();\n" + 
		"var allm = gridDiv_ds.getModifiedRecords();\n" + 
		"\tfor(var i=0;i<allr.length;i++){\n" + 
		"\t\tif(allr[i].get('ID')>0){\n" + 
		"\n" + 
		"\t\tfor(var j=0;j<allm.length;j++){\n" + 
		"\n" + 
		"\n" + 
		"\t\t\tif(allr[i].get('MINGC')==allm[j].get('MINGC')){\n" + 
		"\t\t\t\tExt.MessageBox.alert('��ʾ��Ϣ','�����ֶβ����ظ�');\n" + 
		"\t\t\t\treturn;\n" + 
		"\t\t\t}\n" + 
		"\t\t\tif(allr[i].get('QUANC')==allm[j].get('QUANC')){\n" + 
		"\t\t\t\tExt.MessageBox.alert('��ʾ��Ϣ','ȫ���ֶβ����ظ�');\n" + 
		"\t\t\t\treturn;\n" + 
		"\t\t\t}\n" + 
		"\t\t\tif(allr[i].get('BIANM')==allm[j].get('BIANM')){\n" + 
		"\t\t\t\tExt.MessageBox.alert('��ʾ��Ϣ','�����ֶβ����ظ�');\n" + 
		"\t\t\t\treturn;\n" + 
		"\t\t\t}\n" + 
		"\n" + 
		"\n" + 
		"\t\t}\n" + 
		"\t\t}\n" + 
		"\n" + 
		"\t}";



	public void setChange(String change) {
		Change = change;
	}

//	private void Save() {
//		Visit visit = (Visit) this.getPage().getVisit();
//		visit.getExtGrid1().Save(getChange(), visit);
//	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Visit visit = (Visit) this.getPage().getVisit();
			Save(this.getChange(),visit);
			getSelectData();
		}else if(_RefreshChick){
			_RefreshChick = false;
			getSelectData();
		}
	}
	public int Save(String strchange, Visit visit) {
		
		JDBCcon con = new JDBCcon();
		
		//���ɾ��������
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(strchange);
		StringBuffer sbb = new StringBuffer();
		while (delrsl.next()) {
			String sql2 = "select * from diancgysmykjb where jitrbdwb_id = "+delrsl.getInt("ID");
			ResultSetList rs1 = con.getResultSetList(sql2);
			String sql3 = "select * from jitrbjhb where jitrbdwb_id = "+delrsl.getInt("ID");
			ResultSetList rs2 = con.getResultSetList(sql3);
			if(rs1.next()||rs2.next()){
				setMsg(delrsl.getString("mingc")+"�ѱ�ʹ��(��ֹɾ��)");
				
			}else{
			StringBuffer sql = new StringBuffer();
			sql.append("delete from ").append("jitrbdwb").append(" where id ='")
					.append(delrsl.getString("id")).append("'\n");
			con.getDelete(sql.toString());
			}
		}

		delrsl.close();
		//��������޸ĵ�����
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		//�ж���ӵ������Ƿ��ظ�
		ResultSetList rsl = con.getResultSetList("select * from jitrbdwb");
		ResultSetList rsl_in = null;
		ResultSetList rsl_pd = null;
		//�������ʱ���ظ�ֵ
		StringBuffer st = new StringBuffer();
		//�����޸�ʱ���ظ�ֵ
		StringBuffer st1 = new StringBuffer();
		//��������Ӳ���

		while(mdrsl.next()){
			//�����ȡid
			String id = MainGlobal.getNewID(con, visit.getDiancxxb_id());
			if ("0".equals(mdrsl.getString("ID"))) {
					
				StringBuffer sql1 = new StringBuffer("");
				
				String sql_in = "select * from jitrbdwb where mingc='"+mdrsl.getString("MINGC")+"' or quanc='"+mdrsl.getString("QUANC")+"' or bianm = '"+mdrsl.getString("BIANM")+"'\n";
				rsl_in = con.getResultSetList(sql_in);
				if(rsl_in.next()){
					if(rsl_in.getString("MINGC").equals(mdrsl.getString("MINGC"))){
						st.append("����<"+mdrsl.getString("MINGC")+">");
					}
					if(rsl_in.getString("QUANC").equals(mdrsl.getString("QUANC"))){
						st.append("ȫ��<"+mdrsl.getString("QUANC")+">");
					}
					if(rsl_in.getString("BIANM").equals(mdrsl.getString("BIANM"))){
						st.append("����<"+mdrsl.getString("BIANM")+">");
					}
				}else{
					sql1.append("insert into ").append("jitrbdwb").append("(id")
					.append(",mingc,quanc,bianm,beiz)").append("values").append("('").append(id).append("'")
					.append(",'").append(mdrsl.getString("MINGC")).append("'").append(",'").append(mdrsl.getString("QUANC"))
					.append("'").append(",'").append(mdrsl.getString("BIANM")).append("'").append(",'").append(mdrsl.getString("BEIZ"))
					.append("'").append(") \n");
					con.getUpdate(sql1.toString());
				}
				
			} else {
				
				
				String sql_pd="select * from jitrbdwb where (mingc='"+mdrsl.getString("MINGC")+"' or quanc='"+mdrsl.getString("QUANC")+"' or bianm = '"+mdrsl.getString("BIANM")+"') and id<>'"+mdrsl.getString("id")+"'\n";
				rsl_pd = con.getResultSetList(sql_pd);

				if(rsl_pd.next()){
					if(rsl_pd.getString("MINGC").equals(mdrsl.getString("MINGC"))){
						st1.append("����<"+mdrsl.getString("MINGC")+">");
					}
					if(rsl_pd.getString("QUANC").equals(mdrsl.getString("QUANC"))){
						st1.append("ȫ��<"+mdrsl.getString("QUANC")+">");
					}
					if(rsl_pd.getString("BIANM").equals(mdrsl.getString("BIANM"))){
						st1.append("����<"+mdrsl.getString("BIANM")+">");
					}
				}else{
					StringBuffer sql2 = new StringBuffer("");
					sql2.append("update jitrbdwb set mingc='").append(mdrsl.getString("MINGC")).append("',").append("quanc='")
					.append(mdrsl.getString("QUANC")).append("',").append("bianm='").append(mdrsl.getString("BIANM")).append("',")
					.append("beiz='").append(mdrsl.getString("BEIZ")).append("' ").append("where id = '").append(mdrsl.getString("ID")).append("'");
					con.getUpdate(sql2.toString());
				}
				
			}
		}	
		if(st.length()>0&&st1.length()>0){
			setMsg("���"+st.toString()+"�ظ�"+"����"+"�޸�"+st1.toString()+"�ظ�");
		}else
			if(st.length()>0){
			setMsg("���"+st.toString()+"�ظ�");
		}else
			if(st1.length()>0){
			setMsg("�޸�"+st1.toString()+"�ظ�") ;
		}
		int stl = st.length();
		st.delete(0, stl);
		int stl1 = st1.length();
		st1.delete(0, stl1);
		mdrsl.close();
		con.Close();
		return 1;
	}
	public void getSelectData() {
//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select * from jitrbdwb");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("jitrbdwb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("mingc").setHeader("����");
		egu.getColumn("mingc").setWidth(300);
		egu.getColumn("quanc").setHeader("ȫ��");
		egu.getColumn("quanc").setWidth(300);
		egu.getColumn("bianm").setHeader("����");
		egu.getColumn("bianm").setWidth(300);
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("beiz").setWidth(300);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
//		egu.addToolbarItem("{"+new GridButton("ˢ��","function(){document.getElementById('RefreshButton').click();}").getScript()+"}");
		GridButton gRefresh = new GridButton("ˢ��",
		"function(){document.getElementById('RefreshButton').click();}");
			gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
				egu.addTbarBtn(gRefresh);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save_condition, "SaveButton",condition);
//		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

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
			visit.setList1(null);
			getSelectData();
		}
	}
}
