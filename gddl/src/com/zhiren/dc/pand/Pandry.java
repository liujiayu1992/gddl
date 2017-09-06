package com.zhiren.dc.pand;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-10 16��30
 * �������޸�һ�������¹����̵����ķ���
 */
public class Pandry extends BasePage implements PageValidateListener {
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
//	 ҳ��仯��¼
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}
	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}
	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	//�̵���������
	private IPropertySelectionModel _pandModel;
	public void setPandModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel10(value); 
	}
	public IPropertySelectionModel getPandModel() {
		Visit v = (Visit) getPage().getVisit();
		String dcsql = "and d.id = " + v.getDiancxxb_id();
		if(!v.isDCUser()){
			dcsql = "and (d.fuid = " + v.getDiancxxb_id() + " or d.id =" + v.getDiancxxb_id() + ")";
		}
		if (v.getProSelectionModel10() == null) {
			String sql = "select p.id,p.bianm from pandb p, diancxxb d " +
					"where p.diancxxb_id=d.id " 
					+ dcsql 
					+ " and p.zhuangt=0"
					+ " order by p.id desc";
		    v.setProSelectionModel10(new IDropDownModel(sql,"��ѡ��"));
		}
	    return v.getProSelectionModel10();
	}
	private IDropDownBean _pandValue;
	public void setPandValue(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean10(value);
	}
	public IDropDownBean getPandValue() {
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}
	public String getPandbm() {
		String pandbm = "";
		if (getPandValue() == null) {
			JDBCcon con = new JDBCcon();
			String sql = "select id,bianm from pandb where diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id() + " and zhuangt=0 order by id desc";
			ResultSetList rsl = con.getResultSetList(sql);
			if (rsl.next()) {
				pandbm = rsl.getString("bianm");
			}
			return pandbm;
		}
		return getPandValue().getValue();
	}
	public long getPandbID() {
		if (getPandValue() == null) {
			return -1;
		}
		return getPandValue().getId();
	}
	public void setID(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	public String getID() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _AddChick = false;
	public void AddButton(IRequestCycle cycle) {
		_AddChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		} else if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		} else if(_AddChick) {
			_AddChick = false;
			insertData();
		}
	}
	public void insertData() {
		JDBCcon con = new JDBCcon();
		String sql = "";
		int flag = 0;
		Visit visit = ((Visit) getPage().getVisit());
		con.setAutoCommit(false);
		try {
			con.getDelete("delete pandbmryzzb where pandb_id=" + getPandbID());
			String[] id = getID().split(",");
			for (int i = 0; i < id.length; i++) {
				sql = "insert into pandbmryzzb values (" 
					+ "getNewId(" + visit.getDiancxxb_id() + "),"
					+ getPandbID() + ",'"
					+ MainGlobal.getTableCol("pandbmryzzb", "bum", "id", id[i]) + "','"
					+ MainGlobal.getTableCol("pandbmryzzb", "reny", "id", id[i]) + "','"
					+ MainGlobal.getTableCol("pandbmryzzb", "zhiz", "id", id[i]) + "'"
					+ ")";
				con.getInsert(sql);
				if (flag == -1) {
					con.rollBack();
				}
			} 
			con.commit();
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			con.rollBack();
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}
	public void save() {
		String sSql = "";
		long id = 0;
		int flag = 0;
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û����Ҫ����ļ�¼��");
			return;
		}
		ResultSetList rsl = getExtGrid().getDeleteResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "pandbmryzzb.Save �� getExtGrid().getDeleteResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			//����ɾ������ʱ�����־
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Renyzz,
					"pandbmryzzb",id+"");
			sSql = "delete from pandbmryzzb where id=" + id;
			flag = con.getDelete(sSql);
			if (flag == -1) {
				con.rollBack();
				con.Close();				
			}
		}
		rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "pandbmryzzb.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			if (id == 0) {
				sSql = "insert into pandbmryzzb values(getNewId(" + visit.getDiancxxb_id() + "),"
					+ getPandbID() + ",'"
					+ rsl.getString("bum") + "','"
					+ rsl.getString("reny") + "','"
					+ rsl.getString("zhiz") + "'"
					+ " )";
				flag = con.getInsert(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
			} else {
				//�����޸Ĳ���ʱ�����־
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Renyzz,
						"pandbmryzzb",id+"");
				sSql = "update pandbmryzzb set "
					+ " pandb_id=" + getPandbID() + ","					
					+ " bum='" + rsl.getString("bum") + "',"
					+ " reny='" + rsl.getString("reny") + "',"
					+ " zhiz='" + rsl.getString("zhiz") + "'"
					+ " where id=" + id;
				flag = con.getUpdate(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
			}
		}
	}
	public void loadData() {
		Visit visit = (Visit) getPage().getVisit();
		StringBuffer sID = new StringBuffer();
		JDBCcon con = new JDBCcon();
		try {
			String sql = "select id from pandb where riq<(select riq from pandb where bianm='" + getPandbm() + "'" 
					+ " and diancxxb_id=" + visit.getDiancxxb_id() + ")"
					+ " and diancxxb_id=" + visit.getDiancxxb_id() + " order by id desc";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				String id = rs.getString("id");
				sql = "select id from pandbmryzzb where pandb_id =" + id;
				ResultSet rs2 = con.getResultSet(sql);
				boolean flag = false;
				while (rs2.next()) {
					if (!flag) flag = true;
					sID.append(rs2.getString("id")).append(",");
				}
				if (flag) {
					sID.deleteCharAt(sID.length()-1);
					setID(sID.toString());
					flag = false;
				}
			} 
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}
	public void getSelectData() {
//		Visit visit = (Visit) getPage().getVisit();
		String sSql = "";
		JDBCcon con = new JDBCcon();
		sSql = "select p.id,bum,reny,zhiz from pandbmryzzb p,pandb where p.pandb_id=pandb.id and pandb.bianm='" + getPandbm() + "'";
		ResultSetList rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("bum").setHeader("����");
		egu.getColumn("reny").setHeader("��Ա");
		egu.getColumn("zhiz").setHeader("ְ��");
		egu.addTbarText("�̵���룺");
		ComboBox cobPand = new ComboBox();
		cobPand.setWidth(100);
		cobPand.setTransform("PandDropDown");
		cobPand.setId("PandDropDown");
		cobPand.setLazyRender(true);
		egu.addToolbarItem(cobPand.getScript());
		egu.addTbarText("-");
		GridButton gbt = new GridButton("ˢ��","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		if (getID() != null) {
			gbt = new GridButton("�����ϴ�","function(){document.getElementById('AddButton').click();}");
			gbt.setIcon(SysConstant.Btn_Icon_Create);
			egu.addTbarBtn(gbt);
		}
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);	
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String script = "\nvar tmpIndex = PandDropDown.getValue();\n";
		script = script + "PandDropDown.on('select', function(o,record,index) {if (tmpIndex != PandDropDown.getValue()) {document.forms[0].submit();}});\n";
		egu.addOtherScript(script);
		setExtGrid(egu);
		con.Close();
	}
//	public String getAddHandler(JDBCcon con, ExtGridUtil egu) {
////		Visit visit = (Visit) getPage().getVisit();
//		StringBuffer handler = new StringBuffer();
//		handler.append("\nfunction() {\n");	
//		String sql = "select bum,reny,zhiz from pandbmryzzb where id in(" + getID() + ")";
//		System.out.println(sql);
//		try {
//			ResultSet rs = con.getResultSet(sql);
//			while (rs.next()) {
//				handler.append("var plant = new ").append(egu.gridId).append("_plant({");
//				List columns = egu.getGridColumns();
//				for (int i =1; i < columns.size(); i++) {
//					if(((GridColumn)columns.get(i)).coltype == GridColumn.ColType_default) {
//						GridColumn gc = ((GridColumn)columns.get(i));
//						if (gc.dataIndex.equalsIgnoreCase("bum")) {
//							handler.append(gc.dataIndex).append(": '").append(rs.getString("bum")).append("',");
//						} else if (gc.dataIndex.equalsIgnoreCase("reny")) {
//							handler.append(gc.dataIndex).append(": '").append(rs.getString("reny")).append("',");
//						} else if (gc.dataIndex.equalsIgnoreCase("zhiz")) {
//							handler.append(gc.dataIndex).append(": '").append(rs.getString("zhiz")).append("',");
//						} else {
//							handler.append(gc.dataIndex).append(": '").append(gc.defaultvalue).append("',");
//						}
//					}
//				}
//				handler.deleteCharAt(handler.length()-1);
//				handler.append("});\n");
//				handler.append("gridDiv_ds.insert(gridDiv_ds.getCount(),plant);");
//			}
//		} catch (SQLException e) {
//			// TODO �Զ����� catch ��
//			e.printStackTrace();
//		}
//		handler.append("}");
//		return handler.toString();
//	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setPandModel(null);
			setPandValue(null);
		}
		init();
	}
	private void init() {
		setID(null);
		loadData();
		getSelectData();
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
}
