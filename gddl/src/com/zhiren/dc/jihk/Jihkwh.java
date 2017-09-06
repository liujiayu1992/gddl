package com.zhiren.dc.jihk;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Jihkwh extends BasePage implements PageValidateListener {
	
//	�����û���ʾ
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
	
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	//����
	private String riq;
	
	public void setRiq(String value) {
		riq = value;
	}
	
	public String getRiq() {
		if (riq==null && "".equals(riq)) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	private void Save() {
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		int flag = -1;
		
		if(getChange()==null || "".equals(getChange())) {
			setMsg("û����Ҫ����ļ�¼��");
		}
		con.setAutoCommit(false);
		ResultSetList rsl=getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Jihkwh.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		String sql = "begin \n";
		String jihbID = "";
		String countSql = 
			"select decode(max(xuh),null,0,substr(max(xuh),9)) as curCount from jihzb where jihb_id in (\n" +
			"select id from jihb where to_char(kaisrq,'yyyy-mm-dd')='" + this.getRiq() + "'\n" + 
			")";
		ResultSet rs = con.getResultSet(countSql);
		int currentCount=0;
		try {
			if (rs.next()) {
				currentCount = Integer.parseInt(rs.getString("curCount"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		while(rsl.next()){
			if ("0".equals(rsl.getString("id"))) {	
				jihbID = MainGlobal.getNewID(v.getDiancxxb_id());
				sql += "insert into jihb\n" +
					"  (id,\n" + 
					"   gongysb_id,\n" + 
					"   meikxxb_id,\n" + 
					"   jihkjb_id,\n" + 
					"   yunsdwb_id,\n" + 
					"   pinzb_id,\n" + 
					"   kaisrq,\n" + 
					"   jiezrq,\n" + 
					"   fafzs,\n" + 
					"   lurrq,\n" + 
					"   lury)\n" + 
					"values\n" + 
					"  (" + jihbID + ",\n" +
					"   " + ((IDropDownModel)getGongysModel()).getBeanId(rsl.getString("gongysb_id")) + ",\n" +
					"   " +((IDropDownModel)getMeikModel()).getBeanId(rsl.getString("meikxxb_id")) + ",\n" +
					"   " + (getExtGrid().getColumn("jihkjb_id").combo).getBeanId(rsl.getString("jihkjb_id")) + ",\n" +
					"   " + (getExtGrid().getColumn("yunsdwb_id").combo).getBeanId(rsl.getString("yunsdwb_id")) + ",\n" +
					"   " + (getExtGrid().getColumn("pinzb_id").combo).getBeanId(rsl.getString("pinzb_id")) + ",\n" +
					"   to_date('" + rsl.getString("kaisrq") + "','yyyy-mm-dd'),\n" + 
					"   to_date('" + rsl.getString("jiezrq") + "','yyyy-mm-dd'),\n" + 
					"   " + rsl.getString("fafzs") + ",\n" +
					"   sysdate,\n" + 
					"   '" + rsl.getString("lury") + "'\n" +
					");\n";
				
				//�ƻ�������
				int zhangs = rsl.getInt("fafzs");
				//���ǰ׺Ϊ��ʼ����
//				String prefXhu = DateUtil.Formatdate("yyyyMMdd", new Date());
				String prefXhu = rsl.getString("kaisrq").substring(0, 4) 
					+ rsl.getString("kaisrq").substring(5,7)
					+ rsl.getString("kaisrq").substring(8,10);
				if (zhangs>0) {
					for (int i=0;i<zhangs;i++) {
						currentCount++;
						String subXuh = "";
						if ((currentCount)<10) {
							subXuh = "000" + currentCount;					
						}else if (currentCount<100) {
							subXuh = "00" + currentCount;
						}else if (currentCount<1000) {
							subXuh = "0" + currentCount;
						}else if (currentCount<10000) {
							subXuh = "" + currentCount;
						}
						sql +=
							"insert into jihzb\n" +
							"  (id,jihb_id,xuh)\n" + 
							"values\n" + 
							"  (getnewid(" + v.getDiancxxb_id() + ")," + jihbID + ",'" + prefXhu + subXuh + "');\n";
					}
				}
			}
		}
		sql += "end; \n";
		if (sql.length()>13) {
			flag = con.getInsert(sql);
			if (flag==-1) {
				con.rollBack();
				setMsg("����ʧ��!");
				return;
			}
		}
		con.commit();
		rsl.close();
		con.Close();
		setMsg("����ɹ�!");
	}
	
	private void Delete() {
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		int flag = -1;
		
		if(getChange()==null || "".equals(getChange())) {
			setMsg("û����Ҫ����ļ�¼��");
		}
		con.setAutoCommit(false);
		ResultSetList rsl=getExtGrid().getDeleteResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Jihkwh.Delete �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		String sql = "begin \n";
		while (rsl.next()) {
			if (!"0".equals(rsl.getString("id"))) {
				if (rsl.getInt("shiyzs")>0) {
					setMsg("��" + rsl.getString("meikxxb_id")+ "��" + "�ƻ�����ʹ�ã��޷�ɾ��!");
					return;
				} else {
					sql += "delete from jihb where id=" + rsl.getString("id") + ";\n";
					sql += "delete from jihzb where jihb_id=" + rsl.getString("id") + ";\n";
				}
			}
		}
		sql += "end; \n";
		if (sql.length()>13) {
		flag = con.getDelete(sql); 
			if (flag==-1) {
				con.rollBack();
				setMsg("ɾ������ʧ��!");
				return;
			}
		}
		con.commit();
		rsl.close();
		con.Close();
	}
	
	private void Show(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("��ѡ��һ�����ݽ��в鿴��");
			return;
		}
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "ShujshQ.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		if(rsl.getRows()!=1) {
			setMsg("��ѡ��һ�����д�ӡ��");
			return;
		}		
		if(rsl.next()) {
			setHihkID(rsl.getString("id"));
		}
		
		cycle.activate("JihkPrint");
	}
	
	private void setHihkID(String jihkID) {
		((Visit)getPage().getVisit()).setString8(jihkID);
	}
 
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _DeleteChick = false;
	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	
	private boolean _ShowChick = false;
	public void ShowButton(IRequestCycle cycle) {
		_ShowChick = true;
	}
    
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
//			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			Delete();
			getSelectData();
		}
		if (_ShowChick) {
			_ShowChick = false;
			Show(cycle);
		}
	}

	public void getSelectData() {
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "";
		
		sql = 
			"select jh.id,\n" +
			"       gys.mingc as gongysb_id,\n" + 
			"       mk.mingc as meikxxb_id,\n" + 
			"       jk.mingc as jihkjb_id,\n" + 
			"       nvl(yd.mingc,'��ѡ��') as yunsdwb_id,\n" + 
			"       pz.mingc as pinzb_id,\n" + 
			"       jh.kaisrq,\n" + 
			"       jh.jiezrq,\n" + 
			"       jh.fafzs,\n" + 
			"       (select count(jhz.id) from jihzb jhz where jhz.shifsy=0 and jhz.jihb_id = jh.id) as weisyzs,\n" + 
			"       (select count(jhz.id) from jihzb jhz where jhz.shifsy<>0 and jhz.jihb_id = jh.id) as shiyzs,\n" + 
			"       --jh.lurrq,\n" + 
			"       jh.lury\n" + 
			"  from jihb jh, gongysb gys, meikxxb mk, jihkjb jk, yunsdwb yd, pinzb pz\n" + 
			" where jh.gongysb_id = gys.id\n" + 
			"   and jh.meikxxb_id = mk.id\n" + 
			"   and jh.jihkjb_id = jk.id\n" + 
			"   and jh.yunsdwb_id = yd.id(+)\n" + 		
			"   and jh.pinzb_id = pz.id\n" + 
			"   and to_char(jh.kaisrq,'yyyy-mm-dd') = '" + this.getRiq() + "'";

		ResultSetList rsl = con.getResultSetList(sql);

		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("jihb");
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		//����GRID�Ƿ���Ա༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		//����ÿҳ��ʾ����
		egu.addPaging(25);
		
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("gongysb_id").setHeader("��Ӧ��");
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("gongysb_id").setWidth(100);
		egu.getColumn("meikxxb_id").setHeader("ú��λ");
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setWidth(100);
		egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
		egu.getColumn("jihkjb_id").setEditor(null);
		egu.getColumn("jihkjb_id").setWidth(100);
		egu.getColumn("yunsdwb_id").setHeader("���䵥λ");
		egu.getColumn("yunsdwb_id").setWidth(100);
		egu.getColumn("yunsdwb_id").setDefaultValue("��ѡ��");
		egu.getColumn("pinzb_id").setHeader("Ʒ��");
		egu.getColumn("pinzb_id").setWidth(60);
		egu.getColumn("kaisrq").setHeader("��������");
		egu.getColumn("kaisrq").setEditor(null);
		egu.getColumn("kaisrq").setDefaultValue(this.getRiq());
		egu.getColumn("kaisrq").setWidth(80);
		egu.getColumn("jiezrq").setHeader("��ֹ����");
		egu.getColumn("jiezrq").setDefaultValue(this.getRiq());
		egu.getColumn("jiezrq").setWidth(80);
		egu.getColumn("fafzs").setHeader("��������");
		egu.getColumn("fafzs").setWidth(65);
		egu.getColumn("weisyzs").setHeader("δʹ������");
		egu.getColumn("weisyzs").setEditor(null);
		egu.getColumn("weisyzs").setDefaultValue("0");
		egu.getColumn("weisyzs").setWidth(65);
		egu.getColumn("shiyzs").setHeader("ʹ������");
		egu.getColumn("shiyzs").setEditor(null);
		egu.getColumn("shiyzs").setDefaultValue("0");
		egu.getColumn("shiyzs").setWidth(65);
//		egu.getColumn("lurrq").setHeader("¼������");
//		egu.getColumn("lurrq").setEditor(null);
//		egu.getColumn("lurrq").setDefaultValue(this.getRiq());
//		egu.getColumn("lurrq").setWidth(80);
		egu.getColumn("lury").setHeader("¼��Ա");
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("lury").setDefaultValue(v.getRenymc());
		egu.getColumn("lury").setWidth(80);
		
//		���䵥λ
		ComboBox cysdw = new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(cysdw);
		cysdw.setEditable(true);
		String yunsdwSql = "select id,mingc from yunsdwb where diancxxb_id="+ v.getDiancxxb_id();
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,new IDropDownModel(yunsdwSql, null));
		//����Ʒ��������
		ComboBox c5=new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c5);
		c5.setEditable(true);
		String pinzSql=SysConstant.SQL_Pinz_mei;
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,new IDropDownModel(pinzSql));
		//���ÿھ�������
		ComboBox c6=new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(c6);
		c6.setEditable(true);
		String jihkjSql=SysConstant.SQL_Kouj;
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,new IDropDownModel(jihkjSql));
		
		egu.addTbarText("��������:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq());
		df.Binding("Riq", "");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");
		egu.addToolbarButton("ˢ��",GridButton.ButtonType_Refresh, "RefreshButton");
		egu.addToolbarButton("���",GridButton.ButtonType_Insert, null);
		egu.addToolbarButton("ɾ��",GridButton.ButtonType_Delete_confirm, "DeleteButton");
		egu.addToolbarButton("����",GridButton.ButtonType_Save, "SaveButton");
		egu.addToolbarButton("��ӡ",GridButton.ButtonType_Sel, "ShowButton", null, SysConstant.Btn_Icon_Print);
		
		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
				+"row = irow; \n"
				+"if (eval(gridDiv_ds.getAt(irow).get('ID')||0)>0) {return;}"
				+"if('GONGYSB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
				+"gongysTree_window.show();}});\n"
				+"gridDiv_grid.on('beforeedit',function(e){if(eval(e.record.get('ID')||0)>0){e.cancel=true;}});\n"
			);
		egu.setDefaultsortable(false);
		setExtGrid(egu);
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk_kj,"gongysTree" ,""+v.getDiancxxb_id(),null,null,null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		StringBuffer handler = new StringBuffer();
		handler.append("function() { \n")
		.append("var cks = gongysTree_treePanel.getSelectionModel().getSelectedNode(); \n")
		.append("if(cks==null){gongysTree_window.hide();return;} \n")
		.append("if(cks.getDepth() < 3){ \n")
		.append("Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ���Ӧ�ļƻ��ھ���');")
		.append("return; } \n")
		.append("rec = gridDiv_grid.getSelectionModel().getSelected(); \n")
		.append("rec.set('GONGYSB_ID', cks.parentNode.parentNode.text); \n")
		.append("rec.set('MEIKXXB_ID', cks.parentNode.text); \n")
		.append("rec.set('JIHKJB_ID', cks.text); \n")
		.append("gongysTree_window.hide(); \n")
		.append("return;")
		.append("}");
		ToolbarButton btn = new ToolbarButton(null, "ȷ��", handler.toString());
		bbar.addItem(btn);
		v.setDefaultTree(dt);
		egu.setDefaultsortable(false);
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
	
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
//	��Ӧ��
	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
	}
	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setMeikModels() {
		String sql = "select id,mingc from meikxxb order by xuh,mingc";
		setMeikModel(new IDropDownModel(sql));
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
			visit.setString8(null);
			setGongysModel(null);
			setGongysModels();
			((Visit) getPage().getVisit()).setDropDownBean10(null);
			((Visit) getPage().getVisit()).setProSelectionModel10(null);
			setMeikModel(null);
			setMeikModels();
			setRiq(DateUtil.FormatDate(new Date()));
			
			String dianclb= cycle.getRequestContext().getParameter("lx");
			if(dianclb!=null){
				visit.setString15(dianclb);
			}else{
				visit.setString15("PRINT_BAOER");
			}
		}
		getSelectData();
	}
}
